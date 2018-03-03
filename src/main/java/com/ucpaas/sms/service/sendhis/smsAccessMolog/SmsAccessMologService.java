package com.ucpaas.sms.service.sendhis.smsAccessMolog;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

public interface SmsAccessMologService {

	PageContainer query(Map<String, String> formData);

}
