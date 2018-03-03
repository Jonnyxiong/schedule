package com.ucpaas.sms.action.sysconf;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.ParamConfService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-参数设置
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/paramConf/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/paramConf/edit.jsp") })
public class ParamConfAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	private ParamConfService paramConfService;

	@Resource
	public void setParamConfService(ParamConfService paramConfService) {
		this.paramConfService = paramConfService;
	}

	@Action("/paramConf/query")
	public String query() {
		page = paramConfService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/paramConf/edit")
	public String edit() {
		String param_id = StrutsUtils.getParameterTrim("param_id");
		if (NumberUtils.isDigits(param_id)) {
			data = paramConfService.view(Integer.parseInt(param_id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}

	@Action("/paramConf/save")
	public void save() {
		data = paramConfService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/paramConf/updateStatus")
	public void updateStatus() {
		data = paramConfService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
}
