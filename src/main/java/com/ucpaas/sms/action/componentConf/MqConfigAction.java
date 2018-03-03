
package com.ucpaas.sms.action.componentConf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.componentConf.MqConfigService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 组件管理-MQ配置
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "queryMqC2sDb", location="/WEB-INF/content/componentConf/mqConfig/queryMqC2sDb.jsp"),
		@Result(name = "queryMqC2sIo", location="/WEB-INF/content/componentConf/mqConfig/queryMqC2sIo.jsp"),
		@Result(name = "queryMqSendDb", location="/WEB-INF/content/componentConf/mqConfig/queryMqSendDb.jsp"),
		@Result(name = "queryMqSendIo", location="/WEB-INF/content/componentConf/mqConfig/queryMqSendIo.jsp"),
		@Result(name = "edit", location="/WEB-INF/content/componentConf/mqConfig/edit.jsp")})

public class MqConfigAction extends BaseAction {
	
	private static final long serialVersionUID = -6304536447167217912L;
	@Autowired
	private MqConfigService mqConfigService;
	
	@Action("/componentConf/mqConf/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		
		String middlewareType = Objects.toString(params.get("middleware_type"), "2");
		params.put("middleware_type", middlewareType);
		
		page = mqConfigService.query(params);
		
		// 根据中间件类型返回到“MQ配置”页面中的不同tab页
		StrutsUtils.setAttribute("middleware_type", middlewareType);
		if(middlewareType.equals("1")){
			return "queryMqC2sIo";
		}else if(middlewareType.equals("2")){
			return "queryMqSendIo";
		}else if(middlewareType.equals("3")){
			return "queryMqC2sDb";
		}else{
			return "queryMqSendDb";
		}
		
	}
	
	@Action("/componentConf/mqConf/edit")
	public String add() {
		String mqId = StrutsUtils.getParameterTrim("mq_id");
		if (NumberUtils.isDigits(mqId)) {
			data = mqConfigService.editView(Integer.parseInt(mqId));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/componentConf/mqConf/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = mqConfigService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/mqConf/delete")
	public void delete() {
		String mqId = StrutsUtils.getParameterTrim("mq_id");
		data = mqConfigService.delete(mqId);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/mqConf/queryQueueMessageCount")
	public void queryQueueMessageCount(){
		Map<String, String> params = StrutsUtils.getFormData();
		data = mqConfigService.queryQueueMessageCount(params);
		StrutsUtils.renderJson(data);
	}
	
}
