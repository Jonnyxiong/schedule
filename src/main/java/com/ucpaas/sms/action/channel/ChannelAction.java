package com.ucpaas.sms.action.channel;

import com.jsmsframework.channel.entity.JsmsChannelAttributeRealtimeWeight;
import com.jsmsframework.channel.entity.JsmsChannelAttributeWeightConfig;
import com.jsmsframework.channel.enums.ConfigExValueEnum;
import com.jsmsframework.channel.service.JsmsChannelAttributeRealtimeWeightService;
import com.jsmsframework.channel.service.JsmsChannelAttributeWeightConfigService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.ResultVO;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.constant.LogConstant;
import com.ucpaas.sms.entity.message.Channel;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.service.channel.ChannelService;
import com.ucpaas.sms.util.JsonUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户短信通道管理-短信通道配置
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/channel/channel/query.jsp"),
		@Result(name = "queryHistory", location = "/WEB-INF/content/channel/channel/queryHistory.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/channel/channel/edit.jsp") })
public class ChannelAction extends BaseAction {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelAction.class);
	private static final long serialVersionUID = -1234214414349439397L;
	private ChannelService channelService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private LogService logService;
	@Autowired
	private JsmsChannelAttributeRealtimeWeightService jsmsChannelAttributeRealtimeWeightService;
	@Autowired
	private JsmsChannelAttributeWeightConfigService jsmsChannelAttributeWeightConfigService;
	@Resource
	public void setChannelService(ChannelService channelService) {
		this.channelService = channelService;
	}

	@Action("/channel/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		if(params.get("state") == null){
			params.put("state", "1"); // 默认选择 state： 1 开启
		}
		page = channelService.query(params);
		StrutsUtils.setAttribute("search_state", params.get("state"));
		return "query";
	}
	
	@Action("/channel/queryHistory")
	public String queryHistory() {
		Map<String, String> params = StrutsUtils.getFormData();
		params.put("state", "3"); // 下架通道
		page = channelService.query(params);
		return "queryHistory";
	}

	@Action("/channel/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			data = channelService.view(Integer.parseInt(id));
			data.put("flag", "update");
		} else {
			String identify = commonService.getSysParams("DEFAULT_IDENTIFY").get("param_value").toString();
			data = new HashMap<String, Object>();
			data.put("identify", identify);
			data.put("flag", "create");
		}
		String max_identify = commonService.getSysParams("MAX_RECORD_IDENTIFY").get("param_value").toString();
		data.put("max_identify", max_identify);
		return "edit";
	}

	@Action("/channel/save")
	public void save() {
		data = channelService.save(StrutsUtils.getFormData());
		updateReatWeightByCidAndPrice(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/channel/updateStatus")
	public void updateStatus() {
		Map<String, String> params = StrutsUtils.getFormData();
		
		String id = params.get("id");
		String channelId = params.get("channelId");
		String currentState = params.get("currentState");
		String switch2State = params.get("switch2State");
		
		data = channelService.updateStatus(id, channelId, currentState, switch2State);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channel/getIdentifyByChannelId")
	public void getIdentifyByChannelId() throws Exception {
		data = channelService.getIdentifyByChannelId(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * （下架前）查询通道信息：<br>
	 * 1、包含当前通道的通道组数量 <br>
	 * 2、强制路由通道管理中包含当前通道的配置的数量<br>
	 */
	@Action("/channel/queryOffLineCheckInfo")
	public void queryOffLineCheckInfo(){
		String channelId = StrutsUtils.getParameterTrim("channelId");
		data = channelService.queryOffLineCheckInfo(channelId);
		StrutsUtils.renderJson(data);
	}


	@Action("/channel/list")
	public void channelList() {
		Map<String, String> params = StrutsUtils.getFormData();
        List<Channel> channels = channelService.channelList(params);
        StrutsUtils.renderJson(ResultVO.successDefault(channels));
	}


	/**
	 * 新增or更新通道对应实时权重记录
	 * @param params
	 */
	private void updateReatWeightByCidAndPrice(Map<String,String> params){


		LOGGER.info("----------新增or更新通道对应实时权重记录开始--------------");
		JsmsChannelAttributeRealtimeWeight weight=new JsmsChannelAttributeRealtimeWeight();

		/*更新通道属性实时权重数据*/
		JsmsChannelAttributeRealtimeWeight weight1=jsmsChannelAttributeRealtimeWeightService.getByChannelId(Integer.valueOf(params.get("cid")));



		String otype=params.get("operatorstype");
		//移动号码对应通道价格权重
		BigDecimal ydPriceWeight=BigDecimal.ZERO;
		// 联通号码对应通道价格权重， 小数点后2位，取值范围：[0，100]
		BigDecimal ltPriceWeight=BigDecimal.ZERO;
		// 电信号码对应通道价格权重， 小数点后2位，取值范围：[0，100]
		BigDecimal dxPriceWeight=BigDecimal.ZERO;

		BigDecimal yzRateWeight=BigDecimal.ZERO;

		BigDecimal tzRateWeight=BigDecimal.ZERO;

		BigDecimal yxRateWeight=BigDecimal.ZERO;

		BigDecimal gjRateWeight=BigDecimal.ZERO;
		//价格
		BigDecimal price=BigDecimal.valueOf(Double.valueOf(params.get("costprice")));
		JsmsPage jpages=new JsmsPage();
		jpages.setRows(9999);
		jpages=jsmsChannelAttributeWeightConfigService.queryList(jpages);
		List<JsmsChannelAttributeWeightConfig> configs=jpages.getData();


		weight.setUnitPrice(price);
		Map<String, Object> rdata = new HashMap<String, Object>();
		rdata.put("result", "pass");
		rdata.put("msg", "更新通道对应实时权重记录无更新内容！");
		weight.setChannelid(Integer.valueOf(params.get("cid")));
		weight.setUpdator(AuthorityUtils.getLoginUserId());
		weight.setUpdateDate(new Date());

		LOGGER.info("----------新增or更新通道对应实时权重记录区间属性判断开始--------------");
		for (JsmsChannelAttributeWeightConfig config : configs) {

			if(config.getType()==1){
				if(price.compareTo(config.getStartLine())!=-1 && price.compareTo(config.getEndLine())==-1){
					if("0".equals(otype)){//全网

						if(Objects.equals(ConfigExValueEnum.移动.getValue(), config.getExValue())){
							ydPriceWeight=config.getWeight();
							weight.setYdPriceWeight(ydPriceWeight);
						}else  if(Objects.equals(ConfigExValueEnum.联通.getValue(), config.getExValue())){
							ltPriceWeight=config.getWeight();
							weight.setLtPriceWeight(ltPriceWeight);
						}else if(Objects.equals(ConfigExValueEnum.电信.getValue(), config.getExValue())){
							dxPriceWeight=config.getWeight();
							weight.setDxPriceWeight(dxPriceWeight);
						}

					}else if("1".equals(otype) && Objects.equals(ConfigExValueEnum.移动.getValue(), config.getExValue())){//移动

						ydPriceWeight=config.getWeight();
						weight.setYdPriceWeight(ydPriceWeight);

					}else  if("2".equals(otype) && Objects.equals(ConfigExValueEnum.联通.getValue(), config.getExValue())){//联通

						ltPriceWeight=config.getWeight();
						weight.setLtPriceWeight(ltPriceWeight);

					}else  if("3".equals(otype) && Objects.equals(ConfigExValueEnum.电信.getValue(), config.getExValue())){//电信

						dxPriceWeight=config.getWeight();
						weight.setDxPriceWeight(dxPriceWeight);

					}


				}else if(price.compareTo(BigDecimal.valueOf(1))==0 && price.compareTo(config.getEndLine())==0){
					if("0".equals(otype)){//全网

						if(Objects.equals(ConfigExValueEnum.移动.getValue(), config.getExValue())){
							ydPriceWeight=config.getWeight();
							weight.setYdPriceWeight(ydPriceWeight);
						}else  if(Objects.equals(ConfigExValueEnum.联通.getValue(), config.getExValue())){
							ltPriceWeight=config.getWeight();
							weight.setLtPriceWeight(ltPriceWeight);
						}else if(Objects.equals(ConfigExValueEnum.电信.getValue(), config.getExValue())){
							dxPriceWeight=config.getWeight();
							weight.setDxPriceWeight(dxPriceWeight);
						}

					}else if("1".equals(otype) && Objects.equals(ConfigExValueEnum.移动.getValue(), config.getExValue())){//移动

						ydPriceWeight=config.getWeight();
						weight.setYdPriceWeight(ydPriceWeight);

					}else  if("2".equals(otype) && Objects.equals(ConfigExValueEnum.联通.getValue(), config.getExValue())){//联通

						ltPriceWeight=config.getWeight();
						weight.setLtPriceWeight(ltPriceWeight);

					}else  if("3".equals(otype) && Objects.equals(ConfigExValueEnum.电信.getValue(), config.getExValue())){//电信

						dxPriceWeight=config.getWeight();
						weight.setDxPriceWeight(dxPriceWeight);

					}

				}
			}else if(config.getType()==0){
				//创建通道是对应成功率 100%
				if(config.getEndLine().compareTo(BigDecimal.valueOf(100))==0){
					if(Objects.equals(ConfigExValueEnum.验证码.getValue(),config.getExValue())){
						yzRateWeight=config.getWeight();
					}else  if(Objects.equals(ConfigExValueEnum.通知.getValue(),config.getExValue())){
						tzRateWeight=config.getWeight();
					}else  if(Objects.equals(ConfigExValueEnum.营销.getValue(),config.getExValue())){
						yxRateWeight=config.getWeight();
					}else  if(Objects.equals(ConfigExValueEnum.告警.getValue(),config.getExValue())){
						gjRateWeight=config.getWeight();
					}
				}

			}




		}
		LOGGER.info("----------新增or更新通道对应实时权重记录区间属性判断结束--------------");
		if(null!=weight1){

			Integer update=jsmsChannelAttributeRealtimeWeightService.updateWeightByChannelId(weight);
			if (update > 0){
				rdata.put("result", "success");
				rdata.put("msg", "更新通道对应实时权重记录成功！");
			} else {
				rdata.put("result", "fail");
				rdata.put("msg", "更新通道对应实时权重记录失败！");
			}

		}else{
			weight.setSuccessRate(BigDecimal.valueOf(100));
			weight.setYzSuccessWeight(yzRateWeight);
			weight.setTzSuccessWeight(tzRateWeight);
			weight.setYxSuccessWeight(yxRateWeight);
			weight.setGjSuccessWeight(gjRateWeight);
			Integer add=jsmsChannelAttributeRealtimeWeightService.insert(weight);
			if (add > 0) {
				rdata.put("result", "success");
				rdata.put("msg", "添加通道对应实时权重记录成功！");
			} else {
				rdata.put("result", "fail");
				rdata.put("msg", "添加通道对应实时权重记录失败！");
			}

		}
		LOGGER.info("----------新增or更新通道对应实时权重记录结束--------------");
		logService.add(LogConstant.LogType.add, LogEnum.通道管理.getValue(), "通道配置-通道配置：更新短信通道对应实时权重记录", JsonUtils.toJson(weight), rdata);

	}
}
