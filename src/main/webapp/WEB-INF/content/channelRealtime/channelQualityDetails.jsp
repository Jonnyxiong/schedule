<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


<html>
<head>
	<title>${data.chart.title}</title>
	<style type="text/css">
		.clear{ clear:both}
		.select2-container{
			margin-top: 5px;
		}
		.select2-dropdown {
		  margin-top: -10px;
		}
	</style>
	<script src="${ctx}/js/echart/echarts.min.js"></script>
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
						<span class="icon"> 
							<i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<span class="label label-info"> <a href="javascript:;"
							onclick="go2ChannelQuality()">通道质量状态图</a>
						</span>
						</span>
						<div class="search">
							<ul>
								<li>
									<u:select id="channelId" value="${channelId}" placeholder="通道号" sqlId="findAllChannel" onChange="channelOnChange" excludeValue=""/>
								</li>
								<li>
									<u:select id="timeType" value="${timeType}" 
										data="[{value:'0',text:'5分钟'}, {value:'1',text:'1小时'}, {value:'2',text:'12小时'}, {value:'3',text:'24小时'}]" onChange="timeTypeOnChange"/>
								</li>
							</ul>
						</div>
					</div>
					
					<div class="col-sm-6" style="">
	    					<div id="channelSpeedChart" style="width: 50%; height:300px; float:left;"></div>
	    					<div id="successRateChart" style="width:50%; height:300px; float:left;"></div>
							<div class="clear"></div>
	    					<div id="respRateChart" style="width: 50%; height:300px; float:left; margin-top:40px;"></div>
	    					<div id="reportRateChart" style="width:50%; height:300px; float:left; margin-top:40px;"></div>
							<div class="clear"></div>
							<div id="errorRateChart" style="width:100%; height:300px; float:left; margin-top:40px;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">

var intervalObj; //全局定时器
var GET_SPEED_URL = "${ctx}/channelRealtime/channel/seed-speed";
var GET_SUCC_RATE_URL = "${ctx}/channelRealtime/channel/succRate";
var GET_RESP_RATE_URL = "${ctx}/channelRealtime/channel/respRate";
var GET_REPORT_RATE_URL = "${ctx}/channelRealtime/channel/reportRate";
var GET_CHANNEL_ERROR_URL = "${ctx}/channelRealtime/channelError/data";

var channelSpeedChart = echarts.init(document.getElementById('channelSpeedChart'));
var successRateChart = echarts.init(document.getElementById('successRateChart'));
var respRateChart = echarts.init(document.getElementById('respRateChart'));
var reportRateChart = echarts.init(document.getElementById('reportRateChart'));
var errorRateChart = echarts.init(document.getElementById('errorRateChart'));

$(function(){
	$("#channelId").select2({});
	
	var channelId = getUrlParam("channelId");
	var timeType = getUrlParam("timeType");
	// 初始化通道发送速率
	$("#channelId").val(channelId);
	$("#timeType").val(timeType);
	channelOnChange(channelId, "", false);
	
	echartShowLoading();
	
	var options = echartOptionInit();
	
	channelSpeedChart.setOption(options.channelSpeedOption);
	successRateChart.setOption(options.succRatePieOption);
	respRateChart.setOption(options.respRatePieOption);
	reportRateChart.setOption(options.reportRatePieOption);
	errorRateChart.setOption(options.errorRatePieOption);
	
	// 默认不显示“承诺速率”
	channelSpeedChart.dispatchAction({
		type: 'legendUnSelect',
	    name: '承诺速率'
	});
	
});

// 设置Echart时间曲线图数据（发送速率）
function setTimeLineData(myChart, channelId, timeType, ajaxUrl){
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
					title: {
// 		                text: data.chartName,
		                text: "发送速率",
		                x: 'center'
		            },
		 	        series: [{
		 	            data: data.actualSeries
		 	        },{
		 	            data: data.promiseSeries
		 	        }]
		 	    });
			}
		}
	});
}

//设置Echart饼图数据（发送成功率、应答率、回执率）
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
					series : [
						        {
						            data : data
						        }
						    ]
		 	    });
			}
		}
	});
}

