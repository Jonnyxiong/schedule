package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-节点IP对应关系
 * 
 * @author oylx
 */
public interface IP4NoteService {
	PageContainer query(Map<String, String> params);
}
