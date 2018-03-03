<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
	<title>${data.id==null?'添加':'编辑'}通道属性区间权重配置</title>
    <script src="${ctx}/js/jquery.range.js"></script>
    <link rel="stylesheet" href="${ctx}/js/jquery.range.css">
    <style>
        input, textarea, .uneditable-input {
            width: 100px;
        }
    </style>
</head>


<body menuId="331">
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
                <label class="control-label">类型</label>
                <div class="controls">
                    <select id="type" name="type" onchange="typetoexValue()">
                        <option value="" >请选择类型</option>
                        <option value="0" ${0==data.type?'selected="selected"':'' }>通道成功率区间</option>
                        <option value="1" ${1==data.type?'selected="selected"':'' }>通道价格区间</option>
                    </select>

                </div>
              </div>
              <div class="control-group">
                <label class="control-label">属性</label>
                <div class="controls">

                    <%--<u:select id="exValue1" value="${data.exValue1}" data="[--%>
													<%--{value:'',text:'请选择类型'}]" />--%>
                    <select name="exValue" id="exValue">
                        <option value="" >请先选择类型</option>
                        <%--<option value="0" ${0==data.exValue?'selected="selected"':'' }>短信类型－验证码</option>--%>
                        <%--<option value="1" ${1==data.exValue?'selected="selected"':'' }>短信类型－通知</option>--%>
                        <%--<option value="2" ${0==data.exValue?'selected="selected"':'' }>短信类型－营销</option>--%>
                        <%--<option value="3" ${1==data.exValue?'selected="selected"':'' }>短信类型－告警</option>--%>
                        <%--<option value="4" ${0==data.exValue?'selected="selected"':'' }>发送号码所属运营商－移动</option>--%>
                        <%--<option value="5" ${1==data.exValue?'selected="selected"':'' }>发送号码所属运营商－联通</option>--%>
                        <%--<option value="6" ${1==data.exValue?'selected="selected"':'' }>发送号码所属运营商－电信</option>--%>
                    </select>
                </div>
              </div>
                <div id="rang_list" class="control-group">
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
                <input type="hidden" id="startLine" name="startLine" value="${data.startLine}">
                <input type="hidden" id="endLine" name="endLine" value="${data.endLine}">
                  <input type="hidden" name="weight" value="${data.weight}" maxlength="30" />
              	<input type="hidden" name="id" value="${data.id}">
                  <input type="hidden" name="otype" value="${data.type}">
                  <input type="hidden" name="oexValue" value="${data.exValue}">
                  <%--<input type="hidden" name="type" value="${data.type}">--%>
                  <input type="hidden" id="oldid" name="oldid" value="">
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
var data0="<option value='' >请选择属性</option><option value='0' >短信类型－验证码</option> <option value='1' >短信类型－通知</option> <option value='2' >短信类型－营销</option> <option value='3' >短信类型－告警</option>";
var data1="<option value='' >请选择属性</option><option value='4'>发送号码所属运营商－移动</option> <option value='5' }>发送号码所属运营商－联通</option> <option value='6' >发送号码所属运营商－电信</option>";
var end=0;
var num=1;
var configid="";
function  typetoexValue(value){
    if(value=='0'){
        console.log("进来成功率");
        $("#exValue").empty();
        $("#exValue").append(data0);
//        $('.sud').show();
//        $('.prd').hide();
      //  $("#exValue1").attr("data",data0);
    }else if(value=='1'){
        console.log("进来价格");
        $("#exValue").empty();
        $("#exValue").append(data1);
//        $('.prd').show();
//        $('.sud').hide();
       // $("#exValue1").attr("data",data1);
    }
}


$('#type').change(function(){
   var type1= $("#type").val();

//    if(type1=='0'){
//        $("input[name='pstartLine']").rules("remove");
//        $("input[name='pendLine']").rules("remove");
//        $("input[name='spstartLine']").rules("add",{required:true,number:true,min2Number:true,range:[0,100]});
//        $("input[name='spendLine']").rules("add",{required:true,number:true,min2Number:true,range:[0,100]});
//
//    }else if(type1=='1'){
//         $("input[name='spstartLine']").rules("remove");
//         $("input[name='spendLine']").rules("remove");
//        $("input[name='pstartLine']").rules("add",{required:true,number:true,min4Number:true,range:[0,1]});
//        $("input[name='pendLine']").rules("add",{required:true,number:true,min4Number:true,range:[0,1]});
//
//    }
    console.log(type1);
    typetoexValue(type1);
});





