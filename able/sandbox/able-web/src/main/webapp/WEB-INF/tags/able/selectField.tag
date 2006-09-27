<%@ tag import="com.opensymphony.able.entity.PropertyInfo" %>
<%@ tag import="java.util.List" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%
    String label = "";
    PropertyInfo propertyInfo = PropertyInfo.getPropertyInfo(entity, name);
    if (propertyInfo != null) {
        List<PropertyInfo> list = propertyInfo.getPropertyEntityInfo().getViewFieldProperties();
        if (!list.isEmpty()) {
            label = list.get(0).getName();
        }
    }
    if (propertyInfo.isEnum()) {
%>
<stripes:select name="entity.${name}" value="Medium">
    <stripes:options-collection collection="${actionBean.allValues[name]}"/>
</stripes:select>
<%
    }
    else {
%>
<stripes:select name="entity.${name}" value="Medium">
    <stripes:options-collection collection="${actionBean.allValues[name]}" label="<%= label %>" value="id"/>
</stripes:select>
<%
    }
%>
