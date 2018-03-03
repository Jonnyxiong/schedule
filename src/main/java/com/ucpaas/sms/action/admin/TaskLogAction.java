package com.ucpaas.sms.action.admin;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.admin.TaskLogService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 管理中心-任务日志
 * 
 * @author xiejiaan
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/admin/taskLog/query.jsp"),
		@Result(name = "view", location = "/WEB-INF/content/admin/taskLog/view.jsp") })
public class TaskLogAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	@Autowired
	private TaskLogService taskLogService;

	/**
	 * 查询任务日志
	 * 
	 * @return
	 */
	@Action("/taskLog/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		String start_date0 = params.get("start_date");
		String end_date0 = params.get("end_date");
		
		if (start_date0 == null || end_date0 == null) {
			DateTime dt = DateTime.now();
			start_date0 = dt.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			end_date0 = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("start_date", start_date0);
			params.put("end_date", end_date0);
			
		}
		StrutsUtils.setAttribute("start_date0", start_date0);
		StrutsUtils.setAttribute("end_date0", end_date0);
		page = taskLogService.query(params);
		return "query";
	}

	/**
	 * 查看任务日志
	 * 
	 * @return
	 */
	@Action("/taskLog/view")
	public String view() {
		String logId = StrutsUtils.getParameterTrim("log_id");
		if (NumberUtils.isDigits(logId)) {
			data = taskLogService.view(Integer.parseInt(logId));
		}
		return "view";
	}

}
