<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}测试号码</title>
</head>

<body menuId="268">
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
								<label class="control-label">手机号码</label>
								<div class="controls">
									<input type="text" name="phone" value="${data.phone}"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">联系人</label>
								<div class="controls">
									<input type="text" name="name" value="${data.name}"/>
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
		$(function() {
			$.validator.defaults.ignore = "";
			//表单验证规则
			validate = $("#mainForm").validate({
				rules : {
					phone : {
						required:true,
						checkPhone: true
					},
					name : {
						required:true,
						maxlength:50
					}
				}
			});
			
			jQuery.validator.addMethod("checkPhone",
					function(value, element) {
						var reg1 = /^1[34578]\d{9}$/; 
						var reg2 = /^0{2}\d{8,18}$/; // 国际号码  00开头大于10位
						if(reg1.test(value) || reg2.test(value)){
							return true;
						}else{
							return false;
						}
					}, "请输入合法的手机号码");

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
				url : "${ctx}/channelTest/testPhone/save",
				type : "post",
				timeout : 30000
			};
			$("#mainForm").ajaxSubmit(options);
		};
	</script>
</body>
</html>