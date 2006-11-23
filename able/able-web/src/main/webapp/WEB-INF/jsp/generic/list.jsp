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
            <s:text name="query"/>
            <s:image name="search" src="/gfx/search.gif" style="vertical-align: bottom;"/>
        </p>
       <table id="entities" class="display sortable autostripe">
            <thead>
                <tr>
                    <th></th>
                    <c:forEach items="${actionBean.entityInfo.listProperties}" var="property">
                        <th>
                            <a:text key="entity.${property.name}" defaultValue="${property.displayName}"/>
                        </th>
                    </c:forEach>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${actionBean.allEntities}" var="row" varStatus="rowstat">
                    <tr
                            <c:if test="${(rowstat.index % 2) == 1}">
                                <c:out value="class=even"/>
                            </c:if>
                            >
                        <td>

                            <stripes:checkbox name="entity" value="${row.id}"
                                              onclick="handleCheckboxRangeSelection(this, event);"/>
                        </td>
                        <c:forEach items="${actionBean.entityInfo.listProperties}" var="property">
                            <td>
                                <a:displayPropertyValue entity="${row}" propertyName="${property.name}"/>
                            </td>
                        </c:forEach>

                        <td class="buttons">
                            <s:link beanclass="${actionBean.class.name}" event="view">
                                <img src="${pageContext.request.contextPath}/gfx/view.gif" alt="view" style="border:none;"/>
                                <s:param name="entity" value="${row.id}"/>
                            </s:link>
                            <s:link beanclass="${actionBean.class.name}" event="edit">
                                <img src="${pageContext.request.contextPath}/gfx/edit.gif" alt="edit" style="border:none;"/>
                                <s:param name="entity" value="${row.id}"/>
                            </s:link>
                            <s:link beanclass="${actionBean.class.name}" event="delete">
                                <img src="${pageContext.request.contextPath}/gfx/delete.gif" alt="delete" style="border:none;"/>
                                <s:param name="entity" value="${row.id}"/>
                            </s:link>
                        </td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </jsp:attribute>
    <jsp:attribute name="extraButtons">
        <s:submit class="btn" name="delete" value="Delete"/>
    </jsp:attribute>
</a:form>

<div id="savepage">
    <s:link beanclass="${actionBean.class.name}" event="generateList"><img style="border: none; vertical-align: middle;" src="${pageContext.request.contextPath}/gfx/save.gif"/>this page to
        /WEB-INF/jsp${actionBean.actionUri}/list.jsp
    </s:link>
</div>


</body>
</html>