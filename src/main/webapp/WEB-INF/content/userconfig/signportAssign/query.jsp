<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>签名端口分配范围管理</title>
</head>
<body menuId="85">
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
							<form method="post" id="mainForm" action="${ctx}/signportAssign/query">
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th style='width:5%'>序号</th>
									 <th>子账号</th>
									 <th>起始编号</th>
									 <th>结束编号</th>
									 <th>当前编号</th>
									 <th>更新时间</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td style='width:5%'>${rownum}</td>
							      <td>${clientid}</td>
							      <td>${startnumber}</td>
							      <td>${endnumber}</td>
							      <s:if test="currentnumber==100 or currentnumber==1000">
							          <td>该子帐号签名端口分配完毕</td>
							      </s:if>
							      <s:else>
								      <td>${currentnumber}</td>
							      </s:else>
							      <td>${updatetime}</td>
							     </tr>
							    </s:iterator>
							</tbody>
						</table>
					</div>
						<u:page page="${page}" formId="mainForm" />
				</div>
			</div>
		</div>

	<script type="text/javascript">
		
	</script>
</body>
</html>