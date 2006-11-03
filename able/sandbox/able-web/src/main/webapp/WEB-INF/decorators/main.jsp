<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %><decorator:useHtmlPage id="p"/><html><head>    <title><decorator:title/></title>    <decorator:head/>    <link rel="stylesheet"          type="text/css"          href="<%= request.getContextPath() %>/css/style.css"/>    <%-- table sorting --%>    <script type="text/javascript" src="<%= request.getContextPath() %>/js/tablesort/common.js"></script>    <script type="text/javascript" src="<%= request.getContextPath() %>/js/tablesort/css.js"></script>    <script type="text/javascript"            src="<%= request.getContextPath() %>/js/tablesort/standardista-table-sorting.js"></script>    <%-- calendar widget --%>    <link rel="stylesheet" type="text/css" media="all"          href="<%= request.getContextPath() %>/js/jscalendar/calendar-green.css" title="win2k-cold-1"/>    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jscalendar/calendar.js"></script>    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jscalendar/lang/calendar-<%=request.getLocale().getLanguage()%>.js"></script>        <script type="text/javascript" src="<%= request.getContextPath() %>/js/jscalendar/calendar-setup.js"></script></head><body <decorator:getProperty property="body.onload" writeEntireProperty="true"/>><div id="sidecolumn">    <div id="controlPanel" class="navigationBox">        <h3>Menu</h3>        <ul>            <li><s:link beanclass="com.opensymphony.able.stripes.HomeActionBean">Home</s:link></li>            <li><s:link beanclass="com.opensymphony.able.stripes.UserSettingsActionBean">User Settings</s:link></li>            <c:choose>                <c:when test="${actionBean.context.userLoggedIn}">                    <li><s:link beanclass="com.opensymphony.able.stripes.LogoutActionBean">Logout</s:link></li>                </c:when>                <c:otherwise>                    <li><s:link beanclass="com.opensymphony.able.stripes.RegisterActionBean">Register</s:link></li>                    <li><s:link beanclass="com.opensymphony.able.stripes.LoginActionBean">Login</s:link></li>                </c:otherwise>            </c:choose>        </ul>    </div></div><div id="contentPanel">    <div id="pageContent">        <div class="sectionTitle">${title}</div>        <stripes:messages/>    </div>    <div id="page">        <decorator:body/>    </div>    <div id="footer">        Able-based App v1.0.        <%--        .<%= com.opensymphony.able.service.UpgradeService.getBuildNumber() %>        --%>        Copyright &copy; 2006, Acme Corp    </div></div></body></html>