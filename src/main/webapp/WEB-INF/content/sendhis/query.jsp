<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
<title>短信发送记录</title>
<style type="text/css">
	.widget-title .search, .widget-title .search ul { float: none;}
	.widget-title .search ul.list-inline { margin: 10px 0px; width: 100%; display: block; overflow: hidden;}
	.widget-title .search ul li.li-num	{ margin: 0px;}
	.widget-title .search ul li { height: 36px; }
	.widget-title .search ul li span.span-num { background: red; padding: 5px 10px; line-height: 30px;}
	.widget-title .search ul.ul2 { margin-left: 20px; display: inherit; white-space: normal; width: 1400px;}
	button.button-num {border: 0; padding: 5px 10px; line-height: 30px;}
    .winCls{
    		border:1px solid #9bdf70;
    		background:#f0fbeb;
    		word-wrap:break-word;
    		word-break:break-all;
    		padding: 5px;
    	}
    .btnCls{
    	margin: 3px;
    }
    .pagenum{
		text-align: left !important;
	}
	.sms_content{  word-wrap: break-word;
		  word-break: break-all;
		  overflow: hidden;
		  max-height: 320px;
		  min-height: 20px;
		  display: block;
	   }
	 .sms_status{  word-wrap: break-word;
		  word-break: break-all;
	   }	
</style>
</head>
<body menuId="26">
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
						 <form method="post" id="mainForm" action="${ctx}/sendhis/query">
							<ul class="ul2">
								<li>
									<input type="text" name="identify" autocomplete="off" onfocus="inputControl.setNumber(this, 4, 0, false)" value="<s:property value="#parameters.identify"/>" maxlength="40" placeholder="标识" class="txt_250" style="width:100px;" />
									<input type="text" name="sid_clientid" value="<s:property value="#parameters.sid_clientid"/>" maxlength="40" placeholder="用户账号" class="txt_250" />
									<input type="text" name="phone" value="<s:property value="#parameters.phone"/>" maxlength="20" placeholder="手机号码" class="txt_250" />
									<input type="text" name="content" value="<s:property value="#parameters.content"/>" maxlength="40" placeholder="短信内容" class="txt_250" />
									<input type="text" name="smsid" value="<s:property value="#parameters.smsid"/>" maxlength="40" placeholder="smsid" class="txt_250" />
									<u:select id="channelid" value="${param.channelid}" placeholder="通道号" sqlId="channel" onChange="channelOnChange"/>
								</li>
								<li >
									<label class="control-label">地域选择&nbsp;&nbsp;</label>
									<u:treeSelect id="area_id" value="${area_id}" placeholder="地区" sqlId="area" showPname="true" />
									<u:select id="state" value="${param.state}" data="[
											{value:'',text:'状态：所有'}, {value:'0',text:'未发送'}, {value:'1',text:'提交成功'}, {value:'2',text:'发送成功'}
											, {value:'3',text:'明确成功'}, {value:'4',text:'提交失败'}, {value:'5',text:'发送失败'}, {value:'6',text:'明确失败'}]"  />
									<u:select id="operatorstype" value="${param.operatorstype}" placeholder="运营商类型" dictionaryType="channel_operatorstype"  />
								</li>
								<li>
									<input type="text" name="smscnt" value="${param.smscnt }" placeholder="拆分条数" style="width:100px" onkeyup="this.value=this.value.replace(/[^\d]/g, '')"/>
								</li>
								<li>
									<input type="text" id="start_time" name="start_time" value="${start_time}"  placeholder="开始时间" readonly="readOnly" onfocus="WdatePicker({ minDate:'2016-05-18 00:00:00', maxDate:'#F{$dp.$D(\'end_time\')||\'%y-%M-%d %H:%m:%\'}'})" style="width: 34%;margin-left:14px">
									<span>至</span>
            						<input type="text" id="end_time" name="end_time" value="${end_time}"  placeholder="结束时间" readonly="readOnly" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'start_time\')||\'%y-%M-{%d-45} 00:00:00\'}', maxDate:'%y-%M-%d %H:%m:%s'})" style="width: 34%;margin-left:14px">
								</li>
								<li>
									<input type="text" name="errorcode" value="${param.errorcode }" placeholder="处理描述" style="width:100px"/>
								</li>
								<li name="testhight">
									<u:select id="duration" value="${param.duration}" 
									data="[{value:'-1',text:'发送时长：所有'},{value:'0',text:'0-5s'},{value:'1',text:'6-10s'},{value:'2',text:'11-15s'},{value:'3',text:'16-20s'},{value:'4',text:'21-30s'},
									{value:'5',text:'31-40s'},{value:'6',text:'41-60s'},{value:'7',text:'60s以上'}]" showAll="true"/>
								</li>
								<li>
									<input type="text" name="template_id" value="<s:property value="#parameters.template_id"/>" maxlength="11" placeholder="模板ID" class="txt_250" />
								</li>
								<li>
									<input id="submitBtn" type="button" value="搜索" onclick="submitForm()"/>
								</li>
								<li>
									<a href="javascript:;" class=" label label-info"
									onclick="exportExcel()">导出Excel文件</a>
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
									<!-- 3 --><th>短信内容</th>
									<!-- 4 --><th>手机号码</th>
									<!-- 5 --><th>发送时长（秒）</th>
									<!-- 6 --><th>状态</th>
									<!-- 7 --><th>处理时间</th>
									<!-- 8 --><th>提交状态描述</th>
									<!-- 9 --><th>提交时间</th>
									<!-- 10 --><th>应答状态描述</th>
									<!-- 11 --><th>应答时间</th>
									<!-- 12 --><th>状态报告描述</th>
									<!-- 13 --><th>状态报告时间</th>
									<!-- 14 --><th>send接收状态报告时间</th>
									<!-- 15 --><th>处理描述</th>
									<!-- 16 --><th>用户账号</th>
									<!-- 17 --><th>拆分条数</th>
									<!-- 18 --><th>运营商类型</th>
									<!-- 19 --><th>区域</th>
									<!-- 20 --><th>短信协议类型</th>
									<!-- 21 --><th>短信id</th>
									<!-- 22 --><th>显示号码</th>
									<!-- 23 --><th>模板ID</th>
									<!-- 24 --><th>通道模板ID</th>
									<!-- 25 --><th>模板参数值</th>
								</tr>
							</thead>
							
							<tbody>
								<s:iterator value="page.list">
								
								<tr>
							      <!-- 1 --><td ${td_style}>${rownum}</td>
							      <!-- 2 --><td ${td_style}>${channelid}</td>
							      <!-- 3 --><td ${td_style}>
							      <a href = 'javascript:void(0)' onclick="showdetail(this)" id="${rownum}"><u:truncate length="20" value="${content}"/></a>
							      <span style="display: none;">${content}</span>
							      </td>
								  <!-- 4 --><td>${phone}</td>
								  <!-- 5 --><td>${duration}</td>
								  <!-- 6 --><td>${state_name}</td>
								  <!-- 7 --><td>${date}</td>
								  <!-- 8 --><td>${submit}</td>
								  <!-- 9 --><td>${submitdate}</td>
								  <!-- 10 --><td>${subret}</td>
								  <!-- 11 --><td>${subretdate}</td>
								  <!-- 12 --><td>${report}</td>
								  <!-- 13 --><td>${reportdate}</td>
								  <!-- 14 --><td>${recvreportdate}</td>
								  <!-- 15 --><td>${errorcode}</td>
								  <!-- 16 --><td>${clientid}</td>
								  <!-- 17 --><td>${smscnt}</td>
								  <!-- 18 --><td>${operatorstype_name}</td>
								  <!-- 19 --><td>${area_name} </td>
								  <!-- 20 --><td>${smsfrom}</td>
								  <!-- 21 --><td>${smsid}</td>
								  <!-- 22 --><td>${showphone}</td>
								  <!-- 23 --><td>${template_id}</td>
								  <!-- 24 --><td>${channel_tempid}</td>
								  <!-- 25 --><td><u:truncate length="50" value="${temp_params}"/></td>
							     </tr>
							    </s:iterator>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="${ctx}/js/clipboard/clipboard.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
	<script type="text/javascript">
