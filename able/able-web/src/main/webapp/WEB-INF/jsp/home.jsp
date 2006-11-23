<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>Able Home Page</title>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/ajax/prototype.js"></script>
    <script type="text/javascript" xml:space="preserve">
        /*
        * Function that uses Prototype to invoke an action of a form. Slurps the values
        * from the form using prototype's 'Form.serialize()' method, and then submits
        * them to the server using prototype's 'Ajax.Updater' which transmits the request
        * and then renders the resposne text into the named container.
        *
        * @param form reference to the form object being submitted
        * @param event the name of the event to be triggered, or null
        * @param container the name of the HTML container to insert the result into
        */
        function invoke(form, event, container) {
            var params = Form.serialize(form);
            if (event != null) params = event + '&' + params;
            new Ajax.Updater(container, form.action, {method:'post', postBody:params});
        }
    </script>
</head>

<body>

<p>Welcome to the starter web application for Able using language: <b><%=request.getLocale().getLanguage()%>
</b></p>

<h2>Entity List</h2>

<p>The following are all the entities you can modify.
    A good starting point is the
    <stripes:link href="/bug">Bug List</stripes:link>
</p>

<ul>
    <c:forEach var="entity" items="${actionBean.entities}">
        <li>
            <stripes:link href="/${entity.entityUri}">${entity.entityName} List</stripes:link>
        </li>
    </c:forEach>
</ul>


<h2>Current users</h2>

<p>These other users are also enjoying Able:</p>
<table>
    <tr>
        <td>
            <ul>
                <c:forEach items="${actionBean.users}" var="user">
                    <li>${user.name} (${user.email})</li>
                </c:forEach>
            </ul>
        </td>

        <%--<td style="padding: 20px">
            <a:form beanclass="com.opensymphony.able.demo.action.RegisterActionBean"
                    title="Register"
                    submitEvent="register">
                <a:formFieldText name="user.username"/>
                <a:formFieldText name="user.name"/>
                <a:formFieldText name="user.email"/>
                <a:formFieldPassword name="password"/>
                <a:formFieldPassword name="passwordAgain"/>
            </a:form>
        </td>--%>
    </tr>
</table>

<s:link beanclass="com.opensymphony.able.demo.action.RegisterActionBean">Register</s:link>
an account to be added to the list!
<br>
<br>
<a:form beanclass="com.opensymphony.able.demo.action.CurrentBuildAction"
        title="Build information" submitEvent="none" noButtons="true">
    <table width="500px">
        <tr>
            <td width="200px">
                <s:button name="currentBuild" value="Current" class="btn"
                          onclick="invoke(this.form, this.name, 'current');"/>
            </td>
            <td width="200px">
                <s:button name="buildHistory" value="Build history" class="btn"
                          onclick="invoke(this.form, this.name, 'history');"/>
            </td>
        </tr>
        <tr>
            <td id="current"></td>
            <td id="history"></td>
        </tr>
    </table>
</a:form>

</body>
</html>