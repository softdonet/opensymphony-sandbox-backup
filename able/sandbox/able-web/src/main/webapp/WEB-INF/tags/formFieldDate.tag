<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        include file="formFieldCommonAttrs.tag"
        %><%@
        include file="formFieldHeader.jsp"
        %><s:text name="${name}"
        id="${id}"
        onblur="${onblur}"
        onchange="${onchange}"
        onclick="${onclick}"
        ondblclick="${ondblclick}"
        onfocus="${onfocus}"
        onkeydown="${onkeydown}"
        onkeypress="${onkeypress}"
        onkeyup="${onkeyup}"
        onmousedown="${onmousedown}"
        onmousemove="${onmousemove}"
        onmouseout="${onmouseout}"
        onmouseover="${onmouseover}"
        onmouseup="${onmouseup}"
        onselect="${onselect}"
        /><img src="<%= request.getContextPath() %>/js/jscalendar/img.gif" id="${id}.button" alt="Calendar"/>
<script type="text/javascript">
Calendar.setup({
    inputField     :    "${id}",                        <%-- input field id --%>
    ifFormat       :    Calendar._TT["DEF_DATE_FORMAT"],<%-- format of the input field (from the language spesific file --%>
    button         :    "${id}.button",                 <%-- trigger for the calendar (button ID) --%>
    align          :    "Tl",                           <%-- alignment (defaults to "Bl") --%>
    singleClick    :    true
});
</script>
<%@ include file="formFieldFooter.jsp" %>