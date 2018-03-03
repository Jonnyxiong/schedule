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
import com.ucpaas.sms.service.sysconf.BusineesWarnMgntService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-业务预警阈值管理
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/businessWarnMgnt/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/businessWarnMgnt/edit.jsp")})
public class BusinessWarnMgntAction extends BaseAction {

	private static final long serialVersionUID = -7991304365948663522L;
	
	@Autowired
	private BusineesWarnMgntService businessWarnMgntService;
	
	@Action("/sysConf/businessWarnMgnt/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
				
		page = businessWarnMgntService.query(params);
		StrutsUtils.setAttribute("clientid_search", params.get("clientid"));
		StrutsUtils.setAttribute("warn_type_search", params.get("warn_type"));
		return "query";
	}	
	
	@Action("/sysConf/businessWarnMgnt/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {
			data = businessWarnMgntService.editView(id);
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/sysConf/businessWarnMgnt/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = businessWarnMgntService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sysConf/businessWarnMgnt/delete")
	public void delete() {
		data = businessWarnMgntService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
}
