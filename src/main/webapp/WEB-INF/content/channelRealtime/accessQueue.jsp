<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


<html>
<head>
	<title>${data.chart.title}</title>
	<script src="${ctx}/js/echart/echarts.min.js"></script>
</head>

<body menuId="216">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
			<ul class="nav nav-tabs nav-justified" role="tablist">
				<li class="active"><a href="${ctx}/channelRealtime/accessQueue/page">MQ缓存数量监控</a></li>
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
var GET_ACCESS_QUEUE_URL = "${ctx}/channelRealtime/accessQueue/getAccessQueueInfo";
var INTERVAL_TIME = 10 * 1000; // 10秒

$(function(){
	var option = {
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
		    },
		    legend: {
		        orient: 'horizontal',
		        x: 'center',
		        y: 'bottom',
		        data:['移动行业','移动营销','电信行业','电信营销','联通行业','联通营销','空闲缓存']
		    },
		    series: [
		        {
		            name:'缓存类型',
		            type:'pie',
		            radius: ['50%', '70%'],
		            avoidLabelOverlap: true,
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b}: {c} ({d}%)"
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

	setPieData(myChart, GET_ACCESS_QUEUE_URL);
	window.clearInterval(intervalObj);
	intervalObj = setInterval(function () {
		setPieData(myChart, GET_ACCESS_QUEUE_URL);
	}, INTERVAL_TIME);
	
	// 默认不显示“承诺速率”
	myChart.dispatchAction({
		type: 'legendUnSelect',
	    name: '空闲缓存'
	});
});


//设置Echart饼图数据
function setPieData(myChart, ajaxUrl){
	$.ajax({
		type : "post",
		url : ajaxUrl,
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

</script>
</body>
</html>