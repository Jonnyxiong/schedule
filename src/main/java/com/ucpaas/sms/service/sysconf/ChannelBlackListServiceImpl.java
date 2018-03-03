package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.exception.SmspBusinessException;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.RedisUtils;
import com.ucpaas.sms.util.RegexUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;

/**
 * 系统设置-通道黑名单配置
 * 
 * @author oylx
 */
@Service
@Transactional
public class ChannelBlackListServiceImpl implements ChannelBlackListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelBlackListServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("channelBlackList.query", "channelBlackList.queryCount", params);
	}
	
	@Override
	public Map<String, Object> view(String id) {
		return masterDao.getOneInfo("channelBlackList.view", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params, String id) {
		LOGGER.debug("系统配置-通道黑名单：添加/修改：" + params);
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
		if(StringUtils.isBlank(id)){// 新建
			// 检查通道对应黑名单是否已存在
			Map<String, Object> checkInfo = masterDao.getOneInfo("channelBlackList.blackListCheck", params);
			if(checkInfo != null){
				data.put("result", "fail");
				data.put("msg", "黑名单已经存在，请重新输入");
				return data;
			}else{
				res = masterDao.insert("channelBlackList.insert", params);
				
				String phone = params.get("phone");
				String channelId = params.get("cid");
				String setResult = setInRedis(phone, channelId);
				if(StringUtils.isBlank(setResult)){
					LOGGER.warn("保存通道黑名单到Redis时系统错误, 黑名单={}", phone);
					throw new SmspBusinessException("保存通道黑名单到Redis时系统错误，请联系管理员");
				}
			}
			
			if(res > 0){
				data.put("result", "success");
				data.put("msg", "成功添加 " + res + " 个黑名单");
			}else{
				data.put("result", "fail");
				data.put("msg", "添加黑名单失败");
			}
		}else{
			msg = "修改";
			logType = LogType.update;
			// 更新时如果修改了通道号或者手机号码则进行查重
			if(null != params.get("cid_bak") && null != params.get("cid") && null != params.get("phone_bak") && null != params.get("phone")){
				if(!params.get("cid_bak").equals(params.get("cid")) || !params.get("phone_bak").equals(params.get("phone"))){
					Map<String, Object> checkInfo = masterDao.getOneInfo("channelBlackList.blackListCheck", params);
					if(checkInfo != null){
						data.put("result", "fail");
						data.put("msg", "黑名单已经存在，请重新输入");
						return data;
					}
				}
			}
			res = masterDao.insert("channelBlackList.update", params);
			if(res > 0){
				data.put("result", "success");
				data.put("msg", "成功修改 " + res + " 个黑名单");
				
				// 更新的时候先删除Redis中老的
				String phoneOld = params.get("phone_bak");
				String channelIdOld = params.get("cid_bak");
				String delResutl = deleteInRedis(phoneOld, channelIdOld);
				if(StringUtils.isBlank(delResutl)){
					LOGGER.warn("删除Redis中通道关键字时系统错误，关键字 = {}", phoneOld);
					throw new SmspBusinessException("删除Redis中通道关键字时系统错误，请联系管理员");
				}
				
				// 更新的时候保存新的通道关键字到Redis中
				String phone = params.get("phone");
				String channelId = params.get("cid");
				String setResult = setInRedis(phone, channelId);
				if(StringUtils.isBlank(setResult)){
					LOGGER.warn("保存通道黑名单到Redis时系统错误, 黑名单={}", phone);
					throw new SmspBusinessException("保存通道黑名单到Redis时系统错误，请联系管理员");
				}
				
				
			}else{
				data.put("result", "fail");
				data.put("msg", "修改通道黑名单失败");
			}
		}
		
		logService.add(logType, LogEnum.系统配置.getValue(), "系统配置-通道黑名单："+msg+"黑名单", params, data);
		return data;
	}

	@Override
	public Map<String, Object> delete(String id, String phone, String channelId) {
		int res = masterDao.update("channelBlackList.delete", id);
		Map<String, Object> data = new HashMap<String, Object>();
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功删除黑名单");
			
			// 删除Redis中的通道关键字
			String delResutl = deleteInRedis(phone, channelId);
			if(StringUtils.isBlank(delResutl)){
				LOGGER.warn("删除Redis中通道关键字时系统错误，关键字 = {}", phone);
				throw new SmspBusinessException("删除Redis中通道关键字时系统错误，请联系管理员");
			}
		}else{
			data.put("result", "fail");
			data.put("msg", "删除黑名单失败");
		}
		logService.add(LogType.delete, LogEnum.系统配置.getValue(), "系统配置-通道黑名单：删除黑名单：id", id, data);
		return data;
	}
	
	@Override
	public synchronized Map<String, Object>  importExcel(File importExcel, String fileType) {
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
			data.put("msg", "导入文件格式错误，目前支持.xls格式(97-2003)，请使用模板");
			return data;
		}
		if(importExcel.length() > 1148576L){
			data.put("result", "fail");
			data.put("msg", "您选择的文件大于1M,请将excel拆分后重新上传");
			return data;
		}
		//上传文件、取出数据、删除文件
		FileUtils.upload(fileSavePath, fileName, importExcel);
        List<List<String>> importDataList = ExcelUtils.importExcel(fileSavePath + "/" + fileName);
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
        FileUtils.delete(fileSavePath + "/" + fileName);
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
						errorMsg.append("通道号格式错误;");
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
						errorMsg.append("excel中该黑名单重复，只取第一条.");
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
				row = masterDao.insert("channelBlackList.insertBlack", iMap);
				String key = Objects.toString(iMap.get("phone"), "");
				String value = Objects.toString(iMap.get("cid"), "");
				String setResult = setInRedis(key, value);
				if(StringUtils.isBlank(setResult)){
					LOGGER.warn("保存通道黑名单到Redis时系统错误, 黑名单={}", phone);
					throw new SmspBusinessException("保存通道黑名单到Redis时系统错误，请联系管理员");
				}
				
			} catch (Exception e) {
				iMap.put("reason", "系统错误，未能导入");
				illegalList.add(iMap);
			}
        	if(row > 0){
        		importNum = importNum + row;
        	}else{
        		iMap.put("reason", "数据库中已经存在该数据。");
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
		data.put("status", 0);
		if(importNum != totalNum){
			
			msg.append("Excel中共");
			msg.append(totalNum);
			msg.append("个黑名单; ");
			msg.append("成功添加");
			msg.append(importNum);
			msg.append("个黑名单; ");
			msg.append(ignoreNum);
			msg.append("个黑名单已存在或格式不合法!");
			data.put("msg", msg);
			genExcel(illegalList);
		}else{
			msg.append("Excel中共");
			msg.append(totalNum);
			msg.append("个黑名单; ");
			msg.append("成功添加");
			msg.append(importNum);
			msg.append("个黑名单");
			data.put("msg", msg);
		}
		logService.add(LogType.add, LogEnum.系统配置.getValue(),"系统配置-通道黑名单：批量导入黑名单Excel", fileName);
		return data;
        
        
	}
	
	private boolean genExcel(List<Map<String, Object>> dataList){
		String filePath = ConfigUtils.save_path +"/import"+ "/通道黑名单导入失败列表-userid-"+AuthorityUtils.getLoginUserId()+".xls";
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
	
	/**
	 * 保存黑名单到Redis中
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
}
