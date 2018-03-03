<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>通道测试</title>

<style type="text/css">
	
	#isNeedTimeStampDiv .ckBox,#signModeDiv .ckBox{
		display: inline;
	}
	
	.txt_300{
		width: 300px;
	}
	
</style>
</head>
<body menuId="267">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加</a>
						</span>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelTest/query">
								<ul>
									<li>
										<input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="100"
											placeholder="通道号/通道名称" class="txt_300" />
									</li>
									<li>
										<input type="text" name="testIp" value="<s:property value="#parameters.testIp"/>" maxlength="100"
											placeholder="测试组件IP" class="txt_300" />
									</li>
									<li>
										<label class="control-label">创建时间</label>
										<u:date id="start_time" value="${query_start_time}" placeholder="开始时间" params="minDate:'%y-%M-{%d-45} 00:00:00', maxDate:'#F{$dp.$D(\\'end_time\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
										<span>至</span>
	            						<u:date id="end_time" value="${query_end_time}" placeholder="结束时间" params="minDate:'#F{$dp.$D(\\'start_time\\')||\\'%y-%M-{%d-45} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
									</li>
									<li><input type="submit" value="查询" /></li>
								</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>通道号</th>
					                 <th>通道名称</th>
					                 <th>运营商类型</th>
					                 <th>接入协议</th>
					                 <th>扩展位</th>
					                 <th>通道接入号</th>
					                 <th>通道流量</th>
					                 <th>状态报告</th>
					                 <th>上行</th>
					                 <th>长短信</th>
					                 <th>状态</th>
					                 <th>测试组件IP</th>
					                 <th>创建时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${channel_id}</td>
										<td>${channel_name}</td>
										<td>
											<s:if test="operators_type==0">全网</s:if>
											<s:elseif test="operators_type==1">移动</s:elseif>
											<s:elseif test="operators_type==2">联通</s:elseif>
											<s:elseif test="operators_type==3">电信</s:elseif>
											<s:elseif test="operators_type==4">国际</s:elseif>
										</td>
										<td>
											<s:if test="protocol_type==3">CMPP</s:if>
											<s:elseif test="protocol_type==4">SMGP</s:elseif>
											<s:elseif test="protocol_type==5">SGIP</s:elseif>
											<s:elseif test="protocol_type==6">SMPP</s:elseif>
										</td>
										<td>${extend_size}</td>
										<td>${access_id}</td>
										<td>${speed}</td>
										<td>
											<s:if test="is_report eq 0">不支持</s:if>
											<s:elseif test="is_report eq 1">支持</s:elseif>
										</td>
										<td>
											<s:if test="is_mo eq 0">不支持</s:if>
											<s:elseif test="is_mo eq 1">支持</s:elseif>
										</td>
										<td>
											<s:if test="is_long_sms eq 0">不支持</s:if>
											<s:elseif test="is_long_sms eq 1">支持</s:elseif>
										</td>
										<td>
											<s:if test="state eq 0">测试中</s:if>
											<s:if test="state eq 1">已上架</s:if>
											<s:if test="state eq 2">已关闭</s:if>
											<s:if test="state eq 3">测试通过</s:if>
											<s:if test="state eq 4">测试失败</s:if>
										</td>
										<td>${test_url}</td>
										<td>${create_time}</td>
										<td>
											<a href="javascript:;" onclick="view('${channel_id}')">查看&nbsp;&nbsp;&nbsp;</a>
											
											<!-- 编辑按钮 -->
											<s:if test="state == 0 or state == 3 or state == 4">
												<a href="javascript:;" onclick="edit('${channel_id}')">编辑&nbsp;&nbsp;&nbsp;</a>
											</s:if>
											<%-- <s:else>
												<a href="javascript:;" style="opacity:0;">编辑&nbsp;&nbsp;&nbsp;</a>
											</s:else> --%>
											
											<!-- 测试按钮 -->
											<s:if test="state == 0">
												<a href="javascript:;" onclick="startUserShareTestBox('${channel_id}', '${test_url}')">测试&nbsp;&nbsp;&nbsp;</a>
											</s:if>
											<%-- <s:else>
												<a href="javascript:;" style="opacity:0;">测试&nbsp;&nbsp;&nbsp;</a>
											</s:else> --%>
											
											<!-- 查看测试报告 -->
											<s:if test="state == 0">
												<a href="javascript:;" onclick="viewTestReport('${channel_id}',  '${report_id}', '${state}')">生成测试报告&nbsp;&nbsp;&nbsp;</a>
											</s:if>
											<s:else>
												<a href="javascript:;" onclick="viewTestReport('${channel_id}',  '${report_id}', '${state}')">查看测试报告&nbsp;&nbsp;&nbsp;</a>
											</s:else>
											
											<s:if test="state != 2">
												<a href="javascript:;" onclick="updateChannelStatus('${channel_id}', '${channel_name}', 2)">关闭&nbsp;&nbsp;&nbsp;</a>
											</s:if>
											<s:if test="state == 3">
												<a href="javascript:;" onclick="updateChannelStatus('${channel_id}', '${channel_name}', 1)">上架&nbsp;&nbsp;&nbsp;</a>
											</s:if>
											<%-- <s:else>
												<a href="javascript:;" style="opacity:0;">上架</a>
											</s:else> --%>
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
	
	<div id="userShareTestBox" class="container-fluid" style="display:none;">
			<div class="row">
				<div>
					<form id="userShareTestForm" class="form-horizontal">
						<div class="control-group">
							<label class="control-label">测试模板：</label>
							<div class="controls">
								<u:select id="templateId" dbType="7" sqlId="queryTestTeamplate4Select" onChange="selectTrigger"/>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">模板内容：</label>
							<div class="controls">
								<span id="templateContent"></span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">模板参数：</label>
							<div class="controls">
								<input id="templateParams" name="templateParams" value=""></input>
								<span class="help-block">多个参数使用英文逗号分隔</span>
							</div>
						</div>
						
						<div class="control-group">
							 <label class="control-label">带时间戳：</label>
							<div id="isNeedTimeStampDiv" class="controls">
								<label class="checkbox-inline ckBox">
									<input type="radio" name="isNeedTimeStamp" value="1" checked> 是
								</label>
								<label class="checkbox-inline ckBox">
									<input type="radio" name="isNeedTimeStamp" value="0"> 否
								</label>
							</div>
						</div>
						<div id="signModeDiv" class="control-group">
							 <label class="control-label">签名方式：</label>
							<div class="controls">
								<label class="checkbox-inline ckBox">
									<input type="radio" name="signMode" value="1" checked> 前置
								</label>
								<label class="checkbox-inline ckBox">
									<input type="radio" name="signMode" value="2"> 后置
								</label>
								<label class="checkbox-inline ckBox">
									<input type="radio" name="signMode" value="3"> 不带签名
								</label>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">测试条数：</label>
							<div class="controls">
								<input id="roundOfTest" name="roundOfTest" type="text" />
								<span class="help-block">往测试号码中每个手机号码发送的短信条数</span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">自定义扩展：</label>
							<div class="controls">
								<input id="extend" name="extend" type="text" />
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" >测试号码：</label>
							<div class="controls">
								 <textarea id="testPhone" name="testPhone" rows="9" cols="40" style="height: auto; width: auto;" maxlength="1000"></textarea>
								 <input type="button" onclick="selectPhones();" value="选择" class="btn btn-success">
								 <span class="help-block">多个手机号码使用英文逗号分隔</span>
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
		function view(channel_id){
			location.href = "${ctx}/channelTest/view?id=" + channel_id;
		}
		
		//添加
		function add() {
			location.href = "${ctx}/channelTest/edit";
		}
		
		//编辑
		function edit(channel_id) {
			location.href = "${ctx}/channelTest/edit?id=" + channel_id;
		}
		
		var validate;
		$(function(){
			$.validator.defaults.ignore = "";
			validate = $("#userShareTestForm").validate({
				rules: {
					templateId:{
						required: true
					},
					roundOfTest: {
				    	required: true,
				    	range:[1, 100],
				    	digits: true
				    },
				    templateParams:{
				    	templateParamsValidator: true
				    },
				    testPhone: {
				    	required: true,
				    	mobileValidator1: true,
				    	mobileValidator2: true,
				    	mobileValidator3: true
				    },
				    extend:{
				    	rangelength : [0, 12],
				    	digits: true
				    }
				},
				messages : {
					templateId:{
						required: "请选择测试模板"
					},
				    testPhone: {
				    	required: "请选择测试号码"
				    },
				    extend: {
				    	rangelength : "请输入长度为 0 至 12 的数字",
				    	digits : "只能输入数字"
				    }
				}
			});
			
			// 校验输入的参数个数与占位符个数是否匹配
			jQuery.validator.addMethod("templateParamsValidator",
					function(value, element) {
						var templateContent = $("#templateContent").text();
						// 正则匹配模板内容中的占位符个数
						var placeHolderReg = RegExp("\\{\\d\\}","g");
						var placeHolderNum = 0;
						while(1){
							if(placeHolderReg.exec(templateContent) != null){
								++placeHolderNum;
							}else{
								break;
							}
						}
						
						var paramsNum = 0;
						if(value.trim().length > 0){
							paramsNum = value.split(",").length;
						}
						if(paramsNum != placeHolderNum){
							return false;
						}
						
						return true;
					}, "模板参数个数与模板不匹配");
			
			// 手机号码校验
			jQuery.validator.addMethod("mobileValidator1",
					function(value, element) {
// 						var reg = /^((13[1,4-9])|(147)|(15[0-2,7-9])|(18[2-4,7-8]))\d{8}$/; // 移动号码
// 						var reg2 = /^((13[0,2])|(145)|(15[5,6])|(18[5,6])|(176))\d{8}$/; // 联通
// 						var reg3 = /^((133)|(149)|(153)|(18[0,1,9])|(17[0,3,7]))\d{8}$/; // 电信

						var reg1 = /^1[34578]\d{9}$/;
						var reg2 = /^0{2}\d{8,18}$/; // 国际号码  00开头大于10位
						
						var mobileArray = value.split(",");
						var length = mobileArray.length;
						var result = true;
						for(var pos = 0; pos < length; pos++){
							var phone = mobileArray[pos];
							if(reg1.test(phone) || reg2.test(phone)){
								continue;
							}else{
								result = false;
								break;
							}
						}
						
						return result;
					}, "手机号码不合法");
			
			// 手机号码校验
			jQuery.validator.addMethod("mobileValidator2",
					function(value, element) {
						
						var mobileArray = value.split(",");
						
						var length = mobileArray.length;
						
						if(length > 100){
							return false;
						}
						return true;
					}, "最多100个号码");
			
			// 手机号码校验
			jQuery.validator.addMethod("mobileValidator3",
					function(value, element) {
						
						var mobileArray = value.split(",");
						var allNum = mobileArray.length;
						var uniqueArray = $.unique(mobileArray);
						if(uniqueArray.length == allNum){
							return true;
						}else{
							return false;
						}
						
					}, "不能存在重复号码");
			
		});
		

		// 启动智能测试弹出框
		var startUserShareTestBox = function(channel_id, test_url){
			$("#msg").hide();
			resetForm();
			
			layer.open({
				title:"启动测试",
				type: 1,
				skin: 'layui-layer-demo', //样式类名
				closeBtn: 0, //不显示关闭按钮
				area: ['650px', '730px'],
				shadeClose: true, //开启遮罩关闭
				content: $("#userShareTestBox"),
				btn: ['测试', '取消'],
				yes: function(index, layero){// 点击“确定”按钮的回调
					if (!validate.form()) {
						return false;
					}

					// 开启遮罩
 					var loadIndex = parent.layer.load(1, {shade: [0.2,'#fff']});
					
					$.ajax({
						type : "post",
//						async : false,
						url : "${ctx}/channelTest/channelTestRequest",
						data : {
							templateId : $("#templateId").val(),
							templateParams : $("#templateParams").val(),
							templateContent : $("#templateContent").val(),
							signMode : $("input[name='signMode']:checked").val(),
							isNeedTimeStamp : $("input[name='isNeedTimeStamp']:checked").val(),
							roundOfTest : $("#roundOfTest").val(),
							extend : $("#extend").val(),
							testPhone : $("#testPhone").val(),
							channelId : channel_id,
							test_url : test_url
						},
						success : function(data) {
						    // 关闭遮罩
 							parent.layer.close(loadIndex);
							if(data != null && data.result != null){
								if (data.result == 'success') {
									layer.msg(data.msg, {
										  time: 2000 //2秒关闭（如果不配置，默认是3秒）
										}, function(){
											layer.close(index);
	// 										window.location.reload();
										}); 
								}else{
									layer.alert(data.msg);
								}
								
							}else{
								layer.alert("服务器错误，请联系管理员");
							}
							
						}
					});
					
				}
			});
			
		}
		
		
		// 查询是否有提交的测试还没有生成测试报告
		function queryTestLogNotGenerateReport(channelId){
			var result;
			$.ajax({
				type : "post",
				async : false,
				url : "${ctx}/channelTest/queryTestLogNotGenerateReport",
				data : {
					channelId : channelId
				},
				success : function(response) {
					result = response;
				}
			});
			return result;
		}
		
		// 查看测试报告弹出框
		function viewTestReport(channel_id, report_id, state){
			debugger;
			// 查看分享单是否已经生成过测试报告
// 			var report_id = queryReportIdByChannelId(channel_id);
			// 是否存在测试报告
			var isExistReport = false;
			if($.trim(report_id) == '' || report_id == null){
				isExistReport = false;
			}else{
				isExistReport = true;
			}
			
			// 查询是否有提交的测试还没有生成测试报告
			var hasTestLogNotGenerateReport = false;
			var check = queryTestLogNotGenerateReport(channel_id);
			if(check.result == "success"){
				hasTestLogNotGenerateReport = true;
			}else{
				hasTestLogNotGenerateReport = false;
			}
			
			// 存在没有生成报告的测试记录直接生成测试报告（旧的测试报告没有用了）
			if(hasTestLogNotGenerateReport){
				generateTestReportAndReturn(channel_id);
			}else{
				if(isExistReport){
					// 直接展示测试报告
					if(state != 0){
						queryTestReportByReportIdWithOutOperate(channel_id, report_id);
					}else{
						queryTestReportByReportId(channel_id, report_id);
					}
					
				}else{
					layer.confirm('你还没有提交测试哦~', {
						  btn: ['进行测试', '已经测试']
						}, function(index, layero){
						  // 按钮“进行测试”的回调
						  layer.close(index);
						  // 启动测试弹出框
						  startUserShareTestBox(channel_id);
						}, function(index){
						  //按钮“已经测试”的回调
						  var data = generateTestReportWithOutTest(channel_id);
						  
						  if(data.result == 'success'){
							  queryTestReportByReportId(channel_id, data.report_id);
						  }else{
							  layer.msg(data.msg, {
								  time: 2000
								}, function(){
									layer.close(index);
								}); 
						  }
							
					});
					return false; // 这里不return 代码会一直往下跑
				}
			}
			
		}
		
		function generateTestReportWithOutTest(channel_id){
			var result;
			$.ajax({
				type : "post",
				async : false,
				url : "${ctx}/channelTest/generateTestReportWithOutTest",
				data : {
					channel_id : channel_id
				},
				success : function(data) {
					result = data;
				}
		  	});
			return result;
		}
		
		// 生成测试报告并返回测试报告结果
		function generateTestReportAndReturn(channel_id){
			layer.open({
	    		  type: 2,
	    		  title: '通道测试报告',
	    		  shadeClose: true,
	    		  maxmin: true,
	    		  shade: 0.8,
	    		  area: ['980px', '100%'],
	    		  btn: ['通过', '不通过', '离开'],
	    		  content: "${ctx}/channelTest/generateTestReportAndReturn?channelId=" + channel_id,  // iframe的url
	    		  success : function(data) {
					  if (data.result == 'fail') {
						    layer.msg("服务器错误，请联系管理员", {
							    time: 2000 //2秒关闭（如果不配置，默认是3秒）
							});
					  }
				  },
	    		  yes: function(index, layero){// 点击“通过”按钮的回调
	    			  var state = 3; // 测试通过成功
	    			  
	    			  // 得到iframe页的窗口对象
	    			  var body = layer.getChildFrame('body', index);
	    			  var iframeMethods = window[layero.find('iframe')[0]['name']]; 
	    			  
	    			  // 校验是否已经填写测试报告结论
	    			  var isCommentResult = iframeMethods.validateTestComment();
	    			  var commentText = isCommentResult.commentText;
	    			  var report_id = isCommentResult.report_id;
	    			  if(!isCommentResult.isComment){
	    				  layer.msg("请输入测试结论", {time: 2000});
	    				  return false;
	    			  }
	    			  
	    			  // 更新用户分享单状态
	    			  var updateResult = updateChannelTestStatus(channel_id, state, commentText, report_id);
  	    			  if (updateResult.result == 'success') {
  							layer.msg(updateResult.msg, {
  								  time: 2000 //2秒关闭（如果不配置，默认是3秒）
  								}, function(){
	  								layer.close(index);
  									window.location.reload();
  								}); 
  					  }else{
  							layer.msg(updateResult.msg, {
  								  time: 2000 //2秒关闭（如果不配置，默认是3秒）
  								}, function(){
  								});
  					  }
	    			  
	    			  
				  },
				  btn2: function(index, layero){// 点击“不通过”按钮的回调
	    			  var state = 4; // 对接失败
	    			  
	    			  // 得到iframe页的窗口对象
	    			  var body = layer.getChildFrame('body', index);
	    			  var iframeMethods = window[layero.find('iframe')[0]['name']];
	    			  
	    			  // 校验是否已经填写测试报告结论
	    			  var isCommentResult = iframeMethods.validateTestComment();
	    			  var commentText = isCommentResult.commentText;
	    			  var report_id = isCommentResult.report_id;
	    			  if(!isCommentResult.isComment){
	    				  layer.msg("请输入测试结论", {time: 2000});
	    				  return false;
	    			  }
	    			  
	    			  // 更新用户分享单状态
	    			  var updateResult = updateChannelTestStatus(channel_id, state, commentText, report_id);
  	    			  if (updateResult.result == 'success') {
  							layer.msg(updateResult.msg, {
  								  time: 2000
  								}, function(){
	  								layer.close(index);
  									window.location.reload();
  								}); 
  					  }else{
  							layer.msg(updateResult.msg, {
  								  time: 2000
  								}, function(){
  								});
  					  }
	    			  
				  },
				  btn3: function(index, layero){// 点击“离开”按钮的回调
					  
	    			  layer.close(index); // 关闭iframe 弹窗
				  }
	    		});
		}
		
		
		// 查询测试报告（测试报告已经存在）
		function queryTestReportByReportId(channel_id, report_id){
			layer.open({
	    		  type: 2,
	    		  title: '通道测试报告',
	    		  shadeClose: true,
	    		  maxmin: true,
	    		  shade: 0.8,
	    		  area: ['980px', '100%'],
	    		  btn: ['通过', '不通过', '离开'],
	    		  content: "${ctx}/channelTest/queryTestReportByReportId?channel_id=" + channel_id + "&report_id=" + report_id,  // iframe的url
	    		  success : function(data) {
					  if (data.result == 'fail') {
						    layer.msg("服务器错误，请联系管理员", {
							    time: 2000 //2秒关闭（如果不配置，默认是3秒）
							});
					  }
				  },
	    		  yes: function(index, layero){// 点击“通过”按钮的回调
	    			  var state = 3; // 测试通过
	    			  
	    			  // 得到iframe页的窗口对象
	    			  var body = layer.getChildFrame('body', index);
	    			  var iframeMethods = window[layero.find('iframe')[0]['name']]; 
	    			  
	    			  // 校验是否已经填写测试报告结论
	    			  var isCommentResult = iframeMethods.validateTestComment();
	    			  var commentText = isCommentResult.commentText;
	    			  var report_id = isCommentResult.report_id;
	    			  if(!isCommentResult.isComment){
	    				  layer.msg("请输入测试结论", {time: 2000});
	    				  return false;
	    			  }
	    			  
	    			  // 更新用户分享单状态
	    			  var updateResult = updateChannelTestStatus(channel_id, state, commentText, report_id);
	    			  if (updateResult.result == 'success') {
							layer.msg(updateResult.msg, {
								  time: 2000 //2秒关闭（如果不配置，默认是3秒）
								}, function(){
	  								layer.close(index);
									window.location.reload();
								}); 
					  }else{
							layer.msg(updateResult.msg, {
								  time: 2000 //2秒关闭（如果不配置，默认是3秒）
								}, function(){
								});
					  }
	    			  
	    			  
				  },
				  btn2: function(index, layero){// 点击“不通过”按钮的回调
	    			  var state = 4; // 测试不通过
	    			  
	    			  // 得到iframe页的窗口对象
	    			  var body = layer.getChildFrame('body', index);
	    			  var iframeMethods = window[layero.find('iframe')[0]['name']];
	    			  
	    			  // 校验是否已经填写测试报告结论
	    			  var isCommentResult = iframeMethods.validateTestComment();
	    			  var commentText = isCommentResult.commentText;
	    			  var report_id = isCommentResult.report_id;
	    			  if(!isCommentResult.isComment){
	    				  layer.msg("请输入测试结论", {time: 2000});
	    				  return false;
	    			  }
	    			  
	    			  // 更新用户分享单状态
	    			  var updateResult = updateChannelTestStatus(channel_id, state, commentText, report_id);
	    			  if (updateResult.result == 'success') {
							layer.msg(updateResult.msg, {
								  time: 2000
								}, function(){
	  								layer.close(index);
									window.location.reload();
								}); 
					  }else{
							layer.msg(updateResult.msg, {
								  time: 2000
								}, function(){
								});
					  }
	    			  
				  },
				  btn3: function(index, layero){// 点击“离开”按钮的回调
					  
	    			  layer.close(index); // 关闭iframe 弹窗
				  },
				  end : function() {  
					  window.location.reload();
			      }
	    		});
		}
		
		// 查询测试报告
		function queryTestReportByReportIdWithOutOperate(channel_id, report_id){
			layer.open({
	    		  type: 2,
	    		  title: '通道测试报告',
	    		  shadeClose: true,
	    		  maxmin: true,
	    		  shade: 0.8,
	    		  area: ['980px', '100%'],
	    		  btn: ['离开'],
	    		  content: "${ctx}/channelTest/queryTestReportByReportId?channel_id=" + channel_id + "&report_id=" + report_id,  // iframe的url
	    		  success : function(data) {
					  if (data.result == 'fail') {
						    layer.msg("服务器错误，请联系管理员", {
							    time: 2000 //2秒关闭（如果不配置，默认是3秒）
							});
					  }
				  },
				  yes: function(index, layero){// 点击“离开”按钮的回调
					  
	    			  layer.close(index); // 关闭iframe 弹窗
				  }
	    		});
		}
			
		
		// 用iframe展示选择号码页面
		function selectPhones(){
			layer.open({
	    		  type: 2,
	    		  title: '选择测试号码',
	    		  shadeClose: true,
	    		  maxmin: true,
	    		  shade: 0.8,
	    		  area: ['980px', '100%'],
	    		  btn: ['确定', '取消'],
	    		  content: "${ctx}/channelTest/selectPhonesIframe", //iframe的url
	    		  yes: function(index, layero){// 点击“确定”按钮的回调
	    			  var body = layer.getChildFrame('body', index);
	    			  var iframeMethods = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象
	    			  var getPhones = iframeMethods.getAllSelected(); // 执行iframe页面的方法
	    			  var testPhoneInputVal = $("#testPhone").val();
	    			  if(testPhoneInputVal.trim() != "" && getPhones.trim() != ""){
	    				  testPhoneInputVal = testPhoneInputVal + "," + getPhones;
	    			  }else if(getPhones.trim() != ""){
	    				  testPhoneInputVal = getPhones;
	    			  }
	    			  $("#testPhone").val(testPhoneInputVal); 
	    			  layer.close(index); // 关闭iframe 弹窗
				  }
	    		});
		}
		
		function resetForm(){
			$("#userShareTestForm")[0].reset();
			$("#templateContent").text("");
			$("input[name='signMode']").eq(0).click();
			$("input[name='isNeedTimeStamp']").eq(0).click();
		};
		
		function updateChannelTestStatus(channel_id, state, testComment, report_id){
			var result;
			$.ajax({
				type : "post",
				async : false,
				url : "${ctx}/channelTest/updateChannelTestStatus",
				data : {
					state : state,
					channel_id : channel_id,
					comment : testComment,
					report_id : report_id
				},
				success : function(data) {
					result = data;
				}
		  	});
			
			return result;
		};
		
		
		// 根据channelId查询t_sms_test_channel中相关记录中的reportId
		function queryReportIdByChannelId(channelId){
			var result;
			$.ajax({
				type : "post",
				async : false,
				url : "${ctx}/channelTest/queryReportIdByChannelId",
				data : {
					channelId : channelId
				},
				success : function(data) {
					if(null != data){
						result = data.reportId;
					}
				}
		  	});
			
			return result;
		}
		
		// 测试模板选择下拉框触发器
		function selectTrigger(value,text,isInit){
			
			// 选择为空时将页面上的“模板为空”置空
			if(value.trim() == ""){
				$("#templateContent").text("");
			}
			
			$.ajax({
				type : "post",
				async : false,
				url : "${ctx}/channelTest/template/queryTemplateById",
				data : {
					templateId : value
				},
				success : function(response) {
					if (response != null && response.result == "success") {
						debugger;
						$("#templateContent").text(response.data.content);
					}else{
						console.log("查询模板信息时出错");
					}
				}
			});
		};
		
		function updateChannelStatus(channelId, channelName, state){
			var operate;
			switch(state){
				case 1:
					operate = "上架";
					break;
				case 2:
					operate = "关闭";
					break;
			}
			var confirmText = "确认" + operate + "通道" + channelName + "吗？";
			
			layer.confirm(confirmText, {
				  btn: ['确认', '取消']
				}, function(index, layero){
					$.ajax({
						type : "post",
						async : false,
						url : "${ctx}/channelTest/updateChannelStatus",
						data : {
							channelId : channelId,
							state : state
						},
						success : function(response) {
							if (response != null && response.result == "success") {
								layer.msg(response.msg, {
									  time: 2000
									}, function(){
										window.location.reload();
										layer.close(index);
									}); 
							}else{
								console.log("更新测试通道状态出错");
							}
						}
					});
				}, function(index){
					layer.close(index);
			});
			
		}
		
		//@ sourceURL=queryPending.js

	</script>
</body>
</html>