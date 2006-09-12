<%@ include file="/WEB-INF/header.jsp"%>
<html>
<head>
<title>Able Example</title>
</head>
<body>
<h1>Able Example</h1>

<jsp:useBean id="helper" class="com.opensymphony.able.entity.Entities" scope="page" />

<ul>
  <c:forEach var="entity" items="${helper.entities}">
    <li><stripes:link href="/views/entity/${entity.entityName}/index.jsp">${entity.entityName} List</stripes:link></li>
  </c:forEach>
</ul>
</div>

</body>
</html>
