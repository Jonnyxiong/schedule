<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>业务预警阈值管理</title>
</head>
<body menuId="253">
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
						
						<u:authority menuId="254">
							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
						</u:authority>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/sysConf/businessWarnMgnt/query">
								<ul>
									<li>
										<input type="text" name="clientid" value="${clientid_search}" maxlength="21"
											placeholder="用户账号" class="txt_254" />
									</li>
									<li>
					                  	<u:select id="warn_type" value="${warn_type_search}" data="[{value:'',text:'告警类型：所有'},{value:'0',text:'TPS告警'},
					                  	{value:'1',text:'超频告警'},{value:'2',text:'计费失败告警'}]" showAll="false" />
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
					                 <th>告警类型</th>
					                 <th>一段时间告警值（条）</th>
					                 <th>一段时间值（秒）</th>
					                 <th>全天告警值（条）</th>
					                 <th>更新时间</th>
					                 <u:authority menuId="254">
					                 	<th>操作</th>
					                 </u:authority>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${clientid}</td>
										<td>
											<s:if test="warn_type==0">TPS告警</s:if>
											<s:elseif test="warn_type==1">超频告警</s:elseif>
											<s:elseif test="warn_type==2">计费失败告警</s:elseif>
										</td>
										<td>${time_number}</td>
										<td>${time_data}</td>
										<td>${all_time_number}</td>
										<td>${updatetime}</td>
										<u:authority menuId="254">
											<td>
												<a href="javascript:;" onclick="edit('${id}')">编辑&nbsp;&nbsp;&nbsp;</a>
												<a href="javascript:;" onclick="deleteConf('${id}', '${clientid}', '${warn_type}')">删除&nbsp;&nbsp;&nbsp;</a>
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
				location.href = "${ctx}/sysConf/businessWarnMgnt/edit";
			}else{
				location.href = "${ctx}/sysConf/businessWarnMgnt/edit?id=" + id;
			}
		}
		
		//删除
		function deleteConf(id, clientid, warn_type) {
			var warnTypeName = "";
			if(warn_type == "0"){
				warnTypeName = "TPS告警设置";
			}else if(warn_type == "1"){
				warnTypeName = "超频告警设置";
			}else if(warn_type == "2"){
				warnTypeName = "计费失败告警设置";
			}else{
			}
			
			if (confirm("确定要删除 " + clientid + " 的" + warnTypeName + "该吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/businessWarnMgnt/delete",
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