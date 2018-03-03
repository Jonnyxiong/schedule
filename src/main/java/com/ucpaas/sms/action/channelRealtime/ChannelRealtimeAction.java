package com.ucpaas.sms.action.channelRealtime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.constant.ChartConstant;
import com.ucpaas.sms.model.ChannelSuccessRateRealtime;
import com.ucpaas.sms.model.ClientSuccessRateRealtime;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.model.Highcharts;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.channelRealtime.ChannelRealtimeService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.rest.utils.DateUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 短信通道质量实时监控
 * 
 * @author xiejiaan
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "channelRealtime", location = "/WEB-INF/content/channelRealtime/channelRealtime.jsp"),
	@Result(name = "clientSpeed", location = "/WEB-INF/content/channelRealtime/clientSpeed.jsp"),
	@Result(name = "accessQueue", location = "/WEB-INF/content/channelRealtime/accessQueue.jsp"),
	@Result(name = "channelQualityGraph", location = "/WEB-INF/content/channelRealtime/channelQualityGraph.jsp"),
	@Result(name = "channelQualityTable", location = "/WEB-INF/content/channelRealtime/channelQualityTable.jsp"),
	@Result(name = "channelQualityDetails", location = "/WEB-INF/content/channelRealtime/channelQualityDetails.jsp"),
	@Result(name = "channelError", location = "/WEB-INF/content/channelRealtime/channelError.jsp"),
	@Result(name = "clientQuality", location = "/WEB-INF/content/channelRealtime/clientQuality.jsp"),
	@Result(name = "reportRespStack", location = "/WEB-INF/content/channelRealtime/channelReportRespStack.jsp"),
	@Result(name = "clientSuccessRateStack", location = "/WEB-INF/content/channelRealtime/clientSuccessRateStack.jsp"),
	@Result(name = "channelSuccessRateStack", location = "/WEB-INF/content/channelRealtime/channelSuccessRateStack.jsp"),
	@Result(name = "clientReportRespStack", location = "/WEB-INF/content/channelRealtime/clientReportRespStack.jsp")})
public class ChannelRealtimeAction extends BaseAction {
	private static final long serialVersionUID = 7473305518904461752L;
	@Autowired
	private ChannelRealtimeService channelRealtimeService;

	/**
	 * 短信通道质量实时监控
	 * 
	 * @param ajax
	 *            是否是ajax请求
	 * @param menuId
	 *            菜单id
	 * @param chart
	 *            图表信息
	 * @return
	 */
	private String channelRealtime(boolean ajax, Integer menuId, Highcharts chart) {
		data = new HashMap<String, Object>();
		Map<String, String> params = StrutsUtils.getFormData();

		String channelId = params.get("channel_id");
		String areaIdPid = params.get("area_id_pid");
		String areaId = params.get("area_id");
		String startTimestamp = params.get("start_timestamp");
		if (areaId == null) {
			areaId = "0";
		}
		List<Map<String, Object>> dataList = channelRealtimeService.getChartData(channelId, areaIdPid, areaId,
				startTimestamp);

		Long xMin = null;
		Long newStartTimestamp = null;
		if (dataList != null && dataList.size() > 0) {
			xMin = Long.parseLong(dataList.get(0).get("datatime").toString());
			newStartTimestamp = Long.parseLong(dataList.get(dataList.size() - 1).get("datatime").toString());
		}
		if (ajax) {
			data.put("start_timestamp", newStartTimestamp);
			data.put("dataList", dataList);
			StrutsUtils.renderJson(data);
			return null;
		} else {
			String channel_id_name = params.get("channel_id_name");
			String area_id_input = params.get("area_id_input");
			StringBuilder sb = new StringBuilder();
			sb.append("通道号：");
			sb.append(channel_id_name == null ? "所有" : channel_id_name);
			sb.append("，地区：");
			sb.append(area_id_input == null ? "所有" : area_id_input);
			sb.append("，时间：");
			sb.append(DateTime.now().toString("yyyy-MM-dd"));

			chart.setDataList(dataList);
			chart.setSubtitle(sb.toString());
			chart.setxMin(xMin);
			data.put("chart", chart);
			data.put("menuId", menuId);
			data.put("area_id", areaId);
			data.put("start_timestamp", newStartTimestamp);
			return "channelRealtime";
		}
	}

