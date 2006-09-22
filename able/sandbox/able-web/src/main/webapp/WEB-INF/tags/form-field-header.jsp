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
    <td valign="top"><s:label for="${name}"/></td>
    <td>