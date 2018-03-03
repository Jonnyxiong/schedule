<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>系统关键字管理</title>
<style>
.widget-title{
	overflow:visible;
}
.widget-title:after{
	content:'';
	display:table;
	clear:both;
}
.btn-group{
	border-radius:4px;
	top:5px;
}
.dropdown-menu{
	border-radius:4px;
}
.dropdown-menu>li>a:hover, .dropdown-menu>li>a:focus, .dropdown-submenu:hover>a, .dropdown-submenu:focus>a {
    color: #fff;
    text-decoration: none;
    background-color: #28b779;
    background-image: -moz-linear-gradient(top,#28b779,#28b779);
    background-image: -webkit-gradient(linear,0 0,0 100%,from(#28b779),to(#28b779));
    background-image: -webkit-linear-gradient(top,#28b779,#28b779);
    background-image: -o-linear-gradient(top,#28b779,#28b779);
    background-image: linear-gradient(to bottom,#28b779,#28b779);
    background-repeat: repeat-x;
    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff0088cc',endColorstr='#ff0077b3',GradientType=0);
}

.width-initial{
	width: initial !important;
}
</style>
</head>
<body menuId="12">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
				
					<!-- 批量导入关键字弹出框 -->
					<div class="modal hide fade" id="importExcelBox" tabindex="-1" role="dialog">
						<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
							<h3 id="myModalLabel">批量导入关键字</h3>
						</div>
						<div class="modal-body text-center">
							<form class="form-horizontal" method="post" id="importForm">
								<input type="file" id="importInput" name="upload" accept=".xls"/>
							</form>
							<div class="controls">
								<span id="msg" style="color:red;" style="display:none;"></span>
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
						
						<u:authority menuId="270">
							<span class="label label-info">
									<a href="javascript:;" onclick="exportExcel()">导出系统关键字</a>
							</span>
						</u:authority>
						<u:authority menuId="91">
						<span class="label label-info" data-toggle="modal" data-target="#importExcelBox" >
								<a href="javascript:;" onclick="">批量导入</a>
						</span>
						</u:authority>
						<u:authority menuId="91">
							<span class="label label-info">
									<a href="javascript:;" onclick="add()">添加</a>
							</span>
						</u:authority>
						
						<div class="search width-initial"> 
							<form method="post" id="mainForm" action="${ctx}/keywords/query">
								<ul>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="系统关键字" class="txt_250" /></li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
							</form>
						</div>
						
						<!-- <div class="btn-group">
						  <button class="btn">Action</button>
						  <button class="btn dropdown-toggle" data-toggle="dropdown">
						    <span class="caret"></span>
						  </button>
						  <ul class="dropdown-menu">
						    <li><a>a</a></li>
						    <li><a>b</a></li>
						    <li><a>c</a></li>
						    <li><a>d</a></li>
						  </ul>
						</div> -->
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>系统关键字</th>
					                 <th>添加时间</th>
									 <th>备注</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td><c:out value="${keyword}"/></td>
							      <td>${createtime}</td>
								  <td>${remarks}</td>
							      <td>
							     	 <u:authority menuId="92"><a href="javascript:;" onclick="deleteKeyword(this, '${id}')">删除</a></u:authority> 
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
			// 批量导入弹出框隐藏时事件
			$('#importExcelBox').on('hide.bs.modal', function () {
				  closeImport();
			})
		});
	
		//添加
		function add() {
			location.href = "${ctx}/keywords/add";
		}

		//修改状态：恢复、删除
		function deleteKeyword(a, id) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				location.href = "${ctx}/keywords/deleteKeyword?id="+id;
			}
		}
		
		//Excel批量导入关键字
		function importExcel(btn) {
			$("#msg").hide();
			var options = {
					beforeSubmit : function() {
						$(btn).attr("disabled", true);
					},
					success : function(data) {
						$(btn).attr("disabled", false);
						
						if(data.result==null){
							$("#msg").text("服务器错误，请联系管理员").show();
							return;
						}
						
						$("#msg").text(data.msg).show();
					},
					url : "${ctx}/keywords/import",
					type : "post",
					async : false,
					timeout:30000
				};
			$("#importForm").ajaxSubmit(options);
		}
		
		function closeImport(){
			$("#msg").hide();
			window.location.reload();
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

			mainForm.attr("action", "${ctx}/keywords/exportExcel").submit();
			mainForm.attr("action", action);
		}
		
		function downloadExcelTemplate(){
			var url = "${ctx}/keywords/downloadExcelTemplate";
			jQuery('<form action="'+url+'" method="post"></form>').appendTo('body').submit().remove();
		}
		
	</script>
</body>
</html>