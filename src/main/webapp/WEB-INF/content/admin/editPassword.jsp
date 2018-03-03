<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>修改密码</title>
</head>

<body menuId="7">
<div class="container-fluid" menuI="1">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal" method="post" name="basic_validate" id="mainForm" novalidate="novalidate">
              <div class="control-group">
                <label class="control-label">当前密码</label>
                <div class="controls">
                   <input type="password" name="password" maxlength="20"/>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">新密码</label>
                <div class="controls">
                  <input type="password" id="newPassword" name="newPassword" maxlength="20"/>
                </div>
              </div>
              <div class="control-group">
              <label class="control-label">再次输入新密码</label>
              <div class="controls">
	              <input type="password" name="confirmPassword" maxlength="20"/>
              </div>
            </div>
            <div><span id="msg" class="error" style="display:none;"></span></div>
              <div class="form-actions">
	              <input type="hidden" name="id" value="${id}" />
	             <input type="button" value="修 改" class="org_btn" onclick="save(this)"/>
	             <input type="button" value="取 消" class="grey_btn" onclick="cancel()"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
var validate;
$(function(){
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			password: {
				required: true,
				rangelength:[6,20]
			},
			newPassword: {
				required: true,
				rangelength:[6,20]
			},
			confirmPassword: {
				required: true,
				equalTo: "#newPassword"
			}
		},
		messages: {
			password: {
				required:"请输入当前密码",
				rangelength:"密码长度为{0}~{1}位"
			},
			newPassword: {
				required:"请输入新密码",
				rangelength:"密码长度为{0}~{1}位"
			},
			confirmPassword: {
				required:"请再次输入新密码",
				equalTo:"两次输入密码不一致"
			}
		}
	});
});

function save(btn){
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	
	var options = {
		beforeSubmit : function() {
			$(btn).attr("disabled", true);
		},
		success : function(data) {
			$(btn).attr("disabled", false);
			
			if(data.result==null){
				$("#msg").text("服务器错误，请联系管理员").show();
				return;
			}
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/admin/savePassword",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}

//取消
function cancel(){
	$("#mainForm")[0].reset();
	$("label.error").remove();
}
</script>
</body>
</html>