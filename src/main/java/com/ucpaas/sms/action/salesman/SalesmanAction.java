/**
 * 
 */
package com.ucpaas.sms.action.salesman;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.salesman.SalesmanService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 销售人员管理
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/salesman/saleman/query.jsp"),
		@Result(name = "add", location="/WEB-INF/content/salesman/saleman/add.jsp"),
		@Result(name = "taskQuery", location="/WEB-INF/content/salesman/saleman/task-query.jsp"),
		@Result(name = "taskAdd", location="/WEB-INF/content/salesman/saleman/task-add.jsp")})

public class SalesmanAction extends BaseAction {

	private static final long serialVersionUID = -2196614385215841281L;
	@Autowired
	private SalesmanService salesmanService;
	
	@Action("/salesman/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = salesmanService.query(params);
		return "query";
	}
	
	@Action("/salesman/add")
	public String add() {
		return "add";
	}
	
	@Action("/salesman/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = salesmanService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/salesman/delete")
	public void delete() {
		String id = StrutsUtils.getParameterTrim("id");
		String handOverToId = StrutsUtils.getParameterTrim("handOverToId");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("handOverToId", handOverToId);
		
		data = salesmanService.delete(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/salesman/task/query")
	public String taskQuery() {
		Map<String, String> params = StrutsUtils.getFormData();
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		if (null == start_time || null == end_time) {
			DateTime dt = DateTime.now();
			start_time = dt.withMonthOfYear(1).toString("yyyy-MM");
			end_time = dt.withMonthOfYear(12).toString("yyyy-MM");;
		}
		params.put("start_time", start_time + "-01");
		params.put("end_time", end_time + "-01");
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		page = salesmanService.queryTask(params);
		return "taskQuery";
	}
	
	@Action("/salesman/task/add")
	public String taskAdd() {
		return "taskAdd";
	}
	
	@Action("/salesman/task/save")
	public void taskSave() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = salesmanService.saveTask(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/salesman/task/delete")
	public void taskDelete() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = salesmanService.deleteTask(params);
		StrutsUtils.renderJson(data);
	}
}
