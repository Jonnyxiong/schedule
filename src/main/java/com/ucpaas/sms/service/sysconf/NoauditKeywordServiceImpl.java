package com.ucpaas.sms.service.sysconf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.ucpaas.sms.util.StrUtils;

/**
 * 
 * @author TonyHe
 *	用户免审关键字管理
 */
@Service
@Transactional
public class NoauditKeywordServiceImpl implements NoauditKeywordService{
	private static final Logger LOGGER = LoggerFactory.getLogger(NoauditKeywordServiceImpl.class);
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;
	
	public PageContainer query(Map<String, String> params) {
//		if("0".equals(params.get("status") )){
//			params.put("status", null);
//		}
		PageContainer page = masterDao.getSearchPage("noauditKeyword.query", "noauditKeyword.queryCount", params);
		return page ;
	}
	
	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("系统配置-用户免审关键字管理：添加：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		int res = 0;
		
		String newKeywords = params.get("keyword");
		// 创建时如果clientid已经存在关键字则将老的关键字和新的关键字拼接保存
		List<String> checkInfo = masterDao.selectList("noauditKeyword.getKeywordsByClientId", params);
		StringBuffer existKeywordsInDb = new StringBuffer();
		if(checkInfo != null && checkInfo.size() > 0){
			for (String kw : checkInfo) {
				existKeywordsInDb.append(kw);
			}

			// 删除老的关键字
			masterDao.delete("noauditKeyword.delete", params.get("clientid"));
		}

		if(existKeywordsInDb.length() > 0)
			newKeywords = existKeywordsInDb.toString() + "|" + newKeywords;

		String keywords = keywordsFilter(newKeywords);
		String [] keywordArray = StrUtils.spriltStringByLength(keywords, 4000);
		int successFlag = 0;
		for (String keyword : keywordArray) {
			params.put("keyword", keyword);// 拆分的多条信息只有关键字信息不一样，其余保持一致
			res = masterDao.insert("noauditKeyword.insert", params);
			successFlag += res;
		}
		
		if(successFlag == keywordArray.length){
			data.put("clientid", params.get("clientid"));
			data.put("result", "success");
			data.put("msg", "成功添加用户免审关键字");
		}else{
			data.put("result", "fail");
			data.put("msg", "添加用户免审关键字失败");
		}
		
		logService.add(LogType.add,LogEnum.系统配置.getValue(), "系统配置-用户免审关键字管理：添加用户免审关键字", params, data);
		return data;
	}

	/**
	 * 过滤关键字中多余的“|”和重复的关键字
	 * @param keyword
	 * @return 用“|”分隔的关键字字符串
	 */
	private String keywordsFilter(String keyword){
		String result;
		keyword = Objects.toString(keyword, "");
		keyword = keyword.trim().replace("\n", "");// 去除回车符
		String [] keywordsArray = keyword.split("\\|");
		Map<String, Object> hash = new HashMap<String, Object>();
		
		// 利用hashMap的特性去重
		List<String> tempList = new ArrayList<String>();
		for (String kw : keywordsArray) {
			if(StringUtils.isNotBlank(kw)){
				if(hash.containsKey(kw)){
					
				}else{
					hash.put(kw, true);
					tempList.add(kw);
				}
			}
		}
		
		// 从hashMap中将关键字（key）取出并用“|”拼接
		result = StringUtils.join(tempList, "|");
		
		return result;
	}
	
	@Override
	public Map<String, Object> update(Map<String, String> params) {
		LOGGER.debug("系统配置-用户免审关键字管理：修改：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		
		Map<String, Object> delMap = new HashMap<>();
		delMap.put("clientid", params.get("clientidCopy"));
		masterDao.delete("noauditKeyword.delete", delMap);
		
		String newKeywords = params.get("keyword");
		Map<String, Object> checkInfo = masterDao.getOneInfo("noauditKeyword.getKeywordsByClientId", params);
		if(null != checkInfo){
			String existKeywords = String.valueOf(checkInfo.get("keyword"));
			newKeywords = existKeywords + "|" + newKeywords;
			masterDao.delete("noauditKeyword.delete", params.get("clientid"));
		}
		String keywords = keywordsFilter(newKeywords);
		String [] keywordArray = StrUtils.spriltStringByLength(keywords, 4000);
		
		int res = 0 ;
		int successFlag = 0;
		for (String keyword : keywordArray) {
			params.put("keyword", keyword);// 拆分的多条信息只有关键字信息不一样，其余保持一致
			res = masterDao.insert("noauditKeyword.insert", params);
			successFlag += res;
		}
		
		if(successFlag == keywordArray.length){
			data.put("clientid", params.get("clientid"));
			data.put("result", "success");
			data.put("msg", "修改成功");
		}else{
			data.put("result", "fail");
			data.put("msg", "修改失败");
		}
			
		logService.add(LogType.update, LogEnum.系统配置.getValue(), "系统配置-用户免审关键字：修改用户免审关键字：",params, data);
		return data;
	}

	
	public Map<String, Object> view(String clientid) {
		List<Map<String, Object>> result = masterDao.getSearchList("noauditKeyword.view", clientid);
		Map<String, Object> noauditKeyword = new HashMap<String, Object>();
		if( null != result && result.size() > 0 ){
			noauditKeyword.putAll(result.get(0));
		}
		// 将拆分的通道关键字信息合并（长度超过4000的关键字信息会拆分存储）
		String keyword = "";
		for (Map<String, Object> map : result) {
			keyword += map.get("keyword");
		}
		noauditKeyword.put("keyword", keyword);
		return noauditKeyword ;
	}
	
	public Map<String, Object> delete(String clientid) {
		int res = masterDao.delete("noauditKeyword.delete", clientid);
		Map<String, Object> data = new HashMap<String, Object>();
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功删除用户免审关键字");
		}else{
			data.put("result", "fail");
			data.put("msg", "删除用户免审关键字失败");
		}
		logService.add(LogType.delete, LogEnum.系统配置.getValue(), "系统配置-用户免审关键字：删除用户免审关键字：id", clientid, data);
		return data;
	}

	@Override
	public Map<String, Object> updateStatus(String clientid, String status) {
		Map<String, Object> map = new HashMap<>();
		map.put("clientid", clientid);
		map.put("status", status);
		int res = masterDao.update("noauditKeyword.updateStatus", map);
		map = new HashMap<>();
		if(res > 0){
			map.put("result", "success");
			map.put("msg", "成功修改状态");
		}else{
			map.put("result", "fail");
			map.put("msg", "修改状态失败");
		}
		return map;
	}
	
	@Override
	public Map<String, Object> getKeywordsByCid(String clientid) {
		return masterDao.getOneInfo("noauditKeyword.getKeywordsByClientId", clientid);
	}
}
