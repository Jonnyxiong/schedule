<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<!--sidebar-menu-->
<div id="sidebar"><a href="#" class="visible-phone">
	<i class="fa fa-home"></i>管理中心</a>
	<ul>
	<s:iterator value="data.menuRoot.subMenus">
	 	<li class="submenu" id="menu_id_${menu_id}" pid="${parent_id}" title="${menu_name}" ><a href="${empty menu_url?'':ctx}${empty menu_url?'#':menu_url}"><i class="fa ${menu_class}"></i><span>${menu_name}</span></a>
			<ul>
				<s:iterator value="subMenus" var="sub">
					<li id="menu_id_${menu_id}" pid="${sub.parent_id}"title="${sub.menu_name}" ><a href="${empty menu_url?'':ctx}${empty menu_url?'#':menu_url}" class="${menu_class}"  >${sub.menu_name}</a></li>
				</s:iterator>
			</ul>
		</s:iterator>
	</li>
  </ul>
</div>
<script type="text/javascript">
	function IsPC(){  
           var userAgentInfo = navigator.userAgent;  
           var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");  
           var flag = true;  
           for (var v = 0; v < Agents.length; v++) {  
               if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }  
           }  
           return flag;  
	} 
	// 短信审核业务
	var auditPageTag = $("#menu_id_81").find('a');
	var auditQueryPageTag = $("#menu_id_82").find('a');
	
	auditPageTag.attr('href', 'javascript:;');
	auditPageTag.on('click', function(){
		if(IsPC() && $.getBrowser('name') != 'chrome'){
			layer.alert('审核功能只支持谷歌浏览器和360浏览器极速模式。', {icon: 0});
			return;
		}
		var auditPage = getcookie('auditPage');
		var queryAuditPage = getcookie('queryAuditPage');
		if(auditPage == 1 || queryAuditPage == 1){
			releaseSmsAudit();
			setTimeout(function(){location.href = '${ctx}/smsaudit/audit';}, 500);
		}else{
			location.href = '${ctx}/smsaudit/audit';
		}
		
	})
	auditQueryPageTag.attr('href', 'javascript:;');
	auditQueryPageTag.on('click', function(){
		if(IsPC() && $.getBrowser('name') != 'chrome'){
			layer.alert('审核功能只支持谷歌浏览器和360浏览器极速模式。', {icon: 0});
			return;
		}
		var auditPage = getcookie('auditPage');
		var queryAuditPage = getcookie('queryAuditPage');
		if(auditPage == 1 || queryAuditPage == 1){
			releaseSmsAudit();
			setTimeout(function(){location.href = '${ctx}/smsaudit/query';}, 500);
			
		}else{
			location.href = '${ctx}/smsaudit/query';
		}
		
	})
	
</script>
<!--sidebar-menu-->
