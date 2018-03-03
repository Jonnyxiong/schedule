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
			
			top:350px;
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
		}
		.color-pannel1{background-color:#003870;}
		.color-pannel2{background-color:#004C99;}
		.color-pannel3{background-color:#0066CC;}
		.color-pannel4{background-color:#0080FF;}
		.color-pannel5{background-color:#3399FF;}
		.color-pannel6{background-color:#94E2FF;}
		
	</style>
	<script src="${ctx}/js/echart/echarts.min.js"></script>
	<script src="${ctx}/js/common.js"></script>
</head>

<body menuId="232">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
				<ul class="nav nav-tabs nav-justified" role="tablist">
					<li class="active"><a href="${ctx}/channelHistory/channelQuality/page">通道质量趋势分布图</a></li>
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
						
						<div class="search">
							<ul>
								<li>
									<u:select id="channelId" value="${channelId}" placeholder="通道号" sqlId="channel" onChange="channelOnChange" excludeValue=""/>
								</li>
								<li>
									<u:date id="startTime" value="${startTime}" placeholder="开始时间" maxId="endTime,-1" dateFmt="yyyyMMdd" />
									<span>至</span>
            						<u:date id="endTime" value="${endTime}" placeholder="结束时间" minId="startTime,1" dateFmt="yyyyMMdd" />
								</li>
								<li>
									<li><input type="button" value="搜索" onclick="search()"/></li>
								</li>
								
								<li>
									<a href="javascript:;" class=" label label-info" onclick="exportExcel()">导出Excel文件</a>
								</li>
								
							</ul>
						</div>
					</div>
					
					<div class="col-sm-6" style="overflow:hidden;position:relative;">
							<div id="reportStackChart" style="width:90%; height:600px; float:left; margin-top:40px;"></div>
							<div id="respStackChart" style="width:90%; height:600px; float:left; margin-top:40px;"></div>
							<div class="color-slide">
								<div class='color-pannel color-pannel1'></div>
								<div class='color-pannel color-pannel2'></div>
								<div class='color-pannel color-pannel3'></div>
								<div class='color-pannel color-pannel4'></div>
								<div class='color-pannel color-pannel5'></div>
								<div class='color-pannel color-pannel6'></div>
								<img src="${ctx}/img/color-link.png">	
								<p>颜色越深，回执时间长</p>
							</div>
							<div class="color-slide color-slide2">
								<div class='color-pannel color-pannel1'></div>
								<div class='color-pannel color-pannel2'></div>
								<div class='color-pannel color-pannel3'></div>
								<div class='color-pannel color-pannel4'></div>
								<div class='color-pannel color-pannel5'></div>
								<div class='color-pannel color-pannel6'></div>
								<img src="${ctx}/img/color-link.png">
								<p>颜色越深，应答时间越长</p>
							</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">

var GET_REPORT_RESP_STACK_URL = "${ctx}/channelHistory/channel/getReportRespStackData";

var reportStackChart = echarts.init(document.getElementById('reportStackChart'));
var respStackChart = echarts.init(document.getElementById('respStackChart'));
var echartData;
$(function(){
	initSearchTime();
	$("#channelId").select2({});
	
	echartShowLoading();
	
	// 初始化Echart容器和配置
	var options = echartOptionInit();
	reportStackChart.setOption(options.reportStackOption);
	respStackChart.setOption(options.respStackOption);
	
	search();
	reportStackChart.dispatchAction({
		type: 'legendUnSelect',
	    name: '成功率'
	});
});

function initSearchTime(){
	var dateFamart = new DateFormat(false);
	var nowTime = new Date();
	endTime = dateFamart.format(nowTime, "yyyyMMdd");
	startTime = dateFamart.changeDate(nowTime,"DATE",-30,"yyyyMMdd","yyyyMMdd");
	$("#startTime").val(startTime);
	$("#endTime").val(endTime);
}

function search(){
	var channelId = $("#channelId").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	
	if(startTime == "" || endTime == ""){
		layer.alert("搜索时间不能为空");
		return;
	}
// 	if(Number(startTime) > Number(endTime)){
// 		layer.alert("开始时间不能大于结束时间");
// 		return;
// 	}
	refreshAllEchartData(channelId, startTime, endTime);
	
}


function setReportStackData(reportStackChart, respStackChart, reqDataObj, ajaxUrl){
	$.ajax({
		type : "post",
		url : ajaxUrl,
		data : reqDataObj,
		success : function(data) {
			reportStackChart.hideLoading();
			respStackChart.hideLoading();
			if (data != null) {
				echartData = data;
				reportStackChart.setOption({
					xAxis: {
			            data: data.xAxisData
			        },
					series: [{
		 	            data: data.report1
		 	        },{
		 	            data: data.report2
		 	        },{
		 	            data: data.report3
		 	        },{
		 	            data: data.report4
		 	        },{
		 	            data: data.report5
		 	        },{
		 	            data: data.report6
		 	        },{
		 	            data: data.sccessRate
		 	        },{
		 	            data: data.sendTotal
		 	        }]
		 	    });
				
				respStackChart.setOption({
					xAxis: {
			            data: data.xAxisData
			        },
					series: [{
		 	            data: data.resp1
		 	        },{
		 	            data: data.resp2
		 	        },{
		 	            data: data.resp3
		 	        },{
		 	            data: data.resp4
		 	        },{
		 	            data: data.resp5
		 	        },{
		 	            data: data.resp6
		 	        },{
		 	            data: data.sendTotal
		 	        }]
		 	    });
			}
		}
	});
}


// 通道下拉框选择事件，页面初始化的时候会执行一次
function channelOnChange(value, text, isInit){
	if(isInit){
		return;
	}
	var channelId = value;
	if(channelId == "" || channelId == undefined){
		return;
	}else{
		search();
	}
}


function refreshAllEchartData(channelId, startTime, endTime){
	console.log("reresh stack...");
	var reqDataObj = {};
	reqDataObj.channelId = channelId;
	reqDataObj.startTime = startTime;
	reqDataObj.endTime = endTime;
	setReportStackData(reportStackChart, respStackChart, reqDataObj, GET_REPORT_RESP_STACK_URL);
}


function echartOptionInit(){
	var result = {};
		
	result.reportStackOption = {
		 title: {
                text: '回执率分布图',
                x: 'center'
         },
         tooltip: {
             trigger: 'axis',
        	
         },
         legend: {
         	data:['回执时长[0s-5s]','回执时长(5s-10s]','回执时长(10s-30s]','回执时长(30s-60s]','回执时长[60s以上]','回执未返回','成功率','发送总量'],
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
	            name: '回执率占比',
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
	        },
         ],
         series : [
             {
                 name:'回执时长[0s-5s]',
                 type:'line',
                 stack: '回执率总量',
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
                         color: "#87CEFA",
                         label: {
                             show: false,
//                              textStyle: {
//                                  color: "#fff"
//                              },
//                              position: "insideTop",
//                              formatter: function(p) {
//                                  return p.value > 0 ? (p.value) : '';
//                              }
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
                 name:'回执时长(5s-10s]',
                 type:'line',
                 stack: '回执率总量',
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
                         color: "#3399FF",
                         label: {
                             show: false,
//                              textStyle: {
//                                  color: "#fff"
//                              },
//                              position: "insideTop",
//                              formatter: function(p) {
//                                  return p.value > 0 ? (p.value) : '';
//                              }
                         }
                     }
                 }
             },
             {
                 name:'回执时长(10s-30s]',
                 type:'line',
                 stack: '回执率总量',
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
                         color: "#0080FF",
                         label: {
                             show: false,
//                              textStyle: {
//                                  color: "#fff"
//                              },
//                              position: "insideTop",
//                              formatter: function(p) {
//                                  return p.value > 0 ? (p.value) : '';
//                              }
                         }
                     }
                 }
             },
             {
                 name:'回执时长(30s-60s]',
                 type:'line',
                 stack: '回执率总量',
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
                         color: "#0066CC",
                         label: {
                             show: false,
//                              textStyle: {
//                                  color: "#fff"
//                              },
//                              position: "insideTop",
//                              formatter: function(p) {
//                                  return p.value > 0 ? (p.value) : '';
//                              }
                         }
                     }
                 }
             },
             {
                 name:'回执时长[60s以上]',
                 type:'line',
                 stack: '回执率总量',
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
                         color: "#004C99",
                         label: {
                             show: false,
//                              textStyle: {
//                                  color: "#fff"
//                              },
//                              position: "insideTop",
//                              formatter: function(p) {
//                                  return p.value > 0 ? (p.value) : '';
//                              }
                         }
                     }
                 }
             },
             {
                 name:'回执未返回',
                 type:'line',
                 stack: '回执率总量',
                 areaStyle: {normal: {}},
                 lineStyle: {
                     normal: {
                         width: 0
                     }
                 },
                 yAxisIndex: 0,
                 itemStyle: {
                     normal: {
//                          color: "#c23531",
                         color: "#003366",
                         label: {
                             show: false,
//                              textStyle: {
//                                  color: "#fff"
//                              },
//                              position: "insideTop",
//                              formatter: function(p) {
//                                  return p.value > 0 ? (p.value) : '';
//                              }
                         }
                     }
                 }
             },
             {
                 name:'成功率',
                 type:'line',
                 yAxisIndex: 0,
                 symbolSize: 5,
                 symbol:'none',  //这句就是去掉点的  
                 smooth:true,  //这句就是让曲线变平滑的  
                 itemStyle: {
                     normal: {
                         color: "red",
                         barBorderRadius: 0,
                         label: {
                             show: false,
                             position: "top",
                             formatter: function(p) {
                                 return p.value > 0 ? (p.value) : '';
                             }
                         }
                     }
                 }
             },
             {
                 name:'发送总量',
                 type:'line',
                 yAxisIndex: 1,
                 symbolSize:10,
                 symbol:'none',  //这句就是去掉点的  
                 smooth:true,  //这句就是让曲线变平滑的  
                 itemStyle: {
                     normal: {
                         color: "#FF7F50",
                         barBorderRadius: 0,
//                          label: {
//                              show: false,
//                              position: "top",
//                              formatter: function(p) {
//                                  return p.value > 0 ? (p.value) : '';
//                              }
//                          }
                     }
                 }
             }
         ]
	};
	
	
	result.respStackOption = {
			 title: {
	                text: '应答率分布图',
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
	            	 // 0 ：回执时长[0-10]；1：回执时长[11-30]；2：回执时长[31-60]；3:回执时长[61-120]；4：回执时长[121-300]； 5：回执时长[300以上]； 6、：回执未返回； 7：成功率； 8：发送总量
	             	 var html = '&nbsp&nbsp&nbsp时间：' + (params[0].name != '' ? params[0].name : '-') + '<br />'
	             		 		+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[0].color +'"></span>' 
	             	 		    + params[0].seriesName + '：' + (params[0].value != undefined ? (params[0].value + ' %') : '-') + '<br />'
	             	 		  	+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[1].color +'"></span>' 
	           	 		    	+ params[1].seriesName + '：' + (params[1].value != undefined ? (params[1].value + ' %') : '-') + '<br />'
		           	 		    + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[2].color +'"></span>' 
		       	 		    	+ params[2].seriesName + '：' + (params[2].value != undefined ? (params[2].value + ' %') : '-') + '<br />'
			       	 		    + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[3].color +'"></span>' 
			   	 		    	+ params[3].seriesName + '：' + (params[3].value != undefined ? (params[3].value + ' %') : '-') + '<br />'
	   	 		    			+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[4].color +'"></span>' 
		 		    			+ params[4].seriesName + '：' + (params[4].value != undefined ? (params[4].value + ' %') : '-') + '<br />'
	   	 		    			+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[5].color +'"></span>' 
		 		    			+ params[5].seriesName + '：' + (params[5].value != undefined ? (params[5].value + ' %') : '-') + '<br />'
	   	 		    			+ '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[6].color +'"></span>' 
		 		    			+ params[6].seriesName + '：' + (params[6].value != undefined ? (params[6].value + ' 条') : '-');
	                 return html;
	             }
	         },
	         legend: {
		         	data:['应答时长 [0s-1s]','应答时长(1s-5s]','应答时长 (5s-30s]','应答时长(30s-60s]','应答时长[60以上]','应答超时','发送总量'],
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
		            name: '应答率占比',
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
		        },
	         ],
	         series : [
                 {
                     name:'应答时长 [0s-1s]',
                     type:'line',
                     stack: '应答率总量',
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
                             color: "#87CEFA",
                             label: {
                                 show: false,
//                                  textStyle: {
//                                      color: "#fff"
//                                  },
//                                  position: "insideTop",
//                                  formatter: function(p) {
//                                      return p.value > 0 ? (p.value) : '';
//                                  }
                             }
                         }
                     }
                 },
                 {
                     name:'应答时长(1s-5s]',
                     type:'line',
                     stack: '应答率总量',
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
                             color: "#3399FF",
                             label: {
                                 show: false,
//                                  textStyle: {
//                                      color: "#fff"
//                                  },
//                                  position: "insideTop",
//                                  formatter: function(p) {
//                                      return p.value > 0 ? (p.value) : '';
//                                  }
                             }
                         }
                     }
                 },
                 {
                     name:'应答时长 (5s-30s]',
                     type:'line',
                     stack: '应答率总量',
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
                             color: "#0080FF",
                             label: {
                                 show: false,
//                                  textStyle: {
//                                      color: "#fff"
//                                  },
//                                  position: "insideTop",
//                                  formatter: function(p) {
//                                      return p.value > 0 ? (p.value) : '';
//                                  }
                             }
                         }
                     }
                 },
                 {
                     name:'应答时长(30s-60s]',
                     type:'line',
                     stack: '应答率总量',
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
                             color: "#0066CC",
                             label: {
                                 show: false,
//                                  textStyle: {
//                                      color: "#fff"
//                                  },
//                                  position: "insideTop",
//                                  formatter: function(p) {
//                                      return p.value > 0 ? (p.value) : '';
//                                  }
                             }
                         }
                     }
                 },
                 {
                     name:'应答时长[60以上]',
                     type:'line',
                     stack: '应答率总量',
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
                             color: "#004C99",
                             label: {
                                 show: false,
//                                  textStyle: {
//                                      color: "#fff"
//                                  },
//                                  position: "insideTop",
//                                  formatter: function(p) {
//                                      return p.value > 0 ? (p.value) : '';
//                                  }
                             }
                         }
                     }
                 },
                 {
                     name:'应答超时',
                     type:'line',
                     stack: '应答率总量',
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
                             color: "#003366",
                             label: {
                                 show: false,
//                                  textStyle: {
//                                      color: "#fff"
//                                  },
//                                  position: "insideTop",
//                                  formatter: function(p) {
//                                      return p.value > 0 ? (p.value) : '';
//                                  }
                             }
                         }
                     }
                 },
                 {
                     name:'发送总量',
                     type:'line',
                     yAxisIndex: 1,
                     symbolSize:10,
                     symbol:'none',  //这句就是去掉点的  
                     smooth:true,  //这句就是让曲线变平滑的  
                     itemStyle: {
                         normal: {
                             color: "#FF7F50",
                             barBorderRadius: 0,
//                              label: {
//                                  show: false,
//                                  position: "top",
//                                  formatter: function(p) {
//                                      return p.value > 0 ? (p.value) : '';
//                                  }
//                              }
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
	
	respStackChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
}

function download(url, method){
    jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
                '<input type="text" name="channelId" value="'+$("#channelId").val()+'"/>' + // 通道ID
                '<input type="text" name="startTime" value="'+$("#startTime").val()+'"/>' + // 开始时间
                '<input type="text" name="endTime" value="'+$("#endTime").val()+'"/>' + // 结束时间
            '</form>')
    .appendTo('body').submit().remove();
};

function exportExcel(){
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	
	if(startTime == "" || endTime == ""){
		layer.alert("搜索时间不能为空");
		return;
	}
	
	if(echartData.xAxisData.length == 0){
		layer.alert("分布图中无数据展示，请确认导出数据不为空");
	}else{
		download("${ctx}/channelHistory/reportRespStack/exportExcel", "post");
	}
}

</script>
</body>
</html>