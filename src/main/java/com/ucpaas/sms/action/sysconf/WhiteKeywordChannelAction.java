package com.ucpaas.sms.action.sysconf;


import com.jsmsframework.channel.entity.JsmsWhiteKeywordChannel;
import com.jsmsframework.channel.service.JsmsChannelgroupService;
import com.jsmsframework.channel.service.JsmsWhiteKeywordChannelService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.R;
import com.jsmsframework.common.util.BeanUtil;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.constant.LogConstant;
import com.ucpaas.sms.enums.LogEnum;
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
 * 系统设置-白关键字强制路由通道管理
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/whitekeyword/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/whitekeyword/edit.jsp")})
public class WhiteKeywordChannelAction extends BaseAction {

	private static final long serialVersionUID = -8447433011736150428L;

	@Autowired
	private JsmsWhiteKeywordChannelService jsmsWhiteKeywordChannelService;

	@Autowired
	private JsmsChannelgroupService jsmsChannelgroupService;

	@Autowired
	private LogService logService;

	@Action("/sysConf/whitekeyword/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		JsmsPage<JsmsWhiteKeywordChannel> params1= CommonUtil.initJsmsPage(params);
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.putAll(params);
		params1.setParams(objectMap);
		params1.setOrderByClause("update_time desc");
		JsmsPage queryPage=jsmsWhiteKeywordChannelService.queryList(params1);
		page = CommonUtil.converterJsmsPage2PageContainer(queryPage);
		StrutsUtils.setAttribute("search_sign", params.get("sign"));
		StrutsUtils.setAttribute("search_whiteKeyword", params.get("whiteKeyword"));
		StrutsUtils.setAttribute("search_clientCode", params.get("clientCode"));
		StrutsUtils.setAttribute("channel_id_search", params.get("channelId"));
		StrutsUtils.setAttribute("search_send_type", params.get("sendType"));
		StrutsUtils.setAttribute("search_status", params.get("status"));
		return "query";
	}	
	
	@Action("/sysConf/whitekeyword/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {

			JsmsWhiteKeywordChannel white=jsmsWhiteKeywordChannelService.getById(Integer.valueOf(id));
			data= BeanUtil.beanToMap(white,false);
		//	data = segmentChannelService.editView(id);
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}


	/**
	 * 组装dto
	 * @param params
	 * @return
	 */
	private  JsmsWhiteKeywordChannel paramsToDto(Map<String,String> params){
		Map<String,Object> param=new HashedMap();
		JsmsWhiteKeywordChannel white=new JsmsWhiteKeywordChannel();
		param.putAll(params);
		BeanUtil.mapToBean(param,white);
		if(white.getStatus()==null){
			white.setStatus(1);
		}

		white.setUpdateTime(new Date());
		return white;
	}

	/**
	 * 新增/编辑白关键字强制路由通道保存
	 */
	@Action("/sysConf/whitekeyword/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
	//	boolean isEdit = false;
		R r;
		if(params.get("sign")==null && params.get("whiteKeyword")==null){
		//	r.setCode(500);
			r=R.error("签名和白关键字至少需要填写一项!");
		}else {
			if(params.get("id")!=null){
			//	isEdit=true;
				//校验唯一
				if(jsmsWhiteKeywordChannelService.isExistWhitekeyChannel(paramsToDto(params))){
					r=R.error("已存在白关键字强制路由通道记录,请勿重复提交!");
				}else {
					Integer u=jsmsWhiteKeywordChannelService.update(paramsToDto(params));
					if(u>0){
						r=R.ok("编辑白关键字强制通道成功！");
						logService.add(LogConstant.LogType.update, LogEnum.系统配置.getValue(), "系统配置-白关键字强制路由通道：编辑白关键字强制路由通道成功", params, params.get("id"));
					}else {
						r=R.error("编辑白关键字强制通道失败！");
						logService.add(LogConstant.LogType.update, LogEnum.系统配置.getValue(), "系统配置-白关键字强制路由通道：编辑白关键字强制路由通道失败", params, params.get("id"));
					}
				}

			}else {

				if(jsmsWhiteKeywordChannelService.isExistWhitekeyChannel(paramsToDto(params))){
					r=R.error("已存在白关键字强制路由通道记录,请勿重复提交!");
				}else{
					Integer a=jsmsWhiteKeywordChannelService.insert(paramsToDto(params));
					if(a>0){
						r=R.ok("添加白关键字强制路由通道成功！");
						logService.add(LogConstant.LogType.add, LogEnum.系统配置.getValue(), "系统配置-白关键字强制路由通道：新增白关键字强制路由通道成功", params);
					}else {
						r=R.error("添加白关键字强制通道失败！");
						logService.add(LogConstant.LogType.add, LogEnum.系统配置.getValue(), "系统配置-白关键字强制路由通道：新增白关键字强制路由通道失败", params);
					}
				}

			}

		}


		StrutsUtils.renderJson(r);
	}
	
	@Action("/sysConf/whitekeyword/delete")
	public void delete() {
		Map<String, String> params = StrutsUtils.getFormData();
		R r=jsmsWhiteKeywordChannelService.delWhitekeyChannel(Integer.valueOf(params.get("id")));
		if(r.getCode() ==0){
			logService.add(LogConstant.LogType.delete, LogEnum.系统配置.getValue(), "系统配置-白关键字强制路由通道：删除白关键字强制路由通道状态成功", StrutsUtils.getFormData(), StrutsUtils.getFormData().get("id"));
		}else {
			logService.add(LogConstant.LogType.delete, LogEnum.系统配置.getValue(), "系统配置-白关键字强制路由通道：删除白关键字强制路由通道状态失败", StrutsUtils.getFormData(), StrutsUtils.getFormData().get("id"));
		}

		StrutsUtils.renderJson(r);
	}
	
	@Action("/sysConf/whitekeyword/updateStatus")
	public void updateStatus() {
		R r;

		Integer i=jsmsWhiteKeywordChannelService.updateSelective(paramsToDto(StrutsUtils.getFormData()));
		if(i>0){
			r=R.ok("更新白关键字强制通道状态成功！");
			logService.add(LogConstant.LogType.update, LogEnum.系统配置.getValue(), "系统配置-白关键字强制路由通道：更新白关键字强制路由通道状态成功", StrutsUtils.getFormData(), StrutsUtils.getFormData().get("id"));
		}else {
			r=R.error("更新白关键字强制通道状态失败！");
			logService.add(LogConstant.LogType.update, LogEnum.系统配置.getValue(), "系统配置-白关键字强制路由通道：编辑白关键字强制路由通道失败",StrutsUtils.getFormData(), StrutsUtils.getFormData().get("id"));
		}

		StrutsUtils.renderJson(r);
	}
	
	@Action("/sysConf/whitekeyword/queryOperatorstypeByChannelId")
	public void queryOperatorstypeByChannelId() {
		String channelId = StrutsUtils.getParameterTrim("channelId");
		if(StringUtils.isNotBlank(channelId)){
			data = jsmsChannelgroupService.queryOperatorstypeByChannelId(Integer.valueOf(channelId));
		}else{
			data = new HashMap<>();
		}
		StrutsUtils.renderJson(data);
	}
	
}
