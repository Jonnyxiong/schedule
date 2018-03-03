package com.ucpaas.sms.service;

import com.jsmsframework.audit.entity.JsmsAuditClientGroup;
import com.jsmsframework.audit.entity.JsmsAuditKeywordGroup;
import com.jsmsframework.audit.service.JsmsAuditClientGroupService;
import com.jsmsframework.audit.service.JsmsAuditKeywordGroupService;
import com.jsmsframework.user.audit.service.JsmsUserAuditClientGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by xiongfenglin on 2017/10/31.
 *
 * @author: xiongfenglin
 */
@Service
@Transactional
public class KeyWordGroupServiceImpl implements KeyWordGroupService {
    @Autowired
    private JsmsUserAuditClientGroupService jsmsUserAuditClientGroupService;
    @Autowired
    private JsmsAuditKeywordGroupService jsmsAuditKeywordGroupService;
    @Autowired
    private JsmsAuditClientGroupService jsmsAuditClientGroupService;
    @Override
    @Transactional("message")
    public int modifykeywordGroupSave(JsmsAuditKeywordGroup model, List<String> categoryNameList) {
        return jsmsAuditKeywordGroupService.update(model,categoryNameList);
    }

    @Override
    @Transactional("message")
    public int addkeywordGroupSave(JsmsAuditKeywordGroup model, List<String> categoryNameList) {
        return jsmsAuditKeywordGroupService.insert(model,categoryNameList);
    }

    @Override
    @Transactional("message")
    public int configurationkeyword(Map<String, String> params,JsmsAuditClientGroup jsmsAuditClientGroup) {
        return jsmsUserAuditClientGroupService.configurationkeyword(params,jsmsAuditClientGroup);
    }

    @Override
    @Transactional("message")
    public int deteleJsmsAuditKeywordGroup(Integer kgroupId) {
        return jsmsAuditKeywordGroupService.deteleJsmsAuditKeywordGroup(kgroupId);
    }

    @Override
    public int deteleJsmsAuditClientGroup(Integer cgroupId) {
        return jsmsAuditClientGroupService.deteleJsmsAuditClientGroup(cgroupId);
    }
}
