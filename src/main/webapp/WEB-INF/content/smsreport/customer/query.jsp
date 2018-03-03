<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>客户消耗统计报表(access)</title>
</head>
<body menuId="52">
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
							<form method="post" id="mainForm" action="${ctx}/customerconsume/query" onsubmit="return query()">
								<ul>
									<li>
										<u:date id="start_time" value="${start_time}" placeholder="开始时间" maxId="end_time,-1" dateFmt="yyyy-MM-dd HH:mm:ss" />
										<span>至</span>
	            						<u:date id="end_time" value="${end_time}" placeholder="结束时间" minId="start_time,1" dateFmt="yyyy-MM-dd HH:mm:ss"/>
									</li>
								<li>
									<input type="text" name="clientid" value="<s:property value="#parameters.clientid"/>" maxlength="40" placeholder="用户id" class="txt_250" />
								</li>	
									<li><input id="submitBtn" type="button" value="搜索" onclick="submitForm()"/></li>
								</ul>
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>用户ID</th>
									<th>用户名称</th>
									<th title="发送总数  = 未发送(0) + 提交成功(1) + 明确成功(3) + 提交失败(4) + 明确失败(6) + 审核不通过(7) + 网关接收拦截(8) + 网关发送拦截(9) + 超频(10)">发送总数</th>
									<th title="明确成功条数  = 明确成功(3)">明确成功条数</th>
									<th title="成功待定条数  = 提交成功(1)">成功待定条数</th>
									<th title="计费条数  = 提交成功(1) + 明确成功(3) + 明确失败(6) + 超频(10)">计费条数</th>
									<th title="明确失败条数  = 明确失败(6)">明确失败条数</th>
									<th title="审核不通过条数= 审核不通过(7)">审核不通过条数</th>
									<th title="提交失败条数 = 提交失败(4) + 审核、计费失败(5)">提交失败条数</th>
									<th title="拦截条数 = 网关接收拦截(8) + 网关发送拦截(9) + 超频(10)">拦截条数</th>
									<th title="成功率 = 明确成功(3)/发送总数  ">成功率</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="data.page.list">
									<tr>
										<td>${rownum}</td>
										<td>${clientid}</td>
										<td>${userName}</td>
										<td>${sendTotal}</td>
										<td>${reallySuccessTotal}</td>
										<td>${fakeSuccessFail}</td>
										<td>${chargeTotal}</td>
										<td>${reallyFailTotal}</td>
										<td>${auditFailTotal}</td>
										<td>${submitFailTotal}</td>
										<td>${interceptTotal}</td>
										<td>${successRate}</td>
									</tr>
								</s:iterator>
								
								<s:if test="data.total != null && data.page.list.size != 0">
									<tr style="font-weight: bold;">
										<td colspan="3" style="text-align: center;">总计</td>
										<td>${data.total.sendTotal}</td>
										<td>${data.total.reallySuccessTotal}</td>
										<td>${data.total.fakeSuccessFail}</td>
										<td>${data.total.chargeTotal}</td>
										<td>${data.total.reallyFailTotal}</td>
										<td>${data.total.auditFailTotal}</td>
										<td>${data.total.submitFailTotal}</td>
										<td>${data.total.interceptTotal}</td>
										<td>${data.total.successRate}</td>
								     </tr>
							    </s:if>
							</tbody>
						</table>
					</div>
					<u:page page="${data.page}" formId="mainForm" />
					<input type="hidden" id="totalCount" value="${data.page.totalCount}">
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
				layer.msg("共0条记录，导出Excel文件失败", {icon: 1,time: 1500}); 
				return;
			}
			if(totalCount>20000){
				layer.msg("导出Excel文件条数大于20000条", {icon: 1,time: 1500}); 
				return;
				
			}
			
// 			var clientid = $("input[name='clientid']").val();
// 			if(clientid == null || $.trim(clientid) == ''){
// 				layer.msg("请输入：用户id", {icon: 1,time: 1500}); 
// 				return;
// 			}
			
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
	
			mainForm.attr("action", "${ctx}/customerconsume/exportExcel").submit();
			mainForm.attr("action", action);
		}
		
		
		function submitForm(){
			
// 			var clientid = $("input[name='clientid']").val();
// 			if(clientid == null || $.trim(clientid) == ''){
// 				layer.msg("请输入：用户id", {icon: 1,time: 1500}); 
// 				return;
// 			}
			
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