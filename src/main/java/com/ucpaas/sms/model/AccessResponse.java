package com.ucpaas.sms.model;

import java.util.List;
import java.util.Map;

/**
 * Access HTTPS接口请求响应实体
 *
 */
public class AccessResponse {

	private int total_fee;
	
	private List<Map<String, String>> data;
	
	public AccessResponse(){
		super();
	}
	
	public AccessResponse(int total_fee, List<Map<String, String>> data){
		super();
		this.total_fee = total_fee;
		this.data = data;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}
	
	
	

}
