package com.ucpaas.sms.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于记录t_sms_access_yyyyMMdd表的所有字段
 *
 */
public class AccessTable {

	private String id;
	private String content;
	private String srcphone;
	private String phone;
	private String smscnt;
	private String smsindex;
	private String sign;
	private String showsigntype;
	private String submitid;
	private String smsid;
	private String clientid;
	private String username;
	private String sid;
	private String operatorstype;
	private String smsfrom;
	private String channelid;
	private String state;
	private String errorcode;
	private String date;
	private String submit;
	private String submitdate;
	private String subret;
	private String subretdate;
	private String report;
	private String reportdate;
	private String uid;
	private String smstype;
	private String costfee;
	private String salefee;
	private String productfee;
	private String sub_id;
	private String product_type;
	private String charge_num;
	private String paytype;
	private String agent_id;
	private String isoverratecharge;
	private String channeloperatorstype;
	private String channelremark;
	private String is_reback;
	private String template_id;
	private String channel_tempid;
	private String temp_params;
	private String c2s_id;
	private String process_times;
	private String longsms;
	private String channelcnt;
	
	/**
	 * 以Map的形式返回AccessTable所有的属性, key表示字段名，value表示字段是否存在（1表示字段存在，0表示不存在，默认为0）
	 * @return
	 */
	public Map<String, Integer> getAllFieldsMap() {
		Field[] fields = this.getClass().getDeclaredFields();
		Map<String, Integer> fieldsMap = new HashMap<String, Integer>();
		for (int i = 0; i < fields.length; i++) {
			fieldsMap.put("c_" + fields[i].getName(), 0);
		}
		return fieldsMap;
	}
	
}
