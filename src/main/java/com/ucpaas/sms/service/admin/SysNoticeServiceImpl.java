package com.ucpaas.sms.service.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;

/**
 * 管理员中心-系统通知管理
 * 
 * @author zenglb
 */
@Service
@Transactional
public class SysNoticeServiceImpl implements SysNoticeService {
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;
	
	private static final String NOTE = "0";
	private static final String EMAIL = "1";
	private static final String NOTE_EMAIL = "0,1";

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("sysNotice.query", "sysNotice.queryCount", params);
	}

	@Override
	public Map<String, Object> view(Integer noticeId) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("view", masterDao.getOneInfo("sysNotice.view", noticeId));
		data.put("allTimeRange", masterDao.getSearchList("sysNotice.allTimeRange", null));
		return data;
	}

	@Override
	public Map<String, Object> save(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (masterDao.getSearchSize("sysNotice.check", params) > 0) {// 查重
			data.put("result", "fail");
			data.put("msg", "通知时段名称已被使用，请重新输入");
			return data;
		}

		Integer noticeId = NumberUtils.createInteger(params.get("notice_id"));
		if (noticeId == null) {// 添加审核通知时段
			int i = masterDao.insert("sysNotice.insert", params);
			if (i > 0) {
				String user_ids = params.get("user_id");
				if (StringUtils.isNotBlank(user_ids)) {
					data = masterDao.getOneInfo("sysNotice.getId", params.get("name"));
					noticeId = Integer.valueOf(data.get("notice_id").toString());

					List<Map<String, Object>> noticeUserList = new ArrayList<Map<String, Object>>();
					Map<String, Object> p;
					for (String user_id : user_ids.split(",")) {
						p = new HashMap<String, Object>();
						p.put("notice_id", noticeId);
						p.put("user_id", user_id);
						p.put("alarm_type", getAlarmType(params.get("alarm_type")));
						//获取用户信息
						Map<String, Object> user = masterDao.getOneInfo("admin.getAdmin", user_id);
						if(null != user && !user.isEmpty()){
							p.put("user_mobile", user.get("mobile"));
							p.put("user_email", user.get("email"));
						}
						noticeUserList.add(p);
					}
					masterDao.insert("sysNotice.insertNoticeUser", noticeUserList);// 添加审核通知时段和用户的关系表
				}
				data.put("result", "success");
				data.put("msg", "添加成功");

			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
			}
			logService.add(LogType.add,LogEnum.管理中心.getValue(), "信息管理-管理员中心-审核通知管理：添加审核通知时段", params, data);

		} else {// 修改审核通知时段
			int i = masterDao.update("sysNotice.update", params);
			if (i > 0) {
				if ("true".equals(params.get("is_update_user"))) {
					masterDao.delete("sysNotice.deleteNoticeUser", noticeId);

					String user_ids = params.get("user_id");
					if (StringUtils.isNotBlank(user_ids)) {
						List<Map<String, Object>> noticeUserList = new ArrayList<Map<String, Object>>();
						Map<String, Object> p;
						for (String user_id : user_ids.split(",")) {
							p = new HashMap<String, Object>();
							p.put("notice_id", noticeId);
							p.put("user_id", user_id);
							p.put("alarm_type", getAlarmType(params.get("alarm_type")));
							//获取用户信息
							Map<String, Object> user = masterDao.getOneInfo("admin.getAdmin", user_id);
							if(null != user && !user.isEmpty()){
								p.put("user_mobile", user.get("mobile"));
								p.put("user_email", user.get("email"));
							}
							noticeUserList.add(p);
						}
						masterDao.insert("sysNotice.insertNoticeUser", noticeUserList);
					}
				}else {//修改告警方式
					String user_ids = params.get("user_id");
					if (StringUtils.isNotBlank(user_ids)) {
						Map<String, Object> p = new HashMap<String, Object>();
						p.put("notice_id", noticeId);
//						p.put("alarm_type", params.get("alarm_type"));
						p.put("alarm_type", getAlarmType(params.get("alarm_type")));
						masterDao.insert("sysNotice.updateAlarmType", p);
					}
				}

				data.put("result", "success");
				data.put("msg", "修改成功");

			} else {
				data.put("result", "fail");
				data.put("msg", "审核通知时段不存在，修改失败");
			}
			logService.add(LogType.update,LogEnum.管理中心.getValue(), "信息管理-管理员中心-审核通知管理：修改审核通知时段", params, data);
		}
		return data;
	}

	@Override
	public Map<String, Object> updateStatus(int noticeId, int status) {
		Map<String, Object> data = new HashMap<String, Object>();
		String msg;
		LogType logType = null ;
		switch (status) {
		case 0:
			msg = "关闭";
			logType = LogType.update;
			break;
		case 1:
			msg = "启用";
			logType = LogType.update;
			break;
		case 2:
			msg = "删除";
			logType = LogType.delete;
			break;
		default:
			data.put("result", "fail");
			data.put("msg", "状态不正确，操作失败");
			return data;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notice_id", noticeId);
		params.put("status", status);
		int i = masterDao.update("sysNotice.updateStatus", params);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", msg + "成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "审核通知时段不存在，" + msg + "失败");
		}
		logService.add(logType,LogEnum.管理中心.getValue(), "信息管理-管理员中心-审核通知管理：修改审核通知时段状态", params, data);
		return data;
	}
	
	/**
	 * AlarmType 1、2、3  分别代表  Email、短信、短信&Email
	 */
	private String getAlarmType( String initialAlarmType ){
		
		if(initialAlarmType.equals(EMAIL)){
			return "1";
		}else if(initialAlarmType.equals(NOTE)){
			return "2";
		}else if(initialAlarmType.equals(NOTE_EMAIL)){
			return "3";
		}else{
			return "0";
		}
	}

}
