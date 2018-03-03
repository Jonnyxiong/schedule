<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>白关键字强制路由通道管理</title>
<style type="text/css">
	.width-initial{
		width: initial !important;
	}
</style>
</head>
<body menuId="291">
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
						

						
						<div class="search width-initial">
						 	 <form method="post" id="mainForm" action="${ctx}/sysConf/whitekeyword/query">
								<ul>
									<li>
										<input type="text" name="sign" value="${search_sign}" maxlength="21"
											   placeholder="签名" class="txt_250" />
									</li>
									<li>
										<input type="text" name="whiteKeyword" value="${search_whiteKeyword}" maxlength="21"
											   placeholder="白关键字" class="txt_250" />
									</li>
									<li>
										<label>客户代码:&nbsp;&nbsp;</label>
										<u:select id="clientCode" sqlId="findClientCode4s" value="${search_clientCode}" placeholder="客户代码" showAll="false"/>
										<%--<input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="21"--%>
											<%--placeholder="客户代码" class="txt_250" />--%>
									</li>
									<li>
										<%--<input type="text" name="channel_id" value="<s:property value="#parameters.channel_id"/>" maxlength="11"--%>
											<%--placeholder="通道组ID" class="txt_250" />--%>
											<u:select id="channelId" placeholder="通道组ID" sqlId="channelGroup4whitekey" value="${channel_id_search}" showAll="false" />

									</li>

									<li>
										<u:select id="sendType" value="${search_send_type}" data="[{value:'',text:'发送类型：所有'},{value:'0',text:'行业'},{value:'1',text:'营销'}]" showAll="false" />
									</li>
									<li>
										<u:select id="status" value="${search_status}" data="[{value:'',text:'状态：所有'},{value:'1',text:'开启'},{value:'0',text:'关闭'}]" showAll="false" />
									</li>
									<li><input type="submit" value="搜索" /><u:authority menuId="292">&nbsp;&nbsp;&nbsp;

							<span class="label label-info">
								<a href="javascript:;" onclick="edit(null)">添加</a>
							</span>
									</u:authority></li>
								</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>签名</th>
					                 <th>白关键字</th>
					                 <th>运营商类型</th>
									 <th>发送类型</th>
					                 <th>通道组ID</th>
					                 <th>客户代码</th>
					                 <th>状态</th>
									 <th>备注</th>
					                 <th>更新时间</th>
					                 <u:authority menuId="292">
					                 	<th>操作</th>
					                 </u:authority>
								</tr>
							</thead>
							<tbody>
										<s:iterator value="page.list" status="st">
								<tr>
										<td>${st.index + 1}</td>
										<td>${sign}</td>
										<td>${whiteKeyword}</td>
										<td>
											<s:if test="operatorstype eq 0">全网</s:if>
											<s:if test="operatorstype eq 1">移动</s:if>
											<s:if test="operatorstype eq 2">联通</s:if>
											<s:if test="operatorstype eq 3">电信</s:if>
											<s:if test="operatorstype eq 4">国际</s:if>
										</td>
										<td>
											<c:if test="${empty sendType}">-</c:if>
											<c:if test="${sendType eq 0}">行业</c:if>
											<c:if test="${sendType eq 1}">营销</c:if>
										</td>
										<td>${channelId}</td>
										<td>${clientCode}</td>
										<td>
											<s:if test="status==0">关闭</s:if>
											<s:elseif test="status==1">开启</s:elseif>
										</td>
										<td>${remarks}</td>
										<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${updateTime}" /></td>
										<u:authority menuId="292">
											<td>
												<a href="javascript:;" onclick="edit('${id}')">编辑&nbsp;&nbsp;&nbsp;</a>
												<a href="javascript:;" onclick="deleteConf('${id}', '${channel_id}', '${client_code}')">删除&nbsp;&nbsp;&nbsp;</a>
												<s:if test="status==0">
													<a href="javascript:;" onclick="updateStatus('${id}',1)">开启&nbsp;&nbsp;&nbsp;</a>
												</s:if>
												<s:else>
													<a href="javascript:;" onclick="updateStatus('${id}',0)">关闭&nbsp;&nbsp;&nbsp;</a>
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
						maxlength:20
					}
				}
			});
			
			
		});
		// 添加/编辑
		function edit(id) {
			if(null == id){
				location.href = "${ctx}/sysConf/whitekeyword/edit";
			}else{
				location.href = "${ctx}/sysConf/whitekeyword/edit?id=" + id;
			}
		}
		
		//删除
		function deleteConf(id, channel_id, client_code) {
			if (confirm("确定要删除该白关键字强制通道吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/whitekeyword/delete",
					data : {
						id : id

					},
					success : function(data) {
						if (data.msg == null) {
							alert("服务器错误，请联系管理员");
							return;
						}
                        parent.layer.msg(data.msg, {icon:2},1500);
						if (data.code == 0) {
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
			if (confirm("确定要" + confirmText + "该白关键字强制通道吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/sysConf/whitekeyword/updateStatus",
					data : {
						id : id,
						status : status
					},
					success : function(data) {
						if (data.msg == null) {
							alert("服务器错误，请联系管理员");
							return;
						}
                        parent.layer.msg(data.msg, {icon:2})
					//	alert(data.msg);
						if (data.code == 0) {
							window.location.reload();
						}
					}
				});
			}
		}
		
	</script>
</body>
</html>