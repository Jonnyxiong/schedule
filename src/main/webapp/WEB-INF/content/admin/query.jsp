<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>管理员列表</title>
</head>

<body menuId="7">
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
						<u:authority menuId="7">
							<span class="label label-info">
								<a href="javascript:;" onclick="add()">添加管理员</a>
							</span>
						</u:authority>
						<div class="search">
						 	<form method="post" id="mainForm" action="${ctx}/admin/query">
							<ul>
								<li>
									<u:select id="role_id" value="${param.role_id}" placeholder="管理员身份：所有" sqlId="role" showAll="true" />
								</li>
								<li>
									<input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40"
									placeholder="管理员账号/联系手机/真实姓名" class="txt_250" />
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
					                 <th>管理员账号</th>
					                 <th>管理员id</th>
					                 <th>管理员身份</th>
					                 <th>真实姓名</th>
					                 <th>联系方式</th>
					                 <th>状态</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td>${email}</td>
							      <td>${id}</td>
							      <td>${role_name}</td>
							      <td>${realname}</td>
							      <td>${mobile}</td>
							      <td name="status_name">${status==1 ? "正常" : "禁用"}</td>
							      <td>
									  <c:choose>
										  <c:when test="${sessionScope.LOGIN_ROLE_ID eq 1}">
											  <a href="javascript:;" onclick="edit('${id}')">编辑</a>
											  <c:if test="${(status eq 1) and (sessionScope.LOGIN_USER_ID ne id)}">
												  | <a href="javascript:;" onclick="updateStatus(this, '${id}', 2)">禁用</a>
											  </c:if>
											  <c:if test="${status eq 2}">
												  | <a href="javascript:;" onclick="updateStatus(this, '${id}', 1)">启用</a>
											  </c:if>
											  <c:if test="${(sessionScope.LOGIN_USER_ID ne id)}">
											      | <a href="javascript:;" onclick="updateStatus(this, '${id}', 3)">删除</a>
											  </c:if>
										  </c:when>


										  <c:when test="${sessionScope.LOGIN_ROLE_ID ne 1}">
											  <c:if test="${role_id ne 1}">
												<a href="javascript:;" onclick="edit('${id}')">编辑</a>
											  </c:if>
											  <!-- 不能禁用自己和超级管理员 -->
											  <c:if test="${(status eq 1) and (role_id ne 1) and (sessionScope.LOGIN_USER_ID ne id)}">
												  | <a href="javascript:;" onclick="updateStatus(this, '${id}', 2)">禁用</a>
											  </c:if>
											  <c:if test="${status eq 2}">
												 | <a href="javascript:;" onclick="updateStatus(this, '${id}', 1)">启用</a>
											  </c:if>
											  <!-- 不能删除自己和超级管理员 -->
											  <c:if test="${(sessionScope.LOGIN_USER_ID ne id) and (role_id ne 1)}">
												 | <a href="javascript:;" onclick="updateStatus(this, '${id}', 3)">删除</a>
											  </c:if>
										  </c:when>
									  </c:choose>

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
			location.href = "${ctx}/admin/add";
		}

		//编辑
		function edit(id) {
			location.href = "${ctx}/admin/edit?id=" + id;
		}

		//修改状态：恢复、删除
		function updateStatus(a, id, status) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/admin/updateStatus",
					data : {
						id : id,
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