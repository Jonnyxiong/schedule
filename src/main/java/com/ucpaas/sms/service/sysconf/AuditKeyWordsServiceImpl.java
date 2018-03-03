package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.RegexUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;

@Service
@Transactional
public class AuditKeyWordsServiceImpl implements AuditKeyWordsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditKeyWordsServiceImpl.class);
	
	@Autowired
	private MessageMasterDao messageMasterDao;

	@Override
	public PageContainer query(Map<String, String> params) {
		return messageMasterDao.getSearchPage("auditKeyWords.query", "auditKeyWords.queryCount", params);
	}

	@Override
	public Map<String, Object> editView(int id) {
		return messageMasterDao.getOneInfo("auditKeyWords.getOneInfo", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int saveNum;
		String id = Objects.toString(params.get("id"), "");
		
		int check = messageMasterDao.getOneInfo("auditKeyWords.saveCheck", params);
		if(check != 0){
			data.put("result", "failure");
			data.put("msg", "添加失败，当前类型下面已经存在该关键字");
			return data;
		}
		if(StringUtils.isBlank(id)){// 新建
			saveNum = messageMasterDao.insert("auditKeyWords.insert", params);
			id = String.valueOf(params.get("id")); // 获得mybatis返回的插入数据的主键ID
		}else{// 更新
			saveNum = messageMasterDao.update("auditKeyWords.update", params);
		}
		
		if(saveNum == 1){
			data.put("result", "success");
			data.put("id", id);
			data.put("msg", "保存成功");
			return data;
		}else{
			data.put("result", "success");
			data.put("msg", "保存失败");
			return data;
		}
	}

	@Override
	public Map<String, Object> delete(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int delNum = messageMasterDao.delete("auditKeyWords.delete", params);
		if(delNum == 1){
			result.put("result", "success");
			result.put("msg", "成功删除记录");
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		return result;
	}

	@Override
	public Map<String, Object> importExcel(File uploadFile, String uploadContentType) {
		Map<String, Object> data = new HashMap<String, Object>();
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String fileName = "批量导入审核及超频关键字" + timeStamp + ".xls";
		String filePath = ConfigUtils.save_path;
		String fileAbsPath = ConfigUtils.save_path + "/" + fileName;
		LOGGER.debug("批量导入审核及超频关键字：文件路径 = {}", fileAbsPath);

		// 校验Excel格式、大小
		Map<String, Object> validate = validateExcel(uploadFile, uploadContentType);
		if(validate != null){
			return validate;
		}

		// 获得Excel文件中的数据（上传文件、取出数据、删除文件）
		List<List<String>> excelDataList = getImportExcelData(uploadFile, fileName, filePath, fileAbsPath);
		
		if (excelDataList.size() > 15000) {
			data.put("result", "fail");
			data.put("msg", "您选择的excel中数据记录大于15000条，请您拆分后上传");
			return data;
		}
		
		List<Map<String, Object>> illegalDataList = new ArrayList<Map<String, Object>>(); // Excel中非法数据
		List<Map<String, Object>> legalDataList = new ArrayList<Map<String, Object>>(); // Excel中合法数据
		if (excelDataList != null && excelDataList.size() > 1) {
			validateExcelData(excelDataList, illegalDataList, legalDataList);
		} else {
			data.put("result", "fail");
			data.put("msg", "excel中没有数据");
			return data;
		}
		
		Map<String, Object> saveResult = saveDataBatch(legalDataList);
		
		if("success".equals(saveResult.get("result"))){
			List<Map<String, Object>> rowNotInsert = (List<Map<String, Object>>) saveResult.get("rowNotInsert");
			if(rowNotInsert != null && rowNotInsert.size() > 0){
				illegalDataList.addAll(rowNotInsert);
			}
			
			if(illegalDataList.size() > 0){
				// 生成导入失败的Excel
				generateErrorExcel(illegalDataList);
			}
			
			int totalNum = excelDataList.size() - 1;
			int importFailNum = illegalDataList.size();
			int importNum = totalNum - importFailNum;
			StringBuilder msg = new StringBuilder();
			if(totalNum != importNum){
				msg.append("Excel中共");
				msg.append(totalNum);
				msg.append("条记录；");
				msg.append("成功导入");
				msg.append(importNum);
				msg.append("条记录；存在 ");
				msg.append(importFailNum);
				msg.append("条记录导入失败!");
				data.put("msg", msg.toString());
				data.put("existFail", 1);
				data.put("result", "success");
			}else{
				msg.append("导入成功");
				data.put("msg", msg);
				data.put("result", "success");
			}
		}else{
			data.put("result", "success");
			data.put("msg", "Excel导入失败");
		}
		
		return data;
	}
	
	private boolean generateErrorExcel(List<Map<String, Object>> dataList) {
		String filePath = ConfigUtils.save_path + "/import" + "/审核及超频关键字导入失败列表-userid-" + AuthorityUtils.getLoginUserId()
				+ ".xls";
		File dir = new File(ConfigUtils.save_path + "/import");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setShowPage(false);
		excel.setTitle("审核及超频关键字导入失败列表");
		excel.addHeader(20, "关键字", "keyword");
		excel.addHeader(20, "用户账号", "clientid");
		excel.addHeader(20, "类型", "typeText");
		excel.addHeader(20, "备注", "remarks");
		excel.addHeader(100, "原因", "reason");
		excel.setDataList(dataList);
		return ExcelUtils.exportExcel(excel);
	}
	
	private Map<String, Object> saveDataBatch(List<Map<String, Object>> excelDataList) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> rowNotInsert = new ArrayList<Map<String, Object>>();
		try {
			for (Map<String, Object> oneRow : excelDataList) {
				
				int queryCount = messageMasterDao.getOneInfo("auditKeyWords.saveCheck", oneRow);
				if(queryCount > 0){
					oneRow.put("reason", "数据已经存在或Excel中重复");
					rowNotInsert.add(oneRow);
				}else{
					messageMasterDao.insert("auditKeyWords.insert", oneRow);
				}
			}
			
			result.put("result", "success");
			if(rowNotInsert.size() > 0){
				result.put("rowNotInsert", rowNotInsert);
			}
		} catch (Exception e) {
			result.put("result", "fail");
			LOGGER.debug("批量导入审核及超频关键字时系统错误", e);
		}
		
		return result;
	}



	private Map<String, Object> validateExcel(File uploadFile, String uploadContentType) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (StringUtils.isBlank(uploadContentType)) {
			data.put("result", "fail");
			data.put("msg", "请先选择导入Excel");
			return data;
		}

		if (uploadFile.length() > 1148576L) {
			data.put("result", "fail");
			data.put("msg", "您选择的文件大于1M,请将excel拆分后重新上传");
			return data;
		}

		if (!("application/vnd.ms-excel".equalsIgnoreCase(uploadContentType))) {
			data.put("result", "fail");
			data.put("msg", "导入文件格式错误，目前支持.xls格式(97-2003)，请使用模板");
			return data;
		}
		return null;
	}

	private List<List<String>> getImportExcelData(File uploadFile, String fileName, String filePath, String fileAbsPath) {
		FileUtils.upload(filePath, fileName, uploadFile);
		List<List<String>> excelDataList = ExcelUtils.importExcel(fileAbsPath);
		FileUtils.delete(fileAbsPath);
		
		// 过滤掉excel中的空白行
		if(excelDataList != null && excelDataList.size() > 0){
			List<List<String>> tempList = new ArrayList<>();
			for (int i = 0; i < excelDataList.size(); i++) {
				List<String> row = excelDataList.get(i);
				boolean isNotEmpty = false;
				for (String cell : row) {
					if (StringUtils.isNotBlank(cell)) {
						isNotEmpty = true;
					}
				}
				if (isNotEmpty) {
					tempList.add(row);
				}
			}
			excelDataList = tempList;
		}
		return excelDataList;
	}
	
	private void validateExcelData(List<List<String>> excelDataList, List<Map<String, Object>> illegalDataList,
			List<Map<String, Object>> legalDataList) {
		String keyword = null;
		String clientId = null;
		String type = null;
		String typeText = null;
		String remarks = null;
		
		List<String> row = new ArrayList<String>();
		for (int pos = 1; pos < excelDataList.size(); pos++) {
			// 获得Excel中一行数据
			row = excelDataList.get(pos);
			
			// 对一行数据中的多个单元格数据进行校验
			if (row != null && row.size() > 0) {
				StringBuilder errorMsg = new StringBuilder();
				
				// 校验关键字
				try {
					keyword = Objects.toString(row.get(0), "").trim();
				} catch (Exception e) {
					keyword = "";
				}
				if (StringUtils.isBlank(keyword)) {
					errorMsg.append("关键字为空;");
				}
				if (keyword.length() > 60){
					errorMsg.append("关键字长度超过60;");
				}
				
				// 校验clientId
				try {
					clientId = Objects.toString(row.get(1), "").trim();
				} catch (Exception e) {
					clientId = "";
				}
				if (StringUtils.isBlank(clientId)) {
					errorMsg.append("用户账号为空;");
				}
				if (!RegexUtils.isClientId(clientId) && !"*".equals(clientId)){
					errorMsg.append("用户账号不合法");
				}
				
				// 校验类型
				try {
					type = Objects.toString(row.get(2), "").trim();
				} catch (Exception e) {
					type = "";
				}
				if ("审核关键字".equals(type)) {
					type = "0";
					typeText = "审核关键字";
				}else if("超频关键字".equals(type)){
					type = "1";
					typeText = "超频关键字";
				}else{
					typeText = type;
					errorMsg.append("关键字类型错误;");
				}
				
				// 校验备注
				try {
					remarks = Objects.toString(row.get(3), "").trim();
				} catch (Exception e) {
					remarks = "";
				}
				if (remarks.length() > 128) {
					errorMsg.append("备注超长;");
				}
				
				// 校验后的数据合法的行保存在legalDataList中，非法的行保存在illegalDataList中
				Map<String, Object> validateRow = new HashMap<>();
				
				if (errorMsg.length() > 1) {
					validateRow.put("keyword", keyword);
					validateRow.put("clientid", clientId);
					validateRow.put("remarks", remarks);
					validateRow.put("type", type);
					validateRow.put("typeText", typeText);
					validateRow.put("reason", errorMsg);
					illegalDataList.add(validateRow);
				}else{
					validateRow.put("keyword", keyword);
					validateRow.put("clientid", clientId);
					validateRow.put("remarks", remarks);
					validateRow.put("type", type);
					validateRow.put("typeText", typeText);
					legalDataList.add(validateRow);
				}
			}
		}
	}

	@Override
	public List<Map<String, Object>> queryExportExcelData(Map<String, String> params) {
		return messageMasterDao.getSearchList("auditKeyWords.queryExportExcelData", params);
	}

}
