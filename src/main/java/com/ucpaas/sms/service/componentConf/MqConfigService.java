package com.ucpaas.sms.service.componentConf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 组件管理-中间件配置
 * 
 */
public interface MqConfigService {
	
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);

	/**
	 * 查询编辑页面数据
	 * @param id
	 * @return
	 */
	Map<String, Object> editView(int id);

	/**
	 * 保存
	 * @param params
	 * @return
	 */
	Map<String, Object> save(Map<String, String> params);

	/**
	 * 删除
	 * @param mqId
	 * @return
	 */
	Map<String, Object> delete(String mqId);

	Map<String, Object> queryQueueMessageCount(Map<String, String> params);
	
	/**
	 * 根据mqId直接删除队列
	 * @param mqId
	 * @return
	 */
	boolean deleteQueueOnMq(String mqId);
	
	boolean createQueueOnMq(String middlewareId, String exchange, String queue, String routingkey);
	
}
