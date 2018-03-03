
package com.ucpaas.sms.action.smsAudit;

import com.jsmsframework.audit.dto.JsmsAuditDTO;
import com.jsmsframework.audit.entity.JsmsAuditConclusion;
import com.jsmsframework.audit.entity.JsmsAutoTemplate;
import com.jsmsframework.audit.enums.AuditPage;
import com.jsmsframework.audit.service.JsmsAuditConclusionService;
import com.jsmsframework.audit.service.JsmsAutoTemplateService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.R;
import com.jsmsframework.common.dto.ResultVO;
import com.jsmsframework.common.enums.AutoTemplateSubmitType;
import com.jsmsframework.common.enums.WebId;
import com.jsmsframework.user.audit.service.JsmsUserAutoTemplateService;
import com.jsmsframework.user.entity.JsmsAccount;
import com.jsmsframework.user.entity.JsmsUser;
import com.jsmsframework.user.service.JsmsAccountService;
import com.jsmsframework.user.service.JsmsUserService;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dto.JsmsAutoTemplateDTO;
import com.ucpaas.sms.entity.message.TestAccount;
import com.ucpaas.sms.enums.AuditPageName;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.service.smsAudit.SmsAuditService;
import com.ucpaas.sms.util.CommonUtil;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.rest.utils.DateUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信审核
 */
@Controller
@Scope("prototype")
@Results({
		@Result(name = "audit", location = "/WEB-INF/content/smsAudit/audit.jsp"),
		@Result(name = "yzmAudit", location = "/WEB-INF/content/smsAudit/yzmAudit.jsp"),
		@Result(name = "majorAudit", location = "/WEB-INF/content/smsAudit/majorAudit.jsp"),
		@Result(name = "query", location = "/WEB-INF/content/smsAudit/query.jsp"),
		@Result(name = "hisquery", location = "/WEB-INF/content/smsAudit/hisquery.jsp"),
		@Result(name = "hisbakquery", location = "/WEB-INF/content/smsAudit/hisbakquery.jsp"),
		@Result(name = "autoTemplateQuery", location = "/WEB-INF/content/smsAudit/autoTemplateQuery.jsp"),
		@Result(name = "autoTemplateWaitQuery", location = "/WEB-INF/content/smsAudit/autoTemplateWaitQuery.jsp"),
		@Result(name = "autoTemplateAdd", location = "/WEB-INF/content/smsAudit/autoTemplateAdd.jsp"),
		@Result(name = "templateAdd", location = "/WEB-INF/content/smsAudit/templateAdd.jsp"),
		@Result(name = "testSend", location = "/WEB-INF/content/smsAudit/testSend.jsp"),
		@Result(name = "autoTemplateEdit", location = "/WEB-INF/content/smsAudit/autoTemplateEdit.jsp") })
public class SmsAuditAction extends BaseAction {
	private static final long serialVersionUID = 247987432825275374L;

	@Autowired
	private SmsAuditService smsAuditService;

	@Autowired
	private JsmsAccountService jsmsAccountService;

	@Autowired
	private JsmsAutoTemplateService jsmsAutoTemplateService;

	@Autowired
	private JsmsUserService jsmsUserService;
	@Autowired
	private JsmsAuditConclusionService jsmsAuditConclusionService;
	@Autowired
	private JsmsUserAutoTemplateService jsmsUserAutoTemplateService;

	@Autowired
	private LogService logService;

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

	/**
	 * 审核查询
	 *
	 * @return 返回页面
	 */
	@Action("/smsaudit/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();

		// 更新时间：开始时间
		DateTime dt = DateTime.now();
		String startTime = Objects.toString(params.get("start_time"), "");
		String endTime = Objects.toString(params.get("end_time"), "");
		if (StringUtils.isNotBlank(startTime)) {
			params.put("start_time", startTime);
		}else{
			startTime = dt.toString("yyyy-MM-dd")+" 00:00:00";
			params.put("start_time", startTime);
		}

		// 更新时间：结束时间
		if (StringUtils.isNotBlank(endTime)) {
			params.put("end_time", endTime);
		}else {
			endTime = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("end_time", endTime);
		}

		StrutsUtils.setAttribute("s_start_time",startTime); // 添加回显数据
		StrutsUtils.setAttribute("s_end_time", endTime);
		long start = System.currentTimeMillis();
		page = smsAuditService.auditQueryPage(params);
		long end = System.currentTimeMillis();
		System.out.println("【审核查询】smsAuditService.auditQueryPage耗时" + (end - start) + "ms");
		return "query";
	}

	/**
	 * 历史审核查询 审核库
	 *
	 * @return 返回页面
	 */
	@Action("/smsaudit/hisbakquery")
	public String hisquery() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		int startDate,endDate;
		if(params.get("start_time")==null ||params.get("end_time")==null){
			DateTime dt = DateTime.now();
			String start_time = dt.toString("yyyy-MM-dd")+" 00:00:00";
			String end_time = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("hstart_time",start_time);
			params.put("hend_time", end_time);

		}else {
			params.put("hstart_time",params.get("start_time"));
			params.put("hend_time", params.get("end_time"));
		}


