package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

public interface OverrateWhiteService {
	
	PageContainer query(Map<String, String> params);

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> view(String id);
	
	Map<String, Object> delete(String id);

	Map<String, Object> update(Map<String, String> params);

	Map<String, Object> importExcel(File upload, String uploadContentType);

	List<Map<String, Object>> queryExportExcelData(Map<String, String> map);
	
}
