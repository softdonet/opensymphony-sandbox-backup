<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>${actionBean.entityInfo.entityName} Browse</title>
</head>

<body>

<%--
We set these attributes rather than passing them through the tags because
these are very specific to the generic forms and shouldn't really be exposed
to the average user as attributes, as it will likely confuse them what they
are for.
--%>
<c:set var="form.submit.defaultLabel" value="New" scope="request"/>
<c:set var="form.defaultTitle" value="${actionBean.entityInfo.entityName} Browse" scope="request"/>

<a:form beanclass="${actionBean.class.name}" title="listTitle" submitEvent="edit">
    <jsp:attribute name="formBody">
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
                        <th><a:text key="entity.${property.name}" defaultValue="${property.displayName}"/></th>
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
                                <a:displayPropertyValue entity="${row}" property="${property}"/>
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
    </jsp:attribute>
    <jsp:attribute name="extraButtons">
        <stripes:submit name="delete" value="Delete"/>
    </jsp:attribute>
</a:form>

</body>
</html>