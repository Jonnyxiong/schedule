package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-业务预警人员号码管理
 */
public interface BusineesWarnPhoneMgntService {
	
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	PageContainer query(Map<String, String> params);
	/**
	 * 查询编辑页面数据
	 * @param id
	 * @return
	 */
	Map<String, Object> editView(String id);
	/**
	 * 保存
	 * @param params
	 * @return
	 */
	Map<String, Object> save(Map<String, String> params);
	/**
	 * 删除
	 * @param params
	 * @return
	 */
	Map<String, Object> delete(Map<String, String> params);

}
