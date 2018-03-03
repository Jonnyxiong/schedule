package com.ucpaas.sms.action.sysconf;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.IP4NoteService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-节点IP对应关系
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")
@Results(@Result(name = "query", location = "/WEB-INF/content/sysconf/ip4Note/query.jsp"))
public class IP4NoteAction extends BaseAction {
	private static final long serialVersionUID = -1318934152683673655L;
	@Autowired
	private IP4NoteService ip4NoteService;
	
	@Action("/ip4note/query")
	public String query() {
		page = ip4NoteService.query(StrutsUtils.getFormData());
		return "query";
	}

}
