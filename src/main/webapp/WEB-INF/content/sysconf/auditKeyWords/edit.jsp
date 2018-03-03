<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}超频关键字</title>
	<style type="text/css">
		.control-group .select2-container {
			width: 220px !important;
		}
		.select2-selection{
			width: 600px;
		}
		.checkbox-inline {
			display: inline-block;
			padding-left: 20px;
			margin-bottom: 0;
			font-weight: normal;
			vertical-align: middle;
			cursor: pointer;
		}
		.select2-dropdown {
			margin-top: -26px;
		}

	</style>

</head>

<body menuId="247">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-list"></i>
						</span>
						<h5></h5>
					</div>
					<div class="widget-content nopadding">
						<form class="form-horizontal" method="post" action="#" id="mainForm">
							
							<div class="control-group">
								<label class="control-label">超频关键字</label>
								<div class="controls">
									<input type="text" name="keyword" value="${data.keyword}" maxlength="60" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">用户帐号</label>
								<div class="controls">
									<%--<input type="text" name="clientid" id="clientid" value="${data.clientid}" maxlength="6" />--%>
										<select  name="clientid" id="clientid"  value="${data.clientid}" maxlength="40"  class="txt_250">
											<input type="hidden" id="clientIdHidden" value="${data.clientid}">

										</select>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">备注</label>
								<div class="controls">
									<input type="text" name="remarks" value="${data.remarks}" maxlength="128" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">&nbsp;</label>
								<div class="controls">
									<span id="msg" class="error" style="display: none;"></span>
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

    //获取用户账号
    $.ajax({
        url : '${ctx}/smsaudit/autoTemplate/accounts',
        dataType : 'json',
        async : false,
        success : function(res){
            for(var i = 0; i < res.length; i++){
                res[i].id = res[i].clientid;
                res[i].text = res[i].clientid + "-" + res[i].name;
            }
            res.unshift({id:"*",text:"*"})
            select2Date = res;
        }
    })
    $("#clientid").select2({
        data : select2Date
    })

// select2查询后的值回显
    var clientIdValueFromServer = $("#clientIdHidden").val();
    $("#userid").val(clientIdValueFromServer).trigger("change");


	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			keyword:{
				required:true,
				maxlength:60
			},
			clientid:{
				required:true,
				maxlength:6,
				checkClientId:true
			},
			remarks:{
				maxlength:128
			}
		}
	});
	
	//校验短信账号账号是否合法
	jQuery.validator.addMethod("checkClientId",
		function(value, element) {
			var reg_clientId = /^[a-zA-Z0-9]{6}$/; 
			if(value=="*"){
				return true;
			}
			if(value.length == 6){
				return this.optional(element) || (reg_clientId.test(value));
			}
			
			return false;
	}, "请输入合法的6位子账号；输入 * 则表示平台级别限制");
	
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
			
//			if(data.result==null){
//				$("#msg").text("服务器错误，请联系管理员").show();
//				return;
//			}
            if(data.code==500){
                layer.msg(data.msg, {icon:2});
                //	$("#msg").text("服务器错误，请联系管理员").show();
                return;
            }

            if(data.code == 0){
//                $("#msg").text(data.msg).show();
//               back();
                layer.msg(data.msg, {icon: 1,time: 1500},function(){
                    back();
                });
            }



			$("input[name='id']").val(data.id);
		//	$("#msg").text(data.msg).show();
		},
		url : "${ctx}/sysConf/auditKeyWords/save",
		type : "post",
		timeout : 30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>