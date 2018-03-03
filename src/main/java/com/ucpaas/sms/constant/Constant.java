package com.ucpaas.sms.constant;

/**
 * 常量类
 */
public class Constant {

	public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
	public static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
	public static final String LOGIN_USER_INFO = "LOGIN_USER_INFO";
	public static final int SMS_SEND_MAX_NUM = 100; //短信发送最大手机号码数
	public static final String sms_experience_template = "【云之讯】亲爱的用户，您的短信验证码为[%s]";
	public static final String SMS_VERIFY_CODE = "SMS_VERIFY_CODE";
	
	//订单前缀
	public static String ORDERID_PRE ="";
	
	//同一分钟内的第几笔订单
	public static int ORDER_NUM = 1;

	/**
	 * 短信类型
	 */
	public enum SmsType {
		NOTIFY("0", "通知短信"),
		VERIFICATION_CODE("4", "验证码短信"),
		MARKETING("5", "营销短信");

		private String value;
		private String desc;

		private SmsType(String value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public String getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}

	}

	/**
	 * 数据库流水表前缀
	 */
	public enum DbTablePrefix {

		T_SMS_ACCESS_YYYYMMDD("t_sms_access_", "短信access流水表");

		private String tablePrefix;
		private String desc;

		private DbTablePrefix(String tablePrefix, String desc) {
			this.tablePrefix = tablePrefix;
			this.desc = desc;
		}

		public String getTablePrefix() {
			return tablePrefix;
		}

		public String getDesc() {
			return desc;
		}

	}
	
}
