package com.ucpaas.sms.service.sysconf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;
/**
 * @ClassName: ComponentRunInfoServiceImpl
 * @Description: 组件运行信息service
 * @author: Niu.T
 * @date: 2016年11月1日  下午7:54:59
 */
@Service
public class ComponentRunInfoServiceImpl implements ComponentRunInfoService {
	@Autowired
	private MessageMasterDao masterDao;
	/**
	 * 获取组件运行的信息
	 */
	@Override
	public PageContainer query(Map<String, String> params) {
		//if(params.get("start_time").substring(0,9).equals(params.get("end_time").substring(0,9)) ){
		// 判断选择的时间是否是当天
		SimpleDateFormat dateFormat =   new SimpleDateFormat( "yyyy-MM-dd" );
		String today = dateFormat.format(new Date());
//		String startTime = params.get("start_time");
//		String endTime = params.get("end_time");
		if(params.get("start_time") != null && !params.get("start_time").contains(today)){
			params.put("table_name","t_sms_component_run_info_bak"); //不是当天
		}else{
			params.put("table_name","t_sms_component_run_info"); //是当天
		}
		return masterDao.getSearchPage("componentRunInfo.query", "componentRunInfo.queryCount", params);
	}

}
