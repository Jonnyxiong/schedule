<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>系统通知时段列表</title>
</head>

<body menuId="9">
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
						<span class="label label-info">
						<u:authority menuId="1">
							<a href="javascript:;" onclick="add()">添加系统通知时段</a>
						</u:authority>
						</span>
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/sysNotice/query">
							<ul>
								<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40"
								placeholder="通知时段id/通知时段名称" class="txt_250" /></li>
								<li class="time">
							       	<u:date id="start_date" value="${param.start_date}" placeholder="时段开始时间" maxId="end_date,-1" dateFmt="HH:mm:ss" startDate="00:00:00" />
									<span>至</span>
							       	<u:date id="end_date" value="${param.end_date}" placeholder="时段结束时间" minId="start_date,1" dateFmt="HH:mm:ss" />
								</li>
								 <li>
									<u:select id="status" value="${param.status}" placeholder="状态" dictionaryType="sys_notice_status" />
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
						    	<th>通知时段id</th>
						    	<th>通知时段名称</th>
						    	<th>时段</th>
						    	<th>接收管理员</th>
						    	<th>更新时间</th>
						    	<th>状态</th>
						    	<th>告警方式</th>
						    	<th>操作</th>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td>${notice_id}</td>
							      <td><u:truncate length="20" value="${name}" /></td>
							      <td>${start_date}至${end_date}</td>
							      <td><u:truncate length="20" value="${mobile}" /></td>
							      <td>${update_date}</td>
							      <td name="status_name"><u:ucparams key="${status}" type="sys_notice_status"></u:ucparams></td>
							      <td>
							      	<s:if test="alarm_type==1">
									   	邮件
									</s:if>
									<s:elseif test="alarm_type==2">
									           短信
									</s:elseif>
									<s:else>
									           短信，邮件
									</s:else>
							      </td>
							      <td>
							   		<%--0关闭 --%>
							   		<span name="operate_0" ${status==0 ? "" : "style='display:none;'"}>
											<a href="javascript:;" onclick="view('${notice_id}')">查看</a>
											| <a href="javascript:;" onclick="updateStatus(this, '${notice_id}', 1)">启用</a>
											| <a href="javascript:;" onclick="edit('${notice_id}')">编辑</a>
											| <a href="javascript:;" onclick="updateStatus(this, '${notice_id}', 2)">删除</a>
							   		</span>
							   		
							   		<%--1启用 --%>
							   		<span name="operate_1" ${status==1 ? "" : "style='display:none;'"}>
											<a href="javascript:;" onclick="view('${notice_id}')">查看</a>
											| <a href="javascript:;" onclick="updateStatus(this, '${notice_id}', 0)">关闭</a>
							   		</span>
							   		
							   		<%--2已删除 --%>
							   		<span name="operate_2" ${status==2 ? "" : "style='display:none;'"}>
											<a href="javascript:;" onclick="view('${notice_id}')">查看</a>
											| <a href="javascript:;" onclick="updateStatus(this, '${notice_id}', 0)">恢复</a>
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
function view(notice_id){
	location.href="${ctx}/sysNotice/view?notice_id=" + notice_id;
}

//添加
function add(){
	location.href="${ctx}/sysNotice/add";
}

//编辑
function edit(notice_id){
	location.href="${ctx}/sysNotice/edit?notice_id=" + notice_id;
}

//修改状态：关闭、启用、删除
function updateStatus(a, notice_id, status){
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
			url: "${ctx}/sysNotice/updateStatus",
			data:{
				notice_id : notice_id,
				status : status
			},
			success: function(data){
				if(data.result==null){
					alert("服务器错误，请联系管理员");
					return;
				}
				
				if(data.result=="success"){
					$(a).parent().hide();
					$(a).parents("tr").find("[name='operate_"+status+"']").show();
					$(a).parents("tr").find("[name='status_name']").text(status_name);
					alert($(a).text() + "成功");
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