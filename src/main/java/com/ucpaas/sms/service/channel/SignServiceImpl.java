package com.ucpaas.sms.service.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

/**
 * 用户短信通道管理-签名扩展号管理
 * 
 * @author 刘路
 */
@Service
@Transactional
public class SignServiceImpl implements SignService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("sign.query", "sign.queryCount", params);
	}

	@Override
	public Map<String, Object> view(int logId) {
		return masterDao.getOneInfo("sign.view", logId);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存签名扩展号，添加/修改：" + params);

		Map<String, Object> result = new HashMap<String, Object>();

		Map<String,Object> param=new HashedMap();
		param.putAll(params);
//		List ids1=new ArrayList();
//		if(params.get("id")!=null){
//
//			String[] ids=params.get("id").split(",");
//
//			for (int i = 0; i < ids.length; i++) {
//				ids1.add(ids[i]);
//			}
//			param.put("id",ids1);
//		}

		//先删除带出记录
		if(StringUtils.isNotBlank(params.get("nid"))){
			String[] nids=params.get("nid").split(",");
			Map<String,String> param1=new HashedMap();
			for (int i = 0; i < nids.length; i++) {
				param1.put("id",nids[i]);
				this.delete(param1);
			}
		}



		String id = params.get("id");
		if (StringUtils.isBlank(id)) {// 添加
			/*// 查重: 在同一channelid下，clientid唯一 
			Map<String, Object> check2 = masterDao.getOneInfo("sign.checkSave2", params);
			if (check2 != null) {
				result.put("result", "fail");
				result.put("msg", "该通道下已经分配该用户的扩展号，不能重复分配");
				return result;
			}*/

			String[] clientids=params.get("clientid").split(",");
			for (int j = 0; j < clientids.length; j++) {
				String clientid= clientids[j];
				param.put("clientid",clientid);

				Map<String, Object> check = masterDao.getOneInfo("sign.checkSave1", param);// 查重

				if (check != null) {
					result.put("result", "fail");
					result.put("msg", "该通道下“短信签名”、“扩展号”、“子账号”只能一对一存在，请重新输入");
					return result;
				}


				int i = masterDao.insert("sign.insertSign", param);
				if (i > 0) {
					result.put("result", "success");
					result.put("msg", "添加成功");
				} else {
					result.put("result", "fail");
					result.put("msg", "添加失败");
				}
				logService.add(LogType.add, LogEnum.通道管理.getValue(),"用户通道配置-签名扩展号管理：添加签名扩展号", params, result);
			}


		} else {// 修改


			//先删除编辑记录
			String[] ids=params.get("id").split(",");
			for (int i = 0; i < ids.length; i++) {
				params.put("id",ids[i]);
				this.delete(params);
			}
			//再新增记录



			String[] clientids=params.get("clientid").split(",");
			for (int j = 0; j < clientids.length; j++) {
				String clientid = clientids[j];
				param.put("clientid", clientid);
				//param.put("id","");
//				if(ids.length<j){
//					param.put("id",ids[j]);
//
//				}

				Map<String, Object> check = masterDao.getOneInfo("sign.checkSave1", param);// 查重

				if (check != null) {
					result.put("result", "fail");
					result.put("msg", "该通道下“短信签名”、“扩展号”、“子账号”只能一对一存在，请重新输入");
					return result;
				}


					int i = masterDao.insert("sign.insertSign", param);
					if (i > 0) {
						result.put("result", "success");
						result.put("msg", "编辑成功");
					//	add++;
					} else {
						result.put("result", "fail");
						result.put("msg", "编辑失败");
					}
				}
//				result.put("msg","修改成功"+update+"个,新增"+add+"个。");
				logService.add(LogType.update, LogEnum.通道管理.getValue(), "用户通道配置-签名扩展号管理：修改签名扩展号", params, result);
			}
	//		}
		return result;
	}

	@Override
	public Map<String, Object> delete(Map<String, String> formData) {
		String[] ids=formData.get("id").split(",");
		Map<String, Object> data = new HashMap<String, Object>();
		for (int i = 0; i < ids.length; i++) {

			formData.put("id",ids[i]);
			masterDao.delete("sign.delete", formData);
			data.put("result", "success");
			data.put("msg", "删除成功");
			logService.add(LogType.delete, LogEnum.用户配置.getValue(),"用户通道配置-签名扩展号管理：删除签名扩展号", formData);
		}
		return data;
	}
	
	@Override
	public Map<String, Object> checkSignExist(Map<String, String> formData) {
		Map<String,Object> param=new HashedMap();
		param.putAll(formData);
		if(formData.get("id")!=null){
			String[] ids=formData.get("id").split(",");
			List ids1=new ArrayList();
			for (int i = 0; i < ids.length; i++) {
				ids1.add(ids[i]);
			}
			param.put("id",ids1);
		}

		return masterDao.getOneInfo("sign.checkSignExist", param);
	}
	
	@Override
	public Map<String, Object> checkAppendIdExist(Map<String, String> formData) {
		Map<String,Object> param=new HashedMap();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer ban=new StringBuffer();
		param.putAll(formData);
//		if(formData.get("id")!=null){
//			String[] ids=formData.get("id").split(",");
//			String[] clientids=formData.get("clientIds[]").split(",");
//
//
//			for (int j = 0; j < clientids.length; j++) {
//				String clientid = clientids[j];
//				param.put("clientid", clientid);
//				param.put("id",ids[j]);
//
//			Map<String, Object> check=masterDao.getOneInfo("sign.checkAppendIdExist", param);
//				if(check!=null){
//					if(ban!=null){
//						ban.append(",").append(clientid);
//					}else {
//						ban.append(clientid);
//					}
//				}
//			}
//		}else {
		if(formData.get("id")!=null){
			List<String> ids1=new ArrayList<>();
			String[] ids=formData.get("id").split(",");
			for (int i = 0; i < ids.length; i++) {
				ids1.add(ids[i]);
			}
			formData.put("id",ids1.toString());

		}
			String[] clientids=formData.get("clientIds[]").split(",");
			for (int j = 0; j < clientids.length; j++) {
				String clientid = clientids[j];
				param.put("clientid", clientid);
				Map<String, Object> check=masterDao.getOneInfo("sign.checkAppendIdExist", param);
				if(check!=null){
					if(ban!=null){
						ban.append(",").append(clientid);
					}else {
						ban.append(clientid);
					}
				}
			}
		//}



		String ban1=ban.toString();

		if(StringUtils.isNotBlank(ban1)){
		//if(ban.toString()!=null||!(ban.toString()).equals("")){
			result.put("msg","账号:"+ban.toString()+"已绑定其他扩展号");
			result.put("checked",true);
		}else {
			result.put("checked",false);
		}

		return result;
	}
	
	@Override
	public Map<String, Object> channelAppendIdCheck1(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> checkResult = masterDao.getOneInfo("sign.channelAppendIdCheck1", params);
		if(checkResult != null){
			result.put("success", false);
			return result;
		}
		result.put("success", true);
		return result;
	}
	
	@Override
	public Map<String, Object> channelAppendIdCheck2(Map<String, String> params) {

		Map<String,Object> param=new HashedMap();
		param.putAll(params);
//		if(params.get("id")!=null){
//			String[] ids=params.get("id").split(",");
//			List ids1=new ArrayList();
//			for (int i = 0; i < ids.length; i++) {
//				ids1.add(ids[i]);
//			}
//			param.put("id",ids1);
//		}

		// 针对固签有扩展通道：在同一channelid+sign下，appendid唯一且前缀不能等于其它appendid，如：同一channelid下，存在appendid=1，则其它appendid不能以1开头。
		boolean appendIdUsable = true;
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String,Object>> datalist = masterDao.selectList("sign.searchAlldata", param);
		Map<String,Object> signs=new HashedMap();
		if(datalist.size()>0){
			for (Map<String, Object> map : datalist) {
				if(signs==null||signs.isEmpty()){
					signs.putAll(map);
				}else {
					signs.put("id", signs.get("id").toString() + "," + map.get("id").toString());
					signs.put("clientid", signs.get("clientid").toString() + "," + map.get("clientid").toString());
				}
			}

			result.put("datalist",signs);
		}



		
		Map<String, Object> channelTypeMap = masterDao.getOneInfo("sign.getAllChannelTypeByChannelId", params);
		if(channelTypeMap != null && channelTypeMap.get("channeltype") != null){
			Integer channeltype = (Integer) channelTypeMap.get("channeltype");
			// 固签无自扩展,不做校验
			if(!channeltype.equals(2)){
				result.put("appendIdUsable", appendIdUsable);
				return result;
			}
		}




//
//		String[] ids=params.get("id").split(",");
//		Map<String,Object> param=new HashedMap();
//		param.putAll(params);
//		List ids1=new ArrayList();
//		for (int i = 0; i < ids.length; i++) {
//			ids1.add(ids[i]);
//		}
//		param.put("id",ids1);


		List<String> appendIdList = masterDao.selectList("sign.getAllAppendIdByChannelId", param);
		
		String appendId = params.get("appendId");
		for (String string : appendIdList) {

			if(appendId.equals(string)){
				break;
			}else{
				Pattern pattern = Pattern.compile("^(?!" + appendId + ").*");
				Matcher matcher = pattern.matcher(string);
				if(!matcher.matches()){
					LOGGER.debug("自签通道用户端口校验失败，与端口：" + appendId + "冲突");
					appendIdUsable = false;
					break;
				}

				pattern = Pattern.compile("^(?!" + string + ").*");
				matcher = pattern.matcher(appendId);
				if(!matcher.matches()){
					LOGGER.debug("自签通道用户端口校验失败，与端口：" + appendId + "冲突");
					appendIdUsable = false;
					break;
				}
			}

		}
		
		result.put("appendIdUsable", appendIdUsable);
		return result;
	}
	
	@Override
	public Map<String, Object> channelAppendIdCheck3(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> checkResult = masterDao.getOneInfo("sign.channelAppendIdCheck3", params);
		if(checkResult != null){
			result.put("success", false);
			return result;
		}
		result.put("success", true);
		return result;
	}

}
