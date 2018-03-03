
package com.ucpaas.sms.util.rabbitmq;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
import com.rabbitmq.client.AMQP.Exchange.DeleteOk;
import com.rabbitmq.client.AMQP.Queue.BindOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitMQ 服务端工具类
 * 通过rabbitMQ Java Client API创建queue、exchange和binding
 */
public class RabbitMqServerUtil {
	
	private static Logger logger = LoggerFactory.getLogger(RabbitMqServerUtil.class);
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	
	
	public RabbitMqServerUtil() {
		super();
	}

	/**
	 * 创建连接和信道
	 */
	public void innit(String ip, Integer port, String virtualHost, String username, String password){
		this.factory = new ConnectionFactory();
		this.factory.setHost(ip);
		this.factory.setPort(port);
		this.factory.setVirtualHost(virtualHost);
		this.factory.setUsername(username);
		this.factory.setPassword(password);
		
		try {
			this.connection = factory.newConnection();
		} catch (Exception e) {
			logger.error("【RabbitMq】：创建connection失败 " + e);
		}
		
		try {
			this.channel = this.connection.createChannel();
		} catch (Exception e) {
			logger.error("【RabbitMq】：创建channel失败 " + e);
		}
	}
	
	/**
	 * 关闭所有连接
	 */
	public void closeAll(){
		try {
			this.channel.close();    
			this.connection.close();
		} catch (Exception e) {
			logger.error("【RabbitMq】：关闭channel和connection失败 " + e);
		}
	}
	
	/**
	 * 创建rabbitMQ exchange
	 * <br> 如果存在则什么都不做直接返回
	 * @param exchange
	 * @param type
	 * @param durable
	 * @return DeclareOk
	 */
	public DeclareOk exchangeDeclare(String exchange, String type, boolean durable){
		DeclareOk declareOk = null;
		try {
			declareOk = this.channel.exchangeDeclare(exchange, "direct", true);
		} catch (IOException e) {
			logger.error("【RabbitMq】：创建exchange失败 " + e);
		}
		return declareOk;
	}
	
	/**
	 * 创建rabbitMQ queue 
	 * @param queue
	 * @param durable
	 * @param exclusive
	 * @param autoDelete
	 * @param arguments
	 * @return DeclareOk
	 */
	public com.rabbitmq.client.AMQP.Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
		com.rabbitmq.client.AMQP.Queue.DeclareOk declareOk = null;
		try {
			declareOk = channel.queueDeclare(queue, true, false, false, null);
		} catch (IOException e) {
			logger.error("【RabbitMq】：创建queue失败 " + e);
		}
		return declareOk;
	}
	
	/**
	 * 根据routingKey绑定exchange和queue
	 * @param queue
	 * @param exchange
	 * @param routingKey
	 * @return BindOk
	 */
	public BindOk queueBind(String queue, String exchange, String routingKey) {
		BindOk bindOk = null;
		try {
			bindOk = this.channel.queueBind(queue, exchange, routingKey);
		} catch (Exception e) {
			logger.error("【RabbitMq】：创建queue失败 " + e);
		}
		return bindOk;
	}
	
	/**
	 * 删除 exchange
	 * @param exchange
	 * @return
	 */
	public DeleteOk exchangeDelete(String exchange) {
		DeleteOk deleteOk = null;
		try {
			deleteOk = this.channel.exchangeDelete(exchange);
		} catch (Exception e) {
			logger.error("【RabbitMq】：删除exchange失败 " + e);
		}
		return deleteOk;
	}
	
	/**
	 * 删除 queue
	 * @param queue
	 * @return
	 */
	public com.rabbitmq.client.AMQP.Queue.DeleteOk queueDelete(String queue) {
		com.rabbitmq.client.AMQP.Queue.DeleteOk deleteOk = null;
		try {
			deleteOk = this.channel.queueDelete(queue);
		} catch (Exception e) {
			logger.error("【RabbitMq】：删除exchange失败 " + e);
		}
		return deleteOk;
	}
	
	public static void main(String[] args){
		String url = "172.16.5.21";
		Integer port = 5672;
		String host = "/";
		String username = "guest";
		String password = "guest";
		RabbitMqServerUtil test = new RabbitMqServerUtil();
		test.innit(url, port, host, username, password);
		
		String exchange = "EXCHANGE_OYLX_1";
		String queue = "QUEUE_OYLX_1";
		String routingKey = "ROUTINGKEY_OYLX_1";
		
		test.exchangeDeclare(exchange, "direct", true);
		test.queueDeclare(queue, true, false, false, null);
		test.queueBind(queue, exchange, routingKey);
		
		test.closeAll();
	}

}
