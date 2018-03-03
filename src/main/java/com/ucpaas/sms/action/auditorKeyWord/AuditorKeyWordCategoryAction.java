package com.ucpaas.sms.action.auditorKeyWord;


import com.jsmsframework.audit.entity.JsmsAuditKeywordCategory;
import com.jsmsframework.audit.entity.JsmsAuditKeywordList;
import com.jsmsframework.audit.service.JsmsAuditKeywordCategoryService;
import com.jsmsframework.audit.service.JsmsAuditKeywordListService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.ResultVO;
import com.jsmsframework.common.util.BeanUtil;
import com.ucpaas.sms.action.BaseAction;
import com.ucpaas.sms.dto.AuditKeywordCategoryDTO;
import com.ucpaas.sms.dto.AuditKeywordListDTO;
import com.ucpaas.sms.dto.JsmsAutoTemplateDTO;
import com.ucpaas.sms.model.Excel;
import com.ucpaas.sms.service.auditorKeyWord.AuditorKeyWordService;
import com.ucpaas.sms.util.CommonUtil;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.file.ExcelUtils;
import com.ucpaas.sms.util.file.FileUtils;
import com.ucpaas.sms.util.web.AuthorityUtils;
import com.ucpaas.sms.util.web.StrutsUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 审核关键字分类
 *
 * @author Administrator
 */
