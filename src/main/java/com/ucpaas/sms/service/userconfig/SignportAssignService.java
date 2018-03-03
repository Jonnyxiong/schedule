package com.ucpaas.sms.service.userconfig;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;

/**
 * 签名端口分配范围表 t_sms_clientid_signport_assign
 * @author TonyHe
 *
 */
public interface SignportAssignService{
	
	PageContainer query(Map<String, String> params);

}
