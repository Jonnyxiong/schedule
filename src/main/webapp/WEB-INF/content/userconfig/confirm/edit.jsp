<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}模板类型</title>
	<style type="text/css">
		.select2-selection{
			width: 600px;
		}
		.radioControls{
			padding-top: 15px;
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
<body menuId="46">

<s:if test="data.distoperators==1">
          <s:set name="temp_channelid_dis" value="data.channelid"/>
          <s:set name="temp_policy_dis" value="data.policy"/>
          <s:set name="temp_channelid" value=""/>
          <s:set name="temp_policy" value=""/>
    </s:if>
    <s:if test="data.distoperators==0">
          <s:set name="temp_channelid_dis" value=""/>
          <s:set name="temp_policy_dis" value=""/>
          <s:set name="temp_channelid" value="data.channelid"/>
          <s:set name="temp_policy" value="data.policy"/> 
    </s:if>
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
              <label class="control-label">模板类型</label>
                <div class="controls">
                  <u:select id="templatetype" data="[{value:'0',text:'普通短信'},
                  {value:'4',text:'验证码模板'},
                  {value:'1',text:'云验证模板'}]" value="${data.templatetype
                  }" showAll="false" />
                </div>
              </div>
              
              
              
              <div class="control-group">
                <label class="control-label">是否区分运营商</label>
                <div class="radioControls">
                   <label class="checkbox-inline">
				      <input type="radio" name="distoperators" value="1">是
				   </label>
				   <label class="checkbox-inline">
				      <input type="radio" name="distoperators" value="0">否
				   </label>
				   <input type="hidden" id="h_dist" value="${data.distoperators}" >
                </div>
              </div>
             
              
              <!-- 区分运营商 -->
               <div class="type_list_box">
              
               		<div class="control-group">
	                    <label class="control-label">全网策略</label>
	                    <div class="controls">
	                    	<u:select id="policy_dis" dictionaryType="user_gw_policy" value='${temp_policy_dis }' excludeValue="" onChange="dispolicyChangeTriger"/>
	                    </div>
	                </div>
	               	<div class="control-group">
	               		<label class="control-label">全网通道</label>
	               		<input id="temp_channelid_dis_multiple0" type="hidden"  value="${data.channelid}" />
	               		<input id="temp_channelid_dis_multiple" name="temp_channelid_dis_multiple" type="hidden"  value="${data.channelid}" />
	               		<div class="controls channel_dis">
	                   		<u:select id="channelid_dis" sqlId="findAllChannel" placeholder="通道号" excludeValue="" value='${data.channelid}'></u:select>
	                   		<u:select id="channelid_dis_multiple" sqlId="channel" excludeValue="" value='${data.channelid}' onChange="saveDisSelect2Value"></u:select>
	                 	</div>
