package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-条数配置
 * 
 * @author liulu
 */
public interface SmsCountService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> save(Map<String, String> params);

	int deleteSmscount(String id,String tableid);
	
	Map<String, Object> view(String tableid,String id);
	
}
