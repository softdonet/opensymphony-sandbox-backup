<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<html>
<head>
    <title>Login</title>
</head>

<body>

<a:form beanclass="com.opensymphony.able.demo.action.LoginActionBean"
         title="title" description="description" submitEvent="login">
    <a:formFieldText name="username"/>
    <a:formFieldPassword name="password"/>
    <a:formFieldCheckbox name="remember"/>
</a:form>

</body>
</html>