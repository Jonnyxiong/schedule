package com.ucpaas.sms.service.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.constant.UserConstant;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.MD5;
import com.ucpaas.sms.util.api.RestUtils;
import com.ucpaas.sms.util.api.RestUtils.SmsTemplateId;
import com.ucpaas.sms.util.encrypt.EncryptUtils;
import com.ucpaas.sms.util.web.StrutsUtils;

/**
 * 管理员中心
 * 
 * @author xiejiaan
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public Map<String, Object> getAdmin(Long id) {
		return masterDao.getOneInfo("admin.getAdmin", id);
	}

	@Override
	public Map<String, Object> saveAdmin(Map<String, String> params) {
		LOGGER.debug("保存管理员资料，添加/修改：" + params);
		
		Map<String, Object> data = new HashMap<>();

		Map<String, Object> check = masterDao.getOneInfo("admin.checkAdmin", params);// 查重
		if (check != null) {
			if (params.get("email").equals(check.get("email"))) {
				data.put("result", "fail");
				data.put("msg", "管理账号已被使用，请重新输入");
				return data;

			} else if (params.get("mobile").equals(check.get("mobile"))) {
				data.put("result", "fail");
				data.put("msg", "联系手机已被使用，请重新输入");
				return data;
			}
		}

		String password = params.get("password");
		if (StringUtils.isNotBlank(password)) {
			password = EncryptUtils.encodeMd5(password);
			password = MD5.md5(password);
			params.put("password", password);
		} else {
			params.remove("password");
		}

		String id = params.get("id");
		if (StringUtils.isBlank(id)) {// 添加管理员
			String sid = EncryptUtils.generateSid();

			params.put("sid", sid);
			int i = masterDao.insert("admin.insertAdmin", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "添加成功");
				params.put("user_id", String.valueOf(params.get("id")));
				masterDao.insert("admin.insertAdminRole", params);// 添加角色
			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
			}
			logService.add(LogType.add, LogEnum.管理中心.getValue(),"管理员中心-管理员管理：添加管理员", params, data);

		} else {// 修改管理员
			int i = masterDao.update("admin.updateAdmin", params);
			if (i > 0) {
				data.put("result", "success");
				data.put("msg", "修改成功");

				String roleId = params.get("role_id");
				if (StringUtils.isNotBlank(roleId) && !roleId.equals(params.get("old_role_id"))) {// 修改角色
					params.put("user_id", String.valueOf(params.get("id")));
					masterDao.update("admin.updateAdminRole", params);
				}
			} else {
				data.put("result", "fail");
				data.put("msg", "管理员不存在，修改失败");
			}
			logService.add(LogType.update,LogEnum.管理中心.getValue(), "管理员中心-管理员管理：修改管理员", params, data);
		}

		return data;
	}


	@Override
	public Map<String, Object> savePassword(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();

		String password = params.get("password");
		password = EncryptUtils.encodeMd5(password);
		password = MD5.md5(password);
		params.put("password", password);

		password = params.get("newPassword");
		password = EncryptUtils.encodeMd5(password);
		password = MD5.md5(password);
		params.put("newPassword", password);

		int i = masterDao.update("admin.savePassword", params);
		if (i > 0) {
			data.put("result", "success");
			data.put("msg", "修改成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "当前密码错误，修改失败");
		}
		logService.add(LogType.update,LogEnum.管理中心.getValue(), "管理员中心：修改密码", params, data);
		return data;
	}

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("admin.query", "admin.queryCount", params);
	}

	@Override
	public Map<String, Object> updateStatus(Long id, int status) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		String msg;
		LogType logType = null ;
		switch (status) {
		case UserConstant.USER_STATUS_1:
			msg = "启用";
			logType =  LogType.update;
			break;
		case UserConstant.USER_STATUS_2:
			msg = "禁用";
			logType =  LogType.update;
			break;
		case UserConstant.USER_STATUS_3:
			msg = "删除";
			logType =  LogType.delete;
			break;
		default:
			data.put("result", "fail");
			data.put("msg", "不是恢复或删除，操作失败");
			return data;
		}
		if (Long.valueOf(1).equals(id)) {
			data.put("result", "fail");
			data.put("msg", "不可以删除系统超级管理员");
			return data;
		}

		params.put("id", id);
		params.put("status", status);
		int i = 0;
		if(status == UserConstant.USER_STATUS_3){
			i = masterDao.delete("admin.deleteUser", params);
		}else{
			i = masterDao.update("admin.updateStatus", params);
		} 
		
		if(i > 0){
			data.put("result", "success");
			data.put("msg", msg + "成功");
		}else{
			data.put("result", "fail");
			data.put("msg", "管理员不存在，" + msg + "失败");
		}
		logService.add(logType,LogEnum.管理中心.getValue(), "管理员中心-管理员管理：修改管理员状态", msg, params, data);
		return data;
	}

	@Override
	public Map<String, Object> sendVerifyCode(String mobile) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (masterDao.getSearchSize("admin.checkMobile", mobile) > 0) {
			data.put("result", "fail");
			data.put("msg", "联系手机已经被绑定，请重新输入");
			return data;
		}

		String verifyCode = RandomStringUtils.randomNumeric(4);
		boolean result = RestUtils.sendTemplateSMS(SmsTemplateId.verify_code, mobile, verifyCode);
		if (result) {
			data.put("encrypt_verify_code", EncryptUtils.encodeMd5(mobile + verifyCode));
			data.put("result", "success");
			data.put("msg", "发送短信验证码成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "发送短信验证码失败，请联系管理员");
		}
		logService.add(LogType.update,LogEnum.管理中心.getValue(), "管理员中心-管理员资料-修改：获取验证码", mobile, verifyCode, data);
		return data;
	}

}
