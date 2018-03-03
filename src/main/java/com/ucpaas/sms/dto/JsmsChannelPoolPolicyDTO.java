package com.ucpaas.sms.dto;

import com.jsmsframework.channel.entity.JsmsChannelPoolPolicy;

/**
 * Created by Don on 2017/9/29.
 */
public class JsmsChannelPoolPolicyDTO extends JsmsChannelPoolPolicy {

    private String updateName;

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
}
