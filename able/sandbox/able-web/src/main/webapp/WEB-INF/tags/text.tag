<%@ tag import="net.sourceforge.stripes.localization.LocalizationUtility" %>
<%@ tag import="net.sourceforge.stripes.controller.ActionResolver" %>
<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<%@ tag body-content="empty" %>
<%@ attribute name="key" required="true" type="java.lang.String" %>
<%= LocalizationUtility.getLocalizedFieldName(key, request.getAttribute(ActionResolver.RESOLVED_ACTION).toString(), request.getLocale()) %>