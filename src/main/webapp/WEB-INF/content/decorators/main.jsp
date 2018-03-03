<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<title><decorator:title /> - 云之讯短信调度系统</title>	<meta charset="UTF-8" />
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<link rel="shortcut icon" href="${ctx}/img/favicon.ico" />
	<link rel="bookmark" href="${ctx}/img/favicon.ico"/>
	<%@ include file="/common/meta.jsp"%>
	<decorator:head />
</head>
<body>
	<%--头 --%>
	<cache:cache key="headerCache_user_${LOGIN_USER_ID}" time="600" groups="menuCache_user_${LOGIN_USER_ID}">
		<%--页面顶部用户信息加载 --%>
		<s:action namespace="/admin" name="topHeader" executeResult="true" flush="false" />
	</cache:cache>
	<%--根据角色缓存菜单，默认scope="application"，time永远不过期可以调用OSCacheUtils.flushMenuCache(roleId)来刷新 --%>
	<cache:cache key="headerCache_${LOGIN_ROLE_ID}" time="-1" groups="menuCache_${LOGIN_ROLE_ID}">
		<s:action namespace="/menu" name="header" executeResult="true" flush="false" /><%--页面顶部1、2级菜单 --%>
	</cache:cache>
	<div id="content">
	  <!--breadcrumbs-->
	  <div id="content-header">
	    <div id="breadcrumb">
	    </div>
	    <h1 id="pageTitle"><decorator:title /></h1>
	  </div>
	  <!--End-breadcrumbs-->
	  <decorator:body />
	</div>
	<!--主体部分content eof--> 
	<cache:cache key="footer_jsp" time="-1">
		<%@ include file="/common/footer.jsp"%>
	</cache:cache>
<script type="text/javascript">
//初始化选中菜单
$(function(){
	// seesion超时情况下ajax请求跳转到登陆页面
 	$.ajaxSetup({
 		contentType : "application/x-www-form-urlencoded;charset=utf-8",
 		complete : function(XMLHttpRequest, textStatus) {
 			var sessionstatus = XMLHttpRequest
 					.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，  
 			if (sessionstatus == "timeout") {
 				//如果超时就处理 ，指定要跳转的页面  
 				var url = "${ctx}/index";
 				location.href = url;
 			}
 		}
 	});
	
	var curr_id = '<decorator:getProperty property="body.menuId"/>';
	
	var mi = $("#menu_id_"+curr_id);
	var html = "";
	while(mi.size()>0){
		if(mi.hasClass("submenu")){
			mi.addClass("open");
		}else{
			mi.addClass("active");
		}
		html = '<a href="'+mi.find("a:first").attr("href")+'">'+mi.attr("title")+'</a>' + html;
		mi = $("#menu_id_"+mi.attr("pid"));
	}
	if(html.indexOf('<decorator:title />') < 0 ){
		html = html + '<a href="#"><decorator:title /></a>';
	}
	
		
	var breadcrumb = $("#breadcrumb");
	breadcrumb.html('<a href="${ctx}/index" title="返回首页" class="tip-bottom"><i class="fa fa-home"></i>首页</a>' + html);
	breadcrumb.find("a:last").addClass("current");
	
});
//返回
function back(){
	var referer = "${referer}";
	if(referer != ""){
		location.href = referer;
	}else{
		history.back();
	}
}
</script>
</body>
</html>