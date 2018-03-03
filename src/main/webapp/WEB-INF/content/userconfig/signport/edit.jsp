<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.operater==null?'添加':'修改'}用户帐号和签名端口对应关系</title>
</head>
<body menuId="86">
 <div class="container-fluid">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal" method="post" action="#" id="mainForm">
              <div class="control-group">
                <label class="control-label">子账号</label>
                <div class="controls">
                	<input type="text" name="clientid" id="clientid" value="${data.clientid}" 
                	class="checkClientId checkClientIdAndGetSignport" onkeyup="this.value=this.value.toLowerCase()"></input>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">签名</label>
                <div class="controls">
                	<input type="text" name="sign" id="sign" value="${data.sign}" class="checkSign"></input>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">签名端口</label>
                <div class="controls">
                	<input type="text" name="signport" id="signport" value="${data.signport}" maxlength="20" readOnly="readonly"></input>
                	<input type="hidden" name="signport_endnumber" id="signport_endnumber" readOnly="readonly"></input>
                </div>
              </div>
               <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
              	<input type="hidden" name="id" value="${data.id}"></input>
                <input type="button" value="确定" class="btn btn-success"  class="btn btn-success" onclick="save(this)"/>
             	<input type="button" value="取 消" class="btn btn-error" onclick="back()"/>
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
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			clientid: "required",
			sign: "required",
			signport: "required"
		}
	});
	

	//账号规则：字母、数字、字母与数字组合，区分大小写
	jQuery.validator.addMethod("checkClientId",
			function(value, element) {
				var reg = /^[a-zA-Z0-9]{6}$/;
				return this.optional(element) || (reg.test(value));
			}, "请输入6位的字母、数字、字母与数字组合的账号");
			
	//校验clientid是否已经创建
	jQuery.validator.addMethod("checkClientIdAndGetSignport",
			function(value, element) {
				
				return this.optional(element) || checkClientIdAndGetSignport();
			}, "");
	
	jQuery.validator.addMethod("checkSign",
			function(value, element) {
				return this.optional(element) || (value.length <= 8);
			}, "签名必须小于等于8个字");
});

$(function(){
	if(getUrlParam("id") != null){
		$("#clientid").attr("readonly", "readonly");
	}
	
	var referer = "${referer}";
	var clientid = getUrlParam("clientid");
	if(referer.indexOf("/account/query") != -1){// 是否从账户管理跳转过来
		$("#clientid").attr("readonly", "readonly");
		$("#clientid").val(clientid);
		checkClientIdAndGetSignport(clientid);
	}
})

function checkClientIdAndGetSignport(){
	$("#clientid-error").remove();
	var clientid = $("input[name='clientid']").val();

	$.ajax({
		type : "post",
		async: false,
		url : "${ctx}/signport/checkClientIdAndGetSignport",
		data : {
			clientid : clientid
		},
		success : function(data) {
			if(getUrlParam("id") == null){
				$("#signport").val("");//获得签名端口前先清空
			}
			if(data == null){
				alert("服务器内部错误，请联系管理员");
			}
			if(data.status == -1){
				$('<label id="clientid-error" class="error" for="clientid">该子账号不存在</label>').insertAfter($("#clientid"));
			}else if(data.status == 0){
				$('<label id="clientid-error" class="error" for="clientid">该子账号已经禁用不能分配签名端口</label>').insertAfter($("#clientid"));
			}else{
				if(data.signportlen == -1){
					$('<label id="clientid-error" class="error" for="clientid">该子账号未配置通道组，请在用户通道组管理中配置</label>').insertAfter($("#clientid"));
				}else if(data.signportlen == 0){
					$('<label id="clientid-error" class="error" for="clientid">该子账号签名端口长度为0</label>').insertAfter($("#clientid"));
				}else{
					if(getUrlParam("id") == null){// 新建时
						if(Number(data.currentnumber) > Number(data.endnumber)){
							$('<label id="clientid-error" class="error" for="clientid">该子账号签名端口资源已经全部分配完毕</label>').insertAfter($("#clientid"));
						}else{
							$("#signport").val(data.currentnumber);
							$("#signport_endnumber").val(data.endnumber);
						}
					}
				}
			}
			
		}
	});
	
	return true;
}


function save(btn){
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	var options = {
		beforeSubmit : function(data) {
			$(btn).attr("disabled", true);
		},
		success : function(data) {
			$(btn).attr("disabled", false);
			
			if(data.result==null){
				$("#msg").text("服务器错误，请联系管理员").show();
				return;
			}
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/signport/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>