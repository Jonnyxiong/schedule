package com.ucpaas.sms.service.auditorKeyWord;

import com.jsmsframework.audit.entity.JsmsAuditKeywordCategory;
import com.jsmsframework.audit.entity.JsmsAuditKeywordList;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.ResultVO;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface AuditorKeyWordService {

    //	List<JsmsAuditKeywordCategory> query(Map<String, String> params);
    JsmsPage<JsmsAuditKeywordCategory> queryKeywordCategoryPage(JsmsPage<JsmsAuditKeywordCategory> jsmsPage);

    JsmsPage<JsmsAuditKeywordList> queryKeywordList(JsmsPage<JsmsAuditKeywordList> jsmsPage);

//	@Deprecated
//	String addKeyWord(Map<String, String> params);

//	@Deprecated
//	List<JsmsAuditKeywordCategory> queryEditKeyWord(Map<String, String> params);

    ResultVO addKeywordCategory(Map<String, String> params);

    ResultVO importExcel(File upload, Long adminId,Integer categoryId) throws IOException;

    ResultVO importExcel(File upload, Long adminId) throws IOException;

    ResultVO addKeyword(Map<String, String> params);

    ResultVO delKeywords(Map<String, String> params);

    ResultVO updateKeywordCategory(Map<String, String> params) throws ParseException;

    ResultVO updateKeyword(Map<String, String> params) throws ParseException;

    List<Map<String, Object>> queryExportExcelData(Map<String, String> formData);

    List<JsmsAuditKeywordList> queryKeyWord(Map<String, String> params);

//	List<JsmsAuditKeywordList> queryManage(Map<String, String> params);

//	String addManageKeyWord(Map<String, String> params);
//
//	String updateManageKeyWord(Map<String, String> params);

    List<Map<String, Object>> queryExportManageExcel(Map<String, String> formData);

//	void delectKeyWord(Map<String, String> params);


}
