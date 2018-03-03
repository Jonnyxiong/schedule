<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}关键字超频设置</title>
	<style type="text/css">
		.widget-title .search,.widget-title .search .ul_left { float: left; display: inline; /*height: 36px;*/ margin-bottom: 0px; margin-left: 0px; }
		.widget-title .search,.widget-title .search .ul_right { float: right; display: inline; /*height: 36px;*/ margin-bottom: 0px; margin-left: 0px; }
		.inputNum {
			width: 60px;
			height: 20px;
		}
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
		
		#channelGroupDiv .el-select{
			width: 700px;
		}
		
		#channelGroupDiv .el-select .el-tag {
		    height: 32px;
		    line-height: 32px;
		    box-sizing: border-box;
		    margin: 3px 0 3px 6px;
		}
		
		#channelGroupDiv .controls span {
		    display: inline-block;
		    font-size: 14px;
		    margin-top: 0px;
		}
		
		#channelGroupDiv .el-input__inner{
			color: #fff;
		}
		#channelGroupDiv input[readonly]{
			background-color: #fff;
		}
		#channelGroupDiv .el-select__tags input[type="text"]{
 			border: none;
			background-color: rgba(255,255,255,0);
			outline:none !important;
			padding: 0px 0px 0px 0px;
		}
		
		#channelGroupDiv .el-tag--primary {
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
<body menuId="322">

<div class="container-fluid" menuI="1">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal" method="post" name="basic_validate" id="mainForm" novalidate="novalidate">

              <div class="clientid_div control-group">
                <label class="control-label">用户账号</label>
                <div class="controls">
					<%--u:select id="userid"  sqlId="findClientId" value="${data.userid}" showAll="false"/>--%>
						<select  name="userid" id="userid"  value="${data.userid}" maxlength="40"  class="txt_250">
							<input type="hidden" id="clientIdHidden" value="${data.userid}">

						</select>
					<span class="help-block">* 为特殊用户账号，表示规则适用所有用户账号</span>
                </div>
              </div>
			<div class="control-group">
					<label class="control-label">签名</label>
					<div class="controls">
						<input type="text" name="sign" value="${data.sign}" />
						<span class="help-block">为*，此时为用户账号关键字超频处理逻辑，用户账号级别</span>
					</div>
			</div>

				<div class="control-group">
					<div class="controls" style="padding-top: 15px">
						关键字超频默认限制设置：
					</div>
					<div class="controls">
						<input class="inputNum" type="text" id="overRateNumS" name="overRateNumS" value="${data.overRateNumS}" maxlength="20" style="width: 60px; height: 20px"/>
						条
						<input class="inputNum" type="text" id="overRateTimeS" name="overRateTimeS" value="${data.overRateTimeS}" maxlength="20" style="width: 60px; height: 20px"/>
						秒
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="inputNum" type="text" id="overRateNumM" name="overRateNumM" value="${data.overRateNumM}" maxlength="20" style="width: 60px; height: 20px"/>
						条
						<input class="inputNum" type="text" id="overRateTimeM" name="overRateTimeM" value="${data.overRateTimeM}" maxlength="20" style="width: 60px; height: 20px"/>
						分钟
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input class="inputNum" type="text" id="overRateNumH" name="overRateNumH" value="${data.overRateNumH}" maxlength="20" style="width: 60px; height: 20px"/>
						条
						<input class="inputNum" type="text" id="overRateTimeH" name="overRateTimeH" value="${data.overRateTimeH}" maxlength="20" style="width: 60px; height: 20px"/>
						小时
					</div>
					<div class="controls" style="display: none" id="divOV">
						<span style="color: red; display: none" id="overRate_validate"></span>
					</div>
				</div>

            <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
				  <input type="hidden" id="state" name="state" value="${data.state}" />
	              <input type="hidden" id="hidden_id" name="id" value="${data.id}" />
	              <input type="hidden" name="userid_bak" value="${data.userid}" />
	              <input type="button" value="${data.userid==null?'添 加':'修 改'}" class="btn btn-success" onclick="save(this)"/>
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
var overRate_validate = true;


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
    $("#userid").select2({
        data : select2Date
    })

