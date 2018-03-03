<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}超频白名单</title>
</head>
<body menuId="138">
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
                <label class="control-label">用户帐号</label>
                <div class="controls">
                	<input type="text" value = "${data.clientid}" id="clientid" name="clientid">
                	<span class="help-block">6位数字字母组合的clientId</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">手机号码</label>
                <div class="controls">
                	<textarea name="phone" id="phone" rows="13" cols="120"
							  style="height: auto; width: auto; display: block;" ><c:out value="${data.phone}"/></textarea>
					<span class="help-block">1、当手机号码不为空时，多个号码间以逗号“,”分隔，对应数据库中多条记录，超频白名单为“用户账号 +手机号码”级别；<br>
											 2、当手机号码为空时，超频白名单为“用户账号”级别</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                	<input type="text" name="remarks" id="remarks" value="${data.remarks}" ></input>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                  <span id="tips" style="display:none;">请点击<a href='${ctx}/overrateWhite/exportError' target='_blank'><font color="green">下载</font></a>失败信息</span>
                </div>
              </div>
              <div class="form-actions">
<%--               	<input type="hidden" id="id" name="id" value="${data.id}"> --%>
                <input type="button" value="确定" class="btn btn-success"  class="btn btn-success" onclick="save(this)"/>
             	<input type="button" value="取 消" class="btn btn-error" onclick="toback()"/>
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
			clientid: {
				maxlength:6,
				checkClientId: true,
				required:true
			},
			remarks: {
				maxlength:128,
			},
		}
	});
});

function save(btn){
	var id = $("#id").val();
	$("#msg").hide();
	$("#tips").hide();
	if(!validate.form()){
		return;
	}
	
	/* var url;
	if(id == null || id ==''){
		url = "${ctx}/overrateWhite/save";
	}else{
		url = "${ctx}/overrateWhite/update?id=" + id;
	} */
	
	
	var url = "${ctx}/overrateWhite/save";
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
			
			if(data.existFail != null && data.existFail == 1){
				$("#tips").show();
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
		window.location.href="${ctx}/overrateWhite/query";
	}
</script>
</body>
</html>