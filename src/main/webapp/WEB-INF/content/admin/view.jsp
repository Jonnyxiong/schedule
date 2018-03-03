<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
	<title>管理员资料</title>
</head>
<body menuId="14">
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
            <form class="form-horizontal" method="post" action="${ctx}/admin/edit?id=${data.id}" name="basic_validate" id="basic_validate" novalidate="novalidate">
               <div class="control-group">
                <label class="control-label">管理员账号</label>
                <div class="controls">
                  <span>${data.email}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">管理员身份</label>
                <div class="controls">
                 	<span>${data.role_name}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">真实姓名</label>
                <div class="controls">
                 	<span>${data.realname}</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">联系方式</label>
                <div class="controls">
                  <span>${data.mobile}</span>
                </div>
              </div>
              <div class="form-actions">
                <u:authority menuId="7">
					<input type="hidden" name="id" value="${id}"/>
		 			<input type="submit" value="修 改" class="btn btn-success" />
				</u:authority>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>