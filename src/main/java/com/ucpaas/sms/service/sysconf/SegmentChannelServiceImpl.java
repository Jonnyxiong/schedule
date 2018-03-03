package com.ucpaas.sms.service.sysconf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.util.web.AuthorityUtils;

@Service
@Transactional
public class SegmentChannelServiceImpl implements SegmentChannelService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SegmentChannelServiceImpl.class);
	
	@Autowired
	private MessageMasterDao messageMasterDao;

	@Override
	public PageContainer query(Map<String, String> params) {
		return messageMasterDao.getSearchPage("segmentChannel.query", "segmentChannel.queryCount", params);
	}

	@Override
	public Map<String, Object> editView(String id) {
		return messageMasterDao.getOneInfo("segmentChannel.getOneInfo", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int saveNum;
		String id = Objects.toString(params.get("id"), "");
		
		// 强制路由类型，0：国内手机号码路由，1：国内手机号段路由，2：国外手机号段路由
		String routeType = params.get("route_type");
		if(routeType.equals("0") || routeType.equals("2")){
			// 国内手机号码路由和国外手机号段路由时 phone_segment唯一
			int check = messageMasterDao.getOneInfo("segmentChannel.saveCheckRouteType0AndRouteType2", params);
			if(check != 0){
				data.put("result", "failure");
				data.put("msg", "添加失败，当前手机号码/号段已经存在通道路由规则");
				return data;
			}
		}
		if(routeType.equals("1")){
			// 国内手机号段路由时（area_id + operatorstype）唯一
			int check = messageMasterDao.getOneInfo("segmentChannel.saveCheckRouteType1", params);
			if(check != 0){
				data.put("result", "failure");
				data.put("msg", "添加失败，当前地区加通道运营商条件已经存在通道路由规则");
				return data;
			}
		}
		
		if(StringUtils.isBlank(id)){// 新建
			saveNum = messageMasterDao.insert("segmentChannel.insert", params);
			LOGGER.debug("新建强制路由通道管理记录，插入条数 = {}, 操作管理员 = {}", saveNum, AuthorityUtils.getLoginRealName());
		}else{// 更新
			saveNum = messageMasterDao.update("segmentChannel.update", params);
			LOGGER.debug("更新强制路由通道管理记录，更新条数 = {}, 操作管理员 = {}", saveNum, AuthorityUtils.getLoginRealName());
		}
		
		if(saveNum == 1){
			data.put("result", "success");
			data.put("id", params.get("id"));
			data.put("msg", "保存成功");
			return data;
		}else{
			data.put("result", "fail");
			data.put("msg", "保存失败");
			return data;
		}
	}

	@Override
	public Map<String, Object> delete(Map<String, String> fromData) {
		Map<String, Object> result = new HashMap<String, Object>();
		int delNum = messageMasterDao.delete("segmentChannel.delete", fromData);
		if(delNum == 1){
			result.put("result", "success");
			result.put("msg", "成功删除记录");
			LOGGER.debug("删除强制路由通道管理记录，fromData = {}，操作管理员 = {}", fromData, AuthorityUtils.getLoginRealName());
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		return result;
	}

	@Override
	public Map<String, Object> updateStatus(Map<String, String> formData) {
		Map<String, Object> data = new HashMap<String, Object>();
		int updateNum = 0;
		updateNum = messageMasterDao.update("segmentChannel.updateStatus", formData);
		if(updateNum > 0){
			data.put("result", "success");
			data.put("msg", "更新成功");
		}else{
			data.put("result", "fail");
			data.put("msg", "保存失败");
		}
		return data;
	}

	@Override
	public Map<String, Object> queryOperatorstypeByChannelId(String channelId) {
		Map<String, Object> result = new HashMap<String, Object>();
		String operatorstype = messageMasterDao.getOneInfo("segmentChannel.queryOperatorstypeByChannelId", channelId);
		if(operatorstype != null){
			result.put("result", "success");
			result.put("msg", "查询通道运营商类型成功");
			result.put("operatorstype", operatorstype);
		}else{
			result.put("result", "fail");
			result.put("msg", "查询通道运营商类型失败");
		}
		
		return result;
	}

}
