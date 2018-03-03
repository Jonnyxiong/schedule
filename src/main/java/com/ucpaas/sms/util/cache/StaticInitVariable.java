package com.ucpaas.sms.util.cache;

/**
 * 代理商订单表初始化变量
 */
public class StaticInitVariable {
	
	//代理商订单表-订单前缀
	public static String OEM_AGENT_ORDERID_PRE ="";

	//代理商订单表-订单序号（同一分钟内的第几笔订单）
	public static int OEM_AGENT_ORDER_NUM = 1;

	//客户订单表-订单前缀
	public static String OEM_CLIENT_ORDERID_PRE ="";

	//客户订单表-订单序号（同一分钟内的第几笔订单）
	public static int OEM_CLIENT_ORDER_NUM = 1;

}
