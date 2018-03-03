package com.ucpaas.sms.service.smsAudit;

import com.jsmsframework.audit.dto.JsmsAuditDTO;
import com.jsmsframework.audit.entity.JsmsAudit;
import com.jsmsframework.audit.entity.JsmsAuditBak;
import com.jsmsframework.audit.entity.JsmsAuditClientGroup;
import com.jsmsframework.audit.entity.JsmsAuditkeywordRecord;
import com.jsmsframework.audit.enums.AuditPage;
import com.jsmsframework.audit.enums.AuditType;
import com.jsmsframework.audit.service.JsmsAuditBakService;
import com.jsmsframework.audit.service.JsmsAuditClientGroupService;
import com.jsmsframework.audit.service.JsmsAuditService;
import com.jsmsframework.audit.service.JsmsAuditkeywordRecordService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.ResultVO;
import com.jsmsframework.common.enums.OperatorType;
import com.jsmsframework.common.enums.SmsTypeEnum;
import com.jsmsframework.common.util.BeanUtil;
import com.jsmsframework.user.entity.JsmsAccount;
import com.jsmsframework.user.entity.JsmsClientInfoExt;
import com.jsmsframework.user.entity.JsmsUser;
import com.jsmsframework.user.service.JsmsAccountService;
import com.jsmsframework.user.service.JsmsClientInfoExtService;
import com.jsmsframework.user.service.JsmsUserService;
import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.entity.message.Channel;
import com.ucpaas.sms.entity.message.TestAccount;
import com.ucpaas.sms.enums.AuditPageName;
import com.ucpaas.sms.enums.AuditStatus;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.enums.MobileOperatorEnum;
import com.ucpaas.sms.mapper.message.ChannelMapper;
import com.ucpaas.sms.mapper.message.TestAccountMapper;
import com.ucpaas.sms.model.KeywordMatchTuple;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.model.TestSendRequest;
import com.ucpaas.sms.model.TupleTwo;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.*;
import com.ucpaas.sms.util.rest.utils.EncryptUtil;
import com.ucpaas.sms.util.rest.utils.JsonUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * 短信审核
 */
@Service
@Transactional
public class SmsAuditServiceImpl implements SmsAuditService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsAuditServiceImpl.class);
    private ConcurrentMap<String, Object> concurrentMap = new ConcurrentHashMap<>();

    /**
     * 重要客户
     */
    public final static String MAJOR_CLIENTS = "majorClients";

    /**
     * 待审核的验证码
     */
    public final static String YZM_NUM = "yzmNum";
    /**
     * 待发送的验证码
     */
    public final static String YZM_NUM_SENDING = "yzmNumSending";

    /**
     * 重要客户待审核的短信数量
     */
    public final static String MAJOR_NUM = "majorNum";
    /**
     * 重要客户待发送的短信数量
     */
    public final static String MAJOR_NUM_SENDING = "majorNumSending";
    /**
     * 普通待审核的短信数量(除了以上种类)
     */
    public final static String ORDINARY_NUM = "ordinaryNum";
    /**
     * 普通待发送的短信数量(除了以上种类)
     */
    public final static String ORDINARY_NUM_SENDING = "ordinaryNumSending";
    /**
     * 待审核总数量
     */
    public final static String AUDIT_NUM = "auditNum";
    /**
     * 待发送总数量
     */
    public final static String SEND_NUM = "sendNum";
    /**
     * 锁定数量
     */
    public final static String LOCK_NUM = "lockNum";
    public final static String MAJOR_LOCK_NUM = "majorLockNum";
    public final static String YZM_LOCK_NUM = "yzmLockNum";
    public final static String ORDINARY_LOCK_NUM = "ordinaryLockNum";
