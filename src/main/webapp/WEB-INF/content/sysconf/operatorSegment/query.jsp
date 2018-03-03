<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>运营商号段表</title>
</head>
<body menuId="77">
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
						<u:authority menuId="105">
						<span class="label label-info">
							<a href="javascript:;" onclick="add()">添加运营商号段</a>
						</span>
						</u:authority>
 						<div class="search"> 
							<form method="post" id="mainForm" action="${ctx}/operatorSegment/query">
								<ul>
									<li name="testhight1">
									<u:select id="operater" value="${param.operater}" placeholder="运营商类型" dictionaryType="channel_operatorstype"  />
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
					                 <th>运营商类型</th>
					                 <th>可发号段</th>
					                 <th>备注</th>
					                 <th>更新时间</th>
					                 <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
							      <td style='width:10%'>${rownum}</td>
							      <td>
							      <s:if test="operater == 0">
							      	全网
							      </s:if>
							      <s:if test="operater == 1">
							      	移动
							      </s:if>
							      <s:if test="operater == 2">
							      	联通
							      </s:if>
							      <s:if test="operater == 3">
							     	电信
							      </s:if>
							      <s:if test="operater == 4">
							     	国际
							      </s:if>
							      <s:if test="operater == 5">
							     	虚拟移动
							      </s:if>
							      <s:if test="operater == 6">
							     	虚拟联通
							      </s:if>
							      <s:if test="operater == 7">
							     	虚拟电信
							      </s:if>
							      </td>
							      <td>${numbersegment}</td>
							      <td>${remarks}</td>
							      <td>${update_date}</td>
							      <td>
							     	 <u:authority menuId="106"><a href="javascript:;" onclick="edit('${operater}')">编辑</a></u:authority> 
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
			$("#operater option[value='0']").remove(); 
			$("#operater option[value='4']").remove();
		})
		//添加
		function add() {
			location.href = "${ctx}/operatorSegment/add";
		}
		
		//编辑
		function edit(operater) {
			location.href = "${ctx}/operatorSegment/edit?operater=" + operater;
		}

	</script>
</body>
</html>