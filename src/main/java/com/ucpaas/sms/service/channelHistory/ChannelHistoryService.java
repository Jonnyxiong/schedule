package com.ucpaas.sms.service.channelHistory;

import java.util.List;
import java.util.Map;

/**
 * 短信通道质量实时监控
 * 
 */
public interface ChannelHistoryService {

	/**
	 * 查询通道回执率、应答率堆积图数据
	 * 
	 * @param timeType
	 * @return
	 */
	Map<String, Object> getChannelReportRespStackData(Map<String, String> params);

	/**
	 * 查询导出Excel的数据
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryExportExcelData(Map<String, String> params);

	/**
	 * 查询用户回执率、应答率堆积图数据
	 * 
	 * @param timeType
	 * @return
	 */
	Map<String, Object> getClientIdReportRespStackData(Map<String, String> params);

	/**
	 * 查询导出用户质量Excel的数据
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryExportExcelData4Client(Map<String, String> params);

}
