<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
	<title>权限管理</title>
</head>

<body menuId="8">
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
						<u:authority roleIds="1">
							<span class="label label-info">
								<a href="javascript:;" onclick="add()">添加身份</a>
							</span>
						</u:authority>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/authority/query">
							<ul>
								<li>
									<input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40"
									placeholder="身份名称" class="txt_250" />
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
									 <th width="50px">序号</th>
					                <th>身份名称</th>
					    			<th>状态</th>
									<u:authority roleIds="1">
					                	<th>操作</th>
									</u:authority>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
									<td>${rownum}</td>
									<td>${role_name}</td>
									<td name="status_name">${status==0 ? "禁用" : "正常"}</td>
									<u:authority roleIds="1">
										<td>
											 <a href="javascript:;" onclick="edit('${id}')">编辑</a>
											 <s:if test="status == 1">
												 <s:if test="id != 1"><%--超级管理员不能禁用--%>
		<%-- 								           | <a href="javascript:;" onclick="updateStatus(this, '${id}', 0)">禁用</a> --%>
												   | <a href="javascript:;" onclick="deleteRole(this, '${id}')">删除</a>
												 </s:if>
											 </s:if>
											 <s:else>
		<%-- 							      	 	 | <a href="javascript:;" onclick="updateStatus(this, '${id}', 1)">启用</a> --%>
												 | <a href="javascript:;" onclick="deleteRole(this, '${id}')">删除</a>
											 </s:else>
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
//添加
function add(){
	location.href="${ctx}/authority/add";
}

//编辑
function edit(id){
    location.href="${ctx}/authority/edit?id=" + id;
}

//修改状态：启用、禁用
function updateStatus(a, id, status){
	if(confirm("确定要"+$(a).text()+"吗？")){
		$.ajax({
			type: "post",
			url: "${ctx}/authority/updateStatus",
			data:{
				id : id,
				status : status
			},
			success: function(data){
				if(data.result==null){
					alert("服务器错误，请联系管理员");
					return;
				}
				
				alert(data.msg);
				if(data.result=="success"){
					window.location.reload();
				}
			}
		});
	}
}

//删除角色
function deleteRole(a, id){
	if(confirm("确定要"+$(a).text()+"吗？")){
		$.ajax({
			type: "post",
			url: "${ctx}/authority/deleteRole",
			data:{
				id : id
			},
			success: function(data){
				if(data.result==null){
					alert("服务器错误，请联系管理员");
					return;
				}
				
				if(data.result=="success"){
					$(a).parents("tr").remove();
				}
				alert(data.msg);
			}
		});
	}
}

</script>
</body>
</html>