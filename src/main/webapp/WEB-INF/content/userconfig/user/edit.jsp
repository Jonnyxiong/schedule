<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}用户及通道组配置</title>
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
                	<input type="text" name="userid" id="userid" value="${data.userid}" class="clientid checkClientId1 checkClientId2"
                		onblur="clientInputOnblur()" onkeyup="this.value=this.value.toLowerCase()">
                </div>
              </div>
             <div class="control-group">
              	<label class="control-label">短信类型</label>
              		<div class="controls">
	                    <u:select id="smstype" data="[{value:'0',text:'通知'}, {value:'4',text:'验证码'}, {value:'5',text:'营销'}, {value:'6',text:'告警短信'}, {value:'7',text:'USSD'}, {value:'8',text:'闪信'}]" 
	                    value="${data.smstype}" showAll="false"/>
	                    <input type="hidden" name="smstype" value="${data.smstype}">
	                </div>
             </div>
             <div class="control-group">
              	<label class="control-label">区分运营商</label>
                <div class="controls">
                  <u:select id="distoperators" data="[{value:'0',text:'否'}, {value:'1',text:'是'}]" value="${data.distoperators}" showAll="false" onChange="operatorsTriger"/>
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
							<el-select v-model="value1" name="channelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" @change="handleChange1">
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
							<el-select v-model="value2" name="ydchannelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" @change="handleChange2">
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
							<el-select v-model="value3" name="ltchannelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" @change="handleChange3">
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
							<el-select v-model="value4" name="dxchannelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" @change="handleChange4">
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
						<el-select v-model="value5" name="gjchannelid" multiple filterable multiple-limit="5" size="large" placeholder="请选择" @change="handleChange5">
						    <el-option
						      v-for="item in options5"
						      :label="item.label"
						      :value="item.value"
							  :key="item.flag">
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
							<s:if test="#item.overRate_mode==0">
	              			<div class="template_overrate_group">
	                			<div class="controls overRateConfig" id="template_overrate_<s:property value='#ov.index+1'/>">
		              				<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_num_s"/>" maxlength="20" style="width: 60px; height: 20px"/>
		                  			条
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_time_s"/>" maxlength="20" style="width: 60px; height: 20px"/>
		                  			秒
		                  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		              				<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_num_m"/>" maxlength="20" style="width: 60px; height: 20px"/>
		                  			条
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_time_m"/>" maxlength="20" style="width: 60px; height: 20px"/>
		                  			分钟
		                  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_num_h"/>" maxlength="20" style="width: 60px; height: 20px"/>
		                  			条
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.overRate_time_h"/>" maxlength="20" style="width: 60px; height: 20px"/>
		                  			小时
		                  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                  			<!-- 每个模板超频配置id(数据库) -->
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="<s:property value="#item.id"/>" style="display:none;" />
		                  			<!-- 每个模板超频配置通过标志 -->
		                  			<input class="inputNum_1" type="text" name="overRate_<s:property value='#ov.index+1'/>" value="1" style="display:none;"/>
		                  			<!-- 当前模板超频配置id(页面序号)-->
		                  			<input class="inputNum_1" type="text" name="overRate_id" value="<s:property value='#ov.index+1'/>" style="display:none;"/>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
		                  				</s:if>

		                  			</select>
		                  		
		                  			<input type="button" name="<s:property value="#item.id"/>" value="删除" onclick="deleteTemplateOverRate(this)"/>
	                			</div>
	                			<div class="controls" style="display: none" id="divOV_<s:property value='#ov.index+1'/>">
			                  		<span style="color: red; display: none" id="overRate_validate">设置参数需填写非负整数,0条/0秒或0条/0小时表示该设置参数无效</span>
	               				</div>
	              			</div>
							</s:if>
              			</s:iterator>
              			<input name="overRate_deleteids" style="display:none;" value="0"/>
              		</div>
              		<div class="addBtn controls">
              			<input type="button" value="添加" onclick="addTemplateOverRate()">
              		</div>
            </div>
              
            <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                  <input type="text" name="remarks" value="${data.remarks}" />
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
				    var quanwang = [], yidong = [], liantong = [], dianxin = [],guoji = [];
				    //遍历数据，添加flag
					for(var i = 0; i < data.quanwang.length; i++){
						quanwang[i] = {};
						quanwang[i].value = data.quanwang[i].value;
						if(data.quanwang[i].flag == 2){
                            quanwang[i].label = data.quanwang[i].label + "(自动路由)";
                        } else {
                            quanwang[i].label = data.quanwang[i].label;
                        }
                        quanwang[i].flag = data.quanwang[i].flag;
                    }
                    for(var i = 0; i < data.yidong.length; i++){
                        yidong[i] = {};
                        yidong[i].value = data.yidong[i].value;
                        if(data.yidong[i].flag == 2){
                            yidong[i].label = data.yidong[i].label + "(自动路由)";
                        } else {
                            yidong[i].label = data.yidong[i].label;
                        }
                        yidong[i].flag = data.yidong[i].flag;
                    }
                    for(var i = 0; i < data.liantong.length; i++){
                        liantong[i] = {};
                        liantong[i].value = data.liantong[i].value;
                        if(data.liantong[i].flag == 2){
                            liantong[i].label = data.liantong[i].label + "(自动路由)";
                        } else {
                            liantong[i].label = data.liantong[i].label;
                        }
                        liantong[i].flag = data.liantong[i].flag;
                    }
                    for(var i = 0; i < data.dianxin.length; i++){
                        dianxin[i] = {};
                        dianxin[i].value = data.dianxin[i].value;
                        if(data.dianxin[i].flag == 2){
                            dianxin[i].label = data.dianxin[i].label + "(自动路由)";
                        } else {
                            dianxin[i].label = data.dianxin[i].label;
                        }
                        dianxin[i].flag = data.dianxin[i].flag;
                    }
                    for(var i = 0; i < data.guoji.length; i++){
                        guoji[i] = {};
                        guoji[i].value = data.guoji[i].value;
                        if(data.guoji[i].flag == 2){
                            guoji[i].label = data.guoji[i].label + "(自动路由)";
                        } else {
                            guoji[i].label = data.guoji[i].label;
                        }
                        guoji[i].flag = data.guoji[i].flag;
                    }
                    console.log("quanwang:", quanwang)
					that.options1 = quanwang;
					that.options2 = yidong;
					that.options3 = liantong;
					that.options4 = dianxin;
					that.options5 = guoji;
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
    methods: {
        handleChange1: function(value) {
//            var last = value[value.length-1];
            //var last = 19016;
//            for(var i = 0;i< this.options1.length; i++){
//                if(last == this.options1[i].value && this.options1[i].flag == 2){
//                    layer.msg("该通道组为自动路由通道池,只可选择一个，不可多选")
//                    break;
//                }
//            }
        },
        handleChange2: function(value){
//            var last = value[value.length-1];
//            for(var i = 0;i< this.options1.length; i++){
//                if(last == this.options1[i].value && this.options1[i].flag == 2){
//                    layer.msg("该通道组为自动路由通道池,只可选择一个，不可多选")
//                    break;
//                }
//            }
        },
        handleChange3: function(value){
//            var last = value[value.length-1];
//            for(var i = 0;i< this.options1.length; i++){
//                if(last == this.options1[i].value && this.options1[i].flag == 2){
//                    layer.msg("该通道组为自动路由通道池,只可选择一个，不可多选")
//                    break;
//                }
//            }
        },
        handleChange4: function(value){
//            var last = value[value.length-1];
//            for(var i = 0;i< this.options1.length; i++){
//                if(last == this.options1[i].value && this.options1[i].flag == 2){
//                    layer.msg("该通道组为自动路由通道池,只可选择一个，不可多选")
//                    break;
//                }
//            }
        },
        handleChange5: function(value){
//            var last = value[value.length-1];
//            for(var i = 0;i< this.options1.length; i++){
//                if(last == this.options1[i].value && this.options1[i].flag == 2){
//                    layer.msg("该通道组为自动路由通道池,只可选择一个，不可多选")
//                    break;
//                }
//            }
        }

    },
	watch : {
        /*value1 : function(key){
            this.$nextTick(function(){
                console.log("watch key :" + key);
              $("input[name='channelid']").val(key);
                console.log("watch value1 :" + $("input[name='channelid']").val());
            })
        },*/
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
	
	/*// 超频配置只能配置一条，存在一条配置时隐藏添加按钮
	var overRateConfigNum = $(".template_overrate_group").find('.overRateConfig').length;
	if(overRateConfigNum >= 2){
		 $(".addBtn").hide();//隐藏添加按钮
	}*/
	
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			userid: "required",
			username: "required"
		},
		messages: {
			username: "请输入用户名"
		}
	});
	
	// 子账号规则：字母、数字、字母与数字组合，区分大小写
	jQuery.validator.addMethod("checkClientId1",
			function(value, element) {
				var reg = /^[a-zA-Z0-9]{6}$/;

				return this.optional(element) || (reg.test(value) || value == "smsp_access" ||value =="*" );
			}, "请输入6位的字母、数字、字母与数字组合的子账号");
	
	// 校验clientid是否已经创建
	jQuery.validator.addMethod("checkClientId2",
			function(value, element) {
				if(value == "smsp_access"){
					return true;
				}else if(value =="*"){
                    return true;
				}else{
					return this.optional(element) || (queryIsClientExist() != null);
				}
			}, "子账号不存在，请先创建子账号");
	
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
//    if(clientId=='*'){
//        $("#noAccess").hide();
//
//    }
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

