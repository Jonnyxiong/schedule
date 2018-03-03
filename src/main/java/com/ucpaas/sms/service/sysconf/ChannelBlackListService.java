package com.ucpaas.sms.service.sysconf;

import java.io.File;
import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-通道黑名单配置
 * 
 * @author oylx
 */
public interface ChannelBlackListService {
	PageContainer query(Map<String, String> params);

	Map<String, Object> save(Map<String, String> params, String id);

	Map<String, Object> view(String id);
	
	Map<String, Object> delete(String id, String phone, String channelId);
	
	Map<String, Object> importExcel(File importExcel, String fileType);
}
