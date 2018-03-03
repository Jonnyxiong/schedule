<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> --%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}白关键字强制路由通道</title>
<script src="${ctx}/js/select2.min.js"></script>
<style>
.select2 span{
	display:block !important;
	margin-top: 0px !important;
}
.control-group .select2-container {
	width: 216px !important;
}
.select2-dropdown {
    width: 220px !important;
    margin-top: -10px;
}
#client_code-error.error {
 	float: right;
    margin-top: 7px;
}
</style>
</head>

<body menuId="291">
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

							<div id="signWhiteDiv" class="control-group">
								<label class="control-label signWhiteDiv">签名</label>
								<div class="controls">
									<input type="text" name="sign" value="${data.sign}" maxlength="21" />
								</div>
							</div>

							<div id="whitekeyWordDiv" class="control-group">
								<label class="control-label whitekeyWordDiv">白关键字</label>
								<div class="controls">
									<textarea name="whiteKeyword" id="whiteKeyword" rows="10" cols="80" maxlength="1000"
											  style="height: auto; width: auto; display: block;" ><c:out value="${data.whiteKeyword}"/></textarea>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">通道运营商类型</label>
								<div class="controls">
									<input type="text" id="operatorstypeName" value="${data.operatorstypeName}"  readonly="readonly"/>
									<input type="hidden" id="operatorstype" name="operatorstype" value="${data.operatorstype}"/>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">发送类型</label>
								<div class="controls">
									<u:select id="sendType" defaultValue="0" data="[{value:'1',text:'营销'},
                  {value:'0',text:'行业'}]" value="${data.sendType}" showAll="false" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">通道组ID</label>
								<div class="controls select2">
									<%-- 									<select id="channel_id" type="text" name="channel_id" value="${data.channel_id}" maxlength="11" /> --%>
									<u:select id="channelId" placeholder="" sqlId="channelGroup4whitekey" value="${data.channelId}" showAll="false" onChange="channelIdOnChange"/>
								</div>
							</div>
							<div id="clientCodeDiv" class="control-group">
								<label class="control-label">客户代码</label>
								<div class="controls" style="width: 278px;">
									<%-- 									<input type="text" id="client_code" name="client_code" value="${data.client_code}" maxlength="10" /> --%>
									<u:select id="clientCode" sqlId="findClientCode" value="${data.clientCode}" showAll="false"/>
									<span class="help-block">为*时表示此路由规则适用于所有用户</span>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">备注</label>
								<div class="controls">
									<input type="text" name="remarks" value="${data.remarks}" maxlength="20" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">&nbsp;</label>
								<div class="controls">
									<span id="msg" class="error" style="display: none;"></span>
								</div>
							</div>
							
							<div class="form-actions">
								<input type="hidden" id="id" name="id" value="${data.id}">
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
	/*
	* 去除客户代码星号选择
	*/
	$("option[value='*']").remove();


	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {

            channelId:{
				required:true
			},
            clientCode:{
				clientCodeValidator:true,
				maxlength:10
			},
			remarks:{
				maxlength:20
			}
		}
	});
	
//	jQuery.validator.addMethod("areaIdValidator",
//			function(value, element) {
//				var routeType = $("#route_type").val();
//				if(routeType == 1 && value == ''){
//					return false;
//				}
//				return true;
//			}, "请选择号段地区");
//
//	// phone_segment校验：必填校验
//	jQuery.validator.addMethod("phoneSegmentValidator1",
//			function(value, element) {
//				var routeType = $("#route_type").val();
//				if(routeType != 1 && value == ''){
//					return false;
//				}
//				return true;
//			}, "必须填写");
	
	// phone_segment校验：国内手机号码
//	jQuery.validator.addMethod("phoneSegmentValidator2",
//			function(value, element) {
//				var reg = /^1[34578]\d{9}$/;
//				var routeType = $("#route_type").val();
//				if(routeType == 0 && value != ''){
//					return reg.test(value);
//				}
//				return true;
//			}, "请填写合法的国内手机号码");
	
	// phone_segment校验：国外手机号段
