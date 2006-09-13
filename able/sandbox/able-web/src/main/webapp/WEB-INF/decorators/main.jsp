<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %><decorator:useHtmlPage id="p"/><html><head>    <title>Able Demo</title>    <decorator:head/>    <link rel="stylesheet"
         type="text/css"
         href="<%= request.getContextPath() %>/css/style.css"/>

  <%-- table sorting --%>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tablesort/common.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tablesort/css.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/tablesort/standardista-table-sorting.js"></script>
  
  <%-- calendar widget --%>
  <link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/js/jscalendar/calendar-green.css" title="win2k-cold-1" />
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/jscalendar/calendar.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/jscalendar/lang/calendar-en.js"></script>
  <script type="text/javascript" src="<%= request.getContextPath() %>/js/jscalendar/calendar-setup.js"></script>

</head><body <decorator:getProperty property="body.onload" writeEntireProperty="true"/>>
    <div id="sidecolumn">
        <div id="controlPanel" class="navigationBox">
            <h3>Menu</h3>
            <ul>
             <li><a href="${pageContext.request.contextPath}">Home</a></li>
            
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
                Able-based App v1.0.                            <%--                 .<%= com.opensymphony.able.service.UpgradeService.getBuildNumber() %>                 --%>                Copyright &copy; 2006, Acme Corp            </div>        </div>	</div></div></body></html>