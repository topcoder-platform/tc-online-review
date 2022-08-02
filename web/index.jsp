<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright: Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: The default index page.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<c:choose>
  <c:when test="${not orfn:isUserLoggedIn(pageContext.request)}">
    <%
      response.setHeader("Location", "https://"+ApplicationServer.SOFTWARE_SERVER_NAME+"/review/actions/Login");
    %>
  </c:when>
  <c:otherwise>
    <%
      response.setHeader("Location", "https://"+ApplicationServer.SOFTWARE_SERVER_NAME+"/review/actions/ListProjects");
    %>
  </c:otherwise>
</c:choose>

<%
response.setContentType("text/html");
response.setDateHeader("Expires", 0);
response.setStatus(301); 
%>
<%--

Redirect default requests to Login or ListProjects action.

--%>
