package com.ucpaas.sms.action.channel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.channel.ChannelErrorDescService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 用户短信通道管理-短信错误状态对应关系配置
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/channel/channelErrorDesc/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/channel/channelErrorDesc/edit.jsp") })
public class ChannelErrorDescAction extends BaseAction {
	
	private static final long serialVersionUID = 3418948947405324809L;
	@Autowired
	private ChannelErrorDescService channelErrorDescService;

	@Action("/channelErrorDesc/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = channelErrorDescService.query(params);
		
		StrutsUtils.setAttribute("type", params.get("search_type"));
		return "query";
	}

	@Action("/channelErrorDesc/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (!StringUtils.isBlank(id)) {
			data = channelErrorDescService.view(id);
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}

	@Action("/channelErrorDesc/save")
	public void save() {
		String id = StrutsUtils.getParameterTrim("id");
		Map<String, String> params = StrutsUtils.getFormData();
		if (StringUtils.isBlank(id)) {
			data = channelErrorDescService.save(params);
		} else {
			params.put("id", id);
			data = channelErrorDescService.update(params);
		}
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelErrorDesc/delete")
	public void delete() {
		data = channelErrorDescService.delete(StrutsUtils.getParameterTrim("id"));
		StrutsUtils.renderJson(data);
	}

}
