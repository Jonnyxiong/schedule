<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>通道属性区间权重配置</title>
</head>
<body menuId="331">
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

						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加</a>
						</span>

						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelConfig/query">
							<ul>
								<li>
                  					<u:select id="type" data="[{value:'',text:'类型:所有'},{value:'0',text:'通道成功率区间'},{value:'1',text:'通道价格区间'}]"
                  					value="${search_type}" showAll="false" />
								</li>
								<li>
									<u:select id="exValue" data="[{value:'',text:'属性:所有'},{value:'0',text:'短信类型－验证码'},{value:'1',text:'短信类型－通知'},{value:'2',text:'短信类型－营销'},{value:'3',text:'短信类型－告警'},{value:'4',text:'发送号码所属运营商－移动'},{value:'5',text:'发送号码所属运营商－联通'},{value:'6',text:'发送号码所属运营商－电信'}]"
											  value="${search_exValue}" showAll="false" />
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
					                 <th>类型</th>
					                 <th>属性</th>
					                 <th>区间</th>
					                 <th>区间权重</th>
					                 <th>更新者</th>
					                 <th>更新时间</th>
					                 <th>备注</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="jpage.data" status="st">
									<tr>
									<td>${st.index + 1}</td>
									<td>
										<c:if test="${empty type}">-</c:if>
										<c:if test="${type eq 0}">通道成功率区间</c:if>
										<c:if test="${type eq 1}">通道价格区间</c:if>
									</td>
									<td>
										<c:if test="${exValue eq 0}">短信类型－验证码</c:if>
										<c:if test="${exValue eq 1}">短信类型－通知</c:if>
										<c:if test="${exValue eq 2}">短信类型－营销</c:if>
										<c:if test="${exValue eq 3}">短信类型－告警</c:if>
										<c:if test="${exValue eq 4}">发送号码所属运营商－移动</c:if>
										<c:if test="${exValue eq 5}">发送号码所属运营商－联通</c:if>
										<c:if test="${exValue eq 6}">发送号码所属运营商－电信</c:if>

										</td>
									<td>${region}</td>
									<td>${allWeight}</td>
									<td>${updateName}</td>
									<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${updateDate}" /></td>
									<td style="word-wrap:break-word;word-break:break-all;width: 180px;">${remark}</td>
							      <td>

							      		<a href="javascript:;" onclick="edit('${id}')">编辑</a>



							      	
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
			location.href = "${ctx}/channelConfig/edit";
		}

		//编辑
		function edit(id) {
			location.href = "${ctx}/channelConfig/edit?id=" + id;
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
							url : "${ctx}/channelConfig/queryOffLineCheckInfo",
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
				url : "${ctx}/channelConfig/updateStatus",
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