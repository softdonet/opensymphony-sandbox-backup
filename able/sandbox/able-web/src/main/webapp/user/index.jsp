<%@ include file="/WEB-INF/taglibs/header.jsp"%>
<html>
<body>

<stripes:form action="/User.action">

<%--TODO - can we avoid this?
 --%>
  <stripes:useActionBean binding="/User.action" var="actionBean" />

  <stripes:errors />

  <table class="display">
    <tr>
      <th></th>
      <th>ID</th>
      <th>User Name</th>
      <th>Name</th>
      <th>Email</th>
      <th></th>
    </tr>

    <c:forEach items="${actionBean.allEntities}" var="row" varStatus="rowstat">
      <tr>
        <td><stripes:checkbox name="id" value="${row.id}"
                              onclick="handleCheckboxRangeSelection(this, event);"/></td>
        <td>${row.id}</td>
        <td>
        <stripes:link href="/User.action" event="view">${row.username}
        <stripes:param name="id" value="${row.id}" />
        </stripes:link>
        </td>
        
        <td>${row.name}</td>
        <td>${row.email}</td>
        <td><stripes:link href="/User.action" event="edit">
                                Edit
                                <stripes:param name="id" value="${row.id}" /></stripes:link>
         <stripes:link href="/User.action" event="delete">
                                Delete
                                <stripes:param name="id" value="${row.id}" /></stripes:link></td>
      </tr>
    </c:forEach>
  </table>

  <div class="buttons">
  <stripes:submit name="edit" value="New" />
  <stripes:submit name="bulkEdit" value="Bulk Edit" />
  <stripes:submit name="delete" value="Delete" />
  </div>
</stripes:form>

</body>
</html>