function setErrorRatePieData(myChart, channelId, timeType, ajaxUrl){
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


// 通道下拉框选择事件，页面初始化的时候会执行一次
function channelOnChange(value, text, isInit){
	var channelId = value;
	var timeType = $("#timeType").val();
	
// 	console.log("查询通道： " + channelId);
// 	console.log("查询时间类型：" + timeType);
	
	if(channelId == "" || channelId == undefined || channelId == null){
		return;
	}else{
		refreshAllEchartData(channelId, timeType);
		
		window.clearInterval(intervalObj);
		intervalObj = setInterval(function () {
			refreshAllEchartData(channelId, timeType);
		}, 10000);
	}
}

function timeTypeOnChange(value, text, isInit){
	var channelId = $("#channelId").val();
	var timeType = value;
	
// 	console.log("查询通道： " + channelId);
// 	console.log("查询时间类型：" + timeType);
	
	if(channelId == "" || channelId == undefined || channelId == null){
		return;
	}else{
		refreshAllEchartData(channelId, timeType);
		
		window.clearInterval(intervalObj);
		intervalObj = setInterval(function () {
			refreshAllEchartData(channelId, timeType);
		}, 10000);
	}
}

function refreshAllEchartData(channelId, timeType){
	setTimeLineData(channelSpeedChart, channelId, timeType, GET_SPEED_URL);
	setPieData(successRateChart, channelId, timeType, GET_SUCC_RATE_URL);
	setPieData(respRateChart, channelId, timeType, GET_RESP_RATE_URL);
	setPieData(reportRateChart, channelId, timeType, GET_REPORT_RATE_URL);
	setErrorRatePieData(errorRateChart, channelId, timeType, GET_CHANNEL_ERROR_URL);
}


function echartOptionInit(){
	var result = {};
	
	result.channelSpeedOption = {
            title: {
                text: '通道发送速率',
                x: 'center'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
            	data:['实际速率','承诺速率'],
                bottom: 10,
                left: 'center'
            },
//             color:['#2f4554','#c23531'],
            color:['#28b779','#c23531'],
            toolbox: {
                show: false, //是否显示工具箱
                feature: {
                    saveAsImage: { show: true }
                }
            },
            calculable: true, //容易搞错的属性，折线图、柱状图是否叠加
            grid: {
                left: '3%',
                right: '4%',
                bottom: '10%',
                containLabel: true
            },
            xAxis: {
                type: 'time',
                splitLine: {
                    show: false
                }
            },
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                      {
                          name:'实际速率',
                          type:'line',
                          showSymbol: false
                      },
                      {
                          name:'承诺速率',
                          type:'line',
                          showSymbol: false
                      }
                  ]
        };
	
	
	result.succRatePieOption = {
		    title : {
		        text: '发送成功率',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'right',
		        data: ['发送成功','发送失败','未知']
		    },
		    color:['#91c7ae','#c23531','#2f4554'],
		    series : [
		        {
		            name: '发送成功率',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b} : {c}条"
		                },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '30',
		                        fontWeight: 'bold'
		                    }
		                }
		            }
		        }
		    ]
		};
	
	result.respRatePieOption = {
		    title : {
		        text: '应答率',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/> {b} / {c} / {d}%"
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'right',
		        data: ['0-2s','3-5s','6-10s','11-60s','61-300s','>300s','应答超时']
		    },
		    color:['#91c7ae','#61a0a8','#6e7074','#ca8622','#2f4554','#d48265','#c23531'],
		    series : [
		        {
		            name: '时间/条数/占比',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b} : {c}条"
		                },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '30',
		                        fontWeight: 'bold'
		                    }
		                }
		            }
		        }
		    ]
		};
	
	
	result.reportRatePieOption = {
		    title : {
		        text: '回执率',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/> {b} / {c} / {d}%"
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'right',
		        data: ['0-10s','11-30s','31-60s','61-120s','121-300s','>300s','回执未返回']
		    },
		    color:['#91c7ae','#61a0a8','#6e7074','#ca8622','#2f4554','#d48265','#c23531'],
		    series : [
		        {
		            name: '时间/条数/占比',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b} : {c}条"
		                },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '30',
		                        fontWeight: 'bold'
		                    }
		                }
		            }
		        }
		    ]
		};
	
	result.errorRatePieOption = {
			title : {
		        text: '错误码',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} / {c}条 ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'right'
// 		        data: ['0-5s','6-10s','11-15s','16-30s','>30s']
		    },
		    series : [
		        {
		            name: '错误类型',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
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
		            }
		        }
		    ]
	};
	
	return result;
}

function echartShowLoading(){
	channelSpeedChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
	successRateChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
	respRateChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
	reportRateChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
	errorRateChart.showLoading({  
	    text: '正在努力加载中...'  
	}); 
}

function go2ChannelQuality(){
	var channelId = $("#channelId").val();
	var timeType = $("#timeType").val();
	
	go2URL("${ctx}/channelRealtime/channelQuality/graphPage");
}

</script>
</body>
</html>