<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<s:set var="title_btn" value="#parameters.alarm_id==null ? '添 加' : '修 改'"/>
<html>
<head>
	<title>${param.alarm_id==null?'添加':'修改'}告警条件</title>
</head>
<body menuId="51">
<div class="container-fluid" menuI="1">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal" method="post" name="basic_validate" id="mainForm" novalidate="novalidate">
              <div class="control-group">
                <label class="control-label">选择通道号</label>
                <div class="controls">
                   <u:select id="channel_id" value="${data.channelid}" placeholder="通道号" sqlId="findAllChannel"/>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">提交失败告警值</label>
                <div class="controls">
                 	<input type="text" name="submit_failure_number" value="${data.submit_failure_number}" maxlength="100" onkeyup="this.value=this.value.replace(/[^\d\.]/g, '')"/>
                 	<span class="help-block">*请输入数字</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">发送失败告警值</label>
                <div class="controls">
                  <input type="text" name="send_failure_number" value="${data.send_failure_number}" maxlength="100" onkeyup="this.value=this.value.replace(/[^\d\.]/g, '')"/>
                  <span class="help-block">*请输入数字</span>
                </div>
              </div>
              <div id="send_failure_code_list">
              <div class="control-group failure_code">
                <label class="control-label">发送失败错误码</label>
                <div class="controls">
	              <input type="text" id="send_failure_code_1" value="${data.send_failure_code_1}" class="send_failure_code" maxlength="100"/>
	              <input type="button" id="operate_1" onclick="addFailureCode()" value="添加" class="btn btn-success operate" />
	              <span class="help-block">*请输入错误代码</span>
                </div>
              </div>
              </div>
           	<div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
	              <input type="hidden" name="alarm_id" value="${data.alarm_id}" />
	              <input type="hidden" name="send_failure_codes"  id="send_failure_codes" value="${data.send_failure_codes}" />
	              <input type="button" value="${param.alarm_id==null?'添 加':'修 改'}" class="btn btn-success" onclick="save(this)"/>
	              <input type="button" value="取 消" class="btn btn-error" onclick="location.href='${ctx}/alarm/query'"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>

<script type="text/javascript">
var validate;
$(function(){
	//错误码
	var send_failure_codes = $('#send_failure_codes').val();
	if(send_failure_codes){
		var codes = send_failure_codes.split(',');
		if(codes.length >=1){
			for(var i=1;i<=codes.length;i++){
				if(i > 1){addFailureCode();}
				$('#send_failure_code_'+i).val(codes[i-1]);
				$('#send_failure_code_'+i).val(codes[i-1]);
			}
		}
	}
	
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			channel_id: "required",
			submit_failure_number: "required",
			send_failure_number: "required"
		},
		messages: {
			channel_id: "请选择通道号",
			submit_failure_number: "请输入提交失败告警值",
			send_failure_number: "请输入发送失败错误码"
		}
	});
});

//校验错误码
function validateCode(){
	var len = $('.send_failure_code').length;
	var flag = true;
	var send_failure_codes='';
	for(var i=0;i<len;i++){
		if($('#send_failure_code_'+(i+1)).val() == ''){
			flag = false;
			continue;
		}
		send_failure_codes += $('#send_failure_code_'+(i+1)).val()+',';
	}
	
	$('#send_failure_codes').val(send_failure_codes.substring(0,send_failure_codes.length-1));
	
	return flag;
}

function save(btn){
	$("#msg").hide();
	var flag = true;
	if(!validate.form()){
		flag =false;
	}
	validateCode();
	//if(!validateCode()){
	//	flag =false;
		//$("#msg").text("错误码输入不正确，请确认").show();
	//}
	if(!flag){
		return;
	}
	
	var options = {
		beforeSubmit : function() {
			$(btn).attr("disabled", true);
		},
		success : function(data) {
			$(btn).attr("disabled", false);
			
			if(data.result==null){
				$("#msg").text("服务器错误，请联系管理员").show();
				return;
			}else if(data.result=="success"){
				setTimeout(function(){
					back();
				}, 1000);
			}
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/alarm/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}

/*添加错误码*/
function addFailureCode(){
	var len = $('.send_failure_code').length;
	var html = '<div class="control-group failure_code">'
             	+ '<label class="control-label"></label>'
                + '<div class="controls">'
	            + ' <input type="text" id="send_failure_code_'+(len+1)+'" maxlength="100" class="send_failure_code"/>'
	            + ' <input type="button" id="operate_'+(len+1)+'" onclick="deleteFailureCode(this)" value="删除" class="btn btn-success operate" /></div></div>';
	$('#send_failure_code_list').append(html);
}

/*删除错误码*/
function deleteFailureCode(obj){
	$(obj).closest($('.failure_code')).remove();
	var param_arr = ['send_failure_code','operate'];
	for(var i=0; i<param_arr.length; i++){
		$("."+param_arr[i]).each(function(index){
			$(this).attr('id',param_arr[i]+'_'+(index+1));
	   	});
	}
}
</script>
</body>
</html>