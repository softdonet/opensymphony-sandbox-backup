<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
<title>View ${actionBean.entityInfo.entityName}</title>
</head>

<body>

<h1>View ${actionBean.entityInfo.entityName}</h1>

<stripes:form action="${actionBean.actionUri}">
    <stripes:hidden name="entity"/>

    <stripes:errors/>

    <table class="display">

        <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
            <tr>
                <th>
                    <stripes:label for="entity.${property.name}">${property.displayName}</stripes:label>
                    :
                </th>
                <td>
                    <able:viewField entity="${actionBean.entity}" name="${property.name}"/>
                </td>
            </tr>
        </c:forEach>

    </table>

    <div class="buttons">
        <stripes:submit name="edit" value="Edit"/>
        <stripes:submit name="cancel" value="Back"/>
    </div>
</stripes:form>

</body>
</html>
