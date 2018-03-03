/**
 * 
 */
package com.ucpaas.sms.service.salesman;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
 * 销售人员管理
 *
 */
@Service
@Transactional
public class SalesmanServiceImpl implements SalesmanService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesmanServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private LogService logService;
	
	@Override
	public PageContainer query(Map<String, String> params) {
		LOGGER.debug("销售统计报表-销售人员管理，查询：" + params);
		return masterDao.getSearchPage("salesman.query", "salesman.queryCount", params);
	}
	
	@Override
	public Map<String, Object> save(Map<String, String> params) {
		String name = params.get("name");
		String email = params.get("email");
		Map<String, Object> data = new HashMap<String, Object>();
		if (null == name || null == email) {
			data.put("result", "fail");
			data.put("msg", "姓名和公司邮箱不能为空");
			return data;
		}
		
		params.put("id", UUID.randomUUID().toString().replace("-", ""));
		LOGGER.debug("销售统计报表-销售人员管理，添加：" + params);
		int res = masterDao.insert("salesman.insertSalesman", params);
		
		data.put("result", "success");
		data.put("msg", "成功添加" + res + "个销售人员");
		logService.add(LogType.add, LogEnum.销售统计报表.getValue(),"销售统计报表-销售人员管理，添加", params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> delete(Map<String, String> params){
		LOGGER.debug("销售统计报表-销售人员管理，删除：" + params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		masterDao.update("salesman.handOverCustomer", params);
		masterDao.update("salesman.handOverRechargeRecord", params);
		masterDao.delete("salesman.deleteTask", params);
		int res = masterDao.delete("salesman.deleteSalesman", params);
		if(res == 1){
			data.put("result", "success");
			data.put("msg", "成功删除销售人员");
		}else{
			data.put("result", "fail");
			data.put("msg", "删除销售人员失败");
		}
		logService.add(LogType.delete,LogEnum.销售统计报表.getValue(), "销售统计报表-销售人员删除，", params, data);
		return data;
	}
	
	@Override
	public PageContainer queryTask(Map<String, String> params) {
		LOGGER.debug("销售统计报表-月回款任务管理，查询：" + params);
		return masterDao.getSearchPage("salesman.queryTask", "salesman.queryTaskCount", params);
	}
	
	@Override
	public Map<String, Object> saveTask(Map<String, String> params) {
		String salesman_id = params.get("salesman_id");
		String returned_money_task = params.get("returned_money_task");
		String taskofmoth = params.get("taskofmoth") + "01"; // taskofmoth保存格式为yyyyMMdd，实际使用yyyyMM
		params.put("taskofmoth", taskofmoth);
		Map<String, Object> data = new HashMap<String, Object>();
		if (null == salesman_id || null == returned_money_task || null == taskofmoth) {
			data.put("result", "fail");
			data.put("msg", "输入数据不合法");
			return data;
		}
		
		int check = masterDao.getSearchSize("salesman.insertTaskCheck", params);
		if( check > 0 ){
			data.put("result", "fail");
			data.put("msg", "添加失败，该销售已存在该月月回款任务");
			return data;
		}
		
		LOGGER.debug("销售统计报表-月回款任务管理，添加：" + params);
		int res = masterDao.insert("salesman.insertTask", params);
		
		data.put("result", "success");
		data.put("msg", "成功添加" + res + "条月回款任务");
		logService.add(LogType.add,LogEnum.销售统计报表.getValue(), "销售统计报表-月回款任务管理，添加", params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> deleteTask(Map<String, String> params){
		LOGGER.debug("销售统计报表-月回款任务管理，删除：" + params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		int res = masterDao.delete("salesman.deleteTask", params);
		if(res == 1){
			data.put("result", "success");
			data.put("msg", "成功删除月回款任务");
		}else{
			data.put("result", "fail");
			data.put("msg", "删除月回款任务失败");
		}
		logService.add(LogType.delete, LogEnum.销售统计报表.getValue(),"销售统计报表-月回款任务管理，删除", params, data);
		return data;
	}

}
