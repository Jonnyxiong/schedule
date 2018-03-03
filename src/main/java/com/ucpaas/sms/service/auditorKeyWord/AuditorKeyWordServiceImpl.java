package com.ucpaas.sms.service.auditorKeyWord;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.jsmsframework.audit.entity.JsmsAuditKeywordCategory;
import com.jsmsframework.audit.entity.JsmsAuditKeywordList;
import com.jsmsframework.audit.service.JsmsAuditKeywordCategoryService;
import com.jsmsframework.audit.service.JsmsAuditKeywordListService;
import com.jsmsframework.common.dto.JsmsPage;
import com.jsmsframework.common.dto.ResultVO;
import com.jsmsframework.common.util.BeanUtil;
import com.jsmsframework.user.entity.JsmsUser;
import com.jsmsframework.user.service.JsmsUserService;
import com.ucpaas.sms.dto.AuditKeywordCategoryDTO;
import com.ucpaas.sms.dto.AuditKeywordListDTO;
import com.ucpaas.sms.util.ConfigUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuditorKeyWordServiceImpl implements AuditorKeyWordService {
    public static final int M5_SIZE = 5 * 1024 * 1024;

	@Autowired
	private JsmsAuditKeywordCategoryService jsmsAuditKeywordCategoryService;
	@Autowired
	private JsmsAuditKeywordListService jsmsAuditKeywordListService;
	@Autowired
	private JsmsUserService jsmsUserService;

	/*@Override
	public List<JsmsAuditKeywordCategory> query(Map<String, String> params) {
		JsmsPage<JsmsAuditKeywordCategory> page = new JsmsPage<>();
		String p = params.get("currentPage");
		if(p==null){
			p = "1";
		}
		int pages =Integer.parseInt(p);
		String r = params.get("pageRowCount");
		if(r==null){
			r = "10";
		}
		int row = Integer.parseInt(r);
		page.setRows(row);
		page.setPage(pages);
		List<JsmsAuditKeywordCategory> list = jsmsAuditKeywordCategoryService.queryList(page);
//		List<JsmsAuditKeywordCategory> list = jsmsauditKeywordCategoryMapper.queryList(page);
//		page.setData(list);
		return list;

	}*/

	@Override
	public JsmsPage<JsmsAuditKeywordCategory> queryKeywordCategoryPage(JsmsPage<JsmsAuditKeywordCategory> jsmsPage) {
		jsmsPage.setOrderByClause("update_date DESC");
		jsmsAuditKeywordCategoryService.queryList(jsmsPage);
		List<JsmsAuditKeywordCategory> data = jsmsPage.getData();
		List<JsmsAuditKeywordCategory> result = new ArrayList<>();
		int count = 1;
		for (JsmsAuditKeywordCategory datum : data) {
			AuditKeywordCategoryDTO temp = new AuditKeywordCategoryDTO();
			BeanUtil.copyProperties(datum,temp);
			temp.setRowNum(jsmsPage.getRows() * (jsmsPage.getPage() - 1) + count);
			result.add(temp);
			count ++;
		}
		jsmsPage.setData(result);
		return jsmsPage;
	}

	@Override
	public JsmsPage<JsmsAuditKeywordList> queryKeywordList(JsmsPage jsmsPage) {
		jsmsPage.setOrderByClause("update_date DESC,id DESC");
		jsmsAuditKeywordListService.queryList(jsmsPage);
		List<JsmsAuditKeywordList> data = jsmsPage.getData();
		if(data.isEmpty()){
            return jsmsPage;
        }
		List<AuditKeywordListDTO> result = new ArrayList<>();
		int count = 1;
        Set<Long> adminIds = new HashSet<>();
        for (JsmsAuditKeywordList datum : data) {
			AuditKeywordListDTO temp = new AuditKeywordListDTO();
			BeanUtil.copyProperties(datum,temp);
			temp.setRowNum(jsmsPage.getRows() * (jsmsPage.getPage() - 1) + count);
			result.add(temp);
            adminIds.add(datum.getOperator());
            count ++;
		}
        List<JsmsUser> jsmsUsers = jsmsUserService.getByIds(adminIds);
        Map<Long, String> userMap = new HashMap<>();
        for (JsmsUser jsmsUser : jsmsUsers) {
            userMap.put(jsmsUser.getId(), jsmsUser.getRealname());
        }
        for (AuditKeywordListDTO keywordList : result) {
            keywordList.setOperatorStr(userMap.get(keywordList.getOperator()));
        }
        jsmsPage.setData(result);
		return jsmsPage;
	}


	@Override
    @Transactional
    public ResultVO addKeywordCategory(Map<String, String> params) {
		JsmsAuditKeywordCategory model = new JsmsAuditKeywordCategory();
		model.setCategoryName(params.get("categoryName"));// 类别名称
		model.setCategoryDesc(params.get("categoryDesc"));// 类别描述
		model.setUpdateDate(new Date());
		model.setOperator(Long.valueOf(params.get("adminId")));
        ResultVO resultVO = checkKeywordCategory(model,null);
        if(resultVO != null && resultVO.isFailure()){
            return resultVO;
        }
		int i = jsmsAuditKeywordCategoryService.insert(model);

        return i > 0 ? ResultVO.successDefault():ResultVO.failure();
    }

	@Override
    @Transactional
    public ResultVO importExcel(File file, Long adminId,Integer categoryId) throws IOException {
        String fileName = file.getName();

        if (file.length() > M5_SIZE) {
            return ResultVO.failure("您选择的文件大于5M,请将excel拆分后重新导入");
        }
        String path = new StringBuilder(ConfigUtils.save_path).append(UUID.randomUUID()).append(fileName).toString();
        FileUtils.copyInputStreamToFile(new FileInputStream(file), new File(path));
        List<Map> tempList = null;
        try {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1);
            tempList = ExcelImportUtil.importExcel(new File(path), Map.class, importParams);
            new File(path).delete();

        } catch (Exception e) {
            new File(path).delete();
            return ResultVO.failure("导入文件格式错误，目前只支持Excel导入，请使用模板");
        }
//        logger.debug("号码Excel 读取完成  ----------> 开始解析");
        if(tempList==null){
            return ResultVO.failure("导入文件格式错误，目前只支持Excel导入，请使用模板");
        }

        Map keywordMap = new HashMap();
//        Map<Object, Map<Object, Object>> keywordMap = new HashMap();
//        Map<Object, Keywords> keywordListMap = new HashMap();
        if(tempList.size() > Integer.MAX_VALUE){
//        if(tempList.size() > Integer.parseInt(ConfigUtils.excel_max_import_num)){
            return ResultVO.failure("Excel文件导入数据超过限制<br/>不能超过"+ ConfigUtils.excel_max_import_num);
        }
        int importNum = tempList.size();

        for ( Map map : tempList) {
            Object keyword = map.get("关键字");
            if(keywordMap.get(keyword) == null){
                keywordMap.put(convertToStr(keyword), convertToStr(map.get("备注")));
            }
        }
        int total = keywordMap.size();

        Set keywordSet = keywordMap.keySet();
        List<JsmsAuditKeywordList> keywordList = jsmsAuditKeywordListService.getByKeywords(keywordSet);
        for (JsmsAuditKeywordList keyword : keywordList) {
            if(keyword.getCategoryId().equals(categoryId)){
                keywordMap.remove(keyword.getKeyword());
            }
        }
        Date now = new Date();
        int actTotal = keywordMap.size();
        int insertBatchKeywords = insertBatchKeywords(keywordMap, adminId, now, categoryId);
        StringBuilder msg = new StringBuilder("导入总数：" + importNum + "，实际添加："+insertBatchKeywords);
        if (total < importNum){
            msg.append("</br>重复：" + (importNum - total));
        }
        if (actTotal < total){
            msg.append("</br>已存在：" + (total - actTotal));
        }
        if (insertBatchKeywords < actTotal){
            msg.append("</br>字符长度超限：" + (actTotal - insertBatchKeywords));
        }
        return ResultVO.successDefault(msg.toString());

    }

    private String convertToStr(Object object){
        if(object instanceof Double){
            BigDecimal bigDecimal = new BigDecimal((double) object);
            return bigDecimal.toString();
        }else if(object instanceof String){
            return (String) object;
        }else {
            try {
                return String.valueOf(object) ;
            }catch (Exception e){
//                        logger.error("{} 无法装换",keyword);
                return null;
            }
        }
    }

    @Override
    @Transactional
    public ResultVO importExcel(File file, Long adminId) throws IOException {
        String fileName = file.getName();
//        logger.debug("access importMobile parmas[fileName={}]", fileName);

        if (file.length() > M5_SIZE) {
            return ResultVO.failure("您选择的文件大于5M,请将excel拆分后重新导入");
        }
//        logger.debug("导入的文件类型 ----> {}",file.getContentType());
        String path = new StringBuilder(ConfigUtils.save_path).append(UUID.randomUUID()).append(fileName).toString();
        FileUtils.copyInputStreamToFile(new FileInputStream(file), new File(path));
        List<Map> tempList = null;
        try {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1);

            tempList = ExcelImportUtil.importExcel(new File(path), Map.class, importParams);
//            logger.debug("号码Excel 读取完成 , 删除文件 ----------> {}",path);
            new File(path).delete();

        } catch (Exception e) {
//            logger.debug("号码Excel 读取失败 , 删除文件 ----------> {}",path);
            new File(path).delete();
//            logger.error("解析excel失败：filePath=" + path, e);
            return ResultVO.failure("导入文件格式错误，目前只支持Excel导入，请使用模板");
        }
