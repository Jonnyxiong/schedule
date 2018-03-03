/**    
 * @Title: CommonSeqService.java  
 * @Package: com.ucpaas.sms.service  
 * @Description: TODO
 * @author: Niu.T    
 * @date: 2016年9月6日 下午4:19:49  
 * @version: V1.0    
 */
package com.ucpaas.sms.service;

import com.ucpaas.sms.enums.ClientIdType;

/**  
 * @ClassName: CommonSeqService  
 * @Description: 提供公共的clientid生成序列
 * @author: Niu.T 
 * @date: 2016年9月6日 下午4:19:49  
 */
public interface CommonSeqService {
	
	/**
	 * @Description
	 * 		根据子账号类型获取clientId
	 * 		<br>如果t_sms_clientid_sequence表中存在可用的直接返回一个clientId，否则创建一万个新的clientId再返回
	 * @author
	 * 		Niu.T 
	 * @date
	 * 		2016年9月6日 下午4:21:00
	 * @return
	 * 		String
	 */
	public String getClientIdByType(ClientIdType clientIdType);
	
	/**
	 * @Description  修改clientid状态为1，表示已经使用
	 * @param clientId
	 */
	public boolean updateClientIdStatus(String clientId);

}
