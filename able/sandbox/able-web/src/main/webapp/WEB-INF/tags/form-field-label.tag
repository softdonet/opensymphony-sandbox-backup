<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        attribute name="label" required="true" type="java.lang.String"
        %><%@
        attribute name="value" required="true" type="java.lang.String"
        %><%@
        attribute name="aftertag" required="false" fragment="true"
        %><c:set var="name" value="${label}"/><%@
        include file="form-field-header.jsp"
        %><a:text key="${value}"/><%@
        include file="form-field-footer.jsp"
        %>