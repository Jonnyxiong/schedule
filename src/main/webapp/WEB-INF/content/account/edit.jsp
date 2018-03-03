<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<u:authority menuId="68">
	<s:set var="authority_edit_nodenum" value="true"/>
</u:authority>

<u:authority menuId="69">
	<s:set var="authority_edit_overate" value="true"/>
</u:authority>


<html>
<head>
<title>${data.clientid==null?'添加':'编辑'}子账户</title>
<style type="text/css">
#mourl{
	min-width: 520px;
}
#deliveryurl{
	min-width: 520px;
}
#moip{
	min-width: 520px;
}

.form-horizontal .controls span { display: inline-block; font-size: 14px;  margin-top: 0px !important;}
</style>
</head>
<body menuId="60">
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
						<form class="form-horizontal" method="post" name="basic_validate"
							id="mainForm" novalidate="novalidate">
							<div class="control-group">
								<label class="control-label">用户账号</label>
								<div class="controls">
									<!-- 锁定状态或则子账号已经配置通道组时不可编辑“用户账号” -->
									<s:if test="data.status == 1 or data.isClientIdAssign == 1">
										<input id="clientid" type="text" name="clientid"
										value="${data.clientid}" class="checkAccount" readOnly="readonly"/>
									</s:if>
									<s:else>
										<input id="clientid" type="text" name="clientid"
										value="${data.clientid}" class="checkAccount" readOnly="readonly" onkeyup="this.value=this.value.toLowerCase()"/>
									</s:else>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">选择标识<input type="hidden" id="hid-id" value="${data.max_identify }" /></label>
								<div class="controls">
									<select id="identify" name="identify"></select>
								</div>
							</div>
							<%-- <div class="control-group">
								<label class="control-label">所属平台</label>
								<div class="controls">
									<u:select id="client_ascription" defaultValue="1" data="[{value:'0',text:'阿里平台'},{value:'1',text:'代理商平台'},{value:'2',text:'云平台'}]" 
									value="${data.client_ascription}" showAll="false"/>
									<span for="error"></span>
								</div>
							</div> --%>
							<div class="control-group">
								<label class="control-label">用户名称</label>
								<div class="controls">
									<input type="text" name="name" value="${data.name}"
										maxlength="50" />
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">用户${data.clientid==null?'':'登录'}密码</label>
								<div class="controls">
									<input type="password" name="password" value="${data.password}"
										maxlength="12" class="checkPwd"/>
									<span for="error"></span>
								</div>
								
							</div>
							
							<div class="control-group">
								<label class="control-label">确认密码</label>
								<div class="controls">
									<%-- <input type="password" name="password2" value="${data.password}" maxlength="12"
										class="checkPwdAgain" data-toggle="password"/> --%>
									<input type="password" name="password2" value="${data.password}" maxlength="12"
										class="checkPwdAgain"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">用户等级</label>
								<div class="controls" name="client_level">
				                  <u:select id="client_level" defaultIndex="2" dictionaryType="client_level" value="${data.client_level}" showAll="false" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">用户类型</label>
								<div class="controls" name="client_type">
				                  <u:select id="client_type" dictionaryType="client_type" value="${data.client_type}" showAll="false" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">所属代理商</label>
								<div class="controls" name="agent_id">
								  <input id="agent_id_old" type="hidden" value="${data.agent_id}"/>
				                  <u:select id="agent_id" sqlId="getEnableSmsAgent" placeholder="" value="${data.agent_id}" showAll="false" onChange="agentIdTrigger"/>
				                  <span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">归属销售</label>
								<div class="controls" name="belong_sale">
				                  <u:select id="belong_sale" sqlId="findSalesman" placeholder="请选择归属销售" value="${data.belong_sale}" showAll="false"/>
				                  <span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">状态报告获取方式</label>
								<div class="controls">
									<u:select id="needreport" data="[{value:'0',text:'不获取'},{value:'1',text:'推送简单状态报告'},{value:'2',text:'推送透传状态报告'},{value:'3',text:'用户主动拉取'}]" 
									value="${data.needreport}" showAll="false"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">是否审核</label>
								<div class="controls">
									<u:select id="needaudit" defaultValue="3" data="[{value:'0',text:'不需要'},{value:'1',text:'营销需要'},{value:'2',text:'全部需要'},{value:'3',text:'审核关键字'}]" 
									value="${data.needaudit}" showAll="false"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">上行获取方式</label>
								<div class="controls">
									<u:select id="needmo" data="[{value:'0',text:'不获取'},{value:'1',text:'SMSP推送'},{value:'3',text:'用户主动拉取'}]" 
									value="${data.needmo}" showAll="false"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">IP白名单</label>
								<div class="controls">
									<textarea name="ip" rows="3" cols="90"
										style="height: auto; width: auto;" maxlength="512"
										class="checkWhiteList">${data.ip}</textarea>
									<span for="error"></span>
									<span class="help-block">多个ip以逗号(英文字符)分隔,结尾不需要逗号,支持172.1.1.*/172.1.10/16格式</span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">上行回调地址</label>
								<div class="controls">
									<input id="mourl" type="text" name="mourl" value="${data.mourl}" placeholder=" 输入回调地址 http(s)://"
										maxlength="100" class="checkURL" />
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">状态报告回调地址</label>
								<div class="controls">
									<input id="deliveryurl" type="text" name="deliveryurl" value="${data.deliveryurl}" placeholder=" 输入回调地址 http(s)://"
										maxlength="100" class="checkURL" />
									<span for="error"></span>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">连接节点数</label>
								<div class="controls">
									<input type="text" name="nodenum" value="${empty data.nodenum?1:data.nodenum}"
										maxlength="11" <s:if test="authority_edit_nodenum"></s:if><s:else>readOnly="readOnly"</s:else> />
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">自扩展</label>
								<div class="controls" name="needextend">
				                  <u:select id="needextend" dictionaryType="is_support" value="${data.needextend}" showAll="false"/>
								</div>
							</div>
							<div class="control-group signextend">
								<label class="control-label">签名对应签名端口</label>
								<div class="controls" name="signextend">
				                  <u:select id="signextend" dictionaryType="is_support" value="${data.signextend}" showAll="false" onChange="signextendTriger"/>
				                  <input type="hidden" name="signextend_bak" value="${data.signextend}"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">签名端口长度</label>
								<div class="controls">
									<s:if test="data.signportlen!=null and data.signportlen!='' ">
										<input type="text" name="signportlen" value="${data.signportlen}" readonly="readonly"/>
									</s:if>
									<s:else>
										<input type="text" name="signportlen" value="${data.signportlen}"/>
									</s:else>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">付费类型</label>
								<div class="controls" name="paytype">
				                  <u:select id="paytype" data="[{value:'0',text:'预付费'},{value:'1',text:'后付费'}]" value="${data.paytype}" showAll="false" />
								</div>
							</div>
				            <div class="control-group">
								<div>
									<div class="span5">
										<label class="control-label">短信协议类型</label>
										<!-- 排除0 REST 1 VMSP -->
										<div class="controls">
											<u:select id="smsfrom" value="${data.smsfrom}" dictionaryType="smsfrom" excludeValue="0,1" showAll="false" onChange="smsfromOnChange"/>
										</div>
									</div>
									<div id="http_protocol_type_div" class="span5">
										<label class="control-label">HTTPS子协议类型</label>
										<div class="controls">
											<u:select id="http_protocol_type" data="[{value:'0',text:'https json'},{value:'1',text:'https get/post'},{value:'2',text:'https tx-json'}]"
													  value="${data.http_protocol_type}" showAll="false" onChange="httpProtocolTypeOnChange"/>
										</div>
									</div>
								</div>
				            </div>
				            <div class="control-group">
				              	<label class="control-label">短信类型</label>
				              		<div class="controls ">
					                    <u:select id="smstype" data="[{value:'',text:''}, {value:'0',text:'通知'}, {value:'4',text:'验证码'}, {value:'5',text:'营销'}]" 
					                    	value="${data.smstype}" showAll="false"/>
					                    <span for="error"></span>
					                </div>
				            </div>
				            <div class="control-group">
								<label class="control-label">超频是否扣费</label>
								<div class="controls" name="isoverratecharge">
				                  <u:select id="isoverratecharge" data="[{value:'0',text:'不需要'},{value:'1',text:'需要'}]" value="${data.isoverratecharge}" showAll="false" />
				                  <span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">客户SP号</label>
								<div class="controls">
									<input type="text" name="spnum" value="${data.spnum}" onfocus="inputControl.setNumber(this, 20, 0, false)"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">SGIP协议-客户上行IP</label>
								<div class="controls">
									<input id="moip" type="text" name="moip" value="${data.moip}"
										maxlength="100" class="checkMoip" />
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">SGIP协议-客户上行端口</label>
								<div class="controls">
									<input id="moport" type="text" name="moport" value="${data.moport}"
										onfocus="inputControl.setNumber(this, 11, 0, false)"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">SGIP协议-客户节点编码</label>
								<div class="controls">
									<input type="text" name="nodeid" value="${data.nodeid}" onfocus="inputControl.setNumber(this, 13, 0, false)" />
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">状态报告/上行短信<br>最小拉取间隔(秒)</label>
															 
								<div class="controls">
									<s:if test="data.clientid == null">
										<input type="text" name="getreport_interval" value="5"/>
									</s:if>
									<s:else>
										<input type="text" name="getreport_interval" value="${data.getreport_interval}"/>
									</s:else>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">状态报告/上行短信<br>最大拉取条数</label>
								<div class="controls">
									<s:if test="data.clientid == null">
										<input type="text" name="getreport_maxsize" value="30"/>
									</s:if>
									<s:else>
										<input type="text" name="getreport_maxsize" value="${data.getreport_maxsize}"/>
									</s:else>
									<span for="error"></span>
								</div>
							</div>
							<div id="oauthStatusDiv" class="control-group">
				              	<label class="control-label">认证状态</label>
				              		<div class="controls ">
					                    <u:select id="oauth_status" data="[{value:'2',text:'待认证'}, {value:'3',text:'已认证'}, {value:'4',text:'认证不通过'}]" value="${data.oauth_status}" showAll="false"/>
					                    <span for="error"></span>
					                </div>
				            </div>
				            <div class="control-group">
								<label class="control-label">客户接入速度</label>
								<div class="controls">
									<input type="text" name="access_speed" value="${data.access_speed}"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">支持自扩展位数</label>
								<div class="controls">
									<input type="text" name="extend_size" value="${data.extend_size}"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">用户扩展端口类型</label>
								<div class="controls">
									<u:select id="extendtype" sqlId="getExtendType" placeholder="请选择用户扩展端口类型" value="${data.extendtype}" onChange="extendTriger"></u:select>
									<input type="hidden" name="extendtype_old" value="${data.extendtype}"/>
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">用户扩展端口</label>
								<div class="controls">
									<input type="text" name="extendport" value="${data.extendport}" readonly="readonly"/>
									<input type="hidden" name="extendport_old" value="${data.extendport}" />
									<span for="error"></span>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">通道池策略</label>
								<div class="controls">
									<u:select id="policy_id" sqlId="getPolicyId" placeholder="请选择通道池策略" value="${data.policy_id}"></u:select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">备注</label>
								<div class="controls">
									<input type="text" name="remarks" value="${data.remarks}" maxlength="200" />
									<span for="error"></span>
								</div>
							</div>
							
							<div class="control-group">
				                <label class="control-label">&nbsp;</label>
				                <div class="controls">
				                  <span id="msg" class="error" style="display:none;"></span>
				                </div>
				            </div>
							<div class="form-actions">
								<input type="hidden" name="id" value="${data.id}"/>
								<input type="button" value="${data.clientid==null?'添 加':'修 改'}"
									class="btn btn-success" onclick="save(this)"/> <input
									type="button" value="取 消" class="btn btn-error"
									onclick="back()" />
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		var validate;
		// 是否是新建账号
		var isCreate = ${data.clientid == null ? true : false};
		
		$(function() {
			var flag = '${data.flag}';
			var max_identify = parseInt($("#hid-id").val());
			var opt = '<option>0</option>';
			for (var i = 1;i <= max_identify;i++){
				opt += "<option value='"+i+"'>"+i+"</otpion>";
			}
			$("#identify").html(opt);
			
			var identify = '${data.identify}';
			$("#identify option[value = "+identify+"]").attr("selected","selected");
			if(flag != 'create'){
				$("#identify").attr("disabled",true);
			}
			
			$.validator.defaults.ignore = "";
			
			//表单验证规则
			validate = $("#mainForm").validate({
				errorPlacement: function(error, element) {
					$(element).closest( ".controls" ).find( "span[for='error']" ).append( error );
				},
				errorElement: "span",
				invalidHandler: function(form, validator) {
			        var errors = validator.numberOfInvalids();
			        if (errors) {                    
			            validator.errorList[0].element.focus();
			        }
			    },
				rules : {
					clientid : {
						required : true
					},
					password : {
						required : true,
						rangelength : [ 8, 12 ]
					},
					password2 : {
						required : true,
						rangelength : [ 8, 12 ]
					},
					sid : "required",
					name : "required",
// 					ip : "required",
					nodenum : {
						required : true,
						digits : true,
						range : [1,99]
					},
					overrate_num : {
						required : true,
						digits : true,
						range : [0, 999]
					},
					remarks : "required",
					csign : "required",
					smstype : {
						checkSmsType: true,
                        checkSmsType2 : true
					},
					isoverratecharge : {
						checkOverateCharge: true
					},
					getreport_interval : {
						required : true,
						digits : true,
						range : [1,1000]
					},
					getreport_maxsize : {
						required : true,
						digits : true,
						range : [1,1000]
					},
					access_speed : {
						required : true,
						digits : true,
						range : [1,10000]
					},
					extendtype : {
						required : true
					},
					extendport : {
						required : true
					},
					extend_size : {
						range : [0,10]
					},
					signportlen : {
						range : [0,5],
						isSignextend : true
					},
					agent_id : {
						validateAgentIdAndPayType : true,
						validateAgentIdType : true
					},
					belong_sale: {
						required : true
					}
				},
				messages : {
					clientid : {
						required : "请输入账号"
					},
					password : {
						required : "请输入账户密码",
						rangelength : "密码长度为8-12位"
					},
					password2 : {
						required : "请确认账户密码",
						rangelength : "密码长度为8-12位"
					},
					nodenum : {
						digits : "请输入整数"
					},
					overrate_num : {
						digits : "请输入整数"
					},
					smstype : {
						checkSmsType: '短信协议为SMPP/CMPP/SGIP/SMGP时短信类型必须选择一种'
					},
					extendtype : {
						required : "清选择用户扩展端口类型"
					}
				}
			});

			function checkWhiteList(str) {
				var ipArray = str.split(",");
				
				for ( var pos = 0; pos < ipArray.length; pos ++) {
					var isAdmin = '${sessionScope.LOGIN_ROLE_ID}' == 1 ? true : false;// 是否是超级管理员
					var ip = $.trim(ipArray[pos]);
					
					var reg = /^(((1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d|\*|\d+\/\d+)))$/;
					if(isAdmin){// 超级管理员可以在IP白名单中输入 "*" 号 
						reg = /^(((1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d|\*|\d+\/\d+))|(\*))$/;
					}
// 					if (!/^(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)$/i
// 							.test(t)) {
// 						return true;
// 					}
					if(!reg.test(ip)){
						return true;
					}
				}
				return false;
			}

			// ip地址校验
			jQuery.validator
					.addMethod(
							"checkWhiteList",
							function(value, element) {
								return this.optional(element)
										|| !checkWhiteList(value);
							}, "请输入合法的ip地址");

			// 回调地址校验
			jQuery.validator
					.addMethod(
							"checkURL",
							function(value, element) {
								//下面的代码中应用了转义字符"\"输出一个字符"/"
								var Expression = /^http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
								var objExp = new RegExp(Expression);
								return this.optional(element)
										|| (objExp.test(value));
							}, "请输入合法的回调地址");
			
			// moip地址校验
			jQuery.validator
					.addMethod(
							"checkMoip",
							function(value, element) {
								
								// 判断是不是IP地址
								var splitIpList = value.split(".");
								var isIP = true;
								for(var pos = 0; pos < splitIpList.length; pos++){
									if(isNaN(splitIpList[pos])){
										isIP = false;// 存在非数字说明不是IP地址
									}
								}
								
								if(isIP){
									var ipReg = /^(((1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d|\*|\d+\/\d+)))$/;
									var ipRegExp = new RegExp(ipReg);
									return this.optional(element) || (ipRegExp.test(value));
								}else{
									var urlReg = /^([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
									var urlRegExp = new RegExp(urlReg);
									return this.optional(element) || (urlRegExp.test(value));
								}
	
							}, "请输入合法的SGIP协议客户上行IP或域名");

			// 账号规则：字母、数字、字母与数字组合，区分大小写
			jQuery.validator.addMethod("checkAccount",
					function(value, element) {
// 						var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6}$/; 
						var reg = /^[a-zA-Z0-9]{6}$/; 
						return this.optional(element) || (reg.test(value));
					}, "请输入6位的字母、数字、字母与数字组合的账号");

			// 密码与账号不能相同
			jQuery.validator.addMethod("checkPwd", function(value, element) {
				var clientid = $("#clientid").val();
				return this.optional(element) || !(value == clientid);
			}, "请输入与账号不相同的密码");
			
			// 密码两次输入确认
			jQuery.validator.addMethod("checkPwdAgain", function(value, element) {
				var password = $(".checkPwd").val();
				return this.optional(element) || (value == password);
			}, "两次输入的密码不一致");
			
			// 中文签名字符校验
			jQuery.validator.addMethod("checkCsign", function(value, element) {
				var reg = /^[0-9a-zA-Z\u4e00-\u9fa5]+$/;
				return this.optional(element) || (reg.test(value));
			}, "中文签名只能包含中文、数字、字母");
			
			// 中文签名长度校验
			jQuery.validator.addMethod("checkCsignLength", function(value, element) {
			    var length = value.length;
			    for(var i = 0; i < value.length; i++){
// 			    	中文签名含有英文或数字时，英文和数字也要算两个字符
					length++;
// 			        if(value.charCodeAt(i) > 127){
// 			            length++;
// 			        }
			    }
			  return this.optional(element) || ( length <= 16);   
			}, "请确保输入的中文签名不大于16个字节");
			
			// 英文签名字符校验
			jQuery.validator.addMethod("checkEsign", function(value, element) {
				var reg = /^[0-9a-zA-Z]+$/;
				return this.optional(element) || (reg.test(value));
			}, "英文签名只能包含数字、字母");
			
			// 英文签名长度校验
			jQuery.validator.addMethod("checkEsignLength", function(value, element) {
			  return this.optional(element) || ( value.length <= 18);   
			}, "请确保输入的英文签名不大于18个字节");
			
			// 平台账号规则：32位字母与数字组合
			jQuery.validator.addMethod("checkSid",
					function(value, element) {
						var reg = /^(?![0-9]+$)(?![a-z]+$)[a-z0-9]{32}$/; 
						return this.optional(element) || (reg.test(value));
					}, "请输入32位字母与数字组合的平台账号");
			
			// 选择支持“签名对应端口”时“签名端口长度”必须填写
			jQuery.validator.addMethod("isSignextend",
					function(value, element) {
						var signextend = $("#signextend").val();
						if(signextend == 1){
							if( value == '' || Number(value) <= 0){
								return false;
							}
						}
						
						return true;
					}, "支持签名对应端口时签名端口长度不能为空且必须大于0");
			
			jQuery.validator.addMethod("validateAgentIdAndPayType",
					function(value, element) {
						var paytype = $("#paytype").val();
						if(paytype == 0){
							if( value == ''){
								return false;
							}
						}
						return true;
					}, "预付费用户必须有所属的代理商");
			
			jQuery.validator.addMethod("validateAgentIdType",
					function(value, element) {
						var agent_id_old = $("#agent_id_old").val();
						var agent_id = value;
						var isSameAgentType = false;
						if(agent_id_old != ''){
							$.ajax({
								type : "post",
								async: false,
								url : "${ctx}/account/validateAgentIdType",
								data : {
									agent_id_old : agent_id_old,
									agent_id : agent_id
								},
								success : function(data) {
									if(data.result == 'success'){
										isSameAgentType = true;
									}
								}
							});
							
							if(isSameAgentType){
								return true;
							}else{
								return false;
							}
						}
						
						return true;
					}, "切换代理商前后的代理商类型必须与老的代理商是同类型");

            // HTTPS子协议类型为 https tx-json 时校验短信类型不能为空
            jQuery.validator.addMethod("checkSmsType2",
                function(value, element) {debugger;
					var http_protocol_type = $("#http_protocol_type").val();
                	if(value == '' && http_protocol_type == 2){
                	    return false;
					}

                    return true;
                }, "HTTPS子协议类型为https tx-json时短信类型不能为空");

		});
		
		function save(btn) {
			$("#msg").hide();
			if (!validate.form()) {
				return;
			}
			var clientid = $("#clientid").val();
			var signextend = $("#signextend").val();
			$("input[name='signextend_bak']").val(signextend);
			if(Number(signextend) > 0){
				$("input[name='signportlen']").attr("readonly","readonly");
			}
			
			var id = $("input[name='id']").val();
			var url = id == "" ? "${ctx}/account/save" : "${ctx}/account/update";
			
			var options = {
				beforeSubmit : function() {
					$(btn).attr("disabled", true);
				},
				success : function(data) {
					$(btn).attr("disabled", false);

					if (data.result == null) {
						$("#msg").text("服务器错误，请联系管理员").show();
						return;
					}else{
						if(data.id != null){
							$("input[name='id']").val(data.id);
						}
						$("input[name='extendtype_old']").val($("#extendtype").val());
						console.log("extendtype_old:" + $("input[name='extendtype_old']").val());
						$("input[name='extendport_old']").val($("input[name='extendport']").val());
						console.log("extendport_old:" + $("input[name='extendport_old']").val());
						$("#clientid").attr("readonly",true);
					}

					$("#msg").text(data.msg).show();
				},
				url : url,
				type : "post",
				timeout : 30000
			};
			$("#mainForm").ajaxSubmit(options);
		};
		
		function smsfromOnChange(value, text, isInit){
			$("#smstype").attr("disabled",false);
			
			// 当协议类型是HTTPS的时候短信类型为空
			if(value == 6){
			    var http_protocol_type = $("#http_protocol_type").val();
			    if(http_protocol_type != 2){
					$("#smstype").find("option[value='']").attr("selected",true);
					$("#smstype").attr("disabled","disabled");
				}

				$("#http_protocol_type_div").show();
			}else{
                $("#http_protocol_type").val(0);
				$("#http_protocol_type_div").hide();

			}
			
			// 创建用户账号的时候认证状态的规则
			var smsfrom = value;
			var agentId = $("#agent_id").val();
			if(agentId == ""){ // 无代理商的情况
				if(isCreate){
					// 如果是创建无代理商的用户账号则“认证状态”默认选择为“已认证”
					$("#oauth_status").val(3);
					$("#oauthStatusDiv").hide();
				}
			}else{ // 有代理商的情况
				if(isCreate){
					if(smsfrom == 6){
						// 创建使用HTTP协议的代理商用户账号时认证状态只能是待认证
						$("#oauth_status").val(2);
						$("#oauthStatusDiv").hide();
					}else{
						// 创建非HTTP协议的代理商用户账号时认证状态只能是已认证
						$("#oauth_status").val(3);
						$("#oauthStatusDiv").hide();
					}
				}
			}
		}

       	function httpProtocolTypeOnChange(value, text, isInit) {
		    if(!isInit){
				if(value == 2){
					$("#smstype").attr("disabled", false);
				}else{
					$("#smstype").find("option[value='']").attr("selected",true);
					$("#smstype").attr("disabled","disabled");
				}
			}
        }
		
		function generateClientId(clientIdType){
			var clientId;
			$.ajax({
				type : "post",
// 				async: false,
				url : "${ctx}/account/generateClientId",
				data : {
					clientIdType : clientIdType
				},
				success : function(result) {
					if(result != null){
// 						console.log(result);
						clientId = result.clientId;
// 						console.log(clientId);
						$("#clientid").val(result.clientId);
					}
				}
			});
		}
		
		function extendTriger(value, text, isInit){
			if(!isInit){
				$.ajax({
					type : "post",
					async: false,
					url : "${ctx}/account/getExtendPortByExtendType",
					data : {
						extendtype : value
					},
					success : function(data) {
						if (data != null) {
							var currentnumber = Number(data.currentnumber);
							var endnumber = Number(data.endnumber);
							if(currentnumber <= endnumber){
								$("input[name='extendport']").val(data.currentnumber);
							}else{
								var extendport_old = $("input[name='extendport_old']").val();
								var extendtype_old = $("input[name='extendtype_old']").val();
								
// 								if(extendport_old.length != 0){
								if(extendtype_old == value){
									$("input[name='extendport']").val(extendport_old);
								}else{
									$("input[name='extendport']").val("");
									layer.alert("当前“用户扩展端口类型”的端口已经分配完毕，请选择其他类型");
								}
							}
						}else{
							console.log("${ctx}/account/getExtendPortByExtendType 接口查询时返回null");
						}
					}
				});
				
			}
		}
		
		function signextendTriger(value, text, isInit){
			var signportlen = $("input[name='signportlen']").val();
			if(value == 0){
				$("input[name='signportlen']").attr("readonly","readonly");
			}else if(signportlen.length == 0 || Number(signportlen) == 0){
				$("input[name='signportlen']").removeAttr("readonly","readonly");
			}
		}
		
		function agentIdTrigger(value, text, isInit){
			
			var clientIdType = 0; // 0 归属于代理商的客户的账号，1 直客的账号
			var smsfrom = $("#smsfrom").val();
			
			// 创建用户账号的时候认证状态的规则和根据有无代理商生成clientId
			if(value == ""){ // 无代理商的情况
				clientIdType = 1;
				
				if(isCreate){
					generateClientId(clientIdType);
					
					// 如果是创建无代理商的用户账号则“认证状态”默认选择为“已认证”
					$("#oauth_status").val(3);
					$("#oauthStatusDiv").hide();
				}
			}else{ // 有代理商的情况
				clientIdType = 0;
				if(isCreate){
					generateClientId(clientIdType);
					
					if(smsfrom == 6){
						// 创建使用HTTP协议的代理商用户账号时认证状态只能是待认证
						$("#oauth_status").val(2);
						$("#oauthStatusDiv").hide();
					}else{
						// 创建非HTTP协议的代理商用户账号时认证状态只能是已认证
						$("#oauth_status").val(3);
						$("#oauthStatusDiv").hide();
					}
				}
			}
			
			// 根据代理商的所属销售级联变动clientId的所属销售
			if(!isInit){
				if(value != ""){
					$.ajax({
						type : "post",
						async: false,
						url : "${ctx}/account/getAgentBelongSale",
						data : {
							agentId : value
						},
						success : function(data) {debugger;
							if (data != null && data.agentBelongSale != null) {
								console.log("设置clientId的所属销售为：" + data.agentBelongSale);
								$("#belong_sale").val(data.agentBelongSale);
							}else{
								console.log("查询代理商的所属销售时发生错误");
							}
						}
					});
				}
			}
			
		}
		
		//@ sourceURL= edit.js
	</script>
	
</body>
</html>