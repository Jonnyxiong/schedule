package com.ucpaas.sms.action.channel;

import com.jsmsframework.channel.entity.JsmsChannel;
import com.jsmsframework.channel.entity.JsmsChannelAttributeRealtimeWeight;
import com.jsmsframework.channel.entity.JsmsChannelAttributeWeightConfig;
import com.jsmsframework.channel.enums.ConfigExValueEnum;
import com.jsmsframework.channel.service.JsmsChannelAttributeRealtimeWeightService;
import com.jsmsframework.channel.service.JsmsChannelAttributeWeightConfigService;
import com.jsmsframework.channel.service.JsmsChannelService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.R;
import com.jsmsframework.common.dto.ResultVO;

import com.jsmsframework.common.enums.OperatorType;
import com.jsmsframework.common.util.BeanUtil;
import com.jsmsframework.common.util.StringUtils;
import com.jsmsframework.user.entity.JsmsUser;
import com.jsmsframework.user.service.JsmsUserService;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.constant.LogConstant;
import com.ucpaas.sms.dto.JsmsChannelAWConfigDTO;
import com.ucpaas.sms.entity.message.Channel;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.service.channel.ChannelService;
import com.ucpaas.sms.util.CommonUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 通道组管理-通道属性区间权重配置管理
 * 
 * @author Don
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/channel/channelConfig/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/channel/channelConfig/edit.jsp") })
public class ChannelConfigAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439399L;


	@Autowired
	private JsmsChannelAttributeWeightConfigService jsmsChannelAttributeWeightConfigService;

	@Autowired
	private JsmsUserService jsmsUserService;
	@Autowired
	private JsmsChannelService jsmsChannelService;
	@Autowired
	private JsmsChannelAttributeRealtimeWeightService jsmsChannelAttributeRealtimeWeightService;
	@Autowired
	private LogService logService;



	@Action("/channelConfig/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		JsmsPage<JsmsChannelAttributeWeightConfig> params1= CommonUtil.initJsmsPage(params);
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.putAll(params);
		params1.setParams(objectMap);
		params1.setOrderByClause("update_date desc");
		jpage=jsmsChannelAttributeWeightConfigService.queryList1(params1);
		List<JsmsChannelAttributeWeightConfig> config=jpage.getData();
		List<JsmsChannelAWConfigDTO> nconfig=new ArrayList<>();
		for (JsmsChannelAttributeWeightConfig con : config) {
			JsmsChannelAWConfigDTO newc=new JsmsChannelAWConfigDTO();
			BeanUtil.copyProperties(con,newc);
//			if(con.getType()==0 && con.getEndLine().compareTo(BigDecimal.valueOf(100))==0){
//				newc.setRegion("["+con.getStartLine()+","+con.getEndLine()+"]");
//			}else if(con.getType()==1 && con.getEndLine().compareTo(BigDecimal.valueOf(1))==0) {
//				newc.setRegion("["+con.getStartLine()+","+con.getEndLine()+"]");
//			}else {
//				newc.setRegion("["+con.getStartLine()+","+con.getEndLine()+")");
//			}

			JsmsUser user=jsmsUserService.getById(String.valueOf(con.getUpdator()));
			newc.setUpdateName(user.getRealname());

			nconfig.add(newc);

		}
		jpage.setData(nconfig);

		return "query";
	}
	


	@Action("/channelConfig/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {

			JsmsChannelAttributeWeightConfig config=jsmsChannelAttributeWeightConfigService.getById(Integer.valueOf(id));
			data= BeanUtil.beanToMap(config,false);

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
	private  JsmsChannelAttributeWeightConfig paramsToDto(Map<String,String> params){
		Map<String,Object> param=new HashedMap();
		JsmsChannelAttributeWeightConfig config=new JsmsChannelAttributeWeightConfig();
		param.putAll(params);
		BeanUtil.mapToBean(param,config);

		config.setUpdator(AuthorityUtils.getLoginUserId());
		config.setUpdateDate(new Date());
		return config;
	}



	@Action("/channelConfig/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();

		R r=new R();

			//if(params.get("id")!=null){

				//先删除旧记录
				if(StringUtils.isNotBlank(params.get("oldid"))){
					String[] oids=params.get("oldid").split(",");

					for (int i = 0; i < oids.length; i++) {

						jsmsChannelAttributeWeightConfigService.delete(Integer.valueOf(oids[i]));
					}
				}
					params.put("type",params.get("otype"));
					params.put("exValue",params.get("oexValue"));
					params.put("id","");
					String[] starts=params.get("startLine").split(",");
					String[] ends=params.get("endLine").split(",");
					String[] weights=params.get("weight").split(",");
					BigDecimal allweight=BigDecimal.ZERO;
					BigDecimal lastendline=BigDecimal.ZERO;
					for (int i = 0; i < starts.length; i++) {
						params.put("startLine",starts[i]);
						params.put("endLine",ends[i]);
						params.put("weight",weights[i]);

						JsmsChannelAttributeWeightConfig newConfig=paramsToDto(params);


						Integer ua=jsmsChannelAttributeWeightConfigService.insert(newConfig);
						if(ua>0){
							if(params.get("id")!=null) {
								r = R.ok("编辑通道属性区间权重配置成功！");
								logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：编辑通道属性区间权重配置成功", params, params.get("id"));
							}else {
								r=R.ok("新增通道属性区间权重配置成功！");
								logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：新增通道属性区间权重配置成功", params);
							}

						}else {
							if(params.get("id")!=null) {
								r = R.error("编辑通道属性区间权重配置失败！");
								logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：编辑通道属性区间权重配置失败", params, params.get("id"));
							}else {
								r=R.error("新增通道属性区间权重配置失败！");
								logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：新增通道属性区间权重配置失败", params);

							}

						}
						allweight=allweight.add(newConfig.getWeight());
						lastendline=newConfig.getEndLine();
						//更新价格区间权重+短信类型区间

							updateRealWeightByRange(params);

					}


				//成功率不满区间新增至100权重
				if("0".equals(params.get("type")) && lastendline.compareTo(BigDecimal.valueOf(100))!=0){
					params.put("startLine",lastendline.toString());
					params.put("endLine","100");
					params.put("weight",BigDecimal.valueOf(100).subtract(allweight).toString());
					Integer tohun=jsmsChannelAttributeWeightConfigService.insert(paramsToDto(params));
					if(tohun>0){
						if(params.get("id")!=null) {
						//	r = R.ok("编辑通道属性区间权重增至100配置成功！");
							logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：编辑通道属性区间权重增至100配置成功", params, params.get("id"));
						}else {
						//	r=R.ok("新增通道属性区间权重增至100配置成功！");
							logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：新增通道属性区间权重增至100配置成功", params);
						}

					}else {
						if(params.get("id")!=null) {
						//	r = R.error("编辑通道属性区间权重增至100配置失败！");
							logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：编辑通道属性区间权重增至100配置失败", params, params.get("id"));
						}else {
						//	r=R.error("新增通道属性区间权重增至100配置失败！");
							logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：新增通道属性区间权重增至100配置失败", params);

						}

					}
					updateRealWeightByRange(params);
				}
				//价格不满区间新增至100权重
				if("1".equals(params.get("type")) && lastendline.compareTo(BigDecimal.valueOf(1))!=0){
						params.put("startLine",lastendline.toString());
						params.put("endLine","1");
						params.put("weight",BigDecimal.valueOf(100).subtract(allweight).toString());
					Integer toOne=jsmsChannelAttributeWeightConfigService.insert(paramsToDto(params));
					if(toOne>0){
						if(params.get("id")!=null) {
						//	r = R.ok("编辑通道属性区间权重配置增至1成功！");
							logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：编辑通道属性区间权重增至1配置成功", params, params.get("id"));
						}else {
						//	r=R.ok("新增通道属性区间权重配置增至1成功！");
							logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：新增通道属性区间权重增至1配置成功", params);
						}

					}else {
						if(params.get("id")!=null) {
						//	r = R.error("编辑通道属性区间权重增至1配置失败！");
							logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：编辑通道属性区间权重增至1配置失败", params, params.get("id"));
						}else {
						//	r=R.error("新增通道属性区间权重增至1配置失败！");
							logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道管理-通道属性区间权重配置：新增通道属性区间权重增至1配置失败", params);

						}

					}
					updateRealWeightByRange(params);
				}




		StrutsUtils.renderJson(r);
	}


	/**
	 * //更新价格区间权重+短信类型区间
	 * @param params
	 */
	private void updateRealWeightByRange(Map<String,String> params){

		Integer otype=Integer.valueOf(params.get("exValue"));
		Integer operatorstype=OperatorType.全网.getValue();
		BigDecimal start=BigDecimal.valueOf(Double.valueOf(params.get("startLine")));
		BigDecimal end=BigDecimal.valueOf(Double.valueOf(params.get("endLine")));
		BigDecimal price;
		BigDecimal cweight=BigDecimal.valueOf(Double.valueOf(params.get("weight")));
		BigDecimal rate;
		Map<String, Object> rdata = new HashMap<String, Object>();
		rdata.put("result", "pass");
		rdata.put("msg", "更新通道对应实时权重记录无更新内容！");
//		//移动号码对应通道价格权重
//		BigDecimal ydPriceWeight=BigDecimal.ZERO;
//		// 联通号码对应通道价格权重， 小数点后2位，取值范围：[0，100]
//		BigDecimal ltPriceWeight=BigDecimal.ZERO;
//		// 电信号码对应通道价格权重， 小数点后2位，取值范围：[0，100]
//		BigDecimal dxPriceWeight=BigDecimal.ZERO;
//		// 验证码成功率权重， 小数点后2位，取值范围：[0，100]
//		 BigDecimal yzSuccessWeight;
//		// 通知成功率权重， 小数点后2位，取值范围：[0，100]
//		 BigDecimal tzSuccessWeight=BigDecimal.ZERO;;
//		// 营销成功率权重， 小数点后2位，取值范围：[0，100]
//		 BigDecimal yxSuccessWeight=BigDecimal.ZERO;;
//		// 告警成功率权重， 小数点后2位，取值范围：[0，100]
//		 BigDecimal gjSuccessWeight=BigDecimal.ZERO;;
		JsmsChannelAttributeRealtimeWeight weight=new JsmsChannelAttributeRealtimeWeight();

		weight.setUpdator(AuthorityUtils.getLoginUserId());

		//更新短信类型区间权重
		if("0".equals(params.get("type"))){
			List<JsmsChannelAttributeRealtimeWeight> rtweights=jsmsChannelAttributeRealtimeWeightService.queryAll();
			for (JsmsChannelAttributeRealtimeWeight rtweight : rtweights) {
				rate=rtweight.getSuccessRate();
				if(rate.compareTo(start)!=-1 &&rate.compareTo(end)==-1){
					if(Objects.equals(ConfigExValueEnum.验证码.getValue(), otype)){
						weight.setYzSuccessWeight(cweight);
					}else if(Objects.equals(ConfigExValueEnum.通知.getValue(), otype)){
						weight.setTzSuccessWeight(cweight);
					}else if(Objects.equals(ConfigExValueEnum.营销.getValue(), otype)){
						weight.setYxSuccessWeight(cweight);
					}else if(Objects.equals(ConfigExValueEnum.告警.getValue(), otype)){
						weight.setGjSuccessWeight(cweight);
					}

					weight.setChannelid(rtweight.getChannelid());
					weight.setUpdateDate(new Date());
					Integer update=jsmsChannelAttributeRealtimeWeightService.updateWeightByChannelId(weight);
					if (update > 0){
						rdata.put("result", "success");
						rdata.put("msg", "更新通道对应实时短信类型成功率区间权重记录成功！");
					} else {
						rdata.put("result", "fail");
						rdata.put("msg", "更新通道对应实时短信类型成功率区间权重记录失败！");
					}
					logService.add(LogConstant.LogType.add,LogEnum.通道管理.getValue(), "通道配置-通道属性区间权重配置：更新短信通道对应实时权重记录", weight, rdata);
				}else if(rate.compareTo(BigDecimal.valueOf(100))==0 && rate.compareTo(end)==0){
					if(Objects.equals(ConfigExValueEnum.验证码.getValue(), otype)){
						weight.setYzSuccessWeight(cweight);
					}else if(Objects.equals(ConfigExValueEnum.通知.getValue(), otype)){
						weight.setTzSuccessWeight(cweight);
					}else if(Objects.equals(ConfigExValueEnum.营销.getValue(), otype)){
						weight.setYxSuccessWeight(cweight);
					}else if(Objects.equals(ConfigExValueEnum.告警.getValue(), otype)){
						weight.setGjSuccessWeight(cweight);
					}

					weight.setChannelid(rtweight.getChannelid());
					weight.setUpdateDate(new Date());
					Integer update=jsmsChannelAttributeRealtimeWeightService.updateWeightByChannelId(weight);
					if (update > 0){
						rdata.put("result", "success");
						rdata.put("msg", "更新通道对应实时短信类型成功率区间权重记录成功！");
					} else {
						rdata.put("result", "fail");
						rdata.put("msg", "更新通道对应实时短信类型成功率区间权重记录失败！");
					}
					logService.add(LogConstant.LogType.add,LogEnum.通道管理.getValue(), "通道配置-通道属性区间权重配置：更新短信通道对应实时权重记录", weight, rdata);
				}


			}




		}else if("1".equals(params.get("type"))){
			//更新价格区间权重
			//1.根据 对应所属运营商类型查找所有对应通道号支持的运营商
			if(Objects.equals(ConfigExValueEnum.移动.getValue(), otype)){
				operatorstype= OperatorType.移动.getValue();
			}else if(Objects.equals(ConfigExValueEnum.联通.getValue(), otype)){
				operatorstype=OperatorType.联通.getValue();
			}else if(Objects.equals(ConfigExValueEnum.电信.getValue(), otype)){
				operatorstype=OperatorType.电信.getValue();
			}
			List<JsmsChannel> channels=jsmsChannelService.queryAllByOperatorstype(operatorstype);

			for (JsmsChannel channel : channels) {


				JsmsChannelAttributeRealtimeWeight weight1=jsmsChannelAttributeRealtimeWeightService.getByChannelId(channel.getCid());
//			//价格
				price=channel.getCostprice();
				//比较价格区间 开始 <= 值<结束
				if(price.compareTo(start)!=-1 && price.compareTo(end)==-1){


					if(Objects.equals(OperatorType.全网.getValue(), channel.getOperatorstype())){

						if(Objects.equals(ConfigExValueEnum.移动.getValue(), otype)){
							weight.setYdPriceWeight(cweight);
						}else if(Objects.equals(ConfigExValueEnum.联通.getValue(), otype)){
							weight.setLtPriceWeight(cweight);
						}else if(Objects.equals(ConfigExValueEnum.电信.getValue(), otype)){
							weight.setDxPriceWeight(cweight);
						}


					}else if(Objects.equals(OperatorType.移动.getValue(), channel.getOperatorstype())){
						weight.setYdPriceWeight(cweight);
					}else if(Objects.equals(OperatorType.联通.getValue(), channel.getOperatorstype())){
						weight.setLtPriceWeight(cweight);
					}else if(Objects.equals(OperatorType.电信.getValue(), channel.getOperatorstype())){
						weight.setDxPriceWeight(cweight);
					}
					weight.setChannelid(channel.getCid());

					weight.setUpdateDate(new Date());
					if(null!=weight1){
						Integer update=jsmsChannelAttributeRealtimeWeightService.updateWeightByChannelId(weight);
						if (update > 0){
							rdata.put("result", "success");
							rdata.put("msg", "更新通道对应实时价格区间权重记录成功！");
						} else {
							rdata.put("result", "fail");
							rdata.put("msg", "更新通道对应实时价格区间权重记录失败！");
						}

					}else{
						weight.setUnitPrice(price);
						Integer add=jsmsChannelAttributeRealtimeWeightService.insert(weight);
						if (add > 0) {
							rdata.put("result", "success");
							rdata.put("msg", "添加通道对应实时价格区间权重记录成功！");
						} else {
							rdata.put("result", "fail");
							rdata.put("msg", "添加通道对应实时价格区间权重记录失败！");
						}

					}
					logService.add(LogConstant.LogType.add,LogEnum.通道管理.getValue(), "通道配置-通道属性区间权重配置：更新短信通道对应实时权重记录", weight, rdata);
				}else if(price.compareTo(BigDecimal.valueOf(1))==0 && price.compareTo(end)==0){
					if(Objects.equals(OperatorType.全网.getValue(), channel.getOperatorstype())){

						if(Objects.equals(ConfigExValueEnum.移动.getValue(), otype)){
							weight.setYdPriceWeight(cweight);
						}else if(Objects.equals(ConfigExValueEnum.联通.getValue(), otype)){
							weight.setLtPriceWeight(cweight);
						}else if(Objects.equals(ConfigExValueEnum.电信.getValue(), otype)){
							weight.setDxPriceWeight(cweight);
						}


					}else if(OperatorType.移动.getValue().equals(channel.getOperatorstype())){
						weight.setYdPriceWeight(cweight);
					}else if(Objects.equals(OperatorType.联通.getValue(), channel.getOperatorstype())){
						weight.setLtPriceWeight(cweight);
					}else if(Objects.equals(OperatorType.电信.getValue(), channel.getOperatorstype())){
						weight.setDxPriceWeight(cweight);
					}
					weight.setChannelid(channel.getCid());

					weight.setUpdateDate(new Date());
					if(null!=weight1){
						Integer update=jsmsChannelAttributeRealtimeWeightService.updateWeightByChannelId(weight);
						if (update > 0){
							rdata.put("result", "success");
							rdata.put("msg", "更新通道对应实时价格区间权重记录成功！");
						} else {
							rdata.put("result", "fail");
							rdata.put("msg", "更新通道对应实时价格区间权重记录失败！");
						}

					}else{
						weight.setUnitPrice(price);
						Integer add=jsmsChannelAttributeRealtimeWeightService.insert(weight);
						if (add > 0) {
							rdata.put("result", "success");
							rdata.put("msg", "添加通道对应实时价格区间权重记录成功！");
						} else {
							rdata.put("result", "fail");
							rdata.put("msg", "添加通道对应实时价格区间权重记录失败！");
						}

					}
					logService.add(LogConstant.LogType.add,LogEnum.通道管理.getValue(), "通道配置-通道属性区间权重配置：更新短信通道对应实时权重记录", weight, rdata);

				}



		}


		}

	}


	@Action("/channelConfig/channelConfigCheck")
	public void channelConfigCheck() {
		data = jsmsChannelAttributeWeightConfigService.channelConfigCheck(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}




//	@Action("/channelConfig/save")
//	public void delete() {
//		Map<String, String> params = StrutsUtils.getFormData();
//		R r=jsmsChannelAttributeWeightConfigService.delWhitekeyChannel(Integer.valueOf(params.get("id")));
//		if(r.getCode() ==0){
//			logService.add(LogConstant.LogType.delete, LogEnum.通道管理.getValue(), "系统配置-白关键字强制路由通道：删除白关键字强制路由通道状态成功", StrutsUtils.getFormData(), StrutsUtils.getFormData().get("id"));
//		}else {
//			logService.add(LogConstant.LogType.delete, LogEnum.通道管理.getValue(), "系统配置-白关键字强制路由通道：删除白关键字强制路由通道状态失败", StrutsUtils.getFormData(), StrutsUtils.getFormData().get("id"));
//		}
//
//		StrutsUtils.renderJson(r);
//	}
	
}
