<%@ include file="/WEB-INF/taglibs/header.jsp"%>
<html>
<body>

<stripes:form action="/Bug.action">
<stripes:hidden name="id"/>

  <stripes:errors />

  <table class="display">

                <tr>
                    <th>Opened On:</th>
                    <td><fmt:formatDate value="${actionBean.entity.openDate}"
                                        dateStyle="medium"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.priority">Priority</stripes:label>:</th>
                    <td><stripes:text name="entity.priority" formatPattern="medium" readonly="true"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.component.id">Component</stripes:label>:</th>
                    <td><stripes:text name="entity.component.name" formatPattern="medium" readonly="true"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.owner.id">Owner</stripes:label>:</th>
                    <td><stripes:text name="entity.owner.username" formatPattern="medium" readonly="true"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.status">Status</stripes:label>:</th>
                    <td><stripes:text name="entity.status" formatPattern="medium" readonly="true"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.dueDate">Due date</stripes:label>:</th>
                    <td><stripes:text name="entity.dueDate" formatPattern="medium" readonly="true"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.percentComplete">Percent complete</stripes:label>:</th>
                    <td><stripes:text name="entity.percentComplete" formatType="percentage"  readonly="true"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.shortDescription">Short description</stripes:label>:</th>
                    <td><stripes:text style="width: 500px;" name="entity.shortDescription"  readonly="true"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.longDescription">Long Description</stripes:label>:</th>
                    <td><stripes:textarea style="width:500px; height:3em;" name="entity.longDescription"  readonly="true"/></td>
                </tr>
                <%-- 
                <tr>
                    <th>Attachments:</th>
                    <td>
                        <c:forEach items="${actionBean.entity.attachments}" var="attachment" varStatus="loop">
                            ${attachment.name} (${attachment.size} bytes) -
                            <stripes:link href="/examples/bugzooky/DownloadAttachment.action">
                                <stripes:param name="bugId" value="${actionBean.entity.id}"/>
                                <stripes:param name="attachmentIndex" value="${loop.index}"/>
                                <em>${attachment.preview}...</em>
                            </stripes:link><br/>
                        </c:forEach>

                        Add a new attachment: <stripes:file name="newAttachment"/>
                    </td>
                </tr>  
                --%>
                </table>

<div class="buttons">
  <stripes:submit name="edit" value="Edit" />
  <stripes:submit name="cancel" value="Back" />
</div>
</stripes:form>

</body>
</html>
