package com.ucpaas.sms.action.userconfig;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.userconfig.SignportAssignService;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 签名端口分配范围表 t_sms_clientid_signport_assign
 * @author TonyHe
 *
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/userconfig/signportAssign/query.jsp") })
public class SignportAssignAction extends BaseAction{
	private static final long serialVersionUID = -1384273603193781018L;
	
	@Autowired
	private SignportAssignService signportAssignService ;
	
	@Action("/signportAssign/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = signportAssignService.query(params);
		return "query";
	}
	
}
