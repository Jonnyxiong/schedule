package com.ucpaas.sms.util;

import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.util.cache.StaticInitVariable;
import com.ucpaas.sms.util.rest.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统配置工具类
 * 
 * @author xiejiaan
 */
@Component
public class ConfigUtils {
	private static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);


    @Autowired
	private CommonService commonService;

	/**
	 * 运行环境：development（开发）、devtest（开发测试）、test（测试）、production（线上）
	 */
//	public static String spring_profiles_active;
	
	/**
	 * 系统版本号
	 */
	public static String system_version;
	
	/**
	 * 应用ID
	 */
	public static String web_id;
	
	/**
	 * 是否自动登录
	 */
	public static boolean is_auto_login;

	/**
	 * 配置文件路径
	 */
	public static String config_file_path;
	
	/**
	 * smsp-access短信请求URL
	 */
	public static String smsp_access_url;
	
	/**
	 * smsp-access短信请求clientid
	 */
	public static String smsp_access_clientid;
	
	/**
	 * smsp-access短信请求password
	 */
	public static String smsp_access_password;

	/**
	 * rest接口的域名
	 */
	public static String rest_domain;
	/**
	 * rest接口的版本
	 */
	public static String rest_version;
	/**
	 * 文件本地保存路径
	 */
	public static String save_path;
	/**
	 * Access流水表数据库名字
	 */
	public static String access_database_name;
	/**
	 * Record流水表数据库名字
	 */
	public static String record_database_name;
	/**
	 * 默认无代理商的直客clientId开始创建的位置
	 */
	public static String default_agent_clientid_start;
	/**
	 * 默认有代理商的clientId开始创建的位置
	 */
	public static String default_direct_clientid_start;
	/**
	 * 调度系统下代理商订单的标识（默认为5）
	 */
	public static String platform_oem_agent_order_identify;
	/**
	 * SMSP-TEST请求协议
	 */
	public static String smsp_test_protocol;
	/**
	 * SMSP-TEST请求账号
	 */
	public static String smsp_test_clientid;
	/**
	 * SMSP-TEST请求账号密码
	 */
	public static String smsp_test_password;
	/**
	 * 短信审核/短信测试接口
	 */
	public static String sms_test_send_url;
	/**
	 * 最大允许excel导出量
	 */
	public static String max_export_excel_num;
	/**
	 * 最大允许excel导入量
	 */
	public static String excel_max_import_num;

	/**
	 * redis 配置
	 */
	public static String redis_servers;  // 服务器地址
	public static String redis_port; // 服务器端口
	public static String redis_maxActive;
	public static String redis_maxIdle;
	public static String redis_maxWait;
	public static String redis_testOnBorrow;

	/**
	 * 审核（审核查询）页面关键字内存失效时间
	 */
	public static Integer audit_client_keyword_expire_time;

	/**
	 * 重推状态报告-单个任务重推数量
	 */
	public static Integer report_repush_one_task_process_num;

	/**
	 * 重推状态报告-线程池大小
	 */
	public static Integer report_repush_thread_pool_size;
	
	
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		String path = ConfigUtils.class.getClassLoader().getResource("").getPath() ;
//		spring_profiles_active = System.getProperty("spring.profiles.active");
		config_file_path = path + "system.properties";

		this.initValue();
		
		// 初始化代理商订单ID后缀
		this.initAgentOrderIdPreForOem();
		this.initClientOrderIdPreForOem();
		
		RedisUtils.init(); // 初始化redis工具类
		
		logger.info("\n\n-------------------------【smsp-schedule-{} 启动】\n加载配置文件：\n{}\n", system_version,
				config_file_path);
	}

	/**
	 * 初始化配置项的值
	 */
	private void initValue() {
		Field[] fields = ConfigUtils.class.getFields();
		Object fieldValue = null;
		String name = null, value = null, tmp = null;
		Class<?> type = null;
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
		Matcher matcher = null;
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(config_file_path));

			for (Field field : fields) {
				name = field.getName();
				value = properties.getProperty(name);
				if (StringUtils.isNotBlank(value)) {
					matcher = pattern.matcher(value);
					while (matcher.find()) {
						tmp = properties.getProperty(matcher.group(1));
						if (StringUtils.isBlank(tmp)) {
							logger.error("配置{}存在其它配置{}，请检查您的配置文件", name, matcher.group(1));
						}
						value = value.replace(matcher.group(0), tmp);
					}

					type = field.getType();
					if (String.class.equals(type)) {
						fieldValue = value;
					} else if (Integer.class.equals(type)) {
						fieldValue = Integer.valueOf(value);
					} else if (Boolean.class.equals(type)) {
						fieldValue = Boolean.valueOf(value);
					} else {
						fieldValue = value;
					}
					field.set(this, fieldValue);
				}
				logger.info("加载配置：{}={}", name, field.get(this));
			}
		} catch (Throwable e) {
			logger.error("初始化配置项的值失败：" + name + "=" + value, e);
		}
		
		
	}
	
	private void initAgentOrderIdPreForOem(){

		Date date = new Date();
		int num = 0;
		//后面的1代表代理商下单
		String orderIdPre = DateUtil.dateToStr(date,"yyMMdd") + DateUtil.dateToStr(date, "HHmm")+ ConfigUtils.platform_oem_agent_order_identify;//运营平台下单标识为4
		String numStr =  commonService.getOemAgentOrderTheMostNumForMinute(orderIdPre);
		if(numStr == null){
			num = 1;
		}else{
			num = Integer.valueOf(numStr) + 1;
		}

		StaticInitVariable.OEM_AGENT_ORDERID_PRE = orderIdPre;
		StaticInitVariable.OEM_AGENT_ORDER_NUM = num;
	}

	private void initClientOrderIdPreForOem(){

		Date date = new Date();
		int num = 0;
		//后面的1代表代理商下单
		String orderIdPre = DateUtil.dateToStr(date,"yyMMdd") + DateUtil.dateToStr(date, "HHmm")+ ConfigUtils.platform_oem_agent_order_identify;// oem订单标识3
		String numStr =  commonService.getOemClientOrderTheMostNumForMinute(orderIdPre);
		if(numStr == null){
			num = 1;
		}else{
			num = Integer.valueOf(numStr) + 1;
		}

		StaticInitVariable.OEM_CLIENT_ORDERID_PRE = orderIdPre;
		StaticInitVariable.OEM_CLIENT_ORDER_NUM = num;
	}
	
}
