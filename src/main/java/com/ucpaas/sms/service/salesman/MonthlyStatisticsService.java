/**
 * 
 */
package com.ucpaas.sms.service.salesman;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 销售月统计报表接口
 */
public interface MonthlyStatisticsService {
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);
	
	/**
	 * 查询所有记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryAll(Map<String, String> params);
}
