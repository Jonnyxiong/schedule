package com.ucpaas.sms.service.sysconf;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.model.PageContainer;

/**
 * 系统设置-节点IP对应关系
 * 
 * @author oylx
 */
@Service
@Transactional
public class IP4NoteServiceImpl implements IP4NoteService {

	@Autowired
	private MessageMasterDao masterDao;

	@Override
	public PageContainer query(Map<String, String> params) {
		PageContainer page = masterDao.getSearchPage("ip4Note.query", "ip4Note.queryCount", params);
		
		// 根据send节点的Id查询该节点下面挂载的通道数量
		for (Map<String, Object> row : page.getList()) {
			String sendId = row.get("sendid").toString();
			int sendMountNum = masterDao.getOneInfo("ip4Note.querySendMountNum", sendId);
			row.put("sendMountNum", sendMountNum);
		}
		return page;
	}

}
