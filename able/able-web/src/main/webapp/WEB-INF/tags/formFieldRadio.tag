<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        include file="formFieldCommonAttrs.tag"
        %><%@
        attribute name="isEnum" required="false" type="java.lang.Boolean"
        %><%@
        attribute name="collection" required="true" type="java.util.Collection"
        %><%@
        include file="formFieldHeader.jsp"
        %><c:forEach items="${collection}" var="value">
                <c:choose>
                    <c:when test="${isEnum}">
                        <c:set var="actualValue" value="${stripes:enumName(value)}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="actualValue" value="${value}"/>
                    </c:otherwise>
                </c:choose>
              <stripes:radio id="${id}.${actualValue}" name="${name}" value="${actualValue}"
                checked="New" />
              <stripes:label for="${id}.${actualValue}">${actualValue}</stripes:label>
        </c:forEach> <%@
        include file="formFieldFooter.jsp"
        %>