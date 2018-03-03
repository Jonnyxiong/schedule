package com.ucpaas.sms.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.TaskConstant.TaskId;
import com.ucpaas.sms.dao.MessageMasterDao;

/**
 * 监控业务
 * 
 * @author xiejiaan
 */
@Service
@Transactional
public class MonitorServiceImpl implements MonitorService {
	@Autowired
	private MessageMasterDao masterDao;

	@Override
	public Map<String, Object> getStatTime(TaskId taskId, String startTime) {
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("task_id", taskId.getValue());
		sqlParams.put("start_time", startTime);

		if (taskId == TaskId.task_id_3) {// 每3分钟处理短信记录区域统计表
			sqlParams.put("delay_minute", 3);
		} else {
			sqlParams.put("delay_minute", 9);
		}
		return masterDao.getOneInfo("monitor.getStatTime", sqlParams);
	}

	@Override
	public Map<String, Object> getEndTimeForMinutes(TaskId taskId, String endTime) {
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("task_id", taskId.getValue());
		sqlParams.put("end_time", endTime);

		if (taskId == TaskId.task_id_3) {
			sqlParams.put("delay_minute", masterDao.getOneInfo("monitor.getSmsDelay", null));
		} else {
			sqlParams.put("delay_minute", 9);
		}
		return masterDao.getOneInfo("monitor.getEndTimeForMinutes", sqlParams);
	}

	@Override
	public Map<String, Object> getEndTimeForDay(TaskId taskId, String endTime) {
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("task_id", taskId.getValue());
		sqlParams.put("end_time", endTime);
		return masterDao.getOneInfo("monitor.getEndTimeForDay", sqlParams);
	}

}
