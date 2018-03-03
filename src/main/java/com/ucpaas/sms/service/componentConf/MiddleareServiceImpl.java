package com.ucpaas.sms.service.componentConf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
import com.rabbitmq.client.AMQP.Exchange.DeleteOk;
import com.rabbitmq.client.AMQP.Queue.BindOk;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.model.rabbit.RabbitMqQueue;
import com.ucpaas.sms.util.rabbitmq.RabbitMqConsoleUtil;
import com.ucpaas.sms.util.rabbitmq.RabbitMqServerUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;


@Service
@Transactional
public class MiddleareServiceImpl implements MiddleareService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MiddleareServiceImpl.class);
	
	@Autowired
	private MessageMasterDao messageMasterDao;
	
	private static final String version = "5";
	@SuppressWarnings("serial")
	private static final List<String> mqC2Sio = new ArrayList<String>() {{
	    add("YDhangye"); // 移动行业
	    add("YDyingxiao"); // 移动营销
	    add("LThangye"); 
	    add("LTyingxiao");
	    add("DXhangye");
	    add("DXyingxiao");
	    add("hangye");
	    add("yingxiao");
	    
	    add("ReBackYDhangye");
	    add("ReBackYDyingxiao");
	    add("ReBackLThangye");
	    add("ReBackLTyingxiao");
	    add("ReBackDXhangye");
	    add("ReBackDXyingxiao");
	    
	    add("MoAndRt"); // 上行状态报告
	}};
	
	private final static Map<String, String> mqC2SioMessageType = new HashMap<String, String>() {{
//		01：移动行业，02：移动营销，03：联通行业，04：联通营销，05：电信行业，06：电信营销，07：行业，08：营销，
//		11：异常移动行业，12：异常移动营销，13：异常联通行业，14：异常联通营销，15：异常电信行业，16：异常电信营销，17：通道消息
		put("YDhangye", "01");
		put("YDyingxiao", "02");
		put("LThangye", "03");
		put("LTyingxiao", "04");
		put("DXhangye", "05");
		put("DXyingxiao", "06");
		put("hangye", "07");
		put("yingxiao", "08");
		
		put("ReBackYDhangye", "11");
		put("ReBackYDyingxiao", "12");
		put("ReBackLThangye", "13");
		put("ReBackLTyingxiao", "14");
		put("ReBackDXhangye", "15");
		put("ReBackDXyingxiao", "16");
		
		put("MoAndRt", "21"); // 上行状态报告
	}};
	
	@SuppressWarnings("serial")
	private static final List<String> mqC2sDb = new ArrayList<String>() {{
	    add("C2sDb");
	}};
	
	private final static Map<String, String> mqC2sDbMessageType = new HashMap<String, String>() {{
		put("C2sDb", "00");
	}};
	
	@SuppressWarnings("serial")
	private static final List<String> mqSendDb = new ArrayList<String>() {{
	    add("SendDb");
	}};
	
	private final static Map<String, String> mqSendDbMessageType = new HashMap<String, String>() {{
		put("SendDb", "00");
	}};
	
	@Override
	public PageContainer query(Map<String, String> params) {
		PageContainer page = messageMasterDao.getSearchPage("middlewareConf.query", "middlewareConf.queryCount", params);
		if(null != page && page.getList().size() > 0){
			for (Map<String, Object> middlewareInfo : page.getList()) {
				String middleware_id = String.valueOf(middlewareInfo.get("middleware_id"));
				Integer mqConfigNum = messageMasterDao.getSearchSize("middlewareConf.getMiddlewareMqConfigNum", middleware_id);
				middlewareInfo.put("mqConfigNum", mqConfigNum);
			}
		}
		return page;
	}

	@Override
	public Map<String, Object> editView(int middleware_id) {
		return messageMasterDao.getOneInfo("middlewareConf.getOneInfo", middleware_id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		String updater = AuthorityUtils.getLoginRealName();
		params.put("updater", updater);
		int saveNum;
		
		int num = messageMasterDao.getOneInfo("middlewareConf.saveCheck", params);
		if(num > 0){
			data.put("result", "faile");
			data.put("msg", "保存失败，同一中间件类型只能存在一条记录");
			return data;
		}
		
		// 页面上传过来的middleware_id
		String pageMiddlewareId = Objects.toString(params.get("middleware_id"), "");
		if(StringUtils.isBlank(pageMiddlewareId)){// 新建
			data = generateMiddlewareId(params);
			String generateMid = Objects.toString(data.get("middleware_id"), "");
			if(StringUtils.isBlank(generateMid)){
				return data;// 错误，返回提示信息给页面
			}else{
				pageMiddlewareId = generateMid;
				params.put("middleware_id", generateMid);
				saveNum = messageMasterDao.insert("middlewareConf.insert", params);
			}
			
		}else{// 更新
			
			// 当中间件类型（middleware_type）和中间件地址（host_ip）改变时需要重新生成middleware_id
			boolean isMidNoChange = messageMasterDao.getOneInfo("middlewareConf.isMidNoChange", params);
			params.put("old_middleware_id", pageMiddlewareId);
			if(isMidNoChange){
				saveNum = messageMasterDao.update("middlewareConf.update", params);
			}else{
				data = generateMiddlewareId(params);
				String generateMid = Objects.toString(data.get("middleware_id"), "");
				if(StringUtils.isBlank(generateMid)){
					return data;// 错误，返回提示信息给页面
				}else{
					pageMiddlewareId = generateMid;
					params.put("middleware_id", generateMid);
					saveNum = messageMasterDao.update("middlewareConf.update", params);
					
					// 修改“中间件配置表”时如果middleware_id改变需要更新“MQ配置表”中的middleware_id
					messageMasterDao.update("middlewareConf.updateMqConfTableMiddlewareId", params);
				}
			}
			
		}
		
		if(saveNum == 1){
			data.put("result", "success");
			data.put("middleware_id", pageMiddlewareId);
			data.put("msg", "保存成功");
			return data;
		}else{
			data.put("result", "success");
			data.put("msg", "保存失败");
			return data;
		}
	}

	@Override
	public Map<String, Object> delete(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int delCheck = messageMasterDao.getOneInfo("middlewareConf.deleteCheck", params);
		if(delCheck > 0){
			result.put("result", "fail");
			result.put("msg", "该中间件配置了MQ关系不能直接删除");
			return result;
		}
		int delNum = messageMasterDao.delete("middlewareConf.delete", params);
		if(delNum == 1){
			result.put("result", "success");
			result.put("msg", "成功删除记录");
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		return result;
	}
	
	/**
	 * 生成中间件ID<br>
	 * 中间件id = 机房节点2位 + 中间件类型1位 + 序号1位 + 组件IP第三段 + 组件IP第四段
	 * @param params
	 * @return
	 */
	private Map<String, Object> generateMiddlewareId(Map<String, String> params){
		Map<String, Object> result = new HashMap<String, Object>();
		
		String segment1 = params.get("node_id");
		String segment2 = params.get("middleware_type");
		Integer maxIndex = messageMasterDao.getOneInfo("middlewareConf.getMaxIndexOfMiddlewareInSanmeIp", params);
		int segment3 =  maxIndex == null ? 0 : (maxIndex + 1);
		String hostIp = params.get("host_ip");
		String[] hostIpSeg = hostIp.split("\\.");
		String segment4 = hostIpSeg[2];
		String segment5 = hostIpSeg[3];
		
		if(segment3 >= 10){
			LOGGER.info("【组件管理-中间件配置】：生成中间件ID(middleware_id)失败，hostIp = " + hostIp + "的序号已经被分配完");
			result.put("middleware_id", "");
			result.put("result", "failure");
			result.put("msg", "生成中间件ID失败，当前IP(" + params.get("host_ip") + ")下中间件序号已经被分配完");
			return result;
		}else{
			// 拼接生成中间件ID
			String middlewareId = segment1 + segment2 + segment3 + segment4 + segment5;
			result.put("middleware_id", middlewareId);
			return result;
		}
		
	}

	@Override
	public List<RabbitMqQueue> queryAllQueueByMiddlewareId(String middlewareId) {
		List<RabbitMqQueue> result = new ArrayList<RabbitMqQueue>();
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("middleware_id", middlewareId);
		Map<String, Object> middlewareInfo = messageMasterDao.getOneInfo("middlewareConf.getOneInfo", sqlParams);
		if(null != middlewareInfo){
			String ip = String.valueOf(middlewareInfo.get("host_ip"));
			String port = String.valueOf(middlewareInfo.get("console_port"));
			String username = String.valueOf(middlewareInfo.get("user_name"));
			String password = String.valueOf(middlewareInfo.get("pass_word"));
			boolean getBindings = true;
			
			result = RabbitMqConsoleUtil.getAllQueueList(ip, port, username, password, getBindings);
		}
		
		if(null != result && result.size() > 0){
			return result;
		}else{
			return new ArrayList<RabbitMqQueue>();
		}
	}

	@Override
	public Map<String, Object> createQueueOnRabbitMQ(String middlewareId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 查询RabbitMQ的信息用于创建连接
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("middleware_id", middlewareId);
		Map<String, Object> middlewareInfo = messageMasterDao.getOneInfo("middlewareConf.getOneInfo", sqlParams);
		
		boolean isCreateOk = false;
		if(null != middlewareInfo){
			String middlewareType = String.valueOf(middlewareInfo.get("middleware_type"));
			String ip = String.valueOf(middlewareInfo.get("host_ip"));
			Integer port = Integer.valueOf(String.valueOf(middlewareInfo.get("port")));
			String username = String.valueOf(middlewareInfo.get("user_name"));
			String password = String.valueOf(middlewareInfo.get("pass_word"));
			String virtualHost = "/";
			
			RabbitMqServerUtil rabbitMqChannel = new RabbitMqServerUtil();
			try {
				// 初始化RabbitMQ连接信道
				rabbitMqChannel.innit(ip, port, virtualHost, username, password);
				
				// 根据MQ中间件的类型创建这个MQ上面的队列
				if(middlewareType.equals("1")){
					// MQ_C2S_IO
					for (String queue : mqC2Sio) {
						isCreateOk = createRabbitMqQueue(middlewareId, queue, rabbitMqChannel, mqC2SioMessageType);
					}
				}else if(middlewareType.equals("2")){// MQ_SEND_IO 不在这里创建
				}else if(middlewareType.equals("3")){
					// MQ_C2S_DB
					for (String queue : mqC2sDb) {
						isCreateOk = createRabbitMqQueue(middlewareId, queue, rabbitMqChannel, mqC2sDbMessageType);
					}
				}else if(middlewareType.equals("4")){
					// MQ_SEND_DB
					for (String queue : mqSendDb) {
						isCreateOk = createRabbitMqQueue(middlewareId, queue, rabbitMqChannel, mqSendDbMessageType);
					}
				}else{
				}
			} catch (Exception e) {
				LOGGER.debug("创建rabbitMQ队列失败：" + e);
				
				result.put("result", "fail");
				result.put("msg", "创建失败");
				return result;
			} finally {
				rabbitMqChannel.closeAll();
			}
		}else{
			LOGGER.error("创建rabbitMQ队列失败，查询中间件信息为空middlewareId = " + middlewareId);
		}
		
		if(isCreateOk){
			result.put("result", "success");
			result.put("msg", "创建成功");
		}else{
			result.put("result", "fail");
			result.put("msg", "创建失败");
		}
		return result;
	}
	
	/**
	 * 创建rabbitMQ队列
	 * @param queueName
	 * @return
	 */
	private boolean createRabbitMqQueue(String middlewareId, String queueName, RabbitMqServerUtil rabbitMqChannel, Map<String, String> queueMessageType){
		Map<String, String> bindings = new HashMap<String, String>();
		bindings.put("queue", queueName + "Queue" + version);
		bindings.put("exchange", queueName + "Exchange" + version);
		bindings.put("routingKey", queueName + "Routingkey" + version);
		
		DeclareOk declareExchangeOk = rabbitMqChannel.exchangeDeclare(bindings.get("exchange"), "direct", true);
		com.rabbitmq.client.AMQP.Queue.DeclareOk declareQueueOk = rabbitMqChannel.queueDeclare(bindings.get("queue"), true, false, false, null);
		BindOk bindOk = rabbitMqChannel.queueBind(bindings.get("queue"), bindings.get("exchange"), bindings.get("routingKey"));
		
		Gson gson = new Gson();
		if(null != declareExchangeOk && null != declareQueueOk && null != bindOk){
			Map<String, Object> mqConfigInfo = new HashMap<String, Object>();
			
			mqConfigInfo.put("middleware_id", middlewareId);
			mqConfigInfo.put("mq_queue", bindings.get("queue"));
			mqConfigInfo.put("mq_exchange", bindings.get("exchange"));
			mqConfigInfo.put("mq_routingkey", bindings.get("routingKey"));
			mqConfigInfo.put("queue_name", bindings.get("queue"));
			mqConfigInfo.put("message_type", queueMessageType.get(queueName));
			mqConfigInfo.put("remark", "");
			LOGGER.debug("创建rabbitMQ 队列成功： " + gson.toJson(bindings));
			int insertNum = messageMasterDao.insert("mqConfig.insert", mqConfigInfo);
			if(insertNum == 1){
				LOGGER.debug("写入t_sms_mq_configure表MQ配置信息成功");
			}else{
				LOGGER.debug("写入t_sms_mq_configure表MQ配置信息失败");
			}
			return true;
		}else{
			LOGGER.debug("创建rabbitMQ 队列失败： " + gson.toJson(bindings));
			return false;
		}
	}

	@Override
	public Map<String, Object> deleteQueueOnRabbitMQ(String middlewareId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 查询RabbitMQ的信息用于创建连接
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("middleware_id", middlewareId);
		Map<String, Object> middlewareInfo = messageMasterDao.getOneInfo("middlewareConf.getOneInfo", sqlParams);
		
		boolean isDeleteOk = false;
		if(null != middlewareInfo){
			List<Map<String, Object>> queueList = messageMasterDao.getSearchList("middlewareConf.queryRabbitMqQueueList", sqlParams);
			
			if(null != queueList && queueList.size() > 0){
				String ip = String.valueOf(middlewareInfo.get("host_ip"));
				Integer port = Integer.valueOf(String.valueOf(middlewareInfo.get("port")));
				String username = String.valueOf(middlewareInfo.get("user_name"));
				String password = String.valueOf(middlewareInfo.get("pass_word"));
				String virtualHost = "/";
				
				RabbitMqServerUtil rabbitMqChannel = new RabbitMqServerUtil();
				
				try {
					// 初始化RabbitMQ连接信道
					rabbitMqChannel.innit(ip, port, virtualHost, username, password);
					
					for (Map<String, Object> queue : queueList) {
						DeleteOk deleteExchangeOk = rabbitMqChannel.exchangeDelete(String.valueOf(queue.get("mq_exchange")));
						com.rabbitmq.client.AMQP.Queue.DeleteOk deleteQueueOk = rabbitMqChannel.queueDelete(String.valueOf(queue.get("mq_queue")));
						if(null != deleteExchangeOk && null != deleteQueueOk){
							LOGGER.debug("删除rabbitMQ 队列成功： " + queue);
						}else{
							LOGGER.debug("删除rabbitMQ 队列失败： " + queue);
						}
					}
					
					// 删除t_sms_mq_configure表MQ配置信息
					int deleteNum = messageMasterDao.delete("middlewareConf.deleteMqConfigByMiddlewareId", sqlParams);
					if(deleteNum > 0){
						LOGGER.debug("删除t_sms_mq_configure表MQ配置信息成功 middlewareId =", middlewareId);
					}else{
						LOGGER.debug("删除t_sms_mq_configure表MQ配置信息失败 middlewareId = ", middlewareId);
					}
				} catch (Exception e) {
					LOGGER.debug("删除rabbitMQ 队列失败： " + e);
					result.put("result", "fail");
					result.put("msg", "创建失败");
					return result;
				}
			}else{
				LOGGER.debug("删除rabbitMQ 队列时查询t_sms_mq_configure时返回MQ配置信息为空 middlewareId = " + middlewareId);
				result.put("result", "success");
				result.put("msg", "删除完成");
				return result;
			}
			
			isDeleteOk = true;
		}
		
		if(isDeleteOk){
			result.put("result", "success");
			result.put("msg", "删除完成");
		}else{
			result.put("result", "fail");
			result.put("msg", "删除完成");
		}
		return result;
	}

	@Override
	public PageContainer queryNeedCreateQueueByMiddlewareId(String middlewareId) {
		
		// 查询RabbitMQ的信息用于创建连接
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("middleware_id", middlewareId);
		Map<String, Object> middlewareInfo = messageMasterDao.getOneInfo("middlewareConf.getOneInfo", sqlParams);
		
		PageContainer page = new PageContainer();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(null != middlewareInfo){
			String middlewareType = String.valueOf(middlewareInfo.get("middleware_type"));
			String middlewareName = String.valueOf(middlewareInfo.get("middleware_name"));
			if(middlewareType.equals("1")){
				// MQ_C2S_IO
				for (String queueName : mqC2Sio) {
					list.add(generateNeedCreateQueueInfo(queueName, middlewareId, middlewareName, mqC2SioMessageType));
				}
			}else if(middlewareType.equals("2")){// MQ_SEND_IO 不在这里创建
			}else if(middlewareType.equals("3")){
				// MQ_C2S_DB
				for (String queueName : mqC2sDb) {
					list.add(generateNeedCreateQueueInfo(queueName, middlewareId, middlewareName, mqC2sDbMessageType));
				}
			}else if(middlewareType.equals("4")){
				// MQ_SEND_DB
				for (String queueName : mqSendDb) {
					list.add(generateNeedCreateQueueInfo(queueName, middlewareId, middlewareName, mqSendDbMessageType));
				}
			}else{
			}
			
			page.setList(list);
		}
		
		return page;
	}
	
	private Map<String, Object> generateNeedCreateQueueInfo(String queueName, String middlewareId, String middlewareName, Map<String, String> queueMessageType){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("middlewareId", middlewareId);
		map.put("middlewareName", middlewareName);
		map.put("queue", queueName + "Queue" + version);
		map.put("exchange", queueName + "Exchange" + version);
		map.put("routingKey", queueName + "Routingkey" + version);
		map.put("messageType", queueMessageType.get(queueName));
		
		return map;
	}

}
