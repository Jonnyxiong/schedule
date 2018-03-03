package com.ucpaas.sms.mapper.message;

import com.jsmsframework.common.dto.JsmsPage;
import com.ucpaas.sms.entity.message.TestAccount;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TestAccountMapper{

	int insert(TestAccount testAccount);
	
	int insertBatch(List<TestAccount> testAccountList);
	
	int delete(Integer id);
	
	int update(TestAccount testAccount);
	
	int updateSelective(TestAccount testAccount);
	
	TestAccount getById(Integer id);
	
	List<TestAccount> queryList(JsmsPage<TestAccount> page);

	List<TestAccount> queryParamsList(Map params);
	
	int count(TestAccount testAccount);

}