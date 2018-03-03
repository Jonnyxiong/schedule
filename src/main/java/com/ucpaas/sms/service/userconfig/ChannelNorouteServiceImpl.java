package com.ucpaas.sms.service.userconfig;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;

@Service
@Transactional
public class ChannelNorouteServiceImpl implements ChannelNorouteService{
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;
	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("channelNoroute.query", "channelNoroute.queryCount", params);
	}
	@Override
	public Map<String, Object> updateStatus(Map<String, String> params) {
		Map<String, Object> data = new HashMap<>();
		int res = masterDao.update("channelNoroute.updateStatus", params);
		if (res > 0) {
			data.put("result", "success");
			data.put("msg", "更新成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "更新失败");
		}
		logService.add(LogType.update,LogEnum.用户配置.getValue(),"更新平台帐号未配置通道列表",params,data);
		return data;
	}

}
