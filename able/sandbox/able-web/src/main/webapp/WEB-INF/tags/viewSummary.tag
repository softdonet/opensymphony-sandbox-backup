<%@ tag import="com.opensymphony.able.introspect.Entities" %>
<%@ tag import="com.opensymphony.able.introspect.EntityInfo" %>
<%@ tag import="com.opensymphony.able.introspect.PropertyInfo" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%
    if (entity != null) {
        Object value = null;
        Object pk = "";
        String href = "";
        EntityInfo entityInfo = Entities.info(entity);
        if (entityInfo != null) {
            PropertyInfo propertyInfo = entityInfo.getNameProperty();
            if (propertyInfo != null) {
                value = propertyInfo.getValue(entity);
            }
            else {
                value = entity.toString();
            }
            pk = entityInfo.getIdValue(entity);
            href = entityInfo.getActionUri();
        }
        if (value == null && entity != null) {
            value = entity.toString();
        }
%>
<stripes:link href="<%= href %>"><%= value %><stripes:param name="entity" value="<%= pk %>"/><stripes:param name="view"/></stripes:link>
<%
    }
%>