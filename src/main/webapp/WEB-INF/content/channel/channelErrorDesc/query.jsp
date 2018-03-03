<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>通道错误状态表</title>
</head>
<body menuId="70">
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
						<u:authority menuId="115">
							<span class="label label-info">
								<a href="javascript:;" onclick="add()">添加通道错误</a>
							</span>
						</u:authority>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelErrorDesc/query">
							<ul>
								<li>
									<input type="text" name="channelid" value="<s:property value="#parameters.channelid"/>" maxlength="10"
										placeholder="通道号" class="txt_250" />
								</li>
								<li>
									<input type="text" name="errorcode" value="<s:property value="#parameters.errorcode"/>" maxlength="10"
										placeholder="错误代码" class="txt_250" />
								</li>
								<li>
                  					<u:select id="type" data="[{value:'',text:'错误类型:所有'},{value:'1',text:'应答'},{value:'2',text:'状态报告'}]" 
                  						value="${search_type}" showAll="false" />
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
									 <th>序号</th>
					                 <th>通道号</th>
					                 <th>错误类型</th>
					                 <th>错误代码</th>
					                 <th>错误描述</th>
					                 <th>SMSP平台错误码</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
									<td>${rownum}</td>
									<td>${channelid}</td>
									<td><s:if test="type==1">应答</s:if>
										<s:elseif test="type==2">状态报告</s:elseif>
									</td>
									<td>${errorcode}</td>
									<td>${errordesc}</td>
									<td>${syscode}</td>
									<td>${updatetime}</td>
							      <td>
							      	<u:authority menuId="116"><a href="javascript:;" onclick="edit('${id}')">编辑</a></u:authority>
							      	<u:authority menuId="117">| <a href="javascript:;" onclick="del(this, '${id}')">删除</a></u:authority>
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
		//添加
		function add() {
			location.href = "${ctx}/channelErrorDesc/edit";
		}
		
		function edit(id) {
			location.href = "${ctx}/channelErrorDesc/edit?id=" + id;
		}

		//删除
		function del(a, id) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/channelErrorDesc/delete",
					data : {
						id : id
					},
					success : function(data) {
						if (data == null) {
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