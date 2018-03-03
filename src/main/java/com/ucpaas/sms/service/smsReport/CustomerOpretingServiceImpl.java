package com.ucpaas.sms.service.smsReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.AccessSlaveDao;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.dao.RecordSlaveDao;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 客户运营报表查询
 * 
 */
@Service
@Transactional
public class CustomerOpretingServiceImpl implements CustomerOpretingService {
	@Autowired
	private AccessSlaveDao messageStatSlaveDao;
	
	@Autowired
	private RecordSlaveDao recordSlaveDao;
	
	@Autowired
	private MessageMasterDao messageMasterDao;

	@Override
	public Map<String, Object> query(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		PageContainer pageContainer = messageStatSlaveDao.getSearchPage("customerOpreting.query", "customerOpreting.queryCount", params);
		List<Map<String, Object>> pageList = pageContainer.getList();
		
		for (Map<String, Object> row : pageList) {
			String clientid = Objects.toString(row.get("clientid"), "");
			String channelid = Objects.toString(row.get("channelid"), "");
			String date = Objects.toString(row.get("date"), "");
			String paytype = Objects.toString(row.get("paytype"), "");
			String belongSale = Objects.toString(row.get("belong_sale"), "");
			String operatorstype = Objects.toString(row.get("operatorstype"), "");
			
			// 查询“归属销售”的名称
			String belongSaleName = messageMasterDao.getOneInfo("customerOpreting.queryBelongSaleName", belongSale);
			row.put("belongSaleName", belongSaleName);
			
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			BigDecimal realCost;
			if(operatorstype.equals("-2")){
				sqlParams.put("clientid", clientid);
				sqlParams.put("date", date);
				// 每日合计的真实通道成本价
				realCost =  recordSlaveDao.getOneInfo("customerOpreting.queryRealChannelCostDailySum", sqlParams);
				row.put("realCost", realCost);
			}else{
				sqlParams.put("clientid", clientid);
				sqlParams.put("channelid", channelid);
				sqlParams.put("date", date);
				sqlParams.put("paytype", paytype);
				sqlParams.put("belongSale", belongSale);
				realCost =  recordSlaveDao.getOneInfo("customerOpreting.queryRealChannelCost", sqlParams);
				row.put("realCost", realCost);
			}
			
			if(channelid.equals("0")){
				row.put("realCost", "0.000");
			}
		}
		data.put("page", pageContainer);
		
		
		// 总计数据
		Map<String, Object> totalRowDataMap = messageStatSlaveDao.getOneInfo("customerOpreting.total", params);
		if(totalRowDataMap != null){
			// 查询总计中的真实通道成本价
			Map<String, Object> totalRowSqlParams = new HashMap<String, Object>();
			List<String> clientIdList = messageStatSlaveDao.selectList("customerOpreting.querySearchResultContainClientId", params);
			totalRowSqlParams.putAll(params);
			totalRowSqlParams.put("clientIdList", clientIdList);
			BigDecimal totalRealCost = recordSlaveDao.getOneInfo("customerOpreting.queryTotalRealCost", totalRowSqlParams);
			totalRowDataMap.put("realCost", totalRealCost);
		}
		data.put("total", totalRowDataMap);
		return data;
	}
	
	@Override
	public List<Map<String, Object>> queryAll(Map<String, String> params) {
		List<Map<String, Object>> dataList = messageStatSlaveDao.getSearchList("customerOpreting.query", params);
		List<Map<String, Object>> totalRowList = messageStatSlaveDao.getSearchList("customerOpreting.total", params);
		
		for (Map<String, Object> row : dataList) {
			String clientid = Objects.toString(row.get("clientid"), "");
			String channelid = Objects.toString(row.get("channelid"), "");
			String date = Objects.toString(row.get("date"), "");
			String paytype = Objects.toString(row.get("paytype"), "");
			String belongSale = Objects.toString(row.get("belong_sale"), "");
			String operatorstype = Objects.toString(row.get("operatorstype"), "");
			
			// 查询“归属销售”的名称
			String belongSaleName = messageMasterDao.getOneInfo("customerOpreting.queryBelongSaleName", belongSale);
			row.put("belongSaleName", StringUtils.isBlank(belongSaleName) ? " - " : belongSaleName);
			
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			BigDecimal realCost;
			if(operatorstype.equals("-2")){
				sqlParams.put("clientid", clientid);
				sqlParams.put("date", date);
				// 每日合计的真实通道成本价
				realCost =  recordSlaveDao.getOneInfo("customerOpreting.queryRealChannelCostDailySum", sqlParams);
				row.put("realCost", realCost);
			}else{
				sqlParams.put("clientid", clientid);
				sqlParams.put("channelid", channelid);
				sqlParams.put("date", date);
				sqlParams.put("paytype", paytype);
				sqlParams.put("belongSale", belongSale);
				realCost =  recordSlaveDao.getOneInfo("customerOpreting.queryRealChannelCost", sqlParams);
				row.put("realCost", realCost);
			}
			
			if(channelid.equals("0")){
				row.put("realCost", "0.000");
			}
		}
		
		if(totalRowList.size() != 0 && null != totalRowList){
			totalRowList.get(0).put("date", "总计");
			totalRowList.get(0).put("clientid", " - ");
			totalRowList.get(0).put("name", " - ");
			totalRowList.get(0).put("agent_id", " - ");	
			totalRowList.get(0).put("belongSaleName", " - ");
			totalRowList.get(0).put("paytypeText", " - ");
			totalRowList.get(0).put("smsfrom", " - ");
			totalRowList.get(0).put("operatorstype_name", " - ");
			totalRowList.get(0).put("channelid", " - ");
			totalRowList.get(0).put("remark", " - ");
			
			// 查询总计中的真实通道成本价
			Map<String, Object> totalRowSqlParams = new HashMap<String, Object>();
			List<String> clientIdList = messageStatSlaveDao.selectList("customerOpreting.querySearchResultContainClientId", params);
			totalRowSqlParams.putAll(params);
			totalRowSqlParams.put("clientIdList", clientIdList);
			BigDecimal totalRealCost = recordSlaveDao.getOneInfo("customerOpreting.queryTotalRealCost", totalRowSqlParams);
			totalRowList.get(0).put("realCost", totalRealCost);
						
			dataList.addAll(totalRowList);
		}
		
		return dataList;
	}

