package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-审核关键字
 */
public interface AuditKeyWordsService {
	
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);
	/**
	 * 查询编辑页面数据
	 * @param id
	 * @return
	 */
	Map<String, Object> editView(int id);
	/**
	 * 保存
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
	 * Excel批量导入
	 * @param upload
	 * @param uploadContentType
	 * @return
	 */
	Map<String, Object> importExcel(File upload, String uploadContentType);
	
	/**
	 * Excel 导出数据
	 * @return
	 */
	List<Map<String, Object>> queryExportExcelData(Map<String, String> params);

}
