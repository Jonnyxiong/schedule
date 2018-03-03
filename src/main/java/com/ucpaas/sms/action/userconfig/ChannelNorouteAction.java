package com.ucpaas.sms.action.userconfig;

import java.util.Map;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.userconfig.ChannelNorouteService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 平台帐号未配置通道列表
 * @author TonyHe
 *
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/userconfig/channelNoroute/query.jsp"),
		   @Result(name = "edit", location = "/WEB-INF/content/userconfig/channelNoroute/edit.jsp") })
public class ChannelNorouteAction extends BaseAction{
	private static final long serialVersionUID = -103718108630303535L;
	
	@Autowired
	private  ChannelNorouteService channelNorouteService;
	@Action("/channelNoroute/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = channelNorouteService.query(params);
		Set<String> set = params.keySet();
		for (String str : set) {
			StrutsUtils.setAttribute(str, params.get(str));
		}
		return "query";
	}

	@Action("/channelNoroute/updateStatus")
	public void update() {
	Map<String, String> params = StrutsUtils.getFormData();
	data = channelNorouteService.updateStatus(params);
	StrutsUtils.renderJson(data);
	}
}
