/**
 * 
 */
package com.ucpaas.sms.action.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.account.AccountLockInfoService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 *  账户锁定管理
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/account/accountLockInfo.jsp"),
		@Result(name = "edit", location="/WEB-INF/content/account/edit.jsp")})

public class AccountLockInfoAction extends BaseAction {
	
	private static final long serialVersionUID = -1786281023180892850L;
	@Autowired
	private AccountLockInfoService accountLockInfoService;
	
	@Action("/account/lockInfo/query")
	public String query() {
		page = accountLockInfoService.query(StrutsUtils.getFormData());
		return "query";
	}
	
	@Action("/account/lockInfo/unlockAccount")
	public void updateStatus() {
		String clientid = StrutsUtils.getParameterTrim("clientid");
		String status = StrutsUtils.getParameterTrim("status");
		String operator = StrutsUtils.getParameterTrim("operator");
		Map<String, String> params = new HashMap<String, String>();
		params.put("clientid", clientid);
		params.put("status", status);
		params.put("operator", operator);
		
		data = accountLockInfoService.unlockAccount(params);
		StrutsUtils.renderJson(data);
	}
}
