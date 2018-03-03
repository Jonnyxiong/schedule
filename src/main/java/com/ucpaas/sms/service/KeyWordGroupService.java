package com.ucpaas.sms.service;

import com.jsmsframework.audit.entity.JsmsAuditClientGroup;
import com.jsmsframework.audit.entity.JsmsAuditKeywordGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by xiongfenglin on 2017/10/31.
 *
 * @author: xiongfenglin
 */
public interface KeyWordGroupService {
    int modifykeywordGroupSave(JsmsAuditKeywordGroup model, List<String> categoryNameList) throws Exception;

    int addkeywordGroupSave(JsmsAuditKeywordGroup model, List<String> categoryNameList) throws Exception;

    int configurationkeyword(Map<String, String> params,JsmsAuditClientGroup jsmsAuditClientGroup)throws Exception;

    int deteleJsmsAuditKeywordGroup(Integer kgroupId);

    int deteleJsmsAuditClientGroup(Integer cgroupId);
}