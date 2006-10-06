<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
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
            <a:displayPropertyValue entity="${row.entity}" property="${property}"/>
        </td>
    </c:forEach>
    </tr>
  </c:forEach>
      </tbody>
  </table>
  
  <div class="buttons"><stripes:submit name="save" value="Save" /> <stripes:submit name="cancel" value="Cancel" /></div>
