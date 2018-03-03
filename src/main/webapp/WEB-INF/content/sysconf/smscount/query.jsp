<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>条数配置</title>
</head>
<body menuId="48">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5>
						       
						<a id="bychannel"  href="javascript:void(0)" onclick="changeTable(this.id)">按通道配置</a>
						<a id="bysid" style="margin-left:10px;" onclick="changeTable(this.id)" href="javascript:void(0)">按sid配置</a>
						</h5>
						<span class="label label-info">
						<u:authority menuId="1">
							<a href="javascript:;" onclick="add()">添加配置</a>
						</u:authority>
						</span>
						<div class="search">
							<form method="post" id="mainForm" action="${ctx}/smscount/query">
								<ul>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="通道号/sid" class="txt_250" /></li>
									<li><input type="hidden" name="tableid" id="tableid" value='<s:property value="#request.tableid"/>' /></li>
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
					                 <th>
					                
					                 <s:if test="#request.tableid=='bychannel'">通道号</s:if>
					                 <s:if test="#request.tableid=='bysid'">用户sid</s:if>
					                 </th>
					                 <th>条数</th>
					                 <th>添加时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td>${rownum}</td>
							      <td>
							      <s:if test="#request.tableid=='bychannel'">${channelid }</s:if>
					              <s:if test="#request.tableid=='bysid'">${sid }</s:if>
							      </td>
							      <td>${smscount}</td>
							      <td>${createtime}</td>
							      <td>
							     	 <a href="javascript:;" onclick="deleteSmscount(this, '${id}')">删除</a> |
							     	 <a href="javascript:;" onclick="edit('${id}')">编辑</a> 
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
	
	$("#"+$('#tableid').val()).css('borderBottom','2px solid #28b779');
	
		function changeTable(changeid){
			
			$('#tableid').val(changeid);
			$('#mainForm').submit();
			
		}
		
		//添加
		function add() {
			location.href = "${ctx}/smscount/add?tableid="+$('#tableid').val();
		}
		
		//编辑
		function edit(id) {
			
			location.href = "${ctx}/smscount/add?id=" + id+"&tableid="+$('#tableid').val();
		}

		//修改状态：恢复、删除
		function deleteSmscount(a, id) {
			if (confirm("确定要" + $(a).text() + "吗？")) {
				location.href = "${ctx}/smscount/deleteSmscount?id="+id+"&tableid="+$('#tableid').val();
			}
		}
	</script>
</body>
</html>