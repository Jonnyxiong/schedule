<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}平台错误码</title>
</head>

<body menuId="245">
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
								<label class="control-label">平台错误码</label>
								<div class="controls">
									<input type="text" name="syscode" value="${data.syscode}" maxlength="7" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">错误类型</label>
								<div class="controls">
									<u:select id="type" data="[{value:'1',text:'应答'},{value:'2',text:'状态报告'}]" 
									value="${data.type}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">数据库状态</label>
								<div class="controls">
									<u:select id="state" data="[{value:'1',text:'1'},{value:'4',text:'4'},{value:'5',text:'5'}
																	,{value:'7',text:'7'},{value:'8',text:'8'},{value:'9',text:'9'}
																	,{value:'10',text:'10'}]" 
									value="${data.state}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">平台错误描述</label>
								<div class="controls">
									<input type="text" name="errordesc" value="${data.errordesc}"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">适用协议</label>
								<div class="controls">
									<u:select id="usreprotocol" data="[{value:'1',text:'全部'},{value:'2',text:'四大直连'},{value:'3',text:'CMPP'}
																	,{value:'4',text:'SGIP'},{value:'5',text:'SMGP'},{value:'7',text:'SMPP'}
																	,{value:'8',text:'HTTP'}]" 
									value="${data.usreprotocol}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">组件类型</label>
								<div class="controls">
									<u:select id="component_type" data="[{value:'00',text:'SMSP_C2S'},{value:'01',text:'SMSP_ACCESS'},{value:'02',text:'SMSP_SEND'}
																		,{value:'07',text:'SMSP_REBACK'},{value:'08',text:'SMSP_HTTP'}]" 
									value="${data.component_type}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">备注</label>
								<div class="controls">
									<input type="text" name="remark" value="${data.remark}" maxlength="100" />
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
			syscode:{
				required:true,
				maxlength:7
			},
			type:{
				required:true
			},
			state:{
				required:true,
				maxlength:100
			},
			errordesc:{
				required:true,
				maxlength:30
			},
			usreprotocol:{
				required:true
			},
			component_type:{
				required:true
			},
			remark:{
				maxlength:100
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
		url : "${ctx}/componentConf/smspErrorCodeMgnt/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>