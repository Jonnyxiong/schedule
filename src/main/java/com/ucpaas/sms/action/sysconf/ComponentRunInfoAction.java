package com.ucpaas.sms.action.sysconf;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.ChannelKeywordsService;
import com.ucpaas.sms.service.sysconf.ComponentRunInfoService;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 系统设置 - 组件运行信息
 * @author Niu.T
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/componentRunInfo/query.jsp")})
public class ComponentRunInfoAction extends BaseAction {
	private static final long serialVersionUID = -8637102929860546005L;
	
	@Autowired
	private ComponentRunInfoService componentRunInfoService;
//	private ChannelKeywordsService channelkeyWordsService;
	/**
	 * @Description: 查询组件运行信息 -> 跳转至组件运行信息页面
	 * @author: Niu.T 
	 * @date: 2016年11月1日    下午8:20:05
	 * @return String
	 */
	@Action("/componentRunInfo/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		StrutsUtils.setAttribute("b_component_name", params.get("component_name"));
		StrutsUtils.setAttribute("b_info_content", params.get("info_content"));
		StrutsUtils.setAttribute("b_info_type", params.get("info_type"));
		StrutsUtils.setAttribute("start_time", params.get("start_time"));
		StrutsUtils.setAttribute("end_time", params.get("end_time"));
		page = componentRunInfoService.query(StrutsUtils.getFormData());
		return "query";
	}
	
	
}
