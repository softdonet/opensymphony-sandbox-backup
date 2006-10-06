<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="a" tagdir="/WEB-INF/tags" %>
<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%@ attribute name="property" type="com.opensymphony.able.entity.PropertyInfo" required="true" %>
<%
    boolean done = false;
    if (property != null) {
        done = true;
        if (property.isCollection()) {
%>
[<%= property.getSize(entity) %>]
<stripes:link href="${actionBean.actionUri}_${property.name}?owner=${entity.id}">Edit</stripes:link>
<%
        }
        else if (property.isPersistent()) {
%>
<a:viewSummary entity="${entity[property.name]}"/>
<%
        }
        else if (property.isDate()) {
%>
            <fmt:formatDate value="${entity[property.name]}" dateStyle="medium" />
<%
        }
        else if (property.isNumber()) {
%>
  <fmt:formatNumber value="${entity[property.name]}"/>
<%
        }
        else {
            // TODO log warning no property found?
            done = false;
        }
    }
    if (!done) {
%>
${entity[property.name]}
<%
    }
%>
