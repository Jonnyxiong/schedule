<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>中间件配置</title>

<style type="text/css">
	body{
		background: #F8F8F8 !important;
	}
</style>
</head>
<body menuId="235">
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
						
						<%-- <u:authority menuId="236">
							<span class="label label-info">
								<a href="javascript:;" onclick="deleteQueueOnMiddleware('${data.middlewareId}', '${data.middlewareName}')">删除队列</a>
							</span>
						</u:authority> --%>
						
						<%-- <div class="search">
						 	 <form method="post" id="mainForm" action="">
								<ul>
									<li>
										<input type="text" name="middleware_name" value="<s:property value="#parameters.middleware_name"/>" maxlength="10"
											placeholder="中间件名称" class="txt_250" />
									</li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
						  </form>
						</div> --%>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>中间件ID</th>
					                 <th>中间件名称</th>
					                 <th>MQ队列</th>
					                 <th>MQ交换</th>
					                 <th>MQ路由KEY</th>
					                 <th>消息数</th>
					                <!--  <th>消息类型</th> -->
								</tr>
							</thead>
							<tbody>
								<s:iterator value="data.queueList" status='st' var="i">
									<tr>
										<td>${st.index + 1}</td>
										<td>${data.middlewareId}</td>
										<td>${data.middlewareName}</td>
										<td>${i.queue}</td>
										<td>${i.exchange}</td>
										<td>${i.routingKey}</td>
										<td>${i.messages}</td>
										<%-- <td><s:if test="messageType eq '00'">DB消息</s:if>
										    <s:elseif test="messageType eq '01'">移动行业</s:elseif>
										    <s:elseif test="messageType eq '02'">移动营销</s:elseif>
										    <s:elseif test="messageType eq '03'">联通行业</s:elseif>
											<s:elseif test="messageType eq '04'">联通营销</s:elseif>
											<s:elseif test="messageType eq '05'">电信行业</s:elseif>
											<s:elseif test="messageType eq '06'">电信营销</s:elseif>
											<s:elseif test="messageType eq '07'">行业</s:elseif>
											<s:elseif test="messageType eq '08'">营销</s:elseif>
											<s:elseif test="messageType eq '11'">异常移动行业</s:elseif>
											<s:elseif test="messageType eq '12'">异常移动营销</s:elseif>
											<s:elseif test="messageType eq '13'">异常联通行业</s:elseif>
											<s:elseif test="messageType eq '14'">异常联通营销</s:elseif>
											<s:elseif test="messageType eq '15'">异常电信行业</s:elseif>
											<s:elseif test="messageType eq '16'">异常电信营销</s:elseif>
											<s:elseif test="messageType eq '20'">通道消息</s:elseif>
											<s:elseif test="messageType eq '21'">上行消息</s:elseif>
										</td> --%>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
<%-- 						<u:page page="${page}" formId="mainForm" /> --%>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">

		// 删除mq队列
		/* function deleteQueueOnMiddleware(middlewareId, middlewareName) {
			var text = "确定要删除" + middlewareName + "中间件上面的所有队列吗？";
			layer.confirm(text, function(index){
				var isDelete = false;
				$.ajax({
						type : "post",
						url : "${ctx}/componentConf/middleware/deleteQueueOnRabbitMQ",
						async : false,
						data : {
							middlewareId : middlewareId
						},
						success : function(data) {
							if (data.result == null) {
								alert("服务器错误，请联系管理员");
								return;
							}
							if (data.result == "success") {
								isDelete = true;
							}
						}
				  	});
					if(isDelete){
						layer.msg('删除成功', { time: 2000 }, function(){
							layer.close(index);
							location.href = "${ctx}/componentConf/viewRabbitMqQueueIframe?middlewareId=" + middlewareId + "&middlewareName=" + middlewareName;
						});
					}else{
						layer.msg('删除失败', { time: 2000 }, function(){
							layer.close(index);
						});
					}
				}); 
			
		} */

		
	</script>
</body>
</html>