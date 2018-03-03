package com.ucpaas.sms.service.componentConf;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.model.rabbit.RabbitMqQueue;

/**
 * 组件管理-中间件配置
 * 
 */
public interface MiddleareService {
	
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
	 * @param params
	 * @return
	 */
	Map<String, Object> delete(Map<String, String> params);

	/**
	 * 根据中间件ID查询所有的rabbitMQ队列
	 */
	List<RabbitMqQueue> queryAllQueueByMiddlewareId(String middlewareId);

	/**
	 * 根据MQ中间件ID在对应rabbitMQ上面创建队列
	 * @param middlewareId
	 * @return
	 */
	Map<String, Object> createQueueOnRabbitMQ(String middlewareId);
	
	/**
	 * 根据MQ中间件ID删除对应rabbitMQ上面所有创建队列
	 * @param middlewareId
	 * @return
	 */
	Map<String, Object> deleteQueueOnRabbitMQ(String middlewareId);

	/**
	 * 根据MQ中间件ID返回该MQ上面应该创建的队列
	 * @param middlewareId
	 * @return
	 */
	PageContainer queryNeedCreateQueueByMiddlewareId(String middlewareId);
	
}
