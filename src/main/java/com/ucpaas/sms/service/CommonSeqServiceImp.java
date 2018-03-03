/**
 * @Title: CommonSeqServiceImp.java
 * @Package: com.ucpaas.sms.service
 * @Description: 公用id序列service
 * @author: Niu.T
 * @date: 2016年9月6日 下午4:22:51
 * @version: V1.0
 */
package com.ucpaas.sms.service;

import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.ClientIdType;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.NumConverUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommonSeqServiceImp
 * @Description: 提供公共的clientid生成序列
 * @author: Niu.T
 * @date: 2016年9月6日 下午4:22:51  
 */
@Service
@Transactional
public class CommonSeqServiceImp implements CommonSeqService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonSeqServiceImp.class);

    @Autowired
    private MessageMasterDao masterDao;

    /**
     * @Description: 从clientid序列表中(按规则生成共6位, 36位递增, 首位为a-z, 末位为0-9), 获取当前可用clientid(默认去可用状态中的最小的序列)
     * @author: Niu.T
     * @date: 2016年9月3日 下午4:32:21
     * @return: String
     */
    @Override
    public synchronized String getClientIdByType(ClientIdType clientIdType) {
        // 随机获取一个未使用的clientId
        String unUsedMinClientId = getOneClientIdRandom(clientIdType);

        // 查询到没有使用的clientId直接返回
        if (StringUtils.isNotBlank(unUsedMinClientId)) {
            return unUsedMinClientId;
        } else {
            // 当前没有可以使用的clientId，获得开始创建clientId的起始位置然后创建10000个
            String startClientId = getCreateStartClientId(clientIdType);
            createNext10000ClientId(startClientId, clientIdType);

            // 创建完clientId以后获得一个账号
            unUsedMinClientId = getOneClientIdRandom(clientIdType);
            return unUsedMinClientId;
        }

    }

    /**
     * @Description: 修改clientid状态为1，表示已经使用
     * @param clientId
     */
    @Override
    public boolean updateClientIdStatus(String clientId) {
        if (StringUtils.isNoneBlank(clientId)) {
            try {
                int update = masterDao.update("clientIdSeq.modifyClientIddStatus", clientId);
                return update > 0 ? true : false;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     *
     * @Description
     * 		获得当前t_sms_clientid_sequence表中最大的clientId
     * 		<br>如果t_sms_clientid_sequence不存任何记录则返回配置文件中的默认配置
     * @return
     * 		clientId
     */
    private String getCreateStartClientId(ClientIdType clientIdType) {

        String createStartClientId = "";
        String defaultCreateStartClientId = "";
        if (clientIdType.equals(ClientIdType.agentClientId)) { // 归属于代理商的客户的账号
            defaultCreateStartClientId = ConfigUtils.default_agent_clientid_start;
        } else if (clientIdType.equals(ClientIdType.directClientId)) { // 直客的账号
            defaultCreateStartClientId = ConfigUtils.default_direct_clientid_start;
        } else {
            LOGGER.warn("获得clientId时传入的子账号类型错误 clientIdType = {}", clientIdType.getName());
            return null;
        }

        // 获取当前最大的clientId
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("clientIdType", clientIdType.getValue());
        String clientIdPrefix = "";
        if (clientIdType.equals(ClientIdType.agentClientId)) {
            clientIdPrefix = ConfigUtils.default_agent_clientid_start.substring(0, 1);
        } else {
            clientIdPrefix = ConfigUtils.default_direct_clientid_start.substring(0, 1);
        }

        String cruntentMaxClientIdSeq = masterDao.getOneInfo("clientIdSeq.getCruntentMaxClientIdSeq", sqlParam);
        String cruntentMaxClientId = masterDao.getOneInfo("clientIdSeq.getCruntentMaxClientId", clientIdPrefix);
        if (StringUtils.isBlank(cruntentMaxClientIdSeq) && StringUtils.isBlank(cruntentMaxClientId)) {
            createStartClientId = defaultCreateStartClientId;
        } else if (StringUtils.isNotBlank(cruntentMaxClientIdSeq) && StringUtils.isNotBlank(cruntentMaxClientId)) {
            long ccsDecimal = NumConverUtil.converToDecimal(cruntentMaxClientIdSeq.substring(0, cruntentMaxClientIdSeq.length() - 1));
            long ccDecimal = NumConverUtil.converToDecimal(cruntentMaxClientId.substring(0, cruntentMaxClientId.length() - 1));

            Integer n = Integer.valueOf(cruntentMaxClientIdSeq.substring(cruntentMaxClientIdSeq.length() - 1, cruntentMaxClientIdSeq.length()));
            Integer q = Integer.valueOf(cruntentMaxClientId.substring(cruntentMaxClientId.length() - 1, cruntentMaxClientId.length()));

            ccsDecimal = (long) (ccsDecimal * 10 + n);
            ccDecimal = (long) (ccDecimal * 10 + q);

            // 比较是序列表中的clientId大还是子账号表中的clientId大
            if (ccsDecimal > ccDecimal) {
                createStartClientId = cruntentMaxClientIdSeq;
            } else {
                createStartClientId = cruntentMaxClientId;
            }
        } else if (StringUtils.isNotBlank(cruntentMaxClientIdSeq) && StringUtils.isBlank(cruntentMaxClientId)) {
            createStartClientId = cruntentMaxClientIdSeq;
        } else if (StringUtils.isBlank(cruntentMaxClientIdSeq) && StringUtils.isNotBlank(cruntentMaxClientId)) {
            createStartClientId = cruntentMaxClientId;
        } else {
            // impossible
        }

        return createStartClientId;
    }

    /**
     * 以入参 createStartClientId 开始创建10000个clientId账号
     * @param createStartClientId
     */
    private void createNext10000ClientId(String createStartClientId, ClientIdType clientIdType) {
        long nextClientIdDecimal = NumConverUtil.converToDecimal(createStartClientId.substring(0, createStartClientId.length() - 1));
        Integer n = Integer.valueOf(createStartClientId.substring(createStartClientId.length() - 1, createStartClientId.length()));

        List<String> clientIdList = new LinkedList<String>();
        for (int i = 1; i <= 1000; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 1 && n != null) {
                    j = n + 1; // clientId的最后一位，如果当前是a00173则新创建的应该clientId应该是a00174（j = n + 1 即  4 = 3 + 1）
                    n = null;
                    clientIdList.add(NumConverUtil.converTo36HEX((nextClientIdDecimal), "") + j);
                } else {
                    clientIdList.add(NumConverUtil.converTo36HEX((++nextClientIdDecimal), "") + j);
                }
            }
        }
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("clientIdList", clientIdList);
        sqlParam.put("clientIdType", clientIdType.getValue());
        masterDao.insert("clientIdSeq.createClientIdBatch", sqlParam);
    }

    /**
     * 查询t_sms_clientid_sequence获得没有被使用的clientId
     * @return clientId
     */
    private String getOneClientIdRandom(ClientIdType clientIdType) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("clientIdType", clientIdType.getValue());
        String clientId = masterDao.getOneInfo("clientIdSeq.getOneClientIdRandom", sqlParam);
        if (StringUtils.isNotBlank(clientId)) {
            // 更新lock字段为1表示临时占用，更新lock_start_time表示开始占用时间，ucpaas-sms-task会检查占用超过30分钟的记录并修改lock为0
            masterDao.update("clientIdSeq.lockClientId", clientId);
            LOGGER.debug("锁定clientid = " + clientId);
            return clientId;
        }
        return null;
    }

}
