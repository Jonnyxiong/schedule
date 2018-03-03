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
			<label class="control-label"></label>
		</div>
		<div class="control-group">
			<input type="hidden" id="id" value="${model.id}"/>
			<input type="hidden" id="categoryId" value="${model.categoryId}"/>
			<input type="hidden" id="updateDate" value="${model.updateDateStr}"/>
			<label class="control-label"><span class="red">*</span>关键字</label>
			<div class="controls">
				<input type="text" id="keyword" value="${model.keyword}" placeholder="20个字符以内"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注</label>
			<div class="controls">
				<input type="text" id="remarks" value="${model.remarks}" placeholder="20个字符内容以内">
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
        var keyword = $("#keyword").val();
        var remarks = $("#remarks").val();
        if(!(keyword) || keyword.trim() == ""){
            layer.msg('关键字不能为空或空字符串');
            return;
        }
        if(keyword.length > 20){
            layer.msg('关键字不能超过20个字符');
            return;
        }
        /*if(!(remarks) || remarks.trim() == ""){
            layer.msg('备注不能为空或空字符串');
            return;
        }*/
        if((remarks) && remarks.Length > 20){
            layer.msg('备注不能超过20个字符');
            return;
        }
		$.ajax({
            type: "post",
            url: "${ctx}/auditorkeyword/editkeywords/edit",
            data:{
                id : $("#id").val(),
                updateDate : $("#updateDate").val(),
                categoryId : $("#categoryId").val(),
                keyword : keyword,
                remarks : remarks
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