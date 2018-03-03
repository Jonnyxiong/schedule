package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.BaseDao;
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
 * 系统设置-通道白名单配置
 * 
 * @author oylx
 */
@Service
@Transactional
public class ChannelWhiteListServiceImpl implements ChannelWhiteListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelWhiteListServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("channelWhiteList.query", "channelWhiteList.queryCount", params);
	}

	@Override
	public Map<String, Object> view(String id) {
		return masterDao.getOneInfo("channelWhiteList.view", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params, String id) {
		LOGGER.debug("系统配置-通道白名单：添加/修改：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		int res = 0;
		String msg = "增加" ;
		LogType logType = LogType.add;
		String v = params.get("phone");
		if(StringUtils.isEmpty(v)|| !RegexUtils.isMobile(v)){
			data.put("result", "fail");
			data.put("msg", "手机号码错误");
			return data;
		}
		if (StringUtils.isBlank(id)) {
			// 新建查重
			Map<String, Object> checkInfo = masterDao.getOneInfo("channelWhiteList.whiteListCheck", params);
			if (checkInfo != null) {
				data.put("result", "fail");
				data.put("msg", "白名单已经存在，请重新输入");
				return data;
			} else {
				res = masterDao.insert("channelWhiteList.insert", params);
			}

			if (res > 0) {
				data.put("result", "success");
				data.put("msg", "成功添加 " + res + " 个白名单");
			} else {
				data.put("result", "fail");
				data.put("msg", "添加白名单失败");
			}
		} else {
			msg = "修改";
			logType = LogType.update;
			// 更新时如果修改了通道号或者手机号码则进行查重
			if (null != params.get("cid_bak") && null != params.get("cid") && null != params.get("phone_bak")
					&& null != params.get("phone")) {
				if (!params.get("cid_bak").equals(params.get("cid"))
						|| !params.get("phone_bak").equals(params.get("phone"))) {
					Map<String, Object> checkInfo = masterDao.getOneInfo("channelWhiteList.whiteListCheck", params);
					if (checkInfo != null) {
						data.put("result", "fail");
						data.put("msg", "白名单已经存在，请重新输入");
						return data;
					}
				}
			}
			res = masterDao.update("channelWhiteList.update", params);
			if (res > 0) {
				data.put("result", "success");
				data.put("msg", "成功修改 " + res + " 个白名单");
			} else {
				data.put("result", "fail");
				data.put("msg", "修改通道白名单失败");
			}
		}
		logService.add(logType, LogEnum.系统配置.getValue(),"系统配置-通道白名单："+msg+"白名单", params, data);
		return data;
	}

	@Override
	public Map<String, Object> delete(String id) {
		int res = masterDao.delete("channelWhiteList.delete", id);
		Map<String, Object> data = new HashMap<String, Object>();
		if (res > 0) {
			data.put("result", "success");
			data.put("msg", "成功删除白名单");
		} else {
			data.put("result", "fail");
			data.put("msg", "删除白名单失败");
		}
		logService.add(LogType.delete, LogEnum.系统配置.getValue(), "系统配置-通道白名单：删除白名单：id", id, data);
		return data;
	}

//	@Override 备用方法，批量导入效率高，但在数据库存量很大的时候，数据校验将有内存泄漏的风险
//	public Map<String, Object> importExcelbk(File importExcel, String fileType) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		String fileSavePath = ConfigUtils.save_path;
//		String fileName = "批量导入白名单.xls";
//		LOGGER.debug("批量导入白名单：" + fileName);
//		// 导入文件校验
//		if (StringUtils.isBlank(fileType)) {
//			data.put("result", "fail");
//			data.put("msg", "请先选择导入Excel");
//			return data;
//		}
//
//		if(importExcel.length() > 6291456L){
//			data.put("result", "fail");
//			data.put("msg", "您选择的文件大于6M,请将excel拆分后重新上传");
//			return data;
//		}
//		 if(!("application/vnd.ms-excel".equalsIgnoreCase(fileType))){
//		 data.put("result", "fail");
//		 data.put("msg", "导入文件格式错误，目前支持.xls格式(97-2003)，请使用模板");
//		 return data;
//		 }
//		// 上传文件、取出数据、删除文件
//		FileUtils.upload(fileSavePath, fileName, importExcel);
//        List<List<String>> importDataList = ExcelUtils.importExcel(fileSavePath + "/" + fileName);
//        FileUtils.delete(fileSavePath + "/" + fileName);
//        List<List<String>> datalist = new ArrayList<>();
//        for (int i = 0 ; i < importDataList.size() ; i++) {
//        	if(!(importDataList.get(i).size() == 0)){
//				datalist.add(importDataList.get(i));
//			}
//        }
//        if(datalist.size() > 65536){
//			data.put("result", "fail");
//			data.put("msg", "您选择的excel中数据记录大于65536条，请您拆分后上传");
//			return data;
//		}
//        
//        Integer cid = null ;
//        String phone = null;
//        String remarks = null ;
//        String cidStr = null ;
//        int importNum = 0;
//        int totalNum = 0;
//        List<String> rowRecordList = new ArrayList<String>();
//        List<Map<String,Object>> illegalList = new ArrayList<Map<String,Object>>();
//        List<Map<String, Object>> legalList = new ArrayList<Map<String, Object>>();//合法的
//        List<String> addedList = new ArrayList<String>();
//        if(datalist != null && datalist.size() > 1){
//        	totalNum = datalist.size()-1;
//        	List<Map<String, Object>> result = dao.getSearchList("channelWhiteList.viewForLock", null);
//        	List<String> cidList = new ArrayList<>();
//        	for (Map<String, Object> map : result) {
//        		String existStr = map.get("cid")+"/"+ map.get("phone");
//        		cidList.add(existStr);
//			}
//        	for(int pos = 1; pos < datalist.size(); pos++){
//        		rowRecordList = datalist.get(pos);//得到当前数据进行验证操作
//        		if(rowRecordList != null && rowRecordList.size() > 0){
//					try {
//						cidStr = null ; //初始化三个值，防止出错时读到上一轮循环的内容
//						remarks = null ;
//						phone = null ;
//						cidStr = rowRecordList.get(0).trim();
//						cid = Integer.valueOf(cidStr);
//						phone = rowRecordList.get(1).trim().replace("\n", "");
//						remarks = rowRecordList.get(2).trim();
//					} catch (Exception e) {
//						Map<String, Object> errorMap = new HashMap<>();
//						errorMap.put("cid", cidStr);
//						errorMap.put("phone", phone);
//						errorMap.put("remarks", remarks);
//						errorMap.put("reason", "数据格式错误，请确认excel是否是按照要求填写");
////						errorMap.put("rowNm", pos+1);
//						illegalList.add(errorMap);
//						LOGGER.info("将excel中的数据转换失败");
//						continue ;
//					}
//					
//					Map<String, Object> map = new HashMap<String, Object>();
//					if(!RegexUtils.isMobile(phone)){
//						map.put("cid", cidStr);
//						map.put("remarks", remarks);
//						map.put("phone", phone);
//						map.put("reason", "手机号码不正确");
////						map.put("rowNm", pos+1);//真实行号
//						illegalList.add(map);
//						continue ;
//					}
//					
//					if(cid < 0 || cid > 999999999){
//						map.put("cid", cidStr);
//						map.put("remarks", remarks);
//						map.put("phone", phone);
//						map.put("reason", "通道值不正确");
////						map.put("rowNm", pos+1);//真实行号
//						illegalList.add(map);
//						continue ;
//					}
//					
//					if(addedList.contains(cid+"/"+phone)){ //需要考虑本身excel中数据的重复问题
//						Map<String, Object> errorMap = new HashMap<>();
//						errorMap.put("cid", cidStr);
//						errorMap.put("phone", phone);
//						errorMap.put("remarks", remarks);
//						errorMap.put("reason", "excel中该白名单重复，只取第一条.");
////						errorMap.put("rowNm", pos+1);
//						illegalList.add(errorMap);
//						LOGGER.info("excel中该白名单重复，只取第一条.");
//						continue ;
//					}
//					addedList.add(cid+"/"+phone);
//					if(cidList.contains(cid+"/"+phone)){
//						Map<String, Object> errorMap = new HashMap<>();
//						errorMap.put("cid", cidStr);
//						errorMap.put("phone", phone);
//						errorMap.put("remarks", remarks);
//						errorMap.put("reason", "白名单已经存在该关键词");
////						errorMap.put("rowNm", pos+1);
//						illegalList.add(errorMap);
//						LOGGER.info("白名单已经存在该关键词");
//						continue ;
//					}
//					
//					map.put("cid", cidStr);
//					map.put("remarks", remarks);
//					map.put("phone", phone);
////					map.put("rowNm", pos+1);
//					legalList.add(map);
//    		}
//        }
//        int size = legalList.size();
//    	if(size > 0){
//    		int batch = size / 1000 ;
//    		if(batch == 0 ){
//    			importNum = dao.insert("channelWhiteList.batchInsertWhiteList", legalList);
//    		}else{
//    			int importNumTemp = 0 ;
//        		for (int i = 0; i <= batch; i++) {
//        			LOGGER.info("\n\n批量导入，切割List，第："+(i+1)+"次");
//        			int fromIndex = i*1000 ;
//        			int toIndex = 0 ;
//        			if(i == batch){
//        				toIndex = i*1000+size % 1000 ;
//        			}else{
//        				toIndex = i*1000+1000 ;
//        			}
//        			List<Map<String, Object>> batchList = legalList.subList(fromIndex, toIndex);
//        			try {
//						importNumTemp = dao.insert("channelWhiteList.batchInsertWhiteList", batchList);
//					} catch (Exception e) {
//						for (Map<String, Object> map : batchList) {
//							map.put("reason", "系统错误");//数据在入库过程中由于某些未知原因造成的错误
//							illegalList.add(map);
//						}
//					}
//        			importNum += importNumTemp;
//    			}
//    		}
//    	}else{
//    		importNum = 0 ;
//    	}
//    }else{
//    	data.put("result", "fail");
//		data.put("msg", "excel中没有数据");
//		return data;
//    }
//        int ignoreNum = totalNum - importNum;
//		data.put("result", "success");
//		StringBuffer msg = new StringBuffer();
//		if(importNum != totalNum){
//			data.put("status", 1);
//			msg.append("Excel中共");
//			msg.append(totalNum);
//			msg.append("个白名单; ");
//			msg.append("成功添加");
//			msg.append(importNum);
//			msg.append("个白名单; ");
//			msg.append(ignoreNum);
//			msg.append("个白名单已存在或格式不合法（excel中重复记录以第一条为准）");
//			data.put("msg", msg);
//			data.put("illegal", illegalList);
//			if(genExcel(illegalList)){
//				data.put("status", 1);
//			}
//		}else{
//			data.put("status", 0);
//			msg.append("Excel中共");
//			msg.append(totalNum);
//			msg.append("个白名单; ");
//			msg.append("成功添加");
//			msg.append(importNum);
//			msg.append("个白名单");
//			data.put("msg", msg);
//		}
//		logService.add(LogType.add, "系统配置-通道黑名单：批量导入通道关键字Excel", fileName);
//		return data;
//	}
	
	/**
	 * 此方法每条数据单独发送sql，发送sql的频率很高，但数据校验的时候不会出现上面那个方法的风险
	 */
	@Override
	public synchronized Map<String, Object> importExcel(File importExcel, String fileType) {
		Long startTime = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		String fileSavePath = ConfigUtils.save_path;
		String fileName = "批量导入白名单.xls";
		LOGGER.debug("批量导入白名单：" + fileName);
		// 导入文件校验
		if (StringUtils.isBlank(fileType)) {
			data.put("result", "fail");
			data.put("msg", "请先选择导入Excel");
			return data;
		}

		if(importExcel.length() > 1148576L){
			data.put("result", "fail");
			data.put("msg", "您选择的文件大于1M,请将excel拆分后重新上传");
			return data;
		}
		 if(!("application/vnd.ms-excel".equalsIgnoreCase(fileType))){
		 data.put("result", "fail");
		 data.put("msg", "导入文件格式错误，目前支持.xls格式(97-2003)，请使用模板");
		 return data;
		 }
		// 上传文件、取出数据、删除文件
		FileUtils.upload(fileSavePath, fileName, importExcel);
        List<List<String>> importDataList = ExcelUtils.importExcel(fileSavePath + "/" + fileName);
        FileUtils.delete(fileSavePath + "/" + fileName);
        List<List<String>> datalist = new ArrayList<>();
        for (int i = 0 ; i < importDataList.size() ; i++) {
        	List<String> temp = importDataList.get(i);
        	boolean isNotEmpty = false ;
        	for (String str : temp) {
        		if(!StringUtils.isEmpty(str)){
        			isNotEmpty = isNotEmpty | true ;
        		}
			}
        	if(isNotEmpty){
        		datalist.add(temp);
        	}
        }
        if(datalist.size() > 15000){
			data.put("result", "fail");
			data.put("msg", "您选择的excel中数据记录大于15000条，请您拆分后上传");
			return data;
		}
        
        Integer cid = null ;
        String phone = null;
        String remarks = null ;
        String cidStr = null ;
        int importNum = 0;
        int totalNum = 0;
        List<String> rowRecordList = new ArrayList<String>();
        List<Map<String,Object>> illegalList = new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> legalList = new ArrayList<Map<String, Object>>();//合法的
        List<String> addedList = new ArrayList<String>();
        if(datalist != null && datalist.size() > 1){
        	totalNum = datalist.size()-1;
        	for(int pos = 1; pos < datalist.size(); pos++){
        		rowRecordList = datalist.get(pos);//得到当前数据进行验证操作
        		if(rowRecordList != null && rowRecordList.size() > 0){
        			Map<String, Object> errorMap = new HashMap<>();
        			StringBuffer errorMsg = new StringBuffer() ;
        			remarks = null ;
        			phone = null ;
        			try {
						phone = rowRecordList.get(1).trim();
					} catch (Exception e2) {
						phone = null ;
					}
        			
        			try {
						remarks = rowRecordList.get(2).trim();
					} catch (Exception e2) {
						remarks = null ;
					}
					try {
						cidStr = null ; //初始化三个值，防止出错时读到上一轮循环的内容
						cidStr = rowRecordList.get(0).trim();
						cid = Integer.valueOf(cidStr);
					} catch (Exception e) {
						cid = -1 ;
						errorMsg.append("通道号格式错误");
					}
					
					Map<String, Object> map = new HashMap<String, Object>();
					if(!RegexUtils.isMobile(phone)){
						errorMsg.append("手机号码不正确;");
					}
					
					if(cid < 0 || cid > 999999999){
						errorMsg.append("通道号不正确;");
					}
					
					if(StringUtils.isEmpty(remarks)){
						errorMsg.append("备注不能为空;");
					}
					
					if(!StringUtils.isEmpty(remarks) && remarks.length() > 128){
						errorMsg.append("备注超长;");
					}
					
					if(addedList.contains(cid+"/"+phone)){ //需要考虑本身excel中数据的重复问题
						errorMsg.append("excel中该白名单重复，只取第一条.");
					}
					addedList.add(cidStr+"/"+phone);
					if(errorMsg.length()> 1){
						errorMap.put("cid", cidStr);
						errorMap.put("phone", phone);
						errorMap.put("remarks", remarks);
						errorMap.put("reason", errorMsg);
						illegalList.add(errorMap);
						continue;
					}
					map.put("cid", cidStr);
					map.put("remarks", remarks);
					map.put("phone", phone);
					legalList.add(map);
    		}
        }
        for (Map<String, Object> iMap : legalList) {
            	int row = 0;
				try {
					row = masterDao.insert("channelWhiteList.insertWhite", iMap);
				} catch (Exception e) {
					iMap.put("reason", "系统错误，未能导入");
					illegalList.add(iMap);
				}
            	if(row > 0){
            		importNum = importNum + row;
            	}else{
            		iMap.put("reason", "该白名单在系统中已经存在");
            		illegalList.add(iMap);
            	}
    		}
    }else{
    	data.put("result", "fail");
		data.put("msg", "excel中没有数据");
		return data;
    }
        int ignoreNum = totalNum - importNum;
		data.put("result", "success");
		StringBuffer msg = new StringBuffer();
		if(importNum != totalNum){
			msg.append("Excel中共");
			msg.append(totalNum);
			msg.append("个白名单; ");
			msg.append("成功添加");
			msg.append(importNum);
			msg.append("个白名单; ");
			msg.append(ignoreNum);
			msg.append("个白名单已存在或格式不合法!");
			data.put("msg", msg);
			genExcel(illegalList);
		}else{
			msg.append("Excel中共");
			msg.append(totalNum);
			msg.append("个白名单; ");
			msg.append("成功添加");
			msg.append(importNum);
			msg.append("个白名单");
			data.put("msg", msg);
		}
		Long endTime = System.currentTimeMillis();
		Long totalTime = endTime - startTime ;
		logService.add(LogType.add, LogEnum.系统配置.getValue(),"系统配置-通道白名单：批量导入白名单Excel,耗时："+totalTime+"毫秒", fileName);
		return data;
	}
	
	private boolean genExcel(List<Map<String, Object>> dataList){
		String filePath = ConfigUtils.save_path +"/import"+ "/白名单导入失败列表-userid-"+AuthorityUtils.getLoginUserId()+".xls";
		File dir = new File(ConfigUtils.save_path +"/import");
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		
		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("格式不符合的信息表");
		excel.addHeader(10, "通道号", "cid");
		excel.addHeader(20, "手机号码", "phone");
		excel.addHeader(20, "备注", "remarks");
		excel.addHeader(100, "原因", "reason");
		excel.setDataList(dataList);
		return ExcelUtils.exportExcel(excel);
	}
	
}
