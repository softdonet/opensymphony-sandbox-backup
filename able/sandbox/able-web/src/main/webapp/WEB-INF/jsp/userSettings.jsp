<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<html>
<head>
    <title>User Settings</title>
</head>

<body>

<a:form beanclass="com.opensymphony.able.stripes.UserSettingsActionBean"
         title="updateTitle" description="updateDescription"
         submitEvent="update">
    <input type="hidden" name="user" value="${actionBean.userId}"/>
    <a:form-field-text name="user.name"/>
    <a:form-field-text name="user.email"/>
    <a:form-field-password name="password"/>
    <a:form-field-password name="passwordAgain"/>
</a:form>


</body>
</html>