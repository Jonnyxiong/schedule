<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<title>登录页面</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<link rel="shortcut icon" href="${ctx}/img/favicon.ico" />
	<link rel="bookmark" href="${ctx}/img/favicon.ico"/>
	<link rel="stylesheet" href="${ctx}/css/bootstrap.min.css" />
	<link rel="stylesheet" href="${ctx}/css/bootstrap-responsive.min.css" />
	<link rel="stylesheet" href="${ctx}/css/matrix-login.css" />
	<link href="${ctx}/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
	<link href="${ctx}/font-awesome/css/font-awesome-ie7.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="${ctx}/css/uniform.css" />
</head>
<body style='font-family: "Helvetica Neue", Helvetica, Arial, "PingFang SC", "Hiragino Sans GB", "WenQuanYi Micro Hei", "Microsoft Yahei", sans-serif;'>
<div id="loginbox">
  <form id="loginForm" class="form-vertical" action="${ctx}/login" method="post" >
    <div class="control-group normal_text">
      <h3><img src="${ctx}/img/logo2.png" alt="Logo" /></h3>
    </div>
    <h3 style="text-align: center;color: #fff;margin-top: -10px;">短信调度系统</h3>
    <div class="control-group">
      <div class="controls">
        <div class="main_input_box"> <span class="add-on bg_lg"><i class="fa fa-user"></i></span>
          <input type="text" id="username" name="username"  placeholder="用户名" />
        </div>
      </div>
    </div>
    <div class="control-group">
      <div class="controls">
        <div class="main_input_box"> <span class="add-on bg_ly"><i class="fa fa-lock"></i></span>
          <input type="password" id="passwordInput"  placeholder="密码" />
        </div>
      </div>
    </div>
    <div class="error_box"><span id="errorInfo" style="display:none;"></span></div>
    <div class="form-actions" style="text-align:center;"> 
    	<span class="pull-left">
    		<input type="checkbox" value="" id="remember"  name="radios" />记住登录名</a>
    	</span> 
    	<%-- <span>
    		<a href="${ctx}/agent/common/modifyPwd" style="color: #FFFFFF;font-size: 13px;">修改密码</a>
    	</span> --%>
    	<span class="pull-right">
    		<a type="submit" id="loginBtn" onclick="login(this);" class="btn btn-success" /> 登录 </a>
    	</span> 
    </div>
  	<input type="hidden" id="password" name="password"/>
  </form>
</div>
<!--登录 bof-->
<script src="${ctx}/js/jquery.min.js"></script> 
<script src="${ctx}/js/jquery.form.js"></script>
<script src="${ctx}/js/cookie.js"></script>
<script src="${ctx}/js/hex_ha_ha_ha_ha_ha.js"></script>
<script src="${ctx}/js/jquery.ui.custom.js"></script> 
<script src="${ctx}/js/bootstrap.min.js"></script> 
<script src="${ctx}/js/jquery.uniform.js"></script> 
<script src="${ctx}/js/select2.min.js"></script> 
<script src="${ctx}/js/jquery.validate.js"></script> 
<script src="${ctx}/js/matrix.js"></script> 
<script src="${ctx}/js/matrix.form_validation.js"></script>
<script src="${ctx}/js/matrix.login.js"></script>
<script type="text/javascript">
	$(function(){
		var username = getcookie("login_username");
		if($.trim(username) !=""){
			$("#username").val(username);
			$("#remember").click();
		}
		
		// 登录回车事件
		document.onkeydown = function(event){ 
			var ev = event || window.event;
		    if(ev.keyCode == 13){
		    	var password = $.trim($("#passwordInput").val());
		    	if(password == ""){
		    		$("#loginBtn").click();
		    		$("#passwordInput").focus();
				}else{
			    	$("#loginBtn").click();
				}
		    }
		}
	});
	
	function login(btn){
		var username = $.trim($("#username").val());
		var password = $.trim($("#passwordInput").val());
		//var randCheckCode =  $.trim($("#randCheckCode").val());
		var error = [];
		if(username==""){
			error.push("用户名不能为空");
		}
		if(password==""){
			error.push("密码不能为空");
		}
	//	if(randCheckCode==""){
	//		error.push("验证码不能为空");
	//	}
		
		if(error!=""){
			$("#errorInfo").html(error.join("，")).show();
			return;
		}
		$("#errorInfo").hide();
		
		var options = {
			beforeSubmit : function() {
				$(btn).attr("disabled", true);
			},
			success : function(data) {
				$(btn).attr("disabled", false);
				
				if(data.result==null){
					$("#errorInfo").html("服务器错误，请联系管理员").show();
					return;
				}
				
				if(data.result=="isLogin"){ // 已登录
					$("#errorInfo").html(data.msg).show();
					window.setTimeout(function(){
						location.href=data.url;
					}, 2000);
					return;
				}else if(data.result=="fail"){
					$("#errorInfo").html(data.msg).show();
					$("#randCheckCode_img").click();
					return;
				}
				
				if($("#remember").is(":checked")){
					addcookie("login_username", username, 30*24);
				}else{
					deletecookie("login_username");
				}
				location.href=data.url;
			},
			url : "${ctx}/login",
			type : "post",
			timeout:30000
		};
		$("#password").val(hex_ha_ha_ha_ha_ha(password));
		$("#loginForm").ajaxSubmit(options);
	}
</script>
</body>
</html>