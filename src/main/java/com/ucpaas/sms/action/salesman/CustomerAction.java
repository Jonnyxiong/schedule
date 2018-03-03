/**
 * 
 */
package com.ucpaas.sms.action.salesman;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.salesman.CustomerService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 客户管理
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/salesman/customer/query.jsp"),
		@Result(name = "add", location="/WEB-INF/content/salesman/customer/add.jsp")})

public class CustomerAction extends BaseAction {
	
	private static final long serialVersionUID = 8645727075023786634L;
	@Autowired
	private CustomerService customerService;
	
	@Action("/customer/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = customerService.query(params);
		return "query";
	}
	
	@Action("/customer/add")
	public String add() {
		return "add";
	}
	
	@Action("/customer/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = customerService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/customer/delete")
	public void delete() {
		String id = StrutsUtils.getParameterTrim("id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		data = customerService.delete(params);
		StrutsUtils.renderJson(data);
	}
}
