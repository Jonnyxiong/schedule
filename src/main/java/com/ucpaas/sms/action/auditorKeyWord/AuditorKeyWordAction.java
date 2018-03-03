package com.ucpaas.sms.action.auditorKeyWord;

import com.jsmsframework.audit.dto.JsmsAuditClientGroupDTO;
import com.jsmsframework.audit.dto.JsmsAuditKeywordGroupDTO;
import com.jsmsframework.audit.entity.JsmsAuditClientGroup;
import com.jsmsframework.audit.entity.JsmsAuditKeywordCategory;
import com.jsmsframework.audit.entity.JsmsAuditKeywordGroup;
import com.jsmsframework.audit.entity.JsmsAuditKgroupRefCategory;
import com.jsmsframework.audit.service.JsmsAuditClientGroupService;
import com.jsmsframework.audit.service.JsmsAuditKeywordCategoryService;
import com.jsmsframework.audit.service.JsmsAuditKeywordGroupService;
import com.jsmsframework.audit.service.JsmsAuditKgroupRefCategoryService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.R;
import com.jsmsframework.user.audit.service.JsmsUserAuditClientGroupService;
import com.jsmsframework.user.audit.service.JsmsUserAuditKeywordGroupService;
import com.jsmsframework.user.entity.JsmsAccount;
import com.jsmsframework.user.service.JsmsAccountService;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.common.util.StringUtils;
import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.service.KeyWordGroupService;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.CommonUtil;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.apache.struts2.convention.annotation.Results;
import com.ucpaas.sms.enums.LogEnum;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xiongfenglin on 2017/10/30.
 *
 * @author: xiongfenglin
 */
@Controller
@Scope("prototype")
@Results({@Result(name = "keywordGroupList", location="/WEB-INF/content/auditorKeyWord/keywordGroup.jsp"),
        @Result(name = "keywordGroupAdd", location="/WEB-INF/content/auditorKeyWord/keywordGroupAdd.jsp"),
        @Result(name = "keyWordSconfigurationList", location="/WEB-INF/content/auditorKeyWord/keyWordSconfiguration.jsp"),
        @Result(name = "keyWordSconfigurationAdd", location="/WEB-INF/content/auditorKeyWord/keyWordSconfigurationAdd.jsp"),
        @Result(name = "keyWordSconfiguration", location="/WEB-INF/content/auditorKeyWord/keyWordSconfigurationConfigure.jsp")})
