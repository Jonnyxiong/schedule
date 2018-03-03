<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<title><decorator:title />- 云之讯短信调度系统</title>	<decorator:head />
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<%@ include file="/common/meta.jsp"%>
	<decorator:head />
	
	
	<script src="${ctx}/js/jquery.form.js"></script>
	<script src="${ctx}/js/jquery.ui.custom.js"></script> 
	<script src="${ctx}/js/bootstrap.min.js"></script> 
	<script src="${ctx}/js/jquery.uniform.js"></script> 
	<script src="${ctx}/js/select2.min.js"></script>
	
	<script src="${ctx}/js/validate/jquery.validate.min.js"></script>
	<script src="${ctx}/js/validate/additional-methods.min.js"></script>
	<script src="${ctx}/js/validate/ucpaas-methods.js"></script>
	<script src="${ctx}/js/validate/messages_zh.min.js"></script>
	
	<script src="${ctx}/js/matrix.js"></script>
	<script src="${ctx}/js/matrix.form_validation.js"></script>
	<script src="${ctx}/js/inputControl.js"></script>
	<script src="${ctx}/js/layer/layer.js"></script>
	<script src="${ctx}/js/cookie.js"></script>
</head>

<body>
	<decorator:body />
</body>
</html>