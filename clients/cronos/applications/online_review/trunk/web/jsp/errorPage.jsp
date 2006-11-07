<%@ taglib  uri="/tags/struts-html" prefix="html" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%@ page language="java" session="true" isErrorPage="true" %>


<html>
<head>
    <title>TopCoder - Error</title>
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/home.css' />"/>
    <link type="image/x-icon" rel="shortcut icon" href="<html:rewrite href='/i/favicon.ico' />"/>
    <STYLE TYPE="text/css">
        body {
            text-align: center;
        }

        .centerer {
            width: 400px;
            background-image: url( /i/interface/errorBox.gif );
            background-repeat: no-repeat;
            background-position: top center;
            text-align: left;
            margin-top: 20px;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: 20px;
            padding: 0px;
        }
    </STYLE>
</HEAD>

<body>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td class="homeTopBar" align=left>
        </td>
        <td class="homeTopBar" align=right><A href="/tc?module=Static&d1=about&d2=index" class="loginLinks">About
            TopCoder</A></td>
    </tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td width="50%" class="homeLogo" align="left">
            <A href="/"><html:img src="/i/home/TC_homeLogo.gif" alt="TopCoder" /></A></td>
        <td width="50%" class="homeLogo" align="right">&#160;</td>
    </tr>
</table>

<br><br>
<%
	// try to print stack trace into a String object.

	String stackTrace;	
	if(exception == null) {
		stackTrace = "exception is null";
	} else {
		StringWriter sw = new StringWriter();
		
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        pw.close();
        
        stackTrace = sw.toString();
        stackTrace = stackTrace.replaceAll("\n", "<br>");
        stackTrace = stackTrace.replaceAll("\t", "&nbsp&nbsp&nbsp&nbsp ");
	}

%>
<div align="left" class="homeText">
Exception Info (for debugging): <%= stackTrace %>
</div>
<div class="centerer">
    <div style="padding:25px"><b>
        <span style="font-size: 18px; color: #990000;">Error</span>
        <br><br>
   <span class="homeText">
      <b>An error has occurred when attempting to process your request.</b>
      <br><br>
      You may click <a href="javascript:history.back();">here</a> to return to the last page you were viewing.
      <br><br>
      If you have a question or comment, please email <a HREF="mailto:service@topcoder.com" CLASS="bodyText">service@topcoder.com</a>.
   <br><br><br><br><br><br><br><br><br><br><br><br><br><br>
       <%=new Date().toString()%>
   </span></b>
    </div>
</div>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr><td class="homeFooter" colspan="5">
        <A href="/" class="footerLinks">Home</A>&#160;&#160;|&#160;&#160;
        <A href="/tc?module=Static&d1=about&d2=index" class="footerLinks">About TopCoder</A>&#160;&#160;|&#160;&#160;
        <A href="/tc?module=Static&d1=pressroom&d2=index" class="footerLinks">Press Room</A>&#160;&#160;|&#160;&#160;
        <A href="/tc?module=Static&d1=about&d2=contactus" class="footerLinks">Contact Us</A>&#160;&#160;|&#160;&#160;
        <A href="/tc?module=Static&d1=about&d2=privacy" class="footerLinks">Privacy</A>&#160;&#160;|&#160;&#160;
        <A href="/tc?module=Static&d1=about&d2=terms" class="footerLinks">Terms</A>
        <br>
        <A href="/tc" class="footerLinks">Developer Center</A>&#160;&#160;|&#160;&#160;
        <A href="/corp/?module=Static&d1=corp&d2=index" class="footerLinks">Corporate Services</A></td></tr>
    <tr><td class="copyright" colspan="5">Copyright &#169; 2001-2004, TopCoder, Inc. All rights reserved.</td></tr>
</table>
</body>
</html>
