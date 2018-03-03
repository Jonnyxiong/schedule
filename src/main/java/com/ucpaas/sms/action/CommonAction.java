package com.ucpaas.sms.action;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 公共业务
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")
public class CommonAction extends BaseAction {
	private static final long serialVersionUID = 9093445490209630379L;
	
	@Autowired
	private CommonService commonService;

	@Action("/common/getBackUpChannelNum")
	public void getBackUpChannelNum() {
		data = commonService.getBackUpChannelNum();
		StrutsUtils.renderJson(data);
	}
	
	@Action("/common/getChannelByOperator")
	public void getChannelByOperator() {
		dataList = commonService.getChannelByOperator(StrutsUtils.getFormData());
		StrutsUtils.renderJson(dataList);
	}
	
}
