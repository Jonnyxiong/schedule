package com.ucpaas.sms.service.account;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 账户锁定管理
 */
public interface AccountLockInfoService {
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);
	
	/**
	 * 更新账户状态
	 * @param params
	 * @return
	 */
	Map<String, Object> unlockAccount(Map<String, String> params);
	
}
