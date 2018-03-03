<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
	<title>审核关键字分类</title>
	<style type="text/css">
		body{
			background-color: #F8F8F8;
		}
	</style>
</head>
<body menuId="3298" >
<!--Action boxes-->

<div class="pop" style="display:block;" id="edit-modal">
	<form class="form-horizontal">
		<div class="control-group">
			<label class="control-label"><span class="red">*</span>类别ID</label>
			<div class="controls">
				<span type="text" id="categoryId" value="${model.categoryId}">${model.categoryId}</span>
				<input type="hidden" id="updateDate" value="${model.updateDateStr}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span class="red">*</span>类别名称</label>
			<div class="controls">
				<input type="text" id="categoryName" value="${model.categoryName}" placeholder="10个字符以内"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span class="red">*</span>类别描述</label>
			<div class="controls">
				<input type="text" id="categoryDesc" value="${model.categoryDesc}" placeholder="20个字符以内"/>
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<a href="javascript:;" class="btn btn-success" onclick="edit()">确认</a>&nbsp;&nbsp;
				<a href="javascript:;" class="btn btn-default" onclick="parent.layer.closeAll()">取消</a>
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">


    function edit() {
        var categoryName = $("#categoryName").val();
        var categoryDesc = $("#categoryDesc").val();
        if(!(categoryName) || categoryName.trim() == ""){
            layer.msg('类别名称不能为空或空字符串');
            return;
		}
		if(categoryName.length > 10){
            layer.msg('类别名称不能超过10个字符');
            return;
		}
		if(!(categoryDesc) || categoryDesc.trim() == ""){
            layer.msg('类别描述不能为空或空字符串');
            return;
		}
		if(categoryDesc.Length > 20){
            layer.msg('类别描述不能超过20个字符');
            return;
		}
		$.ajax({
            type: "post",
            url: "${ctx}/auditorkeyword/editcategory",
            data:{
                categoryId : $("#categoryId").text(),
                updateDate : $("#updateDate").val(),
                categoryName : categoryName,
                categoryDesc : categoryDesc
            },
            success: function(data){
                if(data.success==null){
                    layer.msg("服务器错误，请联系管理员");
                    return;
                }

                if(data.success){
                    layer.msg("修改成功",{icon:1,time:800},function () {
                        parent.layer.closeAll();
                    });
                }else{
                    layer.msg(data.msg);
                }
            }
		})

    }


</script>
</body>
</html>