package com.ucpaas.sms.service.sysconf;

import java.util.HashMap;
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
import com.ucpaas.sms.util.web.AuthorityUtils;

@Service
@Transactional
public class SegmentClientServiceImpl implements SegmentClientService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SegmentClientServiceImpl.class);
	
	@Autowired
	private MessageMasterDao messageMasterDao;

	@Override
	public PageContainer query(Map<String, String> params) {
		return messageMasterDao.getSearchPage("segmentClient.query", "segmentClient.queryCount", params);
	}

	@Override
	public Map<String, Object> editView(String id) {
		return messageMasterDao.getOneInfo("segmentClient.getOneInfo", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int saveNum;
		String id = Objects.toString(params.get("id"), "");
		
		int check = messageMasterDao.getOneInfo("segmentClient.saveCheck", params);
		if(check != 0){
			data.put("result", "failure");
			data.put("msg", "添加失败，当前客户代码+用户账号已经存在记录");
			return data;
		}
		if(StringUtils.isBlank(id)){// 新建
			saveNum = messageMasterDao.insert("segmentClient.insert", params);
			id = String.valueOf(params.get("id")); // 获得mybatis返回的插入数据的主键ID
			LOGGER.debug("新建强制路由客户管理记录，插入条数 = {}, 操作管理员 = {}", saveNum, AuthorityUtils.getLoginRealName());
		}else{// 更新
			saveNum = messageMasterDao.update("segmentClient.update", params);
			LOGGER.debug("更新强制路由客户管理记录，更新条数 = {}, 操作管理员 = {}", saveNum, AuthorityUtils.getLoginRealName());
		}
		
		if(saveNum == 1){
			data.put("result", "success");
			data.put("id", id);
			data.put("msg", "保存成功");
			return data;
		}else{
			data.put("result", "fail");
			data.put("msg", "保存失败");
			return data;
		}
	}

	@Override
	public Map<String, Object> delete(Map<String, String> fromData) {
		Map<String, Object> result = new HashMap<String, Object>();
		int delNum = messageMasterDao.delete("segmentClient.delete", fromData);
		if(delNum == 1){
			result.put("result", "success");
			result.put("msg", "成功删除记录");
			LOGGER.debug("删除强制路由客户管理记录，fromData = {}，操作管理员 = {}", fromData, AuthorityUtils.getLoginRealName());
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		return result;
	}

	@Override
	public Map<String, Object> updateStatus(Map<String, String> formData) {
		Map<String, Object> data = new HashMap<String, Object>();
		int updateNum = 0;
		updateNum = messageMasterDao.update("segmentClient.updateStatus", formData);
		if(updateNum > 0){
			data.put("result", "success");
			data.put("msg", "更新成功");
		}else{
			data.put("result", "fail");
			data.put("msg", "保存失败");
		}
		return data;
	}

}
