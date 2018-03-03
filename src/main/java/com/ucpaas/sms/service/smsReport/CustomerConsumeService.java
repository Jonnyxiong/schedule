/**
 * 
 */
package com.ucpaas.sms.service.smsReport;

import java.util.List;
import java.util.Map;

/**
 * 客户消耗统计报表接口
 */
public interface CustomerConsumeService {
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> query(Map<String, String> params) throws Exception;
	
	/**
	 * 查询所有记录  导出Excel
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	List<Map<String, Object>> queryAll(Map<String, String> params) throws Exception;
	
}
