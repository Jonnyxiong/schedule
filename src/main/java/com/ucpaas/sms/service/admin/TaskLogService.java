package com.ucpaas.sms.service.admin;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 管理中心-任务日志
 * 
 * @author xiejiaan
 */
public interface TaskLogService {

	/**
	 * 查询任务日志
	 * 
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);

	/**
	 * 查看任务日志
	 * 
	 * @param logId
	 * @return
	 */
	Map<String, Object> view(int logId);

}
