<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.operater==null?'添加':'修改'}通道运营商号段表</title>
</head>
<body menuId="77">
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
                <label class="control-label">运营商类型</label>
                <div class="controls">
                	<u:select id="operater" value="${data.operater}" placeholder="运营商类型" dictionaryType="channel_operatorstype"  />
                	<input type="hidden" value = "${data.operater}" id="operaterold">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">可发号段</label>
                <div class="controls">
                	<textarea name="numbersegment" placeholder="多个关键词用,逗号隔开" id="numbersegment">${data.numbersegment}</textarea>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                	<input type="text" name="remarks" id="remarks" value="${data.remarks}" maxlength="50"></input>
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
$(function(){
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			operater: "required",
			numbersegment: {
				num_dot_split:true,
				maxlength:200,
				required:true
			},
			remarks: "required"
		}
	});
});

$(function(){
	$("#operater option[value='0']").remove(); 
	$("#operater option[value='4']").remove();
})

function save(btn){
	var selected =$("#operater").find("option:selected").text(); 
	$("#msg").hide();
	if(!validate.form()){
		return;
	}
	var flag = true ;
 	var segArr = $('#numbersegment').val();
 	var s = segArr.split(',').sort();
	for(var i=0 ; i<s.length ; i++){ 
		if (s[i]==s[i+1]){ 
			flag = false ;
		}  
	} 
	if(!flag){
		$("#msg").text("可发号段中有重复内容").show();
		return ;
	}
	
	var index = layer.open({
		 title: '您确定要提交这些更改吗',
	     area: ['390px', '330px'],
	     shade: 0,
	     btn: ['确定', '关闭'],
	     content:'<font color="red">错误的配置将导致系统无法正常运行，请确认！</font>'+
	     '<div>您选择的运营商类型是：<br><font color="red">'+selected+'</font></div>'+
	     '可发号段：<br/><font color="red">'+segArr+'</font>',
	     yes: function(){
	    	 submitData(btn);
	    	 layer.close(index);
	     }
		});
};

	function repeatCheck(){
		var str = $("#numbersegment").val();
	}

	function submitData(btn){
		var operaterold = $("#operaterold").val();
		var operater = $("#operater").val();
		var url;
		if(operaterold == null || operaterold == ''){
			url = "${ctx}/operatorSegment/save";
		}else{
			url = "${ctx}/operatorSegment/update?operaterold=" + operaterold;
		}
		var options = {
			beforeSubmit : function(data) {
				$(btn).attr("disabled", true);
			},
			success : function(data) {
				$(btn).attr("disabled", false);
				
				if(data.result==null){
					$("#msg").text("服务器错误，请联系管理员").show();
					return;
				}
				if(data.result == 'success'){
				$("#operaterold").val(operater);
				}
				$("#msg").text(data.msg).show();
			},
			url : url,
			type : "post",
			timeout:30000
		};
		$("#mainForm").ajaxSubmit(options);
	}
	
	function toback(){
		window.location.href="${ctx}/operatorSegment/query";
	}
</script>
</body>
</html>