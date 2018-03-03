package com.ucpaas.sms.service;

import java.util.Map;

import com.ucpaas.sms.constant.TaskConstant.TaskId;

/**
 * 监控业务
 * 
 * @author xiejiaan
 */
public interface MonitorService {

	/**
	 * 获取当前统计的时间点
	 * 
	 * @param taskId
	 *            任务id
	 * @param startTime
	 *            开始的时间，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	Map<String, Object> getStatTime(TaskId taskId, String startTime);

	/**
	 * 获取当前查询的结束时间（每3分钟统计）
	 * 
	 * @param taskId
	 *            任务id
	 * @param endTime
	 *            结束的时间，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	Map<String, Object> getEndTimeForMinutes(TaskId taskId, String endTime);

	/**
	 * 获取当前查询的结束时间（每日统计）
	 * 
	 * @param taskId
	 *            任务id
	 * @param endTime
	 *            结束的时间，格式：yyyy-MM-dd
	 * @return
	 */
	Map<String, Object> getEndTimeForDay(TaskId taskId, String endTime);

}
