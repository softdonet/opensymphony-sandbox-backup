<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        include file="form-field-common-attrs.tag"
        %><%@
        include file="form-field-header.jsp"
        %><s:password name="${name}"
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
        include file="form-field-footer.jsp"
        %>