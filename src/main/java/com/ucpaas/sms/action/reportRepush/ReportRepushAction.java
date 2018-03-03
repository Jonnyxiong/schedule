package com.ucpaas.sms.action.reportRepush;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.reportRepush.ReportRepushService;
import com.ucpaas.sms.service.sendhis.customerSend.CustomerSendHisService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * 状态报告重推（Access）
 * 
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/reportRepush/query.jsp")})

public class ReportRepushAction extends BaseAction {

	private static final long serialVersionUID = 6834282290966823050L;
	@Autowired
	private CustomerSendHisService customerSendHisService;
	@Autowired
	private ReportRepushService reportRepushService;



	/**
	 * 状态报告重推查询短信记录
	 * @return
	 * @throws Exception 
	 */
	@Action("/reportRepush/query")
	public String query() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		String identify = params.get("identify");
		String clientid = params.get("clientid");
		
		
		if (start_time == null || end_time == null) {
			DateTime dt = DateTime.now();
			start_time = dt.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			end_time = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("start_time", start_time);
			params.put("end_time", end_time);
		}
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		StrutsUtils.setAttribute("identify", identify);
		StrutsUtils.setAttribute("clientid", clientid);
		
		page = customerSendHisService.query(params);
		return "query";
	}

	@Action("/reportRepush/submitRepush")
	public void submitRepush(){
		data = reportRepushService.submitRepush(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/reportRepush/queryWaitRepushNum")
	public void queryWaitRepushNum(){
		data = reportRepushService.queryWaitRepushNum(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	
}
