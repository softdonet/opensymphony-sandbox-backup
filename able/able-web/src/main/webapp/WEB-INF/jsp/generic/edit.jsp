<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
<title><a:text key="editTitle" defaultValue="Edit ${actionBean.entityInfo.entityName}"/></title>
</head>

<body>

<a:form beanclass="${actionBean.class.name}" title="viewTitle" submitEvent="save" cancelEvent="cancel"
        defaultTitle="Edit ${actionBean.entityInfo.entityName}" submitDefaultLabel="Save" cancelDefaultLabel="Back">
    <stripes:hidden name="entity"/>

    <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
        <a:formFieldGeneric entity="${actionBean.entity}" name="${property.name}" defaultLabel="${property.displayName}"/>
    </c:forEach>
</a:form>

<div id="savepage">
    <s:link beanclass="${actionBean.class.name}" event="generateEdit"><img style="border: none; vertical-align: middle;" src="${pageContext.request.contextPath}/gfx/save.gif"/>this page to
        /WEB-INF/jsp${actionBean.actionUri}/edit.jsp
    </s:link>
</div>

</body>
</html>
