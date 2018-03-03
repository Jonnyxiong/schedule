package com.ucpaas.sms.service.smsReport;

import java.util.List;
import java.util.Map;

/**
 * 通道消耗统计报表
 *
 */
public interface ChannelConsumeService {

	/**
	 * 查询
	 * 
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	Map<String, Object> query(Map<String, String> params) throws Exception;
	
	
	/**
	 * 用于Excel导出
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	List<Map<String, Object>> queryAll(Map<String, String> params) throws Exception;
}
