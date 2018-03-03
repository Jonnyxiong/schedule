package com.ucpaas.sms.enums;

/**
 * 短信用户获取状态报告的方式
 */
public enum NeedReportType {

	不需要(0),

	需要简单状态报告(1),

	需要透传状态报告(2),

	用户主动拉取状态报告(3);

	private Integer value;

	NeedReportType(Integer value) {
        this.value = value;
    }
	
    public static NeedReportType getInstance(Integer value){
    	for (NeedReportType type : NeedReportType.values()) {
			if(type.getValue().equals(value)){
				return type;
			}
		}
    	return null;
    }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}

