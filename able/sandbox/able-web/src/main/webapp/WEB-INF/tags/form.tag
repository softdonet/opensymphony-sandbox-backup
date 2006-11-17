<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="beanclass" required="true" type="java.lang.String" %>
<%@ attribute name="name" required="false" type="java.lang.String" %>
<%@ attribute name="id" required="false" type="java.lang.String" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="defaultTitle" required="false" type="java.lang.String" %>
<%@ attribute name="description" required="false" type="java.lang.String" %>
<%@ attribute name="submitEvent" required="true" type="java.lang.String" %>
<%@ attribute name="submitDefaultLabel" required="false" type="java.lang.String" %>
<%@ attribute name="cancelEvent" required="false" type="java.lang.String" %>
<%@ attribute name="cancelDefaultLabel" required="false" type="java.lang.String" %>
<%@ attribute name="formBody" required="false" fragment="true" %>
<%@ attribute name="extraButtons" required="false" fragment="true" %>
<c:if test="${empty name}"><c:set var="name" value="${af:extractFormName(beanclass, submitEvent)}"/></c:if>
<c:if test="${empty id}"><c:set var="id" value="${name}"/></c:if>
<div>
    <h1><a:text key="${title}" defaultValue="${defaultTitle}"/></h1>
    <c:if test="${description ne null}">
        <b><a:text key="${description}"/></b>
    </c:if>

    <s:form name="${name}" id="${name}" method="post" beanclass="${beanclass}">

        <stripes:errors/>

        <c:choose>
            <c:when test="${not empty formBody}">
                <jsp:invoke fragment="formBody"/>
            </c:when>
            <c:otherwise>
                <table id="${id}_table" class="display">
                    <tbody id="${id}_table_body"><jsp:doBody/></tbody>
                </table>
            </c:otherwise>
        </c:choose>

        <div class="buttons">
            <s:submit name="${submitEvent}">${submitDefaultLabel != null ? submitDefaultLabel : "Submit"}</s:submit>
            <c:if test="${not empty cancelEvent}">
                <s:submit name="${cancelEvent}">${cancelDefaultLabel != null ? cancelDefaultLabel : "Cancel"}</s:submit>
            </c:if>
            <jsp:invoke fragment="extraButtons"/>
        </div>
    </s:form>
</div>
