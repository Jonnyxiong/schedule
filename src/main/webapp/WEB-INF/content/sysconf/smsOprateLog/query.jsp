<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>操作日志管理</title>
<style type="text/css">
.sms_content{  word-wrap: break-word;
		  word-break: break-all;
		  overflow: hidden;
		  max-height: 320px;
		  min-height: 20px;
		  display: block;
	   }
	 .sms_status{  word-wrap: break-word;
		  word-break: break-all;
	   }
</style>
</head>
<body menuId="83">
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
 						<div class="search"> 
							<form method="post" id="mainForm" action="${ctx}/smsOprateLog/query">
								<ul>
									<li><input type="text" name="realname" value="<s:property value="#parameters.realname"/>" maxlength="40" placeholder="真实姓名" class="txt_250" /></li>
									<li><input type="text" name="email" value="<s:property value="#parameters.email"/>" maxlength="40" placeholder="用户名(emai)" class="txt_250" /></li>
									<li name="testhight1">
									<u:select id="page_id" value="${param.page_id}" placeholder="模块名" dictionaryType="module_name"  />
									</li>
									<li><input type="text" name="page_url" value="<s:property value="#parameters.page_url"/>" maxlength="40" placeholder="页面url" class="txt_250" /></li>
									<li><input type="text" name="op_desc" value="<s:property value="#parameters.op_desc"/>" maxlength="40" placeholder="操作详情" class="txt_250" /></li>
									<li><input type="text" name="ip" value="<s:property value="ip"/>" placeholder="操作者ip" maxlength="40"  class="txt_250" /></li>
									<li name="testhight1">
									<u:date id="start_time" value="${start_time}" placeholder="日志开始时间" params="minDate:'%y-%M-{%d-45} 00:00:00', maxDate:'#F{$dp.$D(\\'end_time\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
									<span>至</span>
            						<u:date id="end_time" value="${end_time}" placeholder="日志结束时间" params="minDate:'#F{$dp.$D(\\'start_time\\')||\\'%y-%M-{%d-45} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
									</li>
									<li name="testhight1">
									<u:select id="op_type" value="${param.op_type}" placeholder="日志类型" dictionaryType="op_type"  />
									</li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
							</form>
						</div> 
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th style="width: 10px;">序号</th>
					                 <th>真实姓名 </th>
					                 <th>用户名 </th>
					                 <th>管理员id</th>
					                 <th>日志id </th>
					                 <th>模块名称 </th>
					                 <th>访问URL</th>
					                 <th>日志类型 </th>
					                 <th>操作者ip </th>
					                 <th>操作时间 </th>
					                 <th>操作详情 </th>
					                 
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td>${realname}
							      <s:if test="realname == null">
							      		用户已被删除
							      </s:if>
							      </td>
							      <td>${email}
							      <s:if test="email == null">
							      		用户已被删除
							      </s:if>
							      </td>
							      <td>${id}</td>
							      <td>${log_id}</td>
							      <td><u:ucparams key="${page_id}" type="module_name" /></td>
							      <td>${page_url}</td>
							      <td>${op_type_name}</td>
							      <td>${ip}</td>
							      <td><s:date name="%{create_date}" format="yyyy-MM-dd HH:mm:ss" /></td>
							      <td class="sms_content"><c:out value="${op_desc}" /></td>
							     </tr>
							    </s:iterator>
							</tbody>
						</table>
					</div>
						<u:page page="${page}" formId="mainForm" />
				</div>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		$(function(){
			$("#op_type option[value='1']").remove(); 
			$("#op_type option[value='2']").remove();
		});
		function showdetail(a){
			alert(a.innerHTML);
		}
	</script>
</body>
</html>