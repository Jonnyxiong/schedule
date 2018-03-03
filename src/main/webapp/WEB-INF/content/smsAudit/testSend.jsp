<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>短信发送测试</title>
<style type="text/css">
	.form-horizontal .controls span{
		margin-top:0;
	}
	textarea{
		resize:none;
		width:400px;
		height:300px;
	}
	.example{
		line-height: 30px;
	}
	.select2 span{
		display:block !important;
		margin-top: 0px !important;
	}
	.control-group .select2-container {
		width: 216px !important;
	}
	.select2-dropdown {
		width: 220px !important;
	}
	.hover-tip{
		position: relative;
		display: inline-block;
		width:15px;
		height:15px;

	}
	.hover-tip .tips{
		display: none;
		text-align: left;
		color:#FFF;
		position: absolute;
		width:200px;
		padding:10px;
		left:-96px;
		top:21px;
		border-radius:6px;
		background-color: #fe6633;
	}
	.hover-tip:hover .tips{
		display: block;
	}
    .staticsBox{
        width: 400px;
        text-align: right;
    }
    .reset{
        margin-right: 0;
    }
	.operator{
		display: none ;
	}
</style>
</head>

<body menuId="284">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<br/>
		<div class="row-fluid">
			<div class="span12">
				<form class="form-horizontal" id="form">
					<input type="hidden"  id="templateId">
					<div class="control-group">
						<label class="control-label" for="clientId"><span class="red">*</span>用户账号：</label>
						<div class="controls select2">
							<select name="clientid" id="clientId">
							</select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="testChannel">测试通道：</label>
						<div class="controls select2" >
							<select  id="testChannel"  onchange="gradeChange()"></select>
                            <input type="hidden" name="channelid" id="channelid">
                        </div>
						<label class="control-label operator" for="operator">运营商：</label>
						<div class="controls select2" >
							<input class="operator" type="text" style="display: none"  id="operator"  readonly>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="red">*</span>测试手机号码：</label>
						<div class="controls">
							<textarea name="mobile" placeholder="请输入与通道运营商匹配的测试手机号码" id="test_phone" style="width: 400px;height: 50px"></textarea>
							<p>若有多个手机号码，请用英文逗号分隔</p>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">扩展位数：</label>
						<div class="controls">
							<input type="text" name="extend" id="extend" placeholder="最多可输入12位数字">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="red">*</span>短信类型：</label>
						<div class="controls js-radio-group">
							<label class="radio inline">
								<input type="radio" name="smstype" value="4" class="js-radio js-yzm"> 验证码
							</label>
							<label class="radio inline">
								<input type="radio" name="smstype" value="0" class="js-radio" checked="checked"> 通知
							</label>
							<label class="radio inline">
								<input type="radio" name="smstype" value="5" class="js-radio"> 会员营销
							</label>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="red">*</span>测试短信内容：</label>
						<div class="controls" id="textPosition">
							<textarea name="content"  id="temp_content" onkeyup="countChar('temp_content','shuRu')"></textarea>
                            <div class="staticsBox">
                                <span id="shuRu">500</span>/<span id="total">500</span>
                            </div>
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<button type="button"  class="btn btn-primary btn-small js-save">测试</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<button  class="btn btn-small   reset ">重置</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script type="text/javascript">
        $("#temp_content").val("【云之讯】尊敬的用户，感谢您对我司的关注。（仅供参考）");

        function countChar(textareaName, spanName) {
            var count = 500 - document.getElementById(textareaName).value.length;
            var textareaCount = document.getElementById(textareaName).value.length;
            document.getElementById(spanName).innerHTML = document.getElementById(textareaName).value.length;
            var textStr = document.getElementById(textareaName).value;

        }

        countChar('temp_content','shuRu');


            $("input[name=smstype]").click(function () {
                showCont();
            });

            function showCont() {

                switch ($("input[name=smstype]:checked").val()) {
                    case  "4":
                        $("#temp_content").val("【云之讯】您好，您的验证码是：1234");
                        countChar('temp_content','shuRu');
                        break;
                    case "0":
                        $("#temp_content").val("【云之讯】尊敬的用户，感谢您对我司的关注。（仅供参考）");
                        countChar('temp_content','shuRu');
                        break;
                    case "5":
                        $("#temp_content").val("【云之讯】短信产品包全新上市，预定优惠多多");
                        countChar('temp_content','shuRu');
                        break;
                    default:
                        $("#temp_content").val("【云之讯】尊敬的用户，感谢您对我司的关注。");
                        break;
                }
            }

