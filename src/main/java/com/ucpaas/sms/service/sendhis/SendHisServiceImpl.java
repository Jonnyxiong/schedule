package com.ucpaas.sms.service.sendhis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.DbConstant.DbType;
import com.ucpaas.sms.constant.DbConstant.TablePrefix;
import com.ucpaas.sms.dao.RecordSlaveDao;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.model.RecordTable;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.util.ConfigUtils;

/**
 * 短信发送历史-短信发送历史
 * 
 * @author zenglb
 */
@Service
@Transactional
public class SendHisServiceImpl implements SendHisService {
	@Autowired
	private RecordSlaveDao recordSlaveDao;
	
	@Autowired
	private CommonService commonService;

	@Override
	public PageContainer query(Map<String, String> params) throws Exception {
		PageContainer page = new PageContainer();
		
		//==============================分表需求-开始==================================
//		List<String> tableList = new ArrayList<>();
//		List<SplitTableTime> splitTableTimeList = commonService.getSplitTableTime(params.get("start_time"), params.get("end_time"));
//		for(SplitTableTime st : splitTableTimeList){
//			
//			String flag = st.getFlag();
//			String start_time = st.getStart_time();
//			String end_time = st.getEnd_time();
//			List<String> tableListTemp = null;
//			
//			if("old".equals(flag)){
//				tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_record_, start_time,end_time);
//			}else{
//				String identify = params.get("identify");
//				String channelid = params.get("channelid");
//				
//				if(identify != null && channelid != null){
//					tableListTemp = commonService.getExistTable(DbType.ucpaas_message_record, TablePrefix.t_sms_record_, start_time,end_time, identify);
//				}else if(identify != null && channelid == null){
//					tableListTemp = commonService.getExistTable(DbType.ucpaas_message_record, TablePrefix.t_sms_record_, start_time,end_time, identify);
//				}else{
//					tableListTemp = commonService.getExistTableForRecord(DbType.ucpaas_message_record, TablePrefix.t_sms_record_, start_time,end_time,params.get("channelid"));
//				}
//			}
//			tableList.addAll(tableListTemp);
//		}
		
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		
		List<String> tableList = new ArrayList<>();
		List<String> tableListTemp_old = commonService.getExistTable(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time);
		
		List<String> tableListTemp_new = null;
		String identify = params.get("identify");
		if(identify !=null){
			tableListTemp_new = commonService.getExistTable(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time, identify);
		}else{
			tableListTemp_new = commonService.getExistTableForRecord(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time,params.get("channelid"));
		}
		tableList.addAll(tableListTemp_old);
		tableList.addAll(tableListTemp_new);
		
		//==============================分表需求-结束==================================
		
		if (tableList.size() > 0) {
			String tableName = tableList.get(0);
			Map<String, Integer> recordTableSchema = getRecordTableSchema(tableName);
			
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			p.put("table_list", tableList);
			p.putAll(recordTableSchema);

			page = recordSlaveDao.getSearchPage("sendhis.query", "sendhis.queryCount", p);

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
//			List<String> tableListTemp = null;
//			
//			if("old".equals(flag)){
//				tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_record_, start_time,end_time);
//			}else{
//				String identify = params.get("identify");
//				String channelid = params.get("channelid");
//				
//				if(identify != null && channelid != null){
//					tableListTemp = commonService.getExistTable(DbType.ucpaas_message_record, TablePrefix.t_sms_record_, start_time,end_time, identify);
//				}else if(identify != null && channelid == null){
//					tableListTemp = commonService.getExistTable(DbType.ucpaas_message_record, TablePrefix.t_sms_record_, start_time,end_time, identify);
//				}else{
//					tableListTemp = commonService.getExistTableForRecord(DbType.ucpaas_message_record, TablePrefix.t_sms_record_, start_time,end_time,params.get("channelid"));
//				}
//			}
//			tableList.addAll(tableListTemp);
//		}
		
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		
		List<String> tableList = new ArrayList<>();
		List<String> tableListTemp_old = commonService.getExistTable(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time);
		
		List<String> tableListTemp_new = null;
		String identify = params.get("identify");
		if(identify !=null){
			tableListTemp_new = commonService.getExistTable(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time, identify);
		}else{
			tableListTemp_new = commonService.getExistTableForRecord(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time,params.get("channelid"));
		}
		tableList.addAll(tableListTemp_old);
		tableList.addAll(tableListTemp_new);
		
		//==============================分表需求-结束==================================
		if (tableList.size() > 0) {
			String tableName = tableList.get(0);
			Map<String, Integer> recordTableSchema = getRecordTableSchema(tableName);
			
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			p.put("table_list", tableList);
			p.putAll(recordTableSchema);

			return recordSlaveDao.getSearchList("sendhis.query",  p);

		}else{
			return null;
		}
		 
	}
	
	/**
	 * 查询Record流水表表结构状态
	 * @return
	 */
	private Map<String, Integer> getRecordTableSchema(String tableName){
		
		// 获得当前access流水表定义的所有字段，key表示字段名，value表示字段是否存在（下面会有1表示字段存在，0表示不存在）
		RecordTable recordTable = new RecordTable();
		Map<String, Integer> recordTableSchema = recordTable.getAllFieldsMap();
		
		// 根据表名从INFORMATION_SCHEMA.COLUMNS中获得该表存在的所有字段的名字
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("table_name", tableName);
		sqlParams.put("record_database_name", recordSlaveDao.getOneInfo("sendhis.getCurrentDatabaseName", null));
		List<Map<String, Object>> columns = recordSlaveDao.getSearchList("sendhis.getRecordTableSchema", sqlParams);
		
		// 根据实际存在的字段重新设置表结构中字段是否存在的状态（初始化是0，这里设置为 1表示存在）
		for (Map<String, Object> map : columns) {
			recordTableSchema.put(String.valueOf(map.get("COLUMN_NAME")), 1);
		}
		return recordTableSchema;
	}

}
