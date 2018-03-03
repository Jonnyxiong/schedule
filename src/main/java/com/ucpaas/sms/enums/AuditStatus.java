package com.ucpaas.sms.enums;

public enum AuditStatus {
	
	WAIT_TO_AUDIT("待审核", "0"),
	
	PASS("审核通过", "1"),
	
	NOT_PASS("审核不通过", "2"),
	
	TRANSFER_AUDIT("转审", "3");
	
	private String name;
	private String value;

	AuditStatus(String name, String value) {
    	this.name = name;
        this.value = value;
    }
	
    public static AuditStatus getInstance(String value){
    	for (AuditStatus type : AuditStatus.values()) {
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

