<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>组件运行信息</title>
<style type="text/css">
.ctltable{
	border-collapse: collapse;
	word-wrap:break-word;
/* 	table-layout:fixed; */
}
.ctltable tr{
	height: 20px;
} 
.ctltable td{
	
	overflow:hidden;
/* 	white-space: nowrap; // 不换行*/
	padding:10px;
/* 	word-wrap:break-word; */
	table-layout:fixed;
}
</style>
</head>
<body menuId="190">
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
						<div class="search">
							
							<form method="post" id="mainForm" action="${ctx}/componentRunInfo/query">
								<ul id="inputList">
									<li>
										<input type="text" name="component_name" value="${b_component_name }" maxlength="100" placeholder="组件名称" class="txt_250" />
										<input type="text" name="info_content" value="${b_info_content }" maxlength="4000" placeholder="信息内容" class="txt_250" />
                  						<u:select id="info_type" data="[{value:'',text:'信息类型:所有'},{value:'0',text:'队列长度'},{value:'1',text:'TCP连接数'}]" value="${b_info_type}" showAll="false" />

										更新时间：
										<input type="text" id="start_time" name="start_time" value="${start_time}" class="txt_177" value="开始时间" readonly="readOnly" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'end_time\')}'})">
										<span>至</span>
	            						<input type="text" id="end_time" name="end_time" value="${end_time}" class="txt_177" value="结束时间" readonly="readOnly" onfocus="WdatePicker({ dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'start_time\')}'})">
										
										<input id="submitBtn" type="button" value="搜索" onclick="submitForm()"/>
									</li>	
									
								</ul>
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped ctltable">
							<thead>
								<tr>
									 <th style='width:5%'>序号</th>
					                 <th style='width:12%'>组件名称</th>
					                 <th style='width:60%'>信息内容</th>
					                 <th style='width:8%'>信息类型</th>
					                 <th style='width:15%'>更新时间</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td>${component_name}</td>
							      <td class='info-content'>
							     	 <a id="a" href = 'javascript:void(0)' onclick="showdetail(this)" id="${rownum}">
							     	 <u:truncate length="280" value="${info_content}"/>
							     	 </a>
							      	 <span style="display: none;">${info_content}</span>
							      </td>
							      <td class="fontType">${info_type_name}</td>
							      <td>${update_date}</td>
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
	<script type="text/javascript" src="${ctx}/js/clipboard/clipboard.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
	<script type="text/javascript">
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
	
	function isBeyondOneMonth(date1,date2){
		var start_date = Date.parse(date1.replace('-','/'));
		var end_date = Date.parse(date2.replace('-','/'));
		var today = new Date();
		if(today - start_date >  31*24*60*60*1000){
			return false;
		}
		return true;
	}

	//获取开始时间
	function Appendzero(obj)  
    {  
        if(obj<10) return "0" +""+ obj;  
        else return obj;  
    }  
    function startTime(){
		var mydate = new Date();
		 
		var year=mydate.getFullYear();
        var month=mydate.getMonth()+1;  
        var date=mydate.getDate();  
        var weeknum=mydate.getDay();
        var str = year+"-"+Appendzero(month)+"-"+Appendzero(date)+" "+"00"+":"+"00"+":"+"00";
        return str;
	}
// 	$("#start_time").val(startTime());


	//获取当前时间
	function endTime(){
		var mydate = new Date();
		 
		var year=mydate.getFullYear();
        var month=mydate.getMonth()+1;  
        var date=mydate.getDate();  
        var weeknum=mydate.getDay();
        var hours = mydate.getHours();
        var minutes = mydate.getMinutes();
        var seconds = mydate.getSeconds();
        var str = year+"-"+Appendzero(month)+"-"+Appendzero(date)+" "+Appendzero(hours)+":"+Appendzero(minutes)+":"+Appendzero(seconds);
        return str;
		}
		
	
	// closs获取时间


	function submitForm(){
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
		flag = isBeyondOneMonth(start_time,end_time);
		if(flag == false){
			layer.msg("只能查询一个月内的记录", {icon: 1,time: 1500}); 
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
	
	$(function(){
		// 设置显示时间    (1)如果后台没有传入时间数据,则添加默认时间, (2)如果后台时间有时间传入则不设置时间	
		var default_start_time = $("#start_time").val();
		var default_end_time = $("#end_time").val();
		if(default_start_time == "" || default_start_time == null){
			$("#start_time").val(startTime());
		}
		if(default_end_time == "" || default_end_time == null){
			$("#end_time").val(endTime());
		}
	
	});
	
	</script>
</body>
</html>