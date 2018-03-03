package com.ucpaas.sms.mapper.message;

import com.jsmsframework.common.dto.JsmsPage;
import com.ucpaas.sms.entity.message.Channel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ChannelMapper{

	int insert(Channel channel);
	
	int insertBatch(List<Channel> channelList);
	
	int delete(Integer id);
	
	int update(Channel channel);
	
	int updateSelective(Channel channel);
	
	Channel getById(Integer id);

	Channel getByCid(Integer cid);
	
	List<Channel> queryList(JsmsPage<Channel> page);

	List<Channel> channelList(Map params);

	int count(Channel channel);

}