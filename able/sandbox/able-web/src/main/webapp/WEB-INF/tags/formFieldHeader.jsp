<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
%><c:if test="${empty id}"><c:set var="id" value="${name}"/></c:if><s:errors field="${name}">
    <s:errors-header>
    <tr id="${id}_error_row">
        <td colspan="2">
    </s:errors-header>
    <li><s:individual-error/></li>
    <s:errors-footer>
        </td>
    </tr>
    </s:errors-footer>
</s:errors><tr id="${id}_row">
    <th valign="top"><s:label for="${name}"
            ><c:choose>
                <c:when test="${empty label}"><a:text key="${name}" defaultValue="${requestScope['form.field.defaultLabel']}"/></c:when>
                <c:otherwise><a:text key="${label}" defaultValue="${requestScope['form.field.defaultLabel']}"/></c:otherwise>
            </c:choose></s:label></th>
    <td>