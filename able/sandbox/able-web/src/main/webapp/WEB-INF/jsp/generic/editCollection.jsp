<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<body>

<stripes:form action="${actionBean.actionUri}">

  <h2><able:viewSummary entity="${actionBean.owner}"/></h2>
  
  <stripes:hidden name="owner" />
  <stripes:errors />


  <table id="entities" class="display sortable autostripe">
   <thead>
    <tr>
      <th></th>
        <c:forEach items="${actionBean.entityInfo.nameProperties}" var="property">
            <th>${property.displayName}</th>
        </c:forEach>
    </tr>
    </thead>
    <tbody>
  <c:forEach var="row" items="${actionBean.options}">
    <tr>
      <td>
        <input type="checkbox" name="entities" value="${row.value}" ${row.selected ? 'checked' : ''} ${row.selected}/>
      </td>
    <c:forEach items="${actionBean.entityInfo.nameProperties}" var="property">
        <td>
            <able:viewField entity="${row.entity}" name="${property.name}"/>
        </td>
    </c:forEach>
    </tr>
  </c:forEach>
      </tbody>
  </table>
  
  <div class="buttons"><stripes:submit name="save" value="Save" /> <stripes:submit name="cancel" value="Cancel" /></div>
</stripes:form>
</body>
</html>
