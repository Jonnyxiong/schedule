package com.ucpaas.sms.service.channel;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 用户短信通道管理-通道错误状态对应关系
 * 
 * @author oylx
 */
public interface ChannelErrorDescService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(String id);

	Map<String, Object> save(Map<String, String> params);
	
	Map<String, Object> update(Map<String, String> params);
	
	Map<String, Object> delete(String id);

}
