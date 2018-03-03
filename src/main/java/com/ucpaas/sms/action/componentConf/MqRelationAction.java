
package com.ucpaas.sms.action.componentConf;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.componentConf.MqRelationService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 组件管理-SMSP组件与MQ关联配置
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/componentConf/mqRelation/query.jsp"),
		@Result(name = "edit", location="/WEB-INF/content/componentConf/mqRelation/edit.jsp"),
		@Result(name = "createView", location="/WEB-INF/content/componentConf/mqRelation/create.jsp")})

public class MqRelationAction extends BaseAction {
	
	private static final long serialVersionUID = 284703189102447378L;
	@Autowired
	private MqRelationService mqRelationService;
	
	@Action("/componentConf/mqRelation/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = mqRelationService.query(params);
		StrutsUtils.setAttribute("component_id_search", params.get("component_id_search"));
		StrutsUtils.setAttribute("message_type", params.get("message_type"));
		StrutsUtils.setAttribute("mq_id_search", params.get("mq_id"));
		StrutsUtils.setAttribute("mode_search", params.get("mode"));
		
		return "query";
	}
	
	@Action("/componentConf/mqRelation/queryAllMqQueue")
	public void queryAllMqQueue() {
		Map<String, String> params = StrutsUtils.getFormData();
		dataList = mqRelationService.queryAllMqQueue(params);
		StrutsUtils.renderJson(dataList);
	}
	
	@Action("/componentConf/mqRelation/createView")
	public String createView() {
		return "createView";
	}
	
	@Action("/componentConf/mqRelation/create")
	public void create() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = mqRelationService.create(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/mqRelation/edit")
	public String editView() {
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			data = mqRelationService.editView(Integer.parseInt(id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/componentConf/mqRelation/update")
	public void update() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = mqRelationService.update(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/mqRelation/updateMqGetRate")
	public void updateMqGetRate() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = mqRelationService.updateMqGetRate(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/componentConf/mqRelation/delete")
	public void delete() {
		String id = StrutsUtils.getParameterTrim("id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		data = mqRelationService.delete(params);
		StrutsUtils.renderJson(data);
	}
	
}
