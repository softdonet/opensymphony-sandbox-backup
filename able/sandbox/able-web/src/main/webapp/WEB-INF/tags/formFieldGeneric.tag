<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ tag import="com.opensymphony.able.entity.PropertyInfo" %>
<%@ tag import="com.opensymphony.able.view.Input" %>
<%@ tag import="com.opensymphony.able.view.InputType" %>
<%@ tag import="java.util.List" %>
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
<a:formFieldCheckbox name="<%=fieldName%>"/>
<%
        break;

    case Combo:
%>
<a:formFieldSelect collection="${actionBean.allValues[name]}" name="entity.${name}"/>
<%
        break;

    case Password:
%>
<a:formFieldPassword name="<%=fieldName%>"/>
<%
        break;

    case Radio:
%>
<a:formFieldRadio collection="${actionBean.allValues[name]}" name="<%= fieldName %>" isEnum="<%= propertyInfo.isEnum() %>"/>
<%
        break;

    case Text:
%>
<a:formFieldText name="<%=fieldName%>"/>
<%
        break;

    case TextArea:
%>
<a:formFieldTextArea name="<%=fieldName%>" rows="3" cols="50"/>
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
} else if (propertyInfo.isPersistent() || propertyInfo.isEnum()) {
    String label = "";
    List<PropertyInfo> list = propertyInfo.getPropertyEntityInfo().getNameProperties();
    if (!list.isEmpty()) {
        label = list.get(0).getName();
    }

    if (propertyInfo.isEnum()) {
%>
<a:formFieldSelect collection="${actionBean.allValues[name]}" name="entity.${name}"/>
<%
    } else {
%>
<a:formFieldSelect collection="${actionBean.allValues[name]}" value="${actionBean.entity[name].id}" listValue="id" listLabel="<%= label %>" name="entity.${name}"/>
<%
    }
}
else if (propertyInfo.isDate()) {
%>
<a:formFieldDate name="entity.${name}"/>
<%
}
else {
%>
<a:formFieldText name="<%=fieldName%>"/>
<%
            }
        }
    }
    else {
%>
<a:formFieldLabel label="unknown" value="No Property Info for <%=fieldName%>"/>
<%
    }
%>
