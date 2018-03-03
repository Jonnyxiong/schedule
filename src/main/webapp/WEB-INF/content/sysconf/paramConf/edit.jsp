<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
	<title>${data.param_id==null?'添加':'修改'}系统参数</title>
	<style type="text/css">
		.inputNum {
			width: 60px;
			height: 20px;
		}
	</style>
</head>
<body menuId="10">
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
              <div class="control-group">
                <label class="control-label">参数名称</label>
                <div class="controls">
                	<s:if test="data.param_id == null">
                  		 <u:select id="param_key" value="${data.param_key}" dictionaryType="param_key"  excludeValue=""/>
                	</s:if>
                	<s:if test="data.param_id != null">
                		<input style="border-style:none; background: none;width: 300px" type="text" name="param_key" readonly="readonly" value="<u:ucparams key="${data.param_key}"  type="param_key"/>" maxlength="50"/>
                	</s:if>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">参数值</label>
                <s:if test="data.param_key == 'KEYWORD_OVERRATE'">
	              	<s:set name="param_value" value='data.param_value.split("\\\\;")'/>
		            <s:set name="overrates" value='#param_value[0].split("\\\\/")'/>
		            <s:set name="overratem" value='#param_value[1].split("\\\\/")'/>
		            <s:set name="overrateh" value='#param_value[2].split("\\\\/")'/>

	                <div class="controls">
	                    <input class="inputNum" type="text" name="overRate_num_s" value="<s:property value="#overrates[0]"/>" maxlength="20" />
	                  	条
	                  	<input class="inputNum" type="text" name="overRate_time_s" value="<s:property value="#overrates[1]"/>" maxlength="20"/>
	                  	秒
	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    <input class="inputNum" type="text" name="overRate_num_m" value="<s:property value="#overratem[0]"/>" maxlength="20" />
	                  	条
	                  	<input class="inputNum" type="text" name="overRate_time_m" value="<s:property value="#overratem[1]"/>" maxlength="20"/>
	                  	分
	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                  	<input class="inputNum" type="text" name="overRate_num_h" value="<s:property value="#overrateh[0]"/>" maxlength="20"/>
	                  	条
	                  	<input class="inputNum" type="text" name="overRate_time_h" value="<s:property value="#overrateh[1]"/>" maxlength="20"/>
	                  	小时
	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						开关:<input name="overRate_switch" type="checkbox" value="<s:property value="#param_value[3]"/>"/>
	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

						签名属性:<u:select id="overrate_sign" data="[{value:'0',text:'0(用户+签名)'},{value:'1',text:'1(*+签名)'},{value:'2',text:'2(用户+*)'}]"  showAll="false"   />
	                  	<input id="OVERRATE_CONFIG" name="param_value" style="display:none;" value=""/>
	                </div>
	                <div class="controls" style="display: none" id="divOV">
                  		<span style="color: red; display: none" id="overRate_validate">设置参数需填写非负整数,0条/0秒或0条/0小时表示该设置参数无效</span>
	                </div>
                </s:if>
				  <s:elseif test="data.param_key == 'OVERRATE' ">
					  <s:set name="param_value" value='data.param_value.split("\\\\;")'/>
					  <s:set name="overrates" value='#param_value[0].split("\\\\/")'/>
					  <s:set name="overratem" value='#param_value[1].split("\\\\/")'/>
					  <s:set name="overrateh" value='#param_value[2].split("\\\\/")'/>

					  <div class="controls">
						  <input class="inputNum" type="text" name="overRate_num_s1" value="<s:property value="#overrates[0]"/>" maxlength="20" />
						  条
						  <input class="inputNum" type="text" name="overRate_time_s1" value="<s:property value="#overrates[1]"/>" maxlength="20"/>
						  秒
						  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  <input class="inputNum" type="text" name="overRate_num_m1" value="<s:property value="#overratem[0]"/>" maxlength="20" />
						  条
						  <input class="inputNum" type="text" name="overRate_time_m1" value="<s:property value="#overratem[1]"/>" maxlength="20"/>
						  分
						  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  <input class="inputNum" type="text" name="overRate_num_h1" value="<s:property value="#overrateh[0]"/>" maxlength="20"/>
						  条
						  <input class="inputNum" type="text" name="overRate_time_h1" value="<s:property value="#overrateh[1]"/>" maxlength="20"/>
						  小时
						  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  开关:<input name="overRate_switch1" type="checkbox" value="<s:property value="#param_value[3]"/>"/>
						  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  <input id="OVERRATE_CONFIG1" name="param_value" style="display:none;" value=""/>
					  </div>
					  <div class="controls" style="display: none" id="divOV2">
						  <span style="color: red; display: none" id="overRate_validate2">设置参数需填写非负整数,0条/0秒或0条/0小时表示该设置参数无效</span>
					  </div>
				  </s:elseif>



                <s:elseif test="data.param_key == 'CHANNELWARNING'">
                	<s:set name="param_value" value='data.param_value.split("\\\\;")'/>
                	<div class="controls">
