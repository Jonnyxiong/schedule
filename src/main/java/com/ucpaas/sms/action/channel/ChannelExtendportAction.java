package com.ucpaas.sms.action.channel;

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
import com.ucpaas.sms.service.channel.ChannelExtendportService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 通道组管理-自签通道用户端口管理
 * 
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/channel/channelExtendport/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/channel/channelExtendport/edit.jsp") })
public class ChannelExtendportAction extends BaseAction {

	private static final long serialVersionUID = -5844655956498108451L;
	@Autowired
	private ChannelExtendportService channelExtendportService;

	@Action("/channelExtendport/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = channelExtendportService.query(params);
		StrutsUtils.setAttribute("text", params.get("text"));
		return "query";
	}

	@Action("/channelExtendport/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			data = channelExtendportService.view(Integer.parseInt(id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}

	@Action("/channelExtendport/save")
	public void save() {
		data = channelExtendportService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelExtendport/delete")
	public void delete() {
		data = channelExtendportService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/channelExtendport/channelExtendportCheck1")
	public void channelExtendportCheck1() {
		data = channelExtendportService.channelExtendportCheck1(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelExtendport/channelExtendportCheck2")
	public void channelExtendportCheck2() {
		data = channelExtendportService.channelExtendportCheck2(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelExtendport/channelExtendportCheck3")
	public void channelExtendportCheck3() {
		data = channelExtendportService.channelExtendportCheck3(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
}
