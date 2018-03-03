package com.ucpaas.sms.service.admin;

import java.util.Map;

/**
 * 数据查询
 */
public interface DataQueryService {
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 */
	Map<String, Object> query(Map<String, String> params);
	
}
