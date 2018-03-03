<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>查看任务日志</title>
</head>
<body menuId="27">
 <div class="container-fluid">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal">
				<div class="control-group">
	                <label class="control-label">任务日志id</label>
	                <div class="controls">
	                  <span>${data.log_id}</span>
	                </div>
				</div>
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
	                <label class="control-label">数据时间</label>
	                <div class="controls">
	                  <span>${data.data_date}</span>
	                </div>
				</div>
				<div class="control-group">
	                <label class="control-label">开始时间</label>
	                <div class="controls">
	                  <span>${data.start_date}</span>
	                </div>
				</div>
				<div class="control-group">
	                <label class="control-label">结束时间</label>
	                <div class="controls">
	                  <span>${data.end_date}</span>
	                </div>
				</div>
				<div class="control-group">
	                <label class="control-label">持续时间(时:分:秒)</label>
	                <div class="controls">
	                  <span>${data.duration}</span>
	                </div>
				</div>
				<div class="control-group">
	                <label class="control-label">备注</label>
	                <div class="controls">
	                  <span>${data.remark}</span>
	                </div>
				</div>
				<div class="control-group">
	                <label class="control-label">状态</label>
	                <div class="controls">
	                  <span><u:ucparams key="${data.status}" type="task_log_status" /></span>
	                </div>
				</div>
              
              <div class="form-actions">
		 			<input type="button" value="返 回" class="grey_btn" onclick="history.back()"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
</div>
</body>
</html>