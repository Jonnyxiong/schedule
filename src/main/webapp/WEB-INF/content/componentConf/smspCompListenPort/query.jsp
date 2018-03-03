<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>SMSP组件端口配置</title>
</head>
<body menuId="239">
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
						
						<u:authority menuId="240">
							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
						</u:authority>
						
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/componentConf/smspCompListenPort/query">
								<ul>
									<li>
										<u:select id="component_type" data="[{value:'-1',text:'SMSP组件类型：所有'},{value:'00',text:'SMSP_C2S'},{value:'01',text:'SMSP_ACCESS'},
																		{value:'02',text:'SMSP_SEND'},{value:'03',text:'SMSP_REPORT'},{value:'04',text:'SMSP_AUDIT'},
																		{value:'05',text:'SMSP_CHARGE'},{value:'06',text:'SMSP_CONSUMER'},{value:'07',text:'SMSP_REBACK'},
																		{value:'08',text:'SMSP_HTTP'}]"
										value="${component_type}" showAll="true"/>
									</li>
									<li>
										<input type="text" name="port_key" value="<s:property value="#parameters.port_key"/>" maxlength="10"
											placeholder="服务名称" class="txt_250" />
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
					                 <th>服务名称</th>
					                 <th>服务端口</th>
					                 <th>SMSP组件类型</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${port_key}</td>
										<td>${port_value}</td>
										<td><s:if test="component_type eq '00'">SMSP_C2S</s:if>
										    <s:elseif test="component_type eq '01'">SMSP_ACCESS</s:elseif>
										    <s:elseif test="component_type eq '02'">SMSP_SEND</s:elseif>
										    <s:elseif test="component_type eq '03'">SMSP_REPORT</s:elseif>
											<s:elseif test="component_type eq '04'">SMSP_AUDIT</s:elseif>
											<s:elseif test="component_type eq '05'">SMSP_CHARGE</s:elseif>
											<s:elseif test="component_type eq '06'">SMSP_CONSUMER</s:elseif>
											<s:elseif test="component_type eq '07'">SMSP_REBACK</s:elseif>
											<s:elseif test="component_type eq '08'">SMSP_HTTP</s:elseif>
										</td>
										<td>${update_date}</td>
										<u:authority menuId="240">
											<td>
												<a href="javascript:;" onclick="edit('${id}')">编辑</a>
												<a href="javascript:;" onclick="deleteConf('${id}')">删除</a>
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
				location.href = "${ctx}/componentConf/smspCompListenPort/edit";
			}else{
				location.href = "${ctx}/componentConf/smspCompListenPort/edit?id=" + id;
			}
		}

		//删除
		function deleteConf(id) {
			if (confirm("确定要删除" + id + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/componentConf/smspCompListenPort/delete",
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