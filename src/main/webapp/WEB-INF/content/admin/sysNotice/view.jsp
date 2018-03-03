<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>查看系统通知时段</title>
</head>
<body menuId="9">
 <div class="container-fluid">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal" method="post" action="${ctx}/sysNotice/edit?notice_id=${data.view.notice_id}" name="basic_validate" id="basic_validate" novalidate="novalidate">
               <div class="control-group">
                <label class="control-label">通知时段id</label>
                <div class="controls">
                  <span>${data.view.notice_id}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">通知时段名称</label>
                <div class="controls">
                 	<span>${data.view.name}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">时段</label>
                <div class="controls">
                 	<span>${data.view.start_date}至${data.view.end_date}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">接收管理员</label>
                <div class="controls">
                  <span>${data.view.mobile}</span>
                </div>
              </div>
              
              
               <div class="control-group">
                <label class="control-label">更新时间</label>
                <div class="controls">
                  <span>${data.view.update_date}</span>
                </div>
              </div>
              
               <div class="control-group">
                <label class="control-label">状态</label>
                <div class="controls">
                  <span><u:ucparams key="${data.view.status}" type="sys_notice_status"></u:ucparams> </span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">告警方式</label>
                <div class="controls">
                  <span>
                  	<s:if test="data.view.alarm_type==1">
					   	邮件
					</s:if>
					<s:elseif test="data.view.alarm_type==2">
					           短信
					</s:elseif>
					<s:else>
					           短信，邮件
					</s:else>
                  </span>
                </div>
              </div>
              <div class="form-actions">
		 			<input type="submit" value="修 改" class="btn btn-success" />
		 			<input type="button" value="返 回" class="grey_btn" onclick="location.href='${ctx}/sysNotice/query'"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
</div>
</body>
</html>