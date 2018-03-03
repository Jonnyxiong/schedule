package com.ucpaas.sms.service.userconfig;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 用户端口分配范围表t_sms_extendport_assign
 * @author TonyHe
 *
 */
public interface ExtendportAssignService{
	
	PageContainer query(Map<String, String> params);

}
