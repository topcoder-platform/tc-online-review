<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="logic" uri="/tags/struts-logic" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<logic:redirect forward="${(not orfn:isUserLoggedIn(pageContext.request))?'Login':'listMyProjects'}" />

<%--

Redirect default requests to Login global ActionForward.
By using a redirect, the user-agent will change address to match the path of our Login ActionForward. 

--%>
