
package com.ucpaas.sms.util.rabbitmq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucpaas.sms.model.rabbit.QueueBindings;
import com.ucpaas.sms.model.rabbit.RabbitMqQueue;
import com.ucpaas.sms.util.HttpUtils;

/**
 * rabbitMQ 控制台api接口工具类
 * <br>通过控制台api接口查询queue、exchange等信息
 */
public class RabbitMqConsoleUtil {
	
	private static Logger logger = LoggerFactory.getLogger(RabbitMqConsoleUtil.class);
	
	
	public static RabbitMqQueue getQueueInfo(String ip, String port, String username, String password, String queue){
		// 拼接查询某个queue的api接口 http://172.16.5.21:15672/api/queues/%2f/QUEUE_OYLX_1
		StringBuilder url = new StringBuilder();
		url.append("http://");
		url.append(ip);
		url.append(":");
		url.append(port);
		url.append("/api/queues/%2f/");
		url.append(queue);
		
		try {
			RabbitMqQueue queueInfo = new RabbitMqQueue();
			String responseJson = HttpUtils.httpGetWithBasicAuthorization(url.toString(), username, password);
			if(StringUtils.isNotBlank(responseJson)){
				Gson gson = new Gson();
				queueInfo = gson.fromJson(responseJson, RabbitMqQueue.class);
			}
			return queueInfo;
		} catch (Exception e) {
			logger.error("查询单个队列信息失败：" + e);
		}
		return new RabbitMqQueue();
	}

	/**
	 * 查询rabbitMQ中的所有queue
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param getBindings 是否查询queue和exchange绑定关系（查询绑定关系很消耗时间）
	 * @return
	 */
	public static List<RabbitMqQueue> getAllQueueList(String ip, String port, String username, String password, boolean getBindings){
		
		long start = System.currentTimeMillis();
		// 拼接查询所有queue的api接口 http://172.16.5.21:15672/api/queues
		StringBuilder url = new StringBuilder();
		url.append("http://").append(ip).append(":").append(port).append("/api/queues");
		
		List<RabbitMqQueue> allQueueList = new ArrayList<RabbitMqQueue>();
		String responseJson = HttpUtils.httpGetWithBasicAuthorization(url.toString(), username, password);
		if(StringUtils.isNotBlank(responseJson)){
			Gson gson = new Gson();
			allQueueList = gson.fromJson(responseJson, new TypeToken<List<RabbitMqQueue>>() {}.getType());
		}
		long time1 = System.currentTimeMillis();
		logger.debug("查询queues接口耗时 = " + (time1 - start) + "ms");
		
		if(getBindings){
			// 上面接口返回的queue信息中不包括绑定关系，需要通过bindings接口单独查询绑定关系
			QueueBindings queueBindings;
			for (RabbitMqQueue rabbitMqQueue : allQueueList) {
				queueBindings = new QueueBindings();
				queueBindings = getQueueBindings(ip, port, rabbitMqQueue.getQueue(), username, password);
				rabbitMqQueue.setExchange(queueBindings.getExchange());
				rabbitMqQueue.setRoutingKey(queueBindings.getRoutingKey());
			}
			long time2 = System.currentTimeMillis();
			logger.debug("查询bindings接口耗时 = " + (time2 - time1) + "ms");
		}
		return allQueueList;
	}
	
