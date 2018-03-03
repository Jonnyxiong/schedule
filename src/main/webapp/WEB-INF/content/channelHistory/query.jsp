<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>短信通道历史记录查询</title>
</head>
<body menuId="21">
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
								<li>
									<u:select id="stat_type" value="${stat_type}" placeholder="统计类型" dictionaryType="stat_type" onChange="changeStatType" />
								</li>
								<li>
									<u:select id="channel_id" value="${param.channel_id}" placeholder="通道号" sqlId="findAllChannel" />
								</li>
								<li>
									<u:treeSelect id="area_id" value="${area_id}" placeholder="地区" sqlId="area" showPname="true" />
								</li>
								<li id="li_minutes" ${stat_type==1 ? "" : "style='display:none'"}>
									<u:date id="start_time_minutes" value="${start_time_minutes}" placeholder="开始时间" params="minDate:'%y-%M-{%d-45} 00:00:00', maxDate:'#F{$dp.$D(\\'end_time_minutes\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
									<span>至</span>
            						<u:date id="end_time_minutes" value="${end_time_minutes}" placeholder="结束时间" params="minDate:'#F{$dp.$D(\\'start_time_minutes\\')||\\'%y-%M-{%d-45} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
								</li>
								<li id="li_day" ${stat_type==2 ? "" : "style='display:none'"}>
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
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>通道号</th>
					                 <th>区域</th>
					                 <th>请求数量</th>
					                 <th>发送数量</th>
					                 <th>延时数量</th>
					                 <th>验证成功数量</th>
					                 <th>到达数量</th>
					                 <th>并发量（条/秒）</th>
					                 <th>延时时长（秒）</th>
					                 <th>发送成功率（%）</th>
					                 <th>验证成功率（%）</th>
					                 <th>到达成功率（%）</th>
					                 <th>统计时间</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list" var="v">
								<tr>
								  <td>${v.rownum}</td>
							      <td>${v.channelid}</td>
							      <td><u:ucparams key="${v.area_id}" type="area_view"/></td>
							      <td>${v.recvtotalcnt}</td>
							      <td>${v.sendtotalcnt}</td>
							      <td>${v.delaytotalcnt}</td>
							      <td>${v.verifytotalcnt}</td>
							      <td>${v.reachtotalcnt}</td>
							      <td>${v.concurrency}</td>
							      <td>${v.delayavg}</td>
							      <td>${v.sendrate}</td>
							      <td>${v.verifyrate}</td>
							      <td>${v.reachrate}</td>
							      <td>${v.datatime}</td>
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
$(function(){
	$("#stat_type option:first").attr("disabled", true);//禁用“统计类型：所有”
});

//选择统计类型
function changeStatType(value, text){
	if(value==1){
		$("#li_minutes").show();
		$("#li_day").hide();
	}else{
		$("#li_minutes").hide();
		$("#li_day").show();
	}
}
</script>
</body>
</html>