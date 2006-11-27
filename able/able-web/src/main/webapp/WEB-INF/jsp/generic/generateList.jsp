<%@page contentType="text/plain" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
&lt;%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %&gt;
<html>
<head>
    <title>${actionBean.entityInfo.entityName} Browse</title>
</head>

<body>

&lt;a:form beanclass="${actionBean.class.name}" title="listTitle" submitEvent="edit"
        submitDefaultLabel="New" defaultTitle="${actionBean.entityInfo.entityName} Browse"&gt;
    &lt;jsp:attribute name="formBody"&gt;
        <p>
            Search:
            &lt;stripes:text name="query"/&gt;
            &lt;stripes:submit name="search" value="Go"/&gt;
            <a href="http://lucene.apache.org/java/docs/queryparsersyntax.html">Search syntax</a>
        </p>

        <table id="entities" class="display sortable autostripe"&gt;
            <thead>
                <tr>
                    <th></th><c:forEach items="${actionBean.entityInfo.listProperties}" var="property">
                        <th>&lt;a:text key="entity.${property.name}" defaultValue="${property.displayName}"/&gt;</th></c:forEach>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                &lt;c:forEach items="\${actionBean.allEntities}" var="row" varStatus="rowstat">
                    <tr>
                        <td>
                            &lt;stripes:checkbox name="entity" value="\${row.id}"
                                              onclick="handleCheckboxRangeSelection(this, event);"/>
                        </td>
                        <c:forEach items="${actionBean.entityInfo.listProperties}" var="property">
                            <td>
                                &lt;a:displayPropertyValue entity="\${row}" propertyName="${property.name}"/>
                            </td>
                        </c:forEach>

                        <td>
                            &lt;stripes:link beanclass="${actionBean.class.name}" event="view"&gt;
                                View
                                &lt;stripes:param name="entity" value="\${row.id}"/&gt;
                            &lt;/stripes:link&gt;
                            &lt;stripes:link beanclass="${actionBean.class.name}" event="edit"&gt;
                                Edit
                                &lt;stripes:param name="entity" value="\${row.id}"/&gt;
                            &lt;/stripes:link&gt;
                            &lt;stripes:link beanclass="${actionBean.class.name}" event="delete"&gt;
                                Delete
                                &lt;stripes:param name="entity" value="\${row.id}"/&gt;
                            &lt;/stripes:link&gt;
                        </td>

                    </tr>
                &lt;/c:forEach&gt;
            </tbody>
        </table>
    &lt;/jsp:attribute&gt;
    &lt;jsp:attribute name="extraButtons"&gt;
        &lt;stripes:submit name="delete" value="Delete"/&gt;
    &lt;/jsp:attribute&gt;
&lt;/a:form>


</body>
</html>