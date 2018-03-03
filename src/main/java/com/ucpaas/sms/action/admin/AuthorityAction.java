package com.ucpaas.sms.action.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.admin.AuthorityService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 管理员中心-权限管理
 * 
 * @author xiejiaan
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/admin/authority/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/admin/authority/edit.jsp") })
public class AuthorityAction extends BaseAction {
	private static final long serialVersionUID = 7398375055342636165L;

	@Autowired
	private AuthorityService authorityService;

	/**
	 * 查询权限
	 * 
	 * @return
	 */
	@Action("/authority/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = authorityService.query(params);
		StrutsUtils.setAttribute("web_id", params.get("web_id"));
		return "query";
	}

	/**
	 * 添加管理权限
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@Action("/authority/add")
	public String add() throws UnsupportedEncodingException {
		Integer roleId = null;
		data = authorityService.getAuthority(roleId);
		
		return "edit";
	}

	/**
	 * 修改管理权限
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@Action("/authority/edit")
	public String edit() throws UnsupportedEncodingException {
		Integer roleId = null;
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			roleId = Integer.valueOf(id);
		}
		Map<String,String> params = StrutsUtils.getFormData();
		params.put("role_id", roleId.toString());
		
		data = authorityService.getAuthority(roleId);

		return "edit";
	}

	/**
	 * 保存管理权限，包括添加、修改
	 * 
	 * @return
	 */
	@Action("/authority/save")
	public void save() {
		data = authorityService.saveAuthority(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	/**
	 * 修改状态：恢复、禁用
	 * 
	 * @return
	 */
	@Action("/authority/updateStatus")
	public void updateStatus() {
		String roleId = StrutsUtils.getParameterTrim("id");
		String status = StrutsUtils.getParameterTrim("status");
		if (NumberUtils.isDigits(roleId) && NumberUtils.isDigits(status)) {
			data = authorityService.updateStatus(Integer.parseInt(roleId), Integer.parseInt(status));
		}
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 删除权限
	 * 
	 * @return
	 */
	@Action("/authority/deleteRole")
	public void deleteRole() {
		String roleId = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(roleId)) {
			data = authorityService.deleteRole(Integer.parseInt(roleId));
		}
		StrutsUtils.renderJson(data);
	}

}
