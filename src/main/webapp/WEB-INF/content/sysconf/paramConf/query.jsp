<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>系统参数</title>
</head>
<body menuId="10">
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
						<!--<span class="label label-info">
						<u:authority menuId="1">
							<a href="javascript:;" onclick="add()">添加系统参数</a>
						</u:authority>
						</span>-->
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/paramConf/query">
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>参数键</th>
					                 <th>参数值</th>
					                 <th>描述</th>
					                 <th>创建时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td><u:ucparams key="${param_key}" type="param_key"/></td>
							      <td>${param_value}</td>
							      <td>${description}</td>
							      <td>${create_date}</td>
							      <td>
							     	  <u:authority menuId="90"><a href="javascript:;" onclick="edit('${param_id}')">编辑</a></u:authority>
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
	</div>

	<script type="text/javascript">
		//添加
		function add() {
			location.href = "${ctx}/paramConf/edit";
		}

		//编辑
		function edit(id) {
			location.href = "${ctx}/paramConf/edit?param_id=" + id;
		}
	</script>
</body>
</html>