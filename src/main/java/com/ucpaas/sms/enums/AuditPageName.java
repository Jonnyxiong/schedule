package com.ucpaas.sms.enums;

import com.ucpaas.sms.service.smsAudit.SmsAuditServiceImpl;

public enum AuditPageName {

	ORDINARY_NUM("普通审核页面", SmsAuditServiceImpl.ORDINARY_NUM),
	YZM_AUDIT_PAGE("验证码审核页面", SmsAuditServiceImpl.YZM_NUM),
	MAJOR_AUDIT_PAGE("重要客户审核页面", SmsAuditServiceImpl.MAJOR_NUM),
	AUDIT_QUERY_PAGE("审核查询页面", "");
	
	
	private String name;
	private String value;

	AuditPageName(String name, String value) {
    	this.name = name;
        this.value = value;
    }
	
    public static AuditPageName getInstance(String value){
    	for (AuditPageName type : AuditPageName.values()) {
			if(type.getValue().equals(value)){
				return type;
			}
		}
    	return null;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

