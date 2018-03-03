package com.ucpaas.sms.action.sysconf;

import java.io.File;
import java.util.*;

import com.jsmsframework.audit.entity.JsmsOverrateKeyword;
import com.jsmsframework.audit.service.JsmsOverrateKeywordService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.R;
import com.jsmsframework.common.enums.LogEnum;
import com.jsmsframework.common.util.BeanUtil;

import com.ucpaas.sms.constant.LogConstant;

import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.JsonUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.sysconf.AuditKeyWordsService;


/**
 * 系统设置-审核关键字
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/auditKeyWords/query.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/auditKeyWords/edit.jsp")})
public class AuditKeyWordsAction extends BaseAction {

	private static final long serialVersionUID = -5902533162254427898L;

	@Autowired
	private AuditKeyWordsService auditKeyWordsService;
	@Autowired
	private JsmsOverrateKeywordService jsmsOverrateKeywordService;
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
	
	@Action("/sysConf/auditKeyWords/query")
	public String query() {
		Map<String, String> params = StrutsUtils.getFormData();
		JsmsPage pjpage=new JsmsPage();
		pjpage.setParams(params);
		pjpage.setOrderByClause("update_date desc");
		jpage=jsmsOverrateKeywordService.queryList(pjpage);

		return "query";
	}	
	
	@Action("/sysConf/auditKeyWords/edit")
	public String edit() {
		String id = StrutsUtils.getParameterTrim("id");
		if (NumberUtils.isDigits(id)) {
			JsmsOverrateKeyword over=jsmsOverrateKeywordService.getById(Integer.valueOf(id));
			data = BeanUtil.beanToMap(over,false);
					//auditKeyWordsService.editView(Integer.parseInt(id));
		} else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}

	/**
	 *
	 * @param params
	 * @return
	 */
	private JsmsOverrateKeyword paramsToDto(Map<String,String> params){
		Map<String,Object> param=new HashedMap();
		JsmsOverrateKeyword over =new JsmsOverrateKeyword();
		param.putAll(params);
		BeanUtil.mapToBean(param,over);

		over.setUpdateDate(new Date());
		return over;
	}



	@Action("/sysConf/auditKeyWords/save")
	public void save() {
		Map<String, String> params = StrutsUtils.getFormData();
		Map<String ,Object> param=new HashedMap();
		param.putAll(params);
		R r;
		if(params.get("id")!=null){
			//校验唯一性
			Map<String,Object> check=jsmsOverrateKeywordService.checkExist(param);
			if(check!=null){
				r=R.error("已存在针对此用户账号设置超频关键字，请勿重复添加！");
			}else {
				Integer u=jsmsOverrateKeywordService.update(paramsToDto(params));
				if(u>0){
					r=R.ok("编辑超频关键字设置成功！");
					logService.add(LogConstant.LogType.update, LogEnum.系统配置.getValue(), "系统配置-超频关键字设置：编辑超频关键字设置成功",JsonUtils.toJson(params), params.get("id"));
				}else {
					r=R.error("编辑超频关键字设置失败！");
					logService.add(LogConstant.LogType.update, LogEnum.系统配置.getValue(), "系统配置-超频关键字设置：编辑超频关键字设置失败",JsonUtils.toJson(params),params.get("id"));
				}
			}

		}else {
			//校验唯一性
			Map<String,Object> check=jsmsOverrateKeywordService.checkExist(param);
			if(check!=null){
				r=R.error("已存在针对此用户账号设置超频关键字，请勿重复添加！");
			}else {
				Integer a=jsmsOverrateKeywordService.insert(paramsToDto(params));
				if(a>0){
					r=R.ok("添加超频关键字设置成功！");
					logService.add(LogConstant.LogType.update, LogEnum.系统配置.getValue(), "系统配置-超频关键字设置：添加超频关键字设置成功", JsonUtils.toJson(params));
				}else {
					r=R.error("添加超频关键字设置失败！");
					logService.add(LogConstant.LogType.update, LogEnum.系统配置.getValue(), "系统配置-超频关键字设置：添加超频关键字设置失败",JsonUtils.toJson(params));
				}
			}
		}
	//	data = auditKeyWordsService.save(params);
		StrutsUtils.renderJson(r);
	}
	
	@Action("/sysConf/auditKeyWords/delete")
	public void delete() {
		String id = StrutsUtils.getParameterTrim("id");
		R r=jsmsOverrateKeywordService.delOverrate(Integer.valueOf(id));
		if(r.getCode() ==0){
			logService.add(LogConstant.LogType.delete, LogEnum.系统配置.getValue(), "系统配置-超频关键字设置：删除超频关键字设置成功", StrutsUtils.getFormData(),id);
		}else {
			logService.add(LogConstant.LogType.delete, LogEnum.系统配置.getValue(), "系统配置-超频关键字设置：删除超频关键字设置失败", StrutsUtils.getFormData(), id);
		}

		StrutsUtils.renderJson(r);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("id", id);
//		data = auditKeyWordsService.delete(params);
//		StrutsUtils.renderJson(data);
	}
	
	
	@Action("/sysConf/auditKeyWords/downloadExcelTemplate")
	public void downloadExcelTemplate() {
		String path = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/超频关键字批量导入模板.xls");
		FileUtils.download(path);
	}
	
	@Action("/sysConf/auditKeyWords/importExcel")
	public void importExcel() {
		R r=jsmsOverrateKeywordService.addOverrateKeywordBatch(upload, uploadContentType, AuthorityUtils.getLoginUserId(), ConfigUtils.save_path);
		StrutsUtils.renderJson(r);
		//data = auditKeyWordsService.importExcel(upload, uploadContentType);
		//StrutsUtils.renderJson(data); // IE浏览器会直接下载json数据，要解决只能返回text/plain格式文本然后再浏览器端解析json
	}
	
	@Action("/sysConf/auditKeyWords/exportExcel")
	public void exportExcel(){
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String filePath = ConfigUtils.save_path + "/超频关键字-" + timeStamp + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		
		excel.addHeader(20, "超频关键字", "keyword");
		excel.addHeader(20, "用户账号", "clientid");
		excel.addHeader(20, "备注", "remarks");

		Map<String, String> formData = StrutsUtils.getFormData();
		JsmsPage pjpage=new JsmsPage();
		pjpage.setParams(formData);
		pjpage.setRows(60000);
		pjpage=jsmsOverrateKeywordService.queryList(pjpage);
		List<JsmsOverrateKeyword> overs=pjpage.getData();
		List<Map<String, Object>> dataList=new ArrayList<>() ;
		for (JsmsOverrateKeyword over : overs) {
			Map<String, Object> map=BeanUtil.beanToMap(over,false);
			dataList.add(map);
		}
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
	
	@Action("/sysConf/auditKeyWords/exportError")
	public void exportError(){
		String filePath = ConfigUtils.save_path +"/import"+ "/超频关键字导入失败列表-userid-"+ AuthorityUtils.getLoginUserId()+".xls";
		File file = new File(filePath);
		if(file.exists()){
			FileUtils.download(filePath);
		}else{
			StrutsUtils.renderText("文件过期、不存在或者已经被管理员删除");
		}
	}
	
}
