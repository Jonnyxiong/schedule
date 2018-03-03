package com.ucpaas.sms.model.rabbit;

import com.google.gson.annotations.SerializedName;

/**
 * queue 绑定关系
 *
 */
public class QueueBindings {
	
	@SerializedName("destination")
	private String queue; // 绑定的queue名称
	
	@SerializedName("source")
	private String exchange; // 绑定的exchange名称

	@SerializedName("routing_key")
	private String routingKey; // 绑定关系的routingKey
	
	private String vhost;

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
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

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	
}
