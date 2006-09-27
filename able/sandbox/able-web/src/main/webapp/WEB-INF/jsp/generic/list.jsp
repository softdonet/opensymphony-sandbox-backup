<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>${actionBean.entityInfo.entityName} Browse</title>
</head>

<body>

<h1>${actionBean.entityInfo.entityName} Browse</h1>

<stripes:form action="${actionBean.actionUri}">

    <stripes:errors/>

    <p>
        Search:
        <stripes:text name="query"/>
        <stripes:submit name="search" value="Go"/>
    </p>

    <table id="entities" class="display sortable autostripe">
        <thead>
            <tr>
                <th></th>
                <c:forEach items="${actionBean.entityInfo.listProperties}" var="property">
                    <th>${property.displayName}</th>
                </c:forEach>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${actionBean.allEntities}" var="row" varStatus="rowstat">
                <tr>
                    <td>
                        <stripes:checkbox name="entity" value="${row.id}"
                                          onclick="handleCheckboxRangeSelection(this, event);"/>
                    </td>
                    <c:forEach items="${actionBean.entityInfo.listProperties}" var="property">
                        <td>
                            <able:viewField entity="${row}" name="${property.name}"/>
                        </td>
                    </c:forEach>

                    <td>
                        <stripes:link href="${actionBean.actionUri}" event="view">
                            View
                            <stripes:param name="entity" value="${row.id}"/>
                        </stripes:link>
                        <stripes:link href="${actionBean.actionUri}" event="edit">
                            Edit
                            <stripes:param name="entity" value="${row.id}"/>
                        </stripes:link>
                        <stripes:link href="${actionBean.actionUri}" event="delete">
                            Delete
                            <stripes:param name="entity" value="${row.id}"/>
                        </stripes:link>
                    </td>

                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="buttons">
        <stripes:submit name="edit" value="New"/>
        <stripes:submit name="delete" value="Delete"/>
    </div>
</stripes:form>

</body>
</html>