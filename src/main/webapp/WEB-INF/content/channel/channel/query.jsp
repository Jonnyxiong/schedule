<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title></title>
</head>
<body menuId="16">
	<!--Action boxes-->
	<div class="container-fluid">
<!-- 		<hr> -->
		
		<nav class="navbar navbar-success" role="navigation">
			<ul class="nav nav-tabs nav-justified" role="tablist">
				<li class="active"><a href="${ctx}/channel/query">通道管理</a></li>
				<li><a href="${ctx}/channel/queryHistory">通道历史管理</a></li>
			</ul>
		</nav>
		
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<u:authority menuId="107">
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加短信通道</a>
						</span>
						</u:authority>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channel/query">
							<ul>
								<li>
                  					<u:select id="state" data="[{value:'-1',text:'状态:所有'},{value:'0',text:'状态:关闭'},{value:'1',text:'状态:开启'},{value:'2',text:'状态:暂停'}]" 
                  					value="${search_state}" showAll="false" />
								</li>
								<li>
									<input type="text" name="identify" autocomplete="off" onfocus="inputControl.setNumber(this, 4, 0, false)" value="<s:property value="#parameters.identify"/>" maxlength="10"
										placeholder="标识" class="txt_250" />
								</li>
								<li>
									<input type="text" name="sendid" autocomplete="off" onfocus="inputControl.setNumber(this, 11, 0, false)" value="<s:property value="#parameters.sendid"/>" maxlength="11"
										placeholder="SMSP_SEND编号" class="txt_250" />
								</li>
								<li>
									<input type="text" name="cid" value="<s:property value="#parameters.cid"/>" maxlength="10"
										placeholder="通道号" class="txt_250" />
								</li>
								<li>
									<input type="text" name="remark" value="<s:property value="#parameters.remark"/>" maxlength="10"
										placeholder="备注" class="txt_250" />
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
					                 <th>通道号</th>
					                 <th>名称</th>
					                 <th>通道账号</th>
					                 <th>标识</th>
					                 <th>运营商</th>
					                 <th>SEND编号</th>
					                 <th>状态</th>
					                 <th>发送速度</th>
					                 <th>路由速度</th>
					                 <th>滑窗窗口</th>
					                 <th>类型</th>
					                 <th>扩展位数</th>
					                 <th>创建时间</th>
					                 <th>发送时间区间</th>
					                 <th>MQ_ID</th>
					                 <th>号段类型</th>
					                 <th>路由省网</th>
					                 <th>登陆状态</th>
					                 <th>登陆描述</th>
					                 <th>请求方式</th>
					                 <th>归属商务</th>
					                 <th>上行前缀</th>
					                 <th>备注</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
									<td>${rownum}</td>
									<td>${cid}</td>
									<td>${channelname}</td>
									<td>${clientid}</td>
									<td>${identify}</td>
									<td><u:ucparams key="${operatorstype}" type="channel_operatorstype"/> </td>
									<td>${sendid}</td>
									<td>
										<s:if test="state==0">关闭</s:if>
										<s:elseif test="state==1">开启</s:elseif>
										<s:elseif test="state==2">暂停</s:elseif>
									</td>
									<td>${speed}</td>
									<td>${access_speed}</td>
									<td>${sliderwindow}</td>
									<td>
										<s:if test="channeltype==0">自签平台用户端口</s:if>
										<s:elseif test="channeltype==1">固签无自扩展</s:elseif>
										<s:elseif test="channeltype==2">固签有自扩展</s:elseif>
										<s:elseif test="channeltype==3">自签通道用户端口</s:elseif>
									</td>
									<td>${extendsize}</td>
									<td><u:truncate length="10" value="${createtime}"/></td>
									<td>${sendtimearea}</td>
									<td><u:truncate length="10" value="${mq_id_name}"/></td>
									<td>
										<s:if test="segcode_type==0">全国</s:if>
										<s:elseif test="segcode_type==1">省网</s:elseif>
									</td>
									<td>${segName}</td>
									<td>
										<s:if test="login_status==0">未登录</s:if>
										<s:elseif test="login_status==1">登录成功</s:elseif>
										<s:elseif test="login_status==2">登录失败</s:elseif>
									</td>
									<td>${login_desc}</td>
									<td><u:ucparams key="${httpmode}" type="channel_http_mode"/></td>
									<td>${belong_business_name}</td>
									<%--<td>${moprefix}</td>--%>
									<td><u:truncate length="5" value="${moprefix}"/></td>
									<%--<td><u:truncate length="5" value="${remark}"/></td>--%>
									<td>${remark}</td>
									<td>
							      	<u:authority menuId="109">
							      		<a href="javascript:;" onclick="edit('${id}')">编辑</a>
							      	</u:authority>
							      	<br>
							      	<u:authority menuId="108">
							      		<!-- 0：关闭，1：开启，2：暂停，3：下架 -->
								      	<s:if test="state==0">
								     	 	<a href="javascript:;" onclick="updateStatusConfirm(this, '${id}', '${cid}', '${state}', 1)">开启</a> <br>
								     	 	<a href="javascript:;" onclick="updateStatusConfirm(this, '${id}', '${cid}', '${state}', 3)">下架</a> 
								      	</s:if>
								      	<s:if test="state==1">
								     	 <a href="javascript:;" onclick="updateStatusConfirm(this, '${id}', '${cid}', '${state}', 0)">关闭</a> <br>
								     	 <a href="javascript:;" onclick="updateStatusConfirm(this, '${id}', '${cid}', '${state}', 2)">暂停</a> 
								      	</s:if>
								      	<s:if test="state==2">
								     	 <a href="javascript:;" onclick="updateStatusConfirm(this, '${id}', '${cid}', '${state}', 1)">开启</a> <br>
								     	 <a href="javascript:;" onclick="updateStatusConfirm(this, '${id}', '${cid}', '${state}', 0)">关闭</a> 
								      	</s:if>
								      	<br>
							      	</u:authority>
							      	
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

	<script type="text/javascript">
