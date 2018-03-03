<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}通道黑名单</title>
</head>
<body menuId="64">
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
                <label class="control-label">通道号</label>
                <div class="controls">
                	<input type='text'  name="cid" id="cid" value="${data.cid}" onfocus="inputControl.setNumber(this, 9, 0, false)"></input>
                	<input type='hidden'  name="cid_bak" value="${data.cid}"></input>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">手机号码</label>
                <div class="controls">
                	<input type='text' name="phone" id="phone" maxlength="20" value="${data.phone}" onfocus="inputControl.setNumber(this, 11, 0, false)"></input>
                	<input type='hidden' name="phone_bak" value="${data.phone}"></input>
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
                <input type="button" value="确定" class="btn btn-success"  class="btn btn-success" onclick="save(this, '${data.id}')"/>
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
			cid: "required",
			phone: "required",
			remarks: {
				maxlength:120,
				required:true
			}
		}
	});
});

function save(btn, id){
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	var url;
	if(id == null){
		url = "${ctx}/channelBlackList/save";
	}else{
		url = "${ctx}/channelBlackList/save?id=" + id;
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
			
			if(data.result == "success"){
				$("input[name='cid_bak']").val($("input[name='cid']").val());
				$("input[name='phone_bak']").val($("input[name='phone']").val());
			}
			
			$("#msg").text(data.msg).show();
		},
		url : url,
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>