package com.ucpaas.sms.service.componentConf;

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

@Service
@Transactional
public class SmspErrorCodeMgntServiceImpl implements SmspErrorCodeMgntService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmspErrorCodeMgntServiceImpl.class);
	
	@Autowired
	private MessageMasterDao messageMasterDao;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		return messageMasterDao.getSearchPage("smspErrorCodeMgnt.query", "smspErrorCodeMgnt.queryCount", params);
	}

	@Override
	public Map<String, Object> editView(int id) {
		return messageMasterDao.getOneInfo("smspErrorCodeMgnt.getOneInfo", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int saveNum;
		String id = Objects.toString(params.get("id"), "");
		
		int check = messageMasterDao.getOneInfo("smspErrorCodeMgnt.checkSave", params);
		if(check != 0){
			data.put("result", "failure");
			data.put("msg", "保存失败，当前错误码（" + params.get("syscode") + "）已经存在");
			return data;
		}
		
		if(StringUtils.isBlank(id)){// 新建
			saveNum = messageMasterDao.insert("smspErrorCodeMgnt.insert", params);
			id = String.valueOf(params.get("id")); // 获得mybatis返回的插入数据的主键ID
		}else{// 更新
			saveNum = messageMasterDao.update("smspErrorCodeMgnt.update", params);
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
		int delNum = messageMasterDao.delete("smspErrorCodeMgnt.delete", params);
		if(delNum == 1){
			result.put("result", "success");
			result.put("msg", "成功删除记录");
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		return result;
	}

}
