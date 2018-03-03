package com.ucpaas.sms.service.componentConf;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;

@Service
@Transactional
public class MqRelationServiceImpl implements MqRelationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MqConfigServiceImpl.class);
	
	@Autowired
	private MessageMasterDao messageMasterDao;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		return messageMasterDao.getSearchPage("mqRelation.query", "mqRelation.queryCount", params);
	}

	@Override
	public Map<String, Object> editView(int id) {
		return messageMasterDao.getOneInfo("mqRelation.getOneInfo", id);
	}

	@Override
	public Map<String, Object> update(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int saveNum;
		String id = Objects.toString(params.get("id"), "");
		
		if(StringUtils.isBlank(id)){// 新建
			saveNum = messageMasterDao.insert("mqRelation.insert", params);
			id = String.valueOf(params.get("id")); // 获得mybatis返回的插入数据的主键ID
		}else{// 更新
			saveNum = messageMasterDao.update("mqRelation.update", params);
		}
		
		if(saveNum == 1){
			data.put("result", "success");
			data.put("id", id);
			data.put("msg", "保存成功");
			return data;
		}else{
			data.put("result", "success");
			data.put("msg", "保存失败");
			return data;
		}
	}

	@Override
	public Map<String, Object> delete(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int delNum = messageMasterDao.delete("mqRelation.delete", params);
		if(delNum == 1){
			result.put("result", "success");
			result.put("msg", "成功删除记录");
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> queryAllMqQueue(Map<String, String> params) {
		return messageMasterDao.getSearchList("mqRelation.queryAllMqQueue", null);
	}

	@Override
	public Map<String, Object> create(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String componentId = params.get("component_id");
		String producer = Objects.toString(params.get("producer"), "");
		String consumer = Objects.toString(params.get("consumer"), "");
		
		if(StringUtils.isNotBlank(consumer)){
			List<String> consumerList = Arrays.asList(consumer.split(","));
			boolean isInsertOk = insertMqRelationInfo(consumerList, "1", componentId);
			
			if(isInsertOk){
				result.put("result", "success");
				result.put("msg", "保存成功");
			}else{
				result.put("result", "fail");
				result.put("msg", "保存失败");
				LOGGER.error("写入Mq和SMSP组件消费者关系时失败");
				return result;
			}
			
		}
		
		if(StringUtils.isNotBlank(producer)){
			List<String> producerList = Arrays.asList(producer.split(","));
			boolean isInsertOk = insertMqRelationInfo(producerList, "0", componentId);
			if(isInsertOk){
				result.put("result", "success");
				result.put("msg", "保存成功");
			}else{
				result.put("result", "fail");
				result.put("msg", "保存失败");
				LOGGER.error("写入Mq和SMSP组件生产者关系时失败");
				return result;
			}
		}
		
		return result;
	}
	
	private boolean insertMqRelationInfo(List<String> mqIdList, String mqMode, String componentId){
		List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
		
		String get_rate;
		if(mqMode.equals("1")){
			get_rate = "50";
		}else{
			get_rate = "0";
		}
		
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("componentId", componentId);
		for (String mqId : mqIdList) {
			sqlParams.put("mqId", mqId);
			sqlParams.put("mqMode", mqMode);
			int mqAndCompenontRelationNum = messageMasterDao.getSearchSize("mqRelation.queryMqAndCompenontRelationNum", sqlParams);
			if(mqAndCompenontRelationNum > 0){
				continue;
			}
			Map<String, Object> mqRalationSaveInfo = new HashMap<String, Object>();
			Map<String, Object> mqInfo = messageMasterDao.getOneInfo("mqRelation.getMqInfoById", mqId);
			mqRalationSaveInfo.put("component_id", componentId);
			mqRalationSaveInfo.put("message_type", mqInfo.get("message_type"));
			mqRalationSaveInfo.put("mode", mqMode);
			mqRalationSaveInfo.put("mq_id", mqId);
			mqRalationSaveInfo.put("get_rate", get_rate);
			insertList.add(mqRalationSaveInfo);
		}
		
		if(insertList.size() > 0){
			try {
				sqlParams = new HashMap<String, Object>();
				sqlParams.put("insertList", insertList);
				messageMasterDao.insert("mqRelation.insertMqRelationList", sqlParams);
			} catch (Exception e) {
				LOGGER.error("写入Mq和SMSP组件关系时系统错误：" + e);
				return false;
			}
		}
		return true;
	}

	@Override
	public Map<String, Object> updateMqGetRate(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int num = messageMasterDao.update("mqRelation.updateMqGetRate", params);
		if(num > 0){
			result.put("result", "success");
			result.put("msg", "保存成功");
		}else{
			result.put("result", "fail");
			result.put("msg", "保存失败");
		}
		return result;
	}

}
