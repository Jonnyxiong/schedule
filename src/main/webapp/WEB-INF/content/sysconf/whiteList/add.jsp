<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>添加系统黑名单</title>
</head>
<body menuId="13">
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
                <label class="control-label">系统黑名单</label>
                <div class="controls">
                	<textarea class="whiteListsTextarea" name="whiteLists" id="whiteLists"></textarea>
                  <span class="help-block">多个关键字间用分号";"分隔,如"134xxxx1234;134xxxx5678"</span>
                </div>
              </div>
               <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                  <input type="text" name="remark" value="${data.remarks}" maxlength="50" />
                </div>
              </div>
               <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
                <input type="button" value="确定" class="btn btn-success"  class="btn btn-success" onclick="save(this)"/>
             	<input type="button" value="取 消" class="btn btn-error" onclick="back()"/>
              </div>
            </form>
          </div>
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
			whiteLists: "required"
		},
		messages: {
			whiteLists: "请规则填写系统黑名单"
		}
	});
});

function save(btn){
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	var isPass = true;
	var whiteLists = $("#whiteLists").val().split(";");
	$.each(whiteLists,function(i,o){
		if(!$.isEmptyObject(o) && !/^1[0-9]{10}$/.test(o)){
			$("#msg").text("请规则填写系统黑名单").show();
			isPass = false;
			return false;
		}
	});
	if(!isPass){
		return ;
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
		url : "${ctx}/whiteList/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>