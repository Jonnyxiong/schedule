package com.ucpaas.sms.action.channel;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.channel.ChannelGroupService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 用户短信通道管理-通道组管理
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/channel/channelGroup/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/channel/channelGroup/edit.jsp") })
public class ChannelGroupAction extends BaseAction {
	
	private static final long serialVersionUID = -9216069445189093029L;
	private ChannelGroupService channelGroupService;

	@Resource
	public void setChannelGroupService(ChannelGroupService channelGroupService) {
		this.channelGroupService = channelGroupService;
	}

	@Action("/channelGroup/query")
	public String query() {
		page = channelGroupService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/channelGroup/edit")
	public String edit() {
		String channelgroupid = StrutsUtils.getParameterTrim("channelgroupid");
		if (NumberUtils.isDigits(channelgroupid)) {
			data = channelGroupService.view(Integer.parseInt(channelgroupid));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}

	@Action("/channelGroup/save")
	public void save() {
		data = channelGroupService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/channelGroup/updateStatus")
	public void updateStatus() {
		data = channelGroupService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelGroup/delete")
	public void delete() {
		data = channelGroupService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelGroup/switchChannel")
	public void switchChannel() {
		data = channelGroupService.switchChannel(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelGroup/queryChannelGroupByOperator")
	public void queryChannelGroupByOperator() {
		data = channelGroupService.queryChannelGroupByOperator();
		StrutsUtils.renderJson(data);
	}
}
