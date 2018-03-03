package com.ucpaas.sms.service.sendhis.customerSend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.DbConstant.DbType;
import com.ucpaas.sms.constant.DbConstant.TablePrefix;
import com.ucpaas.sms.dao.AccessSlaveDao;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.AccessTable;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.util.ConfigUtils;

/**
 * 短信发送历史-客户发送历史记录（Access）
 * 
 * @author oylx
 */
@Service
@Transactional
public class CustomerSendHisServiceImpl implements CustomerSendHisService {
	@Autowired
	private AccessSlaveDao accessSlaveDao;
	@Autowired
	private MessageMasterDao messageMasterDao;
	@Autowired
	private CommonService commonService;
	

	@Override
	public PageContainer query(Map<String, String> params) throws Exception {
		PageContainer page = new PageContainer();
		
		//==============================分表需求-开始==================================
//		List<String> tableList = new ArrayList<>();
//		List<SplitTableTime> splitTableTimeList = commonService.getSplitTableTime(params.get("start_time"), params.get("end_time"));
//		for(SplitTableTime st : splitTableTimeList){
//			String flag = st.getFlag();
//			String start_time = st.getStart_time();
//			String end_time = st.getEnd_time();
//			
//			List<String> tableListTemp = null;
//			if("old".equals(flag)){
//				tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time,end_time);
//			}else{
//				
//				String identify = params.get("identify");
//				String clientid = params.get("clientid");
//				if(identify != null && clientid != null){
//					tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time, end_time, identify);
//				}else if(identify != null && clientid == null){
//					tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time, end_time, identify);
//				}else{
//					tableListTemp = commonService.getExistTableForAccess(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time,end_time,params.get("clientid"));
//				}
//			}
//			tableList.addAll(tableListTemp);
//		}
		
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		
		List<String> tableList = new ArrayList<>();
		List<String> tableListTemp_old = commonService.getExistTable(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time);
		
		List<String> tableListTemp_new = null;
		String identify = params.get("identify");
		if(identify != null){
			tableListTemp_new = commonService.getExistTable(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time, end_time, identify);
		}else{
			tableListTemp_new = commonService.getExistTableForAccess(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time,params.get("clientid"));
		}
		
		tableList.addAll(tableListTemp_old);
		tableList.addAll(tableListTemp_new);
		
		//==============================分表需求-结束==================================
		
		
		
		
		
		if (tableList.size() > 0) {
			String tableName = tableList.get(0);
			Map<String, Integer> accessTableSchema = getAccessTableSchema(tableName);
		
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			p.put("table_list", tableList);
			p.putAll(accessTableSchema);
			page = accessSlaveDao.getSearchPage("customerSendHis.query", "customerSendHis.queryCount", p);
		}
		return page;
	}
	
	
	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) throws Exception {
		
		
		//==============================分表需求-开始==================================
//		List<String> tableList = new ArrayList<>();
//		List<SplitTableTime> splitTableTimeList = commonService.getSplitTableTime(params.get("start_time"), params.get("end_time"));
//		for(SplitTableTime st : splitTableTimeList){
//			
//			String flag = st.getFlag();
//			String start_time = st.getStart_time();
//			String end_time = st.getEnd_time();
//			
//			List<String> tableListTemp = null;
//			if("old".equals(flag)){
//				tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time,end_time);
//			}else{
//				String identify = params.get("identify");
//				String clientid = params.get("clientid");
//				if(identify != null && clientid != null){
//					tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time, end_time, identify);
//				}else if(identify != null && clientid == null){
//					tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time, end_time, identify);
//				}else{
//					tableListTemp = commonService.getExistTableForAccess(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time,end_time,params.get("clientid"));
//				}
//			}
//			tableList.addAll(tableListTemp);
//		}
		
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		
		List<String> tableList = new ArrayList<>();
		List<String> tableListTemp_old = commonService.getExistTable(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time);
		
		List<String> tableListTemp_new = null;
		String identify = params.get("identify");
		if(identify != null){
			tableListTemp_new = commonService.getExistTable(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time, end_time, identify);
		}else{
			tableListTemp_new = commonService.getExistTableForAccess(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time,params.get("clientid"));
		}
		
		tableList.addAll(tableListTemp_old);
		tableList.addAll(tableListTemp_new);
		
		//==============================分表需求-结束==================================
		
		if (tableList.size() > 0) {
			String tableName = tableList.get(0);
			Map<String, Integer> accessTableSchema = getAccessTableSchema(tableName);
			
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			p.put("table_list", tableList);
			p.putAll(accessTableSchema);
			return accessSlaveDao.getSearchList("customerSendHis.query",  p);
		}else{
			return null;
		}
		 
	}
	
	/**
	 * 查询的Access流水表表结构状态
	 * @return
	 */
	private Map<String, Integer> getAccessTableSchema(String tableName){
		
		// 获得当前access流水表定义的所有字段，key表示字段名，value表示字段是否存在（下面会有1表示字段存在，0表示不存在）
		AccessTable accessTable = new AccessTable();
		Map<String, Integer> accessTableSchema = accessTable.getAllFieldsMap();
		
		// 根据表名从INFORMATION_SCHEMA.COLUMNS中获得该表存在的所有字段的名字
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("table_name", tableName);
		sqlParams.put("access_database_name", accessSlaveDao.getOneInfo("customerSendHis.getCurrentDatabaseName", null));
		List<Map<String, Object>> columns = accessSlaveDao.getSearchList("customerSendHis.getAccessTableSchema", sqlParams);
		
		// 根据实际存在的字段重新设置表结构中字段是否存在的状态（初始化是0，这里设置为 1表示存在）
		for (Map<String, Object> map : columns) {
			accessTableSchema.put(String.valueOf(map.get("COLUMN_NAME")), 1);
		}
		return accessTableSchema;
	}


	@Override
	public Map<String, Object> queryKeyWords(String clientId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询系统关键字
		List<String> systemKeywordList = messageMasterDao.selectList("customerSendHis.querySystemKeyword", null);
		
		List<String> channelKeywordList = new ArrayList<>();
		
		// 查询用户绑定的通道组
		Map<String, Object> userChannelGroup = messageMasterDao.getOneInfo("customerSendHis.queryUserChannelGroup", clientId);
		
		if(userChannelGroup != null){
			List<String> channelGroupStrList = new ArrayList<>();
			// 以下是通道组并不是通道
			String channelid = Objects.toString(userChannelGroup.get("channelid"), "");
			String ydchannelid = Objects.toString(userChannelGroup.get("ydchannelid"), "");
			String ltchannelid = Objects.toString(userChannelGroup.get("ltchannelid"), "");
			String dxchannelid = Objects.toString(userChannelGroup.get("dxchannelid"), "");
			String gjchannelid = Objects.toString(userChannelGroup.get("gjchannelid"), "");
			
			if(StringUtils.isNotBlank(channelid)){
				channelGroupStrList.add(channelid);
			}
			if(StringUtils.isNotBlank(ydchannelid)){
				channelGroupStrList.add(ydchannelid);
			}
			if(StringUtils.isNotBlank(ltchannelid)){
				channelGroupStrList.add(ltchannelid);
			}
			if(StringUtils.isNotBlank(dxchannelid)){
				channelGroupStrList.add(dxchannelid);
			}
			if(StringUtils.isNotBlank(gjchannelid)){
				channelGroupStrList.add(gjchannelid);
			}
			
			
			String channelGroupStr = StringUtils.join(channelGroupStrList, ",");
			List<String> channelGroupList = Arrays.asList(channelGroupStr.split(","));
			
			List<String> channelIdList = new ArrayList<>();
			for (String channelGroupId : channelGroupList) {
				List<String> list = messageMasterDao.selectList("customerSendHis.queryChannelIdByGroupId", channelGroupId);
				channelIdList.addAll(list);
			}
			
			Set<String> channelIdSet = new HashSet<>();
			for (String channelId : channelIdList) {
				// 上面获得的通道组中的通道号可能会重复，这里重复直接跳过
				if(channelIdSet.contains(channelId)){
					continue;
				}else{
					channelIdSet.add(channelId);
					// 一条通道的关键字用“|”分隔， 超过4000个字会存在多条记录
					List<String> channelKeywordInDB = messageMasterDao.selectList("customerSendHis.queryChannelKeywordById", channelId);
					String channelKeywordStr = StringUtils.join(channelKeywordInDB, "");
					// 将通道关键字中的“|”剔除掉然后保存到一个list中
					channelKeywordList.addAll(Arrays.asList(channelKeywordStr.split("\\|")));
				}
			}
			
		}



//		// 将通道关键字中存在的系统关键字移除
//		Set<String> sysKwSet = new HashSet<String>(systemKeywordList);
//		Set<String> channelKwSet = new HashSet<String>(channelKeywordList);
//		Set<String> tempSet = new HashSet<>();
//		for (String ckw : channelKwSet) {
//			tempSet.add(ckw);
//			// 判断通道关键字是否包含于系统关键字的文字中，如果时则移除
//			for (String skw : sysKwSet) {
//				if(skw.indexOf(ckw) != -1){
//					tempSet.remove(ckw);
//				}
//			}
//		}
//
//		result.put("systemKeyword", new ArrayList<String>(sysKwSet));
//		result.put("channelKeyword", new ArrayList<String>(tempSet));


		// 将通道关键字中存在的系统关键字移除
		Set<String> sysKwSet = new HashSet<String>(systemKeywordList);
		Set<String> channelKwSet = new HashSet<String>(channelKeywordList);
		for (String kw : sysKwSet) {
			if(channelKwSet.contains(kw) || StringUtils.isBlank(kw)){
				channelKwSet.remove(kw);
			}
		}

		result.put("systemKeyword", new ArrayList<String>(sysKwSet));
		result.put("channelKeyword", new ArrayList<String>(channelKwSet));


		return result;
	}
	

}
