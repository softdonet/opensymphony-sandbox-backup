<%@ tag import="com.opensymphony.able.entity.PropertyInfo" %>
<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true"  %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    PropertyInfo propertyInfo = PropertyInfo.getPropertyInfo(entity, name);
    if (propertyInfo != null && propertyInfo.isEnum()) {
%>
<c:forEach var="value" items="${actionBean.allValues[name]}">
  <stripes:radio id="entity.${name}.${stripes:enumName(value)}" name="entity.${name}" value="${stripes:enumName(value)}"
    checked="New" />
  <stripes:label for="entity.${name}.${stripes:enumName(value)}">${stripes:enumName(value)}</stripes:label>
</c:forEach>
<%
    } else {
%>
<c:forEach var="value" items="${actionBean.allValues[name]}">
  <stripes:radio id="entity.${name}.${value.id}" name="entity.${name}" value="${value}.id"
    checked="New" />
  <stripes:label for="entity.${name}.${value.id}">${value}.name</stripes:label>
</c:forEach>
<%
    }
%>
