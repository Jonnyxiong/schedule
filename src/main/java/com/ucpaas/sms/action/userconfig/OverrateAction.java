package com.ucpaas.sms.action.userconfig;

import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.R;
import com.ucpaas.sms.constant.LogConstant;

import com.ucpaas.sms.enums.LogEnum;
import com.jsmsframework.common.util.BeanUtil;
import com.jsmsframework.schUserConf.entity.JsmsOverrate;
import com.jsmsframework.schUserConf.service.JsmsOverrateService;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.LogService;


import com.ucpaas.sms.util.CommonUtil;

import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户配置-用户超频设置
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/userconfig/overrate/query.jsp"),
		@Result(name = "view", location = "/WEB-INF/content/userconfig/overrate/view.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/userconfig/overrate/edit.jsp") })
public class OverrateAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	@Autowired
	private JsmsOverrateService jsmsOverrateService;
	@Autowired
	private LogService logService;


	@Action("/overrate/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();

		StrutsUtils.setAttribute("startTime", params.get("startTime"));
		StrutsUtils.setAttribute("endTime",  params.get("endTime"));
		JsmsPage<JsmsOverrate> params1= CommonUtil.initJsmsPage(params);
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.putAll(params);
		objectMap.put("overrateMode",1);
		params1.setParams(objectMap);
		params1.setOrderByClause("overRate_updatetime desc");
		jpage=jsmsOverrateService.queryList(params1);
	//	page = userService.query(StrutsUtils.getFormData());
		return "query";
	}



	@Action("/overrate/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");

		if (StringUtils.isNotBlank(id)) {
			JsmsOverrate over=jsmsOverrateService.getById(Integer.valueOf(id));
			data = BeanUtil.beanToMap(over,false);
		} else {
			data =new HashMap<String, Object>();
		}
		return "edit";
	}
	@Action("/overrate/view")
	public String view() {
		String id = StrutsUtils.getParameterTrim("id");

		if (StringUtils.isNotBlank(id)) {
			JsmsOverrate over=jsmsOverrateService.getById(Integer.valueOf(id));
			data = BeanUtil.beanToMap(over,false);
		} else {
			data =new HashMap<String, Object>();
		}
		return "view";
	}
	/**
	 * 组装dto
	 * @param params
	 * @return
	 */
	private  JsmsOverrate paramsToDto(Map<String,String> params){
		Map<String,Object> param=new HashedMap();
		JsmsOverrate over=new JsmsOverrate();
		param.putAll(params);
		BeanUtil.mapToBean(param,over);
		over.setOverrateMode(1);
		over.setSmstype(-1);
		if(over.getState()==null){
			over.setState(1);
		}

		over.setOverRateUpdatetime(new Date());
		return over;
	}


	/**
	 * 编辑or添加
	 */
	@Action("/overrate/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		Map<String ,Object> param=new HashedMap();
		param.putAll(params);
		R r;
		if(params.get("id")!=null){
			param.put("userid",param.get("userid_bak"));
			params.put("userid",params.get("userid_bak"));
			Map<String, Object> check=jsmsOverrateService.checkExist(param);
			if(check!=null){
				r=R.error("已存在针对此用户账号和此签名关键字超频限制记录,请勿重复添加！");
			}else {
				Integer u=jsmsOverrateService.update(paramsToDto(params));
				if(u>0){
					r=R.ok("编辑关键字超频限制设置成功！");
					logService.add(LogConstant.LogType.update, LogEnum.用户配置.getValue(), "用户配置-关键字超频限制设置：编辑关键字超频限制设置成功",params, params.get("id"));
				}else {
					r=R.error("编辑关键字超频限制设置失败！");
					logService.add(LogConstant.LogType.update, LogEnum.用户配置.getValue(), "用户配置-关键字超频限制设置：编辑关键字超频限制设置失败",params,params.get("id"));
				}
			}



		}else {
			Map<String, Object> check=jsmsOverrateService.checkExist(param);
			if(check!=null){
				r=R.error("已存在针对此用户账号和此签名关键字超频限制记录,请勿重复添加！");
			}else {
			Integer a=jsmsOverrateService.insert(paramsToDto(params));
			if(a>0){
				r=R.ok("添加关键字超频限制设置成功！");
				logService.add(LogConstant.LogType.add, LogEnum.用户配置.getValue(), "用户配置-关键字超频限制设置：添加关键字超频限制设置成功",params);
			}else {
				r=R.error("添加关键字超频限制设置失败！");
				logService.add(LogConstant.LogType.add, LogEnum.用户配置.getValue(), "用户配置-关键字超频限制设置：添加关键字超频限制设置失败",params);
			}
			}
		}


		StrutsUtils.renderJson(r);
	}

	@Action("/overrate/updateStatus")
	public void updateStatus() {
	//	data = userService.updateStatus(StrutsUtils.getFormData());
		Map<String, String> params = StrutsUtils.getFormData();
		R r;
		Integer u=jsmsOverrateService.updateSelective(paramsToDto(params));
		if(u>0){
			r=R.ok("关键字超频限制设置状态成功！");
			logService.add(LogConstant.LogType.update, LogEnum.用户配置.getValue(), "用户配置-关键字超频限制设置：关键字超频限制设置状态成功",params, params.get("id"));
		}else {
			r=R.error("关键字超频限制设置状态失败！");
			logService.add(LogConstant.LogType.update, LogEnum.用户配置.getValue(), "用户配置-关键字超频限制设置：关键字超频限制设置状态失败",params,params.get("id"));
		}

		StrutsUtils.renderJson(r);
	}
	
	/**
	 * 删除用户
	 */
	@Action("/overrate/delete")
	public void delete() {
		Map<String, String> params = StrutsUtils.getFormData();
		R r=jsmsOverrateService.delOverrate(Integer.valueOf(params.get("id")));
		if(r.getCode() ==0){
			logService.add(LogConstant.LogType.delete, LogEnum.用户配置.getValue(), "用户配置-关键字超频限制设置：删除关键字超频限制设置成功", StrutsUtils.getFormData(), StrutsUtils.getFormData().get("id"));
		}else {
			logService.add(LogConstant.LogType.delete, LogEnum.用户配置.getValue(), "用户配置-关键字超频限制设置：删除关键字超频限制设置失败", StrutsUtils.getFormData(), StrutsUtils.getFormData().get("id"));
		}

		StrutsUtils.renderJson(r);
	}

	/**
	 * 根据sid返回已报备的签名列表
	 */
	@Action("/overrate/getSignList")
	public void getSignList() {
	//	data = userService.getSignList(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	/*@Action("/user/getExtendPortByExtendType")
	public void getExtendPort(){
		data = userService.getExtendPortByExtendType(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}*/
	
	@Action("/overrate/queryIsClientExist")
	public void queryIsClientExist(){
	//	data = userService.queryIsClientExist(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 查询sid已经配置的短信类型
	 */
	@Action("/overrate/getSmsTypeBySid")
	public void getSmsTypeBySid(){
	//	data = userService.getSmsTypeBySid(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	
	/**
	 * 查询子账号的短信协议类型和短信类型
	 */
	@Action("/overrate/getSmsFromAndTypeByClientId")
	public void getSmsFromAndTypeByClientId(){
	//	data = userService.getSmsFromAndTypeByClientId(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	
	/**
	 * 根据运营商类型查询通道组
	 */
	@Action("/overrate/queryAllChannelGroup")
	public void queryAllChannelGroup(){
		
	//	data = userService.queryAllChannelGroup();
		StrutsUtils.renderJson(data);
	}
	
}
