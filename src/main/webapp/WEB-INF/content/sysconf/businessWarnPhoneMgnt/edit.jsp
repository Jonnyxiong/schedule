<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}业务预警人员号码</title>
</head>

<body menuId="255">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-list"></i>
						</span>
						<h5></h5>
					</div>
					<div class="widget-content nopadding">
						<form class="form-horizontal" method="post" action="#" id="mainForm">
							
							<div class="control-group">
								<label class="control-label">用户账号</label>
								<div class="controls">
									<input type="text" name="clientid" value="${data.clientid}" maxlength="6" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">告警号码</label>
								<div class="controls">
				                	<textarea name="phone" id="phone" rows="13" cols="120"
											  style="height: auto; width: auto; display: block;" ><c:out value="${data.phone}"/></textarea>
									<span class="help-block">多个告警号码间用分号","分隔,如"134xxxx1234;134xxxx5678"</span>
				                </div>
							</div>

							<div class="control-group">
								<label class="control-label">&nbsp;</label>
								<div class="controls">
									<span id="msg" class="error" style="display: none;"></span>
								</div>
							</div>
							
							<div class="form-actions">
								<input type="hidden" name="id" value="${data.id}">
								<input type="button" onclick="save(this);" value="保  存" class="btn btn-success">
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
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			clientid:{
				required:true,
				maxlength:6
			},
			phone:{
				required:true,
				maxlength:500
			}
		}
	});
	
	
});


function save(btn){
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	
	var isPass = true;
	var phoneLists = $("#phone").val().split(",");
	$.each(phoneLists,function(i,o){
		if($.isEmptyObject(o) && !/^1[0-9]{10}$/.test(o)){
			$("#msg").text("请规则填写手机号码").show();
			isPass = false;
			return false;
		}
	});
	if(!isPass){
		return ;
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
			$("input[name='id']").val(data.id);
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/sysConf/businessWarnPhoneMgnt/save",
		type : "post",
		timeout : 30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>