$(function(){



    var type = '${data.type}';
    var exValue = '${data.exValue}';
    configid='${data.id}';

    typetoexValue(type);

    var all_select=$("#exValue > option");
    console.log(exValue);
    for(var i=0;i<all_select.length;i++){
        var svalue=all_select[i].value;
        if(exValue==svalue){  //取select中所有的option的值与其进行对比，相等则令这个option添加上selected属性
            console.log(svalue);
            $("#exValue option[value='"+svalue+"']").attr("selected","selected");
        }
    }

    if(configid!=""){
        $("#type").attr("disabled",true);
        $("#exValue").attr("disabled",true);
        $.ajax({
            type : "post",
            async : false,
            url : "${ctx}/channelConfig/channelConfigCheck",
            data : {
                type :type ,
                exValue : exValue
            },
            success : function(result) {

                if (result != null && result.datalist != null ) {

                    $("#rang_list").empty();
                    var html="";

                    var leng=(result.datalist).length;
                    console.log("数组长度"+leng);
                    var ids="";
                    $.each(result.datalist,function(n,value) {
                      //  console.log(n+' '+value.id);

                        if(type=="0"){
                            console.log("已进入编辑带出通道成功率"+n);

                                html +='<div class="control-group" id ="rangconfig_'+num+'"><label class="control-label">'+(n+1)+'&nbsp;&nbsp;区间范围:</label><div class="controls"><input type="text" name="spstartLine" id="spstartLine_'+num+'" value="'+value.start_line+'" maxlength="30" style="width:100px;" />%'
                                    +'&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" id="spendLine_'+num+'" name="spendLine" value="'+value.end_line+'" maxlength="30" style="width:100px;"/>%&nbsp;&nbsp;'
                                    +'权重:<input type="text" name="nowweight" id="weight_'+num+'" value="'+value.weight+'" maxlength="30" />%<br/></div></div>';


                        }else{

                                html +='<div class="control-group" id ="rangconfig_'+num+'"><label class="control-label">'+(n+1)+'&nbsp;&nbsp;区间范围:</label><div class="controls"><input type="text" name="pstartLine" id="pstartLine_'+num+'" value="'+value.start_line+'" maxlength="30" style="width:100px;" />元'
                                    +'&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" id="pendLine_'+num+'" name="pendLine" value="'+value.end_line+'" maxlength="30" style="width:100px;" />元&nbsp;&nbsp;'
                                    +'权重:<input type="text"   name="nowweight" id="weight_'+num+'" value="'+value.weight+'" maxlength="30" />%<br/></div></div>';

                        }
                        if(n==0){
                            ids=value.id;
                        }else {
                            ids+=","+value.id;
                        }
                        end=value.end_line;
                        num++;
                    });
                    $("#oldid").val(ids);
                    console.log("编辑带出ids为"+$("#oldid").val());
                    $("#rang_list").append(html);

                    var ohtml="";

                    ohtml+='<div class="control-group" ><div class="controls"><input type="button" name="del" value="删除" onclick="deleterang()"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="add" value="添加" onclick="addrang()"/></div></div>';
                    $("#rang_list").after(ohtml);


                }
            }
        });


    }



    $('#exValue').change(function(){
        num=1;
        $("#oldid").val("");
        var otype= $("#type").val();
        var oexValue=$("#exValue").val();
        var html="";
        $.ajax({
            type : "post",
            async : false,
            url : "${ctx}/channelConfig/channelConfigCheck",
            data : {
                type :otype ,
                exValue : oexValue
            },
            success : function(result) {
                $("#rang_list").empty();
                if(result.datalist==null || result.datalist==''){
                    if(otype=="0"){
                        html +='<div class="control-group" id ="rangconfig_'+num+'"><label class="control-label">'+num+'&nbsp;&nbsp;区间范围:</label><div class="controls"><input type="text" name="spstartLine" id="spstartLine_'+num+'"  maxlength="30" style="width:100px;" />%'
                            +'&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" id="spendLine_'+num+'" name="spendLine"  maxlength="30" style="width:100px;"/>%&nbsp;&nbsp;'
                            +'权重:<input type="text" name="nowweight" id="weight_'+num+'"  maxlength="30" />%<br/></div></div>';

                    }else{
                        html +='<div class="control-group" id ="rangconfig_'+num+'"><label class="control-label">'+num+'&nbsp;&nbsp;区间范围:</label><div class="controls"><input type="text" name="pstartLine" id="pstartLine_'+num+'"  maxlength="30" style="width:100px;" />元'
                            +'&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" id="pendLine_'+num+'" name="pendLine"  maxlength="30" style="width:100px;" />元&nbsp;&nbsp;'
                            +'权重:<input type="text"   name="nowweight" id="weight_'+num+'"  maxlength="30" />%<br/></div></div>';

                    }
                    $("#rang_list").append(html);

                    var ohtml="";
                    $("#opearte").empty();
                    ohtml+='<div id="opearte" class="control-group" ><div class="controls"><input type="button" name="del" value="删除" onclick="deleterang()"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="add" value="添加" onclick="addrang()"/></div></div>';
                    $("#rang_list").after(ohtml);
                    num++;

                }else if (result != null && result.datalist != null  ) {
                    console.log("已进入判断");
                    console.log("带出 list..."+result.datalist);
                    $("#rang_list").empty();



                    $.each(result.datalist,function(n,value) {
                        console.log(n+' '+value.id);
                        if(otype=="0"){
                            html +='<div class="control-group" id ="rangconfig_'+num+'"><label class="control-label">'+(n+1)+'&nbsp;&nbsp;区间范围:</label><div class="controls"><input type="text" name="spstartLine" id="spstartLine_'+num+'" value="'+value.start_line+'" maxlength="30" style="width:100px;" />%'
                                +'&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" id="spendLine_'+num+'" name="spendLine" value="'+value.end_line+'" maxlength="30" style="width:100px;"/>%&nbsp;&nbsp;'
                                +'权重:<input type="text" name="nowweight" id="weight_'+num+'" value="'+value.weight+'" maxlength="30" />%<br/></div></div>';

//
//                            html +='<label class="control-label">'+(n+1)+'&nbsp;&nbsp;<span>区间范围:</span></label><div class="controls"> <div ><span><input class="range-slider1" type="hidden" value="'+value.start_line+','+value.end_line+'" /></span>'
//                                +'&nbsp;&nbsp;&nbsp;&nbsp;<span>权重:<input type="text" id="weight_'+num+'" value="'+value.weight+'" maxlength="60" readonly="readonly"/></span><br/></div></div>';

                        }else{
                            html +='<div class="control-group" id ="rangconfig_'+num+'"><label class="control-label">'+(n+1)+'&nbsp;&nbsp;区间范围:</label><div class="controls"><input type="text" name="pstartLine" id="pstartLine_'+num+'" value="'+value.start_line+'" maxlength="30" style="width:100px;" />元'
                                +'&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" id="pendLine_'+num+'" name="pendLine" value="'+value.end_line+'" maxlength="30" style="width:100px;" />元&nbsp;&nbsp;'
                                +'权重:<input type="text"   name="nowweight" id="weight_'+num+'" value="'+value.weight+'" maxlength="30" />%<br/></div></div>';

                        }
                        if(n==0){
                            ids=value.id;
                        }else {
                            ids+=","+value.id;
                        }
                        end=value.end_line;
                        num++;
                    });
                    $("#oldid").val(ids);
                    $("#rang_list").append(html);

                    var ohtml="";
                    $("#opearte").remove();
                    ohtml+='<div id="opearte" class="control-group" ><div class="controls"><input type="button" name="del" value="删除" onclick="deleterang()"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="add" value="添加" onclick="addrang()"/></div></div>';
                    $("#rang_list").after(ohtml);
//                    $("#rang1").hide();
//                    $("#rang2").hide();

//                    $("#newOne").empty();
//                    $("#newOne").append(num+"&nbsp;&nbsp;区间范围");
//                    if(otype=="0"){
//                        $("#spstartLine").val(end);
//                       // $('.range-slider').jRange('setValue', end+','+end);
//                    }else {
//                        $("#pstartLine").val(end);
//                    }


//                    $('.range-slider1').jRange({
//                        from: 0,
//                        to: 100,
//                        step:0.01,
//                        scale: [0,25,50,75,100],
//                        format: '%s',
//                         width: 600,
//                        showLabels: true,
//                        disable:true,
//                        isRange : true
//                    });

                }

                $("input[name='otype']").val(otype);
                $("input[name='oexValue']").val(oexValue);
            }
        });
    });





	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
            type:{
				required:true
			},
            spstartLine: {
                number:true,
                min2Number:true,
                range:[0,100]
            },
            spendLine: {
                number:true,
                min2Number:true,
                range:[0,100]
            },
            pstartLine: {
                number:true,
                min4Number:true,
                range:[0,1]
            },
            pendLine: {
                number:true,
                min4Number:true,
                range:[0,1]
            },
            remark:{
                maxlength:100
            }
//            ,
//            weight: {
//                required:true,
//                number:true,
//                min2Number:true,
//                range:[0,100]
//            }
		},
		messages: {
            type: {
				required:"请选择类型"
			},
            pstartLine: {
                number:"起始必须为数字"
            },
            pendLine: {
                number:"结束必须为数字"
            }
//            ,
//            weight: {
//                number:"权重必须为数字"
//
//            }
		}
	});

    //自定义validate验证输入的数字小数点位数不能大于两位
    jQuery.validator.addMethod("min2Number",function(value, element){
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

    if(configid!=""){
        $("#type").rules("remove");
//        $("#pstartLine").rules("remove");
//        $("#pendLine").rules("remove");
        if(type!="0"){
//            $("input[name='spstartLine']").rules("remove");
//            $("input[name='spendLine']").rules("remove");
            $("input[name='pstartLine']").rules("add",{required:true,number:true,min4Number:true,range:[0,1]});
            $("input[name='pendLine']").rules("add",{required:true,number:true,min4Number:true,range:[0,1]});
        }else {
//            $("input[name='pstartLine']").rules("remove");
//            $("input[name='pendLine']").rules("remove");
            $("input[name='spstartLine']").rules("add",{required:true,number:true,min2Number:true,range:[0,100]});
            $("input[name='spendLine']").rules("add",{required:true,number:true,min2Number:true,range:[0,100]});
        }
       $("input[name='nowweight']").rules("add",{required:true,number:true,min2Number:true,range:[0,100]});
    }

});

