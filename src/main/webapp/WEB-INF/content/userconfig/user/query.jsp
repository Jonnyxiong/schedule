<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>用户及通道组配置</title>
</head>
<body menuId="15">
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
						<span class="label label-info"> <a href="javascript:;"
							onclick="exportExcel()">导出Excel文件</a>
						</span>
						<u:authority menuId="126">
							<span class="label label-info"> <a href="javascript:;"
								onclick="add()">添加配置</a>
							</span>
						</u:authority>


						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/user/query">
								<ul>
									<li><input type="text" name="text"
										value="<s:property value="#parameters.text"/>" maxlength="40"
										placeholder="用户账号/用户名" class="txt_250" /></li>
									<li><input type="text" name="s_channelid"
										value="<s:property value="#parameters.s_channelid"/>" maxlength="40"
										placeholder="通道组号" class="txt_250" /></li>
										
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
									<th>用户账号</th>
									<th>短信类型</th>
									<th>区分运营商</th>
									<th>全网通道组</th>
									<th>移动通道组</th>
									<th>联通通道组</th>
									<th>电信通道组</th>
									<th>国际通道组</th>
									<th>免黑名单</th>
									<th>免系统关键字</th>
									<th>免通道关键字</th>
									<th>备注</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${userid}</td>
										<td>${smstype2}</td>
										<td>${distoperators2}</td>
										<td>${channelid}</td>
										<td>${ydchannelid}</td>
										<td>${ltchannelid}</td>
										<td>${dxchannelid}</td>
										<td>${gjchannelid}</td>
										<td>
											<s:if test="unblacklist == 0">否</s:if>
											<s:if test="unblacklist == 1">是</s:if>
										</td>
										<td>
											<s:if test="free_keyword == 0">否</s:if>
											<s:if test="free_keyword == 1">是</s:if>
										</td>
										<td>
											<s:if test="free_channel_keyword == 0">否</s:if>
											<s:if test="free_channel_keyword == 1">是</s:if>
										</td>
										<td>${remarks}</td>
										<td style="width: 10%">
											<u:authority menuId="293">
												<a href="javascript:;" onclick="view('${userid}','${smstype}')">查看</a>
											</u:authority>
											<u:authority menuId="128">
									      		<a href="javascript:;" onclick="edit('${userid}','${smstype}')">编辑</a>
											</u:authority>
											<u:authority menuId="129">
									      	  	<a href="javascript:;" onclick="deleteData('${userid}', '${smstype}')">删除</a>
											</u:authority>
											
											<s:if test="state==0">
												<u:authority menuId="127">
												  	<a href="javascript:;" onclick="updateStatus(this, '${userid}','${smstype}' , 1,1)">开启</a>
												</u:authority>
											</s:if>
											<u:authority menuId="127">
												<s:if test="state==1">
													<a href="javascript:;" onclick="updateStatus(this, '${userid}','${smstype}', 0,0)">关闭</a>
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
		//导出Excel文件
		function exportExcel() {
			var totalCount = ${page.totalCount};
			if (totalCount == 0) {
				alert("共0条纪录，导出Excel文件失败");
				return;
			}
			var mainForm = $("#mainForm");
			var action = mainForm.attr("action");

			mainForm.attr("action", "${ctx}/user/exportExcel").submit();
			mainForm.attr("action", action);
		}

		//添加
		function add() {
			location.href = "${ctx}/user/edit";
		}

		//编辑
		function edit(userid, smstype) {
			location.href = "${ctx}/user/edit?userid=" + userid + "&smstype=" + smstype + "&isEdit=" + 1;
		}
        //查看
        function view(userid, smstype) {
            location.href = "${ctx}/user/view?userid=" + userid + "&smstype=" + smstype + "&isEdit=" + 1;
        }

		//修改状态
		function updateStatus(a, userid, smstype, userstatus, state) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/user/updateStatus",
					data : {
						userid : userid,
						smstype : smstype,
						userstatus : userstatus,
						state : state
					},
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

		
		function getSelectedValue(){
			var val = "";  
		    $("[name='ids']:checkbox").each(function(){  
		        if ($(this).attr('checked')) {  
		            val +=$(this).val()+",";  
		        }  
		    });  
		    val = val.length > 0 ? val.substr(0, val.length-1) : '';  
		    return val;
		}
		
		//删除
		function deleteData(userid, smstype) {
			if (confirm("确定要删除吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/user/delete",
					data : {
						userid : userid,
						smstype : smstype
					},
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