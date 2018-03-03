package com.ucpaas.sms.service.sysconf;

import java.util.HashMap;
import java.util.Map;

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
 * 系统设置-条数配置
 * 
 * @author liulu
 */
@Service
@Transactional
public class SmsCountServiceImpl implements SmsCountService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmsCountServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		if ("bychannel".equals(params.get("tableid"))) {
			return masterDao.getSearchPage("smscount.query_channelid", "smscount.queryCount_channelid", params);
		} else {

			return masterDao.getSearchPage("smscount.query_sid", "smscount.queryCount_sid", params);
		}
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存条数配置，添加：" + params);
		String smscount = params.get("smscount");
		Map<String, Object> data = new HashMap<String, Object>();
		if (null == smscount) {
			data.put("result", "fail");
			data.put("msg", "短信条数不为空");
			return data;
		}

		String id = params.get("id");
		// 判断是插入哪个表
		String tableid = params.get("tableid");
		if (StringUtils.isBlank(id)) {// 添加

			int i = 0;
			if ("bychannel".equals(tableid)) {
				Map<String, Object> check = masterDao.getOneInfo("smscount.checkSave_channelid", params);// 查重
				if (check != null) {
					data.put("result", "fail");
					data.put("msg", "通道配置已存在");
					return data;
				}
				i = masterDao.insert("smscount.insert_smscount_channelid", params);
			} else if ("bysid".equals(tableid)) {
				Map<String, Object> check = masterDao.getOneInfo("smscount.checkSave_sid", params);// 查重
				if (check != null) {
					data.put("result", "fail");
					data.put("msg", "用户配置已存在");
					return data;
				}
				i = masterDao.insert("smscount.insert_smscount_sid", params);
			}
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "添加成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
			}
			logService.add(LogType.add, LogEnum.系统配置.getValue(),"系统配置-条数配置：添加配置", params, data);
		} else {// 修改管理员

			int i = 0;
			if ("bychannel".equals(tableid)) {
				Map<String, Object> check = masterDao.getOneInfo("smscount.checkSave_channelid", params);// 查重
				if (check != null) {
					data.put("result", "fail");
					data.put("msg", "通道配置已存在");
					return data;
				}
				i = masterDao.update("smscount.update_smscount_channelid", params);
			} else if ("bysid".equals(tableid)) {
				Map<String, Object> check = masterDao.getOneInfo("smscount.checkSave_sid", params);// 查重
				if (check != null) {
					data.put("result", "fail");
					data.put("msg", "用户配置已存在");
					return data;
				}
				i = masterDao.update("smscount.update_smscount_sid", params);
			}
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "修改成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "修改失败");
			}
			logService.add(LogType.update, LogEnum.系统配置.getValue(),"系统配置-条数配置：修改配置", params, data);
		}

		return data;
	}

	@Override
	public int deleteSmscount(String id,String tableid) {
		if("bysid".equals(tableid)){
		
			return masterDao.delete("smscount.deleteSmscount_sid", id);
		}else{
			
			return masterDao.delete("smscount.deleteSmscount_channelid", id);
		}
	}

	@Override
	public Map<String, Object> view(String tableid, String id) {
		if ("bysid".equals(tableid)) {
			return masterDao.getOneInfo("smscount.view_sid", id);
		} else {
			return masterDao.getOneInfo("smscount.view_channelid", id);
		}

	}

}
