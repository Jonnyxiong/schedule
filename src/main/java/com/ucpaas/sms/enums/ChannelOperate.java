package com.ucpaas.sms.enums;

public enum ChannelOperate {
	
	CLOSE("关闭", 0),
	
	OPEN("开启", 1),
	
	SUSPEND("暂停", 2),
	
	OFF_LINE("下架", 3),
	
	PUSH_ONLINE("重新上架", 99);
	
	private String name;
	private Integer value;

	ChannelOperate(String name, Integer value) {
    	this.name = name;
        this.value = value;
    }
	
    public static ChannelOperate getOperate(Integer value){
    	for (ChannelOperate e : ChannelOperate.values()) {
			if(e.getValue().equals(value)){
				return e;
			}
		}
    	return null;
    }
    
    public static String getNameByValue(Integer value){
    	for (ChannelOperate type : ChannelOperate.values()) {
			if(type.getValue().equals(value)){
				return type.name;
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
	
	public static void main(String[] args) {
		System.out.println("zzzzzzzzzzzzzzzzzzzzzzz" + ChannelOperate.CLOSE.getValue().toString());
	}

}

