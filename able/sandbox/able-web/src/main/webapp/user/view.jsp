<%@ include file="/WEB-INF/taglibs/header.jsp"%>
<html>
<body>

<stripes:useActionBean binding="/User.action" />

<c:set var="entity" value="${actionBean.entity}" />

<table class="display">

  <tr>
    <td>User name</td>
    <td>${entity.username}</td>
  </tr>
  <tr>
    <td>Name</td>
    <td>${entity.name}</td>
  </tr>
  <tr>
    <td>Email</td>
    <td>${entity.email}</td>
  </tr>
</table>

<div class="buttons"><stripes:link href="/User.action" event="edit">
                                Edit
                                <stripes:param name="id" value="${entity.id}" />
</stripes:link></div>

</body>
</html>
