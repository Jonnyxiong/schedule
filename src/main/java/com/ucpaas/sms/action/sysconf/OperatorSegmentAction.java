package com.ucpaas.sms.action.sysconf;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.service.sysconf.OperatorSegmentService;
import com.ucpaas.sms.util.web.StrutsUtils;
/**
 * 
 * @author TonyHe
 *	运营商号段表
 */
@Controller
@Scope("prototype")                 
@Results({ @Result(name = "query", location = "/WEB-INF/content/sysconf/operatorSegment/query.jsp"),
		@Result(name = "add", location = "/WEB-INF/content/sysconf/operatorSegment/edit.jsp"),
		@Result(name = "edit", location = "/WEB-INF/content/sysconf/operatorSegment/edit.jsp")})
public class OperatorSegmentAction extends BaseAction{
	private static final long serialVersionUID = -960799871877334963L;

	private OperatorSegmentService operatorSegmentService;
	@Resource
	public void setOperatorSegmentService(OperatorSegmentService operatorSegmentService) {
		this.operatorSegmentService = operatorSegmentService;
	}

	@Action("/operatorSegment/query")
	public String query() {
		page = operatorSegmentService.query(StrutsUtils.getFormData());
		return "query";
	}

	@Action("/operatorSegment/add")
	public String add() {
		return "add";
	}

	/**
	 * ？？出现了一个逗号，原因待查
	 */
	@Action("/operatorSegment/save")
	public void save() {
		String operater = StrutsUtils.getParameterTrim("operater");
		Map<String, String> map = StrutsUtils.getFormData();
		String oper = (String)map.get("operater").replace(",", "");
		map.put("operater", oper);
		data = operatorSegmentService.save(map,operater);
		StrutsUtils.renderJson(data);
	}
	
	@Action("/operatorSegment/update")
	public void update() {
		String operater = StrutsUtils.getParameterTrim("operaterold");
		Map<String, String> map = StrutsUtils.getFormData();
		String oper = (String)map.get("operater");
		oper = oper.substring(oper.lastIndexOf(",")+1);
		map.put("operater", oper);
		data = operatorSegmentService.update(map,operater);
		StrutsUtils.renderJson(data);
	}

	@Action("/operatorSegment/delete")
	public void deleteKeyword() {
		data = operatorSegmentService.delete(StrutsUtils.getParameterTrim("operater"));
		StrutsUtils.renderJson(data);
	}
	
	@Action("/operatorSegment/edit")
	public String edit() {
		String operater = StrutsUtils.getParameterTrim("operater");
		if(StringUtils.isNoneBlank(operater)){
			data = operatorSegmentService.view(operater);
		}else {
			data = new HashMap<String, Object>();
		}
		return "edit";
	}
}
