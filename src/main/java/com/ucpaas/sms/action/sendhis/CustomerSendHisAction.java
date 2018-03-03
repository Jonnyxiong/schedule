package com.ucpaas.sms.action.sendhis;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.util.CommonUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.sendhis.customerSend.CustomerSendHisService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 短信发送历史-客户发送历史记录（Access）
 * 
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sendhis/customerSend/query.jsp") })
public class CustomerSendHisAction extends BaseAction {

	private static Logger userOperatingLog = LoggerFactory.getLogger("UserOperatingLog");
	private static final long serialVersionUID = 4209600036549395640L;
	@Autowired
	private CustomerSendHisService customerSendHisService;


	/**
	 * 客户发送历史记录
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Action("/cusSendhis/query")
	public String query() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		String start_time = params.get("start_time");
		String end_time = params.get("end_time");
		String identify = params.get("identify");
		String clientid = params.get("clientid");
		
		
		if (start_time == null || end_time == null) {
			DateTime dt = DateTime.now();
			start_time = dt.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			end_time = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("start_time", start_time);
			params.put("end_time", end_time);
		}
		StrutsUtils.setAttribute("start_time", start_time);
		StrutsUtils.setAttribute("end_time", end_time);
		StrutsUtils.setAttribute("identify", identify);
		StrutsUtils.setAttribute("clientid", clientid);
		
		page = customerSendHisService.query(params);
		return "query";
	}

	/**
	 * 导出Excel文件
	 * @throws Exception 
	 */
	@Action("/cusSendhis/exportExcel")
	public void exportExcel() throws Exception {
		HttpServletRequest request = StrutsUtils.getRequest();
		String ip = CommonUtil.getClientIP(request);
		Long userId = AuthorityUtils.getLoginUserId();
		String realName = AuthorityUtils.getLoginRealName();
		Integer roleId = AuthorityUtils.getLoginRoleId();

		userOperatingLog.info("userId={}, realName={}, roleId={}, ip={}, downloaded customer send history",userId,realName,roleId,ip);




		Map<String, String> params = StrutsUtils.getFormData();



		String filePath = ConfigUtils.save_path + "/客户发送历史记录.xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("客户发送历史记录");

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
		if (params.get("agent_id") != null) {
			buffer.append("  代理商ID：");
			buffer.append(params.get("agent_id"));
			buffer.append(";");
		}
		if (params.get("channelid") != null) {
			buffer.append("  通道号：");
			buffer.append(params.get("channelid"));
			buffer.append(";");
		}
		if (params.get("state") != null) {
			buffer.append("  状态：");
			buffer.append(params.get("state"));
			buffer.append(";");
		}
		if (params.get("smstype") != null) {
			buffer.append("  短信类型：");
			String smstype = params.get("smstype");
			String text = "";
			if(StringUtils.isBlank(smstype)){
				text = "所有类型";
			}else if(smstype.equals("0")){
				text = "通知";
			}else if(smstype.equals("4")){
				text = "验证码";
			}else if(smstype.equals("5")){
				text = "营销";
			}else if(smstype.equals("6")){
				text = "告警短信";
			}else if(smstype.equals("7")){
				text = "UUSD";
			}else if(smstype.equals("8")){
				text = "闪信";
			}
			buffer.append(text);
			buffer.append(";");
		}
		if (params.get("operatorstype_name") != null) {
			buffer.append("  运营商类型：");
			buffer.append(params.get("operatorstype_name"));
			buffer.append(";");
		}

		if (params.get("smsid_submitid") != null) {
			buffer.append("  smsid/submitid：");
			buffer.append(params.get("smsid_submitid"));
			buffer.append(";");
		}
		if (params.get("templatetype") != null) {
			buffer.append("  短信模板类型：");
			buffer.append(params.get("templatetype_name"));
			buffer.append(";");
		}
		if (params.get("smsfrom") != null) {
			buffer.append("  短信协议：");
			String smsfrom = params.get("smsfrom");
			String text = "";
			if(smsfrom.equals("-1")){
				text = "所有协议";
			}else if(smsfrom.equals("2")){
				text = "SMPP协议";
			}else if(smsfrom.equals("3")){
				text = "CMPP协议";
			}else if(smsfrom.equals("4")){
				text = "SGIP协议";
			}else if(smsfrom.equals("5")){
				text = "SMGP协议";
			}else if(smsfrom.equals("6")){
				text = "HTTPS协议";
			}
			buffer.append(text);
			buffer.append(";");
		}

		if (params.get("start_time") != null) {
			buffer.append("  开始时间：");
			buffer.append(params.get("start_time"));
			
		}
		if (params.get("end_time") != null) {
			buffer.append("  结束时间：");
			buffer.append(params.get("end_time"));
			
		}
		excel.addRemark(buffer.toString());
		
		excel.addHeader(10, "通道号", "channelid");
		excel.addHeader(10, "通道名称", "channelname");
		excel.addHeader(20, "用户账号", "clientid");
		excel.addHeader(20, "用户用户名称", "username");
		excel.addHeader(20, "代理商ID", "agent_id");
		excel.addHeader(20, "短信ID", "smsid");
		excel.addHeader(40, "订单号", "sub_id");
		excel.addHeader(40, "计费条数", "charge_num");
		excel.addHeader(40, "短信类型", "smstype");
		excel.addHeader(40, "短信内容", "content");
		excel.addHeader(20, "手机号码", "phone");
		excel.addHeader(10, "状态", "state_name");
		excel.addHeader(20, "处理描述", "errorcode");
		excel.addHeader(20, "处理时间", "date");
		excel.addHeader(20, "客户状态报告ID", "submitid");
		excel.addHeader(20, "提交状态描述", "submit");
		excel.addHeader(20, "提交时间", "submitdate");
		excel.addHeader(20, "状态报告描述", "report");
		excel.addHeader(20, "状态报告时间", "reportdate");
		excel.addHeader(10, "拆分条数", "smscnt");
		excel.addHeader(10, "短信拆分序号", "smsindex");
		excel.addHeader(20, "运营商类型", "operatorstype_name");
		excel.addHeader(20, "短信协议类型", "smsfrom");
		excel.addHeader(20, "显示自签名的方式", "showsigntype");
		excel.addHeader(20, "透传ID", "uid");
		excel.addHeader(20, "模板ID", "template_id");
		excel.addHeader(20, "通道模板编号", "channel_tempid");
		excel.addHeader(20, "模板参数值", "temp_params");
		
		List<Map<String, Object>> dataList = customerSendHisService.queryAll(params);
		excel.setDataList(dataList);
		
		boolean generateExcelSuccess = ExcelUtils.exportExcel(excel);
		if (generateExcelSuccess) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}
	
	@Action("/cusSendhis/queryKeyWords")
	public void queryKeyWords(){
		String clientId = StrutsUtils.getParameterTrim("clientId");
		data = customerSendHisService.queryKeyWords(clientId);
		StrutsUtils.renderJson(data);
	}
	
}
