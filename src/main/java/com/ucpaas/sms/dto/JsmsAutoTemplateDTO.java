package com.ucpaas.sms.dto;

import com.jsmsframework.audit.entity.JsmsAutoTemplate;
import org.joda.time.DateTime;

public class JsmsAutoTemplateDTO extends JsmsAutoTemplate {

    private String adminName; // 审核人名称
    private String userName;  //创建人名称
    private String smsTypeStr; // 模板属性
    private String templateTypeStr; // 模板属性
    private String submitTypeStr; // 提交来源
    private String createTimeStr; // 创建时间
    private String updateTimeStr; // 更新时间
    private String stateStr; // 审核状态


    public String getUserName() {
        if(getSubmitType() != null && getSubmitType().equals(3)){
            userName = "-";
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getSmsTypeStr() {
        if(getSmsType() == null){

        }else if(getSmsType().equals(10)){
            smsTypeStr = "行业";
        }else if(getSmsType().equals(11)){
            smsTypeStr = "营销";
        }
        return smsTypeStr;
    }

    public void setSmsTypeStr(String smsTypeStr) {
        this.smsTypeStr = smsTypeStr;
    }

    public String getSubmitTypeStr() {
        if(getSubmitType() == null){
        }else if(getSubmitType().equals(0)){
            submitTypeStr = "客户提交";
        }else if(getSubmitType().equals(1)){
            submitTypeStr = "代理商提交";
        }else if(getSubmitType().equals(2)){
            submitTypeStr = "平台提交";
        }else if(getSubmitType().equals(3)){
            submitTypeStr = "系统自动提交";
        }
        return submitTypeStr;
    }

    public void setSubmitTypeStr(String submitTypeStr) {
        this.submitTypeStr = submitTypeStr;
    }

    public String getCreateTimeStr() {
        if(getCreateTime() != null){
            createTimeStr = new DateTime(getCreateTime()).toString("yyyy-MM-dd HH:mm:ss");;
        }
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        if(getUpdateTime() != null){
            updateTimeStr = new DateTime(getUpdateTime()).toString("yyyy-MM-dd HH:mm:ss");;
        }
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public String getStateStr() {
        if (getState() == null){

        }else if (getState().equals(0)){
            stateStr = "待审核";
        }else if (getState().equals(1)){
            stateStr = "审核通过";
        }else if (getState().equals(3)){
            stateStr = "审核不通过";
        }else if (getState().equals(4)){
            stateStr = "已删除";
        }
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public String getTemplateTypeStr() {
        if (getTemplateType() == null){

        }else if (getTemplateType().equals(0)){
            templateTypeStr = "固定模板";
        }else if (getTemplateType().equals(1)){
            templateTypeStr = "变量模板";
        }
        return templateTypeStr;
    }

    public void setTemplateTypeStr(String templateTypeStr) {
        this.templateTypeStr = templateTypeStr;
    }
}
