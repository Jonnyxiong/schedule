package com.ucpaas.sms.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于记录t_sms_record_yyyyMMdd表的所有字段
 *
 */
public class RecordTable {

	private String c_smsuuid;
	private String c_channelid;	
	private String c_channeloperatorstype;
	private String c_channelremark;
	private String c_operatorstype;
	private String c_area;
	private String c_smsid;	
	private String c_sid;	
	private String c_username;	
	private String c_content;
	private String c_smscnt;
	private String c_state;
	private String c_phone;
	private String c_duration;	
	private String c_remarks;
	private String c_ip;
	private String c_templatetype;	
	private String c_smsfrom;
	private String c_smsindex;	
	private String c_costFee;
	private String c_saleFee;	
	private String c_channelsmsid;	
	private String c_paytype;
	private String c_clientid;	
	private String c_errorcode;	
	private String c_date;
	private String c_submit;	
	private String c_submitdate;	
	private String c_subret;
	private String c_subretdate;	
	private String c_report;
	private String c_reportdate;	
	private String c_recvreportdate;	
	private String c_sidinpaas;
	private String c_showphone;
	private String c_template_id;	
	private String c_channel_tempid;	
	private String c_temp_params;
	private String c_send_id;
	private String c_longsms;	
	private String c_clientcnt;	
	private String c_channelcnt;	
	private String c_senddate;
	
	/**
	 * 以Map的形式返回RecordTable所有的属性, key表示字段名，value表示字段是否存在（1表示字段存在，0表示不存在，默认为0）
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
