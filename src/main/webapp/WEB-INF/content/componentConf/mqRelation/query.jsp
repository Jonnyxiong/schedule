<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>SMSP组件与MQ关联配置</title>
</head>
<body menuId="243">
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
						
						<u:authority menuId="244">
							<span class="label label-info">
								<a href="javascript:;" onclick="create()">添加</a>
							</span>
						</u:authority>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/componentConf/mqRelation/query">
								<ul>
									<li>
										<u:select id="component_id_search" value="${component_id_search}" sqlId="findAllSmspComponent"/>
									</li>
									<li>
										<u:select id="message_type" data="[{value:'',text:'消息类型：所有'},{value:'00',text:'DB消息'},{value:'01',text:'移动行业'},{value:'02',text:'移动营销'},
																		{value:'03',text:'联通行业'},{value:'04',text:'联通营销'},{value:'05',text:'电信行业'},
																		{value:'06',text:'电信营销'},{value:'07',text:'行业'},{value:'08',text:'营销'},
																		{value:'11',text:'异常移动行业'},{value:'12',text:'异常移动营销'},{value:'13',text:'异常联通行业'},
																		{value:'14',text:'异常联通营销'},{value:'15',text:'异常电信行业'},{value:'16',text:'异常电信营销'},
																		{value:'20',text:'通道消息'},{value:'21',text:'上行消息'},{value:'22',text:'状态报告缓存'}]"
											value="${message_type}" showAll="false"/>
									</li>
									<li>
										<u:select id="mq_id" placeholder="MQ_ID：所有" value="${mq_id_search}" sqlId="findAllMqId"/>
									</li>
									<li>
										<u:select id="mode" data="[{value:'',text:'MQ模式：所有'},{value:'0',text:'生产者'},{value:'1',text:'消费者'}]" 
											value="${mode_search}" showAll="false"/>
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
					                 <th>SMSP组件ID(名称)</th>
					                 <th>消息类型</th>
					                 <th>MQ模式</th>
					                 <th>MQ_ID(名称)</th>
					                 <th>消息拉取速度</th>
					                 <th>描述</th>
					                 <th>更新时间</th>
					                 <u:authority menuId="244">
					                 	<th>操作</th>
					                 </u:authority>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${component_id}</td>
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
										<td><s:if test="mode==0">生产者</s:if>
											<s:elseif test="mode==1">消费者</s:elseif>
										</td>
										<td>${mq_id}</td>
										<td>${get_rate}</td>
										<td>${remark}</td>
										<td>${update_date}</td>
										<u:authority menuId="244">
											<td>
												<a href="javascript:;" onclick="edit('${id}', '${component_id}')">编辑&nbsp;&nbsp;&nbsp;</a>
												<a href="javascript:;" onclick="deleteConf('${id}', '${component_id}')">删除&nbsp;&nbsp;&nbsp;</a>
												<s:if test="mode==1">
													<a href="javascript:;" onclick="editMqGetRate('${id}', '${mq_id}', '${get_rate}')">配置速度</a>
												</s:if>
												<s:elseif test="mode==0">
												</s:elseif>
												
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
	
	<div id="editMqGetRateBox" class="container-fluid" style="display:none;">
			<div class="row">
				<div>
					<form id="editMqGetRateForm" class="form-horizontal">
						<div class="control-group">
							 <label class="control-label">MQ名称：</label>
							<div class="controls">
								<span id="mqName"></span>
<!-- 								<input id="mqName" name="mqName" type="text" /> -->
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">拉取速度：</label>
							<div class="controls">
								<input id="mqGetRate" name="mqGetRate" type="text" />
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
	
		var validate;
		$(function(){
			
			$.validator.defaults.ignore = "";
			validate = $("#editMqGetRateForm").validate({
				rules : {
					mqGetRate:{
						required : true,						
						maxlength:11,
						digits:true,
						range:[0,2147483647]
					}
				}
			});
			
		});
		
		function create(){
			location.href = "${ctx}/componentConf/mqRelation/createView";
		}
		
		// 添加/编辑
		function edit(id) {
			location.href = "${ctx}/componentConf/mqRelation/edit?id=" + id;
		}

		//删除
		function deleteConf(id, component_id) {
			if (confirm("确定要删除" + component_id + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/componentConf/mqRelation/delete",
					data : {
						id : id
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
		
		function editMqGetRate(id, mq_id, get_rate){
			$("#mqName").text(mq_id);
			$("#mqGetRate").val(get_rate);
			layer.open({
				title:"修改MQ拉取速度",
				type: 1,
				skin: 'layui-layer-rim', //样式类名
				closeBtn: 0, //不显示关闭按钮
				anim: 2,
				area: ['600px', '235px'],
				shadeClose: true, //开启遮罩关闭
				content: $("#editMqGetRateBox"),
				btn: ['保存', '取消'],
				yes: function(index, layero){// 点击“确定”按钮的回调
					if (!validate.form()) {
						return false;
					}
					
					$.ajax({
						type : "post",
						url : "${ctx}/componentConf/mqRelation/updateMqGetRate",
						data : {
							id : id,
							get_rate : $("#mqGetRate").val()
						},
						success : function(data) {
							if (data.result == null) {
								layer.msg("服务器错误，请联系管理员", { time: 2000 });
								return;
							}
							if (data.result == "success") {
								layer.msg("保存成功", { time: 2000 }, function(){
									layer.close(index);
									window.location.reload();
									}); 
							}
						}
					});
				}
			});
		}
	</script>
</body>
</html>