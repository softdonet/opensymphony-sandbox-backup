<%@page contentType="text/plain" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
&lt;%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %&gt;
<html>
<head>
    <title>&lt;a:text key="viewTitle" defaultValue="View ${actionBean.entityInfo.entityName}"/&gt;</title>
</head>

<body>

&lt;a:form beanclass="${actionBean.class.name}" title="viewTitle" submitEvent="edit" cancelEvent="cancel"
        defaultTitle="View ${actionBean.entityInfo.entityName}" submitDefaultLabel="Edit" cancelDefaultLabel="Back"&gt;
    &lt;stripes:hidden name="entity"/>

    <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
        &lt;a:formFieldLabel label="entity.${property.name}" defaultLabel="${property.displayName}"&gt;
            &lt;jsp:attribute name="fragment"&gt;
                &lt;a:displayPropertyValue entity="\${actionBean.entity}" propertyName="${property.name}"/&gt;
            &lt;/jsp:attribute&gt;
        &lt;/a:formFieldLabel&gt;
    </c:forEach>
&lt;/a:form&gt;

</body>
</html>
