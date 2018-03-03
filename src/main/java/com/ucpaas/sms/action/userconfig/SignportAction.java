package com.ucpaas.sms.action.userconfig;

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
import com.ucpaas.sms.service.userconfig.SignportService;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 用户帐号和签名端口对应关系t_sms_clientid_signport
 * @author TonyHe
 *
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/userconfig/signport/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/userconfig/signport/edit.jsp") })
public class SignportAction extends BaseAction{
	private static final long serialVersionUID = 2124302250214565304L;
	
	@Autowired
	private SignportService signportService;
	@Action("/signport/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = signportService.query(params);
		return "query";
	}
	
	@Action("/signport/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {
			data = signportService.view(id);
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/signport/save")
	public void save() {
		data = signportService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/signport/checkClientIdAndGetSignport")
	public void checkClientIdAndGetSignport(){
		data = signportService.checkClientIdAndGetSignport(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/signport/updateStatus")
	public void updateStatus() {
		data = signportService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/signport/delete")
	public void delete() {
		String id = StrutsUtils.getParameterTrim("id");
		data = signportService.delete(id);
		StrutsUtils.renderJson(data);
	}

}
