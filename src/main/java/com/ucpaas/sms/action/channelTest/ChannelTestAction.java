
package com.ucpaas.sms.action.channelTest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.exception.SmspBusinessException;
import com.ucpaas.sms.service.channelTest.ChannelTestService;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 通道测试管理
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "channelTestQuery", location="/WEB-INF/content/channelTest/channelTest.jsp"),
		@Result(name = "channelTestEdit", location="/WEB-INF/content/channelTest/channelTest-edit.jsp"),
		@Result(name = "channelTestView", location="/WEB-INF/content/channelTest/channelTest-view.jsp"),
		@Result(name = "testManageMent-tab1", location="/WEB-INF/content/channelTest/testManagment/templateMgnt.jsp"),
		@Result(name = "testManageMent-tab2", location="/WEB-INF/content/channelTest/testManagment/testPhoneMgnt.jsp"),
		@Result(name = "testManageMent-tab3", location="/WEB-INF/content/channelTest/testManagment/testSendRecord.jsp"),
		@Result(name = "testManageMent-tab4", location="/WEB-INF/content/channelTest/testManagment/testUpstreamRecord.jsp"),
		@Result(name = "selectPhonesIframe", location="/WEB-INF/content/channelTest/testManagment/selectPhonesIframe.jsp"),
		@Result(name = "viewReportIframe", location="/WEB-INF/content/channelTest/viewReportIframe.jsp"),
		@Result(name = "testPhoneEdit", location="/WEB-INF/content/channelTest/testManagment/testPhone-add.jsp")})

public class ChannelTestAction extends BaseAction {
	
	private static final long serialVersionUID = 7951966362450195236L;
	@Autowired
	private ChannelTestService channelTestService;
	
	private File upload;
	private String uploadFileName;
    private String uploadContentType;
    
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	
	
	@Action("/channelTest/downloadTestPhoneTemplate")
	public void downloadInsertTemplate() {
		String path = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/导入通道测试手机号码.xls");
		FileUtils.download(path);
	}
	
	/**
	 * 通道测试
	 */
	@Action("/channelTest/query")
	public String channelTestQuery() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = channelTestService.channelTestQuery(params);
		
