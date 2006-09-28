<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
<title>Edit ${actionBean.entityInfo.entityName}</title>
</head>

<body>

<h1>Edit ${actionBean.entityInfo.entityName}</h1>

<stripes:form action="${actionBean.actionUri}">
    <stripes:hidden name="entity" value="${actionBean.id}"/>

    <stripes:errors/>

    <table class="display">

        <c:forEach items="${actionBean.entityInfo.editProperties}" var="property">
            <tr>
                <th>
                    <stripes:label for="entity.${property.name}">${property.displayName}</stripes:label>
                    :
                </th>
                <td>
                    <able:editField entity="${actionBean.entity}" name="${property.name}"/>
                </td>
            </tr>
        </c:forEach>

    </table>

    <div class="buttons">
        <stripes:submit name="save" value="Save"/>
        <stripes:submit name="cancel" value="Cancel"/>
    </div>
</stripes:form>

</body>
</html>
