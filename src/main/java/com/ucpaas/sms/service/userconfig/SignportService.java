package com.ucpaas.sms.service.userconfig;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 用户帐号和签名端口对应关系t_sms_clientid_signport
 * @author TonyHe
 *
 */
public interface SignportService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(String id);

	Map<String, Object> save(Map<String, String> params);
	
	Map<String, Object> updateStatus(Map<String, String> params);
	
	Map<String, Object> delete(String id);
	
	Map<String, Object> checkClientIdAndGetSignport(Map<String, String> params);
}
