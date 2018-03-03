<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<s:set var="title_btn" value="#parameters.notice_id==null ? '添 加' : '修 改'"/>
<html>
<head>
	<title>${param.notice_id==null?'添加':'修改'}系统通知时段</title>
</head>
<body menuId="9">
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
              <div class="control-group">
                <label class="control-label">通知时段名称</label>
                <div class="controls">
                   <input type="text" name="name" value="${data.view.name}" maxlength="100"/>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">时段</label>
                <div class="controls">
                 	<u:date id="start_date" value="${data.view.start_date}" placeholder="开始时间" maxId="end_date,-1" dateFmt="HH:mm:ss" startDate="00:00:00" />
					<span>至</span>
			       	<u:date id="end_date" value="${data.view.end_date}" placeholder="结束时间" minId="start_date,1" dateFmt="HH:mm:ss" />
			       	
			       	<label id="start_date-error" class="error" for="start_date" style="display: none;"></label>
			       	<label id="end_date-error" class="error" for="end_date" style="display: none;"></label>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">已添加时段</label>
                <div class="controls">
                  <s:iterator value="data.allTimeRange" status="s">
						<s:set var="test">${notice_id != data.view.notice_id}</s:set>
						<s:if test="#test">
							${name}（${start_date}至${end_date}）<br/>
						</s:if>
					</s:iterator>
                </div>
              </div>
              <div class="control-group control-group-checker">
                <label class="control-label">接收管理员</label>
                <div class="controls">
	              <u:selectMultiple id="user_id" sqlId="adminMobile" value="${data.view.user_id}"/>
                </div>
              </div>
              <div class="control-group control-group-checker">
                <label class="control-label">告警方式</label>
                <div class="controls">
					<input type='checkbox' name='alarm_type' value=0>短信 &nbsp;&nbsp;
					<input type='checkbox' name='alarm_type' value=1>email
                </div>
              </div>
           	<div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
	              <input type="hidden" name="notice_id" value="${data.view.notice_id}" />
	          	  <input type="hidden" name="is_update_user" id="is_update_user"/>
	          	  <input type="hidden" id="alarm_types" value="${data.view.alarm_type}" />
	              <input type="button" value="${param.notice_id==null?'添 加':'修 改'}" class="org_btn" onclick="save(this)"/>
	              <input type="button" value="取 消" class="grey_btn" onclick="location.href='${ctx}/sysNotice/query'"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>

<span id="allTimeRange" style="display:none;">
	[
	<s:iterator value="data.allTimeRange">
		<s:set var="test">${notice_id != data.view.notice_id}</s:set>
		<s:if test="#test">
			${notBlank ? "," : ""}
			{"start_date" : "${start_date}", "end_date" : "${end_date}"}
			<s:set var="notBlank" value="true"/>
		</s:if>
	</s:iterator>
	]
</span>

<script type="text/javascript">
var old_user_id;//修改前的接收管理员
var allTimeRange;//已添加时段
var validate;
$(function(){
	old_user_id = $("#user_id").serialize();
	allTimeRange = eval($("#allTimeRange").text());
	
	var alarm_type = $('#alarm_types').val();
	if(alarm_type){
		// 旧的alarm_type逻辑 '0'、'1'、'0,1' 代表  短信、Email、短信&Email
// 		var types = alarm_type.split(',');
// 		for(var i=0;i<types.length;i++){
// 			$("input:checkbox[value='"+types[i]+"']").attr('checked','true');
// 		}

		// 现在alarm_type逻辑 1、2、3 代表  Email、短信、短信&Email
        if(alarm_type == '1'){
        	$("input:checkbox[value=1]").attr('checked','true');
        }else if(alarm_type == '2'){
        	$("input:checkbox[value=0]").attr('checked','true');
        }else{
        	$("input:checkbox[value=0]").attr('checked','true');
        	$("input:checkbox[value=1]").attr('checked','true');
        }

	}
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			name: "required",
			start_date: "required",
			end_date: "required",
			alarm_type: "required",
			user_id: {
				required:true
			}
		},
		messages: {
			name: "请输入通知时段名称",
			start_date: "请选择时段开始时间",
			end_date: "请选择时段结束时间",
			alarm_type:"请选择告警方式",
			user_id: {
				required:"请选择接收管理员",
				maxlength:"最多可以选择{0}个管理员"
			}
		}
	});
});

//校验时段
function validateTimeRange(){
	if(allTimeRange.length==0){
		return true;
	}
	var start_date = $("#start_date").val();
	var end_date = $("#end_date").val();
	var flag = false;
	$.each(allTimeRange, function(i, n){
		if(i==0 && end_date<n.start_date){
			flag = true;
			return false;
		}
		if(i>0 && start_date>allTimeRange[i-1].end_date && end_date<n.start_date){
			flag = true;
			return false;
		}
		if(i==allTimeRange.length-1 && start_date>n.end_date){
			flag = true;
			return false;
		}
	});
	return flag;
}

function save(btn){
	$("#msg").hide();
	var flag = true;
	if(!validate.form()){
		flag =false;
	}
	//if(!validateTimeRange()){
		//flag =false;
		//$("#msg").text("时段不正确，请重新输入").show();
	//}
	if(!flag){
		return;
	}
	
	if(old_user_id != $("#user_id").serialize()){
		$("#is_update_user").val(true);
	} else {
		$("#is_update_user").val(false);
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
			}else if(data.result=="success"){
				setTimeout(function(){
					back();
				}, 1000);
			}
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/sysNotice/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}
</script>
</body>
</html>