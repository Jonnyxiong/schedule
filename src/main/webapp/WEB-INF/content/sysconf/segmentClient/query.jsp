<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>强制路由客户管理</title>
</head>
<body menuId="251">
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
						
						<u:authority menuId="252">
							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
						</u:authority>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/sysConf/segmentClient/query">
								<ul>
									<li>
										<input type="text" name="client_code" value="${client_code_search}"" maxlength="10"
											placeholder="客户代码" class="txt_250" />
									</li>
									<li>
										<input type="text" name="clientid" value="${clientid_search}"" maxlength="6"
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
					                 <th>客户代码</th>
					                 <th>用户账号</th>
					                 <u:authority menuId="252">
					                 	<th>操作</th>
					                 </u:authority>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${client_code}</td>
										<td>${clientid}</td>
										<u:authority menuId="252">
											<td>
												<a href="javascript:;" onclick="edit('${id}')">编辑&nbsp;&nbsp;&nbsp;</a>
												<a href="javascript:;" onclick="deleteConf('${id}', '${client_code}', '${clientid}')">删除&nbsp;&nbsp;&nbsp;</a>
											</td>
										</u:authority>
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
		// 添加/编辑
		function edit(id) {
			if(null == id){
				location.href = "${ctx}/sysConf/segmentClient/edit";
			}else{
				location.href = "${ctx}/sysConf/segmentClient/edit?id=" + id;
			}
		}
		
		//删除
		function deleteConf(id, client_code, clientid) {
			if (confirm("确定要删除" + client_code + "客户代码下面的" + clientid + "用户吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/segmentClient/delete",
					data : {
						id : id,
						client_code : client_code,
						clientid : clientid
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
		
		function updateStatus(phone_segment, status){
			var confirmText = "";
			if(status == 0){
				confirmText = "关闭";
			}else{
				confirmText = "开启";
			}
			if (confirm("确定要" + confirmText + phone_segment + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/segmentClient/updateStatus",
					data : {
						phone_segment : phone_segment,
						status : status
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
		
	</script>
</body>
</html>