//删除区间
function deleterang(){
    console.log("删除判断"+num);
    if(num==2){
        layer.msg("已经是最后一个！", {icon:2})
        return;
    }else {
        $("#rangconfig_"+(num-1)).remove();
    }


    num--;
    var atype1=$("#type").val();
    var end1="";
    if(atype1=="0"){
        end1=$("#spendLine_"+(num-1)).val();
       //  end1=$("#range_"+(num-1)).val().split(",");
        end=end1;
    }else {
         end1=$("#pendLine_"+(num-1)).val();
        end=end1;
    }
    if(num==2){
        $('#rangconfig_'+(num-1)+' input').removeAttr("readonly");//去除input元素的readonly属性
      //  $('.range-slider1').jRange('enable');
    }

}
//添加区间
function addrang(){
    var ahtml="";
    var atype=$("#type").val();
    console.log("添加方法时，num为"+num);

    var end1="";
    if(atype=="0"){
        end1=$("#spendLine_"+(num-1)).val();
      //  end1=$("#range_"+(num-1)).val().split(",")[1];
      //  end=end1[1];
    }else {
        end1=$("#pendLine_"+(num-1)).val();
      //  end=end1;
    }


    if(atype=="0"){

        ahtml+='<div class="control-group" id ="rangconfig_'+num+'"><label class="control-label" >'+num+'&nbsp;&nbsp;区间范围:</label><div class="controls"><input type="text" class="spstartLine" id="spstartLine_'+num+'" name="spstartLine" value="'+end1+'" maxlength="30" style="width:100px;" />%'
            +'&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" id="spendLine_'+num+'" class="pendLine" name="spendLine" value="" maxlength="30" style="width:100px;" />%&nbsp;&nbsp;'
            +'权重:<input type="text" name="newweight"  id="weight_'+num+'" value="" maxlength="30" />%<br/></div></div>';
//        ahtml+=' <div class="control-group" id ="rangconfig_'+num+'"><label class="control-label" >'+num+'&nbsp;&nbsp;<span>区间范围:</span></label><div class="controls"> <div ><span><input id="range_'+num+'" class="range-slider2" type="hidden" value="'+end1+',+'+end1+'" /></span>'
//            +'&nbsp;&nbsp;&nbsp;&nbsp;<span>权重:<input type="text"  name="newweight" id="weight_'+num+'" value="" maxlength="60" /></span><br/></div></div></div>';
    }else {
        ahtml+='<div class="control-group" id ="rangconfig_'+num+'"><label class="control-label" >'+num+'&nbsp;&nbsp;区间范围:</label><div class="controls"><input type="text" class="pstartLine" id="pstartLine_'+num+'" name="pstartLine" value="'+end1+'" maxlength="30" style="width:100px;" />元'
            +'&nbsp;&nbsp;-&nbsp;&nbsp;<input type="text" id="pendLine_'+num+'" class="pendLine" name="pendLine" value="" maxlength="30" style="width:100px;" />元&nbsp;&nbsp;'
            +'权重:<input type="text" name="newweight"  id="weight_'+num+'" value="" maxlength="30" />%<br/></div></div>';
    }
    $("#rang_list").append(ahtml);
    //添加校验规则
    if(atype==1){
        $("input[name='pstartLine']").rules("add",{required:true,number:true,min4Number:true,range:[0,1]});
        $("input[name='pendLine']").rules("add",{required:true,number:true,min4Number:true,range:[0,1]});
    }else {
        $("input[name='spstartLine']").rules("add",{required:true,number:true,min2Number:true,range:[0,100]});
        $("input[name='spendLine']").rules("add",{required:true,number:true,min2Number:true,range:[0,100]});
    }

    $("input[name='newweight']").rules("add",{required:true,number:true,range:[0,100]});
  //  addjrange();
    num++;
}

