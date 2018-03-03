<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


<html>
<head>
	<title>${data.chart.title}</title>
	<style type="text/css">
		.select2-container{
			margin-top: 5px;
		}
		.select2-dropdown {
		  margin-top: -10px;
		}
		.tip-color {
 		    list-style: none
 		    margin: 0;
 		    padding: 0;
		}
		.tip-color>li {
		    float: left;
		    font-size: 20px;
		    margin-right: 40px;
		    line-height: 30px;
		}
		.tip-color .tip-red {
			color: rgb(255, 51, 51);
		}
		.tip-color .tip-green {
			color: rgb(0, 153, 0);
		}
		.tip-color .tip-yellow {
			color: rgb(255, 204, 51);
		}
		.tip-color .tip-gray {
			color: rgb(204, 204, 204);
		}
		.fa {
		    display: inline-block;
		    font: normal normal normal 14px/1 FontAwesome;
		    font-size: inherit;
		    text-rendering: auto;
		    -webkit-font-smoothing: antialiased;
		    -moz-osx-font-smoothing: grayscale;
		}
		.visibility{
			visibility:hidden;
			animation:show .3s linear both;
		}
		@keyframes show{
			0%{opacity:0}
			100%{opacity:1}
		}
	</style>
	<link rel="stylesheet" type="text/css" href="${ctx}/js/channelChart-res/channel_chart.css" />
	<script src="${ctx}/js/bootstrap-editable/css/bootstrap-editable.css"></script>
	<script src="${ctx}/js/bootstrap-editable/js/bootstrap-editable.min.js"></script>
	
</head>

<body menuId="217">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
				<ul class="nav nav-tabs nav-justified" role="tablist">
					<li class="active"><a href="${ctx}/channelRealtime/channelQuality/graphPage">通道发送质量图形</a></li>
					<li><a href="${ctx}/channelRealtime/channelQuality/tablePage">通道发送质量详单</a></li>
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
						<span id="configBtn" class="label label-info"> <a href="javascript:;">阈值配置</a>
						</span>
						<span class="label label-info"> <a href="javascript:;"
							onclick="go2ChannelDetails()">查看通道详情</a>
						</span>
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/channelRealtime/channelQuality/graphData">
								<ul>
<!-- 									<li> -->
<%-- 										<u:select id="channelId" value="${channelId}" placeholder="通道号" sqlId="channel" onChange="channelOnChange" excludeValue=""/> --%>
<!-- 									</li> -->
									<li>
										<u:select id="timeType" value="${timeType}" defaultIndex="4"
											data="[{value:'0',text:'5分钟'}, {value:'1',text:'1小时'}, {value:'2',text:'12小时'}, {value:'3',text:'24小时'}]" onChange="timeTypeOnChange"/>
									</li>
								</ul>
							</form>
						</div>
					</div>
					
						<div id="configDiv" class="col-sm-6 visibility">
							<div class="widget-content nopadding">
								
								<table class="table table-bordered table-striped table-hover">
									<thead>
										<tr>
											<th>状态</th>
											<th>通道应答率</th>
											<th>通道回执率</th>
											<th>发送成功率</th>
											<th>提交失败条数</th>
											<th>发送失败率</th>
										</tr>
									</thead>
									<tbody id="userbody">
										<s:iterator value="dataList" status="st">
											<tr>
												<td><span><s:if test="data_type==0">上限</s:if><s:else>下限</s:else></span></td>
												<td><div id="${data_type},resp_rate" class="editable1">${resp_rate}</div></td>
												<td><div id="${data_type},report_rate" class="editable1">${report_rate}</div></td>
												<td><span id="${data_type},send_success_rate" class="editable1">${send_success_rate}</span></td>
												<td><span id="${data_type},submit_failure_num" class="editable2">${submit_failure_num}</span></td>
												<td><span id="${data_type},send_failure_rate" class="editable1">${send_failure_rate}</span></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
								
							</div>
						</div>
					
					<div id="tip" style="margin:0 auto; width: 600px; min-height: 50px; height: auto;">
			
						<ul class="tip-color" style="width: 600px;overflow:hidden;margin:0;">
							<li>
								<a href="#"><i class="fa fa-square tip-red"></i>糟糕</a>
							</li>
							<li>
								<a href="#"><i class="fa fa-square tip-green"></i>良好</a>
							</li>
							<li>
								<a href="#"><i class="fa fa-square tip-yellow"></i>一般</a>
							</li>
							<li>
								<a href="#"><i class="fa fa-square tip-gray"></i>空闲</a>
							</li>
						</ul>
					</div>
					<div id="ydhy" class="channelChart"></div>
					<div id="ydyx" class="channelChart"></div>
					<div id="lthy" class="channelChart"></div>
					<div id="ltyx" class="channelChart"></div>
					<div id="dxhy" class="channelChart"></div>
					<div id="dxyx" class="channelChart"></div>
				</div>
			</div>
			
			
		</div>
		
		
	</div>
	
		
