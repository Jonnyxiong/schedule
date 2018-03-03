<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> --%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --%>

<html>
<head>
<title>${data.id==null?'添加':'修改'}号码/号段强制路由通道</title>
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

<body menuId="249">
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
								<label class="control-label">强制路由类型</label>
								<div class="controls">
									<u:select id="route_type" data="[{value:'0',text:'国内手机号码路由'},{value:'1',text:'国内手机号段路由'},{value:'2',text:'国外手机号段路由'}]" 
									value="${data.route_type}" showAll="false" onChange="routeTypeOnChange"/>
								</div>
							</div>
							
							<div id="areaIdDiv" class="control-group">
				              	<label class="control-label">地区</label>
				                <div class="controls">
				                    <u:select id="area_id" placeholder="请选择号段地区" value="${data.area_id}" sqlId="findAllSegCode" showAll="false"/>
				                </div>
				            </div>
				            
							<div id="phoneSegmentDiv" class="control-group">
								<label class="control-label phoneSegmentLabel">手机号码</label>
								<div class="controls">
									<input type="text" name="phone_segment" value="${data.phone_segment}" maxlength="21" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">通道组号</label>
								<div class="controls select2">
<%-- 									<select id="channel_id" type="text" name="channel_id" value="${data.channel_id}" maxlength="11" /> --%>
									<u:select id="channel_id" placeholder="请选择通道组" sqlId="channelGroup4seg" value="${data.channel_id}" showAll="false" onChange="channelIdOnChange"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">通道组运营商类型</label>
								<div class="controls">
									<input type="text" id="operatorstypeName" value="${data.operatorstypeName}" readonly="readonly"/>
									<input type="hidden" id="operatorstype" name="operatorstype" value="${data.operatorstype}"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">类型</label>
								<div class="controls">
									<u:select id="type" data="[{value:'',text:'所有客户路由'},{value:'0',text:'包含的客户路由'},{value:'1',text:'包含的客户不路由'}]" 
									value="${data.type}" showAll="false" onChange="typeOnChange"/>
<!-- 									<span class="help-block">为空时表示此路由规则适用于所有用户</span> -->
								</div>
							</div>
							
							<div id="clientCodeDiv" class="control-group">
								<label class="control-label">客户代码</label>
								<div class="controls" style="width: 278px;">
<%-- 									<input type="text" id="client_code" name="client_code" value="${data.client_code}" maxlength="10" /> --%>
									<u:select id="client_code" sqlId="findClientCode" value="${data.client_code}" showAll="false"/>
								<span class="help-block">为*时表示此路由规则适用于所有用户</span>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">发送类型</label>
								<div class="controls">
									<u:select id="send_type" defaultValue="1" data="[{value:'1',text:'营销'},
                  {value:'0',text:'行业'}]" value="${data.send_type}" showAll="false" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">备注</label>
								<div class="controls">
									<input type="text" name="remarks" value="${data.remarks}" maxlength="200" />
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
var intelvalidate;
var indvalidate;
$(function(){
	
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			area_id:{
				areaIdValidator:true
			},			
			phone_segment:{
				phoneSegmentValidator1:true,
				phoneSegmentValidator2:true,
				phoneSegmentValidator3:true,
                mobileOperatorValidator:true,
				maxlength:21,
				digits:true
			},

			channel_id:{

				required:true
			},
			client_code:{
				clientCodeValidator:true,
				maxlength:10
			},
			remarks:{
				maxlength:200
			}
		}
	});
	
	jQuery.validator.addMethod("areaIdValidator",
			function(value, element) {
				var routeType = $("#route_type").val();
				if(routeType == 1 && value == ''){
					return false;
				}
				return true;
			}, "请选择号段地区");
	
	// phone_segment校验：必填校验
	jQuery.validator.addMethod("phoneSegmentValidator1",
			function(value, element) {
				var routeType = $("#route_type").val();
				if(routeType != 1 && value == ''){
					return false;
				}
				return true;
			}, "必须填写");
	
	// phone_segment校验：国内手机号码
	jQuery.validator.addMethod("phoneSegmentValidator2",
			function(value, element) {
				var reg = /^1[34578]\d{9}$/;
				var routeType = $("#route_type").val();
				if(routeType == 0 && value != ''){
					return reg.test(value);
				}
				return true;
			}, "请填写合法的国内手机号码");
	
	// phone_segment校验：国外手机号段
	jQuery.validator.addMethod("phoneSegmentValidator3",
			function(value, element) {
				var reg = /^\d{1,4}$/;
				var routeType = $("#route_type").val();
				if(routeType == 2 && value != ''){
					return reg.test(value);
				}
				return true;
			}, "国外手机号段为1至4位数字");
	
	jQuery.validator.addMethod("clientCodeValidator",
			function(value, element) {
				var type = $("#type").val();
				if(type != '' && value == ''){
					return false;
				}
				return true;
			}, "必须填写");


    jQuery.validator.addMethod("mobileOperatorValidator",
        function(value, element) {
        	var routeType = $("#route_type").val();
            var mobileOperator = getMobileOperator(value);
            var channelOperator = Number($("#operatorstype").val());
        	// 当路由类型为国内手机号码时进行校验
        	if(routeType == 0){
                // 0：全网 1: 移动  2:联通  3:电信  4:国际


                // 路由通道为全网通道时，手机运营商类型任意
                // 手机号码为国际时，通道运营商类型任意（此处不可能为国际）
                if(channelOperator == 0){
                    return true;
                }
               if( mobileOperator == 4){
                   layer.msg("国内手机号码路由,不能选择国际运营商！！", {icon:2});
                   indvalidate=false;
                   return false;
				}
                // 其他情况下手机号码和通道的运营商类型必须一致
                if(channelOperator !=0 && channelOperator !='' && channelOperator != mobileOperator){
                    layer.msg("手机运营商与通道组运营商类型不匹配！", {icon:2});
                    return false;
                }

                return true;
            }else  if(routeType == 1 && channelOperator ==4 ){
                layer.msg("国内手机号段路由,不能选择国际运营商！！", {icon:2});
                indvalidate=false;
                return false;
			}else if(routeType == 2 && channelOperator !='' && channelOperator !=4 && channelOperator !=0){
                layer.msg("国外手机号段路由,只能选择国际运营商或全网通道组！", {icon:2});
                intelvalidate=false;
                return false;
			} else{
        	    return true;
			}
            intelvalidate=true;
            indvalidate=true;
        }, "");
	
	
	$("#channel_id").select2({});
	$("#client_code").select2({});
	
});


