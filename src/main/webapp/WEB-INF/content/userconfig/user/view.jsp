<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>用户及通道组配置查看</title>
	<style type="text/css">
		.inputNum {
			width: 60px;
			height: 20px;
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
<body menuId="15">
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
                	<input type="text" name="userid" value="${data.userid}" class="clientid checkClientId1 checkClientId2" 
                		onblur="clientInputOnblur()" onkeyup="this.value=this.value.toLowerCase()" readonly>
                </div>
              </div>
             <div class="control-group">
              	<label class="control-label">短信类型</label>
              		<div class="controls">
	                    <u:select id="smstype" data="[{value:'0',text:'通知'}, {value:'4',text:'验证码'}, {value:'5',text:'营销'}, {value:'6',text:'告警短信'}, {value:'7',text:'USSD'}, {value:'8',text:'闪信'}]" 
	                    value="${data.smstype}" showAll="false"/>
	                    <input type="hidden" name="smstype" value="${data.smstype}" readonly>
	                </div>
             </div>
             <div class="control-group">
              	<label class="control-label">区分运营商</label>
                <div class="controls">
                  <u:select id="distoperators" data="[{value:'0',text:'否'}, {value:'1',text:'是'}]" value="${data.distoperators}" showAll="false" onChange="operatorsTriger" />
                </div>
             </div>
             
             
             <!-- 不区分运营商 全网通道路由 -->
             <!-- 全网 0、移动1、联通 2、 电信 3、 国际 -->
              
              
             <!-- 区分运营商 -->
              
              
            <div id="channelGroupDiv">
            	<div class="all_type">
		            <div id="quanwang" class="control-group">
						 <label class="control-label">全网通道组：</label>
						<div class="channelid controls">
							<el-select v-model="value1" name="channelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" disabled="true">
							    <el-option
							      v-for="item in options1"
							      :label="item.label"
							      :value="item.value">
							    </el-option>
							</el-select>
						</div>
					</div>
	              
                </div>
            	
            	<div class="type_list_box">
              		<div id="yidong" class="control-group">
					 <label class="control-label">移动通道组：</label>
					<div class="ydchannelid controls">
							<el-select v-model="value2" name="ydchannelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" disabled="true">
							    <el-option
							      v-for="item in options2"
							      :label="item.label"
							      :value="item.value">
							    </el-option>
							</el-select>
						</div>
					</div>
					
					<div id="liantong" class="control-group">
						 <label class="control-label">联通通道组：</label>
						<div class="ltchannelid controls">
							<el-select v-model="value3" name="ltchannelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" disabled="true">
							    <el-option
							      v-for="item in options3"
							      :label="item.label"
							      :value="item.value">
							    </el-option>
							</el-select>
						</div>
					</div>
					
					<div id="dianxin" class="control-group">
						 <label class="control-label">电信通道组：</label>
						<div class="dxchannelid controls">
							<el-select v-model="value4" name="dxchannelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" disabled="true">
							    <el-option
							      v-for="item in options4"
							      :label="item.label"
							      :value="item.value">
							    </el-option>
							</el-select>
						</div>
					</div>
	               
                   
                </div>
            	
				
				<div id="guoji" class="control-group">
					 <label class="control-label">国际通道组：</label>
					<div class="controls">
						<el-select v-model="value5" name="gjchannelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" disabled="true">
						    <el-option
						      v-for="item in options5"
						      :label="item.label"
						      :value="item.value">
						    </el-option>
						</el-select>
					</div>
				</div>
            </div>
            
            <div class="control-group">
                <label class="control-label">免黑名单</label>
                <div class="controls">
                  <u:select id="unblacklist" data="[{value:'0',text:'否'},
                  {value:'1',text:'是'}]" value="${data.unblacklist}" showAll="false" />
                </div>
            </div>
			<div id="noAccess">
            <div class="control-group">
                <label class="control-label">免系统关键字</label>
                <div class="controls">
                  <u:select id="free_keyword" defaultValue="0" data="[{value:'0',text:'否'},
                  {value:'1',text:'是'}]" value="${data.free_keyword}" showAll="false" />
                </div>
            </div>
			<div class="control-group">
				<label class="control-label">免通道关键字</label>
					<div class="controls">
						<u:select id="free_channel_keyword" defaultValue="0" data="[{value:'0',text:'否'},
                  {value:'1',text:'是'}]" value="${data.free_channel_keyword}" showAll="false" />
				</div>
			</div>
			</div>

				<div class="control-group">
             		<label class="control-label">短信超频配置</label>
	                <div class="controls" style="padding-top: 15px">
	                    <s:set name="param_value" value='data.overrateConfig.param_value.split("\\\\;")'/>
	            		<s:set name="overrates" value='#param_value[0].split("\\\\/")'/>
	            		<s:set name="overratem" value='#param_value[1].split("\\\\/")'/>
	            		<s:set name="overrateh" value='#param_value[2].split("\\\\/")'/>
	                    <span>短信超频默认限制：</span><s:property value="#overrates[0]"/>条/<s:property value="#overrates[1]"/>秒;
	                    						 <s:property value="#overratem[0]"/>条/<s:property value="#overratem[1]"/>分;
	                    						 <s:property value="#overrateh[0]"/>条/<s:property value="#overrateh[1]"/>小时&nbsp;&nbsp;&nbsp;&nbsp;状态：
	                    <s:if test="#param_value[3] == 1">开启</s:if>
	                    <s:else>关闭</s:else>
	                </div>
	                <%--<div class="controls" style="padding-top: 15px">--%>
	                    <%--<s:set name="param_value2" value='data.keywordOverrateConfig.param_value.split("\\\\;")'/>--%>
	            		<%--<s:set name="overrates2" value='#param_value2[0].split("\\\\/")'/>--%>
	            		<%--<s:set name="overratem2" value='#param_value2[1].split("\\\\/")'/>--%>
	            		<%--<s:set name="overrateh2" value='#param_value2[2].split("\\\\/")'/>                    --%>
	                    <%--<span>关键字超频默认限制：</span><s:property value="#overrates2[0]"/>条/<s:property value="#overrates2[1]"/>秒;--%>
	                                              <%--<s:property value="#overratem2[0]"/>条/<s:property value="#overratem2[1]"/>分;--%>
	                                              <%--<s:property value="#overrateh2[0]"/>条/<s:property value="#overrateh2[1]"/>小时&nbsp;&nbsp;&nbsp;&nbsp;状态：--%>
	                    <%--<s:if test="#param_value2[3] == 1">开启</s:if>--%>
	                    <%--<s:else>关闭</s:else>--%>
	                <%--</div>--%>
	                 <div id="template_overrate_list">
	                 	<s:iterator value="data.clientOverRateMap" var="item" status="ov">
	              			<div class="template_overrate_group">
								<s:if test="#item.overRate_mode==0">
	                			<div class="controls overRateConfig" id="template_overrate_<s:property value='#ov.index+1'/>">
		              				<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_num_s"/>" maxlength="20" style="width: 60px; height: 20px" readonly/>
		                  			条
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_time_s"/>" maxlength="20" style="width: 60px; height: 20px" readonly/>
		                  			秒
		                  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		              				<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_num_m"/>" maxlength="20" style="width: 60px; height: 20px" readonly/>
		                  			条
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_time_m"/>" maxlength="20" style="width: 60px; height: 20px" readonly/>
		                  			分钟
		                  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_num_h"/>" maxlength="20" style="width: 60px; height: 20px" readonly/>
		                  			条
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_time_h"/>" maxlength="20" style="width: 60px; height: 20px" readonly/>
		                  			小时
		                  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                  			<!-- 每个模板超频配置id(数据库) -->
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.id"/>" style="display:none;"  readonly/>
		                  			<!-- 每个模板超频配置通过标志 -->
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="1" style="display:none;" readonly/>
		                  			<!-- 当前模板超频配置id(页面序号)-->
		                  			<input class="inputNum_1" type="text" name="overRate_id" value="<s:property value='#ov.index+1'/>" style="display:none;" readonly/>
									开关:<select class="inputNum_1" name="overRate_<s:property value='#ov.index+1'/>" style="width: 150px; margin-right: 20px;">
									<s:if test="#item.state==0">
										<option value="0" selected="selected">关</option>
										<option value="1" >开</option>
									</s:if>
									<s:if test="#item.state==1">
										<option value="0" >关</option>
										<option value="1" selected="selected">开</option>
									</s:if>

								</select>
									<select class="inputNum_1" name="overRate_<s:property value='#ov.index+1'/>" style="width: 150px; margin-right: 20px;">
		                  				<s:if test="#item.overRate_mode==0">
		                  					<option value="0" selected="selected">按短信类型统计</option>
		                  					<option value="1">按关键字统计</option>
		                  				</s:if>
		                  				<%--<s:else>
		                  					<%--<option value="0">按短信类型统计</option>--%>
		                  					<%--<option value="1" selected="selected">按关键字统计</option>--%>
		                  				<%--</s:else>--%>
		                  			</select>


	                			</div>

	                			<div class="controls" style="display: none" id="divOV_<s:property value='#ov.index+1'/>">
			                  		<span style="color: red; display: none" id="overRate_validate">设置参数需填写非负整数,0条/0秒或0条/0小时表示该设置参数无效</span>
	               				</div>
	              			</div>
								</s:if>
              			</s:iterator>
              			<input name="overRate_deleteids" style="display:none;" value="0"/>
              		</div>

            </div>

            <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                  <input type="text" name="remarks" value="${data.remarks}" readonly/>
                </div>
            </div>
            

            <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
	              <input type="hidden" id="hidden_id" name="id" value="${data.id}" />
	              <input type="hidden" name="userid_bak" value="${data.userid}" />

	              <input type="button" value="返 回" class="btn btn-error" onclick="back()"/>
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

