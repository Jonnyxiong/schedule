package com.ucpaas.sms.service.userconfig;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 用户短信通道管理-用户管理
 * 
 * @author zenglb
 */
public interface UserService {
	PageContainer query(Map<String, String> params);

	List<Map<String, Object>> queryAll(Map<String, String> params);

	Map<String, Object> view(Map<String, Object> params);
	
	Map<String, Object> viewSmsOverRateConfig();

	Map<String, Object> save(Map<String, String> params);

	Map<String, Object> updateStatus(Map<String, String> params);

	/**
	 * 删除用户
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> delete(Map<String, String> params);
	

	/**
	 * 根据sid返回已报备的签名列表
	 * @param params
	 * @return
	 */
	Map<String, Object> getSignList(Map<String, String> params);
	
	/**
	 * 根据扩展端口类型返回当前扩展端口分配号
	 * @param params
	 * @return
	 *//*
	Map<String, Object> getExtendPortByExtendType(Map<String, String> params);*/
	
	/**
	 * 查看子账号是否存在
	 * @param params
	 * @return
	 */
	Map<String, Object> queryIsClientExist(Map<String, String> params);
	
	/**
	 * 根据sid查询已经配置的短信类型
	 * @param params
	 * @return
	 */
	Map<String, Object> getSmsTypeBySid(Map<String, String> params);
	
	
	/**
	 * 查询clientid的短信协议类型和短信类型
	 * @param params
	 * @return
	 */
	Map<String, Object> getSmsFromAndTypeByClientId(Map<String, String> params);
	
	/**
	 * 根据运营商类型查询通道组
	 * @param operater
	 * @return
	 */
	Map<String, Object> queryAllChannelGroup();
	
}
