<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<html>
<body>

<stripes:form action="/User.action">

  <stripes:useActionBean binding="/User.action" var="actionBean" />

  <stripes:errors />

  <table class="display">

    <tr>
      <td>User name</td>
      <td><stripes:text name="entity.username" /></td>
    </tr>
    <tr>
      <td>Name</td>
      <td><stripes:text name="entity.name" /></td>
    </tr>
    <tr>
      <td>Email</td>
      <td><stripes:text name="entity.email" /></td>
    </tr>
    <tr>
      <td>Number 2:</td>
      <td><stripes:text name="numberTwo" /></td>
    </tr>
  </table>

  <div class="buttons"><stripes:submit name="save" value="Save" /> <stripes:submit name="cancel" value="Cancel" /></div>
</stripes:form>
</body>
</html>
