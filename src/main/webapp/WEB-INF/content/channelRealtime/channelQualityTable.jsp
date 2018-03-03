<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


<html>
<head>
	<title>${data.chart.title}</title>
	<style type="text/css">
		.select2-dropdown {
		  margin-top: -10px;
		}
		.widget-title .search .select2-container{width:500px !important; margin-top: 5px;}
		.select2-selection__rendered li {line-height:20px !important;}
	</style>
</head>

<body menuId="217">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
				<ul class="nav nav-tabs nav-justified" role="tablist">
					<li><a href="${ctx}/channelRealtime/channelQuality/graphPage">通道发送质量图形</a></li>
					<li class="active"><a href="${ctx}/channelRealtime/channelQuality/tablePage">通道发送质量详单</a></li>
					<li><a href="${ctx}/channelRealtime/reportRespStack/page">通道质量趋势分布图</a></li>
					<li><a href="${ctx}/channelRealtime/channelSuccessRateStack/page">通道成功率趋势分布图</a></li>
				</ul>
		</nav>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/channelRealtime/channelQuality/tablePage">
								<ul>
									<li>
										<input id="tempChannelId" type="hidden" value="${channelId}">
										<u:select id="channelId" value="${channelId}" placeholder="通道号" sqlId="findRunningChannel"/>
<%-- 										<u:select id="channelId" value="${channelId}" placeholder="通道号" data="[{value:'5002',text:'5002'}, {value:'5009',text:'5009'}, {value:'6004',text:'6004'}]"/> --%>
									</li>
									<li>
										<span>时间范围：</span>
										<u:select id="timeType" value="${timeType}" 
											defaultIndex="0" data="[{value:'0',text:'5分钟'}, {value:'1',text:'1小时'}, {value:'2',text:'12小时'}, {value:'3',text:'24小时'}]" onChange="timeTypeOnChange"/>
									</li>
									<li><li><input id="searchBtn" type="submit" value="搜索" /></li></li>
									<li><li><input id="refreshBtn" type="button" value="停止刷新" /></li></li>
								</ul>
							</form>
						</div>
					</div>
					<div id="timer" data-time="" style="font-size:20px;font-weight:800;line-height:30px;border-bottom:1px solid #DDD;background-color:#EEEEEE;">系统时间&nbsp;
						<span class="h"></span>:
						<span class="m"></span>:
						<span class="s"></span>
					</div>
					<div class="col-sm-6" style="">
						<div class="widget-content nopadding">
							
							<table class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 5%">序号</th>
										<th style="width: 8%">开始时间</th>
										<th style="width: 8%">结束时间</th>
										<th style="width: 8%">通道号</th>
										<th style="width: 8%">发送总数</th>
										<th style="width: 8%">回执时长[0-10]</th>
										<th style="width: 8%">回执时长[11-30]</th>
										<th style="width: 8%">回执时长[31-60]</th>
										<th style="width: 8%">回执时长[61-120]</th>
										<th style="width: 8%">回执时长[121-300]</th>
										<th style="width: 8%">回执时长[300以上]</th>
										<th>回执未返回</th>
									</tr>
								</thead>
								<tbody id="userbody">
									<s:iterator value="page.list">
										<tr>
											<td>${rownum}</td>
											<td>${start_time}</td>
											<td>${end_time}</td>
											<td><a href="javascript:void(0)" style="color:#28b779;" onclick="go2ChannelDetails(this)">${channel_id}</a></td>
											<td>${send_total_num}</td>
											<td>${report1}</td>
											<td>${report2}</td>
											<td>${report3}</td>
											<td>${report4}</td>
											<td>${report5}</td>
											<td>${report6}</td>
											<td>${report7}</td>
										</tr>
									</s:iterator>
								</tbody>
							</table>
							
						</div>
						
						<u:page page="${page}" formId="mainForm" />
						
						<div class="widget-content nopadding">
							
							<table class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 5%">序号</th>
										<th style="width: 8%">开始时间</th>
										<th style="width: 8%">结束时间</th>
										<th style="width: 8%">通道号</th>
										<th style="width: 8%">发送总数</th>
										<th style="width: 8%">应答时长[0-2]</th>
										<th style="width: 8%">应答时长[3-5]</th>
										<th style="width: 8%">应答时长[6-10]</th>
										<th style="width: 8%">应答时长[11-60]</th>
										<th style="width: 8%">应答时长[61-300]</th>
										<th style="width: 8%">应答时长[300以上]</th>
										<th>应答超时未返回</th>
									</tr>
								</thead>
								<tbody id="userbody">
									<s:iterator value="page.list">
										<tr>
											<td>${rownum}</td>
											<td>${start_time}</td>
											<td>${end_time}</td>
											<td><a href="javascript:void(0)" style="color:#28b779;" onclick="go2ChannelDetails(this)">${channel_id}</a></td>
											<td>${send_total_num}</td>
											<td>${resp1}</td>
											<td>${resp2}</td>
											<td>${resp3}</td>
											<td>${resp4}</td>
											<td>${resp5}</td>
											<td>${resp6}</td>
											<td>${resp7}</td>
										</tr>
									</s:iterator>
								</tbody>
							</table>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">
