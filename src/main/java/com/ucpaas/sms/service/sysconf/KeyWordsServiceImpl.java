package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;

/**
 * 系统设置-关键字配置
 * 
 * @author zenglb
 */
@Service
@Transactional
public class KeyWordsServiceImpl implements KeyWordsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(KeyWordsServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("keywords.query", "keywords.queryCount", params);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存关键字，添加：" + params);
		String keyWords = params.get("keywords");
		String remarks=params.get("remarks");

		Map<String, Object> data = new HashMap<String, Object>();
		if (null == keyWords) {
			data.put("result", "fail");
			data.put("msg", "关键字不能为空");
			return data;
		}
		String[] ks = keyWords.split(";");
		int i = 0;
		String kw = null;
		for (String s : ks) {
			kw = s.trim();
			if (StringUtils.isNotBlank(kw)) {
				params.put("keyword",kw);
				params.put("remarks",remarks);
				i += masterDao.insert("keywords.insertKeyWord", params);
			}
		}
		data.put("result", "success");
		data.put("msg", "成功添加 " + i + " 个关键字");
		logService.add(LogType.add,LogEnum.系统配置.getValue(), "系统配置-关键字：添加关键字", params, data);
		return data;
	}

	@Override
	public int deleteKeyword(String id) {
		logService.add(LogType.delete,LogEnum.系统配置.getValue(), "系统配置-关键字：删除关键字", id);
		return masterDao.delete("keywords.deleteKeyword", id);
	}
	
	@Override
	public Map<String, Object> importExcel(File importExcel, String fileType) {
		Map<String, Object> data = new HashMap<String, Object>();
		String fileSavePath = ConfigUtils.save_path;
		String fileName = "批量导入关键字.xls";
		LOGGER.debug("批量导入关键字：" + fileName);
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
        
        //过滤数据；数据入库
        String kw = null;
        String remarks="";
        Map<String,Object> inserts=new HashedMap();
        int importNum = 0;
        int totalNum = 0;
        List<String> rowRecordList = new ArrayList<String>();
        if(importDataList != null && importDataList.size() > 1){
        	for(int pos = 1; pos < importDataList.size(); pos++){
        		totalNum++;
        		rowRecordList = importDataList.get(pos);
        		if(rowRecordList != null && rowRecordList.size() > 0){
        			kw = rowRecordList.get(0).trim();
        			if(rowRecordList.size()>1){
						remarks=rowRecordList.get(1);
					}else {
						remarks="";
					}
        			if(StringUtils.isNotBlank(kw)){
						inserts.put("keyword",kw);
						inserts.put("remarks",remarks);
        				importNum += masterDao.insert("keywords.insertKeyWord", inserts);
        			}
        		}
            }
        }
        
        int ignoreNum = totalNum - importNum;
		data.put("result", "success");
		StringBuffer msg = new StringBuffer();
		if(importNum != importDataList.size()-1){
			msg.append("Excel中共");
			msg.append(totalNum);
			msg.append("个关键字; ");
			msg.append("成功添加");
			msg.append(importNum);
			msg.append("个关键字; ");
			msg.append(ignoreNum);
			msg.append("个关键字已存在或格式不合法");
			data.put("msg", msg);
		}else{
			msg.append("Excel中共");
			msg.append(totalNum);
			msg.append("个关键字; ");
			msg.append("成功添加");
			msg.append(importNum);
			msg.append("个关键字");
			data.put("msg", msg);
		}
		logService.add(LogType.add,LogEnum.系统配置.getValue(), "系统配置-关键字：批量导入关键字Excel", fileName);
		return data;
	}

	@Override
	public List<Map<String, Object>> queryExportExcelData(Map<String, String> params) {
		return masterDao.getSearchList("keywords.queryExportExcelData", params);
	}
	
}
