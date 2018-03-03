<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>添加客户</title>
</head>
<body menuId="55">
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
						<form class="form-horizontal" method="post"
							id="mainForm">
							<div class="control-group">
								<label class="control-label">客户名称：</label>
								<div class="controls">
									<input type="text" placeholder="请输客户名称" name="customer_name"
										maxlength="50" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">销售人员邮箱：</label>
								<div class="controls salesman" name="salesman_id">
									<!-- <input type="text" placeholder="请输入销售人员Email" name="salesman_email"
										maxlength="50" /> -->
									<u:select id="salesman_id" placeholder="销售人员" sqlId="findAllSalesman" sqlParams=""/>
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
					customer_name : "required",
					salesman_id : "required"
				}
			});
			
			// select2下拉搜索框
			$("#salesman_id").select2();
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
				url : "${ctx}/customer/save",
				type : "post",
				timeout : 30000
			};
			$("#mainForm").ajaxSubmit(options);
		};
	</script>
</body>
</html>