package com.ucpaas.sms.action.userconfig;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.userconfig.TemplateService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 用户短信通道管理-模板ID管理(废弃)
 * 
 * @author liulu
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/userconfig/template/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/userconfig/template/edit.jsp") })
public class TemplateAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	private TemplateService templateService;

	@Resource
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	@Action("/template/query")
	public String query() {
		page = templateService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/template/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			data = templateService.view(Integer.parseInt(id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}

	@Action("/template/save")
	public void save() {
		data = templateService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/template/updateStatus")
	public void updateStatus() {
		data = templateService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	/**
	 * 删除用户
	 */
	@Action("/template/delete")
	public void delete() {
		data = templateService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
}
