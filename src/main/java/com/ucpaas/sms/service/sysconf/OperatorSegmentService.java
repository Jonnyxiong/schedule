package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

public interface OperatorSegmentService {

	PageContainer query(Map<String, String> formData);

	Map<String, Object> save(Map<String, String> formData , String operater);
	Map<String, Object> update(Map<String, String> formData , String operater);
	
	Map<String, Object> view(String id);

	public Map<String, Object> delete(String id);
	
}
