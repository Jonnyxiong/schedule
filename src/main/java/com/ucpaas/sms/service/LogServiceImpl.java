package com.ucpaas.sms.service;

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
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 日志业务
 * 
 * @author xiejiaan
 */
@Service
@Transactional
public class LogServiceImpl implements LogService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
	@Autowired
	private MessageMasterDao masterDao;

	@Override
	public boolean add(LogType logType,String moduleName, Object... desc) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", AuthorityUtils.getLoginUserId());
		params.put("page_id", moduleName);
		params.put("page_url", StrutsUtils.getRequestURI());
		params.put("op_type", logType.getValue());
		params.put("op_desc", StringUtils.join(desc, ", "));
		params.put("ip", StrutsUtils.getClientIP());

		int i = masterDao.insert("common.addLog", params);
		if (i > 0) {
			LOGGER.debug("添加操作日志成功：" + params);
			return true;

		} else {
			LOGGER.error("添加操作日志失败：" + params);
			return false;
		}
	}
}