	/**
	 * 查询rabbitMQ中某条queue的绑定关系
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 */
	public static QueueBindings getQueueBindings(String ip, String port, String queue, String username, String password){
		
		/*// 接口查询返回的队列和路由的绑定关系json数据格式如下：
		[
		  {
		    "source": "",
		    "vhost": "/",
		    "destination": "QUEUE_OYLX_1",
		    "destination_type": "queue",
		    "routing_key": "QUEUE_OYLX_1",
		    "arguments": {},
		    "properties_key": "QUEUE_OYLX_1"
		  },
		  {
		    "source": "EXCHANGE_OYLX_1",
		    "vhost": "/",
		    "destination": "",
		    "destination_type": "queue",
		    "routing_key": "ROUTINGKEY_OYLX_1",
		    "arguments": {},
		    "properties_key": "ROUTINGKEY_OYLX_1"
		  } 
		]
		*/
		
		long start = System.currentTimeMillis();
		// 拼接queuue和exchange绑定关系的查询api接口 http://172.16.5.21:15672/api/queues/%2F/Accessdb66fjx88Queue/bindings
		StringBuilder url = new StringBuilder();
		url.append("http://");
		url.append(ip);
		url.append(":");
		url.append(port);
		url.append("/api/queues/%2F/");
		url.append(queue);
		url.append("/bindings");
		String responseJson = HttpUtils.httpGetWithBasicAuthorization(url.toString(), username, password);
		long time1 = System.currentTimeMillis();
		logger.debug("查询" + queue + "绑定关系接口消耗时间 = " + (time1 - start) + "ms");
		List<QueueBindings> queueBindingsList = new ArrayList<QueueBindings>();
		if(StringUtils.isNotBlank(responseJson)){
			Gson gson = new Gson();
			queueBindingsList = gson.fromJson(responseJson, new TypeToken<List<QueueBindings>>() {}.getType());
		}
		
		long end = System.currentTimeMillis();
		logger.debug("查询" + queue + "绑定关系反序列化消耗时间 = " + (end - time1) + "ms");
		if(queueBindingsList.size() == 2){
			return queueBindingsList.get(1);
		}else{
			return new QueueBindings();
		}
	}
	
	/**
	 * 查询exchange是否存在binding
	 * @param ip
	 * @param port
	 * @param exchange
	 * @param username
	 * @param password
	 * @return
	 */
	public static int getExchangeBindingsNumber(String ip, String port, String exchange, String username, String password) {
		
		// （查询将exchange作为源的绑定关系） 对应的api接口 http://172.16.5.21:15672/api/exchanges/%2f/channel_exchange_direct/bindings/source
		// （查询将exchange作为目标的绑定关系） 对应的api接口 http://172.16.5.21:15672/api/exchanges/%2f/channel_exchange_direct/bindings/destination
		StringBuilder querySourceUrl = new StringBuilder();
		querySourceUrl.append("http://");
		querySourceUrl.append(ip);
		querySourceUrl.append(":");
		querySourceUrl.append(port);
		querySourceUrl.append("/api/exchanges/%2F/");
		querySourceUrl.append(exchange);
		querySourceUrl.append("/bindings/source");
		
		StringBuilder queryDestinationUrl = new StringBuilder();
		queryDestinationUrl.append("http://");
		queryDestinationUrl.append(ip);
		queryDestinationUrl.append(":");
		queryDestinationUrl.append(port);
		queryDestinationUrl.append("/api/exchanges/%2F/");
		queryDestinationUrl.append(exchange);
		queryDestinationUrl.append("/bindings/destination");
		
		String srcResponseJson = HttpUtils.httpGetWithBasicAuthorization(querySourceUrl.toString(), username, password);
		String desResponseJson = HttpUtils.httpGetWithBasicAuthorization(queryDestinationUrl.toString(), username, password);
		
		Gson gson = new Gson();
		
		if(StringUtils.isBlank(desResponseJson) && StringUtils.isBlank(desResponseJson)){
			return 0;
		}else{
			List<Map<String, Object>> srcList = gson.fromJson(srcResponseJson, new TypeToken<List<Map<String, Object>>>() {}.getType());
			List<Map<String, Object>> desList = gson.fromJson(desResponseJson, new TypeToken<List<Map<String, Object>>>() {}.getType());
			return srcList.size() + desList.size();
		}
		
	}
	

	
	public static void main(String[] args){
		// eg: http://172.16.5.21:15672/api/queues/%2F/Accessdb66fjx88Queue/bindings
		/*QueueBindings queueBindings = getQueueBindings("103.26.3.200", "8004", "Accessdb66fjx88Queue", "guest", "guest");
		Gson gson = new Gson();
		System.out.println("查询 Accessdb66fjx88Queue 的绑定关系： " + gson.toJson(queueBindings));*/
		
//		Gson gson = new Gson();
//		long start = System.currentTimeMillis();
//		List<RabbitMqQueue> list = getAllQueueList("103.26.3.200", "8001", "ucpaas_admin", "ucpaas_BJHLGW_pwd", true);
//		System.out.println("查询所有queues消耗时间 = " + (System.currentTimeMillis() - start) + "ms");
//		System.out.println("查询到quques的数量 = " + list.size());
//		System.out.println("查询到的所有quques " + gson.toJson(list));
		
		getExchangeBindingsNumber("172.16.5.21", "15672", "channel_exchange_direct", "guest", "guest");
	}

}
