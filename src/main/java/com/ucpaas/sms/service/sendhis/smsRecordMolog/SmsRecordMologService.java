package com.ucpaas.sms.service.sendhis.smsRecordMolog;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

public interface SmsRecordMologService {

	PageContainer query(Map<String, String> formData);

}
