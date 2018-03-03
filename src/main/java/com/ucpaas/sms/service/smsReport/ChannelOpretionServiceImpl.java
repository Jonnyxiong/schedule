package com.ucpaas.sms.service.smsReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.RecordSlaveDao;

/**
 * 通道运维报表查询
 * 
 * @author oylx
 */
@Service
@Transactional
public class ChannelOpretionServiceImpl implements ChannelOpretionService {
	@Autowired
	private RecordSlaveDao recordSlaveDao;

	@Override
	public Map<String, Object> query(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("page", recordSlaveDao.getSearchPage("channelOpretion.query", "channelOpretion.queryCount", params));
		data.put("total", recordSlaveDao.getOneInfo("channelOpretion.total", params));
		return data;
	}
	
	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) {
		List<Map<String, Object>> dataList = recordSlaveDao.getSearchList("channelOpretion.query", params);
		List<Map<String, Object>> totalRowList = recordSlaveDao.getSearchList("channelOpretion.total", params);
		
		if(totalRowList.size() != 0 && null != totalRowList){
			totalRowList.get(0).put("date", "总计");
			totalRowList.get(0).put("operatorstype_name", " - ");
			totalRowList.get(0).put("channelid", " - ");
			totalRowList.get(0).put("remark", " - ");
			dataList.addAll(totalRowList);
		}
		
		return dataList;
	}

}
