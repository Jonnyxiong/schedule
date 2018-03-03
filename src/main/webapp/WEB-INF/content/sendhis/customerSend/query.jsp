<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
<title>客户发送记录</title>
<style type="text/css">
	.widget-title .search, .widget-title .search ul { float: none;}
	.widget-title .search ul.list-inline { margin: 10px 0px; width: 100%; display: block; overflow: hidden;}
	.widget-title .search ul li.li-num	{ margin: 0px;}
	.widget-title .search ul li span.span-num { background: red; padding: 5px 10px; line-height: 30px;}
	.widget-title .search ul.ul2 { margin-left: 20px; display: inherit; white-space: normal; width: 1400px;}
	button.button-num {border: 0; padding: 5px 10px; line-height: 30px;}
	.winCls{
    		border:1px solid #9bdf70;
    		background:#f0fbeb;
    		word-wrap:break-word;
    		word-break:break-all;
    		padding: 5px;
   		    margin-top: 10px;
    	}
    .btnCls{
    	margin: 3px;
    }
    .pagenum{
		text-align: left !important;
	}
	.key-color{
		overflow:hidden;
		margin-top: 5px;
	}
	.key-color .fr{
		float:right;
		line-height:20px;
		height:20px;
	}
	.key-color span{
		display:inline-block;
		width:20px;
		height:20px;
		margin-right:5px;
		margin-left:10px;
		vertical-align:middle;
	}
	.key-color .key-system{
		background-color:#f20000;
	}
	.key-color .key-channel{
		background-color:#ffae00;
	}
	
</style>
</head>
<body menuId="71">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<!-- <ul style="width:100%; height: auto; list-style-type:none;  text-align:right;"> -->
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						
						<div class="search" style="height:auto;">
						 <form method="post" id="mainForm" action="${ctx}/cusSendhis/query">
							
							<ul class="ul2">
								<li>