<!--                 		时间间隔 -->
<%--                 		<input class="inputNum" type="text" name="channelwarning_s" value="<s:property value="#param_value[0]"/>" maxlength="20"/> --%>
<!-- 	                  	秒 -->
<!-- 	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
<!-- 	                  	统计短信条数 -->
	                  	<input class="inputNum" type="text" name="channelwarning_n" value="<s:property value="#param_value[0]"/>" maxlength="20"/>
	                  	条
	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                  	预警频率
	                  	<input class="inputNum" type="text" name="channelwarning_h" value="<s:property value="#param_value[1]"/>" maxlength="20"/>
	                  	分钟
	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                  	<span>*发送一次预警的间隔时间</span>
	                  	<input id="CHANNELWARNING" name="param_value" style="display:none;" value=""/>
	                </div>
	                <div class="controls" style="display: none" id="divCW">
	                	<span style="color: red; display: none" hidden="hidden" id="channelwarnvalid">参数不能为空</span>
	                </div>
                </s:elseif>
                <s:elseif test="data.param_key == 'LOG_LEVEL'">
                	<div class="controls">
	                	<input type="text" id="logLevelVal" name="param_value" value="${data.param_value}" class="loglevelValidator" maxlength="20"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
                <s:elseif test="data.param_key == 'AUDIT'">
                	<s:set name="param_value" value='data.param_value.split("\\\\;")'/>
                	<div class="controls">
	                  	审核内容锁定时间
	                  	<input class="inputNum" type="text" name="audit_lock_time" value="<s:property value="#param_value[0]"/>" maxlength="20"/>
	                  	<span for="audit_lock_time">分钟 </span>
	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                  	审核结果有效期
	                  	<input class="inputNum" type="text" name="audit_result_time" value="<s:property value="#param_value[1]"/>" maxlength="20"/>
	                  	<span for="audit_result_time">分钟 </span>
	                  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                  	<input id="AUDIT" name="param_value" style="display:none;" value=""/>
	                </div>
	                <div class="controls" style="display: none" id="div_audit_valid">
	                	<span style="color: red;  hidden="hidden" id="audit_valid"></span>
	                </div>
                </s:elseif>
                <s:elseif test="data.param_key == 'BACKUP_CHANNEL_NUM'">
                	<div class="controls">
	                	<input type="text" name="param_value" value="${data.param_value}" class="backupChannelValidator" maxlength="20"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
                <s:elseif test="data.param_key == 'CHANNEL_RES_FAIL' or data.param_key == 'CHANNEL_LINK_FAIL'">
                	<div class="controls">
	                	<input type="text" name="param_value" value="${data.param_value}" class="channelResAndLinkValidator"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
                <!-- 查余额最小时间间隔 -->
                <s:elseif test="data.param_key == 'GETBALANCE_INTERVAL'">
                	<div class="controls">
	                	<input type="text" name="param_value" value="${data.param_value}" class="getBalanceValidator"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
                <!-- 营销短信的发送时间区间 -->
                <s:elseif test="data.param_key == 'AUDIT_SM_TIMEAREA'">
                	<c:set var="audit_start_time" value="${fn:substringBefore(data.param_value,'|')}" />
                	<c:set var="audit_end_time" value="${fn:substringAfter(data.param_value,'|')}" />
                	<div class="controls">
	                	<u:date id="audit_start_time" value="${audit_start_time}" placeholder="开始时间" maxId="audit_end_time,-1" dateFmt="HH:mm" startDate="00:00"/>
						<span>至</span>
				       	<u:date id="audit_end_time" value="${audit_end_time}" placeholder="结束时间" minId="audit_start_time,1" dateFmt="HH:mm" />
	                	<input id="AUDIT_SM_TIMEAREA" name="param_value" style="display:none;" value=""/>
