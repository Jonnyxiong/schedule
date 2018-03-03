package com.ucpaas.sms.action.channel;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.channel.UusdTemplateService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 通道组管理-彩印通道模板信息
 * 
 */
@Controller
@Scope("prototype")
@Results({ 
  @Result(name = "auditResult", location = "/WEB-INF/content/channel/uusdTemplate/auditResult.jsp"),
  @Result(name = "tokenLog", location = "/WEB-INF/content/channel/uusdTemplate/tokenLog.jsp")
})
public class UusdTemplateAction extends BaseAction {

	private static final long serialVersionUID = 794328696170990738L;
	@Autowired
	private UusdTemplateService uusdTemplateService;

	@Action("/uusd/template")
	public String templatePage() {
		Map<String, String> params = StrutsUtils.getFormData();
		if(params != null && params.get("status") != null){
			StrutsUtils.setAttribute("status", params.get("status"));
		}
		page = uusdTemplateService.queryAuditResult(params);
		return "auditResult";
	}
	
	@Action("/uusd/template/auditResult")
	public String auditResult() {
		Map<String, String> params = StrutsUtils.getFormData();
		if(params != null && params.get("search_status") != null){
			StrutsUtils.setAttribute("search_status", params.get("search_status"));
		}
		page = uusdTemplateService.queryAuditResult(params);
		
		return "auditResult";
	}
	
	@Action("/uusd/template/tokenLog")
	public String tokenLog() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = uusdTemplateService.queryTokenLog(params);
		
		return "tokenLog";
	}

}
