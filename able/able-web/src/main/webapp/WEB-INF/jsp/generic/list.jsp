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
            <a href="http://lucene.apache.org/java/docs/queryparsersyntax.html">Search syntax</a>
        </p>

        <display:table name="actionBean.allEntities" id="row" pagesize="50" requestURI="logEvent" class="display">

            <c:forEach items="${actionBean.entityInfo.listProperties}" var="property">
                <display:column sortable="true" title="${property.displayName}">
                    <a:displayPropertyValue entity="${row}" propertyName="${property.name}"/>
                </display:column>
            </c:forEach>
            <display:column>
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
            </display:column>
        </display:table>

    </jsp:attribute>
    <jsp:attribute name="extraButtons">
        <s:submit class="btn" name="delete" value="Delete"/>
    </jsp:attribute>
</a:form>

<div id="savepage">
    <s:link beanclass="${actionBean.class.name}" event="generateList"><img style="border: none; vertical-align: middle;"
                                                                           src="${pageContext.request.contextPath}/gfx/save.gif"/>this
        page to
        /WEB-INF/jsp${actionBean.actionUri}/list.jsp
    </s:link>
</div>


</body>
</html>