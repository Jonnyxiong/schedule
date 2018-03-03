package com.ucpaas.sms.filter;

import com.ucpaas.sms.aop.LogAspect;
import com.ucpaas.sms.service.admin.AuthorityService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 权限控制filter
 * 
 * @author xiejiaan
 */
@Component
public class AuthorityFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(AuthorityFilter.class);
	private String[] excludeEqualUrls; // 等于此路径不需要控制
	private String[] excludeStartUrls; // 以此路径开始不需要控制
	@Autowired
	private AuthorityService authorityService;

	/**
	 * 检查是否有访问权限
	 * 
	 * @param request
	 * @return
	 */
	private boolean check(HttpServletRequest request) {
		if (ConfigUtils.is_auto_login && !AuthorityUtils.isLogin(request)) {
			AuthorityUtils.setAutoLoginUser(request);
			logger.debug("自动登录");
		}
		String reqUrl = request.getServletPath();

		if (reqUrl.indexOf('.') > -1) {// url包含.且不是.action，直接跳过
			if (reqUrl.endsWith(".action")) {
				reqUrl = reqUrl.substring(0, reqUrl.length() - 7);
			} else {
				return true;
			}
		}
		for (String excludeUrl : excludeStartUrls) { // 以此路径开始不需要控制，直接跳过
			if (reqUrl.startsWith(excludeUrl)) {
				return true;
			}
		}
		for (String excludeUrl : excludeEqualUrls) { // 等于此路径不需要控制，直接跳过
			if (reqUrl.equals(excludeUrl)) {
				return true;
			}
		}

		if (AuthorityUtils.isLogin(request)) {
			
			if(!authorityService.isValidUser(AuthorityUtils.getLoginUserId(request))){
				AuthorityUtils.setLogoutUser(request);
				return false;
			}
			int roleId = AuthorityUtils.getLoginRoleId(request);
			Map<String, Object> data = authorityService.isAuthority(roleId, reqUrl);
//			if ("1".equals(data.get("result"))) {// 是否有访问权限
//				request.setAttribute("select_menu", data);// 当前选中的菜单，用于MenuAction.sideMenu()、main.jsp
//				return true;
//			}
			if (data.get("result").toString().equals("1")) {// 是否有访问权限
				request.setAttribute("select_menu", data.get("select_menu"));// 当前选中的菜单，用于MenuAction.sideMenu()、main.jsp
				return true;
			}
			
			//首页特殊处理
			if("/admin/view".equals(reqUrl) 
					|| "/agent/index/view".equals(reqUrl) 
					|| "/opsplatform/index/view".equals(reqUrl)){
				return true;
			}
			
		}
		logger.debug("没有访问权限：reqUrl={}", reqUrl);
		return false;
	}

	@Override
	public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse,
			FilterChain paramFilterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) paramServletRequest;
		HttpServletResponse response = (HttpServletResponse) paramServletResponse;
		try {
			LogAspect.getInstance().printOptLog(request);
		}catch (Exception e){
			logger.error("日志输出异常 ----> {}",e);
		}

		if (check(request)) {
			paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
			return;
		}else{

			//判断是否是ajax
			if(request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
				response.setHeader("sessionstatus", "timeout");
			}else{
				response.sendRedirect(request.getContextPath() + "/common/403.jsp");
			}

//			// 验证没有权限返回错误页面
//			response.sendRedirect(request.getContextPath() + "/common/403.jsp");
		}
		return;
	}

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
		ServletContext servletContext = paramFilterConfig.getServletContext();
		servletContext.setAttribute("ctx", servletContext.getContextPath());
		logger.info("set ctx = " + servletContext.getContextPath());

		String exclude = paramFilterConfig.getInitParameter("excludeEqualUrls");
		if (exclude != null) {
			excludeEqualUrls = exclude.split(",");
		}

		exclude = paramFilterConfig.getInitParameter("excludeStartUrls");
		if (exclude != null) {
			excludeStartUrls = exclude.split(",");
		}
	}

	@Override
	public void destroy() {
	}
}
