package com.ucpaas.sms.util.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.ucpaas.sms.constant.SysConstant;
import com.ucpaas.sms.constant.UserConstant;
import com.ucpaas.sms.util.ConfigUtils;

/**
 * 权限控制工具类
 * 
 * @author xiejiaan
 */
public class AuthorityUtils {

	/**
	 * 当前登录用户的sid、roleId保存在session中的key
	 */
	private static final String LOGIN_USER_ID = "LOGIN_USER_ID";
	private static final String LOGIN_USER_REALNAME = "LOGIN_USER_REALNAME";
	private static final String LOGIN_ROLE_ID = "LOGIN_ROLE_ID";
	private static final String LOGIN_WEB_ID = "LOGIN_WEB_ID";

	/**
	 * 保存自动登录用户的sid、roleId
	 * 
	 * @param request
	 */
	public static void setAutoLoginUser(HttpServletRequest request) {
		setLoginUser(request, SysConstant.SUPER_ADMIN_USER_ID, SysConstant.SUPER_ADMIN_USER_REALNAME,
				UserConstant.ROLE_SUPER_ADMIN);
	}

	/**
	 * 保存当前登录用户的sid、roleId
	 * 
	 * @param sid
	 * @param roleId
	 */
	public static void setLoginUser(Long userId, String userName, Integer roleId) {
		setLoginUser(StrutsUtils.getRequest(), userId, userName, roleId);
	}

	/**
	 * 保存当前登录用户的sid、roleId
	 * 
	 * @param request
	 * @param sid
	 * @param roleId
	 */
	public static void setLoginUser(HttpServletRequest request, Long userId, String userName, Integer roleId) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN_USER_ID, userId);
		session.setAttribute(LOGIN_USER_REALNAME, userName);
		session.setAttribute(LOGIN_ROLE_ID, roleId);
	}

	/**
	 * 保存当前登录用户的sid、roleId
	 * 
	 * @param sid
	 * @param roleId
	 */
	public static void setLoginUser(Long userId, String userName, Integer roleId,Integer web_id) {
		setLoginUser(StrutsUtils.getRequest(), userId, userName, roleId,web_id);
	}
	
	/**
	 * 保存当前登录用户的sid、roleId
	 * 
	 * @param request
	 * @param sid
	 * @param roleId
	 */
	public static void setLoginUser(HttpServletRequest request, Long userId, String userName, Integer roleId,Integer web_id) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN_USER_ID, userId);
		session.setAttribute(LOGIN_USER_REALNAME, userName);
		session.setAttribute(LOGIN_ROLE_ID, roleId);
		session.setAttribute(LOGIN_WEB_ID, web_id);
	}
	/**
	 * 获取当前登录用户的sid
	 * 
	 * @return
	 */
	public static Long getLoginUserId() {
		return getLoginUserId(StrutsUtils.getRequest());
	}

	/**
	 * 获取当前登录用户的sid
	 * 
	 * @param request
	 * @return
	 */
	public static Long getLoginUserId(HttpServletRequest request) {
		Long id = null;
		Object obj = request.getSession().getAttribute(LOGIN_USER_ID);
		if (obj != null) {
			id = (Long) obj;
		}
		return id;
	}

	/**
	 * 获取当前登录用户的roleId
	 * 
	 * @return
	 */
	public static Integer getLoginRoleId() {
		return getLoginRoleId(StrutsUtils.getRequest());
	}

	/**
	 * 获取当前登录用户的roleId
	 * 
	 * @param request
	 * @return
	 */
	public static Integer getLoginRoleId(HttpServletRequest request) {
		Integer roleId = null;
		Object obj = request.getSession().getAttribute(LOGIN_ROLE_ID);
		if (obj != null) {
			roleId = Integer.parseInt(obj.toString());
		}
		return roleId;
	}

	/**
	 * 获取当前登录用户的web_id
	 * 
	 * @param request
	 * @return
	 */
	public static Integer getLoginWebId(HttpServletRequest request) {
		Integer web_id = null;
		Object obj = request.getSession().getAttribute(LOGIN_WEB_ID);
		if (obj != null) {
			web_id = Integer.parseInt(obj.toString());
		}
		return web_id;
	}
	/**
	 * 退出当前登录用户
	 * 
	 */
	public static void setLogoutUser() {
		setLogoutUser(StrutsUtils.getRequest());
	}

	/**
	 * 退出当前登录用户
	 * 
	 * @param request
	 */
	public static void setLogoutUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(LOGIN_USER_ID);
		session.removeAttribute(LOGIN_ROLE_ID);
		session.removeAttribute(LOGIN_USER_REALNAME);
		session.removeAttribute(LOGIN_WEB_ID);
	}

	/**
	 * 当前登录的用户名
	 * 
	 * @return
	 */
	public static final String getLoginRealName() {
		return (String) StrutsUtils.getRequest().getSession(true).getAttribute(LOGIN_USER_REALNAME);
	}

	/**
	 * 当前登录的用户名
	 *
	 * @return
	 */
	public static final String getLoginRealName(HttpServletRequest request) {
		return (String) request.getSession(true).getAttribute(LOGIN_USER_REALNAME);
	}

	/**
	 * 判断当前是否已登录
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		return isLogin(StrutsUtils.getRequest());
	}

	/**
	 * 判断当前是否已登录
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isLogin(HttpServletRequest request) {
		Long sid = getLoginUserId(request);
		String webId = String.valueOf(getLoginWebId(request));
		if (sid != null && webId.equals(ConfigUtils.web_id)) {
			// 已经登录且用户的webId正确
			return true;
		}
		return false;
	}

}