function save(btn){
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	if(!intelvalidate){
        layer.msg("强制路由类型选择运营商类型有误,请重新选择！", {icon:2});
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
            if(data.result=="failure"){
                layer.msg(data.msg, {icon: 2});
                return;
            }

			$("input[name='id']").val(data.id);
		//	$("#msg").text(data.msg).show();
            layer.msg(data.msg, {icon: 1,time: 1500},function(){
                back();
            });
		},
		url : "${ctx}/sysConf/segmentChannel/save",
		type : "post",
		timeout : 30000
	};
	$("#mainForm").ajaxSubmit(options);
};

function typeOnChange(value, text, isInit){
	if(value == ''){
		// 类型为“所有客户路由”时“客户代码”为*
		var $client_code = $("#client_code").select2();
        $client_code.val("*").trigger("change");

       // $("#client_code").val("*").select2();

        // 隐藏“客户代码”输入框
		$("#clientCodeDiv").hide();


	}else{
		// 显示“客户代码”输入框
		$("#clientCodeDiv").show();
	}
}

function routeTypeOnChange(value, text, isInit){
	var $phoneSegmentLabel = $(".phoneSegmentLabel");
	switch(value){
		case  '0':
			$(".phoneSegmentLabel").html("国内手机号码");
			$("#area_id").val("");
			$("#areaIdDiv").hide();
			
			$("#phoneSegmentDiv").show();
			break;
		case '1':
			$("#areaIdDiv").show();
			$("input[name='phone_segment']").val("");
			
			$("#phoneSegmentDiv").hide();
			break;
		case '2':
			$(".phoneSegmentLabel").html("国外手机号段");
			$("#area_id").val("");
			$("#areaIdDiv").hide();
			
			$("#phoneSegmentDiv").show();
			break;
	}
}

function channelIdOnChange(value, text, isInit){
	$.ajax({
		type : "post",
		async: false,
		url : "${ctx}/sysConf/segmentChannel/queryOperatorstypeByChannelId",
		data : {
			channelId : value
		},
		success : function(data) {
			if(data != null){
				var operatorstype = data.operatorstype;
				var operatorstypeName;
				var phoneSegment=$("input[name='phone_segment']").val();
                var mobileOperator = getMobileOperator(phoneSegment);
                var routeType = $("#route_type").val();
				switch(operatorstype){
					case  '0':
						operatorstypeName = "全网";
						break;
					case '1':
						operatorstypeName = "移动";
						break;
					case '2':
						operatorstypeName = "联通";
						break;
					case '3':
						operatorstypeName = "电信";
						break;
					case '4':
						operatorstypeName = "国际";
						break;
                    default:
                        operatorstypeName = "未知";
				}
				if(routeType==0){
                    // 其他情况下手机号码和通道的运营商类型必须一致
                    if(operatorstype!=0 && operatorstype !=undefined && operatorstype != mobileOperator){
                        layer.msg("手机运营商与通道组运营商类型不匹配！", {icon:2});
                        intelvalidate=false;
                        return false;
                    }
				} else if(operatorstype !=4 && operatorstype !=0 && routeType==2){
                    layer.msg("国外手机号段路由,只能选择国际运营商或全网通道组！", {icon:2});
                    intelvalidate=false;
                    return false;
				}else  if((routeType==0 || routeType ==1)&& operatorstype==4){
                    layer.msg("国内手机号码路由及国内手机号段路由,只能选择非国际运营商", {icon:2});
                    intelvalidate=false;
                    return false;
				}
                intelvalidate=true;
				$("#operatorstype").val(data.operatorstype);
				$("#operatorstypeName").val(operatorstypeName);
			}
		}
	});
}

</script>
</body>
</html>