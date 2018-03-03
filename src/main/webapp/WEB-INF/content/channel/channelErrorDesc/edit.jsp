<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}通道错误状态关系</title>
</head>

<body menuId="70">
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
								<label class="control-label">通道号</label>
								<div class="controls">
									<input type="text" name="channelid" value="${data.channelid}"
										class="checkChannelid" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">错误类型</label>
								<div class="controls">
									<u:select id="type" data="[{value:'1',text:'应答'},{value:'2',text:'状态报告'}]" value="${data.type}" showAll="false"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">错误代码</label>
								<div class="controls">
									<input type="text" name="errorcode" value="${data.errorcode}" maxlength="32" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">错误描述</label>
								<div class="controls">
									<input type="text" name="errordesc" value="${data.errordesc}"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">SMSP平台错误码</label>
								<div class="controls">
<%-- 									<u:select id="syscode" placeholder="" value="${data.syscode}" sqlId="findAllSysErrorCode"/> --%>
									<input type="text" name="syscode" value="${data.syscode}" maxlength="7" />
								</div>
							</div>
							

							<div class="control-group">
								<label class="control-label">&nbsp;</label>
								<div class="controls">
									<span id="msg" class="error" style="display: none;"></span>
								</div>
							</div>
							<div class="form-actions">
								<input type="hidden" name="id" value="${data.id}"> <input
									type="button" onclick="save(this);" value="保  存"
									class="btn btn-success"> <input type="button"
									value="取 消" class="btn btn-error" onclick="back()" />
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>



	<script type="text/javascript">
		var validate;
		$(function() {
			$.validator.defaults.ignore = "";
			//表单验证规则
			validate = $("#mainForm").validate({
				rules : {
					channelid : "required",
					errorcode : {
						required:true,
						maxlength:32
					},
					errordesc : {
						required:true,
						maxlength:30
					}
				}
			});

			// 通通号输入框既可以输入通道号也可以输入协议类型（CMPP）
			jQuery.validator.addMethod("checkChannelid", function(value,
					element) {
				var reg = /^[a-zA-Z0-9]{1,10}$/;
				return this.optional(element) || (reg.test(value));
			}, "请输入1-10位的通道号或者协议类型");
			
			$("#syscode").select2({});
		});

		function save(btn) {
			$("#msg").hide();
			if (!validate.form()) {
				return;
			}
			var options = {
				beforeSubmit : function() {
					$(btn).attr("disabled", true);
				},
				success : function(data) {
					$(btn).attr("disabled", false);

					if (data.result == null) {
						$("#msg").text("服务器错误，请联系管理员").show();
						return;
					}
					if (data.result == 'success'){
						$("input[name='id']").val(data.id);
					}
					$("#msg").text(data.msg).show();
				},
				url : "${ctx}/channelErrorDesc/save",
				type : "post",
				timeout : 30000
			};
			$("#mainForm").ajaxSubmit(options);
		};
	</script>
</body>
</html>