package com.ucpaas.sms.service.sendhis.customerSend;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 短信发送历史-客户发送历史记录（Access）
 * 
 * @author oylx
 */
public interface CustomerSendHisService {

	/**
	 * 短信发送记录
	 * 
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	PageContainer query(Map<String, String> params) throws Exception;
	
	/**
	 * 查询数据 用于导出
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	 
	List<Map<String, Object>> queryAll(Map<String, String> params) throws Exception;

	/**
	 * 查询所有的系统关键字和当前clientId绑定的通道的所有通道关键字
	 */
	Map<String, Object> queryKeyWords(String clientId);
}
