package com.ucpaas.sms.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * ucpaas_message_stats 主库DAO
 *
 */
@Repository
public class StatsMasterDao extends BaseDao {

	@Override
	@Resource(name = "stats_master_sqlSessionTemplate")
	protected void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
