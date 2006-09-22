<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<html>
<head>
    <title>Register</title>
</head>
<body>

<a:form beanclass="com.opensymphony.able.stripes.RegisterActionBean"
         title="title" description="description"
         submitEvent="register">
    <a:form-field-text name="user.username"/>
    <a:form-field-text name="user.name"/>
    <a:form-field-text name="user.email"/>
    <a:form-field-password name="password"/>
    <a:form-field-password name="passwordAgain"/>
</a:form>

</body>
</html>