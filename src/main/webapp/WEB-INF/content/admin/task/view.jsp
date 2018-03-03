<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>查看任务</title>
</head>

<body menuId="43">
<!--Action boxes-->
  <div class="container-fluid">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal" method="post">
            	
				<div class="control-group">
					<label class="control-label">任务id</label>
					<div class="controls">
						<span>${data.task_id}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">任务名称</label>
					<div class="controls">
						<span>${data.task_name}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">任务类型</label>
					<div class="controls">
						<span><u:ucparams key="${data.task_type}" type="task_type" /></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">存储过程名称</label>
					<div class="controls">
						<span>${data.procedure_name}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">执行类型</label>
					<div class="controls">
						<span><u:ucparams key="${data.execute_type}" type="task_execute_type" /></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">下次执行时间</label>
					<div class="controls">
						<span>${data.execute_next}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">执行周期</label>
					<div class="controls">
						<span>${data.execute_period}<u:ucparams key="${data.execute_type}" type="task_execute_type" /></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">扫描类型</label>
					<div class="controls">
						<span><u:ucparams key="${data.scan_type}" type="task_scan_type" /></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">下次扫描时间</label>
					<div class="controls">
						<span>${data.scan_next}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">扫描周期</label>
					<div class="controls">
						<span>${data.scan_period}<u:ucparams key="${data.scan_type}" type="task_scan_type" /></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">是否每次扫描<br/>都执行</label>
					<div class="controls">
						<span><u:ucparams key="${data.scan_execute}" type="task_scan_execute" /></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">依赖任务</label>
					<div class="controls">
						<span><u:selectMultiple id="dependency" value="${data.dependency}" sqlId="task" excludeValue="${data.task_id}, " showSelectAll="false" disabled="true" /></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">分组</label>
					<div class="controls">
						<span>${data.group}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">组内排序</label>
					<div class="controls">
						<span>${data.order}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">创建时间</label>
					<div class="controls">
						<span>${data.create_date}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">更新时间</label>
					<div class="controls">
						<span>${data.update_date}</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">状态</label>
					<div class="controls">
						<span><u:ucparams key="${data.status}" type="task_status" /></span>
					</div>
				</div>
				<div class="form-actions">
					<input type="button" value="返 回" class="btn btn-success" onclick="back()" />
				</div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>