
package com.ucpaas.sms.action.salesman;

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
import com.ucpaas.sms.service.salesman.MonthlyStatisticsService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 销售人员月统计报表
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/salesman/monthlystatistics/query.jsp")})

public class MonthlyStatisticsAction extends BaseAction {
	
	private static final long serialVersionUID = 5498953302132385964L;
	@Autowired
	private MonthlyStatisticsService monthlyStatisticsService;
	
	@Action("/salesman/monthlystatistics/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		String name = null == params.get("name") ? "" : params.get("name");
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		if (null == start_time || null == end_time) {
			DateTime dt = DateTime.now();
			start_time = dt.withMonthOfYear(1).toString("yyyy-MM");
			end_time = dt.withMonthOfYear(12).toString("yyyy-MM");;
		}
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		
		params.put("start_time", start_time + "-01");
		params.put("end_time", end_time + "-01");
		params.put("name", name);
		page = monthlyStatisticsService.query(params);
		return "query";
	}
	
	/**
	 * 导出销售人员月销售统计报表Excel
	 */
	@Action("/salesman/monthlystatistics/exportExcel")
	public void exportExcel(){
		Map<String, String> params = StrutsUtils.getFormData();
		String name = null == params.get("name") ? "" : params.get("name");
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		if (null == start_time || null == end_time) {
			DateTime dt = DateTime.now();
			start_time = dt.withMonthOfYear(1).toString("yyyy-MM");
			end_time = dt.withMonthOfYear(12).toString("yyyy-MM");;
		}
				
		params.put("start_time", start_time + "-01");
		params.put("end_time", end_time + "-01");
		params.put("name", name);
		params.put("limit", "");
		
		String filePath = ConfigUtils.save_path + "/销售人员月销售统计报表.xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("销售人员月销售统计报表");

		StringBuffer buffer = new StringBuffer();
        buffer.append("查询条件：");
		if (params.get("start_time") != null) {
			buffer.append("  开始时间：");
			buffer.append(params.get("start_time"));
			buffer.append(";");
		}

		if (params.get("end_time") != null) {
			buffer.append("  结束时间：");
			buffer.append(params.get("end_time"));
			
		}
		
		if (params.get("name") != null) {
			buffer.append("  销售人员姓名：");
			buffer.append(params.get("name"));
		}

		excel.addRemark(buffer.toString());
		
		excel.addHeader(10, "销售人员", "salesman_name");
		excel.addHeader(20, "客户名称", "customer_name");
		excel.addHeader(20, "月份", "recharge_time");
		excel.addHeader(20, "备注", "rechargemark");
		excel.addHeader(20, "充值金额", "rechargemoney");
		excel.addHeader(20, "合同单价", "recharge_unit_price");
		excel.addHeader(20, "成本价", "recharge_cost_price");
		excel.addHeader(20, "利润", "recharge_profit");
		excel.addHeader(20, "该客户月提成", "month_customer_royalty");
		excel.addHeader(20, "销售人员月提成", "month_salesman_royalty");
		excel.addHeader(20, "月回款任务额", "month_returned_money_task");
		excel.addHeader(20, "月实际回款", "month_actual_returned_money");
		excel.addHeader(20, "指标完成比率", "target_finished_ratio");
		excel.addHeader(20, "指标完成系数", "coefficient_of_individual_targets");
		excel.addHeader(20, "月实际提成", "month_actual_royalty");
		excel.setDataList(monthlyStatisticsService.queryAll(params));
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}
	
}
