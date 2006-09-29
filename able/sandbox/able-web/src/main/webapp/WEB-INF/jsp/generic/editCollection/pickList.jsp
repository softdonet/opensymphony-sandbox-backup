<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

    <h2>Current values</h2>

  <table id="entities" class="display sortable autostripe">
   <thead>
    <tr>
    <c:forEach items="${actionBean.entityInfo.nameProperties}" var="property">
        <th>${property.displayName}</th>
    </c:forEach>
        <th>Remove?</th>
    </tr>
    </thead>
    <tbody>
  <c:forEach var="row" items="${actionBean.ownedEntities}">
    <tr>
    <c:forEach items="${actionBean.entityInfo.nameProperties}" var="property">
        <td>
            <able:viewField entity="${row}" name="${property.name}"/>
        </td>
    </c:forEach>
        <td>
          <input type="checkbox" name="delete" value="${row.id}"/>
        </td>
    </tr>
  </c:forEach>
      </tbody>
  </table>

    <br />

    <h2>Add new values</h2>

    <p>
        Search:
        <stripes:text name="query"/>
        <stripes:submit name="search" value="Go"/>
    </p>

    <table id="entities" class="display sortable autostripe">
     <thead>
      <tr>
          <c:forEach items="${actionBean.entityInfo.nameProperties}" var="property">
          <th>${property.displayName}</th>
          </c:forEach>
          <th>Add?</th>
      </tr>
      </thead>
      <tbody>
    <c:forEach var="row" items="${actionBean.searchResults}">
      <tr>
      <c:forEach items="${actionBean.entityInfo.nameProperties}" var="property">
          <td>
              <able:viewField entity="${row}" name="${property.name}"/>
          </td>
      </c:forEach>
          <td>
              <input type="checkbox" name="add" value="${row.id}"/>
          </td>
      </tr>
    </c:forEach>
        </tbody>
    </table>


  <div class="buttons"><stripes:submit name="save" value="Save" /> <stripes:submit name="cancel" value="Cancel" /></div>
