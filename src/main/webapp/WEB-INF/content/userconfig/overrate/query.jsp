<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>关键字超频限制设置</title>
</head>
<body menuId="322">
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

						<u:authority menuId="323">
							<span class="label label-info"> <a href="javascript:;"
								onclick="add()">添加</a>
							</span>
						</u:authority>


						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/overrate/query">
								<ul>
									<li><input type="text" name="userid"
										value="<s:property value="#parameters.userid"/>" maxlength="40"
										placeholder="用户账号" class="txt_250" /></li>
									<li><input type="text" name="sign"
										value="<s:property value="#parameters.sign"/>" maxlength="40"
										placeholder="签名内容" class="txt_250" /></li>
									<li name="">
										<label class="control-label">更新时间&nbsp;&nbsp;</label>
										<u:date id="startTime" value="${startTime}" placeholder="请选择开始时间" params="minDate:'%y-%M-{%d-90} 00:00:00', maxDate:'#F{$dp.$D(\\'endTime\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
										<span>至</span>
										<u:date id="endTime" value="${endTime}" placeholder="请选择结束时间" params="minDate:'#F{$dp.$D(\\'startTime\\')||\\'%y-%M-{%d-90} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
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
									<th>用户账号</th>
									<th>签名内容</th>
									<th>超频规则</th>
									<th>状态</th>
									<th>更新时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="jpage.data" status="st">
									<tr>
										<td>${st.index + 1}</td>
										<td>${userid}</td>
										<td>${sign}</td>
										<td>${overRateNumS}/${overRateTimeS};${overRateNumM}/${overRateTimeM};${overRateNumH}/${overRateTimeH}</td>
										<td>
											<c:if test="${state eq 0}">关闭</c:if>
											<c:if test="${state eq 1}">开启</c:if>
										</td>
										<td>
											<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${overRateUpdatetime}" />
										</td>

										<td style="width: 10%">
											<u:authority menuId="327">
												<a href="javascript:;" onclick="view('${id}')">查看</a>
											</u:authority>
											<u:authority menuId="323">
											<%--<u:authority menuId="128">--%>
									      		<a href="javascript:;" onclick="edit('${id}')">编辑</a>
											<%--</u:authority>--%>
											<%--<u:authority menuId="127">--%>
												<s:if test="state==0">
													<a href="javascript:;" onclick="updateStatus(this, '${id}',1)">开启</a>
												</s:if>
											<%--</u:authority>--%>
											<%--<u:authority menuId="127">--%>
												<s:if test="state==1">
													<a href="javascript:;" onclick="updateStatus(this, '${id}',0)">关闭</a>
												</s:if>
											<%--</u:authority>--%>
											<%--<u:authority menuId="129">--%>
									      	  	<a href="javascript:;" onclick="deleteData('${id}')">删除</a>
											<%--</u:authority>--%>

											</u:authority>

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

        //查看
        function view(id) {
            location.href = "${ctx}/overrate/view?id=" + id ;
        }
		//添加
		function add() {
			location.href = "${ctx}/overrate/edit";
		}

		//编辑
		function edit(id) {
			location.href = "${ctx}/overrate/edit?id=" + id + "&isEdit=" + 1;
		}


		//修改状态
		function updateStatus(a, id,  state) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/overrate/updateStatus",
					data : {
						id:id,
						state : state
					},
					success : function(data) {
						if (data.code == 500) {
                            layer.msg(data.msg, {icon:2});
							//alert("服务器错误，请联系管理员");
							return;
						}
                        layer.msg(data.msg, {icon: 1,time: 1500},function(){
                            location.reload();
                        });
					}
				});
			}
		}

		
		function getSelectedValue(){
			var val = "";  
		    $("[name='ids']:checkbox").each(function(){  
		        if ($(this).attr('checked')) {  
		            val +=$(this).val()+",";  
		        }  
		    });  
		    val = val.length > 0 ? val.substr(0, val.length-1) : '';  
		    return val;
		}
		
		//删除
		function deleteData(id) {
			if (confirm("确定要删除吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/overrate/delete",
					data : {
						id : id
					},
					success : function(data) {
						if (data.code == 500) {
                            layer.msg(data.msg, {icon:2});
						//	alert("服务器错误，请联系管理员");
							return;
						}


                        layer.msg(data.msg, {icon: 1,time: 1500},function(){
                            location.reload();
                        });
//						alert(data.msg);
//						if (data.code == 0) {
//							window.location.reload();
//						}
					}
				});
			}
		}
	</script>
</body>
</html>