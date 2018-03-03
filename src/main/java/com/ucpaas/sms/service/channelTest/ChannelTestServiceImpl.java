package com.ucpaas.sms.service.channelTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.Constant;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.dao.MessageTestMasterDao;
import com.ucpaas.sms.exception.SmspBusinessException;
import com.ucpaas.sms.model.AccessRequest;
import com.ucpaas.sms.model.AccessResponse;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.HttpUtils;
import com.ucpaas.sms.util.JsonUtils;
import com.ucpaas.sms.util.RegexUtils;
import com.ucpaas.sms.util.StrUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;

@Service
@Transactional
public class ChannelTestServiceImpl implements ChannelTestService {
	
	@Autowired
	private MessageTestMasterDao messageTestMasterDao;
	
	@Autowired
	private MessageMasterDao messageMasterDao;
	
	private static final Logger logger = LoggerFactory.getLogger(ChannelTestServiceImpl.class);
	
	
	@Override
	public PageContainer channelTestQuery(Map<String, String> params) {
		return messageTestMasterDao.getSearchPage("channelTest.channelTestQuery", "channelTest.channelTestQueryCount", params);
	}
	
	@Override
	public Map<String, Object> channelTestEdit(String id) {
		return messageTestMasterDao.getOneInfo("channelTest.channelTestView", id);
	}

	@Override
	public Map<String, Object> channelTestSave(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String channelName = params.get("channel_name");
		String channelId = params.get("id");
		
		int num = messageTestMasterDao.getOneInfo("channelTest.checkSave", params);
		if(num > 0){
			result.put("result", "fail");
			result.put("msg", "通道名称已经存在，请重新输入");
			return result;
		}
		
		if(StringUtils.isBlank(channelId)){
			num = messageTestMasterDao.insert("channelTest.save", params);
		}else{
			params.put("state", "0"); // 只要是编辑都会把通道状态变为“测试中”
			num = messageTestMasterDao.update("channelTest.update", params);
		}
		
		if(num > 0){
			result.put("result", "fail");
			result.put("msg", "保存成功");
		}else{
			result.put("result", "fail");
			result.put("msg", "保存失败");
		}
		
		return result;
	}

	@Override
	public PageContainer queryTemplatePaging(Map<String, String> params) {
		return messageTestMasterDao.getSearchPage("channelTest.queryTemplate", "channelTest.queryTemplateCount", params);
	}
	
	@Override
	public Map<String, Object> queryTemplateById(String templateId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> templateInfo = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(templateId)){
			templateInfo = messageTestMasterDao.getOneInfo("channelTest.getTemplateById", templateId);
		}
		
