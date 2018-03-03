package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.RegexUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
/**
 * 
 * @author TonyHe
 *	超频白名单管理
 */

@Service
@Transactional
public class OverrateWhiteServiceImpl implements OverrateWhiteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OverrateWhiteServiceImpl.class);
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;
	@Override
	public PageContainer query(Map<String, String> params) {
		PageContainer page = masterDao.getSearchPage("overrateWhite.query", "overrateWhite.queryCount", params);
		return page ;
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("系统配置-超频白名单管理：添加：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		String phoneStr = Objects.toString(params.get("phone"), "");
		String clientId = Objects.toString(params.get("clientid"), "");
		String remarks = Objects.toString(params.get("remarks"), "");
		List<String> phoneList = Arrays.asList(phoneStr.split(","));
		List<String> legalPhoneList = new ArrayList<String>();
		
		List<Map<String, Object>> failPhoneList = new ArrayList<Map<String, Object>>();// 保存失败的手机号码
		Map<String, Object> errorData;
		for (String phone : phoneList) {
			if(phoneList.size() == 1){
				if(StringUtils.isBlank(phone)){
					legalPhoneList.add(phone);
					break;
				}
			}else{
				if(StringUtils.isBlank(phone)){
					errorData = new HashMap<String, Object>();
					errorData.put("clientid", clientId);
					errorData.put("phone", phone);
					errorData.put("reason", "手机号码为空");
					failPhoneList.add(errorData);
					continue;
				}
			}
			
			if(!RegexUtils.isMobile(phone) && !RegexUtils.isOverSeaMobile(phone)){
				errorData = new HashMap<String, Object>();
				errorData.put("clientid", clientId);
				errorData.put("phone", phone);
				errorData.put("reason", "手机号码不合法");
				failPhoneList.add(errorData);
				continue;
			}
			
			legalPhoneList.add(phone);
		}
		
		for (String phone : legalPhoneList) {
			int res = 0;
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			sqlParams.put("clientid", clientId);
			sqlParams.put("phone", phone);
			sqlParams.put("remarks", remarks);
			
			Map<String, Object> checkInfo = masterDao.getOneInfo("overrateWhite.repeatCheck", sqlParams);
			if(checkInfo != null){
				errorData = new HashMap<String, Object>();
				errorData.put("clientid", clientId);
				errorData.put("phone", phone);
				errorData.put("reason", "手机号码已经存在");
				failPhoneList.add(errorData);
			}else{
				res = masterDao.insert("overrateWhite.insert", sqlParams);
				if(res > 0){
				}else{
					errorData = new HashMap<String, Object>();
					errorData.put("clientid", clientId);
					errorData.put("phone", phone);
					errorData.put("reason", "保存失败");
					failPhoneList.add(errorData);
				}
			}
		}
		
		int phoneTotalNum = phoneList.size();
		int saveFailNum = failPhoneList.size();
		int saveSuccessNum = phoneTotalNum - saveFailNum;
		StringBuilder msg = new StringBuilder();
		if(phoneTotalNum != saveSuccessNum){
			msg.append("共");
			msg.append(phoneTotalNum);
			msg.append("个手机号码；");
			msg.append("成功保存 ");
			msg.append(saveSuccessNum);
			msg.append("个， ");
			msg.append(saveFailNum);
			msg.append("个号码保存失败!");
			data.put("msg", msg.toString());
			data.put("result", "success");
			data.put("existFail", 1);
		}else{
			msg.append("保存成功");
			data.put("msg", msg);
			data.put("result", "success");
		}
		
		if(saveFailNum > 0){
			generateErrorExcel(failPhoneList);
		}
		
		logService.add(LogType.add,LogEnum.系统配置.getValue(), "系统配置-超频白名单管理：添加超频白名单", params, data);
		return data;
	}
	
	private boolean generateErrorExcel(List<Map<String, Object>> dataList) {
		String filePath = ConfigUtils.save_path + "/import" + "/超频白名单保存失败列表-userid-" + AuthorityUtils.getLoginUserId()
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
		excel.setTitle("超频白名单保存失败列表");
		excel.addHeader(20, "用户账号", "clientid");
		excel.addHeader(20, "手机号码", "phone");
		excel.addHeader(100, "原因", "reason");
		excel.setDataList(dataList);
		return ExcelUtils.exportExcel(excel);
	}
	
	@Override
	public Map<String, Object> view(String id) {
		return masterDao.getOneInfo("overrateWhite.view", id);
	}

	@Override
	public Map<String, Object> delete(String id) {
		int res = masterDao.update("overrateWhite.delete", id);
		Map<String, Object> data = new HashMap<String, Object>();
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功删除超频白名单关键字");
		}else{
			data.put("result", "fail");
			data.put("msg", "删除超频白名单失败");
		}
		logService.add(LogType.delete, LogEnum.系统配置.getValue(), "系统配置-超频白名单管理：删除超频白名单：id", id, data);
		return data;
	}

	@Override
	public Map<String, Object> update(Map<String, String> params) {
		LOGGER.debug("系统配置-超频白名单管理：修改：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		String phone = params.get("phone");
		// 电话号码可以为空；不为空的时候校验合法性
		if(StringUtils.isNotBlank(phone)){
			if(!RegexUtils.isMobile(phone) && !RegexUtils.isOverSeaMobile(phone)){
				data.put("result", "fail");
				data.put("msg", "电话号码不合法");
				return data;
			}
		}
		
		Map<String, Object> checkInfo = masterDao.getOneInfo("overrateWhite.repeatCheck", params);
		int res = 0 ;
		if(checkInfo != null){
			data.put("result", "fail");
			data.put("msg", "超频白名单已经存在");
			return data;
		}else{
			res = masterDao.update("overrateWhite.update", params);
			if(res > 0){
				data.put("id", params.get("id"));
				data.put("result", "success");
				data.put("msg", "保存成功");
			}else{
				data.put("result", "fail");
				data.put("msg", "保存失败");
			}
		}
		logService.add(LogType.update, LogEnum.系统配置.getValue(), "系统配置-超频白名单管理：修改超频白名单：",params, data);
		return data;
	}

	@Override
	public Map<String, Object> importExcel(File uploadFile, String uploadContentType) {
		Map<String, Object> data = new HashMap<String, Object>();
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String fileName = "批量导入超频白名单" + timeStamp + ".xls";
		String filePath = ConfigUtils.save_path;
		String fileAbsPath = ConfigUtils.save_path + "/" + fileName;
		LOGGER.debug("批量导入超频白名单：文件路径 = {}", fileAbsPath);

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
		
		for (Map<String, Object> info : legalDataList) {
			int res = 0;
			
			Map<String, Object> checkInfo = masterDao.getOneInfo("overrateWhite.repeatCheck", info);
			if(checkInfo != null){
				info.put("reason", "手机号码已经存在");
				illegalDataList.add(info);
			}else{
				res = masterDao.insert("overrateWhite.insert", info);
				if(res > 0){
				}else{
					info.put("reason", "保存失败");
					illegalDataList.add(info);
				}
			}
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
			msg.append("成功导入 ");
			msg.append(importNum);
			msg.append(" 条记录；存在 ");
			msg.append(importFailNum);
			msg.append(" 条记录导入失败!");
			data.put("msg", msg.toString());
			data.put("existFail", 1);
			data.put("result", "success");
		}else{
			msg.append("导入成功");
			data.put("msg", msg);
			data.put("result", "success");
		}
		
		return data;
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
		String clientId = null;
		String phone = null;
		String remarks = null;
		
		List<String> row = new ArrayList<String>();
		for (int pos = 1; pos < excelDataList.size(); pos++) {
			// 获得Excel中一行数据
			row = excelDataList.get(pos);
			
			// 对一行数据中的多个单元格数据进行校验
			if (row != null && row.size() > 0) {
				StringBuilder errorMsg = new StringBuilder();
				
				// 校验clientId
				try {
					clientId = Objects.toString(row.get(0), "").trim();
				} catch (Exception e) {
					clientId = "";
				}
				if (StringUtils.isBlank(clientId)) {
					errorMsg.append("用户账号为空;");
				}
				if (!RegexUtils.isClientId(clientId)){
					errorMsg.append("用户账号不合法");
				}
				
				// 校验手机号码
				try {
					phone = Objects.toString(row.get(1), "").trim();
				} catch (Exception e) {
					phone = "";
				}
				if (StringUtils.isBlank(phone)) {
					errorMsg.append("Excel中导入的手机号码不能为空（手机号码为空时，超频白名单为“用户账号”级别）");
				}
				if (!RegexUtils.isMobile(phone) && !RegexUtils.isOverSeaMobile(phone)){
					errorMsg.append("手机号码不合法");
				}
				
				// 校验备注
				try {
					remarks = Objects.toString(row.get(2), "").trim();
				} catch (Exception e) {
					remarks = "";
				}
				if (remarks.length() > 128) {
					errorMsg.append("备注超长;");
				}
				
				// 校验后的数据合法的行保存在legalDataList中，非法的行保存在illegalDataList中
				Map<String, Object> validateRow = new HashMap<>();
				
				if (errorMsg.length() > 1) {
					validateRow.put("clientid", clientId);
					validateRow.put("phone", phone);
					validateRow.put("remarks", remarks);
					validateRow.put("reason", errorMsg);
					illegalDataList.add(validateRow);
				}else{
					validateRow.put("clientid", clientId);
					validateRow.put("phone", phone);
					validateRow.put("remarks", remarks);
					legalDataList.add(validateRow);
				}
			}
		}
	}

	@Override
	public List<Map<String, Object>> queryExportExcelData(Map<String, String> params) {
		return masterDao.getSearchList("overrateWhite.queryExportExcelData", params);
	}

}
