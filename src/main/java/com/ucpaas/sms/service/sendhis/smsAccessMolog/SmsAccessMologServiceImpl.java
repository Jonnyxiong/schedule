package com.ucpaas.sms.service.sendhis.smsAccessMolog;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.AccessSlaveDao;
import com.ucpaas.sms.model.PageContainer;
@Service
@Transactional
public class SmsAccessMologServiceImpl implements SmsAccessMologService{

	@Autowired
	private AccessSlaveDao messageStatSlaveDao;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		return messageStatSlaveDao.getSearchPage("smsAccessMolog.query", "smsAccessMolog.queryCount", params);
	}

}
