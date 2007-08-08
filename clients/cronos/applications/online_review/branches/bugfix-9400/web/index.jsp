<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<logic:redirect forward="Login"/>

<%--

Redirect default requests to Login global ActionForward.
By using a redirect, the user-agent will change address to match the path of our Login ActionForward. 

--%>
