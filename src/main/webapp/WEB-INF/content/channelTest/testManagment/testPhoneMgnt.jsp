<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
</head>
<body menuId="268">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
			<ul class="nav nav-tabs nav-justified" role="tablist">
				<li><a href="${ctx}/channelTest/testManageMent">测试模板管理</a></li>
				<li class="active"><a href="${ctx}/channelTest/testManageMent-tab2">测试号码管理</a></li>
				<li><a href="${ctx}/channelTest/testManageMent-tab3">测试短信发送记录</a></li>
				<li><a href="${ctx}/channelTest/testManageMent-tab4">测试上行短信记录</a></li>
			</ul>
		</nav>
	
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<!-- 批量导入弹出框 -->
					<div class="modal hide fade" id="importExcelBox" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="keyboard">
						<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
							<h3 id="myModalLabel">批量测试手机号码 </h3>
<!-- 							<font color="red">*批量导入耗费系统资源较多，请避免在系统高峰期使用</font> -->
						</div>
						<div class="modal-body text-center">
							<form class="form-horizontal" method="post" id="mainForm">
								<input type="file" id="importInput" name="upload" accept=".xls"/>
							</form>
							<div class="controls">
								<span id="msg" style="color:red;" style="display:none;"></span><br/>
							</div>
						</div>
						<div class="modal-footer">
						<a href="#" class="btn" onclick="downLoadTemplate()">下载Excel模板</a>
						<a href="#" class="btn" data-dismiss="modal" onclick="closeImport()">关闭</a>
						<a href="#" class="btn btn-primary" onclick="importExcel(this)">导入</a>
						</div>
					</div>
					
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<span class="label label-info" data-toggle="modal" data-target="#importExcelBox" >
							<a href="javascript:;" onclick="">批量导入号码</a>
						</span>
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加</a>
						</span>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelTest/testManageMent-tab2">
								<ul>
									<li><u:select id="operators_type" data="[{value:'',text:'运营商类型：所有'}, {value:'1',text:'移动'}, {value:'2',text:'联通'}, {value:'3',text:'电信'}, {value:'4',text:'国际'}]" value="${operators_type_search}" showAll="false"/></li>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="手机号码/联系人" class="txt_250" /></li>
									<li><input type="submit" value="搜索"/></li>
								</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									 <th>序号</th>
									 <th>手机号码</th>
					                 <th>联系人</th>
					                 <th>运营商</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${phone}</td>
										<td>${name}</td>
										<td>
											<s:if test="operators_type eq 1">移动</s:if>
											<s:elseif test="operators_type eq 2">联通</s:elseif>
											<s:elseif test="operators_type eq 3">电信</s:elseif>
											<s:elseif test="operators_type eq 4">国际</s:elseif>
										</td>
										<td>${update_time}</td>
										<td>
											<a href="javascript:;" onclick="editPhone('${id}')">编辑&nbsp;&nbsp;&nbsp;</a>
											<a href="javascript:;" onclick="delPhone('${id}', '${phone}')">删除</a>
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
	
	<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
	<script type="text/javascript">
		
		$(function(){
			$('#importExcelBox').on('hide.bs.modal', function () {
				  closeImport();
			})
		});
			
		//添加
		function add() {
			location.href = "${ctx}/channelTest/testPhone/edit";
		}
		
		//编辑
		function editPhone(id) {
			location.href = "${ctx}/channelTest/testPhone/edit?id=" + id;
		}

		function downLoadTemplate(){
			var url = "${ctx}/channelTest/downloadTestPhoneTemplate";
			jQuery('<form action="'+url+'" method="post"></form>')
	              .appendTo('body').submit().remove();
		}
		
		//Excel批量导入
		function importExcel(btn) {
			$("#msg").hide();
			var indexs = "" ;
			var options = {
					beforeSubmit : function() {
						$(btn).attr("disabled", true);
						index = layer.load(1, {
						    shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
					},
					success : function(data) {
						$(btn).attr("disabled", false);debugger;
						layer.close(index);
						if(data==null){
							$("#msg").text("服务器错误，请联系管理员").show();
							return;
						}
						$("#msg").text(data.msg).show();
					},
					url : "${ctx}/channelTest/testPhone/import",
					type : "post",
					timeout:300000
				};
			$("#mainForm").ajaxSubmit(options);
		}
		
		function closeImport(){
			$("#msg").hide();
			window.location.reload();
		}
		
		// 删除测试手机号码
		function delPhone(id, phone){
			if (confirm("确定要删除测试手机号码：" + phone + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/channelTest/testPhone/delete",
					data : {
						id : id,
						phone : phone
					},
					success : function(data) {
						if (data.result == null) {
							alert("服务器错误，请联系管理员");
							return;
						}
						alert(data.msg);
						if (data.result == "success") {
							window.location.reload();
						}
					}
				});
			}
		}
	
	//@ sourceURL=testPhoneMgnt.js
	</script>
</body>
</html>