<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>通道属性实时权重</title>
</head>
<body menuId="332">
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

						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelRealTimeWeight/query">
							<ul>

								<li>
									<input type="text" name="channelid" value="<s:property value="#parameters.channelid"/>" maxlength="10"
										placeholder="通道号" class="txt_250" />
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
					                 <th>状态报告成功率</th>
					                 <th>单价(元)</th>
					                 <th>抗投诉率</th>
					                 <th>扩展标志</th>
					                 <th>低销额度</th>
					                 <th>验证码成功率权重</th>
					                 <th>通知成功率权重</th>
					                 <th>营销成功率权重</th>
					                 <th>告警成功率权重</th>
					                 <th>移动对应通道价格权重</th>
					                 <th>联通对应通道价格权重</th>
					                 <th>电信对应通道价格权重</th>
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
									<td>${channelid}</td>
									<td>${successRate}</td>
									<td>${unitPrice}</td>
									<td>${antiComplaint}</td>
									<td>
										<c:if test="${empty exFlag}">-</c:if>
										<c:if test="${exFlag eq 0}">-</c:if>
										<c:if test="${exFlag eq 1}">客情</c:if>
										<c:if test="${exFlag eq 2}">低销</c:if>
										<c:if test="${exFlag eq 3}">客情+低销</c:if>
									</td>
									<td>${lowConsumeLimit}</td>
									<td>${yzSuccessWeight}</td>
									<td>${tzSuccessWeight}</td>
									<td>${yxSuccessWeight}</td>
									<td>${gjSuccessWeight}</td>
									<td>${ydPriceWeight}</td>
									<td>${ltPriceWeight}</td>
									<td>${dxPriceWeight}</td>
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
	
		<%--//添加--%>
		<%--function add() {--%>
			<%--location.href = "${ctx}/channelRealTimeWeight/edit";--%>
		<%--}--%>

		//编辑
		function edit(id) {
			location.href = "${ctx}/channelRealTimeWeight/edit?id=" + id;
		}


		
	</script>
</body>
</html>