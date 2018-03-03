package com.ucpaas.sms.service.userconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.web.AuthorityUtils;

/**
 * 用户短信通道管理-用户管理
 * 
 * @author zenglb
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("user.query", "user.queryCount", params);
	}

	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) {
		return masterDao.getSearchList("user.query", params);
	}

	@Override
	public Map<String, Object> view(Map<String, Object> params) {
		Map<String, Object> userMap = masterDao.getOneInfo("user.view", params);
		Map<String, Object> paramConfMap = masterDao.getOneInfo("paramConf.viewByKey","OVERRATE");
		Map<String, Object> keywordOverrateConfig = masterDao.getOneInfo("paramConf.viewByKey","KEYWORD_OVERRATE");
		List<Map<String, Object>> clientOverRateMap = masterDao.getSearchList("user.queryClientOverRateBySmsType", params);
		if(userMap != null && !userMap.isEmpty()) {
			if(paramConfMap != null && !paramConfMap.isEmpty()) {
				userMap.put("overrateConfig", paramConfMap);
			}
			if(keywordOverrateConfig != null && !keywordOverrateConfig.isEmpty()) {
				userMap.put("keywordOverrateConfig", keywordOverrateConfig);
			}
			if(clientOverRateMap != null && clientOverRateMap.size() != 0){
				userMap.put("clientOverRateMap", clientOverRateMap);
			}
		}
		return userMap;
	}
	
	@Override
	public Map<String, Object> viewSmsOverRateConfig() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 查询“短信超频配置”信息
		Map<String, Object> overRateMap = masterDao.getOneInfo("paramConf.viewByKey", "OVERRATE");
		if(overRateMap != null && !overRateMap.isEmpty()) {
			data.put("overrateConfig", overRateMap);
		}
		
		// 查询“关键字超频配置”信息
		Map<String, Object> keywordOverRateMap = masterDao.getOneInfo("paramConf.viewByKey", "KEYWORD_OVERRATE");
		if(keywordOverRateMap != null && !keywordOverRateMap.isEmpty()) {
			data.put("keywordOverrateConfig", keywordOverRateMap);
		}
		
		return data;
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存用户，添加/修改：" + params);
		String smstype = params.get("smstype").replace(",", "");
		params.put("smstype", smstype);
		if(params.get("distoperators").equals("0")){// 不区分运营商
			// 全网
			String channelid_multiple = params.get("channelid") == null ? "" : params.get("channelid");
		    params.put("channelid", channelid_multiple);
		}else{
			// 移动
			String ydchannelid = params.get("ydchannelid") == null ? "" : params.get("ydchannelid");
			params.put("ydchannelid", ydchannelid);
			
			// 联通
			String ltchannelid = params.get("ltchannelid") == null ? "" : params.get("ltchannelid");
			params.put("ltchannelid", ltchannelid);
			
			// 电信
			String dxchannelid = params.get("dxchannelid") == null ? "" : params.get("dxchannelid");
			params.put("dxchannelid", dxchannelid);
			
		}
		// 国际
		String gjchannelid = params.get("gjchannelid") == null ? "" : params.get("gjchannelid");
		params.put("gjchannelid", gjchannelid);
		
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> check = new HashMap<String, Object>();
		
		check = masterDao.getOneInfo("user.checkClientIdSave", params);// 查重
		if (check != null) {
			data.put("result", "fail");
			data.put("msg", "用户已存在");
			return data;
		}
		
		//获取模板超频删除id,进行删除操作
		String overRate_deleteids = Objects.toString(params.get("overRate_deleteids"), "0");
		if(!overRate_deleteids.equals("0")) {
			String[] ids = overRate_deleteids.trim().split(";");
			for(String id : ids) {
				int deleteid = Integer.parseInt(id);
				// 根据主键删除记录
				masterDao.delete("user.deleteOverRateById", deleteid);
			}
		}
		
		String overRate_id = Objects.toString(params.get("overRate_id"), "");
		if(!overRate_id.equals("")) {

			String[] overRate_id_arry = overRate_id.split(",");

			for(String ov_id : overRate_id_arry) {
				Map<String, Object> orMap = new HashMap<String, Object>();
				Map<String, Object> sqlParams = new HashMap<String, Object>();
				String temOverRate = params.get("overRate_" + ov_id);
				String[] para = temOverRate.split(",");
				orMap.put("overRate_num_s", para[0]);
				orMap.put("overRate_time_s", para[1]);
				orMap.put("overRate_num_m", para[2]);
				orMap.put("overRate_time_m", para[3]);
				orMap.put("overRate_num_h", para[4]);
				orMap.put("overRate_time_h", para[5]);
				orMap.put("state", para[8]);
				orMap.put("overRate_mode", para[9]);
				orMap.put("userid", params.get("userid"));
				if(orMap.get("overRate_mode").equals("0")){
					orMap.put("smstype", params.get("smstype"));
				}else{
					orMap.put("smstype", -1);
				}
				sqlParams.put("id", para[6]);
				// 先删除在插入
				masterDao.delete("user.deleteOverRateById", sqlParams);

				int i = masterDao.insert("user.insertTemOverRate", orMap);
				if(i > 0) {
					if(data.get("temOverRateId") != null) {
						data.put("temOverRateId", data.get("temOverRateId") + ";" +ov_id + "," + orMap.get("id"));
					}else {
						data.put("temOverRateId", ov_id + "," + orMap.get("id"));
					}
				}

			}
		}else{
			masterDao.delete("user.deleteOverRateByUserAndSmsType", params);
		}
		
		
		String id = params.get("id");
		
		if (StringUtils.isBlank(id)) {// 添加
			int i = masterDao.insert("user.insertUserGw", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "添加成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
			}
			
			logService.add(LogType.add, LogEnum.用户配置.getValue(),"用户配置-用户配置：添加用户", params, data);
			
		} else {// 修改管理员
			int i = masterDao.update("user.updateUserGw", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "修改成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "用户不存在，修改失败");
			}
			logService.add(LogType.update, LogEnum.用户配置.getValue(),"用户配置-用户配置：修改用户", params, data);
		}
		return data;
	}

	@Override
	public Map<String, Object> updateStatus(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int i = masterDao.update("user.updateStatus", params);
		String msg = "操作";
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", msg + "成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "用户不存在，" + msg + "失败");
		}
		logService.add(LogType.update,LogEnum.用户配置.getValue(), "用户配置-用户：修改用户状态", msg, params, data);
		return data;
	}

	@Override
	public Map<String, Object> delete(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int deleteUserGwNum = masterDao.delete("user.delete", params);
		if(deleteUserGwNum > 0) {
			int userGwCount = masterDao.getOneInfo("user.queryUserGwCountByUser", params);
			int deleteOverRateNum = 0;
			if(userGwCount > 0){
				deleteOverRateNum = masterDao.delete("user.deleteOverRateByUserAndSmsType", params);
			}else{
				deleteOverRateNum = masterDao.delete("user.deleteOverRateByUser", params);
			}
			
			Gson gson = new Gson();
			LOGGER.debug("删除{}的用户及通道组配置成功，删除t_template_overrate（超频配置）记录{}条，操作员 = {}", gson.toJson(params), deleteOverRateNum, AuthorityUtils.getLoginRealName());
			data.put("result", "success");
			data.put("msg", "删除成功");
		}else{
			data.put("result", "success");
			data.put("msg", "记录不存在或者已经被删除");
		}
		logService.add(LogType.delete,LogEnum.用户配置.getValue(), "用户配置-用户：删除用户", params);
		return data;
	}


	/**
	 * 根据sid返回已报备的签名列表
	 */
	@Override
	public Map<String, Object> getSignList(Map<String, String> params) {
		LOGGER.debug("根据sid返回已报备的签名列表：" + params);
		
		List<Map<String, Object>> channelidList = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String sid= params.get("sid");
		if (StringUtils.isBlank(sid)) {
			resultMap.put("sid", "sid为空");
			return resultMap;
		}
		List<Map<String, Object>> list = masterDao.getSearchList("user.getSignList", params);
		if (null != params && !params.isEmpty()) {
			channelidList = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> map : list) {
				List<String> signList = new ArrayList<String>();
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("channelid", map.get("channelid").toString());
//				String[] signs = "sss,dd".split(",");
//				if (signs.length >= 1) {
//					for (int i = 0; i < signs.length; i++) {
//						signList.add(signs[i]);
//					}
//				}
				temp.put("signList", signList);
				channelidList.add(temp);
			}
		}
		resultMap.put("sid", sid);
		resultMap.put("channelidList", channelidList);
		return resultMap;
	}
	
	/*@Override
	public Map<String, Object> getExtendPortByExtendType(Map<String, String> params){
		return masterDao.getOneInfo("user.getExtendPort", params);
	}*/
	
	@Override
	public Map<String, Object> queryIsClientExist(Map<String, String> params){
		return masterDao.getOneInfo("user.queryIsClientExist", params);
	}
	
	@Override
	public Map<String, Object> getSmsTypeBySid(Map<String, String> params){
		return masterDao.getOneInfo("user.getSmsTypeBySid", params);
	}
	
	private String extendportBusinness(int currentnumber, int endnumber, int extendtype){
		Map<String, Object> sqlParams = new HashMap<String, Object>(); 
		if(currentnumber != endnumber){
			// 扩展端口分配表currentnumber + 1; 
			sqlParams.put("extendtype", extendtype);
			int i = masterDao.update("user.updateEpCurrentNum", sqlParams);
			if (i < 0) {
				LOGGER.debug("msg", "扩展端口类型：" + extendtype + " currentNum加1失败");
				return "fail";
			}
			LOGGER.debug("扩展端口类型：" + extendtype + "currentnumber加1， currentnumber：" + currentnumber);
		}else{// currentnumber等于endnumber 
			
			// 修改当前扩展端口类型状态为禁用
			sqlParams.put("status", 1);
			sqlParams.put("extendtype", extendtype);
			int i = masterDao.update("user.updateEpStatus", sqlParams);
			if (i < 0) {
				LOGGER.debug("msg", "修改扩展端口类型：" + extendtype + " 状态失败");
				return "fail";
			}
			LOGGER.debug("扩展端口类型：" + extendtype + "已经分配完，状态修改为禁用");
			
			// 并启用预留扩展端口, 6对应13、7对应14、8对应15
			if(extendtype == 6 || extendtype == 7 || extendtype == 8){
				sqlParams.put("status", 0);
				if(extendtype == 6){
					sqlParams.put("extendtype", 13);
				}
				if(extendtype == 7){
					sqlParams.put("extendtype", 14);
				}
				if(extendtype == 8){
					sqlParams.put("extendtype", 15);
				}
				i = masterDao.update("user.updateEpStatus", sqlParams);
				if (i < 0) {
					LOGGER.debug("修改扩展端口类型：" + extendtype + " 状态失败");
					return "fail";
				}
				LOGGER.debug("扩展端口类型：" + extendtype + "状态修改为启用");
			}
		}
		logService.add(LogType.add,LogEnum.用户配置.getValue(), "用户配置-用户配置：分配扩展端口号=", currentnumber);
		return "success";
	}

	@Override
	public Map<String, Object> getSmsFromAndTypeByClientId(Map<String, String> params) {
		return masterDao.getOneInfo("user.getSmsFromAndTypeByClientId", params);
	}

	@Override
	public Map<String, Object> queryAllChannelGroup() {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> quanwang = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> yidong = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> liantong = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dianxin = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> guoji = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("operater", 0);
		quanwang = masterDao.getSearchList("user.queryChannelGroupByOperater", sqlParams);
		sqlParams.put("operater", 1);
		yidong = masterDao.getSearchList("user.queryChannelGroupByOperater", sqlParams);
		sqlParams.put("operater", 2);
		liantong = masterDao.getSearchList("user.queryChannelGroupByOperater", sqlParams);
		sqlParams.put("operater", 3);
		dianxin = masterDao.getSearchList("user.queryChannelGroupByOperater", sqlParams);
		sqlParams.put("operater", 4);
		guoji = masterDao.getSearchList("user.queryChannelGroupByOperater", sqlParams);
		
		
		data.put("quanwang", quanwang);
		data.put("yidong", yidong);
		data.put("liantong", liantong);
		data.put("dianxin", dianxin);
		data.put("guoji", guoji);
		
		return data;
	}
	
	
}
