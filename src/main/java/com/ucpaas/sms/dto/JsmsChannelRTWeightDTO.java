package com.ucpaas.sms.dto;

import com.jsmsframework.channel.entity.JsmsChannelAttributeRealtimeWeight;

/**
 * Created by Don on 2017/10/9.
 */
public class JsmsChannelRTWeightDTO extends JsmsChannelAttributeRealtimeWeight{

    private String updateName;

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
}
