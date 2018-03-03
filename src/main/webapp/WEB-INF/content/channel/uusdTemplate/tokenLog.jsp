<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


<html>
<head>
	<title>${data.chart.title}</title>
</head>

<body menuId="228">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
				<ul class="nav nav-tabs nav-justified" role="tablist">
					<li><a href="${ctx}/uusd/template/auditResult">彩印模板审核状态</a></li>
					<li class="active"><a href="${ctx}/uusd/template/tokenLog">彩印通道授权日志</a></li>
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
						 	<form method="post" id="mainForm" action="${ctx}/uusd/template/tokenLog">
							<ul>
								<li>
                  					<input type="text" name="channelid" autocomplete="off" value="<s:property value="#parameters.channelid"/>" maxlength="11"
										placeholder="通道ID" class="txt_250" />
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
					                 <th>通道ID</th>
					                 <th>TOKEN</th>
					                 <th>TOKEN有效时间</th>
					                 <th>刷新TOKEN</th>
					                 <th>创建时间</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${channelid}</td>
										<td>${access_token}</td>
										<td>${expires_in}</td>
										<td>${refresh_token}</td>
										<td>${create_time}</td>
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

//@ sourceURL= tokenLog.js
</script>
</body>
</html>