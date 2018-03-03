/**
 * 
 */
package com.ucpaas.sms.service.salesman;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 客户接口
 */
public interface CustomerService {
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);
	
	/**
	 * 添加
	 * @param params
	 * @return
	 */
	Map<String, Object> save(Map<String, String> params);
	
	/**
	 * 删除
	 * @param params
	 * @return
	 */
	Map<String, Object> delete(Map<String, String> params);
	
}
