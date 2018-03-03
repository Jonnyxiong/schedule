/**
 * 
 */
package com.ucpaas.sms.service.smsAudit;

import com.jsmsframework.audit.enums.AuditPage;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.ResultVO;
import com.ucpaas.sms.entity.message.TestAccount;
import com.ucpaas.sms.enums.AuditPageName;
import com.ucpaas.sms.model.PageContainer;

import java.util.List;
import java.util.Map;

/**
 * 短信审核
 */
public interface SmsAuditService {
	
	/**
	 * 短信审核查询页面
	 * @param params
	 * @return
	 */
	PageContainer auditQueryPage(Map<String, String> params);
	
	/**
	 * 短信审核页面
	 * @param params
	 * @param kind -> AuditPageName YZM_NUM | MAJOR_NUM | ORDINARY_NUM
	 * @return
	 */
//	List<Map<String, Object>> auditPage(String smsType,AuditPageName kind);
	List<Map<String, Object>> auditPage(Map<String, String> params,AuditPageName kind,AuditPage auditPage);
	/**
	 * 获取所有的测试账号
	 * @return List<TestAccount>
	 */
	List<TestAccount> getTestClientid(Map params);

	/**
	 * 更新短信审核状态
	 * @param params   （auditIds/status/userId/transferToId/remark）
	 * @return
	 */
	Map<String, Object> updateStatus(Map<String, String> params);
	Map<String, Object> updateStatus(Map<String, String> params,AuditPageName auditPageName);

	ResultVO testSendAction(Map<String, String> params);

	Map<String, Object> getKindsAuditNum();

	void updateKindsAuditNum();

	void cacheKindsAuditId();

	/**
	 * 查询短信审核超时时间
	 * @return
	 */
	Map<String, Object> getAuditExpireTime();
	
	/**
	 * 查询待审核记录数
	 * @return
	 */
	Map<String, Object> getNeedAuditNum();

	/**
	 * 短信历史审核查询
	 * @param page
	 * @return
	 */
	JsmsPage hisauditQueryPage(JsmsPage page);
	/**
	 * 短信历史备份库审核查询
	 * @param page
	 * @return
	 */
	JsmsPage hisbakauditQueryPage(JsmsPage page);


	/**
	 * 查询历史审核记录
	 * @param params
	 * @return
	 */
	List<Map<String ,Object>> queryhisAll(Map<String,String> params);

	/**
	 * 查询历史审核记录
	 * @param params
	 * @return
	 */
	List<Map<String ,Object>> queryhisbakAll(Map<String,String> params);


	/**
	 * 设置审核过期
	 * @param auditId
	 * @return
	 */
	Map<String, Object> setAuditExpired(String auditId);
}
