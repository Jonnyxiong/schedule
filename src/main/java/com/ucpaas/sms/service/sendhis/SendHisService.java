package com.ucpaas.sms.service.sendhis;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 短信发送历史-短信发送历史
 * 
 * @author zenglb
 */
public interface SendHisService {

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
}
