<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<style type="text/css">
	body{
		background: #F8F8F8 !important;
	}
</style>
</head>
<body menuId="268">
	<!--Action boxes-->
	<div class="container-fluid">
<!-- 		<hr> -->
	
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
<!-- 						<span class="icon"> <i class="fa fa-th"></i> -->
<!-- 						</span> -->
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/channelTest/selectPhonesIframe">
								<ul>
									<li><u:select id="operators_type" data="[{value:'',text:'运营商类型：所有'}, {value:'1',text:'移动'}, {value:'2',text:'联通'}, {value:'3',text:'电信'}, {value:'4',text:'国际'}]" value="${operators_type_search}" showAll="false"/></li>
									<li><input type="text" name="text" value="<s:property value="#parameters.text"/>" maxlength="40" placeholder="手机号码/联系人" class="txt_250" /></li>
									<li><input type="submit" value="搜索"/></li>
								</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
									 <th>手机号码</th>
					                 <th>联系人</th>
					                 <th>运营商</th>
					                 <th style="width:5%"><input id="selectAll" type="checkbox" onclick="selectAll(this)"/>全选</th>
								</tr>
							</thead>
							<tbody id="tbody">
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${phone}</td>
										<td>${name}</td>
										<td>
											<s:if test="operators_type eq 1">移动</s:if>
											<s:elseif test="operators_type eq 2">联通</s:elseif>
											<s:elseif test="operators_type eq 3">电信</s:elseif>
											<s:elseif test="operators_type eq 4">国际</s:elseif>
										</td>
										<td>
											<input type="checkbox" name="selectPhone" value="${phone}"/>
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
		// checkbox全选事件
		function selectAll(box) {
			var select = $(box).is(":checked");
			$("#tbody tr").each(function() {
				$(this).find(":checkbox").prop("checked", select);
				var myspan=$(this).find(":checkbox").parent();
				if(select){
						myspan.attr("class","checked");
				}else{
					myspan.attr("class","");
				}
			});
			
		}
		
		// 获得页面选中的号码并用英文逗号分隔
		function getAllSelected(){
			var selectPhones = "";
		    $("[name='selectPhone']:checkbox").each(function(){
		    	if ($(this).attr('checked')) {
			    	selectPhones +=$(this).val()+",";
		    	}
		    }); 
		    selectPhones = selectPhones.length > 0 ? selectPhones.substr(0, selectPhones.length-1) : '';
		    return selectPhones;
		}
	//@ sourceURL=selectPhoneIframe.js
	</script>
</body>
</html>