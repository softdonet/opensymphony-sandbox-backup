<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<body>

<stripes:form action="${actionBean.actionUri}">

    <h1>
        <a:viewSummary entity="${actionBean.owner}"/>
    </h1>

    <stripes:hidden name="owner"/>
    <stripes:errors/>

    <c:choose>
        <c:when test="${actionBean.displayType == 'PickList'}">
            <%@ include file="/WEB-INF/jsp/generic/editCollection/pickList.jsp" %>
        </c:when>
        <c:otherwise>
            <%@ include file="/WEB-INF/jsp/generic/editCollection/checkbox.jsp" %>
        </c:otherwise>
    </c:choose>

</stripes:form>
</body>
</html>
