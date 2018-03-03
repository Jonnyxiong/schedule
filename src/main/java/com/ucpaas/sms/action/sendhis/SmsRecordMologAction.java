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
import com.ucpaas.sms.service.sendhis.smsRecordMolog.SmsRecordMologService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 通道上行明细表
 * @author TonyHe
 *
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sendhis/smsRecordMolog/query.jsp") })
public class SmsRecordMologAction  extends BaseAction {
	private static final long serialVersionUID = 842727924657272508L;
	private SmsRecordMologService smsRecordMologService;
	@Resource
	public void setSmsRecordMologService(SmsRecordMologService smsRecordMologService) {
		this.smsRecordMologService = smsRecordMologService;
	}
	
	@Action("/smsRecordMolog/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		String start_time = params.get("start_time");
		String end_time = params.get("end_time") ;
		if(end_time == null && end_time == null ){
			DateTime dt = DateTime.now();
			start_time = dt.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			end_time = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("start_time", start_time);
			params.put("end_time", end_time);
		}
		page = smsRecordMologService.query(params);
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		return "query";
	}

}
