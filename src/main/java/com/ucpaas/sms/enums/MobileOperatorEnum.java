package com.ucpaas.sms.enums;

/**
 * 手机号码运营商类型
 */
public enum MobileOperatorEnum {
	CHINA_MOBILE("1", "移动号码"),
	CHINA_UNICOM("2", "联通号码"),
	CHINA_TELECOM("3", "电信号码"),
	INTERNATIONAL_MOBILE("4", "国际号码");

	private String value;
	private String desc;

	private MobileOperatorEnum(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDescByValue(String value) {
		if(value == null){ return null;}
		String result = null;
		for (MobileOperatorEnum s : MobileOperatorEnum.values()) {
			if (value.equals(s.getValue())) {
				result = s.getDesc();
				break;
			}
		}
		return result;
	}


}