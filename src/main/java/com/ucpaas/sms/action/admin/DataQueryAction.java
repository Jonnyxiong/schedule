package com.ucpaas.sms.action.admin;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.admin.DataQueryService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 管理中心-数据查询
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")

@Results({ @Result(name = "query", location = "/WEB-INF/content/admin/dataQuery/query.jsp")})
public class DataQueryAction extends BaseAction {
	private static final long serialVersionUID = 1678878357430539193L;
	@Autowired
	private DataQueryService dataQueryService;

	/**
	 * 查询任务
	 * 
	 * @return
	 */
	@Action("/dataQuery/query")
	public String query() {
		data = dataQueryService.query(StrutsUtils.getFormData());
		return "query";
	}

}