public class AuditorKeyWordAction extends BaseAction{
    @Autowired
    private JsmsAuditKeywordGroupService jsmsAuditKeywordGroupService;
    @Autowired
    private JsmsAuditKgroupRefCategoryService jsmsAuditKgroupRefCategoryService;
    @Autowired
    private LogService logService;
    @Autowired
    private KeyWordGroupService keyWordGroupService;
    @Autowired
    private JsmsUserAuditKeywordGroupService jsmsUserAuditKeywordGroupService;
    @Autowired
    private JsmsAuditClientGroupService jsmsAuditClientGroupService;
    @Autowired
    private JsmsAccountService jsmsAccountService;
    @Autowired
    private JsmsUserAuditClientGroupService jsmsUserAuditClientGroupService;
    @Autowired
    private JsmsAuditKeywordCategoryService jsmsAuditKeywordCategoryService;
    @Action("/keywordgroup/list")
    public String keywordGroupList() {
        Map<String, String> params = StrutsUtils.getFormData();
        Map<String, Object> objectMap = new HashMap<>();
        List<JsmsAuditKeywordGroup> list = new ArrayList<>();
        Map<String,Object> data1=new HashedMap();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 类别名称
        Object obj = params.get("categoryName");
        if (obj != null) {
            objectMap.put("categoryName", obj);
            data1.put("categoryName",obj);
        }
        // 组别
        obj = params.get("modelId");
        if (obj != null) {
            objectMap.put("kgroupId", obj);
            data1.put("kgroupId",obj);
        }
        JsmsPage<JsmsAuditKeywordGroup> jsmsPage = CommonUtil.initJsmsPage(params);
        jsmsPage.setParams(objectMap);
        JsmsPage queryPage = jsmsUserAuditKeywordGroupService.queryList(jsmsPage);
        for (Object temp : queryPage.getData()) {
            JsmsAuditKeywordGroupDTO jsmsAuditKeywordGroupDTO = new JsmsAuditKeywordGroupDTO();
            BeanUtils.copyProperties(temp , jsmsAuditKeywordGroupDTO);
            if(jsmsAuditKeywordGroupDTO.getUpdateDate()!=null){
                jsmsAuditKeywordGroupDTO.setUpdateTimeStr(formatter.format(jsmsAuditKeywordGroupDTO.getUpdateDate()));
            }
            list.add(jsmsAuditKeywordGroupDTO);
        }
        data=data1;
        queryPage.setData(list);
        page = CommonUtil.converterJsmsPage2PageContainer(queryPage);
        return "keywordGroupList";
    }
    @Action("/keyWordGroup/add")
    public String keyWordGroupAdd() {
        Map<String, String> params = StrutsUtils.getFormData();
        Map<String,Object> data1=new HashedMap();
        List<JsmsAuditKgroupRefCategory> jsmsAuditKgroupRefCategory =  new ArrayList<>();
        JsmsAuditKeywordCategory jsmsAuditKeywordCategory = new JsmsAuditKeywordCategory();
        List<String> list = new ArrayList<>();
        if(params.get("kgroupId")!=null){
            Integer kgroupId=Integer.valueOf(params.get("kgroupId"));
            JsmsAuditKeywordGroup jsmsAuditKeywordGroup=jsmsAuditKeywordGroupService.getByKgroupId(kgroupId);
            if(jsmsAuditKeywordGroup!=null){
                data1.put("kgroupName",jsmsAuditKeywordGroup.getKgroupName());
                data1.put("remark",jsmsAuditKeywordGroup.getRemark());
                if(jsmsAuditKeywordGroup.getKgroupId()!=null){
                    jsmsAuditKgroupRefCategory = jsmsAuditKgroupRefCategoryService.getBykgroupId(jsmsAuditKeywordGroup.getKgroupId());
                    if(jsmsAuditKgroupRefCategory!=null&&jsmsAuditKgroupRefCategory.size()>0){
                        for(int j =0;j<jsmsAuditKgroupRefCategory.size();j++){
                            if(jsmsAuditKgroupRefCategory.get(j).getCategoryId()!=null){
                                jsmsAuditKeywordCategory = jsmsAuditKeywordCategoryService.getByCategoryId(jsmsAuditKgroupRefCategory.get(j).getCategoryId());
                                list.add(jsmsAuditKeywordCategory.getCategoryName());
                            }
                        }
                    }
                }
                data1.put("categoryNameList",list);
                data1.put("kgroupId",kgroupId);
            }
            data=data1;
        }
        return "keywordGroupAdd";
    }
    @Action("/keyWordGroup/Save")
    public void modifykeywordGroupSave() throws Exception {
        Map<String, String> params = StrutsUtils.getFormData();
        List<String> categoryNameList = new ArrayList<>();
        int flag =0;
        R r = new R();
        boolean isMod = false;
        JsmsAuditKeywordGroup jsmsAuditKeywordGroup = new JsmsAuditKeywordGroup();
        // ID
        Object obj = params.get("kgroupId");
        if (obj != null) {
            isMod = true;
            jsmsAuditKeywordGroup.setKgroupId(Integer.parseInt(obj.toString()));
        }
        // 组别名称
        obj = params.get("kgroupName");
        if (obj != null) {
            jsmsAuditKeywordGroup.setKgroupName(obj.toString());
        }
        // 类别
        obj = params.get("categoryNameList");
        if (obj != null) {
             categoryNameList = java.util.Arrays.asList(obj.toString().split(","));
        }
        // 备注
        obj = params.get("remark");
        if (obj != null) {
            jsmsAuditKeywordGroup.setRemark(obj.toString());
        }
        jsmsAuditKeywordGroup.setOperator(AuthorityUtils.getLoginUserId());
            if (isMod) {
                if (jsmsAuditKeywordGroup.getKgroupId()==null || jsmsAuditKeywordGroupService.getByKgroupId(jsmsAuditKeywordGroup.getKgroupId()) == null){
                    r.setCode(500);
                    r.setMsg("用户组不存在！");
                }else{
                    if(jsmsAuditKeywordGroupService.checkKgroupName(jsmsAuditKeywordGroup.getKgroupName(),jsmsAuditKeywordGroup.getKgroupId())>0){
                        r.setCode(500);
                        r.setMsg("用户组别名称已经存在！");
                    }else{
                        logService.add(LogType.update,LogEnum.审核关键字管理.getValue(), "审核关键字管理-审核关键字分组：编辑分组", params, jsmsAuditKeywordGroup);
                        jsmsAuditKeywordGroup.setUpdateDate(new Date());
                        flag = keyWordGroupService.modifykeywordGroupSave(jsmsAuditKeywordGroup,categoryNameList);
                        if(flag>0){
                            r.setCode(200);
                            r.setMsg("操作成功！");
                        }else{
                            r.setCode(500);
                            r.setMsg("操作异常！");
                        }
                    }
                }
            } else {
                if(jsmsAuditKeywordGroupService.checkKgroupNameOfInsert(jsmsAuditKeywordGroup.getKgroupName())>0){
                    r.setCode(500);
                    r.setMsg("用户组别名称已经存在！");
                }else{
                    logService.add(LogType.add, LogEnum.审核关键字管理.getValue(), "审核关键字管理-审核关键字分组：添加分组", params, jsmsAuditKeywordGroup);
                    jsmsAuditKeywordGroup.setUpdateDate(new Date());
                    flag = keyWordGroupService.addkeywordGroupSave(jsmsAuditKeywordGroup,categoryNameList);
                    if(flag>0){
                        r.setCode(200);
                        r.setMsg("操作成功！");
                    }else{
                        r.setCode(500);
                        r.setMsg("操作异常！");
                    }
                }
            }
        StrutsUtils.renderJson(r);
    }
    @Action("/keyWordGroup/del")
    public void delkeywordGroup() throws Exception {
        Map<String, String> params = StrutsUtils.getFormData();
        int flag =0;
        R r = new R();
        Object obj = params.get("kgroupId");
        if (params.get("kgroupId")==null || jsmsAuditKeywordGroupService.getByKgroupId(Integer.parseInt(String.valueOf(params.get("kgroupId")))) == null){
            r.setCode(500);
            r.setMsg("用户组不存在！");
        }else{
            logService.add(LogType.update,LogEnum.审核关键字管理.getValue(), "审核关键字管理-审核关键字分组：删除分组", params, params.get("kgroupId"));
            //删除时判断在该用户组是否已经配置了
            if(jsmsAuditClientGroupService.getKgroupIdToClient(Integer.parseInt(String.valueOf(params.get("kgroupId"))))>0){
                r.setCode(500);
                r.setMsg("该组别配置了用户组,请先解绑用户组配置！");
            }else{
                //删除用户组时还要删除该用户组绑定的关键字分类
                flag = keyWordGroupService.deteleJsmsAuditKeywordGroup(Integer.parseInt(String.valueOf(params.get("kgroupId"))));
                if(flag>0){
                    r.setCode(200);
                    r.setMsg("删除成功！");
                }else{
                    r.setCode(500);
                    r.setMsg("删除失败！");
                }
            }
        }
        StrutsUtils.renderJson(r);
    }
    @Action("/keywordsconfiguration/list")
    public String keyWordSconfiguration() {
        Map<String, String> params = StrutsUtils.getFormData();
        Map<String, Object> objectMap = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<JsmsAuditClientGroupDTO> list = new ArrayList<>();
        Map<String,Object> data1=new HashedMap();
        // 组别
        Object obj = params.get("modelId");
        if (obj != null) {
            objectMap.put("kgroupId", obj);
            data1.put("kgroupId",obj);
        }
        // 类别名称
        obj = params.get("mistiness");
        if (obj != null) {
            objectMap.put("mistiness", obj);
            data1.put("mistiness",obj);
        }
        JsmsPage<JsmsAuditKeywordGroup> jsmsPage = CommonUtil.initJsmsPage(params);
        jsmsPage.setParams(objectMap);
        JsmsPage queryPage = jsmsUserAuditClientGroupService.queryList(jsmsPage);
        for (Object temp : queryPage.getData()) {
            JsmsAuditClientGroupDTO jsmsAuditClientGroupDTO = new JsmsAuditClientGroupDTO();
            BeanUtils.copyProperties(temp , jsmsAuditClientGroupDTO);
            if(jsmsAuditClientGroupDTO.getUpdateDate()!=null){
                jsmsAuditClientGroupDTO.setUpdateTimeStr(formatter.format(jsmsAuditClientGroupDTO.getUpdateDate()));
            }
            list.add(jsmsAuditClientGroupDTO);
        }
        data=  data1;
        queryPage.setData(list);
        page = CommonUtil.converterJsmsPage2PageContainer(queryPage);
        return "keyWordSconfigurationList";
    }
    @Action("/keyWordSconfiguration/getGroup")
    public void getGroup() {
        List<JsmsAuditKeywordGroup> list = new ArrayList<>();
        list = jsmsAuditKeywordGroupService.getList();
        StrutsUtils.renderJson(list);
    }
    @Action("/keyWordSconfiguration/getAuditKeywordCategory")
    public void getAuditKeywordCategory() {
        List<JsmsAuditKeywordCategory> list = new ArrayList<>();
        list = jsmsAuditKeywordCategoryService.getList();
        StrutsUtils.renderJson(list);
    }
    @Action("/keyWordSconfiguration/getAuditKeywordCategoryExit")
    public void getAuditKeywordCategoryExit() {
        Map<String, String> params = StrutsUtils.getFormData();
        List<JsmsAuditKeywordCategory> exitlist = new ArrayList<>();
        exitlist = jsmsAuditKeywordCategoryService.getExitList(Integer.parseInt(String.valueOf(params.get("kgroupId"))));
        StrutsUtils.renderJson(exitlist);
    }
    @Action("/keyWordSconfiguration/add")
    public String keyWordSconfigurationAdd() {
        Map<String, String> params = StrutsUtils.getFormData();
        Map<String,Object> data1=new HashedMap();
        List<String> list = new ArrayList<>();
        if(params.get("cgroupId")!=null){
            Integer cgroupId=Integer.valueOf(params.get("cgroupId"));
            JsmsAuditClientGroup jsmsAuditClientGroup=jsmsAuditClientGroupService.getByCgroupId(cgroupId);
            if(jsmsAuditClientGroup!=null){
                data1.put("cgroupName",jsmsAuditClientGroup.getCgroupName());
                data1.put("remark",jsmsAuditClientGroup.getRemark());
                data1.put("cgroupId",cgroupId);
            }
            data=data1;
        }
        return "keyWordSconfigurationAdd";
    }
    @Action("/keyWordSconfiguration/Save")
    public void modifykeyWordSconfigurationSave() throws Exception {
        Map<String, String> params = StrutsUtils.getFormData();
        List<String> categoryNameList = new ArrayList<>();
        int flag =0;
        R r = new R();
        boolean isMod = false;
        JsmsAuditClientGroup jsmsAuditClientGroup = new JsmsAuditClientGroup();
        // ID
        Object obj = params.get("cgroupId");
        if (obj != null) {
            isMod = true;
            jsmsAuditClientGroup.setCgroupId(Integer.parseInt(obj.toString()));
        }
        // 组别名称
        obj = params.get("cgroupName");
        if (obj != null) {
            jsmsAuditClientGroup.setCgroupName(obj.toString());
        }
        // 备注
        obj = params.get("remark");
        if (obj != null) {
            jsmsAuditClientGroup.setRemark(obj.toString());
        }
        jsmsAuditClientGroup.setOperator(AuthorityUtils.getLoginUserId());
        jsmsAuditClientGroup.setUpdateDate(new Date());
        jsmsAuditClientGroup.setIsDefault(0);
        if (isMod) {
            if (jsmsAuditClientGroup.getCgroupId()==null || jsmsAuditClientGroupService.getByCgroupId(jsmsAuditClientGroup.getCgroupId()) == null){
                r.setCode(500);
                r.setMsg("审核用户组不存在！");
                StrutsUtils.renderJson(r);
            }else{
                if(jsmsAuditClientGroupService.checkcgroupName(jsmsAuditClientGroup.getCgroupName(),jsmsAuditClientGroup.getCgroupId())>0){
                    r.setCode(500);
                    r.setMsg("审核用户组别名称已经存在！");
                }else{
                    logService.add(LogType.update,LogEnum.审核关键字管理.getValue(), "审核关键字管理-用户关键字配置：编辑分组", params, jsmsAuditClientGroup);
                    flag = jsmsAuditClientGroupService.update(jsmsAuditClientGroup);
                    if(flag>0){
                        r.setCode(200);
                        r.setMsg("操作成功");
                    }else{
                        r.setCode(500);
                        r.setMsg("操作异常！");
                    }
                }
            }
        } else {
            if(jsmsAuditClientGroupService.checkcgroupNameOfInsert(jsmsAuditClientGroup.getCgroupName())>0){
                r.setCode(500);
                r.setMsg("审核用户组别名称已经存在！");
            }else{
                logService.add(LogType.add, LogEnum.审核关键字管理.getValue(), "审核关键字管理-用户关键字配置：添加分组", params, jsmsAuditClientGroup);
                flag = jsmsAuditClientGroupService.insert(jsmsAuditClientGroup);
                if(flag>0){
                    r.setCode(200);
                    r.setMsg("操作成功");
                }else{
                    r.setCode(500);
                    r.setMsg("操作异常！");
                }
            }
        }
        StrutsUtils.renderJson(r);
    }
    @Action("/keyWordSconfiguration/del")
    public void delkeyWordSconfiguration() throws Exception {
        Map<String, String> params = StrutsUtils.getFormData();
        int flag =0;
        R r = new R();
        Object obj = params.get("cgroupId");
        if (params.get("cgroupId")==null || jsmsAuditClientGroupService.getByCgroupId(Integer.parseInt(String.valueOf(obj))) == null){
            r.setCode(500);
            r.setMsg("审核用户组不存在！");
        }else{
            logService.add(LogType.update,LogEnum.审核关键字管理.getValue(), "审核关键字管理-用户关键字配置：删除分组", params, params.get("cgroupId"));
            flag = keyWordGroupService.deteleJsmsAuditClientGroup(Integer.parseInt(String.valueOf(obj)));
            if(flag>0){
                r.setCode(200);
                r.setMsg("删除成功！");
            }else{
                r.setCode(500);
                r.setMsg("删除失败！");
            }
        }
        StrutsUtils.renderJson(r);
    }
    //配置
    @Action("/keyWordSconfiguration/configure")
    public String keyWordSconfigurationConfigure() {
        Map<String,Object> data1=new HashedMap();
        Map<String, String> params = StrutsUtils.getFormData();
        List<JsmsAuditKeywordGroup> list = new ArrayList<>();
        JsmsAuditClientGroup jsmsAuditClientGroup= new JsmsAuditClientGroup();
        // 组别名称
        Object obj = params.get("cgroupId");
        if (obj != null) {
            data1.put("cgroupId",Integer.parseInt(String.valueOf(obj)));
            jsmsAuditClientGroup = jsmsAuditClientGroupService.getByCgroupId(Integer.parseInt(String.valueOf(obj)));
            if(jsmsAuditClientGroup!=null){
                data1.put("cgroupName",(String.valueOf(jsmsAuditClientGroup.getCgroupName())));
                data1.put("kgroupId",jsmsAuditClientGroup.getKgroupId());
            }
        }
        data=data1;
        return "keyWordSconfiguration";
    }
    //配置
    @Action("/keyWordSconfiguration/getAccount")
    public void getAccount() {
        Map<String,Object> data1=new HashedMap();
        Map<String, String> params = StrutsUtils.getFormData();
        List<JsmsAccount>  jsmsAccountList = new ArrayList<>();
        List<JsmsAccount>  jsmsAccountExistList = new ArrayList<>();
        String leftContent="";
        String rightContent="";
        if(StringUtils.isNotBlank(params.get("flag"))&&StringUtils.isNotBlank(params.get("content"))){
            if(params.get("flag").equals("0")){
                leftContent = params.get("content");
            }else if(params.get("flag").equals("1")){
                rightContent = params.get("content");
            }
        }
        jsmsAccountList = jsmsAccountService.getAllAccount(leftContent);
        data1.put("jsmsAccountList",jsmsAccountList);
        jsmsAccountExistList = jsmsAccountService.getAllAccountOfExist(Integer.parseInt(String.valueOf(params.get("cgroupId"))),rightContent);
        data1.put("jsmsAccountExistList",jsmsAccountExistList);
        StrutsUtils.renderJson(data1);
    }

