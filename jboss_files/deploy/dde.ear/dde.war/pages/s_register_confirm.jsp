<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


<%
	// STANDARD PAGE VARIABLES
	String page_name = "s_register_confirm.jsp";
	String action = request.getParameter("a");	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>TopCoder Software</title>
	<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
	<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
</head>

<body class="body">

<!-- Header begins -->
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/nav.jsp" %>
<!-- Header ends -->

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr><td width="100%" height="2" bgcolor="#000000"><img src="/images/clear.gif" alt="" width="10" height="2" border="0" /></td></tr>
</table>
<!-- breadcrumb ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
	<tr valign="top">

<!-- Left Column begins -->
		<td width="165" class="leftColumn">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr><td height="5"><img src="/images/clear.gif" alt="" width="165" height="5" border="0" /></td></tr>
			</table>
		</td>
		<td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0" /></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
		<td width="100%">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
				<tr><td class="normal"><img src="/images/headRegNewAcct.gif" alt="Register New Account" width="545" height="35" border="0" /></td></tr>
				<tr><td><h3>Activate your New Account</h3></td></tr>
			</table>
				
			<table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
				<tr valign="top">
					<td class="normal">
						<p><strong>Thank you for registering your New TopCoder Software Account.</strong></p>

						<p>A confirmation email has been sent to the email address that you provided to us on the New Account Registration 
						Form. When you receive that email, click the link at the bottom of the email to Activate your account.</p>

						<p>If you have any questions about Activating your TopCoder Software Account, call us at 866-TOP-CODE (866-867-2633).</p>
					</td>
				</tr>
				<tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></tr>
			</table>
<!--Middle Column ends -->

<!-- Gutter 3 begins -->
		<td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
<!-- Gutter 3 ends -->
	</tr>
</table>
	
<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
