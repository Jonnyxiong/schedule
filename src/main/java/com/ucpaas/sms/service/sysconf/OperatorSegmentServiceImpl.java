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
@Service
@Transactional
public class OperatorSegmentServiceImpl implements OperatorSegmentService{
	private static final Logger LOGGER = LoggerFactory.getLogger(OperatorSegmentServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("operatorSegment.query", "operatorSegment.queryCount", params);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params, String operater) {
		LOGGER.debug("系统配置-运营商号段表：添加：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		int res = 0;
		Map<String, Object> checkInfo = masterDao.getOneInfo("operatorSegment.segmentCheck", params);
		if(checkInfo != null){
			data.put("result", "fail");
			data.put("msg", "运营商类型已经存在，请重新输入或追加");
			return data;
		}else{
			res = masterDao.insert("operatorSegment.insert", params);
		}
		
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功添加 " + res + " 个运营商号段");
		}else{
			data.put("result", "fail");
			data.put("msg", "添加运营商号段失败");
		}
		logService.add(LogType.add,LogEnum.系统配置.getValue(), "系统配置-运营商号段：添加/修改运营商号段", params, data);
		return data;
	}

	@Override
	public Map<String, Object> update(Map<String, String> params, String operater) {
		String toOperater = params.get("operater").toString();
		LOGGER.debug("系统配置-运营商号段表：修改：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		int res = 0;
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("operater", operater);
		Map<String, Object> checkInfo = masterDao.getOneInfo("operatorSegment.segmentCheck", queryMap);
		Map<String, Object> checkInfoexist = masterDao.getOneInfo("operatorSegment.segmentCheck", params);
		if(checkInfo == null){
			data.put("result", "fail");
			data.put("msg", "运营商类型不存在，请重新选择。");
			return data;
		}else if(!toOperater.equals(operater) && checkInfoexist != null){
			data.put("result", "fail");
			data.put("msg", "运营商类型已经存在，不能修改为该运营商类型。");
			return data;
		}else{
			params.put("text", operater);
			res = masterDao.update("operatorSegment.update", params);
		}
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功修改 " + res + " 个运营商号段");
		}else{
			data.put("result", "fail");
			data.put("msg", "运营商号段失败");
		}
		logService.add(LogType.update, LogEnum.系统配置.getValue(),"系统配置-运营商号段：添加/修改运营商号段", params, data);
		return data;
	}

	
	
	@Override
	public Map<String, Object> view(String id) {
		return masterDao.getOneInfo("operatorSegment.view", id);
	}

	@Override
	public Map<String, Object> delete(String id) {
		int res = masterDao.delete("operatorSegment.delete", id);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", "fail");
		data.put("msg", "删除功能屏蔽");
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功删除运营商号段");
		}else{
			data.put("result", "fail");
			data.put("msg", "删除运营商号段失败");
		}
		return data;
	}


}
