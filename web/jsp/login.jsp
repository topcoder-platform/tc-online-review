<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright: Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: The login page for the online review application.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <title><or:text key="global.title.level2"
        arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
        arg1='${orfn:getMessage(pageContext, "login.title")}' /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS from wireframes -->
    <script language="javascript" type="text/javascript" src="/js/or/expand_collapse.js"><!-- @ --></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
</head>
<body>

<body>
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%; padding-bottom: 40px;" align="center">

                    <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                        <div align="center">
                            <or:text key="error.com.cronos.onlinereview.Errors" />
                            <br /><span class="bigRed"><s:actionerror /></span>
                        </div>
                    </c:if>

                    <br />

                    <s:form action="Login" focus="userName" namespace="/actions">
                        <input type="hidden" name="postBack" value="true"/>

                        <c:set var="referer" value="${orfn:getSafeRedirect(pageContext.request)}" />
                        <c:if test="${not empty referer}">
                            <input type="hidden" name="forwardUrl" value="${referer}" />
                        </c:if>

                        <table class="stat" cellpadding="0" cellspacing="0" width="400">
                            <tr>
                                <td class="title" colspan="2"><or:text key="login.formLogin.title" /></td>
                            </tr>
                            <tr>
                                <td class="value" colspan="2">&#160;</td>
                            </tr>
                            <tr>
                                <td class="value"><div align="right"><or:text key="login.formLogin.userName" /> </div></td>
                                <td class="value"><input type="text" name="userName"  value="<or:fieldvalue field='userName' />" /></td>
                            </tr>
                            <tr>
                                <td class="value"><div align="right"><or:text key="login.formLogin.password" /> </div></td>
                                <td class="value"><input type="password" name="password" /></td>
                            </tr>
                            <tr>
                                <td class="value" colspan="2">
                                    <div class="rememberMe">
                                         <input type="checkbox" name="rememberMe" <c:if test="${not empty rememberMe}"> checked="on"</c:if> />
                                        <or:text key="login.formLogin.rememberMe"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="valueLast">&#160;</td>
                                <td class="valueLast"><input type="image" alt="<or:text key='login.formLogin.btnLogin.alt' />"
                                        src="<or:text key='login.formLogin.btnLogin.img' />" border="0" class="imgLogin" /></td>
                            </tr>
                            <tr>
                                <td class="lastRowTD" colspan="2"><!-- @ --></td>
                            </tr>
                        </table><br />
                    </s:form>
                    <p align="left" style="width: 400px;">
                    <strong><or:text key="login.forgotPassword" /></strong><br/>
                    <or:text key="login.cannotRememberPassword1" /> <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?&module=RecoverPassword"><or:text key="clickHere" /></a> <or:text key="login.cannotRememberPassword2" /><br /><br />
                    <strong><or:text key="login.newToTopCoder" /></strong><br/>
                    <a href="https://<%=ApplicationServer.SERVER_NAME%>/reg/"><or:text key="login.registerNow" /></a> <or:text key="login.afterYouCompleteTheRegProcess" />
                    </p>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
