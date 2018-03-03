package com.ucpaas.sms.service;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.constant.DbConstant.DbType;
import com.ucpaas.sms.constant.DbConstant.TablePrefix;

/**
 * 公共业务
 * 
 * @author xiejiaan
 */
public interface CommonService {

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 *            未加密字符串
	 * @return
	 */
	Map<String, Object> login(String username, String password);

	/**
	 * 数据库中是否含有此表
	 * 
	 * @param table_name
	 * @param table_schema
	 * @return
	 */
	boolean hasTable(String table_name, String table_schema);

	/**
	 * 查询远程文件是否存在
	 * 
	 * @param table
	 * @param localPath
	 * @return
	 */
	String queryRemotePath(String table, String localPath);

	/**
	 * 业务监控
	 * 
	 * @return
	 */
	Map<String, Object> monitor();

	/**
	 * 查询在时间范围内存在的表格
	 * 
	 * @param dbType
	 * 			    数据库
	 * @param tablePrefix
	 *            查询表格前缀
	 * @param startDate
	 *            开始时间，格式：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss
	 * @param endDate
	 *            结束时间，格式：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public List<String> getExistTable(DbType dbType, TablePrefix tablePrefix, String startDate, String endDate);
	
	/**
	 * 分表需求：通过identify直接获取表名
	 * @param dbType
	 * @param tablePrefix
	 * @param startDate
	 * @param endDate
	 * @param identify
	 * @return
	 */
	public List<String> getExistTable(DbType dbType, TablePrefix tablePrefix, String startDate, String endDate,String identify);
	
	
	/**
	 * 分表需求：根据开始时间和结束时间和当前时间判断
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
//	List<SplitTableTime> getSplitTableTime(String start_time, String end_time) throws Exception;
	
	/**
	 * 分表需求（identify放在t_sms_account里面）：根据客户账号（clientid）和时间获取存在的表格
	 * @param dbType
	 * @param tablePrefix
	 * @param startDate
	 * @param endDate
	 * @param clientid
	 * @return
	 */
	public List<String> getExistTableForAccess(DbType dbType, TablePrefix tablePrefix, String startDate, String endDate,String clientid);
	
	/**
	 * 分表需求：直接拼接所有的表
	 * @param dbType
	 * @param tablePrefix
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<String> getExistTableForAccess(DbType dbType, TablePrefix tablePrefix, String startDate, String endDate);
	
	
	/**
	 * 分表需求：（identify放在t_sms_channel里面）：根据通道id(cid)和时间获取存在的表格
	 * @param dbType
	 * @param tablePrefix
	 * @param startDate
	 * @param endDate
	 * @param cid
	 * @return
	 */
	public List<String> getExistTableForRecord(DbType dbType, TablePrefix tablePrefix, String startDate, String endDate,String cid);
	
	/**
	 * 分表需求：直接拼接所有的表
	 * @param dbType
	 * @param tablePrefix
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<String> getExistTableForRecord(DbType dbType, TablePrefix tablePrefix, String startDate, String endDate);
	
	/**
	 * 分表需求（identify放在t_sms_client_identify_log里面）：根据客户账号和时间获取存在的表格
	 * 
	 * @param dbType  数据库
	 * @param tablePrefix  查询表格前缀
	 * @param startDate  开始时间，格式：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss
	 * @param endDate  结束时间，格式：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss
	 * @param clientid 客户id
	 * @return
	 * @throws Exception 
	 */
	List<String> getExistTableForSplitTable(DbType dbType,TablePrefix tablePrefix, String startDate, String endDate,String clientid) throws Exception;
	
	/**
	 * 查询备份通道数
	 * @return
	 */
	Map<String, Object> getBackUpChannelNum();
	
	/**
	 * 按运营商查询通道
	 * @return
	 */
	List<Map<String, Object>> getChannelByOperator(Map<String, String> params);
	/**
	 * @Description: 获取系统参数
	 * @author: Niu.T 
	 * @date: 2016年10月14日 下午3:54:12  
	 * @param paramKey
	 * @return: Map<String,Object>
	 */
	Map<String,Object> getSysParams(String paramKey);
	
	/**
	 * 获取代理商统一分钟最大的订单前缀
	 * @param orderIdPre
	 * @return
	 */
	String getOemAgentOrderTheMostNumForMinute(String orderIdPre);
	
	/**
	 * 获取oem客户订单id序号的最大值
	 * @param orderIdPre
	 * @return
	 */
	String getOemClientOrderTheMostNumForMinute(String orderIdPre);
}