var intervalObj;
var INTERVAL_TIME = 60 * 1000; // 60秒

$(function(){
	$("#timer").stopWatch();
	
	var select2 = $("#channelId").select2({
		multiple : true
	});
	
	// select2 赋值
	var channelIdList = $("#tempChannelId").val().split(",");
	if(channelIdList.length > 0){
		$("#channelId").val(channelIdList).trigger("change");
	}
	
	$('#channelId').on('select2:select', function (evt) {
		var idList = $("#channelId").val();
		var channelIdList = [];
		$.each(idList, function(index,val) {
		      if(val != ""){
			      channelIdList.push(val);
		      }
		});
		if(channelIdList.length == 0){
			channelIdList.push("");
		}
		$("#channelId").val(channelIdList).trigger("change");
	});
	
	$('#channelId').on('select2:unselect', function (evt) {
		var channelIdList = $("#channelId").val();
		
		if(channelIdList == null || channelIdList.length == 0){
			channelIdList = [];
			channelIdList.push("");
		}
		$("#channelId").val(channelIdList).trigger("change");
	});
	
	$('#channelId').on('select2:change', function (evt) {
	});
	
	// 定时器
	window.clearInterval(intervalObj);
	intervalObj = setInterval(function () {
		clickSearchBtn();
	}, INTERVAL_TIME);
	
	bindRefreshBtnToggle();
	
});

function timeTypeOnChange(value, text, isInit){
	if(isInit){
		return;
	}
	
	var channelId = $("#channelId").val();
	var timeType = value;
	console.log("查询通道： " + channelId);
	console.log("查询时间类型：" + timeType);
	
	$("#mainForm").submit();
}

function clickSearchBtn(){
	console.log("定时刷新...");
	$("#searchBtn").click();
}

// 刷新按钮切换事件
function bindRefreshBtnToggle(){
	$("#refreshBtn").toggle(
	  function () {
	      $(this).val("开始刷新");
	      $(this).css("background-color","#3a87ad");
	      console.log("停止刷新...");
	      clearRefreshInterval();
	  },
	  function () {
	      $(this).val("停止刷新");
	      $(this).css("background-color","#DA542E");
	      console.log("开始刷新...");
	      clickSearchBtn();
	      setRefreshInterval();
	  }
	);
}

function setRefreshInterval(){
	intervalObj = setInterval(function () {
		clickSearchBtn();
	}, INTERVAL_TIME);
}

function clearRefreshInterval(){
	window.clearInterval(intervalObj);
}

function go2ChannelDetails(obj){
	var channelId = obj.text;
	var timeType = $("#timeType").val();
	go2URL("${ctx}/channelRealtime/channelQuality/details/page?channelId=" + channelId + "&timeType=" + timeType);
}


</script>
</body>
</html>