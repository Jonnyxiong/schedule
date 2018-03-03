package com.ucpaas.sms.service.channelRealtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.ChannelSuccessRateRealtime;
import com.ucpaas.sms.model.ClientSuccessRateRealtime;
import com.ucpaas.sms.model.PageContainer;

/**
 * 短信通道质量实时监控
 * 
 * @author xiejiaan
 */
public interface ChannelRealtimeService {

	/**
	 * 获取图表数据
	 * 
	 * @param channelId
	 *            通道id，null表示所有通道
	 * @param areaIdPid
	 *            地区父id
	 * @param areaId
	 *            地区id
	 * @param startTimestampStr
	 *            开始的时间戳，null表示今天0点0分0秒
	 * @return
	 */
	List<Map<String, Object>> getChartData(String channelId, String areaIdPid, String areaId, String startTimestampStr);

	/**
	 * 查询单个clientId的发送速度信息
	 * 
	 * @param clientId
	 * @return
	 */
	Map<String, Object> getClientSendSpeedById(String clientId);

	/**
	 * 查询所有clientId的发送速度信息
	 * 
	 * @return
	 */
	Map<String, Object> getAllClientSendSpeed();

	/**
	 * 查询Access MQ队列信息
	 * 
	 * @return
	 */
	List<Map<String, Object>> getAccessQueueInfo();

	/**
	 * 查询通道发送速度信息
	 * 
	 * @param channelId
	 * @param timeType
	 * @return
	 */
	Map<String, Object> getChannelSendSpeedById(String channelId, String timeType);

	/**
	 * 查询通道发送成功率
	 * 
	 * @param channelId
	 * @param timeType
	 * @return
	 */
	List<Map<String, Object>> getChannelSuccRateById(String channelId, String timeType);

	/**
	 * 查询通道应答率
	 * 
	 * @param channelId
	 * @param timeType
	 * @return
	 */
	List<Map<String, Object>> getChannelRespRateById(String channelId, String timeType);

	/**
	 * 查询通道回执率
	 * 
	 * @param channelId
	 * @param timeType
	 * @return
	 */
	List<Map<String, Object>> getChannelReportRateById(String channelId, String timeType);

	/**
	 * 查询通道质量图谱信息
	 * 
	 * @param channelId
	 * @param timeType
	 * @return
	 */
	List<Map<String, Object>> getChannelQualityGraphData(Map<String, String> params);

	/**
	 * 查询通道质量表单信息
	 * 
	 * @param timeType
	 * @return
	 */
	PageContainer getChannelQualityTableData(Map<String, String> params);

	/**
	 * 查询通道错误信息
	 * 
	 * @param channelId
	 * @param timeType
	 * @return
	 */
	Map<String, Object> getChannelErrorDataById(String channelId, String timeType);

	/**
	 * 查询客户发送质量信息
	 * 
	 * @param timeType
	 * @return
	 */
	PageContainer getClientQualityTableData(Map<String, String> params);

	/**
	 * 查询通道回执率、应答率堆积图数据
	 * 
	 * @param timeType
	 * @return
	 */
	Map<String, Object> getChannelReportRespStackData(String channelId, String timeType);

	/**
	 * 查询通道质量指数阀值
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryChannelQualityConfig();

	/**
	 * 更新通道质量指数阀值
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> channelQualityConfigUpdate(Map<String, String> params);

	/**
	 * 查询用户回执率、应答率堆积图数据
	 * 
	 * @param timeType
	 * @return
	 */
	Map<String, Object> getClientReportRespStackData(String clientId, String timeType);

	/**
	 * 查询用户成功率堆积图数据
	 * 
	 * @param timeType
	 * @return
	 */
	Map<String, Object> getClientSuccessRateStackData(String clientId, String timeType);
	
	/**
	 * 查询通道成功率堆积图数据
	 * 
	 * @param timeType
	 * @return
	 */
	Map<String, Object> getChannelSuccessRateStackData(String channelId,String startTime,String endTime);
	
	/**
	 * 查询通道成功率数据
	 * 
	 * @param timeType
	 * @return
	 */
	List<ChannelSuccessRateRealtime> queryAllChannelSuccessRateStackData(String channelId, String startTime,
			String endTime);

	/**
	 * 查询用户成功率数据
	 * 
	 * @param timeType
	 * @return
	 */
	List<ClientSuccessRateRealtime> queryAllClientSuccessRateStackData(String channelId, String startTime,
			String endTime);

}
