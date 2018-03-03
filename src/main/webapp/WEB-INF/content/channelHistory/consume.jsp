<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>短信通道消耗记录查询(废弃)</title>
</head>
<body menuId="44">
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
						 <form method="post" id="mainForm" action="${ctx}/channelConsume/query" onsubmit="return query()">
						 	<ul>
								<li>
									<u:select id="stat_type" value="${stat_type}" placeholder="统计类型" dictionaryType="consume_stat_type" onChange="changeStatType" />
								</li>
								<li>
									<u:select id="channel_id" value="${param.channel_id}" placeholder="通道号" sqlId="channel" />
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
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>通道名称</th>
					                 <th>通道号</th>
					                 <th>所属通道</th>
					                 <th>通道备注</th>
					                 <th>消耗条数（条）</th>
					                 <th>计费条数（条）</th>
					                 <th>消耗总成本（元）</th>
					                 <th>单位成本（元/条）</th>
					                
								</tr>
							</thead>
							<tbody>
								<s:iterator value="data.page.list" var="v">
								<tr>
								  <td>${v.rownum}</td>
							      <td>${v.channelname}</td>
							      <td>${v.channelid}</td>
							      <td><u:ucparams key="${v.operatorstype}" type="channel_operatorstype"/></td>
							      <td>${v.remark }</td>
							      <td>${v.consumetotalcnt}</td>
							      <td>${v.chargetotal}</td>
							      <td>${v.costpricetotal}</td>
							      <td>${v.costprice}</td>
							      
							     </tr>
							    </s:iterator>
							    <s:if test="data.total!=null">
									<tr style="font-weight: bold;">
									  <td colspan="5" style="text-align: center;">总计</td>
								      <td>${data.total.consumetotalcnt}</td>
								      <td>${data.total.chargetotal}</td>
								      <td>${data.total.costpricetotal}</td>
								      <td>${data.total.costprice}</td>
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
</script>
</body>
</html>