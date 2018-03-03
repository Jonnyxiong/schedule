package com.ucpaas.sms.action.alarm;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.alarm.AlarmService;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 预警条件管理
 *
 */
@Controller
@Scope("prototype")
@Results({ @Result(name = "query", location = "/WEB-INF/content/alarm/query.jsp"),
			@Result(name = "view", location = "/WEB-INF/content/alarm/view.jsp"),
			@Result(name = "edit", location = "/WEB-INF/content/alarm/edit.jsp")})
public class AlarmAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1097217024479474390L;

	@Autowired
	private AlarmService alarmService;
	
	/**
	 * 分页查询告警条件
	 * @return
	 */
	@Action("/alarm/query")
	public String query() {
		page = alarmService.query(StrutsUtils.getFormData());
		return "query";
	}
	
	/**
	 * 配置告警条件
	 * 
	 * @return
	 */
	@Action("/alarm/add")
	public String add() {
		return "edit";
	}
	
	/**
	 * 修改告警条件
	 * 
	 * @return
	 */
	@Action("/alarm/edit")
	public String edit() {
		String alarm_id = StrutsUtils.getParameterTrim("alarm_id");
		if (NumberUtils.isDigits(alarm_id)) {
			data = alarmService.getByAlarmId(Long.valueOf(alarm_id));
		}
		return "edit";
	}
	
	/**
	 * 配置告警条件，包括添加、修改
	 * 
	 * @return
	 */
	@Action("/alarm/save")
	public void save() {
		data = alarmService.saveAlarmCondition(StrutsUtils.getFormData());
		StrutsUtils.renderJson(data);
	}
	
	/**
	 * 查看通知时段
	 * 
	 * @return
	 */
	@Action("/alarm/view")
	public String view() {
		String alarmId = StrutsUtils.getParameterTrim("alarm_id");
		if (NumberUtils.isDigits(alarmId)) {
			data = alarmService.getByAlarmId(Long.valueOf(alarmId));
		}
		return "view";
	}
	
	/**
	 * 修改告警条件状态：0关闭，1启用，2已删除
	 * 
	 * @return
	 */
	@Action("/alarm/updateStatus")
	public void updateStatus() {
		String alarmId = StrutsUtils.getParameterTrim("alarm_id");
		String status = StrutsUtils.getParameterTrim("status");
		if (NumberUtils.isDigits(alarmId) && NumberUtils.isDigits(status)) {
			data = alarmService.updateStatus(Integer.valueOf(alarmId), Integer.valueOf(status));
			StrutsUtils.renderJson(data);
		}
	}
}
