package com.ucpaas.sms.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.util.web.AuthorityUtils;

/**
 * 首页
 * 
 * @author xiejiaan
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "noLogin", location = "/WEB-INF/content/login.jsp"),
		@Result(name = "isLogin", type = "redirect", location = "/admin/view")})
public class IndexAction extends BaseAction {
	private static final long serialVersionUID = 4783786334291109411L;

	@Action("/index")
	public String index() {
		
		if (AuthorityUtils.isLogin()) {
			return "isLogin"; // 跳转到“管理中心-管理员资料”
		}else{
			return "noLogin";
		}
	}

}
