package com.ucpaas.sms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.DbConstant.DbType;
import com.ucpaas.sms.dao.BaseDao;
import com.ucpaas.sms.dao.CommonDao;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.service.admin.AuthorityService;

/**
 * 自定义标签业务
 * 
 * @author xiejiaan
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {
	@Autowired
	private MessageMasterDao masterDao;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private AuthorityService authorityService;

	@Override
	public List<Map<String, Object>> getTagDataForDictionary(String dictionaryType, Map<String, Object> sqlParams) {
		sqlParams.put("dictionaryType", dictionaryType);
		return masterDao.getSearchList("tag.queryDictionary", sqlParams);
	}

	@Override
	public List<Map<String, Object>> getTagDataForSql(Integer dbType, String sqlId, Map<String, Object> sqlParams) {
		BaseDao dao;
		if(null != DbType.getInstance(dbType)){
			dao = commonDao.getDao(DbType.getInstance(dbType));
		}else{
			dao = masterDao;
		}

		return dao.getSearchList("tag." + sqlId, sqlParams);
	}

	@Override
	public boolean isAuthority(int menuId) {
		return authorityService.isAuthority(menuId);
	}
	
	@Override
	public boolean isRole(String roleIds) {
		return authorityService.isRole(roleIds);
	}

	@Override
	public String getCityNameByAreaId(Map<String, Object> params) {
		String tmp = masterDao.getOneInfo("tag.getCityNameByAreaId", params);
		return tmp;
	}
}
