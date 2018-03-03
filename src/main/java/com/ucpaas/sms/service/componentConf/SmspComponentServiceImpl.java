package com.ucpaas.sms.service.componentConf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.util.web.AuthorityUtils;

@Service
@Transactional
public class SmspComponentServiceImpl implements SmspComponentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmspComponentServiceImpl.class);
	
	@Autowired
	private MessageMasterDao messageMasterDao;

	@Override
	public PageContainer query(Map<String, String> params) {
		return messageMasterDao.getSearchPage("smspComponent.query", "smspComponent.queryCount", params);
	}

	@Override
	public Map<String, Object> editView(int id) {
		return messageMasterDao.getOneInfo("smspComponent.getOneInfo", id);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int saveNum;
		
		String id = Objects.toString(params.get("id"), "");
		String componentId = Objects.toString(params.get("component_id"), "");
		
		if(StringUtils.isBlank(id)){// 新建
			Map<String, Object> failureInfo = savePreCheck(params);
			if(null != failureInfo){
				return failureInfo;
			}
			
			data = generateComponentId(params);
			String generateCompoId = Objects.toString(data.get("component_id"), "");
			
			if(StringUtils.isBlank(generateCompoId)){
				return data;// 错误，返回提示信息给页面
			}else{
				componentId = generateCompoId;
				params.put("component_id", generateCompoId);
				saveNum = messageMasterDao.insert("smspComponent.insert", params);// 插入
			}
			
			Gson gson = new Gson();
			LOGGER.debug("新建SMSP组件配置 = {}，操作员 = {}", gson.toJson(params), AuthorityUtils.getLoginRealName());
		}else{
			
			// 5.5需求中 组件Id可以在编辑时手工修改  所以编辑页面中组件Id只用页面传过来的值去更新不在 根据“机房节点”、“中间件类型”、“中间件地址”来计算
			/*// 当机房节点、中间件类型（middleware_type）、中间件地址（host_ip）改变时需要重新生成middleware_id
			boolean componentIdNotChange = messageMasterDao.getOneInfo("smspComponent.isCompoIdChange", params);
			params.put("old_component_id", pageComponentId);
			
			if(isCompoIdChange){
				// 直接更新
				saveNum = messageMasterDao.update("smspComponent.update", params);
			}else{
				Map<String, Object> failureInfo = savePreCheck(params);
				if(null != failureInfo){
					return failureInfo;
				}
				// 重新生成component_id，然后更新
				data = generateComponentId(params);
				String generateCompoId = Objects.toString(data.get("component_id"), "");
				
				if(StringUtils.isBlank(generateCompoId)){
					return data;// 错误，返回提示信息给页面
				}else{
					pageComponentId = generateCompoId;
					params.put("component_id", generateCompoId);
					saveNum = messageMasterDao.update("smspComponent.update", params);
					
					// 修改“SMSP组件配置”时如果中的component_id改变需要更新“组件MQ关系表”中的component_id
					messageMasterDao.update("smspComponent.updateComponentConfTableMiddlewareId", params);
				}
			}*/
			
			Map<String, Object> failureInfo = savePreCheck(params);
			if(null != failureInfo){
				return failureInfo;
			}
			
			// 判断当前组件是否已经修改
			String oldComponentId = messageMasterDao.getOneInfo("smspComponent.queryComponentIdById", id);
			params.put("old_component_id", oldComponentId);
			saveNum = messageMasterDao.update("smspComponent.update", params);
			// 修改“SMSP组件配置”时如果中的component_id改变需要更新“组件MQ关系表”中的component_id
			if(!oldComponentId.equals(componentId)){
				messageMasterDao.update("smspComponent.updateComponentConfTableMiddlewareId", params);
			}
			
			Gson gson = new Gson();
			LOGGER.debug("修改SMSP组件配置 = {}，操作员 = {}", gson.toJson(params), AuthorityUtils.getLoginRealName());
		}
		
		if(saveNum == 1){
			data.put("result", "success");
			data.put("id", params.get("id"));
			data.put("componentId", componentId);
			data.put("msg", "保存成功");
			return data;
		}else{
			data.put("result", "success");
			data.put("msg", "保存失败");
			return data;
		}
	}
	
	private Map<String, Object> savePreCheck(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 同一个IP上智能配置一个同类型组件
		int check = messageMasterDao.getOneInfo("smspComponent.checkSave", params);
		if(check != 0){
			data.put("result", "failure");
			data.put("msg", "同一IP上只能配置一个同类型组件，当前IP（" + params.get("host_ip") + "）下面已经配置过该类型组件");
			return data;
		}
		
		// 组件Id查重
		String componentId = Objects.toString(params.get("component_id"), "");
		if(StringUtils.isNotBlank(componentId)){
			check = messageMasterDao.getOneInfo("smspComponent.checkCompnentIdExist", params);
			if(check != 0){
				data.put("result", "failure");
				data.put("msg", "当前组件ID已经存在");
				return data;
			}
		}
		
		return null;
	}

	@Override
	public Map<String, Object> delete(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int delCheck = messageMasterDao.getOneInfo("smspComponent.deleteCheck", params);
		if(delCheck > 0){
			result.put("result", "fail");
			result.put("msg", "该组件配置了MQ关系不能直接删除");
			return result;
		}
		
		int delNum = messageMasterDao.delete("smspComponent.delete", params);
		if(delNum == 1){
			result.put("result", "success");
			result.put("msg", "成功删除记录");
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		
		Gson gson = new Gson();
		LOGGER.debug("删除SMSP组件配置 = {}，操作员 = {}", gson.toJson(params), AuthorityUtils.getLoginRealName());
		return result;
	}
	
	/**
	 * 生成SMSP组件ID<br>
	 * SMSP组件ID = 机房节点2位+组件类型2位+组件IP第三段+组件IP第四段 ,最长10位,一个IP上只放一个同类型组件
	 * @param params
	 * @return
	 */
	private Map<String, Object> generateComponentId(Map<String, String> params){
		Map<String, Object> result = new HashMap<String, Object>();
		
		String segment1 = params.get("node_id");
		String segment2 = params.get("component_type");
		String hostIp = params.get("host_ip");
		String[] hostIpSeg = hostIp.split("\\.");
		String segment3 = hostIpSeg[2];
		String segment4 = hostIpSeg[3];
		
		// 拼接生成中间件ID
		String componentId = segment1 + segment2 + segment3 + segment4;
		
		if(componentId.length() > 10){
			LOGGER.info("【组件管理-SMSP组件配置】：生成SMSP组件ID(component_id)不合法，ID " + componentId + "超过10位");
			result.put("middleware_id", "");
			result.put("result", "failure");
			result.put("msg", "生成中间件ID失败，ID " + componentId + "超过10位");
		}else{
			result.put("component_id", componentId);
		}
		
		return result;
	}

	@Override
	public Map<String, Object> getOneComponetInfo(String middlewareId) {
		return messageMasterDao.getOneInfo("smspComponent.getOneComponetInfo", middlewareId);
	}

}
