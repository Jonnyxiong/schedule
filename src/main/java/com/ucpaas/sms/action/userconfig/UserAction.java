package com.ucpaas.sms.action.userconfig;

import java.util.HashMap;
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
import com.ucpaas.sms.service.userconfig.UserService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 用户配置-用户及通道组配置
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/userconfig/user/query.jsp"),
		@Result(name = "view", location = "/WEB-INF/content/userconfig/user/view.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/userconfig/user/edit.jsp") })
public class UserAction extends BaseAction {
	private static final long serialVersionUID = -1234214414349439397L;
	private UserService userService;

	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Action("/user/query")
	public String query() {
		page = userService.query(StrutsUtils.getFormData());
		return "query";
	}

	/**
	 * 导出Excel文件
	 */
	@Action("/user/exportExcel")
	public void exportExcel() {
		Map<String, String> params = StrutsUtils.getFormData();
		String text = params.get("text");
		String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
		String filePath = ConfigUtils.save_path + "/用户管理" + timeStamp + ".xls";

		Excel excel = new Excel();
		excel.setFilePath(filePath);
		excel.setTitle("用户管理");
		if (text != null) {
			excel.addRemark("查询条件：" + text);
		}
		excel.addHeader(20, "用户账号", "userid");
		excel.addHeader(20, "短信类型", "smstype2");
		excel.addHeader(20, "区分运营商", "distoperators2");
		excel.addHeader(20, "全网通道组", "channelid");
		excel.addHeader(20, "移动通道组", "ydchannelid");
		excel.addHeader(20, "联通通道组", "ltchannelid");
		excel.addHeader(20, "电信通道组", "dxchannelid");
		excel.addHeader(20, "国际通道组", "gjchannelid");
		excel.addHeader(20, "免黑名单", "unblacklist2");
		excel.addHeader(20, "免系统关键字", "free_keyword2");
		excel.addHeader(20, "免通道关键字", "free_channel_keyword2");
		excel.addHeader(40, "备注", "remarks");
		excel.setDataList(userService.queryAll(params));
		if (ExcelUtils.exportExcel(excel)) {
			FileUtils.download(filePath);
			FileUtils.delete(filePath);
		} else {
			StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
		}
	}

	@Action("/user/edit")
	public String edit() {
		String userid = StrutsUtils.getParameterTrim("userid");
		String smstype = StrutsUtils.getParameterTrim("smstype");
		if (StringUtils.isNotBlank(userid)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userid", userid);
			params.put("smstype", smstype);
			data = userService.view(params);
		} else {
			data = userService.viewSmsOverRateConfig();
		}
		return "edit";
	}

	@Action("/user/view")
	public String view() {
		String userid = StrutsUtils.getParameterTrim("userid");
		String smstype = StrutsUtils.getParameterTrim("smstype");
		if (StringUtils.isNotBlank(userid)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userid", userid);
			params.put("smstype", smstype);
			data = userService.view(params);
		} else {
			data = userService.viewSmsOverRateConfig();
		}
		return "view";
	}

	@Action("/user/save")
	public void save() {
		data = userService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	@Action("/user/updateStatus")
	public void updateStatus() {
		data = userService.updateStatus(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 删除用户
	 */
	@Action("/user/delete")
	public void delete() {
		data = userService.delete(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	/**
	 * 根据sid返回已报备的签名列表
	 */
	@Action("/sign/getSignList")
	public void getSignList() {
		data = userService.getSignList(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	/*@Action("/user/getExtendPortByExtendType")
	public void getExtendPort(){
		data = userService.getExtendPortByExtendType(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}*/
	
	@Action("/user/queryIsClientExist")
	public void queryIsClientExist(){
		data = userService.queryIsClientExist(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 查询sid已经配置的短信类型
	 */
	@Action("/user/getSmsTypeBySid")
	public void getSmsTypeBySid(){
		data = userService.getSmsTypeBySid(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	
	/**
	 * 查询子账号的短信协议类型和短信类型
	 */
	@Action("/user/getSmsFromAndTypeByClientId")
	public void getSmsFromAndTypeByClientId(){
		data = userService.getSmsFromAndTypeByClientId(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	
	/**
	 * 根据运营商类型查询通道组
	 */
	@Action("/user/queryAllChannelGroup")
	public void queryAllChannelGroup(){
		
		data = userService.queryAllChannelGroup();
		StrutsUtils.renderJson(data);
	}
	
}
