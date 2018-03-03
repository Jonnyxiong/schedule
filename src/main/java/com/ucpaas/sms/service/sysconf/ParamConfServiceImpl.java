package com.ucpaas.sms.service.sysconf;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;

/**
 * 系统配置-系统参数
 * 
 * @author zenglb
 */
@Service
@Transactional
public class ParamConfServiceImpl implements ParamConfService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParamConfServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("paramConf.query", "paramConf.queryCount", params);
	}

	@Override
	public Map<String, Object> view(int logId) {
		return masterDao.getOneInfo("paramConf.view", logId);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存参数配置，添加/修改：" + params);

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> check = masterDao.getOneInfo("paramConf.checkSave", params);// 查重
		if (check != null) {
			data.put("result", "fail");
			data.put("msg", "该参数已经配置了,请进行编辑修改");
			return data;
		}
		String param_id = params.get("param_id");
		if (StringUtils.isBlank(param_id)) {// 添加
			int i = masterDao.insert("paramConf.insertParam", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "添加成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
			}
			logService.add(LogType.add, LogEnum.系统配置.getValue(),"系统配置-参数配置：添加系统参数", params, data);
		} else {
			int i = masterDao.update("paramConf.updateParam", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "修改成功");
			} else {
				data.put("result", "fail");
				data.put("msg", "系统参数不存在，修改失败");
			}
			logService.add(LogType.update,LogEnum.系统配置.getValue(), "系统配置-参数配置：修改系统参数", params, data);
		}
		return data;
	}

	@Override
	public Map<String, Object> updateStatus(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int i = masterDao.update("paramConf.updateStatus", params);
		String msg = "操作";
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", msg + "成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "系统参数不存在，" + msg + "失败");
		}
		logService.add(LogType.delete,LogEnum.系统配置.getValue(), "系统配置-参数配置：修改系统参数状态", msg, params, data);
		return data;
	}

}