		if(null == templateInfo){
			result.put("result", "fail");
			result.put("msg", "根据模板ID查询结果为空");
		}else{
			result.put("result", "success");
			result.put("data", templateInfo);
		}
		return result;
	}
	
	@Override
	public Map<String, Object> updateTemplateById(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("admin_id", String.valueOf(AuthorityUtils.getLoginUserId()));
		int resNum = messageTestMasterDao.update("channelTest.updateTemplateById", params);
		if(resNum == 1){
			result.put("result", "success");
			result.put("msg", "保存成功");
		}else{
			result.put("result", "fail");
			result.put("msg", "保存失败");
		}
		return result;
	}

	@Override
	public Map<String, Object> delTemplateById(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int delNum = messageTestMasterDao.delete("channelTest.delTemplateById", params);
		if(delNum == 1){
			result.put("result", "success");
			result.put("msg", "成功删除记录");
		}else{
			result.put("result", "fail");
			result.put("msg", "删除记录失败");
		}
		return result;
	}

	@Override
	public Map<String, Object> insertTemplate(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int saveNum;
		
		Map<String, Object> checkInfo = messageTestMasterDao.getOneInfo("channelTest.checkTemplateName", params);
		if(null != checkInfo){
			data.put("result", "fail");
			data.put("msg", "模板名称已经存在");
			return data;
		}
		
		params.put("admin_id", String.valueOf(AuthorityUtils.getLoginUserId()));
		saveNum = messageTestMasterDao.insert("channelTest.insertTemplate", params);
		
		if(saveNum == 1){
			data.put("result", "success");
			data.put("msg", "保存成功");
			return data;
		}else{
			data.put("result", "fail");
			data.put("msg", "保存失败");
			return data;
		}
	}
	
	@Override
	public List<Map<String, Object>> queryTestTeamplate4Select() {
		return messageTestMasterDao.getSearchList("channelTest.queryTestTeamplate4Select", null);
	}


	@Override
	public PageContainer queryTestPhonePaging(Map<String, String> params) {
		return messageTestMasterDao.getSearchPage("channelTest.queryTestPhone", "channelTest.queryTestPhoneCount", params);
	}


	@Override
	public Map<String, Object> deleteTestPhone(String id) {
		Map<String, Object> rersult = new HashMap<String, Object>();
		int delNum = messageTestMasterDao.delete("channelTest.deleteTestPhone", id);
		if(delNum == 1){
			rersult.put("result", "success");
			rersult.put("msg", "删除成功");
			return rersult;
		}else{
			rersult.put("result", "fail");
			rersult.put("msg", "删除失败");
			return rersult;
		}
	}
	
	@Override
	public Map<String, Object> importTestPhone(File upload, String uploadContentType) {
		Map<String, Object> data = new HashMap<String, Object>();
		String fileSavePath = ConfigUtils.save_path;
		String fileName = "批量导入通道测试手机号码.xls";
		//导入文件校验
		if(StringUtils.isBlank(uploadContentType) || !("application/vnd.ms-excel".equalsIgnoreCase(uploadContentType))){
			data.put("result", "fail");
			data.put("msg", "导入文件格式错误，目前支持.xls格式(97-2003)，请使用模板");
			return data;
		}
		
		if(upload.length() > 1148576L){
			data.put("result", "fail");
			data.put("msg", "您选择的文件大于1M,请将excel拆分后重新上传");
			return data;
		}
		
		//上传文件、取出数据、删除文件
		FileUtils.upload(fileSavePath, fileName, upload);
        List<List<String>> importDataList = ExcelUtils.importExcel(fileSavePath + "/" + fileName);
        FileUtils.delete(fileSavePath + "/" + fileName);
        List<List<String>> datalist = new ArrayList<>();
        for (int i = 0 ; i < importDataList.size() ; i++) {
        	List<String> temp = importDataList.get(i);
        	boolean isNotEmpty = false ;
        	for (String str : temp) {
        		if(!StringUtils.isEmpty(str)){
        			isNotEmpty = isNotEmpty | true ;
        		}
			}
        	if(isNotEmpty){
        		datalist.add(temp);
        	}
        }
        if(datalist.size() > 15000){
			data.put("result", "fail");
			data.put("msg", "您选择的excel中数据记录大于15000条，请您拆分后上传");
			return data;
		}
        
        String phone = null;
        String name = null ;
        String adminId = String.valueOf(AuthorityUtils.getLoginUserId());
        List<String> rowRecordList = new ArrayList<String>();
        // 获取Excel中合法的导入数据
        List<Map<String, Object>> legalList = new ArrayList<Map<String, Object>>();
        if(datalist != null && datalist.size() > 1){
        	for(int pos = 1; pos < datalist.size(); pos++){
        		rowRecordList = datalist.get(pos);//得到当前数据进行验证操作
        		if(rowRecordList != null && rowRecordList.size() > 0){
        			phone = null;
        			name = null;
        			try {
						phone = rowRecordList.get(0).trim();
					} catch (Exception e2) {
						phone = null;
					}
        			
        			try {
						name = rowRecordList.get(1).trim();
					} catch (Exception e2) {
						name = null ;
					}
						
					// 非法手机号码
					if(!RegexUtils.isMobile(phone) && !RegexUtils.isOverSeaMobile(phone)){
						continue;
					}
					// 非法联系人
					if(!StringUtils.isBlank(name) && name.length() > 50){
						continue;
					}
					
					Map<String, Object> map = new HashMap<>();
					
					map.put("phone", phone);
					map.put("name", name);
					map.put("operators_type", RegexUtils.getMobileOperator(phone));
					map.put("admin_id", adminId);
					legalList.add(map);
        		}
            }
        	
        }else{
        	data.put("result", "fail");
   		 	data.put("msg", "excel中没有数据");
   		 	return data;
        }
        
        List<String> allTestPhoneList = messageTestMasterDao.selectList("channelTest.queryAllTestPhone", null);
        Set<String> testPhoneInDb = new HashSet<String>(allTestPhoneList);
        
        // 过滤Excel中号码在DB中已经存在的部分
        List<Map<String, Object>> insertDataList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> phoneInfo : legalList) {
			String phoneTemp = (String) phoneInfo.get("phone");
			if(!testPhoneInDb.contains(phoneTemp)){
				insertDataList.add(phoneInfo);
			}
		}
        
        try {
        	int insertNum = 0;
        	if(insertDataList !=null && insertDataList.size() > 0){
        		insertNum = messageTestMasterDao.insert("channelTest.batchInsertTestPhone", insertDataList);
        	}
        	
        	
        	if(insertNum > 0){
        		int failNum = (datalist.size() - 1 ) - insertNum; // 需要减去列头
        		
        		if(failNum == 0){
        			data.put("msg", "导入成功");
        		}else{
        			data.put("msg", "部分导入成功，Excel中存在" + failNum + "条不合法或已存在记录");
        		}
        		data.put("result", "success");
        	}else{
        		data.put("result", "fail");
        		data.put("msg", "导入失败, Excel中所有手机号码不合法或已存在");
        	}
			
		} catch (Exception e) {
			data.put("result", "fail");
    		data.put("msg", "系统内部错误，请联系管理员");
		}
		return data;
	}

	/**
	 * 启动智能通道测试
	 * 
	 * @param 智能测试请求参数
	 * @return 请求结果
	 */
	@Override
	public Map<String, Object> channelTestRequest(Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> checkResult = checkShareTestPara(params);
		if(checkResult.get("result").equals("fail")){
			result.put("result", "fail");
			result.put("msg", checkResult.get("msg"));
			return result;
		}
		
		String templateId = params.get("templateId");
		String isNeedTimeStamp = Objects.toString(params.get("isNeedTimeStamp"), "0");
		String allMobile = params.get("testPhone");
		// roundOfTest对应页面的“测试条数”,默认“测试条数”为一条
		int roundOfTest = Integer.valueOf(Objects.toString(params.get("roundOfTest"), "1"));
		String templateContent = checkResult.get("templateContent");
		String templateSmsType = checkResult.get("templateSmsType");
		String channelId = params.get("channelId");
		String extend = params.get("extend");

		
		AccessRequest accessRequest = new AccessRequest();
		accessRequest.setClientid(ConfigUtils.smsp_test_clientid);
		accessRequest.setPassword(ConfigUtils.smsp_test_password);
		accessRequest.setSmstype(templateSmsType);
		accessRequest.setContent(templateContent);
		accessRequest.setUid(channelId);
		accessRequest.setExtend(extend);
		
		List<String> mobileGroupList = splitSmsMobile(allMobile);
		
		String accessResponseJson = "";
		AccessResponse accessResponse = new AccessResponse();
        logger.info("【通道智能测试】短信分组批量发送开始，分组数量 ={}", mobileGroupList.size());
        String channelTestRequestURL = messageTestMasterDao.getOneInfo("channelTest.queryChannelTestRequestURL", channelId);
        channelTestRequestURL = ConfigUtils.smsp_test_protocol + "://" + channelTestRequestURL + "/smsp/access/{clientid}/sendsms";
        channelTestRequestURL = channelTestRequestURL.replace("{clientid}", accessRequest.getClientid());
        
        boolean requestSuccess = true;
        for (int round = 0; round < roundOfTest; round ++){
        	int roudNum = round + 1;
        	for (int pos = 0; pos < mobileGroupList.size(); pos++) {
                int groupNum = pos + 1;
                
                // 判断是否添加时间戳
                if(isNeedTimeStamp.equals("1")){
                	accessRequest.setContent(templateContent + " / " + DateTime.now().toString("yyyy-MM-dd HH:mm:ss ") + RandomStringUtils.randomNumeric(4));
                }
                
                logger.info("----------- 第{}轮第{}组开始发送-----------", roudNum, groupNum);
                accessRequest.setMobile(mobileGroupList.get(pos));
                logger.info("-------------smsp_access_url----------{}", channelTestRequestURL);
                String data =  JsonUtils.toJson(accessRequest);
                
                // 线上https
                if (channelTestRequestURL.startsWith("https")) {
                    logger.debug("【通道智能测试】使用https协议请求短信接口,url={},data={}",channelTestRequestURL,data);
                    accessResponseJson = HttpUtils.httpPost(channelTestRequestURL,data, true);
                } else {
                    logger.debug("【通道智能测试】使用http协议请求短信接口,url={},data={}",channelTestRequestURL,data);
                    accessResponseJson = HttpUtils.httpPost(channelTestRequestURL, JsonUtils.toJson(accessRequest), false);
                }

                if (StringUtils.isBlank(accessResponseJson)) {
                    logger.info("【通道智能测试】第{}轮第{}组发送失败，请求SMSP无响应", roudNum, groupNum);
                    requestSuccess = false;
                    break;
                } else {
                    try {
                    	accessResponse = JsonUtils.toObject(accessResponseJson, AccessResponse.class);
                        logger.debug("----------- 第{}轮第{}组发送成功，accessResponse = {}", roudNum, groupNum, accessResponseJson);
                    } catch (Exception e) {
                        logger.error("【通道智能测试】SMSP后台返回JSON格式错误，accessResponse = {}", accessResponseJson);
                        throw new SmspBusinessException("请求SMSP-TEST的响应JSON格式错误");
                    }
                }
                
                if ( null == accessResponse ) {
                	requestSuccess = false;
                	break;
                }
            }
        }
        
        // 所有请求均有返回才算整个只能测试的请求成功
        if (requestSuccess) {
        	result.put("result", "success");
        	result.put("msg", "测试提交成功");
        	
        	// 写入智能测试提交记录
        	Map<String, Object> logParams = new HashMap<String, Object>();
        	logParams.put("channel_id", channelId);
        	logParams.put("test_num", roundOfTest);
        	logParams.put("template_id", templateId);
        	logParams.put("test_mobiles", allMobile);
        	int insertLogNum = messageTestMasterDao.insert("channelTest.insertChannelTestLog", logParams);
        	if(insertLogNum == 1){
        		logger.debug("【通道智能测试】插入智能测试提交记录成功");
        	}else{
        		logger.error("【通道智能测试】插入智能测试提交记录失败，提交记录信息 = {}", logParams);
        	}
        } else {
        	result.put("result", "fail");
        	result.put("msg", "请求SMSP-TEST组件失败，请联系系统管理员");
        }
		
		return result;
	}
	
	/**
	 * 校验并处理通道智能测试请求参数
	 * @param params
	 * @return
	 */
	private Map<String, String> checkShareTestPara(Map<String, String> params){
		Map<String, String> result = new HashMap<String, String>();
		
		// 检验模板是否存在
		Map<String, Object> testTeamplateMap = messageTestMasterDao.getOneInfo("channelTest.getTemplateById", params.get("templateId"));
		if(null == testTeamplateMap){
			result.put("result", "fail");
			result.put("msg", "测试模板不存在");
			return result;
		}
		
		// 检验模板参数是否为空
		String templateParams = params.get("templateParams");
		String [] templateParamsArray = {};
		
		// 校验模板参数与模板占位符个数是否匹配
		String templateContent = String.valueOf(testTeamplateMap.get("content"));
		Matcher match = Pattern.compile("(\\{.*?\\})").matcher(templateContent);
		int placeholderNum = 0;
		while (match.find()) {
    	   ++placeholderNum;
		}
		
		if(placeholderNum != 0){
			if(null != params.get("templateParams")){
				templateParamsArray = templateParams.split(",");
			}else{
				result.put("result", "fail");
				result.put("msg", "测试模板参数不能为空");
				return result;
			}
		}
		if(placeholderNum != templateParamsArray.length){
			result.put("result", "fail");
			result.put("msg", "测试模板中占位符与参数个数不匹配");
			return result;
		}
		
		// 将请求参数替换到通道测试短信模板中
		try {
			templateContent = fillTemplatePlaceHolder(templateContent, templateParamsArray);
		} catch (Exception e) {
			logger.error("【通道智能测试】将请求参数替换到通道测试短信模板时系统错误，templateContent = {}，templateParamsArray={}，异常={}", templateContent, templateParamsArray, e);
			result.put("result", "fail");
			result.put("msg", "系统错误，请联系管理员");
			return result;
		}
		
		// 将模板签名添加到短信内容中
		String signMode = params.get("signMode");
		String sign = Objects.toString(testTeamplateMap.get("sign"), "");
		
		// 短信内容中包含有中文的时候使用“【】”包裹签名  如果全部是英文则使用“[]” 
		if(StrUtils.isChinese(sign) || StrUtils.isChinese(templateContent)){
			sign = "【" + sign + "】";
		}else{
			sign = "[" + sign + "]";
		}
		
		// 显示签名的方式，0：不限制，1：前置，2：后置，3：不带签名
		if(signMode.equals("0") || signMode.equals("1")){
			templateContent = sign + templateContent;
		}else if(signMode.equals("2")){
			templateContent = templateContent + sign;
		}else{
			// 不带签名
		}
		result.put("templateContent", templateContent);
		
		if(templateContent.length() < 2){
			result.put("result", "fail");
			result.put("msg", "测试短信内容长度不能小于2");
			return result;
		}
		
		if(templateContent.length() > 670){
			result.put("result", "fail");
			result.put("msg", "测试短信内容长度不能大于670");
			return result;
		}
		
		// 获得请求短信类型
		result.put("templateSmsType", String.valueOf(testTeamplateMap.get("type")));
		
		if(null != params.get("testPhone") && StringUtils.isNotBlank(params.get("testPhone"))){
		}else{
			result.put("result", "fail");
			result.put("msg", "测试手机号码不能为空");
			return result;
		}
		
		/*if(null != params.get("roundOfTest") && StringUtils.isNotBlank(params.get("roundOfTest"))){
			result.put("result", "fail");
			result.put("msg", "测试条数不能为空");
			return result;
		}*/
		
		
		
		result.put("result", "success");
		return result;
	}
	
	/**
     * 将短信按每组100个进行分组（Access HTTPS 接口单次请求最多100个号码）
     * @param mobiles（用英文逗号分隔的手机号码）
     * @return 
     */
    private List<String> splitSmsMobile(String mobiles) {
        List<String> mobileList = new ArrayList<String>();

        String [] mobilesArray = mobiles.split(",");
        List<String> mobilesList = Arrays.asList(mobilesArray);
        List<String> temp = new ArrayList<String>();

        if (mobilesArray.length <= Constant.SMS_SEND_MAX_NUM) {
            mobileList.add(StringUtils.join(mobilesArray, ","));
            return mobileList;
        }else{
        	
        	for (int pos = 0; pos < mobilesList.size(); pos++) {
        		temp.add(mobilesList.get(pos));
        		
        		if (((pos + 1) % Constant.SMS_SEND_MAX_NUM == 0) || (pos == mobilesList.size() - 1)) {
        			mobileList.add(StringUtils.join(temp.toArray(), ","));
        			temp = new ArrayList<String>();
        		}
        	}
        	
        }

        return mobileList;
    }
    
    /**
     * 将参数替换到通道智能测试模板中
     * 
     * @param templateContent eg: 我在北京{1}看{2}
     * @param templateParams eg: ["天安门", "升旗"]
     * @return 我在北京天安门看升旗
     */
    private String fillTemplatePlaceHolder(String templateContent, String[] templateParams){
        Matcher m=Pattern.compile("\\{(\\d)\\}").matcher(templateContent);
        while(m.find()){
        	String matcherString = m.group(); // 匹配到的占位符
        	Integer paramPos = Integer.parseInt(m.group(1)); // 参数的位置
        	templateContent = templateContent.replace(matcherString, templateParams[paramPos - 1]);
        }
        return templateContent;
    }

    
    @Override
	public Map<String, Object> updateChannelTestStatus(Map<String, String> params) {
    	Map<String, Object> result = new HashMap<String, Object>();
		if(null == params || StringUtils.isBlank(params.get("state")) || StringUtils.isBlank(params.get("channel_id"))){
			result.put("result", "fail");
			result.put("msg", "请求参数非法，参数为空");
			logger.debug("更新用户分享单号失败：请求参数为空, params=" + params);
			return result;
		}
		
		// 将测试结论写到测试报告表中
		if(params.get("state").equals("3")){// 对接成功时表示测试通过
			params.put("is_test_paas", "1");
		}else if(params.get("state").equals("4")){// 对接失败表示测试不通过
			params.put("is_test_paas", "0");
		}
		
		int updateTestReportNum = messageTestMasterDao.update("channelTest.updateTestReport", params);
		if(updateTestReportNum == 1){
			logger.debug("更新测试报告结论和状态成功：" + params);
		}else{
			logger.debug("更新测试报告结论和状态失败：" + params);
		}
		
		int updateNum = messageTestMasterDao.update("channelTest.updateChannelTestStatus", params);
		if(updateNum == 1){
			result.put("result", "success");
			result.put("msg", "操作成功");
			
		}else{
			result.put("result", "fail");
			result.put("msg", "操作失败");
			logger.debug("更新用户分享单号失败：更新成功数量为0，params=" + params);
		}
		return result;
	}

	/**
     * 生成通道智能测试报告
     * @param channelId
     */
	@Override
	public Map<String, Object> generateSmartTestReport(String channelId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			// 1、生成测试报告ID
			String reportId = UUID.randomUUID().toString().replace("-", "");
			logger.debug("【通道智能测试】生成测试报告，channelId={}， reportId= {}", channelId, reportId);
			
			// 查询的分享单号信息
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("channelId", channelId);
			paramsMap.put("reportId", reportId);
			
			// 2、统计通道质量指数（短信各个状态的条数、回执时间分布、响应时间分布）
			Map<String, Object> indexsDataMap = messageTestMasterDao.getOneInfo("channelTest.getChannelTestIndexes", paramsMap);
			if(null != indexsDataMap){
				indexsDataMap.put("report_id", reportId);
				messageTestMasterDao.insert("channelTest.insertChannelTestIndexes", indexsDataMap);
			}else{
				logger.debug("【通道智能测试】生成测试报告， 查询流水表生成通道质量指数为空，channelId={}， reportId= {}", channelId, reportId);
			}
			
			// 3、统计通道错误码
			List<Map<String, Object>> errorDataList = messageTestMasterDao.getSearchList("channelTest.getChannelTestErrorData", paramsMap);
			if(null == errorDataList){
				logger.debug("【通道智能测试】生成测试报告， 查询流水表生成通道错误码信息为空，channelId={}， reportId= {}", channelId, reportId);
			}else{
				if(errorDataList.size() > 0){
					
					Map<String, Object> insertParams = new HashMap<String, Object>();
					insertParams.put("dataList", errorDataList);
					messageTestMasterDao.insert("channelTest.insertChannelTestErrorData", insertParams);
				}
			}
			
			// 4、查看通道登录状态
			Map<String, Object> loginStatusMap = messageTestMasterDao.getOneInfo("channelTest.queryChannelTestLoginStatus", paramsMap);
			
			if( null == loginStatusMap ){
				logger.debug("【通道智能测试】生成测试报告， 查询查看通道登录状态为空，channelId={}， reportId= {}", channelId, reportId);
				loginStatusMap = new HashMap<String, Object>();
				loginStatusMap.put("login_status", "0"); // 登陆失败
				loginStatusMap.put("login_desc", "生成智能测试报告时查询不到通道登陆状态"); // 登陆失败
			}
			
			// 5、是否支持状态报告
			int isSupportReport = messageTestMasterDao.getSearchSize("channelTest.queryIsSupportReport", paramsMap);
			
			// 6、 是否支持长短信
			int isSupportLongSms = messageTestMasterDao.getSearchSize("channelTest.queryIsSupportLongSms", paramsMap);
			
			// 7、是否支持上行
			int isSupportUpstream = messageTestMasterDao.getSearchSize("channelTest.queryIsSupportUpstream", paramsMap);
			
			
			// 8、保存测试报告
			Map<String, Object> smartTestReportMap = new HashMap<String, Object>();
			// 查询测试通道详情
			Map<String, Object> channelTestDetails = messageTestMasterDao.getOneInfo("channelTest.queryChannelTestDetailsByChannelId", paramsMap);
			smartTestReportMap.put("report_id", reportId);
			smartTestReportMap.put("channel_id", channelId);
			smartTestReportMap.putAll(loginStatusMap);
			smartTestReportMap.put("account", Objects.toString(channelTestDetails.get("account"), ""));
			smartTestReportMap.put("password", Objects.toString(channelTestDetails.get("password"), ""));
			smartTestReportMap.put("operators_type", Objects.toString(channelTestDetails.get("operators_type"), "1"));
			smartTestReportMap.put("access_id", Objects.toString(channelTestDetails.get("access_id"), ""));
			smartTestReportMap.put("sp_id", Objects.toString(channelTestDetails.get("sp_id"), ""));
			smartTestReportMap.put("service_id", Objects.toString(channelTestDetails.get("service_id"), ""));
			smartTestReportMap.put("mt_ip", Objects.toString(channelTestDetails.get("mt_ip"), ""));
			smartTestReportMap.put("mt_port", Objects.toString(channelTestDetails.get("mt_port"), "0"));
			smartTestReportMap.put("protocol_type", Objects.toString(channelTestDetails.get("protocol_type"), "3"));
			smartTestReportMap.put("extend_size", Objects.toString(channelTestDetails.get("extend_size"), "0"));
			smartTestReportMap.put("admin_id", AuthorityUtils.getLoginUserId());
			smartTestReportMap.put("is_report", isSupportReport);
			smartTestReportMap.put("is_long_sms", isSupportLongSms);
			smartTestReportMap.put("is_mo", isSupportUpstream);
			smartTestReportMap.put("report_type", 1);
			
			
			int insertNum = messageTestMasterDao.insert("channelTest.insertChannelTestReport", smartTestReportMap);
			if(insertNum == 1) {
				result.put("result", "success");
				result.put("report_id", reportId);
				result.put("msg", "测试报告生成成功");
			}else{
				result.put("result", "fail");
				result.put("msg", "测试报告生成失败");
				logger.error("【通道智能测试】生成测试报告保存时返回插入记录为 0");
				return result;
			}
			
			messageTestMasterDao.update("channelTest.updateChannelTestRecordReportId", paramsMap);
			// 更新 智能测试提交记录表（t_sms_cloud_test_log）中记录的reportId
			messageTestMasterDao.update("channelTest.updateChannelTestLogReportId", paramsMap);
			// 更新 智能测试短信发送记录表（t_sms_cloud_mt_yyyyMMdd）中记录的reportId
			messageTestMasterDao.update("channelTest.updateMtRecordReportId", paramsMap);
			// 更新 智能测试上行表（t_sms_cloud_mo） 中记录的reportId
			messageTestMasterDao.update("channelTest.updateMoRecordReportId", paramsMap);
			
		} catch (Exception e) {
			logger.error("【通道智能测试】生成测试报告时系统错误：" + e);
			throw new SmspBusinessException("测试报告生成时系统错误，请联系管理员");
		}
		
		return result;
	}
	
	
	@Override
	public Map<String, Object> queryTestLogNotGenerateReport(String channelId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询最后一次提交测试且还没有生成测试报告的测试记录（没有找到不代表没有进行过测试）
		String reportId = messageTestMasterDao.getOneInfo("channelTest.queryTestLogNotGenerateReport", channelId);
		
		// 提交测试记录表中没有记录
		if(StringUtils.isBlank(reportId)){
			// 没有找到没有记录
			result.put("result", "fail");
		}else{
			// 存在提交了测试但是没有用于生成测试报告的记录
			result.put("result", "success");
		}
		return result;
	}
	
	
	
	@Override
	public Map<String, Object> generateTestReportAndReturn(String channelId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		String reportId = "";
		Map<String, Object> generateReultMap = generateSmartTestReport(channelId);
		if(generateReultMap.get("result").equals("success")){
			reportId = String.valueOf(generateReultMap.get("report_id"));
		}else{
			// 生成智能测试报告失败
			return generateReultMap;
		}
		
		
		// 查询智能测试报告信息
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("reportId", reportId);
		sqlParam.put("channelId", channelId);
		Map<String, Object> smartTestReportMap = messageTestMasterDao.getOneInfo("channelTest.queryTestReportById", sqlParam);
		
		if(null == smartTestReportMap){
			smartTestReportMap = new HashMap<String, Object>();
			logger.debug("【通道智能测试】查询智能测试报告返回为空：reportId = {}" + reportId);
		}else{
			// 根据admin_id查询操作者名字
			String adminId = "";
			String adminName = "";
			adminId = Objects.toString(smartTestReportMap.get("admin_id"), "");
			adminName = messageMasterDao.getOneInfo("channelTest.getAdminNameById", adminId);
			smartTestReportMap.put("admin_name", Objects.toString(adminName, ""));
		}
		
		
		// 资源接入分享单信息
		Map<String, Object> channelInfo = messageTestMasterDao.getOneInfo("channelTest.queryChannelTestDetailsByChannelId", sqlParam); 
		result.put("reportId", reportId);
		result.put("reportMap", smartTestReportMap);
		result.put("channelInfo", channelInfo);
		
		return result;
	}

	/**
	 * 查询智能测试报告
	 * @param reportId
	 * @param reportId
	 * @return
	 */
	@Override
	public Map<String, Object> queryTestReportByReportId(String channelId, String reportId) {
		logger.debug("【通道智能测试】查询通道智能测试报告开始：reportId = {}" + reportId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 查询智能测试报告
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("reportId", reportId);
		Map<String, Object> smartTestReportMap = messageTestMasterDao.getOneInfo("channelTest.queryTestReportById", sqlParam);
		
		if(null == smartTestReportMap){
			smartTestReportMap = new HashMap<String, Object>();
			logger.debug("【通道智能测试】查询智能测试报告返回为空：reportId = {}" + reportId);
		}else{
			// 根据admin_id查询操作者名字
			String adminId = "";
			String adminName = "";
			adminId = Objects.toString(smartTestReportMap.get("admin_id"), "");
			adminName = messageMasterDao.getOneInfo("channelTest.getAdminNameById", adminId);
			smartTestReportMap.put("admin_name", Objects.toString(adminName, ""));
		}
		
		// 资源接入分享单信息
		sqlParam.put("channelId", channelId);
		Map<String, Object> channelInfo = messageTestMasterDao.getOneInfo("channelTest.queryChannelTestDetailsByChannelId", sqlParam);
		
		result.put("reportMap", smartTestReportMap);
		result.put("channelInfo", channelInfo);
		result.put("reportId", reportId); // 这个reportId需要传递给测试报告页面要用查询Echart中的展示数据
		
		
		logger.debug("【通道智能测试】查询通道智能测试报告成功：reportId = {}" + reportId);
		return result;
	}
	
	@Override
	public Map<String, Object> queryTestReportEchartDataByReportId(String reportId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 短信状态分布统计
		List<Map<String, Object>> smsStateList = getCloudSmsStateByReportId(reportId);
		
		// 回执分布统计
		List<Map<String, Object>> reportIndexesList = getReportIndexesByReportId(reportId);
		
		// 应答分布统计
		List<Map<String, Object>> respIndexesList = getRespIndexesByReportId(reportId);
		
		// 错误码统计
		Map<String, Object> cloudShareErrorMap = getChannelErrorByReportId(reportId);
		
		result.put("smsStateList", smsStateList);
		result.put("reportIndexesList", reportIndexesList);
		result.put("respIndexesList", respIndexesList);
		result.put("cloudShareErrorMap", cloudShareErrorMap);
		
		return result;
	}

	private Map<String, Object> getChannelErrorByReportId(String reportId){
		// 获得通道错误统计数据
		List<Map<String, Object>> channelErrorMapList = messageTestMasterDao.getSearchList("channelTest.queryChannelTestErrorByReportId", reportId);

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
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("legendList", legendList);
		resultMap.put("pieDataList", pieDataList);
		return resultMap;
	}
	
	private List<Map<String, Object>> getRespIndexesByReportId(String reportId){
		// 获得通道应答率的统计数据
		Map<String, Object> channelRespRateMap = messageTestMasterDao.getOneInfo("channelTest.getRespIndexesByReportId", reportId);

		Map<String, Object> resp1Map = new HashMap<String, Object>();
		Map<String, Object> resp2Map = new HashMap<String, Object>();
		Map<String, Object> resp3Map = new HashMap<String, Object>();
		Map<String, Object> resp4Map = new HashMap<String, Object>();
		Map<String, Object> resp5Map = new HashMap<String, Object>();

		if (null != channelRespRateMap) {
			// 构建EChart pie显示数据
			resp1Map.put("name", "0-2s");
			resp1Map.put("value", channelRespRateMap.get("resp_num_1"));
			resp2Map.put("name", "3-10s");
			resp2Map.put("value", channelRespRateMap.get("resp_num_2"));
			resp3Map.put("name", "11-300s");
			resp3Map.put("value", channelRespRateMap.get("resp_num_3"));
			resp4Map.put("name", ">300s");
			resp4Map.put("value", channelRespRateMap.get("resp_num_4"));
			resp5Map.put("name", "应答超时");
			resp5Map.put("value", channelRespRateMap.get("resp_num_5"));
		} else {
			// 默认数据
			resp1Map.put("name", "0-2s");
			resp1Map.put("value", 0);
			resp2Map.put("name", "3-10s");
			resp2Map.put("value", 0);
			resp3Map.put("name", "11-300s");
			resp3Map.put("value", 0);
			resp4Map.put("name", ">300s");
			resp4Map.put("value", 0);
			resp5Map.put("name", "应答超时");
			resp5Map.put("value", 0);
		}

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList.add(resp1Map);
		resultList.add(resp2Map);
		resultList.add(resp3Map);
		resultList.add(resp4Map);
		resultList.add(resp5Map);
		return resultList;
	}
	
	private List<Map<String, Object>> getReportIndexesByReportId(String reportId){
		// 获得通道回执率的统计数据
		Map<String, Object> reportIndexesMap = messageTestMasterDao.getOneInfo("channelTest.getReportIndexesByReportId", reportId);

		// 构建EChart pie显示数据
		Map<String, Object> report1Map = new HashMap<String, Object>();
		Map<String, Object> report2Map = new HashMap<String, Object>();
		Map<String, Object> report3Map = new HashMap<String, Object>();
		Map<String, Object> report4Map = new HashMap<String, Object>();
		Map<String, Object> report5Map = new HashMap<String, Object>();
		Map<String, Object> report6Map = new HashMap<String, Object>();
		Map<String, Object> report7Map = new HashMap<String, Object>();
		if (null != reportIndexesMap) {
			report1Map.put("name", "0-10s");
			report1Map.put("value", Objects.toString(reportIndexesMap.get("report_num_1"), "0"));
			report2Map.put("name", "11-30s");
			report2Map.put("value", reportIndexesMap.get("report_num_2"));
			report3Map.put("name", "31-60s");
			report3Map.put("value", reportIndexesMap.get("report_num_3"));
			report4Map.put("name", "61-120s");
			report4Map.put("value", reportIndexesMap.get("report_num_4"));
			report5Map.put("name", "121-300s");
			report5Map.put("value", reportIndexesMap.get("report_num_5"));
			report6Map.put("name", ">300s");
			report6Map.put("value", reportIndexesMap.get("report_num_6"));
			report7Map.put("name", "回执未返回");
			report7Map.put("value", reportIndexesMap.get("report_num_7"));

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
			report1Map.put("name", "0-5s");
			report1Map.put("value", 0);
			report2Map.put("name", "6-10s");
			report2Map.put("value", 0);
			report3Map.put("name", "11-30s");
			report3Map.put("value", 0);
			report4Map.put("name", "31-60s");
			report4Map.put("value", 0);
			report5Map.put("name", "61-300s");
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
	}
	
	private List<Map<String, Object>> getCloudSmsStateByReportId(String reportId){
		// 查询智能测试通道的通道质量指数统计
		Map<String, Object> smsStateMap = messageTestMasterDao.getOneInfo("channelTest.getCloudSmsStateByReportId", reportId);
		Map<String, Object> state0Map = new HashMap<String, Object>();
		Map<String, Object> state1Map = new HashMap<String, Object>();
		Map<String, Object> state2Map = new HashMap<String, Object>();
		Map<String, Object> state3Map = new HashMap<String, Object>();
		Map<String, Object> state4Map = new HashMap<String, Object>();
		Map<String, Object> state5Map = new HashMap<String, Object>();
		Map<String, Object> state6Map = new HashMap<String, Object>();
		if (null != smsStateMap) {
			// 构建EChart pie显示数据
			state0Map.put("name", "未发送");
			state0Map.put("value", smsStateMap.get("no_send_num"));
			state1Map.put("name", "提交成功");
			state1Map.put("value", smsStateMap.get("submit_success_num"));
			state2Map.put("name", "发送成功");
			state2Map.put("value", smsStateMap.get("subret_success_num"));
			state3Map.put("name", "明确成功");
			state3Map.put("value", smsStateMap.get("send_success_num"));
			state4Map.put("name", "提交失败");
			state4Map.put("value", smsStateMap.get("submit_failure_num"));
			state5Map.put("name", "发送失败");
			state5Map.put("value", smsStateMap.get("subret_failure_num"));
			state6Map.put("name", "明确失败");
			state6Map.put("value", smsStateMap.get("send_failure_num"));

		} else {
			// 默认数据
			state0Map.put("name", "未发送");
			state0Map.put("value", 0);
			state1Map.put("name", "提交成功");
			state1Map.put("value", 0);
			state2Map.put("name", "发送成功");
			state2Map.put("value", 0);
			state3Map.put("name", "明确成功");
			state3Map.put("value", 0);
			state4Map.put("name", "提交失败");
			state4Map.put("value", 0);
			state5Map.put("name", "发送失败");
			state5Map.put("value", 0);
			state6Map.put("name", "明确失败");
			state6Map.put("value", 0);
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList.add(state0Map);
		resultList.add(state1Map);
		resultList.add(state2Map);
		resultList.add(state3Map);
		resultList.add(state4Map);
		resultList.add(state5Map);
		resultList.add(state6Map);

		return resultList;
	}

	@Override
	public Map<String, Object> insertSmartTestReport(Map<String, String> params) {
		// insertSmartTestReport
		Map<String, Object> result = new HashMap<String, Object> ();
		int insertNum = messageTestMasterDao.insert("channelTest.insertSmartTestReport", params);
		if(insertNum == 1){
			result.put("result", "success");
			result.put("msg", "保存成功");
		}else{
			result.put("result", "fail");
			result.put("msg", "保存失败");
		}
		
		return result;
	}

	@Override
	public PageContainer queryTestSendRecord(Map<String, String> params) {
		return messageTestMasterDao.getSearchPage("channelTest.queryTestSendRecord", "channelTest.queryTestSendRecordCount", params);
	}

	@Override
	public PageContainer queryTestUpstreamRecord(Map<String, String> params) {
		return messageTestMasterDao.getSearchPage("channelTest.queryTestUpstreamRecord", "channelTest.queryTestUpstreamRecordCount", params);
	}

	@Override
	public Map<String, Object> generateTestReportWithOutTest(String channelId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			// 1、生成测试报告ID
			String reportId = UUID.randomUUID().toString().replace("-", "");
			logger.debug("【通道智能测试】生成测试报告，channelId={}， reportId= {}", channelId, reportId);
			
			// 查询的分享单号信息
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("channelId", channelId);
			paramsMap.put("reportId", reportId);
			
			// 查询分享单详情
			Map<String, Object> userShareDetails = messageTestMasterDao.getOneInfo("channelTest.queryChannelTestDetailsByChannelId", paramsMap);
			// 2、保存测试报告
			Map<String, Object> smartTestReportMap = new HashMap<String, Object>();
			smartTestReportMap.put("channel_id", channelId);
			smartTestReportMap.putAll(userShareDetails);
			smartTestReportMap.put("report_id", reportId);
			smartTestReportMap.put("login_status", "0"); // TODO
			smartTestReportMap.put("is_report", Objects.toString(userShareDetails.get("is_report"), "0"));
			smartTestReportMap.put("is_long_sms", Objects.toString(userShareDetails.get("is_long_sms"), "0"));
			smartTestReportMap.put("admin_id", AuthorityUtils.getLoginUserId());
			smartTestReportMap.put("report_type", 2);
			
			
			int insertNum = messageTestMasterDao.insert("channelTest.insertChannelTestReport", smartTestReportMap);
			if(insertNum == 1) {
				result.put("result", "success");
				result.put("report_id", reportId);
				result.put("msg", "测试报告生成成功");
			}else{
				result.put("result", "fail");
				result.put("msg", "测试报告生成失败");
				logger.error("【通道智能测试】生成测试报告保存时返回插入记录为 0");
				return result;
			}
			
			messageTestMasterDao.update("channelTest.updateChannelTestReportId", paramsMap);
			
		} catch (Exception e) {
			logger.error("【通道智能测试】生成测试报告时系统错误：" + e);
			throw new SmspBusinessException("测试报告生成时系统错误，请联系管理员");
		}
		return result;
	}

	@Override
	public Map<String, Object> queryReportIdByChannelId(String channelId) {
		Map<String, Object> result = new HashMap<String, Object>();
		String reportId = messageTestMasterDao.getOneInfo("channelTest.queryReportIdByChannelId", channelId);
		
		result.put("reportId", Objects.toString(reportId, ""));
		return result;
	}

	@Override
	public Map<String, Object> updateChannelStatus(String channelId, String state) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("channelId", channelId);
		sqlParam.put("state", state);
		sqlParam.put("adminId", AuthorityUtils.getLoginUserId());
		int num = messageTestMasterDao.update("channelTest.updateChannelStatus", sqlParam);
		
		if(num == 1){
			result.put("result", "success");
			result.put("msg", "操作成功");
		}else{
			result.put("result", "fail");
			result.put("msg", "操作失败或通道状态已经被改变");
		}
		
		return result;
	}

	@Override
	public Map<String, Object> addTestPhone(Map<String, String> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 查重
		int check = messageTestMasterDao.getOneInfo("channelTest.checkTestPhone", param);
		if(check > 0){
			result.put("result", "fail");
			result.put("msg", "保存失败，手机号码已经存在");
			return result;
		}
		
		String phone = param.get("phone");
		param.put("operators_type", RegexUtils.getMobileOperator(phone));
		param.put("admin_id", AuthorityUtils.getLoginUserId().toString());
		
		int saveNum = 0;
		if(StringUtils.isBlank(param.get("id"))){
			saveNum = messageTestMasterDao.insert("channelTest.insertTestPhone", param);
		}else{
			saveNum = messageTestMasterDao.insert("channelTest.updateTestPhone", param);
		}
		
		if(saveNum == 1){
			result.put("result", "success");
			result.put("msg", "保存成功");
		} 
		return result;
	}

	@Override
	public Map<String, Object> getOneTestPhone(String id) {
		return  messageTestMasterDao.getOneInfo("channelTest.getOneTestPhone", id);
	}

}
