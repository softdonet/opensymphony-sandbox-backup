<%@ include file="/WEB-INF/header.jsp"%>
<html>
<body>

<stripes:form action="/User.action">

<%--TODO - can we avoid this? --%>
  <stripes:useActionBean binding="/User.action" var="actionBean" />


  <stripes:errors />
  <stripes:hidden name="id"/>

  <table class="display">
    <tr>
      <th>ID</th>
      <th>User Name</th>
      <th>Name</th>
      <th>Email</th>
      <th></th>
    </tr>

    <c:forEach items="${actionBean.bulkEditEntities}" var="row" varStatus="rowstat">
      <tr>
        <td>${row.id}
        </td>
        <td><stripes:text name="bulkEditEntities[${rowstat.index}].username" /></td>
        <td><stripes:text name="bulkEditEntities[${rowstat.index}].name" /></td>
        <td><stripes:text name="bulkEditEntities[${rowstat.index}].email" /></td>
      </tr>
    </c:forEach>
  </table>

  <div class="buttons"><stripes:submit name="save" value="Save" /> <stripes:submit name="cancel" value="Cancel" /></div>
</stripes:form>

</body>
</html>
