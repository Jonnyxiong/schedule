package com.ucpaas.sms.service.alarm;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 告警管理Service接口
 * 
 */
public interface AlarmService {

	/**
	 * 分页查询告警条件
	 * 
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);
	
	/**
	 * 保存告警条件
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> saveAlarmCondition(Map<String, String> params);
	
	/**
	 * 根据告警条件ID查询
	 * @param alarmId
	 * @return
	 */
	Map<String, Object> getByAlarmId(Long alarmId);
	
	/**
	 * 修改告警条件状态：0关闭，1启用，2已删除
	 * @param alarmId
	 * @param status
	 * @return
	 */
	Map<String, Object> updateStatus(Integer alarmId, Integer status);
}
