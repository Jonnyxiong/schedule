package com.ucpaas.sms.model.rabbit;

import com.google.gson.annotations.SerializedName;

/**
 * Rabbit MQ 队列信息
 *
 */
public class RabbitMqQueue {
	
	@SerializedName("name")
	private String queue; // 队列名称
	
	private String messages; // 队列中的消息数量

	private boolean durable; // 是否持久化
	
	private String exchange; // 队列的路由
	
	private String routingKey; // 路由Key
	
	public boolean isDurable() {
		return durable;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}
	
	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	
}