//        logger.debug("号码Excel 读取完成  ----------> 开始解析");
        if(tempList==null){
//            logger.error("解析excel失败：filePath=" + path);
            return ResultVO.failure("导入文件格式错误，目前只支持Excel导入，请使用模板");
        }

        Map<Object, Object> categoryMap = new HashMap();
        Map<Object, Map<Object, Object>> keywordMap = new HashMap();
        Map<Object, Keywords> keywordListMap = new HashMap();
        if(tempList.size() > Integer.parseInt(ConfigUtils.excel_max_import_num)){
//            logger.error("excel 数量限制超过限制：excel_max_import_num = {}, 实际导入数 = {}" ,ConfigUtils.excel_max_import_num,list.size());
            return ResultVO.failure("Excel文件导入数据超过限制<br/>不能超过"+ ConfigUtils.excel_max_import_num);
        }
        for ( Map map : tempList) {
            if(categoryMap.get(map.get("类别名称")) == null){
                categoryMap.put(map.get("类别名称"), map.get("类别描述"));
            }
            Map<Object, Object> temp = new HashMap();
            Keywords tempModel = this.new Keywords();
            temp.put(map.get("关键字"), map.get("备注"));
            tempModel.setKeyword((String) map.get("关键字"));
            tempModel.setRemarks((String) map.get("备注"));
            tempModel.setCategory((String) map.get("类别名称"));
            if(keywordMap.get(map.get("类别名称")) == null){
                keywordMap.put(map.get("类别名称"), temp);
            }else{
                keywordMap.get(map.get("类别名称")).putAll(temp);
            }
            if(keywordListMap.get(map.get("类别名称")) == null){
                keywordListMap.put(map.get("关键字"), tempModel);
            }

        }

        Set categorys = categoryMap.keySet();
        List<JsmsAuditKeywordCategory> categories = jsmsAuditKeywordCategoryService.getByCategoryNames(categorys);
        for (JsmsAuditKeywordCategory keywordCategory : categories) {
            categoryMap.remove(keywordCategory.getCategoryName());
        }
        Date now = new Date();
        int insertBatch = insertBatchCategories(categoryMap, adminId, now);
        Set categorys2 = keywordMap.keySet();
        List<JsmsAuditKeywordCategory> newCategoryList = jsmsAuditKeywordCategoryService.getByCategoryNames(categorys2);

        Set keywordSet = new HashSet();
        for (Keywords tempKeyword : keywordListMap.values()) {
            keywordSet.add(tempKeyword.getKeyword());
        }
        List<JsmsAuditKeywordList> keywords = jsmsAuditKeywordListService.getByKeywords(keywordSet);
        for (JsmsAuditKeywordList keyword : keywords) {
            keywordListMap.remove(keyword.getKeyword());
        }
        int insertBatchKeywords = insertBatchKeywords(keywordListMap, adminId, now, newCategoryList);
        return ResultVO.successDefault("新增关键字分类"+insertBatch+"个</br>新增关键字"+insertBatchKeywords+"个");

    }

    private class Keywords extends JsmsAuditKeywordList{
	    private String category;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    private int insertBatchCategories(Map<Object, Object> categoryMap,Long adminId,Date date){
        int count = 0;
        if(categoryMap.isEmpty()){
            return count;
        }
        List<JsmsAuditKeywordCategory> modelList = new ArrayList<>();
        Set<Map.Entry<Object, Object>> entries = categoryMap.entrySet();
        for (Map.Entry entry : entries) {
            JsmsAuditKeywordCategory temp = new JsmsAuditKeywordCategory();
            temp.setCategoryName((String) entry.getKey());
            temp.setCategoryDesc((String) entry.getValue());
            temp.setOperator(adminId);
            temp.setUpdateDate(date);
            modelList.add(temp);
            count++;
        }

        int i = jsmsAuditKeywordCategoryService.insertBatch(modelList);
        return i > 0 ? count : i;
    }


    private int insertBatchKeywords(Map<String, String> keywordListMap,Long adminId,Date date,Integer categoryId){
        int count = 0;
        if(keywordListMap.isEmpty()){
            return count;
        }
        List<JsmsAuditKeywordList> modelList = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = keywordListMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (StringUtils.isBlank(entry.getKey()) || entry.getKey().length() > 20){
                continue;
            }
            if(StringUtils.isNotBlank(entry.getValue()) && entry.getValue().length() > 20){
                continue;
            }
            JsmsAuditKeywordList temp = new JsmsAuditKeywordList();
            temp.setCategoryId(categoryId);
            temp.setKeyword(entry.getKey());
            temp.setRemarks(entry.getValue());
            temp.setOperator(adminId);
            temp.setUpdateDate(date);
            modelList.add(temp);
            count++;
        }
        if(modelList.isEmpty()){
            return 0;
        }
        int i = jsmsAuditKeywordListService.insertBatch(modelList);
        return i > 0 ? count : i;
    }

    private int insertBatchKeywords(Map<Object, Keywords> keywordListMap,Long adminId,Date date,List<JsmsAuditKeywordCategory> newCategoryList){
        int count = 0;
        if(keywordListMap.isEmpty()){
            return count;
        }
        Map<String, Integer> categoryMap = new HashMap<>();
        for (JsmsAuditKeywordCategory category : newCategoryList) {
            categoryMap.put(category.getCategoryName(),category.getCategoryId());
        }
        List<JsmsAuditKeywordList> modelList = new ArrayList<>();

        for (Keywords keywords : keywordListMap.values()) {
            JsmsAuditKeywordList temp = new JsmsAuditKeywordList();
            temp.setCategoryId(categoryMap.get(keywords.getCategory()));
            temp.setKeyword(keywords.getKeyword());
            temp.setOperator(adminId);
            temp.setUpdateDate(date);
            modelList.add(temp);
            count++;
        }

        int i = jsmsAuditKeywordListService.insertBatch(modelList);
        return i > 0 ? count : i;
    }


	@Override
    @Transactional
    public ResultVO addKeyword(Map<String, String> params) {
        Integer categoryId = Integer.parseInt(params.get("categoryId"));
		JsmsAuditKeywordList model = new JsmsAuditKeywordList();
        model.setCategoryId(categoryId );// 类别id
		model.setKeyword(params.get("keyword"));// 关键字
		model.setRemarks(params.get("remarks"));// 备注
		model.setUpdateDate(new Date());
		model.setOperator(Long.valueOf(params.get("adminId")));
        ResultVO resultVO = checkKeyword(model,null,categoryId );
        if(resultVO != null && resultVO.isFailure()){
            return resultVO;
        }
		int i = jsmsAuditKeywordListService.insert(model);
		return i > 0 ? ResultVO.successDefault():ResultVO.failure();
	}

	@Override
    @Transactional
    public ResultVO delKeywords(Map<String, String> params) {
		String idStr = params.get("id");
		if(StringUtils.isBlank(idStr)){
			return ResultVO.failure("缺少参数, 请刷新后再试!");
		}
//		model.setOperator(Long.valueOf(params.get("adminId")));
		int delete = jsmsAuditKeywordListService.delete(Long.valueOf(idStr));
		return delete > 0 ? ResultVO.successDefault():ResultVO.failure("未找当前关键字,请确认当前关键是否已被删除");
	}

	private ResultVO checkKeywordCategory(JsmsAuditKeywordCategory model,Integer categoryId){

		if(StringUtils.isBlank(model.getCategoryName())){
			return ResultVO.failure("类别名称不能为空或空字符串");
		}
		if(model.getCategoryName().length() > 10){
			return ResultVO.failure("类别名称不能超过10个字符");

		}
		if(StringUtils.isBlank(model.getCategoryDesc())){
			return ResultVO.failure("类别描述不能为空或空字符串");
		}
		if(model.getCategoryDesc().length() > 20){
			return ResultVO.failure("类别描述不能超过20个字符");
		}
        List<JsmsAuditKeywordCategory> categoryList = jsmsAuditKeywordCategoryService.getByCategoryName(model.getCategoryName());
//        JsmsAuditKeywordCategory category = jsmsAuditKeywordCategoryService.getByCategoryName(model.getCategoryName());
        if(categoryList.isEmpty()){
            return null;
        }else {
            for (JsmsAuditKeywordCategory category : categoryList) {
                if( category == null || category.getCategoryId() == null || category.getCategoryId().equals(categoryId)){
                    continue;
                }else{
                    return ResultVO.failure("类别名称已存在,不能重复添加");
                }
            }
        }
        return null;

    }

	private ResultVO checkKeyword(JsmsAuditKeywordList model,Long id,Integer categoryId ){

		if(StringUtils.isBlank(model.getKeyword())){
			return ResultVO.failure("关键字不能为空或空字符串");
		}
		if(model.getKeyword().length() > 20){
			return ResultVO.failure("关键字不能超过20个字符");

		}
		/*if(StringUtils.isBlank(model.getRemarks())){
			return ResultVO.failure("备注不能为空或空字符串");
		}*/
		if(StringUtils.isNotBlank(model.getRemarks()) && model.getRemarks().length() > 20){
			return ResultVO.failure("备注不能超过20个字符");
		}
        JsmsAuditKeywordList keywordList = jsmsAuditKeywordListService.getByKeywordAndCategoryId(model.getKeyword(),categoryId);
		if (keywordList == null){
            return null;
        }else if(keywordList.getId().equals(id)){
            return null;
        }else{
            return ResultVO.failure("关键字已存在,不能重复添加");
        }
	}

	@Override
    @Transactional
    public ResultVO updateKeywordCategory(Map<String, String> params) throws ParseException {
		JsmsAuditKeywordCategory newModel = new JsmsAuditKeywordCategory();
		Integer categoryId = Integer.parseInt(params.get("categoryId"));
		newModel.setCategoryName(params.get("categoryName"));// 类别名称
		newModel.setCategoryDesc(params.get("categoryDesc"));// 类别描述
		newModel.setOperator(Long.valueOf(params.get("adminId")));
		newModel.setUpdateDate(new Date());
        ResultVO resultVO = checkKeywordCategory(newModel,categoryId);
        if(resultVO != null && resultVO.isFailure()){
            return resultVO;
        }
		JsmsAuditKeywordCategory oldModel = new JsmsAuditKeywordCategory();
		oldModel.setCategoryId(categoryId);
		oldModel.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(params.get("updateDate")));
		int u = jsmsAuditKeywordCategoryService.updateIdempotent(oldModel,newModel);
		return u > 0 ? ResultVO.successDefault():ResultVO.failure("当前数据不是最新数据,请刷新后重试...");

	}

	@Override
    @Transactional
    public ResultVO updateKeyword(Map<String, String> params) throws ParseException {
        Integer categoryId = Integer.parseInt(params.get("categoryId"));
		JsmsAuditKeywordList newModel = new JsmsAuditKeywordList();
		Long id = Long.parseLong(params.get("id"));
		newModel.setKeyword(params.get("keyword"));// 类别名称
		newModel.setRemarks(params.get("remarks"));// 类别描述
		newModel.setOperator(Long.valueOf(params.get("adminId")));
        ResultVO resultVO = checkKeyword(newModel,id,categoryId );
        if(resultVO != null && resultVO.isFailure()){
            return resultVO;
        }
		JsmsAuditKeywordList oldModel = new JsmsAuditKeywordList();
		oldModel.setId(id);
		oldModel.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(params.get("updateDate")));
		int u = jsmsAuditKeywordListService.updateIdempotent(oldModel,newModel);
		return u > 0 ? ResultVO.successDefault():ResultVO.failure("当前数据不是最新数据,请刷新后重试...");
	}

	@Override
	public List<Map<String, Object>> queryExportExcelData(Map<String, String> params) {
		JsmsAuditKeywordCategory model = new JsmsAuditKeywordCategory();
		model.setCategoryName(params.get("categoryName"));// 类别名称
		//return jsmsauditKeywordCategoryMapper.queryExportExcelData(model);
		return null;
	}

	@Override
	public List<JsmsAuditKeywordList> queryKeyWord(Map<String, String> params) {
//		JsmsPage<JsmsAuditKeywordList> page = CommonUtil.initJsmsPage(params);
//		JsmsPage queryList = (JsmsPage) jsmsauditKeywordListMapper.queryList(page);
//		return queryList;
		
		JsmsPage<JsmsAuditKeywordList> page = new JsmsPage<>();
		String p = params.get("currentPage"); 
		if(p==null){
			p = "1";
		}
		int pages =Integer.parseInt(p);
		String r = params.get("pageRowCount");
		if(r==null){
			r = "10";
		}
		int row = Integer.parseInt(r);
		page.setRows(row);
		page.setPage(pages);
		page.setRows(row);
		page.setPage(pages);
//		List<JsmsAuditKeywordList> list = jsmsAuditKeywordListService.queryList(page);
//		return list;
		return null;
	}

	/*@Override
	public List<JsmsAuditKeywordList> queryManage(Map<String, String> params) {
		JsmsAuditKeywordList model = new JsmsAuditKeywordList();
		long Id = Long.valueOf(params.get("Id")).longValue();
		model.setId(Id);
//		List<JsmsAuditKeywordList> list = jsmsAuditKeywordListService.queryManage(model);
//		return list;
		return null;
	}*/

	/*@Override
	public String addManageKeyWord(Map<String, String> params) {
		JsmsAuditKeywordList model = new JsmsAuditKeywordList();
		model.setKeyword(params.get("keyword"));
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", params.get("keyword"));
		int i = jsmsAuditKeywordListService.count(param);
		if (i > 0) {
			return "当前关键字已经存在";
		} else {
			model.setKeyword(params.get("keyword"));
			model.setUpdateDate(new Date());
			model.setClientid(params.get("clientId"));
			int j = jsmsAuditKeywordListService.insert(model);
			if (j > 0) {
				return " 添加成功";
			} else {
				return "添加失败";
			}
		}
	}*/

	/*@Override
	public String updateManageKeyWord(Map<String, String> params) {
		JsmsAuditKeywordList model = new JsmsAuditKeywordList();
		model.setKeyword(params.get("keyword"));
		Integer categoryId = Integer.parseInt(params.get("categoryId"));
		model.setCategoryId(categoryId);
		model.setRemarks(params.get("remarks"));
		model.setUpdateDate(new Date());
		int i = jsmsAuditKeywordListService.update(model);
		if (i > 0) {
			return " 更新成功";
		} else {
			return "更新失败";
		}
	}
*/
	@Override
	public List<Map<String, Object>> queryExportManageExcel(Map<String, String> formData) {
		JsmsAuditKeywordList model = new JsmsAuditKeywordList();
		model.setKeyword(formData.get("categoryId"));
//		List<Map<String, Object>> list = jsmsAuditKeywordListService.queryExportManageExcel(model);
//		return list;
		return null;
	}

/*
	@Override
	public void delectKeyWord(Map<String, String> params) {
		JsmsAuditKeywordList model = new JsmsAuditKeywordList();
		Integer Id = Integer.parseInt(params.get("Id"));
		model.setCategoryId(Id);
//		jsmsAuditKeywordListService.delete(model);
	}
*/

}
