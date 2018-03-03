package com.ucpaas.sms.tag;

import com.jsmsframework.common.dto.JsmsPage;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.TagService;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页标签
 * 
 * @author xiejiaan
 */
public class JpageTag extends BaseTag {
	private static final long serialVersionUID = 7941249268861678376L;

	private JsmsPage jpage;
	/**
	 * 查询的表单id
	 */
	private String formId;

	@Override
	public String getTemplateFile() {
		return "jpage.ftl";
	}

	@Override
	public Map<String, Object> getTemplateParams(TagService tagService) {
		if (jpage == null) {
			jpage = new JsmsPage();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("jpage", jpage);
		params.put("formId", formId);
		return params;
	}


	public JsmsPage getJpage() {
		return jpage;
	}

	public void setJpage(JsmsPage jpage) {
		this.jpage = jpage;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

}
