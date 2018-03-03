package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 
 * @author TonyHe
 *	用户免审关键字管理
 */
public interface NoauditKeywordService{

	PageContainer query(Map<String, String> params);

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> view(String clientid);
	
	Map<String, Object> delete(String clientid);

	Map<String, Object> update(Map<String, String> params);
	
	Map<String, Object> updateStatus(String clientid , String status);

	Map<String, Object> getKeywordsByCid(String clientid);
}
