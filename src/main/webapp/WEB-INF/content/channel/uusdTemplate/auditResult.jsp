<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>


<html>
<head>
	<title>${data.chart.title}</title>
</head>

<body menuId="228">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<nav class="navbar navbar-success" role="navigation">
				<ul class="nav nav-tabs nav-justified" role="tablist">
					<li class="active"><a href="${ctx}/uusd/template/auditResult">彩印模板审核状态</a></li>
					<li><a href="${ctx}/uusd/template/tokenLog">彩印通道授权日志</a></li>
				</ul>
		</nav>
		
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						
						<div class="search">
						 	<form method="post" id="mainForm" action="${ctx}/uusd/template/auditResult">
							<ul>
								<li>
                  					<input type="text" name="template_id" autocomplete="off" value="<s:property value="#parameters.template_id"/>" maxlength="11"
										placeholder="模板ID" class="txt_250" />
								</li>
								<li>
									<input type="text" name="channelid" autocomplete="off" value="<s:property value="#parameters.channelid"/>" maxlength="11"
										placeholder="通道ID" class="txt_250" />
								</li>
								<li>
									<input type="text" name="channel_tempid" autocomplete="off" value="<s:property value="#parameters.channel_tempid"/>" maxlength="32"
										placeholder="通道模板ID" class="txt_250" />
								</li>
								<li>
<%-- 									<input type="text" name="status" value="<s:property value="#parameters.status"/>" maxlength="100" --%>
<!-- 										placeholder="状态" class="txt_250" /> -->
									<u:select id="search_status" data="[{value:'-1',text:'状态:所有'},{value:'0',text:'状态:待审核'},{value:'1',text:'状态:审核通过'},{value:'3',text:'状态:审核不通过'}]" 
                  					value="${search_status}" showAll="false" />
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
					                 <th>模板ID</th>
					                 <th>通道ID</th>
					                 <th>通道审核状态</th>
					                 <th>通道返回结果</th>
					                 <th>通道模板ID</th>
					                 <th>备注</th>
					                 <th>创建时间</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${template_id}</td>
										<td>${channelid}</td>
										<td>
											<s:if test="status==0">待审核</s:if>
											<s:elseif test="status==1">审核通过</s:elseif>
											<s:else>审核不通过</s:else>
										</td>
										<td>${result}</td>
										<td>${channel_tempid}</td>
										<td>${remark}</td>
										<td>${create_time}</td>
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

//@ sourceURL= auditResult.js
</script>
</body>
</html>