@Controller
@Scope("prototype")
@Results({
        @Result(name = "query", location = "/WEB-INF/content/auditorKeyWord/query.jsp"),
        @Result(name = "editKeywordsCategory", location = "/WEB-INF/content/auditorKeyWord/editCategory.jsp"),
        @Result(name = "editkeywords", location = "/WEB-INF/content/auditorKeyWord/editKeywords.jsp"),
        @Result(name = "keyWordsManage", location = "/WEB-INF/content/auditorKeyWord/keyWordsManage.jsp")
})
public class AuditorKeyWordCategoryAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    private File upload;

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    @Autowired
    private AuditorKeyWordService auditorKeyWordService;
    @Autowired
    private JsmsAuditKeywordCategoryService jsmsAuditKeywordCategoryService;
    @Autowired
    private JsmsAuditKeywordListService jsmsAuditKeywordListService;


    /**
     * 查询所有
     *
     * @return
     */
    @Action("/auditorkeyword/query")
    public String query() {
        Map<String, String> params = StrutsUtils.getFormData();
        JsmsPage<JsmsAuditKeywordCategory> jsmsPage = CommonUtil.initJsmsPage(params);
        auditorKeyWordService.queryKeywordCategoryPage(jsmsPage);
        page = CommonUtil.converterJsmsPage2PageContainer(jsmsPage);
        StrutsUtils.setAttribute("max_export_excel_num", ConfigUtils.max_export_excel_num);

        return "query";
    }


    /**
     * 添加字段
     *
     * @return
     */
    @Action("/auditorkeyword/addcategory")
    public void addCategory() {
        Map<String, String> params = StrutsUtils.getFormData();
        Long userId = AuthorityUtils.getLoginUserId();
        params.put("adminId", String.valueOf(userId));
        ResultVO resultVO = auditorKeyWordService.addKeywordCategory(params);
        StrutsUtils.renderJson(resultVO);
    }
    @Action("/auditorkeyword/importBatch")
    public void importBatch() {
        String categoryId = StrutsUtils.getParameter("categoryId");
        ResultVO resultVO;
        if (StringUtils.isBlank(categoryId)) {
            StrutsUtils.renderJson(ResultVO.failure("参数缺失,请刷新页面后再试..."));
            return ;
        }
        Long userId = AuthorityUtils.getLoginUserId();
        try {
            resultVO = auditorKeyWordService.importExcel(upload,userId,Integer.valueOf(categoryId));
        } catch (IOException e) {
            resultVO = ResultVO.failure("导入失败");
        }
        StrutsUtils.renderJson(resultVO);
    }

    @Action("/auditorkeyword/exportTemplate")
    public void exportTemplate(){
        String filePath = StrutsUtils.getRequest().getServletContext().getRealPath("/templateFile/审核关键字导入模板.xlsx");
        File file = new File(filePath);
        if(file.exists()){
            FileUtils.download(filePath);
        }else{
            StrutsUtils.renderText("文件过期、不存在或者已经被管理员删除");
        }
    }
    /**
     * 编辑关键字分类
     * @return
     */
    @Action("/auditorkeyword/editcategory")
    public void editcategory() {
        Map<String, String> params = StrutsUtils.getFormData();
        Long userId = AuthorityUtils.getLoginUserId();
        params.put("adminId", String.valueOf(userId));
        ResultVO resultVO = null;
        try {
            resultVO = auditorKeyWordService.updateKeywordCategory(params);
        } catch (ParseException e) {
            resultVO = ResultVO.failure("参数无效, 请稍后再试...");
        }
        StrutsUtils.renderJson(resultVO);
    }


    /**
     * 导出报表
     */
    @Action("/auditorkeyword/exportcategory")
    public void exportcategory() {
        Map<String, String> params = StrutsUtils.getFormData();
        StringBuffer filePath = new StringBuffer(ConfigUtils.save_path);
        if(!ConfigUtils.save_path.endsWith("/")){
            filePath.append("/");
        }
        filePath.append("审核关键字分类")
                .append(".xls")
                .append("$$$")
                .append(UUID.randomUUID());
        Excel excel = new Excel();
        excel.setFilePath(filePath.toString());
        excel.setTitle("审核关键字分类");
        StringBuffer buffer = new StringBuffer();
        buffer.append("查询条件：");
        String condition = null;
        condition = params.get("condition");
        if (StringUtils.isNoneBlank(condition)) {
            buffer.append("  类别名称：");
            buffer.append(condition);
            buffer.append(";");
        }
        excel.addRemark(buffer.toString());

        excel.addHeader(20, "序号", "rowNum");
        excel.addHeader(20, "类别ID", "categoryId");
        excel.addHeader(20, "类别名称", "categoryName");
        excel.addHeader(20, "类别描述", "categoryDesc");
        excel.addHeader(20, "更新时间", "updateDateStr");

        Map<String, String> formData = StrutsUtils.getFormData();
        List<Map<String, Object>> dataList = auditorKeyWordService.queryExportExcelData(formData);
        excel.setDataList(dataList);
        excel.setShowRownum(false);
        excel.setShowPage(false);
        excel.setShowGridLines(false);
        excel.setShowTitle(false);

        ResultVO resultVO = null;
        try {
            JsmsPage<JsmsAuditKeywordCategory> jsmsPage = CommonUtil.initJsmsPage(params);
            jsmsPage.setRows(Integer.MAX_VALUE);
            auditorKeyWordService.queryKeywordCategoryPage(jsmsPage);
            List<JsmsAutoTemplateDTO> list = new ArrayList<>();
            List result = new ArrayList();
            for (Object temp : jsmsPage.getData()) {
                result.add(BeanUtils.describe(temp));
            }

            if (result == null || result.size() <= 0){
                resultVO = ResultVO.failure("没有数据！先不导出了  ^_^");
//            }else if (result.size() > Integer.valueOf(ConfigUtils.max_export_excel_num)){
            }else if (result.size() > Integer.valueOf(Integer.MAX_VALUE)){
                resultVO =  ResultVO.failure("数据量超过"+ConfigUtils.max_export_excel_num+"，请缩小范围后再导出吧  ^_^");
            }
            excel.setDataList(result);
            if (ExcelUtils.exportExcel(excel)) {
                resultVO = ResultVO.successDefault();
                resultVO.setMsg("报表生成成功");
                resultVO.setData(excel.getFilePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StrutsUtils.renderJson(resultVO);
    }

    /**
     * 导出报表
     */
    @Action("/auditorkeyword/exportkeyword")
    public void exportkeyword() {
        Map<String, String> params = StrutsUtils.getFormData();
        String categoryId = StrutsUtils.getParameter("categoryId");
        if(StringUtils.isBlank(categoryId)){
            StrutsUtils.renderJson(ResultVO.failure("参数缺失,请刷新页面后重试..."));
            return;
        }
        JsmsAuditKeywordCategory category = jsmsAuditKeywordCategoryService.getByCategoryId(Integer.valueOf(categoryId));
        String categoryName = null;
        if(category != null){
            categoryName = category.getCategoryName();
        }
        StringBuffer filePath = new StringBuffer(ConfigUtils.save_path);
        if(!ConfigUtils.save_path.endsWith("/")){
            filePath.append("/");
        }
        filePath.append(categoryName +" - 审核关键字")
                .append(".xls")
                .append("$$$")
                .append(UUID.randomUUID());
        Excel excel = new Excel();
        excel.setFilePath(filePath.toString());
        excel.setTitle(categoryName +" - 审核关键字");
        StringBuffer buffer = new StringBuffer();
        buffer.append("查询条件：");
        String condition = null;
        condition = params.get("condition");
        if (StringUtils.isNoneBlank(condition)) {
            buffer.append("  关键字：");
            buffer.append(condition);
            buffer.append(";");
        }
        excel.addRemark(buffer.toString());

        excel.addHeader(20, "序号", "rowNum");
        excel.addHeader(20, "关键字", "keyword");
        excel.addHeader(20, "备注", "remarks");
        excel.addHeader(20, "操作人", "operatorStr");
        excel.addHeader(20, "更新时间", "updateDateStr");

        Map<String, String> formData = StrutsUtils.getFormData();
        List<Map<String, Object>> dataList = auditorKeyWordService.queryExportExcelData(formData);
        excel.setDataList(dataList);
        excel.setShowRownum(false);
        excel.setShowPage(false);
        excel.setShowGridLines(false);
        excel.setShowTitle(false);

        ResultVO resultVO = null;
        try {

            JsmsPage<JsmsAuditKeywordList> jsmsPage = CommonUtil.initJsmsPage(params);
            jsmsPage.setRows(Integer.MAX_VALUE);
            auditorKeyWordService.queryKeywordList(jsmsPage);
            List<JsmsAutoTemplateDTO> list = new ArrayList<>();
            List result = new ArrayList();
            for (Object temp : jsmsPage.getData()) {
                result.add(BeanUtils.describe(temp));
            }
            if (result == null || result.size() <= 0){
                resultVO = ResultVO.failure("没有数据！先不导出了  ^_^");
//            }else if (result.size() > Integer.valueOf(ConfigUtils.max_export_excel_num)){
            }else if (result.size() > Integer.valueOf(Integer.MAX_VALUE)){
                resultVO =  ResultVO.failure("数据量超过"+ConfigUtils.max_export_excel_num+"，请缩小范围后再导出吧  ^_^");
            }
            excel.setDataList(result);
            if (ExcelUtils.exportExcel(excel)) {
                resultVO = ResultVO.successDefault();
                resultVO.setMsg("报表生成成功");
                resultVO.setData(excel.getFilePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StrutsUtils.renderJson(resultVO);
    }

    /**
     * 编辑审核关键字分类
     *
     * @return
     */
    @Action("/auditorkeyword/editkeywordscategory")
    public String editKeywordsCategory() {
        String categoryIdStr = StrutsUtils.getParameter("categoryId");

        if (StringUtils.isNotBlank(categoryIdStr)){
            Integer categoryId = Integer.valueOf(categoryIdStr);
            JsmsAuditKeywordCategory keywordCategory = jsmsAuditKeywordCategoryService.getByCategoryId(categoryId);
            AuditKeywordCategoryDTO model = new AuditKeywordCategoryDTO();
            BeanUtil.copyProperties(keywordCategory,model);
            StrutsUtils.setAttribute("model",model);
        }
        return "editKeywordsCategory";
    }

    /**
     * 编辑审核关键字分类
     */
    @Action("/auditorkeyword/editkeywords")
    public String editKeywordsPage() {
        String idStr = StrutsUtils.getParameter("id");

        if (StringUtils.isNotBlank(idStr)){
            Long id = Long.valueOf(idStr);
            JsmsAuditKeywordList keyword = jsmsAuditKeywordListService.getById(id);
            AuditKeywordListDTO model = new AuditKeywordListDTO();
            BeanUtil.copyProperties(keyword,model);
            StrutsUtils.setAttribute("model",model);
        }
        return "editkeywords";
    }


    /**
     * 添加关键字
     *
     * @return
     */
    @Action("/auditorkeyword/addkeywords")
    public void addKeywords() {
        Map<String, String> params = StrutsUtils.getFormData();
        params.put("adminId", String.valueOf(AuthorityUtils.getLoginUserId()));
        ResultVO resultVO = auditorKeyWordService.addKeyword(params);
        StrutsUtils.renderJson(resultVO);
    }

    /**
     * 添加关键字
     *
     * @return
     */
    @Action("/auditorkeyword/delkeywords")
    public void delKeywords() {
        Map<String, String> params = StrutsUtils.getFormData();
        params.put("adminId", String.valueOf(AuthorityUtils.getLoginUserId()));
        ResultVO resultVO = auditorKeyWordService.delKeywords(params);
        StrutsUtils.renderJson(resultVO);
    }

    /**
     * 编辑关键字
     * @return
     */
    @Action("/auditorkeyword/editkeywords/edit")
    public void editKeywords() {
        Map<String, String> params = StrutsUtils.getFormData();
        Long userId = AuthorityUtils.getLoginUserId();
        params.put("adminId", String.valueOf(userId));
        ResultVO resultVO = null;
        try {
            resultVO = auditorKeyWordService.updateKeyword(params);
        } catch (ParseException e) {
            resultVO = ResultVO.failure("参数无效, 请稍后再试...");
        }
        StrutsUtils.renderJson(resultVO);
    }

    /**
     * 管理审核关键字
     *
     * @return
     */
    @Action("/auditorkeyword/keywordsmanage")
    public String keyWordsManage() {
        Map<String, String> params = StrutsUtils.getFormData();
        String categoryId = StrutsUtils.getParameter("categoryId");
        String categoryName = StrutsUtils.getParameter("categoryName");
        try {
            categoryName = URLDecoder.decode(categoryName,"UTF8");
        } catch (UnsupportedEncodingException e) {
        }
        if(StringUtils.isNotBlank(categoryId)){
            JsmsPage<JsmsAuditKeywordList> jsmsPage = CommonUtil.initJsmsPage(params);
            auditorKeyWordService.queryKeywordList(jsmsPage);
            page = CommonUtil.converterJsmsPage2PageContainer(jsmsPage);
            JsmsAuditKeywordCategory category = jsmsAuditKeywordCategoryService.getByCategoryId(Integer.valueOf(categoryId));
            if(category != null){
                categoryName = category.getCategoryName();
            }
        }
        StrutsUtils.setAttribute("categoryId",categoryId);
        StrutsUtils.setAttribute("categoryName",categoryName);
        StrutsUtils.setAttribute("max_export_excel_num", ConfigUtils.max_export_excel_num);

        return "keyWordsManage";
    }

    /**
     * 查询关键字的所有
     *
     * @return
     */
    @Action("/auditorkeyword/jump")
    public String queryKeyWord() {
        Map<String, String> params = StrutsUtils.getFormData();
        List<JsmsAuditKeywordList> list = auditorKeyWordService.queryKeyWord(params);
        return "jump";

    }


    /**
     * 关键字管理添加
     *
     * @return
     */
//    @Action("/auditorKeyWord/addManage")
//    public R addaddManageKeyWord() {
//        Map<String, String> params = StrutsUtils.getFormData();
//        String str = auditorKeyWordService.addManageKeyWord(params);
//        return R.ok(str);
//    }


//    /**
//     * 关键查询
//     *
//     * @return
//     */
//    @Action("/auditorKeyWord/queryManage")
//    public List<JsmsAuditKeywordList> queryManage() {
//        Map<String, String> params = StrutsUtils.getFormData();
//        List<JsmsAuditKeywordList> list = auditorKeyWordService.queryManage(params);
//        return list;
//
//    }


    /**
     * 关键字更新
//     */
//    @Action("/auditorKeyWord/updateManage")
//    public R updateManageKeyWord() {
//        Map<String, String> params = StrutsUtils.getFormData();
//        String str = auditorKeyWordService.updateManageKeyWord(params);
//        return R.ok(str);
//    }


    /**
     * 关键在管理导出
     */
    @Action("/auditorKeyWord/exportManageExcel")
    public void exportManageExcel() {
        String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
        String filePath = ConfigUtils.save_path + "/关键子管理-" + timeStamp + ".xls";
        Excel excel = new Excel();
        excel.setFilePath(filePath);


        excel.addHeader(20, "关键字", "keyword");
        excel.addHeader(20, "备注", "remarks");
        excel.addHeader(20, "操作人", "operator");
        excel.addHeader(20, "更新时间", "updateDate");


        Map<String, String> formData = StrutsUtils.getFormData();
        List<Map<String, Object>> dataList = auditorKeyWordService.queryExportManageExcel(formData);
        excel.setDataList(dataList);
        excel.setShowRownum(false);
        excel.setShowPage(false);
        excel.setShowGridLines(false);
        excel.setShowTitle(false);

        boolean generateExcelSuccess = ExcelUtils.exportExcel(excel);
        if (generateExcelSuccess) {
            FileUtils.download(filePath);
            FileUtils.delete(filePath);
        } else {
            StrutsUtils.renderText("导出Excel文件失败，请联系管理员");
        }
    }



}