<%-- 									<input type="text" name="identify" onkeyup="value=value.replace(/[^\\d.]/g,'')" value="<s:property value="#parameters.identify"/>" maxlength="40" placeholder="标识" class="txt_250" /> --%>
									<input type="text" name="identify" autocomplete="off" onfocus="inputControl.setNumber(this, 4, 0, false)" value="<s:property value="#parameters.identify"/>" maxlength="40" placeholder="标识" class="txt_250" />
									<input type="text" name="clientid" value="<s:property value="#parameters.clientid"/>" maxlength="40" placeholder="用户账号" class="txt_250" autocomplete="off"/>
									<input type="text" name="phone" value="<s:property value="#parameters.phone"/>" maxlength="20" placeholder="手机号码" class="txt_250" />
									<input type="text" name="content" value="<s:property value="#parameters.content"/>" maxlength="40" placeholder="短信内容" class="txt_250" />
									<input type="text" name="agent_id" value="<s:property value="#parameters.agent_id"/>" maxlength="40" placeholder="代理商ID" class="txt_250" />
									<input type="text" name="smsid_submitid" value="<s:property value="#parameters.smsid_submitid"/>" maxlength="40" placeholder="smsid/submitid" class="txt_250" />
								</li>
								<li>
									<u:select id="channelid" value="${param.channelid}" placeholder="通道号" sqlId="channel"/>
									<u:select id="state" value="${param.state}" placeholder="状态" dictionaryType="sms_record_state" />
									<u:select id="smstype" value="${param.smstype}" placeholder="短信类型" dictionaryType="smstype" />
									<u:select id="operatorstype" value="${param.operatorstype}" placeholder="运营商类型" dictionaryType="channel_operatorstype"  />
									<u:select id="smsfrom"   value="${param.smsfrom}" data="[
											{value:'-1',text:'协议类型：所有'}, {value:'2',text:'SMPP协议'},  
               							    {value:'3',text:'CMPP协议'}, {value:'4',text:'SGIP协议'},
               							    {value:'5',text:'SMGP协议'}, {value:'6',text:'HTTPS协议'} ]" 
               						/>
								</li>
								<li>
									<input type="text" name="template_id" value="<s:property value="#parameters.template_id"/>" maxlength="11" placeholder="模板ID" class="txt_250" />
								</li>
								<li>
									<input style="width:34%;margin-left:14px;" type="text" id="start_time" name="start_time" value="${start_time}"  placeholder="开始时间" onfocus="WdatePicker({ minDate:'2016-05-18 00:00:00', maxDate:'#F{$dp.$D(\'end_time\')||\'%y-%M-%d %H:%m:%\'}'})"/>
									<span>至</span>
									<input style="width:34%;" type="text" id="end_time" name="end_time" value="${end_time}" placeholder="开始时间" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'start_time\')||\'%y-%M-{%d-45} 00:00:00\'}', maxDate:'%y-%M-%d %H:%m:%s'})"/>
								</li>
								<li>
									<input id="submitBtn" type="button" value="搜索" onclick="submitForm()"/>
								</li>
								<li>
									<a href="javascript:;" class=" label label-info" onclick="exportExcel()">导出Excel文件</a>
								</li>
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
					<u:page page="${page}" formId="mainForm" />
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
					                <!-- 1 --><th>序号</th>									 
									<!-- 2 --><th>通道号</th>
									<!-- 3 --><th>通道名称</th>
									<!-- 4 --><th>用户账号</th>
									<!-- 5 --><th>用户名称 </th>
									<!-- 6 --><th>代理商ID</th>
									<!-- 7 --><th>短信id</th>
									<!-- 8 --><th>订单号</th>
									<!-- 9 --><th>计费条数</th>
									<!-- 10 --><th>短信类型</th>
									<!-- 11 --><th>短信内容</th>
									<!-- 12 --><th>手机号码</th>
									<!-- 13 --><th>状态</th>
									<!-- 14 --><th>处理描述</th>
									<!-- 15 --><th>处理时间</th>
									<!-- 16 --><th>提交状态描述</th>
									<!-- 17 --><th>提交时间</th>
									<!-- 18 --><th>客户状态报告ID</th>
									<!-- 19 --><th>状态报告描述</th>
									<!-- 20 --><th>状态报告时间</th>
									<!-- 21 --><th>扩展端口</th>
									<!-- 22 --><th>短信签名</th>
									<!-- 23 --><th>拆分条数</th>
									<!-- 24 --><th>短信拆分序号</th>
									<!-- 25 --><th>运营商类型</th>
									<!-- 26 --><th>短信协议类型</th>
									<!-- 27 --><th>显示自签名的方式 </th>
									<!-- 28 --><th>用户透传ID</th>
									<!-- 29 --><th>模板ID</th>
									<!-- 30 --><th>通道模板ID</th>
									<!-- 31 --><th>模板参数值</th>
									<th>C2S_ID</th>
								</tr>
							</thead>
							
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <!-- 1 --><td>${rownum}</td>
							      <!-- 2 --><td>${channelid}</td>
							      <!-- 3 --><td>${channelname}</td>
								  <!-- 4 --><td>${clientid}</td>
								  <!-- 5 --><td>${username}</td>
								  <!-- 6 --><td>${agent_id}</td>
								  <!-- 7 --><td>${smsid}</td>
								  <!-- 8 --><td>${sub_id}</td>
								  <!-- 9 --><td>${charge_num}</td>
								  <!-- 10 --><td>${smstype}</td>
							      <!-- 11 --><td ${td_style}>
											      <a href = 'javascript:void(0)' onclick="showSmsContent(this, ${state}, '${clientid}')" id="${rownum}"><u:truncate length="20" value="${content}"/></a>
											      <span style="display: none;">${content}</span>
							      			 </td>
								  <!-- 12 --><td>${phone}</td>
								  <!-- 13 --><td>${state_name}</td>
								  <!-- 14 --><td>${errorcode}</td>
								  <!-- 15 --><td>${date}</td>
								  <!-- 16 --><td>${submit}</td>
								  <!-- 17 --><td>${submitdate}</td>
								  <!-- 18 --><td>${submitid}</td>
								  <!-- 19 --><td>${report}</td>
								  <!-- 20 --><td>${reportdate}</td>
								  <!-- 21 --><td>${srcphone}</td>
								  <!-- 22 --><td>${sign}</td>
								  <!-- 23 --><td>${smscnt}</td>
								  <!-- 24 --><td>${smsindex}</td>
								  <!-- 25 --><td>${operatorstype_name}</td>
								  <!-- 26 --><td>${smsfrom}</td>
								  <!-- 27 --><td>${showsigntype}</td>
								  <!-- 28 --><td>${uid}</td>
								  <!-- 29 --><td>${template_id}</td>
								  <!-- 30 --><td>${channel_tempid}</td>
								  <!-- 31 --><td><u:truncate length="50" value="${temp_params}"/></td>
								  <td>${c2s_id}</td>
							     </tr>
							    </s:iterator>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="smsContentBox" class="container-fluid hide">
		<div>
			<form class="form-horizontal">
				<div class="form-group">
					<div class="col-xs-12 winCls">
						<span id="smsContent" class="hide"></span>
						<span id="smsContentDisplay"></span>
					</div>
					<div class="clearfix key-color hide">
						<div class="fr">
							<span class="key-channel"></span>通道关键字
						</div>
						<div class="fr">
							<span class="key-system"></span>系统关键字
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	
	<script type="text/javascript" src="${ctx}/js/clipboard/clipboard.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
	<script type="text/javascript">
		$(function(){
			 
		})
	
	$("li[name='testhight']").find('select,input').width(120); 
	$("li[name='testhight1']").find('select,input').width(150); 
	//导出Excel文件
	function exportExcel() {
		var totalCount = ${page.totalCount};
		if (totalCount == 0) {
			layer.msg("共0条纪录，导出Excel文件失败", {icon: 1,time: 1500}); 
			return;
		}
		if(totalCount>10000){
			layer.msg("导出Excel文件条数大于10000条", {icon: 1,time: 1500}); 
			return;
			
		}
		
		var identify = $("input[name='identify']").val();
		var clientid = $("input[name='clientid']").val();
		if($.trim(identify) == '' && $.trim(clientid) == ''){
			layer.msg("请输入：标识或者客户账号", {icon: 1,time: 1500}); 
			return;
		}
		
		var start_time = $("#start_time").val();
		var end_time = $("#end_time").val();
		if(start_time == '' || end_time == ''){
			layer.msg("请输入：开始时间和结束时间", {icon: 1,time: 1500}); 
			return;
		}
		
		var flag = isTheSameDay(start_time,end_time);
		if(flag == false){
			layer.msg("请输入同一天的日期", {icon: 1,time: 1500}); 
			return;
		}
		
		var mainForm = $("#mainForm");
		var action = mainForm.attr("action");
	
		mainForm.attr("action", "${ctx}/cusSendhis/exportExcel").submit();
		mainForm.attr("action", action);
	}
	
	//Date对象：date1 date2
	function isTheSameDay(date1,date2){
		var date1_day = date1.substr(0,10);
		var date2_day = date2.substr(0,10);
		if(date1_day == date2_day){
			return true;
		}else{
			return false;
		}
	}
	
	function submitForm(){
		
		var identify = $("input[name='identify']").val();
		var clientid = $("input[name='clientid']").val();
		if($.trim(identify) == '' && $.trim(clientid) == ''){
			layer.msg("请输入：标识或者客户账号", {icon: 1,time: 1500}); 
			return;
		}
		
		var start_time = $("#start_time").val();
		var end_time = $("#end_time").val();
		if(start_time == '' || end_time == ''){
			layer.msg("请输入：开始时间和结束时间", {icon: 1,time: 1500}); 
			return;
		}
		
		var flag = isTheSameDay(start_time,end_time);
		if(flag == false){
			layer.msg("请输入同一天的日期", {icon: 1,time: 1500}); 
			return;
		}
		
		$("#mainForm").submit();
	}
	
	// 显示短信内容弹出框
	function showSmsContent(a, state, clientId){
		var selector = '#'+a.id;
		var texts = $(selector).siblings('span').text();
		$("#smsContent").text(texts);
		$("#smsContentDisplay").text(texts);
		
		var btns = [];
		if(state == 8 || state == 9){
			btns = ['复制短信', '显示关键字'];
		}else{
			btns = ['复制短信'];
		}
		
		layer.open({
				title:"短信内容",
				type: 1,
				skin: 'layui-layer-rim', //样式类名
// 				closeBtn: 0, //不显示关闭按钮
				anim: 2,
				area: ['500px', '300px'],
				shadeClose: true, //开启遮罩关闭
				content: $("#smsContentBox"),
				btn: btns,
				yes: function(index, layero){
			    	// 点击“复制短信”按钮，直接通过text直接返回复印的内容
					var clipboard = new Clipboard('.layui-layer-btn0', {
				        text: function() {
				            return $("#smsContent").text();
				        }
				    });
				
					clipboard.on('success', function(e) {
						layer.close(index);
						layer.msg("复制成功");
					});
					
					clipboard.on('error', function(e) {
						layer.msg("浏览器版本低，复制失败，请手动复制或者重新点击");
					});
				},
				btn2: function(index, layero){
					var systemKeyword = [];
					var channelKeyword = [];
					
					try {
						$.ajax({
							type : "post",
							async: false,
							data : {
								clientId : clientId
							},
							url : "${ctx}/cusSendhis/queryKeyWords",
							success : function(data) {
								systemKeyword = data.systemKeyword;
								channelKeyword = data.channelKeyword;
							}
						});
					} catch (e) {
						console.error("查询关键字异常", e);
					}
					
					var keyWordSerch = new KeyWordSerch();
					var smsContent = $("#smsContent").text();
					var needMatchPos = true;

                    var htmlArray = [];

                    var channelKeyWordMatch = keyWordSerch.searchDirect(smsContent, channelKeyword, needMatchPos);
                    if(channelKeyWordMatch.length > 0){
                        htmlArray = [];
                        var p1 = 0;
                        var p2 = 0;
                        // 根据位置将content中匹配到的关键字标红
                        for(var i=0; i<channelKeyWordMatch.length; i++){
                            var begin = channelKeyWordMatch[i].begin;
                            var end = channelKeyWordMatch[i].end;
                            p2 = channelKeyWordMatch[i].begin;
                            htmlArray.push(smsContent.substring(p1, p2));
                            htmlArray.push("<span style='color:#ffae00'>" + smsContent.substring(begin, p1=end) + "</span>");
                        }
                        htmlArray.push(smsContent.substring(p1));

                        $("#smsContentDisplay")[0].innerHTML  = htmlArray.join("");// 将关键字标红然后替换在短信内容的页面容器中
                    }

					var systemKeyWordMatch = keyWordSerch.searchDirect(smsContent, systemKeyword, needMatchPos);
					if(systemKeyWordMatch.length > 0){
                        htmlArray = [];
						var p1 = 0;
						var p2 = 0;
						// 根据位置将content中匹配到的关键字标红
						for(var i=0; i<systemKeyWordMatch.length; i++){
							var begin = systemKeyWordMatch[i].begin;
							var end = systemKeyWordMatch[i].end;
							p2 = systemKeyWordMatch[i].begin;
							htmlArray.push(smsContent.substring(p1, p2));
							htmlArray.push("<span style='color:#f20000'>" + smsContent.substring(begin, p1=end) + "</span>");
						}
						htmlArray.push(smsContent.substring(p1));

						$("#smsContentDisplay")[0].innerHTML  = htmlArray.join("");// 将关键字标红然后替换在短信内容的页面容器中
						smsContent = htmlArray.join("");
					}

					// 显示“系统关键字”和“通道关键字”颜色块
					if(systemKeyWordMatch.length > 0 || channelKeyWordMatch.length > 0){
						$(".key-color").show();
					}
					
					return false;
				},
				end : function(){// 弹出框消失的时候执行
					$(".key-color").hide();
				}
			});
	}
	
	$("input[name='clientid']").keyup(function(){
		$("input[name='identify']").val(""); // 清空
		
		try {
			$.ajax({
				type : "post",
				async: false,
				data : {
					clientId : $("input[name='clientid']").val()
				},
				url : "${ctx}/account/getIdentifyByClientId",
				success : function(data) {
					if (data != null && data.identify != null) {
						$("input[name='identify']").val(data.identify);
					}else{
						console.log("查询client标识为空");
					}
				}
			});
		} catch (e) {
			console.error("查询client标识异常", e);
		}
	});
	
	</script>
</body>
</html>