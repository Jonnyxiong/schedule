package com.ucpaas.sms.service.channelHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.StatsMasterDao;

/**
 * 短信通道质量历史监控
 * 
 */
@Service
@Transactional
public class ChannelHistoryServiceImpl implements ChannelHistoryService {
	
	@Autowired
	private StatsMasterDao statsMasterDao;
	
	private static final Logger logger = LoggerFactory.getLogger(ChannelHistoryServiceImpl.class);

	@Override
	public Map<String, Object> getChannelReportRespStackData(Map<String, String> params) {
		
		// 获得通道回执率、应答率堆积图的统计数据
		List<Map<String, Object>> channelReportStackDataMap = statsMasterDao.getSearchList("channelHistory.getChannelReportRespStackData", params);
		
		// 构建EChart显示数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Object> report1 = new ArrayList<Object>();
		List<Object> report2 = new ArrayList<Object>();
		List<Object> report3 = new ArrayList<Object>();
		List<Object> report4 = new ArrayList<Object>();
		List<Object> report5 = new ArrayList<Object>();
		List<Object> report6 = new ArrayList<Object>();
		List<Object> resp1 = new ArrayList<Object>();
		List<Object> resp2 = new ArrayList<Object>();
		List<Object> resp3 = new ArrayList<Object>();
		List<Object> resp4 = new ArrayList<Object>();
		List<Object> resp5 = new ArrayList<Object>();
		List<Object> resp6 = new ArrayList<Object>();
		List<Object> sccessRate = new ArrayList<Object>();
		List<Object> sendTotal = new ArrayList<Object>();
		List<Object> xAxisData = new ArrayList<Object>();
		for (Map<String, Object> map : channelReportStackDataMap) {
			xAxisData.add(map.get("data_time"));
			report1.add(map.get("report1"));
			report2.add(map.get("report2"));
			report3.add(map.get("report3"));
			report4.add(map.get("report4"));
			report5.add(map.get("report5"));
			report6.add(map.get("report6"));
			resp1.add(map.get("resp1"));
			resp2.add(map.get("resp2"));
			resp3.add(map.get("resp3"));
			resp4.add(map.get("resp4"));
			resp5.add(map.get("resp5"));
			resp6.add(map.get("resp6"));
			sccessRate.add(map.get("sccuss_rate"));
			sendTotal.add(map.get("send_total_num"));
		}
		resultMap.put("xAxisData", xAxisData);
		resultMap.put("report1", report1);
		resultMap.put("report2", report2);
		resultMap.put("report3", report3);
		resultMap.put("report4", report4);
		resultMap.put("report5", report5);
		resultMap.put("report6", report6);
		resultMap.put("resp1", resp1);
		resultMap.put("resp2", resp2);
		resultMap.put("resp3", resp3);
		resultMap.put("resp4", resp4);
		resultMap.put("resp5", resp5);
		resultMap.put("resp6", resp6);
		resultMap.put("sccessRate", sccessRate);
		resultMap.put("sendTotal", sendTotal);
		return resultMap;
			
	}

	@Override
	public List<Map<String, Object>> queryExportExcelData(Map<String, String> params) {
		List<Map<String, Object>> resultList = statsMasterDao.getSearchList("channelHistory.getExportExcelData", params);
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> queryExportExcelData4Client(Map<String, String> params) {
		List<Map<String, Object>> resultList = statsMasterDao.getSearchList("channelHistory.getExportExcelData4Client", params);
		return resultList;
	}

	@Override
	public Map<String, Object> getClientIdReportRespStackData(Map<String, String> params) {

		// 获得用户回执率、应答率堆积图的统计数据
		List<Map<String, Object>> channelReportStackDataMap = statsMasterDao.getSearchList("channelHistory.getClientReportRespStackData", params);
		
		// 构建EChart显示数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Object> report1 = new ArrayList<Object>();
		List<Object> report2 = new ArrayList<Object>();
		List<Object> report3 = new ArrayList<Object>();
		List<Object> report4 = new ArrayList<Object>();
		List<Object> report5 = new ArrayList<Object>();
		List<Object> report6 = new ArrayList<Object>();
		List<Object> delay1 = new ArrayList<Object>();
		List<Object> delay2 = new ArrayList<Object>();
		List<Object> delay3 = new ArrayList<Object>();
		List<Object> delay4 = new ArrayList<Object>();
		List<Object> delay5 = new ArrayList<Object>();
		List<Object> sendTotal = new ArrayList<Object>();
		List<Object> xAxisData = new ArrayList<Object>();
		for (Map<String, Object> map : channelReportStackDataMap) {
			xAxisData.add(map.get("data_time"));
			report1.add(map.get("report1"));
			report2.add(map.get("report2"));
			report3.add(map.get("report3"));
			report4.add(map.get("report4"));
			report5.add(map.get("report5"));
			report6.add(map.get("report6"));
			delay1.add(map.get("delay1"));
			delay2.add(map.get("delay2"));
			delay3.add(map.get("delay3"));
			delay4.add(map.get("delay4"));
			delay5.add(map.get("delay5"));
			sendTotal.add(map.get("send_total_num"));
		}
		resultMap.put("xAxisData", xAxisData);
		resultMap.put("report1", report1);
		resultMap.put("report2", report2);
		resultMap.put("report3", report3);
		resultMap.put("report4", report4);
		resultMap.put("report5", report5);
		resultMap.put("report6", report6);
		resultMap.put("delay1", delay1);
		resultMap.put("delay2", delay2);
		resultMap.put("delay3", delay3);
		resultMap.put("delay4", delay4);
		resultMap.put("delay5", delay5);
		resultMap.put("sendTotal", sendTotal);
		return resultMap;
	}
	
}
