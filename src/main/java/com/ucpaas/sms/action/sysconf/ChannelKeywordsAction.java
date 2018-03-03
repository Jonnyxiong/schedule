package com.ucpaas.sms.action.sysconf;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ucpaas.sms.service.sysconf.ChannelKeywordsService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-通道关键字配置
 * 
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/channelKeywords/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/channelKeywords/edit.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/channelKeywords/edit.jsp")})
public class ChannelKeywordsAction extends BaseAction {
	private static final long serialVersionUID = -8637102929860546005L;
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

	public ChannelKeywordsService getChannelkeyWordsService() {
		return channelkeyWordsService;
	}

	public void setChannelkeyWordsService(ChannelKeywordsService channelkeyWordsService) {
		this.channelkeyWordsService = channelkeyWordsService;
	}

	@Autowired
	private ChannelKeywordsService channelkeyWordsService;
	
	@Action("/channelKeywords/query")
	public String query() {
		page = channelkeyWordsService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/channelKeywords/add")
	public String add() {
		return "add";
	}

	@Action("/channelKeywords/edit")
	public String edit() {
		String cid = StrutsUtils.getParameterTrim("cid");
		if(StringUtils.isNoneBlank(cid)){
			data = channelkeyWordsService.view(cid);
		}else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/channelKeywords/save")
	public void save() {
		String id = StrutsUtils.getParameterTrim("id");
		data = channelkeyWordsService.save(StrutsUtils.getFormData(), id);
		StrutsUtils.renderJson(data);
	}

/*	@Action("/channelKeywords/delete")
	public void deleteKeyword() {
		data = channelkeyWordsService.delete(StrutsUtils.getParameterTrim("cid"));
		StrutsUtils.renderJson(data);
	}*/
	
	@Action("/channelKeywords/import")
	public void importExcel(){
		data = channelkeyWordsService.importExcel(upload, uploadContentType);
//		StrutsUtils.render("text/plain",data.get("msg").toString());
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelKeywords/exportError")
	public void exportError(){
		String filePath = ConfigUtils.save_path +"/import"+ "/通道关键字导入失败列表-userid-"+AuthorityUtils.getLoginUserId()+".xls";
		File file = new File(filePath);
		if(file.exists()){
			FileUtils.download(filePath);
		}else{
			StrutsUtils.renderText("文件过期、不存在或者已经被管理员删除");
		}
	}
	
	@Action("/channelKeywords/getKeywordsByCid")
	public void getKeywordsByCid() {
		String cid = StrutsUtils.getParameterTrim("cid");
		data = channelkeyWordsService.getChannelKeywordsByCid(cid);
		StrutsUtils.renderJson(data);
	}

	@Action("/channelKeywords/downloadExcelTemplate")
	public void downloadInsertTemplate() {
		String path = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/通道关键字批量导入模板.xls");
		FileUtils.download(path);
	}
	
	@Action("/channelKeywords/exportExcel")
	public void exportExcel(){
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String filePath = ConfigUtils.save_path + "/通道关键字-" + timeStamp + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		
		excel.addHeader(20, "通道号", "cid");
		excel.addHeader(20, "通道关键字", "keyword");
		excel.addHeader(20, "状态", "status");
		excel.addHeader(20, "描述", "remarks");
		
		List<Map<String, Object>> dataList = channelkeyWordsService.queryExportExcelData(StrutsUtils.getFormData());
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
