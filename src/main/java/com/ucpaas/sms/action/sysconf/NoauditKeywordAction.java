package com.ucpaas.sms.action.sysconf;


import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.NoauditKeywordService;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 
 * @author TonyHe
 *	用户面审关键词管理
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/noauditKeyword/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/noauditKeyword/edit.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/noauditKeyword/edit.jsp")})
public class NoauditKeywordAction extends BaseAction{
	private static final long serialVersionUID = 7361826869080333170L;

	private NoauditKeywordService noauditKeywordService;

	@Resource
	public void setNoauditKeywordService(NoauditKeywordService noauditKeywordService) {
		this.noauditKeywordService = noauditKeywordService;
	}

	@Action("/noauditKeyword/query")
	public String query() {
		Map<String, String> formData = StrutsUtils.getFormData();
		page = noauditKeywordService.query(formData);
		StrutsUtils.setAttribute("status", formData.get("status"));
		return "query";
	}

	@Action("/noauditKeyword/add")
	public String add() {
		String clientid = StrutsUtils.getParameterTrim("clientidold");
		data = noauditKeywordService.view(clientid);
		return "add";
	}

	@Action("/noauditKeyword/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = noauditKeywordService.save(params );
		StrutsUtils.renderJson(data);
	}
	
	@Action("/noauditKeyword/update")
	public void update() {
		Map<String, String> params = StrutsUtils.getFormData();
		System.out.println(params);
		data = noauditKeywordService.update(params);
		StrutsUtils.renderJson(data);
	}

	@Action("/noauditKeyword/delete")
	public void deleteKeyword() {
		String clientid = StrutsUtils.getParameterTrim("clientid");
		data = noauditKeywordService.delete(clientid);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/noauditKeyword/updateStatus")
	public void updateStatus() {
		String clientid = StrutsUtils.getParameter("clientid");
		String status = StrutsUtils.getParameter("status");
		data = noauditKeywordService.updateStatus(clientid, status);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/noauditKeyword/getKeywordsByCid")
	public void getKeywordsByCid() {
		String clientid = StrutsUtils.getParameterTrim("clientid");
		data = noauditKeywordService.getKeywordsByCid(clientid);
		StrutsUtils.renderJson(data);
	}
	
}
