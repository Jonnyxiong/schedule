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
				<li class="active"><a href="${ctx}/channelTest/testManageMent">测试模板管理</a></li>
				<li><a href="${ctx}/channelTest/testManageMent-tab2">测试号码管理</a></li>
				<li><a href="${ctx}/channelTest/testManageMent-tab3">测试短信发送记录</a></li>
				<li><a href="${ctx}/channelTest/testManageMent-tab4">测试上行短信记录</a></li>
			</ul>
		</nav>
	
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelTest/testManageMent">
								<ul>
<!-- 									<li><button type="button" class="btn btn-success btn-sm" onclick="addTemplate()">添加模板</button></li> -->
									<li><u:select id="type" data="[{value:'',text:'模板类型：所有'}, {value:'0',text:'通知模板'}, 
																   {value:'4',text:'验证码模板'}, {value:'5',text:'营销模板'}, {value:'6',text:'告警模板'}]" value="${type_search}" showAll="false"/></li>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="模板ID/模板名称" class="txt_250" /></li>
									<li><input type="submit" value="搜索"/></li>
									<li>
										<span class="label label-info">
											<a href="javascript:;" onclick="addTemplate()">添加模板</a>
										</span>
									</li>
								</ul>
								
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									 <th>模板ID</th>
									 <th>模板名称</th>
					                 <th>模板类型</th>
					                 <th>模板签名</th>
					                 <th>模板内容</th>
					                 <th>创建时间</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${template_id}</td>
										<td>${template_name}</td>
										<td>
											<s:if test="type eq 0">通知模板</s:if>
											<s:elseif test="type eq 4">验证码模板</s:elseif>
											<s:elseif test="type eq 5">营销模板</s:elseif>
											<s:elseif test="type eq 6">告警模板</s:elseif>
										</td>
										<td>${sign}</td>
										<td>${content}</td>
										<td>${create_time}</td>
										<td>${update_time}</td>
										<td>
											<a href="javascript:;" onclick="delTemplate('${template_id}','${template_name}')">删除</a>
											<a href="javascript:;" onclick="editTemplate('${template_id}','${template_name}','${type}','${sign}','${content}')">编辑</a>
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
	
	<div id="addTemplateBox" class="container-fluid" style="display:none;">
			<div class="row">
				<div>
					<form id="templateForm" class="form-horizontal">
						<div class="control-group">
							 <label class="control-label">模板名称：</label>
							<div class="controls">
								<input id="template_name" name="template_name" type="text" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">模板类型：</label>
							<div class="controls">
								<u:select id="template_type" data="[{value:'0',text:'通知模板'},{value:'4',text:'验证码模板'},
																	{value:'5',text:'营销模板'},{value:'6',text:'告警模板'}]" showAll="false"/>
							</div>
						</div>
						<div class="control-group">
							 <label class="control-label">模板签名：</label>
							<div class="controls">
								<input id="sign" name="sign" type="text" />
								<span class="help-block">签名长度为2至12个字符，不能包含【、】</span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" >模板内容：</label>
							<div class="controls">
								 <textarea id="template_content" name="template_content" rows="5" cols="40" style="height: auto; width: auto;" maxlength="1000"></textarea>
								 <span class="help-block">内容中可以使用{1}、{2}、{3}依次类推占位符替代可变参数<br>占位符中数字必须从1开始</span>
							</div>
						</div>
						<div class="control-group">
			                <label class="control-label">&nbsp;</label>
			                <div class="controls">
			                  <span id="msg" class="error" style="display:none;"></span>
			                </div>
			            </div>
					</form>
				</div>
			</div>
	</div>

	<script type="text/javascript">
		var validate;
		$(function(){
			
			$.validator.defaults.ignore = "";
			validate = $("#templateForm").validate({
				rules : {
					template_name : {
						required : true,
						maxlength : 50
					},
					sign : {
						required : true,
						signValidator:true,
// 						maxlength : 12
						rangelength : [2, 12]
					},
					template_content : {
						templateContentValidator1:true,
						templateContentValidator2:true,
						required : true,
						maxlength : 1000
					}
				},
				messages : {
					sign : {
						rangelength : "请输入长度为 2 至 12 之间的签名"	
					}
				}
			});
			
			// 短信签名校验
			jQuery.validator.addMethod("signValidator",
					function(value, element) {
						
						var reg = RegExp("([\\【])|([\\】])");
						if(reg.test(value)){
							return false;
						}
						
						return true;
					}, "签名不合法");
			
			// 模板内容中占位符匹配校验
			jQuery.validator.addMethod("templateContentValidator1",
					function(value, element) {
						if(value.indexOf("{") != -1 || value.indexOf("}") != -1){
							var reg = /^([^\{\}]*\{[^\{\}]*\}[^\{\}]*)*$/;
							if(reg.test(value)){
								return true;
							}else{
								return false;
							}
						}
						return true;
					}, "请输入配对的占位符");
			
			// 模板内容占位符中的数字顺序校验
			jQuery.validator.addMethod("templateContentValidator2",
					function(value, element) {
						var numberReg = RegExp("\\d");
						// 匹配有多少个成对的{}
						var placeHolderReg0 = RegExp("\\{.*?\\}","g");
						var placeHolderNum0 = 0;
						while(1){
							var result = placeHolderReg0.exec(value);
							if(result != null){
								if(numberReg.exec(result[0]) == null){
									return false; // 如果{}中的字符不是数字，校验失败
								}
							}else{
								break;
							}
						}
						
						var placeHolderReg = RegExp("\\{\\d\\}","g");
						var pos = 1; // 模板占位符中的数字从 1 开始
						while(1){
							var result = placeHolderReg.exec(value);
							if(result != null){
								
								if(pos == Number(numberReg.exec(result[0])[0])){
									++pos;
								}else{
									return false;
								}
							}else{
								return true;
							}
						}
					}, "占位符不合法");
		});
		
		// 删除模板
		function delTemplate(template_id, template_name){
			var tips = "确定要删除模板“" + template_name + "”吗？";
			layer.confirm(tips, function(index){
				var result = templateMgnt.del(template_id)
				if(result){
					layer.msg("删除成功", {
						  time: 2000
						}, function(){
							window.location.reload();
						}); 
				}else{
					layer.msg("系统内部错误，请联系管理员", {
						  time: 2000
						}); 
				}
			});
		}
		
		// 编辑模板
		function editTemplate(template_id, template_name, type, sign, content){
			templateMgnt.setTemplateBoxData(template_name, type, sign, content);
			
			layer.open({
				title:"添加模板",
				type: 1,
				skin: 'layui-layer-demo', //样式类名
				closeBtn: 0, //不显示关闭按钮
				anim: 2,
				area: ['600px', '500px'],
				shadeClose: true, //开启遮罩关闭
				content: $("#addTemplateBox"),
				btn: ['保存', '取消'],
				yes: function(index, layero){// 点击“确定”按钮的回调
					if (!validate.form()) {
						return false;
					}
					
					var templateInfo = templateMgnt.getTemplateBoxData();
					templateInfo.template_id = template_id;
					var result = templateMgnt.update(templateInfo);
					if(result){
						layer.close(index);
						window.location.reload();
					}
				}
			});
		}
		
		
		// 添加模板
		function addTemplate(){
			$("#msg").hide();
			$("#templateForm")[0].reset();
			layer.open({
				title:"添加模板",
				type: 1,
				skin: 'layui-layer-demo', //样式类名
				closeBtn: 0, //不显示关闭按钮
				anim: 2,
				area: ['600px', '500px'],
				shadeClose: true, //开启遮罩关闭
				content: $("#addTemplateBox"),
				btn: ['保存', '取消'],
				yes: function(index, layero){// 点击“确定”按钮的回调
					if (!validate.form()) {
						return false;
					}
					
					var templateInfo = templateMgnt.getTemplateBoxData();debugger;
					var result = templateMgnt.insert(templateInfo);
					if(result){
						layer.close(index);
						window.location.reload();
					}
				}
			});
		}
		
	var templateMgnt = {
			insert : function (templateInfo) {
				var result = false;
				$.ajax({
					type : "post",
					url : "${ctx}/channelTest/template/insert",
					async : false,
					data : {
						template_name : templateInfo.template_name,
						type : templateInfo.type,
						content : templateInfo.content,
						sign : templateInfo.sign
					},
					success : function(data) {
						if (data.result == null) {
							$("#msg").text("服务器错误，请联系管理员").show();
							return;
						}
						if (data.result == "success") {
							$("#msg").hide();
							result = true;
						}else{
							$("#msg").text(data.msg).show();
							result = false;
						}
					}
				});
				return result;
			},
			update : function (templateInfo){
				var result = false;
				$.ajax({
					type : "post",
					url : "${ctx}/channelTest/template/update",
					async : false,
					data : templateInfo,
					success : function(data) {
						if (data.result == null) {
							$("#msg").text("服务器错误，请联系管理员").show();
							return;
						}
						if (data.result == "success") {
							$("#msg").hide();
							result = true;
						}else{
							$("#msg").text(data.msg).show();
							result = false;
						}
					}
				});
				return result;
			},
			del : function (template_id){
				var result = false;
				$.ajax({
					type : "post",
					url : "${ctx}/channelTest/template/delte",
					async : false,
					data : {
						template_id : template_id
					},
					success : function(data) {
						if (data.result == null) {
							alert("服务器错误，请联系管理员");
						}
						if (data.result == "success") {
							result = true;
						}
					}
				});
				return result;
			},
			getTemplateBoxData : function(){
				var info = {};
				info.template_name = $("#template_name").val();
				info.type = $("#template_type").val();
				info.content = $("#template_content").val();
				info.sign = $("#sign").val();
				
				return info;
			},
			setTemplateBoxData : function(template_name, type, sign, content){
				$("#template_name").val(template_name);
				$("#template_type").val(type);
				$("#sign").val(sign);
				$("#template_content").val(content);
			}
		
	}
	
	//@ sourceURL=templateMgnt.js
	</script>
</body>
</html>