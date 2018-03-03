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
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.dao.RecordSlaveDao;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.CommonService;

/**
 * 通道消耗统计报表
 *
 */
@Service
@Transactional
public class ChannelConsumeServiceImpl implements ChannelConsumeService{

	@Autowired
	private RecordSlaveDao recordSlaveDao;
	@Autowired
	private MessageMasterDao messageMasterDao;
	@Autowired
	private CommonService commonService;
	
	/**
	 * 分页查询
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> query(Map<String, String> params) throws Exception {
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
//				tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_record_, start_time,end_time);
//			}else{
//				tableListTemp = commonService.getExistTableForRecord(DbType.ucpaas_message_record, TablePrefix.t_sms_record_, start_time,end_time,params.get("channelid"));
//			}
//			tableList.addAll(tableListTemp);
//		}
		
		
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		List<String> tableList = new ArrayList<>();
		
		List<String> tableListTemp_old = commonService.getExistTable(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time);
		List<String> tableListTemp_new = null;
		
		String channelid = params.get("channelid");
		if(channelid != null && !"".equals(channelid)){
			tableListTemp_new = commonService.getExistTableForRecord(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time,params.get("channelid"));
		}else{
			tableListTemp_new = commonService.getExistTableForRecord(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time);
		}
		
		tableList.addAll(tableListTemp_old);
		tableList.addAll(tableListTemp_new);
		
		//==============================分表需求-结束==================================
		
		if (tableList.size() > 0) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			p.put("table_list", tableList);

			List<Map<String, Object>> pageList = recordSlaveDao.getSearchList("channelConsume.query", p);
			for (Map<String, Object> row : pageList) {
				String channelRemark = messageMasterDao.getOneInfo("channelConsume.queryChannelRemarkByChannelId", row.get("channelid"));
				row.put("channelRemark", channelRemark);
			}
			data.put("page", pageList);
			data.put("total", recordSlaveDao.getOneInfo("channelConsume.total", p));

		}
		return data;
	}

	/**
	 * Excel用于导出
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) throws Exception {
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
//				tableListTemp = commonService.getExistTable(DbType.ucpaas_message_statistics, TablePrefix.t_sms_record_, start_time,end_time);
//			}else{
//				tableListTemp = commonService.getExistTableForRecord(DbType.ucpaas_message_record, TablePrefix.t_sms_record_, start_time,end_time,params.get("channelid"));
//			}
//			tableList.addAll(tableListTemp);
//		}
		
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		List<String> tableList = new ArrayList<>();
		
		List<String> tableListTemp_old = commonService.getExistTable(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time);
		List<String> tableListTemp_new = null;
		
		String channelid = params.get("channelid");
		if(channelid != null && !"".equals(channelid)){
			tableListTemp_new = commonService.getExistTableForRecord(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time,params.get("channelid"));
		}else{
			tableListTemp_new = commonService.getExistTableForRecord(DbType.ucpaas_message_record_slave, TablePrefix.t_sms_record_, start_time,end_time);
		}
		
		tableList.addAll(tableListTemp_old);
		tableList.addAll(tableListTemp_new);
		
		//==============================分表需求-结束==================================
		
		if (tableList.size() > 0) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			p.put("table_list", tableList);

			List<Map<String, Object>> allDataList = recordSlaveDao.getSearchList("channelConsume.query",  p);
			for (Map<String, Object> row : allDataList) {
				String channelRemark = messageMasterDao.getOneInfo("channelConsume.queryChannelRemarkByChannelId", row.get("channelid"));
				row.put("channelRemark", channelRemark);
			}
			
			List<Map<String, Object>> totalRowList = recordSlaveDao.getSearchList("channelConsume.total", p);

			
			if(totalRowList.size() != 0 && null != totalRowList){
				totalRowList.get(0).put("channelid", "总计");
				totalRowList.get(0).put("channelRemark", " - ");
				totalRowList.get(0).put("sendSuccess", totalRowList.get(0).get("successTotal"));
				totalRowList.get(0).put("submitFailed", totalRowList.get(0).get("sumbmitFail"));
				totalRowList.get(0).put("sendFailed", totalRowList.get(0).get("sendFail"));
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
