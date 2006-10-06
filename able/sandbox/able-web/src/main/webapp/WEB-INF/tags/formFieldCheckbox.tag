<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        include file="formFieldCommonAttrs.tag"
        %><%@
        include file="formFieldHeader.jsp"
        %><s:checkbox name="${name}"
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
        /><%@
        include file="formFieldFooter.jsp"
        %>