try {
	var channelid = '${data.channelid}';
	var ydchannelid = '${data.ydchannelid}';
	var ltchannelid = '${data.ltchannelid}';
	var dxchannelid = '${data.dxchannelid}';
	var gjchannelid = '${data.gjchannelid}';
	
} catch (e) {
	// TODO: handle exception
}

vm = new Vue({
	el : "#channelGroupDiv",
	mounted: function(){
		var that = this;
		$.ajax({
			type : "post",
			async: false,
			url : "${ctx}/user/queryAllChannelGroup",
			data : {
				operater : 0
			},
			success : function(data) {
				if (data != null) {
					that.options1 = data.quanwang;
					that.options2 = data.yidong;
					that.options3 = data.liantong;
					that.options4 = data.dianxin;
					that.options5 = data.guoji;
					
					if(channelid.trim().length > 0){
						that.value1 = channelid.split(",");
						$("input[name='channelid']").val(channelid.split(","));
						console.log("int value1: ",$("input[name='channelid']").val());
					}
					if(ydchannelid.trim().length > 0){
						that.value2 = ydchannelid.split(",");
						$("input[name='ydchannelid']").val(ydchannelid.split(","));
						console.log("int value2: ",$("input[name='ydchannelid']").val())
					}
					if(ltchannelid.trim().length > 0){
						that.value3 = ltchannelid.split(",");
						$("input[name='ltchannelid']").val(ltchannelid.split(","));
						console.log("int value3: ",$("input[name='ltchannelid']").val())
					}
					if(dxchannelid.trim().length > 0){
						that.value4 = dxchannelid.split(",");
						$("input[name='dxchannelid']").val(dxchannelid.split(","));
						console.log("int value4: ",$("input[name='dxchannelid']").val())
					}
					if(gjchannelid.trim().length > 0){
						that.value5 = gjchannelid.split(",");
						$("input[name='gjchannelid']").val(gjchannelid.split(","));
						console.log("int value5: ",$("input[name='gjchannelid']").val())
					}
				}else{
					console.log("查询通道组时出错！");
				}
			}
		});
	},
	data : {
		options1 : [],
		options2 : [],
		options3 : [],
		options4 : [],
		options5 : [],
		value1: '',
		value2: '',
		value3: '',
		value4: '',
		value5: ''
	},
	watch : {
		value1 : function(value){
			this.$nextTick(function(){
				$("input[name='channelid']").val(value);
				console.log("watch value1 :" + $("input[name='channelid']").val());
			})
		},
		value2 : function(value){
			this.$nextTick(function(){
				$("input[name='ydchannelid']").val(value);
				console.log("watch value2 :" + $("input[name='ydchannelid']").val());
			})
		},
		value3 : function(value){
			this.$nextTick(function(){
				$("input[name='ltchannelid']").val(value);
				console.log("watch value3 :" + $("input[name='ltchannelid']").val());
			})
		},
		value4 : function(value){
			this.$nextTick(function(){
				$("input[name='dxchannelid']").val(value);
				console.log("watch value4 :" + $("input[name='dxchannelid']").val());
			})
		},
		value5 : function(value){
			this.$nextTick(function(){
				$("input[name='gjchannelid']").val(value);
				console.log("watch value5 :" + $("input[name='gjchannelid']").val());
			})
		}
	}
});