// select2查询后的值回显
    var clientIdValueFromServer = $("#clientIdHidden").val();
    $("#userid").val(clientIdValueFromServer).trigger("change");

	var id = getUrlParam("id");
	if( null != id && id != ""){
		console.log("编辑")
        $("#userid").attr("disabled","disabled");
	}

	// 超频配置只能配置一条，存在一条配置时隐藏添加按钮
	var overRateConfigNum = $(".template_overrate_group").find('.overRateConfig').length;
	if(overRateConfigNum >= 2){
		 $(".addBtn").hide();//隐藏添加按钮
	}
	
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			userid: "required",
			sign: "required",
            overRateNumS: "required",
            overRateTimeS: "required",
            overRateNumM: "required",
            overRateTimeM: "required",
            overRateNumH: "required",
            overRateTimeH: "required"
		},
		messages: {
            sign: "请输入签名"
		}
	});
	


	
});


// 超频配置校验
function overRateValiS(ons,ots,onm,otm,onh,oth,valid,odiv){
	 overRate_validate = false;
	
	if(ons == "" || ots == "" || onm == "" || otm == "" || onh == "" || oth == "") {
		valid.text("参数不能为空");
		odiv.css('display','block');
		valid.css('display','block');
        overRate_validate = false;
	}else {
		var onsValue = Number(ons);
		var otsValue = Number(ots);
		var onmValue = Number(onm);
		var otmValue = Number(otm);
		var onhValue = Number(onh);
		var othValue = Number(oth);
		if(!/^\d+$/.test(onsValue) || !/^\d+$/.test(otsValue) || !/^\d+$/.test(onmValue) || !/^\d+$/.test(otmValue) 
			|| !/^\d+$/.test(onhValue) || !/^\d+$/.test(othValue)){
			valid.text("设置参数需填写非负整数");
			odiv.css('display','block');
			valid.css('display','block');
			overRate_validate = false;
	  	}else if(onsValue > 100 || onmValue > 100){
	  		valid.text("秒（分钟）频率次参数不能大于100");
	  		odiv.css('display','block');
	  		valid.css('display','block');
			overRate_validate = false;
	  	}else if(onhValue > 1000){
	  		valid.text("小时频率次参数不能大于1000");
	  		odiv.css('display','block');
	  		valid.css('display','block');
			overRate_validate = false;
	  	}else if(otsValue > 120 || otmValue > 120){
	  		valid.text("秒(分钟)频率时间参数不能大于120秒(分钟)");
	  		odiv.css('display','block');
			valid.css('display','block');
			overRate_validate = false;
	  	}else if(othValue > 24){
	  		valid.text("小时频率时间参数不能大于24小时");
	  		odiv.css('display','block');
			valid.css('display','block');
			overRate_validate = false;
	  	}
	  	else if(onmValue < onsValue || onhValue < onmValue){
	  		valid.text("分钟频率次数必须大于等于秒频率次数，小时频率次数必须大于等于分钟频率次数");
	  		odiv.css('display','block');
	  		valid.css('display','block');
			overRate_validate = false;
	  	}else{
	  		valid.text("");
	  		odiv.css('display','none');
	  		valid.css('display','none');
	  		overRate_validate = true;
	  	}
	}
	
	
	$("#msg").html("");
	$("#msg").text("");

} 

function save(btn){
	$("#msg").hide();
    $("#msg").html("");
    $("#msg").text("");

	
	if(!validate.form()){
		return;
	}
	var userId=$("#userid").val();
	var sign=$("input[name='sign']").val();
	if(userId=="*" && sign=="*"){
        $("#msg").text("关键字超频不支持用户账号为*且签名为*！").show();
	    return;
	}




    var ons = $("#overRateNumS").val();
    var ots = $("#overRateTimeS").val();
    var onm = $("#overRateNumM").val();
    var otm = $("#overRateTimeM").val();
    var onh = $("#overRateNumH").val();
    var oth = $("#overRateTimeH").val();
    var odiv = $("#divOV");
    var ovs = $("#overRate_validate");

    overRateValiS(ons,ots,onm,otm,onh,oth,ovs,odiv);


    if(!overRate_validate){
        return;
    }
	var options = {
		beforeSubmit : function() {
			$(btn).attr("disabled", true);
		},
		success : function(data) {
			$(btn).attr("disabled", false);
			
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

		//	$("input[name='userid_bak']").val($("input[name='clientid']").val());


		},
		url : "${ctx}/overrate/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};






</script>
</body>
</html>