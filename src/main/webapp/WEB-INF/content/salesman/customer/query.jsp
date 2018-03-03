<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>客户管理</title>
</head>
<body menuId="55">
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
						<span class="label label-info">
						<u:authority menuId="1">
							<a href="javascript:;" onclick="add(this)">添加客户</a>
						</u:authority>
						</span>
						<div class="search">
						  <form method="post" id="mainForm" action="${ctx}/customer/query">
							<ul>
								<li><input type="text" name="name" value="<s:property value="#parameters.name"/>"  maxlength="50" placeholder="客户名称/销售人员姓名" class="txt_250" /></li>
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
									<th>客户名称</th>
									<th>销售人员姓名</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${customer_name}</td>
										<td>${salesman_name}</td>
						     	  		<td>
						     	  			<a href="javascript:;" onclick="delCustomer(this, '${id}')">删除</a> 
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
		//添加
		function add(btn) {
// 			$("#msg").hide();
// 			var options = {
// 				beforeSubmit : function() {
// 					$(btn).attr("disabled", true);
// 				},
// 				success : function(data) {
// 					$(btn).attr("disabled", false);
// 					if(data==null){
// 						$("#msg").text("服务器错误，请联系管理员").show();
// 						return;
// 					}
// 					$("#msg").text(data.msg).show();
// 				},
// 				url : "${ctx}/salesman/add",
// 				type : "post",
// 				timeout:30000
// 			};
// 			$("#addSalesmanForm").ajaxSubmit(options);

			location.href = "${ctx}/customer/add";
		}
		
		//删除
		function delCustomer(a, id) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/customer/delete",
					data : {
						id : id
					},
					success : function(data) {
						if (data == null) {
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
	</script>
</body>
</html>