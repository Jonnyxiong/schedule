<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.chart.title}</title>
</head>

<body menuId="${data.menuId}">
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
									<u:select id="channel_id" value="${param.channel_id}" placeholder="通道号" sqlId="findAllChannel" />
								</li>
								<li>
									<u:treeSelect id="area_id" value="${data.area_id}" placeholder="地区" sqlId="area" showPname="true" />
								</li>
								<li>
									<input type="submit" value="搜索" />
								</li>
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						
						<u:highcharts highcharts="${data.chart}" id="chart" onLoad="chartOnLoad" />
						
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">
var channel_id = "${param.channel_id}";
var area_id = "${data.area_id}";
var start_timestamp = "${data.start_timestamp}";//开始的时间戳 

function chartOnLoad(){
	setInterval(function () {
		$.ajax({
			type : "post",
			url : "${ctx}/channelRealtime/ajax",
			data : {
				channel_id : channel_id,
				area_id : area_id,
				start_timestamp : start_timestamp
			},
			success : function(data) {
				if (data.dataList != null) {
					start_timestamp = data.start_timestamp;
					chartFunction.addPoint("chart", data.dataList);
				}
			}
		});
		
	}, 60000);
}

</script>
</body>
</html>