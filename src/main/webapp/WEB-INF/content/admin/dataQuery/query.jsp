<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
<title>数据查询</title>
</head>

<body menuId="66">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
					</div>
					<div class="widget-content nopadding">
					 	<form method="post" id="mainForm" class="form-horizontal">
							<div class="control-group">
								<label class="control-label">数据库</label>
								<div class="controls">
				                  <u:select id="db_type" value="${data.db_type}" defaultIndex="1"  dictionaryType="db_type" includeValue="4,5" />
								</div>
							</div>
						   	<div class="control-group">
								<label class="control-label">执行的查询SQL</label>
								<div class="controls">
									<textarea name="excuteSql" rows="10" cols="100"
										style="height: auto; width: auto;" maxlength="5000"
										class="excuteSqlValidator"><s:property value="#parameters.excuteSql"/></textarea>
								</div>
							</div>
							
							<u:authority menuId="1">
								<div class="control-group">
									<div class="controls">
										<input type="submit" value="执行查询"
											class="btn btn-success" onclick="excuteQuery()" />
									</div>
								</div>
							</u:authority>
					  </form>
					</div>
					
					<div class="widget-content nopadding">
						<s:if test="data.errorMsg != null">
							<div class="control-group">
				                <label class="control-label">&nbsp;</label>
				                  <span id="msg" class="red" style="font-size: initial;"><s:property value="data.errorMsg"/></span>
				                <div class="controls">
				                </div>
				            </div>
						</s:if>
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<s:iterator var="th_name" value="data.rowKeyList">
							    		<th><s:property value="th_name"/></th>
									</s:iterator>
							   </tr>
							</thead>
							
							<tbody>
							    <s:iterator value="data.page.list" status="st">
									<tr>
										<s:iterator value="data.page.list.get(#st.index).values">
							    			<td>
							    				<s:property/>
							    			</td>
										</s:iterator>
								    </tr>
							    </s:iterator>
							</tbody>
						</table>
					</div>
					<s:if test="data.page != null">
						<u:page page="${data.page}" formId="mainForm" />
					</s:if>
				</div>
			</div>
		</div>
	</div>

      
<script type="text/javascript">
var validate;
$(function() {
	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		excuteSql : "required"
	});
	// 不能合法SQL校验
	jQuery.validator.addMethod("excuteSqlValidator", function(value, element) {
	    var illegalCharaters = ['DELETE', 'INSERT', 'UPDATE', 'ALTER', 'CALL', 'CREATE', 'DROP'];
	    var excuteSql = value.toUpperCase();
	    for(var pos=0; pos<illegalCharaters.length; pos++){
	    	if(excuteSql.indexOf(illegalCharaters[pos]) != -1){
	    		return this.optional(element) || false;
	    	}
	    }
	    
	    var sql = $.trim(value);
	    var select_str = sql.substring(0, 6).toUpperCase();
	    if( select_str != "SELECT" ){
	    	return false
	    }
	    return this.optional(element) || true;
	}, "请输入合法的查询SQL");
});

function excuteQuery(){
	$("#msg").hide();
	if (!validate.form()) {
		return;
	}
}
</script>
</body>
</html>