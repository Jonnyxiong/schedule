<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
<s:if test="data.onlyView == 1">
	<title>查看测试通道</title>
</s:if>
<s:else>
	<title>${data.channel_id==null?'添加':'编辑'}测试通道</title>
</s:else>
<%-- <title>${data.channel_id==null?'添加':'编辑'}测试通道</title> --%>
<style type="text/css">
/* 	body{ */
/* 		background: #F8F8F8 !important; */
/* 	} */
	.fixRow {
		margin-left: 0px;
	}
	.clear{ clear:both }
	
	#shareDetailsIframe .ckBox{
		display: inline;
	}
</style>
</head>

<body menuId="266">
	<!--Action boxes-->
	<div id="shareDetailsIframe" class="container-fluid">
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
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道名称：</label>
										<div class="controls">
											<input type="text" name="channel_name" value="${data.channel_name}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道账号：</label>
										<div class="controls">
											<input type="text" name="account" value="${data.account}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道密码：</label>
										<div class="controls">
											<input type="text" name="password" value="${data.password}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">运营商类型：</label>
										<div class="controls">
											<u:select id="operators_type" dictionaryType="channel_operatorstype" value="${data.operators_type}" placeholder="类型" excludeValue="5,6,7" />
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道接入号：</label>
										<div class="controls">
											<input type="text" name="access_id" value="${data.access_id}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">企业代码：</label>
										<div class="controls">
											<input type="text" name="sp_id" value="${data.sp_id}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">服务代码：</label>
										<div class="controls">
											<input type="text" name="service_id" value="${data.service_id}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">接入IP：</label>
										<div class="controls">
											<input type="text" name="mt_ip" value="${data.mt_ip}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">端口：</label>
										<div class="controls">
											<input type="text" name="mt_port" value="${data.mt_port}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">节点编码：</label>
										<div class="controls">
											<input type="text" name="node_id" value="${data.node_id}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道流量：</label>
										<div class="controls">
											<input type="text" name="speed" value="${data.speed}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">支持状态报告：</label>
										<div class="controls">
											<u:select id="is_report" dictionaryType="is_support" value="${data.is_report}" showAll="false"/>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">支持上行：</label>
										<div class="controls">
											<u:select id="is_mo" dictionaryType="is_support" value="${data.is_mo}" showAll="false"/>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">支持长短信：</label>
										<div class="controls">
											<u:select id="is_long_sms" dictionaryType="is_support" value="${data.is_long_sms}" showAll="false"/>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">SMSP-TEST组件IP：</label>
										<div class="controls">
											<input type="text" name="test_url" value="${data.test_url}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">SMSP-TEST组件端口：</label>
										<div class="controls">
											<input type="text" name="test_port" value="${data.test_port}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">接入协议：</label>
										<div class="controls">
											<u:select id="protocol_type" data="[{value:'3',text:'CMPP'}, {value:'4',text:'SMGP'}, {value:'5',text:'SGIP'}, {value:'6',text:'SMPP'}]" value="${data.protocol_type}" showAll="false"/>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">支持自扩展位数：</label>
										<div class="controls">
											<input type="text" name="extend_size" value="${data.extend_size}">
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">是否需要加86：</label>
										<div class="controls">
											<u:select id="need_prefix" data="[{value:'0',text:'否'}, {value:'1',text:'是'}]" value="${data.need_prefix}" showAll="false"/>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道说明：</label>
										<div class="controls">
											<input type="text" name="remark" value="${data.remark}" maxlength="50" />
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
				                <label class="control-label">&nbsp;</label>
				                <div class="controls">
				                  <span id="msg" class="error" style="display:none;"></span>
				                </div>
				            </div>
				            
				            <div class="form-actions">
				              	<input type="hidden" name="id" value="${data.channel_id}">
				                <input type="button" id="saveBtn" onclick="save(this);" value="保  存" class="btn btn-success">
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
	
	validate = $("#mainForm").validate({
		rules: {
			channel_name:{
				required: true,
				maxlength: 255
			},
			account: {
				required: true,
				maxlength: 30
			},
			password: {
				required: true,
				maxlength: 30
			},
			operators_type : {
				required: true
			},
			access_id: {
				maxlength: 21
			},
			sp_id: {
				maxlength: 30
			},
			service_id: {
				maxlength: 10
			},
			extend_size: {
				required: true,
				range:[0, 12],
				digits: true
			},
			mt_ip: {
				required: true,
				maxlength: 32,
				checkIp: true
			},
			mt_port: {
				required: true,
				digits: true,
				range:[0, 2147483647] 
			},
			node_id: {
				digits: true,
				range:[0, 2147483647]
			},
			speed: {
				required: true,
				range:[1, 1000] 
			},
			test_url:{
				required: true,
				checkIp: true
			},
			test_port: {
				required: true,
				range:[0, 2147483647] 
			},
			remark: {
				maxlength: 50
			}
			
		},
		messages: {
		}
	})
	
	jQuery.validator.addMethod( "checkIp",
			function(value, element) {
				var ipReg = /^(((1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d|\*|\d+\/\d+)))$/;
				var ipRegExp = new RegExp(ipReg);
				return this.optional(element) || (ipRegExp.test(value));

			}, "请输入合法的IP");
})
function save(btn){
	var saveResult = {
		code : 1, // 1 表单校验失败  2保存成功  3保存失败
		msg : "请检查参数填写是否正确"
	};
		
	if(!validate.form()){
		return saveResult;
	}
	
	if($("#saveBtn").hasClass("disabled")){
		return false;
	}else{
	   	$("#saveBtn").addClass("disabled");
	}
	
	var options = {
		beforeSubmit : function() {
// 			$(btn).attr("disabled", true);
// 			$("#saveBtn").hasClass("disabled")
		},
		success : function(data) {
// 			$(btn).attr("disabled", false);
			$("#saveBtn").removeClass("disabled");
			
			if(data.result==null){
				$("#msg").text("服务器错误，请联系管理员").show();
				saveResult.code = 3;
				saveResult.msg = "服务器错误，请联系管理员";
				return;
			}
			
			if(data.result == 'success'){
				saveResult.code = 2;
				saveResult.msg = "保存成功";
			}
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/channelTest/save",
		type : "post",
		async : false,
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
	
	return saveResult;
};
</script>
</body>
</html>