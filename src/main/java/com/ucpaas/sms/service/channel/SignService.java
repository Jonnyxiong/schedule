package com.ucpaas.sms.service.channel;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 用户短信通道管理-签名扩展号管理
 * 
 * @author 刘路
 */
public interface SignService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(int id);

	Map<String, Object> save(Map<String, String> params);


	Map<String, Object> delete(Map<String, String> formData);
	
	/**
	 * 同一通道下面签名唯一
	 * @param formData
	 * @return
	 */
	Map<String, Object> checkSignExist(Map<String, String> formData);
	
	Map<String, Object> checkAppendIdExist(Map<String, String> formData);
	
	Map<String, Object> channelAppendIdCheck1(Map<String, String> params);
	
	Map<String, Object> channelAppendIdCheck2(Map<String, String> params);
	
	Map<String, Object> channelAppendIdCheck3(Map<String, String> params);
}
