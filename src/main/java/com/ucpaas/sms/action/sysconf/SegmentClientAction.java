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
import com.ucpaas.sms.service.sysconf.SegmentClientService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-强制路由客户管理
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/segmentClient/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/segmentClient/edit.jsp")})
public class SegmentClientAction extends BaseAction {

	private static final long serialVersionUID = 1861646055080924517L;
	
	@Autowired
	private SegmentClientService segmentClientService;
	
	@Action("/sysConf/segmentClient/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
				
		page = segmentClientService.query(params);
		StrutsUtils.setAttribute("client_code_search", params.get("client_code"));
		StrutsUtils.setAttribute("clientid_search", params.get("clientid"));
		return "query";
	}	
	
	@Action("/sysConf/segmentClient/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {
			data = segmentClientService.editView(id);
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/sysConf/segmentClient/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = segmentClientService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sysConf/segmentClient/delete")
	public void delete() {
		data = segmentClientService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sysConf/segmentClient/updateStatus")
	public void updateStatus() {
		data = segmentClientService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
}
