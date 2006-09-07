<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %><decorator:useHtmlPage id="p"/><html><head>    <title>Able Demo</title>    <decorator:head/>    <link rel="stylesheet"
         type="text/css"
         href="${pageContext.request.contextPath}/css/style.css"/>
</head><body <decorator:getProperty property="body.onload" writeEntireProperty="true"/>>
    <div id="sidecolumn">
        <div id="controlPanel" class="navigationBox">
            <h3>Menu</h3>
            <ul>
            <%-- 
                <ww:if test="currentUser != null">
                    <li><a href="<ww:url action="user/settings" method="input"/>">My Settings</a></li>
                    <li><a href="<ww:url value="/user/${currentUser.id}/profile"/>">My Profile</a></li>
                    <li><a href="<ww:url action="logout"/>">Logout</a></li>
                </ww:if>
                <ww:else>
                    <li><a href="<ww:url action="register" method="input"/>">Register</a></li>
                    <li><a href="<ww:url action="login" method="input"/>">Login</a></li>
                </ww:else>
            --%>
            </ul>
        </div>
    </div>

            <div id="contentPanel">
                <div id="pageContent">
                    <div class="sectionTitle">${title}</div>
                    <stripes:messages/>
                </div>

		<div id="page">
			<decorator:body/>
			</div>
			
                <div id="footer">
                Able-based App v1.0                            <%--                 .<%= com.opensymphony.able.service.UpgradeService.getBuildNumber() %>                 --%>                | Copyright &copy; 2006, Acme Corp            </div>        </div>	</div></div></body></html>