	@Override
	public void exportExcel(Map<String, String> formData) {
		Map<String, String> params = StrutsUtils.getFormData();
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String filePath = ConfigUtils.save_path + "/客户运营报表" + timeStamp + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("客户运营报表");

		StringBuffer buffer = new StringBuffer();
        buffer.append("查询条件：");
        if(params.get("stat_type").equals("1")){
        	buffer.append("  统计类型-");
    		buffer.append("每日统计");
    		buffer.append(";");
    		
        	if (params.get("start_time_day") != null) {
        		buffer.append("  开始时间-");
        		buffer.append(params.get("start_time_day"));
        		buffer.append(";");
        	}
        	
        	if (params.get("end_time_day") != null) {
    			buffer.append("  结束时间-");
    			buffer.append(params.get("end_time_day"));
    			
    		}
        }else{
        	buffer.append("  统计类型-");
    		buffer.append("每月统计");
    		buffer.append(";");
    		
        	if (params.get("start_time_month") != null) {
        		buffer.append("  开始时间-");
        		buffer.append(params.get("start_time_month"));
        		buffer.append(";");
        	}
        	
        	if (params.get("end_time_month") != null) {
    			buffer.append("  结束时间-");
    			buffer.append(params.get("end_time_month"));
    		}
        }
        
        if(params.get("text") != null){
        	buffer.append("  账号-");
        	buffer.append(params.get("text"));
        	buffer.append(";");
        }
        
        buffer.append("  付费类型-");
        if(params.get("paytype").equals("-1")){
        	buffer.append("所有");
        }else{
        	buffer.append(params.get("paytypeText"));
        }
        buffer.append(";");
		
        buffer.append("  协议类型-");
        if(params.get("smsfrom") == null){
        	buffer.append("所有");
        }else{
        	buffer.append(params.get("smsfrom_name"));
        }
        buffer.append(";");
        
        buffer.append("  通道号-");
		buffer.append(params.get("channel_id") !=null ? params.get("channel_id") : " 所有");
		buffer.append(";");
		
		buffer.append("  运营商类型-");
		buffer.append(params.get("operatorstype_name"));
		buffer.append(";");
		
		buffer.append("  所属销售-");
		buffer.append(params.get("belong_sale_name"));
		buffer.append(";");
		
		excel.addRemark(buffer.toString());
		
		excel.addHeader(20, "时间", "date");
		excel.addHeader(20, "用户ID", "clientid");
		excel.addHeader(20, "用户名称", "name");
		excel.addHeader(20, "代理商ID", "agent_id");
		excel.addHeader(20, "所属销售", "belongSaleName");
		excel.addHeader(20, "付费类型", "paytypeText");
		excel.addHeader(20, "运营商类型", "operatorstype_name");
		excel.addHeader(20, "通道号", "channelid");
		excel.addHeader(20, "通道备注", "remark");
		excel.addHeader(20, "计费规则", "chargeRuleStr");
		excel.addHeader(20, "计费条数", "chargetotal");
		excel.addHeader(20, "通道预估价(元)", "costfee");
		excel.addHeader(20, "通道成本价(元)", "realCost");
		excel.addHeader(20, "客户购买价(元)", "salefee");
		excel.addHeader(20, "代理商成本价(元)", "productfee");
		excel.addHeader(20, "代理商佣金(元)", "agent_commission");
		excel.addHeader(20, "毛利(元)", "grossprofit");
		excel.addHeader(20, "毛利率", "grossmargin");
		
		excel.setDataList(this.queryAll(params));
		
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
		
	}
	
	

}
