package com.ucpaas.sms.action.channel;


import com.jsmsframework.channel.entity.JsmsChannelPoolPolicy;
import com.jsmsframework.channel.service.JsmsChannelPoolPolicyService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.R;
import com.jsmsframework.common.util.BeanUtil;
import com.jsmsframework.common.util.StringUtils;

import com.jsmsframework.user.entity.JsmsAccount;

import com.jsmsframework.user.entity.JsmsUser;
import com.jsmsframework.user.service.JsmsUserService;
import com.ucpaas.sms.action.BaseAction;

import com.ucpaas.sms.constant.LogConstant;
import com.ucpaas.sms.dto.JsmsChannelPoolPolicyDTO;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.service.LogService;

import com.ucpaas.sms.util.CommonUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.collections.map.HashedMap;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import java.math.BigDecimal;
import java.util.*;

/**
 * 通道管理-通道池策略
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/channel/channelPoolPolicy/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/channel/channelPoolPolicy/edit.jsp") })
public class ChannelPoolPolicyAction extends BaseAction {
	private static final long serialVersionUID = -123421441434943456L;

	@Autowired
	private JsmsChannelPoolPolicyService jsmsChannelPoolPolicyService;


	@Autowired
	private JsmsUserService jsmsUserService;

	@Autowired
	private LogService logService;

	@Action("/channelPoolPolicy/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		JsmsPage<JsmsChannelPoolPolicy> params1= CommonUtil.initJsmsPage(params);
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.putAll(params);
		params1.setParams(objectMap);
	//	params1.setOrderByClause("update_time desc");
		jpage=jsmsChannelPoolPolicyService.queryList(params1);
		List<JsmsChannelPoolPolicy>  data1=jpage.getData();
		List<JsmsChannelPoolPolicyDTO> datau=new ArrayList<>();
		for (JsmsChannelPoolPolicy jsmsChannelPoolPolicy : data1) {
			JsmsChannelPoolPolicyDTO dto=new JsmsChannelPoolPolicyDTO();
			BeanUtil.copyProperties(jsmsChannelPoolPolicy,dto);
			JsmsUser acc=jsmsUserService.getById(String.valueOf(jsmsChannelPoolPolicy.getUpdator()));
			dto.setUpdateName(acc.getRealname());
			datau.add(dto);
			//JsmsAccount acc=jsmsAccountService.getById()
		}
		jpage.setData(datau);
		//	StrutsUtils.setAttribute("search_state", params.get("state"));
		return "query";
	}
	


	@Action("/channelPoolPolicy/edit")
	public String edit() {
		String policyId = StrutsUtils.getParameterTrim("policyId");
		if (StringUtils.isNotBlank(policyId)) {

			JsmsChannelPoolPolicy pool=jsmsChannelPoolPolicyService.getByPolicyId(Long.valueOf(policyId));
			data= BeanUtil.beanToMap(pool,false);
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
	private  JsmsChannelPoolPolicy paramsToDto(Map<String,String> params){
		Map<String,Object> param=new HashedMap();
		JsmsChannelPoolPolicy pool=new JsmsChannelPoolPolicy();
		param.putAll(params);
		BeanUtil.mapToBean(param,pool);

		pool.setAntiComplaintWeight(pool.getAntiComplaintWeight()==null? BigDecimal.ZERO:pool.getAntiComplaintWeight());
		pool.setCustomerRelationWeight(pool.getCustomerRelationWeight()==null? BigDecimal.ZERO:pool.getCustomerRelationWeight());
		pool.setLowConsumeWeight(pool.getLowConsumeWeight()==null?  BigDecimal.ZERO:pool.getLowConsumeWeight());
		pool.setSuccessWeight(pool.getSuccessWeight()==null?BigDecimal.ZERO:pool.getSuccessWeight());
		pool.setPriceWeight(pool.getPriceWeight()==null?BigDecimal.ZERO:pool.getPriceWeight());



		pool.setUpdator(AuthorityUtils.getLoginUserId());
		pool.setUpdateDate(new Date());
		return pool;
	}


	@Action("/channelPoolPolicy/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		//	boolean isEdit = false;
		R r;
//		if(params.get("sign")==null && params.get("whiteKeyword")==null){
//			//	r.setCode(500);
//			r=R.error("签名和白关键字至少需要填写一项!");
//		}else {
		//更新默认为否
		if("1".equals(params.get("isDefault"))){
			int d=jsmsChannelPoolPolicyService.updatedefault();
			if(d>0){
				r=R.ok("修改默认策略成功！");
				logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道池策略配置：修改默认策略成功", params, params.get("policyId"));
			}else {
				r=R.error("修改默认策略失败！");
				logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道池策略配置：修改默认策略失败", params, params.get("policyId"));
			}
		}

		if(params.get("policyId")!=null){
			//	isEdit=true;
			//校验唯一
//				if(jsmsWhiteKeywordChannelService.isExistWhitekeyChannel(paramsToDto(params))){
//					r=R.error("已存在白关键字强制路由通道记录,请勿重复提交!");
//				}else {
			Integer u=jsmsChannelPoolPolicyService.update(paramsToDto(params));
			if(u>0){
				r=R.ok("编辑通道池策略配置成功！");
				logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道池策略配置：编辑通道池策略配置成功", params, params.get("policyId"));
			}else {
				r=R.error("编辑通道池策略配置失败！");
				logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道池策略配置：编辑通道池策略配置失败", params, params.get("policyId"));
			}
//				}

		}else {

//				if(jsmsWhiteKeywordChannelService.isExistWhitekeyChannel(paramsToDto(params))){
//					r=R.error("已存在白关键字强制路由通道记录,请勿重复提交!");
//				}else{
			Integer a=jsmsChannelPoolPolicyService.insert(paramsToDto(params));
			if(a>0){
				r=R.ok("新增通道池策略配置成功！");
				logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道管理-通道池策略配置：新增通道池策略配置成功", params);
			}else {
				r=R.error("新增通道池策略配置失败！");
				logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道管理-通道池策略配置：新增通道池策略配置失败", params);
			}
//				}

		}

//		}


		StrutsUtils.renderJson(r);
	}
//
//	@Action("/channel/updateStatus")
//	public void updateStatus() {
//		Map<String, String> params = StrutsUtils.getFormData();
//
//		String id = params.get("id");
//		String channelId = params.get("channelId");
//		String currentState = params.get("currentState");
//		String switch2State = params.get("switch2State");
//
//		data = channelService.updateStatus(id, channelId, currentState, switch2State);
//		StrutsUtils.renderJson(data);
//	}
//
//	@Action("/channel/getIdentifyByChannelId")
//	public void getIdentifyByChannelId() throws Exception {
//		data = channelService.getIdentifyByChannelId(StrutsUtils.getFormData());
//		StrutsUtils.renderJson(data);
//	}
//
//	/**
//	 * （下架前）查询通道信息：<br>
//	 * 1、包含当前通道的通道组数量 <br>
//	 * 2、强制路由通道管理中包含当前通道的配置的数量<br>
//	 */
//	@Action("/channel/queryOffLineCheckInfo")
//	public void queryOffLineCheckInfo(){
//		String channelId = StrutsUtils.getParameterTrim("channelId");
//		data = channelService.queryOffLineCheckInfo(channelId);
//		StrutsUtils.renderJson(data);
//	}
//
//
//	@Action("/channel/list")
//	public void channelList() {
//		Map<String, String> params = StrutsUtils.getFormData();
//        List<Channel> channels = channelService.channelList(params);
//        StrutsUtils.renderJson(ResultVO.successDefault(channels));
//	}
//
}
