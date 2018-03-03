/**
 * 
 */
package com.ucpaas.sms.action.salesman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.salesman.CustomerRechargeService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 销售人员管理
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/salesman/recharge/query.jsp"),
		@Result(name = "add", location="/WEB-INF/content/salesman/recharge/add.jsp")})

public class CustomerRechargeAction extends BaseAction {
	
	private static final long serialVersionUID = 9027647055105616827L;
	@Autowired
	private CustomerRechargeService customerRechargeService;
	
	@Action("/recharge/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		if (null == start_time || null == end_time) {
			DateTime dt = DateTime.now();
			start_time = dt.withMonthOfYear(1).toString("yyyy-MM");
			end_time = dt.withMonthOfYear(12).toString("yyyy-MM");;
		}
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		
		params.put("start_time", start_time + "-01");
		params.put("end_time", end_time + "-01");
		page = customerRechargeService.query(params);
		return "query";
	}
	
	@Action("/recharge/add")
	public String add() {
		return "add";
	}
	
	@Action("/recharge/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = customerRechargeService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/recharge/delete")
	public void delete() {
		String id = StrutsUtils.getParameterTrim("id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		data = customerRechargeService.delete(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/recharge/getsalesmanbyid")
	public void getSalesmanById() {
		String id = StrutsUtils.getParameterTrim("customer_id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("customer_id", id);
		List<Map<String, Object>> list = customerRechargeService.getSalesmanById(params);
		Map<String, Object> retMap = new HashMap<String, Object>();
		if(list != null && list.size() == 1){
			retMap.put("salesman_id", list.get(0).get("id"));
			retMap.put("salesman_name", list.get(0).get("name"));
		}
//		return retMap;
		StrutsUtils.renderJson(retMap);
	}
	
}