		StrutsUtils.setAttribute("s_start_time", params.get("hstart_time")); // 添加回显数据
		StrutsUtils.setAttribute("s_end_time", params.get("hend_time"));
		StrutsUtils.setAttribute("aduitor", params.get("aduitor"));
		StrutsUtils.setAttribute("clientid1", params.get("clientid"));
		StrutsUtils.setAttribute("content1", params.get("content"));
		StrutsUtils.setAttribute("sign1", params.get("sign"));
		StrutsUtils.setAttribute("smstype1", params.get("smstype"));
		StrutsUtils.setAttribute("status1", params.get("status"));


		params.put("ishis","1");

		JsmsPage<JsmsAuditDTO> params1=CommonUtil.initJsmsPage(params);
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.putAll(params);
		params1.setParams(objectMap);

		JsmsPage queryPage= smsAuditService.hisauditQueryPage(params1);
		page = CommonUtil.converterJsmsPage2PageContainer(queryPage);
		return "hisbakquery";
	}


	/**
	 * 历史审核查询 审核过期备份库
	 *
	 * @return 返回页面
	 */
	@Action("/smsaudit/hisquery")
	public String hisbakquery() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		int startDate,endDate;

		if(params.get("start_time")==null ||params.get("end_time")==null){
			DateTime dt = DateTime.now();
			String start_time = dt.toString("yyyy-MM-dd")+" 00:00:00";
			String end_time = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("hstart_time",start_time);
			params.put("hend_time", end_time);

		}else {
			params.put("hstart_time",params.get("start_time"));
			params.put("hend_time", params.get("end_time"));
		}


		StrutsUtils.setAttribute("s_start_time", params.get("hstart_time")); // 添加回显数据
		StrutsUtils.setAttribute("s_end_time", params.get("hend_time"));
		StrutsUtils.setAttribute("aduitor", params.get("aduitor"));
		StrutsUtils.setAttribute("clientid1", params.get("clientid"));
		StrutsUtils.setAttribute("content1", params.get("content"));
		StrutsUtils.setAttribute("sign1", params.get("sign"));
		StrutsUtils.setAttribute("smstype1", params.get("smstype"));
		StrutsUtils.setAttribute("status1", params.get("status"));



		params.put("ishis","1");
		JsmsPage<JsmsAuditDTO> params1=CommonUtil.initJsmsPage(params);
		Map<String, Object> objectMap = new HashMap<>();
		objectMap.putAll(params);
		params1.setParams(objectMap);

		JsmsPage queryPage= smsAuditService.hisbakauditQueryPage(params1);
		page = CommonUtil.converterJsmsPage2PageContainer(queryPage);
		return "hisquery";
	}





	/**
	 * 验证码短信审核
	 * @return 返回页面
	 */
	@Action("/smsaudit/yzmaudit")
	public String yzmAudit() {
//		String smsType = StrutsUtils.getParameterTrim("smsType");
		Map<String, String> params = StrutsUtils.getFormData();
		dueAuditPageParam(params);
		long start = System.currentTimeMillis();
		dataList = smsAuditService.auditPage(params, AuditPageName.YZM_AUDIT_PAGE, AuditPage.YZM_AUDIT_PAGE);
		long end = System.currentTimeMillis();
		System.out.println("【审核查询】smsAuditService.auditPage" + (end - start) + "ms");
		StrutsUtils.setAttribute("params",params); // 添加回显数据
		return "yzmAudit";
	}
	/**
	 * 重要客户短信审核
	 *
	 * @return 返回页面
	 */
	@Action("/smsaudit/majoraudit")
	public String majorAudit() {
//		String smsType = StrutsUtils.getParameterTrim("smsType");
		Map<String, String> params = StrutsUtils.getFormData();
		dueAuditPageParam(params);
		long start = System.currentTimeMillis();
		dataList = smsAuditService.auditPage(params,AuditPageName.MAJOR_AUDIT_PAGE, AuditPage.MAJOR_AUDIT_PAGE);
		long end = System.currentTimeMillis();
		System.out.println("【审核查询】smsAuditService.auditPage" + (end - start) + "ms");
		StrutsUtils.setAttribute("params",params); // 添加回显数据
		return "majorAudit";
	}
	/**
	 * 普通短信审核
	 *
	 * @return 返回页面
	 */
	@Action("/smsaudit/audit")
	public String audit() {

		Map<String, String> params = StrutsUtils.getFormData();
		dueAuditPageParam(params);
		long start = System.currentTimeMillis();
		dataList = smsAuditService.auditPage(params,AuditPageName.ORDINARY_NUM, AuditPage.ORDINARY_AUDIT_PAGE);
		long end = System.currentTimeMillis();
		System.out.println("【审核查询】smsAuditService.auditPage" + (end - start) + "ms");
		StrutsUtils.setAttribute("params",params); // 添加回显数据
		return "audit";
	}

	private void dueAuditPageParam(Map<String, String> params){

		// 更新时间：开始时间
		DateTime dt = DateTime.now();
		String startTime = Objects.toString(params.get("start_time"), "");
		String endTime = Objects.toString(params.get("end_time"), "");
		if (StringUtils.isNotBlank(startTime)) {
			params.put("start_time", startTime);
		}else{
			startTime = dt.toString("yyyy-MM-dd")+" 00:00:00";
			params.put("start_time", startTime);
		}

		// 更新时间：结束时间
		if (StringUtils.isNotBlank(endTime)) {
			params.put("end_time", endTime);
		}else {
			endTime = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("end_time", endTime);
		}
	}


	/**
	 * 获取短信测试账号
	 *
	 */
	@Action("/smsaudit/getTestClientid")
	public void getTestClientid() {
		Map params = new HashMap();
        List<TestAccount> testClientid = smsAuditService.getTestClientid(params);
        StrutsUtils.renderJson(testClientid);
	}

	/**
	 * 短信发送测试
	 * @return 返回页面
	 */
	@Action("/smsaudit/testSend")
	public String testSend() {
//		String smsType = StrutsUtils.getParameterTrim("smsType");
		return "testSend";
	}

	/**
	 * 短信发送测试
	 * @return 返回页面
	 */
	@Action("/smsaudit/testSendAction")
	public void testSendAction() {
		Map<String, String> params = StrutsUtils.getFormData();
		ResultVO resultVO = null;
		try {
			resultVO = smsAuditService.testSendAction(params);
		} catch (Exception e) {
			resultVO = ResultVO.failure("系统错误...");
			e.printStackTrace();
		}
		StrutsUtils.renderJson(resultVO);
	}

	/**
	 * 更新审核记录状态
	 */
	@Action("/smsaudit/updateStatus")
	public void updateStatus() {
		Map<String, String> params = StrutsUtils.getFormData();

		Long userId = AuthorityUtils.getLoginUserId();
		params.put("userId", String.valueOf(userId));
		data = smsAuditService.updateStatus(params);
		StrutsUtils.renderJson(data);
	}
	/**
	 * 更新审核记录状态
	 */
	@Action("/smsaudit/updateYZMStatus")
	public void updateYZMStatus() {
		Map<String, String> params = StrutsUtils.getFormData();

		Long userId = AuthorityUtils.getLoginUserId();
		params.put("userId", String.valueOf(userId));
		data = smsAuditService.updateStatus(params,AuditPageName.YZM_AUDIT_PAGE);
		StrutsUtils.renderJson(data);
	}
	/**
	 * 更新审核记录状态
	 */
	@Action("/smsaudit/updateMajorStatus")
	public void updateMajorStatus() {
		Map<String, String> params = StrutsUtils.getFormData();

		Long userId = AuthorityUtils.getLoginUserId();
		params.put("userId", String.valueOf(userId));
		data = smsAuditService.updateStatus(params,AuditPageName.MAJOR_AUDIT_PAGE);
		StrutsUtils.renderJson(data);
	}
	/**
	 * 更新审核记录状态
	 */
	@Action("/smsaudit/updateOrdinaryStatus")
	public void updateOrdinaryStatus() {
		Map<String, String> params = StrutsUtils.getFormData();

		Long userId = AuthorityUtils.getLoginUserId();
		params.put("userId", String.valueOf(userId));
		data = smsAuditService.updateStatus(params,AuditPageName.ORDINARY_NUM);
		StrutsUtils.renderJson(data);
	}

	/**
	 * 设置审核过期
	 */
	@Action("/smsaudit/setAuditExpired")
	public void setAuditExpired() {
		String auditId = StrutsUtils.getParameterTrim("auditId");
		data = smsAuditService.setAuditExpired(auditId);

		StrutsUtils.renderJson(data);
	}

	/**
	 * 查询审核超时时间
	 */
	@Action("/smsaudit/getAuditExpireTime")
	public void getAuditExpireTime() {
		data = smsAuditService.getAuditExpireTime();
		StrutsUtils.renderJson(data);
	}

	/**
	 * 查询待审核数(当前用户排除其他用户锁定的记录数)
	 */
	@Action("/smsaudit/getAuditNum")
	public void getAuditNum() {
		data = smsAuditService.getNeedAuditNum();
		StrutsUtils.renderJson(data);
	}

	/**
	 * 查询待审核数(当前用户排除其他用户锁定的记录数)
	 */
	@Action("/smsaudit/getKindsAuditNum")
	public void getKindsAuditNum() {
		data = smsAuditService.getKindsAuditNum();
		StrutsUtils.renderJson(data);
	}

