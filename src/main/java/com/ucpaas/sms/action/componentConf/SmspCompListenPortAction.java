
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
import com.ucpaas.sms.service.componentConf.SmspCompListenPortService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 组件管理-SMSP组件监听端口配置
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/componentConf/smspCompListenPort/query.jsp"),
		@Result(name = "edit", location="/WEB-INF/content/componentConf/smspCompListenPort/edit.jsp")})

public class SmspCompListenPortAction extends BaseAction {
	
	private static final long serialVersionUID = -928981108706408765L;
	@Autowired
	private SmspCompListenPortService smspCompListenPortService;
	
	@Action("/componentConf/smspCompListenPort/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = smspCompListenPortService.query(params);
		StrutsUtils.setAttribute("component_type", params.get("component_type"));
		return "query";
	}
	
	@Action("/componentConf/smspCompListenPort/edit")
	public String add() {
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			data = smspCompListenPortService.editView(Integer.parseInt(id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/componentConf/smspCompListenPort/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = smspCompListenPortService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/smspCompListenPort/delete")
	public void delete() {
		String id = StrutsUtils.getParameterTrim("id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		data = smspCompListenPortService.delete(params);
		StrutsUtils.renderJson(data);
	}
}
