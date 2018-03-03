package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-强制路由通道管理
 */
public interface SegmentChannelService {
	
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
	/**
	 * 更新状态
	 * @param params
	 * @return
	 */
	Map<String, Object> updateStatus(Map<String, String> formData);
	/**
	 * 根据通道号查询通道的运营商类型
	 * @param channelId
	 * @return
	 */
	Map<String, Object> queryOperatorstypeByChannelId(String channelId);

}