// 超频配置校验
function overRateValiS(ons,ots,onm,otm,onh,oth,valid,odiv,osign){
	var overRate_validate = false;

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
	// 如果校验通过，则将标志改为1
	if(overRate_validate == false) {
		$(osign).val(0);
	}else{
		$(osign).val(1);
	}
}

//判断是否有选择了flag为2 的通道
function checkflag(channelid, opt,smstype){
    var arr_channel = channelid.split(",");
    var opt = opt;

    for(var i = 0;i < arr_channel.length; i++){
        for(var j = 0; j < opt.length; j++){
            if($("#userid").val() == "*"){
                if(opt[j].flag != 2 && opt[j].value == arr_channel[i]){
                    layer.msg("用户账号为'*'的时候，所选通道组只能为自动路由通道池",{icon:2})
                    return false;
                } else if(arr_channel.length > 1){
                    layer.msg("用户账号为'*'的时候，只能选择一个自动路由通道池通道组",{icon:2})
                    return false;
				}else if( opt[j].flag == 2  && (smstype==7 ||smstype==8)){
                    layer.msg("选择自动路由通道池通道组,短信类型只能是验证码,通知,营销,告警",{icon:2})
                    return false;
				}
			} else {
                if(opt[j].flag == 2 && opt[j].value == arr_channel[i]){
                    layer.msg("自动路由通道池通道组只能适用于用户账号为*的情况通道",{icon:2})
                    return false;
                }
			}

        }
    }
    return true;
}
function save(btn){
	$("#msg").hide();
	
	$("#channelid-error").remove();
	$("#ydchannelid-error").remove();
	$("#ltchannelid-error").remove();
	$("#dxchannelid-error").remove();
	
	
	if($("#distoperators").val() == 0){
		if(vm.value1.length == 0){
			if($("#channelid-error").length == 0){
				$('<label id="channelid-error" class="cg-error" style="color:red;">请选择通道组</label>').insertAfter( $(".channelid").find(".el-select"));
			}
		}
	}else{
		// 校验移动、联通、电信通道组是否至少选择一种，无选中是添加错误标示
		var yd = vm.value2.length;
		var lt = vm.value3.length;
		var dx = vm.value4.length;
		if(yd == 0 && lt == 0 && dx == 0){
			if($("#ydchannelid-error").length == 0){
				$('<label id="ydchannelid-error" class="cg-error" style="color:red;">移动、联通、电信至少选择一种通道组</label>').insertAfter( $(".ydchannelid").find(".el-select"));
			}
			if($("#ltchannelid-error").length == 0){
				$('<label id="ltchannelid-error" class="cg-error" style="color:red;">移动、联通、电信至少选择一种通道组</label>').insertAfter( $(".ltchannelid").find(".el-select"));
			}
			if($("#dxchannelid-error").length == 0){
				$('<label id="dxchannelid-error" class="cg-error" style="color:red;">移动、联通、电信至少选择一种通道组</label>').insertAfter( $(".dxchannelid").find(".el-select"));
			}
		}
	}
	
	
	if(!validate.form()){
		return;
	}
	
	// 校验移动、联通、电信通道组是否至少选择一种
	if($(".cg-error").length != 0){
		return;
	}
	console.log("开始通道flag校验了")
	var smstype=$("#smstype").val(); //7,8
	//校验是否选择了flag2的通道且选择了多个
	var channelid = $("input[name='channelid']").val();
	var ydchannelid = $("input[name='ydchannelid']").val();
	var ltchannelid = $("input[name='ltchannelid']").val();
	var dxchannelid = $("input[name='dxchannelid']").val();
	var gjchannelid = $("input[name='gjchannelid']").val();
	if(channelid){
	    var opt = vm.options1;
	    console.log(checkflag(channelid, opt))
        if(!checkflag(channelid, opt,smstype)){
            return;
		}
	}
    if(ydchannelid){
        var opt = vm.options2;
        if(!checkflag(ydchannelid, opt,smstype)){
            return;
        }
    }
    if(ltchannelid){
        var opt = vm.options3;
        if(!checkflag(ltchannelid, opt,smstype)){
            return;
        }
    }
    if(dxchannelid){
        var opt = vm.options4;
        if(!checkflag(dxchannelid, opt,smstype)){
            return;
        }
    }
    if(gjchannelid){
        var opt = vm.options5;
        if(!checkflag(gjchannelid, opt,smstype)){
            return;
        }
    }
    console.log("开始通道flag校验完毕")
	//保存时对参数做校验
	var tidArray = new Array();
	var tog = $(".template_overrate_group");
	var overRateConfigTypeList = [];
    console.log("开始超频校验")
	for(var i = 0; i<tog.length; i++) {
		var confs = $(tog.get(i)).children("div:first-child").children("input");
		var ons = confs.get(0);
		var ots = confs.get(1);
		var onm = confs.get(2);
		var otm = confs.get(3);
		var onh = confs.get(4);
		var oth = confs.get(5);
		var osign = confs.get(7);//参数是否通过标志
		var overRateMode = $(tog.get(i)).children("div:first-child").find("select")[1].value;
		overRateConfigTypeList.push(overRateMode);
		var odiv = $(tog.get(i)).children("div:last-child");
		var ovs = odiv.children("span:first-child");
		
		/* (function( ons, ots, onh, oth, ovs, odiv, osign){
			overRateValiS(ons.value,ots.value,onh.value,oth.value,ovs,odiv,osign);
		})(ons, ots, onh, oth, ovs, odiv, osign); */
		
		overRateValiS(ons.value,ots.value,onm.value,otm.value,onh.value,oth.value,ovs,odiv,osign);
	
		// 判断每个标志，遇到错误的即返回
		if($(osign).val() == 0) {
			return;
		}
	}
	if(overRateConfigTypeList.length == 2){
		if(overRateConfigTypeList[0] == overRateConfigTypeList[1]){
			$("#msg").text("短信超频限制不能同时配置两条相同统计类型的记录").show();
			return;
		}
	}
    console.log("超频校验完毕")
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

			if(data.result = "success"){
				// 将成功入库后的模板超频配置id赋值
				if(data.temOverRateId && data.temOverRateId != "") {
					var idArry = data.temOverRateId.split(";");debugger;
					for(var i=0;i < idArry.length;i++) {
						var item = idArry[i].split(",");
						var divto = $("#template_overrate_" + item[0]);
						divto.children("input:nth-child(7)").val(item[1]);
					}
				}
				console.log("保存成功... overRate_deleteids=" + $("input[name='overRate_deleteids']").val());
                $("input[name='overRate_deleteids']").val(0);
                $("#msg").text(data.msg).show();
                back();
			}

			$("input[name='userid_bak']").val($("input[name='clientid']").val());


		},
		url : "${ctx}/user/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};