$(function(){
	
	var userId = getUrlParam("userid");
	if( null != userId && userId != ""){
		$("input[name='userid']").val(userId);
		$("input[name='userid']").attr("readonly","readonly");
	}
	if(userId=='smsp_access'){
		$("#noAccess").hide();

	}


	var smstype = getUrlParam("smstype");
	if(smstype != null && smstype != ""){
		$("#smstype").find("option[value='"+smstype+"']").attr("selected",true);
		$("input[name='smstype']").val(smstype);
		$("#smstype").attr("disabled","disabled");
	}else{
		$("#smstype").attr("disabled",false);
	}
	

	$(".control-group").find('select').attr("disabled", true);
    $("input[name='ydchannelid']").attr("disabled", true);
    $("input[name='dxchannelid']").attr("disabled", true);
    $("input[name='ltchannelid']").attr("disabled", true);
    $("input[name='channelid']").attr("disabled", true);
    $("input[name='gjchannelid']").attr("disabled", true);
});

function hide(obj){
	obj.hide();
	obj.find('select').attr("disabled", true);
}

function show(obj){
	obj.show();
	obj.find('select').attr("disabled", false);
}

// 子账号输入框失去焦点时
function clientInputOnblur(){
	
	var clientId = $("input[name='userid']").val()
	var smsTypeList = [0,4,5,6];
	for(var pos=0; pos<smsTypeList.length; pos++){
		$("#smstype").find("option[value='"+smsTypeList[pos]+"']").attr("disabled",false);
	}
    if(clientId=='smsp_access'){
        $("#noAccess").hide();

    }
	// 使用标准协议(2:SMPP 3:CMPP 4:SGIP 5:SMGP)接入的子帐号
	// 只能根据用户帐号中配置的短信类型来配置一条记录
	var smsfrom = "";
	var smstype = "";
	$.ajax({
		type : "post",
		async: false,
		url : "${ctx}/user/getSmsFromAndTypeByClientId",
		data : {
			clientId : clientId
		},
		success : function(data) {
			if(data != null && data.smsfrom != null && data.smstype != null){
				smsfrom = data.smsfrom;
				smstype = data.smstype;
				if(smsfrom == 2 || smsfrom == 3 || smsfrom == 4 || smsfrom == 5){
					for(var pos=0; pos<smsTypeList.length; pos++){
						if(smsTypeList[pos] != smstype){
							$("#smstype").find("option[value='"+smsTypeList[pos]+"']").attr("disabled",true);
						}else{
							$("#smstype").find("option[value='"+smsTypeList[pos]+"']").attr("selected",true);
						}
					}
				}
			}
		}
	});
	
	// 禁用用户已经配置的短信类型
	disableSmsTypeByUserId(clientId);
}

