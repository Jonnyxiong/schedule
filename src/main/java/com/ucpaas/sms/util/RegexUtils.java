package com.ucpaas.sms.util;

import com.ucpaas.sms.enums.MobileOperatorEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	private static final Logger LOG = LoggerFactory.getLogger(RegexUtils.class);
	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Throwable e) {
			LOG.error("验证邮箱地址错误", e);
			flag = false;
		}

		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Throwable e) {
			LOG.error("验证手机号码错误", e);
			flag = false;
		}
		return flag;
	}
	
	public static boolean isMobile(String patterns){
		boolean flag = false;
		List<String> regexList = new ArrayList<>();
		regexList.add("^13\\d{9}$");
		regexList.add("^14[5|7|9]\\d{8}$");
		regexList.add("^15[0|1|2|3|5|6|7|8|9]\\d{8}$");
		regexList.add("^18\\d{9}$");
		regexList.add("^170\\d{8}$");
		regexList.add("^17[1|3|5|6|7|8]\\d{8}$");
		regexList.add("^173\\d{8}$");
		for (String regex : regexList) {
			try {
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(patterns);
				flag = m.matches();
				if(flag){
					break ;
				}
			} catch (Exception e) {
				flag = false;
				continue;
			}
		}
		return flag;
	}

	/**
	 * 是否是移动号码
	 *
	 * @return
	 */
	public static boolean isCMCCMobile(String phone){
		if(StringUtils.isEmpty(phone)){
			return false ;
		}

		if(phone.matches("/^(((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8})|((170[3,5,6])\\d{7})$/")){
			return true ;
		}
		return false ;
	}

	/**
	 * 是否是联通号码
	 * @return
	 */
	public static boolean isCUCCMobile(String phone){
		if(StringUtils.isEmpty(phone)){
			return false ;
		}
		if(phone.matches("/^(((13[0-2])|(145)|(15[5,6])|(17[1,5,6])|(18[5,6]))\\d{8}$)|(170[4,7,8,9]\\d{7})$/")){
			return true ;
		}
		return false ;
	}

	/**
	 * 是否是电信号码
	 * @return
	 */
	public static boolean isCTCCMobile(String phone){
		if(StringUtils.isEmpty(phone)){
			return false ;
		}
		if(phone.matches("/^(((133)|(149)|(153)|(17[3,7])|(18[0,1,9]))\\d{8})|((170[0,1,2])|(1731)\\d{7})$/")){
			return true ;
		}
		return false ;
	}

	/**
	 * 是否是电信号码
	 * @return
	 */
	public static boolean isInternationMobile(String phone){
		if(StringUtils.isEmpty(phone)){
			return false ;
		}
		if(phone.matches("/^0{2}\\d{8,18}$/")){
			return true ;
		}
		return false ;
	}

	public static boolean isOverSeaMobile(String phone){
		if(StringUtils.isEmpty(phone)){
			return false ;
		}
		if(phone.startsWith("00") && phone.length()>10){
			return true ;
		}
		return false ;
	}

	/**
	 * 校验是否是合法的短信子账号clientId
	 * @param str
	 * @return
	 */
	public static boolean isClientId(String str){
		boolean flag = false;
		try {
			String check = "^[a-zA-Z0-9]{6}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(str);
			flag = matcher.matches();
		} catch (Throwable e) {
			LOG.error("验证邮箱地址错误", e);
			flag = false;
		}

		return flag;
	}
	
	/**
	 * @param mobile
	 * @return "1"：移动  "2"：联通  "3"：电信  "4":国际
	 */
	public static String getMobileOperator(String mobile) {

		// 从t_sms_operater_segment运营商号段表获得的一下匹配信息
		// 移动手机号码正则
		String chinaMobileRegex = "^(((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8})|((170[3,5,6])\\d{7})$";
		// 联通手机号码正则
		String chinaUnicomRegex = "^(((13[0-2])|(145)|(15[5,6])|(17[1,5,6])|(18[5,6]))\\d{8}$)|(170[4,7,8,9]\\d{7})$";
		// 电信手机号码正则
		String chinaTelecomRegex = "^(((133)|(149)|(153)|(17[3,7])|(18[0,1,9]))\\d{8})|((170[0,1,2])|(1731)\\d{7})$";
		// 国际手机号码
		String internationTeleRegex = "^0{2}\\d{8,18}$";

		List<String> operatorRegexList = new ArrayList<String>();
		operatorRegexList.add(chinaMobileRegex);
		operatorRegexList.add(chinaUnicomRegex);
		operatorRegexList.add(chinaTelecomRegex);
		operatorRegexList.add(internationTeleRegex);
		String operatorType = null;
		try {
			for (int pos = 0; pos < operatorRegexList.size(); pos ++){
				Pattern p = Pattern.compile(operatorRegexList.get(pos));
				Matcher m = p.matcher(mobile);
				if(m.matches()){
					if(pos == 0){// 移动
						operatorType = MobileOperatorEnum.CHINA_MOBILE.getValue();
					}else if(pos == 1){// 联通
						operatorType = MobileOperatorEnum.CHINA_UNICOM.getValue();
					}else if(pos == 2){// 电信
						operatorType = MobileOperatorEnum.CHINA_TELECOM.getValue();
					}else if(pos == 3){// 国际
						operatorType = MobileOperatorEnum.INTERNATIONAL_MOBILE.getValue();
					}else{
					}
					
					break;
				}
			}
			if(StringUtils.isBlank(operatorType)){
				if(isOverSeaMobile(mobile)){
					operatorType = MobileOperatorEnum.INTERNATIONAL_MOBILE.getValue();
				}else{
					// 如果最后没有判断出来手机的运营商类型暂时返回移动类型
					operatorType = MobileOperatorEnum.CHINA_MOBILE.getValue();
				}
			}
			
		} catch (Throwable e) {
			LOG.error("验证手机号码运营商类型错误", e);
		}
		return operatorType;
		
	}
	
}
