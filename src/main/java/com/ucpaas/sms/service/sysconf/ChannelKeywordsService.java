package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-通道关键字配置
 * 
 */
public interface ChannelKeywordsService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> view(String cid);
	
	Map<String, Object> save(Map<String, String> params, String id);

	Map<String, Object> delete(String id);

	Map<String, Object> importExcel(File upload, String uploadContentType);
	
	Map<String, Object> getChannelKeywordsByCid(String cid);

	List<Map<String, Object>> queryExportExcelData(Map<String, String> map);
	
}
