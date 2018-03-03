package com.ucpaas.sms.action.sysconf;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.SmsCountService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-条数配置（废弃）
 * 
 * @author liulu
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/smscount/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/smscount/add.jsp") })
public class SmsCountAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	private SmsCountService smsCountService;

	@Resource
	public void setSmsCountService(SmsCountService smsCountService) {
		this.smsCountService = smsCountService;
	}

	@Action("/smscount/query")
	public String query() {
		Map<String, String> formData = StrutsUtils.getFormData();
		if(formData.get("tableid")==null){
			formData.put("tableid", "bychannel");
		}
		page = smsCountService.query(formData);
		StrutsUtils.setAttribute("tableid", formData.get("tableid"));
		return "query";
	}

//	@Action("/smscount/add")
//	public String add() {
//		Map<String, String> formData = StrutsUtils.getFormData();
//		StrutsUtils.setAttribute("tableid", formData.get("tableid"));
//		
//		return "add";
//	}
//	
	@Action("/smscount/add")
	public String add() {
		String id = StrutsUtils.getParameterTrim("id");
		String tableid= StrutsUtils.getParameterTrim("tableid");
		if (StringUtils.isNotBlank(id)) {
			data = smsCountService.view(tableid,id);
		} else {
			data = new HashMap<String, Object>();
		}
		data.put("tableid", tableid);
		return "add";
	}

	@Action("/smscount/save")
	public void save() {
		data = smsCountService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/smscount/deleteSmscount")
	public String deleteSmscount() {
		smsCountService.deleteSmscount(StrutsUtils.getParameterTrim("id"),StrutsUtils.getParameterTrim("tableid"));
		return query();
	}
}
