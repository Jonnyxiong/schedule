package com.ucpaas.sms.action.admin;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.admin.SysNoticeService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 管理员中心-系统通知管理
 * 
 * @author zenglb
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/admin/sysNotice/query.jsp"),
		@Result(name = "view", location = "/WEB-INF/content/admin/sysNotice/view.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/admin/sysNotice/edit.jsp") })
public class SysNoticeAction extends BaseAction {
	private static final long serialVersionUID = -4968542684473314918L;
	private SysNoticeService sysNoticeService;

	@Resource
	public void setSysNoticeService(SysNoticeService sysNoticeService) {
		this.sysNoticeService = sysNoticeService;
	}

	/**
	 * 查询通知时段
	 * 
	 * @return
	 */
	@Action("/sysNotice/query")
	public String query() {
		page = sysNoticeService.query(StrutsUtils.getFormData());
		return "query";
	}

	/**
	 * 查看通知时段
	 * 
	 * @return
	 */
	@Action("/sysNotice/view")
	public String view() {
		String noticeId = StrutsUtils.getParameterTrim("notice_id");
		if (NumberUtils.isDigits(noticeId)) {
			data = sysNoticeService.view(Integer.valueOf(noticeId));
		}
		return "view";
	}

	/**
	 * 添加通知时段
	 * 
	 * @return
	 */
	@Action("/sysNotice/add")
	public String add() {
		data = sysNoticeService.view(null);
		return "edit";
	}

	/**
	 * 修改通知时段
	 * 
	 * @return
	 */
	@Action("/sysNotice/edit")
	public String edit() {
		String noticeId = StrutsUtils.getParameterTrim("notice_id");
		if (NumberUtils.isDigits(noticeId)) {
			data = sysNoticeService.view(Integer.valueOf(noticeId));
		}
		return "edit";
	}

	/**
	 * 保存通知时段，包括添加、修改
	 * 
	 * @return
	 */
	@Action("/sysNotice/save")
	public void save() {
		data = sysNoticeService.save(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}

	/**
	 * 修改通知时段状态：关闭、启用、删除
	 * 
	 * @return
	 */
	@Action("/sysNotice/updateStatus")
	public void updateStatus() {
		String noticeId = StrutsUtils.getParameterTrim("notice_id");
		String status = StrutsUtils.getParameterTrim("status");
		if (NumberUtils.isDigits(noticeId) && NumberUtils.isDigits(status)) {
			data = sysNoticeService.updateStatus(Integer.parseInt(noticeId), Integer.parseInt(status));
			StrutsUtils.renderJson(data);
		}
	}

}
