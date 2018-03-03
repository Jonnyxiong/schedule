/**
 * 
 */
package com.ucpaas.sms.service.salesman;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 客户充值记录管理接口
 */
public interface CustomerRechargeService {
	
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
	
	/**
	 * 通过客户id查询对应销售
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getSalesmanById(Map<String, String> params);
	
}
