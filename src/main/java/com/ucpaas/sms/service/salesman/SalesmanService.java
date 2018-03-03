/**
 * 
 */
package com.ucpaas.sms.service.salesman;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 销售统计报表接口
 */
public interface SalesmanService {
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);
	
	/**
	 * 添加销售人员
	 * @param params
	 * @return
	 */
	Map<String, Object> save(Map<String, String> params);
	
	/**
	 * 删除销售人员
	 * @param params
	 * @return
	 */
	Map<String, Object> delete(Map<String, String> params);
	
	/**
	 * 查询月回款任务
	 * @param params
	 * @return
	 */
	PageContainer queryTask(Map<String, String> params);
	
	/**
	 * 添加月回款任务
	 * @param params
	 * @return
	 */
	Map<String, Object> saveTask(Map<String, String> params);
	
	/**
	 * 删除月回款任务
	 * @param params
	 * @return
	 */
	Map<String, Object> deleteTask(Map<String, String> params);
	
}
