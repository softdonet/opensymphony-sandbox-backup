<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:select name="entity.priority" value="Medium">
    <stripes:options-collection collection="${actionBean.allValues.priority}"/>
</stripes:select>
