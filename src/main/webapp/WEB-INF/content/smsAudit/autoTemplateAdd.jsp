<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>智能模板</title>
<style type="text/css">
	body{
		background-color: #F8F8F8;
	}
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
</style>
</head>

<body menuId="283">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<form class="form-horizontal" id="form">
					<input type="hidden" name="templateId" id="templateId">
                    <div class="control-group">
                        <label class="control-label" for="adminNAme">创建者：</label>
                        <div class="controls select2">
                            <select  id="adminNAme" style="display: none;">
                            </select>
                            <input type="text"  id="userName" value="${sessionScope.LOGIN_USER_REALNAME}"  style="display: none;" readonly>
							<input type="text"  id="userName1" value="${sessionScope.LOGIN_USER_REALNAME}"  style="display: none;" readonly>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="submit_type">提交来源：</label>
                        <div class="controls select2">
							<input type="text" name="admin1" id="admin1" style="display: none;" >
                            <select  id="submit_type" style="display: none;"></select>
                            <input type="text"  id="submit_type1"  style="display: none;" readonly>
							<input type="hidden" id ="submitType" name="submitType">
                        </div>
                    </div>
					<div class="control-group">
						<label class="control-label" for="clientId"><span class="red">*</span>用户账号</label>
						<div class="controls select2">
							<select name="clientId" id="clientId" style="display: none;"></select>
                            <input type="text" name="clientId" id="clientId1" style="display: none;" readonly>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="sign"><span class="red">*</span>短信签名</label>
						<div class="controls">
							<input type="text" id="sign" name="sign"  placeholder="限2-12个字，中文、英文和数字">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><span class="red">*</span>模板属性</label>
						<div class="controls js-radio-group">
							<label class="radio inline">
								<input type="radio" name="smsType" value="10" class="js-radio js-yzm" > 行业
							</label>
							<label class="radio inline">
								<input type="radio" name="smsType" value="11" class="js-radio"> 营销
							</label>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">
							<span class="hover-tip">
								<img src="${ctx}/img/icon-tip.gif" alt="">
								<div class="tips">
									<p>固定模板请勿使用特殊符号，例如"[]","【】"</p>
									<p>变量模板中的变量请使用"{}"表示，模板中至少包含一个变量，请勿使用特殊符号，例如"[]","【】"</p>
								</div>
							</span>
							<span class="red">*</span>模板类型
						</label>
						<div class="controls">
							<select name="templateType" class="template">
								<option value="1">变量模板</option>
								<option value="0">固定模板</option>
							</select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">示例</label>
						<div class="controls">
							<span class="example"></span>
						</div>
					</div>
					<div class="control-group">
                        <div style="float: left">
                            <label class="control-label"><span class="red">*</span>模板内容</label>
                            <div class="controls">
                                <textarea name="content"  placeholder="请输入模板内容&#10;模板内容、短信签名的总长度不可超过500个字" id="temp_content"></textarea>
                            </div>
                        </div>
						<div style="float: right;" id="trueSms" >
                            <label class="control-label">真实短信对比</label>
                            <div class="controls" >
                                <div id="smsContent"   style="width: 390px;height: 310px; border: 1px solid;padding: 0px;cursor: not-allowed"></div>
                            </div>
                        </div>
                    </div>
                    <div class="control-group" id="checkOut">
                        <label class="control-label"><span class="red">*</span>审核结果</label>
                        <div class="controls js-radio-group">
                            <label class="radio inline">
                                <input type="radio" name="state" value="1" class="audit-radio "> 审核通过
                            </label>
                            <label class="radio inline">
                                <input type="radio" name="state" value="3" class="audit-radio"> 审核不通过
                            </label>
                        </div>
                    </div>
                    <div class="control-group" id="auditNoPass" style="display: none">
                        <label class="control-label" for="noPass"><span class="red">*</span>审核不通过原因</label>
                        <div class="controls">
                            <input type="text" id="noPass" name="remark" placeholder="请填写不通过原因">（最多可输入15个中文字符）
                        </div>
                    </div>
					<div class="control-group">
						<div class="controls">
							<input type="hidden" id ="contenta" value="${data.content}">
							<%--<button type="submit" class="btn">Sign in</button>--%>
							<a href="javascript:;" class="btn btn-primary btn-small js-save">确定</a>
								<a href="javascript:;" class="btn btn-small js-back">取消</a>
						</div>

					</div>
				</form>
			</div>
		</div>
	</div>

	<script type="text/javascript">
	//不同模板不同短信类型，示例不同
	//key的第一位代表短信类型，第二位代表模板类型，验证码短信没有固定模板
	var map = {
		"101" : " 验证码示例：您的验证码是{}，如非本人操作 ，请忽略此条短信。\r\n \r\n \r\n \r\n \r\n \r\n通知示例：尊敬的{}用户，您的账户已入账{}元，请及时查收，如有疑问请联系客服热线。",
		"100" : " 因受台风天鸽影响，8月24日全市人民放假一天，请市民注意人身安全。",
		"110" : " 欢迎您成为酒店集团会员，我们将竭诚为您服务。如非本人操作，请致电如家客服热线。",
		"111" : " 尊敬的{}用户，本店将于下周举行年中大促，退订回复TD。"
	};

	$(function(){
	    var clientId = getQueryString("clientId");
	    var menuId = getQueryString("menuId");
        var select2Date;

	    if(clientId != "" && clientId != undefined && clientId != null){
	        //如果是编辑页面
            $("#clientId").remove();
            $("#clientId1").show();
            $("#userName").show();
            $("#submit_type1").show();
            $("#checkOut").show();
			insertData();
		} else{
            $("input[name='smsType']:eq(0)").attr('checked',true);
            $("input[name='smsType']:eq(0)").closest("span").addClass("checked")
         //   console.log($("input[name='smsType']:eq(0)"));

			//修改示例
            draw("10", "1");


            //如果是新增模板页面
            $("#clientId1").remove();
            $("#checkOut").remove();
            $("#clientId").show();
            $("#userName").show();
            $("#submit_type1").show();
            $("#trueSms").hide();
            $("#submit_type1").val('平台提交');


            //获取用户账号
            $.ajax({
                url : '${ctx}/smsaudit/autoTemplate/accounts',
                dataType : 'json',
                async : false,
                success : function(res){
                    for(var i = 0; i < res.length; i++){
                        res[i].id = res[i].clientid;
                        res[i].text = res[i].clientid + "-" + res[i].name;
                    }
                    res.unshift({id:"",text:""})
                    select2Date = res;
                }
            })
            $("#clientId").select2({
                data : select2Date
            })

        }
        if(menuId == 82){
            $("input[name='smsType']:eq(0)").attr('checked',true);
            $("input[name='smsType']:eq(0)").closest("span").addClass("checked");
            //修改示例
            draw("10", "1");


            // var radio1 = $(".js-radio-group").find(".js-radio");
            var content2 = decodeURI(getQueryString("content")).replace(/[\r\n]/g,"")//去掉回车换行;
            var sign2 = decodeURI(getQueryString("sign"));

            $("#checkOut").remove();
            $("#submit_type1").val('平台提交');
            $("#userName").val($("#userName1").val());
            $("#sign").val(sign2);
            $("#temp_content").val(content2);
        //    radio1[0].checked = true;
        }


        //选择审核结果
        $(".audit-radio").click(function(){
            var value = $(this).val();
            if(value == 1){
                $("#auditNoPass").hide();
            }else {
                $("#auditNoPass").show();
            }


        });


		//选择短信类型
		$(".js-radio").click(function(){
			var value = $(this).val();
			var templateType = $(".template").val();
			draw(value, templateType);


		})
		//选择模板类型
		$(".template").change(function(){
			var template = $(this).val();
			var smstype = getSmsType();



			draw(smstype, template);
		})
	    //提交
		$(".js-save").click(function(){
            $("#temp_content").val($("#temp_content").val().replace(/[\r\n]/g,""));//去掉回车换行
          //  $("#temp_content").val($("#temp_content").val().replace(/[ ]/g,""));    //去掉空格
            var sign = $.trim($("#sign").val());
            var sign_length = sign.length*1;
            var content =  $.trim($("#temp_content").val());
            var content_length = content.length*1;
            var templateType = $(".template").val();
            var auditContent =  $.trim($("#noPass").val());
            var auditContent_length = auditContent.length*1;

            var isChineseSms = true;



            if($("input[name='state']:radio:checked").val() == 1 || !(clientId != "" && clientId != undefined && clientId != null)){
                if(sign_length > 12 || sign_length < 2){
                    parent.layer.msg("签名长度必须在2-12之间", {icon:2});
                    return false;
                }
                if(!/^([a-z0-9\u4E00-\u9FA5])+$/i.test(sign)){
                    parent.layer.msg("签名格式为中文或者英文字母或者数字", {icon:2});
                    return false;
                }
                if((sign_length + content_length) > 500 ){
                    parent.layer.msg("模板内容+签名长度超过500", {icon:2});
                    return false;
                }

                if(/([\u4E00-\u9FA5]|[\（\）\《\》\——\；\，\。\“\”\！\【\】])/.test(content)){
                    //中文短信
                    if(content.indexOf('【') != -1 || content.indexOf('】') != -1 || content.indexOf('【】') != -1){
                        parent.layer.msg("中文短信模板内容不能包含【,】和【】", {icon:2});
                        return false;
                    }
                } else {
                    if(content.indexOf('[') != -1 || content.indexOf(']') != -1 || content.indexOf('【') != -1 || content.indexOf('】') != -1){
                        parent.layer.msg("英文短信模板内容不能包含[,]和[]", {icon:2});
                        return false;
                    }
                }

                var placeHolderReg0 = RegExp("\\{.*?\\}","g");
                //0是固定模板 1是变量模板
                if(templateType == 0 && placeHolderReg0.test(content)){
                    parent.layer.msg("固定模板中不能包含'{}'", {icon:2});
                    return false;
                }
            }
            if($("input[name='state']:radio:checked").val() == 3){
                if(auditContent_length> 15){
                    parent.layer.msg("不通过原因最多可输入15个字符", {icon:2});
                    return false;
                }
                if(auditContent_length < 0){
                    parent.layer.msg("请填写不通过原因", {icon:2});
                    return false;
                }
            }
            var index = layer.confirm('确认提交编辑的模板记录？', {
                btn: ['确定','返回修改'] //按钮
            }, function(){
                var params = $("#form").serialize();
                var _p = parent;
                $.ajax( {
                    type : "POST",
                    url : "${ctx}/smsaudit/autoTemplate/save",
                    data : params,
                    success : function(res) {
                        if(res.code == 500 && res.msg == '已存在相同类型的智能模板，已自动删除'){
                            parent.layer.msg(res.msg, {icon:2,time: 1500});
                            /*parent.layer.msg(res.msg, {icon:2,time: 1500},function () {
//                                parent.layer.closeAll();
                                setTimeout(function () {
                                    _p.location.reload();
                                },2000)

//                                return;
                            })*/

                        }
                        if(res.code == 500){
                            parent.layer.msg(res.msg, {icon:2,time: 1500})
                            layer.close(index);
                            return;

                        }
                        if(res.code != 0){
                            parent.layer.msg(res.msg, {icon:2})
                            return;
                        }

                        parent.layer.msg(res.msg, {icon: 1,time: 1500});
                        /*parent.layer.msg(res.msg, {icon: 1,time: 1500},function(){
                            _p.location.reload();
                        });*/
                        closePage();
//                        parent.window.location.reload();

                    }
                });
            },function () {
				layer.close(index)
            });


            <%--var options = {--%>
                <%--url: "${ctx}/smsaudit/autoTemplate/save",--%>
                <%--success: function () {--%>
                    <%--alert("ajax请求成功");--%>
                <%--}--%>
            <%--};--%>
            <%--$("#form").ajaxForm(options);--%>
		})
		//取消
		$(".js-back").click(function(){
            closePage()
		})
		/*
		 * @params s  smstype
		 * @params t templateType
		  * */
		function draw(s, t){
			var key = s + t;
			$(".example").text(map[key]);
		}
		function getSmsType(){
		    var radio = $(".js-radio-group").find(".js-radio");
		    var smstype;
		    for(var i = 0; i < radio.length; i++){
				if(radio[i].checked){
                    smstype = radio[i].value;
                    break;
				}
			}

			return smstype;
		}





        function closePage() {
            var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
            parent.layer.close(index); //执行关闭
        }

        function getQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var url = decodeURI(window.location.search);
            var r = url.substr(1).match(reg);
            if (r != null) return unescape(r[2]); return "";
        }

        function insertData(){
			var userName = decodeURI(getQueryString("userName"));
			var submitType = decodeURI(getQueryString("submitType"));
			var clientId = decodeURI(getQueryString("clientId"));
			var templateId = decodeURI(getQueryString("templateId"));
			var smsType = decodeURI(getQueryString("smsType"));
	//		var sign = decodeURI(getQueryString("sign"));
	//		var content = decodeURI(getQueryString("content"));
			var sign="${data.sign}";
            var content=$("#contenta").val().replace(/[\r\n]/g,"");
			var templateType = decodeURI(getQueryString("templateType"));
			var smsContent="${data.smsContent}";
		//	var smsContent = decodeURI(getQueryString("smsContent"));
            $("#userName").val(userName);
            if(submitType == '系统自动提交' ){
                $("#submitType").val(3);
                $("#userName").val("-");
                $("#trueSms").show();
                var arr = content.split("{}")
				var smsstr = smsContent;


                for(var i = 0; i<arr.length;i++){
                    smsstr = smsstr.replace(arr[i],'||')

                }
                var result = smsstr.split('||')
                console.log(result);
                var smsContent_ = smsContent
                var ret = [];

				for (var i = 0, j = result.length; i < j; i++) {
                       if (ret.indexOf(result[i]) === -1) {
								ret.push(result[i]);
                      }
				}
                console.log(ret);
                for(var j = 0; j<ret.length;j++){
                    var b = ret[j];
                    if(ret[j] != ''){
                        smsContent_ = smsContent_.replace(b,'<span style="color: red">'+b+'</span>')
                    }
                }
                console.log(smsContent_);
                $("#smsContent").html(smsContent_);

            }else if(submitType == '平台提交'){
                $("#submitType").val(2);
                $("#trueSms").hide();
            }else if(submitType == '代理商提交'){
                $("#submitType").val(1);
                $("#trueSms").hide();
            }else if(submitType == '客户提交'){
                $("#submitType").val(0);
                $("#trueSms").hide();
            }

            $("#submit_type1").val(submitType);
            $("#clientId1").val(clientId);
			$("#templateId").val(templateId);
			$(".template").val(templateType);
			$("#temp_content").val(content);
			$("#sign").val(sign);

            var radio = $(".js-radio-group").find(".js-radio");
            for(var i = 0; i < radio.length; i++){
                radio[i].checked = false;
                $(radio[i]).closest("span").removeClass("checked");
                if(radio[i].value == smsType){
                    radio[i].checked = true;
                    $(radio[i]).closest("span").addClass("checked");
					break;
                }
            }
			//修改示例
			draw(smsType, templateType);
        }

        if(clientId != "" && clientId != undefined && clientId != null){
            //如果是编辑页面
            $('#clientId').on('select2:select', function (evt) {
                var clientId = evt.params.data.clientid || "";
                var radio = $(".js-radio-group").find(".js-radio");
                var templateType = $(".template").val();


                $.ajax( {
                    type : "GET",
                    url : "${ctx}/smsaudit/getAccountSmsTypeByClientId",
                    data : {
                        clientId : clientId
                    },
                    success : function(res) {
                        console.log(res);
                        if(res != null){


                            for(var i = 0; i < radio.length; i++){
                                radio[i].checked = false;
                                radio[i].disabled = true;
                                $(radio[i]).closest("span").removeClass("checked");
                                if(radio[i].value == res){
                                    radio[i].disabled = false;
                                    radio[i].checked = true;
                                    $(radio[i]).closest("span").addClass("checked");
                                    draw(res, templateType);
                                }
                            }
                        } else {
                            for(var i = 0; i < radio.length; i++){
                                radio[i].checked = false;
                                radio[i].disabled = false;
                                $(radio[i]).closest("span").removeClass("checked");

                            }
                        }
                    }
                });
            });
        }

    });

    //@ sourceURL=autoTemplaterAdd.js
	</script>
</body>
</html>