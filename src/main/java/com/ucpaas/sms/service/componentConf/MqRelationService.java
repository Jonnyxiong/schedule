package com.ucpaas.sms.service.componentConf;

import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 组件管理-SMSP组件与MQ关联配置
 * 
 */
public interface MqRelationService {
	
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
	Map<String, Object> editView(int id);

	/**
	 * 保存
	 * @param params
	 * @return
	 */
	Map<String, Object> update(Map<String, String> params);

	/**
	 * 删除
	 * @param params
	 * @return
	 */
	Map<String, Object> delete(Map<String, String> params);

	List<Map<String, Object>> queryAllMqQueue(Map<String, String> params);

	Map<String, Object> create(Map<String, String> params);

	Map<String, Object> updateMqGetRate(Map<String, String> params);
	
}