//		$(".reset").click(function () {
//                location.reload()
//            });
        $(function () {
            channel()
            getTestClientid();

            function getTestClientid() {
                $.ajax({
                    url : "${ctx}/smsaudit/getTestClientid",
                    dataType : 'json',
                    async : false,
                    success : function(data){
                        var htmlStr = '<option value="">请选择</option>';
                        for(var i = 0; i < data.length; i++){
                            htmlStr += '<option value="'+data[i]["clientid"]+'">'+data[i]["clientid"]+'</option>'
                        }
                        $("#clientId").html(htmlStr);
                    }
                });
            }
            function channel() {
                var select2Date;
                $.ajax({
                    url : "${ctx}/channel/list",
                    dataType : 'json',
                    async : false,
                    success : function(data){
                        var res = data.data;
                        for(var i = 0; i < res.length; i++){
                            res[i].id = res[i].operatorstype + "-" + res[i].cid;
//                            res[i].text = res[i].channelname;
                            res[i].text = res[i].cid + "（" + res[i].channelname + "）";
                        }
                        res.unshift({id:"-",text:"请选择"});
                        select2Date = res;
                    }
                });

                $("#testChannel").select2({
                    data : select2Date
                })
            }


        });



        //通道对应的运营商
        function gradeChange(){
            var objS = document.getElementById("testChannel");
            var grade = objS.options[objS.selectedIndex].getAttribute('value');

            channelid = grade.split('-')[1];
            grade = grade.split('-')[0];
            $("#channelid").val(channelid);
            if(grade == 0 && grade != ''){
                $(".operator").show();
                $("#operator").val("全网");
			}else if(grade == 1){
                $(".operator").show();
                $("#operator").val("移动");
			}else if(grade == 2){
                $(".operator").show();
                $("#operator").val("联通");
            }else if(grade == 3){
                $(".operator").show();
                $("#operator").val("电信");
            }else if(grade == 4){
                $(".operator").show();
                $("#operator").val("国际");
            }else  if(grade == ''){
                $(".operator").hide();
            }else {
                $(".operator").hide();
            }
        }

        //测试
        $(".js-save").click(function(){
            var clientId = $.trim($("#clientId").val());
            var extend = $.trim($("#extend").val());
            var mobile = $.trim($("#test_phone").val());
            var extend_length = extend.length*1;
            var content =  $.trim($("#temp_content").val());
            var operator =  $.trim($("#operator").val());
            var content_length = content.length*1;
            var objS = document.getElementById("testChannel");
            var grade = objS.options[objS.selectedIndex].getAttribute('value');
            if(grade !=null){
                grade = grade.split('-')[0]
			}
            var mobileArr = mobile.split(',');
            if(!clientId){
                parent.layer.msg("请选择用户账号", {icon:2});
                return false;
            }
            if(!mobile){
                parent.layer.msg("请输入手机号", {icon:2});
                return false;
			}
            if(grade != 4 && grade != ''){
                if(!/^(\d+,)*\d+$/.test(mobile)){
                    parent.layer.msg("多个手机号需用英文逗号分隔", {icon:2});
                    return false;
                }
                for(var i=0;i<mobileArr.length;i++){
                    if(!(/^1[34578]\d{9}$/.test(mobileArr[i]))){
                        parent.layer.msg("手机号码有误，请重填", {icon:2});
                        return false;
                    }
                    if(grade != 0 && grade!= getMobileOperator(mobileArr[i])){
                        parent.layer.msg("请输入可接收通道运营商短信的手机号码", {icon:2});
                        return false;
                    }
                }

            }else if(grade == ''){
                if(!/^(\d+,)*\d+$/.test(mobile)){
                    parent.layer.msg("多个手机号需用英文逗号分隔", {icon:2});
                    return false;
                }
                for(var i=0;i<mobileArr.length;i++){
                    if(!(/^1[34578]\d{9}$/.test(mobileArr[i]))){
                        parent.layer.msg("手机号码有误，请重填", {icon:2});
                        return false;
                    }
                }

            }else if(grade ==4){
                for(var i=0;i<mobileArr.length;i++){
                    if(!(/^0{2}\d{8,18}$/.test(mobileArr[i])) ){
                        parent.layer.msg("手机号码有误，请重填", {icon:2});
                        return false;
                    }
                    if(grade != 0 && grade!= getMobileOperator(mobileArr[i])){
                        parent.layer.msg("请输入可接收通道运营商短信的手机号码", {icon:2});
                        return false;
                    }
                }
            }

            if(extend_length > 12 ||!/^[0-9]*$/i.test(extend)){
                parent.layer.msg("扩展位数最多为12位的纯数字", {icon:2});
                return false;
            }
            if(content_length > 500){
                parent.layer.msg("测试短信内容长度超过500", {icon:2});
                return false;
            }

            if(!/^(【[^\n]{2,12}】[^【】]{1,496})|([^【】]{1,496}【[^\n]{2,12}】)$/.test(content)){
                parent.layer.msg("测试短信内容需包含签名+内容", {icon:2});
                return false;
            }

            var params = $("#form").serialize();
            $.ajax({
                url : "${ctx}/smsaudit/testSendAction",
                dataType : 'json',
				data:params,
				type:'POST',
                async : false,
                success : function(data){
                    if(data.success!=null && data.success == true ){
                        parent.layer.msg("测试短信下发成功，请到客户发送记录表查询结果", {icon:1});
                    }else {
                        parent.layer.msg(data.msg, {icon:2});
                    }
                }
            });


        })

	</script>
</body>
</html>