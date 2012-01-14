<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.shared.util.ApplicationServer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<jsp:useBean id="sessionInfo" class="com.topcoder.web.common.SessionInfo" scope="request" />
<script language="JavaScript" type="text/javascript" src="/js/tcscript.js"><!-- @ --></script>

<div style="margin: 10px 0px 40px 0px;">

    <div style="float: right; margin-left: 6px;">
        <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc"><img src="/i/or/topcoderLogo.png" alt="TopCoder Competitions" /></a>
    </div>
    <div style="float: right; margin-left: 6px;">
        <a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/"><img src="/i/or/studioLogo.png" alt="TopCoder Studio" /></a>
    </div>

    <div style="float: right; clear: right; margin-left: 6px;">
        <c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
            Hello, <tc-webtag:handle coderId="${orfn:getLoggedInUserId(pageContext.request)}" context="component" />
             | <html:link action="/actions/Logout.do?method=logout">Logout</html:link>
        </c:if>
        <c:if test="${not orfn:isUserLoggedIn(pageContext.request)}">
            <html:link page="/jsp/login.jsp">Login</html:link>
             | <a href="http://<%=ApplicationServer.SERVER_NAME%>/reg/">Register</a>
             | <a href="http://<%=ApplicationServer.SERVER_NAME%>/">Home</a>
        </c:if>
    </div>

    <a href="/review"><img src="/i/or/brandingLogo.png" alt="TopCoder Online Review" /></a>
</div>
