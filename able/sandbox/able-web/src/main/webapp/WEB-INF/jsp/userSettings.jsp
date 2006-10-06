<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<html>
<head>
    <title>User Settings</title>
</head>

<body>

<a:form beanclass="com.opensymphony.able.stripes.UserSettingsActionBean"
         title="updateTitle"
         submitEvent="update">
    <input type="hidden" name="user" value="${actionBean.userId}"/>
    <a:formFieldText name="user.name"/>
    <a:formFieldText name="user.email"/>
    <a:formFieldPassword name="password"/>
    <a:formFieldPassword name="passwordAgain"/>
</a:form>


</body>
</html>