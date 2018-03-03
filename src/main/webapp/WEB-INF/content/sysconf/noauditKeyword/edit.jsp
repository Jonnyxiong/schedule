<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}用户免审关键字</title>
	<style type="text/css">
		#removeDulicateBtn.display-none{
 			display: none;
		}
	</style>
</head>
<body menuId="136">
 <div class="container-fluid">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
          <input type="hidden" value = "${data.clientid}" id="isNotCreate">
<%--           <input type="hidden" value = "${data.clientid}" id="clientidold"> --%>
            <form class="form-horizontal" method="post" action="#" id="mainForm">
              <div class="control-group">
                <label class="control-label">用户帐号</label>
                <div class="controls">
                	<input type="text" value="${data.clientid}" id="clientid" name="clientid">
                	<span class="help-block">将已创建的用户账号记录修改为*的该条关键字记录会自动追加到原有的平台限制级别记录中</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">免审关键字</label>
                <div class="controls">
                <textarea name="keyword" id="keyword" rows="13" cols="120"
							  style="height: auto; width: auto;" >${data.keyword}</textarea>
				<input id="removeDulicateBtn" type="button" value="去重" class="btn btn-warning display-none" onclick="removeDulicate()"/>
				<span class="help-block">多个免审关键字间用分号"|"分隔,如"aaa|bbb"<br>超过4000个字的记录数据库中会按顺序分割保存</span>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                	<input type="text" name="remarks" id="remarks" value="${data.remarks}"></input>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
                <input type="button" value="确定" class="btn btn-success"  class="btn btn-success" onclick="save(this)"/>
             	<input type="button" value="取 消" class="btn btn-error" onclick="toback()"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>


<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
<script type="text/javascript">
var validate;
var clientidCopy = "${data.clientid}"; //clientid副本
var duplicateKeywords = [];

$(function(){
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			clientid: {
				checkClientId: true,
				required: true
			},
			keyword: {
				required: true,
			},
			remarks: {
				maxlength: 128
			},
		}
	});
	
	//校验短信账号是否合法
	jQuery.validator.addMethod("checkClientId",
		function(value, element) {
			var reg_clientId = /^[a-zA-Z0-9]{6}$/; 
			if(value=="*"){
				return true;
			}
			if(value.length == 6){
				return this.optional(element) || (reg_clientId.test(value));
			}
			
			return false;
	}, "请输入合法的6位子账号；输入 * 则表示平台级别限制");
	
	
});

$(function(){
})

function save(btn){
	var isNotCreate = $("#isNotCreate").val();
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	var url;
	if(isNotCreate == null || isNotCreate ==''){
		url = "${ctx}/noauditKeyword/save";
	}else{
		url = "${ctx}/noauditKeyword/update";
	}
	var options = {
		beforeSubmit : function() {
			var keyword = $("#keyword").val();
			/*if(!isNotCreate){
				//新建时是否已经存在的关键字，如果存在关键字则判断是否有重复的关键字
				if(isKeyWordDuplicate(keyword)){
					$("#removeDulicateBtn").css('display','block'); 
					return false;
				}
			}*/
			
			$(btn).attr("disabled", true);
		},
		data:{
			clientidCopy : clientidCopy
		},
		success : function(data) {
			$(btn).attr("disabled", false);
			if(data.result==null){
				$("#msg").text("服务器错误，请联系管理员").show();
				return;
			}
			if(data.result =="success"){
				$("#clientidold").val(clientid);
			}
			$("#msg").text(data.msg).show();
		},
		url : url,
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};

	
function toback(){
	window.location.href="${ctx}/noauditKeyword/query";
}

/**
 * 根据clientid判断clientid是否存在关键字
 * 如果存在关键字需要判断当前页面关键字中是否与已存在的重复并提示
 */
function isKeyWordDuplicate(text){
	var keyWordsStr;
	var clientid = $("#clientid").val();
	// 根据通道id查询通道的关键字
	$.ajax({
		type : "post",
		async : false,
		url : "${ctx}/noauditKeyword/getKeywordsByCid",
		data:{
			clientid : clientid
		},
		success : function(data) {
			if(data != null && data.keyword != null){
				keyWordsStr = data.keyword;
			}else{
				keyWordsStr = "";
			}
		}
	});
	
	var keyWordsArray = keyWordsStr.split("|");
	if(keyWordsArray.length == 0){
		return false;
	}
	var keyWordSerch = new KeyWordSerch(); // 关键字匹配工具
	duplicateKeywords = keyWordSerch.searchDirect(text, keyWordsArray, false);
	var htmlArray = [];
	if(duplicateKeywords.length > 0){
		$("#msg").html("以下关键字已经重复，请点击去重按钮后然后重新保存<br>" + duplicateKeywords.join("、")).show();
		return true;
	}else{
		return false;
	}
	
}

/**
 * 去除重复的关键字
 */
function removeDulicate(){
	var keywordArray = $("#keyword").val().split("|");
	var resultArray = [];
	
	// 生成重复关键字hash表
	var duplicateHash = {};
	for(var pos=0; pos<duplicateKeywords.length; pos++){
		duplicateHash[duplicateKeywords[pos]] = true;
	}
	
	// 去重
	var tempArray = [];
	for(var pos=0; pos<keywordArray.length; pos++){
		var kw = keywordArray[pos];
		if(duplicateHash.hasOwnProperty(kw)){
			// 重复的关键字直接忽略
		}else{
			if(kw.trim() != ""){
				tempArray.push(kw)
			}
		}
	}
	// 如果一个长的关键字中包含短的已经存在的关键字则视为重复
	for(var pos=0; pos<tempArray.length; pos++){
		var kw = tempArray[pos];
		for(var i=0; i<duplicateKeywords.length; i++){
			if(kw.indexOf(duplicateKeywords[i]) != -1){
				
			}else{
				resultArray.push(kw)
			}
		}
	}
	
	// 将去重后的关键字复制给关键字输入框
	$("#keyword").val(resultArray.join("|"));
	$("#removeDulicateBtn").css('display','none'); 
	$("#msg").html("关键字已经去重，请重新点击保存").show();
}

</script>
</body>
</html>