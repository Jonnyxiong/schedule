package com.ucpaas.sms.action.sysconf;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.ChannelBlackListService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.JsonUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-通到黑名单配置
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/channelBlackList/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/channelBlackList/edit.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/channelBlackList/edit.jsp")})
public class ChannelBlackListAction extends BaseAction {

	private static final long serialVersionUID = 2442415330680864130L;
	@Autowired
	private ChannelBlackListService channelBlackListService;
	
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
	public void setChannelBlackListService(ChannelBlackListService channelBlackListService) {
		this.channelBlackListService = channelBlackListService;
	}

	@Action("/channelBlackList/query")
	public String query() {
		page = channelBlackListService.query(StrutsUtils.getFormData());
		return "query";
	}
	 
	@Action("/channelBlackList/add")
	public String add() {
		return "add";
	}
	
	@Action("/channelBlackList/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if(StringUtils.isNoneBlank(id)){
			data = channelBlackListService.view(id);
		}else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}

	@Action("/channelBlackList/save")
	public void save() {
		String id = StrutsUtils.getParameterTrim("id");
		data = channelBlackListService.save(StrutsUtils.getFormData(), id);
		StrutsUtils.renderJson(data);
	}

	@Action("/channelBlackList/delete")
	public void deleteKeyword() {
		String id = StrutsUtils.getParameterTrim("id");
		String phone = StrutsUtils.getParameterTrim("phone");
		String channelId = StrutsUtils.getParameterTrim("channelId");
		data = channelBlackListService.delete(id, phone, channelId);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelBlackList/import")
	public void importExcel() {
		data = channelBlackListService.importExcel(upload, uploadContentType);
		StrutsUtils.renderJson(data);
//		StrutsUtils.render("text/plain",data.get("msg").toString());
	}
	
	@Action("/channelBlackList/exportError")
	public void exportError(){
		String filePath = ConfigUtils.save_path +"/import"+ "/通道黑名单导入失败列表-userid-"+AuthorityUtils.getLoginUserId()+".xls";
		File file = new File(filePath);
		if(file.exists()){
			FileUtils.download(filePath);
		}else{
			StrutsUtils.renderText("文件过期、不存在或者已经被管理员删除");
		}
	}
	
	@Action("/channelBlackList/downloadExcelTemplate")
	public void downloadInsertTemplate() {
		String path = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/通道黑名单批量导入模板 .xls");
		FileUtils.download(path);
	}
	
}
