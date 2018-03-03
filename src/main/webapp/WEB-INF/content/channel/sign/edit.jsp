<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}固签签名扩展号</title>


  <style type="text/css">
    .hide {
      display: none;
    }
    #clientIdDiv .el-select{
      width: 700px;
    }

    #clientIdDiv .el-select .el-tag {
      height: 32px;
      line-height: 32px;
      box-sizing: border-box;
      margin: 3px 0 3px 6px;
    }

    #clientIdDiv .controls span {
      display: inline-block;
      font-size: 14px;
      margin-top: 0px;
    }

    #clientIdDiv .el-input__inner{
      color: #fff;
    }
    #clientIdDiv input[readonly]{
      background-color: #fff;
    }
    #clientIdDiv .el-select__tags input[type="text"]{
      border: none;
      background-color: rgba(255,255,255,0);
      outline:none !important;
      padding: 0px 0px 0px 0px;
    }

    #clientIdDiv .el-tag--primary {
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

</head>
<body menuId="47">
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
                <label class="control-label">通道选择</label>
                <div class="controls">
                	<s:if test="data.channelid == null">
		                <u:select id="channelid" sqlId="channel" sqlParams="{isWithSign:1}" placeholder="通道号" excludeValue="" value="${data.channelid}"></u:select>
                	</s:if>
                	<s:else>
                		<input id="channelid" type="text" name="channelid" value="${data.channelid}" readonly="readonly"/>
                	</s:else>
                <span class="help-block">* 适用于固签无扩展通道和固签有扩展通道</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">短信签名</label>
                <div class="controls">
                  <input type="text" name="sign" value="${data.sign}" class="checkSign checkSign2"/>
                </div>
            </div>
              
            <div class="control-group">
                <label class="control-label">扩展号</label>
                <div class="controls">
                  <input type="text" name="appendid" value="${data.appendid}" class="checkAppendid"  onfocus="inputControl.setNumber(this, 20, 0, false)"  />
                  <span class="help-block">* 1-20位数字；为空时表示用户占用整个通道的端口</span>
                </div>
            </div>
              

              
              <div id="clientIdDiv" class="control-group">
                <label class="control-label">子账号</label>
                <div class="controls">
                  <el-select v-model="value1"  id="clientid" name="clientid" multiple filterable  size="large" placeholder="请选择"  >
                    <el-option
                            v-for="item in options1"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                  </el-select>
                  <span class="help-block">*注意：当输入多个账户时将不支持上行接收。</span>
                </div>
              </div>

              <div class="control-group">
                <label class="control-label">计费号码</label>
                <div class="controls">
                  <input type="text" name="fee_terminal_id" value="${data.fee_terminal_id}" />
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                  <input type="text" name="username" value="${data.username}" maxlength="20"/>
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
                <input type="hidden" name="nid" value="">
                <input type="button" onclick="save(this);" value="保  存" class="btn btn-success">
                <input type="button" value="取 消" class="btn btn-error" onclick="back()"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>


<!-- 引入element-ui组件库 -->
<script src="${ctx}/js/element-ui/vue.min.js"></script>
<script src="${ctx}/js/element-ui/element-ui.js"></script>
<script type="text/javascript">
var validate;
var clientid="${data.clientid}";
var nclientids;
//alert(clientid);
// vue实现多选通道
$(function(){

    $($("input[name='appendid']")[0]).blur(function adddata(){
        console.log($("input[name='appendid']").val());

        $.ajax({
            type : "post",
            async : false,
            url : "${ctx}/sign/channelAppendIdCheck2",
            data : {
                id : $("input[name='id']").val(),
                channelid : $("#channelid").val(),
                appendId : $("input[name='appendid']").val(),
                sign:$("input[name='sign']").val()

            },
            success : function(result) {

                if (result != null && result.datalist != null ) {
                    console.log("已进入判断");
                    console.log("appendid onlur...");
                    $("input[name='nid']").val(result.datalist.id);
                    $("input[name='username']").val(result.datalist.username);
                    $("input[name='fee_terminal_id']").val(result.datalist.fee_terminal_id);

                    nclientids =result.datalist.clientid;

                    $("input[name='clientid']").val(nclientids.split(","));
                    vm.value1=nclientids.split(",");

                }
            }
        });
    });



    vm = new Vue({
      el : "#clientIdDiv",
      mounted: function(){
          var that = this;
          $.ajax({
              type : "post",
              async: false,
              url : "${ctx}/sign/accounts",
              success : function(data) {
                  if (data != null) {
                      that.options1 = data.clientData;


                      if(clientid.trim().length > 0){

                              that.value1 = clientid.split(",");
                              $("input[name='clientid']").val(clientid.split(","));

                      }
  //					console.log("查询用户clientId：" + data.clientData)
                  }else{
                      console.log("查询用户clientId出错！");
                  }
              }
          });
      },
      data : {
          options1 : [],
          value1: ''

      },
      watch : {
          value1 : function(value){
              this.$nextTick(function(){
                  $("input[name='clientid']").val(value);
  // 				console.log("watch value1 :" + $("input[name='channelid']").val());
                if(value.length==2){
                    layer.msg("支持多个账户时候将不支持上行接收，谨慎使用！", {icon:5});
                }


              })
          }
      }
  });

	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			channelid: {
				required : true,
				channelAppendIdCheck1 : true
			},
			sign: "required",
			appendid: {
				channelAppendIdCheck2 : true,
				channelAppendIdCheck3 : true
			},
			username:{
				required : true,
				maxlength : 60
			},

            fee_terminal_id : {
                maxlength : 21
            }
		}
	});
	