	private String channelRealtime(Integer menuId, Highcharts chart) {
		return channelRealtime(false, menuId, chart);
	}

	/**
	 * ajax获取图表数据
	 * 
	 * @return
	 */
	@Action("/channelRealtime/ajax")
	public String ajax() {
		return channelRealtime(true, null, null);
	}

	/**
	 * 短信通道数量实时监控
	 * 
	 * @return
	 */
	@Action("/channelRealtime/quantity")
	public String quantity() {
		return channelRealtime(17, ChartConstant.channel_realtime_quantity);
	}

	/**
	 * 短信通道成功率实时监控
	 * 
	 * @return
	 */
	@Action("/channelRealtime/rate")
	public String rate() {
		return channelRealtime(18, ChartConstant.channel_realtime_rate);
	}

	/**
	 * 短信通道延时时长实时监控
	 * 
	 * @return
	 */
	@Action("/channelRealtime/delay")
	public String delay() {
		return channelRealtime(19, ChartConstant.channel_realtime_delay);
	}

	/**
	 * 短信通道并发量实时监控
	 * 
	 * @return
	 */
	@Action("/channelRealtime/concurrency")
	public String concurrency() {
		return channelRealtime(20, ChartConstant.channel_realtime_concurrency);
	}
	
	
	/**
	 * 短信发送速率监控
	 * @return
	 */
	@Action("/channelRealtime/clientSpeed/page")
	public String monitor() {
		return "clientSpeed";
	}
	
	/**
	 * 查询clientId发送速度
	 */
	@Action("/channelRealtime/sigleClient/seed-speed")
	public void getClientSendSpeedById(){
		data = new HashMap<String, Object>();
		Map<String, String> params = StrutsUtils.getFormData();
		if(null != params && params.get("clientId") != null){
			String clientId = params.get("clientId");
			data = channelRealtimeService.getClientSendSpeedById(clientId);
			StrutsUtils.renderJson(data);
			
		}
	}
	
