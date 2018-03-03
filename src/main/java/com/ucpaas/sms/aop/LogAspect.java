package com.ucpaas.sms.aop;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.ucpaas.sms.util.CommonUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

//@Component
//@Aspect
public class LogAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String requestPath = null; // 请求地址
	private String userName = null; // 用户名
	private Long userId = null; // 用户id
	private String ip = null; // ip
	private Map<?, ?> inputParamMap = null; // 传入参数
	private Map<String, Object> outputParamMap = null; // 存放输出结果
	private long startTimeMillis = 0; // 开始时间
	private long endTimeMillis = 0; // 结束时间

	/**
	 * @Title：doBeforeInServiceLayer
	 * @Description: 方法调用前触发 记录开始时间
	 * @param joinPoint
	 */
//	@Before("execution(* *..action1..*.*(..))")
	public void doBeforeInServiceLayer(JoinPoint joinPoint) {
		startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
	}

	/**
	 * 
	 * @Title：doAfterInServiceLayer
	 * @Description: 方法调用后触发 记录结束时间
	 * @param joinPoint
	 */
//	@After("execution(* *..action1..*.*(..))")
	public void doAfterInServiceLayer(JoinPoint joinPoint) {
		try {
			endTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间
			this.printOptLog();
		} catch (Exception e) {
			logger.error("日志组件出错", e);
		}
	}

	/**
	 * 
	 * @Title：doAround
	 * @Description: 环绕触发
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
//	@Around("execution(* *..action1..*.*(..)) && @annotation(action)")
	public Object doAround(ProceedingJoinPoint pjp, Action action) throws Throwable {
		/**
		 * 1.获取request信息 2.根据request获取session 3.从session中取出登录用户信息
		 */
		try {
//ServletRequestAttributes may be null, check it first.
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//            ServletWebRequest servletContainer = (ServletWebRequest)RequestContextHolder.getRequestAttributes();
//			HttpServletRequest request = servletContainer.getRequest();
//			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//			ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//			HttpServletRequest request = sra.getRequest();
			HttpSession session = request.getSession();
			// 从session中获取用户信息
			userId = AuthorityUtils.getLoginUserId();
			if (userId != null) {
				userName = AuthorityUtils.getLoginRealName();

			} else {
				userName = "用户未登录";
			}
			ip = CommonUtil.getClientIP(request);
			// 获取输入参数
			inputParamMap = request.getParameterMap();
			// 获取请求地址
			requestPath = request.getRequestURI();

			// 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
			outputParamMap = new HashMap<String, Object>();
		} catch (Exception e) {
			logger.error("日志组件出错", e);
		}
		Object result = pjp.proceed();// result的值就是被拦截方法的返回值

		try {
			outputParamMap.put("result", result);
		} catch (Exception e) {
			logger.error("日志组件出错", e);
		}

		return result;
	}

	/**
	 * 
	 * @Title：printOptLog
	 * @Description: 输出日志
	 */
	private void printOptLog() {
		Gson gson = new Gson(); // 需要用到google的gson解析包
		String optTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);
		logger.debug("userId={},username={},ip={},url={},beginTime={},elapsedTime={},requestParam={},reponse={}",
				userId, userName, ip, requestPath, optTime, (endTimeMillis - startTimeMillis) + "ms",
				gson.toJson(inputParamMap), gson.toJson(outputParamMap));
	}

	/**
	 *
	 * @Title：printOptLog
	 * @Description: 输出日志
	 */
	public void printOptLog(HttpServletRequest request) {
        userId = AuthorityUtils.getLoginUserId(request);
        if (userId != null) {
            userName = AuthorityUtils.getLoginRealName(request);

        } else {
            userName = "用户未登录";
        }
        ip = CommonUtil.getClientIP(request);
        // 获取输入参数
        inputParamMap = request.getParameterMap();
        // 获取请求地址
        requestPath = request.getRequestURI();
		Gson gson = new Gson(); // 需要用到google的gson解析包
		String optTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);
		logger.debug("userId={},username={},ip={},url={},beginTime={},requestParam={}",
				userId, userName, ip, requestPath, optTime,
				gson.toJson(inputParamMap));
	}

	public static LogAspect getInstance(){
        return new LogAspect();
    }
}
