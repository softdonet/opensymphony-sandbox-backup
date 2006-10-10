<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title><a:text key="viewTitle" defaultValue="View ${actionBean.entityInfo.entityName}"/></title>
</head>

<body>

<a:form beanclass="${actionBean.class.name}" title="viewTitle" submitEvent="edit" cancelEvent="cancel"
        defaultTitle="View ${actionBean.entityInfo.entityName}" submitDefaultLabel="Edit" cancelDefaultLabel="Back">
    <stripes:hidden name="entity"/>

    <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
        <a:formFieldLabel label="entity.${property.name}" defaultLabel="${property.displayName}">
            <jsp:attribute name="fragment">
                <a:displayPropertyValue entity="${actionBean.entity}" propertyName="${property.name}"/>
            </jsp:attribute>
        </a:formFieldLabel>
    </c:forEach>
</a:form>

<div align="center">
    <s:link beanclass="${actionBean.class.name}" event="generateView">Save this page to /WEB-INF/jsp${actionBean.actionUri}/view.jsp</s:link>
    <br/>
    <br/>
</div>

</body>
</html>
