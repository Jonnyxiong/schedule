package com.ucpaas.sms.enums;

/**
 * t_sms_account表HTTPS协议的分类
 */
public enum HttpProtocolType {

	HTTPS_JSON("https json", 0),

	HTTPS_GETPOST("https get/post", 1),

	HTTPS_TXJSON("https tx-json", 2);

	private String name;
	private Integer value;

	HttpProtocolType(String name, Integer value) {
    	this.name = name;
        this.value = value;
    }
	
    public static HttpProtocolType getInstance(Integer value){
    	for (HttpProtocolType type : HttpProtocolType.values()) {
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

