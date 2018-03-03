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
	</style>
	<script src="${ctx}/js/echart/echarts.min.js"></script>
</head>

<body menuId="218">
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
							<ul>
								<li>
									<u:select id="channelId" value="${channelId}" placeholder="通道号" sqlId="channel" onChange="channelOnChange" excludeValue=""/>
								</li>
								<li>
									<u:select id="timeType" value="${timeType}" 
										data="[{value:'0',text:'5分钟'}, {value:'1',text:'1小时'}, {value:'2',text:'12小时'}, {value:'3',text:'24小时'}]" onChange="timeTypeOnChange"/>
								</li>
							</ul>
						</div>
					</div>
					
					<div class="col-sm-6" style="">
						<div class="widget-content nopadding">
							
							<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	    					<div id="myChart" style="min-width: 600px;min-height:600px;"></div>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">

var intervalObj;
var myChart = echarts.init(document.getElementById('myChart'));
var GET_CHANNEL_ERROR_URL = "${ctx}/channelRealtime/channelError/data";
var INTERVAL_TIME = 60 * 1000; // 60秒

$(function(){
	var option = {
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} / {c}条 ({d}%)"
		    },
		    legend: {
		        orient: 'horizontal',
		        x: 'center',
		        y: 'bottom'
		    },
		    series: [
		        {
		            name:'错误类型',
		            type:'pie',
		            radius: ['50%', '70%'],
		            avoidLabelOverlap: true,
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b} / {c}条 ({d}%)"
		                },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '30',
		                        fontWeight: 'bold'
		                    }
		                }
		            },
		            labelLine: {
		                normal: {
		                    show: true
		                }
		            },
		            data:[
		            ]
		        }
		    ]
		};
	
	myChart.setOption(option);
	
	$("#channelId").select2({});

});


//通道下拉框选择事件，页面初始化的时候会执行一次
function channelOnChange(value, text, isInit){
	var channelId = value;
	var timeType = $("#timeType").val();
	
	console.log("查询通道： " + channelId);
	console.log("查询时间类型：" + timeType);
	
	if(channelId == "" || channelId == undefined){
		return;
	}else{
		setPieData(myChart, channelId, timeType, GET_CHANNEL_ERROR_URL);
		
		window.clearInterval(intervalObj);
		intervalObj = setInterval(function () {
			setPieData(myChart, channelId, timeType, GET_CHANNEL_ERROR_URL);
		}, INTERVAL_TIME);
	}
}

function timeTypeOnChange(value, text, isInit){
	var channelId = $("#channelId").val();
	var timeType = value;
	
	console.log("查询通道： " + channelId);
	console.log("查询时间类型：" + timeType);
	
	if(channelId == "" || channelId == undefined){
		return;
	}else{
		setPieData(myChart, channelId, timeType, GET_CHANNEL_ERROR_URL);
		
		window.clearInterval(intervalObj);
		intervalObj = setInterval(function () {
			setPieData(myChart, channelId, timeType, GET_CHANNEL_ERROR_URL);
		}, INTERVAL_TIME);
	}
}

//设置Echart饼图数据
function setPieData(myChart, channelId, timeType, ajaxUrl){
	$.ajax({
		type : "post",
		url : ajaxUrl,
		data : {
			channelId : channelId,
			timeType : timeType
		},
		success : function(data) {
			myChart.hideLoading();
			if (data != null) {
				myChart.setOption({
					legend: {
		 		        data: data.legendList
				    },
					series : [
						        {
						            data : data.pieDataList
						        }
						    ]
		 	    });
			}
		}
	});
}

</script>
</body>
</html>