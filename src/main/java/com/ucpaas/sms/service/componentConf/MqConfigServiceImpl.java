package com.ucpaas.sms.service.componentConf;

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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
public class MqConfigServiceImpl implements MqConfigService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MqConfigServiceImpl.class);
	
	@Autowired
	private MessageMasterDao messageMasterDao;
	
	@Autowired
	private SmspComponentService smspComponentService;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		// 根据中间件类型查询对应中间件信息分页
		String middlewareType = Objects.toString(params.get("middleware_type"), "0");
		List<Map<String, Object>> middlewareIdList = messageMasterDao.getSearchList("mqConfig.queryMiddlewareIdByType", middlewareType);
		
		if(middlewareIdList.size() == 0){
			return new PageContainer();
		}
		
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.putAll(params);
		sqlParams.put("middlewareIdList", middlewareIdList);
		PageContainer page = messageMasterDao.getSearchPage("mqConfig.query", "mqConfig.queryCount", sqlParams);
		
		/*// 查询MQ队列的消息数，添加到分页信息中
		String middlewareId = "";
		Map<String, Object> componetInfo = new HashMap<String, Object>();
		List<Map<String, Object>>  list = page.getList();
		for (Map<String, Object> map : list) {
			// middlewareId和上一次查询一样的话使用上次的查询结果，否则重新查询
			if(!middlewareId.equals(String.valueOf(map.get("middleware_id")))){
				middlewareId = String.valueOf(map.get("middleware_id"));
				componetInfo = smspComponentService.getOneComponetInfo(middlewareId);
			}
			
			if(null != componetInfo){
				String ip = String.valueOf(componetInfo.get("ip"));
				String port = String.valueOf(componetInfo.get("port"));
				String username = String.valueOf(componetInfo.get("username"));
				String password = String.valueOf(componetInfo.get("password"));
				String queue = String.valueOf(map.get("mq_queue"));
				RabbitMqQueue queueInfo = RabbitMqConsoleUtil.getQueueInfo(ip, port, username, password, queue);
				if(null != queueInfo){
					map.put("message_count", Objects.toString(queueInfo.getMessages(), "0"));
				}
				
			}
		}*/
		return page;
	}

	@Override
	public Map<String, Object> editView(int id) {
		return messageMasterDao.getOneInfo("mqConfig.getOneInfo", id);
	}

	/**
	 * 创建队列<br>
	 * 通道上架操作中有调用
	 */
	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int saveNum;
		String mqId = Objects.toString(params.get("mq_id"), "");
		String mqQueue = Objects.toString(params.get("mq_queue"), "");
		String mqExchange = Objects.toString(params.get("mq_exchange"), "");
		String routingkey = Objects.toString(params.get("mq_routingkey"), "");
		String middlewareId = Objects.toString(params.get("middleware_id"), "");
		
		if(StringUtils.isBlank(mqId)){// 新建
			int check = messageMasterDao.getOneInfo("mqConfig.checkSave", mqQueue);
			if(check != 0){
				data.put("result", "failure");
				data.put("msg", "添加失败，当前队列名(" + mqQueue + ")已经存在");
				return data;
			}
			
			int checkExchange = messageMasterDao.getOneInfo("mqConfig.checkExchange", mqExchange);
			if(checkExchange != 0){
				data.put("result", "failure");
				data.put("msg", "添加失败，当前MQ交换(" + mqExchange + ")已经存在");
				return data;
			}
			
			int checkRoutingKey = messageMasterDao.getOneInfo("mqConfig.checkRoutingKey", routingkey);
			if(checkRoutingKey != 0){
				data.put("result", "failure");
				data.put("msg", "添加失败，当前MQ路由KEY(" + routingkey + ")已经存在");
				return data;
			}
			
			saveNum = messageMasterDao.insert("mqConfig.insert", params);
			mqId = String.valueOf(params.get("mq_id")); // 获得mybatis返回的插入数据的主键ID
		}else{// 更新(目前没有更新操作)
			saveNum = messageMasterDao.update("mqConfig.update", params);
		}
		
		if(saveNum == 1){
			// 保存成功后再rabbitMQ上面创建队列
			boolean isCreateQueueOk = createQueueOnMq(middlewareId, mqExchange, mqQueue, routingkey);
			if(isCreateQueueOk){
				data.put("result", "success");
				data.put("mq_id", mqId);
				data.put("msg", "创建队列成功");
			}else{
				// rabbitMQ上创建队列失败回滚前面的insert
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				data.put("result", "fail");
				data.put("msg", "创建队列失败，请联系管理员");
			}
			return data;
		}else{
			data.put("result", "fail");
			data.put("msg", "保存失败");
			LOGGER.debug("保存rabbitMQ queue = {} 信息时【失败】, 操作员 = {}", mqQueue, AuthorityUtils.getLoginRealName());
			return data;
		}
	}
	
	@Override
	public boolean createQueueOnMq(String middlewareId, String exchange, String queue, String routingkey){
		
		// 获得RabbitMQ中间件信息
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("middleware_id", middlewareId);
		Map<String, Object> middlewareInfo = messageMasterDao.getOneInfo("middlewareConf.getOneInfo", sqlParams);
		
		String ip = String.valueOf(middlewareInfo.get("host_ip"));
		Integer port = Integer.valueOf(String.valueOf(middlewareInfo.get("port")));
		String username = String.valueOf(middlewareInfo.get("user_name"));
		String password = String.valueOf(middlewareInfo.get("pass_word"));
		String virtualHost = "/";
		
		try {
			// 初始化RabbitMQ连接信道
			RabbitMqServerUtil rabbitMqChannel = new RabbitMqServerUtil();
			rabbitMqChannel.innit(ip, port, virtualHost, username, password);
			
			// 创建队列完成队列绑定关系
			DeclareOk declareExchangeOk = rabbitMqChannel.exchangeDeclare(exchange, "direct", true);
			com.rabbitmq.client.AMQP.Queue.DeclareOk declareQueueOk = rabbitMqChannel.queueDeclare(queue, true, false, false, null);
			BindOk bindOk = rabbitMqChannel.queueBind(queue, exchange, routingkey);
			
			if(null != declareExchangeOk && null != declareQueueOk && null != bindOk){
				LOGGER.debug("创建rabbitMQ queue = {} 【成功】, 操作员 = {}", queue, AuthorityUtils.getLoginRealName());
				return true;
			}else{
				LOGGER.debug("创建rabbitMQ queue = {} 【失败】, 操作员 = {}", queue, AuthorityUtils.getLoginRealName());
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("创建rabbitMQ queue = {} 【发生错误】", e);
			return false;
		}
	}

	@Override
	public Map<String, Object> delete(String mqId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("mq_id", mqId);
		int delCheck = messageMasterDao.getOneInfo("mqConfig.deleteCheck", sqlParam);
		if(delCheck > 0){
			result.put("result", "fail");
			result.put("msg", "该MQ已经和SMSP组件配置关系不能直接删除");
			return result;
		}
		
		boolean isDeleteQueueOk = deleteQueueOnMq(mqId);
		int delNum = messageMasterDao.delete("mqConfig.delete", sqlParam);
		if(delNum == 1){
			if(isDeleteQueueOk){
				result.put("result", "success");
				result.put("msg", "成功删除记录和MQ上队列");
			}else{
				result.put("result", "success");
				result.put("msg", "成功删除记录但是删除MQ上队列失败");
			}
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		return result;
	}
	
	@Override
	public boolean deleteQueueOnMq(String mqId){
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("mq_id", mqId);
		Map<String, Object> mqInfo = messageMasterDao.getOneInfo("mqConfig.getOneInfo", sqlParam);
		
		if(null != mqInfo){
			String middlewareId = String.valueOf(mqInfo.get("middleware_id"));
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			sqlParams.put("middleware_id", middlewareId);
			Map<String, Object> middlewareInfo = messageMasterDao.getOneInfo("middlewareConf.getOneInfo", sqlParams);
			
			String ip = String.valueOf(middlewareInfo.get("host_ip"));
			Integer port = Integer.valueOf(String.valueOf(middlewareInfo.get("port")));
			String consolePort = String.valueOf(middlewareInfo.get("console_port"));
			String username = String.valueOf(middlewareInfo.get("user_name"));
			String password = String.valueOf(middlewareInfo.get("pass_word"));
			String virtualHost = "/";
			
			// 初始化RabbitMQ连接信道
			RabbitMqServerUtil rabbitMqChannel = new RabbitMqServerUtil();
			rabbitMqChannel.innit(ip, port, virtualHost, username, password);
			
			String queue = String.valueOf(mqInfo.get("mq_queue"));
			String exchange = String.valueOf(mqInfo.get("mq_exchange"));
			
			int exchangeBindingNum = RabbitMqConsoleUtil.getExchangeBindingsNumber(ip, consolePort, exchange, username, password);
			String operator = AuthorityUtils.getLoginRealName();
			if(exchangeBindingNum <= 1){
				DeleteOk deleteExchangeOk = rabbitMqChannel.exchangeDelete(exchange);
				if(null != deleteExchangeOk){
					LOGGER.debug("删除rabbitMQ exchange【成功】，exchange = {}，操作员 = {}", exchange, operator);
				}else{
					LOGGER.warn("删除rabbitMQ exchange【失败】，exchange = {}，操作员 = {}", exchange, operator);
					return false;
				}
				
			}
			
			com.rabbitmq.client.AMQP.Queue.DeleteOk deleteQueueOk = rabbitMqChannel.queueDelete(queue);
			if(null != deleteQueueOk){
				LOGGER.debug("删除rabbitMQ queue 【成功】, queue = {}, 操作员 = {}", queue, operator);
				return true;
			}else{
				LOGGER.debug("删除rabbitMQ queue 【失败】, queue = {}, 操作员 = {}", queue, operator);
				return false;
			}
		}else{
			LOGGER.debug("删除队列时查询mq信息为空,mqId = ", mqId);
			return false;
		}
	}

	@Override
	public Map<String, Object> queryQueueMessageCount(Map<String, String> params) {
		String middlewareId = params.get("middleware_id");
		String queueName = params.get("mq_queue");
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("middleware_id", middlewareId);
		Map<String, Object> middlewareInfo = messageMasterDao.getOneInfo("middlewareConf.getOneInfo", sqlParams);
		
		String ip = String.valueOf(middlewareInfo.get("host_ip"));
		String port = String.valueOf(middlewareInfo.get("console_port"));
		String username = String.valueOf(middlewareInfo.get("user_name"));
		String password = String.valueOf(middlewareInfo.get("pass_word"));
		RabbitMqQueue rabbitMqQueue  = RabbitMqConsoleUtil.getQueueInfo(ip, port, username, password, queueName);
		
		Map<String, Object> result = new HashMap<String, Object>();
		if(null != rabbitMqQueue){
			result.put("result", "success");
			result.put("queueMessageCount", Objects.toString(rabbitMqQueue.getMessages(), "0"));
			return result;
		}else{
			result.put("result", "success");
			result.put("queueMessageCount", "0");
			return result;
		}
	}

}
