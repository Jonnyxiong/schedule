package com.ucpaas.sms.service.alarm;

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
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;

/**
 * 告警管理Service接口实现
 * 
 */
@Service
@Transactional
public class AlarmServiceImpl implements AlarmService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	/**
	 * 分页查询告警条件
	 */
	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("alarm.query", "alarm.queryCount", params);
	}
	
	/**
	 * 保存告警条件
	 */
	@Override
	public Map<String, Object> saveAlarmCondition(Map<String, String> params) {
		LOGGER.debug("保存告警条件信息，添加/修改：" + params);

		Map<String, Object> data = new HashMap<String, Object>();

		List<Map<String, Object>> check = masterDao.getSearchList("alarm.getByChannelid", params);// 查重
		if (check.size() >= 1) {
			data.put("result", "fail");
			data.put("msg", "该通道号已经设置过告警条件，请确认");
			return data;
		}
		String alarm_id = params.get("alarm_id");
		if (StringUtils.isBlank(alarm_id)) {// 添加告警条件
			params.put("status", "0");//新添加的告警条件默认未启用
			int i = masterDao.insert("alarm.insertAlarmCondition", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "添加告警条件成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "添加告警条件失败");
			}
			logService.add(LogType.add,LogEnum.系统配置.getValue(), "系统配置-告警管理：添加告警条件", params, data);

		} else {// 修改警条件信息
			int i = masterDao.update("alarm.updateAlarmCondition", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "修改成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "修改失败");
			}
			logService.add(LogType.update,LogEnum.系统配置.getValue(), "系统配置-告警管理：修改告警条件", params, data);
		}

		return data;
	}

	/**
	 *  根据告警条件ID查询
	 */
	@Override
	public Map<String, Object> getByAlarmId(Long alarmId) {
		return masterDao.getOneInfo("alarm.getByAlarmId", alarmId);
	}

	/**
	 * 修改告警条件状态：0关闭，1启用，2已删除
	 */
	@Override
	public Map<String, Object> updateStatus(Integer alarmId, Integer status) {
		Map<String, Object> data = new HashMap<String, Object>();
		String msg;
		LogType logType = null ;
		switch (status.intValue()) {
		case 0:
			msg = "关闭";
			logType = LogType.update;
			break;
		case 1:
			msg = "启用";
			logType = LogType.update;
			break;
		case 2:
			msg = "删除";
			logType = LogType.delete;
			break;
		default:
			data.put("result", "fail");
			data.put("msg", "状态不正确，操作失败");
			return data;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("alarm_id", alarmId);
		params.put("status", status);
		int i = masterDao.update("alarm.updateStatus", params);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", msg + "成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "告警条件不存在，" + msg + "失败");
		}
		logService.add(logType, LogEnum.系统配置.getValue(),"系统配置-告警管理："+msg+"告警条件状态", params, data);
		return data;
	}

}
