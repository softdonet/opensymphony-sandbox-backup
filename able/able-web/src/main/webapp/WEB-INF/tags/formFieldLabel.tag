<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        attribute name="label" required="true" type="java.lang.String"
        %><%@
        attribute name="defaultLabel" required="false" type="java.lang.String"
        %><%@
        attribute name="value" required="false" type="java.lang.String"
        %><%@
        attribute name="fragment" required="false" fragment="true"
        %><%@
        attribute name="aftertag" required="false" fragment="true"
        %><c:set var="name" value="${label}"/><%@
        include file="formFieldHeader.jsp"
        %><c:if test="${not empty value}"><a:text key="${value}"/></c:if><jsp:invoke fragment="fragment"/><%@
        include file="formFieldFooter.jsp"
        %>