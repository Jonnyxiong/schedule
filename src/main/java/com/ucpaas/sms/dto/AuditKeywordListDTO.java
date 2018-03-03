package com.ucpaas.sms.dto;

import com.jsmsframework.audit.entity.JsmsAuditKeywordList;
import org.apache.commons.lang3.time.DateFormatUtils;

public class AuditKeywordListDTO extends JsmsAuditKeywordList {

    private Integer rowNum; // 更新时间
    private String operatorStr; // 操作者
    private String updateDateStr; // 更新时间

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getOperatorStr() {
        return operatorStr;
    }

    public void setOperatorStr(String operatorStr) {
        this.operatorStr = operatorStr;
    }

    public String getUpdateDateStr() {
        if (getUpdateDate() != null){
            updateDateStr = DateFormatUtils.format(getUpdateDate(), "yyyy-MM-dd HH:mm:ss");
        }
        return updateDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }
}
