package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;
@Service
@Transactional
public class SmsOprateLogServiceImpl implements SmsOprateLogService{
	@Autowired
	private MessageMasterDao masterDao;
	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("smsOprateLog.query", "smsOprateLog.queryCount", params);
	}
	
}
