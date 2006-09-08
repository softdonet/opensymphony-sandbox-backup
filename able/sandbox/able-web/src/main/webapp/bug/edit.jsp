<%@ include file="/WEB-INF/taglibs/header.jsp"%>
<%@ taglib prefix="bug" tagdir="/WEB-INF/tags/entity/bug" %>
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
                    <td>
                      <bug:fieldEditPriority/>
                    </td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.component.id">Component</stripes:label>:</th>
                    <td>
                        <stripes:select name="entity.component.id">
                            <stripes:options-collection collection="${actionBean.allValues.component}"
                                                        label="name" value="id"/>
                        </stripes:select>
                    </td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.owner.id">Owner</stripes:label>:</th>
                    <td>
                        <stripes:select name="entity.owner.id">
                            <stripes:options-collection collection="${actionBean.allValues.owner}"
                                                        label="username" value="id"/>
                        </stripes:select>
                    </td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.status">Status</stripes:label>:</th>
                    <td>
                        <c:forEach var="status" items="<%=com.opensymphony.able.model.Status.values()%>">
                            <stripes:radio id="entity.status.${stripes:enumName(status)}"
                                           name="entity.status"
                                           value="${stripes:enumName(status)}"
                                           checked="New"/>
                            <stripes:label for="entity.status.${stripes:enumName(status)}">${stripes:enumName(status)}</stripes:label>
                        </c:forEach>

                    </td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.dueDate">Due date</stripes:label>:</th>
                    <td><stripes:text name="entity.dueDate" formatPattern="medium"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.percentComplete">Percent complete</stripes:label>:</th>
                    <td><stripes:text name="entity.percentComplete" formatType="percentage"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.shortDescription">Short description</stripes:label>:</th>
                    <td><stripes:text style="width: 500px;" name="entity.shortDescription"/></td>
                </tr>
                <tr>
                    <th><stripes:label for="entity.longDescription">Long Description</stripes:label>:</th>
                    <td><stripes:textarea style="width:500px; height:3em;" name="entity.longDescription"/></td>
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
  <stripes:submit name="save" value="Save" /> 
  <stripes:submit name="cancel" value="Cancel" />
  </div>
</stripes:form>

</body>
</html>
