package com.ucpaas.sms.service.smsReport;

import java.util.List;
import java.util.Map;

/**
 * 客户运维报表查询
 * 
 * @author oylx
 */
public interface CustomerOpretionService {

	/**
	 * 客户运维报表查询
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> query(Map<String, String> params);
	
	/**
	 * 客户运维报表查询(用于Excel导出)
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryAll(Map<String, String> params);

}
