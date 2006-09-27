<%@ include file="/WEB-INF/header.jsp" %>
<html>
<body>

<stripes:form action="/${actionBean.entityInfo.entityUri}">
    <stripes:hidden name="entity"/>

    <stripes:errors/>

    <table class="display">

        <tr>
            <th>
                <stripes:label for="entity.username">Username</stripes:label>
                :
            </th>
            <td>
                <stripes:text name="entity.username"/>
            </td>
        </tr>
        <tr>
            <th>
                <stripes:label for="entity.username">First Name</stripes:label>
                :
            </th>
            <td>
                <stripes:text name="entity.username"/>
            </td>
        </tr>
        <tr>
            <th>
                <stripes:label for="entity.lastName">Last Name</stripes:label>
                :
            </th>
            <td>
                <stripes:text name="entity.lastName"/>
            </td>
        </tr>

    </table>

    <div class="buttons">
        <stripes:submit name="save" value="Save"/>
        <stripes:submit name="cancel" value="Cancel"/>
    </div>
</stripes:form>

</body>
</html>
