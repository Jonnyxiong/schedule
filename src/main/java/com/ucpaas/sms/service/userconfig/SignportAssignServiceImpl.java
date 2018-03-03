package com.ucpaas.sms.service.userconfig;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;

/**
 * 签名端口分配范围表 t_sms_clientid_signport_assign
 * @author TonyHe
 *
 */
@Service
@Transactional
public class SignportAssignServiceImpl implements SignportAssignService{
	@Autowired
	private MessageMasterDao masterDao;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("signportAssign.query", "signportAssign.queryCount", params);
	}

}
