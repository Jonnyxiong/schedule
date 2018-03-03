
package com.ucpaas.sms.action.componentConf;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.componentConf.SmspComponentService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 组件管理-SMSP组件配置
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/componentConf/smspComponent/query.jsp"),
		@Result(name = "edit", location="/WEB-INF/content/componentConf/smspComponent/edit.jsp")})

public class SmspComponentAction extends BaseAction {
	
	private static final long serialVersionUID = 3959353274127958317L;
	@Autowired
	private SmspComponentService smspComponentService;
	
	@Action("/componentConf/smspComponent/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = smspComponentService.query(params);
		StrutsUtils.setAttribute("component_type", params.get("component_type"));
		return "query";
	}
	
	@Action("/componentConf/smspComponent/edit")
	public String add() {
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			data = smspComponentService.editView(Integer.parseInt(id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/componentConf/smspComponent/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = smspComponentService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/smspComponent/delete")
	public void delete() {
		String componentId = StrutsUtils.getParameterTrim("component_id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("component_id", componentId);
		data = smspComponentService.delete(params);
		StrutsUtils.renderJson(data);
	}
}
