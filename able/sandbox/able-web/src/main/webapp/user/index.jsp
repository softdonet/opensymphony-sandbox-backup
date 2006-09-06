<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<html>
<body>

<stripes:form action="/User.action">

  <stripes:useActionBean binding="/User.action" var="actionBean" />


  <stripes:errors />

  <table class="display">
    <tr>
      <th></th>
      <th>ID</th>
      <th>Opened On</th>
      <th>Description</th>
      <th>Component</th>
      <th>Priority</th>
      <th>Status</th>
      <th>Owner</th>
      <th></th>
    </tr>

    <c:forEach items="${actionBean.allEntities}" var="row" varStatus="rowstat">
      <tr>
        <td>${row.id}</td>
        <td>${row.username}</td>
        <td>${row.name}</td>
        <td>${row.email}</td>
        <td><stripes:link href="/User.action" event="view">
                                Edit
                                <stripes:param name="id" value="${row.id}" />
        </stripes:link></td>
      </tr>
    </c:forEach>
  </table>

  <div class="buttons"><stripes:submit name="view" value="Bulk Edit" /></div>
</stripes:form>
</body>
</html>
