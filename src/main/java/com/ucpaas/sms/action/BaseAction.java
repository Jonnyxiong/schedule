package com.ucpaas.sms.action;

import java.util.List;
import java.util.Map;

import com.jsmsframework.common.dto.JsmsPage;
import com.opensymphony.xwork2.ActionSupport;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * action类的基类
 * 
 * @author xiejiaan
 */
public class BaseAction extends ActionSupport {
	private static final long serialVersionUID = -6024322463400978622L;

	/**
	 * 分页信息
	 */
	protected PageContainer page;


	protected JsmsPage jpage;
	/**
	 * 返回数据
	 */
	protected Map<String, Object> data;
	
	/**
	 * 返回List数据
	 */
	protected List<Map<String, Object>> dataList;

	/**
	 * 请求页面，用于返回刷新
	 */
	private String referer;

	public PageContainer getPage() {
		return page;
	}

	public void setPage(PageContainer page) {
		this.page = page;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getReferer() {
		referer = StrutsUtils.getReferer();
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public JsmsPage getJpage() {
		return jpage;
	}

	public void setJpage(JsmsPage jpage) {
		this.jpage = jpage;
	}
}
