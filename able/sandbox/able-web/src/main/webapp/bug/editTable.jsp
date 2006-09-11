<%@ include file="/WEB-INF/header.jsp"%>
<html>
<body>

<stripes:form action="/Bug.action">

<%--TODO - can we avoid this? --%>
  <stripes:useActionBean binding="/Bug.action" var="actionBean" />


  <stripes:errors />
  <stripes:hidden name="id"/>

  <table class="display">
    <tr>
           <th>ID</th>
           <th>Priority</th>
           <th>Component</th>
           <th>Owner</th>
           <th>Short Description</th>
           <th>Long Description</th>
     <th></th>
    </tr>

    <c:forEach items="${actionBean.bulkEditEntities}" var="row" varStatus="rowstat">
      <tr>
        <td>${row.id}
        </td>
                    <td>
                        <stripes:select name="bulkEditEntities[${rowstat.index}].priority" value="Medium">
                            <stripes:options-collection collection="${actionBean.allValues.priority}"/>
                        </stripes:select>
                    </td>
                    <td>
                        <stripes:select name="bulkEditEntities[${rowstat.index}].component.id">
                            <stripes:options-collection collection="${actionBean.allValues.component}"
                                                        label="name" value="id"/>
                        </stripes:select>
                    </td>
                    <td>
                        <stripes:select name="bulkEditEntities[${rowstat.index}].owner.id">
                            <stripes:options-collection collection="${actionBean.allValues.owner}"
                                                        label="username" value="id"/>
                        </stripes:select>
                    </td>
                    <td><stripes:text name="bulkEditEntities[${rowstat.index}].shortDescription"/></td>
                    <td><stripes:textarea name="bulkEditEntities[${rowstat.index}].longDescription"/></td>
                </tr>
                <%-- 
                        <c:forEach items="${actionBean.bulkEditEntities[${rowstat.index}].attachments}" var="attachment" varStatus="loop">
                            ${attachment.name} (${attachment.size} bytes) -
                            <stripes:link href="/examples/bugzooky/DownloadAttachment.action">
                                <stripes:param name="bugId" value="${actionBean.bulkEditEntities[${rowstat.index}].id}"/>
                                <stripes:param name="attachmentIndex" value="${loop.index}"/>
                                <em>${attachment.preview}...</em>
                            </stripes:link><br/>
                        </c:forEach>

                        Add a new attachment: <stripes:file name="newAttachment"/>
                    </td>
                --%>
        
      </tr>
    </c:forEach>
  </table>

  <div class="buttons"><stripes:submit name="save" value="Save" /> <stripes:submit name="cancel" value="Cancel" /></div>
</stripes:form>

</body>
</html>
