<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="u" uri="/ucpaas-tags" %>
<%@ taglib prefix="cache" uri="http://www.opensymphony.com/oscache" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<s:set var="locale"><%=org.springframework.context.i18n.LocaleContextHolder.getLocale().toString()%></s:set>
<s:set var="lang"><%=org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage()%></s:set>