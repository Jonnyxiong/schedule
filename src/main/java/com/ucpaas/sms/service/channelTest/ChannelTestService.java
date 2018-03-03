package com.ucpaas.sms.service.channelTest;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 通道测试管理
 */
public interface ChannelTestService {

	/**
	 * 通道测试-列表页面
	 * @param params
	 * @return
	 */
	PageContainer channelTestQuery(Map<String, String> params);
	
	/**
	 * 通道测试-编辑页面
	 * @param id
	 * @return
	 */
	Map<String, Object> channelTestEdit(String id);
	
	/**
	 * 通道测试-保存测试通道
	 * @param params
	 * @return
	 */
	Map<String, Object> channelTestSave(Map<String, String> params);
	
	/**
	 * 分页查询模板信息
	 * @param params
	 * @return
	 */
	PageContainer queryTemplatePaging(Map<String, String> params);
	
	/**
	 * 根据模板ID查询模板信息
	 * @param params
	 * @return
	 */
	Map<String, Object> queryTemplateById(String templateId);
	
	/**
	 * 根据模板ID更新模板信息
	 * @param params
	 * @return
	 */
	Map<String, Object> updateTemplateById(Map<String, String> params);
	
	/**
	 * 根据模板ID删除模板
	 * @param params
	 * @return
	 */
	Map<String, Object> delTemplateById(Map<String, String> params);
	
	/**
	 * 添加模板
	 * @param params
	 * @return
	 */
	Map<String, Object> insertTemplate(Map<String, String> params);
	
	/**
	 * 查询模板信息用于显示下拉框
	 * @return
	 */
	List<Map<String, Object>> queryTestTeamplate4Select();
	
	
	/**
	 * 分页查询测试号码
	 * @param params
	 * @return
	 */
	PageContainer queryTestPhonePaging(Map<String, String> params);
	
	
	/**
	 * 查询测试报告
	 * @param params
	 * @return
	 */
	Map<String, Object> deleteTestPhone(String phone);
	
	/**
	 * excel导入手机号码
	 * @param upload
	 * @param uploadContentType
	 * @return
	 */
	Map<String, Object> importTestPhone(File upload, String uploadContentType);
	
	
	/**
	 * 启动通道测试
	 * @param 通道智能测试参数
	 * @return
	 */
	Map<String, Object> channelTestRequest(Map<String, String> params);
	
	/**
	 * 生成测试报告
	 * @param shareId分享单号
	 * @return
	 */
	Map<String, Object> generateSmartTestReport(String shareId);
	
	/**
	 * 查看分享单号是否已经提交过测试，用于生成测试报告前的检查
	 * @param shareId分享单号
	 * @return
	 */
	Map<String, Object> queryTestLogNotGenerateReport(String shareId);
	
	/**
	 * 生成测试报告并返回测试报告详情
	 * @param shareId
	 * @return
	 */
	Map<String, Object> generateTestReportAndReturn(String shareId);
	
	/**
	 * 根据测试报告Id查询测试报告
	 * @param channelId
	 * @param reportId
	 * @return
	 */
	Map<String, Object> queryTestReportByReportId(String channelId, String reportId);
	
	/**
	 * 查询测试报告中Echart数据，用于展示短信状态分布、错误码、回执率、应答率
	 * @param shareId分享单号
	 * @return
	 */
	Map<String, Object> queryTestReportEchartDataByReportId(String reportId);
	
	
	/**
	 * 录入测试报告
	 * @param shareId分享单号
	 * @return
	 */
	Map<String, Object> insertSmartTestReport(Map<String, String> params);
	
	
	/**
	 * 分页查询查询测试短信发送记录
	 * @param params
	 * @return
	 */
	PageContainer queryTestSendRecord(Map<String, String> params);
	
	
	/**
	 * 分页查询查询测试上行短信记录
	 * @param params
	 * @return
	 */
	PageContainer queryTestUpstreamRecord(Map<String, String> params);

	Map<String, Object> generateTestReportWithOutTest(String channelId);

	Map<String, Object> queryReportIdByChannelId(String channelId);

	Map<String, Object> updateChannelTestStatus(Map<String, String> params);

	Map<String, Object> updateChannelStatus(String channelId, String state);

	/**
	 * 添加测试号码
	 * @param param
	 * @return
	 */
	Map<String, Object> addTestPhone(Map<String, String> param);

	
	Map<String, Object> getOneTestPhone(String id);

}
