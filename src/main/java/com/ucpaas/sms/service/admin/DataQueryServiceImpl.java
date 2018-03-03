package com.ucpaas.sms.service.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.constant.DbConstant.DbType;
import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.CommonDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.web.AuthorityUtils;

/**
 * 数据查询
 *
 */
@Service
@Transactional
public class DataQueryServiceImpl implements DataQueryService {
	private static final Logger logger = LoggerFactory.getLogger(DataQueryServiceImpl.class);

//	@Autowired
//	private AccessSlaveDao messageStatSlaveDao;
	@Autowired
	private LogService logService;
	@Autowired
	private CommonDao commonDao;

	@Override
	public Map<String, Object> query(Map<String, String> params) {

		Long userId = AuthorityUtils.getLoginUserId(); // 获取用户登录的id
		String userName = AuthorityUtils.getLoginRealName();
		String dbType = params.get("db_type");
		String excuteSql = params.get("excuteSql");
		logger.info("用户【{}】,id={},在数据库{}执行,执行sql={}", userName, userId, excuteSql);

		if (null != excuteSql && dbType!=null) {
			Map<String, Object> data = new HashMap<String, Object>();
			excuteSql = excuteSql.replaceAll("[\\t\\n\\r]", " ");

			// sql校验
			data = sqlValidator(excuteSql);
			if (data.size() != 0) {
				return data;
			}

			// 拼接执行查询结果总数的sql
			String queryCountSql = "SELECT COUNT(1) AS totalCount FROM" + "( " + excuteSql + ") t";
			params.put("queryCountSql", queryCountSql);
			logger.debug("数据查询，执行查询的sql为：" + params);
			PageContainer page = new PageContainer();
			try {
				//page = messageStatSlaveDao.getSearchPage("dataQuery.excuteQuery", "dataQuery.queryCount", params);
				page = commonDao.getDao(DbType.getInstance(Integer.valueOf(dbType))).getSearchPage("dataQuery.excuteQuery", "dataQuery.queryCount", params);
			} catch (Exception e) {
				// 执行查询Sql错误捕获错误返回
				String errorMsg = e.getCause().toString();
				data.put("errorMsg", errorMsg);
				logger.debug("数据查询，执行查询发生错误errorMsg：" + errorMsg);
				return data;
			}

			// 获取查询数据的列名 rowKeySet
			List<Map<String, Object>> list = page.getList();
			Set<String> rowKeySet = new HashSet<String>();
			if (null != list && list.size() != 0) {
				Map<String, Object> rowObject = (Map<String, Object>) list.get(0);
				rowKeySet = rowObject.keySet();
			}

			data.put("rowKeyList", rowKeySet.toArray());
			data.put("page", page);
			logService.add(LogType.query, LogEnum.管理中心.getValue(), "管理中心-数据查询：", params);

			return data;
		} else {
			return new HashMap<String, Object>();
		}

	}

	private Map<String, Object> sqlValidator(String sql) {
		Map<String, Object> data = new HashMap<String, Object>();

		sql = sql.toUpperCase();
		if (sql.length() < 6) {
			data.put("errorMsg", "请输入合法的查询SQL");
			logger.debug("数据查询，执行不合法的SQL");
			return data;
		}
		String select_str = sql.substring(0, 6);
		if (!select_str.equals("SELECT")) {
			data.put("errorMsg", "请输入合法的查询SQL");
			logger.debug("数据查询，执行不合法的SQL");
			return data;
		}

		String[] illegalCharaters = { "DELETE", "INSERT", "UPDATE", "ALTER", "CALL", "CREATE", "DROP" };
		for (int pos = 0; pos < illegalCharaters.length; pos++) {
			if (sql.indexOf(illegalCharaters[pos]) != -1) {
				data.put("errorMsg", "请输入合法的查询SQL");
				logger.debug("数据查询，执行不合法的SQL");
				return data;
			}
		}

		return data;
	}

}
