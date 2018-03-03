<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>通道池策略配置</title>
</head>
<body menuId="333">
	<!--Action boxes-->
	<div class="container-fluid">
<!-- 		<hr> -->

		
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<u:authority menuId="107">
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加</a>
						</span>
						</u:authority>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelPoolPolicy/query">
							<ul>


								<li>
									<input type="text" name="policyName" value="<s:property value="#parameters.policyName"/>" maxlength="30"
										placeholder="策略名称" class="txt_250" />
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
					                 <th>策略名称</th>
					                 <th>通道成功率权重</th>
					                 <th>通道价格权重</th>
					                 <th>抗投诉率权重</th>
					                 <th>低销权重</th>
					                 <th>客情权重</th>
					                 <th>是否默认策略</th>
					                 <th>备注</th>
					                 <th>更新人</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="jpage.data" status="st">
									<tr>
									<td>${st.index + 1}</td>
									<td>${policyName}</td>
									<td>${successWeight}</td>
									<td>${priceWeight}</td>
									<td>${antiComplaintWeight}</td>
									<td>${lowConsumeWeight}</td>
									<td>${customerRelationWeight}</td>
									<td>
										<c:if test="${empty isDefault}">-</c:if>
										<c:if test="${isDefault eq 0}">否</c:if>
										<c:if test="${isDefault eq 1}">是</c:if>
									</td>
									<td style="word-wrap:break-word;word-break:break-all;width: 180px;">${remark}</td>
									<td>${updateName}</td>
									<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${updateDate}" /></td>
							      <td>

							      		<a href="javascript:;" onclick="edit('${policyId}')">编辑</a>

							      	
							      </td>
							     </tr>
							    </s:iterator>
							</tbody>
						</table>
					</div>
						<u:jpage jpage="${jpage}" formId="mainForm" />
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
			location.href = "${ctx}/channelPoolPolicy/edit";
		}

		//编辑
		function edit(policyId) {
			location.href = "${ctx}/channelPoolPolicy/edit?policyId=" + policyId;
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
							url : "${ctx}/channelPoolPolicy/queryOffLineCheckInfo",
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
				url : "${ctx}/channelPoolPolicy/updateStatus",
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