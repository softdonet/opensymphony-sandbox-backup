<%@
        include file="/WEB-INF/jsp/include/taglibs.jspf"
        %><%@
        tag body-content="scriptless"
        %><%@
        include file="formFieldCommonAttrs.tag"
        %><%@
        attribute name="size" required="false" type="java.lang.Integer"
        %><%@
        attribute name="collection" required="true" type="java.util.Collection"
        %><%@
        attribute name="listValue" required="false" type="java.lang.String"
        %><%@
        attribute name="listLabel" required="false" type="java.lang.String"
        %><%@
        include file="formFieldHeader.jsp"
        %><s:select name="${name}" value="${value}" size="${size}"
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
        include file="formFieldFooter.jsp"
        %>