<script type="text/javascript" src="${ctx}/js/channel_chart.js"></script>
<script type="text/javascript">
var intervalObj;
var INTERVAL_TIME = 60 * 1000; // 60秒

// var channelQualityList = [];
var ydhy,ydyx,lthy,ltyx,dxhy,dxyx;

$(function(){
	$("#timer").stopWatch();
// 	$("#configDiv").hide();
	var timeType = 3; // 24小时
	
	buildChannelQualityChart(timeType);
	
	// 设置定时刷新通道质量图谱
	window.clearInterval(intervalObj);
	intervalObj = setInterval(function () {
		buildChannelQualityChart(timeType);
	}, INTERVAL_TIME);
	
	buildConfigToggle();
	
	bindEditable();

});


function getQualityData(){
	var start = new Date();
	var channelQualityList;
	var result;
	$.ajax({
		type : "post",
		async : false,
		url : "${ctx}/channelRealtime/channelQuality/graphData",
		data : {
			timeType : $("#timeType").val()
		},
		success : function(data) {
			if (data != null) {
				channelQualityList = data;
			}
		}
	});
	
	if(channelQualityList.length > 0){
		result = [];
		var ydhy = { id:"ydhy", name:"移动行业", type:1, children:[]};
		var ydyx = { id:"ydyx", name:"移动营销", type:1, children:[]};
		var lthy = { id:"lthy", name:"联通行业", type:2, children:[]};
		var ltyx = { id:"ltyx", name:"联通营销", type:2, children:[]};
		var dxhy = { id:"dxhy", name:"电信行业", type:3, children:[]};
		var dxyx = { id:"dxyx", name:"电信营销", type:3, children:[]};
		result.push(ydhy);
		result.push(ydyx);
		result.push(lthy);
		result.push(ltyx);
		result.push(dxhy);
		result.push(dxyx);
		
		var pos = 0;
		for(var i=1; i<=3; i++){
			for(var j=0; j<2; j++){
				var ziyou = { name:"自有", ownerType:1, children:[]};
				var zhilian = { name:"直连", ownerType:2, children:[]};
				var disanfang = { name:"第三方", ownerType:3, children:[]};
				
				$.each(channelQualityList, function(index, item){
					if(item.operatorType == i && item.industryType == j){
						if(item.ownerType == 1){
							ziyou.children.push(item);
						}else if(item.ownerType == 2){
							zhilian.children.push(item);
						}else{
							disanfang.children.push(item);
						}
					}
				});
				if(ziyou.children.length > 0){
					result[pos].children.push(ziyou);
				}
				if(zhilian.children.length > 0){
					result[pos].children.push(zhilian);
				}
				if(disanfang.children.length > 0){
					result[pos].children.push(disanfang);
				}
				pos++;
			}
		}
		
		var end = new Date();
		console.log("channel chart ajax get date time: " + (end - start) + "ms");
		
		return result;
	}else{
		$("#tip").hide();
	}
	
}

