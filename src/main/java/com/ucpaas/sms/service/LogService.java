package com.ucpaas.sms.service;

import com.ucpaas.sms.constant.LogConstant.LogType;

/**
 * 日志业务
 * 
 * @author xiejiaan
 */
public interface LogService {

	/**
	 * 添加操作日志
	 * 
	 * @param logType
	 *            日志类型
	 * @param desc
	 *            日志信息
	 * @return 是否添加成功
	 */
	boolean add(LogType logType, String moduleName ,Object... desc);

}
