package com.ucpaas.sms.service.channel;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 通道组管理-彩印通道模板信息
 * 
 */
public interface UusdTemplateService {
	
	PageContainer queryAuditResult(Map<String, String> params);
	
	PageContainer queryTokenLog(Map<String, String> params);

}
