package com.ucpaas.sms.service.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
 * 用户短信通道管理-通道管理
 * 
 * @author oylx
 */
@Service
@Transactional
public class ChannelGroupServiceImpl implements ChannelGroupService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelGroupServiceImpl.class);

	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("channelGroup.query", "channelGroup.queryCount", params);
	}

	@Override
	public Map<String, Object> view(int channelgroupid) {
		return masterDao.getOneInfo("channelGroup.view", channelgroupid);
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		LOGGER.debug("保存通道组，添加/修改：" + params);

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> check = masterDao.getOneInfo("channelGroup.checkSave", params);// 查重
		if (check != null) {
			data.put("result", "fail");
			data.put("msg", "通道组名称已被使用，请重新输入");
			return data;
		}
		
		String operatorstype = Objects.toString(params.get("operatorstype"), "");
		String channelid = Objects.toString(params.get("channelid"), "");
		String ydchannelid = Objects.toString(params.get("ydchannelid"), "");
		String ltchannelid = Objects.toString(params.get("ltchannelid"), "");
		String dxchannelid = Objects.toString(params.get("dxchannelid"), "");
		String gjchannelid = Objects.toString(params.get("gjchannelid"), "");
		if(operatorstype.equals("0")){
			params.put("channelid", channelid);
		}else if(operatorstype.equals("1")){
			params.put("channelid", ydchannelid);
		}else if(operatorstype.equals("2")){
			params.put("channelid", ltchannelid);
		}else if(operatorstype.equals("3")){
			params.put("channelid", dxchannelid);
		}else if(operatorstype.equals("4")){
			params.put("channelid", gjchannelid);
		}else{
		}
		
		if (StringUtils.isBlank(params.get("channelgroupid"))) {// 添加
			int i = masterDao.insert("channelGroup.insertChannelGroup", params);
			if (i > 0) {
				// 获得插入通道明细表后的自增长channelgroupid
				String channelgroupid = ObjectUtils.toString(params.get("channelgroupid"));
				params.put("channelgroupid", channelgroupid);
				String [] channelidArray = params.get("channelid").split(",");
				int inertRes = 0;
				for (int pos = 0; pos < channelidArray.length; pos++) {
					String chanlid=channelidArray[pos];
					String[] chan= chanlid.split("\\|");


					params.put("channelid", chan[0]);
					params.put("weight",chan[1]);
					params.put("sort", String.valueOf(pos));
					// 插入通道与通道组关系表
					inertRes += masterDao.insert("channelGroup.insertChannelGroupRef", params);
				}
				if(inertRes != channelidArray.length){
					data.put("result", "fail");
					data.put("msg", "添加失败");
					return data;
				}
			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
				return data;
			}
			data.put("msg", "保存成功");
			logService.add(LogType.add, LogEnum.通道管理.getValue(),"通道组管理：添加通道组", params, data);
		} else {
			// 更新通道组明细表
			int i = masterDao.update("channelGroup.updateChannelGroup", params);
			if (i == 0) {
				data.put("result", "fail");
				data.put("msg", "通道组不存在，修改失败");
				return data;
			}
			// 更新通道组关系表
			masterDao.delete("channelGroup.deleteChannelGroupRef", params);
			String [] channelidArray = params.get("channelid").split(",");
			int inertRes = 0;
			for (int pos = 0; pos < channelidArray.length; pos++) {

				String[] chan= channelidArray[pos].split("\\|");

				params.put("channelid", chan[0]);
				params.put("weight",chan[1]);
				params.put("sort", String.valueOf(pos));
				// 插入通道与通道组关系表
				inertRes += masterDao.insert("channelGroup.insertChannelGroupRef", params);
			}
			if(inertRes != channelidArray.length){
				data.put("result", "fail");
				data.put("msg", "修改通道组失败");
				return data;
			}
			data.put("msg", "修改成功");
			logService.add(LogType.update, LogEnum.通道管理.getValue(),"通道组管理：修改通道组", params, data);
		}
		data.put("result", "success");
		return data;
	}

	@Override
	public Map<String, Object> updateStatus(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		int i = masterDao.update("channelGroup.updateStatus", params);
		String msg = "操作";
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", msg + "成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "通道组不存在，" + msg + "失败");
		}
		logService.add(LogType.update,LogEnum.通道管理.getValue(), "通道组管理：修改通道组", msg, params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> delete(Map<String, String> params){
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Long> map = masterDao.getOneInfo("channelGroup.checkInuse", params);
		if(map.get("count") > 0){
			data.put("result", "fail");
			data.put("msg", "通道组正在使用中，不能删除");
			return data;
		}
		int i = masterDao.delete("channelGroup.delete", params);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", "删除通道组成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "删除通道组失败");
		}
		logService.add(LogType.delete, LogEnum.通道管理.getValue(),"通道组管理-通道组管理：删除通道组", params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> switchChannel(Map<String, String> params){
		Map<String, Object> data = new HashMap<String, Object>();
		String channelid = params.get("channelid");
		String switchChannelid = params.get("switchChannelid");
		if(!NumberUtils.isDigits(channelid)){
			params.put("channelid", "");
		}
		if(!NumberUtils.isDigits(switchChannelid)){
			params.put("switchChannelid", "");
		}
		
		int i = masterDao.update("channelGroup.switchChannel", params);
		data.put("result", "success");
		data.put("msg", "成功切换"+ i +"个通道组的" + channelid + "为" + switchChannelid);
		
		logService.add(LogType.update, LogEnum.通道管理.getValue(),"通道组管理-通道组管理：切换通道组", params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> queryChannelGroupByOperator() {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> quanwang = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> yidong = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> liantong = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dianxin = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> guoji = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("operatorstype", 0);
		quanwang = masterDao.getSearchList("channelGroup.queryChannelGroupByOperater", sqlParams);
		sqlParams.put("operatorstype", 1);
		yidong = masterDao.getSearchList("channelGroup.queryChannelGroupByOperater", sqlParams);
		sqlParams.put("operatorstype", 2);
		liantong = masterDao.getSearchList("channelGroup.queryChannelGroupByOperater", sqlParams);
		sqlParams.put("operatorstype", 3);
		dianxin = masterDao.getSearchList("channelGroup.queryChannelGroupByOperater", sqlParams);
		sqlParams.put("operatorstype", 4);
		guoji = masterDao.getSearchList("channelGroup.queryChannelGroupByOperater", sqlParams);
		
		
		data.put("quanwang", quanwang);
		data.put("yidong", yidong);
		data.put("liantong", liantong);
		data.put("dianxin", dianxin);
		data.put("guoji", guoji);
		
		return data;
	}

}
