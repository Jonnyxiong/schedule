<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<u:authority menuId="221">
	<s:set var="authority_query_221" value="true"/>
</u:authority>

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

<body menuId="215">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
			<ul class="nav nav-tabs nav-justified" role="tablist">
				<li><a href="${ctx}/channelRealtime/clientSpeed/page">用户发送速率统计</a></li>
				<li class="active"><a href="${ctx}/channelRealtime/clientQuality/page">回执率和应答率统计</a></li>
				<li><a href="${ctx}/channelRealtime/clientReportRespStack/page">用户质量趋势分布图</a></li>
				<li><a href="${ctx}/channelRealtime/clientSuccessRateStack/page">用户成功率趋势分布图</a></li>
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
							<form method="post" id="mainForm" action="${ctx}/channelRealtime/clientQuality/page">
								<ul>
									<li>
										<input id="tempClientId" type="hidden" value="${clientId}">
										<u:select id="clientId" value="${clientId}" placeholder="短信账号" sqlId="findRunningClientId"/>
									</li>
									<li>
										<span>时间范围：</span>
										<u:select id="timeType" value="${timeType}" 
										   defaultIndex="0"	data="[{value:'0',text:'5分钟'}, {value:'1',text:'1小时'}, {value:'2',text:'12小时'}, {value:'3',text:'24小时'}]" onChange="timeTypeOnChange"/>
									</li>
									<li><li><input id="searchBtn" type="submit" value="搜索" /></li>
									<li><li><input id="refreshBtn" type="button" value="停止刷新" /></li>
								</ul>
							</form>
						</div>
					</div>
					<div id="timer" data-time="" style="font-size:20px;font-weight:800;background: #efefef;line-height:30px;border-bottom:1px solid #DDD;">系统时间&nbsp;
							<span class="h"></span>:
							<span class="m"></span>:
							<span class="s"></span>
				    </div>
					<div class="col-sm-6" style="">
						<div class="widget-content nopadding">
							
							<table class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 4%">序号</th>
										<th style="width: 8%">开始时间</th>
										<th style="width: 8%">结束时间</th>
										<th style="width: 8%">客户ID</th>
										<th style="width: 13%">客户名称</th>
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
											<td>${client_id}</td>
											<td>${client_name}</td>
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
						
						<s:if test="authority_query_221">
							<div class="widget-content nopadding">
							
							<table class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th style="width: 4%">序号</th>
										<th style="width: 8%">开始时间</th>
										<th style="width: 8%">结束时间</th>
										<th style="width: 8%">客户ID</th>
										<th style="width: 13%">客户名称</th>
										<th style="width: 8%">发送总数</th>
										<th style="width: 8%">订单缓存延时[0-1]</th>
										<th style="width: 8%">订单缓存延时[2-3]</th>
										<th style="width: 8%">订单缓存延时[3-5]</th>
										<th style="width: 8%">订单缓存延时[5以上]</th>
										<th>未发送到Send</th>
									</tr>
								</thead>
								<tbody id="userbody">
									<s:iterator value="page.list">
										<tr>
											<td>${rownum}</td>
											<td>${start_time}</td>
											<td>${end_time}</td>
											<td>${client_id}</td>
											<td>${client_name}</td>
											<td>${send_total_num}</td>
											<td>${orderDelay1}</td>
											<td>${orderDelay2}</td>
											<td>${orderDelay3}</td>
											<td>${orderDelay4}</td>
											<td>${orderDelay5}</td>
										</tr>
									</s:iterator>
								</tbody>
							</table>
							
						</div>
						</s:if>
						
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
	
	var select2 = $("#clientId").select2({
		multiple : true
	});
	
	// select2 赋值
	var clientIdList = $("#tempClientId").val().split(",");
	if(clientIdList.length > 0){
		$("#clientId").val(clientIdList).trigger("change");
	}
	
	// select2选中赋值逻辑
	$('#clientId').on('select2:select', function (evt) {
		var idList = $("#clientId").val();
		var clientIdList = [];
		$.each(idList, function(index,val) {
		      if(val != ""){
		    	  clientIdList.push(val);
		      }
		});
		if(clientIdList.length == 0){
			clientIdList.push("");
		}
		$("#clientId").val(clientIdList).trigger("change");
	});
	
	// select2取消选中赋值逻辑
	$('#clientId').on('select2:unselect', function (evt) {
		var clientIdList = $("#clientId").val();
		
		if(clientIdList == null || clientIdList.length == 0){
			clientIdList = [];
			clientIdList.push("");
		}
		$("#clientId").val(clientIdList).trigger("change");
	});
	
	
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
	$("#searchBtn").click();
}

//刷新按钮切换事件
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


</script>
</body>
</html>