package com.ucpaas.sms.service.userconfig;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

public interface ChannelNorouteService {
	
	PageContainer query(Map<String, String> params);
	
	Map<String, Object> updateStatus(Map<String, String> params);

}
