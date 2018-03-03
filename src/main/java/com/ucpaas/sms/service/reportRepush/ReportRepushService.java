package com.ucpaas.sms.service.reportRepush;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 重推状态报告
 */
public interface ReportRepushService {

    /**
     * 提交重推状态报告请求
     * @param params
     * @return
     */
    Map<String, Object> submitRepush(Map<String, String> params);

    /**
     * 更新Access表流水记录是否重推状态报告状态
     * @param tableName
     * @param pKey
     */
    void updateAccessRepushStatus(String tableName, String pKey);

    /**
     * 查询待推送的数量
     * @param formData
     * @return
     */
    Map<String,Object> queryWaitRepushNum(Map<String, String> formData);
}
