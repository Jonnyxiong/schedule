//自定义验证方法

/*
 * 手机号码验证
 */
jQuery.validator.addMethod("mobile_real", function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9\._-]+@[a-zA-Z0-9_-]+\.[a-zA-Z0-9_-]{2,4}|(13[0-9]|15[012356789]|18[02356789]|14[57])[0-9]{8}$/.test(value);
}, "请输入有效的手机号码");

jQuery.validator.addMethod("mobile", function(value, element) {
	return this.optional(element) || /^1[0-9]{10}$/.test(value);
}, "请输入有效的手机号码");

/*
 * email验证
 */
jQuery.validator.addMethod("email2", function(value, element) {
	return this.optional(element) || /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(value);
}, "请输入有效的电子邮件");


/*jQuery.validator.addMethod("num_dot_split", function(value, element) {
	return this.optional(element) || /^((1)\d{2,3}\,)*(1)\d{2,3}$/.test(value);
}, "请输入数字以1开头，以逗号分隔，每个数字不超过4位");*/
jQuery.validator.addMethod("num_dot_split", function(value, element) {
	return this.optional(element) || /^(\d{3,4}[,])*(\d{3,4})$/.test(value);
}, "请输入数字，以逗号分隔，每个数字不超过4位");


//校验平台账号和子账号是否合法
jQuery.validator.addMethod("checkClientIdAndSid",
	function(value, element) {
		var reg_sid = /^(?![0-9]+$)(?![a-z]+$)[a-z0-9]{32}$/; 
		var reg_clientId = /^[a-zA-Z0-9]{6}$/; 
		if(value.length == 6){
			return this.optional(element) || (reg_clientId.test(value));
		}
		if(value.length == 32){
			return this.optional(element) || (reg_sid.test(value));
		}
		
		return false;
}, "请输入合法的6位子账号或32位平台账号");

//校验子账号是否合法
jQuery.validator.addMethod("checkClientId",
	function(value, element) {
		var reg_clientId = /^[a-zA-Z0-9]{6}$/; 
		if(value.length == 6){
			return this.optional(element) || (reg_clientId.test(value));
		}
		
		return false;
}, "请输入合法的6位子账号");

// 子账户管理中短信类型校验
jQuery.validator.addMethod("checkSmsType",
		function(value, element) {
			var smsfrom = $("#smsfrom").val(); // 短信协议类型
			if((smsfrom == 2 || smsfrom == 3 || smsfrom == 4 || smsfrom == 5) && value == ""){
				return false;
			}else{
				return true;
			}
		}, "短信协议为SMPP/CMPP/SGIP/SMGP时短信类型必须选择一种");

//子账户管理中超频扣费校验
jQuery.validator.addMethod("checkOverateCharge",
		function(value, element) {
			var paytype = $("#paytype").val(); // 短信协议类型
			if(paytype == 1 && value == 1){
				return false;
			}else{
				return true;
			}
		}, "后付费时超频不能扣费");

