<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>超频白名单管理</title>
<style type="text/css">
.width-initial{
	width: initial !important;
}
</style>
</head>
<body menuId="138">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
				
					<div class="modal hide fade" id="importExcelBox" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="keyboard">
						<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
							<h3 id="myModalLabel">导入超频白名单 &nbsp;&nbsp;&nbsp;</h3>
						</div>
						<div class="modal-body text-center">
							<form class="form-horizontal" method="post" id="mainForm">
								<input type="file" id="importInput" name="upload" accept=".xls"/>
							</form>
							<div class="controls">
								<span id="msg" style="color:red;" style="display:none;"></span><br/>
								<span id="tips" style="display:none;">点击<a href='${ctx}/overrateWhite/exportError'><font color="green">下载</font></a>失败列表</span>
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
						<span class="label label-info">
								<a href="javascript:;" onclick="exportExcel()">导出通道关键字</a>
						</span>
						<u:authority menuId="139">
							<span class="label label-info" data-toggle="modal" data-target="#importExcelBox" >
									<a href="javascript:;" onclick="">批量导入</a>
							</span>
						</u:authority>
						<u:authority menuId="139">
							<span class="label label-info">
								<a href="javascript:;" onclick="add()">添加</a>
							</span>
						</u:authority>
 						<div class="search width-initial"> 
							<form method="post" id="mainForm" action="${ctx}/overrateWhite/query">
								<ul>
								<li name="testhight1">
									<li><input type="text" name="clientid" value="<s:property value="#parameters.clientid"/>" maxlength="80" placeholder="用户帐号" class="txt_250" /></li>
									<li><input type="text" name="phone" value="<s:property value="#parameters.phone"/>" maxlength="80" placeholder="手机号码" class="txt_250" /></li>
								<li><input type="submit" value="搜索" /></li>
								</ul>
							</form>
						</div> 
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th style='width:5%'>序号</th>
					                 <th>用户帐号</th>
					                 <th>手机号码</th>
					                 <th>备注</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td style='width:10%'>${rownum}</td>
							      <td>${clientid}</td>
							      <td>${phone}</td>
							      <td>${remarks}</td>
							      <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${createtime}" /></td>
							      <td>
							      <%-- <u:authority menuId="140">
							     	 <a href="javascript:;" onclick="edit('${id}')">编辑</a>
							     </u:authority> --%>
							     <u:authority menuId="141">
							     	 <a href="javascript:;" onclick="deleteItem(this,'${id}')">删除</a>
							     </u:authority>	 
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
			location.href = "${ctx}/overrateWhite/edit";
		}
		
		//编辑
		function edit(id) {
			location.href = "${ctx}/overrateWhite/edit?id=" + id;
		}
		
		function deleteItem(a,id){
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/overrateWhite/delete?id=" + id,
					success : function(data) {
						if (data.result == null) {
							alert("服务器错误，请联系管理员");
							return;
						}
						alert(data.msg);
						if (data.result == "success") {
							window.location.reload();
						}
					}
				});
			}
		}
		
		//Excel批量导入
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
						console.log("layer index open: " + index);
					},
					success : function(data) {
						$(btn).attr("disabled", false);
						layer.close(index);
						console.log("layer index close: " + index);
						if(data==null){
							$("#msg").text("服务器错误，请联系管理员").show();
							return;
						}
						$("#msg").text(data.msg).show();
						if(data.existFail == 1){
							$("#tips").show();
						}
					},
					url : "${ctx}/overrateWhite/import",
					type : "post",
					timeout: 30000000
			};
			$("#mainForm").ajaxSubmit(options);
		}
		
		function closeImport(){
			$("#msg").hide();
			window.location.reload();
		}
		
		function downloadExcelTemplate(){
			var url = "${ctx}/overrateWhite/downloadExcelTemplate";
			jQuery('<form action="'+url+'" method="post"></form>').appendTo('body').submit().remove();
		}
		
		function exportExcel(){
			var totalCount = ${page.totalCount};
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

			mainForm.attr("action", "${ctx}/overrateWhite/exportExcel").submit();
			mainForm.attr("action", action);
		}
	</script>
</body>
</html>