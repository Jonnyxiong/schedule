package com.ucpaas.sms.action.sysconf;

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
import com.ucpaas.sms.service.sysconf.SegmentChannelService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-强制路由通道管理
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/segmentChannel/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/segmentChannel/edit.jsp")})
public class SegmentChannelAction extends BaseAction {

	private static final long serialVersionUID = -8447433011736150428L;
	
	@Autowired
	private SegmentChannelService segmentChannelService;
	
	@Action("/sysConf/segmentChannel/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
				
		page = segmentChannelService.query(params);
		StrutsUtils.setAttribute("text", params.get("text"));
		StrutsUtils.setAttribute("status", params.get("status"));
		StrutsUtils.setAttribute("channel_id_search", params.get("channel_id"));
		StrutsUtils.setAttribute("search_area_id", params.get("area_id"));
		StrutsUtils.setAttribute("search_send_type", params.get("send_type"));
		StrutsUtils.setAttribute("search_status", params.get("status"));
		return "query";
	}	
	
	@Action("/sysConf/segmentChannel/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {
			data = segmentChannelService.editView(id);
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/sysConf/segmentChannel/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = segmentChannelService.save(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sysConf/segmentChannel/delete")
	public void delete() {
		data = segmentChannelService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sysConf/segmentChannel/updateStatus")
	public void updateStatus() {
		data = segmentChannelService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sysConf/segmentChannel/queryOperatorstypeByChannelId")
	public void queryOperatorstypeByChannelId() {
		String channelId = StrutsUtils.getParameterTrim("channelId");
		if(StringUtils.isNotBlank(channelId)){
			data = segmentChannelService.queryOperatorstypeByChannelId(channelId);
		}else{
			data = new HashMap<>();
		}
		StrutsUtils.renderJson(data);
	}
	
}
