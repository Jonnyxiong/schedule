package com.ucpaas.sms.service.account;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.BaseDao;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.web.AuthorityUtils;

/**
 *  账户锁定管理
 *
 */
@Service
@Transactional
public class AccountLockInfoServiceImpl implements AccountLockInfoService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountLockInfoServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("accountLockInfo.query", "accountLockInfo.queryCount", params);
	}
	
	@Override
	public Map<String, Object> unlockAccount(Map<String, String> params){
		LOGGER.debug("账户管理-账户锁定管理，解锁账户：" + params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		// 更新用户登录状态表 t_sms_account_login_status
		int res = masterDao.update("accountLockInfo.updateAccountLockStatus", params);
		if(res > 0){
			data.put("result", "success");
			data.put("msg", "成功解锁账户");
		}else{
			data.put("result", "fail");
			data.put("msg", "解锁账户失败");
		}
		
		// 更新账户表 t_sms_account
		res = masterDao.update("accountLockInfo.unlockAccount", params);
		
		logService.add(LogType.update, LogEnum.账户管理.getValue(),"账户管理-账户锁定管理，更新状态：", params, data);
		return data;
	}
	
}
