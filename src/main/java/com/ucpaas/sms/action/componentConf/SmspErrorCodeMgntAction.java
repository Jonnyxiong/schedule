
package com.ucpaas.sms.action.componentConf;

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
import com.ucpaas.sms.service.componentConf.SmspErrorCodeMgntService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 组件管理-平台错误码管理
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/componentConf/smspErrorCodeMgnt/query.jsp"),
		@Result(name = "edit", location="/WEB-INF/content/componentConf/smspErrorCodeMgnt/edit.jsp")})

public class SmspErrorCodeMgntAction extends BaseAction {
	
	private static final long serialVersionUID = 7798317613871503702L;
	@Autowired
	private SmspErrorCodeMgntService smspErrorCodeMgntService;
	
	@Action("/componentConf/smspErrorCodeMgnt/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = smspErrorCodeMgntService.query(params);
		StrutsUtils.setAttribute("component_type", params.get("component_type"));
		return "query";
	}
	
	@Action("/componentConf/smspErrorCodeMgnt/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {
			data = smspErrorCodeMgntService.editView(Integer.parseInt(id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/componentConf/smspErrorCodeMgnt/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = smspErrorCodeMgntService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/smspErrorCodeMgnt/delete")
	public void delete() {
		String id = StrutsUtils.getParameterTrim("id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		data = smspErrorCodeMgntService.delete(params);
		StrutsUtils.renderJson(data);
	}
}
