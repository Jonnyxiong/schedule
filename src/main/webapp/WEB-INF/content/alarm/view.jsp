<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>查看告警条件</title>
</head>
<body menuId="51">
 <div class="container-fluid">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal" method="post" action="${ctx}/alarm/edit?alarm_id=${data.alarm_id}" name="basic_validate" id="basic_validate" novalidate="novalidate">
               <div class="control-group">
                <label class="control-label">通道号</label>
                <div class="controls">
                  <span>${data.channelid}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">提交失败告警值</label>
                <div class="controls">
                 	<span>${data.submit_failure_number}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">发送失败告警值</label>
                <div class="controls">
                 	<span>${data.send_failure_number}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">错误码</label>
                <div class="controls">
                  <span>${data.send_failure_codes}</span>
                </div>
              </div>
               <div class="control-group">
                <label class="control-label">状态</label>
                <div class="controls">
                  <span><u:ucparams key="${data.status}" type="sys_notice_status"></u:ucparams> </span>
                </div>
              </div>
              <div class="form-actions">
		 			<input type="submit" value="修 改" class="btn btn-success" />
		 			<input type="button" value="返 回" class="grey_btn" onclick="location.href='${ctx}/alarm/query'"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
</div>
</body>
</html>