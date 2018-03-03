<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
	<title>${categoryName} - 关键字管理</title>
</head>

<body menuId="3298">
<!--Action boxes-->
<div class="container-fluid">
	<hr>
	<div class="row-fluid">
		<div class="span12">
			<div class="widget-box">
				<!-- 批量导入关键字弹出框 -->
				<div class="modal hide fade" id="importExcelBox" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="keyboard">
					<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
						<h3 id="myModalLabel">导入关键字 &nbsp;&nbsp;&nbsp;</h3>
						<!-- 							<font color="red">*批量导入耗费系统资源较多，请避免在系统高峰期使用；<br>360等浏览器请点击网址栏右侧浏览器图标取消兼容模式</font> -->
					</div>
					<div class="modal-body text-center">
						<form class="form-horizontal" method="post" id="importForm">
							<input type="file" id="importInput" name="upload" accept=".xlsx,.xls"/>
						</form>
						<div class="controls">
							<span id="msg" style="color:red;" style="display:none;"></span><br/>
							<span id="tips" style="display:none;">点击<a href='${ctx}/channelKeywords/exportError'><font color="green">下载</font></a>失败列表</span>
						</div>
					</div>
					<div class="modal-footer">
						<a href="#" class="btn" onclick="downloadExcelTemplate()">下载Excel模板</a>
						<a href="#" class="btn" data-dismiss="modal" onclick="closeImport()">关闭</a>
						<a href="#" class="btn btn-primary" onclick="importExcel(this)">导入</a>
					</div>
				</div>
				<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
					<h5></h5>
					<div class="search">
						<form method="post" id="mainForm">
							<input type="hidden" id="categoryId" name="categoryId" value="${categoryId}">
							<ul>
								<li>
									<input type="text" name="condition" value="<s:property value="#parameters.condition"/>" maxlength="40" placeholder="关键字" class="txt_177" />
								</li>
								<li>
									<input type="submit" id="_search" value="查询" />
								</li>
								<li>
									<input type="button" value="添加" onclick="addModal()"/>
								</li>
								<li>
									<span class="label label-info" data-toggle="modal" data-target="#importExcelBox" >
										<a href="javascript:;" onclick="">批量导入</a>
									</span>
									<%--<input type="button" value="批量导入" onclick="addModal()"/>--%>
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
							<th>关键字</th>
							<th>备注</th>
							<th>操作人</th>
							<th>更新时间</th>
							<th>操作</th>
						</tr>
						</thead>
						<tbody>
						<s:iterator value="page.list">
							<tr>
								<td>${rowNum}</td>
								<td>${keyword}</td>
								<td>${remarks}</td>
								<td>${operatorStr}</td>
								<td>${updateDateStr}</td>
								<td>
									<a href="javascript:;" onclick="edit(this, '${id}')">编辑</a>
									| <a href="javascript:;" onclick="del(this, '${id}', '${keyword}')">删除</a>
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
			<label class="control-label"><span class="red">*</span>关键字</label>
			<div class="controls">
				<input type="text" id="keyword" placeholder="20个字符以内"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注</label>
			<div class="controls">
				<input type="text" id="remarks" placeholder="20个字符内容以内">
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

    function downloadExcelTemplate(){
        var url = "${ctx}/auditorkeyword/exportTemplate";
        jQuery('<form action="'+url+'" method="post"></form>').appendTo('body').submit().remove();
    }

    //Excel批量导入关键字
    function importExcel(btn) {
		/* if($.browser.msie&&($.browser.version == "8.0")){
		 layer.msg("批量导入不支持ie8或关闭浏览器的兼容模式");
		 return;
		 }  */
        var confirmIndex = layer.confirm('确定要导入这些数据吗', {
            btn: ['确认','取消'] //按钮
        }, function(){
            $("#msg").hide();
            $("#tips").hide();
            var indexs = "" ;
            var options = {
                beforeSubmit : function() {
                    $(btn).attr("disabled", true);
                    index = layer.load(1, {
                        shade: [0.5,'#fff'] //0.1透明度的白色背景
                    });
                },
				data:{
                    categoryId:$('#categoryId').val()
				},
                success : function(data) {debugger;
                    $(btn).attr("disabled", false);
                    layer.close(index);
                    if(data.success==null){
                        layer.msg("服务器错误，请联系管理员");
                        return;
                    }
                    if(data.success){
                        layer.alert(data.msg,{icon:1},function () {
                            layer.closeAll();
                            $('#_search').click();
                        });
                    }else{
                        layer.msg(data.msg);
                    }
                    /*if(data==null){
                        $("#msg").text("服务器错误，请联系管理员").show();
                        return;
                    }
                    $("#msg").text(data.msg).show();*/
                },
                url : "${ctx}/auditorkeyword/importBatch",
                type : "post",
                timeout:30000000
            };
            $("#importForm").ajaxSubmit(options);
            layer.close(confirmIndex);
        }, function(){
            layer.close(confirmIndex);
            return;
        });
    }
    //添加
    function addModal(){
        layer.open({
            type: 1,
            skin: 'layui-layer-demo', //样式类名
            closeBtn: 1,
            anim: 2,
            title:'新增审核关键字',
            area: ['500px', '240px'],
            shadeClose: true, //开启遮罩关闭
            content: $("#add-modal")
        });
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
            url:"${ctx}/auditorkeyword/exportkeyword",

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


    function add() {
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
            url: "${ctx}/auditorkeyword/addkeywords",
            data:{
                categoryId : $('#categoryId').val(),
                keyword : keyword,
                remarks : remarks
            },
            success: function(data){
                if(data.success==null){
                    layer.msg("服务器错误，请联系管理员");
                    return;
                }
                if(data.success){
                    layer.msg("添加成功",{icon:1,time:800},function () {
                        layer.closeAll();
                        $('#_search').click();
                    });
                }else{
                    layer.msg(data.msg);
                }
            }
        })

    }


    //编辑
    function edit(btn, id){
        layer.open({
            type: 2,
            skin: 'layui-layer-demo', //样式类名
            closeBtn: 1,
            anim: 2,
            title:'编辑审核关键字',
            area: ['500px', '240px'],
            shadeClose: true, //开启遮罩关闭
            content: "${ctx}/auditorkeyword/editkeywords?id=" + id,
            end:function () {
                $('#_search').click();
            }
        });
    }

    //关键字管理
    function del(btn,id,keyword){
        layer.confirm('确认删除关键字：<span class="red">'+keyword+'</span> ？', {
            btn: ['删除','取消'] //按钮
        }, function(){
            $.ajax({
                type: "post",
                <%--url: "${ctx}/auditorkeyword/keywordsmanage/add",--%>
                url: "${ctx}/auditorkeyword/delkeywords",
                data:{
                    id : id
                },
                success: function(data){
                    if(data.success==null){
                        layer.msg("服务器错误，请联系管理员");
                        return;
                    }
                    if(data.success){
                        layer.msg("删除成功",{icon:1,time:800},function () {
                            layer.closeAll();
                            $('#_search').click();
                        });
                    }else{
                        layer.msg(data.msg);
                    }
                }
            })
        }, function() {
            layer.closeAll();
        });

        <%--location.href="${ctx}/auditorkeyword/keywordsmanage?categoryId=" + categoryId;--%>
    }

</script>
</body>
</html>