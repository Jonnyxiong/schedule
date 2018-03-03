package com.ucpaas.sms.service.userconfig;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 用户短信通道管理-短信通道配置
 * 
 * @author zenglb
 */
public interface TemplateService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(int id);

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> updateStatus(Map<String, String> params);

	Map<String, Object> delete(Map<String, String> formData);
}
