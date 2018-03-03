package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-黑名单配置
 * 
 * @author zenglb
 */
public interface SystemBlackListService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> deleteWhiteList(String id, String phone);
	
	Map<String, Object> importExcel(File importExcel, String fileType);
}