//	/**
//	 * 查询待审核数(当前用户排除其他用户锁定的记录数)
//	 */
//	@Action("/smsaudit/getAuditNum")
//	public void getUnAuditNum() {
//		data = smsAuditService.getNeedAuditNum();
//		StrutsUtils.renderJson(data);
//	}

	/**
	 * 查询短信模板审核数
	 */
	@Action("/smsaudit/autoTemplate/getTemplateAuditNum")
	public void getTemplateAuditNum() {
		data = jsmsAutoTemplateService.getAuditNum();
		StrutsUtils.renderJson(data);
	}


	/**
	 * 导出Excel文件
	 *
	 * @throws Exception
	 */
	@Action("/smsaudit/hisExportExcel")
	public void exportExcel() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		String filePath = ConfigUtils.save_path + "/短信审核库历史查询.xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("短信审核库历史查询");

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
		if (params.get("") != null) {
			buffer.append("  结束时间：");
			buffer.append(params.get("end_time"));

		}
		excel.addRemark(buffer.toString());


		excel.addHeader(20, "用户账号", "clientid");
		excel.addHeader(20, "短信类型", "smstypeName");
		excel.addHeader(20, "短信内容", "content");
		excel.addHeader(20, "签名", "sign");
		excel.addHeader(20, "创建时间", "createtime");
		excel.addHeader(20, "发送数量", "sendnum");
		excel.addHeader(20, "审核状态", "statusName");
		excel.addHeader(20, "审核人", "auditpersonName");
		excel.addHeader(20, "审核时间", "audittime");
		excel.setDataList(smsAuditService.queryhisAll(params));

		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}


	/**
	 * 导出Excel文件
	 *
	 * @throws Exception
	 */
	@Action("/smsaudit/hisbakExportExcel")
	public void bakexportExcel() throws Exception {
		Map<String, String> params = StrutsUtils.getFormData();
		String filePath = ConfigUtils.save_path + "/短信审核备份库历史查询.xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("短信审核备份库历史查询");

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
		if (params.get("") != null) {
			buffer.append("  结束时间：");
			buffer.append(params.get("end_time"));

		}
		excel.addRemark(buffer.toString());


		excel.addHeader(20, "用户账号", "clientid");
		excel.addHeader(20, "短信类型", "smstypeName");
		excel.addHeader(20, "短信内容", "content");
		excel.addHeader(20, "签名", "sign");
		excel.addHeader(20, "创建时间", "createtime");
		excel.addHeader(20, "发送数量", "sendnum");
		excel.addHeader(20, "审核状态", "statusName");
		excel.addHeader(20, "审核人", "auditpersonName");
		excel.addHeader(20, "审核时间", "audittime");
		excel.setDataList(smsAuditService.queryhisAll(params));

		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}


	/**
	 * 智能模板管理--已审核
	 *
	 * @return 返回页面
	 */
	@Action("/smsaudit/autoTemplate/query")
	public String autoTemplateQuery() {
		Map<String, String> params = StrutsUtils.getFormData();
		Map<String, Object> objectMap = new HashMap<>();

		Object obj = new Object();
		// 审核状态
		obj = params.get("state");
		if(obj==null){
			objectMap.put("state",99);
		}
		if (obj != null) {
			objectMap.put("state", obj);
		}
		// 模板属性
		obj = params.get("smsType");
		if (obj != null) {
			objectMap.put("smsType", obj);
		}
		// 模板类型
		obj = params.get("templateType");
		if (obj != null) {
			objectMap.put("templateType", obj);
		}
		// 模版ID
		obj = params.get("templateId");
		if (obj != null) {
			objectMap.put("templateId", obj);
		}
		// 短信签名
		obj = params.get("sign");
		if (obj != null) {
			objectMap.put("sign", obj);
		}
		// 模版内容
		obj = params.get("content");
		if (obj != null) {
			objectMap.put("content", obj);
		}
		// 用户帐号
		obj = params.get("clientId");
		if (obj != null) {
			objectMap.put("clientId", obj);
		}
		// 提交来源
		obj = params.get("submitType");
		if (obj != null) {
			objectMap.put("submitType", obj);
		}
		// 创建人
		obj = params.get("userName");
		if (obj != null) {
			objectMap.put("userName", obj);
		}
		// 审核人
		obj = params.get("adminName");
		if (obj != null) {
			objectMap.put("adminName", obj);
		}

		// 更新时间：开始时间
		DateTime dt = DateTime.now();
		String startTime = Objects.toString(params.get("startTime"), "");
		String endTime = Objects.toString(params.get("endTime"), "");
		if (StringUtils.isNotBlank(startTime)) {
			objectMap.put("startTime", startTime);
		}else{

			startTime = DateUtil.dateToStr(DateUtil.getDateFromToday(-30),DateUtil.YMR_SLASH)+" 00:00:00";
			objectMap.put("startTime", startTime);
		}

		// 更新时间：结束时间
		if (StringUtils.isNotBlank(endTime)) {
			objectMap.put("endTime", endTime);
		}else {
			endTime = dt.toString("yyyy-MM-dd HH:mm:ss");
			objectMap.put("endTime", endTime);
		}

		// 用于页面回显"更新时间"
		StrutsUtils.setAttribute("startTime", startTime);
		StrutsUtils.setAttribute("endTime", endTime);
		StrutsUtils.setAttribute("clientIdHidden", Objects.toString(params.get("clientId"), ""));

		JsmsPage<JsmsAutoTemplate> jsmsPage = CommonUtil.initJsmsPage(params);
		jsmsPage.setParams(objectMap);
		jsmsPage.setOrderByClause("a.create_time DESC");

		JsmsPage queryPage = jsmsUserAutoTemplateService.findListOfAutoTemplate(jsmsPage,WebId.短信调度系统.getValue());
		/*List<JsmsAutoTemplateDTO> list = new ArrayList<>();
		for (Object temp : queryPage.getData()) {
			JsmsAutoTemplateDTO jsmsAutoTemplateDTO = new JsmsAutoTemplateDTO();
			BeanUtils.copyProperties(temp , jsmsAutoTemplateDTO);
			JsmsUser jsmsUser = jsmsUserService.getById(String.valueOf(jsmsAutoTemplateDTO.getAdminId()));
			if(jsmsUser != null){
				jsmsAutoTemplateDTO.setAdminName(jsmsUser.getRealname());
			}
			JsmsUser jsmsUser1 = jsmsUserService.getById(jsmsAutoTemplateDTO.getUserId());
			if(jsmsUser1 != null){
				jsmsAutoTemplateDTO.setUserName(jsmsUser1.getRealname());
			}

			list.add(jsmsAutoTemplateDTO);
		}
		queryPage.setData(list);*/
		page = CommonUtil.converterJsmsPage2PageContainer(queryPage);
		return "autoTemplateQuery";
	}

	/**
	 * 智能模板管理--已审核 --> 导出
	 *
	 * @return 返回页面
	 */
	@Action("/smsaudit/autoTemplate/export")
	public void autoTemplateExport() {
		Map<String, String> params = StrutsUtils.getFormData();

		Object obj = new Object();
//		objectMap.putAll(params);
		// 审核状态
		obj = params.get("state");
		if(obj==null){
			params.put("state","99");
		}

		// 更新时间：开始时间
		if (StringUtils.isBlank(params.get("startTime"))) {
			String startTime = DateUtil.dateToStr(DateUtil.getDateFromToday(-30),DateUtil.YMR_SLASH)+" 00:00:00";
			params.put("startTime", startTime);
		}
		// 更新时间：结束时间
		if (StringUtils.isBlank(params.get("endTime"))) {
			DateTime dt = DateTime.now();
			String endTime = dt.toString("yyyy-MM-dd HH:mm:ss");
			params.put("endTime", endTime);
		}

		StringBuffer filePath = new StringBuffer(ConfigUtils.save_path);
		if(!ConfigUtils.save_path.endsWith("/")){
			filePath.append("/");
		}
		filePath.append("智能模板已审核记录")
				.append(".xls")
				.append("$$$")
				.append(UUID.randomUUID());
		Excel excel = new Excel();
		excel.setFilePath(filePath.toString());
		excel.setTitle("智能模板已审核记录");

		StringBuffer buffer = new StringBuffer();
		buffer.append("查询条件：");
		String condition = null;
		condition = params.get("_state");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  审核状态：");
			buffer.append(condition);
			buffer.append(";");
		}

		condition = params.get("_smsType");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  模板属性：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("_");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  模板属性：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("_templateType");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  模板类型：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("templateId");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  模板ID：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("sign");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  签名：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("content");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  模板内容：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("clientId");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  用户账号：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("_submitType");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  提交来源：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("userName");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  创建者：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("adminName");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  审核人：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("startTime");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  更新时间(开始)：");
			buffer.append(condition);
			buffer.append(";");
		}
		condition = params.get("endTime");
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(condition)) {
			buffer.append("  更新时间(结束)：");
			buffer.append(condition);
			buffer.append(";");
		}

		excel.addRemark(buffer.toString());

		excel.addHeader(20, "模板ID", "templateId");
		excel.addHeader(20, "用户账号", "clientId");
		excel.addHeader(20, "模板属性", "smsTypeStr");
		excel.addHeader(20, "模板类型", "templateTypeStr");
		excel.addHeader(20, "模板内容", "content");
		excel.addHeader(20, "短信签名", "sign");
		excel.addHeader(20, "提交来源", "submitTypeStr");
		excel.addHeader(20, "创建者", "userName");
		excel.addHeader(20, "创建时间", "createTimeStr");
		excel.addHeader(20, "审核状态", "stateStr");
		excel.addHeader(20, "原因", "remark");
		excel.addHeader(20, "审核人", "adminName");
		excel.addHeader(20, "更新时间", "updateTimeStr");

		JsmsPage<JsmsAutoTemplate> jsmsPage = CommonUtil.initJsmsPage(params);
		jsmsPage.setOrderByClause("a.create_time DESC");
		jsmsPage.setRows(Integer.valueOf(ConfigUtils.max_export_excel_num) + 1);

		ResultVO resultVO = null;
		try {
			jsmsAutoTemplateService.findList(jsmsPage);
			List<JsmsAutoTemplateDTO> list = new ArrayList<>();
			List result = new ArrayList();
			for (Object temp : jsmsPage.getData()) {
				JsmsAutoTemplateDTO jsmsAutoTemplateDTO = new JsmsAutoTemplateDTO();
				BeanUtils.copyProperties(temp , jsmsAutoTemplateDTO);
				JsmsUser jsmsUser = jsmsUserService.getById(String.valueOf(jsmsAutoTemplateDTO.getAdminId()));
				if(jsmsUser != null){
					jsmsAutoTemplateDTO.setAdminName(jsmsUser.getRealname());
				}
				JsmsUser jsmsUser1 = jsmsUserService.getById(jsmsAutoTemplateDTO.getUserId());
				if(jsmsUser1 != null){
					jsmsAutoTemplateDTO.setUserName(jsmsUser1.getRealname());
				}

				Map<String, String> describe = org.apache.commons.beanutils.BeanUtils.describe(jsmsAutoTemplateDTO);
				result.add(describe);
			}

			if (result == null || result.size() <= 0){
				resultVO = ResultVO.failure("没有数据！先不导出了  ^_^");
			}else if (result.size() > Integer.valueOf(ConfigUtils.max_export_excel_num)){
				resultVO =  ResultVO.failure("数据量超过"+ConfigUtils.max_export_excel_num+"，请缩小范围后再导出吧  ^_^");
			}

            /* 合计需要添加合计方法
            page = (JsmsPage) this.getClass().getMethod(queryMethod + "Total",page.getClass()).invoke(this, page);
            result.add(BeanUtils.describe(page.getTotalOtherData().get("totalLine")));*/
			excel.setDataList(result);
			if (ExcelUtils.exportExcel(excel)) {
				resultVO = ResultVO.successDefault();
				resultVO.setMsg("报表生成成功");
				resultVO.setData(excel.getFilePath());
			}
		} catch (Exception e) {
//			logg.error("生成报表失败", e);
			e.printStackTrace();
		}
		StrutsUtils.renderJson(resultVO);
	}



	/**
	 * 智能模板管理--待审核
	 *
	 * @return 返回页面
	 */
	@Action("/smsaudit/autoTemplate/waitquery")
	public String autoTemplateWaitQuery() {
		Map<String, String> params = StrutsUtils.getFormData();
		Map<String, Object> objectMap = new HashMap<>();

		// 模版ID
		Object obj = new Object();

		objectMap.put("state","0"); //待审核
		obj = params.get("templateId");
		if (obj != null) {
			objectMap.put("templateId", obj);
		}

		// 用户帐号
		obj = params.get("clientId");
		if (obj != null) {
			objectMap.put("clientId", obj);
		}
		// 模板属性
		obj = params.get("smsType");
		if (obj != null) {
			objectMap.put("smsType", obj);
		}
		// 模板类型
		obj = params.get("templateType");
		if (obj != null) {
			objectMap.put("templateType", obj);
		}
		// 模版内容
		obj = params.get("content");
		if (obj != null) {
			objectMap.put("content", obj);
		}
		// 提交来源
		obj = params.get("submitType");
		if (obj != null) {
			objectMap.put("submitType", obj);
		}

		// 短信签名
		obj = params.get("sign");
		if (obj != null) {
			objectMap.put("sign", obj);
		}
		// 创建人
		obj = params.get("userName");
		if (obj != null) {
			objectMap.put("userName", obj);
		}
		DateTime dt = DateTime.now();
		// 创建时间：开始时间
		String startTime = Objects.toString(params.get("startTime"), "");
		String endTime = Objects.toString(params.get("endTime"), "");
		if (StringUtils.isNotBlank(startTime)) {
			objectMap.put("createStartTime", startTime);
		}else{

			startTime = DateUtil.dateToStr(DateUtil.getDateFromToday(-30),DateUtil.YMR_SLASH)+" 00:00:00";
			objectMap.put("createStartTime", startTime);
		}

		// 创建时间：结束时间
		if (StringUtils.isNotBlank(endTime)) {
			objectMap.put("createEndTime", endTime);
		}else {
			endTime = dt.toString("yyyy-MM-dd HH:mm:ss");
			objectMap.put("createEndTime", endTime);
		}

		// 用于页面回显"创建时间"
		StrutsUtils.setAttribute("startTime", startTime);
		StrutsUtils.setAttribute("endTime", endTime);
		StrutsUtils.setAttribute("clientIdHidden", Objects.toString(params.get("clientId"), ""));

		JsmsPage<JsmsAutoTemplate> jsmsPage = CommonUtil.initJsmsPage(params);
		jsmsPage.setParams(objectMap);

		obj = params.get("querydesc");
		if(obj==null){
			obj=2;
		}
		if("1".equals(obj.toString())){
			jsmsPage.setOrderByClause("a.create_time DESC");
		}else if("2".equals(obj.toString())){
			jsmsPage.setOrderByClause("a.match_amount DESC,a.create_time DESC");
		}

		//	StrutsUtils.setAttribute("querydesc", obj);

		JsmsPage queryPage = jsmsUserAutoTemplateService.findListOfAutoTemplate(jsmsPage,WebId.短信调度系统.getValue());
		/*List<JsmsAutoTemplateDTO> list = new ArrayList<>();
		for (Object temp : queryPage.getData()) {
			JsmsAutoTemplateDTO jsmsAutoTemplateDTO = new JsmsAutoTemplateDTO();
			BeanUtils.copyProperties(temp , jsmsAutoTemplateDTO);
			JsmsUser jsmsUser = jsmsUserService.getById(String.valueOf(jsmsAutoTemplateDTO.getAdminId()));
			if(jsmsUser != null){
				jsmsAutoTemplateDTO.setAdminName(jsmsUser.getRealname());
			}
			JsmsUser jsmsUser1 = jsmsUserService.getById(jsmsAutoTemplateDTO.getUserId());
			if(jsmsUser1 != null){
				jsmsAutoTemplateDTO.setUserName(jsmsUser1.getRealname());
			}

			list.add(jsmsAutoTemplateDTO);
		}
		queryPage.setData(list);*/
		page = CommonUtil.converterJsmsPage2PageContainer(queryPage);
		return "autoTemplateWaitQuery";
	}

	/**
	 * 查询所有的客户
	 */
	@Action("/smsaudit/autoTemplate/accounts")
	public void getAccounts() {
		String clientId = StrutsUtils.getParameterTrim("clientId");
		List<JsmsAccount> data = jsmsAccountService.findAllList(clientId);
		StrutsUtils.renderJson(data);
	}

	/**
	 * 查询所有的客户
	 */
	@Action("/smsaudit/audit/conclusion")
	public void getConclusion() {
		String remark = StrutsUtils.getParameterTrim("opt_remark");
		List<JsmsAuditConclusion> data = jsmsAuditConclusionService.findAllList(remark);
		StrutsUtils.renderJson(data);
	}

	/**
	 * 查询客户模版列表
	 */
	@Action("/smsaudit/autoTemplate/list")
	public void autoTemplateList() {
		Map<String, String> params = StrutsUtils.getFormData();

		JsmsPage<JsmsAutoTemplate> page = new JsmsPage<>();
		Object obj = params.get("rows");
		if (obj != null) {
			page.setRows(Integer.parseInt(obj.toString()));
		}

		obj = params.get("page");
		if (obj != null) {
			page.setPage(Integer.parseInt(obj.toString()));
		}

		Map<String, Object> objectMap = new HashMap<>();

		// 模版ID
		obj = params.get("templateId");
		if (obj != null) {
			objectMap.put("templateId", obj);
		}

		// 用户帐号
		obj = params.get("clientId");
		if (obj != null) {
			objectMap.put("clientId", obj);
		}

		// 短信类型
		obj = params.get("smsType");
		if (obj != null) {
			objectMap.put("smsType", obj);
		}

		// 模版内容
		obj = params.get("content");
		if (obj != null) {
			objectMap.put("content", obj);
		}

		// 短信签名
		obj = params.get("sign");
		if (obj != null) {
			objectMap.put("sign", obj);
		}

		// 审核人
		obj = params.get("adminName");
		if (obj != null) {
			objectMap.put("adminName", obj);
		}

		// 审核人
		obj = params.get("startTime");
		if (obj != null) {
			objectMap.put("startTime", obj);
		}

		// 审核人
		obj = params.get("endTime");
		if (obj != null) {
			objectMap.put("endTime", obj);
		}

		page.setParams(objectMap);
		page.setOrderByClause("a.create_time DESC");
		StrutsUtils.renderJson(jsmsAutoTemplateService.findList(page));
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	/**
	 * 查询所有的客户
	 */
	@Action("/smsaudit/autoTemplate/add")
	public String autoTemplateAdd() {
		Map<String, String> params = StrutsUtils.getFormData();
		if(params.get("templateId")!=null){
			Integer templateId=Integer.valueOf(params.get("templateId"));
			JsmsAutoTemplate auto=jsmsAutoTemplateService.getByTemplateId(templateId);
			Map<String,Object> data1=new HashedMap();

			data1.put("content",replaceBlank(auto.getContent()));
			data1.put("sign",auto.getSign());
			data1.put("smsContent",replaceBlank(auto.getSmsContent()));
			data=data1;
		}

		return "autoTemplateAdd";
	}
	/**
	 * 查询所有的客户
	 */
	@Action("/smsaudit/template/add")
	public String templateAdd() {
		Map<String, String> params = StrutsUtils.getFormData();
		if(params.get("templateId")!=null){
			Integer templateId=Integer.valueOf(params.get("templateId"));
			JsmsAutoTemplate auto=jsmsAutoTemplateService.getByTemplateId(templateId);
			Map<String,Object> data1=new HashedMap();

			data1.put("content",replaceBlank(auto.getContent()));
			data1.put("sign",auto.getSign());
			data1.put("smsContent",replaceBlank(auto.getSmsContent()));
			data=data1;
		}

		return "templateAdd";
	}

	/**
	 * 弃用
	 */
	@Action("/smsaudit/autoTemplate/edit")
	public String autoTemplateEdit() {
		return "autoTemplateEdit";
	}


	/**
	 * 新增or编辑智能模板审核
	 */
	@Action("/smsaudit/autoTemplate/save")
	public void autoTemplateSave() {
		Map<String, String> params = StrutsUtils.getFormData();

		boolean isMod = false;
		JsmsAutoTemplate template = new JsmsAutoTemplate();

		// 模版ID
		Object obj = params.get("templateId");
		if (obj != null) {
			isMod = true;
			template.setTemplateId(Integer.parseInt(obj.toString()));
		}

		// 用户帐号
		obj = params.get("clientId");
		if (obj != null) {
			template.setClientId(obj.toString());
		}

		// 模版类型
		obj = params.get("templateType");
		if (obj != null) {
			template.setTemplateType(Integer.parseInt(obj.toString()));
		}

		// 短信类型
		obj = params.get("smsType");
		if (obj != null) {
			template.setSmsType(Integer.parseInt(obj.toString()));
		}/*else{
			StrutsUtils.renderJson(R.error("请选择短信类型"));
			return;
		}*/

		// 模版内容
		obj = params.get("content");
		if (obj != null) {
			template.setContent(obj.toString());
		}
		//审核状态
		obj = params.get("state");

		if (obj != null) {
			template.setState(Integer.valueOf(obj.toString()));
		}
		// 短信签名
		obj = params.get("sign");
		if (obj != null) {
			template.setSign(obj.toString());
		}


		//不通过原因
		obj =params.get("remark");
		if(obj !=null){
			template.setRemark(obj.toString());
		}
		//不通过原因
		obj =params.get("remark");
		if(obj !=null){
			template.setRemark(obj.toString());
		}
		//提交类型
		obj =params.get("submitType");
		if(obj !=null){
			template.setSubmitType(Integer.valueOf(obj.toString()));
		}
		// 审核人
		template.setAdminId(AuthorityUtils.getLoginUserId());



		R r;

		if (StringUtils.isBlank(template.getClientId()) || jsmsAccountService.getByClientId(template.getClientId()) == null){
			R.error("用户账号不存在");
		}
		if (isMod) {
			logService.add(LogType.update,LogEnum.短信审核.getValue(), "短信审核-智能模版管理：审核模版", params, template);
			if(template.getState().equals(1)){
				template.setRemark(" ");
			}
			r = jsmsAutoTemplateService.modAutoTemplate(template);
		} else {
			logService.add(LogType.add, LogEnum.短信审核.getValue(), "短信审核-智能模版管理：添加模版", params, template);
			if(template.getState()==null) {
				template.setState(1);
			}
			template.setWebId(1);
			template.setUserId(String.valueOf(AuthorityUtils.getLoginUserId()));
			template.setSubmitType(AutoTemplateSubmitType.平台提交.getValue());
			r = jsmsAutoTemplateService.addAutoTemplate(template);
		}

		StrutsUtils.renderJson(r);
	}

	/**
	 * 查询客户模版列表
	 */
	@Action("/smsaudit/autoTemplate/del")
	public void autoTemplateDel() {

		String templateIdStr = StrutsUtils.getParameterTrim("templateId");
		Integer templateId = null;
		// 模版ID
		if (StringUtils.isNotBlank(templateIdStr)) {
			templateId  = Integer.parseInt(templateIdStr);
		}

		if (templateId == null) {
			StrutsUtils.renderJson(R.error("模版ID不能为空"));
			return;
		}

		logService.add(LogType.update, LogEnum.短信审核.getValue(), "短信审核-智能模版管理：删除模版", templateId, templateId);
		R r = jsmsAutoTemplateService.delAutoTemplate(templateId);
		StrutsUtils.renderJson(r);
	}

	/**
	 * 下载批量添加智能模板Excel
	 */
	@Action("/smsaudit/downloadExcelTemplate")
	public void downloadExcelTemplate() {
		String path = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/批量添加智能模板.xls");
		FileUtils.download(path);
	}


	/**
	 * 批量添加智能模板
	 */
	@Action("/smsaudit/addAutoTemplateBatch")
	public void importExcel() {
		R r = jsmsUserAutoTemplateService.addAutoTemplateBatch(upload, uploadContentType, String.valueOf(AuthorityUtils.getLoginUserId()), ConfigUtils.save_path, WebId.短信调度系统.getValue());
		StrutsUtils.renderJson(r);
	}

	@Action("/smsaudit/exportImportResult")
	public void exportError(){
		String filePath = ConfigUtils.save_path +"/import"+ "/批量添加智能模板结果-userid-" + AuthorityUtils.getLoginUserId()+".xls";
		File file = new File(filePath);
		if(file.exists()){
			FileUtils.download(filePath);
		}else{
			StrutsUtils.renderText("文件过期、不存在或者已经被管理员删除");
		}
	}

	@Action("/smsaudit/getAccountSmsTypeByClientId")
	public void getAccountSmsTypeByClientId(){
		String clientId = StrutsUtils.getParameterTrim("clientId");
		JsmsAccount jsmsAccount = jsmsAccountService.getByClientId(clientId);
		String smsType = null;
		if(jsmsAccount != null)
			smsType = String.valueOf(jsmsAccount.getSmstype());

		StrutsUtils.renderJson(smsType);
	}

}
