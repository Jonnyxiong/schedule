package com.ucpaas.sms.service.sysconf;

import java.util.HashMap;
import java.util.Map;

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
 * 
 * @author TonyHe
 *	超频用户记录
 */

@Service
@Transactional
public class OverrateUserListServiceImpl implements OverrateUserListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OverrateUserListServiceImpl.class);
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;
	@Override
	public PageContainer query(Map<String, String> params) {
		PageContainer page = masterDao.getSearchPage("overrateUserList.query", "overrateUserList.queryCount", params);
		return page ;
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("系统配置-超频用户记录：添加：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		int res = 0;
		Map<String, Object> checkInfo = masterDao.getOneInfo("overrateUserList.repeatCheck", params);
		if(checkInfo != null){
			data.put("result", "fail");
			data.put("msg", "超频用户记录已经存在");
			return data;
		}else{
			res = masterDao.insert("overrateUserList.insert", params);
		}
		
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功添加 " + res + " 个超频用户记录");
		}else{
			data.put("result", "fail");
			data.put("msg", "添加超频用户记录失败");
		}
		logService.add(LogType.add,LogEnum.系统配置.getValue(), "系统配置-超频用户记录：添加超频用户记录", params, data);
		return data;
	}

	@Override
	public Map<String, Object> view(String id) {
		return masterDao.getOneInfo("overrateUserList.view", id);
	}

	@Override
	public Map<String, Object> delete(String id) {
		int res = masterDao.update("overrateUserList.delete", id);
		Map<String, Object> data = new HashMap<String, Object>();
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功删除超频用户记录");
		}else{
			data.put("result", "fail");
			data.put("msg", "删除超频用户记录失败");
		}
		logService.add(LogType.delete, LogEnum.系统配置.getValue(), "系统配置-超频用户记录：删除超频用户记录：id", id, data);
		return data;
	}

	@Override
	public Map<String, Object> update(Map<String, String> params) {
		LOGGER.debug("系统配置-超频用户记录：修改：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> checkInfo = masterDao.getOneInfo("overrateUserList.repeatCheck", params);
		Map<String, Object> selfInfo = masterDao.getOneInfo("overrateUserList.view", params);
		int res = 0 ;
		if(checkInfo == null || 
				(selfInfo.get("clientid").equals(checkInfo.get("clientid")))){
			res = masterDao.update("overrateUserList.update", params);
		}else{
			data.put("result", "fail");
			data.put("msg", "超频用户记录已经存在");
			return data;
		}
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功修改 " + res + " 个超频用户记录");
		}else{
			data.put("result", "fail");
			data.put("msg", "修改超频用户记录失败");
		}
		logService.add(LogType.update, LogEnum.系统配置.getValue(), "系统配置-超频用户记录：修改超频用户记录：",params, data);
		return data;
	}

}
