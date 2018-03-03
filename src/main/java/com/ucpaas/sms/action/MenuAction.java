package com.ucpaas.sms.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.service.MenuService;

/**
 * 菜单
 * 
 * @author xiejiaan
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "header", location = "/WEB-INF/content/menu/header.jsp"),
		@Result(name = "sideMenu", location = "/WEB-INF/content/menu/sideMenu.jsp") })
public class MenuAction extends BaseAction {
	private static final long serialVersionUID = 346913635975674774L;
	@Autowired
	private MenuService menuService;

	/**
	 * 页面顶部1、2级菜单
	 * 
	 * @return
	 */
	@Action("/menu/header")
	public String header() {
		data = menuService.getHeaderMenu();
		return "header";
	}
}
