<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
	<title>${data.id==null?'添加':'编辑'}通道属性实时权重</title>
</head>

<body menuId="332">
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
                <label class="control-label">通道号</label>
                <div class="controls">
	                	<input type="text" name="channelid" value="${data.channelid}" readonly="readonly"/>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">状态报告成功率</label>
                <div class="controls">
                    <input type="hidden" name="oldsuccessRate" value="${data.successRate}" maxlength="60" />
                  <input type="text" name="successRate" value="${data.successRate}" maxlength="60" />
                </div>
              </div>
                <div class="control-group">
                    <label class="control-label">单价</label>
                    <div class="controls">
                        <input type="text" name="unitPrice" value="${data.unitPrice}" maxlength="60" readonly="readonly"/>元
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">抗投诉率</label>
                    <div class="controls">
                        <input type="text" name="antiComplaint" value="${data.antiComplaint}" maxlength="60" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">扩展标志</label>
                    <div class="controls">

                            <input name="exFlag" class="kq" type="checkbox" value="1" />客情&nbsp;&nbsp;
                            <input name="exFlag" class="dx" type="checkbox" value="2" />低销
                            <label id="exvalue-error" class="error" for="exFlag" style="display: none;"></label>

                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">低销额度</label>
                    <div class="controls">
                        <input type="text" name="lowConsumeLimit" value="${data.lowConsumeLimit}" maxlength="15" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">验证码成功率权重</label>
                    <div class="controls">
                        <input type="text" name="yzSuccessWeight" value="${data.yzSuccessWeight}" maxlength="60" readonly="readonly"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">通知成功率权重</label>
                    <div class="controls">
                        <input type="text" name="tzSuccessWeight" value="${data.tzSuccessWeight}" maxlength="60" readonly="readonly"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">营销成功率权重</label>
                    <div class="controls">
                        <input type="text" name="yxSuccessWeight" value="${data.yxSuccessWeight}" maxlength="60" readonly="readonly" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">告警成功率权重</label>
                    <div class="controls">
                        <input type="text" name="gjSuccessWeight" value="${data.gjSuccessWeight}" maxlength="60" readonly="readonly"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">移动对应通道价格权重</label>
                    <div class="controls">
                        <input type="text" name="ydPriceWeight" value="${data.ydPriceWeight}" maxlength="60" readonly="readonly"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">联通对应通道价格权重</label>
                    <div class="controls">
                        <input type="text" name="ltPriceWeight" value="${data.ltPriceWeight}" maxlength="60" readonly="readonly"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">电信对应通道价格权重</label>
                    <div class="controls">
                        <input type="text" name="dxPriceWeight" value="${data.dxPriceWeight}" maxlength="60" readonly="readonly" />
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
              	<input type="hidden" name="id" value="${data.id}">

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
$(function(){
    var exFlag = '${data.exFlag}'; //用el表达式获取在控制层存放的复选框的值为字符串类型
    if(exFlag== 1){
        $(".kq").prop('checked',true);
    }else if(exFlag== 2){
        console.log(2);
        $('.dx').prop('checked',true);
    }else if(exFlag== 3){
        console.log(3);
        $(".kq").prop('checked',true);
        $('.dx').prop('checked',true);
    }


    $.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
            successRate:{
				number:true,
                minNumber:true,
                range:[0,100]
			},
            unitPrice:{
                number:true,
                min4Number:true,
                range:[0,1]
            },
            antiComplaint:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            lowConsumeLimit:{
                maxlength:15,
                digtNumber:true,
                min4Number:true,
                number:true
            },
            yzSuccessWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            tzSuccessWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            yxSuccessWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            gjSuccessWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            ydPriceWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            ltPriceWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
            },
            remark:{
                maxlength:100
            },
            dxPriceWeight:{
                number:true,
                minNumber:true,
                range:[0,100]
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
    },"小数点后最多为两位");

    //自定义validate验证输入的数字小数点位数不能大于两位
    jQuery.validator.addMethod("digtNumber",function(value, element){
        var returnVal = true;
        inputZ=value;
        var ArrMen= inputZ.split(".");    //截取字符串
        if(ArrMen.length==2){
            if(ArrMen[0].length>9){    //判断小数点后面的字符串长度
                returnVal = false;
                return false;
            }
        }
        return returnVal;
    },"整数位数不能多于9位");
    //自定义validate验证输入的数字小数点位数不能大于四位
    jQuery.validator.addMethod("min4Number",function(value, element){
        var returnVal = true;
        inputZ=value;
        var ArrMen= inputZ.split(".");    //截取字符串
        if(ArrMen.length==2){
            if(ArrMen[1].length>4){    //判断小数点后面的字符串长度
                returnVal = false;
                return false;
            }
        }
        return returnVal;
    },"小数点后最多为四位");



});


function saveConfirm(btn){

    $("#msg").hide();
	if(!validate.form()){
		return;
	}
		ajaxSubmit(btn);
};

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
		url : "${ctx}/channelRealTimeWeight/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}
</script>
</body>
</html>