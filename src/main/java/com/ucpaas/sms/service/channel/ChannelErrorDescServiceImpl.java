package com.ucpaas.sms.service.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
 * 通道管理-通道错误状态对应关系
 * 
 */
@Service
@Transactional
public class ChannelErrorDescServiceImpl implements ChannelErrorDescService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelErrorDescServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("channelErrorDesc.query", "channelErrorDesc.queryCount", params);
	}

	@Override
	public Map<String, Object> view(String id) {
		return masterDao.getOneInfo("channelErrorDesc.view", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("通道管理-通道错误状态关系，添加：" + params);

		Map<String, Object> data = new HashMap<String, Object>();
		int res = 0;
		res = masterDao.getSearchSize("channelErrorDesc.saveCheck", params);
		if(res > 0){
			data.put("result", "fail");
			data.put("msg", "已经存在相同的通道错误状态关系，通道号+错误类型+错误码需要唯一");
			return data;
		}
		
		String id = UUID.randomUUID().toString();
		params.put("id", id);
		res = masterDao.insert("channelErrorDesc.insert", params);
		if (res > 0) {
			data.put("id", id);
			data.put("result", "success");
			data.put("msg", "添加成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "添加失败");
		}
		logService.add(LogType.add, LogEnum.通道管理.getValue(),"通道管理-通道错误状态关系：添加通道错误状态关系", params, data);
		
		return data;
	}
	
	@Override
	public Map<String, Object> update(Map<String, String> params) {
		LOGGER.debug("用户通道配置-通道错误状态关系，更新：" + params);

		Map<String, Object> data = new HashMap<String, Object>();
		int res = 0;
		res = masterDao.getSearchSize("channelErrorDesc.saveCheck", params);
		if(res > 0){
			data.put("result", "fail");
			data.put("msg", "已经存在相同的通道错误状态关系，通道号+错误类型+错误码需要唯一");
			return data;
		}
		
		res = masterDao.update("channelErrorDesc.update", params);
		if (res > 0) {
			data.put("id", params.get("id"));
			data.put("result", "success");
			data.put("msg", "更新成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "更新失败");
		}
		logService.add(LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道错误状态关系：更新", params, data);
		return data;
		
	}
	
	@Override
	public Map<String, Object> delete(String id) {
		LOGGER.debug("用户通道配置-通道错误状态关系，删除：" + id);
		Map<String, Object> data = new HashMap<String, Object>();
		int i = masterDao.delete("channelErrorDesc.delete", id);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", "删除成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "删除失败");
		}
		logService.add(LogType.delete, LogEnum.通道管理.getValue(), "通道管理-通道错误状态关系：删除", id);
		return data;
	}

}
