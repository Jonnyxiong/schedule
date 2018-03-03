package com.ucpaas.sms.action.sendhis;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sendhis.smsAccessMolog.SmsAccessMologService;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 客户上行明细表
 * @author TonyHe
 *
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sendhis/smsAccessMolog/query.jsp") })
public class SmsAccessMologAction  extends BaseAction {
	private static final long serialVersionUID = 7540786566644759726L;
	private SmsAccessMologService smsAccessMologService;
	@Resource
	public void setSmsAccessMologService(SmsAccessMologService smsAccessMologService) {
		this.smsAccessMologService = smsAccessMologService;
	}
	
	@Action("/smsAccessMolog/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		String start_time = params.get("start_time");
		String end_time = params.get("end_time") ;
		if(start_time == null && end_time == null ){
			DateTime dt = DateTime.now();
			start_time = dt.minusHours(1).toString("yyyy-MM-dd HH:mm:ss") ;
			end_time = dt.toString("yyyy-MM-dd HH:mm:ss") ;
			params.put("start_time", start_time);
			params.put("end_time", end_time);
		}
		page = smsAccessMologService.query(params);
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		return "query";
	}
	
}
