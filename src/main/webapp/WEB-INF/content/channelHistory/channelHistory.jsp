<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.chart.title}</title>
</head>

<body menuId="${data.menuId}">
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
						 	<form method="post" id="mainForm">
							<ul>
<%-- 								<li>
									<u:select id="stat_type" value="${stat_type}" placeholder="统计类型" dictionaryType="stat_type" onChange="changeStatType" />
								</li>--%>
 								<li>
									<u:select id="channel_id" value="${param.channel_id}" placeholder="通道号" sqlId="findAllChannel" />
								</li> 
								<li>
									<u:treeSelect id="area_id" value="${area_id}" placeholder="地区" sqlId="area" showPname="true" />
								</li> 
<%-- 								<li id="li_minutes" ${stat_type==1 ? "" : "style='display:none'"}>
									<u:date id="start_time_minutes" value="${start_time_minutes}" placeholder="开始时间" params="minDate:'%y-%M-{%d-45} 00:00:00', maxDate:'#F{$dp.$D(\\'end_time_minutes\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
									<span>至</span>
            						<u:date id="end_time_minutes" value="${end_time_minutes}" placeholder="结束时间" params="minDate:'#F{$dp.$D(\\'start_time_minutes\\')||\\'%y-%M-{%d-45} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
								</li> --%>
								<li id="li_day">
									<u:date id="start_time_day" value="${start_time_day}" placeholder="开始时间" dateFmt="yyyy-MM-dd" minToday="-45" maxId="end_time_day" maxToday="" />
									<span>至</span>
            						<u:date id="end_time_day" value="${end_time_day}" placeholder="结束时间" dateFmt="yyyy-MM-dd" minToday="-45" minId="start_time_day" maxToday="" />
								</li>
								<li>
									<input type="submit" value="搜索" />
								</li>
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<u:highcharts highcharts="${data.chart}" id="chart" />
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">
$(function(){
	$("#stat_type option:first").attr("disabled", true);//禁用“统计类型：所有”
	$("#stat_type option:eq(1)").attr("disabled", true);//禁用“统计类型：每三分钟统计”
/* 	var channelidSelect = $("#channel_id").select2({
		multiple : true,
		maximumSelectionLength : 10,
	}); */
});

$("#channel_id").change(function(){
	$("#mainForm").submit();
})

//选择统计类型
/* function changeStatType(value, text){
		$("#li_day").show();
} */
</script>
</body>
</html>