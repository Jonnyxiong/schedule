package com.ucpaas.sms.action.sendhis;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ucpaas.sms.util.CommonUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.sendhis.SendHisService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 短信发送历史-短信发送历史
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sendhis/query.jsp") })
public class SendHisAction extends BaseAction {
	private static Logger userOperatingLog = LoggerFactory.getLogger("UserOperatingLog");
	private static final long serialVersionUID = -1234214414349439397L;
	private SendHisService sendHisService;

	@Resource
	public void setSendHisService(SendHisService sendHisService) {
		this.sendHisService = sendHisService;
	}

	/**
	 * 短信发送记录
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Action("/sendhis/query")
	public String query() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		String area_id = params.get("area_id");
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		String duration = params.get("duration");
		String identify = params.get("identify");
		
		if(start_time == null || end_time == null){
			DateTime dt = DateTime.now();
			start_time = dt.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			end_time = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("start_time", start_time);
			params.put("end_time", end_time);
		}
		if (area_id == null) {
			area_id = "0";
			params.put("area_id", area_id);
		}
		if(duration == null){
			params.put("duration", "-1");
		}
		
		StrutsUtils.setAttribute("area_id", area_id);
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		StrutsUtils.setAttribute("identify", identify);
		
		page = sendHisService.query(params);
		return "query";
	}

	/**
	 * 导出Excel文件
	 * @throws Exception 
	 */
	@Action("/sendhis/exportExcel")
	public void exportExcel() throws Exception {
		HttpServletRequest request = StrutsUtils.getRequest();
		String ip = CommonUtil.getClientIP(request);
		Long userId = AuthorityUtils.getLoginUserId();
		String realName = AuthorityUtils.getLoginRealName();
		Integer roleId = AuthorityUtils.getLoginRoleId();

		userOperatingLog.info("userId={}, realName={}, roleId={}, ip={}, downloaded send history",userId,realName,roleId,ip);
		Map<String, String> params = StrutsUtils.getFormData();
		String filePath = ConfigUtils.save_path + "/短信发送记录.xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("短信发送记录");


		StringBuffer buffer = new StringBuffer();
        buffer.append("查询条件：");
		if (params.get("sid_clientid") != null) {
			buffer.append("用户账号：");
			buffer.append(params.get("sid_clientid"));
			buffer.append(";");
		}
		if (params.get("phone") != null) {
			buffer.append("  手机号码：");
			buffer.append(params.get("phone"));
			buffer.append(";");
		}
		if (params.get("content") != null) {
			buffer.append("  短信内容：");
			buffer.append(params.get("content"));
			buffer.append(";");
		}
		if (params.get("channel_id_name") != null) {
			buffer.append("  通道号：");
			buffer.append(params.get("channel_id_name"));
			buffer.append(";");
		}
		if (params.get("area_id_name") != null) {
			buffer.append("  区域：");
			buffer.append(params.get("area_id_name"));
			buffer.append(";");
		}
		if (params.get("state_name") != null) {
			buffer.append("  状态：");
			buffer.append(params.get("state_name"));
			buffer.append(";");
		}
		if (params.get("operatorstype_name") != null) {
			buffer.append("  运营商类型：");
			buffer.append(params.get("operatorstype_name"));
			buffer.append(";");
		}
		if (params.get("start_time") != null) {
			buffer.append("  开始时间：");
			buffer.append(params.get("start_time"));
			buffer.append(";");
		}

		if (params.get("end_time") != null) {
			buffer.append("  结束时间：");
			buffer.append(params.get("end_time"));
			
		}
		excel.addRemark(buffer.toString());
		
		excel.addHeader(10, "通道号", "channelid");
		excel.addHeader(40, "短信内容", "content");
		excel.addHeader(20, "手机号码", "phone");
		excel.addHeader(10, "发送时长(秒)", "duration");
		excel.addHeader(10, "状态", "state_name");
		excel.addHeader(20, "提交状态描述", "submit");
		excel.addHeader(20, "处理时间", "date");
		excel.addHeader(20, "提交时间", "submitdate");
		excel.addHeader(20, "应答状态描述", "subret");
		excel.addHeader(20, "应答时间", "subretdate");
		excel.addHeader(20, "状态报告描述", "report");
		excel.addHeader(20, "状态报告时间", "reportdate");
		excel.addHeader(20, "send接收状态报告时间", "recvreportdate");
		excel.addHeader(20, "处理描述", "errorcode");
		excel.addHeader(20, "平台账号", "sid");
		excel.addHeader(20, "用户账号", "clientid");
		excel.addHeader(10, "拆分条数", "smscnt");
		excel.addHeader(20, "运营商类型", "operatorstype_name");
		excel.addHeader(20, "区域", "area_name");
		excel.addHeader(20, "短信协议类型", "smsfrom");
		excel.addHeader(20, "短信id", "smsid");
		excel.addHeader(20, "显示号码", "showphone");
		excel.addHeader(20, "模板ID", "template_id");
		excel.addHeader(20, "通道模板编号", "channel_tempid");
		excel.addHeader(20, "模板参数值", "temp_params");
		
		excel.setDataList(sendHisService.queryAll(params));
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}
}
