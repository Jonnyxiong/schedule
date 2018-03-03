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
		.color-slide{
			position:absolute;
			width:94px;
			
			top:460px;
			left:90%;
		}
		.color-slide2{
			top:1000px;
		}
		.color-slide p{
			width:70px;
		
		}
		.color-slide img{
			width:25px;
			height:205px;
			position:absolute;
			right:0;
			top:0;
		}
		.color-pannel{
			width:70px;
			height:34px;
			color:#FFF;
			text-align:center;
			line-height:34px;
		}
		.color-pannel1{background-color:#34A952;}
		.color-pannel2{background-color:#E94336;}
		.color-pannel3{background-color:#FBBD04;}
	</style>
	<script src="${ctx}/js/echart/echarts.min.js"></script>
</head>

<body menuId="217">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
				<ul class="nav nav-tabs nav-justified" role="tablist">
				<li><a href="${ctx}/channelRealtime/channelQuality/graphPage">通道发送质量图形</a></li>
				<li><a href="${ctx}/channelRealtime/channelQuality/tablePage">通道发送质量详单</a></li>
				<li><a href="${ctx}/channelRealtime/reportRespStack/page">通道质量趋势分布图</a></li>
				<li class="active"><a href="${ctx}/channelRealtime/channelSuccessRateStack/page">通道成功率趋势分布图</a></li>
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
<!-- 						<span class="label label-info"> <a href="javascript:;" -->
<!-- 							onclick="go2ChannelQuality()">返回</a> -->
<!-- 						</span> -->
						
						<div class="search">
							<ul>
								<li>
									<u:select id="channelId" value="${channelId}" placeholder="通道号" sqlId="findAllChannel" onChange="channelOnChange" excludeValue=""/>
								</li>
								<li>
									<input type="hidden" id="timeType" name="timeType" value="3"/>
									<!-- 
									<u:select id="timeType" value="${timeType}" defaultIndex="4"
										data="[{value:'0',text:'5分钟'}, {value:'1',text:'1小时'}, {value:'2',text:'12小时'}, {value:'3',text:'24小时'}]" onChange="timeTypeOnChange"/>
										 -->
								</li>
								<li>
									<a href="javascript:;" class=" label label-info" onclick="exportExcel()">导出Excel文件</a>
								</li>
							</ul>
						</div>
					</div>
					
					<div class="col-sm-6" style="position:relative;overflow:hidden;">
							<div id="reportStackChart" style="width:90%; height:600px; float:left; margin-top:40px;"></div>
							<div class="color-slide">
								
								<div class='color-pannel color-pannel2'>失败率</div>
								<div class='color-pannel color-pannel3'>未知率</div>
								<div class='color-pannel color-pannel1'>成功率</div>
							</div>
							<div id="timer" data-time="" style="font-size:20px;font-weight:800;position:absolute;top:10px;
						left:10px;">系统时间&nbsp;
								<span class="h"></span>:
								<span class="m"></span>:
								<span class="s"></span>
							</div>
							 
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">

var intervalObj; //全局定时器
var GET_REPORT_RESP_STACK_URL = "${ctx}/channelRealtime/channel/getSuccessRateStackData";
var INTERVAL_TIME = 60 * 1000; // 60秒

var reportStackChart = echarts.init(document.getElementById('reportStackChart'));
var echartData;
$(function(){
	$("#timer").stopWatch();
	
	
	$("#channelId").select2({});
	 
	var channelId = $("#channelId").val();
	var timeType = $("#timeType").val();
	refreshAllEchartData(channelId, timeType);
		
	window.clearInterval(intervalObj);
	intervalObj = setInterval(function () {
		refreshAllEchartData(channelId, timeType);
	}, INTERVAL_TIME);
	
	echartShowLoading();
	
	// 初始化Echart容器和配置
	var options = echartOptionInit();
	reportStackChart.setOption(options.reportStackOption);
	
});


function setReportStackData(reportStackChart, channelId, timeType, ajaxUrl){
	$.ajax({
		type : "post",
		url : ajaxUrl,
		data : {
			channelId : channelId,
			timeType : timeType
		},
		success : function(data) {
			echartData = data;
			reportStackChart.hideLoading();
			if (data != null) {
				reportStackChart.setOption({
					xAxis: {
			            data: data.xAxisData
			        },
					series: [{
		 	            data: data.sendTotal
		 	        },{
		 	            data: data.submitFail
		 	        },{
		 	            data: data.sendFail
		 	        },{
		 	            data: data.successRate
		 	        },{
		 	            data: data.fakeSuccessRate
		 	        },{
		 	            data: data.reallyFailRate
		 	        }]
		 	    });
				
			}
		}
	});
}


// 通道下拉框选择事件，页面初始化的时候会执行一次
function channelOnChange(value, text, isInit){
	var channelId = value;
	var timeType = $("#timeType").val();
	
	console.log("查询客户： " + channelId);
	console.log("查询时间类型：" + timeType);
	
	if(channelId == "" || channelId == undefined){
		return;
	}else{
		refreshAllEchartData(channelId, timeType);
		
		window.clearInterval(intervalObj);
		intervalObj = setInterval(function () {
			refreshAllEchartData(channelId, timeType);
		}, INTERVAL_TIME);
	}
}

function timeTypeOnChange(value, text, isInit){
	var channelId = $("#channelId").val();
	var timeType = value;
	
	console.log("查询客户： " + channelId);
	console.log("查询时间类型：" + timeType);
	
	if(channelId == "" || channelId == undefined){
		return;
	}else{
		refreshAllEchartData(channelId, timeType);
		
		window.clearInterval(intervalObj);
		intervalObj = setInterval(function () {
			refreshAllEchartData(channelId, timeType);
		}, INTERVAL_TIME);
	}
}

function refreshAllEchartData(channelId, timeType){
	console.log("reresh stack...");
	setReportStackData(reportStackChart, channelId, timeType, GET_REPORT_RESP_STACK_URL);
}


function echartOptionInit(){
	var result = {};
		
	result.reportStackOption = {
		 title: {
                text: '成功率趋势分布图',
                x: 'center'
         },
         tooltip: {
             trigger: 'axis',
        	 axisPointer: {
                 animation: false
             },
             formatter: function (params) {
            	 if(!(params instanceof Array)){
            		 return;
            	 }
             	 var html = '&nbsp&nbsp&nbsp时间：' + ((params[0].name==undefined||params[0].name == '') ? '-':params[0].name) + '<br />'
		    			+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[5].color +'"></span>' 
 		    			+ params[5].seriesName + '：' + (params[5].value != undefined ? (params[5].value + ' %') : '-') + '<br />'
		    			+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[4].color +'"></span>' 
 		    			+ params[4].seriesName + '：' + (params[4].value != undefined ? (params[4].value + ' %') : '-') + '<br />'
             		 		+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[3].color +'"></span>' 
             	 		    + params[3].seriesName + '：' + (params[3].value != undefined ? (params[3].value + ' %') : '-') + '<br />'
             	 		  	+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[2].color +'"></span>' 
           	 		    	+ params[2].seriesName + '：' + (params[2].value != undefined ? (params[2].value + ' 条') : '-') + '<br />'
	           	 		    + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[1].color +'"></span>' 
	       	 		    	+ params[1].seriesName + '：' + (params[1].value != undefined ? (params[1].value + ' 条') : '-') + '<br />'
	           	 		    + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[0].color +'"></span>' 
	       	 		    	+ params[0].seriesName + '：' + (params[0].value != undefined ? (params[0].value + ' 条') : '-');
                 return html;
             }
         },
         legend: {
         	data:['发送总量','提交失败总量','发送失败总量','成功率','未知率','失败率'],
	    	bottom: 10,
	    	icon: 'circle',
            left: 'center'
         },
         toolbox: {
             show: false, //是否显示工具箱
             feature: {
                 saveAsImage: { show: true }
             }
         },
         calculable: false,
         grid: {
             left: '3%',
             right: '3%',
             bottom: '10%',
             containLabel: true
         },
         xAxis: {
             type: 'category',
             splitLine: {
                 show: false
             },
             boundaryGap: false,
             data: [] // 此处即使没有数据也一定要设置为空不然Echart会报错
         },
         yAxis: [
             {
	            type: 'value',
	            name: '条数占比',
	            min: 0,
	            max: 100,
	            position: 'left',
	            axisLabel: {
	                formatter: '{value} %'
	            }
	        },
	        {
	            type: 'value',
	            name: '发送数量',
	            position: 'right',
	            axisLabel: {
	                formatter: '{value} 条'
	            }
	        }
         ],
         series : [
	        	 {
	                 name:'发送总量',
	                 type:'line',
	                 yAxisIndex: 1,
	                 symbolSize:10,
	                 symbol:'none',  //这句就是去掉点的  
	                 smooth:true,  //这句就是让曲线变平滑的  
	                 itemStyle: {
	                     normal: {
	                         color: "#4285F3",
	                         barBorderRadius: 0,
	                     }
	                 }
	             },{
                     name:'提交失败总量',
                     type:'line',
                     yAxisIndex: 1,
                     symbolSize:10,
                     symbol:'none',  //这句就是去掉点的  
                     smooth:true,  //这句就是让曲线变平滑的  
                     itemStyle: {
                         normal: {
                             color: "grey",
                             barBorderRadius: 0,
                         }
                     }
                 },{
                     name:'发送失败总量',
                     type:'line',
                     yAxisIndex: 1,
                     symbolSize:10,
                     symbol:'none',  //这句就是去掉点的  
                     smooth:true,  //这句就是让曲线变平滑的  
                     itemStyle: {
                         normal: {
                             color: "#FF0000",
                             barBorderRadius: 0,
                         }
                     }
                 },
                   {
                       name:'成功率',
                       type:'line',
                       stack: '条数占比',
                       symbol:'none',  //这句就是去掉点的  
                       smooth:true,  //这句就是让曲线变平滑的  
                       areaStyle: {normal: {}},
                       lineStyle: {
                           normal: {
                               width: 0
                           }
                       },
                       yAxisIndex: 0,
                       itemStyle: {
                           normal: {
                               color: "#34A952",
                               label: {
                                   show: false
                               }
                           }
                       },
                       markLine: {
                           symbol: ['none', 'none'],
                           label: {
                               normal: {show: false}
                           },
                           lineStyle: {
                               normal: {
                            	   type: 'dotted',
                                   color: 'red',
                                   width: 1
                               }
                           },
                           data: [{
                               yAxis: 80
                           }]
                       }
                   },
                   {
                       name:'未知率',
                       type:'line',
                       stack: '条数占比',
                       symbol:'none',  //这句就是去掉点的  
                       smooth:true,  //这句就是让曲线变平滑的  
                       areaStyle: {normal: {}},
                       lineStyle: {
                           normal: {
                               width: 0
                           }
                       },
                       yAxisIndex: 0,
                       itemStyle: {
                           normal: {
                               color: "#FBBD04",
                               label: {
                                   show: false
                               }
                           }
                       }
                   },
                   {
                       name:'失败率',
                       type:'line',
                       stack: '条数占比',
                       symbol:'none',  //这句就是去掉点的  
                       smooth:true,  //这句就是让曲线变平滑的  
                       areaStyle: {normal: {}},
                       lineStyle: {
                           normal: {
                               width: 0
                           }
                       },
                       yAxisIndex: 0,
                       itemStyle: {
                           normal: {
                               color: "#E94336",
                               label: {
                                   show: false
                               }
                           }
                       }
                   }
               ]
	};
	
	return result;
}

function echartShowLoading(){
		
	reportStackChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	 
	
}

function exportExcel(){
	
	if(echartData.xAxisData.length == 0){
		layer.alert("分布图中无数据展示，请确认导出数据不为空");
	}else{
		download("${ctx}/channelHistory/channel/getSuccessRateStackData/exportExcel", "post");
	}
}

function download(url, method){
    jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
                '<input type="text" name="channelId" value="'+$("#channelId").val()+'"/>' + // 通道ID
                '<input type="text" name="timeType" value="'+$("#timeType").val()+'"/>' + // 时间
            '</form>')
    .appendTo('body').submit().remove();
};

</script>
</body>
</html>