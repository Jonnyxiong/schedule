<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.middleware_id==null?'添加':'修改'}SMSP组件与MQ关联配置</title>
</head>

<body menuId="243">
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
								<label class="control-label">SMSP组件ID</label>
								<div class="controls">
<%-- 									<input type="text" name="component_id" value="${data.component_id}" maxlength="11" /> --%>
									<li>
										<u:select id="component_id" value="${data.component_id}" sqlId="findAllSmspComponent"/>
									</li>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">消息类型</label>
								<div class="controls">
									<u:select id="message_type" data="[{value:'',text:'请选择消息类型'},{value:'00',text:'DB消息'},{value:'01',text:'移动行业'},{value:'02',text:'移动营销'},
																		{value:'03',text:'联通行业'},{value:'04',text:'联通营销'},{value:'05',text:'电信行业'},
																		{value:'06',text:'电信营销'},{value:'07',text:'行业'},{value:'08',text:'营销'},
																		{value:'11',text:'异常移动行业'},{value:'12',text:'异常移动营销'},{value:'13',text:'异常联通行业'},
																		{value:'14',text:'异常联通营销'},{value:'15',text:'异常电信行业'},{value:'16',text:'异常电信营销'},
																		{value:'21',text:'上行消息'},{value:'22',text:'状态报告缓存'}]"
									value="${data.message_type}" showAll="false"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">MQ模式</label>
								<div class="controls">
									<u:select id="mode" data="[{value:'',text:'请选择MQ模式'},{value:'0',text:'生产者'},{value:'1',text:'消费者'}]" 
									value="${data.mode}" showAll="false" onChange="modeOnChange"/>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">MQ_ID</label>
								<div class="controls">
<%-- 									<input type="text" name="mq_id" value="${data.mq_id}" maxlength="11" /> --%>
									<li>
										<u:select id="mq_id" placeholder="请选择MQ_ID" value="${data.mq_id}" sqlId="findAllMqId"/>
									</li>
								</div>
							</div>
							
							<div id="getRateDiv" class="control-group">
								<label class="control-label">消息拉取速率</label>
								<div class="controls">
									<input type="text" name="get_rate" value="${data.get_rate}" maxlength="11" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">描述</label>
								<div class="controls">
									<input type="text" name="remark" value="${data.remark}" maxlength="100" />
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
	
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			component_id:{
				required:true
			},
			message_type:{
				required:true
			},
			mode:{
				required:true
			},
			mq_id:{
				required:true,
				maxlength:11
			},
            get_rate:{
				maxlength:11,
                digits:true,
                checkMqGetRate1:true,
                checkMqGetRate2:true
            },
            remark:{
                maxlength:100
            }
        },
		messages: {
			component_id : '请选择组件ID',
			message_type : '请选择消息类型',
			mode : '请选择MQ模式',
			mq_id : '请选择MQ_ID'
		}
	});

	jQuery.validator.addMethod("checkMqGetRate1",
			function(value, element) {
				var mode = $("#mode").val();
				if(mode == 1){
					if(value == '' || value == null || typeof(value)=="undefined"){
						return false;
					}else{
						return true;
					}
				}else{
					return true;
				}

			}, "请输入消息拉取速率");

	jQuery.validator.addMethod("checkMqGetRate2",
			function(value, element) {
				var mode = $("#mode").val();
				if(mode == 1){
                    if( !/^\d+$/.test(value) ){
                        return false;
                    }
                    if( value<0 || value>2147483647){
                        return false;
                    }
					return true;
				}else{
					return true;
				}

			}, "请输入[0,2147483647]范围整数");

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
			
			$("input[name='id']").val(data.id);
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/componentConf/mqRelation/update",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};

// MQ模式切换时事件
function modeOnChange(value, text, isInit) {
	if(value == 0){
	    $("#getRateDiv").hide();
        $("input[name='get_rate']").val(0);
	}else{
        $("#getRateDiv").show();
	}
}
</script>
</body>
</html>