    //配置保存
    @Action("/keyWordSconfiguration/save")
    public void keyWordSconfigurationSave() throws Exception {
        Map<String, String> params = StrutsUtils.getFormData();
        int flag =0;
        R r = new R();
        JsmsAuditClientGroup jsmsAuditClientGroup = new JsmsAuditClientGroup();
        // ID
        Object obj = params.get("cgroupId");
        if (obj != null) {
            jsmsAuditClientGroup.setCgroupId(Integer.parseInt(obj.toString()));
        }
        // 组别名称
        obj = params.get("kgroupId");
        if (obj != null) {
            jsmsAuditClientGroup.setKgroupId(Integer.parseInt(String.valueOf(obj)));
        }
        jsmsAuditClientGroup.setOperator(AuthorityUtils.getLoginUserId());
        if (jsmsAuditClientGroup.getCgroupId()==null || jsmsAuditClientGroupService.getByCgroupId(jsmsAuditClientGroup.getCgroupId()) == null){
            r.setCode(500);
            r.setMsg("审核用户组不存在");
        }
        logService.add(LogType.update,LogEnum.审核关键字管理.getValue(), "审核关键字管理-用户关键字配置：配置用户", params, jsmsAuditClientGroup);
        jsmsAuditClientGroup.setUpdateDate(new Date());
        flag = keyWordGroupService.configurationkeyword(params,jsmsAuditClientGroup);
        if(flag>0){
            r.setCode(200);
            r.setMsg("配置成功");
        }else{
            r.setCode(500);
            r.setMsg("配置失败");
        }
        StrutsUtils.renderJson(r);
    }
}
