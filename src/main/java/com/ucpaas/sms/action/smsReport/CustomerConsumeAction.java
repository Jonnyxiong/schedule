package com.ucpaas.sms.action.smsReport;

import java.util.HashMap;
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
import com.ucpaas.sms.service.smsReport.CustomerConsumeService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 客户消耗统计报表
 *
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "query", location="/WEB-INF/content/smsreport/customer/query.jsp")})
public class CustomerConsumeAction extends BaseAction {

	private static final long serialVersionUID = -6177619975196145331L;
	
	@Autowired
	private CustomerConsumeService customerConsumeService;
	
	/**
	 * 分页查询
	 * @return
	 * @throws Exception 
	 */
	@Action("/customerconsume/query")
	public String query() throws Exception{
		Map<String, String> params = StrutsUtils.getFormData();
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		if(start_time == null || end_time == null){
			DateTime dt = DateTime.now();
			start_time = dt.toString("yyyy-MM-dd")+" 00:00:00";
			end_time = dt.toString("yyyy-MM-dd HH:mm:ss");
		}
		
		if(null == start_time || null == end_time){
			data = new HashMap<String, Object>();
			return "query";
		}else{
			StrutsUtils.setAttribute("start_time", start_time);
			StrutsUtils.setAttribute("end_time", end_time);
			
			data = customerConsumeService.query(params);
			return "query";
		}
	}
	
	/**
	 * 导出客户消耗统计报表Excel
	 * @throws Exception 
	 */
	@Action("/customerconsume/exportExcel")
	public void exportExcel() throws Exception{
		Map<String, String> params = StrutsUtils.getFormData();
		String filePath = ConfigUtils.save_path + "/客户消耗统计报表(access).xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("客户消耗统计报表(access)");

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
		if (params.get("clientid") != null) {
			buffer.append("  用户账号：");
			buffer.append(params.get("clientid"));
			
		}
		excel.addRemark(buffer.toString());
		
		excel.addHeader(10, "用户ID", "clientid");
		excel.addHeader(10, "用户名称", "userName");
		excel.addHeader(20, "发送总数", "sendTotal");
		excel.addHeader(20, "明确成功条数", "reallySuccessTotal");
		excel.addHeader(20, "成功待定条数", "fakeSuccessFail");
		excel.addHeader(20, "计费条数", "chargeTotal");
		excel.addHeader(20, "明确失败条数", "reallyFailTotal");
		excel.addHeader(20, "审核不通过条数", "auditFailTotal");
		excel.addHeader(20, "提交失败条数", "submitFailTotal");
		excel.addHeader(20, "拦截条数", "interceptTotal");
		excel.addHeader(20, "成功率", "successRate");
		
		excel.setDataList(customerConsumeService.queryAll(params));
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}

}
