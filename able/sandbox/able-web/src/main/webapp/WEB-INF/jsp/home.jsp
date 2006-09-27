<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>Able Home Page</title>
</head>

<body>

<p>Welcome to the starter web application for Able</p>

<p>Useful links</p>

<ul>
    <li><stripes:link href="/person">Person List</stripes:link></li>
</ul>

<h2>Entity List</h2>

<ul>
  <c:forEach var="entity" items="${actionBean.entities}">
    <li><stripes:link href="/${entity.entityUri}">${entity.entityName} List</stripes:link></li>
  </c:forEach>
</ul>


<h2>Current users</h2>
These other users are also enjoying Able:

<ul>
    <c:forEach items="${actionBean.users}" var="user">
        <li>${user.name} (${user.email})</li>
    </c:forEach>
</ul>

<hr/>

<s:link beanclass="com.opensymphony.able.stripes.RegisterActionBean">Register</s:link> an account to be added to the list!

</body>
</html>