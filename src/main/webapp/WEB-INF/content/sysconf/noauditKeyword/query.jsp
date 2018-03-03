<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>用户免审关键字管理</title>
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
<body menuId="136">
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
						<u:authority menuId="145">
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加用户免审关键字</a>
						</span>
						</u:authority>
 						<div class="search"> 
							<form method="post" id="mainForm" action="${ctx}/noauditKeyword/query">
								<ul>
								<li name="testhight1">
									<li><input type="text" name="clientid" value="<s:property value="#parameters.clientid"/>" maxlength="80" placeholder="用户帐号/平台帐号" class="txt_250" /></li>
									<li><input type="text" name="keyword" value="<s:property value="#parameters.keyword"/>" maxlength="80" placeholder="免审关键字" class="txt_250" /></li>
									<li><u:select id="status" data="[{value:'0',text:'状态：所有'},{value:'1',text:'启用'},
                                       {value:'7',text:'禁用'}]" value="${param.status}" showAll="false" />
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
									 <th style='width:5%'>序号</th>
					                 <th>用户帐号/平台帐号</th>
					                 <th>免审关键字</th>
					                 <th>备注</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td style='width:10%'>${rownum}</td>
							      <td>${clientid}</td>
							      <td class="sms_status"><u:truncate length="500" value="${keyword}"/></td>
							      <td class="status" style="display: none">${status}</td>
							      <td>${remarks}</td>
							      <td><s:date name="%{createtime}" format="yyyy-MM-dd HH:mm:ss" /></td>
							      <td>
							      <u:authority menuId="146">
							     	 <a href="javascript:;" onclick="edit('${clientid}')">编辑</a>
							     </u:authority>	 
							     <u:authority menuId="147">
							     	 <s:if test="status == 1">
							     	 	<a href="javascript:;" onclick="updateStatus(this,'${clientid}')">禁用</a>
							     	 </s:if>
							     	 <s:if test="status == 7">
							     	 	<a href="javascript:;" onclick="updateStatus(this,'${clientid}')">启用</a>
							     	 </s:if>
							     </u:authority>	 
							      </td>
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
		})
		//添加
		function add() {
			location.href = "${ctx}/noauditKeyword/add";
		}
		
		//编辑
		function edit(clientid) {
			location.href = "${ctx}/noauditKeyword/add?clientidold=" + clientid;
		}
		
		function updateStatus(a,clientid){
			var status = $(a).parent("td").siblings(".status").text();
			var statusName = "" ;
			if(status == 1){
				statusName = '禁用';
				status = 7 ;
			}else{
				statusName = '启用';
				status = 1 ;
			}
			if(confirm("确定要"+ statusName +"该条数据吗？")){
				$.ajax({
					type: "post",
					url: "${ctx}/noauditKeyword/updateStatus",
					data:{
						clientid : clientid,
						status : status
					},
					success: function(data){
						if(data.result==null){
							alert("服务器错误，请联系管理员");
							return;
						}
						
						if(data.result=="success"){
							$(a).parent("td").siblings(".status").text(status);
							if(status == 1){
								statusName = '禁用';
							}else{
								statusName = '启用';
							}
							$(a).text(statusName);
						}else{
							alert(data.msg);
						}
					}
				});
			}
		}
	</script>
</body>
</html>