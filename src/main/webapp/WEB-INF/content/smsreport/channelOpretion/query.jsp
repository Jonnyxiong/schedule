<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>通道运维报表查询</title>
</head>
<body menuId="73">
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
						<div class="search">
						 <form method="post" id="mainForm" action="${ctx}/channelOpretion/query" onsubmit="return query()">
						 	<ul>
								<li>
									<u:select id="stat_type" value="${stat_type}" placeholder="统计类型" dictionaryType="consume_stat_type" onChange="changeStatType" />
								</li>
								<li>
									<u:select id="channel_id" value="${param.channel_id}" placeholder="通道号" sqlId="channel" />
								</li>
								<li>
									<u:select id="operatorstype" value="${param.operatorstype}" placeholder="运营商类型" dictionaryType="channel_operatorstype"  />
								</li>
								<li id="li_day" ${stat_type==1 ? "" : "style='display:none'"}>
									<u:date id="start_time_day" value="${start_time_day}" placeholder="开始时间" dateFmt="yyyyMMdd" params="" />
									<span>至</span>
            						<u:date id="end_time_day" value="${end_time_day}" placeholder="结束时间" dateFmt="yyyyMMdd" params="" />
								</li>
								<li id="li_month" ${stat_type==2 ? "" : "style='display:none'"}>
									<u:date id="start_time_month" value="${start_time_month}" placeholder="开始时间" dateFmt="yyyyMM" params="minDate:'{%y-1}-%M-%d', maxDate:'#F{$dp.$D(\\'end_time_month\\')||\\'%y-%M-%d\\'}'" />
									<span>至</span>
            						<u:date id="end_time_month" value="${end_time_month}" placeholder="结束时间" dateFmt="yyyyMM" params="minDate:'#F{$dp.$D(\\'start_time_month\\')||\\'{%y-1}-%M-%d\\'}', maxDate:'%y-%M-%d'" />
								</li>
								<li>
									<input type="submit" value="搜索" />
								</li>
								<li>
									<a href="javascript:;" class=" label label-info" onclick="exportExcel()">导出Excel文件</a>
								</li>
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>时间</th>
					                 <th>运营商</th>
					                 <th>通道号</th>
					                 <th>通道备注</th>
					                 <th title="1+2+3+5+6状态，按短信拆分后条数计算">总发送量</th>
					                 <th>成功率(3/总发送量)</th>
					                 <th>成功量(2+3)</th>
					                 <th>提交成功(1)</th>
					                 <th>发送成功(2)</th>
					                 <th>明确成功(3)</th>
					                 <th>提交失败(4)</th>
					                 <th>发送失败(5)</th>
					                 <th>明确失败(6)</th>
					                 <th>未发送(0)</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="data.page.list" var="v">
								<tr>
								  <td>${v.rownum}</td>
							      <td>${v.date}</td>
						  		  <td>${v.operatorstype_name}</td>
								  <s:if test="operatorstype!=-1">
								  		<td>${v.channelid}</td>
								        <td>${v.remark}</td>
								        <td>${v.sendtotal}</td>
								        <td>${v.successrate}</td>
								        <td>${v.successtotal}</td>
								        <td>${v.submitsuccess}</td>
								        <td>${v.subretsuccess}</td>
								        <td>${v.reportsuccess}</td>
								        <td>${v.submitfail}</td>
								        <td>${v.subretfail}</td>
								        <td>${v.reportfail}</td>
								        <td>${v.notsend}</td>
								  </s:if>
								  <s:else>
								  		<td>${v.channelid}</td>
								        <td>-</td>
								        <td>${v.sendtotal}</td>
								        <td>-</td>
								        <td>-</td>
								        <td>-</td>
								        <td>-</td>
								        <td>-</td>
								        <td>-</td>
								        <td>-</td>
								        <td>-</td>
								        <td>-</td>
								  </s:else>
							      
							     </tr>
							    </s:iterator>
							    <s:if test="data.total != null && data.page.list.size != 0">
									<tr style="font-weight: bold;">
									  <td colspan="5" style="text-align: center;">总计(不计拦截)</td>
								      <td>${data.total.sendtotal}</td>
								      <td>${data.total.successrate}</td>
								      <td>${data.total.successtotal}</td>
								      <td>${data.total.submitsuccess}</td>
								      <td>${data.total.subretsuccess}</td>
								      <td>${data.total.reportsuccess}</td>
								      <td>${data.total.submitfail}</td>
								      <td>${data.total.subretfail}</td>
								      <td>${data.total.reportfail}</td>
								      <td>${data.total.notsend}</td>
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
$(function(){
	$("#stat_type option:first").attr("disabled", true);//禁用“统计类型：所有”
});

//选择统计类型
function changeStatType(value, text){
	if(value==1){
		$("#li_day").show();
		$("#li_month").hide();
	}else{
		$("#li_day").hide();
		$("#li_month").show();
	}
}

function query() {
	var start_time_day = new Date(getDay($("#start_time_day").val())).getTime();
	var end_time_day = new Date(getDay($("#end_time_day").val())).getTime();

	var time = (end_time_day - start_time_day) / (1000 * 3600 * 24);
	if(time > 45) {
		alert("最多可以查询45天的数据");
		return false;
	}
}

function getDay(source) {
	var year = source.substring(0,4);
	var month = source.substring(4,6);
	var day = source.substring(6,8);
	return year + "/" + month + "/" + day;
}

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

	mainForm.attr("action", "${ctx}/channelOpretion/exportExcel").submit();
	mainForm.attr("action", action);
}
</script>
</body>
</html>