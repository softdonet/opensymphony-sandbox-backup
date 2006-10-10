<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>${actionBean.entityInfo.entityName} Browse</title>
</head>

<body>

<a:form beanclass="${actionBean.class.name}" title="listTitle" submitEvent="edit" 
        submitDefaultLabel="New" defaultTitle="${actionBean.entityInfo.entityName} Browse">
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
                                <a:displayPropertyValue entity="${row}" propertyName="${property.name}"/>
                            </td>
                        </c:forEach>

                        <td>
                            <stripes:link beanclass="${actionBean.class.name}" event="view">
                                View
                                <stripes:param name="entity" value="${row.id}"/>
                            </stripes:link>
                            <stripes:link beanclass="${actionBean.class.name}" event="edit">
                                Edit
                                <stripes:param name="entity" value="${row.id}"/>
                            </stripes:link>
                            <stripes:link beanclass="${actionBean.class.name}" event="delete">
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

<div align="center">
    <s:link beanclass="${actionBean.class.name}" event="generateList">Save this page to /WEB-INF/jsp${actionBean.actionUri}/list.jsp</s:link>
    <br/>
    <br/>
</div>

</body>
</html>