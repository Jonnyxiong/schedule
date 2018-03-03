<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>中间件配置</title>
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
						
						<u:authority menuId="236">
							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
						</u:authority>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/componentConf/middleware/query">
								<ul>
									<li>
										<u:select id="middleware_type" data="[{value:'-1',text:'中间件类型：所有'},{value:'0',text:'REDIS'},{value:'1',text:'MQ_C2S_IO'},{value:'2',text:'MQ_SEND_IO'},
																			{value:'3',text:'MQ_C2S_DB'},{value:'4',text:'MQ_SEND_DB'},{value:'5',text:'KAFKA'}]" 
										value="${middleware_type}" showAll="true"/>
									</li>
									<li>
										<input type="text" name="middleware_name" value="<s:property value="#parameters.middleware_name"/>" maxlength="10"
											placeholder="中间件名称" class="txt_250" />
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
									 <th>序号</th>
					                 <th>中间件ID</th>
					                 <th>中间件类型</th>
					                 <th>中间件名称</th>
					                 <th>地址</th>
					                 <th>端口</th>
					                 <th>控制台端口</th>
					                 <th>用户名</th>
					                 <th>密码</th>
					                 <th>所属机房</th>
					                 <th>更新时间</th>
					                 <u:authority menuId="236">
					                 	<th>操作</th>
					                 </u:authority>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${middleware_id}</td>
										<td><s:if test="middleware_type==0">REDIS</s:if>
										    <s:elseif test="middleware_type==1">MQ_C2S_IO</s:elseif>
										    <s:elseif test="middleware_type==2">MQ_SEND_IO</s:elseif>
										    <s:elseif test="middleware_type==3">MQ_C2S_DB</s:elseif>
											<s:elseif test="middleware_type==4">MQ_SEND_DB</s:elseif>
											<s:elseif test="middleware_type==5">KAFKA</s:elseif>
										</td>
										<td>${middleware_name}</td>
										<td>${host_ip}</td>
										<td>${port}</td>
										<td>${console_port}</td>
										<td>${user_name}</td>
										<td>${pass_word}</td>
										<td><s:if test="node_id==10">北京亦庄</s:if>
										    <s:elseif test="node_id==11">北京兆维</s:elseif>
										    <s:elseif test="node_id==12">北京互联港湾</s:elseif>
										</td>
										<td>${update_date}</td>
										<u:authority menuId="236">
											<td>
												<a href="javascript:;" onclick="edit('${middleware_id}')">编辑&nbsp;&nbsp;&nbsp;</a>
												<a href="javascript:;" onclick="deleteConf('${middleware_id}', '${middleware_name}', '${mqConfigNum}')">删除&nbsp;&nbsp;&nbsp;</a>
												<s:if test="middleware_type eq 1 or middleware_type eq 2 or middleware_type eq 3 or middleware_type eq 4">
													<s:if test="mqConfigNum gt 0">
														<a href="javascript:;" onclick="viewRabbitMqQueueIframe('${middleware_id}', '${middleware_name}')">查看MQ队列</a>
													</s:if>
													<s:else>
														<a href="javascript:;" onclick="createRabbitMqQueueIframe('${middleware_id}', '${middleware_name}')">创建MQ队列</a>
													</s:else>
												</s:if>
											</td>
										</u:authority>
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

	<script type="text/javascript">

		// 添加/编辑
		function edit(middleware_id) {
			if(null == middleware_id){
				location.href = "${ctx}/componentConf/middleware/edit";
			}else{
				location.href = "${ctx}/componentConf/middleware/edit?middleware_id=" + middleware_id;
			}
		}

		// 删除
		function deleteConf(middleware_id, middleware_name, mqConfigNum) {
			if(Number(mqConfigNum) > 0){
				layer.msg(middleware_name + ' MQ上目前有' + mqConfigNum + "个队列，不能直接删除", { time: 2000 });
			}else{
				if (confirm("确定要删除" + middleware_id + "吗？")) {
					$.ajax({
						type : "post",
						url : "${ctx}/componentConf/middleware/delete",
						data : {
							middleware_id : middleware_id
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
			
		}
		
		// 查看rabbitMq队列弹出框
		function viewRabbitMqQueueIframe(middleware_id, middleware_name){
			var titleName = middleware_name + "队列详情";
			layer.open({
	    		  type: 2,
	    		  title: titleName,
	    		  shadeClose: true,
	    		  maxmin: true,
	    		  shade: 0.8,
	    		  area: ['980px', '100%'],
	    		  btn: ['删除队列','关闭'],
	    		  content: "${ctx}/componentConf/viewRabbitMqQueueIframe?middlewareId=" + middleware_id + "&middlewareName=" + middleware_name, //iframe的url
	    		  yes: function(index, layero){// 点击“确定”按钮的回调
	    			    var iframeIndex = index;
	    			    var text = "确定要删除" + middleware_name + "中间件上面的所有队列吗？";
	    				layer.confirm(text, function(index){
	    					var isDelete = false;
	    					$.ajax({
	    							type : "post",
	    							url : "${ctx}/componentConf/middleware/deleteQueueOnRabbitMQ",
	    							async : false,
	    							data : {
	    								middlewareId : middleware_id
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
	    								layer.close(iframeIndex);
	    								layer.close(index);
	    								
	    								viewRabbitMqQueueIframe(middleware_id, middleware_name);
	    							});
	    						}else{
	    							layer.msg('删除失败', { time: 2000 }, function(){
	    								layer.close(index);
	    							});
	    						}
	    					}); 
				  },
				  end: function(){
					  window.location.reload();
				  }
	    	});
		}
		
		function createRabbitMqQueueIframe(middleware_id, middleware_name){
			
			layer.open({
	    		  type: 2,
	    		  title: '创建RabbitMQ队列',
	    		  shadeClose: true,
	    		  maxmin: true,
	    		  shade: 0.8,
	    		  area: ['980px', '100%'],
	    		  btn: ['创建队列','关闭'],
	    		  content: "${ctx}/componentConf/createRabbitMqQueueIframe?middlewareId=" + middleware_id + "&middlewareName=" + middleware_name, //iframe的url
	    		  yes: function(index, layero){// 点击“创建”按钮的回调
	    			    var iframeIndex = index;
	    			  	var text = "确定要在" + middleware_name + "中间件上面创建这些队列吗？";
	    				layer.confirm(text, function(index){
	    					var isCreate = false;
	    					$.ajax({
	    							type : "post",
	    							url : "${ctx}/componentConf/middleware/createQueueOnMiddleware",
	    							async : false,
	    							data : {
	    								middlewareId : middleware_id
	    							},
	    							success : function(data) {
	    								if (data.result == null) {
	    									alert("服务器错误，请联系管理员");
	    									return;
	    								}
	    								if (data.result == "success") {
	    									isCreate = true;
	    								}
	    							}
	    					  	});
	    						if(isCreate){
	    							layer.msg('创建成功', { time: 2000 }, function(){
	    								layer.close(iframeIndex);
	    								layer.close(index);
	    								
	    								viewRabbitMqQueueIframe(middleware_id, middleware_name);
	    							});
	    						}else{
	    							layer.msg('创建失败', { time: 2000 }, function(){
	    								layer.close(index);
	    							});
	    						}
	    					}); 
				  },
				  end: function(){
// 					  window.location.reload();
				  }
	    	});
		}
	</script>
</body>
</html>