/*    Set<String> majorAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + MAJOR_NUM +":*");
    Set<String> yzmAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + YZM_NUM +":*");
    Set<String> ordinaryAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + AUDIT_NUM +":*");*/
    /**
     * 页面审核展示限制
     */
    public final static String SMS_AUDIT_LOCKED = "SMSAuditLocked:";
    /**
     * 页面审核展示限制
     */
    public final static String LIMIT = "limit";


    @Autowired
    private MessageMasterDao messageMasterDao;
    @Autowired
    private LogService logService;
    @Autowired
    private JsmsAuditService jsmsAuditService;
    @Autowired
    private JsmsAuditBakService jsmsAuditBakService;
    @Autowired
    private JsmsUserService jsmsUserService;
    @Autowired
    private JsmsAuditkeywordRecordService jsmsAuditkeywordRecordService;

    @Autowired
    private JsmsClientInfoExtService jsmsClientInfoExtService;
    @Autowired
    private JsmsAccountService jsmsAccountService;
    @Autowired
    private TestAccountMapper testAccountMapper;
    @Autowired
    private JsmsClientInfoExtService clientInfoExtService;
    @Autowired
    private JsmsAuditClientGroupService auditClientGroupService;
    @Autowired
    private ChannelMapper channelMapper;

    private static ConcurrentHashMap<String, TupleTwo<Map<String, Object>, DateTime>> clientAuditKeywordMap = new ConcurrentHashMap<>();


    @Override
    public PageContainer auditQueryPage(Map<String, String> params) {

        long start = System.currentTimeMillis();
        Map<String, Object> data = this.queryAuditRecord(AuditPageName.AUDIT_QUERY_PAGE, AuditPage.AUDIT_QUERY_PAGE, params);
        PageContainer pageDatas = (PageContainer) data.get("pageData");
        List<Map<String, Object>> pageData = pageDatas.getList();
        long end = System.currentTimeMillis();
        LOGGER.debug("【审核查询】查询审核记录耗时{}ms", end - start);

        // 将审核记录中的审核关键字标红
        long start2 = System.currentTimeMillis();
        pageData = this.markAuditKeywordByList(pageData);
        long end2 = System.currentTimeMillis();
        LOGGER.debug("【审核查询】加载关键字信息耗时{}ms", end2 - start2);

        pageDatas.setList(suppleClientInfo(pageData));
        return pageDatas;

    }

    /**
     * 补充客户标签信息
     *
     * @param page
     * @return
     */
    private List<Map<String, Object>> suppleClientInfo(List<Map<String, Object>> page) {
        Set clientIds = new TreeSet<>();
        for (Map<String, Object> map : page) {
            clientIds.add(map.get("clientid"));
        }
        if (!clientIds.isEmpty()) {
            List<JsmsClientInfoExt> clientInfoExts = clientInfoExtService.getByClientIds(clientIds);
            Map<String, String> clientLabelMap = new HashMap<>();
            for (JsmsClientInfoExt infoExt : clientInfoExts) {
                clientLabelMap.put(infoExt.getClientid(), infoExt.getClientLabel());
            }
            for (Map<String, Object> map : page) {
                String clientId = (String) map.get("clientid");
                if (StringUtils.isNotBlank(clientId)) {
                    map.put("clientLabel", clientLabelMap.get(clientId));
                }
            }
        }
        return page;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> auditPage(Map<String, String> params, AuditPageName kind, AuditPage auditPage) {

        /*Map<String, String> queryParams = new HashMap<>();
        queryParams.put("smsType", smsType);
        Map<String, Object> data = this.queryAuditRecord(kind, queryParams);*/
        Map<String, Object> data = this.queryAuditRecord(kind, auditPage, params);
        List<Map<String, Object>> pageData = (List<Map<String, Object>>) data.get("pageData");
        // 将审核记录中的审核关键字标红
        pageData = this.markAuditKeywordByList(pageData);
        return suppleClientInfo(pageData);

    }

    /**
     * 标红审核记录列表中的关键字
     *
     * @param page
     * @return
     */
    private List<Map<String, Object>> markAuditKeywordByList(List<Map<String, Object>> page) {
        for (Map<String, Object> map : page) {
            long start2 = System.currentTimeMillis();
            this.markAuditKeywordByMap(map);
            long end2 = System.currentTimeMillis();
            LOGGER.debug("【审核查询】标记一条记录的关键字耗时{}ms", end2 - start2);
        }
        return page;
    }


    /**
     * 标红单个审核记录中的关键字
     *
     * @param audit 审核记录
     * @return 返回的是匹配到的关键字
     */
    private List<String> markAuditKeywordByMap(Map<String, Object> audit) {
        // 查询该账号是否需要审核关键字   mark:这个检查感觉没什么必要
//        Integer needAuditKeywords = messageMasterDao.getOneInfo("smsAudit.getClientIdIsNeedAuditKeywords", clientid);

        String clientid = (String) audit.get("clientid");
        Map<String, Object> worldMap; // 这个map用来实现关键字标红查询
        if (clientAuditKeywordMap.containsKey(clientid)) {
            TupleTwo<Map<String, Object>, DateTime> tuple = (TupleTwo) clientAuditKeywordMap.get(clientid);
            DateTime now = new DateTime();
            DateTime expireTime = tuple.second.plusMinutes(ConfigUtils.audit_client_keyword_expire_time);

            // 判断关键字Map是否超时
            if (expireTime.isAfter(now)) {
                worldMap = tuple.first;
                LOGGER.debug("命中clientAuditKeywordMap");
            } else {
                // 关键字搜索Map不存在则调用下面方法构建并返回
                worldMap = this.putClientAuditKeywordInCahche(clientid);
            }

        } else {
            worldMap = this.putClientAuditKeywordInCahche(clientid);
        }

        // 转移HTML防止注入，JSP页面输出是不能再用c:cout 因为下面需要显示标红的关键字
        String content = StringEscapeUtils.escapeHtml4(audit.get("content").toString());

        // 匹配当前内容中的关键字
        List<KeywordMatchTuple<String, Integer, Integer>> keywordMatchTuples = KeyWrodSearchUtils.searchKeywordPosByWorldMap(content, worldMap);

        // 将短信内容中的关键字用span标红
        int p1 = 0;
        int p2 = 0;
        List<String> htmlList = new ArrayList<>();
        List<String> matchKeywordList = new ArrayList<>();
        for (KeywordMatchTuple<String, Integer, Integer> keywordMatchTuple : keywordMatchTuples) {
            matchKeywordList.add(keywordMatchTuple.keyword);
            int begin = keywordMatchTuple.start;
            int end = keywordMatchTuple.end;
            p2 = keywordMatchTuple.start;
            htmlList.add(content.substring(p1, p2));
            htmlList.add("<span style='color:red'>" + content.substring(begin, p1 = end) + "</span>");
        }
        htmlList.add(content.substring(p1));
        audit.put("contentWithmarked", StringUtils.join(htmlList, ""));

        // 结束短信内容标红，返回的是匹配到的关键字列表
        return matchKeywordList;
    }


    /**
     * 添加一个账号的审核关键字到本地内存
     *
     * @param clientid
     * @return
     */
    private Map<String, Object> putClientAuditKeywordInCahche(String clientid) {
        long start = System.currentTimeMillis();
        List<JsmsAuditClientGroup> clientGroups = auditClientGroupService.getKgroupIdsByClientId(clientid);
        List<Integer> keywordGroupIds = new ArrayList<>();
        for (JsmsAuditClientGroup clientGroup : clientGroups) {
            keywordGroupIds.add(clientGroup.getKgroupId());
        }
        List<String> auditKeywordsList;
        if (keywordGroupIds.isEmpty()){
            auditKeywordsList = new ArrayList<>();
        }else{
            auditKeywordsList = messageMasterDao.selectList("smsAudit.getKeywordsByKGroupIds", keywordGroupIds);
        }

        Map<String, Object> worldMap = KeyWrodSearchUtils.builWorldMap(auditKeywordsList);
        TupleTwo<Map<String, Object>, DateTime> newKeyTuple = new TupleTwo(worldMap, new DateTime());
        clientAuditKeywordMap.put(clientid, newKeyTuple);
        LOGGER.debug("生成一个账号的关键字Map耗时{}ms", System.currentTimeMillis() - start);
        return worldMap;
    }

    /**
     * 更新短信审核状态
     */
    @Override
    public List<TestAccount> getTestClientid(Map params) {
        return testAccountMapper.queryParamsList(params);
    }

    /**
     * 更新短信审核状态
     */
    @Override
    public Map<String, Object> updateStatus(Map<String, String> params) {
        String pageName = Objects.toString(params.get("pageName"), "");
        return updateStatus(params, AuditPageName.getInstance(pageName));
    }
    public Map<String, Object> updateStatus(Map<String, String> params,AuditPageName auditPageName) {
        Map<String, Object> result = new HashMap<String, Object>();

        String auditIds = Objects.toString(params.get("auditIds"), "");
        String status = Objects.toString(params.get("status"), "0");
        String userId = Objects.toString(params.get("userId"), "");
        String transferToId = Objects.toString(params.get("transferToId"), "");
        String remark = Objects.toString(params.get("remark"), "");
        List<String> auditIdList = new ArrayList<String>();

        if (StringUtils.isNotBlank(auditIds)) {
            auditIdList = Arrays.asList(auditIds.split(","));
        } else {
            result.put("result", "fail");
            result.put("msg", "审核ID不能为空");
            return result;
        }


        try {
            if (AuditStatus.WAIT_TO_AUDIT.getValue().equals(status)) { // 页面离开时释放待审核的记录

                // 删除Redis中保存的锁定记录
                this.delAuditIdInRedis(auditIdList,auditPageName);
                result.put("result", "success");

            } else {

                Map<String, Object> sqlParams = new HashMap<String, Object>();
                sqlParams.put("releaseIdList", auditIdList);
                sqlParams.put("status", status);

                // 转审时的信息
                if (AuditStatus.TRANSFER_AUDIT.getValue().equals(status)) {
                    sqlParams.put("transferperson", transferToId);

                } else {
                    sqlParams.put("auditperson", userId);

                }

                sqlParams.put("remark", remark);

                int i = messageMasterDao.update("smsAudit.updateStatus", sqlParams);
                if (i > 0) {

                    //插表记录关键字与审核关系
                    if (!AuditStatus.TRANSFER_AUDIT.getValue().equals(status)) {
                        for (String auditId : auditIdList) {

                            JsmsAudit audit = jsmsAuditService.getByAuditid(Long.valueOf(auditId));
                            Map<String, Object> auditMap = BeanUtil.beanToMap(audit, false);
                            List<String> keywordList = this.markAuditKeywordByMap(auditMap);
                            List<JsmsAuditkeywordRecord> insertList = new ArrayList<>();
                            for (String kw : keywordList) {
                                JsmsAuditkeywordRecord record = new JsmsAuditkeywordRecord();
                                record.setAuditCreateTime(audit.getCreatetime());
                                record.setAuditStatus(Integer.valueOf(status));
                                record.setAuditid(audit.getAuditid());
                                record.setKeyword(kw);
                                insertList.add(record);
                            }

                            if (insertList.size() > 0) {
                                int insertNum = jsmsAuditkeywordRecordService.insertBatch(insertList);
                                if (insertNum <= 0) {
                                    result.put("result", "fail");
                                    result.put("msg", "操作失败，短信审核关键字记录表失败");
                                    LOGGER.debug("更新短信审核状态【失败】，操作人={}，，审核id为={},关键字为={}", AuthorityUtils.getLoginRealName(), audit.getAuditid(), JsonUtil.toJsonStr(keywordList));
                                }
                            }
                        }
                    }


                    // 删除Redis中保存的锁定记录
                    this.delAuditIdInRedis(auditIdList, auditPageName);
                    result.put("result", "success");
                    LOGGER.debug("更新短信审核状态为：{}，操作人={}，审核ID={}", AuditStatus.getInstance(status).getName(),
                            AuthorityUtils.getLoginRealName(), JsonUtil.toJsonStr(auditIdList));
                } else {
                    result.put("result", "fail");
                    result.put("msg", "操作失败，记录已经被被审核或不存在");
                    LOGGER.debug("更新短信审核状态【失败】，操作人={}，操作信息={}", AuthorityUtils.getLoginRealName(), JsonUtil.toJsonStr(sqlParams));
                }

                if (params.get("pageName") != null && params.get("pageName").equals("auditPage")) {
                    logService.add(LogType.update, LogEnum.短信审核.getValue(), "短信审核 - 短信审核，修改审核记录状态：", params, result);
                } else {
                    logService.add(LogType.update, LogEnum.短信审核.getValue(), "短信审核 - 短信审核查询，修改审核记录状态：", params, result);
                }
            }
        } catch (Exception e) {
            LOGGER.error("更新短信审核记录状态时发生异常：", e);
            result.put("result", "fail");
            result.put("msg", "操作失败，请联系管理员");
        }

        return result;
    }

    /**
     * 更新短信审核状态
     */
    @Override
    public ResultVO testSendAction(Map<String, String> params) {

        final TestSendRequest testSend = new TestSendRequest();
        testSend.setClientid(params.get("clientid"));
        testSend.setMobile(params.get("mobile"));
        testSend.setSmstype(params.get("smstype"));
        testSend.setContent(params.get("content"));
        testSend.setExtend(params.get("extend"));
        testSend.setChannelid(params.get("channelid"));

        if (!RegexUtils.isClientId(testSend.getClientid()))
            return ResultVO.failure("用户账号不能为空或者格式不对");

        if (StringUtils.isBlank(testSend.getMobile()))
            return ResultVO.failure("手机号不能为空");

        if (StringUtils.isBlank(testSend.getSmstype()))
            return ResultVO.failure("请选择短信类型");

        if (StringUtils.isNotBlank(testSend.getExtend()) && !testSend.getExtend().matches("^\\d{0,12}$"))
            return ResultVO.failure("扩展位数只能是12位以内数字组合");

        if (StringUtils.isBlank(testSend.getContent()))
            return ResultVO.failure("请输入测试短信内容");

        if (testSend.getContent().length() > 500)
            return ResultVO.failure("短信内容不能超过500");

        if (!testSend.getContent().matches("^(【[^\\n]{2,12}】[^【】]{1,496})|([^【】]{1,496}【[^\\n]{2,12}】)$"))
            return ResultVO.failure("请输入正确格式的短信");

        String[] mobileArr = testSend.getMobile().split(",");
        for (String mobile : mobileArr) {
            if (!RegexUtils.isMobile(mobile))
                return ResultVO.failure("号码格式不正确");
        }

        ResultVO resultVO = null;
        if (StringUtils.isNotBlank(testSend.getChannelid()))
            resultVO = checkOperatorstype(Integer.parseInt(testSend.getChannelid()), testSend.getMobile());

        if (resultVO != null && resultVO.isFail())
            return resultVO;

        JsmsAccount jsmsAccount = jsmsAccountService.getByClientId(testSend.getClientid());
        if (jsmsAccount == null)
            return ResultVO.failure("客户账号不存在");

//        testSend.setClientid("a00025");// todo --> 注释掉
        try {
            String md5Digest = EncryptUtil.md5Digest(jsmsAccount.getPassword());
//            String md5Digest = EncryptUtil.md5Digest("yzxwarn1");// todo --> 注释掉
            testSend.setPassword(md5Digest.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String configUrl = ConfigUtils.sms_test_send_url;
//        String configUrl = "https://api.ucpaas.com/sms-partner/access/{clientid}/sendsms";// todo --> 注释掉
        final String testSendUrl = ConfigUtils.sms_test_send_url.replaceFirst("\\{[^\\}]*\\}", testSend.getClientid());
//        final String testSendUrl = configUrl.replaceFirst("\\{[^\\}]*\\}", testSend.getClientid());// todo --> 注释掉
        LOGGER.debug("请求的地址 -----------------------------> {}", testSendUrl);
        ExecutorService exec = Executors.newFixedThreadPool(1);
        FutureTask futureTask = new FutureTask(new Callable() {
            @Override
            public String call() throws Exception {
                LOGGER.debug("测试短信发送的线程 ----------> start");
                String resp = null;
                // 线上https
                LOGGER.debug("测试短信发送参数 ----------> {}", JsonUtils.toJson(testSend));

                if (testSendUrl.startsWith("https")) {
                    resp = HttpUtils.httpPost(testSendUrl, JsonUtils.toJson(testSend), true);
                    LOGGER.debug("测试短信发送的的响应内容 ----------> {}", resp);
                } else {
                    resp = HttpUtils.httpPost(testSendUrl, JsonUtils.toJson(testSend), false);
                    LOGGER.debug("测试短信发送的的响应内容 ----------> {}", resp);
                }
                LOGGER.debug("测试短信发送的线程 ----------> start");
                return resp;
            }
        });
        exec.submit(futureTask);
        String resp = null;
        try {
            resp = (String) futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        LOGGER.debug("短信审核 - 短信发送测试：{}", resp);
        logService.add(LogType.update, LogEnum.短信审核.getValue(), "短信审核 - 短信发送测试", JsonUtils.toJson(testSend), resp);
        exec.shutdown();
        return ResultVO.successDefault(resp);
    }

    /**
     * 检查通道号和手机号运营商类型是否匹配
     */
    private ResultVO checkOperatorstype(Integer channelId, String mobiles) {
        String[] mobileArr = mobiles.split(",");
        if (mobileArr.length > 100)
            return ResultVO.failure("号码不能超过100个");

        Channel channel = channelMapper.getByCid(channelId);

        if (OperatorType.全网.getValue().equals(channel.getOperatorstype()))
            return ResultVO.successDefault();

        for (String mobile : mobileArr) {

            String mobileOperator = RegexUtils.getMobileOperator(mobile);
            if (!channel.getOperatorstype().toString().equals(mobileOperator))
                return ResultVO.failure("通道所属运营商类型：" + OperatorType.getDescByValue(channel.getOperatorstype())
                        + "<br/>号码：" + mobile + "为" + MobileOperatorEnum.getDescByValue(mobileOperator));

        }
        return ResultVO.successDefault();
    }

    /**
     * 查询短信审核记录<br>
     * 该方法使用同步防止不同用户同一时间进入页面时锁定相同的记录<br>
     *
     * @param auditKind       页面枚举
     * @param queryConditions 页面的查询条件
     * @return
     */
    private Map<String, Object> queryAuditRecord(AuditPageName auditKind, AuditPage auditPage, Map<String, String> queryConditions) {
        Map<String, Object> result = new HashMap<String, Object>();

        LOGGER.debug("进入{},审核人={}", auditKind.getName(), AuthorityUtils.getLoginRealName());

        String loginUserId = String.valueOf(AuthorityUtils.getLoginUserId());
        String loginUserName = AuthorityUtils.getLoginRealName();
        List<String> waitLockedList = new ArrayList<>(); // 当前查询需要被锁定的记录
        if (AuditPageName.AUDIT_QUERY_PAGE.equals(auditKind)) {// 审核查询页面
            PageContainer pageContainer = new PageContainer();
            pageContainer.setPageRowCount(100);// 默认每页100条数据
            pageContainer.setPageRowArray(new Integer[]{10, 30, 50, 100, 200, 500});
            pageContainer = messageMasterDao.getSearchPage("smsAudit.query", "smsAudit.queryCount", queryConditions, pageContainer);
            Set<String> keySet = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + "*");
            // 查询已经被锁定的审核记录
            TreeSet<Long> lockedAuditids = new TreeSet<>();
            Map<String,String> auditIdLockedMap = new HashMap<>(); // 已经被锁定的记录
            if (keySet != null && !keySet.isEmpty()) {
                for (String key : keySet) {
                    String[] array = key.split(":");
                    String auditid = array[array.length - 1];
                    lockedAuditids.add(Long.parseLong(auditid));
                    auditIdLockedMap.put(auditid,key);
                }
            }

            LOGGER.debug("当前已经锁定的审核记录共={}条，详细信息={}", auditIdLockedMap.size(), auditIdLockedMap);
            if(!auditIdLockedMap.isEmpty()){
                for (Map<String, Object> map : pageContainer.getList()) {
                    String auditid = String.valueOf(map.get("auditid"));

                    // 获得锁定人的信息
                    if(auditIdLockedMap.get(auditid) != null){
                        // 如果Redis中存在说明已经被锁定，获取其中的信息
                        Map<String, String> lockInfo = RedisUtils.hgetallSpecificDb(6, auditIdLockedMap.get(auditid));
                        map.put("lockByUserId", lockInfo.get("userid"));
                        map.put("lockByUserName", lockInfo.get("username"));
                    }else{
                        // 当前用户只能锁定status = 0 或者(status = 3 && transferperson 等于自己) 的记录
                        map.put("lockByUserId", "");
                        map.put("lockByUserName", "");
                    }

                }
            }

            result.put("pageData", pageContainer);

        } else {
            synchronized (SmsAuditServiceImpl.class) {


                Set<String> keySet = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + auditKind.getValue() + ":*");
                // 查询已经被锁定的审核记录
                TreeSet<Long> lockedAuditids = new TreeSet<>();
                List<String> auditIdLockedList = new ArrayList<>(); // 已经被锁定的记录
                if (keySet != null && !keySet.isEmpty()) {
                    for (String key : keySet) {
                        String[] array = key.split(":");
                        String auditid = array[array.length - 1];
                        lockedAuditids.add(Long.parseLong(auditid));
                        auditIdLockedList.add(auditid);
                    }
                }

                LOGGER.debug("当前已经锁定的审核记录共={}条，详细信息={}", auditIdLockedList.size(), auditIdLockedList);

//			 	if(AuditPageName.AUDIT_PAGE.equals(auditPageName)){// 审核页面
                Date startCreateTime = null;
                Date endCreateTime = null;
                try {
                    if (StringUtils.isNoneBlank(queryConditions.get(""))) {
                        startCreateTime = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(queryConditions.get("start_time"));
                    }
                    if (StringUtils.isNoneBlank(queryConditions.get(""))) {
                        endCreateTime = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(queryConditions.get("end_time"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                /* todo
                List<JsmsAudit> jsmsAuditList = jsmsAuditService.queryAuditRecord(queryConditions.get("clientid"), AuthorityUtils.getLoginUserId(), (TreeSet) concurrentMap.get(MAJOR_CLIENTS),
                        lockedAuditids, queryConditions.get("content"), startCreateTime, endCreateTime, queryConditions.get("sign"),
                        Integer.parseInt(queryConditions.get("smsType")), Integer.parseInt(queryConditions.get("lessSendnum")),
                        Integer.parseInt(queryConditions.get("greaterSendnum")), Long.parseLong(queryConditions.get("auditid")), auditPage, Integer.parseInt(queryConditions.get("limit")));
                */Map<String, Object> sqlParams = new HashMap<String, Object>();
                sqlParams.put("sms_audit_lock", auditIdLockedList);
                sqlParams.put("transferperson", loginUserId);
                sqlParams.putAll(queryConditions); // 审核页面审核的短信类型
                List<Map<String, Object>> auditPageList = new ArrayList<>();
                if (AuditPageName.ORDINARY_NUM.equals(auditKind)) {// 普通审核页面
                    sqlParams.put("clientIds", concurrentMap.get(MAJOR_CLIENTS));
                    auditPageList = messageMasterDao.getSearchList("smsAudit." + ORDINARY_NUM, sqlParams);
                } else if (AuditPageName.YZM_AUDIT_PAGE.equals(auditKind)) {
                    auditPageList = messageMasterDao.getSearchList("smsAudit." + YZM_NUM, sqlParams);
                } else if (AuditPageName.MAJOR_AUDIT_PAGE.equals(auditKind)) {
                    Set<String> clientIds = (Set<String>) concurrentMap.get(MAJOR_CLIENTS);
                    if (clientIds != null && !clientIds.isEmpty()) {
                        sqlParams.put("clientIds", concurrentMap.get(MAJOR_CLIENTS));
                        auditPageList = messageMasterDao.getSearchList("smsAudit." + MAJOR_NUM, sqlParams);
                    }
                }
                for (Map<String, Object> map : auditPageList) {
                    waitLockedList.add(String.valueOf(map.get("auditid")));
                }
                result.put("pageData", auditPageList);

//				}else if(){}
                LOGGER.debug("当前查询后需要新增的锁定的记录有{}条，详细信息={}", waitLockedList.size(), waitLockedList);
                this.hmsetAuditIdInRedis(waitLockedList, auditKind);
            }


            // 将需要锁定的审核记录保存到Redis中
        }

        return result;
    }

    /**
     * 将锁定的审核记录保存到Redis中
     *
     * @return
     */
    private void hmsetAuditIdInRedis(List<String> auditIdList, AuditPageName auditKind) {
        // 获得所有锁定的key
        Map<String, String> hash = new HashMap<>();
        hash.put("auditid", "");
        hash.put("userid", String.valueOf(AuthorityUtils.getLoginUserId()));
        hash.put("username", AuthorityUtils.getLoginRealName());
        hash.put("createtime", DateTime.now().toString("yyyyMMddHHmmss"));

        int auditExpireTime = (int) this.getAuditExpireTime().get("auditExpireTime");
        // Redis中的审核锁定记录比页面审核超时时间长30秒
        int redisExpireTime = auditExpireTime * 60 + 30;
        List<String> tempList = new ArrayList<>();
        if (auditIdList != null && !auditIdList.isEmpty()) {
            Jedis jedis = null;
            try {
                jedis = RedisUtils.getJedis();
                Pipeline pipeline = jedis.pipelined();
                pipeline.select(6);
                for (String auditId : auditIdList) {
                    String key = SMS_AUDIT_LOCKED + auditKind.getValue() + ":" + auditId;
                    key = key.toLowerCase();
                    hash.put("auditid", auditId);
                    tempList.add(key);
                    pipeline.hmset(key, hash);
                    pipeline.expire(key, redisExpireTime);
                }
                pipeline.sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        LOGGER.debug("保存审核锁定记录到Redis中，锁定人={}，锁定记录={}", AuthorityUtils.getLoginRealName(), tempList);

    }

    /**
     * 删除Redis中的锁定审核记录
     *
     * @param auditIdList
     */
    private void delAuditIdInRedis(List<String> auditIdList, AuditPageName pageKind) {

        /*Jedis jedis = null;
        try {
            jedis = RedisUtils.getJedis();
            Pipeline pipeline = jedis.pipelined();
            pipeline.select(6);
            for (String auditId : auditIdList) {
                String key = SMS_AUDIT_LOCKED + pageKind.getValue() + ":" + auditId;
                hash.put("auditid", auditId);
                tempList.add(key);
                pipeline.hmset(key, hash);
                pipeline.expire(key, redisExpireTime);
            }
            pipeline.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }*/

        // redis中锁定的审核记录 key = SMS_AUDIT_KEY_PREFIX + auditId

        // 获得所有锁定的key
        List<String> tempList = new ArrayList<>();
        if (auditIdList != null && !auditIdList.isEmpty()) {
            for (String auditId : auditIdList) {
                tempList.add(SMS_AUDIT_LOCKED + pageKind.getValue()+ ":" + auditId); // todo  + audit
            }
        }
        String[] keys = tempList.toArray(new String[tempList.size()]);

        // 审核锁定记录保存在db 6
        int index = 6;

        // 删除db 6中的审核记录
        RedisUtils.delKeySpecifiedDb(index, keys);

        LOGGER.debug("删除Redis中锁定记录，被删除的锁定记录={}", tempList);
    }

    @Override
    public Map getKindsAuditNum() {
        checkLockNum(concurrentMap);
        return concurrentMap;
    }

    @Override
    @Scheduled(cron = "0/10 * * * * ?") // 15秒执行一次
    public void updateKindsAuditNum() {
        Map<String, Object> total = messageMasterDao.getOneInfo("smsAudit.getNeedAuditNum", null);
        Long auditNum = (Long) total.get(AUDIT_NUM);
        BigDecimal sendNum = (BigDecimal) total.get(SEND_NUM);
        concurrentMap.put(AUDIT_NUM, auditNum);
        concurrentMap.put(SEND_NUM, sendNum.longValue());

        Map<String, Object> yzm = messageMasterDao.getOneInfo("smsAudit.getYZMAuditNum", null);
        Long yzmNum = (Long) yzm.get(YZM_NUM);
        BigDecimal yzmNumSending = (BigDecimal) yzm.get(YZM_NUM_SENDING);
        concurrentMap.put(YZM_NUM, yzmNum);
        concurrentMap.put(YZM_NUM_SENDING, yzmNumSending.longValue());
        Set<String> clientIds = getMajorClientIds();
        concurrentMap.put(MAJOR_CLIENTS, clientIds);
        Long majorNum = 0L;
        BigDecimal majorNumSending = BigDecimal.ZERO;
        if (clientIds.isEmpty()) {
            concurrentMap.put(MAJOR_NUM, 0L);
            concurrentMap.put(MAJOR_NUM_SENDING, 0L);
        } else {
            Map<String, Object> sqlParam = new HashMap();
            sqlParam.put("clientIds", clientIds);
            Map<String, Object> major = messageMasterDao.getOneInfo("smsAudit.getMojorAuditNum", sqlParam);
            majorNum = (Long) major.get(MAJOR_NUM);
            majorNumSending = (BigDecimal) major.get(MAJOR_NUM_SENDING);
            concurrentMap.put(MAJOR_NUM, majorNum);
            concurrentMap.put(MAJOR_NUM_SENDING, majorNumSending.longValue());
        }
        // todo 普通
        concurrentMap.put(ORDINARY_NUM, auditNum - yzmNum - majorNum);
//		concurrentMap.put(ORDINARY_NUM_SENDING, total.get(SEND_NUM) - concurrentMap.get(YZM_NUM_SENDING) - concurrentMap.get(MAJOR_NUM_SENDING));
        concurrentMap.put(ORDINARY_NUM_SENDING, sendNum.subtract(yzmNumSending).subtract(majorNumSending).longValue());

    }

    @Override
//    @Scheduled(cron = "* 0/10 * * * ?") // 10分钟执行一次
    public void cacheKindsAuditId() {
        // todo
    }

    private void checkLockNum(Map<String, Object> map) {
        Set<String> majorAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + MAJOR_NUM + ":*");
        Set<String> yzmAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + YZM_NUM + ":*");
        Set<String> ordinaryAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + ORDINARY_NUM + ":*");
        map.put(MAJOR_LOCK_NUM, majorAuditLock == null ? 0 : majorAuditLock.size());
        map.put(YZM_LOCK_NUM, yzmAuditLock == null ? 0 : yzmAuditLock.size());
        map.put(ORDINARY_LOCK_NUM, ordinaryAuditLock == null ? 0 : ordinaryAuditLock.size());
    }

    private Set<String> getMajorClientIds() {
        List<JsmsClientInfoExt> auditKeyAccountInfoExt = jsmsClientInfoExtService.getAuditKeyAccountInfoExt();
        Set<String> clientIds = new TreeSet<>();
        for (JsmsClientInfoExt jsmsClientInfoExt : auditKeyAccountInfoExt) {
            clientIds.add(jsmsClientInfoExt.getClientid());
        }
        return clientIds;
    }

    /**
     * 查询审核超时时间<br>
     * 单位：分钟
     */
    @Override
    public Map<String, Object> getAuditExpireTime() {
        Map<String, Object> data = messageMasterDao.getOneInfo("smsAudit.getAuditExpireTime", null);
        String resultContent = "";
        int auditExpireTime = 15;// 默认短信审核超时时间
        try {
            if (null != data && null != data.get("auditExpireTime")) {
                resultContent = (String) data.get("auditExpireTime");
                if (StringUtils.isNotBlank(resultContent)) {
                    auditExpireTime = Integer.valueOf(resultContent.split(";")[0]);
                }
            }
        } catch (Exception e) {
            LOGGER.error("查询审核超时时间是发送错误：" + e);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("auditExpireTime", auditExpireTime);
        return resultMap;
    }


    /**
     * 查询“待审核记录数”、“待发送数”、“锁定审核记录数”
     */
    @Override
    public Map<String, Object> getNeedAuditNum() {
        int auditNum = 0;
        int sendNum = 0;
        int lockNum = 0;

        // 查询审核状态为0和3的记录然后计算出审核记录数和待发送数
        Map<String, Object> data = messageMasterDao.getOneInfo("smsAudit.getNeedAuditNum", null);
        if (data != null) {
            auditNum = Integer.valueOf(String.valueOf(data.get(AUDIT_NUM)));
            sendNum = Integer.valueOf(String.valueOf(data.get(SEND_NUM)));
        }

        // 查询当前已经锁定的审核记录数
        Set<String> keySet = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + "*");
        lockNum = keySet == null ? 0 : keySet.size();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(AUDIT_NUM, auditNum);
        resultMap.put(SEND_NUM, sendNum);
        resultMap.put(LOCK_NUM, lockNum);
        checkLockNum(resultMap);
//        Set<String> majorAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + MAJOR_NUM +":*");
//        Set<String> yzmAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + YZM_NUM +":*");
//        Set<String> ordinaryAuditLock = RedisUtils.getKeysSpecifiedDb(6, SMS_AUDIT_LOCKED + AUDIT_NUM +":*");

        return resultMap;
    }

    /**
     * 短信审核查询页面
     *
     * @param page
     * @return
     */
    @Override
    public JsmsPage hisauditQueryPage(JsmsPage page) {

        if (page.getParams().get("aduitor") != null) {
            JsmsPage usepage = new JsmsPage();
            usepage.getParams().put("realname", page.getParams().get("aduitor"));
            usepage.setRows(99); //哥不信有那么多重名
            usepage = jsmsUserService.queryList(usepage);
            List<JsmsUser> user = usepage.getData();
            List auditIds = new ArrayList();
            for (JsmsUser jsmsUser : user) {

                auditIds.add(jsmsUser.getId());
            }

            page.getParams().put("auditperson", auditIds);
        }

        page.setOrderByClause("audittime desc");

        List<JsmsAuditDTO> result = new ArrayList<>();
        JsmsPage pages = jsmsAuditService.queryList(page);
        List<JsmsAudit> list = pages.getData();
        int i = 0;
        int rownum = 1;
        if (!list.isEmpty() || list.size() > 0) {
            for (JsmsAudit jsmsAudit : list) {
                JsmsAuditDTO dto = new JsmsAuditDTO();
                BeanUtil.copyProperties(jsmsAudit, dto);
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.通知.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.通知.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.验证码.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.验证码.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.营销.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.营销.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.告警.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.告警.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.USSD.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.USSD.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.闪信.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.闪信.getDesc());
                }

                if (jsmsAudit.getStatus().equals(AuditType.待审核.getValue())) {
                    dto.setStatusName(AuditType.待审核.getDesc());
                }
                if (jsmsAudit.getStatus().equals(AuditType.审核通过.getValue())) {
                    dto.setStatusName(AuditType.审核通过.getDesc());
                }
                if (jsmsAudit.getStatus().equals(AuditType.审核不通过.getValue())) {
                    dto.setStatusName(AuditType.审核不通过.getDesc());
                }
                if (jsmsAudit.getStatus().equals(AuditType.转审.getValue())) {
                    dto.setStatusName(AuditType.转审.getDesc());
                }
                if (jsmsAudit.getAuditperson() != null) {
                    Long id = Long.valueOf(jsmsAudit.getAuditperson());
                    JsmsUser audits = jsmsUserService.getById(String.valueOf(id));
                    if (audits != null) {
                        dto.setAuditpersonName(audits.getRealname());
                    } else {
                        dto.setAuditpersonName("未知");
                    }

                }
                if (StringUtils.isNoneBlank(jsmsAudit.getTransferperson())) {
                    Long id = Long.valueOf(jsmsAudit.getTransferperson());
                    JsmsUser tranUser = jsmsUserService.getById(String.valueOf(id));
                    if (tranUser != null) {
                        dto.setTransferpersonName(tranUser.getRealname());
                    } else {
                        dto.setTransferpersonName("未知");
                    }

                }
                if (jsmsAudit.getCreatetime() != null) {
                    dto.setCreatetimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jsmsAudit.getCreatetime()));

                } else {
                    dto.setCreatetimeStr("-");
                }
                if (jsmsAudit.getAudittime() != null) {
                    dto.setAudittimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jsmsAudit.getAudittime()));
                } else {
                    dto.setAudittimeStr("-");
                }


                rownum = (pages.getPage() - 1) * pages.getRows() + 1 + i;
                i++;
                dto.setRownum(rownum);
                result.add(dto);
            }
        }
