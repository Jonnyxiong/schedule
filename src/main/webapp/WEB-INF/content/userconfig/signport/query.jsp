<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>用户帐号和签名端口对应关系</title>
</head>
<body menuId="86">
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
						<u:authority menuId="130">
						<span class="label label-info">
								<a href="javascript:;" onclick="add()">添加</a>
						</span>
						</u:authority>
						<div class="search"> 
							<form method="post" id="mainForm" action="${ctx}/signport/query">
								<ul>
									<li><input type="text" name="clientid"
										value="<s:property value="#parameters.clientid"/>" maxlength="40"
										placeholder="子账号" class="txt_250" /></li>
									<li><input type="text" name="sign"
										value="<s:property value="#parameters.sign"/>" maxlength="40"
										placeholder="签名" class="txt_250" /></li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th style='width:5%'>序号</th>
					                 <th>子帐号</th>
					                 <th>签名</th>
					                 <th>签名端口</th>
					                 <th>状态</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td style='width:5%'>${rownum}</td>
							      <td>${clientid}</td>
							      <td>${sign}</td>
							      <td>${signport}</td>
							      <td>${status==0?'正常':'关闭'}</td>
							      <td>${updatedate}</td>
							      <td>
							      <u:authority menuId="132">
							     	 <a href="javascript:;" onclick="edit('${id}')">编辑</a>
							      </u:authority>
							      <u:authority menuId="131">
							     	 <s:if test="status == 0">
							     	 	| <a href="javascript:;" onclick="updateStatus(this, 1, '${id}')">关闭</a>
							     	 </s:if>
							     	 <s:else>
							     	 	| <a href="javascript:;" onclick="updateStatus(this, 0, '${id}')">开启</a>
							     	 </s:else>
<%-- 							     	 | <a href="javascript:;" onclick="del(this, '${id}')">删除</a> --%>
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
		$(function(){
		});
		//添加
		function add() {
			location.href = "${ctx}/signport/edit";
		}
		
		//编辑
		function edit(id) {
			location.href = "${ctx}/signport/edit?id=" + id;
		}
		
		function updateStatus(a, status, id) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/signport/updateStatus",
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

		//修改状态：删除
		function del(a, id) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/signport/delete?id=" + id,
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