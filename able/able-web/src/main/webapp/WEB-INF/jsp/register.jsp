<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<html>
<head>
    <title>Register</title>
</head>
<body>

<a:form beanclass="com.opensymphony.able.demo.action.RegisterActionBean"
         title="title" description="description"
         submitEvent="register">
    <a:formFieldText name="user.username"/>
    <a:formFieldText name="user.name"/>
    <a:formFieldText name="user.email"/>
    <a:formFieldPassword name="password"/>
    <a:formFieldPassword name="passwordAgain"/>
</a:form>

</body>
</html>