//		pages.setTotalRecord(jsmsAuditService.countHis(page));
        pages.setData(result);
        return pages;
    }

    /**
     * 短信审核查询页面
     *
     * @param page
     * @return
     */
    @Override
    public JsmsPage hisbakauditQueryPage(JsmsPage page) {

        if (page.getParams().get("aduitor") != null) {
            JsmsPage usepage = new JsmsPage();
            usepage.getParams().put("realname", page.getParams().get("aduitor"));
            usepage.setRows(99); //哥不信有那么多重名+1
            usepage = jsmsUserService.queryList(usepage);
            List<JsmsUser> user = usepage.getData();
            List auditIds = new ArrayList();
            for (JsmsUser jsmsUser : user) {

                auditIds.add(jsmsUser.getId());
            }

            page.getParams().put("auditperson", auditIds);
        }

        page.setOrderByClause("audittime desc");
        List<JsmsAuditDTO> result = new ArrayList<>();
        JsmsPage pages = jsmsAuditBakService.queryList(page);
        List<JsmsAuditBak> list = pages.getData();
        int i = 0;
        int rownum = 1;
        if (!list.isEmpty() || list.size() > 0) {
            for (JsmsAuditBak jsmsAudit : list) {
                JsmsAuditDTO dto = new JsmsAuditDTO();
                BeanUtil.copyProperties(jsmsAudit, dto);
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.通知.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.通知.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.验证码.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.验证码.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.营销.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.营销.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.告警.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.告警.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.USSD.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.USSD.getDesc());
                }
                if (jsmsAudit.getSmstype().equals(SmsTypeEnum.闪信.getValue())) {
                    dto.setSmsTypeName(SmsTypeEnum.闪信.getDesc());
                }

                if (jsmsAudit.getStatus().equals(AuditType.待审核.getValue())) {
                    dto.setStatusName(AuditType.待审核.getDesc());
                }
                if (jsmsAudit.getStatus().equals(AuditType.审核通过.getValue())) {
                    dto.setStatusName(AuditType.审核通过.getDesc());
                }
                if (jsmsAudit.getStatus().equals(AuditType.审核不通过.getValue())) {
                    dto.setStatusName(AuditType.审核不通过.getDesc());
                }
                if (jsmsAudit.getStatus().equals(AuditType.转审.getValue())) {
                    dto.setStatusName(AuditType.转审.getDesc());
                }
                if (jsmsAudit.getAuditperson() != null) {
                    Long id = Long.valueOf(jsmsAudit.getAuditperson());
                    JsmsUser audits = jsmsUserService.getById(String.valueOf(id));
                    if (audits != null) {
                        dto.setAuditpersonName(audits.getRealname());
                    } else {
                        dto.setAuditpersonName("未知");
                    }

                }
                if (jsmsAudit.getTransferperson() != null) {
                    Long id = Long.valueOf(jsmsAudit.getTransferperson());
                    JsmsUser tranUser = jsmsUserService.getById(String.valueOf(id));
                    if (tranUser != null) {
                        dto.setTransferpersonName(tranUser.getRealname());
                    } else {
                        dto.setTransferpersonName("未知");
                    }

                }
                if (jsmsAudit.getCreatetime() != null) {
                    dto.setCreatetimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jsmsAudit.getCreatetime()));

                } else {
                    dto.setCreatetimeStr("-");
                }
                if (jsmsAudit.getAudittime() != null) {
                    dto.setAudittimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jsmsAudit.getAudittime()));
                } else {
                    dto.setAudittimeStr("-");
                }


                rownum = (pages.getPage() - 1) * pages.getRows() + 1 + i;
                i++;
                dto.setRownum(rownum);
                result.add(dto);
            }
        }
