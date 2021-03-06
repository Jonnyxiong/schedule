<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.middleware_id==null?'添加':'修改'}中间件配置</title>
</head>

<body menuId="235">
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
								<label class="control-label">中间件类型</label>
								<div class="controls">
									<u:select id="middleware_type" data="[{value:'0',text:'REDIS'},{value:'1',text:'MQ_C2S_IO'},{value:'2',text:'MQ_SEND_IO'},
																		{value:'3',text:'MQ_C2S_DB'},{value:'4',text:'MQ_SEND_DB'},{value:'5',text:'KAFKA'}]" 
									value="${data.middleware_type}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">中间件名称</label>
								<div class="controls">
									<input type="text" name="middleware_name" value="${data.middleware_name}" maxlength="100" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">中间件地址</label>
								<div class="controls">
									<input type="text" name="host_ip" value="${data.host_ip}" maxlength="50" />
									<%-- <s:if test="data.middleware_id==null">
										<input type="text" name="host_ip" value="${data.host_ip}" maxlength="50" />
									</s:if>
									<s:else>
										<input type="text" name="host_ip" value="${data.host_ip}" maxlength="50" readonly="readonly"/>
									</s:else> --%>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">端口</label>
								<div class="controls">
									<input type="text" name="port" value="${data.port}" maxlength="11" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">控制台端口</label>
								<div class="controls">
									<input type="text" name="console_port" value="${data.console_port}" maxlength="11" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">用户名</label>
								<div class="controls">
									<input type="text" name="user_name" value="${data.user_name}" maxlength="50" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">密码</label>
								<div class="controls">
									<input type="text" name="pass_word" value="${data.pass_word}" maxlength="50" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">所属机房</label>
								<div class="controls">
									<u:select id="node_id" data="[{value:'10',text:'北京亦庄'},{value:'11',text:'北京兆维'},{value:'12',text:'北京互联港湾'}]" 
									value="${data.node_id}" showAll="false"/>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">&nbsp;</label>
								<div class="controls">
									<span id="msg" class="error" style="display: none;"></span>
								</div>
							</div>
							
							<div class="form-actions">
								<input type="hidden" name="middleware_id" value="${data.middleware_id}">
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
			middleware_type:{
				required:true
			},
			middleware_name:{
				required:true,
				maxlength:100
			},
			host_ip:{
				required:true,
				checkip:true,
				maxlength:50
			},
			port:{
				maxlength:11,
				digits:true,
				range:[1,2147483647]
			},
			console_port:{
				maxlength:11,
				digits:true,
				range:[1,2147483647]
			},
			user_name:{
				maxlength:50
			},
			pass_word:{
				maxlength:50
			},
			node_id:{
				required:true
			}
		}
	});
	
	
	jQuery.validator.addMethod("checkip",
			function(value, element) {
				var reg = /^(((1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)))$/;;
				return this.optional(element) || (reg.test(value));
			}, "请输入合法的ip地址");
	
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
			
			$("input[name='middleware_id']").val(data.middleware_id);
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/componentConf/middleware/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>