package com.ucpaas.sms.action.channel;

import com.jsmsframework.channel.entity.JsmsChannelAttributeRealtimeWeight;
import com.jsmsframework.channel.entity.JsmsChannelAttributeWeightConfig;
import com.jsmsframework.channel.enums.ConfigExValueEnum;
import com.jsmsframework.channel.service.JsmsChannelAttributeRealtimeWeightService;
import com.jsmsframework.channel.service.JsmsChannelAttributeWeightConfigService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.R;
import com.jsmsframework.common.dto.ResultVO;
import com.jsmsframework.common.util.BeanUtil;
import com.jsmsframework.common.util.StringUtils;
import com.jsmsframework.user.entity.JsmsUser;
import com.jsmsframework.user.service.JsmsUserService;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.constant.LogConstant;
import com.ucpaas.sms.dto.JsmsChannelRTWeightDTO;
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
 * 用户短信通道管理-通道属性实时权重表
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/channel/channelRealTimeWeight/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/channel/channelRealTimeWeight/edit.jsp") })
public class ChannelRealTimeWeightAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439452L;

	@Autowired
	private JsmsChannelAttributeRealtimeWeightService jsmsChannelAttributeRealtimeWeightService;
	@Autowired
	private JsmsUserService jsmsUserService;
	@Autowired
	private JsmsChannelAttributeWeightConfigService jsmsChannelAttributeWeightConfigService;

	@Autowired
	private LogService logService;


	@Action("/channelRealTimeWeight/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		JsmsPage<JsmsChannelAttributeRealtimeWeight> params1= CommonUtil.initJsmsPage(params);
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.putAll(params);
		params1.setParams(objectMap);
		//	params1.setOrderByClause("update_time desc");
		jpage=jsmsChannelAttributeRealtimeWeightService.queryList(params1);
		List<JsmsChannelAttributeRealtimeWeight>  data1=jpage.getData();
		List<JsmsChannelAttributeRealtimeWeight> datau=new ArrayList<>();
		for (JsmsChannelAttributeRealtimeWeight weight : data1) {
			JsmsChannelRTWeightDTO dto=new JsmsChannelRTWeightDTO();
			BeanUtil.copyProperties(weight,dto);
			if(weight.getUpdator()==0L){
				dto.setUpdateName("系统自动更新");
			}else {
				JsmsUser acc=jsmsUserService.getById(String.valueOf(weight.getUpdator()));
				dto.setUpdateName(acc.getRealname());
			}

			datau.add(dto);
			//JsmsAccount acc=jsmsAccountService.getById()
		}
		jpage.setData(datau);
		return "query";
	}
	


	@Action("/channelRealTimeWeight/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (StringUtils.isNotBlank(id)) {

			JsmsChannelAttributeRealtimeWeight weight=jsmsChannelAttributeRealtimeWeightService.getById(Long.valueOf(id));
			data= BeanUtil.beanToMap(weight,false);
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
	private  JsmsChannelAttributeRealtimeWeight paramsToDto(Map<String,String> params){
		Map<String,Object> param=new HashedMap();
		JsmsChannelAttributeRealtimeWeight weight=new JsmsChannelAttributeRealtimeWeight();
		param.putAll(params);
		BeanUtil.mapToBean(param,weight);

		Integer exFlag=0;
		if(params.get("exFlag")!=null){
			String[] exFlags=params.get("exFlag").split(",");
			for (int i = 0; i < exFlags.length; i++) {
				exFlag +=Integer.valueOf(exFlags[i]);
			}
		}

		weight.setLowConsumeLimit(weight.getLowConsumeLimit()==null? BigDecimal.ZERO:weight.getLowConsumeLimit());
		weight.setSuccessRate(weight.getSuccessRate()==null? BigDecimal.ZERO:weight.getSuccessRate());
		weight.setAntiComplaint(weight.getAntiComplaint()==null?  BigDecimal.ZERO:weight.getAntiComplaint());

		weight.setExFlag(exFlag);
		weight.setUpdator(AuthorityUtils.getLoginUserId());
		weight.setUpdateDate(new Date());
		return weight;
	}


	@Action("/channelRealTimeWeight/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();

		R r;

		if(params.get("id")!=null){
			//判断成功率是否变化 to 更新对应区间权重
			String old=params.get("oldsuccessRate");
			String news=params.get("successRate")==null?"0":params.get("successRate");
			BigDecimal success=BigDecimal.valueOf(Double.valueOf(news));

			JsmsChannelAttributeRealtimeWeight weight=paramsToDto(params);
			if(!old.equals(news)){
				List<JsmsChannelAttributeWeightConfig> configs=jsmsChannelAttributeWeightConfigService.queryAllBySmSType();
				for (JsmsChannelAttributeWeightConfig config : configs) {
					if(success.compareTo(config.getStartLine())!=-1 && success.compareTo(config.getEndLine())==-1){
						if(Objects.equals(ConfigExValueEnum.验证码.getValue(), config.getExValue())){
							weight.setYzSuccessWeight(config.getWeight());
						}else if(Objects.equals(ConfigExValueEnum.通知.getValue(), config.getExValue())){
							weight.setTzSuccessWeight(config.getWeight());
						}else if(Objects.equals(ConfigExValueEnum.营销.getValue(), config.getExValue())){
							weight.setYxSuccessWeight(config.getWeight());
						}else  if(Objects.equals(ConfigExValueEnum.告警.getValue(),config.getExValue())){
							weight.setGjSuccessWeight(config.getWeight());
						}

					}else if(success.compareTo(new BigDecimal(100)) ==0  && config.getEndLine().compareTo(success)==0){
						if(Objects.equals(ConfigExValueEnum.验证码.getValue(), config.getExValue())){
							weight.setYzSuccessWeight(config.getWeight());
						}else if(Objects.equals(ConfigExValueEnum.通知.getValue(), config.getExValue())){
							weight.setTzSuccessWeight(config.getWeight());
						}else if(Objects.equals(ConfigExValueEnum.营销.getValue(), config.getExValue())){
							weight.setYxSuccessWeight(config.getWeight());
						}else  if(Objects.equals(ConfigExValueEnum.告警.getValue(),config.getExValue())){
							weight.setGjSuccessWeight(config.getWeight());
						}

					}

				}



			}

			Integer u=jsmsChannelAttributeRealtimeWeightService.update(weight);
			if(u>0){
				r=R.ok("编辑通道属性实时权重成功！");
				logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道属性实时权重：编辑通道属性实时权重成功", params, params.get("id"));
			}else {
				r=R.error("编辑通道属性实时权重失败！");
				logService.add(LogConstant.LogType.update, LogEnum.通道管理.getValue(), "通道管理-通道属性实时权重：编辑通道属性实时权重失败", params, params.get("id"));
			}


		}else {
			r=R.error("该实时成功率权重不存在！");


		}


		StrutsUtils.renderJson(r);
	}


	
}
