<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>超频用户记录管理</title>
</head>
<body menuId="137">
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
						<u:authority menuId="142">
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加超频用户记录</a>
						</span>
						</u:authority>
 						<div class="search"> 
							<form method="post" id="mainForm" action="${ctx}/overrateUserList/query">
								<ul>
								<li name="testhight1">
									<li><input type="text" name="clientid" value="<s:property value="#parameters.clientid"/>" maxlength="80" placeholder="用户帐号/平台帐号" class="txt_250" /></li>
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
							      <td>${remarks}</td>
							      <td>${createtime}</td>
							      <td><u:authority menuId="143">
							     	 <a href="javascript:;" onclick="edit('${id}')">编辑</a></u:authority>
							     	 <u:authority menuId="144">
							     	 <a href="javascript:;" onclick="deleteItem(this,'${id}')">删除</a>
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
			location.href = "${ctx}/overrateUserList/add";
		}
		
		//编辑
		function edit(id) {
			location.href = "${ctx}/overrateUserList/add?id=" + id;
		}
		
		
		function deleteItem(a,id){
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/overrateUserList/delete?id=" + id,
					success : function(data) {
						if (data.result == null) {
							alert("服务器错误，请联系管理员");
							return;
						}
						alert(data.msg);
						if (data.result == "success") {
							window.location.reload();
						}
					}
				});
			}
		}
	</script>
</body>
</html>