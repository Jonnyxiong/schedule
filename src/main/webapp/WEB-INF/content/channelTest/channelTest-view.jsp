<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
<title>查看测试通道</title>
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
											<span>${data.channel_name}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道账号：</label>
										<div class="controls">
											<span>${data.account}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道密码：</label>
										<div class="controls">
											<span>${data.password}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">运营商类型：</label>
										<div class="controls">
											<span><u:ucparams key="${data.operators_type}" type="channel_operatorstype"></u:ucparams></span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道接入号：</label>
										<div class="controls">
											<span>${data.access_id}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">企业代码：</label>
										<div class="controls">
											<span>${data.sp_id}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">服务代码：</label>
										<div class="controls">
											<span>${data.service_id}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">接入IP：</label>
										<div class="controls">
											<span>${data.mt_ip}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">端口：</label>
										<div class="controls">
											<span>${data.mt_port}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">节点编码：</label>
										<div class="controls">
											<span>${data.node_id}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道流量：</label>
										<div class="controls">
											<span>${data.speed}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">支持状态报告：</label>
										<div class="controls">
											<span><u:ucparams key="${data.is_report}" type="is_support"></u:ucparams></span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">支持上行：</label>
										<div class="controls">
											<span><u:ucparams key="${data.is_mo}" type="is_support"></u:ucparams></span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">支持长短信：</label>
										<div class="controls">
											<span><u:ucparams key="${data.is_long_sms}" type="is_support"></u:ucparams></span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">SMSP-TEST组件IP：</label>
										<div class="controls">
											<span>${data.test_url}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">SMSP-TEST组件端口：</label>
										<div class="controls">
											<span>${data.test_port}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">接入协议：</label>
										<div class="controls">
											<span>
												<s:if test="data.protocol_type eq 3">CMPP</s:if>
												<s:if test="data.protocol_type eq 4">SMGP</s:if>
												<s:if test="data.protocol_type eq 5">SGIP</s:if>
												<s:if test="data.protocol_type eq 6">SMPP</s:if>
											</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">支持自扩展位数：</label>
										<div class="controls">
											<span>${data.extend_size}</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">是否需要加86：</label>
										<div class="controls">
											<span>
												<s:if test="data.need_prefix == 0">否</s:if>
												<s:if test="data.need_prefix eq 1">是</s:if>
											</span>
										</div>
									</div>
								</div>
							</div>
							
							<div class="control-group">
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">通道说明：</label>
										<div class="controls">
											<span>${data.remark}</span>
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
				                <input type="button" value="返 回" class="btn btn-error" onclick="back()"/>
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
	
	var options = {
		beforeSubmit : function() {
			$(btn).attr("disabled", true);
		},
		success : function(data) {debugger;
			$(btn).attr("disabled", false);
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