	@Action("/channelRealtime/allClient/seed-speed")
	public void getAllClientSendSpeed(){
		data = new HashMap<String, Object>();
		data = channelRealtimeService.getAllClientSendSpeed();
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * ACCESS队列消息数量监控
	 * @return
	 */
	@Action("/channelRealtime/accessQueue/page")
	public String accessQueuePage() {
		return "accessQueue";
	}
	
	@Action("/channelRealtime/accessQueue/getAccessQueueInfo")
	public void getAccessQueueInfo() {
		dataList = new ArrayList<Map<String, Object>>();
		dataList = channelRealtimeService.getAccessQueueInfo();
		StrutsUtils.renderJson(dataList);
	}
	
	/**
	 * 通道发送质量图形
	 * @return
	 */
	@Action("/channelRealtime/channelQuality/graphPage")
	public String channelQualityPage() {
		dataList = channelRealtimeService.queryChannelQualityConfig();
		return "channelQualityGraph";
	}
	
	/**
	 * 通道发送质量详单
	 * @return
	 */
	@Action("/channelRealtime/channelQuality/tablePage")
	public String channelQualityTable() {
		Map<String, String> params = StrutsUtils.getFormData();
		String timeType = params.get("timeType");
		String channelId = params.get("channelId");
		if(timeType == null){
			timeType = "0";
			params.put("timeType", timeType);
		}
		
		if(channelId == null){
			channelId = "";
			params.put("channelId", channelId);
		}
		StrutsUtils.setAttribute("timeType", params.get("timeType"));
		StrutsUtils.setAttribute("channelId", params.get("channelId"));
		
		page = channelRealtimeService.getChannelQualityTableData(params);
		
		return "channelQualityTable";
	}
	
	@Action("/channelRealtime/channelQuality/graphData")
	public void getChannelQualityGraphData() {
		dataList = new ArrayList<Map<String, Object>>();
		Map<String, String> params = StrutsUtils.getFormData();
		String timeType = "";
		if(null != params && params.get("timeType") != null){
			timeType = params.get("timeType");
		}else{
			StrutsUtils.renderJson(dataList);
		}
		long time1 = System.currentTimeMillis();
		dataList = channelRealtimeService.getChannelQualityGraphData(params);
		long time2 = System.currentTimeMillis();
		System.out.println("##### action get service data time #####" + (time2 - time1));
		
		StrutsUtils.renderJson(dataList);
		long time3 = System.currentTimeMillis();
		System.out.println("##### action renderJson time #####" + (time3 - time2));
	}
	
	
	/**
	 * 通道质量监控详情
	 * @return
	 */
	@Action("/channelRealtime/channelQuality/details/page")
	public String channelQualityDetails() {
		Map<String, String> params = StrutsUtils.getFormData();
		if(null != params && params.get("channelId") != null && params.get("timeType") != null){
			StrutsUtils.setAttribute("channelId", params.get("channelId"));
			StrutsUtils.setAttribute("timeType", params.get("timeType"));
		}
		
		return "channelQualityDetails";
	}
	
	/**
	 * 查询通道发送速率
	 */
	@Action("/channelRealtime/channel/seed-speed")
	public void getChannelSendSpeedById(){
		data = new HashMap<String, Object>();
		Map<String, String> params = StrutsUtils.getFormData();
		String channelId = "";
		String timeType = "";
		if(null != params && params.get("channelId") != null && params.get("timeType") != null){
			channelId = params.get("channelId");
			timeType = params.get("timeType");
		}else{
			StrutsUtils.renderJson(data); // 返回空
		}
		
		data = channelRealtimeService.getChannelSendSpeedById(channelId, timeType);
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 查询通道发送成功速率
	 */
	@Action("/channelRealtime/channel/succRate")
	public void getChannelSuccRateById(){
		dataList = new ArrayList<Map<String, Object>>();
		Map<String, String> params = StrutsUtils.getFormData();
		String channelId = "";
		String timeType = "";
		if(null != params && params.get("channelId") != null && params.get("timeType") != null){
			channelId = params.get("channelId");
			timeType = params.get("timeType");
		}else{
			StrutsUtils.renderJson(dataList); // 返回空
		}
		
		dataList = channelRealtimeService.getChannelSuccRateById(channelId, timeType);
		StrutsUtils.renderJson(dataList);
	}
	
	/**
	 * 查询通道应答率
	 */
	@Action("/channelRealtime/channel/respRate")
	public void getChannelRespRateById(){
		dataList = new ArrayList<Map<String, Object>>();
		Map<String, String> params = StrutsUtils.getFormData();
		String channelId = "";
		String timeType = "";
		if(null != params && params.get("channelId") != null && params.get("timeType") != null){
			channelId = params.get("channelId");
			timeType = params.get("timeType");
		}else{
			StrutsUtils.renderJson(dataList); // 返回空
		}
		
		dataList = channelRealtimeService.getChannelRespRateById(channelId, timeType);
		StrutsUtils.renderJson(dataList);
	}
	
	/**
	 * 查询通道应答率
	 */
	@Action("/channelRealtime/channel/reportRate")
	public void getChannelReportRateById(){
		dataList = new ArrayList<Map<String, Object>>();
		Map<String, String> params = StrutsUtils.getFormData();
		String channelId = "";
		String timeType = "";
		if(null != params && params.get("channelId") != null && params.get("timeType") != null){
			channelId = params.get("channelId");
			timeType = params.get("timeType");
		}else{
			StrutsUtils.renderJson(dataList); // 返回空
		}
		
		dataList = channelRealtimeService.getChannelReportRateById(channelId, timeType);
		StrutsUtils.renderJson(dataList);
	}
	
	/**
	 * 通道错误统计
	 * @return
	 */
	@Action("/channelRealtime/channelError/page")
	public String channelError() {
		return "channelError";
	}
	
	/**
	 * 查询通道应答率
	 */
	@Action("/channelRealtime/channelError/data")
	public void getChannelErrorDataById(){
		data = new HashMap<String, Object>();
		Map<String, String> params = StrutsUtils.getFormData();
		String channelId = "";
		String timeType = "";
		if(null != params && params.get("channelId") != null && params.get("timeType") != null){
			channelId = params.get("channelId");
			timeType = params.get("timeType");
		}else{
			StrutsUtils.renderJson(data); // 返回空
		}
		
		data = channelRealtimeService.getChannelErrorDataById(channelId, timeType);
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 客户发送质量
	 * @return
	 */
	@Action("/channelRealtime/clientQuality/page")
	public String clientQuality() {
		Map<String, String> params = StrutsUtils.getFormData();
		String timeType = params.get("timeType");
		String clientId = params.get("clientId");
		if(timeType == null){
			timeType = "0";
			params.put("timeType", timeType);
		}
		
		if(clientId == null){
			clientId = "";
			params.put("clientId", clientId);
		}
		
		StrutsUtils.setAttribute("timeType", timeType);
		StrutsUtils.setAttribute("clientId", clientId);
		
		page = channelRealtimeService.getClientQualityTableData(params);
		
		return "clientQuality";
	}
	
	
	/**
	 * 查询通道回执率堆积图数据
	 */
	@Action("/channelRealtime/channel/getReportRespStackData")
	public void getChannelReportRespStackData(){
		Map<String, String> params = StrutsUtils.getFormData();
		String timeType = params.get("timeType");
		String channelId = params.get("channelId");
		if(timeType == null){
			timeType = "1";
			params.put("timeType", timeType);
		}
		
		if(channelId == null){
			channelId = "";
			params.put("channelId", channelId);
		}
		StrutsUtils.setAttribute("timeType", params.get("timeType"));
		StrutsUtils.setAttribute("channelId", params.get("channelId"));
		
		data = channelRealtimeService.getChannelReportRespStackData(channelId, timeType);
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 通道回执应答页面
	 */
	@Action("/channelRealtime/reportRespStack/page")
	public String reportRespStackPage(){
		return "reportRespStack";
	}
	
	/**
	 * 通道质量指数阀值配置更新
	 */
	@Action("/channelRealtime/config/update")
	public void channelQualityConfigUpdate(){
		Map<String, String> params = StrutsUtils.getFormData();
		
		channelRealtimeService.channelQualityConfigUpdate(params);
		System.out.println(params);
	}
	
	/**
	 * 用户质量趋势分布图
	 */
	@Action("/channelRealtime/clientReportRespStack/page")
	public String clientReportRespStackPage(){
		return "clientReportRespStack";
	}
	
	/**
	 * 查询用户回执率堆积图数据
	 */
	@Action("/channelRealtime/client/getReportRespStackData")
	public void getClientReportRespStackData(){
		Map<String, String> params = StrutsUtils.getFormData();
		String timeType = params.get("timeType");
		String clientId = params.get("clientId");
		if(timeType == null){
			timeType = "1";
			params.put("timeType", timeType);
		}
		
		if(clientId == null){
			clientId = "";
			params.put("clientId", clientId);
		}
		StrutsUtils.setAttribute("timeType", params.get("timeType"));
		StrutsUtils.setAttribute("clientId", params.get("clientId"));
		
		data = channelRealtimeService.getClientReportRespStackData(clientId, timeType);
		StrutsUtils.renderJson(data);
	}

	
	/**
	 * 用户成功率趋势分布图
	 */
	@Action("/channelRealtime/clientSuccessRateStack/page")
	public String clientSuccessRateStackPage(){
		return "clientSuccessRateStack";
	}
	
	
	/**
	 * 查询用户成功率堆积图数据
	 */
	@Action("/channelRealtime/client/getSuccessRateStackData")
	public void getClientSuccessRateStackData(){
		Map<String, String> params = StrutsUtils.getFormData();
		String timeType = params.get("timeType");
		String clientId = params.get("clientId");
		if(timeType == null){
			timeType = "3";  //默认选择24小时
			params.put("timeType", timeType);
		}
		
		if(clientId == null){
			clientId = "";
			params.put("clientId", clientId);
		}
		StrutsUtils.setAttribute("timeType", params.get("timeType"));
		StrutsUtils.setAttribute("clientId", params.get("clientId"));
		
		data = channelRealtimeService.getClientSuccessRateStackData(clientId, timeType);
		StrutsUtils.renderJson(data);
	}
	
	
	
	/**
	 * 通道成功率趋势分布图
	 */
	@Action("/channelRealtime/channelSuccessRateStack/page")
	public String channelSuccessRateStackPage(){
		return "channelSuccessRateStack";
	}
	
	
	/**
	 * 查询通道成功率堆积图数据
	 */
	@Action("/channelRealtime/channel/getSuccessRateStackData")
	public void getChannelSuccessRateStackData(){
		Map<String, String> params = StrutsUtils.getFormData();
		String timeType = params.get("timeType");
		String channelId = params.get("channelId");
		if(timeType == null){
			timeType = "3";  //默认选择24小时
			params.put("timeType", timeType);
		}
		
		if(channelId == null){
			channelId = "";
			params.put("channelId", channelId);
		}
		StrutsUtils.setAttribute("timeType", params.get("timeType"));
		StrutsUtils.setAttribute("channelId", params.get("channelId"));

		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");
		
		data = channelRealtimeService.getChannelSuccessRateStackData(channelId, startTime,endTime);
		StrutsUtils.renderJson(data);
	}
	
	
	

	/**
	 * 导出Excel通道成功率堆积图数据
	 */
	@Action("/channelHistory/Client/getSuccessRateStackData/exportExcel")
	public void exportExcel4Client() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		
		String timeType = params.get("timeType");
		String clientId = params.get("clientId");
		if(timeType == null){
			timeType = "3";  //默认选择24小时
			params.put("timeType", timeType);
		}
		
		if(clientId == null){
			clientId = "";
			params.put("channelId", clientId);
		}
		StrutsUtils.setAttribute("timeType", params.get("timeType"));
		StrutsUtils.setAttribute("clientId", params.get("clientId"));

		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");
		

		// 获得通洞啊回执率、应答率堆积图的统计数据
		List<ClientSuccessRateRealtime> cssrs = channelRealtimeService.queryAllClientSuccessRateStackData(clientId,startTime,endTime);
		
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		Long userId = AuthorityUtils.getLoginUserId();
		String filePath = ConfigUtils.save_path + "/" + clientId + "用户成功率分布_" + timeStamp + userId + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle(clientId + "用户成功率分布");

		StringBuffer buffer = new StringBuffer();
		if (params.get("clientId") != null) {
			buffer.append("  用户id：");
			buffer.append(params.get("channelId"));
			buffer.append(";");
		}

		if (params.get("startTime") != null) {
			buffer.append("  开始时间：");
			buffer.append(startTime);

		}

		if (params.get("endTime") != null) {
			buffer.append("  结束时间：");
			buffer.append(endTime);

		}
		excel.addRemark(buffer.toString());

		excel.addHeader(20, "时间", "dataTime");
		excel.addHeader(10, "用户id", "clientId");
		excel.addHeader(10, "用户名称", "clientName");
		excel.addHeader(20, "发送总量", "sendTotal");
		excel.addHeader(20, "拦截总量", "interceptTotal");
		excel.addHeader(20, "成功率", "successRate");
		excel.addHeader(20, "未知率", "fakeSuccessRate");
		excel.addHeader(20, "失败率", "reallyFailRate");
		
		
		List<Map<String, Object>> excelData = new ArrayList<>();
		for (ClientSuccessRateRealtime cssr : cssrs) {
			Map excelMap = new HashMap<>();
			excelMap.put("dataTime", DateUtil.dateToStr(cssr.getDataTime(), "yyyy-MM-dd HH:mm"));
			excelMap.put("clientId",cssr.getClientId());
			excelMap.put("clientName", cssr.getClientId());
			excelMap.put("sendTotal", cssr.getSendTotal());
			excelMap.put("interceptTotal", cssr.getInterceptTotal());
			excelMap.put("successRate",cssr.getSuccessRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() +" %");
			excelMap.put("fakeSuccessRate", cssr.getFakeSuccessRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%");
			excelMap.put("reallyFailRate", cssr.getReallyFailRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() +"%");
			excelData.add(excelMap);
		}
		excel.setDataList(excelData );
		excel.setShowRownum(false);
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}
	
	
	
	/**
	 * 导出Excel通道成功率堆积图数据
	 */
	@Action("/channelHistory/channel/getSuccessRateStackData/exportExcel")
	public void exportExcel4Channel() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		
		String timeType = params.get("timeType");
		String channelId = params.get("channelId");
		if(timeType == null){
			timeType = "3";  //默认选择24小时
			params.put("timeType", timeType);
		}
		
		if(channelId == null){
			channelId = "";
			params.put("channelId", channelId);
		}
		StrutsUtils.setAttribute("timeType", params.get("timeType"));
		StrutsUtils.setAttribute("channelId", params.get("channelId"));

		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");
		

		// 获得通洞啊回执率、应答率堆积图的统计数据
		List<ChannelSuccessRateRealtime> cssrs = channelRealtimeService.queryAllChannelSuccessRateStackData(channelId,startTime,endTime);
		
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		Long userId = AuthorityUtils.getLoginUserId();
		String filePath = ConfigUtils.save_path + "/" + channelId + "通道成功率分布_" + timeStamp + userId + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle(channelId + "通道成功率分布");

		StringBuffer buffer = new StringBuffer();
		if (params.get("clientId") != null) {
			buffer.append("  通道id：");
			buffer.append(params.get("channelId"));
			buffer.append(";");
		}

		if (params.get("startTime") != null) {
			buffer.append("  开始时间：");
			buffer.append(startTime);

		}

		if (params.get("endTime") != null) {
			buffer.append("  结束时间：");
			buffer.append(endTime);

		}
		excel.addRemark(buffer.toString());

		excel.addHeader(20, "时间", "dataTime");
		excel.addHeader(10, "通道id", "channelId");
		excel.addHeader(10, "通道名称", "channelName");
		excel.addHeader(20, "发送总量", "sendTotal");
		excel.addHeader(20, "提交失败总量", "submitFail");
		excel.addHeader(20, "发送失败总量", "sendFail");
		excel.addHeader(20, "成功率", "successRate");
		excel.addHeader(20, "未知率", "fakeSuccessRate");
		excel.addHeader(20, "失败率", "reallyFailRate");
		
		
		List<Map<String, Object>> excelData = new ArrayList<>();
		for (ChannelSuccessRateRealtime cssr : cssrs) {
			Map excelMap = new HashMap<>();
			excelMap.put("dataTime", DateUtil.dateToStr(cssr.getDataTime(), "yyyy-MM-dd HH:mm"));
			excelMap.put("channelId", cssr.getChannelId());
			excelMap.put("channelName", cssr.getChannelName());
			excelMap.put("sendTotal", cssr.getSendTotal());
			excelMap.put("submitFail", cssr.getSubmitFail());
			excelMap.put("sendFail", cssr.getSendFail());
			excelMap.put("successRate",cssr.getSuccessRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() +" %");
			excelMap.put("fakeSuccessRate", cssr.getFakeSuccessRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%");
			excelMap.put("reallyFailRate", cssr.getReallyFailRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() +"%");
			excelData.add(excelMap);
		}
		excel.setDataList(excelData );
		excel.setShowRownum(false);
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}
	
	
	private Map<String, Object> getStartAndEndTime(String timeType) {
		Map<String, Object> result = new HashMap<String, Object>();
		DateTime now = new DateTime();
		// now = new DateTime(2016,12,22,0,0,0);
		String startTime = "";
		String endTime = "";
		if (timeType.equals("0")) { // 5分钟
			startTime = now.minusMinutes(5).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("1")) { // 1小时
			startTime = now.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("2")) { // 12小时
			startTime = now.minusHours(12).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("3")) { // 24小时
			startTime = now.minusHours(24).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else {

		}

		result.put("startTime", startTime);
		result.put("endTime", endTime);

		return result;
	}
}