<!-- 	                	<span for="param_value"></span> -->
	                </div>
	                <div class="controls" style="display: none" id="div_audit_time_valid">
	                	<span style="color: red;  hidden="hidden" id="audit_time_valid"></span>
	                </div>
                </s:elseif>
                <!-- 异常订单超时时间 -->
                <s:elseif test="data.param_key == 'REBACK_TIME_OVER'">
                	<div class="controls">
	                	<input type="text" name="param_value" value="${data.param_value}" class="rebackTimeOutValidator"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
                <!-- 直连通道连续登录失败N次告警 -->
                <s:elseif test="data.param_key == 'CONTINUE_LOGIN_FAILED_NUM'">
                	<div class="controls">
	                	<input type="text" name="param_value" value="${data.param_value}" class="channelLoginNumValidator"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
                <s:elseif test="data.param_key == 'PARAM_REPORT_GET_NUMBER'">
                	<div class="controls">
	                	<input type="text" name="param_value" value="${data.param_value}" class="reportGetNumValidator"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
                
                <s:elseif test="data.param_key == 'QUERYREPORT_INTERVAL'">
                	<div class="controls">
	                	<input type="text" name="param_value" value="${data.param_value}" class="queryReportValidator"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
                
                <s:elseif test="data.param_key == 'CHANNEL_REPORT_FAIL'">
					<s:set name="param_value" value='data.param_value.split("\\\\;")'/>
                	<div class="controls">
	                	所需条数：<input type="text" name="needNum" value="<s:property value="#param_value[0]" />" class="channelReportValidator" /><span for="needNum"></span>
	                	时间区间：<input type="text" name="tiemRange" value="<s:property value="#param_value[1]" />"  class="channelReportValidator1" />
						<input id="CHANNEL_REPORT_FAIL" name="param_value" style="display:none;" value=""/>

						<span for="tiemRange"></span>
	                </div>
                </s:elseif>

			    <s:elseif test="data.param_key == 'KEYWORD_REGULAR'">
                	<div class="controls">
	                	<input type="text" name="param_value" value="${data.param_value}" class="keywordRegularValidator"/>
	                	<span for="param_value"></span>
	                </div>
                </s:elseif>
				  <s:elseif test="data.param_key == 'DEFAULT_ERROR_CODE'">
					  <s:set name="param_value" value='data.param_value.split("\\\\|")'/>
					  <div class="controls">
						  cmpp: <input type="text" name="cmpp" value="<s:property value="#param_value[0]" />" maxlength="20"/>
						  smgp: <input type="text" name="smgp" value="<s:property value="#param_value[1]"/>"  maxlength="20"/>
						  sgip:<input type="text" name="sgip" value="<s:property value="#param_value[2]" />"  maxlength="20"/>
						  smpp:<input type="text" name="smpp" value="<s:property value="#param_value[3]" />"  maxlength="20"/>
						  https:<input type="text" name="https" value="<s:property value="#param_value[4]"/>" maxlength="20" />
						  <input id="DEFAULT_ERROR_CODE" name="param_value" style="display:none;" value=""/>
					  </div>
					  <div class="controls" style="display: none" id="divOV1">
						  <span style="color: red; display: none" id="overRate_validate1">SGIP不允许为0；SMGP和SMPP不予许为0/00/000；CMPP和https不允许出现deliver</span>
					  </div>
				  </s:elseif>
				  <s:elseif test="data.param_key == 'CHANNELGTOUP_WEIGHT_RATIO'">
					  <s:set name="param_value" value='data.param_value.split("\\\\|")'/>
					  <div class="controls">
						  通道权重: <input type="text" id="cweight" name="cweight" value="<s:property value="#param_value[0]" />" maxlength="20"/>
						  通道成功率权重: <input type="text" id="sweight" name="sweight" value="<s:property value="#param_value[1]"/>"  maxlength="20"/>

						  <input id="CHANNELGTOUP_WEIGHT_RATIO" name="param_value" style="display:none;" value=""/>
					  </div>
					  <div class="controls" style="display: none" id="divOVX">
						  <span style="color: red; display: none" id="chan_validate">小数点后2位，取值范围[0，100]， 两值和为100</span>
					  </div>
				  </s:elseif><s:elseif test="data.param_key == 'CHANNEL_SUCCESS_RATE_UPDATE'">

				  <div class="controls">
					  <input type="text" id="updateRate" name="updateRate" value="${data.param_value}" maxlength="20"/>


					  <input id="CHANNEL_SUCCESS_RATE_UPDATE" name="param_value" style="display:none;" value=""/>
				  </div>
				  <div class="controls" style="display: none" id="divOVU">
					  <span style="color: red; display: none" id="update_validate">通道成功率的统计频率，单位：分钟，每次统计前N分钟的数据，N＝统计频率</span>
				  </div>
			  </s:elseif>
                <s:else>
	                <div class="controls">
	                  <input type="text" id="other" name="param_value" value="${data.param_value}"  maxlength="100"/>
	                  <span for="param_value"></span>
	                </div>
                </s:else>
              </div>
              <div class="control-group">
                <label class="control-label">描述</label>
                <div class="controls">
                  <input type="text"  name="description" value="${data.description}"  maxlength="150" style="width: 490px"/>
                </div>
              </div>
			  <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
	              <input type="hidden" name="param_id" value="${data.param_id}" />
	              <input type="button" value="${data.param_id==null?'添 加':'修 改'}" class="btn btn-success" onclick="save(this)"/>
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
var overRate_validate = true; //超频参数校验
var cw_validate = true; //通道成功率预警
var error_validate=true; //默认状态报告校验
var pkey="${data.param_key}";
var pvalue="${data.param_value}";
$(function(){
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		errorPlacement: function(error, element) {
			$(element).closest( ".controls" ).find( "span[for='" + element.attr( "name" ) + "']" ).append( error );
		},
		errorElement: "span",
		rules: {
			key: "required",
			value: "required",
			param_value: { 
//				required : true,
                requiredValidator : true
			}
		},
		messages: {
			key: "请输入参数键",
			value: "请输入参数值"
		}
	});

	//短信超频配置
    var pv1  = $("input[name='param_value']");
    if(pv1.attr("id") == "OVERRATE_CONFIG1") {
        // 根据超频开关值，设置checkbox是否选中
        var osw = $("input[name='overRate_switch1']");
        var orval = osw.val();
        if(orval == 1) {
            osw.attr("checked", true);
        }else if(orval == 0) {
            osw.attr("checked", false);
        }

    }

	// 关键字超频配置

	if(pkey== 'KEYWORD_OVERRATE'){

        var va11=pvalue.split(";");

            console.log(va11[4]);
            $("#overrate_sign ").val(va11[4]);

	}

	var pv  = $("input[name='param_value']");
	if(pv.attr("id") == "OVERRATE_CONFIG") {
		// 根据超频开关值，设置checkbox是否选中
		var osw = $("input[name='overRate_switch']");
		var orval = osw.val();
		if(orval == 1) {
			osw.attr("checked", true);
		}else if(orval == 0) {
			osw.attr("checked", false);
		}
    }
