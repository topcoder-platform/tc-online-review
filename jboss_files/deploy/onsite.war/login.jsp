<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page contentType="text/html; charset=ISO-8859-1"
         import="com.topcoder.web.common.BaseServlet,
                 com.topcoder.web.common.StringUtils,
                 com.topcoder.web.onsite.Constants" %>
<jsp:useBean id="sessionInfo" class="com.topcoder.web.common.SessionInfo" scope="request"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    String nextpage = (String) request.getAttribute(BaseServlet.NEXT_PAGE_KEY);
    if (nextpage == null) nextpage = request.getParameter(BaseServlet.NEXT_PAGE_KEY);
    if (nextpage == null) nextpage = request.getHeader("Referer");
    if (nextpage == null) nextpage = "http://" + request.getServerName();
    String message = (String) request.getAttribute("message");
    if (message == null) message = "";
%>

<html>
<head>
<title>2007 TopCoder Open - Computer Programming Tournament</title>
<link type="image/x-icon" rel="shortcut icon" href="http://<%=ApplicationServer.SERVER_NAME%>/i/favicon.ico"/>
<%--
<style TYPE="text/css">
html{ 
margin:0px;
padding:0px;
font-family: Arial, Verdana, Helvetica, sans-serif;
}

body{ 
font-family: Arial, Verdana, Helvetica, sans-serif;
line-height: normal;
font-size: 12px;
color: #666666;
background: #FFA500;
margin:0px;
padding:0px;
}

img{
border: none;
display: block;
}

h1{
color: #333333;
font-style: italic;
font-size: 125%;
}

p{
padding: 0px;
margin: 0px 0px 10px 0px;
}

td, th{ 
line-height: normal;
font-size: 12px;
}

#content{
width: 700px;
margin: 20px;
background: #FFFFFF;
border: 4px solid #cc6600;
}
</style>
--%>
    <SCRIPT type="text/javascript">
        function submitEnter(e) {
            var keycode;
            if (window.event) keycode = window.event.keyCode;
            else if (e) keycode = e.which;
            else return true;
            if (keycode == 13) {
                document.frmLogin.submit();
                return false;
            } else return true;
        }
    </SCRIPT>
<link type="text/css" rel="stylesheet" href="http://<%=ApplicationServer.SERVER_NAME%>/css/tournaments/tccc07.css"/>
<link type="text/css" rel="stylesheet" href="http://<%=ApplicationServer.SERVER_NAME%>/css/coders.css"/>
</head>

<body>

<%--
<div align="center" style="background: transparent;">
    <div id="content">
        <div style="margin: 20px;" align="left">
--%>
<div align="center" style="background: transparent;">
    <div id="containAll">
    <div id="content">

<div style="float:right;">
<a href="http://<%=ApplicationServer.SERVER_NAME%>/"><img style="margin-right:30px;" src="http://<%=ApplicationServer.SERVER_NAME%>/i/tournament/tccc07/topcoderlogo.png" border=0/></a>
</div>
<A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=tournaments&d2=tccc07&d3=about"><img style="margin-left:40px;" src="http://<%=ApplicationServer.SERVER_NAME%>/i/tournament/tccc07/tourneyLogo.png" border=0/></A>

        <h1 style="margin:80px 0px 0px 40px; padding:20px 0px 0px 20px;">Component Competition Wager System</h1>

        <div align="center" style="margin:40px;">
        <form method="post" name="frmLogin" action="<jsp:getProperty name="sessionInfo" property="servletPath"/>">
            <input type="hidden" name="<%=BaseServlet.NEXT_PAGE_KEY%>" value="<%= StringUtils.htmlEncode(nextpage) %>">
            <input type="hidden" name="<%=Constants.MODULE_KEY%>" value="Login">
            <table border="0" cellpadding="3" cellspacing="0">
                <tr valign="top">
                    <td class="bodyText" colspan="3" align="center">
                        <span class="bigRed"><%= message %></span>
                    </td>
                </tr>
                <tr valign="middle">
                    <td align="right">Handle:</td>
                    <td colspan="2" align="left">
                        <input type="text" name="<%=Constants.USER_NAME%>" value="" maxlength="15" size="12" onkeypress="submitEnter(event)">
                    </td>
                </tr>
                <tr valign="middle">
                    <td align="right" width="50%">Password:</td>
                    <td align="left">
                        <input type="password" name="<%=Constants.PASSWORD%>" value="" maxlength="30" size="12" onkeypress="submitEnter(event)">
                    </td>
                    <td width="50%">
                        &#160;&#160;<a href="JavaScript:document.frmLogin.submit()">Login&#160;&gt;</a>
                    </td>
                </tr>
            </table>
            <script>
                document.frmLogin.<%=Constants.USER_NAME%>.focus();
            </script>
        </form>
        </div>
        <div style="height:20px;">&nbsp;</div>

    </div>
</div>
</div>
</body>
</html>
