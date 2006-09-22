<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>Able Home Page</title>
</head>

<body>

Welcome to Able. These other users are also enjoying Able:

<ul>
    <c:forEach items="${actionBean.users}" var="user">
        <li>${user.name} (${user.email})</li>
    </c:forEach>
</ul>

<hr/>

<s:link beanclass="com.opensymphony.able.stripes.RegisterActionBean">Register</s:link> an account to be added to the list!

</body>
</html>