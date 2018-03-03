<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}通道关键字</title>
	<style type="text/css">
		#removeDulicateBtn.display-none{
 			display: none;
		}
	</style>
</head>
<body menuId="63">
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
                <label class="control-label">通道号</label>
                <div class="controls">
                	<input type='text'  name="cid" id="cid" value="<c:out value="${data.cid}"/>" onfocus="inputControl.setNumber(this, 9, 0, false)"></input>
                	<input type='hidden'  name="cid_bak" value="${data.cid}" ></input>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">通道关键字</label>
                <div class="controls">
                	<textarea name="keyword" id="keyword" rows="13" cols="120"
							  style="height: auto; width: auto; display: block;" ><c:out value="${data.keyword}"/></textarea>
					<div id="duplicateDisplay" style="display: none;"></div>
					<%--<input id="removeDulicateBtn" type="button" value="去重" class="btn btn-warning display-none" onclick="removeDulicate()"/>--%>
					<span class="help-block">多个通道关键字间用分号"|"分隔,如"aaa|bbb"</span>
                </div>
              </div>
              <s:if test="data.id != null">
              	<div class="control-group">
                <label class="control-label">状态</label>
                <div class="controls">
                	<s:if test="data.status == 1">
                		<select name="status" id="status"> 
                		<option value="1" selected="selected">正常</option>
                		<option value="7">禁用</option>
                	</select>
                	</s:if>
                	<s:if test="data.status == 7">
                		<select name="status" id="status"> 
                		<option value="1">正常</option>
                		<option value="7" selected="selected">禁用</option>
                	</select>
                	</s:if>
                </div>
               </div>
              </s:if>
              
              <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                	<input type="text" name="remarks" id="remarks" value="<c:out value="${data.remarks}"/>"></input>
                </div>
              </div>
               <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
                <input type="button" value="保 存" class="btn btn-success"  class="btn btn-success" onclick="save(this, '${data.id}')"/>
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
var duplicateKeywords = [];
$(function(){
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			cid: "required",
			keyword: "required",
			remarks: {maxlength:120},
		}
	});
});

function save(btn, id){
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	var url;
	if(id == null){
		url = "${ctx}/channelKeywords/save";//创建
	}else{
		url = "${ctx}/channelKeywords/save?id=" + id;//更新
	}
	var options = {
		beforeSubmit : function() {
			var keyword = $("#keyword").val();
			if(id == null || id == ''){
				//新建时需要判断该通道是否已经存在的关键字，如果存在关键字则判断是否有重复的关键字
//				if(isKeyWordDuplicate(keyword)){
//					$("#removeDulicateBtn").css('display','block');
//					return false;
//				}
			}
			
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
		url : url,
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};

/**
 * 根据通道id判断通道是否存在关键字
 * 如果存在关键字需要判断当前页面关键字中是否与已存在的重复并提示
 */
function isKeyWordDuplicate(text){
	var keyWordsStr;
	var cid = $("#cid").val();
	// 根据通道id查询通道的关键字
	$.ajax({
		type : "post",
		async : false,
		url : "${ctx}/channelKeywords/getKeywordsByCid",
		data:{
			cid : cid
		},
		success : function(data) {
			if(data != null && data.keywords != null){
				keyWordsStr = data.keywords;
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
		/* var p1 = 0;
		var p2 = 0;
		// 根据位置将text中匹配到的关键字标红
		for(var i=0; i<result.length; i++){
			var begin = result[i].begin;
			var end = result[i].end;
			p2 = result[i].begin;
			htmlArray.push(text.substring(p1, p2));
			htmlArray.push("<span style='color:red'>" + text.substring(begin, p1=end) + "</span>");
		}
		htmlArray.push(text.substring(p1));
		
		var duplicateDisplay = $("#duplicateDisplay")[0];
		var keywordTextArea = $("#keyword")[0];
		duplicateDisplay.innerHTML = htmlArray.join(""); */
		
		/* duplicateDisplay.style.display = "block";
		keywordTextArea.style.display = "none"; */
		
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