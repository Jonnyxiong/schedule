<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>自签通道用户端口管理</title>
</head>
<body menuId="186">
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
						<u:authority menuId="187">
						<span class="label label-info"> 
							<a href="javascript:;" onclick="add()">添加</a>
						</span>
						</u:authority>
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/channelExtendport/query">
								<ul>
									<li>
										<input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40"
											placeholder="通道号/用户账号" class="txt_250" />
										<input type="text" name="extendport" value="<s:property value="#parameters.extendport"/>" maxlength="40"
											placeholder="通道用户端口" class="txt_250" />
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
									<th>通道号</th>
									<th>用户账号</th>
									<th>通道用户端口</th>
									<th>备注</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${channelid}</td>
										<td>${clientid}</td>
										<td>${extendport}</td>
										<td>${remark}</td>
										<td>
								      	 <u:authority menuId="188">
								      	 	<a href="javascript:;" onclick="edit('${id}')">编辑</a>
								      	 </u:authority>
								      	 <u:authority menuId="189">
								      	 	| <a href="javascript:;" onclick="deleteData('${id}')">删除</a>
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
		//添加
		function add() {
			location.href = "${ctx}/channelExtendport/edit";
		}

		//编辑
		function edit(id) {
			location.href = "${ctx}/channelExtendport/edit?id=" + id;
		}

		//删除
		function deleteData(id) {
			if (confirm("确定要删除吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/channelExtendport/delete",
					data : {
						id : id
					},
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
		//@ sourceURL=login.js
	</script>
	
</body>
</html>