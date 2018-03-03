<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>客户充值管理</title>
</head>
<body menuId="56">
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
						<span class="label label-info">
						<u:authority menuId="1">
							<a href="javascript:;" onclick="add(this)">添加充值记录</a>
						</u:authority>
						</span>
						<div class="search">
						  <form method="post" id="mainForm" action="${ctx}/recharge/query">
							<ul>
								<li>
									<u:date id="start_time" value="${start_time}" placeholder="开始时间" dateFmt="yyyy-MM" params="" />
									<span>至</span>
            						<u:date id="end_time" value="${end_time}" placeholder="结束时间" dateFmt="yyyy-MM" params="" />
								</li>
								<li><input type="text" name="name" value="<s:property value="#parameters.name"/>"  maxlength="50" placeholder="客户名称/销售人员姓名" class="txt_250" /></li>
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
									<th>销售人员姓名</th>
									<th>客户名称</th>
									<th>当次充值金额</th>
									<th>当月充值合作次数</th>
									<th>当次充值合同单价</th>
									<th>当次充值成本价</th>
									<th>充值时间</th>
									<th>当次充值利润</th>
									<th>当次充值的销售提成</th>
									<th>是否上月余额</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${salesman_name}</td>
										<td>${customer_name}</td>
										<td>${recharge_money}</td>
										<td>${recharge_mark}</td>
										<td>${recharge_unit_price}</td>
										<td>${recharge_cost_price}</td>
										<td>${recharge_time}</td>
										<td>${recharge_profit}</td>
										<td>${recharge_royalty}</td>
										<td>${is_remain==1?"是":"否"}</td>
						     	  		<td>
						     	  			<a href="javascript:;" onclick="delSalesman(this, '${id}')">删除</a> 
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
		//添加
		function add(btn) {
			location.href = "${ctx}/recharge/add";
		}
		
		//删除
		function delSalesman(a, id) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/recharge/delete",
					data : {
						id : id
					},
					success : function(data) {
						if (data == null) {
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