package com.ucpaas.sms.service.admin;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 管理中心-任务管理
 * 
 * @author xiejiaan
 */
public interface TaskService {

	/**
	 * 查询任务
	 * 
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);

	/**
	 * 查看任务
	 * 
	 * @param taskId
	 * @return
	 */
	Map<String, Object> view(int taskId);

	/**
	 * 保存任务，包括添加、修改
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> save(Map<String, String> params);

	/**
	 * 修改任务状态：关闭、启用、删除
	 * 
	 * @param taskId
	 * @param status
	 * @return
	 */
	Map<String, Object> updateStatus(int taskId, int status);

}
