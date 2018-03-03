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
import com.ucpaas.sms.service.sysconf.ChannelWhiteListService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-通到白名单配置
 * 
 * @author oylx
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/channelWhiteList/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/channelWhiteList/edit.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/channelWhiteList/edit.jsp")})
public class ChannelWhiteListAction extends BaseAction {
	private static final long serialVersionUID = -4732219486047706109L;
	@Autowired
	private ChannelWhiteListService channelWhiteListService;
	
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
	public void setChannelWhiteListService(ChannelWhiteListService channelWhiteListService) {
		this.channelWhiteListService = channelWhiteListService;
	}

	@Action("/channelWhiteList/query")
	public String query() {
		page = channelWhiteListService.query(StrutsUtils.getFormData());
		return "query";
	}
	 
	@Action("/channelWhiteList/add")
	public String add() {
		return "add";
	}
	
	@Action("/channelWhiteList/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if(StringUtils.isNoneBlank(id)){
			data = channelWhiteListService.view(id);
		}else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
	
	@Action("/channelWhiteList/save")
	public void save() {
		String id = StrutsUtils.getParameterTrim("id");
		data = channelWhiteListService.save(StrutsUtils.getFormData(), id);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelWhiteList/delete")
	public void deleteKeyword() {
		data = channelWhiteListService.delete(StrutsUtils.getParameterTrim("id"));
		StrutsUtils.renderJson(data);
	}
	
	@Action("/channelWhiteList/import")
	public void importExcel() {
		data = channelWhiteListService.importExcel(upload, uploadContentType);
		StrutsUtils.render("text/plain",data.get("msg").toString());
	}
	
	@Action("/channelWhiteList/exportError")
	public void exportError(){
		String filePath = ConfigUtils.save_path +"/import"+ "/白名单导入失败列表-userid-"+AuthorityUtils.getLoginUserId()+".xls";
		File file = new File(filePath);
		if(file.exists()){
			FileUtils.download(filePath);
		}else{
			StrutsUtils.renderText("文件过期、不存在或者已经被管理员删除");
		}
		}
}
