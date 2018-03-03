<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>销售人员管理</title>
</head>
<body menuId="54">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<!-- 删除销售人员模太框（Modal） -->
		<div class="modal fade" id="delSalesman" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<form method="post" id="delSalesmanForm">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="myModalLabel">删除销售人员</h4>
						</div>
						<div class="modal-body form-horizontal">
							<div class="control-group">
				                <label class="control-label">将该销售的客户交接给：</label>
				                <div class="controls" name="handOverToId">
				                  <u:select id="handOverToId" placeholder="销售人员" sqlId="findAllSalesman" sqlParams=""/>
				                </div>
				            </div>
						</div>
						<div class="controls text-center">
							<span id="delMsg" style="color:red;" style="display:none;"></span>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal" onclick="close()">关闭
							</button>
							<button id="btnDelSalesMan" type="button" class="btn btn-primary" onclick="delSalesman()">交接并删除</button>
						</div>
			        </form>
				</div>
			
			</div>
			
		</div>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<span class="label label-info">
						<u:authority menuId="1">
							<a href="javascript:;" onclick="add(this)">添加销售人员</a>
						</u:authority>
						</span>
						<div class="search">
						  <form method="post" id="mainForm" action="${ctx}/salesman/query">
							<ul>
								<li><input type="text" name="name" value="<s:property value="#parameters.name"/>"  maxlength="50" placeholder="销售人员姓名" class="txt_250" /></li>
								<li><input type="submit" value="搜索" /></li>
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>姓名</th>
									<th>Email</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${name}</td>
										<td>${email}</td>
										<td>
							     	  		<a href="javascript:;" data-toggle="modal" data-target="#delSalesman" onclick="getDelSalesmanId('${id}')" >删除</a> 
							      		</td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
					<u:page page="${page}" formId="mainForm" />
				</div>
			</div>
		</div>
	</div>
	

	<script type="text/javascript">
		var validate;
		$(function() {
			$.validator.defaults.ignore = "";
			//表单验证规则
			validate = $("#delSalesmanForm").validate({
				rules : {
					handOverToId : "required"
				},
				messages : {
					handOverToId : "必须"
				}
			});
			
			// 删除销售人员-交接客户-隐藏自己选项
			$('#delSalesman').on('show.bs.modal', function () {
				  $("#handOverToId option[value= " + salesmanId + "]").hide();
			})
			// 删除销售人员-交接客户-恢复自己选项
			$('#delSalesman').on('hide.bs.modal', function () {
				  $("#handOverToId option[value= " + salesmanId + "]").show();
				  window.location.reload();
			})
			
		});
		
		var salesmanId;
		function getDelSalesmanId( id ){
			salesmanId = id;
		}
		
		//添加
		function add(btn) {
			location.href = "${ctx}/salesman/add";
		}
		
		//删除
		function delSalesman(){
			if (!validate.form()) {
				return;
			}
			var id = salesmanId;
			var handOverToId = $("#handOverToId option:selected").val();
			$.ajax({
				type : "post",
				url : "${ctx}/salesman/delete",
				data : {
					id : id,
					handOverToId : handOverToId
				},
				success : function(data) {
					if (data == null || data.result == "fail") {
						alert("服务器错误，请联系管理员");
						return;
					}
// 					alert(data.msg);
					if (data.result == "success") {
						$("#handOverToId").attr("disabled","disabled");
						$("#btnDelSalesMan").attr("disabled","disabled");
					}
					$("#delMsg").text(data.msg).show();
				}
			});
			
		}
	</script>
</body>
</html>