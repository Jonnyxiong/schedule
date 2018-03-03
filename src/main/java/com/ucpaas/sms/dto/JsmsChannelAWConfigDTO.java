package com.ucpaas.sms.dto;

import com.jsmsframework.channel.entity.JsmsChannelAttributeWeightConfig;

/**
 * Created by Don on 2017/9/30.
 */
public class JsmsChannelAWConfigDTO extends JsmsChannelAttributeWeightConfig {


    private String updateName;


    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }


}
