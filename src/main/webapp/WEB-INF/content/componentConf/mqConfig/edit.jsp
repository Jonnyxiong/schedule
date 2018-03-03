<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.middleware_id==null?'添加':'修改'}MQ配置</title>
</head>

<body menuId="241">
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
								<label class="control-label">MQ中间件ID</label>
								<div class="controls">
<%-- 									<input type="text" name="middleware_id" value="${data.middleware_id}" maxlength="11" /> --%>
									<li>
										<u:select id="middleware_id" value="${data.middleware_id}" sqlId="findAllMqMiddleware"/>
									</li>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">MQ队列</label>
								<div class="controls">
									<input type="text" name="mq_queue" value="${data.mq_queue}" maxlength="100" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">MQ交换</label>
								<div class="controls">
									<input type="text" name="mq_exchange" value="${data.mq_exchange}" maxlength="100" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">MQ路由KEY</label>
								<div class="controls">
									<input type="text" name="mq_routingkey" value="${data.mq_routingkey}" maxlength="100" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">名称</label>
								<div class="controls">
									<input type="text" name="queue_name" value="${data.queue_name}" maxlength="100" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">消息类型</label>
								<div class="controls">
									<u:select id="message_type" data="[{value:'',text:'请选择消息类型'},{value:'00',text:'DB消息'},{value:'01',text:'移动行业'},{value:'02',text:'移动营销'},
																		{value:'03',text:'联通行业'},{value:'04',text:'联通营销'},{value:'05',text:'电信行业'},
																		{value:'06',text:'电信营销'},{value:'07',text:'行业'},{value:'08',text:'营销'},
																		{value:'11',text:'异常移动行业'},{value:'12',text:'异常移动营销'},{value:'13',text:'异常联通行业'},
																		{value:'14',text:'异常联通营销'},{value:'15',text:'异常电信行业'},{value:'16',text:'异常电信营销'},
																		{value:'20',text:'通道消息'},{value:'21',text:'上行消息'},{value:'22',text:'状态报告缓存'}]"
									value="${data.message_type}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">描述</label>
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
								<input type="hidden" name="mq_id" value="${data.mq_id}">
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
			middleware_id:{
				required:true,
				maxlength:11
			},
			mq_queue:{
				required:true,
				maxlength:100
			},
			mq_exchange:{
				required:true,
				maxlength:100
			},
			mq_routingkey:{
				required:true,
				maxlength:100
			},
			queue_name:{
				required:true,
				maxlength:100
			},
			message_type:{
				required:true
			},
			remark:{
				maxlength:100
			}
		},
		messages: {
			middleware_id : '请选择MQ中间件'
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
			if(data.result = "success"){
				$("input[name='mq_id']").val(data.mq_id);
			}
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/componentConf/mqConf/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>