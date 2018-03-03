package com.ucpaas.sms.service.channel;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;

@Service
@Transactional
public class UusdTemplateServiceImpl implements UusdTemplateService {

	@Autowired
	private MessageMasterDao masterDao;

	@Override
	public PageContainer queryAuditResult(Map<String, String> params) {
		return masterDao.getSearchPage("uusdTemplate.queryAuditResult", "uusdTemplate.queryAuditResultCount", params);
	}


	@Override
	public PageContainer queryTokenLog(Map<String, String> params) {
		return masterDao.getSearchPage("uusdTemplate.queryTokenLog", "uusdTemplate.queryTokenLogCount", params);
	}


}
