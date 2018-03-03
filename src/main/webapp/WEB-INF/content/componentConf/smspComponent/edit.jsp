<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}SMSP组件配置</title>
</head>

<body menuId="237">
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
							<s:if test="data.id != null">
								<div id="compnonetDiv" class="control-group">
									<label class="control-label">组件ID</label>
									<div class="controls">
										<input type="text" name="component_id" value="${data.component_id}"/>
									</div>
								</div>
							</s:if>
							<s:else>
								<div id="compnonetDiv" class="control-group hide">
									<label class="control-label">组件ID</label>
									<div class="controls">
										<input type="text" name="component_id" value="${data.component_id}"/>
									</div>
								</div>
							</s:else>
							
							<div class="control-group">
								<label class="control-label">组件类型</label>
								<div class="controls">
									<u:select id="component_type" data="[{value:'00',text:'SMSP_C2S'},{value:'01',text:'SMSP_ACCESS'},{value:'02',text:'SMSP_SEND'},
																		{value:'03',text:'SMSP_REPORT'},{value:'04',text:'SMSP_AUDIT'},{value:'05',text:'SMSP_CHARGE'},
																		{value:'06',text:'SMSP_CONSUMER'},{value:'07',text:'SMSP_REBACK'},{value:'08',text:'SMSP_HTTP'}]" 
									value="${data.component_type}" showAll="false" onChange="componentTypeOnChange"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">组件名称</label>
								<div class="controls">
									<input type="text" name="component_name" value="${data.component_name}" maxlength="100" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">组件IP地址</label>
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
								<label class="control-label">所属机房</label>
								<div class="controls">
									<u:select id="node_id" data="[{value:'10',text:'北京亦庄'},{value:'11',text:'北京兆维'},{value:'12',text:'北京互联港湾'}]" 
									value="${data.node_id}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">连接REDIS线程数量</label>
								<div class="controls">
									<input type="text" name="redis_thread_num" value="${data.redis_thread_num}" maxlength="11" />
								</div>
							</div>
							
							<div id="sgipDiv" class="control-group">
								<label class="control-label">SGIP状态报告开关</label>
								<div class="controls">
									<u:select id="sgip_report_switch" data="[{value:'0',text:'关闭'},{value:'1',text:'开启'}]" 
									value="${data.sgip_report_switch}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">MQ_ID</label>
								<div class="controls">
									<u:select id="mq_id" placeholder="" value="${data.mq_id}" sqlId="findAllMqId"/>
									<span class="help-block">MQ_ID用于关联SMSP_C2S/SMSP_HTTP组件与MQ上行队列</span>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">消费者开关</label>
								<div class="controls">
									<u:select id="component_switch" defaultIndex="2" data="[{value:'0',text:'关闭'},{value:'1',text:'开启'}]" 
									value="${data.component_switch}" showAll="false"/>
								</div>
							</div>
							
							<div id="blackListDiv" class="control-group">
								<label class="control-label">黑名单加载开关</label>
								<div class="controls">
									<u:select id="black_list_switch" data="[{value:'0',text:'关闭'},{value:'1',text:'开启'}]" 
									value="${data.black_list_switch}" showAll="false"/>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">&nbsp;</label>
								<div class="controls">
									<span id="msg" class="error" style="display: none;"></span>
								</div>
							</div>
							
							<div class="form-actions">
								<input type="hidden" name="id" value="${data.id}" />
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
			component_id:{
				componentIdValidator1:true,
				componentIdValidator2:true
			},
			component_type:{
				required:true
			},
			component_name:{
				required:true,
				maxlength:100
			},
			host_ip:{
				required:true,
				checkip:true,
				maxlength:50
			},
			node_id:{
				required:true
			},
			redis_thread_num:{
				maxlength:11,
				digits:true,
				range:[1,2147483647]
			},
			sgip_report_switch:{
				required:true
			},
			mq_id:{
				checkMqId:true,
				maxlength:11,
				digits:true,
				range:[1,2147483647]
			}
		}
	});
	
	
	jQuery.validator.addMethod("checkip",
			function(value, element) {
				var reg = /^(((1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)))$/;;
				return this.optional(element) || (reg.test(value));
			}, "请输入合法的ip地址");
	
	jQuery.validator.addMethod("checkMqId",
			function(value, element) {
				var componentType = $("#component_type").val();
				if(componentType == '00' || componentType == '08'){
					if($.trim(value) == ''){
						return false;
					}
				}
				
				return true;
			}, "组件类型为SMSP_C2S和SMSP_HTTP时MQ_ID不能为空");
	
	jQuery.validator.addMethod("componentIdValidator1",
			function(value, element) {debugger;
				if($("#compnonetDiv").hasClass("hide")){
					return true;
				}else{
					if($.trim(value) == ""){
						return false;
					}else{
						return true;
					}
				}
			}, "必须填写");
	
	jQuery.validator.addMethod("componentIdValidator2",
			function(value, element) {
				if($("#compnonetDiv").hasClass("hide")){
					return true;
				}else{
					var reg = /^[1-9]*[1-9][0-9]*$/;
					if(reg.test(value)){
						if(value<1 || value>2147483647){
							return false;
						}
						return true;
					}
					return false;
				}
			}, "请输入 1 至 2147483647 之间的数值");
	
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
			if(data.result == 'success'){
				$("input[name='id']").val(data.id);
				$("input[name='component_id']").val(data.componentId);
			}
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/componentConf/smspComponent/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}

function componentTypeOnChange(value, text, isInit){
	if(value == '02'){
		$("#sgipDiv").show();
	}else{
	    $("#sgip_report_switch").val(0);
		$("#sgipDiv").hide();
	}
	
	if(value == '06'){
		$("#blackListDiv").show();
	}else{
	    $("#black_list_switch").val(0);
		$("#blackListDiv").hide();
	}
	
}

</script>
</body>
</html>