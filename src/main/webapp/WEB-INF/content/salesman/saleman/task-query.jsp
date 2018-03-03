<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>月回款任务管理</title>
</head>
<body menuId="58">
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
							<a href="javascript:;" onclick="add(this)">添加月回款任务</a>
						</u:authority>
						</span>
						<div class="search">
						  <form method="post" id="mainForm" action="${ctx}/salesman/task/query">
							<ul>
								<li>
									<u:date id="start_time" placeholder="开始时间" value="${start_time}" readOnly="false" dateFmt="yyyy-MM" params="" />至
									<u:date id="end_time" placeholder="结束时间" value="${end_time}" readOnly="false" dateFmt="yyyy-MM" params="" />
								</li>
								<li><input type="text" name="name" value="<s:property value="#parameters.name"/>"  maxlength="50" placeholder="销售人员姓名" class="txt_250" /></li>
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
									<th>销售人员姓名</th>
									<th>销售人员本月回款任务额</th>
									<th>月份</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${salesman_name}</td>
										<td>${returned_money_task}</td>
										<td>${taskofmoth}</td>
						     	  		<td>
						     	  			<a href="javascript:;" onclick="delTask(this, '${id}')">删除</a> 
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
			location.href = "${ctx}/salesman/task/add";
		}
		
		//删除
		function delTask(a, id) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/salesman/task/delete",
					data : {
						id : id
					},
					success : function(data) {
						if (data == null || data.result == "fail") {
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