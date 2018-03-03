package com.ucpaas.sms.model;

/**
 * Created by dylan on 2017/9/9.
 * 短信发送测试
 */
public class TestSendRequest {

    /**
     * 账号, 6位
     * (必选)
     */
    private String clientid;
    /**
     * 密码, md5加密, 32位 , 小写
     * (必选)
     */
    private String password;
    /**
     * 发送手机号码, 国内短信不要加前缀, 国际短信号码必须带响应的国家区号
     * (必选)
     */
    private String mobile;
    /**
     * 短信类型
     * (必选)
     */
    private String smstype;
    /**
     * 【签名】 + 短信内容， utf-8编码， 短信内容最长不能超过500个字符（包括英文字母）
     * (必选)
     */
    private String content;
    /**
     * 定时发送时间
     * (可选)
     */
    private String sendtime;
    /**
     * 自扩展端口，12位以内纯数字
     * (可选)
     */
    private String extend;
    /**
     * 用户透传ID，随状态报告返回，最长60位
     * (可选)
     */
    private String uid;
    /**
     * 通道ID
     * (可选)
     */
    private String channelid;

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmstype() {
        return smstype;
    }

    public void setSmstype(String smstype) {
        this.smstype = smstype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }


}
