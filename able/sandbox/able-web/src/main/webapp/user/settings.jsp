<%@ taglib prefix="ww" uri="/webwork" %>
<html>
<head>
    <title>Account Settings: ${currentUser.username}</title>
</head>

<body>
<ww:form method="post">
    <ww:label label="Username" value="%{currentUser.username}"/>
    <ww:label label="Email" value="%{currentUser.email}"/>
    <ww:textfield label="Name" name="currentUser.name"/>
    <ww:submit value="Update & Save" align="center"/>
</ww:form>
</body>
</html>