<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>号码/号段强制路由通道</title>
<style type="text/css">
	.width-initial{
		width: initial !important;
	}
</style>
</head>
<body menuId="249">
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
						
						<u:authority menuId="250">
							<!-- <span class="label label-info">
								<a href="javascript:;" onclick="importAreaPhoneSection()">导入地区号段</a>
							</span>
							<span class="label label-info">
								<a href="javascript:;" onclick="openSwitchAreaChannelBox()">地区路由切换</a>
							</span> -->
							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
						</u:authority>
						
						<div class="search width-initial">
						 	 <form method="post" id="mainForm" action="${ctx}/sysConf/segmentChannel/query">
								<ul>
									<li>
										<input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="21"
											placeholder="手机号码/客户代码" class="txt_250" />
									</li>
									<li>
										<input type="text" name="channel_id" value="<s:property value="#parameters.channel_id"/>" maxlength="11"
											placeholder="通道组号" class="txt_250" />
									</li>
									<li>
					                  	<u:select id="area_id" placeholder="地区：所有" value="${search_area_id}" sqlId="findAllSegCode"/>
									</li>
									<li>
					                  	<u:select id="status" value="${search_status}" data="[{value:'',text:'状态：所有'},{value:'1',text:'开启'},{value:'0',text:'关闭'}]" showAll="false" />
									</li>
									<li>
										<u:select id="send_type" value="${search_send_type}" data="[{value:'',text:'发送类型：所有'},{value:'0',text:'行业'},{value:'1',text:'营销'}]" showAll="false" />
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
					                 <th>强制路由类型</th>
					                 <th>地区</th>
					                 <th>手机号码/号段</th>
					                 <th>通道组号</th>
					                 <th>通道运营商类型</th>
					                 <th>类型</th>
									 <th>发送类型</th>
					                 <th>客户代码</th>
					                 <th>状态</th>
					                 <th>备注</th>
					                 <th>更新时间</th>
					                 <u:authority menuId="250">
					                 	<th>操作</th>
					                 </u:authority>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>
											<s:if test="route_type eq 0">国内手机号码路由</s:if>
											<s:if test="route_type eq 1">国内手机号段路由</s:if>
											<s:if test="route_type eq 2">国外手机号段路由</s:if>
										</td>
										<td>
											<c:if test="${empty area_name}"> - </c:if>
											<c:if test="${not empty area_name}">${area_name}</c:if>
										</td>
										<td>
											<c:if test="${empty phone_segment}">${area_name}地区号段</c:if>
											<c:if test="${not empty phone_segment}">${phone_segment}</c:if>
										</td>
										<td>${channel_id}</td>
										<td>
											<s:if test="operatorstype eq 0">全网</s:if>
											<s:if test="operatorstype eq 1">移动</s:if>
											<s:if test="operatorstype eq 2">联通</s:if>
											<s:if test="operatorstype eq 3">电信</s:if>
											<s:if test="operatorstype eq 4">国际</s:if>
										</td>
										<td>
											<c:if test="${empty type}">所有客户路由</c:if>
											<c:if test="${type eq 0}">包含的用户路由</c:if>
											<c:if test="${type eq 1}">包含的用户不路由</c:if>
										</td>
										<td>
											<c:if test="${empty send_type}">-</c:if>
											<c:if test="${send_type eq 0}">行业</c:if>
											<c:if test="${send_type eq 1}">营销</c:if>
										</td>
										<td>
											<c:if test="${empty client_code}"> - </c:if>
											<c:if test="${not empty client_code}">${client_code}</c:if>
										</td>
										<td>
											<s:if test="status==0">关闭</s:if>
											<s:elseif test="status==1">开启</s:elseif>
										</td>
										<td>${remarks}</td>
										<td>${update_time}</td>
										<u:authority menuId="250">
											<td>
												<a href="javascript:;" onclick="edit('${id}')">编辑&nbsp;&nbsp;&nbsp;</a>
												<a href="javascript:;" onclick="deleteConf('${id}', '${channel_id}', '${client_code}')">删除&nbsp;&nbsp;&nbsp;</a>
												<s:if test="status==0">
													<a href="javascript:;" onclick="updateStatus('${id}', 1)">开启&nbsp;&nbsp;&nbsp;</a>
												</s:if>
												<s:else>
													<a href="javascript:;" onclick="updateStatus('${id}', 0)">关闭&nbsp;&nbsp;&nbsp;</a>
												</s:else>
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
		var validate;
		$(function(){
			
			$.validator.defaults.ignore = "";
			//表单验证规则
			validate = $("#segmentChannelBoxForm").validate({
				rules: {
					s_area_id:{
						required:true
					},			
					s_channel_id:{
						required:true,
						digits:true,
						max:999999999,
						min:1
					},
					s_client_code:{
						maxlength:10
					},
					s_remarks:{
						maxlength:200
					}
				}
			});
			
			
		});
		// 添加/编辑
		function edit(id) {
			if(null == id){
				location.href = "${ctx}/sysConf/segmentChannel/edit";
			}else{
				location.href = "${ctx}/sysConf/segmentChannel/edit?id=" + id;
			}
		}
		
		//删除
		function deleteConf(id, channel_id, client_code) {
			if (confirm("确定要删除" + id + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/segmentChannel/delete",
					data : {
						id : id,
						channel_id : channel_id,
						client_code : client_code
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
		
		function updateStatus(id, status){
			var confirmText = "";
			if(status == 0){
				confirmText = "关闭";
			}else{
				confirmText = "开启";
			}
			if (confirm("确定要" + confirmText + id + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/segmentChannel/updateStatus",
					data : {
						id : id,
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
		
	</script>
</body>
</html>