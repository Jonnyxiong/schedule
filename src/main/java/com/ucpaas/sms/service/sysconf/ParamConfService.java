package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统配置-系统参数
 * 
 * @author zenglb
 */
public interface ParamConfService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(int logId);

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> updateStatus(Map<String, String> params);
}
