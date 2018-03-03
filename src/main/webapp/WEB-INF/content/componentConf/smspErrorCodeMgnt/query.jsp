<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>平台错误码管理</title>
</head>
<body menuId="245">
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
						
						<u:authority menuId="246">
							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
						</u:authority>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/componentConf/smspErrorCodeMgnt/query">
								<ul>
									<li>
										<u:select id="component_type" data="[{value:'',text:'组件类型: 所有'},{value:'00',text:'SMSP_C2S'},{value:'01',text:'SMSP_ACCESS'},{value:'02',text:'SMSP_SEND'}]" 
											value="${component_type}" showAll="false"/>
									</li>
									<li>
										<input type="text" name="syscode" value="<s:property value="#parameters.syscode"/>" maxlength="10"
											placeholder="平台错误码" class="txt_250" />
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
					                 <th>平台错误码</th>
					                 <th>错误类型</th>
					                 <th>数据库状态</th>
					                 <th>错误描述</th>
					                 <th>适用协议</th>
					                 <th>组件类型</th>
					                 <th>备注</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${syscode}</td>
										<td><s:if test="type == 1">应答</s:if>
											<s:elseif test="type==2">状态报告</s:elseif>
										</td>
										<td>${state}</td>
										<td>${errordesc}</td>
										<td><s:if test="usreprotocol==1">全部</s:if>
											<s:elseif test="usreprotocol==2">四大直连</s:elseif>
											<s:elseif test="usreprotocol==3">CMPP</s:elseif>
											<s:elseif test="usreprotocol==4">SGIP</s:elseif>
											<s:elseif test="usreprotocol==5">SMGP</s:elseif>
											<s:elseif test="usreprotocol==7">SMPP</s:elseif>
											<s:elseif test="usreprotocol==8">HTTP</s:elseif>
										</td>
										<td><s:if test="component_type eq '00'">SMSP_C2S</s:if>
											<s:elseif test="component_type eq '01'">SMSP_ACCESS</s:elseif>
											<s:elseif test="component_type eq '02'">SMSP_SEND</s:elseif>
											<s:elseif test="component_type eq '07'">SMSP_REBACK</s:elseif>
											<s:elseif test="component_type eq '08'">SMSP_HTTP</s:elseif>
										</td>
										<td>${remark}</td>
										<td>${updatetime}</td>
										<u:authority menuId="246">
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
				location.href = "${ctx}/componentConf/smspErrorCodeMgnt/edit";
			}else{
				location.href = "${ctx}/componentConf/smspErrorCodeMgnt/edit?id=" + id;
			}
		}

		//删除
		function deleteConf(id) {
			if (confirm("确定要删除" + id + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/componentConf/smspErrorCodeMgnt/delete",
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