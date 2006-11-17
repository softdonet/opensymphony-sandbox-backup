<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>Able Home Page</title>
</head>

<body>

<p>Welcome to the starter web application for Able</p>

<h2>Entity List</h2>

<p>The following are all the entities you can modify.
    A good starting point is the <stripes:link href="/bug">Bug List</stripes:link></p>

<ul>
  <c:forEach var="entity" items="${actionBean.entities}">
    <li><stripes:link href="/${entity.entityUri}">${entity.entityName} List</stripes:link></li>
  </c:forEach>
</ul>


<h2>Current users</h2>

<p>These other users are also enjoying Able:</p>

<ul>
    <c:forEach items="${actionBean.users}" var="user">
        <li>${user.name} (${user.email})</li>
    </c:forEach>
</ul>

<s:link beanclass="com.opensymphony.able.demo.action.RegisterActionBean">Register</s:link> an account to be added to the list!

</body>
</html>