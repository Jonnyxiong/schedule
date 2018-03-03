package com.ucpaas.sms.service.smsReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.dao.RecordSlaveDao;
import com.ucpaas.sms.model.PageContainer;

/**
 * 通道运营报表查询
 * 
 */
@Service
@Transactional
public class ChannelOpretingServiceImpl implements ChannelOpretingService {
	@Autowired
	private RecordSlaveDao recordSlaveDao;
	@Autowired
	private MessageMasterDao messageMasterDao;

	@Override
	public Map<String, Object> query(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		PageContainer page = recordSlaveDao.getSearchPage("channelOpreting.query", "channelOpreting.queryCount", params);
		
		setBelongBusinessName(page.getList());
		
		data.put("page", page);
		data.put("total", recordSlaveDao.getOneInfo("channelOpreting.total", params));
		return data;
	}
	
	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) {
		List<Map<String, Object>> dataList = recordSlaveDao.getSearchList("channelOpreting.query", params);
		List<Map<String, Object>> totalRowList = recordSlaveDao.getSearchList("channelOpreting.total", params);
		
		setBelongBusinessName(dataList);
		
		if(totalRowList.size() != 0 && null != totalRowList){
			totalRowList.get(0).put("date", "总计");
			totalRowList.get(0).put("operatorstype_name", "合计");
			totalRowList.get(0).put("channelid", " - ");
			totalRowList.get(0).put("remark", " - ");
			dataList.addAll(totalRowList);
		}
		
		return dataList;
	}
	
	/**
	 * 添加页面显示“归属商务”信息
	 * @param dataList
	 */
	private void setBelongBusinessName(List<Map<String, Object>> dataList){
		for (Map<String, Object> row : dataList) {
			String belongBusiness = Objects.toString(row.get("belong_business"), "");
			String belongBusinessName = "";
			if(StringUtils.isNotBlank(belongBusiness)){
				belongBusinessName = messageMasterDao.getOneInfo("channelOpreting.queryBelongBusinessName", belongBusiness);
			}
			row.put("belong_business_name", Objects.toString(belongBusinessName, ""));
		}
	}

}
