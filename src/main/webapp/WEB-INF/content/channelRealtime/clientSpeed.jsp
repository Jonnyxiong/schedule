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
		.clear{ clear:both}
		.select2-selection__rendered li {line-height:20px !important;}
		.select2-selection--multiple{overflow:hidden;width:680px;}
	</style>
	<script src="${ctx}/js/echart/echarts.min.js"></script>
</head>

<body menuId="215">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
			<ul class="nav nav-tabs nav-justified" role="tablist">
				<li class="active"><a href="${ctx}/channelRealtime/clientSpeed/page">用户发送速率统计</a></li>
				<li><a href="${ctx}/channelRealtime/clientQuality/page">回执率和应答率统计</a></li>
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
							<ul>
								<li style="overflow:hidden;width:680px;">
									<u:select id="client_id" value="" placeholder="短信账号" sqlId="findRunningClientId" />
								</li>
								<!-- <li>
									<input type="submit" value="搜索" />
								</li> -->
							</ul>
						</div>
					</div>
					
					<div class="col-sm-6" style="position:relative;">
						<div class="widget-content nopadding">
							
							<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	    					<div id="myChart" style="min-width: 600px;min-height:600px;"></div>
	    					 
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
var SIGLE_CLIENT_SPEED_URL = "${ctx}/channelRealtime/sigleClient/seed-speed";
var ALL_CLIENT_SPEED_URL = "${ctx}/channelRealtime/allClient/seed-speed";
var INTERVAL_TIME = 60 * 1000; // 60秒

var myChart = echarts.init(document.getElementById('myChart'));

var series_data 	 = [],			//series配置数组 
	clientName_map 	 = {}, 			//判断是否加载过clientName
	echart_init_flag = false,		//是否加载过所有，初始化加载所有
	legend_data		 = ['承诺速率'],
	value_arr			 = [];
	name_arr		 = [];
$(function(){
	$("#timer").stopWatch();
	
	myChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
	var option = {
            title: {
                x: 'center'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
            	
                bottom: 10,
                left: 'center',
                icon: 'circle',
            },
//             color:['#2f4554','#c23531'],
           // color:['#28b779','#c23531'],
            toolbox: {
                show: false,
                feature: {
                    saveAsImage: { show: true }
                }
            },
            calculable: true,
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
                },
            	data: [] // 此处即使没有数据也一定要设置为空不然Echart会报错
            },
            yAxis : [
                {
                    type : 'value',
                    name : '发送速率'
                }
            ],
            series : [] 
        };
	
	myChart.setOption(option);
	
	// 默认不显示“承诺速率”
	myChart.dispatchAction({
		type: 'legendUnSelect',
	    name: '承诺速率'
	});
	
	// 渲染“短信账号”下拉框为select2
	$("#client_id").select2({
		multiple : true
	});
	// select2选中赋值逻辑
	$('#client_id').on('select2:select', function (evt) {
		
		var clientName = evt.params.data.text;
		var clientId   = evt.params.data.id;
		
		echartDraw(clientId, clientName);
	});
	
	// select2取消选中赋值逻辑
	$('#client_id').on('select2:unselect', function (evt) {
	
	});	
	echartDraw();
});

function setEchartData(myChart, clientId, ajaxUrl, clientName){
	$.ajax({
		type : "post",
		url : ajaxUrl,
		data : {
			clientId : clientId
		},
		success : function(data) {
			myChart.hideLoading();
			if (!data) {return;}
			//如果加载过clientName
			
			if(clientName_map[clientName]){return;}
			//记录clientName
			clientName_map[clientName] = true;
			if(clientId === '' || clientId === undefined){	
				series_data.push({
                    name:'所有',
                    type:'line',
                    showSymbol: false,
                    smooth:true, 
                    data: data.actualSeries
                },{
                    name:'承诺速率',
                    type:'line',
                    smooth:true, 
                    showSymbol: false,
                    data: data.promiseSeries
                })
                legend_data.push('所有');
				
				myChart.setOption({
					legend:{
						data :legend_data
					},
		 	        series: series_data
		 	    });
			} else {
				series_data.push({
                    name:clientName,
                    type:'line',
                    showSymbol: false,
                    smooth:true, 
                    data: data.actualSeries
                })
                legend_data.push(clientName);
				myChart.setOption({
					legend:{
						data :legend_data
					},
		 	        series: series_data
		 	    });
			}
			
		}
	});
};


function echartDraw(clientId, clientName){
	
	 if(clientId == "" || clientId == undefined){
		window.clearInterval(intervalObj);
		setEchartData(myChart, clientId, ALL_CLIENT_SPEED_URL, clientName);
        intervalObj = setInterval(function () {
             setEchartData(myChart, clientId, ALL_CLIENT_SPEED_URL, clientName);
        }, INTERVAL_TIME);
	}else{
		window.clearInterval(intervalObj);
      	setEchartData(myChart, clientId, SIGLE_CLIENT_SPEED_URL, clientName);
        intervalObj = setInterval(function () {
              setEchartData(myChart, clientId, SIGLE_CLIENT_SPEED_URL, clientName);
        }, INTERVAL_TIME);
	}

};

// 客户下拉框选择事件，页面初始化的时候会执行一次
function clientOnChange(value, text, isInit){
	
};

function normalize(arr){
　　　　if(arr && Array.isArray(arr)){
　　　　　　var i, len, map = {};
　　　　　　for(i = arr.length; i >= 0; --i){
　　　　　　　　if(arr[i] in map){
　　　　　　　　　　arr.splice(i, 1);
　　　　　　　　} else {
　　　　　　　　　　map[arr[i]] = true;
　　　　　　　　}
　　　　　　}
　　　　}
　　　　return arr;
　　}  
</script>
</body>
</html>