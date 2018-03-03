package com.ucpaas.sms.action.sysconf;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Resource;

import com.ucpaas.sms.constant.LogConstant;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.service.sysconf.SystemBlackListServiceImpl;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.exception.SmspBusinessException;
import com.ucpaas.sms.service.sysconf.SystemBlackListService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 系统设置-系统黑名单
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/whiteList/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/whiteList/add.jsp") })
public class SystemBlackListAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	private SystemBlackListService whiteListService;

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemBlackListAction.class);

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

	@Resource
	public void setwhiteListService(SystemBlackListService whiteListService) {
		this.whiteListService = whiteListService;
	}

	@Action("/whiteList/query")
	public String query() {
		page = whiteListService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/whiteList/add")
	public String add() {
		return "add";
	}

	@Action("/whiteList/save")
	public void save() {
		try {
			data = whiteListService.save(StrutsUtils.getFormData());
		} catch (SmspBusinessException e) {
			data = new HashMap<String, Object>();
			data.put("result", "fail");
			data.put("msg", e.getMessage());
		}
		StrutsUtils.renderJson(data);
	}

	@Action("/whiteList/deleteWhiteList")
	public void deleteWhiteList() {
		String id = StrutsUtils.getParameterTrim("id");
		String phone = StrutsUtils.getParameterTrim("phone");
		data = whiteListService.deleteWhiteList(id, phone);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/whiteList/import")
	public void importExcel() {
		data = whiteListService.importExcel(upload, uploadContentType);
		try {
			logService.add(LogConstant.LogType.add, LogEnum.系统配置.getValue(), "系统配置-黑名单：批量导入黑名单", AuthorityUtils.getLoginRealName());
		}catch (Exception e){
			LOGGER.error("导入系统黑名单已经完成，写日志时发生异常 e = {}", e);
		}

		StrutsUtils.renderJson(data);
	}

	@Action("/whiteList/downloadExcelTemplate")
	public void downloadExcelTemplate() {
		String path = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/系统黑名单批量导入模板.xls");
		FileUtils.download(path);
	}

	@Action("/whiteList/exportError")
	public void exportError(){
		String filePath = ConfigUtils.save_path +"/import"+ "/批量导入系统黑名单-userid-"+ AuthorityUtils.getLoginUserId()+".xls";
		File file = new File(filePath);
		if(file.exists()){
			FileUtils.download(filePath);
		}else{
			StrutsUtils.renderText("文件过期、不存在或者已经被管理员删除");
		}
	}

}
