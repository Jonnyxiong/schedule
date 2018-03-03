<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>${data.middleware_id==null?'添加':'修改'}SMSP组件与MQ关联配置</title>

	<style type="text/css">
		
		#multipleSelectDiv .el-select{
			width: 700px;
		}
		
		#multipleSelectDiv .el-select .el-tag {
		    height: 32px;
		    line-height: 32px;
		    box-sizing: border-box;
		    margin: 3px 0 3px 6px;
		}
		
		#multipleSelectDiv .controls span {
		    display: inline-block;
		    font-size: 14px;
		    margin-top: 0px;
		}
		
		#multipleSelectDiv .el-input__inner{
			color: #fff;
		}
		#multipleSelectDiv input[readonly]{
			background-color: #fff;
		}
		#multipleSelectDiv .el-select__tags input[type="text"]{
 			border: none;
			background-color: rgba(255,255,255,0);
			outline:none !important;
			padding: 0px 0px 0px 0px;
		}
		
		#multipleSelectDiv .el-tag--primary {
		    background-color: #fff;
		    border-color: #28b779;
		    color: #28b779;
		}
		.el-select-dropdown.is-multiple .el-select-dropdown__item.selected {
		    color: #13ce66 !important;
		    background-color: #fff !important;
		}
	</style>
	
<!-- 引入element-ui样式 -->
<link rel="stylesheet" href="${ctx}/js/element-ui/element-ui.css" />
<!-- 引入element-ui组件库 -->
<script src="${ctx}/js/element-ui/vue.min.js"></script>
<script src="${ctx}/js/element-ui/element-ui.js"></script>
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
									<u:select id="component_id" value="${data.component_id}" sqlId="findAllSmspComponent"/>
								</div>
							</div>
							
							<div id="multipleSelectDiv" class="control-group">
			              		<div id="quanwang">
									<label class="control-label">组件生产者</label>
									<div class="controls">
										<el-select v-model="value1" name="producer" multiple filterable size="large" placeholder="请选择" >
										    <el-option
										      v-for="item in options1"
										      :label="item.label"
										      :value="item.value">
										    </el-option>
										</el-select>
									</div>
			              		</div>
			              		<div id="yidong">
									<label class="control-label">组件消费者</label>
									<div class="controls">
										<el-select v-model="value2" name="consumer" multiple filterable size="large" placeholder="请选择" >
										    <el-option
										      v-for="item in options2"
										      :label="item.label"
										      :value="item.value">
										    </el-option>
										</el-select>
									</div>
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

//vue实现多选通道
vm = new Vue({
	el : "#multipleSelectDiv",
	mounted: function(){
		var that = this;
		$.ajax({
			type : "post",
			async: false,
			url : "${ctx}/componentConf/mqRelation/queryAllMqQueue",
			success : function(data) {
				if (data != null) {
					that.options1 = data;
					that.options2 = data;
				}else{
					console.log("查询mq队列时出错！");
				}
			}
		});
	},
	data : {
		options1 : [],
		options2 : [],
		value1: '',
		value2: ''
	},
	watch : {
		value1 : function(value){
			this.$nextTick(function(){
				$("input[name='producer']").val(value);
				console.log("watch value1 :" + $("input[name='producer']").val());
			})
		},
		value2 : function(value){
			this.$nextTick(function(){
				$("input[name='consumer']").val(value);
				console.log("watch value2 :" + $("input[name='consumer']").val());
			})
		}
	}
});


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
			producer:{
				checkProducerAndConsumer: true
			},
			consumer:{
				checkProducerAndConsumer: true
			}
		},
		messages: {
			component_id : '请选择组件ID',
			message_type : '请选择消息类型'
		}
	});
	
	
	jQuery.validator.addMethod("checkProducerAndConsumer",
			function(value, element) {
				var producer = $("input[name='producer']").val()
				var consumer = $("input[name='consumer']").val()
				if($.trim(producer) == '' && $.trim(consumer) == ''){
					return false;
				}else{
					return true;
				}
				return this.optional(element) || (reg.test(value));
			}, "消费者和生产者不能均为空");
	
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
		url : "${ctx}/componentConf/mqRelation/create",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>