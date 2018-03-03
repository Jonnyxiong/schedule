package com.ucpaas.sms.service.channel;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 用户短信通道管理-通道管理
 * 
 * @author oylx
 */
public interface ChannelGroupService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(int id);

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> updateStatus(Map<String, String> params);
	
	Map<String, Object> delete(Map<String, String> params);
	
	Map<String, Object> switchChannel(Map<String, String> params);
	
	Map<String, Object> queryChannelGroupByOperator();
}
