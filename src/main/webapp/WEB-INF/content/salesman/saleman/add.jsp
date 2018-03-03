<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>添加销售人员</title>
</head>
<body menuId="54">
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
								<label class="control-label">姓名：</label>
								<div class="controls">
									<input type="text" placeholder="请输入姓名" name="name"
										maxlength="50" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Email：</label>
								<div class="controls">
									<input type="text" placeholder="请输入公司邮箱" name="email" class="emailValidator"
										maxlength="50" />
								</div>
							</div>
							<div class="control-group">
				                <label class="control-label">&nbsp;</label>
				                <div class="controls">
				                  <span id="msg" class="error" style="display:none;"></span>
				                </div>
				            </div>
							<div class="form-actions">
								<input type="button" value="确定" class="btn btn-success"
									class="btn btn-success" onclick="save(this)" /> <input
									type="button" value="取 消" class="btn btn-error"
									onclick="back()" />
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
					name : "required",
					email : "required"
				}
			});
			
			// 公司邮箱校验
			jQuery.validator.addMethod("emailValidator", function(value, element) {
			    var reg = /^([a-zA-Z0-9_-])+@ucpaas.com$/;
			    return this.optional(element) || (reg.test(value));
			}, "请输入合法的公司邮箱");
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

					if (data == null) {
						$("#msg").text("服务器错误，请联系管理员").show();
						return;
					}

					$("#msg").text(data.msg).show();
				},
				url : "${ctx}/salesman/save",
				type : "post",
				timeout : 30000
			};
			$("#mainForm").ajaxSubmit(options);
		};
	</script>
</body>
</html>