package com.ucpaas.sms.service.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * 通道组管理-自签通道用户端口管理
 */
@Service
@Transactional
public class ChannelExtendportServiceImpl implements ChannelExtendportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelExtendportServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("channelExtendport.query", "channelExtendport.queryCount", params);
	}

	@Override
	public Map<String, Object> view(int logId) {
		return masterDao.getOneInfo("channelExtendport.view", logId);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("添加/修改自签通道用户端口：" + params);

		Map<String, Object> result = new HashMap<String, Object>();
		
		String id = params.get("id");
		if (StringUtils.isBlank(id)) {// 添加
			// 查重: 在同一channelid下，clientid唯一 
			Map<String, Object> check = masterDao.getOneInfo("channelExtendport.saveCheck", params);
			if (check != null) {
				result.put("result", "fail");
				result.put("msg", "该通道下已经分配该用户端口，不能重复分配");
				return result;
			}
			
			int i = masterDao.insert("channelExtendport.insert", params);
			if (i > 0) {
				result.put("result", "success");
				result.put("msg", "添加成功");
			} else {
				result.put("result", "fail");
				result.put("msg", "添加失败");
			}
			logService.add(LogType.add,LogEnum.用户配置.getValue(), "通道组管理-自签通道用户端口：添加", params, result);
		} else {// 修改
			int i = masterDao.update("channelExtendport.update", params);
			if (i > 0) {
				result.put("result", "success");
				result.put("msg", "修改成功");
			} else {
				result.put("result", "fail");
				result.put("msg", "自签通道用户端口不存在，修改失败");
			}
			logService.add(LogType.update,LogEnum.用户配置.getValue(), "通道组管理-自签通道用户端口：修改", params, result);
		}
		return result;
	}

	@Override
	public Map<String, Object> delete(Map<String, String> params) {
		LOGGER.debug("删除自签通道用户端口：" + params);
		Map<String, Object> result = new HashMap<String, Object>();
		int i = masterDao.delete("channelExtendport.delete", params);
		if (i > 0) {
			result.put("result", "success");
			result.put("msg", "删除成功");
		} else {
			result.put("result", "fail");
			result.put("msg", "自签通道用户端口不存在，修改失败");
		}
		logService.add(LogType.delete,LogEnum.用户配置.getValue(), "通道组管理-自签通道用户端口：删除", params, result);
		return result;
	}

	@Override
	public Map<String, Object> channelExtendportCheck1(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> checkResult = masterDao.getOneInfo("channelExtendport.channelExtendportCheck1", params);
		if(checkResult != null){
			result.put("success", false);
			return result;
		}
		result.put("success", true);
		return result;
	}
	
	@Override
	public Map<String, Object> channelExtendportCheck2(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> extendportList = masterDao.selectList("channelExtendport.getAllExtendportByChannelId", params);
		
		boolean extendportUsable = true;
		String extendport = params.get("extendport");
		for (String string : extendportList) {
			Pattern pattern = Pattern.compile("^(?!" + extendport + ").*");
			Matcher matcher = pattern.matcher(string);
			if(!matcher.matches()){
				LOGGER.debug("自签通道用户端口校验失败，与端口：" + extendport + "冲突");
				extendportUsable = false;
				break;
			}
			
			pattern = Pattern.compile("^(?!" + string + ").*");
			matcher = pattern.matcher(extendport);
			if(!matcher.matches()){
				LOGGER.debug("自签通道用户端口校验失败，与端口：" + extendport + "冲突");
				extendportUsable = false;
				break;
			}
			
		}
		
		result.put("extendportUsable", extendportUsable);
		return result;
	}
	
	@Override
	public Map<String, Object> channelExtendportCheck3(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> checkResult = masterDao.getOneInfo("channelExtendport.channelExtendportCheck3", params);
		if(checkResult != null){
			result.put("success", false);
			return result;
		}
		result.put("success", true);
		return result;
	}

}
