<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        include file="form-field-common-attrs.tag"
        %><%@
        attribute name="multiple" required="false" type="java.lang.Boolean"
        %><%@
        attribute name="size" required="false" type="java.lang.Integer"
        %><%@
        attribute name="collection" required="true" type="java.util.Collection"
        %><%@
        attribute name="listValue" required="true" type="java.lang.String"
        %><%@
        attribute name="listLabel" required="true" type="java.lang.String"
        %><%@
        include file="form-field-header.jsp"
        %><s:select name="${name}" multiple="${multiple}" size="${size}"
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
          onselect="${onselect}">
    <s:options-collection collection="${collection}" value="${listValue}" label="${listLabel}"/>
</s:select><%@
        include file="form-field-footer.jsp"
        %>