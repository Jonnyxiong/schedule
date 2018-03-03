package com.ucpaas.sms.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ucpaas.sms.dao.BaseDao;

/**
 * 通道测试数据库DAO
 */
@Repository
public class MessageTestMasterDao extends BaseDao {

	@Override
	@Resource(name = "message_test_master_sqlSessionTemplate")
	protected void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
