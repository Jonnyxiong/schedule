<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>平台帐号未配置通道列表</title>
</head>
<body menuId="84">
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
						 	 <form method="post" id="mainForm" action="${ctx}/channelNoroute/query">
							<ul>
								<li name="testhight1">
									<u:date id="start_time" value="${start_time}" placeholder="开始时间" dateFmt="yyyyMMdd" />
									<span>至</span>
            						<u:date id="end_time" value="${end_time}" placeholder="结束时间" dateFmt="yyyyMMdd" />
								</li>
								<li><u:select id="status" data="[{value:'-1',text:'状态:所有'},{value:'0',text:'正常'},{value:'1',text:'关闭'}]" value="${status}" showAll="false" /></li>
								<li><input type="text" name="query_sid" value="<s:property value="#parameters.query_sid"/>" maxlength="40" placeholder="sid" class="txt_250" /></li>
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
									 <th>平台账号</th>
									 <th>状态</th>
									 <th>时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
									<td>${rownum}</td>
									<td>${sid}</td>
									<td>
										<s:if test="status == 0">正常</s:if>
										<s:if test="status == 1">关闭</s:if>
									</td>
									<td>${time}</td>
							      <td>
							      	<u:authority menuId="126">
							      	<a href="javascript:;" onclick="go2UserGw('${sid}')">配置</a>
							       |</u:authority> <a href="javascript:;" onclick="updataStatus(this, '${sid}')">关闭</a>
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
		
		function go2UserGw(sid) {
			location.href = "${ctx}/user/edit?userid=" + sid;
		}
		
		function updataStatus(a, sid) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/channelNoroute/updateStatus",
					data : {
						sid : sid
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