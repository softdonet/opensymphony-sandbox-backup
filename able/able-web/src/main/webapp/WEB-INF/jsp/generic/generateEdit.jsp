<%@page contentType="text/plain" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
&lt;%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %&gt;
<html>
<head>
<title>&lt;a:text key="editTitle" defaultValue="Edit ${actionBean.entityInfo.entityName}"/&gt;</title>
</head>

<body>

&lt;a:form beanclass="${actionBean.class.name}" title="viewTitle" submitEvent="save" cancelEvent="cancel"
        defaultTitle="Edit ${actionBean.entityInfo.entityName}" submitDefaultLabel="Save" cancelDefaultLabel="Back"&gt;
    &lt;stripes:hidden name="entity"/>

    <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
        &lt;a:formFieldGeneric entity="\${actionBean.entity}" name="${property.name}" defaultLabel="${property.displayName}"/&gt;
    </c:forEach>
&lt;/a:form&gt;

</body>
</html>