// 		$(function(){
// 			var max_identify = "${max_identify}";
// 			var identify = "${flag_identify}";
// 			var opt = '<option>0</option>';
// 			for (var i = 1;i <= max_identify;i++){
// 				opt += "<option value='"+i+"'>"+i+"</otpion>";
// 			}
// 			$("#identify").html(opt);
// 			debugger
// 			$("#identify option[value = "+identify+"]").attr("selected","selected");
			
			
// 		});
	
		//添加
		function add() {
			location.href = "${ctx}/channel/edit";
		}

		//编辑
		function edit(id) {
			location.href = "${ctx}/channel/edit?id=" + id;
		}

		//修改状态前的确认弹出框
		function updateStatusConfirm(a, id, channelId, currentState, switch2State) {

			var confirmText = "确定要" + $(a).text() + "通道" + channelId +"吗？";
			layer.confirm(
				confirmText,
				{btn : ['确定', '取消']},
				function(index){
					// 下架
					if(switch2State == '3'){
						// 下架前确认通道组关系和强制路由关系
						$.ajax({
							type : "post",
							url : "${ctx}/channel/queryOffLineCheckInfo",
							data : {
								channelId : channelId
							},
							async : false,
							success : function(data) {
								if (data == null && data.channelGroupNum == null && data.segmentChannelNum == null) {
									layer.alert("服务器错误，请联系管理员");
									return;
								}
								
								// channelGroupNum 是指包含当前通道的通道组的数量
								if(data.channelGroupNum > 0){
									var text = "共有" + data.channelGroupNum + "个通道组中包含当前通道，继续下架会解绑通道组与该通道的绑定关系"
									layer.confirm(
										text, 
										{btn : ['继续', '取消']},
										function(index){
											layer.close(index);
											
											// segmentChannelNum 是指当前通道配置的强制路由规则数量
											if(data.segmentChannelNum > 0){
												var text = "强制路由通道管理中配置了" + data.segmentChannelNum + "条该通道的配置，继续下架会删除这些配置"
												layer.confirm(
													text, 
													{btn : ['继续', '取消']},
													function(index){
														layer.close(index);
														updateStatus(a, id, channelId, currentState, switch2State);
													}, function(index){
														layer.close(index);
												});
											}else{
												updateStatus(a, id, channelId, currentState, switch2State);
											}
											
										}, function(index){
											layer.close(index);
									});
								}else{
									updateStatus(a, id, channelId, currentState, switch2State);
									return;
								}
								
							}
						});
					}else{
						// 0：关闭，1：开启，2：暂停
						updateStatus(a, id, channelId, currentState, switch2State);
					}
					
					layer.close(index);
			   });
		}
		
		function updateStatus(a, id, channelId, currentState, switch2State){
			var index = layer.load(1, {
			    shade: [0.5,'#fff'] //0.5透明度的白色背景
			});
			
			$.ajax({
				type : "post",
				url : "${ctx}/channel/updateStatus",
				data : {
					id : id,
					channelId : channelId,
					currentState : currentState,
					switch2State : switch2State
				},
				success : function(data) {
					layer.close(index);
					if (data.result == null) {
						layer.alert("服务器错误，请联系管理员");
						return;
					}
					
					layer.alert(data.msg, {time:5000, end:function(){
						if (data.result == "success") {
							window.location.reload();
						}
					}});
				}
			});
		}
		
	</script>
</body>
</html>