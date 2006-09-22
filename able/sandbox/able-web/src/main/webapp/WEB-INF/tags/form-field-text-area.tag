<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        include file="form-field-common-attrs.tag"
        %><%@
        attribute name="rows" required="true" type="java.lang.Integer"
        %><%@
        attribute name="cols" required="true" type="java.lang.Integer"
        %><%@
        include file="form-field-header.jsp"
        %><s:textarea name="${name}" rows="${rows}" cols="${cols}"
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
        /><%@ include file="form-field-footer.jsp"
        %>