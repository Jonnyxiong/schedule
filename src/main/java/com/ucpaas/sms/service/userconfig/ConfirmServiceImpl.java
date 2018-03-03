package com.ucpaas.sms.service.userconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;

/**
 * 用户短信通道管理-模板类型管理(废弃)
 * 
 * @author liulu
 */
@Service
@Transactional
public class ConfirmServiceImpl implements ConfirmService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("confirm.query", "confirm.queryCount", params);
	}

	@Override
	public Map<String, Object> view(int logId) {
		return masterDao.getOneInfo("confirm.view", logId);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存模板，添加/修改：" + params);
		
		if(params.get("distoperators").equals("0")){// 不区分运营商
			String policy = params.get("policy");
			String channelid_multiple = params.get("temp_channelid_multiple") == null ? "" : params.get("temp_channelid_multiple");
			//绑定固定通道--全网
			if(policy.equals("1")){
				params.put("channelid", channelid_multiple);
			}
			
		}else{
			String policy_dis = params.get("policy_dis");
			String channelid_dis = params.get("temp_channelid_dis_multiple") == null ? "" : params.get("temp_channelid_dis_multiple");
			//绑定固定通道--全网
			if(policy_dis.equals("1")){
				params.put("channelid_dis", channelid_dis);
			}
			
			String ydpolicy = params.get("ydpolicy");
			String ydchannelid = params.get("temp_ydchannelid_multiple") == null ? "" : params.get("temp_ydchannelid_multiple");
			//绑定固定通道--移动
			if(ydpolicy.equals("1")){
				params.put("ydchannelid", ydchannelid);
			}
			
			String ltpolicy = params.get("ltpolicy");
			String ltchannelid = params.get("temp_ltchannelid_multiple") == null ? "" : params.get("temp_ltchannelid_multiple");
			//绑定固定通道--联通
			if(ltpolicy.equals("1")){
				params.put("ltchannelid", ltchannelid);
			}
			
			String dxpolicy = params.get("dxpolicy");
			String dxchannelid = params.get("temp_dxchannelid_multiple") == null ? "" : params.get("temp_dxchannelid_multiple");
			//绑定固定通道--电信
			if(dxpolicy.equals("1")){
				params.put("dxchannelid", dxchannelid);
			}
			
			String gjpolicy = params.get("gjpolicy");
			String gjchannelid = params.get("temp_gjchannelid_multiple") == null ? "" : params.get("temp_gjchannelid_multiple");
			//绑定固定通道--国际
			if(gjpolicy.equals("1")){
				params.put("gjchannelid", gjchannelid);
			}
			
		}

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> check = masterDao.getOneInfo("confirm.checkSave", params);// 查重
		if (check != null) {
			data.put("result", "fail");
			data.put("msg", "模板类型被使用，请重新输入");
			return data;
		}
		String id = params.get("id");
		if (StringUtils.isBlank(id)) {// 添加
			int i = masterDao.insert("confirm.insertConfirm", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "添加成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
			}
			logService.add(LogType.add,LogEnum.用户配置.getValue(), "用户通道配置-模板管理类型：添加模板", params, data);
		} else {// 修改管理员
			int i = masterDao.update("confirm.updateConfirm", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "修改成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "短信通道不存在，修改失败");
			}
			logService.add(LogType.update, LogEnum.用户配置.getValue(),"用户通道配置-通道配置：修改模板类型", params, data);
		}
		return data;
	}

	@Override
	public Map<String, Object> updateStatus(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int i = masterDao.update("confirm.updateStatus", params);
		String msg = "操作";
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", msg + "成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "模板不存在，" + msg + "失败");
		}
		logService.add(LogType.update, LogEnum.用户配置.getValue(),"用户通道配置-模板管理--修改状态", msg, params, data);
		return data;
	}

	@Override
	public Map<String, Object> delete(Map<String, String> formData) {
		masterDao.delete("confirm.delete", formData);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", "success");
		data.put("msg", "删除成功");
		logService.add(LogType.delete, LogEnum.用户配置.getValue(),"用户通道配置-模板：删除模板", formData);
		return data;
	}

}