//	}else  if(pv.attr("id") == "CHANNELGTOUP_WEIGHT_RATIO"){
//        $("input[name='cweight']").rules("add",{required:true,number:true,minNumber:true,range:[0,100]});
//        $("input[name='sweight']").rules("add",{required:true,number:true,minNumber:true,range:[0,100]});
//	}

//自定义validate验证输入的数字小数点位数不能大于两位
    jQuery.validator.addMethod("minNumber",function(value, element){
        var returnVal = true;
        inputZ=value;
        var ArrMen= inputZ.split(".");    //截取字符串
        if(ArrMen.length==2){
            if(ArrMen[1].length>2){    //判断小数点后面的字符串长度
                returnVal = false;
                return false;
            }
        }
        return returnVal;
    },"小数点后最多为两位位");

	jQuery.validator.addMethod("requiredValidator", function(value, element) {
		if( value.trim() == '' ){
		    if(element.getAttribute("class")=='keywordRegularValidator'){
		        return true;
			}
			return false;
		}
		return true;
	}, "必须填写");

	// 日志级别参数值校验
	jQuery.validator.addMethod("loglevelValidator", function(value, element) {
		if( value.trim() == '' ){
			return false;
		}
	    var reg = /^[0-8]{1}$/;
	    return this.optional(element) || (reg.test(value));
	}, "请输入0到8中的一个整数");
	
	// 备份通道数校验
	jQuery.validator.addMethod("backupChannelValidator", function(value, element) {
		if( value.trim() == '' ){
			return false;
		}
	    var reg = /^[1-5]{1}$/;
	    return this.optional(element) || (reg.test(value));
	}, "请输入1到5中的一个整数");
	
	jQuery.validator.addMethod("channelResAndLinkValidator", function(value, element) {
		if( value.trim() == '' ){
			return false;
		}
	    if( Number(value) < 1 || Number(value) > 500 ){
	    	return false;
	    }
	    return true;
	}, "请输入1到500中的一个整数");
	
	// 用户查费接口调用频率时间范围校验
	jQuery.validator.addMethod("getBalanceValidator", function(value, element) {
		if( value.trim() == '' ){
			return false;
		}
	    if( Number(value) < 1 || Number(value) > 300 ){
	    	return false;
	    }
	    return true;
	}, "请输入1到300中的一个整数");
	
	// 异常订单超时时间校验
	jQuery.validator.addMethod("rebackTimeOutValidator", function(value, element) {
		if( value.trim() == '' ){
			return false;
		}
		var valueList = value.split(";");
		var param1 = valueList[0];
		var param2 = valueList[1];
	    if( !/^\d+$/.test(param1) || !/^\d+$/.test(param2) ){
	    	return false;
	    }
	    if( param1<1 || param1>86400){
	    	return false;
	    }
	    if( param2<1 || param2>10000){
	    	return false;
	    }
	    return true;
	}, "取值范围：秒[1，86400]，次[1，10000]，用“;”分割");
	
	// 异常订单超时时间校验
	jQuery.validator.addMethod("channelLoginNumValidator", function(value, element) {
		if( value.trim() == '' ){
			return false;
		}
	    if( Number(value) < 1 || Number(value) > 500 ){
	    	return false;
	    }
	    return true;
	}, "请输入1到500中的一个整数");

	jQuery.validator.addMethod("reportGetNumValidator", function(value, element) {
        if( value.trim() == '' ){
            return false;
        }
        var valueList = value.split(";");
        var param1 = valueList[0];
        var param2 = valueList[1];
        if( !/^\d+$/.test(param1) || !/^\d+$/.test(param2) ){
            return false;
        }
        if( param1<1 || param1>20000){
            return false;
        }
        if( param2<1 || param2>300){
            return false;
        }
        return true;
    }, "取值范围：重推数量[1，20000]，间隔[1，300]（单位：秒），用英文“;”分割");
	
	jQuery.validator.addMethod("queryReportValidator", function(value, element) {
		if( value.trim() == '' ){
			return false;
		}
		if( !/^\d+$/.test(value)){
	    	return false;
	    }
		if( Number(value) < 1 || Number(value) > 100 ){
	    	return false;
	    }
		return true;
	}, "请输入1到100中的一个整数");
	
	jQuery.validator.addMethod("channelReportValidator", function(value, element) {
		if( value.trim() == '' ){
			return false;
		}
		if( !/^\d+$/.test(value)){
	    	return false;
	    }
		if( Number(value.trim()) < 1 || Number(value.trim()) > 500 ){
	    	return false;
	    }
		return true;
	}, "请输入1到500中的一个整数");
    jQuery.validator.addMethod("channelReportValidator1", function(value, element) {
        if( value.trim() == '' ){
            return false;
        }
        if( !/^\d+$/.test(value)){
            return false;
        }
        if( Number(value.trim()) < 1 || Number(value.trim()) > 1440 ){
            return false;
        }
        return true;
    }, "请输入1到1440中的一个整数");
	jQuery.validator.addMethod("keywordRegularValidator", function(value, element) {
		if( value.trim() == '' ){
			return true;
		}

		return true;
	}, "请输入1到500中的一个整数");
});