/*添加模板超频配置*/
function addTemplateOverRate(){
	var len;
	var overRateConfigNum = $(".template_overrate_group").find('.overRateConfig').length;
	if(overRateConfigNum) {
		len = Number(overRateConfigNum) + 1;
	}else {
		len = 1;
	}
	
	var html = '<div class="template_overrate_group">'
		+ '<div class="controls overRateConfig" id="template_overrate_' +len+ '">'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_' +len+ '"value ="0" maxlength="20" style="width: 60px; height: 20px"/>'
		+ '		条'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_' +len+ '"value ="0" maxlength="20" style="width: 60px; height: 20px"/>'
		+ '		秒'
		+ '		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_' +len+ '"value ="0" maxlength="20" style="width: 60px; height: 20px"/>'
		+ '		条'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_' +len+ '"value ="0" maxlength="20" style="width: 60px; height: 20px"/>'
		+ '		分钟'
		+ '		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_' +len+ '"value ="0" maxlength="20" style="width: 60px; height: 20px"/>'
		+ '		条'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_' +len+ '"value ="0" maxlength="20" style="width: 60px; height: 20px"/>'
		+ '		小时'
		+ '		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_' +len+ '" value="-1" style="display:none"/>'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_' +len+ '" value="1" style="display:none"/>'
		+ '		<input class="inputNum_' +len+ '" type="text" name="overRate_id" value="' +len+ '" style="display:none"/>'
        + '		开关:<select class="inputNum_' +len+ '" name="overRate_' +len+ '" style="width: 150px; margin-right: 20px;">'
        + '		<option value="0" >关</option>'
        + '		<option value="1" selected="selected">开</option>'
        + '     </select>'
		+ '     <select class="inputNum_' +len+ '" name="overRate_' +len+ '"style="width: 150px; margin-right: 20px;">'
		+ '		    <option value="0" selected="selected">按短信类型统计</option>'
		+ '     </select>'
		+ '		<input type="button" name="0" value="删除" onclick="deleteTemplateOverRate(this)"/>'
		+ '		</div>'
		+ '<div class="controls" style="display: none" id="divOV_' +len+ '">'
		+ '		<span style="color: red; display: none" id="overRate_validate">设置参数需填写非负整数,0条/0秒或0条/0小时表示该设置参数无效</span>'
		+ '</div>'
		+ '</div>';
	$('#template_overrate_list').append(html);
	
	var overRateConfigNum = $(".template_overrate_group").find('.overRateConfig').length;
	if(overRateConfigNum >= 2){
		$(".addBtn").hide();
	}
	
}

/*删除模板超频配置*/
function deleteTemplateOverRate(obj){
	var id = $(obj).attr("name");
	if(id != 0) {
		//删除的模板超频配置id赋值
		var inputdelete = $("input[name='overRate_deleteids']");
		var idsval = inputdelete.val();
		if(idsval == 0){
			inputdelete.val(id);
		}else{
			inputdelete.val(idsval+";"+id);
		}
		console.log("需要删除的超频配置" + inputdelete.val());
	}
	
	$(obj).closest($('.template_overrate_group')).remove();
	$(".addBtn").show();
}



</script>
</body>
</html>