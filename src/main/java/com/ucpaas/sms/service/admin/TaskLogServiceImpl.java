package com.ucpaas.sms.service.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;

/**
 * 管理中心-任务日志
 * 
 * @author xiejiaan
 */
@Service
@Transactional
public class TaskLogServiceImpl implements TaskLogService {
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("taskLog.query", "taskLog.queryCount", params);
	}

	@Override
	public Map<String, Object> view(int logId) {
		return masterDao.getOneInfo("taskLog.view", logId);
	}
}
