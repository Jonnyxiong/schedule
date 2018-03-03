package com.ucpaas.sms.service.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.constant.UserConstant;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.cache.OSCacheUtils;
import com.ucpaas.sms.util.cache.OSCacheUtils.CacheType;
import com.ucpaas.sms.util.web.AuthorityUtils;

/**
 * 管理员中心-权限管理
 * 
 * @author xiejiaan
 */
@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private LogService logService;

	@Override
	public Map<String, Object> isAuthority(int roleId, String url) {
		Map<String, Object> data = OSCacheUtils.getCache(CacheType.authority_url, roleId, url);
		if (data == null) {
			data = isAuthorityEntry(roleId, url);
			OSCacheUtils.addCache(CacheType.authority_url, roleId, url, data);
		}
		return data;
	}


	/*private Map<String, Object> isAuthorityEntry(int roleId, String url) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menu_url", url);
		params.put("role_id", roleId);
		Map<String, Object> info = dao.getOneInfo("authority.getMenuId", params);
		data.put("result", "1");
		if (info != null) {// 有权限的条件：1.没有配置菜单；2.分配了菜单，且菜单和角色状态是1
			data.putAll(info);
		}
		return data;
	}*/

	private Map<String, Object> isAuthorityEntry(int roleId, String url) {
		Map<String, Object> data = new HashMap<String, Object>();

		boolean existsMenuUrl = masterDao.getOneInfo("authority.existsMenuUrl", url);
		if (!existsMenuUrl) {// 有权限的条件：1.没有配置菜单；2.分配了菜单，且菜单和角色状态是1
			data.put("result", 1);
			return data;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menu_url", url);
		params.put("role_id", roleId);
		Map<String, Object> info = masterDao.getOneInfo("authority.getParentIds", params);// 查询父菜单
		if (info != null) {
			String[] parentIds = info.get("parent_ids").toString().split(",");
			List<Map<String, Object>> data2 = masterDao.getSearchList("authority.getSelectMenu", parentIds);// 查询当前选中的菜单
			Map<String, Object> selectMenu = new HashMap<String, Object>();
			for (Map<String, Object> map : data2) {
				selectMenu.put("menu" + map.get("level") + "_id", map.get("menu_id"));
			}

			data.put("result", 1);
			data.put("select_menu", selectMenu);
			return data;
		}
		data.put("result", 0);
		return data;
	}

	
	@Override
	public boolean isAuthority(int menuId) {
		Integer roleId = AuthorityUtils.getLoginRoleId();// 获取当前登录用户roleId
		if (roleId == null) {
			return false;
		}
		Boolean data = OSCacheUtils.getCache(CacheType.authority_menuId, roleId, menuId);
		if (data == null) {
			data = isAuthorityEntry(roleId, menuId);
			OSCacheUtils.addCache(CacheType.authority_menuId, roleId, menuId, data);
		}
		return data;
	}

	
	@Override
	public boolean isRole(String roleIds) {
		String [] roleArray = roleIds.split(";");
		String roleId = String.valueOf(AuthorityUtils.getLoginRoleId());// 获取当前登录用户roleId
		if (roleId == null) {
			return false;
		}else{
			for (String id : roleArray) {
				if(id.equals(roleId)){
					return true;
				}
			}
		}
		
		return false;
	}

	public Boolean isAuthorityEntry(Integer roleId, Integer menuId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", roleId);
		params.put("menu_id", menuId);
		int i = masterDao.getSearchSize("authority.isAuthorityMenuId", params);
		return i > 0 ? true : false;
	}

	@Override
	public PageContainer query(Map<String, String> params) {
		return masterDao.getSearchPage("authority.query", "authority.queryCount", params);
	}

	@Override
	public Map<String, Object> getAuthority(Integer roleId) {
		Map<String, Object> data = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", roleId);
		if (roleId == null) {
			data = new HashMap<String, Object>();
		} else {
			data = masterDao.getOneInfo("authority.getRole", params);// 获取角色信息
		}

		Map<String,Object> params2 = new HashMap<String,Object>();
		params2.put("role_id", roleId);
		params2.put("web_id", null);
		
		List<Map<String, Object>> menuList = masterDao.getSearchList("authority.getAllMeum", params2);// 获取所有的菜单
		Map<String, List<Map<String, Object>>> menuMap = new TreeMap<String, List<Map<String, Object>>>();

		if (menuList.size() > 0) {
			String parentId = menuList.get(0).get("parent_id").toString();
			String key = parentId;// 父菜单ID
			List<Map<String, Object>> value = new ArrayList<Map<String, Object>>();// 父菜单的所有子菜单

			for (Map<String, Object> map : menuList) {
				parentId = map.get("parent_id").toString();
				if (!key.equals(parentId)) {
					menuMap.put(key, value);
					key = parentId;
					value = new ArrayList<Map<String, Object>>();
				}
				value.add(map);
			}
			menuMap.put(key, value);
		}

		data.put("menuMap", menuMap);
		return data;
	}

	@Override
	public Map<String, Object> getAuthority(Map<String, String> paramsTemp) {
		String roleId = paramsTemp.get("role_id");
		
		Map<String, Object> data = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", roleId);
		if (roleId == null) {
			data = new HashMap<String, Object>();
		} else {
			data = masterDao.getOneInfo("authority.getRole", params);// 获取角色信息
		}

		List<Map<String, Object>> menuList = masterDao.getSearchList("authority.getAllMeum", paramsTemp);// 获取所有的菜单
		Map<String, List<Map<String, Object>>> menuMap = new TreeMap<String, List<Map<String, Object>>>();

		if (menuList.size() > 0) {
			String parentId = menuList.get(0).get("parent_id").toString();
			String key = parentId;// 父菜单ID
			List<Map<String, Object>> value = new ArrayList<Map<String, Object>>();// 父菜单的所有子菜单

			for (Map<String, Object> map : menuList) {
				parentId = map.get("parent_id").toString();
				if (!key.equals(parentId)) {
					menuMap.put(key, value);
					key = parentId;
					value = new ArrayList<Map<String, Object>>();
				}
				value.add(map);
			}
			menuMap.put(key, value);
		}

		data.put("menuMap", menuMap);
		return data;
	}


	@Override
	public Map<String, Object> saveAuthority(Map<String, String> params) {
		Map<String, Object> data = new HashMap<String, Object>();

		int check = masterDao.getSearchSize("authority.checkRole", params);// 查重
		if (check > 0) {
			data.put("result", "fail");
			data.put("msg", "管理身份已被使用，请重新输入");
			return data;
		}

		Integer roleId = null;
		if (NumberUtils.isDigits(params.get("role_id"))) {
			roleId = Integer.valueOf(params.get("role_id"));
		}
		if (roleId == null) {// 添加角色
//			int i = masterDao.insert("authority.insertRole", params.get("role_name"));
			
			Map<String,Object> roleParams = new HashMap<>();
			roleParams.put("role_name", params.get("role_name"));
			int i = masterDao.insert("authority.insertRole", roleParams);

			if (i > 0) {
				String menuIds = params.get("menu_id");
				if (StringUtils.isNotBlank(menuIds)) {
					/*data = masterDao.getOneInfo("authority.getRole", params);
					roleId = Integer.valueOf(data.get("id").toString());*/
					roleId = Integer.valueOf(roleParams.get("id").toString());

					List<Map<String, Object>> roleMenuList = new ArrayList<Map<String, Object>>();
					Map<String, Object> p;
					for (String menuId : menuIds.split(",")) {
						p = new HashMap<String, Object>();
						p.put("role_id", roleId);
						p.put("menu_id", menuId);
						roleMenuList.add(p);
					}
					masterDao.insert("authority.insertRoleMenu", roleMenuList);// 添加角色菜单
				}
				data.put("result", "success");
				data.put("msg", "添加成功");

			} else {
				data.put("result", "fail");
				data.put("msg", "添加失败");
			}
			logService.add(LogType.add,LogEnum.管理中心.getValue(), "信息管理-管理员中心-权限管理：添加管理身份（角色）", params, data);

		} else {// 修改角色
			int i = masterDao.update("authority.updateRole", params);

			if (i > 0) {
				if ("true".equals(params.get("isUpdateMenu"))) {
					masterDao.delete("authority.deleteRoleMenu", roleId);

					String menuIds = params.get("menu_id");
					if (StringUtils.isNotBlank(menuIds)) {
						List<Map<String, Object>> roleMenuList = new ArrayList<Map<String, Object>>();
						Map<String, Object> p;
						for (String menuId : menuIds.split(",")) {
							p = new HashMap<String, Object>();
							p.put("role_id", roleId);
							p.put("menu_id", menuId);
							roleMenuList.add(p);
						}
						masterDao.insert("authority.insertRoleMenu", roleMenuList);// 修改角色菜单
					}

					OSCacheUtils.flushMenuCache(roleId);// 刷新页面中的菜单缓存
					OSCacheUtils.flushCache(CacheType.authority_url, roleId);
					OSCacheUtils.flushCache(CacheType.authority_menuId, roleId);
				}

				data.put("result", "success");
				data.put("msg", "修改成功");

			} else {
				data.put("result", "fail");
				data.put("msg", "管理身份不存在，修改失败");
			}
			logService.add(LogType.update,LogEnum.管理中心.getValue(), "信息管理-管理员中心-权限管理：修改管理身份（角色）", params, data);
		}

		return data;
	}

	@Override
	public Map<String, Object> updateStatus(int roleId, int status) {
		Map<String, Object> data = new HashMap<String, Object>();

		if (status == UserConstant.ROLE_STATUS_0 && hasUserInRole(roleId)) { // 删除前check
			data.put("result", "fail");
			data.put("msg", "管理身份已绑定用户，删除失败");
			return data;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		String msg;
		switch (status) {
		case UserConstant.ROLE_STATUS_1:
			msg = "启用";
			break;
		case UserConstant.ROLE_STATUS_0:
			msg = "禁用";
			break;
		default:
			data.put("result", "fail");
			data.put("msg", "不是恢复或删除，操作失败");
			return data;
		}

		params.put("role_id", roleId);
		params.put("status", status);
		int i = masterDao.update("authority.updateStatus", params);
		if (i > 0) {
			OSCacheUtils.flushMenuCache(roleId);// 刷新页面中的菜单缓存
			OSCacheUtils.flushCache(CacheType.authority_url, roleId);
			OSCacheUtils.flushCache(CacheType.authority_menuId, roleId);

			data.put("result", "success");
			data.put("msg", msg + "成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "管理身份不存在，" + msg + "失败");
		}
		OSCacheUtils.flushMenuCache(roleId);// 刷新页面中的菜单缓存
		OSCacheUtils.flushCache(CacheType.authority_url, roleId);
		OSCacheUtils.flushCache(CacheType.authority_menuId, roleId);
		
		logService.add(LogType.update, LogEnum.管理中心.getValue(),"信息管理-管理员中心-权限管理：修改管理身份（角色）状态", msg, params, data);
		return data;
	}
	
	public Map<String, Object> deleteRole(int roleId){
		Map<String, Object> data = new HashMap<String, Object>();
		if (hasUserInRole(roleId)) { // 删除前check
			data.put("result", "fail");
			data.put("msg", "管理身份已绑定用户，删除失败");
			return data;
		}
		int i = masterDao.delete("authority.deleteRole", roleId);
		if (i > 0) {
			OSCacheUtils.flushMenuCache(roleId);// 刷新页面中的菜单缓存
			OSCacheUtils.flushCache(CacheType.authority_url, roleId);
			OSCacheUtils.flushCache(CacheType.authority_menuId, roleId);


			data.put("result", "success");
			data.put("msg", "删除成功");
		} else {
			data.put("result", "fail");
			data.put("msg", "管理身份不存在，删除失败");
		}
		logService.add(LogType.delete, LogEnum.管理中心.getValue(),"信息管理-管理员中心-权限管理：删除管理身份（角色）roleId:"+roleId, data);
		return data;
	}

	/**
	 * 是否有用户绑定这个身份！
	 * 
	 * @param roleId
	 * @return
	 */
	boolean hasUserInRole(Integer roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", roleId);
		int tmp = masterDao.getSearchSize("authority.hasUserInRole", params);
		if (tmp > 0) {
			return true;
		}
		return false;
	}

	
	public boolean isValidUser(Long userId) {
		if(userId==1)
			return true;
		Map paramsTemp = new HashMap<>();
		paramsTemp.put("userId", userId);
		List<Map<String, Object>> userList = masterDao.getSearchList("authority.selectValidUser", paramsTemp );// 获取所有的菜单
		if(userList!=null&&userList.size()>0)
			return true;
		else
			return false;
	}
}
