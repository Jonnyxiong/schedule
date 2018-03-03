<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


<html>
<head>
<title>添加充值记录</title>
</head>
<body menuId="56">
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
								<div class="controls" name="customer_id">
									<u:select id="customer_id" placeholder="客户" sqlId="findAllCustomer" onChange="selectSalesman"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">销售人员：</label>
								<div class="controls" name="salesman_id">
									<%-- <u:select id="salesman_id" placeholder="销售人员" sqlId="findAllSalesman" sqlParams=""/> --%>
									<input id="salesman_name" value="${salesman_name}" type="text"  disabled="disabled" >
									<input id="salesman_id" name="salesman_id" value="${salesman_id}" type="text" style="display:none;">
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">充值金额：</label>
								<div class="controls">
									<input type="text" name="recharge_money"
										maxlength="50" onfocus="inputControl.setNumber(this, 10, 3, false)" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">当月充值轮次：</label>
								<div class="controls">
									<input type="text" name="recharge_mark"
										maxlength="50" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">此次充值合同单价：</label>
								<div class="controls">
									<input type="text" id="recharge_unit_price" name="recharge_unit_price" class="priceValidator"
										maxlength="50" onfocus="inputControl.setNumber(this, 10, 3, false)" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">此次充值成本价：</label>
								<div class="controls">
									<input type="text" id="recharge_cost_price" name="recharge_cost_price"
										maxlength="50" onfocus="inputControl.setNumber(this, 10, 3, false)" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">充值时间：</label>
								<div class="controls" name="rechargetime">
									<u:date id="rechargetime" value="${rechargetime}" readOnly="false" dateFmt="yyyy-MM" params="" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">是否上月余额：</label>
								<div class="controls" name="is_remain">
				                  <u:select id="is_remain" data="[{value:'0',text:'否'},{value:'1',text:'是'}]" showAll="false" />
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
					customer_id : "required",
					recharge_money : {required : true, number : true , min : 0},
					recharge_mark : "required",
					recharge_unit_price : {required : true, number : true , min : 0},
					recharge_cost_price : {required : true, number : true , min : 0},
					rechargetime : "required"
				},
				messages : {
					customer_id : "请选择客户",
					recharge_money : {required : "请输入充值金额", number : "请输入合法充值金额" , min : "请输入合法数字金额"},
					recharge_mark : "请输入充值轮次",
					recharge_unit_price : {required : "请输入合同单价", number : "请输入合法合同单价" , min : "请输入合法合同单价"},
					recharge_cost_price : {required : "请输入成本单价", number : "请输入合法成本单价" , min : "请输入合法成本单价"},
					rechargetime : "请选择充值时间"
				}
			});
			
			// 充值价格校验
			jQuery.validator.addMethod("priceValidator", function(value, element) {
			    var recharge_cost_price = $("#recharge_cost_price").val();
			    var recharge_unit_price = $("#recharge_unit_price").val();
			    return this.optional(element) || ((recharge_unit_price - recharge_cost_price) >= 0);
			}, "充值合同单价须大于等于充值成本价");
			
			// select2下拉搜索框
			$("#customer_id").select2();
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
				url : "${ctx}/recharge/save",
				type : "post",
				timeout : 30000
			};
			$("#mainForm").ajaxSubmit(options);
		};
		
		function selectSalesman(value, text){
			$.ajax({
				type : "post",
				url : "${ctx}/recharge/getsalesmanbyid",
				data : {
					customer_id : value
				},
				success : function(data) {
					$("#salesman_id").val(data.salesman_id);
					$("#salesman_name").val(data.salesman_name);
					
				}
			});
			
		};
	</script>
</body>
</html>