<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="beanclass" required="true" type="java.lang.String" %>
<%@ attribute name="name" required="false" type="java.lang.String" %>
<%@ attribute name="id" required="false" type="java.lang.String" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="description" required="false" type="java.lang.String" %>
<%@ attribute name="submitEvent" required="true" type="java.lang.String" %>
<%@ attribute name="cancelEvent" required="false" type="java.lang.String" %>
<%@ attribute name="formBody" required="false" fragment="true" %>
<%@ attribute name="extraButtons" required="false" fragment="true" %>
<c:if test="${empty name}"><c:set var="name" value="${af:extractFormName(beanclass, submitEvent)}"/></c:if>
<c:if test="${empty id}"><c:set var="id" value="${name}"/></c:if>
<div>
    <h1><a:text key="${title}" defaultValue="${requestScope['form.defaultTitle']}"/></h1>
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
            <s:submit name="${submitEvent}">
                ${requestScope['form.submit.defaultLabel'] != null ? requestScope['form.submit.defaultLabel'] : "Submit"}
            </s:submit>
            <c:if test="${not empty cancelEvent}">
                <s:submit name="${cancelEvent}">
                    ${requestScope['form.cancel.defaultLabel'] != null ? requestScope['form.cancel.defaultLabel'] : "Submit"}
                </s:submit>
            </c:if>
            <jsp:invoke fragment="extraButtons"/>
        </div>
    </s:form>
</div>
