
package com.ucpaas.sms.action.account;

import com.jsmsframework.common.dto.ResultVO;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.enums.ClientIdType;
import com.ucpaas.sms.exception.ClientInfoException;
import com.ucpaas.sms.exception.SmspBusinessException;
import com.ucpaas.sms.service.CommonSeqService;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.service.account.AccountService;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户管理
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/account/query.jsp"),
		@Result(name = "edit", location="/WEB-INF/content/account/edit.jsp")})

public class AccountAction extends BaseAction {
	
	private static final long serialVersionUID = 8262904242938393755L;
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CommonSeqService commonSeqService;

	@Autowired
	private CommonService commonService;
	
	@Action("/account/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = accountService.query(params);
		StrutsUtils.setAttribute("statusSearch", params.get("status"));
		return "query";
	}
	
	@Action("/account/edit")
	public String edit() {
		String account = StrutsUtils.getParameterTrim("clientid");
		String smstype = StrutsUtils.getParameterTrim("smstype");
		String max_identify = commonService.getSysParams("MAX_ACCESS_IDENTIFY").get("param_value").toString();
		if (StringUtils.isNoneBlank(account)) {
			data = accountService.view(account, smstype);
			data.put("flag","update");
		} else {
			String identify = commonService.getSysParams("DEFAULT_IDENTIFY").get("param_value").toString();
			data = new HashMap<String, Object>();
			data.put("identify", identify);
			data.put("flag","create");
		}
		data.put("max_identify", max_identify);
		return "edit";
	}
	
	@Action("/account/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		try {
			params.put("adminId", AuthorityUtils.getLoginUserId().toString());
			data = accountService.save(params, "create");
		} catch (ClientInfoException e) {
			data = new HashMap<String, Object>();
			data.put("result", "fail");
			data.put("msg", e.getMessage());
		}catch (SmspBusinessException e) {
			data = new HashMap<String, Object>();
			data.put("result", "fail");
			data.put("msg", e.getMessage());
		}
		StrutsUtils.renderJson(data);
	}

	@Action("/account/updateClientLabel")
	public void updateClientLabel() {
		Map<String, String> params = StrutsUtils.getFormData();
		ResultVO resultVO;
		try {
			params.put("adminId", AuthorityUtils.getLoginUserId().toString());
			resultVO = accountService.updateClientLabel(params, "create");
		} catch (ClientInfoException e) {
			resultVO = ResultVO.failure(e.getMessage());
		}catch (SmspBusinessException e) {
			resultVO = ResultVO.failure(e.getMessage());
		}
		StrutsUtils.renderJson(resultVO);
	}

	@Action("/account/update")
	public void update() {
		Map<String, String> params = StrutsUtils.getFormData();
		try {
			params.put("adminId", AuthorityUtils.getLoginUserId().toString());
			data = accountService.save(params, "update");
		} catch (ClientInfoException e) {
			data = new HashMap<String, Object>();
			data.put("result", "fail");
			data.put("msg", e.getMessage());
		} catch (SmspBusinessException e) {
			data = new HashMap<String, Object>();
			data.put("result", "fail");
			data.put("msg", e.getMessage());
		}
		StrutsUtils.renderJson(data);
	}
	
	@Action("/account/updateStatus")
	public void updateStatus() {
		data = accountService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/account/isClientChannelGroupAssign")
	public void isClientChannelGroupAssign() {
		data = accountService.isClientChannelGroupAssign(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/account/generateClientId")
	public void generateClientId(){
		Map<String, Object> clientIdMap = new HashMap<String, Object>();
		Integer clientIdType = Integer.valueOf(StrutsUtils.getParameterTrim("clientIdType"));
		String clientId = commonSeqService.getClientIdByType(ClientIdType.getInstance(clientIdType));
		clientIdMap.put("clientId", clientId);
		data = clientIdMap;
		StrutsUtils.renderJson(data);
	}
	
	@Action("/account/getIdentifyByClientId")
	public void getIdentifyByChannelId() throws Exception {
		data = accountService.getIdentifyByClientId(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 根据扩展端口类型查询当前可用的扩展端口
	 * @throws Exception
	 */
	@Action("/account/getExtendPortByExtendType")
	public void getExtendPortByExtendType() throws Exception {
		data = accountService.getExtendPortByExtendType(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/account/validateAgentIdType")
	public void validateAgentIdType() throws Exception {
		data = accountService.validateAgentIdType(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/account/getAgentBelongSale")
	public void getAgentBelongSale() throws Exception {
		String agentId = StrutsUtils.getParameterTrim("agentId");
		data = accountService.getAgentBelongSale(agentId);
		StrutsUtils.renderJson(data);
	}
	
}
