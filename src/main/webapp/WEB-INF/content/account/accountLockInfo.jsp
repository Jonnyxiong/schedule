<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>子账户锁定管理</title>
</head>

<body menuId="62">
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
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/account/lockInfo/query">
								<ul>
									<li>
										<input type="text" name="text"
											value="<s:property value="#parameters.text"/>" maxlength="40"
											placeholder="子账号/锁定代码" class="txt_250" />
									</li>
									<li>
					                  	<u:select id="status" value="${param.status}" data="[{value:'',text:'状态:所有'},{value:'0',text:'锁定'},{value:'1',text:'解锁'}]" showAll="false" />
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
									<th>子账号</th>
									<th>锁定代码</th>
									<th>锁定原因</th>
									<th>锁定时间</th>
									<th>解锁时间</th>
									<th>解锁人</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${clientid}</td>
										<td>${code}</td>
										<td>${remarks}</td>
										<td>${locktime}</td>
										<td>${unlocktime}</td>
										<td>${unlockby}</td>
										<td>
											<s:if test="status == 0">锁定</s:if> 
											<s:if test="status == 1">解锁</s:if> 
										<td>
										<u:authority menuId="121">
											<s:if test="status == 0">
												<a href="javascript:;"
													onclick="unlockAccount(this, '${clientid}', '${sessionScope.LOGIN_USER_REALNAME}')">解锁</a> 
											</s:if>
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
		function unlockAccount(a, clientid, operator) {
			if (confirm("确定要" + $(a).text() + clientid + "账户吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/account/lockInfo/unlockAccount",
					data : {
						clientid : clientid,
						operator : operator
					},
					success : function(data) {
						if (data.result == null) {
							alert("服务器错误，请联系管理员");
							return;
						}
						alert(data.msg);
						if (data.result == "success") {
							location.href = "${ctx}/account/lockInfo/query"
// 							window.location.reload();
						}
					}
				});
			}
		}
	</script>
</body>
</html>