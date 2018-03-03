<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>超频关键字管理</title>
<style type="text/css">
.width-initial{
	width: initial !important;
}
</style>
</head>
<body menuId="247">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<!-- 批量导入弹出框 -->
					<div class="modal hide fade" id="importExcelBox" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="keyboard">
						<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
							<h3 id="myModalLabel">导入审核/超频关键字 &nbsp;&nbsp;&nbsp;</h3>
						</div>
						<div class="modal-body text-center">
							<form class="form-horizontal" method="post" id="importForm">
								<input type="file" id="importInput" name="upload" accept=".xls"/>
							</form>
							<div class="controls">
								<span id="msg" style="color:red;" style="display:none;"></span><br/>
								<span id="tips" style="display:none;">点击<a href='${ctx}/sysConf/auditKeyWords/exportError'><font color="green">下载</font></a>失败列表</span>
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
						
						<u:authority menuId="271">
							<span class="label label-info">
								<a href="javascript:;" onclick="exportExcel()">导出Excel</a>
							</span>
						</u:authority>
						<u:authority menuId="248">
							<span class="label label-info" data-toggle="modal" data-target="#importExcelBox" >
								<a href="javascript:;" onclick="">批量导入</a>
							</span>
							<span class="label label-info">
								<a href="javascript:;" onclick="add()">添加</a>
							</span>
						</u:authority>
						
						<div class="search width-initial">
						 	 <form method="post" id="mainForm" action="${ctx}/sysConf/auditKeyWords/query">
								<ul>
									<li>
										<input type="text" name="keyword" value="<s:property value="#parameters.keyword"/>" maxlength="60"
											placeholder="超频关键字" class="txt_250" />
									</li>
									<li>
										<input type="text" name="clientid" value="<s:property value="#parameters.clientid"/>" maxlength="6"
											placeholder="用户账号" class="txt_250" />
									</li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>超频关键字</th>
					                 <th>用户帐号</th>
					                 <th>备注</th>
					                 <th>更新时间</th>
					                 <u:authority menuId="248">
					                 	<th>操作</th>
					                 </u:authority>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="jpage.data" status="st">
									<tr>
										<td>${st.index + 1}</td>
										<td>${keyword}</td>
										<td>${clientid}</td>
										<td>${remarks}</td>
										<td>
											<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${updateDate}" />
										</td>
										<u:authority menuId="248">
											<td>
												<a href="javascript:;" onclick="edit('${id}')">编辑</a>&nbsp;&nbsp;&nbsp;
												<a href="javascript:;" onclick="deleteConf('${id}', '${keyword}')">删除</a>
											</td>
										</u:authority>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
						<u:jpage jpage="${jpage}" formId="mainForm" />
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	
		$(function(){
			$('#importExcelBox').on('hide.bs.modal', function () {
				  closeImport();
			})
		});
		
		function closeImport(){
			$("#msg").hide();
			window.location.reload();
		}


        //添加
        function add() {
            location.href = "${ctx}/sysConf/auditKeyWords/edit";
        }
		// 添加/编辑
		function edit(id) {
			location.href = "${ctx}/sysConf/auditKeyWords/edit?id=" + id;

		}
		
		//删除
		function deleteConf(id, keyword) {
			if (confirm("确定要删除 " + keyword + " 吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/auditKeyWords/delete",
					data : {
						id : id
					},
					success : function(data) {
                        if (data.code == 500) {
                            layer.msg(data.msg, {icon:2});
                            //	alert("服务器错误，请联系管理员");
                            return;
                        }

                        layer.msg(data.msg, {icon: 1,time: 1500},function(){
                            location.reload();
                        });
//                        alert(data.msg);
//						if (data.result == "success") {
//							window.location.reload();
//						}
					}
				});
			}
		}
		
		function downloadExcelTemplate(){
			var url = "${ctx}/sysConf/auditKeyWords/downloadExcelTemplate";
			jQuery('<form action="'+url+'" method="post"></form>').appendTo('body').submit().remove();
		}
		
		function importExcel(btn) {
			$("#msg").hide();
			$("#tips").hide();
			var index = "" ;
			var options = {
					beforeSubmit : function() {
						$(btn).attr("disabled", true);
						index = layer.load(1, {
						    shade: [0.5,'#fff'] //0.5透明度的白色背景
						});
					},
					success : function(data) {
						$(btn).attr("disabled", false);
						layer.close(index);
						if(data==null){
							$("#msg").text("服务器错误，请联系管理员").show();
							return;
						}
						$("#msg").text(data.msg).show();
						if(data.data.existFail == 1){
							$("#tips").show();
						}
					},
					url : "${ctx}/sysConf/auditKeyWords/importExcel",
					type : "post",
					timeout: 30000000
			};
			$("#importForm").ajaxSubmit(options);
		}
		
		function exportExcel(){
			var totalCount = ${jpage.totalRecord};
			if (totalCount == 0) {
				layer.msg("共0条纪录，导出Excel文件失败", {icon: 1,time: 2000}); 
				return;
			}
			if(totalCount>50000){
				layer.msg("导出Excel文件条数大于50000条", {icon: 1,time: 2000}); 
				return;
			}
			
			var mainForm = $("#mainForm");
			var action = mainForm.attr("action");

			mainForm.attr("action", "${ctx}/sysConf/auditKeyWords/exportExcel").submit();
			mainForm.attr("action", action);
		}
		
	</script>
</body>
</html>