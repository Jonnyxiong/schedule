package com.ucpaas.sms.service.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;

/**
 * 管理中心-任务管理
 * 
 * @author xiejiaan
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("task.query", "task.queryCount", params);
	}

	@Override
	public Map<String, Object> view(int taskId) {
		return masterDao.getOneInfo("task.view", taskId);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (masterDao.getSearchSize("task.check", params) > 0) {// 查重
			data.put("result", "fail");
			data.put("msg", "任务名称已被使用，请重新输入");
			return data;
		}
//		if("5".equals(params.get("task_type"))){
//			params.put("procedure_name", "p_create_table_day");
//		}
		String taskId = params.get("task_id");
		if (StringUtils.isBlank(taskId)) {// 添加任务
			int i = masterDao.insert("task.insert", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "添加成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "添加任务失败");
			}
			logService.add(LogType.add, LogEnum.管理中心.getValue(),"管理中心-任务管理：添加任务", params, data);

		} else {// 修改任务
			int i = masterDao.update("task.update", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "修改成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "任务不存在，修改失败");
			}
			logService.add(LogType.update,LogEnum.管理中心.getValue(), "管理中心-任务管理：修改任务", params, data);
		}
		return data;
	}

	@Override
	public Map<String, Object> updateStatus(int taskId, int status) {
		Map<String, Object> data = new HashMap<String, Object>();
		String msg;
		LogType logType = null ;
		switch (status) {
		case 0:
			msg = "关闭";
			logType = logType.update;
			break;
		case 1:
			msg = "启用";
			logType = logType.update;
			break;
		case 3:
			msg = "删除";
			logType = logType.delete;
			break;
		default:
			data.put("result", "fail");
			data.put("msg", "状态不正确，操作失败");
			return data;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("task_id", taskId);
		params.put("status", status);
		int i = masterDao.update("task.updateStatus", params);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", msg + "成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "任务不存在，" + msg + "失败");
		}
		logService.add(logType,LogEnum.管理中心.getValue(), "管理中心-任务管理：修改任务状态", params, data);
		return data;
	}

}
