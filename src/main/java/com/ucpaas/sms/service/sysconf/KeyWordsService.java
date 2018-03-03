package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-关键字配置
 * 
 * @author zenglb
 */
public interface KeyWordsService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> save(Map<String, String> params);

	int deleteKeyword(String id);
	
	Map<String, Object> importExcel(File importExcel, String fileType);

	List<Map<String, Object>> queryExportExcelData(Map<String, String> params);

}
