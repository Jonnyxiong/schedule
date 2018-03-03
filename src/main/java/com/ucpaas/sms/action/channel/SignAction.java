package com.ucpaas.sms.action.channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.jsmsframework.user.entity.JsmsAccount;
import com.jsmsframework.user.service.JsmsAccountService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.channel.SignService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 通道组管理-固签签名扩展号管理
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/channel/sign/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/channel/sign/edit.jsp") })
public class SignAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	private SignService signService;
	@Autowired
	private JsmsAccountService jsmsAccountService;
	@Resource
	public void setSignService(SignService signService) {
		this.signService = signService;
	}

	@Action("/sign/query")
	public String query() {
		page = signService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/sign/edit")
	public String edit() {
		String[] ids;
		if(StrutsUtils.getParameterTrim("id")!=null){
			ids = StrutsUtils.getParameterTrim("id").split(",");
			Map<String,Object> signs=new HashedMap();
			Map<String,Object> sign=new HashedMap();
			for (int i = 0; i < ids.length; i++) {

				if (NumberUtils.isDigits(ids[i])) {
					sign = signService.view(Integer.parseInt(ids[i]));
					if(signs==null||signs.isEmpty()){
						signs.putAll(sign);
					}else {
						signs.put("id",signs.get("id").toString()+","+sign.get("id").toString());
						signs.put("clientid",signs.get("clientid").toString()+","+sign.get("clientid").toString());
					}
				} else {
					data = new HashMap<String, Object>();
				}
			}
			data=signs;

		}else{
			data = new HashMap<String, Object>();
		}

		return "edit";
	}

	@Action("/sign/save")
	public void save() {
		data = signService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sign/checkSignExist")
	public void checkSignExist(){
		data = signService.checkSignExist(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sign/checkAppendIdExist")
	public void checkAppendIdExist(){
		data = signService.checkAppendIdExist(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/sign/delete")
	public void delete() {
		data = signService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sign/channelAppendIdCheck1")
	public void channelAppendIdCheck1() {
		data = signService.channelAppendIdCheck1(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sign/channelAppendIdCheck2")
	public void channelAppendIdCheck2() {
		data = signService.channelAppendIdCheck2(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/sign/channelAppendIdCheck3")
	public void channelAppendIdCheck3() {
		data = signService.channelAppendIdCheck3(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	/**
	 * 查询所有的客户
	 */
	@Action("/sign/accounts")
	public void getAccounts() {
		String clientId = StrutsUtils.getParameterTrim("clientId");
		List<Map<String, Object>> clientIds=jsmsAccountService.findALLAccountForSearch();
		Map<String,Object> data1=new HashedMap();
		data1.put("clientData",clientIds);
		data=data1;
		StrutsUtils.renderJson(data);
	}
}
