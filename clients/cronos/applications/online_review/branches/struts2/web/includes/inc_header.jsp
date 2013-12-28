 <%--
 *
 * Copyright (C) 2004 - 2013 TopCoder Inc., All Rights Reserved.
 *
 * <p>
 *   1.1 (TCCC-5802) change log:
 *   <ol>
 *    Add a social account login link.
 *   </ol>
 * </p>
 * Version 1.1
 * Author: TCSASSEMBLER, ecnu_haozi
 --%>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,
                 com.topcoder.shared.util.ApplicationServer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<jsp:useBean id="sessionInfo" class="com.topcoder.web.common.SessionInfo" scope="request" />
<script language="JavaScript" type="text/javascript" src="/js/tcscript.js"><!-- @ --></script>
<script language="JavaScript" type="text/javascript" src="/js/jquery-1.10.2.min.js"><!-- @ --></script>
<script src="//d19p4zemcycm7a.cloudfront.net/w2/auth0-widget-2.3.6.min.js"></script>

<script>
    var widget = new Auth0Widget({
        domain: '<%=ApplicationServer.DOMAIN_AUTH0%>',
        clientID: '<%=ApplicationServer.CLIENT_ID_AUTH0%>',
        callbackURL: 'https://<%=ApplicationServer.REG_SERVER_NAME%><%=ApplicationServer.REDIRECT_URL_AUTH0%>'
    });

    function showAuth0Widget(){
        widget.signin({
            state: 'https://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/review/actions/ListProjects.do?method=listProjects',
            icon: 'http://www.topcoder.com/i/24x24_brackets.png', 
            showIcon: true}).on('signin_ready', function() {
            $('.a0-email input').each(function() {
                $(this)
                .clone()
                .attr('type','text')
                .attr('placeholder', 'Username')
                .attr('title', 'Username')
                .insertAfter($(this)).prev().remove();
            });
        });  
    }       

    $(function () {
        $('.social-login').click(function () { showAuth0Widget(); });
    });
</script>    

<div style="margin: 10px 0px 40px 0px;">

    <div style="float: right; margin-left: 6px;">
        <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc"><img src="/i/or/topcoderLogo.png" alt="TopCoder Competitions" /></a>
    </div>
    <div style="float: right; margin-left: 6px;">
        <a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/"><img src="/i/or/studioLogo.png" alt="TopCoder Studio" /></a>
    </div>

    <div style="float: right; clear: right; margin-left: 6px;">
        <c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
            Hello, <tc-webtag:handle coderId="${orfn:getLoggedInUserId(pageContext.request)}" />
             | <html:link action="/actions/Logout.do?method=logout">Logout</html:link>
        </c:if>
        <c:if test="${not orfn:isUserLoggedIn(pageContext.request)}">
            <a href="javascript:;" class="social-login">Social login</a>
             | <html:link page="/jsp/login.jsp">Login</html:link>
             | <a href="http://<%=ApplicationServer.SERVER_NAME%>/reg/">Register</a>
             | <a href="http://<%=ApplicationServer.SERVER_NAME%>/">Home</a>
        </c:if>
    </div>

    <a href="/review"><img src="/i/or/brandingLogo.png" alt="TopCoder Online Review" /></a>
</div>
