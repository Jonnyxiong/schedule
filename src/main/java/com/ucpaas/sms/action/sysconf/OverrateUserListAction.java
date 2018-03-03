package com.ucpaas.sms.action.sysconf;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.OverrateUserListService;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 
 * @author TonyHe
 *	超频用户记录
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/overrateUserList/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/overrateUserList/edit.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/overrateUserList/edit.jsp")})
public class OverrateUserListAction extends BaseAction{
	private static final long serialVersionUID = -2079540545514895435L;
	private OverrateUserListService overrateUserListService;
	@Resource
	public void setOverrateUserListService(OverrateUserListService overrateUserListService) {
		this.overrateUserListService = overrateUserListService;
	}

	@Action("/overrateUserList/query")
	public String query() {
		page = overrateUserListService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/overrateUserList/add")
	public String add() {
		String id = StrutsUtils.getParameterTrim("id");
		data = overrateUserListService.view(id);
		return "add";
	}

	@Action("/overrateUserList/save")
	public void save() {
		Map<String, String> map = StrutsUtils.getFormData();
		data = overrateUserListService.save(map);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/overrateUserList/update")
	public void update() {
		Map<String, String> map = StrutsUtils.getFormData();
		data = overrateUserListService.update(map);
		StrutsUtils.renderJson(data);
	}

	@Action("/overrateUserList/delete")
	public void deleteKeyword() {
		data = overrateUserListService.delete(StrutsUtils.getParameterTrim("id"));
		StrutsUtils.renderJson(data);
	}
}
