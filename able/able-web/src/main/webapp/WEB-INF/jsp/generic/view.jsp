<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title><a:text key="viewTitle" defaultValue="View ${actionBean.entityInfo.entityName}"/></title>
</head>

<body>

<a:form beanclass="${actionBean.class.name}" title="viewTitle" submitEvent="edit" cancelEvent="cancel"
        defaultTitle="View ${actionBean.entityInfo.entityName}" submitDefaultLabel="Edit" cancelDefaultLabel="Back">
    <stripes:hidden name="entity"/>
     <br>
    <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
        <a:formFieldLabel label="entity.${property.name}" defaultLabel="${property.displayName}">
            <jsp:attribute name="fragment">
                <a:displayPropertyValue entity="${actionBean.entity}" propertyName="${property.name}"/>
            </jsp:attribute>
        </a:formFieldLabel>
    </c:forEach>
    <br>
    <br>
</a:form>

<div id="savepage">
    <s:link beanclass="${actionBean.class.name}" event="generateView"><img style="border: none; vertical-align: middle;" src="${pageContext.request.contextPath}/gfx/save.gif"/>this page to
        /WEB-INF/jsp${actionBean.actionUri}/list.jsp
    </s:link>
</div>

</body>
</html>
