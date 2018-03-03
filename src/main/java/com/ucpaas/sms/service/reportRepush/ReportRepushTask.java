package com.ucpaas.sms.service.reportRepush;

import com.ucpaas.sms.util.ReportRepushUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;


public class ReportRepushTask extends AbstractTask {

    private static final Logger LOGGER = LoggerFactory.getLogger("ReportRepushService");

    String taskName;
    String tableName;
    boolean forceRepush;
    List<Map<String, Object>> accessList;

    public ReportRepushTask(String taskName, String tableName, boolean forceRepush, List<Map<String, Object>> accessList){
        this.taskName = taskName;
        this.tableName = tableName;
        this.forceRepush = forceRepush;
        this.accessList = accessList;
    }

    @Override
    public String doAction() throws Exception {
        long start = System.currentTimeMillis();
        LOGGER.debug("重推状态报告任务开始，taskName={},推送数量={}", taskName, accessList.size());
        ReportRepushUtil.repushBatch(taskName, tableName, forceRepush, accessList);
        int completeTaskNum = ReportRepushUtil.reportRepushCompleteTaskNum.addAndGet(1);
        int taskNum = ReportRepushUtil.reportRepushTaskNum;
        LOGGER.debug("重推状态报告任务结束，taskName={},推送数量={},耗时={}ms,总任务数{}个,当前已完成任务数{}个",
                        taskName, accessList.size(), System.currentTimeMillis() - start, taskNum, completeTaskNum);
        return String.valueOf(accessList.size());
    }

}
