<%@ tag import="com.opensymphony.able.entity.Entities" %>
<%@ tag import="com.opensymphony.able.entity.EntityInfo" %>
<%@ tag import="com.opensymphony.able.entity.PropertyInfo" %>
<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%@ attribute name="property" type="java.lang.String" required="true" %>

<%
    Object value = null;
    EntityInfo entityInfo = Entities.info(entity);
    if (entityInfo != null) {
        PropertyInfo propertyInfo = entityInfo.getProperty(property);
        if (propertyInfo != null) {
            value = propertyInfo.getValue(entity);
        }
    }
    if (value != null) {
        out.print(value);
    }
%>