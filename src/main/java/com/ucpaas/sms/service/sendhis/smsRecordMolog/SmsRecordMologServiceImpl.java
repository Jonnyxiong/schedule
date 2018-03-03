package com.ucpaas.sms.service.sendhis.smsRecordMolog;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.RecordSlaveDao;
import com.ucpaas.sms.model.PageContainer;
@Service
@Transactional
public class SmsRecordMologServiceImpl implements SmsRecordMologService{

	@Autowired
	private RecordSlaveDao recordSlaveDao;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		PageContainer page = recordSlaveDao.getSearchPage("smsRecordMolog.query", "smsRecordMolog.queryCount", params);
		return page;
	}

}
