<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" session="true" isErrorPage="true" %>
<%@ page import="java.util.Date"%>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">
<head>
    <title>Online Review - Error</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link type="image/x-icon" rel="shortcut icon" href="<html:rewrite href='/i/favicon.ico' />" />

    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />

<style TYPE="text/css">
<!--
body {
text-align: center;
}

.centerer {
width: 400px;
background: transparent;
text-align: left;
margin-top: 20px;
margin-left: auto;
margin-right: auto;
margin-bottom: 20px;
padding: 0px;
}
-->
</style>

</head>

<body>
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%; margin-bottom: 40px;" align="center">

<%
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("com.cronos.onlinereview");
    if (exception == null) {
        log.error("exception is null");
    } else {
        log.error(exception, exception);
    }
%>

    <div class="centerer">
        <div style="padding:25px">
            <b><span style="font-size:18px;color:#990000;">Error</span></b>
            <br /><br />
            <b>An error has occurred when attempting to process your request.</b>
            <br /><br />
            You may click <a href="javascript:history.back();">here</a> to return to the last page you were viewing.
            <br /><br />
            If you have a question or comment, please email <a href="mailto:service@topcoder.com" class="bodyText">service@topcoder.com</a>.
            <br /><br />
            <%= new Date().toString() %>
        </div>
    </div>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
