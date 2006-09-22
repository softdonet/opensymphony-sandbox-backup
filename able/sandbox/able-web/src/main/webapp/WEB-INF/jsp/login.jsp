<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<html>
<head>
    <title>Login</title>
</head>

<body>

<a:form beanclass="com.opensymphony.able.stripes.LoginActionBean"
         title="title" description="description" submitEvent="login">
    <a:form-field-text name="username"/>
    <a:form-field-password name="password"/>
    <a:form-field-checkbox name="remember"/>
</a:form>

</body>
</html>