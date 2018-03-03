/**
 * 
 */
package com.ucpaas.sms.service.salesman;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.BaseDao;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.service.LogService;

/**
 * 销售月统计报表
 *
 */
@Service
@Transactional
public class MonthlyStatisticsServiceImpl implements MonthlyStatisticsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyStatisticsServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private LogService logService;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		LOGGER.debug("销售统计报表-销售月统计报表，查询：" + params);
		PageContainer page = masterDao.getSearchPage("monthlyStatistics.query", "monthlyStatistics.queryCount", params);
		
		return page;
	}
	
	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) {
		LOGGER.debug("销售统计报表-销售月统计报表，查询：" + params);
		
		return masterDao.getSearchList("monthlyStatistics.query", params);
	}
	
}
