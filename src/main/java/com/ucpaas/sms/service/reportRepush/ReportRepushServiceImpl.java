package com.ucpaas.sms.service.reportRepush;


import com.ucpaas.sms.constant.DbConstant;
import com.ucpaas.sms.dao.AccessMasterDao;
import com.ucpaas.sms.dao.AccessSlaveDao;
import com.ucpaas.sms.service.CommonService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.JsonUtils;
import com.ucpaas.sms.util.ReportRepushUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 重推状态报告
 */
@Service
@Transactional
public class ReportRepushServiceImpl implements ReportRepushService {

    private static final Logger LOGGER = LoggerFactory.getLogger("ReportRepushService");

    @Autowired
    private AccessMasterDao accessMasterDao;
    @Autowired
    private AccessSlaveDao accessSlaveDao;
    @Autowired
    private CommonService commonService;

    private static List<FutureTask<String>> futureTaskList = new ArrayList<>();


    @Override
    public Map<String, Object> submitRepush(Map<String, String> params) {

        Map<String, Object> result = new HashMap<>();

        synchronized (ReportRepushServiceImpl.class){

            if(ReportRepushUtil.isAllTaskDone(futureTaskList)){
                futureTaskList.clear();
            }else{
                result.put("result", "fail");
                result.put("msg", "当前存在一个推送任务未完成，请稍后再试");
                return result;
            }


            String start_time = params.get("start_time");
            String end_time = params.get("end_time");
            String clientid = params.get("clientid");
            List<String> tableList;
            Map<String, Object> sqlParams = new HashMap<>();

            String identify = params.get("identify");
            if(StringUtils.isNotBlank(identify)){
    //            tableList = commonService.getExistTableForAccess(DbConstant.DbType.ucpaas_message_access_slave, DbConstant.TablePrefix.t_sms_access_, start_time,end_time,identify);
                tableList = commonService.getExistTable(DbConstant.DbType.ucpaas_message_access_slave, DbConstant.TablePrefix.t_sms_access_,start_time,end_time,identify);
            }else{
                tableList = commonService.getExistTableForAccess(DbConstant.DbType.ucpaas_message_access_slave, DbConstant.TablePrefix.t_sms_access_, start_time,end_time,clientid);
            }
            if (tableList.size() == 0) {
                result.put("result", "success");
                result.put("msg", "当前选择推送记录数为0，请选择推送记录");
                return result;
            }else{
                sqlParams.put("table_list", tableList);
            }

            String talbeName = tableList.get(0);
            String isRepushAll = params.get("isRepushAll"); // 是否推送查询出来的所有记录
            String isForceRepush = params.get("isForceRepush"); // 是否强制推送
            boolean forceRepush = false;
            if(StringUtils.isNotBlank(isForceRepush)){
                forceRepush = true;
            }

            if(StringUtils.isBlank(isRepushAll)){
                forceRepush = true;
                ExecutorService executor = Executors.newFixedThreadPool(1);
                ReportRepushUtil.reportRepushTaskNum = 1;
                ReportRepushUtil.reportRepushCompleteTaskNum = new AtomicInteger(0);
                String id = params.get("id");
                List<String> idList = Arrays.asList(id.split(","));
                sqlParams.put("idList", idList);
                List<Map<String, Object>> accessList = accessSlaveDao.selectList("reportRepush.queryAccessRepushRecordByIds", sqlParams);

                FutureTask<String> task = new FutureTask<>(new ReportRepushTask("指定记录状态报告推送任务", talbeName, forceRepush, accessList));
                executor.submit(task);
                executor.shutdown();

                futureTaskList.add(task);

                result.put("result", "success");
                result.put("msg", "状态报告正在推送中");
            }else{

                // 查询需要重推状态报告的记录数
                int repushCount = 0;

                sqlParams.putAll(params);

                repushCount = accessSlaveDao.getSearchSize("reportRepush.queryRepushCount", sqlParams);

                if(repushCount > 0){

                    // 单个推送任务处理的数据数量
                    int taskProccessNum = ConfigUtils.report_repush_one_task_process_num == null ? 5000 : ConfigUtils.report_repush_one_task_process_num;
                    // 任务数量
                    int taskNum = (int) Math.ceil((double) repushCount / taskProccessNum);
                    ReportRepushUtil.reportRepushTaskNum = taskNum;
                    ReportRepushUtil.reportRepushCompleteTaskNum = new AtomicInteger(0);
                    Integer defualtPoolSize = ConfigUtils.report_repush_thread_pool_size == null ? 20 : ConfigUtils.report_repush_thread_pool_size;
                    int poolSize = Math.min(taskNum, defualtPoolSize);
                    ExecutorService executor = Executors.newFixedThreadPool(poolSize);
                    for (int page = 1; page <= taskNum; page++){
                        String limit = "limit " + (page - 1) * taskProccessNum + "," + taskProccessNum;
                        sqlParams.put("limit", limit);
                        List<Map<String, Object>> accessList = accessSlaveDao.selectList("reportRepush.queryAccessRepushRecordByParams", sqlParams);
                        String taskName = clientid + "重推状态报告任务_" + sqlParams.get("limit");
                        FutureTask<String> task = new FutureTask<>(new ReportRepushTask(taskName, talbeName, forceRepush, accessList));
                        executor.submit(task);
                        futureTaskList.add(task);
                    }
                    executor.shutdown();
                    LOGGER.debug("用户{}重推状态报告任务分配结束,分配任务数为{}", clientid, taskNum);
                    result.put("result", "success");
                    result.put("msg", "状态报告正在推送中");
                }else{
                    result.put("result", "success");
                    result.put("msg", "当前选择推送记录数为0，请选择推送记录");
                    return result;
                }

            }
        }

        return result;
    }

    @Override
    public void updateAccessRepushStatus(String tableName, String pKey) {
        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.put("tableName", tableName);
        sqlParams.put("id", pKey);
        accessMasterDao.update("reportRepush.updateAccessRepushStatus", sqlParams);
    }

    @Override
    public Map<String, Object> queryWaitRepushNum(Map<String, String> formData) {
        String queryParamsJsonStr = formData.get("queryParamsJsonStr");
        Map<String, Object> sqlParams = JsonUtils.toObject(queryParamsJsonStr, Map.class);

        Map<String, Object> result = new HashMap<>();
        result.put("waitRepushNum", accessMasterDao.getSearchSize("reportRepush.queryWaitRepushNum", sqlParams));
        return result;
    }
}
