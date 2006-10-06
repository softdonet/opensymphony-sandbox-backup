<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title><a:text key="viewTitle" defaultValue="View ${actionBean.entityInfo.entityName}"/></title>
</head>

<body>

<%--
We set these attributes rather than passing them through the tags because
these are very specific to the generic forms and shouldn't really be exposed
to the average user as attributes, as it will likely confuse them what they
are for.
--%>
<c:set var="form.submit.defaultLabel" value="Edit" scope="request"/>
<c:set var="form.cancel.defaultLabel" value="Back" scope="request"/>
<c:set var="form.defaultTitle" value="View ${actionBean.entityInfo.entityName}" scope="request"/>

<a:form beanclass="${actionBean.class.name}" title="viewTitle" submitEvent="edit" cancelEvent="cancel">
    <stripes:hidden name="entity"/>

    <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
        <c:set var="form.field.defaultLabel" value="${property.displayName}" scope="request"/>
        <a:formFieldLabel label="entity.${property.name}">
            <jsp:attribute name="fragment">
                <a:displayPropertyValue entity="${actionBean.entity}" property="${property}"/>
            </jsp:attribute>
        </a:formFieldLabel>
    </c:forEach>
</a:form>

</body>
</html>
