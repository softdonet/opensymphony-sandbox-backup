<%@ page import="com.opensymphony.able.service.UpgradeService"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="ww" uri="/webwork" %>
<decorator:useHtmlPage id="p"/>
<html>
<head>
    <title>Juice</title>
    <ww:head/>
    <decorator:head/>
</head>

<body <decorator:getProperty property="body.onload" writeEntireProperty="true"/>>

<div id="container">

    <div id="widecolumn">
		<div id="page">
			<decorator:body/>
            <div class="copyright">
                Juice-based App v1.0.<%= UpgradeService.getBuildNumber() %> | Copyright &copy; 2006, Acme Corp
            </div>
        </div>
	</div>

    <div id="sidecolumn">
        <div id="controlPanel" class="navigationBox">
            <h3>Menu</h3>
            <ul>
                <ww:if test="currentUser != null">
                    <li><a href="/user/settings!input">My Settings</a></li>
                    <li><a href="/user/${currentUser.id}/profile">My Profile</a></li>
                    <li><a href="/logout">Logout</a></li>
                </ww:if>
                <ww:else>
                    <li><a href="/register!input">Register</a></li>
                    <li><a href="/login!input">Login</a></li>
                </ww:else>
            </ul>
        </div>
    </div>
</div>

</body>
</html>