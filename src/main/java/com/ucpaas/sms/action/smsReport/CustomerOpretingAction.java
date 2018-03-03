package com.ucpaas.sms.action.smsReport;

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
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.smsReport.CustomerOpretingService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 客户运营报表查询
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/smsreport/customerOpreting/query.jsp") })
public class CustomerOpretingAction extends BaseAction {
	
	private static final long serialVersionUID = 2280326269311523494L;
	@Autowired
	private CustomerOpretingService customerOpretingService;

	
	@Action("/customerOpreting/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		String stat_type = params.get("stat_type");
		String start_time_day = params.get("start_time_day");
		String end_time_day = params.get("end_time_day");
		String start_time_month = params.get("start_time_month");
		String end_time_month = params.get("end_time_month");
		String belong_sale = params.get("belong_sale");
		if (stat_type == null || start_time_day == null || end_time_day == null || start_time_month == null
				|| end_time_month == null) {
			stat_type = "1";
			DateTime dt = DateTime.now();
			start_time_day = dt.minusDays(1).toString("yyyyMMdd");
			end_time_day = start_time_day;
			start_time_month = dt.minusMonths(1).toString("yyyyMM");
			end_time_month = start_time_month;

			params.put("stat_type", stat_type);
			params.put("start_time_day", start_time_day);
			params.put("end_time_day", end_time_day);
			params.put("start_time_month", start_time_month);
			params.put("end_time_month", end_time_month);
		}
		StrutsUtils.setAttribute("stat_type", stat_type);
		StrutsUtils.setAttribute("start_time_day", start_time_day);
		StrutsUtils.setAttribute("end_time_day", end_time_day);
		StrutsUtils.setAttribute("start_time_month", start_time_month);
		StrutsUtils.setAttribute("end_time_month", end_time_month);
		StrutsUtils.setAttribute("belong_sale", belong_sale);
		
		data = customerOpretingService.query(params);
		return "query";
	}
	
	
	/**
	 * 导出Excel文件
	 */
	@Action("/customerOpreting/exportExcel")
	public void exportExcel() {
		customerOpretingService.exportExcel(StrutsUtils.getFormData());
	}

}
