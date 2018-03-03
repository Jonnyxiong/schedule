package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

public interface SmsOprateLogService {

	PageContainer query(Map<String, String> formData);

}
