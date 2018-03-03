<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.task_id==null?'添加':'修改'}任务</title>
</head>
<body menuId="43">
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
            <form class="form-horizontal" method="post" id="mainForm">
            
          	<s:if test="data.task_id!=null">
				<div class="control-group">
					<label class="control-label">任务id</label>
					<div class="controls">
						${data.task_id}
						<input type="hidden" name="task_id" value="${data.task_id}" />
					</div>
				</div>
          	</s:if>
            
			<div class="control-group">
				<label class="control-label">任务名称</label>
				<div class="controls">
					<input type="text" name="task_name" value="${data.task_name}" maxlength="100" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">数据库类型</label>
				<div class="controls">
					<u:select id="db_type" value="${data.db_type}" defaultIndex="1"  dictionaryType="db_type" excludeValue="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">任务类型</label>
				<div class="controls">
					<u:select id="task_type" value="${data.task_type}" defaultIndex="1"  dictionaryType="task_type" excludeValue="" onChange="change_task_type" />
					<label for="task_type" class="error" style="display:none;"></label>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">存储过程名称</label>
				<div class="controls">
					<input type="text" id="procedure_name" name="procedure_name" value="${data.procedure_name}" maxlength="200" />
					<span>两个参数：第一个为入参（统计时间），第二个为出参（结果，1表示成功）</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">执行类型</label>
				<div class="controls">
					<u:select id="execute_type" value="${data.execute_type}" defaultIndex="1" dictionaryType="task_execute_type" excludeValue="" onChange="change_execute_type" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">下次执行时间</label>
				<div class="controls">
					<u:date id="execute_next" value="${data.execute_next}" params="dateFmt: execute_next_fmt" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">执行周期</label>
				<div class="controls">
					<input type="text" id="execute_period" name="execute_period" value="${data.execute_period}" onfocus="inputControl.setNumber(this, 9, 0, false)" />
					<span id="span_execute_period"></span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">扫描类型</label>
				<div class="controls">
					<u:select id="scan_type" value="${data.scan_type}" defaultIndex="1" dictionaryType="task_scan_type" excludeValue="" onChange="change_scan_type" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">下次扫描时间</label>
				<div class="controls">
					<u:date id="scan_next" value="${data.scan_next}" dateFmt="yyyy-MM-dd HH:mm:00" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">扫描周期</label>
				<div class="controls">
					<input type="text" name="scan_period" value="${data.scan_period}" onfocus="inputControl.setNumber(this, 9, 0, false)" />
					<span id="span_scan_period"></span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否每次扫描<br/>都执行</label>
				<div class="controls">
					<u:select id="scan_execute" value="${data.scan_execute}" defaultIndex="1" dictionaryType="task_scan_execute" excludeValue="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">依赖任务</label>
				<div class="controls">
					<u:selectMultiple id="dependency" value="${data.dependency}" sqlId="task" excludeValue="${data.task_id}, " />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">分组</label>
				<div class="controls">
					<input type="text" name="group" value="${data.group}" onfocus="inputControl.setNumber(this, 9, 0, false)" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">组内排序</label>
				<div class="controls">
					<input type="text" name="order" value="${data.order}" onfocus="inputControl.setNumber(this, 9, 0, false)" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">&nbsp;</label>
				<div class="controls">
					<span id="msg" class="error" style="display:none;"></span>
				</div>
			</div>
            
            <div class="form-actions">
                <input type="button" value="保  存" class="btn btn-success" onclick="save(this)" />
                <input type="button" value="取 消" class="btn btn-error" onclick="back()" />
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>

<script type="text/javascript">
var execute_type = "${data.execute_type}";
var execute_next_fmt = "yyyyMMddHHmm";
var validate;
$(function(){
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			task_name: "required",
			task_type: "required",
			db_type: "required",
			procedure_name: "required",
			execute_next: "required",
			execute_period: {
				required:true,
				min:1
			},
			scan_type: "required",
			scan_next: "required",
			scan_period: {
				required:true,
				min:1
			},
			scan_execute: "required",
			group: "required",
			order: "required"
		},
		messages: {
			task_name: "请输入任务名称",
			task_type: "请输入任务类型",
			db_type: "请输入连接的数据库",
			procedure_name: "请输入存储过程名称",
			execute_next: "请输入下次执行时间",
			execute_period: {
				required:"请输入执行周期",
				min:"执行周期必须大于0"
			},
			scan_type: "请输入扫描类型",
			scan_next: "请输入下次扫描时间",
			scan_period: {
				required:"请输入扫描周期",
				min:"扫描周期必须大于0"
			},
			scan_execute: "请输入是否每次扫描都执行",
			group: "请输入分组",
			order: "请输入组内排序"
		}
	});
});

//任务类型
function change_task_type(value, text){
	if(value==1){
		$("#procedure_name").attr("disabled", false);
	}else{
		$("#procedure_name").attr("disabled", true).val("");
		$("#procedure_name-error").hide();
	}
}

//执行类型
function change_execute_type(value, text){
	if(value=="0"){
		$("#execute_next, #execute_period").attr("disabled", true).val("");
		$("#execute_next-error, #execute_period-error").hide();
		$("#span_execute_period").empty();
		return;
	}else{
		$("#execute_next, #execute_period").attr("disabled", false);
		$("#span_execute_period").text(text);
	}
	
	var dateFmt;
	switch(parseInt(value)){
		case 1 : dateFmt="yyyyMMddHHmm"; break;
		case 2: dateFmt="yyyyMMddHH"; break;
		case 3: 
		case 4: dateFmt="yyyyMMdd"; break;
		case 5: 
		case 6: dateFmt="yyyyMM"; break;
		case 7: dateFmt="yyyy"; break;
	}
	
	if(execute_type != value){
		$("#execute_next").val("");
	}
	execute_type = value;
	execute_next_fmt = dateFmt;
}

//扫描类型
function change_scan_type(value, text){
	$("#span_scan_period").text(text);
}

function save(btn){
	$("#msg").hide();
	if(!validate.form()){
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
		},
		url : "${ctx}/task/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
}
</script>
</body>
</html>