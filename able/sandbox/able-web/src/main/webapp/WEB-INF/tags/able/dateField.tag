<%@ attribute name="entity" type="java.lang.Object" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true"  %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
  <stripes:text name="entity.${name}" formatPattern="medium" id="${name}"/>
  <img src="<%= request.getContextPath() %>/js/jscalendar/img.gif" id="${name}.button"/>
  <script type="text/javascript">
    Calendar.setup({
        inputField     :    "entity.${name}",        <%-- input field id --%>
        ifFormat       :    "%d-%b-%Y",       <%-- format of the input field --%>
        button         :    "entity.${name}.button", <%-- trigger for the calendar (button ID) --%>
        align          :    "Tl",             <%-- alignment (defaults to "Bl") --%>
        singleClick    :    true
    });
  </script>
