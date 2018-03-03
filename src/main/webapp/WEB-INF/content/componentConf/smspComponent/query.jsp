<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>SMSP组件配置</title>
</head>
<body menuId="237">
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
							<a href="javascript:;" onclick="edit(null)">添加</a>
						</span>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/componentConf/smspComponent/query">
								<ul>
									<li>
										<u:select id="component_type" data="[{value:'-1',text:'SMSP组件类型：所有'},{value:'00',text:'SMSP_C2S'},{value:'01',text:'SMSP_ACCESS'},
																		{value:'02',text:'SMSP_SEND'},{value:'03',text:'SMSP_REPORT'},{value:'04',text:'SMSP_AUDIT'},
																		{value:'05',text:'SMSP_CHARGE'},{value:'06',text:'SMSP_CONSUMER'},{value:'07',text:'SMSP_REBACK'},{value:'08',text:'SMSP_HTTP'}]"
										value="${component_type}" showAll="true"/>
									</li>
									<li>
										<input type="text" name="component_name" value="<s:property value="#parameters.component_name"/>" maxlength="10"
											placeholder="SMSP组件名称" class="txt_250" />
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
					                 <th>组件ID</th>
					                 <th>组件类型</th>
					                 <th>组件名称</th>
					                 <th>组件IP地址</th>
					                 <th>所属机房</th>
					                 <th>连接REDIS线程数量</th>
					                 <th>SGIP状态报告</th>
					                 <th>MQ_ID</th>
					                 <th>消费者开关</th>
					                 <th>黑名单加载开关</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${component_id}</td>
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
										<td>${component_name}</td>
										<td>${host_ip}</td>
										<td><s:if test="node_id==10">北京亦庄</s:if>
										    <s:elseif test="node_id==11">北京兆维</s:elseif>
										    <s:elseif test="node_id==12">北京互联港湾</s:elseif>
										</td>
										<td>${redis_thread_num}</td>
										<td>
											<s:if test="sgip_report_switch==0">关闭</s:if>
										    <s:elseif test="sgip_report_switch==1">开启</s:elseif>
										</td>
										<td>${mq_id}</td>
										<td>
											<s:if test="component_switch==0">关闭</s:if>
										    <s:elseif test="component_switch==1">开启</s:elseif>
										</td>
										<td>
											<s:if test="black_list_switch==0">关闭</s:if>
										    <s:elseif test="black_list_switch==1">开启</s:elseif>
										</td>
										<td>${update_date}</td>
										<td>
											<u:authority menuId="238">
												<a href="javascript:;" onclick="edit('${id}')">编辑</a>
												<a href="javascript:;" onclick="deleteConf('${id}','${component_id}')">删除</a>
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

		// 添加/编辑
		function edit(id) {
			if(null == id){
				location.href = "${ctx}/componentConf/smspComponent/edit";
			}else{
				location.href = "${ctx}/componentConf/smspComponent/edit?id=" + id;
			}
		}

		//删除
		function deleteConf(id, component_id) {
			if (confirm("确定要删除组件ID为 " + component_id + " 这条记录吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/componentConf/smspComponent/delete",
					data : {
						id : id,
						component_id : component_id
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