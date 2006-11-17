<%@ taglib prefix="ww" uri="/webwork" %>
<html>
<head><title></title>
</head>
<body>

<h2>User Login</h2>

<ww:form method="post">
    <ww:textfield label="Username" name="username"/>
    <ww:password label="Password" name="password"/>

    <ww:checkbox label="Remember me please" name="remember" value="false" />
    <ww:submit value="Submit"/>
</ww:form>

<p/>

<a href="<ww:url action="forgotPassword" method="input"/>">Forgot your password?</a>

</body>
</html>
