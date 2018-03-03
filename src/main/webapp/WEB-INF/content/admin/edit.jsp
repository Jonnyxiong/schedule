<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}管理员</title>
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
                <label class="control-label">管理员账号</label>
                <div class="controls">
                   <input type="text" name="email" value="${data.email}" maxlength="100"/>
                </div>
              </div>
              
              <div class="control-group">
                <label class="control-label">管理员身份</label>
                <div class="controls">
                    <c:if test="${sessionScope.LOGIN_ROLE_ID eq 1}">
                        <u:select id="role_id" placeholder="请选择管理员身份" value="${data.role_id}" sqlId="role" excludeValue=""/>
                    </c:if>
                    <c:if test="${sessionScope.LOGIN_ROLE_ID ne 1}">
                        <u:select id="role_id" placeholder="请选择管理员身份" value="${data.role_id}" sqlId="role" excludeValue=",1"/>
                    </c:if>
                </div>
              </div>

              <div class="control-group">
                <label class="control-label">真实姓名</label>
                <div class="controls">
                  <input type="text" name="realname" value="${data.realname}" maxlength="20"/>
                </div>
              </div>

              <div class="control-group">
                <label class="control-label">联系方式</label>
                <div class="controls">
                  <input type="text" name="mobile" value="${data.mobile}" autocomplete="off" onfocus="inputControl.setNumber(this, 11, 0, false)"/>
                </div>
              </div>
                <input type="hidden">
                <div class="control-group">
              <label class="control-label">登录密码</label>
              <div class="controls">

	              <input type="text" name="password" maxlength="20" autocomplete="off" onfocus="this.type='password'"/>
	              <s:if test="data.id!=null">
		              <span class="help-block">为空则不修改</span>
	              </s:if>
              </div>
            </div>

           	<div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
                  <input type="hidden" name="old_role_id" value="${data.role_id}" />
	              <input type="hidden" name="id" value="${data.id}" />
	              <input type="button" value="${data.id==null?'添 加':'修 改'}" class="btn btn-success" onclick="save(this)"/>
	              <input type="button" value="取 消" class="btn btn-error" onclick="back()"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>

<script type="text/javascript">
var validate;

$(function(){
	

	$.validator.defaults.ignore = "";
	var pwd = ${data.sid==null?'true':'false'}; //修改时密码不是必填
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			role_id: "required",
			email: {
				required: true,
				email2: true
			},
			realname: "required",
			mobile: {
				required: true,
				mobile: true
			},
			password: {
				required: pwd,
				rangelength:[6,20]
			}
		},
		messages: {
			role_id: "请输入管理员身份",
			email: {
				required:"请输入管理员账号"
			},
			realname: "请输入真实姓名",
			mobile: {
				required: "请输入联系手机",
				mobile: "请输入有效的联系手机"
			},
			password: {
				required:"请输入登录密码",
				rangelength:"密码长度为{0}~{1}位"
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
		url : "${ctx}/admin/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>