<!-- 	                 	<div class="controls"> -->
<!-- 								<span for="error" class="customError hide controls" style="color:red;"></span> -->
<!-- 						</div> -->
	               	</div>
	               	
					<div class="control-group">
	                    <label class="control-label">移动策略</label>
	                    <div class="controls">
	                     <u:select id="ydpolicy" dictionaryType="user_gw_policy" value="${data.ydpolicy}" excludeValue="" onChange="ydpolicyChangeTriger"/>
	                    </div>
                    </div>
	                <div class="control-group">
	                    <label class="control-label">移动通道</label>
	                    <input id="temp_ydchannelid_multiple0" type="hidden"  value="${data.ydchannelid}" />
	                    <input id="temp_ydchannelid_multiple" name="temp_ydchannelid_multiple" type="hidden"  value="${data.ydchannelid}" />
	                    <div class="controls ydchannel">
	                      <u:select id="ydchannelid" sqlId="findAllChannel" placeholder="通道号" excludeValue="" value="${data.ydchannelid}"></u:select>
	                      <u:select id="ydchannelid_multiple" sqlId="channel" excludeValue="" value='${data.ydchannelid}' onChange="saveYdSelect2Value"></u:select>
	                    </div>
	                </div>
                  	
					<div class="control-group">
                    	<label class="control-label">联通策略</label>
                    	<div class="controls">
                     		<u:select id="ltpolicy" dictionaryType="user_gw_policy" value="${data.ltpolicy}" excludeValue="" onChange="ltpolicyChangeTriger"/>
                    	</div>
                  	</div>
	                <div class="control-group">
		                <label class="control-label">联通通道</label>
		                <input id="temp_ltchannelid_multiple0" type="hidden"  value="${data.ltchannelid}" />
		                <input id="temp_ltchannelid_multiple" name="temp_ltchannelid_multiple" type="hidden"  value="${data.ltchannelid}" />
		                <div class="controls ltchannel">
		                	<u:select id="ltchannelid" sqlId="findAllChannel" placeholder="通道号" excludeValue="" value="${data.ltchannelid}"></u:select>
		                	<u:select id="ltchannelid_multiple" sqlId="channel" excludeValue="" value='${data.ltchannelid}' onChange="saveLtSelect2Value"></u:select>
		                </div>
	                </div>
                  	
				   <div class="control-group">
	                    <label class="control-label">电信策略</label>
	                    <div class="controls">
	                    	<u:select id="dxpolicy" dictionaryType="user_gw_policy" value="${data.dxpolicy}" excludeValue="" onChange="dxpolicyChangeTriger"/>
	                    </div>
                   </div>
	               <div class="control-group">
	                    <label class="control-label">电信通道</label>
	                    <input id="temp_dxchannelid_multiple0"  type="hidden"  value="${data.dxchannelid}"/>
	                    <input id="temp_dxchannelid_multiple" name="temp_dxchannelid_multiple" type="hidden"  value="${data.dxchannelid}"/>
	                    <div class="controls dxchannel">
	                      <u:select id="dxchannelid" sqlId="findAllChannel" placeholder="通道号" excludeValue="" value="${data.dxchannelid}"></u:select>
	                      <u:select id="dxchannelid_multiple" sqlId="channel" excludeValue="" value='${data.dxchannelid}' onChange="saveDxSelect2Value"></u:select>
	                    </div>
	               </div>
                   
				  <div class="control-group">
	                    <label class="control-label">国际策略</label>
	                    <div class="controls">
	                    	<u:select id="gjpolicy" dictionaryType="user_gw_policy" value="${data.gjpolicy}" excludeValue="" onChange="gjpolicyChangeTriger"/>
	                    </div>
	              </div>
                  <div class="control-group">
	                    <label class="control-label">国际通道</label>
	                    <input id="temp_gjchannelid_multiple0" type="hidden"  value="${data.gjchannelid}"/>
	                    <input id="temp_gjchannelid_multiple" name="temp_gjchannelid_multiple" type="hidden"  value="${data.gjchannelid}"/>
	                    <div class="controls gjchannel">
	                    	<u:select id="gjchannelid" sqlId="findAllChannel" placeholder="通道号" excludeValue="" value="${data.gjchannelid}"></u:select>
	                    	<u:select id="gjchannelid_multiple" sqlId="channel" excludeValue="" value='${data.gjchannelid}' onChange="saveGjSelect2Value"></u:select>
	                    </div>
                  </div>
                
              </div>

			  <!-- 不区分运营商 全网通道路由 -->
              <div class="all_type">

              <div class="control-group">
                <label class="control-label">全网策略</label>
                <div class="controls">
                   <u:select id="policy" dictionaryType="user_gw_policy" value='${temp_policy}' excludeValue="" onChange="policyChangeTriger"/>
                </div>
              </div>
              
              <div class="control-group">
                <label class="control-label">全网通道</label>
                <input id="temp_channelid_multiple0" type="hidden"  value="${data.channelid}" />
                <input id="temp_channelid_multiple" name="temp_channelid_multiple" type="hidden"  value="${data.channelid}" />
                <div class="controls channelid">
                  <u:select id="channelid" sqlId="channel" placeholder="通道号" excludeValue="" value='${temp_channelid}'>
                  </u:select>
                  <u:select id="channelid_multiple" sqlId="channel" excludeValue="" value='${temp_channelid}' onChange="saveSelect2Value">
                  </u:select>
                </div>
              </div>
                
              </div>
              
              
               <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                  <input type="text" name="remark" value="${data.remarks}" maxlength="50" />
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
			
			templateid: "required"
		},
		messages: {
			
			templateid:"请输入模板号"
		}
	});
});

