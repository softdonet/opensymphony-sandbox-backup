<%@ tag import="com.opensymphony.able.entity.PropertyInfo" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="able" tagdir="/WEB-INF/tags/able" %>
<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="entityName" type="java.lang.String" required="false" %>

<%
    PropertyInfo propertyInfo = PropertyInfo.getPropertyInfo(entity, name);
    boolean done = false;
    if (propertyInfo != null) {
        done = true;
        if (propertyInfo.isCollection()) {
%>
[<%= propertyInfo.getSize(entity) %>]
<stripes:link href="${actionBean.actionUri}/${name}?oid=${entity.id}">Edit</stripes:link>
<%
        }
        else if (propertyInfo.isPersistent()) {
%>
<able:viewSummary entity="${entity[name]}"/>
<%
        }
        else if (propertyInfo.isDate()) {
%>
            <fmt:formatDate value="${entity[name]}" dateStyle="medium" />
<%
        }
        else if (propertyInfo.isNumber()) {
%>
  <fmt:formatNumber value="${entity[name]}"/>
<%
        }
        else {
            // TODO log warning no property found?
            done = false;
        }
    }
    if (!done) {
%>
${entity[name]}
<%
    }
%>
