package com.ucpaas.sms.service.sysconf;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.exception.SmspBusinessException;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.RedisUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统设置-黑名单配置
 * 
 * @author zenglb
 */
@Service
@Transactional
public class SystemBlackListServiceImpl implements SystemBlackListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemBlackListServiceImpl.class);

	@Autowired
	private MessageMasterDao messageMasterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return messageMasterDao.getSearchPage("whiteList.query", "whiteList.queryCount", params);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存黑名单，添加：" + params);
		String whiteLists = params.get("whiteLists");
		Map<String, Object> data = new HashMap<String, Object>();
		if (null == whiteLists) {
			data.put("result", "fail");
			data.put("msg", "关键字不能为空");
			return data;
		}
		String[] ks = whiteLists.split(";");
		int i = 0;
		String kw = null;
		for (String s : ks) {
			kw = s.trim();
			
			if (StringUtils.isNotBlank(kw)) {
				Map<String, String> maps=new HashMap<String, String>();
				maps.put("kw", kw);
				maps.put("remark",params.get("remark") );
				i += messageMasterDao.insert("whiteList.insertWhiteList", maps);
				String channelId = "0";
				String setResult = setInRedis(kw, channelId);
				if(StringUtils.isBlank(setResult)){
					LOGGER.warn("保存系统黑名单到Redis时系统错误, 关键字={}", kw);
					throw new SmspBusinessException("保存系统黑名单到Redis时系统错误，请联系管理员");
				}
			}
		}
		
		if(i == 0){
			data.put("result", "success");
			data.put("msg", "当前手机号码已经存在");
		}else{
			data.put("result", "success");
			data.put("msg", "成功添加 " + i + " 个手机号");
		}
		
		logService.add(LogType.add,LogEnum.系统配置.getValue(), "系统配置-黑名单配置：添加黑名单配置", params, data);
		return data;
	}
	
	/**
	 * 保存系统黑明单到Redis中
	 * @param phone
	 * @param channelId
	 * @return
	 */
	private String setInRedis(String phone, String channelId) {
		String key = "black_list:" + phone;
		int index = 5; // 黑名单保存在Redis第5个分区
		String value = RedisUtils.getSpecificDb(key, index);
		String newValue;
		
		if(StringUtils.isBlank(value)){
			// 系统黑名单为全局黑名单，如果黑名单只有系统级别的保存为 0&
			newValue = channelId + "&";
			return RedisUtils.setSpecificDb(key, newValue, index);
		}else{
			// 如果当前号码既是系统黑名单又是通道黑名单则拼接保存，格式为 0&channelId&channelId&
			Set<String> set = new HashSet<String>(Arrays.asList(value.split("&")));
			if(!set.contains(channelId)){
				set.add(channelId);
			}
			newValue = StringUtils.join(set, "&").concat("&"); // 必须以 & 结尾
			return RedisUtils.setSpecificDb(key, newValue, index);
		}
	}
	
	private String deleteInRedis(String phone, String channelId){
		String key = "black_list:" + phone;
		int index = 5; // 黑名单保存在Redis第5个分区
		String value = RedisUtils.getSpecificDb(key, index);
		
		if(StringUtils.isBlank(value)){
			return "OK";
		}else{
			Set<String> set = new HashSet<String>(Arrays.asList(value.split("&")));
			set.remove(channelId);
			String newValue = StringUtils.join(set, "&");
			if(StringUtils.isBlank(newValue)){
				Long delete = RedisUtils.delKeySpecifiedDb(index, key);
				if(delete != 0l){
					return "OK";
				}else{
					return null;
				}
			}else{
				newValue = newValue.concat("&");
			}
			
			return RedisUtils.setSpecificDb(key, newValue, index);
		}
	}

	@Override
	public Map<String, Object> deleteWhiteList(String id, String phone) {
		Map<String, Object> result = new HashMap<String, Object>();
		int res = messageMasterDao.delete("whiteList.deleteWhiteList", id);
		if(res == 1){
			result.put("result", "success");
			result.put("msg", "操作成功");
		}else{
			result.put("result", "fail");
			result.put("msg", "操作失败");
		}
		
		String channelId = "0";
		String delResutl = deleteInRedis(phone, channelId);
		if(StringUtils.isBlank(delResutl)){
			LOGGER.warn("删除Redis中系统关键字时系统错误，关键字 = {}", phone);
			throw new SmspBusinessException("删除Redis中系统关键字时系统错误，请联系管理员");
		}
		logService.add(LogType.delete,LogEnum.系统配置.getValue(), "系统配置-黑名单配置：删除黑名单配置:删除id："+id);
		
		return result;
	}
	
	@Override
	public Map<String, Object> importExcel(File importExcel, String fileType){
		Map<String, Object> data = new HashMap<String, Object>();
		String fileSavePath = ConfigUtils.save_path;
		String fileName = "批量导入黑名单.xls";
		LOGGER.debug("批量导入黑名单：" + fileName);
		
		//导入文件校验
		if(StringUtils.isBlank(fileType)){
			data.put("result", "fail");
			data.put("msg", "请先选择导入Excel");
			return data;
		}

		if(!("application/vnd.ms-excel".equalsIgnoreCase(fileType))){
			data.put("result", "fail");
			data.put("msg", "导入文件格式错误，目前支持.xsl格式(97-2003)");
			return data;
		}
		//上传文件、取出数据、删除文件
		FileUtils.upload(fileSavePath, fileName, importExcel);
        List<List<String>> importDataList = ExcelUtils.importExcel(fileSavePath + "/" + fileName);
        FileUtils.delete(fileSavePath + "/" + fileName);

		// 过滤掉excel中的空白行
		if(importDataList != null && importDataList.size() > 0){
			List<List<String>> tempList = new ArrayList<>();
			for (int i = 0; i < importDataList.size(); i++) {
				List<String> row = importDataList.get(i);
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
			importDataList = tempList;
		}

		if(importDataList.size() > 60000){
			data.put("result", "fail");
			data.put("msg", "导入记录条数不能超过60000条，请拆分成多个Excel导入");
			return data;
		}

		List<Map<String, Object>> illegalDataList = new ArrayList();
		List<Map<String, Object>> legalDataList = new ArrayList();

		Map<String, Object> validateR = this.validateSystemBlackList(importDataList, illegalDataList, legalDataList);

		if("success".equals(validateR.get("success"))){
			long now = System.currentTimeMillis();
			this.insertExcelDataBatch(legalDataList, illegalDataList);
			long time1 = System.currentTimeMillis();

			for (Map<String, Object> map : legalDataList) {

				// 将黑名单保存到Redis中
				String channelId = "0";
				String whiteList = map.get("phone").toString();
				String setResult = setInRedis(whiteList, channelId);
				if(StringUtils.isBlank(setResult)){
					LOGGER.error("保存系统黑名单到Redis时系统错误, 关键字={}", whiteList);
					throw new SmspBusinessException("保存系统黑名单到Redis时系统错误，请联系管理员");
				}
			}

			long time2 = System.currentTimeMillis();
			LOGGER.debug("批量导入系统黑名单结束，耗时={} ms", time1 - now);
			data.put("result", "success");
			data.put("msg", "导入完成");
		}else{
			data.put("result", "fail");
			data.put("msg", validateR.get("msg"));
		}

		List<Map<String, Object>> importResultList = new ArrayList<>();
		importResultList.addAll(legalDataList);
		importResultList.addAll(illegalDataList);
		this.generateErrorExcel(importResultList);

		return data;

        /*//过滤数据；数据入库
        int importNum = 0;
        int totalNum = 0;
        Map<String, String> maps = new HashMap<String, String>();
        List<String> rowRecordList = new ArrayList<String>();
        int len = 0;
        if(importDataList !=null && importDataList.size() > 1){
			for(int pos = 1; pos < importDataList.size(); pos++){
				String whiteList = "";
		        String remark = "";
				rowRecordList = importDataList.get(pos);
				if(rowRecordList != null && rowRecordList.size() > 0){
					totalNum++;
					if(rowRecordList.get(0) != null){
						whiteList = rowRecordList.get(0).trim();
					}
					if(rowRecordList.size() == 2 && rowRecordList.get(1) != null){
						remark = rowRecordList.get(1).trim();
					}
					if(isMobile(whiteList)){
		    			maps.put("kw", whiteList);
		    			len = StrUtils.getStrLength(remark, false) > 50 ? 50 : StrUtils.getStrLength(remark, false);
		    			maps.put("remark", remark.substring(0, len));// 备注最多50个字
		    			importNum += messageMasterDao.insert("whiteList.insertWhiteList", maps);
		    			
		    			// 将黑名单保存到Redis中
		    			String channelId = "0";
						String setResult = setInRedis(whiteList, channelId);
						if(StringUtils.isBlank(setResult)){
							LOGGER.warn("保存系统黑名单到Redis时系统错误, 关键字={}", whiteList);
							throw new SmspBusinessException("保存系统黑名单到Redis时系统错误，请联系管理员");
						}
		    		}
				}
			}
        }*/
        
        // 返回页面显示信息
//        int totalNum = importDataList.size() -1;
        /*int ignoreNum = totalNum - importNum;
		data.put("result", "success");
		StringBuffer msg = new StringBuffer();
		if(importNum != totalNum){
			msg.append("Excel中共");
			msg.append(totalNum);
			msg.append("个黑名单; ");
			msg.append("成功添加");
			msg.append(importNum);
			msg.append("个黑名单; ");
			msg.append(ignoreNum);
			msg.append("个黑名单已存在或格式不合法");
			data.put("msg", msg);
		}else{
			msg.append("Excel中共");
			msg.append(totalNum);
			msg.append("个黑名单; ");
			msg.append("成功添加");
			msg.append(importNum);
			msg.append("个黑名单");
			data.put("msg", msg);
		}*/


	}

	private boolean generateErrorExcel(List<Map<String, Object>> dataList) {
		String filePath = ConfigUtils.save_path + "/import" + "/批量导入系统黑名单-userid-" + AuthorityUtils.getLoginUserId()
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
		excel.setTitle("批量导入系统黑名单结果列表");
		excel.addHeader(20, "手机号码", "phone");
		excel.addHeader(20, "备注", "remarks");
		excel.addHeader(20, "是否导入成功", "importState");
		excel.addHeader(20, "导入结果备注", "failReason");
		excel.setDataList(dataList);
		return ExcelUtils.exportExcel(excel);
	}

	private Map<String, Object> validateSystemBlackList(List<List<String>> excelDataList, List<Map<String, Object>> illegalDataList, List<Map<String, Object>> legalDataList) {
		Map<String, Object> result = new HashMap();

		List<String> row = null;
		row = (List)excelDataList.get(0);
		if ("手机号码".equals(row.get(0)) && "备注".equals(row.get(1))) {
			for(int pos = 1; pos < excelDataList.size(); ++pos) {
				row = (List)excelDataList.get(pos);
				if (row != null && row.size() > 0) {
					StringBuilder errorMsg = new StringBuilder();
					String phone = "";
					String remarks = "";


					try {
						phone = Objects.toString(row.get(0), "").trim();
						remarks = Objects.toString(row.get(1), "").trim();

					} catch (Exception var18) {
						errorMsg.append("数据格式异常;");
					}

					if(phone.length() > 20 || phone.length() < 11){
						errorMsg.append("请输入11位到20位的手机号码");
					}

					if(remarks.length() > 128){
						errorMsg.append("备注长度不能超过128");
					}

					String errorMsgStr = errorMsg.toString();
					Map<String, Object> systemBlackMap = new HashMap<>();

					systemBlackMap.put("phone", phone);
					systemBlackMap.put("remarks", remarks);
					if (org.apache.commons.lang3.StringUtils.isNotBlank(errorMsgStr)) {
						systemBlackMap.put("importState", "失败");
						systemBlackMap.put("failReason", errorMsgStr);
						illegalDataList.add(systemBlackMap);
					} else {
						systemBlackMap.put("importState", "成功");
						systemBlackMap.put("failReason", errorMsgStr);
						legalDataList.add(systemBlackMap);
					}
				}
			}


			result.put("success", "success");
			result.put("illegalDataList", illegalDataList);
			result.put("legalDataList", legalDataList);
			return result;
		} else {
			result.put("success", "fail");
			result.put("msg", "请使用系统提供的批量导入系统黑名单模板进行导入");
			return result;
		}
	}

	private String insertExcelDataBatch(List<Map<String, Object>> legalDataList, List<Map<String, Object>> illegalDataList) {
		try {
			Iterator i$ = legalDataList.iterator();
			List<Map<String, Object>> tempList = new ArrayList<>();
			Set<String> tempSet = new HashSet<>();

			int count = 0;
			int size = legalDataList.size();
			int countSize = 0;
			int insertSize = 0;
			while(i$.hasNext()) {
				Map<String, Object> row = (Map)i$.next();

				countSize ++;
				Map<String, Object> sqlParams = new HashMap<>();
				sqlParams.put("text", row.get("phone"));
				int isExist = messageMasterDao.getSearchSize("whiteList.queryCount", sqlParams);
				if (isExist > 0 || tempSet.contains(row.get("phone"))) {
					row.put("importState", "失败");
					row.put("failReason", "数据已经存在");
					illegalDataList.add(row);
					i$.remove();
				} else {
					tempSet.add(row.get("phone").toString());
					row.put("importState", "成功");
					row.put("failReason", "");
					tempList.add(row);
					count++;
				}

				if(count == 400 || (countSize == size && tempList.size() > 0)) {

					sqlParams.clear();
					sqlParams.put("list", tempList);
					messageMasterDao.insert("whiteList.insertWhiteListBatch", sqlParams);
					insertSize += tempList.size();
					count = 0;
					tempList.clear();
				}

			}

			return null;
		} catch (Exception var6) {
			LOGGER.debug("批量导入智能模板时系统错误", var6);
			return "批量导入智能模板时系统错误";
		}
	}


	private boolean isMobile(String mobileNum){
		String regExp = "^1[0-9]{10}$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(mobileNum);
		
		return matcher.find();
	}
	
}