//	jQuery.validator.addMethod("phoneSegmentValidator3",
//			function(value, element) {
//				var reg = /^\d{1,4}$/;
//				var routeType = $("#route_type").val();
//				if(routeType == 2 && value != ''){
//					return reg.test(value);
//				}
//				return true;
//			}, "国外手机号段为1至4位数字");
	
	jQuery.validator.addMethod("clientCodeValidator",
			function(value, element) {
				var type = $("#type").val();
				if(type != '' && value == ''){
					return false;
				}
				return true;
			}, "必须填写");

//
//    jQuery.validator.addMethod("mobileOperatorValidator",
//        function(value, element) {
//        	var routeType = $("#route_type").val();
//
//        	// 当路由类型为国内手机号码时进行校验
//        	if(routeType == 0){
//                // 0：全网 1: 移动  2:联通  3:电信  4:国际
//                var mobileOperator = getMobileOperator(value);
//                var channelOperator = Number($("#operatorstype").val());
//
//                // 路由通道为全网通道时，手机运营商类型任意
//                // 手机号码为国际时，通道运营商类型任意（此处不可能为国际）
//                if(channelOperator == 0 || mobileOperator == 4){
//                    return true;
//                }
//
//                // 其他情况下手机号码和通道的运营商类型必须一致
//                if(channelOperator != mobileOperator){
//                    return false;
//                }
//
//                return true;
//            }else{
//        	    return true;
//			}
//
//        }, "手机运营商与通道运营商类型不匹配");
	
	
	$("#channel_id").select2({});
	$("#client_code").select2({});
	
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

		url : "${ctx}/sysConf/whitekeyword/save",
		type : "post",
        success : function(res) {
            $(btn).attr("disabled", false);
			console.log(res);
            if(res.code == 500 && res.msg !=null){
                $("#msg").text(res.msg).show();
                return;
            }
            if(res.code != 0){
                layer.msg(res.msg, {icon:2})
                return;
            }
            //	$("input[name='id']").val(res.data.id);
            $("#msg").text(res.msg).show();
        },
		timeout : 10000
	};
	$("#mainForm").ajaxSubmit(options);
};
//
//function typeOnChange(value, text, isInit){
//	if(value == ''){
//		// 类型为“所有客户路由”时“客户代码”为*
//		var $client_code = $("#client_code").select2();
//        $client_code.val("*").trigger("change");
//
//       // $("#client_code").val("*").select2();
//
//        // 隐藏“客户代码”输入框
//		$("#clientCodeDiv").hide();
//
//
//	}else{
//		// 显示“客户代码”输入框
//		$("#clientCodeDiv").show();
//	}
//}

//function routeTypeOnChange(value, text, isInit){
//	var $phoneSegmentLabel = $(".phoneSegmentLabel");
//	switch(value){
//		case  '0':
//			$(".phoneSegmentLabel").html("国内手机号码");
//			$("#area_id").val("");
//			$("#areaIdDiv").hide();
//
//			$("#phoneSegmentDiv").show();
//			break;
//		case '1':
//			$("#areaIdDiv").show();
//			$("input[name='phone_segment']").val("");
//
//			$("#phoneSegmentDiv").hide();
//			break;
//		case '2':
//			$(".phoneSegmentLabel").html("国外手机号段");
//			$("#area_id").val("");
//			$("#areaIdDiv").hide();
//
//			$("#phoneSegmentDiv").show();
//			break;
//	}
//}

function channelIdOnChange(value, text, isInit){
	$.ajax({
		type : "post",
		async: false,
		url : "${ctx}/sysConf/whitekeyword/queryOperatorstypeByChannelId",
		data : {
			channelId : value
		},
		success : function(data) {
			if(data != null){
				var operatorstype = data.operater;

				var operatorstypeName;
				
				switch(operatorstype){
					case  0:
						operatorstypeName = "全网";
						break;
					case 1:
						operatorstypeName = "移动";
						break;
					case 2:
						operatorstypeName = "联通";
						break;
					case 3:
						operatorstypeName = "电信";
						break;
					case 4:
                        operatorstypeName= "国际";
						break;
				}
              //  alert(operatorstypeName);
				$("#operatorstype").val(data.operater);
				$("#operatorstypeName").val(operatorstypeName);
			}
		}
	});
}

</script>
</body>
</html>