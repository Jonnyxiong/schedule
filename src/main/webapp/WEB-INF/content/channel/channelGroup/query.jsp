<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>通道组管理</title>
</head>
<body menuId="76">
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
						<u:authority menuId="111"><span class="label label-info"> <a href="javascript:;" onclick="add()">添加通道组</a> </span></u:authority>
						<%-- <u:authority menuId="110"><span class="label label-info"> <a href="javascript:;" onclick="switchChannel()">全组切换通道</a> </span></u:authority> --%>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelGroup/query">
							<ul>
								<li>
									<input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40"
									placeholder="通道组ID/通道组名称" class="txt_250" />
								</li>
								<li>
									<input type="text" name="channelid" value="<s:property value="#parameters.channelid"/>" maxlength="40"
									placeholder="组内通道" class="txt_250" />
								</li>
								<li>
									<input type="submit" value="搜索" />
								</li>
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>通道组ID</th>
					                 <th>通道组名称</th>
					                 <th>通道组类型</th>
					                 <th>运营商类型</th>
					                 <th>组内通道</th>
									 <th>通道权重</th>
					                 <th>备注</th>
					                 <th>更新时间</th>
					                 <th>状态</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
									<td>${rownum}</td>
									<td>${channelgroupid}</td>
									<td><c:out value="${channelgroupname}"/></td>
									<td>
										<s:if test="flag eq 0">云通道对应通道组</s:if>
										<s:elseif test="flag eq 1">普通通道组</s:elseif>
										<s:elseif test="flag eq 2">自动路由通道池</s:elseif>
									</td>
									<td><u:ucparams key="${operater}" type="channel_operatorstype"/></td>
									<td>${channelid}</td>
									<td>${weight}</td>
									<td><c:out value="${remarks}" /></td>
									<td>${update_date}</td>
									<td>${status==1?'开启':'关闭'}</td>
								    <td>
								      	<u:authority menuId="113">
								      	<s:if test="status==1">
								     	 	<a href="javascript:;" onclick="updateStatus(this, '${channelgroupid}', 2)">关闭</a> 
								      	</s:if>
								      	<s:if test="status==2">
								     	 <a href="javascript:;" onclick="updateStatus(this, '${channelgroupid}', 1)">开启</a> 
								      	</s:if>
								      	</u:authority>
								      	<!-- 只有普通通道组可以进行标记和删除 -->
								      	<s:if test="flag != 0">
									      	<u:authority menuId="112">
									      	| <a href="javascript:;" onclick="edit('${channelgroupid}')">编辑</a>
									      	</u:authority>
									      	<u:authority menuId="114">
									      	| <a href="javascript:;" onclick="del(this, '${channelgroupid}')">删除</a>
									   		</u:authority>
								      	</s:if>
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
	
		//添加
		function add() {
			location.href = "${ctx}/channelGroup/edit";
		}

		//编辑
		function edit(channelgroupid) {
			location.href = "${ctx}/channelGroup/edit?channelgroupid=" + channelgroupid;
		}
		
		function del(a, channelgroupid) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/channelGroup/delete",
					data : {
						channelgroupid : channelgroupid
					},
					success : function(data) {
						if (data == null) {
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

		//修改状态：恢复、删除
		function updateStatus(a, channelgroupid, status) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/channelGroup/updateStatus",
					data : {
						channelgroupid : channelgroupid,
						status : status
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
		
		function switchChannel(){
			layer.prompt({
				  title: '请输入需要被切换的通道号，并确认',
				  formType: 0 //0 文本 1密码 2多行文本
				}, function(channelid){
				  layer.prompt({title: '请输入用于替换"'+ channelid +'"的通道号，并确认', formType: 0}, function(switchChannelid){
					  	console.log(channelid);
				  		console.log(switchChannelid);
				  		$.ajax({
							type : "post",
							url : "${ctx}/channelGroup/switchChannel",
							data : {
								channelid : channelid,
								switchChannelid : switchChannelid
							},
							success : function(data) {
								if (data != null) {
									layer.alert(data.msg, {icon: 1, title:'提示'}, function(index){
										window.location.reload();
									});
								}
							}
						});
				  });
				});

			
			
			
		}
	</script>
</body>
</html>