//	// 校验子账号是否合法
//	jQuery.validator.addMethod("checkClientIdAndSid",
//			function(value, element) {
//				var reg_clientId = /^[a-zA-Z0-9]{6}$/;
//				if(value.length == 6){
//					return this.optional(element) || (reg_clientId.test(value));
//				}
//
//				return false;
//			}, "请输入合法的6位子账号");
	
	// 短信签名长度校验
	jQuery.validator.addMethod("checkSign",
			function(value, element) {
				return this.optional(element) || (value.length <= 12);
			}, "签名必须小于等于12个字");
	
//	// 同一通道下签名不唯一 	（channelid + sign + appendid）相同时，clientid可以存在多个，此时可能会导致上行错乱
//	jQuery.validator.addMethod("checkSign2",
//			function(value, element) {
//				return this.optional(element) || (checkSignExist() == null);
//			}, "当前通道下已经存在该短信签名");
	
	// 同一通道下扩展号唯一 
//	jQuery.validator.addMethod("checkAppendid",
//			function(value, element) {
//				return this.optional(element) || (checkAppendIdExist() == null);
//			}, "当前通道下已经存在该扩展号");

	// 在相同channelid下，若存在clientid有值，并且appendid为空记录时，则表示此通道已分配给该用户；此时，不能再把此channelid的端口分配给其他用户
	jQuery.validator.addMethod("channelAppendIdCheck1",
			function(value, element) {
				var checkResult = true;
				$.ajax({
			 		type : "post",
			 		async : false,
			 		url : "${ctx}/sign/channelAppendIdCheck1",
			 		data : {
			 			id : $("input[name='id']").val(),
			 			channelid : value
			 		},
			 		success : function(result) {
			 			if (result != null && result.success != null) {
			 				checkResult = result.success;
			 			}
			 		}
			 	});
				return this.optional(element) || checkResult;
			}, "该通道已唯一分配给用户，请选择其他通道");
	
	// 针对固签有扩展通道：在相同channelid+sign下，appendid唯一且前缀不能等于其它appendid
	jQuery.validator.addMethod("channelAppendIdCheck2",
			function(value, element) {
				var checkResult = true;
				$.ajax({
			 		type : "post",
			 		async : false,
			 		url : "${ctx}/sign/channelAppendIdCheck2",
			 		data : {
			 			id : $("input[name='id']").val(),
			 			channelid : $("#channelid").val(),
			 			sign:$("input[name='sign']").val(),
			 			appendId : value
			 		},
			 		success : function(result) {
			 			if (result != null && result.appendIdUsable != null ) {
			 				checkResult = result.appendIdUsable;
			 			}

			 		}
			 	});
				return this.optional(element) || checkResult;
			}, "同一通道下扩展前缀重复");
	
	jQuery.validator.addMethod("channelAppendIdCheck3",
			function(value, element) {
				var check = true;
				if($.trim(value) === ""){
					$.ajax({
				 		type : "post",
				 		async : false,
				 		url : "${ctx}/sign/channelAppendIdCheck3",
				 		data : {
				 			id : $("input[name='id']").val(),
				 			channelid : $("#channelid").val()
				 		},
				 		success : function(result) {
				 			if (result != null && result.success != null) {
				 				check = result.success;
				 			}
				 		}
				 	});
				}
				return check;
			}, "该通道已存在分配扩展号的用户，不能设置该用户为唯一分配用户");
	
});



function save(btn){
	$("#msg").hide();
    $("#clientid-error").remove();
    var clientIds=vm.value1;
 //   alert(clientIds);
    //用户选择校验，不能为空
    var clientIdIsEmpty=false;
    if(clientIds.length == 0){
 //       alert(clientIds);
        clientIdIsEmpty=true;
    }
    if(clientIdIsEmpty){
//        alert(clientIdIsEmpty);
        if($("#clientid-error").length == 0){
            $('<label id="clientid-error" style="color:red;">请选择子账号</label>').insertAfter( $("#clientIdDiv").find(".el-select"))
        }
        if(validate.form()){
            return;
        }
    }



	if(!validate.form()){
		return;
	}

    if(checkAppendIdExist()){
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
            back();
		},
		url : "${ctx}/sign/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};


<%--function checkSignExist(){--%>
	<%--var id = "${data.id}";--%>
	<%--var sign = $("input[name='sign']").val();--%>
	<%--var channelid = $("#channelid").val();--%>
    <%--var appendid = $("input[name='appendid']").val();--%>
	<%--var result;--%>
	<%--$.ajax({--%>
		<%--type : "post",--%>
		<%--async: false,--%>
		<%--url : "${ctx}/sign/checkSignExist",--%>
		<%--data : {--%>
			<%--id : id,--%>
			<%--channelid : channelid,--%>
            <%--appendid:appendid,--%>
			<%--sign : sign--%>
		<%--},--%>
		<%--success : function(data) {--%>
			<%--result = data;--%>
		<%--}--%>
	<%--});--%>
	<%--return result;--%>
<%--}--%>


function checkAppendIdExist(){
	var id = "${data.id}";
	var appendid = $("input[name='appendid']").val();
	var channelid = $("#channelid").val();
    var sign = $("input[name='sign']").val();
    var clientIds=vm.value1;
	var result=true;
	$.ajax({
		type : "post",
		async: false,
		url : "${ctx}/sign/checkAppendIdExist",
		data : {
			id : id,
			channelid : channelid,
			appendid : appendid,
            clientIds:clientIds,
            sign : sign
		},
		success : function(data) {
		    if(data.checked==true){

                parent.layer.msg(data.msg, {icon:5});
                result= true;
            }else {
                result= false;
            }

		}
	});
	return result;
}






</script>
</body>
</html>