<%@ tag import="com.opensymphony.able.entity.PropertyInfo" %>
<%@ tag import="com.opensymphony.able.view.Input" %>
<%@ tag import="com.opensymphony.able.view.InputType" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="able" tagdir="/WEB-INF/tags/able" %>
<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="entityName" type="java.lang.String" required="false" %>

<%
    if (entityName == null) {
        entityName = "entity.";
    }
    String fieldName = entityName + name;
    if (entity == null) {
        System.out.println("No entity!!!");
    }
    PropertyInfo propertyInfo = PropertyInfo.getPropertyInfo(entity, name);
    if (propertyInfo != null) {
        boolean done = true;
        Input input = propertyInfo.getInput();
        if (input == null) {
            done = false;
        }
        else {
            String customControl = input.customControl();
            if (customControl != null) {
                // TODO include some custom controll here...
            }
            InputType type = input.type();
            switch (type) {
            case Checkbox:
%>
<stripes:checkbox name="<%=fieldName%>"/>
<%
        break;

    case Combo:
%>
<able:selectField entity="${entity}" name="${name}"/>
<%
        break;

    case Password:
%>
<stripes:password name="<%=fieldName%>"/>
<%
        break;

    case Radio:
%>
<able:radioField entity="${entity}" name="${name}"/>
<%
        break;

    case Text:
%>
<stripes:text name="<%=fieldName%>"/>
<%
        break;

    case TextArea:
%>
<stripes:textarea name="<%=fieldName%>" formatPattern="medium"/>
<%
                break;
            default:
                done = false;
        }
    }
    if (!done) {
        if (propertyInfo.isCollection()) {
%>
<%-- TODO - what to do here - link to the edit page? --%>
<%
        }
        else if (propertyInfo.isPersistent() || propertyInfo.isEnum()) {
%>
<able:selectField entity="${entity}" name="${name}"/>
<%
}
else if (propertyInfo.isDate()) {
%>
<able:dateField entity="${entity}" name="${name}"/>
<%
}
else {
%>
<stripes:text name="<%=fieldName%>"/>
<%
            }
        }
    }
    else {
%>
No Property Info for <%=fieldName%>
<%
    }
%>
