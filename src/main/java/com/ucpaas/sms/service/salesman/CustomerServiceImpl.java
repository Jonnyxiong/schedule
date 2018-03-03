/**
 * 
 */
package com.ucpaas.sms.service.salesman;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;


/**
 * 客户管理
 *
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private LogService logService;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		
		return masterDao.getSearchPage("customer.query", "customer.queryCount", params);
	}
	
	@Override
	public Map<String, Object> save(Map<String, String> params) {
		String customer_name = params.get("customer_name");
		String salesman_id = params.get("salesman_id");
		Map<String, Object> data = new HashMap<String, Object>();
		if( null == customer_name || null == salesman_id){
			data.put("result", "fail");
			data.put("msg", "客户名称和销售人员不合法");
		}
		
//		List<Map<String, Object>> checkList = dao.getSearchList("customer.checkCustomer", params);
//		int res = 0;
//		if( null != checkList && checkList.size() == 1 ){
//			res = dao.update("customer.refreshCustomer", params);
//		}else{
//			params.put("id", UUID.randomUUID().toString().replace("-", ""));
//			res = dao.insert("customer.insertCustomer", params);
//		}
		params.put("id", UUID.randomUUID().toString().replace("-", ""));
		int res = masterDao.insert("customer.insertCustomer", params);
		LOGGER.debug("销售统计报表-客户管理，添加：" + params);
		
		data.put("result", "success");
		data.put("msg", "成功添加" + res + "个客户");
		logService.add(LogType.add,LogEnum.销售统计报表.getValue(), "销售统计报表-客户管理，添加：", params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> delete(Map<String, String> params){
		LOGGER.debug("销售统计报表-客户管理，删除：" + params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		int res = masterDao.delete("customer.deleteCustomer", params);
		if(res == 1){
			data.put("result", "success");
			data.put("msg", "成功删除客户");
		}else{
			data.put("result", "fail");
			data.put("msg", "删除客户失败");
		}
		logService.add(LogType.add, LogEnum.销售统计报表.getValue(),"销售统计报表-客户管理，删除", params, data);
		return data;
	}

}