function save(btn){
 	$("#msg").hide();
	var pv  = $("input[name='param_value']");
	if(pv.attr("id") == "OVERRATE_CONFIG") {
		// 关键字超频
		var osw = $("input[name='overRate_switch']");
		var oswval = osw.is(':checked') == true ? 1:0;
		var ovesign= $("#overrate_sign ").val();
		var odiv = $("#divOV");
		var ons = $("input[name='overRate_num_s']");
		var ots = $("input[name='overRate_time_s']");
		var onm = $("input[name='overRate_num_m']");
		var otm = $("input[name='overRate_time_m']");
		var onh = $("input[name='overRate_num_h']");
		var oth = $("input[name='overRate_time_h']");
		var orv = $("#overRate_validate");
		
		overRateValiS(ons.val(),ots.val(),onm.val(),otm.val(),onh.val(),oth.val(),orv,odiv);
		
		if(!overRate_validate){
			return;
		}
		
		// 拼接超频配置参数
		var onsval = ons.val() == ""?"0":ons.val();
		var otsval = ots.val() == ""?"0":ots.val();
		var onmval = onm.val() == ""?"0":onm.val();
		var otmval = otm.val() == ""?"0":otm.val();
		var onhval = onh.val() == ""?"0":onh.val();
		var othval = oth.val() == ""?"0":oth.val();
		var overrate = onsval + "/" + otsval + ";" + onmval + "/" + otmval + ";" + onhval + "/" + othval + ";" + oswval+ ";" + ovesign;
		pv.val(overrate);
	}else if(pv.attr("id") == "OVERRATE_CONFIG1"){
        // 短信超频
        var osw1 = $("input[name='overRate_switch1']");
        var oswval1 = osw1.is(':checked') == true ? 1:0;
        var odiv2 = $("#divOV2");
        var ons1 = $("input[name='overRate_num_s1']");
        var ots1 = $("input[name='overRate_time_s1']");
        var onm1 = $("input[name='overRate_num_m1']");
        var otm1 = $("input[name='overRate_time_m1']");
        var onh1 = $("input[name='overRate_num_h1']");
        var oth1 = $("input[name='overRate_time_h1']");
        var orv2 = $("#overRate_validate2");

        overRateValiS(ons1.val(),ots1.val(),onm1.val(),otm1.val(),onh1.val(),oth1.val(),orv2,odiv2);

        if(!overRate_validate){
            return;
        }

        // 拼接超频配置参数
        var onsval1 = ons1.val() == ""?"0":ons1.val();
        var otsval1 = ots1.val() == ""?"0":ots1.val();
        var onmval1 = onm1.val() == ""?"0":onm1.val();
        var otmval1 = otm1.val() == ""?"0":otm1.val();
        var onhval1 = onh1.val() == ""?"0":onh1.val();
        var othval1 = oth1.val() == ""?"0":oth1.val();
        var overrate1 = onsval1 + "/" + otsval1 + ";" + onmval1 + "/" + otmval1 + ";" + onhval1 + "/" + othval1 + ";" + oswval1;
        pv.val(overrate1);

	}else if(pv.attr("id") == "CHANNEL_REPORT_FAIL"){

        var needNum = $("input[name='needNum']");
        var tiemRange = $("input[name='tiemRange']");
        var value=needNum.val().trim()+";"+tiemRange.val().trim();


       $("input[name='param_value']").rules("remove");
        if(!validate.form()){
            return;
        }
        pv.val(value);

	}else if(pv.attr("id") == "DEFAULT_ERROR_CODE"){
        error_validate=true;

        var cmpp = $("input[name='cmpp']");
        var smgp = $("input[name='smgp']");
        var sgip = $("input[name='sgip']");
        var smpp = $("input[name='smpp']");
        var https = $("input[name='https']");
        var orv1 = $("#overRate_validate1");
        var odiv1 = $("#divOV1");
        orv1.text("");

        errorCodeValiS(cmpp.val(),smgp.val(),sgip.val(),smpp.val(),https.val(),orv1,odiv1);
        $("#msg").text("");
        if(!error_validate){
            return;
        }


	    var dsum=cmpp.val()+"|"+smgp.val()+"|"+sgip.val()+"|"+smpp.val()+"|"+https.val();

        pv.val(dsum);
	}else if(pv.attr("id") == "CHANNELWARNING"){
		var cdiv = $("#divCW");
		var cvalid = $("#channelwarnvalid");
		var csm  = $("input[name='channelwarning_n']");
		var cwn  = $("input[name='channelwarning_h']");
		
		channelwarning(csm.val(),cwn.val(),cdiv,cvalid);
		if(!cw_validate) {
			return;
		}
		
		// 拼接通道预警配置参数
	    var cwvalue = csm.val() + ";" + cwn.val();
	    pv.val(cwvalue);
	}else if(pv.attr("id") == "AUDIT"){
// 		var audit_num = $("input[name='audit_num']").val();
		var audit_lock_time = $("input[name='audit_lock_time']").val();
		var audit_result_time = $("input[name='audit_result_time']").val();
		var div_audit_valid = $("#div_audit_valid");
		var audit_valid = $("#audit_valid");
		
//			校验范围： 获取条数[1，100]， 锁定时间[1，15]，有效期[30，10080]
//			初始值：30，   15， 1440
		if(audit_lock_time == "" || audit_result_time == ""){
			audit_valid.text("参数不能为空");
			div_audit_valid.css("display","block");
			return;
		}else if(!/^\d+$/.test(audit_lock_time) || !/^\d+$/.test(audit_result_time)) {
			audit_valid.text("设置参数需填写非负整数");
			div_audit_valid.css("display","block");
			return;
		}else if(audit_lock_time<1 || audit_lock_time>15){
			audit_valid.text("设置审核内容锁定时间范围大于等于1分钟，小于15分钟");
			div_audit_valid.css("display","block");
			return;
		}else if(audit_result_time<30 || audit_result_time>10080){
			audit_valid.text("设置审核结果有效期范围大于等于30分钟，小于10080分钟");
			div_audit_valid.css("display","block");
			return;
		}else{
			audit_valid.text("");
			div_audit_valid.css("display","none");
		}
		var value = audit_lock_time + ";" + audit_result_time;
		pv.val(value);
		
	}else if(pv.attr("id") == "AUDIT_SM_TIMEAREA"){
		var audit_start_time = $("#audit_start_time").val();
		var audit_end_time = $("#audit_end_time").val();
		var div_audit_time_valid = $("#div_audit_time_valid");
		var audit_time_valid = $("#audit_time_valid");
		if(audit_start_time == "" || audit_end_time == ""){
			audit_time_valid.text("参数不能为空");
			div_audit_time_valid.css("display","block");
			return;
		}else{
			audit_time_valid.text("");
			div_audit_time_valid.css("display","none");
		}
		var value = audit_start_time + "|" + audit_end_time;
		pv.val(value);
	}else if(pv.attr("id") == "CHANNELGTOUP_WEIGHT_RATIO"){

        var cdiv=$("#divOVX");
        var crv=$("#chan_validate");
        var cweight=$("#cweight").val();
        var sweight=$("#sweight").val();
        var ival = parseInt(cweight);

        if(isNaN(ival)){
            crv.text("通道权重必须为数字");
            cdiv.css("display","block");
            crv.css("display","block");
            return;
        }else{
            crv.text("");
            cdiv.css("display","none");
        }
        ival = parseInt(sweight);
        if(isNaN(ival)){
            crv.text("通道成功率权重必须为数字");
            cdiv.css("display","block");
            crv.css("display","block");
            return;
        }else{
            crv.text("");
            cdiv.css("display","none");
        }
        var ArrMen=cweight.split(".");
        if(ArrMen.length==2){
            if(ArrMen[1].length>2){    //判断小数点后面的字符串长度
                crv.text("通道权重不超过两位小数");
                cdiv.css("display","block");
                crv.css("display","block");
                return ;
            }else{
                crv.text("");
                cdiv.css("display","none");
            }
        }else{
            crv.text("");
            cdiv.css("display","none");
        }
        ArrMen=sweight.split(".");
        if(ArrMen.length==2){
            if(ArrMen[1].length>2){    //判断小数点后面的字符串长度
                crv.text("通道成功率权重不超过两位小数");
                cdiv.css("display","block");
                crv.css("display","block");
                return ;
            }else{
                crv.text("");
                cdiv.css("display","none");
            }
        }else{
            crv.text("");
            cdiv.css("display","none");
        }
        if(Number(cweight)+Number(sweight)!=100){
            crv.text("权重总和必须等于100");
            cdiv.css("display","block");
            crv.css("display","block");
            return;
		}else{
            crv.text("");
            cdiv.css("display","none");
        }
        var value=cweight+ "|" + sweight;
        pv.val(value);

	}else if(pv.attr("id") == "CHANNEL_SUCCESS_RATE_UPDATE"){

        var cdiv=$("#divOVU");
        var crv=$("#update_validate");
        var up=$("#updateRate").val();
		console.log("进入更新频率判断")
		if(Number(up)<5){
            layer.msg("更新频率设置过小,请谨慎设置！", {icon:5});
		}

        if(!/^\d+$/.test(up)){
            crv.text("通道成功率的更新频率为整数");
            cdiv.css("display","block");
            crv.css("display","block");
            return;
        }else{
            crv.text("");
            cdiv.css("display","none");
        }
        var value=up;
        pv.val(value);

    }else{
// 		$("#msg").hide();
		if(!validate.form()){
			return;
		}
		
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
			
			// $("#msg").text(data.msg).show();
            layer.msg(data.msg, {icon: 1,time: 1500},function(){
                back();
            });

		},
		url : "${ctx}/paramConf/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};

// 通道成功率预警校验
function channelwarning(smsnum, warnrate, cdiv, warnvalid) {
	//非空判断
	if(smsnum == "" || warnrate == ""){
		warnvalid.text("参数不能为空");
		cdiv.css("display","block");
		warnvalid.css("display","block");
		cw_validate = false;
	}else {
		var smsnumval = Number(smsnum);
		var warnrateval = Number(warnrate);

		if(!/^\d+$/.test(smsnumval) || !/^\d+$/.test(warnrateval)) {
			warnvalid.text("设置参数需填写非负整数");
			cdiv.css("display","block");
			warnvalid.css("display","block");
			cw_validate = false;
		}else if(smsnumval < 5){
			warnvalid.text("统计短信条数取值大于等于5条");
			cdiv.css("display","block");
			warnvalid.css("display","block");
			cw_validate = false;
		}else if(smsnumval > 100000000){
			warnvalid.text("统计短信条数取值小于100000000条");
			cdiv.css("display","block");
			warnvalid.css("display","block");
			cw_validate = false;
		}else if(warnrateval < 30){
			warnvalid.text("发送告警频率取值大于等于30分钟");
			cdiv.css("display","block");
			warnvalid.css("display","block");
			cw_validate = false;
		}else if(warnrateval > 300){
			warnvalid.text("发送告警频率取值小于等于300分钟");
			cdiv.css("display","block");
			warnvalid.css("display","block");
			cw_validate = false;
		}else{
			warnvalid.text("");
			cdiv.css("display","none");
			warnvalid.css("display","none");
			cw_validate = true;
		}
	}
}

// 超频参数校验
function overRateValiS(ons,ots,onm,otm,onh,oth,valid,odiv){
	
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
}


//默认状态返回报告校验
function errorCodeValiS(cmpp,smgp,sgip,smpp,https,valid,odiv){
		if(cmpp.toLowerCase() =="delivrd"){
            valid.text("cmpp设置非法");
            odiv.css('display','block');
            valid.css('display','block');
            error_validate = false;
		}
    if(https.toLowerCase() =="delivrd"){
        valid.text("https设置非法");
        odiv.css('display','block');
        valid.css('display','block');
        error_validate = false;
    }
    if(smgp=='0'||smgp=='00'||smgp=='000'){
        valid.text("smgp设置非法");
        odiv.css('display','block');
        valid.css('display','block');
        error_validate = false;
    }
    if(smpp=='0'||smpp=='00'||smpp=='000'){
        valid.text("smpp设置非法");
        odiv.css('display','block');
        valid.css('display','block');
        error_validate = false;
    }
    if(sgip=='0'){
        valid.text("sgip设置非法");
        odiv.css('display','block');
        valid.css('display','block');
        error_validate = false;
    }


//    if(ons == "" || ots == "" || onm == "" || otm == "" || onh == "" || oth == "") {
//        valid.text("参数不能为空");
//        odiv.css('display','block');
//        valid.css('display','block');
//        overRate_validate = false;
//    }else {
//        var onsValue = Number(ons);
//        var otsValue = Number(ots);
//        var onmValue = Number(onm);
//        var otmValue = Number(otm);
//        var onhValue = Number(onh);
//        var othValue = Number(oth);
//        if(!/^\d+$/.test(onsValue) || !/^\d+$/.test(otsValue) || !/^\d+$/.test(onmValue) || !/^\d+$/.test(otmValue)
//            || !/^\d+$/.test(onhValue) || !/^\d+$/.test(othValue)){
//            valid.text("设置参数需填写非负整数");
//            odiv.css('display','block');
//            valid.css('display','block');
//            overRate_validate = false;
//        }else if(onsValue > 100 || onmValue > 100){
//            valid.text("秒（分钟）频率次参数不能大于100");
//            odiv.css('display','block');
//            valid.css('display','block');
//            overRate_validate = false;
//        }else if(onhValue > 1000){
//            valid.text("小时频率次参数不能大于1000");
//            odiv.css('display','block');
//            valid.css('display','block');
//            overRate_validate = false;
//        }else if(otsValue > 120 || otmValue > 120){
//            valid.text("秒(分钟)频率时间参数不能大于120秒(分钟)");
//            odiv.css('display','block');
//            valid.css('display','block');
//            overRate_validate = false;
//        }else if(othValue > 24){
//            valid.text("小时频率时间参数不能大于24小时");
//            odiv.css('display','block');
//            valid.css('display','block');
//            overRate_validate = false;
//        }
//        else if(onmValue < onsValue || onhValue < onmValue){
//            valid.text("分钟频率次数必须大于等于秒频率次数，小时频率次数必须大于等于分钟频率次数");
//            odiv.css('display','block');
//            valid.css('display','block');
//            overRate_validate = false;
//        }else{
//            valid.text("");
//            odiv.css('display','none');
//            valid.css('display','none');
//            overRate_validate = true;
//        }
//    }
}

</script>
</body>
</html>