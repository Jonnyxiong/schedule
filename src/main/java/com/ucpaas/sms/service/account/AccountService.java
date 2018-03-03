/**
 * 
 */
package com.ucpaas.sms.service.account;

import java.util.Map;

import com.jsmsframework.common.dto.ResultVO;
import com.ucpaas.sms.model.PageContainer;

/**
 * 账户管理
 */
public interface AccountService {
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);
	
	/**
	 * 显示账户信息
	 * @param params
	 * @return
	 */
	Map<String, Object> view(String account, String smstype);
	
	/**
	 * 添加/修改
	 * @param params
	 * @return
	 */
	Map<String, Object> save(Map<String, String> params, String oprate);

	/**
	 * 添加/修改
	 * @param params
	 * @return
	 */
	ResultVO updateClientLabel(Map<String, String> params, String oprate);

	/**
	 * 更新账户状态
	 * @param params
	 * @return
	 */
	Map<String, Object> updateStatus(Map<String, String> params);
	
	/**
	 * 判断子账号是否配置通道组路由
	 * @param params
	 * @return
	 */
	Map<String, Object> isClientChannelGroupAssign(Map<String, String> params);
	
	/**
	 * 查询clientId对应的identify
	 * @param params
	 * @return
	 */
	Map<String, Object> getIdentifyByClientId(Map<String, String> params);

	/**
	 * 根据扩展端口类型查询当前可用的扩展端口
	 * @param formData
	 * @return
	 */
	Map<String, Object> getExtendPortByExtendType(Map<String, String> formData);

	/**
	 * 校验子账号切换代理商后与老的代理商是否类型相同
	 * @param formData
	 * @return
	 */
	Map<String, Object> validateAgentIdType(Map<String, String> formData);

	Map<String, Object> getAgentBelongSale(String agentId);

	Map<String, Object>  getOneByClientId(String clientId);


}