// 	$("li[name='testhight']").find('select,input').width(120); 
// 	$("li[name='testhight1']").find('select,input').width(150); 
	//导出Excel文件
	$(function(){
	})
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
		var channelid = $("#channelid").val();
		
		if($.trim(identify) == '' && $.trim(channelid) == ''){
			layer.msg("请输入标识或者选择通道号", {icon: 1,time: 1500}); 
			return;
		}
		
		var start_time = $("#start_time").val();
		var end_time = $("#end_time").val();
		if(start_time == "" || end_time == ""){
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

		mainForm.attr("action", "${ctx}/sendhis/exportExcel").submit();
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
		var channelid = $("#channelid").val();
		
		if($.trim(identify) == '' && $.trim(channelid) == ''){
			layer.msg("请输入标识或者选择通道号", {icon: 1,time: 1500}); 
			return;
		}
		
		var start_time = $("#start_time").val();
		var end_time = $("#end_time").val();
		if(start_time == "" || end_time == ""){
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
	
	var index = '';
	var content = '' ;
	function showdetail(a){
		content = '' ;
		var selector = '#'+a.id;
		var texts = $(selector).siblings('span').text();
		 content = texts ;
		 if($.browser.msie&&($.browser.version == "8.0")){
			 index = layer.open({
				  type: 1,
				  skin: 'layui-layer-rim', //加上边框
				  area: ['420px', '340px'], //宽高
				  content: '<button class="btn btnCls" onclick="tips()">点击复制</button><div class="winCls" id="copyContent">'+texts+'</div>'
				}); 
			}else{
				index = layer.open({
					  type: 1,
					  skin: 'layui-layer-rim', //加上边框
					  area: ['420px', '340px'], //宽高
					  content: '<button class="btn btnCls" data-clipboard-action="copy" data-clipboard-target="#copyContent">点击复制</button><div class="winCls" id="copyContent">'+texts+'</div>'
					}); 
			}
		
	}
	
	var clipboard = new Clipboard('.btn', {
	target: function() {
	    return document.querySelector('#copyContent');
	}
	});
	
	clipboard.on('success', function(e) {
		layer.close(index);
		layer.msg("复制成功");
	});
	
	clipboard.on('error', function(e) {
		layer.msg("复制失败，浏览器版本低，请手动复制或者重新点击");
	});
	
	function tips(){
		window.clipboardData.setData('text',content)
// 		if(window.clipboardData.setData('text',content)){
// 		layer.msg("复制成功");
// 		layer.close(index);
// 		}else{
// 			layer.msg("浏览器版本低，复制失败，请手动复制或者重新点击");
// 			layer.close(index);
// 		}
		
	}
	
	function channelOnChange(value, text, isInit){
		if(value == ""){
			return;
		}else{
			try {
				$.ajax({
					type : "post",
					async: false,
					data : {
						channelid : value
					},
					url : "${ctx}/channel/getIdentifyByChannelId",
					success : function(data) {
						if (data != null && data.identify != null) {
							$("input[name='identify']").val(data.identify);
						}else{
							console.log("查询通道标识为空");
						}
					}
				});
			} catch (e) {
				console.error("查询通道标识异常", e);
			}
		}
	}
	
	</script>
</body>
</html>