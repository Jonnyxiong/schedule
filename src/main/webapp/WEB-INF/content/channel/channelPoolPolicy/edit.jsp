<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
	<title>${data.policyId==null?'添加':'修改'}策略配置</title>
</head>

<body menuId="333">
<!--Action boxes-->
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
                <label class="control-label">策略名称</label>
                <div class="controls">
                	<s:if test="data.policyId != null">
	                	<input type="text" name="policyName" value="${data.policyName}" readonly="readonly"/>
                	</s:if>
                	<s:else>
                		<input type="text" name="policyName" value="${data.policyName}" maxlength="30"/>
                	</s:else>
                  <%--<span class="help-block">* 1-4位数字</span>--%>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">通道成功率权重</label>
                <div class="controls">
                  <input type="text" name="successWeight" value="${data.successWeight}" maxlength="6"/> %
                </div>
              </div>
                <div class="control-group">
                    <label class="control-label">通道价格权重</label>
                    <div class="controls">
                        <input type="text" name="priceWeight" value="${data.priceWeight}" maxlength="6" />%
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">抗投诉率权重</label>
                    <div class="controls">
                        <input type="text" name="antiComplaintWeight" value="${data.antiComplaintWeight}" maxlength="6" />%
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">低销权重</label>
                    <div class="controls">
                        <input type="text" name="lowConsumeWeight" value="${data.lowConsumeWeight}" maxlength="6" />%
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">客情权重</label>
                    <div class="controls">
                        <input type="text" name="customerRelationWeight" value="${data.customerRelationWeight}" maxlength="6" />%
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">是否为默认策略</label>
                    <div class="controls">
                        <u:select id="isDefault" defaultValue="0" data="[{value:'1',text:'是'},
                  {value:'0',text:'否'}]" value="${data.isDefault}" showAll="false" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">备注</label>
                    <div class="controls">
                        <input type="text" name="remark" value="${data.remark}" maxlength="100" />
                    </div>
                </div>


              <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
              	<input type="hidden" name="policyId" value="${data.policyId}">
                  <input type="hidden" id="detype" value="${data.isDefault}">
                <input type="button" onclick="saveConfirm(this);" value="保  存" class="btn btn-success">
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
var weight_validate;   //权重校验
$(function(){
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
            policyName: {
				required:true,
                maxlength:30
			},
            successWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
			},
            priceWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            antiComplaintWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            lowConsumeWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            customerRelationWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            remark:{
                maxlength:100
            }
		},
		messages: {
            policyName: {
				required:"请输入策略名称",
                maxlength:"长度不能超过30"
			},
            successWeight: {
                number:"请输入数字",
                range:"请输入0~100区间值",


			},
            priceWeight: {
                number:"请输入数字",
                range:"请输入0~100区间值",
            },
            antiComplaintWeight: {
                number:"请输入数字",
                range:"请输入0~100区间值",

            },

            lowConsumeWeight: {
                number:"请输入数字",
                range:"请输入0~100区间值",

            },
            customerRelationWeight: {
                number:"请输入数字",
                range:"请输入0~100区间值",

            },
            remark: {
                maxlength:"请输入100字符以内",

            }
		}
	});


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
    },"小数点后最多为两位");         //验证错误信息


});


function saveConfirm(btn){
    weight_validate=true;
    var suw = $("input[name='successWeight']").val();
    var prw = $("input[name='priceWeight']").val();
    var anw = $("input[name='antiComplaintWeight']").val();
    var low = $("input[name='lowConsumeWeight']").val();
    var cuw = $("input[name='customerRelationWeight']").val();
    weightVailis(suw,prw,anw,low,cuw);

	if(!validate.form()){
		return;
	}
    if(!weight_validate){
	    return;
    }

    var detype=$("#detype").val();
    var ndetype=$("#isDefault").val();
    if(detype !=null && detype ==1 && ndetype!=1){
        layer.msg("该条为默认策略池配置,不可修改为非默认！",{icon:2})
        return;
    }
    $("#msg").hide();

    ajaxSubmit(btn);

	
};


function weightVailis(suw,prw,anw,low,cuw){


        var suwValue = Number(suw)==null?0:Number(suw);
        var prwValue = Number(prw)==null?0:Number(prw);
        var anwValue = Number(anw)==null?0:Number(anw);
        var lowValue = Number(low)==null?0:Number(low);
        var cuwValue = Number(cuw)==null?0:Number(cuw);
        var sum=suwValue+prwValue+anwValue+lowValue+cuwValue;
        console.log(sum);
        if(sum!=100){
            console.log("权重判断进行中....")
            $("#msg").text("总权重必须为100%").show();
            weight_validate=false;
        }


}


function ajaxSubmit(btn){
	var options = {
		beforeSubmit : function() {
			$(btn).attr("disabled", true);
		},
		success : function(data) {
			$(btn).attr("disabled", false);
			
			if(data == null || data.code==500){
				$("#msg").text("服务器错误，请联系管理员").show();
				return;
			}
			if(data.code == 0){
				$("input[name='id']").val(data.id);
			}

            layer.msg(data.msg, {icon: 1,time: 1500},function(){
                back();
            });
		},
		url : "${ctx}/channelPoolPolicy/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}
</script>
</body>
</html>