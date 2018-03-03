/**
 * 
 */
package com.ucpaas.sms.service.salesman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.BaseDao;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.service.LogService;

/**
 * 客户充值记录管理
 *
 */
@Service
@Transactional
public class CustomerRechargeServiceImpl implements CustomerRechargeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRechargeServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private LogService logService;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		
		return masterDao.getSearchPage("recharge.query", "recharge.queryCount", params);
	}
	
	@Override
	public Map<String, Object> save(Map<String, String> params) {
		String customer_id = params.get("customer_id");
		String salesman_id = params.get("salesman_id");
		String recharge_money = params.get("recharge_money");
		String recharge_mark = params.get("recharge_mark");
		String recharge_unit_price = params.get("recharge_unit_price");
		String recharge_cost_price = params.get("recharge_cost_price");
		String rechargetime = params.get("rechargetime") + "-01";
		params.put("rechargetime", rechargetime);
		Map<String, Object> data = new HashMap<String, Object>();
		if(customer_id == null || salesman_id == null || recharge_money == null
				|| recharge_mark == null || recharge_unit_price == null || recharge_cost_price == null){
			data.put("result", "fail");
			data.put("msg", "输入数据不合法");
			return data;
		}
		
		LOGGER.debug("销售统计报表-客户充值管理，添加：" + params);
		int res = masterDao.insert("recharge.insertRecharge", params);
		
		data.put("result", "success");
		data.put("msg", "成功添加" + res + "条充值记录");
		logService.add(LogType.add, LogEnum.销售统计报表.getValue(),"销售统计报表-客户充值管理，添加", params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> delete(Map<String, String> params){
		LOGGER.debug("销售统计报表-客户充值管理，删除：" + params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		int res = masterDao.delete("recharge.deleteRecharge", params);
		if(res == 1){
			data.put("result", "success");
			data.put("msg", "成功删除充值记录");
		}else{
			data.put("result", "fail");
			data.put("msg", "删除充值记录失败");
		}
		logService.add(LogType.delete, LogEnum.销售统计报表.getValue(),"销售统计报表-客户充值管理，添加", params, data);
		return data;
	}
	
	@Override
	public List<Map<String, Object>> getSalesmanById(Map<String, String> params) {
		
		return masterDao.getSearchList("recharge.getSalesmanById", params);
	}

}
