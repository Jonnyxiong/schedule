package com.ucpaas.sms.action.smsReport;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.smsReport.ChannelOpretingService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 通道运营报表
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/smsreport/channelOpreting/query.jsp") })
public class ChannelOpretingAction extends BaseAction {
	
	private static final long serialVersionUID = -8056798339913829321L;
	@Autowired
	private ChannelOpretingService channelOpretingService;

	
	@Action("/channelOpreting/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		String stat_type = params.get("stat_type");
		String start_time_day = params.get("start_time_day");
		String end_time_day = params.get("end_time_day");
		String start_time_month = params.get("start_time_month");
		String end_time_month = params.get("end_time_month");
		if (stat_type == null || start_time_day == null || end_time_day == null || start_time_month == null
				|| end_time_month == null) {
			stat_type = "1";
			DateTime dt = DateTime.now();
			start_time_day = dt.minusDays(1).toString("yyyyMMdd");
			end_time_day = start_time_day;
			start_time_month = dt.minusMonths(1).toString("yyyyMM");
			end_time_month = start_time_month;

			params.put("stat_type", stat_type);
			params.put("start_time_day", start_time_day);
			params.put("end_time_day", end_time_day);
			params.put("start_time_month", start_time_month);
			params.put("end_time_month", end_time_month);
		}
		StrutsUtils.setAttribute("stat_type", stat_type);
		StrutsUtils.setAttribute("start_time_day", start_time_day);
		StrutsUtils.setAttribute("end_time_day", end_time_day);
		StrutsUtils.setAttribute("start_time_month", start_time_month);
		StrutsUtils.setAttribute("end_time_month", end_time_month);
		
		data = channelOpretingService.query(params);
		return "query";
	}
	
	/**
	 * 导出Excel文件
	 */
	@Action("/channelOpreting/exportExcel")
	public void exportExcel() {
		Map<String, String> params = StrutsUtils.getFormData();
		String filePath = ConfigUtils.save_path + "/通道运营报表.xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("通道运营报表");

		StringBuffer buffer = new StringBuffer();
        buffer.append("查询条件：");
        if(params.get("stat_type").equals("1")){
        	buffer.append("  统计类型：");
    		buffer.append("  每日统计");
    		buffer.append(";");
    		
        	if (params.get("start_time_day") != null) {
        		buffer.append("  开始时间：");
        		buffer.append(params.get("start_time_day"));
        		buffer.append(";");
        	}
        	
        	if (params.get("end_time_day") != null) {
    			buffer.append("  结束时间：");
    			buffer.append(params.get("end_time_day"));
    			
    		}
        }else{
        	buffer.append("  统计类型：");
    		buffer.append("  每月统计");
    		buffer.append(";");
    		
        	if (params.get("start_time_month") != null) {
        		buffer.append("  开始时间：");
        		buffer.append(params.get("start_time_month"));
        		buffer.append(";");
        	}
        	
        	if (params.get("end_time_month") != null) {
    			buffer.append("  结束时间：");
    			buffer.append(params.get("end_time_month"));
    			
    		}
        }
        
        buffer.append("  通道号：");
		buffer.append(params.get("channel_id") !=null ? params.get("channel_id") : " 所有");
		buffer.append(";");
		
		buffer.append("  运营商类型：");
		buffer.append(params.get("operatorstype_name"));
		buffer.append(";");
		excel.addRemark(buffer.toString());
		
		excel.addHeader(20, "时间", "date");
		excel.addHeader(20, "运营商", "operatorstype_name");
		excel.addHeader(20, "通道号", "channelid");
		excel.addHeader(20, "通道备注", "remark");
		excel.addHeader(20, "归属商务", "belong_business_name");
		excel.addHeader(20, "计费条数", "chargetotal");
		excel.addHeader(20, "总成本(元)", "costtotal");
		excel.setDataList(channelOpretingService.queryAll(params));
		
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}

}
