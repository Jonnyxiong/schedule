package com.ucpaas.sms.service.channel;

import com.jsmsframework.channel.entity.JsmsChannelAttributeRealtimeWeight;
import com.jsmsframework.channel.entity.JsmsChannelAttributeWeightConfig;
import com.jsmsframework.channel.enums.ConfigExValueEnum;
import com.jsmsframework.channel.service.JsmsChannelAttributeRealtimeWeightService;
import com.jsmsframework.channel.service.JsmsChannelAttributeWeightConfigService;
import com.jsmsframework.channel.service.JsmsChannelAttributeWeightConfigServiceImpl;
import com.jsmsframework.common.dto.JsmsPage;
import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.entity.message.Channel;
import com.ucpaas.sms.enums.ChannelOperate;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.mapper.message.ChannelMapper;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.model.rabbit.QueueBindings;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.service.componentConf.MqConfigService;
import com.ucpaas.sms.util.JsonUtils;
import com.ucpaas.sms.util.rabbitmq.RabbitMqConsoleUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;




/**
 * 用户短信通道管理-短信通道配置
 * 
 * @author zenglb
 */
@Service
public class ChannelServiceImpl implements ChannelService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelServiceImpl.class);

	@Autowired
	private MessageMasterDao messageMaster;
	@Autowired
	private LogService logService;
	@Autowired
	private MqConfigService mqConfigService;
	@Autowired
	private ChannelMapper channelMapper;


	@Override
	public PageContainer query(Map<String, String> params) {
		PageContainer page = messageMaster.getSearchPage("channel.query", "channel.queryCount", params);

		// 查询“归属商务”信息
		for (Map<String, Object> row : page.getList()) {
			String belongBusiness = Objects.toString(row.get("belong_business"), "");
			String belongBusinessName = "";
			if (StringUtils.isNotBlank(belongBusiness)) {
				belongBusinessName = messageMaster.getOneInfo("channel.queryBelongBusinessName", belongBusiness);
			}
			
			row.put("belong_business_name", belongBusinessName);
			
			String mqId = Objects.toString(row.get("mq_id"), "");
			String mqIdName = "";
			if (StringUtils.isNotBlank(mqId)) {
				mqIdName = messageMaster.getOneInfo("channel.queryMqQueueName", mqId);
			}
			row.put("mq_id_name", mqIdName);
		}
		return page;
	}

	@Override
	public Map<String, Object> view(int logId) {
		return messageMaster.getOneInfo("channel.view", logId);
	}

	@Override
	@Transactional
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存通道，添加/修改：" + params);

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> check = messageMaster.getOneInfo("channel.checkSave", params);// 查重
		if (check != null) {
			data.put("result", "fail");
			data.put("msg", "通道号已被使用，请重新输入");
			return data;
		}
		String id = params.get("id");
		
		// 拼接通道的发送时间区间
		String sendtimearea_start_time = Objects.toString(params.get("sendtimearea_start_time"), "");
		String sendtimearea_end_time = Objects.toString(params.get("sendtimearea_end_time"), "");
		String sendtimearea = sendtimearea_start_time + "|" + sendtimearea_end_time;
		params.put("sendtimearea", sendtimearea);
		String[] exvalues=params.get("exvalue").split(",");
		Integer exvalue=0;
		for (int i = 0; i < exvalues.length; i++) {
			exvalue +=Integer.valueOf(exvalues[i]);
		}
		params.put("exvalue",exvalue.toString());
		if (StringUtils.isBlank(id)) {// 添加
			// 创建MQ队列并获得成功返回的mqId TODO
			String channelId = Objects.toString(params.get("cid"), "");
			String mqId = this.creaMqQueue(channelId);
			if(StringUtils.isBlank(mqId)){
				throw new RuntimeException("创建通道对应Mq队列失败");
			}
			params.put("mq_id", mqId);
			
			int i = messageMaster.insert("channel.insertChannel", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "添加成功");
				
				// 保存省网通道信息
				this.saveChannelSegcode(params);
				
			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
			}
			logService.add(LogType.add,LogEnum.通道管理.getValue(), "通道配置-通道配置：添加短信通道", params, data);



		} else {// 修改
			int i = messageMaster.update("channel.updateChannel", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "修改成功");
				
				// 保存省网通道信息
				this.saveChannelSegcode(params);
			} else {
				data.put("result", "fail");
				data.put("msg", "短信通道不存在，修改失败");
			}
			logService.add(LogType.update,LogEnum.通道管理.getValue(), "通道配置-通道配置：修改短信通道", params, data);




		}


		// 此处id用来页面页面回写，必须
		data.put("id", params.get("id"));
		return data;
	}



	/**
	 * 根据当前通道号创建Mq队列<br>
	 *  MQ中间件ID：使用中间件类型为“MQ_SEND_IO”对应的中间件ID；<br>
	 *  MQ队列：channel_通道号；<br>
	 *  MQ交换：channel_通道号；<br>
	 *  MQ路由KEY：channel_通道号；<br>
	 *  名称：channel_通道号；<br>
	 *  消息类型：通道消息；<br>
	 *  描述：channel_通道号；<br>
	 * @param channelId
	 * @return
	 */
	private String creaMqQueue(String channelId) {
		
		String name = "channel_" + channelId;
		// 查询中间件类型是 MQ_send_io 的中间件Id
		String middlewareId = messageMaster.getOneInfo("channel.querySendIoMiddlewareId", null);
		
		Map<String, String> mqQueueParam = new HashMap<String, String>();
		mqQueueParam.put("mq_id", null);
		mqQueueParam.put("mq_queue", name);
		mqQueueParam.put("mq_exchange", name);
		mqQueueParam.put("mq_routingkey", name);
		mqQueueParam.put("middleware_id", middlewareId);
		mqQueueParam.put("queue_name", name);
		mqQueueParam.put("message_type", "20"); // 消息类型：通道消息
		mqQueueParam.put("remark", name);
		
		Map<String, Object> result = mqConfigService.save(mqQueueParam);
		if(result != null && result.get("result").equals("success")){
			// 创建成功返回mqId
			LOGGER.debug("创建通道{}的Mq消息队列成功,队列名称={}, mq_id = {}", channelId, name, result.get("mq_id"));
			return String.valueOf(result.get("mq_id"));
		}else{
			return null;
		}
	}

	/**
	 * 根据省网类型更新t_sms_segcode_channel表中的记录
	 * @param params
	 */
	private void saveChannelSegcode(Map<String, String> params){
		Map<String, Object> segCodeInfo = new HashMap<String, Object>();
		segCodeInfo.put("cid", params.get("cid"));
		segCodeInfo.put("segcode", params.get("segcode"));
		
		String segcodeType = Objects.toString(params.get("segcode_type"), "0"); // 路由类型，0：全国 1：省网
		messageMaster.delete("channel.deleteChannelSegcode", segCodeInfo);
		if("1".equals(segcodeType)){
			messageMaster.insert("channel.insertChannelSegcode", segCodeInfo);
		}else{
			// 路由类型为“全国”类型时不存在记录
		}
	}

	
	/**
	 * 切换通道状态
	 */
	@Override
	@Transactional
	public Map<String, Object> updateStatus(String id, String channelId, String currentState, String switch2State) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		switch2State = Objects.toString(switch2State, "0");
		ChannelOperate channelOperate = ChannelOperate.getOperate(Integer.valueOf(switch2State));
		LOGGER.debug("更新通道状态操作开始，通道号 = {}，操作员 = {}", channelId, AuthorityUtils.getLoginRealName());
		
		Map<String, Object> switchResult = null;
		switch (channelOperate) {
			case CLOSE:
				break;
			case OPEN:
				switchResult = openChannel(channelId);
				break;
			case SUSPEND:
				break;
			case OFF_LINE:
				switchResult = offLineChannel(channelId);
				break;
			case PUSH_ONLINE:
				switchResult = pushChannelOnLine(channelId);
				break;
			default:
				switchResult = null;
				break;
		}
		
		// 切换通道状态时处理失败
		if(switchResult != null){
			return switchResult;
		}
		
		// 重新上架的通道将通道状态置为关闭状态
		if(ChannelOperate.PUSH_ONLINE.equals(channelOperate)){
			switch2State =  ChannelOperate.CLOSE.getValue().toString();
		}
		
		// 切换通道状态的所有处理完成以后修改t_sms_channel表中的state
		// 0：关闭，1：开启，2：暂停，3：下架
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("id", id);
		sqlParam.put("switch2State", switch2State);
		int i = messageMaster.update("channel.updateStatus", sqlParam);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", channelOperate.getName() + "操作成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "短信通道不存在，" + channelOperate.getName() + "失败");
		}
		
		logService.add(LogType.update, LogEnum.通道管理.getValue(),"用户通道配置-通道配置：修改短信通道状态", channelOperate.getName(), channelId, data);
		return data;
	}
	
	/**
	 * 开启通道<br>
	 * 1、校验通道绑定的MQ队列是否存在
	 * @param channelId
	 * @return
	 */
	private Map<String, Object> openChannel(String channelId){
		Map<String, Object> data = new HashMap<String, Object>();
		// 查询MQ队列详细信息
		Map<String, Object> queueInfo = messageMaster.getOneInfo("channel.queryMqQueueInfo", channelId);
		if(queueInfo == null || queueInfo.get("middleware_id") == null){
			data.put("result", "fail");
			data.put("msg", "开启通道失败，查询通道绑定MQ队列信息不存在");
			LOGGER.debug("开启通道{}失败，查询通道绑定MQ队列信息不存在", channelId);
			return data;
		}
		
		// 查询MQ队列对应的RabbitMQ服务器连接信息
		String middlewareId = queueInfo.get("middleware_id").toString();
		Map<String, Object> mqServerInfo = messageMaster.getOneInfo("channel.queryMqServerInfo", middlewareId);
		
		String mqServerIp = Objects.toString(mqServerInfo.get("host_ip"), "");
		String mqServerPort = Objects.toString(mqServerInfo.get("console_port"), "");
		String mqServerUserName = Objects.toString(mqServerInfo.get("user_name"), "");
		String mqPassword = Objects.toString(mqServerInfo.get("pass_word"), "");
		String mqQueueName = Objects.toString(queueInfo.get("mq_queue"), "");
		if(mqServerInfo == null || StringUtils.isBlank(mqServerIp) || StringUtils.isBlank(mqServerPort)
				 || StringUtils.isBlank(mqServerUserName) || StringUtils.isBlank(mqPassword) || StringUtils.isBlank(mqQueueName)){
			data.put("result", "fail");
			data.put("msg", "开启通道失败，查询MQ服务器信息不存在");
			
			LOGGER.debug("开启通道{}失败，查询MQ服务器信息不存在", channelId);
			return data;
		}
		
		// 查询队列的绑定关系
		QueueBindings queueBindings = RabbitMqConsoleUtil.getQueueBindings(mqServerIp, mqServerPort, mqQueueName, mqServerUserName, mqPassword);
		
		// exchange和routingKey不存在时算队列异常
		if(StringUtils.isBlank(queueBindings.getExchange()) || StringUtils.isBlank(queueBindings.getRoutingKey())){
			data.put("result", "fail");
			data.put("msg", "开启通道失败，通道队列绑定关系不存在");
			
			LOGGER.debug("开启通道{}失败，通道队列绑定关系不存在", channelId);
			return data;
		}
		
		return null;
	}
	
	/**
	 * 重新上架通道<br>
	 * 只创建通道对应的Mq队列并将MqId更新到通道表<br>
	 * @param channelId
	 * @return
	 */
	private Map<String, Object> pushChannelOnLine(String channelId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(channelId)){
			result.put("result", "fail");
			result.put("msg", "操作失败，通道不存在或通道已经被下架");
			return result;
		}
		
		// 重新创建通道对应的Mq队列
		String mqId = this.creaMqQueue(channelId);
		if(StringUtils.isBlank(mqId)){
			LOGGER.debug("重新上架通道{}失败，创建通道对应Mq队列失败", channelId);
			throw new RuntimeException("创建通道对应Mq队列失败");
		}
		
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("channelId", channelId);
		sqlParams.put("mqId", mqId);
		messageMaster.update("channel.updateChannelMqId", sqlParams);
		
		LOGGER.debug("重新上架通道{}，创建通道对应Mq队列成功 mqId={}", channelId, mqId);
		
		return null;
	}
	
	/**
	 * 下架通道时校验
	 * @param channelId
	 * @return
	 */
	private Map<String, Object> offLineChannel(String channelId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(channelId)){
			result.put("result", "fail");
			result.put("msg", "操作失败，通道不存在或通道已经被下架");
			return result;
		}
		
		// 1、删除下架通道的通道组绑定关系
		int channelGroupRefNum = messageMaster.delete("channel.deleteChannelGroupRef", channelId);
		// 2、删除强制路由通道管理中的路由配置
		int segmentChannelNum = messageMaster.delete("channel.deleteSegmentChannel", channelId);
		// 3、删除t_signextno_gw表中相关记录
		int signexGwNum = messageMaster.delete("channel.deleteSignexGw", channelId);
		// 4、删除t_sms_channel_extendport表中相关记录
		int channelExtendportNum = messageMaster.delete("channel.deleteChannelExtendport", channelId);
		// 5、删除t_sms_channel_extendport_assign表中相关记录
		int channelExtendportAssignNum = messageMaster.delete("channel.deleteChannelExtendportAssign", channelId); 
		// 6、删除通道关键字相关记录
		int channelKeywordNum = messageMaster.delete("channel.updateChannelKeywordStatus", channelId);
		// 7、删除通道黑名单相关记录
		int channelBlackListNum = messageMaster.delete("channel.deleteChannelBlackList", channelId);
		// 8、删除MQ_ID对应的通道MQ队列
		String mqId = messageMaster.getOneInfo("channel.queryChannelMqId", channelId);
		Map<String, Object> deleteMqInfo = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(mqId)){
			// 删除 SMSP组件与通道队列的关系，这种关系正常情况不存在，这里防止下架失败所以做删除
			int delNum = messageMaster.delete("channel.deleteMqRelationRef", mqId);
			LOGGER.debug("下架通道{}时，删除SMSP组件与通道队列关系{}条， mqId={}", channelId, delNum, mqId);
			
			deleteMqInfo = mqConfigService.delete(mqId);
			Map<String, Object> sqlParam = new HashMap<String, Object>();
			sqlParam.put("channelId", channelId);
			sqlParam.put("mqId", null);
			messageMaster.update("channel.updateChannelMqId", sqlParam);
			if(deleteMqInfo != null && "fail".equals(deleteMqInfo.get("result"))){
				LOGGER.debug("下架通道{}失败，删除通道MQ队列失败 mqId={}", channelId, mqId);
				throw new RuntimeException("下架通道失败，删除通道MQ队列失败");
			}
		}
		
		LOGGER.debug("下架通道{}前清理数据完成，删除通道组绑定关系{}个，删除通道强制路由配置{}个，删除t_signextno_gw记录{}条，" + System.getProperty("line.separator")
				+ "删除t_sms_channel_extendport表{}记录，删除t_sms_channel_extendport_assign表{}条记录，修改通道关键字{}条记录状态为7"
				+ "删除通道黑名单{}条记录，删除MQ队列结果为 {}", channelId, channelGroupRefNum, segmentChannelNum, signexGwNum, channelExtendportNum, channelExtendportAssignNum
				, channelKeywordNum, channelBlackListNum, deleteMqInfo);
		
		return null;
	}
	
	@Override
	public Map<String, Object> getIdentifyByChannelId(Map<String, String> params) {
		if(null != params && params.get("channelid") != null){
			return messageMaster.getOneInfo("channel.getIdentifyByChannelId", params);
		}
		
		return new HashMap<String, Object>();
	}

	@Override
	public Map<String, Object> queryOffLineCheckInfo(String channelId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 1、包含当前通道的通道组数量
		Integer channelGroupNum = messageMaster.getOneInfo("channel.queryChannelRefChannelGroupNum", channelId);
		result.put("channelGroupNum", channelGroupNum != null ? channelGroupNum.intValue() : 0);
		
		// 2、强制路由通道管理中包含当前通道的配置的数量
		Integer segmentChannelNum = messageMaster.getOneInfo("channel.querySegmentChannelNum", channelId);
		result.put("segmentChannelNum", segmentChannelNum != null ? segmentChannelNum.intValue() : 0);
		return result;
	}
	@Override
	public List<Channel> channelList(Map params){
        params.put("state",1);
        return channelMapper.channelList(params);
	}


}
