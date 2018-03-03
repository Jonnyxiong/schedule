<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
	<title>审核关键字分类</title>
</head>

<body menuId="3298" >
<!--Action boxes-->
<div class="container-fluid">
	<hr>
	<div class="row-fluid">
		<div class="span12">
			<div class="widget-box">
				<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
					<h5></h5>
					<div class="search">
						<form method="post" id="mainForm">
							<ul>
								<li>
									<input type="text" name="condition" value="<s:property value="#parameters.condition"/>" maxlength="40" placeholder="类别名称" class="txt_177" />
								</li>
								<li>
									<input type="submit" id="_search" value="查询" />
								</li>
								<li>
									<input type="button" value="新增类型" onclick="addModal()"/>
								</li>
								<li>
									<span class="label label-info">
										<a href="javascript:;" onclick="exportExcel()">导出Excel</a>
									</span>
								</li>
							</ul>
						</form>
					</div>
				</div>
				<div class="widget-content nopadding">
					<table class="table table-bordered table-striped">
						<thead>
						<tr>
							<th>序号</th>
							<th>类别ID</th>
							<th>类别名称</th>
							<th>类别描述</th>
							<th>更新时间</th>
							<th>操作</th>
						</tr>
						</thead>
						<tbody>
						<s:iterator value="page.list">
							<tr>
								<td>${rowNum}</td>
								<td>${categoryId}</td>
								<td>${categoryName}</td>
								<td>${categoryDesc}</td>
								<td>${updateDateStr}</td>
								<td>
									<a href="javascript:;" onclick="edit(this, '${categoryId}')">编辑</a>
									| <a href="javascript:;" onclick="manage(this, '${categoryId}','${categoryName}')">关键字管理</a>
								</td>
							</tr>
						</s:iterator>
						</tbody>
					</table>
				</div>
				<u:page page="${page}" formId="mainForm" />
			</div>
		</div>
	</div>
</div>
<div class="pop" style="display:none;" id="add-modal">
	<form class="form-horizontal">
		<div class="control-group">
			<label class="control-label"><span class="red">*</span>类别名称</label>
			<div class="controls">
				<input type="text" id="categoryName" placeholder="10个字符以内"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span class="red">*</span>类别描述</label>
			<div class="controls">
				<input type="text" id="categoryDesc" placeholder="20个字符以内"/>
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<a href="javascript:;" class="btn btn-success" onclick="add()">确认</a>&nbsp;&nbsp;
				<a href="javascript:;" class="btn btn-default" onclick="layer.closeAll()">取消</a>
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
    var max_export_excel_num = '${max_export_excel_num}';

    //添加
    function addModal(){
        layer.open({
            type: 1,
            skin: 'layui-layer-demo', //样式类名
            closeBtn: 1,
            anim: 2,
			title:'新增审核关键字类别',
            area: ['500px', '240px'],
            shadeClose: true, //开启遮罩关闭
            content: $("#add-modal")
        });
    }

    function add() {
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
            url: "${ctx}/auditorkeyword/addcategory",
            data:{
                categoryName : categoryName,
                categoryDesc : categoryDesc
            },
            success: function(data){
                if(data.success==null){
                    layer.msg("服务器错误，请联系管理员");
                    return;
                }

                if(data.success){
                    layer.msg("添加成功",{icon:1,time:800},function () {
                        layer.closeAll();
                        $("#_search").click();
                    });
                }else{
                    layer.msg(data.msg);
                }
            }
		})

    }

    //导出Excel文件
    function exportExcel() {
        var totalCount = ${page.totalCount};
        if (totalCount == 0) {
            layer.alert("共0条记录，导出Excel文件失败");
            return;
        }
        if(max_export_excel_num && totalCount > max_export_excel_num){
            layer.alert("导出Excel文件条数大于"+max_export_excel_num+"条");
            return;
        }else if (totalCount > 6000) {
            layer.alert("导出Excel文件条数大于6000条");
            return;
        }
        var msg = "正在生成报表，请稍后...";
        layer.msg('<div style="color:#506470;font-family: "microsoft yahei","Arial Narrow",HELVETICA;">'+msg+'</div>', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time: 600000}) ;
        var options = {
            url:"${ctx}/auditorkeyword/exportcategory",
            success : function(result) {
                layer.closeAll(); //疯狂模式，关闭所有层
                if(result == null){
                    layer.msg("导出错误，请稍后重试...", {icon:2});
                }else if(result.success == true){
                    var exportFileName = result.data;
                    var turnForm = document.createElement("form");
                    document.body.appendChild(turnForm);
                    turnForm.method = 'post';
                    turnForm.action = "${ctx}/file/downExcel";
                    //创建表单
                    var newElement = document.createElement("input");
                    newElement.setAttribute("name","exportFileName");
                    newElement.setAttribute("type","hidden");
                    newElement.setAttribute("value",exportFileName);
                    turnForm.appendChild(newElement);
                    turnForm.submit();
                }else{
                    layer.msg(result.msg, {icon:2},1500);

                }
            },
            complete : function(XMLHttpRequest,status){
                console.log(status);
            },
            type : "post",
            async: true,
            timeout:30000
        };
        $("#mainForm").ajaxSubmit(options);
    }

    //编辑
    function edit(btn, categoryId){
        layer.open({
            type: 2,
            skin: 'layui-layer-demo', //样式类名
            closeBtn: 1,
            anim: 2,
            title:'编辑审核关键字类别',
            area: ['500px', '240px'],
            shadeClose: true, //开启遮罩关闭
            content: "${ctx}/auditorkeyword/editkeywordscategory?categoryId=" + categoryId ,
            end:function () {
                $('#_search').click();
            }
        });
    }

    //关键字管理
    function manage(btn,categoryId,categoryName){
        location.href="${ctx}/auditorkeyword/keywordsmanage?categoryId=" + categoryId+ "&categoryName=" + encodeURI(encodeURI(categoryName));
    }

</script>
</body>
</html>