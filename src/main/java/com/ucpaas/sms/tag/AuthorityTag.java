package com.ucpaas.sms.tag;

import java.util.Map;

import com.ucpaas.sms.service.TagService;

/**
 * 权限控制标签
 * 
 * @author xiejiaan
 */
public class AuthorityTag extends BaseTag {
	private static final long serialVersionUID = 6332741277200616979L;

	/**
	 * 菜单id
	 */
	private Integer menuId;
	
	private String roleIds;

	@Override
	public String getTemplateFile() {
		return null;
	}

	@Override
	public Map<String, Object> getTemplateParams(TagService tagService) {
		return null;
	}

	@Override
	public boolean isEvaluated(TagService tagService) {
		if(menuId == null){
			// 通过角色控制前台标签显示
			return tagService.isRole(roleIds);
		}else{
			// 通过访问菜单的权限控制前台标签显示
			return tagService.isAuthority(menuId);
		}
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	
	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

}