function addjrange(){
    $('.range-slider2').jRange({
        from: 0,
        to: 100,
        step:0.01,
        scale: [0,25,50,75,100],
        format: '%s',
         width: 600,
        showLabels: true,
        isRange : true
    });
}
function saveConfirm(btn){
    console.log("-----------进入保存校验-----------");
    $("#msg").hide();


	if(!validate.form()){
      //  $("#msg").text("校验不通过").show();
		return;
	}
    console.log("-----------校验字段通过-----------");
    var we=0;
    for(var i = 1; i<num;i++){
       console.log("num值:"+num);

        we +=$("#weight_"+i).val()-0;
        console.log("值"+$("#weight_"+i).val());
    }
    console.log("累加值"+we);
 //   var weight=$("input[name='weight']").val();
 //   if(configid!=""){
        //编辑
        if(we>100){
            $("#msg").text("总权重不能超过100").show();
            return;
        }
//        var rang2 = $(".range-slider2").val();
//        console.log("2保存时判断前"+rang2);
        var etype=$("#type").val();
//        console.log("2保存时判断"+etype);
        var pstartSum="";
        var pendSum="";
        var weightSum="";
        if(etype==0){
         //   $("input[name='pstartLine']").rules("remove");
        //    $("input[name='pendLine']").rules("remove");
            var raedit="";
            var raend=0;
//            if(num==1){
//                var rast=$("#spstartLine_"+1).val();
//                var raen=$("#spendLine_"+1).val();
//                if(rast!=0){
//                    $("#msg").text("首个起始区间必须从0开始！").show();
//                    return;
//                }
//                if(Number(rast)==Number(raen)){
//                    $("#msg").text("区间起始与结束不能相等！").show();
//                    return;
//                }else if(Number(rast)>Number(raen)){
//                    $("#msg").text("区间起始不能大于结束").show();
//                    return;
//                }
//
//            }
            for(var i = 1; i<num;i++) {
              //  spstartLine_1
                var rast=$("#spstartLine_"+i).val();
                var raen=$("#spendLine_"+i).val();
                console.log("第"+i+"个:值为"+rast+","+raen);
                if(i==1 &&  rast!=0){
                    $("#msg").text("首个起始区间必须从0开始！").show();
                    return;
                }
                if(rast >100 || raen >100){
                    layer.msg("区间值不能大于100", {icon: 2});
                    return;
                }
                if(Number(rast)==Number(raen)){
                    $("#msg").text("区间起始与结束不能相等！").show();
                    return;
                }else if(Number(rast)>Number(raen)){
                    $("#msg").text("区间起始不能大于结束").show();
                    return;
                }
                if(raend!=rast){
                    $("#msg").text("非持续区间,请检查修改！"+i).show();
                    return;
                }
                var ewe=$("#weight_"+i).val()
                var ArrMen=ewe .split(".");    //截取字符串
                if(ArrMen.length==2){
                    if(ArrMen[1].length>2){    //判断小数点后面的字符串长度
                        $("#msg").text("区间权重小数点不能超过两位,请检查修改！").show();
                        return;
                    }
                }
                if(i==1){
                    pstartSum +=rast;
                    pendSum+=raen;
                    weightSum+=$("#weight_"+i).val();


                }else {
                    pstartSum +=","+rast;
                    pendSum +=","+raen;
                    weightSum+=","+$("#weight_"+i).val();
                }
                raend=raen;

            }


        }else if(etype ==1) {
        //    $("input[name='spstartLine']").rules("remove");
       //     $("input[name='spendLine']").rules("remove");
            var jendline=0;
           // var sline,eline;

//            if(num==1){
//                var rast=$("#pstartLine_"+1).val();
//                var raen=$("#pendLine_"+1).val();
//                if(rast!=0){
//                    $("#msg").text("首个起始区间必须从0开始！").show();
//                    return;
//                }
//                if(Number(rast)==Number(raen)){
//                    $("#msg").text("区间起始与结束不能相等！").show();
//                    return;
//                }else if(Number(rast)>Number(raen)){
//                    $("#msg").text("区间起始不能大于结束").show();
//                    return;
//                }
//
//            }

            for(var i = 1; i<num;i++) {
                var  sline=$("#pstartLine_"+i).val();
                var  eline=$("#pendLine_"+i).val();
                if(i==1 &&  Number(sline)!=0){
                    $("#msg").text("首个起始区间必须从0开始！").show();
                    return;
                }
                if(Number(sline) >1 || Number(eline) >1){
                    layer.msg("区间值不能大于1", {icon: 2});
                    return;
                }
                if(sline==eline){
                    $("#msg").text("区间起始与结束不能相等").show();
                    return;
                }else if(sline>eline){
                    $("#msg").text("区间起始不能大于结束").show();
                    return;
                }
                if(jendline!=sline){
                    $("#msg").text("非持续区间,请检查修改！").show();
                    return;
                }
                var ewe=$("#weight_"+i).val()
                var ArrMen=ewe .split(".");    //截取字符串
                if(ArrMen.length==2){
                    if(ArrMen[1].length>2){    //判断小数点后面的字符串长度
                        $("#msg").text("区间权重小数点不能超过两位,请检查修改！").show();
                        return;
                    }
                }
                if(i==1){
                    pstartSum +=$("#pstartLine_"+i).val();
                    pendSum+=$("#pendLine_"+i).val();
                    weightSum+=$("#weight_"+i).val();
                }else {
                    pstartSum +=","+$("#pstartLine_"+i).val();
                    pendSum +=","+$("#pendLine_"+i).val();
                    weightSum+=","+$("#weight_"+i).val();
                }

                jendline=eline;
            }


        }
        console.log("编辑保存起始...."+pstartSum);
        console.log("编辑保存结束...."+pendSum);
        console.log("编辑保存权重...."+weightSum);
        $("#startLine").val(pstartSum);
        $("#endLine").val(pendSum);
        $("input[name='weight']").val(weightSum);

	ajaxSubmit(btn);

	
};

function ajaxSubmit(btn){
	var options = {
		beforeSubmit : function() {
		    $("#type").attr("disabled", false);
            $("#exValue").attr("disabled", false);
			$(btn).attr("disabled", true);
		},
		success : function(data) {
			$(btn).attr("disabled", false);
            $("#type").attr("disabled", true);
            $("#exValue").attr("disabled", true);


            if(data == null || data.code==500){
				$("#msg").text("服务器错误，请联系管理员").show();
				return;
			}
			if(data.code == 0){
				$("input[name='id']").val(data.id);
			}

//			$("#msg").text(data.msg).show();
//            back();

            layer.msg(data.msg, {icon: 1,time: 1500},function(){
                back();
            });
		},
		url : "${ctx}/channelConfig/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}
</script>
</body>
</html>