//区分运营商
var h_dist=$("#h_dist").val();
if(h_dist==1){
	  
	  $("input[name='distoperators']:eq(0)").attr("checked",'checked'); 
}else{
	  
	  $("input[name='distoperators']:eq(1)").attr("checked",'checked'); 
}

$("input[name='distoperators']").click(function(){
  var val = $(this).val();
  if(val==1){
    $(".type_list_box").show();
    $(".all_type").hide();
  }
  if(val==0){
    $(".type_list_box").hide();
    $(".all_type").show();
  }
})

$("input[name='distoperators']").each(function(){
  
  var cke=$(this).attr("checked");
  
  if(cke){
  	var val = $(this).val();
  	if(val==1){
  	      $(".type_list_box").show();
  	      $(".all_type").hide();
  	    }
  	    if(val==0){
  	      $(".type_list_box").hide();
  	      $(".all_type").show();
  	    }
  	
  }
  
  
})


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
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/confirm/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};

//多通道路由选择select2控件初始化
$(function(){
	var backupChannelNum;// "备份通道数量"在系统参数中配置
	$.ajax({
		type : "post",
		async:false,
		url : "${ctx}/common/getBackUpChannelNum",
		success : function(data) {
			if (data != null && data.param_value != null) {
				backupChannelNum = data.param_value;
			}else{
				console.log("查询备份通道数时出错！")
			}
		}
	});
	// 不区分运营商：全网
	var channelid_multiple = $("#channelid_multiple").select2({
		tags : true,
		multiple : true,
		maximumSelectionLength : backupChannelNum
	});
	disableSelect2MultiSelectSort($("#channelid_multiple"));
	initSelect2Value($(".channelid"), channelid_multiple, $("#temp_channelid_multiple0").val().split(","));
	policyChangeTriger();
	
	// 区分运营商：全网
	var channelid_dis_multiple = $("#channelid_dis_multiple").select2({
		tags : true,
		multiple : true,
		maximumSelectionLength : backupChannelNum
	});
	disableSelect2MultiSelectSort($("#channelid_dis_multiple"));
	initSelect2Value($(".channel_dis"), channelid_dis_multiple, $("#temp_channelid_dis_multiple0").val().split(","));
	dispolicyChangeTriger();
	
	// 区分运营商：移动
	var ydchannelid_multiple = $("#ydchannelid_multiple").select2({
		tags : true,
		multiple : true,
		maximumSelectionLength : backupChannelNum
	});
	disableSelect2MultiSelectSort($("#ydchannelid_multiple"));
	initSelect2Value($(".ydchannel"), ydchannelid_multiple, $("#temp_ydchannelid_multiple0").val().split(","));
	ydpolicyChangeTriger();
	
	// 区分运营商：联通
	var ltchannelid_multiple = $("#ltchannelid_multiple").select2({
		tags : true,
		multiple : true,
		maximumSelectionLength : backupChannelNum
	});
	disableSelect2MultiSelectSort($("#ltchannelid_multiple"));
	initSelect2Value($(".ltchannel"), ltchannelid_multiple, $("#temp_ltchannelid_multiple0").val().split(","));
	ltpolicyChangeTriger();
	
	// 区分运营商：电信
	var dxchannelid_multiple = $("#dxchannelid_multiple").select2({
		tags : true,
		multiple : true,
		maximumSelectionLength : backupChannelNum
	});
	disableSelect2MultiSelectSort($("#dxchannelid_multiple"));
	initSelect2Value($(".dxchannel"), dxchannelid_multiple, $("#temp_dxchannelid_multiple0").val().split(","));
	dxpolicyChangeTriger();
	
	// 区分运营商：国际
	var gjchannelid_multiple = $("#gjchannelid_multiple").select2({
		tags : true,
		multiple : true,
		maximumSelectionLength : backupChannelNum
	});
	disableSelect2MultiSelectSort($("#gjchannelid_multiple"));
	initSelect2Value($(".gjchannel"), gjchannelid_multiple, $("#temp_gjchannelid_multiple0").val().split(","));
	gjpolicyChangeTriger();
	
	$("#temp_channelid_multiple").val($("#temp_channelid_multiple0").val());
	$("#temp_gjchannelid_multiple").val($("#temp_gjchannelid_multiple0").val());
	$("#temp_dxchannelid_multiple").val($("#temp_dxchannelid_multiple0").val());
	$("#temp_ltchannelid_multiple").val($("#temp_ltchannelid_multiple0").val());
	$("#temp_ydchannelid_multiple").val($("#temp_ydchannelid_multiple0").val());
	$("#temp_channelid_dis_multiple").val($("#temp_channelid_dis_multiple0").val());
});

