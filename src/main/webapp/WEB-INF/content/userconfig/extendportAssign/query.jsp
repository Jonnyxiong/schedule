<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>用户端口分配范围管理</title>
</head>
<body menuId="87">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
				
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i> </span>
						<h5></h5>
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/extendportAssign/query">
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th style='width:5%'>序号</th>
					                 <th>用户端口扩展类型</th>
					                 <th>起始编号</th>
					                 <th>结束编号</th>
					                 <th>当前编号</th>
					                 <th>重用编号</th>
					                 <th>状态</th>
					                 <th>描述</th>
					                 <th>更新时间</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td style='width:5%'>${rownum}</td>
							      <td>${extendtype}</td>
							      <td>${startnumber}</td>
							      <td>${endnumber}</td>
							      <td>${currentnumber}</td>
							      <td><u:truncate length="20" value="${reusenumber}"/></td>
							      <td>
							      <s:if test="status == 0 ">
							     	 正常
							      </s:if>
							      <s:if test="status == 1 ">
							      	禁用
							      </s:if>
							      <s:if test="status == 2 ">
							      	预留
							      </s:if>
							      </td>
							      <td>${remark}</td>
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
	</div>

</body>
</html>