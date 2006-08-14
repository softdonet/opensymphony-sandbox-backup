<%@ taglib prefix="ww" uri="/webwork" %>
<html>
<head>
    <title></title>
</head>

<body>

<h2>New User Registration</h2>

    <ww:form method="post">
       <ww:textfield label="Desired username" name="user.username"/>
       <ww:textfield label="Full name" name="user.name"/>
       <ww:textfield label="Email" name="user.email"/>
       <ww:password label="Password" name="password"/>
       <ww:password label="Confirm password" name="confirmPassword"/>
       <ww:checkbox label="Weekly Newsletter" name="user.newsletter"/>
       <ww:submit value="Register"/>

    </ww:form>

 </body>
</html>