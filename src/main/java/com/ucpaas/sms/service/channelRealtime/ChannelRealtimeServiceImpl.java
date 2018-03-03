package com.ucpaas.sms.service.channelRealtime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.DbConstant.DbType;
import com.ucpaas.sms.constant.DbConstant.TablePrefix;
import com.ucpaas.sms.constant.TaskConstant.TaskId;
import com.ucpaas.sms.dao.AccessMasterDao;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.dao.StatsMasterDao;
import com.ucpaas.sms.model.ChannelSuccessRateRealtime;
import com.ucpaas.sms.model.ClientStackData;
import com.ucpaas.sms.model.ClientSuccessRateRealtime;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.service.MonitorService;
import com.ucpaas.sms.util.UcpaasDateUtils;
import com.ucpaas.sms.util.rest.utils.DateUtil;

/**
 * 短信通道质量实时监控
 * 
 * @author xiejiaan
 */
@Service
@Transactional
public class ChannelRealtimeServiceImpl implements ChannelRealtimeService {
	@Autowired
	private AccessMasterDao accessMasterDao;
	@Autowired
	private StatsMasterDao statsMasterDao;
	@Autowired
	private MessageMasterDao messageMasterDao;
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private CommonService commonService;

	private static final Logger logger = LoggerFactory.getLogger(ChannelRealtimeServiceImpl.class);

	@Override
	public List<Map<String, Object>> getChartData(String channelId, String areaIdPid, String areaId,
			String startTimestampStr) {
		Map<String, Object> timeBucketMap;
		List<Map<String, Object>> originalDataList;

		timeBucketMap = getDataTimeBucket(startTimestampStr);
		if (timeBucketMap != null) {
			originalDataList = getOriginalDataList(timeBucketMap, channelId, areaId, areaIdPid);
			return buildChartDataList(originalDataList, timeBucketMap, channelId);
		} else {
			return null;
		}
	}

	/**
	 * 根据页面highchart传过来的时间查询下次展示数据的时间段
	 * 
	 * @param startTimestampStr
	 * @return
	 */
	private Map<String, Object> getDataTimeBucket(String startTimestampStr) {
		String startTime = null;
		String endTime = null;
		String endTimestampStr = null;
		if (startTimestampStr == null) {
			DateTime dt = DateTime.now().withMillisOfDay(0);
			startTimestampStr = String.valueOf(dt.getMillis());
			startTime = dt.toString("yyyy-MM-dd HH:mm:ss");
		} else {
			startTime = new DateTime(Long.parseLong(startTimestampStr)).toString("yyyy-MM-dd HH:mm:ss");
		}
		Map<String, Object> map = monitorService.getStatTime(TaskId.task_id_3, startTime);
		if (map == null) {
			return null;
		}
		endTime = map.get("end_time").toString();
		endTimestampStr = map.get("end_timestamp").toString();
		Map<String, Object> timeBucketMap = new HashMap<String, Object>();
		timeBucketMap.put("startTime", startTime);
		timeBucketMap.put("endTime", endTime);
		timeBucketMap.put("startTimestampStr", startTimestampStr);
		timeBucketMap.put("endTimestampStr", endTimestampStr);

		return timeBucketMap;
	}

	private List<Map<String, Object>> getOriginalDataList(Map<String, Object> timeBucketMap, String channelId,
			String areaId, String areaIdPid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("date", DateTime.now().toString("yyyyMMdd"));
		params.put("channel_id", channelId);
		params.put("area_id_pid", areaIdPid);
		params.put("area_id", areaId);
		params.put("start_time", timeBucketMap.get("startTime"));
		params.put("end_time", timeBucketMap.get("endTime"));
		return accessMasterDao.getSearchList("channelRealtime.getChartData", params);
	}

	private List<Map<String, Object>> buildChartDataList(List<Map<String, Object>> dataList,
			Map<String, Object> timeBucket, String channelId) {
		List<Map<String, Object>> chartDataList = new ArrayList<Map<String, Object>>();

		long startTimestamp = Long.parseLong((String) timeBucket.get("startTimestampStr"));
		long endTimestamp = Long.parseLong((String) timeBucket.get("endTimestampStr"));
		int size = dataList.size();
		int index = 0;
		Map<String, Object> data = null;
		long datatime = 0;

		Map<String, Object> channelDefaultRate = new HashMap<String, Object>();
		if (channelId != null) {
			channelDefaultRate = accessMasterDao.getOneInfo("channelRealtime.getChannelDefaultRate",
					Integer.valueOf(channelId));
		}

		for (long i = startTimestamp; i <= endTimestamp; i += 180000) {
			if (datatime == 0 && index < size) {
				data = dataList.get(index++);
				datatime = Long.parseLong(data.get("datatime").toString());
			}
			if (i == datatime) {
				chartDataList.add(data);
				data = null;
				datatime = 0;
			} else {
				Map<String, Object> defaultData = new HashMap<String, Object>();
				defaultData.put("sendtotalcnt", 0);
				defaultData.put("timelytotalcnt", 0);
				defaultData.put("reachtotalcnt", 0);
				defaultData.put("concurrency", 0);
				if (channelDefaultRate != null && !channelDefaultRate.isEmpty()) {
					defaultData.put("timelyrate", channelDefaultRate.get("timelyrate"));
					defaultData.put("reachrate", channelDefaultRate.get("reachrate"));
				} else {
					defaultData.put("timelyrate", 100);
					defaultData.put("reachrate", 100);
				}
				defaultData.put("datatime", i);

				chartDataList.add(defaultData);
			}
		}

		return chartDataList;
	}

