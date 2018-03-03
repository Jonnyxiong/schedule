<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}SMSP组件端口配置</title>
</head>

<body menuId="239">
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
						<form class="form-horizontal" method="post" action="#"
							id="mainForm">
							<div class="control-group">
								<label class="control-label">组件类型</label>
								<div class="controls">
									<u:select id="component_type" data="[{value:'00',text:'SMSP_C2S'},{value:'01',text:'SMSP_ACCESS'},{value:'02',text:'SMSP_SEND'},
																		{value:'03',text:'SMSP_REPORT'},{value:'04',text:'SMSP_AUDIT'},{value:'05',text:'SMSP_CHARGE'},
																		{value:'06',text:'SMSP_CONSUMER'},{value:'07',text:'SMSP_REBACK'},{value:'08',text:'SMSP_HTTP'}]" 
									value="${data.component_type}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">服务名称</label>
								<div class="controls">
									<input type="text" name="port_key" value="${data.port_key}" maxlength="50" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">服务端口</label>
								<div class="controls">
									<input type="text" name="port_value" value="${data.port_value}" maxlength="11" />
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
			component_type:{
				required:true
			},
			port_key:{
				required:true,
				maxlength:50
			},
			port_value:{
				required:true,
				maxlength:11,
				digits:true,
				range:[1,2147483647]
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
			
			$("input[name='id']").val(data.id);
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/componentConf/smspCompListenPort/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>