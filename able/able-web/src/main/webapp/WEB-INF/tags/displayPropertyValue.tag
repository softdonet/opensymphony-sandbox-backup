<%@ tag import="com.opensymphony.able.introspect.PropertyInfo" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%@ attribute name="propertyName" type="java.lang.String" required="true" %>
<%
    PropertyInfo property = PropertyInfo.getPropertyInfo(entity, propertyName);
    boolean done = false;
    if (property != null) {
        done = true;
        if (property.isCollection()) {
%>
[<%= property.getSize(entity) %>]
<stripes:link href="${actionBean.actionUri}_${propertyName}?owner=${entity.id}">Edit</stripes:link>
<%
        }
        else if (property.isPersistent()) {
%>
<a:viewSummary entity="${entity[propertyName]}"/>
<%
        }
        else if (property.isDate()) {
%>
            <fmt:formatDate value="${entity[propertyName]}" dateStyle="medium" />
<%
        }
        else if (property.isNumber()) {
%>
  <fmt:formatNumber value="${entity[propertyName]}"/>
<%
        }
        else {
            // TODO log warning no property found?
            done = false;
        }
    }
    if (!done) {
%>
${entity[propertyName]}
<%
    }
%>
