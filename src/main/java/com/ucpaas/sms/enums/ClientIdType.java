package com.ucpaas.sms.enums;

public enum ClientIdType {
	
	agentClientId("归属于代理商的子账号", 0),
	
	directClientId("直客的子账号", 1);
	
	private String name;
	private Integer value;

	ClientIdType(String name, Integer value) {
    	this.name = name;
        this.value = value;
    }
	
    public static ClientIdType getInstance(Integer value){
    	for (ClientIdType type : ClientIdType.values()) {
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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}

