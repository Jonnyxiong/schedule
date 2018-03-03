package com.ucpaas.sms.action.userconfig;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.userconfig.ExtendportAssignService;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 用户端口分配范围表t_sms_extendport_assign
 * @author TonyHe
 *
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/userconfig/extendportAssign/query.jsp")})
public class ExtendportAssignAction extends BaseAction{
	private static final long serialVersionUID = 3692572446157762229L;
	@Autowired
	private ExtendportAssignService extendportAssignService ;
	@Action("/extendportAssign/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = extendportAssignService.query(params);
		return "query";
	}
	
}
