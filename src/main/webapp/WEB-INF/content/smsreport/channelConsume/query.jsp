<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>通道消耗统计报表</title>
</head>
<body menuId="50">
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
						<span class="label label-info"> <a href="javascript:;"
							onclick="exportExcel()">导出Excel文件</a>
						</span>


						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/daysmsreport/query" onsubmit="return query();">
								<ul>
									<li>
										<u:date id="start_time" value="${start_time}" placeholder="开始时间" maxId="end_time,-1" dateFmt="yyyy-MM-dd HH:mm:ss"/>
										<span>至</span>
	            						<u:date id="end_time" value="${end_time}" placeholder="结束时间" minId="start_time,1" dateFmt="yyyy-MM-dd HH:mm:ss"/>
									</li>
									<li>
										<input type="text" name="channelid" value="<s:property value="#parameters.channelid"/>" maxlength="50" placeholder="通道号" class="txt_250" />
									</li>
									<%-- <li>
										<input type="text" name="cid" value="<s:property value="#parameters.cid"/>" maxlength="50" placeholder="通道id" class="txt_250" />
									</li> --%>
									<li><input id="submitBtn" type="button" value="搜索" onclick="submitForm()" /></li>
								</ul>
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>通道号</th>
									<th>通道备注</th>
									<th title="发送总数 = 提交成功(1) + 发送成功(2) + 明确成功(3) + 发送失败(5) + 明确失败(6)">发送总数</th>
									<th title="成功待定条数 = 提交成功(1) + 发送成功(2)">成功待定条数</th>
									<th title="明确成功条数 = 明确成功(3)">明确成功条数</th>
									<th title="提交失败条数 = 提交失败(4)">提交失败条数</th>
									<th title="发送失败条数 = 发送失败(5) + 明确失败(6)">发送失败条数</th>
									<th title="成功率 = 明确成功条数 /发送总数">成功率</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="data.page" status="st">
									<tr>
										<td>${st.index + 1}</td>
										<td>${channelid}</td>
										<td>${channelRemark}</td>
										<td>${sendTotal}</td>
										<td>${undetermined}</td>
										<td>${successTotal}</td>
										<td>${sumbmitFail}</td>
										<td>${sendFail}</td>
										<td>${successRate}</td>
									</tr>
								</s:iterator>
								
								<s:if test="data.total != null && data.page.size() != 0">
									<tr style="font-weight: bold;">
									  <td colspan="3" style="text-align: center;">总计</td>
								      <td>${data.total.sendTotal}</td>
								      <td>${data.total.undetermined}</td>
								      <td>${data.total.successTotal}</td>
								      <td>${data.total.sumbmitFail}</td>
								      <td>${data.total.sendFail}</td>
								      <td>${data.total.successRate}</td>
								     </tr>
							    </s:if>
							</tbody>
						</table>
					</div>
					<input type="hidden" id="totalCount" value="${data.page.size()}">
				</div>
			</div>
		</div>
	</div>
	

	<script type="text/javascript">
	
		function query() {
			
// 			var start_time = new Date($("#start_time").val());
// 			var start_time_full = start_time.getFullYear() + "" + start_time.getMonth() + "" + start_time.getDate();
// 			var end_time = new Date($("#end_time").val());
// 			var end_time_full = end_time.getFullYear() + "" + end_time.getMonth() + "" + end_time.getDate();
	
// 			if(start_time_full != end_time_full) {
// 				alert("开始时间和结束时间只能是同一天");
// 				return false;
// 			}
			
			return true;
		}
		
		//Date对象：date1 date2
		function isTheSameDay(date1,date2){
			
			var date1_day = date1.substr(0,10);
			var date2_day = date2.substr(0,10);
			if(date1_day == date2_day){
				return true;
			}else{
				return false;
			}
			
			
		}
		
		//导出Excel文件
		function exportExcel() {

			var totalCount = 0;
			if($("#totalCount").val() != undefined && $("#totalCount")!= null){
				totalCount = $("#totalCount").val();
			}

			if (totalCount == 0) {
				alert("共0条记录，导出Excel文件失败");
				return;
			}
			if(totalCount>10000){
				alert("导出Excel文件条数大于10000条");
				return;
				
			}
			
			var start_time = $("#start_time").val();
			var end_time = $("#end_time").val();
			if(start_time == "" || end_time == ""){
				layer.msg("请输入：开始时间和结束时间", {icon: 1,time: 1500}); 
				return;
			}
			
			var flag = isTheSameDay(start_time,end_time);
			if(flag == false){
				layer.msg("请输入同一天的日期", {icon: 1,time: 1500}); 
				return;
			}
			
			var mainForm = $("#mainForm");
			var action = mainForm.attr("action");
	
			mainForm.attr("action", "${ctx}/daysmsreport/exportExcel").submit();
			mainForm.attr("action", action);
		}
		

		function submitForm(){
			
			var start_time = $("#start_time").val();
			var end_time = $("#end_time").val();
			
			if(start_time == "" || end_time == ""){
				layer.msg("请输入：开始时间和结束时间", {icon: 1,time: 1500}); 
				return;
			}
			var flag = isTheSameDay(start_time,end_time);
			if(flag == false){
				layer.msg("请输入同一天的日期", {icon: 1,time: 1500}); 
				return;
			}
			
			$("#mainForm").submit();
		}
		

	</script>
</body>
</html>