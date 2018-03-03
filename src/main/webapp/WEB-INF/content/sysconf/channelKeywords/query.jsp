<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>通道关键字配置</title>
<style type="text/css">
.ctltable{
	border-collapse: collapse;
	table-layout:fixed}
.ctltable td {
	text-overflow:ellipsis;
	overflow:hidden;
	white-space: nowrap;
	padding:10px
}
.width-initial{
	width: initial !important;
}
</style>
</head>
<body menuId="63">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
				
				<!-- 批量导入关键字弹出框 -->
					<div class="modal hide fade" id="importExcelBox" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="keyboard">
						<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
							<h3 id="myModalLabel">导入通道关键字 &nbsp;&nbsp;&nbsp;</h3>
<!-- 							<font color="red">*批量导入耗费系统资源较多，请避免在系统高峰期使用；<br>360等浏览器请点击网址栏右侧浏览器图标取消兼容模式</font> -->
						</div>
						<div class="modal-body text-center">
							<form class="form-horizontal" method="post" id="importForm">
								<input type="file" id="importInput" name="upload" accept=".xls"/>
							</form>
							<div class="controls">
								<span id="msg" style="color:red;" style="display:none;"></span><br/>
								<span id="tips" style="display:none;">点击<a href='${ctx}/channelKeywords/exportError'><font color="green">下载</font></a>失败列表</span>
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
						<span class="label label-info">
								<a href="javascript:;" onclick="exportExcel()">导出通道关键字</a>
						</span>
						<u:authority menuId="96">
							<span class="label label-info" data-toggle="modal" data-target="#importExcelBox" >
									<a href="javascript:;" onclick="">批量导入</a>
							</span>
						</u:authority>
						<u:authority menuId="96">
						<span class="label label-info">
								<a href="javascript:;" onclick="add()">添加</a>
						</span>
						</u:authority>
						<div class="search width-initial">
							<form method="post" id="mainForm" action="${ctx}/channelKeywords/query">
								<ul>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="通道关键字" class="txt_250" /></li>
									<li>
										<u:select id="status" value="${param.status}" placeholder="状态" dictionaryType="keywordstatus"></u:select>
									</li>
									<li>
										<u:select id="channel_id" value="${param.channel_id}" placeholder="通道号" sqlId="channel" />
									</li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped ctltable">
							<thead>
								<tr>
									 <th style='width:5%'>序号</th>
					                 <th style='width:10%'>通道号</th>
					                 <th style='width:40%'>关键字</th>
					                 <th style='width:20%'>备注</th>
					                 <th style='width:15%'>更新时间</th>
					                 <th style='width:15%'>状态</th>
					                 <th style='width:10%'>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td style='width:10%'>${rownum}</td>
							      <td><c:out value="${cid}"/></td>
							      <td><u:truncate length="100" value="${keyword}" /></td>
							      <td><c:out value="${remarks}"/></td>
							      <td>${createtime}</td>
							      <td>
							      <s:if test="status == 1">正常</s:if>
							      <s:if test="status == 7">禁用</s:if>
							      </td>
							      <td>
							     	 <u:authority menuId="97"> <a href="javascript:;" onclick="edit('${cid}')">编辑</a>
							     	 </u:authority> 
							     	 <%-- <u:authority menuId="98">| <a href="javascript:;" onclick="delChannelKeyword(this, '${cid}')">删除</a></u:authority> --%> 
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
	<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#importExcelBox').on('hide.bs.modal', function () {
				  closeImport();
			})
		});
			
		// 添加
		function add() {
			location.href = "${ctx}/channelKeywords/add";
		}
		
		// 编辑
		function edit(cid) {
			location.href = "${ctx}/channelKeywords/edit?cid=" + cid;
		}

		// 删除
		function delChannelKeyword(a, cid) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				$.ajax({
					type : "post",
					url : "${ctx}/channelKeywords/delete?cid=" + cid,
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
						if(data==null){
							$("#msg").text("服务器错误，请联系管理员").show();
							return;
						}
						$("#msg").text(data.msg).show();
						if(data.existFail == 1){
							$("#tips").show();
						}
					},
					url : "${ctx}/channelKeywords/import",
					type : "post",
// 					async : false,
					timeout: 30000000
			};
			$("#importForm").ajaxSubmit(options);
		}
		
		function closeImport(){
			$("#msg").hide();
			window.location.reload();
		}
		
		function downloadExcelTemplate(){
			var url = "${ctx}/channelKeywords/downloadExcelTemplate";
			jQuery('<form action="'+url+'" method="post"></form>').appendTo('body').submit().remove();
		}
		
		function exportExcel(){
			var totalCount = ${page.totalCount};
			if (totalCount == 0) {
				layer.msg("共0条纪录，导出Excel文件失败", {icon: 1,time: 2000}); 
				return;
			}
			if(totalCount>50000){
				layer.msg("导出Excel文件条数大于50000条", {icon: 1,time: 2000}); 
				return;
			}
			
			var mainForm = $("#mainForm");
			var action = mainForm.attr("action");

			mainForm.attr("action", "${ctx}/channelKeywords/exportExcel").submit();
			mainForm.attr("action", action);
		}
		
	</script>
</body>
</html>