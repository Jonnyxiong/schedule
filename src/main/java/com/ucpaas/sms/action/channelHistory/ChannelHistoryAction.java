package com.ucpaas.sms.action.channelHistory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.channelHistory.ChannelHistoryService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 短信通道质量历史监控
 * 
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "reportRespStack", location = "/WEB-INF/content/channelHistory/channelReportRespStack.jsp"),
		@Result(name = "clientReportRespStack", location = "/WEB-INF/content/channelHistory/clientReportRespStack.jsp") })

public class ChannelHistoryAction extends BaseAction {

	private static final long serialVersionUID = 6939972991594924259L;

	@Autowired
	private ChannelHistoryService channelHistoryService;

	/**
	 * 通道质量历史监控页面
	 */
	@Action("/channelHistory/channelQuality/page")
	public String channelHistoryPage() {
		return "reportRespStack";
	}

	/**
	 * 查询通道回执率堆积图数据
	 */
	@Action("/channelHistory/channel/getReportRespStackData")
	public void getChannelReportRespStackData() {
		Map<String, String> params = StrutsUtils.getFormData();

		String channelId = Objects.toString(params.get("channelId"), "");
		String startTime = Objects.toString(params.get("startTime"), "");
		String endTime = Objects.toString(params.get("endTime"), "");

		if (StringUtils.isBlank(channelId)) {
			data = new HashMap<String, Object>();
			StrutsUtils.renderJson(data);
		} else {

			if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
				DateTime now = new DateTime();
				endTime = now.toString("yyyyMMdd");
				startTime = now.minusDays(30).toString("yyyyMMdd");
			}

			StrutsUtils.setAttribute("startTime", startTime);
			StrutsUtils.setAttribute("endTime", endTime);
			StrutsUtils.setAttribute("channelId", channelId);

			data = channelHistoryService.getChannelReportRespStackData(params);
			StrutsUtils.renderJson(data);

		}

	}

	/**
	 * 通道回执应答页面
	 */
	@Action("/channelHistory/reportRespStack/page")
	public String reportRespStackPage() {
		return "reportRespStack";
	}

	/**
	 * 导出Excel
	 */
	@Action("/channelHistory/reportRespStack/exportExcel")
	public void exportExcel() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		String channelId = Objects.toString(params.get("channelId"), "");
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		Long userId = AuthorityUtils.getLoginUserId();
		String filePath = ConfigUtils.save_path + "/" + channelId + "通道质量趋势分布_" + timeStamp + userId + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle(channelId + "通道质量趋势分布");

		StringBuffer buffer = new StringBuffer();
		if (params.get("channelId") != null) {
			buffer.append("  通道号：");
			buffer.append(params.get("channelId"));
			buffer.append(";");
		}

		if (params.get("startTime") != null) {
			buffer.append("  开始时间：");
			buffer.append(params.get("startTime"));

		}

		if (params.get("endTime") != null) {
			buffer.append("  结束时间：");
			buffer.append(params.get("endTime"));

		}
		excel.addRemark(buffer.toString());

		excel.addHeader(10, "时间", "data_time");
		excel.addHeader(10, "通道号", "channel_id");
		excel.addHeader(10, "通道名称", "channel_name");
		excel.addHeader(20, "回执时长[0s-5s]", "report1");
		excel.addHeader(20, "回执时长(5s-10s]", "report2");
		excel.addHeader(20, "回执时长(10s-30s]", "report3");
		excel.addHeader(20, "回执时长(30s-60s]", "report4");
		excel.addHeader(20, "回执时长[60s以上]", "report5");
		excel.addHeader(20, "回执未返回", "report6");
		excel.addHeader(20, "应答时长[0s-1s]", "resp1");
		excel.addHeader(20, "应答时长 (1s-5s]", "resp2");
		excel.addHeader(20, "应答时长(5s-30s]", "resp3");
		excel.addHeader(20, "应答时长(30s-60s]", "resp4");
		excel.addHeader(20, "应答时长[60s以上]", "resp5");
		excel.addHeader(20, "应答超时", "resp6");
		excel.addHeader(20, "成功率", "sccuss_rate");
		excel.addHeader(20, "发送总量(条)", "send_total_num");

		excel.setDataList(channelHistoryService.queryExportExcelData(params));
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}

	
	
	/**
	 * 用户质量历史监控页面
	 */
	@Action("/channelHistory/clientQuality/page")
	public String clientReportRespStackPage() {
		return "clientReportRespStack";
	}
	/**
	 * 查询用户回执率堆积图数据
	 */
	@Action("/channelHistory/client/getReportRespStackData")
	public void getClientReportRespStackData() {
		Map<String, String> params = StrutsUtils.getFormData();

		String clientId = Objects.toString(params.get("clientId"), "");
		String startTime = Objects.toString(params.get("startTime"), "");
		String endTime = Objects.toString(params.get("endTime"), "");

		if (StringUtils.isBlank(clientId)) {
			data = new HashMap<String, Object>();
			StrutsUtils.renderJson(data);
		} else {

			if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
				DateTime now = new DateTime();
				endTime = now.toString("yyyyMMdd");
				startTime = now.minusDays(30).toString("yyyyMMdd");
			}

			StrutsUtils.setAttribute("startTime", startTime);
			StrutsUtils.setAttribute("endTime", endTime);
			StrutsUtils.setAttribute("clientId", clientId);

			data = channelHistoryService.getClientIdReportRespStackData(params);
			StrutsUtils.renderJson(data);

		}

	}
	
	


	/**
	 * 导出Excel
	 */
	@Action("/channelHistory/clientReportRespStack/exportExcel")
	public void exportExcel4Client() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		String clientId = Objects.toString(params.get("clientId"), "");
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		Long userId = AuthorityUtils.getLoginUserId();
		String filePath = ConfigUtils.save_path + "/" + clientId + "用户质量趋势分布_" + timeStamp + userId + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle(clientId + "用户质量趋势分布");

		StringBuffer buffer = new StringBuffer();
		if (params.get("clientId") != null) {
			buffer.append("  用户账号：");
			buffer.append(params.get("clientId"));
			buffer.append(";");
		}

		if (params.get("startTime") != null) {
			buffer.append("  开始时间：");
			buffer.append(params.get("startTime"));

		}

		if (params.get("endTime") != null) {
			buffer.append("  结束时间：");
			buffer.append(params.get("endTime"));

		}
		excel.addRemark(buffer.toString());

		excel.addHeader(10, "时间", "data_time");
		excel.addHeader(10, "用户账号", "client_id");
		excel.addHeader(10, "用户名称", "client_name");
		excel.addHeader(20, "回执时长[0s-5s]", "report1");
		excel.addHeader(20, "回执时长(5s-10s]", "report2");
		excel.addHeader(20, "回执时长(10s-30s]", "report3");
		excel.addHeader(20, "回执时长(30s-60s]", "report4");
		excel.addHeader(20, "回执时长(60s以上)", "report5");
		excel.addHeader(20, "回执未返回", "report6");
		excel.addHeader(20, "延迟时长[0s-1s]", "delay1");
		excel.addHeader(20, "延迟时长(1s-3s]", "delay2");
		excel.addHeader(20, "延迟时长(3s-5s]", "delay3");
		excel.addHeader(20, "延迟时长(5s以上)", "delay4");
		excel.addHeader(20, "未发送", "delay5");
		excel.addHeader(20, "发送总量(条)", "send_total_num");

		excel.setDataList(channelHistoryService.queryExportExcelData4Client(params));
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}
}
