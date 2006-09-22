<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<%@ tag body-content="scriptless" %>
<%@ attribute name="beanclass" required="true" type="java.lang.String" %>
<%@ attribute name="name" required="false" type="java.lang.String" %>
<%@ attribute name="id" required="false" type="java.lang.String" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="description" required="false" type="java.lang.String" %>
<%@ attribute name="submitEvent" required="true" type="java.lang.String" %>
<c:if test="${empty name}"><c:set var="name" value="${af:extractFormName(beanclass, submitEvent)}"/></c:if>
<c:if test="${empty id}"><c:set var="id" value="${name}"/></c:if>
<div>
    <s:form name="${name}" id="${name}" method="post" beanclass="${beanclass}">
        <table id="${id}_table">
            <thead id="${id}_table_head">
                <tr>
                    <c:if test="${description ne null}">
                        <td colspan="2"><b><a:text key="${title}"/></b>: <a:text key="${description}"/></td>
                    </c:if>
                    <c:if test="${description eq null}">
                        <td colspan="2"><b><a:text key="${title}"/></b></td>
                    </c:if>
                </tr>
            </thead>
            <tbody id="${id}_table_body"><jsp:doBody/></tbody>
            <tbody id="${id}_table_buttons">
                <tr>
                    <td colspan="2">
                        <s:submit name="${submitEvent}"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </s:form>
</div>
