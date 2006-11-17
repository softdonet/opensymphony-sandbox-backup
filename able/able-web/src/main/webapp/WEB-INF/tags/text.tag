<%@ tag import="net.sourceforge.stripes.localization.LocalizationUtility" %>
<%@ tag import="net.sourceforge.stripes.controller.ActionResolver" %>
<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<%@ tag body-content="empty" %>
<%@ attribute name="key" required="true" type="java.lang.String" %>
<%@ attribute name="defaultValue" required="false" type="java.lang.String" %>
<%
    String s = LocalizationUtility.getLocalizedFieldName(key, request.getAttribute(ActionResolver.RESOLVED_ACTION).toString(), request.getLocale());
    if (s != null) {
        out.print(s);
    } else if (defaultValue != null && !"".equals(defaultValue)) {
        out.print(defaultValue);
    } else {
        out.print(key);
    }
%>