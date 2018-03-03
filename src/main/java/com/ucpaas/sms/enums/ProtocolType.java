package com.ucpaas.sms.enums;

public enum ProtocolType {

	SMPP("SMPP", 2),

	CMPP("CMPP", 3),

	SGIP("SGIP", 4),

	SGMP("SGMP", 5),

	HTTPS("HTTPS", 6);

	private String name;
	private Integer value;

	ProtocolType(String name, Integer value) {
    	this.name = name;
        this.value = value;
    }
	
    public static ProtocolType getInstance(Integer value){
    	for (ProtocolType type : ProtocolType.values()) {
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

