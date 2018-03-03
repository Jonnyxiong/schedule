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
import com.ucpaas.sms.service.userconfig.ConfirmService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 用户短信通道管理-短信通道配置
 * 
 * @author liulu
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/userconfig/confirm/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/userconfig/confirm/edit.jsp") })
public class ConfirmAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	private ConfirmService confirmService;

	@Resource
	public void setTemplateService(ConfirmService confirmService) {
		this.confirmService = confirmService;
	}

	@Action("/confirm/query")
	public String query() {
		page = confirmService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/confirm/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			data = confirmService.view(Integer.parseInt(id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}

	@Action("/confirm/save")
	public void save() {
		data = confirmService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/confirm/updateStatus")
	public void updateStatus() {
		data = confirmService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	/**
	 * 删除用户
	 */
	@Action("/confirm/delete")
	public void delete() {
		data = confirmService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
}
