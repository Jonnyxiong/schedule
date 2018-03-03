<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>告警管理列表</title>
</head>

<body menuId="51">
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
						<u:authority menuId="95">
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">配置告警条件</a>
						</span>
						</u:authority>
						
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/alarm/query">
							<ul>
								<li>
									<u:select id="channel_id" value="${param.channel_id}" placeholder="通道号" sqlId="findAllChannel" />
								</li>
								<li>
									<u:select id="status" value="${param.status}" placeholder="状态" dictionaryType="alarm_condition_status" />
								</li>
								<li><input type="submit" value="搜索" /></li>
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<th width="50px">序号</th>
						    	<th>通道号</th>
						    	<th>提交失败告警值</th>
						    	<th>发送失败告警值</th>
						    	<th>错误码</th>
						    	<th>状态</th>
						    	<th>操作</th>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td>${channelid}</td>
							      <td>${submit_failure_number}</td>
							      <td>${send_failure_number}</td>
							      <td><u:truncate length="20" value="${send_failure_codes}" /></td>
							      <td name="status_name"><u:ucparams key="${status}" type="alarm_condition_status"></u:ucparams></td>
							      <td>
							   		<%--0关闭 --%>
							   		<span name="operate_0" ${status==0 ? "" : "style='display:none;'"}>
											<a href="javascript:;" onclick="view('${alarm_id}')">查看</a>
											<u:authority menuId="133">
											| <a href="javascript:;" onclick="updateStatus(this, '${alarm_id}', 1)">启用</a>
											</u:authority>
											<u:authority menuId="134">
											| <a href="javascript:;" onclick="edit('${alarm_id}')">编辑</a>
											</u:authority>
											<u:authority menuId="135">
											| <a href="javascript:;" onclick="updateStatus(this, '${alarm_id}', 2)">删除</a>
							   				</u:authority>
							   		</span>
							   		
							   		<%--1启用 --%>
							   		<span name="operate_1" ${status==1 ? "" : "style='display:none;'"}>
											<a href="javascript:;" onclick="view('${alarm_id}')">查看</a>
											<u:authority menuId="137">
											| <a href="javascript:;" onclick="updateStatus(this, '${alarm_id}', 0)">关闭</a>
											</u:authority>
							   		</span>
							   		
							   		<%--2已删除 --%>
							   		<span name="operate_2" ${status==2 ? "" : "style='display:none;'"}>
											<a href="javascript:;" onclick="view('${alarm_id}')">查看</a>
											| <a href="javascript:;" onclick="updateStatus(this, '${alarm_id}', 0)">恢复</a>
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
<script type="text/javascript">
//查看
function view(alarm_id){
	location.href="${ctx}/alarm/view?alarm_id=" + alarm_id;
}

//添加
function add(){
	location.href="${ctx}/alarm/add";
}

//编辑
function edit(alarm_id){
	location.href="${ctx}/alarm/edit?alarm_id=" + alarm_id;
}

//修改状态：关闭、启用、删除
function updateStatus(a, alarm_id, status){
	var status_name="";
	switch(status){
	case 0:	status_name = "关闭";
			break;
	case 1:	status_name = "启用";
			break;
	case 2:	status_name = "已删除";
			break;
	}
	
	if(confirm("确定要"+ $(a).text() +"吗？")){
		$.ajax({
			type: "post",
			url: "${ctx}/alarm/updateStatus",
			data:{
				alarm_id : alarm_id,
				status : status
			},
			success: function(data){
				if(data.result==null){
					alert("服务器错误，请联系管理员");
					return;
				}
				
				if(data.result=="success"){
					if(status == 2){
						alert($(a).text() + "成功");
						location.href = "${ctx}/alarm/query";
					}else{
						$(a).parent().hide();
						$(a).parents("tr").find("[name='operate_"+status+"']").show();
						$(a).parents("tr").find("[name='status_name']").text(status_name);
						alert($(a).text() + "成功");
					}
				}else{
					alert(data.msg);
				}
			}
		});
	}
}
</script>
</body>
</html>