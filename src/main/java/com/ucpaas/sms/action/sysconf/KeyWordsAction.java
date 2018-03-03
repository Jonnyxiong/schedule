package com.ucpaas.sms.action.sysconf;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.sysconf.KeyWordsService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;


/**
 * 系统设置-系统关键字管理
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/keywords/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/keywords/add.jsp") })
public class KeyWordsAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	private KeyWordsService keyWordsService;
	
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
	public void setKeyWordsService(KeyWordsService keyWordsService) {
		this.keyWordsService = keyWordsService;
	}

	@Action("/keywords/query")
	public String query() {
		page = keyWordsService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/keywords/add")
	public String add() {
		return "add";
	}

	@Action("/keywords/save")
	public void save() {
		data = keyWordsService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/keywords/deleteKeyword")
	public String deleteKeyword() {
		keyWordsService.deleteKeyword(StrutsUtils.getParameterTrim("id"));
		return query();
	}
	
	@Action("/keywords/import")
	public void importExcel() {
		data = keyWordsService.importExcel(upload, uploadContentType);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/keywords/downloadExcelTemplate")
	public void downloadInsertTemplate() {
		String path = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/系统关键字批量导入模板.xls");
		FileUtils.download(path);
	}
	
	@Action("/keywords/exportExcel")
	public void exportExcel(){
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String filePath = ConfigUtils.save_path + "/系统关键字-" + timeStamp + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
//		excel.setTitle("关键字记录");

//		StringBuffer buffer = new StringBuffer();
//		excel.addRemark(buffer.toString());
		
		excel.addHeader(20, "系统关键字", "keyword");
		excel.addHeader(20, "备注", "remarks");
		Map<String, String> formData = StrutsUtils.getFormData();

		List<Map<String, Object>> dataList = keyWordsService.queryExportExcelData(formData);
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
