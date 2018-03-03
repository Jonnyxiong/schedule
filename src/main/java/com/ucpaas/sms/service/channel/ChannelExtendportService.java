package com.ucpaas.sms.service.channel;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 通道组管理-自签通道用户端口管理
 */
public interface ChannelExtendportService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(int id);

	Map<String, Object> save(Map<String, String> params);
	
	Map<String, Object> delete(Map<String, String> params);
	
	Map<String, Object> channelExtendportCheck1(Map<String, String> params);
	
	Map<String, Object> channelExtendportCheck2(Map<String, String> params);
	
	Map<String, Object> channelExtendportCheck3(Map<String, String> params);
}
