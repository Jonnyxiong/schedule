package com.ucpaas.sms.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ucpaas.sms.enums.HttpProtocolType;
import com.ucpaas.sms.enums.NeedReportType;
import com.ucpaas.sms.enums.ProtocolType;
import com.ucpaas.sms.service.account.AccountService;
import com.ucpaas.sms.service.reportRepush.ReportRepushService;
import com.ucpaas.sms.util.encrypt.EncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

@Lazy(false)
@Component
public class ReportRepushUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("ReportRepushService");

    @Autowired
    private AccountService accountService;

    @Autowired
    private ReportRepushService reportRepushService;

    private static ReportRepushUtil reportRepushUtil;

    public static Integer reportRepushTaskNum = 0;
    public static AtomicInteger reportRepushCompleteTaskNum = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        reportRepushUtil = this;
        reportRepushUtil.accountService = this.accountService;
        reportRepushUtil.reportRepushService = this.reportRepushService;
    }

    /**
     * 重推状态报告
     * @param access
     * @return
     */
    public static boolean repush(String taskName, String tableName, boolean forceRepush, Map<String, Object> access) {


        String clientid = access.get("clientid").toString();
        String id = access.get("id").toString();

        Integer smsfrom = Integer.valueOf(access.get("smsfrom").toString());
        ProtocolType userProtocolTypeEnum = ProtocolType.getInstance(smsfrom);
        String state = access.get("state").toString();
        String report = ReportRepushUtil.getReportFromAccess(state, access);
        // 根据状态获得状态报告的状态码
        String reportStatus = ReportRepushUtil.getReportStatus(state);

        String reportdateStr = ReportRepushUtil.getReportDateStr(access);


        // 查询account信息
        Map<String, Object> account = reportRepushUtil.accountService.getOneByClientId(clientid);
        if(account == null){
            LOGGER.debug("【重推状态报告】查询用户{}account表信息失败, taskName={}", clientid, taskName);
            return false;
        }
        if(account.get("needreport") == null){
            LOGGER.debug("【重推状态报告】查询用户{}状态报告推送方式信息失败, taskName={}", clientid, taskName);
            return false;
        }
        if(account.get("http_protocol_type") == null){
            LOGGER.debug("【重推状态报告】查询用户{} Http协议类型信息失败, taskName={}", clientid, taskName);
            return false;
        }

        Integer needReportType = Integer.valueOf(account.get("needreport").toString());
        Integer httpProtocolType = Integer.valueOf(account.get("http_protocol_type").toString());

        NeedReportType userNeedReportTypeEnum = NeedReportType.getInstance(needReportType);
        HttpProtocolType userHttpProtocolTypeEnum = HttpProtocolType.getInstance(httpProtocolType);

        StringBuilder  reportStr = new StringBuilder ();

        try {

            // 生成标准协议状态报告内容
            if(ProtocolType.SMPP.equals(userProtocolTypeEnum) || ProtocolType.CMPP.getValue().equals(smsfrom)
                    || ProtocolType.SGIP.getValue().equals(smsfrom) || ProtocolType.SGMP.getValue().equals(smsfrom)){

                String base64Report = EncryptUtils.encodeBase64(report);

                reportStr.append("clientid=");
                reportStr.append(access.get("clientid"));
                reportStr.append("&msgid=");
                reportStr.append(access.get("submitid"));
                reportStr.append("&status=");
                reportStr.append(access.get("state"));
                reportStr.append("&remark=");
                reportStr.append(base64Report);
                reportStr.append("&phone=");
                reportStr.append(access.get("phone"));
                reportStr.append("&usmsfrom=");
                reportStr.append(smsfrom);

                // 标准协议状态报告保存在数据库db3
                String key = "report_cache:" + clientid;
                RedisUtils.lPushSpecificDb(3, key, reportStr.toString());
                RedisUtils.expire(3, key, 60 * 60 * 24 * 3);
                LOGGER.debug("ReportRepushUtil 向Redis中添加key={}，value={}", key, reportStr.toString());

            }else if(ProtocolType.HTTPS.equals(userProtocolTypeEnum) && NeedReportType.用户主动拉取状态报告.equals(userNeedReportTypeEnum)){


                Map<String, Object> map = new HashMap<>();
                map.put("sid", access.get("smsid"));
                map.put("uid", access.get("uid"));
                map.put("mobile", access.get("phone"));
                map.put("report_status", reportStatus);
                map.put("desc", report);
                map.put("user_receive_time", reportdateStr);
                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                String gsonStr = gson.toJson(map);
                reportStr.append(EncryptUtils.encodeBase64(gsonStr));

                // HTTPS协议（主动拉取）状态报告保存在数据库db4
                String key = "active_report_cache:" + clientid;
                RedisUtils.lPushSpecificDb(4, key, reportStr.toString());
                RedisUtils.expire(4, key, 60 * 60 * 24 * 3);
                LOGGER.debug("ReportRepushUtil 向Redis中添加key={}，value={}", key, reportStr.toString());

            }else if(ProtocolType.HTTPS.equals(userProtocolTypeEnum) &&
                    (NeedReportType.需要简单状态报告.equals(userNeedReportTypeEnum) || NeedReportType.需要透传状态报告.equals(userNeedReportTypeEnum))){

                // 获得重推状态报告地址
                String deliveryurl = account.get("deliveryurl").toString();
                if(StringUtils.isBlank(deliveryurl)){
                    LOGGER.debug("【重推状态报告】用户{}状态报告重推地址为空, taskName={}", clientid, taskName);
                    return false;
                }

                if(HttpProtocolType.HTTPS_GETPOST.equals(userHttpProtocolTypeEnum)){

                    reportStr.append("type=");
                    reportStr.append(1); // type 1为状态报告  2上行短信
                    reportStr.append("&sid=");
                    reportStr.append(access.get("smsid"));
                    reportStr.append("&mobile=");
                    reportStr.append(access.get("phone"));
                    reportStr.append("&status=");
                    reportStr.append(reportStatus);
                    reportStr.append("&desc=");
                    try {
                        reportStr.append(URLEncoder.encode(report, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        LOGGER.debug("【重推状态报告】用户{}，{}表，主键{}，转换状态报告时编码错误", clientid, tableName, id);
                    }
                    reportStr.append("&extend=");
                    reportStr.append("");
                    reportStr.append("&batchid=");
                    reportStr.append(access.get("uid"));
                    reportStr.append("&time=");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date reportdate = sdf.parse(reportdateStr);
                    long timeMills = new DateTime(reportdate).getMillis();
                    reportStr.append(timeMills);

                }else if(HttpProtocolType.HTTPS_JSON.equals(userHttpProtocolTypeEnum)){

                    List<Map<String, Object>> list = new ArrayList<>();
                    Map<String, Object> map = new HashMap<>();
                    map.put("sid", access.get("smsid"));
                    map.put("uid", access.get("uid"));
                    map.put("mobile", access.get("phone"));
                    map.put("report_status", reportStatus);
                    map.put("desc", report);
                    map.put("user_receive_time", reportdateStr);
                    list.add(map);
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    reportStr.append(gson.toJson(list));

                }else if(HttpProtocolType.HTTPS_TXJSON.equals(userHttpProtocolTypeEnum)){

                    List<Map<String, Object>> list = new ArrayList<>();
                    Map<String, Object> map = new HashMap<>();
                    map.put("sid", access.get("smsid"));
                    map.put("uid", access.get("uid"));
                    map.put("mobile", access.get("phone"));
                    map.put("report_status", reportStatus);
                    map.put("desc", report);
                    map.put("recv_time", reportdateStr);
                    list.add(map);
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    reportStr.append(gson.toJson(list));

                }else{
                    LOGGER.debug("重推状态报告用户={} HTTP协议类型异常", clientid);
                    return false;
                }

                LOGGER.debug("【重推状态报告】主动Post请求发送状态报告开始--------------------------------------");
                LOGGER.debug("deliveryurl={}, report={}", deliveryurl, reportStr.toString());
                // 发送请求推送状态报告
                String result = HttpUtils.httpPost(deliveryurl, reportStr.toString(), false);
                LOGGER.debug("主动Post请求发送状态报告返回响应={}", result);
                LOGGER.debug("【重推状态报告】主动Post请求发送状态报告结束--------------------------------------");


            }

        }catch (Exception e){
            LOGGER.debug("推送状态报告时异常，e = {}", e);
            return false;
        }

        return true;
    }



    public static void repushBatch(String taskName, String tableName, boolean forceRepush, List<Map<String, Object>> accessList) {
        int count = 0;
        for (Map<String, Object> access : accessList) {
            long start = System.currentTimeMillis();
            ReportRepushUtil.repush(taskName, tableName, forceRepush, access);
            LOGGER.debug("【重推状态报告】taskName={}推送第{}条记录完成，耗时={}ms",
                    taskName, count++, System.currentTimeMillis() - start);
        }
    }

    private static void updateAccessRepushStatus(String tableName, String pKey){
        reportRepushUtil.reportRepushService.updateAccessRepushStatus(tableName, pKey);
        LOGGER.debug("重推状态报告更新{}表中主键为{}的记录的状态报告重推次数", tableName, pKey);
    }

    public static boolean isAllTaskDone(List<FutureTask<String>> futureTaskList){
        boolean result = true;
        if(futureTaskList == null || futureTaskList.size() == 0){
            return true;
        }else{
            for (FutureTask<String> stringFutureTask : futureTaskList) {

                if(!stringFutureTask.isDone()){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 不同的状态需要从不同的字段获得状态报告<br/>
     *      state = 3 时从report字段取出<br/>
     *      state = 4 时从submitid字段取出<br/>
     *      state = 5、7、8、9、10 时从errorcode字段取出<br/>
     * @param state
     * @param access
     * @return
     */
    private static String getReportFromAccess(String state, Map<String, Object> access) {
        String report = "";
        // 根据状态从不同的字段获得状态报告
        if (state.equals("3") || state.equals("6")){
            report = access.get("report").toString();
        }else if(state.equals("4")){
            report = access.get("submitid").toString();
        }else{ // state = 5、7、8、9、10
            report = access.get("errorcode").toString();
        }

        // 如果report中存在*号则取*号前的
        int i = report.indexOf("*");
        if (i != -1) {
            report = report.substring(0, i);
        }

        return report;
    }

    /**
     * 根据短信的状态获得状态报告的状态<br/>
     *      state = 3 时状态报告状态为"SUCCESS"<br/>
     *      state != 3 时状态报告状态为"FAIL"
     * @param state
     * @return
     */
    private static String getReportStatus(String state) {
        String reportStatus = "FAIL";
        if(state.equals("3"))
            reportStatus = "SUCCESS";
        else
            reportStatus = "FAIL";

        return reportStatus;
    }

    private static String getReportDateStr(Map<String, Object> access) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Object reportdate = access.get("reportdate");
        Object submitdate = access.get("submitdate");

        Object date = null;
        if(reportdate != null) {
            date = (Date) reportdate;
        } else if (submitdate != null) {
            date = (Date) submitdate;
        } else {
        }

        if(date != null){
            return sdf.format(date);
        } else {
            return "";
        }

    }

}
