<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>子账户管理</title>
</head>

<body menuId="61">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<!-- 更新账户弹出框 -->
					<div class="modal hide fade" id="updateAccountBox" tabindex="-1" role="dialog">
						<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
							<h3 id="myModalLabel"></h3>
						</div>
						<div class="modal-body text-center">
							<div class="control-group">
								备注&nbsp&nbsp&nbsp
								<textarea id="remarks" name="remarks" rows="3" cols="60"
											style="height: auto; width: auto;" maxlength="512"
											class="checkRemarks"></textarea>
								<div class="controls">
									<span id="msg" style="color:red;" style="display:none;"></span>
								</div>
							</div>
						</div>
						<div class="modal-footer">
						<a href="#" class="btn" data-dismiss="modal" onclick="closBox()">关闭</a>
						<a href="#" class="btn btn-primary" onclick="updateAccount(this)"></a>
						</div>
					</div>
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<u:authority menuId="122">
							<span class="label label-info">
									<a href="javascript:;" onclick="add()">添加账户</a>
							</span>
						</u:authority>
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/account/query">
								<ul>
									<li>
										<input type="text" name="clientId" value="<s:property value="#parameters.clientId"/>" maxlength="6"
											placeholder="用户账户" class="txt_250" />
									</li>
									<li>
										<input type="text" name="userName" value="<s:property value="#parameters.userName"/>" maxlength="50"
											placeholder="用户名称" class="txt_250" />
									</li>
									<li>
										<input type="text" name="identify" autocomplete="off" onfocus="inputControl.setNumber(this, 4, 0, false)" value="<s:property value="#parameters.identify"/>" maxlength="20" placeholder="标识" class="txt_250" />
									</li>
									<li>
					                  	<u:select id="status" defaultIndex="2" value="${statusSearch}" data="[{value:'-1',text:'状态：所有'},{value:'1',text:'开启'},{value:'5',text:'冻结'},
					                  	{value:'6',text:'注销'},{value:'7',text:'锁定'}]" showAll="false" />
									</li>
									<li><input type="submit" id="search-btn" value="搜索" /></li>
								</ul>
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>用户账号</th>
									<th>用户名称</th>
									<th>所属平台</th>
									<th>标识</th>
									<th>代理商ID</th>
									<th>认证状态</th>
									<th>短信类型</th>
									<th>短信协议类型</th>
									<th>超频扣费</th>
									<th>是否审核</th>
									<th>状态报告获取方式</th>
									<th>上行获取方式</th>
									<th>账户状态</th>
									<th>连接节点数</th>
									<th>付费类型</th>
									<th>自扩展位数</th>
									<th>用户扩展端口</th>
									<th>签名端口长度</th>
									<th>账号标签</th>
									<th>备注</th>
									<th style="width:5%;">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${clientid}</td>
										<td>${name}</td>
										<td>
											<s:if test="client_ascription==0">阿里平台</s:if>
											<s:elseif test="client_ascription==1">代理商平台</s:elseif>
											<s:elseif test="client_ascription==2">云平台</s:elseif>
										</td>
										<td>${identify}</td>
										<td>${agent_id}</td>
										<td>${oauth_status_name}</td>
										<td><u:ucparams key="${smstype}" type="smstype" /></td>
										<td><u:ucparams key="${smsfrom}" type="smsfrom" /></td>
										<td>${isoverratecharge == 1 ? "是" : "否"}</td>
										<td>${needaudit}</td>
										<td>${needreport}</td>
										<td>${needmo}</td>
										<td>
											<s:if test="status == 1">开启</s:if>
											<s:if test="status == 5">冻结</s:if>
											<s:if test="status == 6">注销</s:if>
											<s:if test="status == 7">锁定</s:if>
										</td>
										<td>${nodenum}</td>
										<td>${paytype}</td>
										<td>${extend_size}</td>
										<td>${extendport}</td>
										<td>${signportlen}</td>
										<td><u:truncate length="20" value="${client_label}"/></td>
										<td><u:truncate length="20" value="${remarks}"/></td>
										<td>
											<u:authority menuId="123">
											<span>
													<a href="javascript:;" onclick="edit('${clientid}','${smstype}')">编辑</a> 
											</span> 
											</u:authority>
											<u:authority menuId="124">
											<span ${status == 1 ? "" : "style='display:none;'"}> 
												  <br><a href="javascript:;" onclick="updateAccountBox('${clientid}', 5)">冻结</a>
											</span> 
											</u:authority>
											<u:authority menuId="124">
											<span ${status == 7 ? "" : "style='display:none;'"}> 
												  <br><a href="javascript:;" onclick="go2AccountLockInfo('${clientid}')">解锁</a> 
											</span> 
											</u:authority>
											<u:authority menuId="124">
											<span ${status == 5 ? "" : "style='display:none;'"}> 
												  <br><a href="javascript:;" onclick="updateAccountBox('${clientid}', 1)">解冻</a>
											</span>
											</u:authority>
											<u:authority menuId="126">
											<span>
												  <br><a href="javascript:;" onclick="go2UserGw('${clientid}', '${signextend}', '${smstype}')">通道组配置</a> 
											</span>
											</u:authority>
											<u:authority menuId="130">
											<s:if test="signextend == 1">
												<span>
													  <br><a href="javascript:;" onclick="go2SignPort('${clientid}', '${status}')">签名报备</a> 
												</span>
											</s:if>
											</u:authority>
											<span>
												  <br><a href="javascript:;" onclick="editModal('${clientid}', '${client_label}')">账号标签</a>
											</span>
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
	<div class="pop" style="display:none;" id="edit-modal">
		<form class="form-horizontal">
			<div class="control-group">
				<input type="hidden" id="clientId" value=""/>
				<label class="control-label">账号标签</label>
				<div class="controls">
					<textarea id="clientLabel" ></textarea>
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<a href="javascript:;" class="btn btn-success" onclick="editLabel()">确认</a>&nbsp;&nbsp;
					<a href="javascript:;" class="btn btn-default" onclick="layer.closeAll()">取消</a>
				</div>
			</div>
		</form>
	</div>

	<script type="text/javascript">

        //添加
        function editLabel(){
            var clientLabel = $("#clientLabel").val();
            /*if(!(clientLabel) || clientLabel.trim() == ""){
                layer.msg('类别名称不能为空或空字符串');
                return;
            }*/
            if(clientLabel.length > 50){
                layer.msg('客户标签不能超过50个字符');
                return;
            }
            $.ajax({
                type : "post",
                url : "${ctx}/account/updateClientLabel",
                data : {
                    clientId : $("#clientId").val(),
                    clientLabel : $("#clientLabel").val()
                },
				success: function(data){
					if(data.success==null){
						layer.msg("服务器错误，请联系管理员");
						return;
					}

					if(data.success){
						layer.msg(data.msg,{icon:1,time:800},function () {
							$("#search-btn").click();
							layer.closeAll();
						});
					}else{
						layer.msg(data.msg);
					}
				}
            });
        }
        //添加
        function editModal(clientid,clientLabel){
            $("#clientId").val(clientid);
            $("#clientLabel").val(clientLabel);
            layer.open({
                type: 1,
                skin: 'layui-layer-demo', //样式类名
                closeBtn: 1,
                anim: 2,
                title:'编辑账号标签',
                area: ['500px'],
                shadeClose: true, //开启遮罩关闭
                content: $("#edit-modal")
            });
        }
		$(function(){
			// 更新账号弹出框隐藏时事件
			$('#updateAccountBox').on('hide.bs.modal', function () {
				closBox();
			})
		});
		
		//添加
		function add() {
			location.href = "${ctx}/account/edit";
		}

		//编辑
		function edit(clientid, smstype) {
			location.href = "${ctx}/account/edit?clientid=" + clientid + "&smstype=" + smstype;
		}
		
		function go2AccountLockInfo(clientid) {
			location.href = "${ctx}/account/lockInfo/query?clientid=" + clientid + "&status=0";
		}

		//用于保存更新账号状态时数据
		var updateClientid;
		var updateStatus;
		function updateAccountBox(clientid, status){
			// 修改弹出框标题和按钮
			if(status == 5){
				$("#myModalLabel").text("冻结账户");
				$(".btn-primary").text("冻结");
			}else if(status == 1){
				$("#myModalLabel").text("开启账户");
				$(".btn-primary").text("开启");
			}
			
			// 触发弹出框显示
			$('#updateAccountBox').modal('show');
			
			updateClientid = clientid;
			updateStatus = status;
		}
		
		function updateAccount(btn) {
			$("#msg").hide();
			var remarks = '${sessionScope.LOGIN_USER_REALNAME}' + ": " + $("#remarks").val();
			
			$.ajax({
				type : "post",
				url : "${ctx}/account/updateStatus",
				data : {
					clientid : updateClientid,
					status : updateStatus,
					remarks : remarks
				},
				success : function(data) {
					if (data.result == null) {
						$("#msg").text("服务器错误，请联系管理员").show();
						return;
					}
					$("#msg").text(data.msg).show();
				}
			});
		}
		
		function closBox(){
			$("#msg").hide();
			window.location.reload();
		}
		
		// 跳转到用户通道组配置界面
		function go2UserGw(clientid, signextend, smstype){
			var url;
			if(smstype != null && smstype != ''){
				url = "${ctx}/user/edit?userid=" + clientid + "&smstype=" + smstype + "&signextend=" + signextend;
			}else{
				url = "${ctx}/user/edit?userid=" + clientid + "&signextend=" + signextend;
			}
			
			go2URL(url);
		}
		
		function go2SignPort(clientid, status){
			// 报备签名端口前需要校验子账号是否禁用、是否配置通道组路由
			$.ajax({
				type : "post",
				async: false,
				url : "${ctx}/account/isClientChannelGroupAssign",
				data : {
					userid : clientid
				},
				success : function(data) {
					if (data == null) {
						layer.alert('子账号未配置用户通道组', {icon: 0});
					}else{
						if(status == 2){
							layer.alert('子账号禁用时不能报备签名端口', {icon: 0});
						}else{
// 							location.href = "${ctx}/signport/edit?clientid=" + clientid;
							var url = "${ctx}/signport/edit?clientid=" + clientid;
							go2URL(url);
						}
					}
				}
			});
			
		}
	</script>
</body>
</html>