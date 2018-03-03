package com.ucpaas.sms.service.userconfig;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;

/**
 * 用户端口分配范围表t_sms_extendport_assign
 * @author TonyHe
 *
 */
@Service
@Transactional
public class ExtendportAssignServiceImpl implements ExtendportAssignService{
	@Autowired
	private MessageMasterDao masterDao;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("extendportAssign.query", "extendportAssign.queryCount", params);
	}
	
}