function timeTypeOnChange(value, text, isInit){
	if(isInit){
		return;
	}
	
	var timeType = value;
	buildChannelQualityChart(timeType);
	
	// 设置定时刷新通道质量图谱
	window.clearInterval(intervalObj);
	intervalObj = setInterval(function () {
		buildChannelQualityChart(timeType);
	}, INTERVAL_TIME);
	
}

function buildChannelQualityChart(timeType){
// 	console.log("channel chart painting begain.");
	var start = new Date();
	var column;
	var cell;
	if(timeType == 0){
		column = 1;
		cell = 1;
	}else if(timeType == 1){
		column = 12;
		cell = 1;
	}else if(timeType == 2){
		column = 12;
		cell = 12;
	}else{
		column = 24;
		cell = 12;
	}
	var channelQualityData = getQualityData();

	$(".channelChart").empty();
	if(typeof(channelQualityData) != "undefined" && channelQualityData != null){
		$("#tip").show(); // 显示状态提示
		
		$.each(channelQualityData, function(index, item){
			if(item.children.length > 0){
				console.log("painting: " + item.id);
				if(item.id == "ydhy"){
					ydhy = Channel_Chart("#" + item.id);
					ydhy.setOption({data : item, column : column, cell : cell});
				}else if(item.id == "ydyx"){
					ydyx = Channel_Chart("#" + item.id);
					ydyx.setOption({data : item, column : column, cell : cell});
				}else if(item.id == "lthy"){
					lthy = Channel_Chart("#" + item.id);
					lthy.setOption({data : item, column : column, cell : cell});
				}else if(item.id == "ltyx"){
					ltyx = Channel_Chart("#" + item.id);
					ltyx.setOption({data : item, column : column, cell : cell});
				}else if(item.id == "dxhy"){
					dxhy = Channel_Chart("#" + item.id);
					dxhy.setOption({data : item, column : column, cell : cell});
				}else{
					dxyx = Channel_Chart("#" + item.id);
					dxyx.setOption({data : item, column : column, cell : cell});
				}
			}
		});
		statusListClickEvent();
	}else{
		$("#tip").hide();
	}
	
	var end = new Date();
	console.log("channel chart painting time: " + (end - start) + "ms");
}

function go2ChannelDetails(){
// 	var channelId = $("#channelId").val();
// 	var timeType = $("#timeType").val();
	
	go2URL("${ctx}/channelRealtime/channelQuality/details/page");
}

function statusListClickEvent(){
	
	$(".status-list").find("ul").click( function () { 
		var channelId = $(this).parent().parent().find("p.id").text();
		var timeType = $("#timeType").val();
		go2URL("${ctx}/channelRealtime/channelQuality/details/page?channelId=" + channelId + "&timeType=" + timeType);
	});
}

function buildConfigToggle(){
	  
	  $("#configBtn").click(function(){
		  $("#configDiv").toggleClass("visibility");
	  })
		 
}

function bindEditable(){
	$.fn.editable.defaults.mode = 'popup';
	
 	$('.editable1').editable({
	    type: 'text',
	    pk: 1,
	    url: '${ctx}/channelRealtime/config/update',
	    title: '输入阀值',
    	validate: function(value) {
    		// 非空校验
    	    if($.trim(value) == '') {
    	        return '必须填写';
    	    }
    		
    		if(value >= 1 || value <= 0){
    			return '数值范围(0,1)且保留3位小数';
    		}
    		
    		var length = value.toString().split(".")[1].length;
    		if(length > 3){
    			return '小数点后不能超过3位';
    		}
    		
    	}
	});
 	
 	$('.editable2').editable({
	    type: 'text',
	    pk: 1,
	    url: '${ctx}/channelRealtime/config/update',
	    title: '输入阀值',
    	validate: function(value) {
    		// 非空校验
    	    if($.trim(value) == '') {
    	        return '必须填写';
    	    }
    		
    		if(value <= 100){
    			return '数值必须大于等于100';
    		}
    		
    	}
	});
}

//@ sourceURL= channelQualityGraph.js
</script>
</body>
</html>