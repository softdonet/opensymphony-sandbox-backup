<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>List entities</title>
</head>

<body>

<c:set var="action" value="/${actionBean.entityInfo.entityUri}"/>
<stripes:form action="${action}">

  <stripes:errors />

  <p>
  Search: <stripes:text name="query"/> <stripes:submit name="search" value="Go" />
  </p>

  <table id="entities" class="display sortable autostripe">
   <thead>
    <tr>
      <th></th>
      <th>Id</th>
      <th>Username</th>
      <th>First Name</th>
      <th>Last Name</th>
      <th>Email</th>
      <th>Password</th>
      <th>Leads</th>
      <th>Team</th>
      <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${actionBean.allEntities}" var="row" varStatus="rowstat">
      <tr>
        <td><stripes:checkbox name="id" value="${row.id}" onclick="handleCheckboxRangeSelection(this, event);" /></td>
        <td>${row.id}</td>
        <td>${row.username}</td>

        <td><stripes:link href="${action}" event="edit">
                                Edit
                                <stripes:param name="entity" value="${row.id}" />
        </stripes:link> <stripes:link href="${action}" event="delete">
                                Delete
                                <stripes:param name="entity" value="${row.id}" />
        </stripes:link></td>

      </tr>
    </c:forEach>
    </tbody>
  </table>

  <div class="buttons"><stripes:submit name="edit" value="New" /> <stripes:submit
    name="delete" value="Delete" /></div>
</stripes:form>

</body>
</html>