//		pages.setTotalRecord(jsmsAuditService.countHis(page));
        pages.setData(result);
        return pages;
    }

    /**
     * 查询历史审核记录
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> queryhisAll(Map<String, String> params) {
        Map<String, Object> params1 = new HashMap<>();
        params1.putAll(params);
        if (params.get("aduitor") != null) {
            JsmsPage usepage = new JsmsPage();
            usepage.getParams().put("realname", params.get("aduitor"));
            usepage.setRows(99);  //哥不信有那么多重名+2
            usepage = jsmsUserService.queryList(usepage);
            List<JsmsUser> user = usepage.getData();
            List auditIds = new ArrayList();
            for (JsmsUser jsmsUser : user) {

                auditIds.add(jsmsUser.getId());
            }

            params1.put("auditperson", auditIds);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> list = jsmsAuditService.queryhisAll(params1);
        int i = 0;
        int rownum = 1;
        if (!list.isEmpty() || list.size() > 0) {
            for (Map<String, Object> map : list) {
                rownum = 1 + i;
                i++;
                map.put("rownum", rownum);
                result.add(map);
            }
        }

        return result;
    }

    /**
     * 查询历史审核记录
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> queryhisbakAll(Map<String, String> params) {
        Map<String, Object> params1 = new HashMap<>();
        params1.putAll(params);
        if (params.get("aduitor") != null) {
            JsmsPage usepage = new JsmsPage();
            usepage.getParams().put("realname", params.get("aduitor"));
            usepage.setRows(99);  //哥不信有那么多重名+2
            usepage = jsmsUserService.queryList(usepage);
            List<JsmsUser> user = usepage.getData();
            List auditIds = new ArrayList();
            for (JsmsUser jsmsUser : user) {

                auditIds.add(jsmsUser.getId());
            }

            params1.put("auditperson", auditIds);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> list = jsmsAuditBakService.queryhisAll(params1);
        int i = 0;
        int rownum = 1;
        if (!list.isEmpty() || list.size() > 0) {
            for (Map<String, Object> map : list) {
                rownum = 1 + i;
                i++;
                map.put("rownum", rownum);
                result.add(map);
            }
        }

        return result;
    }

    @Override
    public Map<String, Object> setAuditExpired(String auditId) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isBlank(auditId)) {
            result.put("result", "fail");
            result.put("msg", "审核ID不能为空");
            return result;
        }

        messageMasterDao.update("smsAudit.updateAuditExpired", auditId);
        result.put("result", "success");
        result.put("msg", "操作成功");
        return result;
    }

}
