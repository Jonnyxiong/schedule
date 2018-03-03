package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
import com.ucpaas.sms.util.StrUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;

/**
 * 系统设置-通道关键字配置
 * 
 */
@Service
@Transactional
public class ChannelKeywordsServiceImpl implements ChannelKeywordsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelKeywordsServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("channelKeywords.query", "channelKeywords.queryCount", params);
	}

	@Override
	public Map<String, Object> view(String cid) {
		List<Map<String, Object>> result = masterDao.getSearchList("channelKeywords.view", cid);
		Map<String, Object> channelKeywordsInfo = new HashMap<String, Object>();
		if (null != result && result.size() > 0) {
			channelKeywordsInfo.putAll(result.get(0));
		}
		// 将拆分的通道关键字信息合并（长度超过4000的关键字信息会拆分存储）
		String keyword = "";
		for (Map<String, Object> map : result) {
			keyword += map.get("keyword");
		}
		channelKeywordsInfo.put("keyword", keyword);
		return channelKeywordsInfo;
	}

	@Override
	public Map<String, Object> save(Map<String, String> params, String id) {

		LOGGER.debug("系统配置-通道关键字：添加/修改：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		String msg = "增加";
		LogType logType = LogType.add;
		int res;
		if (StringUtils.isBlank(id)) {// 添加
			// 校验通道是否已经创建关键字
			String pageAddKeywords = params.get("keyword");
			StringBuffer existKeywordsInDb = new StringBuffer();
			List<String> dbChannelKeywordList = masterDao.selectList("channelKeywords.getChannelKeywordsByCid", params);
			if (dbChannelKeywordList != null && dbChannelKeywordList.size() > 0) {
				for (String ckw : dbChannelKeywordList) {
					existKeywordsInDb.append(ckw);
				}

				masterDao.delete("channelKeywords.delete", params.get("cid"));
			}

			String allChannelKeywords = "";
			if(existKeywordsInDb.length() > 0)
                allChannelKeywords = existKeywordsInDb.toString() + "|" + pageAddKeywords;
            else
                allChannelKeywords = pageAddKeywords;

			allChannelKeywords = keywordsFilter(allChannelKeywords);
			// 若通道关键字长度超过4000则拆分成多条保存
			String[] channelKeywordArray = StrUtils.spriltStringByLength(allChannelKeywords, 4000);
			int successFlag = 0;
			for (String keyword : channelKeywordArray) {
				params.put("keyword", keyword);// 拆分的多条信息只有关键字信息不一样，其余保持一致
				params.put("status", "1");
				res = masterDao.insert("channelKeywords.insert", params);
				successFlag += res;
			}

			if (successFlag == channelKeywordArray.length) {
				data.put("result", "success");
				data.put("msg", "成功添加通道关键字");
			} else {
				data.put("result", "fail");
				data.put("msg", "添加关键字失败");
			}
		} else {// 更新
				// 校验通道是否已经创建关键字
			msg = "修改";
			logType = LogType.update;
			String newKeywords = params.get("keyword");
			if (null != params.get("cid") && null != params.get("cid_bak")) {
				if (!params.get("cid").equals(params.get("cid_bak"))) {

					Map<String, Object> checkInfo = masterDao.getOneInfo("channelKeywords.getKeywordsByCid", params);
					if (checkInfo != null) {
						String existKeywords = String.valueOf(checkInfo.get("keywords"));
						newKeywords = existKeywords + "|" + newKeywords;
						masterDao.delete("channelKeywords.delete", params.get("cid"));
					}
				}
			}

			// 先删除该通道老的关键字信息再重新创建
			masterDao.delete("channelKeywords.delete", params.get("cid_bak"));

			// 若关键字长度超过4000则拆分成多条保存
			String keywords = keywordsFilter(newKeywords);
			String[] keywordArray = StrUtils.spriltStringByLength(keywords, 4000);
			int successFlag = 0;
			for (String keyword : keywordArray) {
				params.put("keyword", keyword);// 拆分的多条信息只有关键字信息不一样，其余保持一致
				res = masterDao.insert("channelKeywords.insert", params);
				successFlag += res;
			}

			if (successFlag == keywordArray.length) {
				data.put("result", "success");
				data.put("msg", "成功修改通道关键字");
			} else {
				data.put("result", "fail");
				data.put("msg", "修改通道关键字失败");
			}
		}

		logService.add(logType, LogEnum.系统配置.getValue(), "系统配置-通道关键字：" + msg + "通道关键字", params, data);
		return data;
	}

	/**
	 * 过滤关键字中多余的“|”和重复的关键字
	 * 
	 * @param keyword
	 * @return 用“|”分隔的关键字字符串
	 */
	private String keywordsFilter(String keyword) {
		String result;
		keyword = Objects.toString(keyword, "");
		keyword = keyword.trim().replace("\n", "");// 去除回车符
		String[] keywordsArray = keyword.split("\\|");
		Set<String> set = new LinkedHashSet<String>();

		// 去重
		for (String kw : keywordsArray) {
			if (StringUtils.isNotBlank(kw)) {
				if (set.contains(kw)) {

				} else {
					set.add(kw);
				}
			}
		}

		// 将关键字用“|”拼接
		result = StringUtils.join(set, "|");

		return result;
	}

	@Override
	public Map<String, Object> delete(String cid) {
		LOGGER.debug("系统配置-通道关键字：删除：" + cid);
		// 通道关键字软删除
		int res = masterDao.delete("channelKeywords.delete", cid);
		Map<String, Object> data = new HashMap<String, Object>();
		if (res > 0) {
			data.put("result", "success");
			data.put("msg", "成功删除通道关键字");
		} else {
			data.put("result", "fail");
			data.put("msg", "删除通道关键字失败");
		}
		logService.add(LogType.delete, LogEnum.系统配置.getValue(), "系统配置-通道关键字：删除通道关键字：id", cid, data);
		return data;
	}

	@Override
	public synchronized Map<String, Object> importExcel(File uploadFile, String uploadContentType) {
		Map<String, Object> data = new HashMap<String, Object>();
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String fileName = "批量导入通道关键字" + timeStamp + ".xls";
		String filePath = ConfigUtils.save_path;
		String fileAbsPath = ConfigUtils.save_path + "/" + fileName;
		LOGGER.debug("批量导入通道关键字：文件路径 = {}", fileAbsPath);

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

		Map<String, Object> result = insertImportDataList(legalDataList);
		List<Map<String, Object>> illegal = (List<Map<String, Object>>) result.get("illegalDataList");
		boolean hasInsertData = (boolean) result.get("hasInsertData");
		if(illegal.size() > 0){
			illegalDataList.addAll(illegal);
		}
		
		int totalNum = excelDataList.size() - 1; // 导入数据总数需要减去标题行
		int failNum = illegalDataList.size();
		int inserNum = totalNum - failNum; 
		data.put("result", "success");
		StringBuilder msg = new StringBuilder();
		// 因为excel行数据中可能存在部分关键字导入成功 所以insertNum = 0表示所有excel中所有行都存在导入失败数据
		if(inserNum == 0 && !hasInsertData){
			msg.append("Excel导入失败");
			data.put("msg", msg);
			data.put("existFail", 1);
			genExcel(illegalDataList);
		}else if (hasInsertData || (inserNum > 0 && totalNum != inserNum)) {
			msg.append("Excel部分导入成功，");
			msg.append("存在");
			msg.append(failNum);
			msg.append("条记录导入失败!");
			data.put("msg", msg);
			data.put("existFail", 1);
			genExcel(illegalDataList);
		} else {
			msg.append("导入成功");
			data.put("msg", msg);
		}
		logService.add(LogType.add, LogEnum.系统配置.getValue(), "系统配置-通道关键词：批量导入通道关键字Excel", fileName);
		return data;
	}

	private void validateExcelData(List<List<String>> excelDataList, List<Map<String, Object>> illegalDataList,
			List<Map<String, Object>> legalDataList) {
		Integer cid = null;
		String keyword = null;
		String remarks = null;
		String cidStr = null;
		List<String> row = new ArrayList<String>();
		for (int pos = 1; pos < excelDataList.size(); pos++) {
			row = excelDataList.get(pos);// 得到当前数据进行验证操作
			if (row != null && row.size() > 0) {
				StringBuffer errorMsg = new StringBuffer();
				// 校验通道号
				try {
					cidStr = Objects.toString(row.get(0), "").trim();
				} catch (Exception e) {
					cidStr = "";
				}
				try {
					cid = Integer.valueOf(cidStr);
					if (cid < 0 || cid > 999999999) {
						errorMsg.append("通道号格式错误;");
					}
				} catch (Exception e) {
					errorMsg.append("通道号格式错误;");
				}
				
				// 校验关键字
				try {
					keyword = Objects.toString(row.get(1), "").trim();
				} catch (Exception e) {
					keyword = "";
				}
				if (StringUtils.isBlank(keyword)) {
					errorMsg.append("关键字为空;");
				}
				
				// 校验备注
				try {
					remarks = Objects.toString(row.get(2), "").trim();
				} catch (Exception e) {
					remarks = "";
				}
				if (!StringUtils.isBlank(remarks) && remarks.length() > 128) {
					errorMsg.append("备注超长;");
				}
				
				// 校验后的数据合法的行保存在legalDataList中，非法的行保存在illegalDataList中
				Map<String, Object> validateRow = new HashMap<>();
				
				if (errorMsg.length() > 1) {
					validateRow.put("cid", cidStr);
					validateRow.put("keyword", keyword);
					validateRow.put("remarks", remarks);
					validateRow.put("reason", errorMsg);
					illegalDataList.add(validateRow);
					continue;
				}else{
					validateRow.put("cid", cid);
					validateRow.put("keyword", keyword);
					validateRow.put("remarks", remarks);
					legalDataList.add(validateRow);
				}
			}
		}
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

	private boolean genExcel(List<Map<String, Object>> dataList) {
		String filePath = ConfigUtils.save_path + "/import" + "/通道关键字导入失败列表-userid-" + AuthorityUtils.getLoginUserId()
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
		excel.setTitle("通道关键字导入失败列表");
		excel.addHeader(10, "通道号", "cid");
		excel.addHeader(20, "关键字", "keyword");
		excel.addHeader(20, "备注", "remarks");
		excel.addHeader(100, "原因", "reason");
		excel.setDataList(dataList);
		return ExcelUtils.exportExcel(excel);
	}

	/**
	 * 将excel中的数据按照通道来分组，相同的合并到一起
	 * 
	 * @param keywordsList
	 * @return
	 */
	private Map<String, Object> mergeSameChannelKeywords(Set<Integer> channelIdSet,
			List<Map<String, Object>> keywordsList) {
		HashMap<String, Object> resultData = new HashMap<>();
		List<Integer> deleteData = new ArrayList<>(); // 已经存在的记录，和新的数据合并后需要先删除老的数据
		List<Map<String, Object>> insertData = new ArrayList<>(); // 可以被插入到数据库中的数据
		
		for (Integer channelId : channelIdSet) {
			List<Map<String, Object>> channelKeywordInDb = masterDao.getSearchList("channelKeywords.viewForOne", channelId);
			StringBuffer keywordbuffer = null;
			String remarks = null;

			// 数据库中查询到该通道的关键字信息为空，表示该通道不存在关键字记录
			if (channelKeywordInDb == null || channelKeywordInDb.size() == 0) {
				keywordbuffer = new StringBuffer();
				for (Map<String, Object> rowData : keywordsList) {
					int rowDataChannelId = (int) rowData.get("cid");
					if (rowDataChannelId == channelId) {
						keywordbuffer.append(rowData.get("keyword"));
						keywordbuffer.append("|");
						remarks = (String) rowData.get("remarks");
					}
				}
				String keywords = keywordbuffer.toString();
				if (keywords.length() > 0) {
					keywords = keywords.substring(0, keywords.length() - 1);
				}

				// 过滤关键字中多余的“|”和重复的关键字
				keywords = keywordsFilter(keywords);
				String[] keywordArray = StrUtils.spriltStringByLength(keywords, 4000); // 超过4000拆成多条，其他保持一致
				for (String keyword : keywordArray) {
					Map<String, Object> temp = new HashMap<>();
					temp.put("cid", channelId);
					temp.put("keyword", keyword);
					temp.put("remarks", remarks);
					temp.put("status", 1);
					insertData.add(temp);
				}
			} else {
				int status = (int) channelKeywordInDb.get(0).get("status");
				keywordbuffer = new StringBuffer();
				int id = (int) channelKeywordInDb.get(0).get("id");
				deleteData.add(id);
				keywordbuffer.append(channelKeywordInDb.get(0).get("keyword"));
				for (Map<String, Object> legalMap : keywordsList) {
					int cid = (int) legalMap.get("cid");
					if (cid == channelId) {
						keywordbuffer.append("|").append(legalMap.get("keyword"));
						try {
							remarks = (String) channelKeywordInDb.get(0).get("remarks");
						} catch (Exception e) {
							remarks = null;
						}
					}
				}
				String keywords = keywordbuffer.toString();
				keywords = keywordsFilter(keywords);
				String[] keywordArray = StrUtils.spriltStringByLength(keywords, 4000); // 超过4000拆成多条，其他保持一致
				for (String keyword : keywordArray) {
					Map<String, Object> temp = new HashMap<>();
					temp.put("cid", channelId);
					temp.put("keyword", keyword);
					temp.put("remarks", remarks);
					temp.put("status", status);
					insertData.add(temp);
				}
				// }
			}
		}
		resultData.put("insertData", insertData);
		resultData.put("deleteData", deleteData);
		return resultData;
	}
	
	
	/**
	 * 将excel中的数据按照通道来分组，相同的合并到一起
	 * 
	 * @param importDataList
	 * @return
	 */
	private Map<String, Object> insertImportDataList(List<Map<String, Object>> importDataList) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> illegalDataList = new ArrayList<Map<String, Object>>();
		
		String channelId;
		String keyword;
		String remarks;
		List<String> importKeywordList;
		List<String> illegalKeywordList; // 非法的重复的关键字
		Set<String> legalKeywordSet; // 合法的关键字
		Map<String, Object> illegalData;
		Map<String, Object> legalData;
		boolean hasInsertData = false;
		
		for (Map<String, Object> rowData : importDataList) {
			channelId = Objects.toString(rowData.get("cid"), "");
			keyword = Objects.toString(rowData.get("keyword"), "");
			keyword = keyword.trim().replace("\r\n", "");// 去除回车符
			remarks = Objects.toString(rowData.get("remarks"), "");
			importKeywordList = Arrays.asList(keyword.split("\\|"));
			
			List<Map<String, Object>> channelKeywordInfoExistInDb = masterDao.getSearchList("channelKeywords.queryChannelKeywordByCid", channelId);
			
			String channelKeywordStrInDb = null;
			StringBuilder sb = new StringBuilder();
			if(channelKeywordInfoExistInDb != null && channelKeywordInfoExistInDb.size() > 0){
				for (Map<String, Object> map : channelKeywordInfoExistInDb) {
					if(sb.length() == 0){
						sb.append(map.get("keyword"));
					}else{
						sb.append("|");
						sb.append(map.get("keyword"));
					}
				}
				channelKeywordStrInDb = sb.toString();
			}
			
			illegalKeywordList = new ArrayList<String>();
			legalKeywordSet = new LinkedHashSet<String>();
			
			if (StringUtils.isNotBlank(channelKeywordStrInDb)) {
				// 将该通道已经存在的关键字保存在 channelKeywordSet 中
				Set<String> channelKeywordSet = new HashSet<String>(Arrays.asList(channelKeywordStrInDb.split("\\|")));
					
				for (String kw : importKeywordList) {
					if(StringUtils.isNotBlank(kw)){
						if(channelKeywordSet.contains(kw) || legalKeywordSet.contains(kw)){
							illegalKeywordList.add(kw);
						}else{
							legalKeywordSet.add(kw);
						}
					}
				}
				
				if(illegalKeywordList.size() > 0){
					illegalData = new HashMap<String, Object>();
					illegalData.put("cid", channelId);
					illegalData.put("keyword", StringUtils.join(illegalKeywordList, "|"));
					illegalData.put("remarks", remarks);
					illegalData.put("reason", "关键字已存在");
					illegalDataList.add(illegalData);
				}
			}else{
				for (String kw : importKeywordList) {
					if(StringUtils.isNotBlank(kw)){
						if(legalKeywordSet.contains(kw)){
							illegalKeywordList.add(kw);
						}else{
							legalKeywordSet.add(kw);
						}
					}
				}
				
				if(illegalKeywordList.size() > 0){
					illegalData = new HashMap<String, Object>();
					illegalData.put("cid", channelId);
					illegalData.put("keyword", StringUtils.join(illegalKeywordList, "|"));
					illegalData.put("remarks", remarks);
					illegalData.put("reason", "关键字已存在");
					illegalDataList.add(illegalData);
				}
			}
			
			if(legalKeywordSet.size() > 0){
				String legalKeywordStr = StringUtils.join(legalKeywordSet, "|");
				String mergeChannelKeyword = null;
				if(StringUtils.isNotBlank(channelKeywordStrInDb)){
					mergeChannelKeyword = channelKeywordStrInDb + "|" + legalKeywordStr;
				}else{
					mergeChannelKeyword = legalKeywordStr;
				}
//				String keywordStr = keywordsFilter(mergeChannelKeyword);
				
				masterDao.delete("channelKeywords.delete", channelId);
				String[] keywordArray = StrUtils.spriltStringByLength(mergeChannelKeyword, 4000); // 超过4000拆成多条，其他保持一致
				List<Map<String, Object>> insertDataList = new ArrayList<>();
				for (String kw : keywordArray) {
					legalData = new HashMap<String, Object>();
					legalData.put("cid", channelId);
					legalData.put("keyword", kw);
					legalData.put("remarks", remarks);
					legalData.put("status", 1);
					insertDataList.add(legalData);
				}
				
				masterDao.insert("channelKeywords.batchInsertKeyWord", insertDataList);
				hasInsertData = true;
			}
		}
		
		result.put("illegalDataList", illegalDataList);
		result.put("hasInsertData", hasInsertData);
		return result;
	}

	@Override
	public Map<String, Object> getChannelKeywordsByCid(String cid) {
		return masterDao.getOneInfo("channelKeywords.getChannelKeywordsByCid", cid);
	}

	@Override
	public List<Map<String, Object>> queryExportExcelData(Map<String, String> params) {
		return masterDao.getSearchList("channelKeywords.queryExportExcelData", params);
	}
}
