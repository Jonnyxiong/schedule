package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

public interface OverrateUserListService {
	
	PageContainer query(Map<String, String> params);

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> view(String id);
	
	Map<String, Object> delete(String id);

	Map<String, Object> update(Map<String, String> params);
	
}
