package com.ucpaas.sms.service.channel;

import com.ucpaas.sms.entity.message.Channel;
import com.ucpaas.sms.model.PageContainer;

import java.util.List;
import java.util.Map;

/**
 * 用户短信通道管理-短信通道配置
 * 
 * @author zenglb
 */
public interface ChannelService {
	
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(int id);

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> updateStatus(String id, String channelId, String currentState, String switch2State);
	
	/**
	 * 根据通道id查询通道对应的流水表的标识
	 * @param params
	 * @return
	 */
	Map<String, Object> getIdentifyByChannelId(Map<String, String> params);

	/**
	 * （下架前）查询通道信息：<br>
	 * 1、包含当前通道的通道组数量 <br>
	 * 2、强制路由通道管理中包含当前通道的配置的数量<br>
	 * @param channelId
	 * @return
	 */
	Map<String, Object> queryOffLineCheckInfo(String channelId);


	List<Channel> channelList(Map<String,String> params);
	
}
