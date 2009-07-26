<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
    <title><bean:message key="global.title.level2"
        arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
        arg1='${orfn:getMessage(pageContext, "login.title")}' /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

    <!-- CSS and JS from wireframes -->
    <script language="javascript" type="text/javascript" src="<html:rewrite href='/js/or/expand_collapse.js' />"><!-- @ --></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
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
                            <bean:message key="error.com.cronos.onlinereview.Errors" />
                            <br /><span class="bigRed"><html:errors /></span>
                        </div>
                    </c:if>

                    <br />

                    <html:form action="/actions/Login" focus="userName">
                        <html:hidden property="method" value="login" />

                        <c:set var="referer" value="${orfn:getSafeRedirect(pageContext.request)}" />
                        <c:if test="${not empty referer}">
                            <html:hidden property="forwardUrl" value="${referer}" />
                        </c:if>

                        <table class="stat" cellpadding="0" cellspacing="0" width="400">
                            <tr>
                                <td class="title" colspan="2"><bean:message key="login.formLogin.title" /></td>
                            </tr>
                            <tr>
                                <td class="value" colspan="2">&#160;</td>
                            </tr>
                            <tr>
                                <td class="value"><div align="right"><bean:message key="login.formLogin.userName" /> </div></td>
                                <td class="value"><html:text property="userName" /></td>
                            </tr>
                            <tr>
                                <td class="value"><div align="right"><bean:message key="login.formLogin.password" /> </div></td>
                                <td class="value"><input type="password" name="password" /></td>
                            </tr>
                            <tr>
                                <td class="valueLast">&#160;</td>
                                <td class="valueLast"><html:image altKey="login.formLogin.btnLogin.alt"
                                        srcKey="login.formLogin.btnLogin.img" border="0" styleClass="imgLogin" /></td>
                            </tr>
                            <tr>
                                <td class="lastRowTD" colspan="2"><!-- @ --></td>
                            </tr>
                        </table><br />
                    </html:form>
                    <p align="left" style="width: 400px;">
                    <strong><bean:message key="login.forgotPassword" /></strong><br/>
                    <bean:message key="login.cannotRememberPassword1" /> <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?&module=RecoverPassword"><bean:message key="clickHere" /></a> <bean:message key="login.cannotRememberPassword2" /><br /><br />
                    <strong><bean:message key="login.newToTopCoder" /></strong><br/>
                    <a href="https://<%=ApplicationServer.SERVER_NAME%>/reg/"><bean:message key="login.registerNow" /></a> <bean:message key="login.afterYouCompleteTheRegProcess" />
                    </p>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
