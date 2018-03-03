<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}超频用户记录</title>
</head>
<body menuId="137">
 <div class="container-fluid">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
          <input type="hidden" value = "${data.id}" id="id">
            <form class="form-horizontal" method="post" action="#" id="mainForm">
              <div class="control-group">
                <label class="control-label">用户帐号（子帐户）或平台帐号</label>
                <div class="controls">
                	<input type="text" value = "${data.clientid}" id="clientid" name="clientid">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                	<input type="text" name="remarks" id="remarks" value="${data.remarks}"></input>
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
             	<input type="button" value="取 消" class="btn btn-error" onclick="toback()"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>


<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
<script type="text/javascript">
var validate;
$(function(){
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			clientid: {
				checkClientIdAndSid: true,
				required:true
			},
			remarks: {
				maxlength:128,
			}
		}
	});
});

$(function(){
})

function save(btn){
	var id = $("#id").val();
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	var url;
	if(id == null || id ==''){
		url = "${ctx}/overrateUserList/save";
	}else{
		url = "${ctx}/overrateUserList/update?id=" + id;
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
		url : url,
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};

	
	function toback(){
		window.location.href="${ctx}/overrateUserList/query";
	}
</script>
</body>
</html>