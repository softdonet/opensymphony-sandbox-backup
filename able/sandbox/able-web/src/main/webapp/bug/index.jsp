<%@ include file="/WEB-INF/taglibs/header.jsp"%>
<html>
<body>

<stripes:form action="/Bug.action">

<%--TODO - can we avoid this?
 --%>
  <stripes:useActionBean binding="/Bug.action" var="actionBean" />

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
        <td><stripes:checkbox name="id" value="${row.id}"
                              onclick="handleCheckboxRangeSelection(this, event);"/></td>
        <td>${row.id}</td>
                        <td><fmt:formatDate value="${row.openDate}" dateStyle="medium"/></td>
        <td>
        <stripes:link href="/Bug.action" event="view">${row.shortDescription}
        <stripes:param name="id" value="${row.id}" />
        </stripes:link>
        </td>
        
                        <td>${row.component.name}</td>
                        <td>${row.priority}</td>
                        <td>${row.status}</td>
                        <td>${row.owner.username}</td>
        
        <td><stripes:link href="/Bug.action" event="edit">
                                Edit
                                <stripes:param name="id" value="${row.id}" /></stripes:link>
         <stripes:link href="/Bug.action" event="delete">
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
