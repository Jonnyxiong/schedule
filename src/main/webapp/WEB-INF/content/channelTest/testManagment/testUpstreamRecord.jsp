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
				<li><a href="${ctx}/channelTest/testManageMent-tab3">测试短信发送记录</a></li>
				<li class="active"><a href="${ctx}/channelTest/testManageMent-tab4">测试上行短信记录</a></li>
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
						 	 <form method="post" id="mainForm" action="${ctx}/channelTest/testManageMent-tab4">
								<ul>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="通道号/手机号码" class="txt_250" /></li>
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
									 <th>通道号</th>
									 <th>手机号码</th>
									 <th>目的号码</th>
									 <th>短信内容</th>
									 <th>上行时间</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${channel_id}</td>
										<td>${phone}</td>
										<td>${tophone}</td>
										<td>${content}</td>
										<td>${receivedate}</td>
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