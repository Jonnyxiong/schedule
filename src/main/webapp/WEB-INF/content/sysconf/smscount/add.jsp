<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}配置(${data.tableid=='bysid'?'sid':'通道号'})</title>
</head>
<body menuId="48">
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
            	
            <s:if test="data.tableid=='bychannel'">
              <div class="control-group">
                <label class="control-label">通道号</label>
                <div class="controls">
                  <input type="text" name="channelid" value="${data.channelid}" onfocus="inputControl.setNumber(this, 9, 0, false)" />
                  <span class="help-block">* 1-9位数字</span>
                </div>
              </div>
              </s:if>
              <s:if test="data.tableid=='bysid'">
              <div class="control-group">
                <label class="control-label">用户sid</label>
                <div class="controls">
                	<input type="text" name="sid" value="${data.sid}">
                </div>
              </div>
              </s:if>
              <div class="control-group">
                <label class="control-label">条数</label>
                <div class="controls">
                  <input type="text" name="smscount" value="${data.smscount}" maxlength="30" onfocus="inputControl.setNumber(this, 9, 0, false)" />
              	  <span class="help-block">* 1-9位数字</span>
                </div>
              </div>
               <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
              	
              	<input type="hidden" name="tableid" value="${data.tableid }" />
              	<input type="hidden" name="id" value="${data.id}" />
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
			smscount: "required",
			cid:"required",
			sid:"required"
		},
		messages: {
			smscount: "请规则填写短信条数",
			cid:'请规则填写通道号',
			sid:'请规则填写用户sid'
		}
	});
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
		url : "${ctx}/smscount/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>