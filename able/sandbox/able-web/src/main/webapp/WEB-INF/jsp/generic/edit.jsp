<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
<title><a:text key="editTitle" defaultValue="Edit ${actionBean.entityInfo.entityName}"/></title>
</head>

<body>

<%--
We set these attributes rather than passing them through the tags because
these are very specific to the generic forms and shouldn't really be exposed
to the average user as attributes, as it will likely confuse them what they
are for.
--%>
<c:set var="form.submit.defaultLabel" value="Save" scope="request"/>
<c:set var="form.cancel.defaultLabel" value="Back" scope="request"/>
<c:set var="form.defaultTitle" value="Edit ${actionBean.entityInfo.entityName}" scope="request"/>

<a:form beanclass="${actionBean.class.name}" title="viewTitle" submitEvent="save" cancelEvent="cancel">
    <stripes:hidden name="entity"/>

    <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
        <c:set var="form.field.defaultLabel" value="${property.displayName}" scope="request"/>
        <a:formFieldGeneric entity="${actionBean.entity}" name="${property.name}"/>
    </c:forEach>
</a:form>

</body>
</html>
