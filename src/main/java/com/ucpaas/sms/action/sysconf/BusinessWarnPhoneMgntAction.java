package com.ucpaas.sms.action.sysconf;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.BusineesWarnPhoneMgntService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-业务预警人员号码管理
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/businessWarnPhoneMgnt/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/businessWarnPhoneMgnt/edit.jsp")})
public class BusinessWarnPhoneMgntAction extends BaseAction {

	private static final long serialVersionUID = -5771488278800185569L;
	
	@Autowired
	private BusineesWarnPhoneMgntService businessWarnPhoneMgntService;
	
	@Action("/sysConf/businessWarnPhoneMgnt/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
				
		page = businessWarnPhoneMgntService.query(params);
		StrutsUtils.setAttribute("clientid_search", params.get("clientid"));
		StrutsUtils.setAttribute("phone_search", params.get("phone"));
		return "query";
	}	
	
	@Action("/sysConf/businessWarnPhoneMgnt/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {
			data = businessWarnPhoneMgntService.editView(id);
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/sysConf/businessWarnPhoneMgnt/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = businessWarnPhoneMgntService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sysConf/businessWarnPhoneMgnt/delete")
	public void delete() {
		data = businessWarnPhoneMgntService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
}