	@Override
	public Map<String, Object> getClientSendSpeedById(String clientId) {

		// 获取24小时的开始和结束时间（5分钟的整数倍、比当前时间早且最近）
		long now = new DateTime().getMillis();
		long yusu = now % 300000L;
		now = now - yusu;
		String startTime = new DateTime(now).minusHours(24).toString("yyyy-MM-dd HH:mm:ss");
		String endTime = new DateTime(now).toString("yyyy-MM-dd HH:mm:ss");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_client_speed_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("clientId", clientId);
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得24小时范围内客户发送速率的统计数据
			List<Map<String, Object>> clientSpeedDataList = statsMasterDao
					.getSearchList("channelRealtime.getClientSendSpeedById", params);
			String clientPromiseSpeed = Objects
					.toString(messageMasterDao.getOneInfo("channelRealtime.getClientPromiseSpeed", clientId), "50");

			// 返回构建的Echart显示数据
			return getEchartTimeLineData(clientSpeedDataList, clientPromiseSpeed, startTime, endTime);
		} else {
			return new HashMap<String, Object>();
		}

	}

	private Map<String, Object> getEchartTimeLineData(List<Map<String, Object>> dataList, String promiseSpeed,
			String startTimeStr, String endTimeStr) {

		DateTime startTime = UcpaasDateUtils.parseDate(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		DateTime endTime = UcpaasDateUtils.parseDate(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		long startTimeOfMills = startTime.getMillis();
		long endTimeOfMills = endTime.getMillis();

		List<Object> actualSeries = new ArrayList<Object>();
		List<Object> promiseSeries = new ArrayList<Object>();

		List<Map<String, Object>> timeList = dataList;
		int size = timeList.size();
		int index = 0;
		Map<String, Object> data = null;
		long timeMils = 0;
		for (long i = startTimeOfMills; i <= endTimeOfMills; i += 300000) {
			Map<String, Object> actualPointMap = new HashMap<String, Object>();
			Map<String, Object> promisePointMap = new HashMap<String, Object>();
			List<String> actualValueList = new ArrayList<String>();
			List<String> promiseValueList = new ArrayList<String>();
			String dateTime = "";
			String actualSpeed = "";
			String defaultSpeed = "0";

			// 获得clientId短信发送速率数据
			if (timeMils == 0 && index < size) {
				data = timeList.get(index++);
				timeMils = Long.parseLong(data.get("data_time").toString());
			}
			if (i == timeMils) {
				dateTime = new DateTime(i).toString("MM-dd HH:mm");
				actualSpeed = String.valueOf(data.get("send_speed").toString());

				actualValueList.add(dateTime);
				actualValueList.add(actualSpeed);
				actualPointMap.put("name", "实际速率");
				actualPointMap.put("value", actualValueList);
				actualSeries.add(actualPointMap);

				promiseValueList.add(dateTime);
				promiseValueList.add(promiseSpeed);
				promisePointMap.put("name", "承诺速率");
				promisePointMap.put("value", promiseValueList);
				promiseSeries.add(promisePointMap);

				data = null;
				timeMils = 0;
			} else {
				dateTime = new DateTime(i).toString("MM-dd HH:mm");

				// 该时间点clientId没有速率数据时的默认数据
				actualValueList.add(dateTime);
				actualValueList.add(defaultSpeed);
				actualPointMap.put("name", "实际速率");
				actualPointMap.put("value", actualValueList);
				actualSeries.add(actualPointMap);

			}
			promiseValueList.add(dateTime);
			promiseValueList.add(promiseSpeed);
			promisePointMap.put("name", "承诺速率");
			promisePointMap.put("value", promiseValueList);
			promiseSeries.add(promisePointMap);
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("actualSeries", actualSeries);
		resultMap.put("promiseSeries", promiseSeries);

		return resultMap;
	}

	/**
	 * 构建Echart时间曲线图数据（短信账号发送速率、通道发送速率）
	 * 
	 * @param dataList
	 * @param promiseSpeed
	 * @return
	 */
	private Map<String, Object> getEchartTimeLineData_Old(List<Map<String, Object>> dataList, String promiseSpeed) {
		if (dataList.size() > 0 && dataList != null) {
			String chartName = (String) dataList.get(dataList.size() - 1).get("chart_name");
			List<Object> actualSeries = new ArrayList<Object>();
			List<Object> promiseSeries = new ArrayList<Object>();

			// 构建“实际速率”和“承诺速率”两条数据线的数据
			for (int pos = 0; pos < dataList.size(); pos++) {
				Map<String, Object> actualPointMap = new HashMap<String, Object>();
				Map<String, Object> promisePointMap = new HashMap<String, Object>();
				List<String> actualValueList = new ArrayList<String>();
				List<String> promiseValueList = new ArrayList<String>();

				String dateTime = dataList.get(pos).get("data_time").toString();
				String sendSpeed = String.valueOf(dataList.get(pos).get("send_speed"));

				actualValueList.add(dateTime);
				actualValueList.add(sendSpeed);
				actualPointMap.put("name", "实际速率");
				actualPointMap.put("value", actualValueList);
				actualSeries.add(actualPointMap);

				promiseValueList.add(dateTime);
				promiseValueList.add(promiseSpeed);
				promisePointMap.put("name", "承诺速率");
				promisePointMap.put("value", promiseValueList);
				promiseSeries.add(promisePointMap);
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("chartName", chartName);
			resultMap.put("actualSeries", actualSeries);
			resultMap.put("promiseSeries", promiseSeries);

			/**
			 * Object { chartName: "a000000", actualSeries:[ { name: "实际速率",
			 * value: ["11/29 16:15", 100] }, { ... } ], promiseSeries:[ { name:
			 * "承诺速率", value: ["11/29 16:15", 100] }, { ... } ] }
			 */
			return resultMap;
		}

		return new HashMap<String, Object>();
	}

	@Override
	public Map<String, Object> getAllClientSendSpeed() {

		// 获取24小时的开始和结束时间（5分钟的整数倍、比当前时间早且最近）
		long now = new DateTime().getMillis();
		long yusu = now % 300000L;
		now = now - yusu;
		String startTime = new DateTime(now).minusHours(24).toString("yyyy-MM-dd HH:mm:ss");
		String endTime = new DateTime(now).toString("yyyy-MM-dd HH:mm:ss");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_client_speed_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得24小时范围内客户发送速率的统计数据
			List<Map<String, Object>> clientSpeedDataList = statsMasterDao
					.getSearchList("channelRealtime.getAllClientSendSpeed", params);
			String allClientPromiseSpeed = Objects
					.toString(messageMasterDao.getOneInfo("channelRealtime.getAllClientPromiseSpeed", null), "50");

			// 返回构建的Echart显示数据
			return getEchartTimeLineData(clientSpeedDataList, allClientPromiseSpeed, startTime, endTime);
		} else {
			return new HashMap<String, Object>();
		}

	}

	@Override
	public List<Map<String, Object>> getAccessQueueInfo() {

		Map<String, Object> params = new HashMap<String, Object>();
		DateTime now = new DateTime();
		String date = now.toString("yyyyMMdd");
		String startTime = now.toString("yyyy-MM-dd");
		String endTime = now.toString("yyyy-MM-dd");
		params.put("date", date);
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_access_queue_stat_, startTime, endTime);
		if (tableList.size() == 0) {
			logger.info("【ACCESS队列消息数监控】- t_sms_access_queue_stat_{}{}", date, "表不存在");

			return new ArrayList<Map<String, Object>>();
		}

		// 统计数据的最新数据时间
		String dataTime = statsMasterDao.getOneInfo("channelRealtime.getAccessQueueDataMaxTime", params);
		if (StringUtils.isBlank(dataTime)) {
			logger.info("【ACCESS队列消息数监控】- 查询统计数据的最新数据时间为空，查询流水表 = t_sms_access_queue_stat_{}", date);

			Map<String, Object> queueInfoMap1 = new HashMap<String, Object>();
			Map<String, Object> queueInfoMap2 = new HashMap<String, Object>();
			Map<String, Object> queueInfoMap3 = new HashMap<String, Object>();
			Map<String, Object> queueInfoMap4 = new HashMap<String, Object>();
			Map<String, Object> queueInfoMap5 = new HashMap<String, Object>();
			Map<String, Object> queueInfoMap6 = new HashMap<String, Object>();
			Map<String, Object> queueInfoMap7 = new HashMap<String, Object>();
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			queueInfoMap1.put("name", "移动行业");
			queueInfoMap1.put("value", 0);
			queueInfoMap2.put("name", "移动营销");
			queueInfoMap2.put("value", 0);
			queueInfoMap3.put("name", "电信行业");
			queueInfoMap3.put("value", 0);
			queueInfoMap4.put("name", "电信营销");
			queueInfoMap4.put("value", 0);
			queueInfoMap5.put("name", "联通行业");
			queueInfoMap5.put("value", 0);
			queueInfoMap6.put("name", "联通营销");
			queueInfoMap6.put("value", 0);
			queueInfoMap7.put("name", "空闲缓存");
			queueInfoMap7.put("value", 100000000);
			resultList.add(queueInfoMap1);
			resultList.add(queueInfoMap2);
			resultList.add(queueInfoMap3);
			resultList.add(queueInfoMap4);
			resultList.add(queueInfoMap5);
			resultList.add(queueInfoMap6);
			resultList.add(queueInfoMap7);
			return resultList;

		} else {

			String maxFreeNumber = "100000000";// 默认最大条数1亿
			params.put("message_max_number", maxFreeNumber);
			params.put("data_time", dataTime);

			// 查询队列数据
			List<Map<String, Object>> accessQueueInfoList = statsMasterDao
					.getSearchList("channelRealtime.getAccessQueueInfo", params);

			// 构建Echart数据
			if (accessQueueInfoList != null && accessQueueInfoList.size() > 0) {
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				Map<String, Object> freeSpaceMap = new HashMap<String, Object>();
				Integer freeNum = Integer.parseInt(maxFreeNumber);

				for (int pos = 0; pos < accessQueueInfoList.size(); pos++) {
					Map<String, Object> queueInfoMap = new HashMap<String, Object>();
					queueInfoMap.put("name", accessQueueInfoList.get(pos).get("queue_type_name"));
					Integer messageNum = Integer
							.valueOf(String.valueOf(accessQueueInfoList.get(pos).get("message_num")));
					queueInfoMap.put("value", messageNum);
					freeNum -= messageNum;

					resultList.add(queueInfoMap);
				}

				freeSpaceMap.put("name", "空闲缓存");
				freeSpaceMap.put("value", freeNum);
				resultList.add(freeSpaceMap);

				logger.info("【ACCESS队列消息数监控】- 查询统计数据返回页面，数据查询条件={}", params);
				return resultList;
			}

			return new ArrayList<Map<String, Object>>();
		}

	}

	@Override
	public Map<String, Object> getChannelSendSpeedById(String channelId, String timeType) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();

		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_channel_indexes_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			params.put("channelId", channelId);
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得通道发送速率的统计数据
			List<Map<String, Object>> channelSpeedDataList = statsMasterDao
					.getSearchList("channelRealtime.getChannelSendSpeedById", params);
			String channelPromiseSpeed = Objects
					.toString(messageMasterDao.getOneInfo("channelRealtime.getChannelPromiseSpeed", channelId), "50");

			// 构建Echart显示数据
			resultMap = getEchartTimeLineData(channelSpeedDataList, channelPromiseSpeed, startTime, endTime);
			return resultMap;
		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public List<Map<String, Object>> getChannelSuccRateById(String channelId, String timeType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_channel_indexes_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			params.put("channelId", channelId);
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得通道发送速率的统计数据
			Map<String, Object> channelSuccRateMap = statsMasterDao.getOneInfo("channelRealtime.getChannelSuccRateById",
					params);

			Map<String, Object> successMap = new HashMap<String, Object>();
			Map<String, Object> failureMap = new HashMap<String, Object>();
			Map<String, Object> unknownMap = new HashMap<String, Object>();
			if (null != channelSuccRateMap) {
				// 构建EChart pie显示数据
				successMap.put("name", "发送成功");
				successMap.put("value", channelSuccRateMap.get("send_sccuss_num"));
				failureMap.put("name", "发送失败");
				failureMap.put("value", channelSuccRateMap.get("send_failure_num"));
				unknownMap.put("name", "未知");
				unknownMap.put("value", channelSuccRateMap.get("send_unknown_num"));

			} else {
				// 默认数据
				successMap.put("name", "发送成功");
				successMap.put("value", 0);
				failureMap.put("name", "发送失败");
				failureMap.put("value", 0);
				unknownMap.put("name", "未知");
				unknownMap.put("value", 0);
			}
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			resultList.add(successMap);
			resultList.add(failureMap);
			resultList.add(unknownMap);

			return resultList;

		} else {
			return new ArrayList<Map<String, Object>>();
		}
	}

	@Override
	public List<Map<String, Object>> getChannelRespRateById(String channelId, String timeType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_channel_indexes_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			params.put("channelId", channelId);
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得通道应答率的统计数据
			Map<String, Object> channelRespRateMap = statsMasterDao.getOneInfo("channelRealtime.getChannelRespRateById",
					params);

			Map<String, Object> resp1Map = new HashMap<String, Object>();
			Map<String, Object> resp2Map = new HashMap<String, Object>();
			Map<String, Object> resp3Map = new HashMap<String, Object>();
			Map<String, Object> resp4Map = new HashMap<String, Object>();
			Map<String, Object> resp5Map = new HashMap<String, Object>();
			Map<String, Object> resp6Map = new HashMap<String, Object>();
			Map<String, Object> resp7Map = new HashMap<String, Object>();

			if (null != channelRespRateMap) {
				// 构建EChart pie显示数据
				resp1Map.put("name", "0-2s");
				resp1Map.put("value", channelRespRateMap.get("resp_num_1"));
				resp2Map.put("name", "3-5s");
				resp2Map.put("value", channelRespRateMap.get("resp_num_2"));
				resp3Map.put("name", "6-10s");
				resp3Map.put("value", channelRespRateMap.get("resp_num_3"));
				resp4Map.put("name", "11-60s");
				resp4Map.put("value", channelRespRateMap.get("resp_num_4"));
				resp5Map.put("name", "61-300s");
				resp5Map.put("value", channelRespRateMap.get("resp_num_5"));
				resp6Map.put("name", ">300s");
				resp6Map.put("value", channelRespRateMap.get("resp_num_6"));
				resp7Map.put("name", "应答超时");
				resp7Map.put("value", channelRespRateMap.get("resp_num_7"));
			} else {
				// 默认数据
				resp1Map.put("name", "0-2s");
				resp1Map.put("value", 0);
				resp2Map.put("name", "3-5s");
				resp2Map.put("value", 0);
				resp3Map.put("name", "6-10s");
				resp3Map.put("value", 0);
				resp4Map.put("name", "11-60s");
				resp4Map.put("value", 0);
				resp5Map.put("name", "61-300s");
				resp5Map.put("value", 0);
				resp6Map.put("name", ">300s");
				resp6Map.put("value", 0);
				resp7Map.put("name", "应答超时");
				resp7Map.put("value", 0);
			}

			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			resultList.add(resp1Map);
			resultList.add(resp2Map);
			resultList.add(resp3Map);
			resultList.add(resp4Map);
			resultList.add(resp5Map);
			resultList.add(resp6Map);
			resultList.add(resp7Map);
			return resultList;

		} else {
			return new ArrayList<Map<String, Object>>();
		}
	}

	@Override
	public List<Map<String, Object>> getChannelReportRateById(String channelId, String timeType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_channel_indexes_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			params.put("channelId", channelId);
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得通道回执率的统计数据
			Map<String, Object> channelRespRateMap = statsMasterDao
					.getOneInfo("channelRealtime.getChannelReportRateById", params);

			// 构建EChart pie显示数据
			Map<String, Object> report1Map = new HashMap<String, Object>();
			Map<String, Object> report2Map = new HashMap<String, Object>();
			Map<String, Object> report3Map = new HashMap<String, Object>();
			Map<String, Object> report4Map = new HashMap<String, Object>();
			Map<String, Object> report5Map = new HashMap<String, Object>();
			Map<String, Object> report6Map = new HashMap<String, Object>();
			Map<String, Object> report7Map = new HashMap<String, Object>();
			if (null != channelRespRateMap) {
				report1Map.put("name", "0-10s");
				report1Map.put("value", channelRespRateMap.get("report_num_1"));
				report2Map.put("name", "11-30s");
				report2Map.put("value", channelRespRateMap.get("report_num_2"));
				report3Map.put("name", "31-60s");
				report3Map.put("value", channelRespRateMap.get("report_num_3"));
				report4Map.put("name", "61-120s");
				report4Map.put("value", channelRespRateMap.get("report_num_4"));
				report5Map.put("name", "121-300s");
				report5Map.put("value", channelRespRateMap.get("report_num_5"));
				report6Map.put("name", ">300s");
				report6Map.put("value", channelRespRateMap.get("report_num_6"));
				report7Map.put("name", "回执未返回");
				report7Map.put("value", channelRespRateMap.get("report_num_7"));

				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				resultList.add(report1Map);
				resultList.add(report2Map);
				resultList.add(report3Map);
				resultList.add(report4Map);
				resultList.add(report5Map);
				resultList.add(report6Map);
				resultList.add(report7Map);

				return resultList;
			} else {
				// 默认数据
				report1Map.put("name", "0-10s");
				report1Map.put("value", 0);
				report2Map.put("name", "11-30s");
				report2Map.put("value", 0);
				report3Map.put("name", "31-60s");
				report3Map.put("value", 0);
				report4Map.put("name", "61-120s");
				report4Map.put("value", 0);
				report5Map.put("name", "121-300s");
				report5Map.put("value", 0);
				report6Map.put("name", ">300s");
				report6Map.put("value", 0);
				report7Map.put("name", "回执未返回");
				report7Map.put("value", 0);
			}

			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			resultList.add(report1Map);
			resultList.add(report2Map);
			resultList.add(report3Map);
			resultList.add(report4Map);
			resultList.add(report5Map);
			resultList.add(report6Map);
			resultList.add(report7Map);

			return resultList;

		} else {
			return new ArrayList<Map<String, Object>>();
		}
	}

	private Map<String, Object> getStartAndEndTime(String timeType) {
		Map<String, Object> result = new HashMap<String, Object>();
		DateTime now = new DateTime();
		// now = new DateTime(2016,12,22,0,0,0);
		String startTime = "";
		String endTime = "";
		if (timeType.equals("0")) { // 5分钟
			startTime = now.minusMinutes(5).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("1")) { // 1小时
			startTime = now.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("2")) { // 12小时
			startTime = now.minusHours(12).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("3")) { // 24小时
			startTime = now.minusHours(24).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else {

		}

		result.put("startTime", startTime);
		result.put("endTime", endTime);

		return result;
	}

	private Map<String, String> getStartAndEndTime2(String timeType, DateTime time) {
		Map<String, String> result = new HashMap<String, String>();
		DateTime now = time;
		if (time != null) {
			now = time;
		} else {
			now = new DateTime();
		}
		// now = new DateTime(2016,12,22,0,0,0);
		String startTime = "";
		String endTime = "";
		if (timeType.equals("0")) { // 5分钟
			startTime = now.minusMinutes(5).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("1")) { // 1小时
			startTime = now.minusHours(1).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("2")) { // 12小时
			startTime = now.minusHours(12).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else if (timeType.equals("3")) { // 24小时
			startTime = now.minusHours(24).toString("yyyy-MM-dd HH:mm:ss");
			endTime = now.toString("yyyy-MM-dd HH:mm:ss");
		} else {

		}

		result.put("startTime", startTime);
		result.put("endTime", endTime);

		return result;
	}

	@Override
	public List<Map<String, Object>> getChannelQualityGraphData(Map<String, String> params) {
		DateTime now = new DateTime();
		String date = now.toString("yyyyMMdd");
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("date", date);
		String maxDataTimeStr = statsMasterDao.getOneInfo("channelRealtime.getChannelQualityMaxDateTime", sqlParam);
		DateTime maxDataTime;
		if (StringUtils.isBlank(maxDataTimeStr)) {
			maxDataTime = null;
		} else {
			maxDataTime = UcpaasDateUtils.parseDate(maxDataTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		String timeType = Objects.toString(params.get("timeType"), "0");
		Map<String, String> timeMap = getStartAndEndTime2(timeType, maxDataTime);
		String startTime = timeMap.get("startTime");
		String endTime = timeMap.get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_channel_status_graph_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			sqlParams.put("tableList", tableList);
			sqlParams.put("startTime", startTime);
			sqlParams.put("endTime", endTime);

			long time1 = System.currentTimeMillis();
			List<Map<String, Object>> allChannelList = statsMasterDao
					.getSearchList("channelRealtime.getChannelListByTime", sqlParams);
			long time2 = System.currentTimeMillis();
			System.out.println("##### service query1 time #####" + (time2 - time1));

			// 获得通道状态的统计数据
			List<Map<String, Object>> channelQualityList = new ArrayList<Map<String, Object>>();
			if (allChannelList != null && allChannelList.size() > 0) {
				long time3 = System.currentTimeMillis();
				for (Map<String, Object> channel : allChannelList) {
					sqlParams.put("channelId", channel.get("channelId"));
					List<Map<String, Object>> statusDataList = statsMasterDao
							.getSearchList("channelRealtime.getChannelStatusGraphData", sqlParams);

					Map<String, Object> channelInfo = new HashMap<String, Object>();
					channelInfo.putAll(channel);
					channelInfo.put("list", statusDataList);

					channelQualityList.add(channelInfo);
				}
				long time4 = System.currentTimeMillis();
				System.out.println("##### service query2 time #####" + (time4 - time3));

				return buildChannelChartData(channelQualityList, startTime, endTime);
			} else {
				return new ArrayList<Map<String, Object>>();
			}

		} else {
			return new ArrayList<Map<String, Object>>();
		}
	}

	private List<Map<String, Object>> buildChannelChartData(List<Map<String, Object>> dataList, String startTimeStr,
			String endTimeStr) {
		long start = System.currentTimeMillis();

		DateTime startTime = UcpaasDateUtils.parseDate(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		DateTime endTime = UcpaasDateUtils.parseDate(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		long startTimeOfMills = startTime.getMillis() + 300000;
		long endTimeOfMills = endTime.getMillis();

		for (Map<String, Object> channelMap : dataList) {
			List<Map<String, Object>> timeList = (List<Map<String, Object>>) channelMap.get("list");
			List<Map<String, Object>> timeListTemp = new ArrayList<Map<String, Object>>();
			int size = timeList.size();
			int index = 0;
			Map<String, Object> data = null;
			long datatime = 0;
			for (long i = startTimeOfMills; i <= endTimeOfMills; i += 300000) {
				if (datatime == 0 && index < size) {
					data = timeList.get(index++);
					datatime = Long.parseLong(data.get("dataTime").toString());
				}
				if (i == datatime) {
					data.put("dataTime", new DateTime(datatime).toString("MM-dd HH:mm"));
					timeListTemp.add(data);
					data = null;
					datatime = 0;
				} else {
					Map<String, Object> defaultData = new HashMap<String, Object>();
					defaultData.put("dataTime", new DateTime(i).toString("MM-dd HH:mm"));
					defaultData.put("status", -1);
					defaultData.put("desp", "空闲");

					timeListTemp.add(defaultData);
				}
			}
			channelMap.put("list", timeListTemp);

		}
		logger.debug("【构建通道质量图谱数据时长】= {}ms", System.currentTimeMillis() - start);
		return dataList;
	}

	@Override
	public PageContainer getChannelQualityTableData(Map<String, String> params) {
		// String timeType = params.get("timeType");
		//
		// String startTime = (String)
		// getStartAndEndTime(timeType).get("startTime");
		// String endTime = (String)
		// getStartAndEndTime(timeType).get("endTime");
		DateTime now = new DateTime();
		String date = now.toString("yyyyMMdd");
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		// date = "20161221";
		sqlParam.put("date", date);
		String maxDataTimeStr = statsMasterDao.getOneInfo("channelRealtime.getChannelIndexesMaxDateTime", sqlParam);
		DateTime maxDataTime;
		if (StringUtils.isBlank(maxDataTimeStr)) {
			maxDataTime = null;
		} else {
			maxDataTime = UcpaasDateUtils.parseDate(maxDataTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		String timeType = Objects.toString(params.get("timeType"), "0");
		Map<String, String> timeMap = getStartAndEndTime2(timeType, maxDataTime);
		String startTime = timeMap.get("startTime");
		String endTime = timeMap.get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_channel_indexes_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			String channelIds = Objects.toString(params.get("channelId"), "");

			sqlParams.put("tableList", tableList);
			sqlParams.put("startTime", startTime);
			sqlParams.put("endTime", endTime);
			if (StringUtils.isNotBlank(channelIds)) {
				List<String> channelList = Arrays.asList(channelIds.split(","));
				sqlParams.put("channelList", channelList);
			}

			sqlParams.putAll(params);

			// 获得通道状态的统计数据
			PageContainer pageContainer = new PageContainer();
			pageContainer.setPageRowCount(10);// 默认每页10条数据
			return statsMasterDao.getSearchPage("channelRealtime.getChannelQualityTableData",
					"getChannelQualityTableDataCount", sqlParams, pageContainer);

		} else {
			return new PageContainer();
		}
	}

	@Override
	public Map<String, Object> getChannelErrorDataById(String channelId, String timeType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_channel_error_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			params.put("channelId", channelId);
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得通道错误统计数据
			List<Map<String, Object>> channelErrorMapList = statsMasterDao
					.getSearchList("channelRealtime.getChannelErrorDataById", params);

			List<Map<String, Object>> pieDataList = new ArrayList<Map<String, Object>>();
			List<String> legendList = new ArrayList<String>();
			if (null != channelErrorMapList && channelErrorMapList.size() > 0) {
				// 构建EChart pie显示数据
				for (Map<String, Object> map : channelErrorMapList) {
					Map<String, Object> pieMap = new HashMap<String, Object>();
					legendList.add(map.get("error_code").toString());
					pieMap.put("name", map.get("error_code").toString());
					pieMap.put("value", map.get("error_num"));
					pieDataList.add(pieMap);
				}

			} else {
				Map<String, Object> pieMap = new HashMap<String, Object>();
				legendList.add("error_code");
				pieMap.put("name", "error_code");
				pieMap.put("value", 0);
				pieDataList.add(pieMap);
			}
			Map<String, Object> resultList = new HashMap<String, Object>();
			resultList.put("legendList", legendList);
			resultList.put("pieDataList", pieDataList);
			return resultList;

		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public PageContainer getClientQualityTableData(Map<String, String> params) {
		String timeType = params.get("timeType");
		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_client_indexes_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			sqlParams.put("tableList", tableList);
			sqlParams.put("startTime", startTime);
			sqlParams.put("endTime", endTime);

			String clientIds = Objects.toString(params.get("clientId"), "");
			if (StringUtils.isNotBlank(clientIds)) {
				List<String> clientIdList = Arrays.asList(clientIds.split(","));
				sqlParams.put("clientIdList", clientIdList);
			}
			sqlParams.putAll(params);

			// 获得通道状态的统计数据
			PageContainer pageContainer = new PageContainer();
			pageContainer.setPageRowCount(10);// 默认每页10条数据
			return statsMasterDao.getSearchPage("channelRealtime.getClientQualityTableData",
					"getClientQualityTableDataCount", sqlParams, pageContainer);

		} else {
			return new PageContainer();
		}
	}

	@Override
	public Map<String, Object> getChannelReportRespStackData(String channelId, String timeType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_channel_indexes_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			params.put("channelId", channelId);
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得通道回执率、应答率堆积图的统计数据
			List<Map<String, Object>> channelReportStackDataMap = statsMasterDao
					.getSearchList("channelRealtime.getChannelReportRespStackData", params);

			// 构建EChart显示数据
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<Object> report1 = new ArrayList<Object>();
			List<Object> report2 = new ArrayList<Object>();
			List<Object> report3 = new ArrayList<Object>();
			List<Object> report4 = new ArrayList<Object>();
			List<Object> report5 = new ArrayList<Object>();
			List<Object> report6 = new ArrayList<Object>();
			List<Object> resp1 = new ArrayList<Object>();
			List<Object> resp2 = new ArrayList<Object>();
			List<Object> resp3 = new ArrayList<Object>();
			List<Object> resp4 = new ArrayList<Object>();
			List<Object> resp5 = new ArrayList<Object>();
			List<Object> resp6 = new ArrayList<Object>();
			List<Object> sccessRate = new ArrayList<Object>();
			List<Object> sendTotal = new ArrayList<Object>();
			List<Object> xAxisData = new ArrayList<Object>();
			for (Map<String, Object> map : channelReportStackDataMap) {
				xAxisData.add(map.get("data_time"));
				report1.add(map.get("report1"));
				report2.add(map.get("report2"));
				report3.add(map.get("report3"));
				report4.add(map.get("report4"));
				report5.add(map.get("report5"));
				report6.add(map.get("report6"));
				resp1.add(map.get("resp1"));
				resp2.add(map.get("resp2"));
				resp3.add(map.get("resp3"));
				resp4.add(map.get("resp4"));
				resp5.add(map.get("resp5"));
				resp6.add(map.get("resp6"));
				sccessRate.add(map.get("sccuss_rate"));
				sendTotal.add(map.get("send_total_num"));
			}
			resultMap.put("xAxisData", xAxisData);
			resultMap.put("report1", report1);
			resultMap.put("report2", report2);
			resultMap.put("report3", report3);
			resultMap.put("report4", report4);
			resultMap.put("report5", report5);
			resultMap.put("report6", report6);
			resultMap.put("resp1", resp1);
			resultMap.put("resp2", resp2);
			resultMap.put("resp3", resp3);
			resultMap.put("resp4", resp4);
			resultMap.put("resp5", resp5);
			resultMap.put("resp6", resp6);
			resultMap.put("sccessRate", sccessRate);
			resultMap.put("sendTotal", sendTotal);
			return resultMap;

		} else {
			return new HashMap<String, Object>();
		}
	}

	@Override
	public List<Map<String, Object>> queryChannelQualityConfig() {
		return statsMasterDao.getSearchList("channelRealtime.queryChannelQualityConfig", null);
	}

	@Override
	public Map<String, Object> channelQualityConfigUpdate(Map<String, String> params) {
		String name = params.get("name");
		String value = params.get("value");

		List<String> list = Arrays.asList(name.split(","));

		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("data_type", list.get(0));
		sqlParams.put("clonum_name", list.get(1));
		sqlParams.put("value", value);

		statsMasterDao.update("channelRealtime.updateChannelQualityConfig", sqlParams);
		return null;
	}

	@Override
	public Map<String, Object> getClientReportRespStackData(String clientId, String timeType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime startDateTime = formatter.parseDateTime(startTime);
		DateTime endDateTime = formatter.parseDateTime(endTime);

		startDateTime = cleanTimeFor5MinuteBegin(startDateTime);
		endDateTime = cleanTimeFor5MinuteEnd(endDateTime);

		List<String> timeList = new ArrayList<>();
		while (startDateTime.isBefore(endDateTime.getMillis())) {
			timeList.add(startDateTime.toString("MM/dd HH:mm"));
			startDateTime = startDateTime.plusMinutes(5);
		}
		timeList.add(startDateTime.toString("MM/dd HH:mm"));

		// 获得统计数据表
		List<String> tableList = commonService.getExistTable(DbType.ucpaas_message_stats_master,
				TablePrefix.t_sms_client_indexes_stat_, startTime, endTime);

		if (tableList.size() > 0) {
			params.put("clientId", clientId);
			params.put("tableList", tableList);
			params.put("startTime", startTime);
			params.put("endTime", endTime);

			// 获得通道回执率、应答率堆积图的统计数据
			List<Map<String, Object>> clientReportStackDataMap = statsMasterDao
					.getSearchList("channelRealtime.getClientReportRespStackData", params);

			// 构建EChart显示数据
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<Object> report1List = new ArrayList<Object>();
			List<Object> report2List = new ArrayList<Object>();
			List<Object> report3List = new ArrayList<Object>();
			List<Object> report4List = new ArrayList<Object>();
			List<Object> report5List = new ArrayList<Object>();
			List<Object> report6List = new ArrayList<Object>();
			// List<Object> report7 = new ArrayList<Object>();
			List<Object> delay1List = new ArrayList<Object>();
			List<Object> delay2List = new ArrayList<Object>();
			List<Object> delay3List = new ArrayList<Object>();
			List<Object> delay4List = new ArrayList<Object>();
			List<Object> delay5List = new ArrayList<Object>();
			List<Object> sendTotalList = new ArrayList<Object>();
			List<Object> xAxisData = new ArrayList<Object>();

			List<ClientStackData> scdList = new ArrayList<>();
			for (Map<String, Object> map : clientReportStackDataMap) {
				ClientStackData scd = new ClientStackData();
				scd.setDataTime((String) map.get("data_time"));
				BigDecimal report1 = map.get("report1") != null ? (BigDecimal) map.get("report1") : BigDecimal.ZERO;
				scd.setReport1(report1.toString());
				BigDecimal report2 = map.get("report2") != null ? (BigDecimal) map.get("report2") : BigDecimal.ZERO;
				scd.setReport2(report2.toString());
				BigDecimal report3 = map.get("report3") != null ? (BigDecimal) map.get("report3") : BigDecimal.ZERO;
				scd.setReport3(report3.toString());
				BigDecimal report4 = map.get("report4") != null ? (BigDecimal) map.get("report4") : BigDecimal.ZERO;
				scd.setReport4(report4.toString());
				BigDecimal report5 = map.get("report5") != null ? (BigDecimal) map.get("report5") : BigDecimal.ZERO;
				scd.setReport5(report5.toString());
				BigDecimal report6 = map.get("report6") != null ? (BigDecimal) map.get("report6") : BigDecimal.ZERO;
				scd.setReport6(report6.toString());

				BigDecimal delay1 = map.get("delay1") != null ? (BigDecimal) map.get("delay1") : BigDecimal.ZERO;
				scd.setDelay1(delay1.toString());
				BigDecimal delay2 = map.get("delay2") != null ? (BigDecimal) map.get("delay2") : BigDecimal.ZERO;
				scd.setDelay2(delay2.toString());
				BigDecimal delay3 = map.get("delay3") != null ? (BigDecimal) map.get("delay3") : BigDecimal.ZERO;
				scd.setDelay3(delay3.toString());
				BigDecimal delay4 = map.get("delay4") != null ? (BigDecimal) map.get("delay4") : BigDecimal.ZERO;
				scd.setDelay4(delay4.toString());
				BigDecimal delay5 = map.get("delay5") != null ? (BigDecimal) map.get("delay5") : BigDecimal.ZERO;
				scd.setDelay5(delay5.toString());

				BigDecimal stn = map.get("send_total_num") != null ? (BigDecimal) map.get("send_total_num")
						: BigDecimal.ZERO;
				scd.setSendTotalNum(stn.toString());
				scdList.add(scd);

				String dateTime = (String) map.get("data_time");
				if (dateTime != null) {
					timeList.remove(dateTime);
				}
			}

			// 添加数据为0的时间段数据
			for (String dateTime : timeList) {

				ClientStackData scd = new ClientStackData();
				scd.setDataTime(dateTime);
				scd.setReport1("0");
				scd.setReport2("0");
				scd.setReport3("0");
				scd.setReport4("0");
				scd.setReport5("0");
				scd.setReport6("0");
				scd.setDelay1("0");
				scd.setDelay2("0");
				scd.setDelay3("0");
				scd.setDelay4("0");
				scd.setDelay5("0");
				scd.setSendTotalNum("0");
				scdList.add(scd);

			}

			Map<String, ClientStackData> csdTree = new TreeMap<String, ClientStackData>();
			for (ClientStackData csd : scdList) {
				csdTree.put(csd.getDataTime(), csd);
			}

			for (Entry<String, ClientStackData> entry : csdTree.entrySet()) {

				ClientStackData csd = entry.getValue();
				xAxisData.add(csd.getDataTime());
				report1List.add(csd.getReport1());
				report2List.add(csd.getReport2());
				report3List.add(csd.getReport3());
				report4List.add(csd.getReport4());
				report5List.add(csd.getReport5());
				report6List.add(csd.getReport6());
				delay1List.add(csd.getDelay1());
				delay2List.add(csd.getDelay2());
				delay3List.add(csd.getDelay3());
				delay4List.add(csd.getDelay4());
				delay5List.add(csd.getDelay5());
				sendTotalList.add(csd.getSendTotalNum());
			}

			resultMap.put("xAxisData", xAxisData);
			resultMap.put("report1", report1List);
			resultMap.put("report2", report2List);
			resultMap.put("report3", report3List);
			resultMap.put("report4", report4List);
			resultMap.put("report5", report5List);
			resultMap.put("report6", report6List);
			// resultMap.put("report7", report7);
			resultMap.put("delay1", delay1List);
			resultMap.put("delay2", delay2List);
			resultMap.put("delay3", delay3List);
			resultMap.put("delay4", delay4List);
			resultMap.put("delay5", delay5List);
			resultMap.put("sendTotal", sendTotalList);
			return resultMap;

		} else {
			return new HashMap<String, Object>();
		}
	}

	/**
	 * 格式化时间，去掉5分为单位后的时间，累加，如 2017-02-28 16:03 --> 2017-02-28 16:05 2017-02-28
	 * 13:00 --> 2017-02-28 13:00
	 * 
	 * @param startDateTime
	 * @return
	 */
	private DateTime cleanTimeFor5MinuteBegin(DateTime startDateTime) {
		long sdt = startDateTime.getMillis();
		long yusu = sdt % 300000L;
		if (yusu > 0) {
			sdt = sdt - yusu;
			sdt = sdt + 300000L;
		}
		startDateTime = new DateTime(sdt);
		return startDateTime;
	}

	/**
	 * 格式化时间，去掉5分为单位后的时间，减少，如 2017-02-28 16:03 --> 2017-02-28 16:00 2017-02-28
	 * 13:00 --> 2017-02-28 13:00
	 * 
	 * @param endDateTime
	 * @return
	 */
	private DateTime cleanTimeFor5MinuteEnd(DateTime endDateTime) {
		long sdt = endDateTime.getMillis();
		long yusu = sdt % 300000L;
		sdt = sdt - yusu;
		endDateTime = new DateTime(sdt);
		return endDateTime;
	}

	@Override
	public Map<String, Object> getClientSuccessRateStackData(String clientId, String timeType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String startTime = (String) getStartAndEndTime(timeType).get("startTime");
		String endTime = (String) getStartAndEndTime(timeType).get("endTime");

		params.put("clientId", clientId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		// 获得用户回执率、应答率堆积图的统计数据
		List<ClientSuccessRateRealtime> cssrs = queryAllClientSuccessRateStackData(clientId, startTime, endTime);

		// 构建EChart显示数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Object> xAxisData = new ArrayList<Object>();

		List<Object> sendTotal = new ArrayList<Object>();
		List<Object> interceptTotal = new ArrayList<Object>();
		List<Object> successRate = new ArrayList<Object>();
		List<Object> fakeSuccessRate = new ArrayList<Object>();
		List<Object> reallyFailRate = new ArrayList<Object>();
		for (ClientSuccessRateRealtime cssr : cssrs) {
			xAxisData.add(DateUtil.dateToStr(cssr.getDataTime(), "MM/dd HH:mm"));

			sendTotal.add(cssr.getSendTotal());
			interceptTotal.add(cssr.getInterceptTotal());
			successRate.add(cssr.getSuccessRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			fakeSuccessRate.add(cssr.getFakeSuccessRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			reallyFailRate.add(cssr.getReallyFailRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

		}
		resultMap.put("xAxisData", xAxisData);
		resultMap.put("sendTotal", sendTotal);
		resultMap.put("interceptTotal", interceptTotal);
		resultMap.put("successRate", successRate);
		resultMap.put("fakeSuccessRate", fakeSuccessRate);
		resultMap.put("reallyFailRate", reallyFailRate);
		return resultMap;
	}

	@Override
	public Map<String, Object> getChannelSuccessRateStackData(String channelId, String startTime, String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channelId", channelId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		// 获得通洞啊回执率、应答率堆积图的统计数据
		List<ChannelSuccessRateRealtime> cssrs = queryAllChannelSuccessRateStackData(channelId, startTime, endTime);

		// 构建EChart显示数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Object> xAxisData = new ArrayList<Object>();

		List<Object> sendTotal = new ArrayList<Object>(); // 发送总量
		List<Object> submitFail = new ArrayList<Object>(); // 提交失败总量
		List<Object> sendFail = new ArrayList<Object>(); // 发送失败总量
		List<Object> successRate = new ArrayList<Object>();
		List<Object> fakeSuccessRate = new ArrayList<Object>();
		List<Object> reallyFailRate = new ArrayList<Object>();
		for (ChannelSuccessRateRealtime cssr : cssrs) {
			xAxisData.add(DateUtil.dateToStr(cssr.getDataTime(), "MM/dd HH:mm"));
			sendTotal.add(cssr.getSendTotal());
			submitFail.add(cssr.getSubmitFail());
			sendFail.add(cssr.getSendFail());
			successRate.add(cssr.getSuccessRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			fakeSuccessRate.add(cssr.getFakeSuccessRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			reallyFailRate.add(cssr.getReallyFailRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

		}
		resultMap.put("xAxisData", xAxisData);
		resultMap.put("sendTotal", sendTotal);
		resultMap.put("submitFail", submitFail);
		resultMap.put("sendFail", sendFail);
		resultMap.put("successRate", successRate);
		resultMap.put("fakeSuccessRate", fakeSuccessRate);
		resultMap.put("reallyFailRate", reallyFailRate);
		return resultMap;
	}

	@Override
	public List<ChannelSuccessRateRealtime> queryAllChannelSuccessRateStackData(String channelId, String startTime,
			String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channelId", channelId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		// 获得通洞啊回执率、应答率堆积图的统计数据
		List<ChannelSuccessRateRealtime> cssrs = statsMasterDao
				.queryAll("channelRealtime.getChannelSuccessRateStackData", params);
		return cssrs;
	}

	@Override
	public List<ClientSuccessRateRealtime> queryAllClientSuccessRateStackData(String clientId, String startTime,
			String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("clientId", clientId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		// 获得用户回执率、应答率堆积图的统计数据
		List<ClientSuccessRateRealtime> cssrs = statsMasterDao.queryAll("channelRealtime.getClientSuccessRateStackData",
				params);

		return cssrs;

	}

}
