<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays header for all pages used in online review application.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<jsp:useBean id="sessionInfo" class="com.topcoder.onlinereview.component.webcommon.SessionInfo" scope="request" />
<script language="JavaScript" type="text/javascript" src="/js/tcscript.js"><!-- @ --></script>

<div style="margin: 10px 0px 40px 0px;">

    <div style="float: right; clear: right; margin-left: 6px;">
        <c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
            Hello, <tc-webtag:handle coderId="${orfn:getLoggedInUserId(pageContext.request)}" />
             | <a href="<or:url value='/actions/Logout' />">Logout</a>
        </c:if>
        <c:if test="${not orfn:isUserLoggedIn(pageContext.request)}">
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
                        state: 'https://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/review/actions/ListProjects',
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

            <a href="javascript:;" class="social-login">Social login</a>
             | <a href="<%=com.cronos.onlinereview.util.ConfigHelper.getNewAuthUrl()%>?retUrl=https://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/review">Login</a>
             | <a href="http://<%=ApplicationServer.SERVER_NAME%>/reg/">Register</a>
             | <a href="http://<%=ApplicationServer.SERVER_NAME%>/">Home</a>
        </c:if>
    </div>

    <a href="/review" style="font-size: 18px;font-weight: bold;color: grey;text-decoration: none;"><img src="/i/or/topcoder.svg" alt="topcoder"  style="float:left; margin-right: 10px;"/>Online Review</a>
</div>
