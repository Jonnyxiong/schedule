
package com.ucpaas.sms.action.componentConf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.model.rabbit.RabbitMqQueue;
import com.ucpaas.sms.service.componentConf.MiddleareService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 组件管理-中间件配置
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/componentConf/middlewareConf/query.jsp"),
		@Result(name = "edit", location="/WEB-INF/content/componentConf/middlewareConf/edit.jsp"),
		@Result(name = "viewRabbitMqQueueIframe", location="/WEB-INF/content/componentConf/middlewareConf/viewRabbitMqQueueIframe.jsp"),
		@Result(name = "createRabbitMqQueueIframe", location="/WEB-INF/content/componentConf/middlewareConf/createRabbitMqQueueIframe.jsp")})
                                                         

public class MiddlewareConfAction extends BaseAction {
	
	private static final long serialVersionUID = 3728769816084394289L;
	@Autowired
	private MiddleareService middleareService;
	
	@Action("/componentConf/middleware/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		
		page = middleareService.query(params);
		StrutsUtils.setAttribute("middleware_type", params.get("middleware_type"));
		return "query";
	}
	
	@Action("/componentConf/middleware/edit")
	public String edit() {
		String middlewareId = StrutsUtils.getParameterTrim("middleware_id");
		if (NumberUtils.isDigits(middlewareId)) {
			data = middleareService.editView(Integer.parseInt(middlewareId));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/componentConf/middleware/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = middleareService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/middleware/delete")
	public void delete() {
		String middlewareId = StrutsUtils.getParameterTrim("middleware_id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("middleware_id", middlewareId);
		data = middleareService.delete(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/viewRabbitMqQueueIframe")
	public String viewRabbitMqQueueIframe() {
		String middlewareId = StrutsUtils.getParameter("middlewareId");
		String middlewareName = StrutsUtils.getParameter("middlewareName");
		
		List<RabbitMqQueue> queueList = middleareService.queryAllQueueByMiddlewareId(middlewareId);
		data = new HashMap<String, Object>();
		data.put("queueList", queueList);
		data.put("middlewareId", middlewareId);
		data.put("middlewareName", middlewareName);
		return "viewRabbitMqQueueIframe";
	}
	
	@Action("/componentConf/createRabbitMqQueueIframe")
	public String createRabbitMqQueueIframe() {
		String middlewareId = StrutsUtils.getParameter("middlewareId");
		String middlewareName = StrutsUtils.getParameter("middlewareName");
		data = new HashMap<String, Object>();
		data.put("page", middleareService.queryNeedCreateQueueByMiddlewareId(middlewareId));
		data.put("middlewareId", middlewareId);
		data.put("middlewareName", middlewareName);
		return "createRabbitMqQueueIframe";
	}
	
	@Action("/componentConf/middleware/createQueueOnMiddleware")
	public void createQueueOnMiddleware(){
		String middlewareId = StrutsUtils.getParameter("middlewareId");
		data = middleareService.createQueueOnRabbitMQ(middlewareId);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/middleware/deleteQueueOnRabbitMQ")
	public void deleteQueueOnRabbitMQ(){
		String middlewareId = StrutsUtils.getParameter("middlewareId");
		data = middleareService.deleteQueueOnRabbitMQ(middlewareId);
		StrutsUtils.renderJson(data);
	}
}