		StrutsUtils.setAttribute("query_start_time", params.get("start_time"));
		StrutsUtils.setAttribute("query_end_time", params.get("end_time"));
		return "channelTestQuery";
	}
	
	/**
	 * 通道测试-编辑
	 */
	@Action("/channelTest/edit")
	public String channelTestEdit() {
		String id = StrutsUtils.getParameterTrim("id");
		data = channelTestService.channelTestEdit(id);
		
		return "channelTestEdit";
	}
	
	/**
	 * 通道测试-查看
	 */
	@Action("/channelTest/view")
	public String channelTestView() {
		String id = StrutsUtils.getParameterTrim("id");
		data = channelTestService.channelTestEdit(id);
		if(data == null){
			data = new HashMap<String, Object>();
		}
		data.put("onlyView", 1);
		
		return "channelTestView";
	}
	
	/**
	 * 通道测试-保存测试通道
	 */
	@Action("/channelTest/save")
	public void channelTestSave() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = channelTestService.channelTestSave(params);
		
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 测试管理-tab1（测试模板管理）
	 */
	@Action("/channelTest/testManageMent")
	public String templateMgnt() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = channelTestService.queryTemplatePaging(params);
		
		StrutsUtils.setAttribute("type_search", params.get("type"));
		return "testManageMent-tab1";
	}
	
	/**
	 * 测试管理-tab2（测试号码管理）
	 */
	@Action("/channelTest/testManageMent-tab2")
	public String testPhoneMgnt() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = channelTestService.queryTestPhonePaging(params);
		StrutsUtils.setAttribute("operators_type_search", params.get("operators_type"));
		return "testManageMent-tab2";
	}
	
	/**
	 * 测试管理-tab3（测试短信发送记录）
	 */
	@Action("/channelTest/testManageMent-tab3")
	public String testSendRecord() {
		page = channelTestService.queryTestSendRecord(StrutsUtils.getFormData());
		return "testManageMent-tab3";
	}
	
	/**
	 * 测试管理-tab4（测试上行短信记录）
	 */
	@Action("/channelTest/testManageMent-tab4")
	public String testUpstreamRecord() {
		page = channelTestService.queryTestUpstreamRecord(StrutsUtils.getFormData());
		return "testManageMent-tab4";
	}
	
	/**
	 * 根据ID查询智能测试模板
	 */
	@Action("/channelTest/template/queryTemplateById")
	public void queryTemplateById() {
		String templateId = StrutsUtils.getParameterTrim("templateId");
		data = channelTestService.queryTemplateById(templateId);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelTest/queryReportIdByChannelId")
	public void queryReportIdByChannelId(){
		String channelId = StrutsUtils.getParameterTrim("channelId");
		data = channelTestService.queryReportIdByChannelId(channelId);
		StrutsUtils.renderJson(data);
	}
	
	
	@Action("/channelTest/updateChannelStatus")
	public void updateChannelStatus(){
		String channelId = StrutsUtils.getParameterTrim("channelId");
		String state = StrutsUtils.getParameterTrim("state");
		data = channelTestService.updateChannelStatus(channelId, state);
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 新增智能测试模板
	 */
	@Action("/channelTest/template/insert")
	public void insertTemplate() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = channelTestService.insertTemplate(params);
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 更新智能测试模板
	 */
	@Action("/channelTest/template/update")
	public void updateTemplateById() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = channelTestService.updateTemplateById(params);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelTest/updateChannelTestStatus")
	public void updateUserShareStatus(){
		Map<String, String> params = StrutsUtils.getFormData();
		data = channelTestService.updateChannelTestStatus(params);
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 查询智能测试模板用于显示“测试模板”下拉框
	 */
	@Action("/channelTest/template/queryTestTeamplate4Select")
	public void queryTestTeamplate4Select() {
		dataList = channelTestService.queryTestTeamplate4Select();
		StrutsUtils.renderJson(dataList);
	}
	
	/**
	 * 删除智能测试模板
	 */
	@Action("/channelTest/template/delte")
	public void delTemplateById() {
		Map<String, String> params = StrutsUtils.getFormData();
		data = channelTestService.delTemplateById(params);
		StrutsUtils.renderJson(data);
	}
	
	
	/**
	 * 选择智能测试手机号码ifram
	 */
	@Action("/channelTest/selectPhonesIframe")
	public String selectPhonesIframe() {
		Map<String, String> params = StrutsUtils.getFormData();
		page = channelTestService.queryTestPhonePaging(params);
		StrutsUtils.setAttribute("operators_type_search", params.get("operators_type"));
		return "selectPhonesIframe";
	}
	
	/**
	 * 删除智能测试手机号码
	 */
	@Action("/channelTest/testPhone/delete")
	public void deleteTestPhone() {
		String id = StrutsUtils.getParameterTrim("id");
		data = channelTestService.deleteTestPhone(id);
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 批量导入智能测试手机号码
	 */
	@Action("/channelTest/testPhone/import")
	public void importTestPhone() {
		data = channelTestService.importTestPhone(upload, uploadContentType);
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 根据分享单号查询智能测试报告
	 */
	@Action("/channelTest/queryTestReportByReportId")
	public String queryTestReportByReportId() {
		String reportId = StrutsUtils.getParameterTrim("report_id");
		String channelId = StrutsUtils.getParameterTrim("channel_id");
		String pageName = StrutsUtils.getParameterTrim("pageName");
		
		data = channelTestService.queryTestReportByReportId(channelId, reportId);
		data.put("pageName", pageName);
		return "viewReportIframe";
	}
	
	
	/**
	 * 没有测试直接生成测试报告
	 * @return
	 */
	@Action("/channelTest/generateTestReportWithOutTest")
	public void generateTestReportWithOutTest() {
		String channelId = StrutsUtils.getParameterTrim("channel_id");
		if (StringUtils.isNotBlank(channelId)) {
			try {
				data = channelTestService.generateTestReportWithOutTest(channelId);
			} catch (SmspBusinessException e) {
				data = new HashMap<String, Object>();
				data.put("result", "fail");
				data.put("msg", e.getMessage());
			}
		} else {
			data = new HashMap<String, Object>();
		}
		
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 生成测试报告并返回测试报告结果
	 * @return
	 */
	@Action("/channelTest/generateTestReportAndReturn")
	public String generateTestReportAndReturn() {
		String channelId = StrutsUtils.getParameterTrim("channelId");
		if (StringUtils.isNotBlank(channelId)) {
			try {
				data = channelTestService.generateTestReportAndReturn(channelId);
			} catch (SmspBusinessException e) {
				data = new HashMap<String, Object>();
				data.put("result", "fail");
				data.put("msg", e.getMessage());
			}
		} else {
			data = new HashMap<String, Object>();
		}
		return "viewReportIframe";
	}
	
	/**
	 * 根据分享单号查询智能测试Echart数据
	 */
	@Action("/channelTest/queryTestReportEchartDataByReportId")
	public void queryTestReportEchartDataByShareId() {
		String reportId = StrutsUtils.getParameterTrim("reportId");
		if (StringUtils.isNotBlank(reportId)) {
			data = channelTestService.queryTestReportEchartDataByReportId(reportId);
		} else {
			data = new HashMap<String, Object>();
		}
		StrutsUtils.renderJson(data);
	}
	
	
	/**
	 * 启动通道智能测试
	 */
	@Action("/channelTest/channelTestRequest")
	public void startUserShareTest() {
		Map<String, String> params = StrutsUtils.getFormData();
		
		try {
			data = channelTestService.channelTestRequest(params);
		} catch (SmspBusinessException e) {
			data = new HashMap<String, Object>();
			data.put("result", "fail");
			data.put("msg", e.getMessage());
		}
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 查询最后一次提交测试且还没有生成测试报告的测试记录 
	 */
	@Action("/channelTest/queryTestLogNotGenerateReport")
	public void queryTestLogNotGenerateReport() {
		String channelId = StrutsUtils.getParameterTrim("channelId");
		data = channelTestService.queryTestLogNotGenerateReport(channelId);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelTest/testPhone/edit")
	public String editTestPhone(){
		String id = StrutsUtils.getParameterTrim("id");
		if(StringUtils.isNotBlank(id)){
			data = channelTestService.getOneTestPhone(id);
		}else{
			data = new HashMap<String, Object>();
		}
		return "testPhoneEdit";
	}
	
	@Action("/channelTest/testPhone/save")
	public void saveTestPhone(){
		Map<String, String> param = StrutsUtils.getFormData();
		data = channelTestService.addTestPhone(param);
		StrutsUtils.renderJson(data);
	}
	
}
