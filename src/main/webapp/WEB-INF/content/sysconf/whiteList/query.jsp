<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>系统黑名单管理</title>
</head>
<body menuId="13">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<!-- 批量导入黑名单弹出框 -->
					<div class="modal hide fade" id="importExcelBox" tabindex="-1" role="dialog">
						<div class="modal-header">
							<h3 id="myModalLabel">批量导入</h3>
						</div>
						<div class="modal-body text-center">
							<form class="form-horizontal" method="post" id="mainForm">
								<input type="file" id="importInput" name="upload" accept=".xls"/>
							</form>
							<div class="controls">
								<span id="msg" class="hide" ></span>
								<span id="tips" style="display:none;">点击<a href='${ctx}/whiteList/exportError'><font color="green">下载</font></a>导入结果列表</span>
							</div>
						</div>
						<div class="modal-footer">
							<a href="#" class="btn" onclick="downloadExcelTemplate()">下载Excel模板</a>
							<a href="#" class="btn" data-dismiss="modal" onclick="closeImport()">关闭</a>
							<a href="#" class="btn btn-primary" onclick="importExcel(this)">导入</a>
						</div>
					</div>
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<u:authority menuId="93">
						<span class="label label-info" data-toggle="modal" data-target="#importExcelBox" >
							
								<a href="javascript:;" onclick="">批量导入</a>
						</span>
						</u:authority>
						<u:authority menuId="93">
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加</a>
						</span>
						</u:authority>	
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/whiteList/query">
								<ul>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="手机号" class="txt_250" /></li>
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
					                 <th>手机号</th>
					                 <th>备注</th>
					                 <th>添加时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td>${phone}</td>
							      <td>${remark }</td>
							      <td>${createtime}</td>
							      <td>
							     	 <u:authority menuId="94"><a href="javascript:;" onclick="deleteWhiteList(this, '${id}', '${phone}')">删除</a></u:authority> 
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
	</div>

	<script type="text/javascript">
		$(function(){
			// 批量导入弹出框隐藏时事件
			$('#importExcelBox').on('hide.bs.modal', function () {
				  closeImport();
			})
		});
		
		//添加
		function add() {
			location.href = "${ctx}/whiteList/add";
		}

		/* //修改状态：恢复、删除
		function deleteWhiteList(a, id, phone) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				location.href = "${ctx}/whiteList/deleteWhiteList?id=" + id + "&phone=" + phone;
			}
		} */
		
		function deleteWhiteList(a, id, phone) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/whiteList/deleteWhiteList?id=" + id + "&phone=" + phone,
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
		
		//Excel批量导入关键字
		function importExcel(btn) {
			$("#msg").hide();
            $("#tips").hide();
            var index = "" ;
			var options = {
					beforeSubmit : function() {
						$(btn).attr("disabled", true);
                        index = layer.load(1, {
                            shade: [0.5,'#fff'] //0.5透明度的白色背景
                        });
					},
					success : function(data) {
						$(btn).attr("disabled", false);
                        layer.close(index);
						if(data.result==null){
							$("#msg").text("服务器错误，请联系管理员").show();
							return;
						}

                        if(data.result == "fail"){
                            $("#msg").text(data.msg).show();
                        }else{
                            $("#tips").show();
                            $("#msg").text(data.msg).show();
                        }
//						$("#msg").text(data.msg).show();
						
// 						setTimeout("closeImport()", 2000);
					},
					url : "${ctx}/whiteList/import",
					type : "post",
//					async : false,
					timeout:300000
				};
			$("#mainForm").ajaxSubmit(options);
		}
		
		function closeImport(){
			$("#msg").hide();
			window.location.reload();
		}

        function downloadExcelTemplate(){
            var url = "${ctx}/whiteList/downloadExcelTemplate";
            jQuery('<form action="'+url+'" method="post"></form>').appendTo('body').submit().remove();
        }

	</script>
</body>
</html>