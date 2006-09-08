<%@ include file="/WEB-INF/taglibs/header.jsp"%>
<html>
<body>

<stripes:form action="/User.action">
<stripes:hidden name="id"/>

<%-- 
<c:set var="entity" value="${actionBean.entity}" />
--%>

<table class="display">

    <tr>
      <td>User name</td>
      <td><stripes:text name="entity.username" readonly="true"/></td>
    </tr>
    <tr>
      <td>Name</td>
      <td><stripes:text name="entity.name" readonly="true"/></td>
    </tr>
    <tr>
      <td>Email</td>
      <td><stripes:text name="entity.email" readonly="true"/></td>
    </tr>
</table>

<div class="buttons">
  <stripes:submit name="edit" value="Edit" />
  <stripes:submit name="cancel" value="Back" />
</div>

</stripes:form>
</body>
</html>
