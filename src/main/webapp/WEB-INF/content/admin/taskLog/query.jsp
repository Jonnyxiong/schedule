<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
<title>任务日志</title>
</head>

<body menuId="27">
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
						<div class="search">
						 <form method="post" id="mainForm">
							<ul>
								<%-- 
								<li>
									<input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="任务日志id/数据时间" class="txt_250" />
								</li>
								 --%>
								<li class="time">
									<u:date id="start_date" value="${start_date0}" placeholder="开始时间" maxId="end_date" maxToday="" />
									<span>至</span>
									<u:date id="end_date" value="${end_date0}" placeholder="结束时间" minId="start_date" maxToday=""  />
								</li>
								<li>
									<u:select id="task_id" value="${param.task_id}" placeholder="任务" sqlId="task"  />
								</li>
								<%-- 
								<li>
									<u:select id="task_type" value="${param.task_type}" placeholder="任务类型" dictionaryType="task_type" />
								</li>
								 --%>
								<li>
									<u:select id="duration" value="${param.duration}" placeholder="持续时间" dictionaryType="duration" />
								</li>
								<li>
									<u:select id="status" value="${param.status}" placeholder="状态" dictionaryType="task_log_status" />
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
							    	<th>任务日志id</th>
									<th>任务id</th>
									<th>任务名称</th>
									<th>任务类型</th>
									<th>数据时间</th>
									<th>开始时间</th>
									<th>持续时间(时:分:秒)</th>
    								<th>状态</th>
							    	<th>操作</th>
							    </tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
							     <tr>
							      <td>${rownum}</td>
							      <td>${log_id}</td>
							      <td>${task_id}</td>
							      <td><u:truncate value="${task_name}" length="60" /></td>
							      <td><u:ucparams key="${task_type}" type="task_type" /></td>
							      <td>${data_date}</td>
							      <td>${start_date}</td>
							      <td>${duration}</td>
								  <td><u:ucparams key="${status}" type="task_log_status" /></td>
							      <td>
							  		<a href="javascript:;" onclick="view('${log_id}')">查看</a>
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
function view(log_id){
	location.href="${ctx}/taskLog/view?log_id=" + log_id;
}
</script>
</body>
</html>