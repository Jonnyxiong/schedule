<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
</head>
<body menuId="268">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
			<ul class="nav nav-tabs nav-justified" role="tablist">
				<li><a href="${ctx}/channelTest/testManageMent">测试模板管理</a></li>
				<li><a href="${ctx}/channelTest/testManageMent-tab2">测试号码管理</a></li>
				<li class="active"><a href="${ctx}/channelTest/testManageMent-tab3">测试短信发送记录</a></li>
				<li><a href="${ctx}/channelTest/testManageMent-tab4">测试上行短信记录</a></li>
			</ul>
		</nav>
	
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelTest/testManageMent-tab3">
								<ul>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="手机号码/通道名称" class="txt_250" /></li>
									<li><input type="submit" value="搜索"/></li>
								</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									 <th>序号</th>
									 <th>短信ID</th>
									 <th>通道ID</th>
									 <th>通道名称</th>
									 <th>手机号码</th>
									 <th>号码运营商</th>
									 <th>短信内容</th>
									 <th>长短信</th>
									 <th>拆分条数</th>
									 <th>拆分序号</th>
									 <th>发送状态</th>
									 <th>状态报告ID</th>
									 <th>状态报告描述</th>
									 <th>显示号码</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${smsid}</td>
										<td>${channel_id}</td>
										<td>${channel_name}</td>
										<td>${phone}</td>
										<td>
											<s:if test="operators_type eq 1">移动</s:if>
											<s:elseif test="operators_type eq 2">联通</s:elseif>
											<s:elseif test="operators_type eq 3">电信</s:elseif>
											<s:elseif test="operators_type eq 4">国际</s:elseif>
											<s:else>其他</s:else>
										</td>
										<td><u:truncate length="20" value="${content}"/></td>
										<td>
											<s:if test="longsms eq 0">否</s:if>
											<s:elseif test="longsms eq 1">是</s:elseif>
										</td>
										<td>${smscnt}</td>
										<td>${smsindex}</td>
										<td>
											<s:if test="state eq 0">未发送</s:if>
											<s:elseif test="state eq 1">提交成功</s:elseif>
											<s:elseif test="state eq 2">发送成功</s:elseif>
											<s:elseif test="state eq 3">明确成功</s:elseif>
											<s:elseif test="state eq 4">提交失败</s:elseif>
											<s:elseif test="state eq 4">发送失败</s:elseif>
											<s:elseif test="state eq 4">明确失败</s:elseif>
											<s:else>其他</s:else>
										</td>
										<td>${channelsmsid}</td>
										<td>${report}</td>
										<td>${show_phone}</td>
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
		
	</script>
</body>
</html>