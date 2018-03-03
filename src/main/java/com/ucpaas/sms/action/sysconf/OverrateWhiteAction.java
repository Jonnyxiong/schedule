package com.ucpaas.sms.action.sysconf;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.sysconf.OverrateWhiteService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 
 * @author TonyHe
 *	超频白名单管理
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/overrateWhite/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/overrateWhite/edit.jsp")})
public class OverrateWhiteAction extends BaseAction{
	private static final long serialVersionUID = 8552053074211460430L;
	private OverrateWhiteService overrateWhiteService;
	
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

	@Resource
	public void setOverrateWhiteService(OverrateWhiteService overrateWhiteService) {
		this.overrateWhiteService = overrateWhiteService;
	}

	@Action("/overrateWhite/query")
	public String query() {
		page = overrateWhiteService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/overrateWhite/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if(StringUtils.isNotBlank(id)){
			data = overrateWhiteService.view(id);
		}
		return "edit";
	}

	@Action("/overrateWhite/save")
	public void save() {
		Map<String, String> map = StrutsUtils.getFormData();
		data = overrateWhiteService.save(map);
		StrutsUtils.renderJson(data);
	}
	
	/*@Action("/overrateWhite/update")
	public void update() {
		Map<String, String> map = StrutsUtils.getFormData();
		data = overrateWhiteService.update(map);
		StrutsUtils.renderJson(data);
	}*/

	@Action("/overrateWhite/delete")
	public void deleteKeyword() {
		data = overrateWhiteService.delete(StrutsUtils.getParameterTrim("id"));
		StrutsUtils.renderJson(data);
	}
	
	@Action("/overrateWhite/exportError")
	public void exportError(){
		String filePath = ConfigUtils.save_path +"/import"+ "/超频白名单保存失败列表-userid-"+AuthorityUtils.getLoginUserId()+".xls";
		File file = new File(filePath);
		if(file.exists()){
			FileUtils.download(filePath);
		}else{
			StrutsUtils.renderText("文件过期、不存在或者已经被管理员删除");
		}
	}
	
	@Action("/overrateWhite/downloadExcelTemplate")
	public void downloadInsertTemplate() {
		String path = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/超频白名单批量导入模板.xls");
		FileUtils.download(path);
	}
	
	@Action("/overrateWhite/import")
	public void importExcel(){
		data = overrateWhiteService.importExcel(upload, uploadContentType);
//		StrutsUtils.render("text/plain",data.get("msg").toString());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/overrateWhite/exportExcel")
	public void exportExcel(){
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String filePath = ConfigUtils.save_path + "/超频白名单-" + timeStamp + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		
		excel.addHeader(20, "用户账号", "clientid");
		excel.addHeader(20, "手机号码", "phone");
		excel.addHeader(20, "描述", "remarks");
		
		List<Map<String, Object>> dataList = overrateWhiteService.queryExportExcelData(StrutsUtils.getFormData());
		excel.setDataList(dataList);
		excel.setShowRownum(false);
		excel.setShowPage(false);
		excel.setShowGridLines(false);
		excel.setShowTitle(false);
		
		boolean generateExcelSuccess = ExcelUtils.exportExcel(excel);
		if (generateExcelSuccess) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}
}
