package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import com.ucpaas.sms.model.PageContainer;
/**
 * @ClassName: ComponentRunInfoService
 * @Description: 组件运行信息service
 * @author: Niu.T
 * @date: 2016年11月1日  下午7:55:49
 */
public interface ComponentRunInfoService {
	/**
	 * @Description: 查询组件运行信息
	 * @author: Niu.T 
	 * @date: 2016年11月1日    下午8:01:42
	 * @param params
	 * @return PageContainer
	 */
	PageContainer query(Map<String, String> params);
}	
