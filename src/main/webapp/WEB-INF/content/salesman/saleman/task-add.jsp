<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>添加月回款任务</title>
</head>
<body menuId="58">
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
						<form class="form-horizontal" method="post"	id="mainForm">
							<div class="control-group">
								<label class="control-label">销售人员：</label>
								<div class="controls salesman" name="salesman_id">
									<u:select id="salesman_id" placeholder="销售人员" sqlId="findAllSalesman" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">月回款任务额：</label>
								<div class="controls">
									<input type="text" name="returned_money_task"
										maxlength="50" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">月份：</label>
								<div class="controls" name="taskofmoth">
									<u:date id="taskofmoth" value="${taskofmoth}" readOnly="false" dateFmt="yyyyMM" params="" />
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
					salesman_id : "required",
					returned_money_task : {"required":true, "digits":true, min:1, max:2000000000},
					taskofmoth : "required"
				},
				messages : {
					salesman_id : "请选择销售人员",
					returned_money_task : "请输入大于0小于2000000000的正整数"
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
				url : "${ctx}/salesman/task/save",
				type : "post",
				timeout : 30000
			};
			$("#mainForm").ajaxSubmit(options);
		};
	</script>
</body>
</html>