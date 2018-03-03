package com.ucpaas.sms.action.sysconf;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.SmsOprateLogService;
import com.ucpaas.sms.util.web.StrutsUtils;

@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/smsOprateLog/query.jsp") })
public class SmsOprateLogAction extends BaseAction{
	private static final long serialVersionUID = -9036915909964441985L;
	
	private SmsOprateLogService smsOprateLogService;
	@Resource
	public void setSmsOprateLogService(SmsOprateLogService smsOprateLogService) {
		this.smsOprateLogService = smsOprateLogService;
	}

	@Action("/smsOprateLog/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		if( start_time== null && end_time == null ){
			DateTime dt = DateTime.now();
			start_time = dt.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			end_time = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("start_time", start_time);
			params.put("end_time", end_time);
		}
		page = smsOprateLogService.query(params);
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		return "query";
	}

}