// 禁用用户已经配置的短信类型
function disableSmsTypeByUserId(userId){
	$.ajax({
		type : "post",
		async: false,
		url : "${ctx}/user/getSmsTypeBySid",
		data : {
			userid : userId
		},
		success : function(data) {
			var $smsType = $("#smstype");
			if(data != null && data.smstype != null){
				// 获得用户已经设置通道组配置的短信类型并且禁用
				var smsTypeUsedList = data.smstype.split(",");
				if(smsTypeUsedList.length != 0){
					for(var pos=0; pos<smsTypeUsedList.length; pos++){
						$smsType.find("option[value='"+smsTypeUsedList[pos]+"']").attr("disabled",true);
					}
				}
				
				// 该账号还未设置通道组配置的短信类型
				var smsTypeNotUsedList =  $("#smstype").find("option:enabled");
				if(smsTypeNotUsedList != null && smsTypeNotUsedList.size() > 0){
					$smsType.find("option[value='"+smsTypeNotUsedList[0].value+"']").attr("selected", true);
				}else{
// 					layer.alert("短信账号-" + userId + "的所有短信类型均已经配置通道组");
				}
			}
		}
	});
}

// 是否区分运营商
function operatorsTriger(value, text, isInit){
	 if(value == 1){
		 $(".all_type").hide();//全网
		 $(".type_list_box").show();// 移动、联通、电信
	 }else{
		 $(".all_type").show();
		 $(".type_list_box").hide();
	 }
}


function queryIsClientExist(){
	var clientid = $("input[name='userid']").val();
	var result;
	$.ajax({
		type : "post",
		async: false,
		url : "${ctx}/user/queryIsClientExist",
		data : {
			clientid : clientid
		},
		success : function(data) {
			result = data;
		}
	});
	
	return result;
	
}





</script>
</body>
</html>