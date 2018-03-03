package com.ucpaas.sms.service.smsReport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.AccessSlaveDao;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;

/**
 * 客户运维报表查询
 * 
 */
@Service
@Transactional
public class CustomerOpretionServiceImpl implements CustomerOpretionService {
	@Autowired
	private AccessSlaveDao messageStatSlaveDao;
	@Autowired
	private MessageMasterDao messageMasterDao;

	@Override
	public Map<String, Object> query(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		PageContainer pageContainer = messageStatSlaveDao.getSearchPage("customerOpretion.query", "customerOpretion.queryCount", params);
		
		List<Map<String, Object>> pageList = pageContainer.getList();
		for (Map<String, Object> row : pageList) {
			String belongSale = Objects.toString(row.get("belong_sale"), "");
			
			// 查询“归属销售”的名称
			String belongSaleName = messageMasterDao.getOneInfo("customerOpreting.queryBelongSaleName", belongSale);
			row.put("belongSaleName", belongSaleName);
		}
		data.put("page", pageContainer);
		data.put("total", messageStatSlaveDao.getOneInfo("customerOpretion.total", params));
		return data;
	}
	
	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) {
		List<Map<String, Object>> dataList = messageStatSlaveDao.getSearchList("customerOpretion.query", params);
		List<Map<String, Object>> totalRowList = messageStatSlaveDao.getSearchList("customerOpretion.total", params);
		
		for (Map<String, Object> row : dataList) {
			String belongSale = Objects.toString(row.get("belong_sale"), "");
			
			// 查询“归属销售”的名称
			String belongSaleName = messageMasterDao.getOneInfo("customerOpreting.queryBelongSaleName", belongSale);
			row.put("belongSaleName", belongSaleName);
		}
		
		if(totalRowList.size() != 0 && null != totalRowList){
			totalRowList.get(0).put("date", "总计");
			totalRowList.get(0).put("clientid", " - ");
			totalRowList.get(0).put("name", " - ");
			totalRowList.get(0).put("agent_id", " - ");
			totalRowList.get(0).put("paytype", " - ");
			totalRowList.get(0).put("operatorstype_name", " - ");
			totalRowList.get(0).put("channelid", " - ");
			totalRowList.get(0).put("remark", " - ");
			dataList.addAll(totalRowList);
		}
		
		return dataList;
	}

}
