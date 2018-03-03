<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>短信运营日报</title>
</head>
<body menuId="59">
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
						<a href="javascript:;" onclick="exportExcel()">导出Excel文件</a>
						</span>


						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/smsOperatingDaily/query">
								<ul>
									<li>
										<u:date id="start_time" value="${start_time}" placeholder="开始时间" dateFmt="yyyy-MM-dd" params="" />
										<span>至</span>
	            						<u:date id="end_time" value="${end_time}" placeholder="结束时间" dateFmt="yyyy-MM-dd" params="" />
									</li>
									<li>
										<li><input type="text" name="name" value="<s:property value="#parameters.name"/>" maxlength="50" placeholder="客户Id/客户名称" class="txt_250" /></li>
									</li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>序号</th>
									<th>用户ID</th>
									<th>用户名称</th>
									<th>通道号</th>
									<th>消耗条数</th>
									<th>计费条数</th>
									<th>通道价（元）</th>
									<th>销售价（元）</th>
									<th>毛利</th>
									<th>毛利率</th>
									<th>日期</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="data.page.list">
									<s:if test="record_type == 2">
										<tr style="background-color: antiquewhite;">
											<td>${rownum}</td>
											<td>${user_id}</td>
											<td>${user_name}</td>
											<td>${channel_id}</td>
											<td>${consume_total}</td>
											<td>${charge_total}</td>
											<td>${channel_cost_total}</td>
											<td>${channel_sale_total}</td>
											<td>${gross_profit}</td>
											<td>${gross_margin}</td>
											<td>${consume_time}</td>
										</tr>
									</s:if>
									<s:else>
										<tr>
											<td>${rownum}</td>
											<td>${user_id}</td>
											<td>${user_name}</td>
											<td>${channel_id}</td>
											<td>${consume_total}</td>
											<td>${charge_total}</td>
											<td>${channel_sale_total}</td>
											<td>${channel_cost_total}</td>
											<td>${gross_profit}</td>
											<td>${gross_margin}</td>
											<td>${consume_time}</td>
										</tr>
									</s:else>
								</s:iterator>
								
								<s:if test="data.total != null">
									<tr style="font-weight: bold;">
									  <td colspan="4" style="text-align: center;">总计</td>
								      <td>${data.total.consume_total}</td>
								      <td>${data.total.charge_total}</td>
								      <td>${data.total.channel_cost_total}</td>
								      <td>${data.total.channel_sale_total}</td>
								      <td>${data.total.gross_profit}</td>
								      <td>${data.total.gross_margin}</td>
								      <td>${data.total.consume_time}</td>
								     </tr>
							    </s:if>
							</tbody>
						</table>
					</div>
					<u:page page="${data.page}" formId="mainForm" />
				</div>
			</div>
		</div>
	</div>
	

	<script type="text/javascript">
		//导出Excel文件
		function exportExcel() {
			var totalCount = ${data.page.totalCount};
			if (totalCount == 0) {
				alert("共0条记录，导出Excel文件失败");
				return;
			}
			if(totalCount>20000){
				alert("导出Excel文件条数大于20000条");
				return;
				
			}
			var mainForm = $("#mainForm");
			var action = mainForm.attr("action");
	
			mainForm.attr("action", "${ctx}/smsOperatingDaily/exportExcel").submit();
			mainForm.attr("action", action);
		}
		
// 		function query() {
// 			var start_time = new Date($("#start_time").val());
// 			var end_time = new Date($("#end_time").val());

// 			var time = (end_time - start_time) / (1000 * 3600 * 24);
// 			if(time > 45) {
// 				alert("最多可以查询45天的数据");
// 				return false;
// 			}
// 		}
	</script>
</body>
</html>