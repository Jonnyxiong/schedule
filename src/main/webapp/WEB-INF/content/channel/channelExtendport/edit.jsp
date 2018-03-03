<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}自签通道用户端口</title>
</head>
<body menuId="186">
<!--Action boxes-->
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
                <label class="control-label">通道选择</label>
                <div class="controls" name="channelid">
                	<s:if test="data.channelid == null">
                		<!-- 用户自签通道用户端口 -->
		                <u:select id="channelid" sqlId="channel" sqlParams="{isUserSignChannel:1}" placeholder="通道号" excludeValue="" value="${data.channelid}" ></u:select>
                	</s:if>
                	<s:else>
                		<input id="channelid" type="text" name="channelid" value="${data.channelid}" readonly="readonly"/>
                	</s:else>
                <span class="help-block">* 适用于类型为自签通道用户端口的通道</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">用户账号</label>
                <div class="controls">
                	<s:if test="data.id == null">
	                	<input type="text" name="clientid" value="${data.clientid}" class="checkClientIdAndSid" />
                	</s:if>
                	<s:else>
                		<input type="text" name="clientid" value="${data.clientid}" class="checkClientIdAndSid" readonly="readonly"/>
                	</s:else>
                </div>
              </div>
              
            <div class="control-group">
                <label class="control-label">通道用户端口</label>
                <div class="controls">
                  <input type="text" name="extendport" value="${data.extendport}" onfocus="inputControl.setNumber(this, 20, 0, false)" />
                  <span class="help-block">* 1-20位数字</span>
                </div>
            </div>
            
            <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                  <input type="text" name="remark" value="${data.remark}" maxlength="60"/>
                </div>
            </div>
              
              <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
              	<input type="hidden" name="id" value="${data.id}">
                <input type="button" onclick="save(this);" value="保  存" class="btn btn-success">
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
			channelid: {
				required : true,
				channelExtendportCheck1 : true
			},
			clientid: {
				required : true
			},
			extendport: {
				channelExtendportCheck2 : true,
				channelExtendportCheck3 : true
			}
		}
	});
	
	// 校验平台账号和子账号是否合法
	jQuery.validator.addMethod("checkClientIdAndSid",
			function(value, element) {
				var reg_clientId = /^[a-zA-Z0-9]{6}$/; 
				if(value.length == 6){
					return this.optional(element) || (reg_clientId.test(value));
				}
				
				return false;
			}, "请输入合法的6位子账号");
	
	jQuery.validator.addMethod("channelExtendportCheck1",
			function(value, element) {
				var checkResult = true;
				$.ajax({
			 		type : "post",
			 		async : false,
			 		url : "${ctx}/channelExtendport/channelExtendportCheck1",
			 		data : {
			 			id : $("input[name='id']").val(),
			 			channelid : value
			 		},
			 		success : function(result) {
			 			if (result != null && result.success != null) {
			 				checkResult = result.success;
			 			}
			 		}
			 	});
				return this.optional(element) || checkResult;
			}, "该通道已唯一分配给用户，请选择其他通道");
	
	jQuery.validator.addMethod("channelExtendportCheck2",
			function(value, element) {
				var checkResult = true;
				$.ajax({
			 		type : "post",
			 		async : false,
			 		url : "${ctx}/channelExtendport/channelExtendportCheck2",
			 		data : {
			 			id : $("input[name='id']").val(),
			 			channelid : $("#channelid").val(),
			 			extendport : value
			 		},
			 		success : function(result) {
			 			if (result != null && result.extendportUsable != null) {
			 				checkResult = result.extendportUsable;
			 			}
			 		}
			 	});
				return this.optional(element) || checkResult;
			}, "同一通道下扩展前缀重复");
	
	jQuery.validator.addMethod("channelExtendportCheck3",
			function(value, element) {
				var check = true;
				if($.trim(value) === ""){
					$.ajax({
				 		type : "post",
				 		async : false,
				 		url : "${ctx}/channelExtendport/channelExtendportCheck3",
				 		data : {
				 			id : $("input[name='id']").val(),
				 			channelid : $("#channelid").val()
				 		},
				 		success : function(result) {
				 			if (result != null && result.success != null) {
				 				check = result.success;
				 			}
				 		}
				 	});
				}
				return check;
			}, "该通道已存在分配端口的用户，不能设置该用户为唯一分配用户");
	
});



function save(btn){
	$("#msg").hide();
	if(!validate.form()){
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
			}
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/channelExtendport/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};


</script>
</body>
</html>