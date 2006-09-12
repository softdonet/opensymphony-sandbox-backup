<%@ include file="/WEB-INF/header.jsp"%>
<html>
<head>
<title>Able Example</title>
</head>
<body>
<h1>Able Example</h1>

<p>Welcome to the starter web application for Able</p>

<p>The best place to start with is with the <a href="/views/entity/bug/index.jsp">Bugs List</a> which shows a reasonably complex entity model you can use as a start</p>

<h2>Entity List</h2>
<jsp:useBean id="helper" class="com.opensymphony.able.entity.Entities" scope="page" />

<ul>
  <c:forEach var="entity" items="${helper.entities}">
    <li><stripes:link href="/views/entity/${entity.entityUri}/index.jsp">${entity.entityName} List</stripes:link></li>
  </c:forEach>
</ul>
</div>

</body>
</html>
