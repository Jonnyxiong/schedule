<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>模板类型管理</title>
</head>
<body menuId="46">
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
						<span class="label label-info"> <u:authority menuId="1">
								<a href="javascript:;" onclick="add()">添加模板类型</a>
							</u:authority>
						</span>
						
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>模板类型</th>
									<th>是否区分运营商</th>
									<th>通道选择策略</th>
									<th>全网通道</th>
									<th>移动通道</th>
									<th>联通通道</th>
									<th>电信通道</th>
									<th>国际通道</th>
									<th>备注</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>
										<s:if test="templatetype==0">
											普通短信
										</s:if>
										<s:if test="templatetype==1">
											云验证码注册类短信
										</s:if>
										<s:if test="templatetype==2">
											云验证码动态登录类短信
										</s:if>
										<s:if test="templatetype==3">
											云验证码手机绑定类短信
										</s:if>
										<s:if test="templatetype==4">
											验证码短信
										</s:if>
										<s:if test="templatetype==5">
											订单通知模板类短信
										</s:if>
										</td>
										<td>
										<s:if test="distoperators==0">否</s:if>
										<s:if test="distoperators==1">是</s:if>
										</td>
										
										<td>
											<s:if test="distoperators==0">
												${policy}
											</s:if>
											<s:if test="distoperators==1">
												<u:truncate length="20" value="全网：${policy }，移动：${ydpolicy}，联通：${ltpolicy}，电信：${dxpolicy}，国际：${gjpolicy}"></u:truncate>
											</s:if>
										</td>
										
										<td>${channelid}</td>
										<td>${ydchannelid}</td>
										<td>${ltchannelid}</td>
										<td>${dxchannelid}</td>
										<td>${gjchannelid}</td>
										
										<td>${remarks}</td>
										<td name="status_name">${state==1 ? "开启" : "已关闭"}</td>
										<td><s:if test="state==0">
												<a href="javascript:;"
													onclick="updateStatus(this, '${id}',1)">开启</a> 
								      	| <a href="javascript:;" onclick="edit('${id}')">编辑</a>
								      	| <a href="javascript:;"
													onclick="deleteData('${id}')">删除</a>
											</s:if> <s:if test="state==1">
												<a href="javascript:;"
													onclick="updateStatus(this, '${id}',0)">关闭</a>
											</s:if></td>
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
			location.href = "${ctx}/confirm/edit";
		}

		//编辑
		function edit(id) {
			location.href = "${ctx}/confirm/edit?id=" + id;
		}

		//修改状态
		function updateStatus(a, id, state) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/confirm/updateStatus",
					data : {
						id : id,
						state : state
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
		
		
		//删除
		function deleteData(id) {
			if (confirm("确定要删除吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/confirm/delete",
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