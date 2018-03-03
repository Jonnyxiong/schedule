package com.ucpaas.sms.service.userconfig;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;

/**
 * 用户配置-用户帐号和签名端口对应关系t_sms_clientid_signport
 * @author TonyHe
 *
 */
@Service
@Transactional
public class SignportServiceImpl implements SignportService{
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;
	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("signport.query", "signport.queryCount", params);
	}

	@Override
	public Map<String, Object> view(String id) {
		return masterDao.getOneInfo("signport.view", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		Map<String, String> check = masterDao.getOneInfo("signport.saveCheck", params);
		if(check != null){
			data.put("result", "fail");
			data.put("msg", "该子账号已经使用过该签名");
			return data;
		}
		
		if(StringUtils.isBlank(params.get("id"))){
			int currentnumber = Integer.valueOf(params.get("signport"));
			int endnumber = Integer.valueOf(params.get("signport_endnumber"));
			int length = params.get("signport_endnumber").length();
			if(currentnumber > endnumber){
				data.put("result", "fail");
				data.put("msg", "该子账号签名端口资源已经分配殆尽");
				return data;
			}else{
				
				Map<String, String> signPorMap = new HashMap<String, String>();
				signPorMap = getSignPortByLength(length, currentnumber);
				params.putAll(signPorMap);
				if(currentnumber == 0){
					masterDao.insert("signport.insertSignPortAssign", params);
				}else{
					masterDao.update("signport.updateSignPortAssign", params);
				}
				
				String uuid = UUID.randomUUID().toString();
				params.put("id", uuid);
				params.put("signport", (String)signPorMap.get("currentnumber"));
				int i = masterDao.insert("signport.insert", params);
				if (i > 0) {
					data.put("result", "success");
					data.put("msg", "分配签名端口成功");
				} else {
					data.put("result", "fail");
					data.put("msg", "分配签名端口失败");
				}
			}
			logService.add(LogType.add,LogEnum.用户配置.getValue(), "用户配置-用户帐号和签名端口对应关系，添加：", params, data);
			
		}else{
			int i = masterDao.update("signport.update", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "更新签名端口成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "更新签名端口失败");
			}
			logService.add(LogType.update,LogEnum.用户配置.getValue(), "用户配置-用户帐号和签名端口对应关系，修改：", params, data);
		}
		return data;
	}
	
	@Override
	public Map<String, Object> checkClientIdAndGetSignport(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> statusMap = new HashMap<String, Object>();
		// t_sms_account表中查看子账号状态
		statusMap = masterDao.getOneInfo("signport.getAccountStatus", params);
		
		if(statusMap == null){
			data.put("status", -1); // 子账号不存在
			return data;
		}
		if(String.valueOf(statusMap.get("status")).equals("1")){// 1：注册完成，5：冻结，6：注销，7：锁定
			data.put("status", 1);
			
			Map<String, Object> signPortMap = new HashMap<String, Object>();
			signPortMap = masterDao.getOneInfo("signport.getClientIdSignPort", params);
			if(signPortMap != null){
				// 先查看“签名端口分配范围表”存在记录返回即可
				data.put("endnumber", signPortMap.get("endnumber"));
				data.put("currentnumber", signPortMap.get("currentnumber"));
				data.put("signportlen", signPortMap.get("endnumber").toString().length());
			}else{
				// “签名端口分配范围表”不存在记录时需要查看t_sms_account表中该子账号的signportlen
				Map<String, Object> lengthMap = new HashMap<String, Object>();
				lengthMap = masterDao.getOneInfo("signport.getClientIdSignPortLength", params);
				if(lengthMap != null){
					int length = Integer.valueOf(Objects.toString(lengthMap.get("signportlen"), "0"));
					if(length == 0){
						data.put("signportlen", 0);
					}else{
						data.putAll(getSignPortByLength(length, 0));
					}
				}else{
					data.put("status", -1); // 子账号不存在
				}
			}
		}else{
			data.put("status", 0); // 子账号禁用状态，禁用状态的子账号不能分配签名端口
		}
		return data;
	}
	
	@Override
	public Map<String, Object> updateStatus(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int i = masterDao.update("signport.updateStatus", params);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", "分配子账号签名端口成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "分配子账号签名端口失败");
		}
		logService.add(LogType.add,LogEnum.用户配置.getValue(), "用户配置-用户帐号和签名端口对应关系，添加：", params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> delete(String id) {
		Map<String, Object> data = new HashMap<String, Object>();
		int i = masterDao.delete("signport.delete", id);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", "删除子账号签名端口成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "删除子账号签名端口失败");
		}
		logService.add(LogType.delete,LogEnum.用户配置.getValue(), "用户配置-用户帐号和签名端口对应关系，删除子账号签名：id = ", id , data);
		return data;
	}
	
	private Map<String, String> getSignPortByLength(int length, int currentNum){
		StringBuilder format = new StringBuilder("");
		StringBuilder endnumber = new StringBuilder("");
		for(int i=0; i<length; i++){
			format.append("0");
			endnumber.append("9");
		}
		DecimalFormat df = new DecimalFormat(format.toString());
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("startnumber", format.toString());
		resultMap.put("currentnumber", df.format(currentNum));
		resultMap.put("currentnumber2", df.format(currentNum + 1));
		resultMap.put("endnumber", endnumber.toString());
		
		return resultMap;
	}

}
