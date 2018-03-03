<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>业务预警人员号码管理</title>
</head>
<body menuId="255">
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
						
						<u:authority menuId="256">
							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
						</u:authority>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/sysConf/businessWarnPhoneMgnt/query">
								<ul>
									<li>
										<input type="text" name="clientid" value="${clientid_search}" maxlength="21"
											placeholder="用户账号" class="txt_254" />
									</li>
									<li>
										<input type="text" name="phone" value="${phone_search}" maxlength="11"
											placeholder="告警号码" class="txt_250" />
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
					                 <th>用户账号</th>
					                 <th>告警电话</th>
					                 <th>更新时间</th>
					                 <u:authority menuId="256">
					                 	<th>操作</th>
					                 </u:authority>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${clientid}</td>
										<td>${phone}</td>
										<td>${updatetime}</td>
										<u:authority menuId="256">
											<td>
												<a href="javascript:;" onclick="edit('${id}')">编辑&nbsp;&nbsp;&nbsp;</a>
												<a href="javascript:;" onclick="deleteConf('${id}', '${clientid}')">删除&nbsp;&nbsp;&nbsp;</a>
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
				location.href = "${ctx}/sysConf/businessWarnPhoneMgnt/edit";
			}else{
				location.href = "${ctx}/sysConf/businessWarnPhoneMgnt/edit?id=" + id;
			}
		}
		
		//删除
		function deleteConf(id, clientid) {
			
			if (confirm("确定要删除 " + clientid + " 的告警号码该吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/businessWarnPhoneMgnt/delete",
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
		
		
	</script>
</body>
</html>