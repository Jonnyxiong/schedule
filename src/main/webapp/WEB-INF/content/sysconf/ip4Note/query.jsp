<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>节点IP对应关系</title>
</head>
<body menuId="67">
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
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>SMSP_SEND 编号</th>
					                 <th>SMSP_SEND IP</th>
					                 <th>端口</th>
					                 <th>挂载通道数量</th>
					                 <th>备注</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td>${sendid}</td>
							      <td>${sendip}</td>
							      <td>${sendport}</td>
							      <td>${sendMountNum}</td>
							      <td>${remark}</td>
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
</body>
</html>