function policyChangeTriger(){
  	var policy = $("#policy").val(); //通道选择策略
  	if( policy ==1 ){//绑定固定通道
		$("#channelid").hide();
		$(".channelid").find(".select2-selection").show();
  	}else{
  		$("#channelid").show();
  		$(".channelid").find(".select2-selection").hide();
  	}
}

function dispolicyChangeTriger(){
  	var policy_dis = $("#policy_dis").val(); //通道选择策略
  	if( policy_dis ==1 ){//绑定固定通道
		$("#channelid_dis").hide();
		$(".channel_dis").find(".select2-selection").show();
  	}else{
  		$("#channelid_dis").show();
  		$(".channel_dis").find(".select2-selection").hide();
  	}
}

function ydpolicyChangeTriger(){
  	var ydpolicy = $("#ydpolicy").val(); //通道选择策略
  	if( ydpolicy ==1 ){//绑定固定通道
		$("#ydchannelid").hide();
		$(".ydchannel").find(".select2-selection").show();
  	}else{
  		$("#ydchannelid").show();
  		$(".ydchannel").find(".select2-selection").hide();
  	}
}

function ltpolicyChangeTriger(){
  	var ltpolicy = $("#ltpolicy").val(); //通道选择策略
  	if( ltpolicy ==1 ){//绑定固定通道
		$("#ltchannelid").hide();
		$(".ltchannel").find(".select2-selection").show();
  	}else{
  		$("#ltchannelid").show();
  		$(".ltchannel").find(".select2-selection").hide();
  	}
}

function dxpolicyChangeTriger(){
  	var dxpolicy = $("#dxpolicy").val(); //通道选择策略
  	if( dxpolicy ==1 ){//绑定固定通道
		$("#dxchannelid").hide();
		$(".dxchannel").find(".select2-selection").show();
  	}else{
  		$("#dxchannelid").show();
  		$(".dxchannel").find(".select2-selection").hide();
  	}
}

function gjpolicyChangeTriger(){
  	var gjpolicy = $("#gjpolicy").val(); //通道选择策略
  	if( gjpolicy ==1 ){//绑定固定通道
		$("#gjchannelid").hide();
		$(".gjchannel").find(".select2-selection").show();
  	}else{
  		$("#gjchannelid").show();
  		$(".gjchannel").find(".select2-selection").hide();
  	}
}

</script>
</body>
</html>