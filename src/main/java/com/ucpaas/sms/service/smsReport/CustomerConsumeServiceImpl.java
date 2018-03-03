/**
 * 
 */
package com.ucpaas.sms.service.smsReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.DbConstant.DbType;
import com.ucpaas.sms.constant.DbConstant.TablePrefix;
import com.ucpaas.sms.dao.AccessSlaveDao;
import com.ucpaas.sms.service.CommonService;

/**
 * 客户消耗统计报表实现
 */
@Service
@Transactional
public class CustomerConsumeServiceImpl implements CustomerConsumeService {
	
	@Autowired
	private AccessSlaveDao accessSlaveDao;
	@Autowired
	private CommonService commonService;
	
	@Override
	public Map<String, Object> query(Map<String, String> params) throws Exception{
		Map<String, Object> data = new HashMap<String, Object>();
		params = paramHandler(params); // 查询参数处理
		
		
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
//				tableListTemp = commonService.getExistTableForAccess(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time,end_time,params.get("clientid"));
//			}
//			tableList.addAll(tableListTemp);
//		}
		
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		List<String> tableList = new ArrayList<>();
		List<String> tableListTemp_old = commonService.getExistTable(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time);
		String clientid = params.get("clientid");
		
		List<String> tableListTemp_new = null;
		if(clientid != null && !"".equals(clientid)){
			tableListTemp_new = commonService.getExistTableForAccess(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time,clientid);
		}else{
			tableListTemp_new = commonService.getExistTableForAccess(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time);
		}
		
		tableList.addAll(tableListTemp_old);
		tableList.addAll(tableListTemp_new);
		
		//==============================分表需求-结束==================================
		
		if (tableList.size() > 0) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			p.put("table_list", tableList);
			data.put("page", accessSlaveDao.getSearchPage("customerConsume.query", "customerConsume.queryCount", p));
			data.put("total", accessSlaveDao.getOneInfo("customerConsume.total", p));
		}
		return data;
	}
	
	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) throws Exception{
		params = paramHandler(params);
		
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
//				tableListTemp = commonService.getExistTableForAccess(DbType.ucpaas_message_statistics, TablePrefix.t_sms_access_, start_time,end_time,params.get("clientid"));
//			}
//			tableList.addAll(tableListTemp);
//		}
		
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		List<String> tableList = new ArrayList<>();
		List<String> tableListTemp_old = commonService.getExistTable(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time);
		String clientid = params.get("clientid");
		
		List<String> tableListTemp_new = null;
		if(clientid != null && !"".equals(clientid)){
			tableListTemp_new = commonService.getExistTableForAccess(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time,clientid);
		}else{
			tableListTemp_new = commonService.getExistTableForAccess(DbType.ucpaas_message_access_slave, TablePrefix.t_sms_access_, start_time,end_time);
		}		
		tableList.addAll(tableListTemp_old);
		tableList.addAll(tableListTemp_new);
		
		//==============================分表需求-结束==================================
		
		if (tableList.size() > 0) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			p.put("table_list", tableList);
			List<Map<String, Object>> allDataList = accessSlaveDao.getSearchList("customerConsume.query",  p);
			List<Map<String, Object>> totalRowList = accessSlaveDao.getSearchList("customerConsume.total", p);
			
			if(totalRowList.size() != 0 && null != totalRowList){
				totalRowList.get(0).put("clientid", "总计");
				totalRowList.get(0).put("sendTotal", totalRowList.get(0).get("sendTotal"));
				totalRowList.get(0).put("successTotal", totalRowList.get(0).get("successTotal"));
				totalRowList.get(0).put("sumbmitFail", totalRowList.get(0).get("sumbmitFail"));
				totalRowList.get(0).put("successRate", totalRowList.get(0).get("successRate"));
				totalRowList.get(0).put("audit", totalRowList.get(0).get("audit"));
				totalRowList.get(0).put("intercept", totalRowList.get(0).get("intercept"));
				totalRowList.get(0).put("undetermined", totalRowList.get(0).get("undetermined"));
				totalRowList.get(0).put("successRate", totalRowList.get(0).get("successRate"));
				
				allDataList.addAll(totalRowList);
			}
			return allDataList;
		}else{
			return null;
		}
	}
	
	
	private Map<String, String> paramHandler(Map<String, String> params){
//		DateTime dt = DateTime.now();
//		String today = dt.toString("yyyy-MM-dd");
//		String start_time = params.get("start_time");
//		String end_time = params.get("end_time");
//		start_time = today + " " + start_time;
//		end_time = today + " " + end_time;
//		params.put("start_time", start_time);
//		params.put("end_time", end_time);
		
		return params;
	}
}
