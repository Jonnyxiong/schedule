<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>MQ配置</title>
</head>
<body menuId="241">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		
		<nav class="navbar navbar-success" role="navigation">
			<ul class="nav nav-tabs nav-justified" role="tablist">
				<li class="active"><a href="${ctx}/componentConf/mqConf/query?middleware_type=2">MQ_SEND_IO</a></li>
				<li><a href="${ctx}/componentConf/mqConf/query?middleware_type=1">MQ_C2S_IO</a></li>
				<li><a href="${ctx}/componentConf/mqConf/query?middleware_type=4">MQ_SEND_DB</a></li>
				<li><a href="${ctx}/componentConf/mqConf/query?middleware_type=3">MQ_C2S_DB</a></li>
			</ul>
		</nav>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						
						<u:authority menuId="242">
							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
						</u:authority>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/componentConf/mqConf/query">
								<ul>
									<li>
										<input type="hidden" name="middleware_type" value="2">
									</li>
									<li>
										<input type="text" name="queue_name" value="<s:property value="#parameters.queue_name"/>" maxlength="100"
											placeholder="MQ名称" class="txt_250" />
									</li>
									<li>
										<input type="text" name="remark" value="<s:property value="#parameters.remark"/>" maxlength="10"
											placeholder="描述" class="txt_250" />
									</li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>MQ_ID</th>
					                 <th>MQ名称</th>
					                 <th>MQ中间件ID</th>
					                 <th>MQ中间件名称</th>
					                 <th>MQ队列</th>
					                 <th>MQ交换</th>
					                 <th>MQ路由KEY</th>
					                 <th>消息类型</th>
<!-- 					                 <th>消息数</th> -->
					                 <th>描述</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${mq_id}</td>
										<td>${queue_name}</td>
										<td>${middleware_id}</td>
										<td>${middleware_name}</td>
										<td>${mq_queue}</td>
										<td>${mq_exchange}</td>
										<td>${mq_routingkey}</td>
										<td><s:if test="message_type eq '00'">DB消息</s:if>
										    <s:elseif test="message_type eq '01'">移动行业</s:elseif>
										    <s:elseif test="message_type eq '02'">移动营销</s:elseif>
										    <s:elseif test="message_type eq '03'">联通行业</s:elseif>
											<s:elseif test="message_type eq '04'">联通营销</s:elseif>
											<s:elseif test="message_type eq '05'">电信行业</s:elseif>
											<s:elseif test="message_type eq '06'">电信营销</s:elseif>
											<s:elseif test="message_type eq '07'">行业</s:elseif>
											<s:elseif test="message_type eq '08'">营销</s:elseif>
											<s:elseif test="message_type eq '11'">异常移动行业</s:elseif>
											<s:elseif test="message_type eq '12'">异常移动营销</s:elseif>
											<s:elseif test="message_type eq '13'">异常联通行业</s:elseif>
											<s:elseif test="message_type eq '14'">异常联通营销</s:elseif>
											<s:elseif test="message_type eq '15'">异常电信行业</s:elseif>
											<s:elseif test="message_type eq '16'">异常电信营销</s:elseif>
											<s:elseif test="message_type eq '20'">通道消息</s:elseif>
											<s:elseif test="message_type eq '21'">上行消息</s:elseif>
											<s:elseif test="message_type eq '22'">状态报告缓存</s:elseif>
										</td>
<%-- 										<td>${message_count}</td> --%>
										<td>${remark}</td>
										<td>${update_date}</td>
										<td>
<%-- 												<a href="javascript:;" onclick="edit('${mq_id}')">编辑</a> --%>

											<!-- 通道队列不能在当前页面删除，只能在通道下架功能中删除 -->
											<%-- <u:authority menuId="257">
												<a href="javascript:;" onclick="deleteConf('${queue_name}', '${mq_id}')">删除</a>
											</u:authority> --%>
												<a href="javascript:;" onclick="queryMessageCount('${middleware_id}', '${mq_queue}')">查看消息数</a>
										</td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
						<u:page page="${page}" formId="mainForm" />
				</div>
			</div>
		</div>
	</div>

	<div id="queueMessageCountBox" class="container-fluid" style="display:none;">
			<div class="row">
				<div>
					<form id="queueMessageCountForm" class="form-horizontal">
						<div class="control-group">
							 <label class="control-label">队列名称：</label>
							<div class="controls">
								<span id="queueName"></span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">消息数：</label>
							<div class="controls">
								<span id="queueMessageCount"></span>
							</div>
						</div>
						
						<div class="control-group">
			                <label class="control-label">&nbsp;</label>
			                <div class="controls">
			                  <span id="msg" class="error" style="display:none;"></span>
			                </div>
			            </div>
					</form>
				</div>
			</div>
	</div>
	
	<script type="text/javascript">
	
		// 添加/编辑
		function edit(mq_id) {
			if(null == mq_id){
				location.href = "${ctx}/componentConf/mqConf/edit";
			}else{
				location.href = "${ctx}/componentConf/mqConf/edit?mq_id=" + mq_id;
			}
		}

		//删除
		function deleteConf(queue_name, mq_id) {
			if (confirm("确定要删除" + queue_name + "队列吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/componentConf/mqConf/delete",
					data : {
						mq_id : mq_id
					},
					success : function(data) {
						if (data.result == null) {
							alert("服务器错误，请联系管理员");
							return;
						}
						alert(data.msg);
						if (data.result == "success") {
							window.location.reload();
						}
					}
				});
			}
		}
		
		function queryMessageCount(middleware_id, mq_queue){
			$.ajax({
				type : "post",
				url : "${ctx}/componentConf/mqConf/queryQueueMessageCount",
				data : {
					middleware_id : middleware_id,
					mq_queue : mq_queue
				},
				success : function(data) {
					if (data.result == null) {
						layer.msg("服务器错误，请联系管理员");
						return;
					}
					if (data.result == "success") {
						$("#queueName").text(mq_queue);
						$("#queueMessageCount").text(data.queueMessageCount);
						layer.open({
							title:"查询队列消息数",
							type: 1,
							skin: 'layui-layer-rim', //样式类名
							closeBtn: 0, //不显示关闭按钮
							anim: 2,
							area: ['400px', '235px'],
							shadeClose: true, //开启遮罩关闭
							content: $("#queueMessageCountBox"),
							btn: ['关闭'],
							yes: function(index, layero){
								layer.close(index);
							}
						});
					}
				}
			});
		}
	</script>
</body>
</html>