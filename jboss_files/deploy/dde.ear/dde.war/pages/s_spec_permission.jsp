<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page import="com.topcoder.web.common.WebConstants" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%
	// STANDARD PAGE VARIABLES
	String page_name = "s_spec_permission.jsp";
	String action = request.getParameter("a");	
	
	long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID;
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

<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/nav.jsp" %>

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
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
				<tr><td><h3>You do not have permission to participate in this Specification forum</h3></td></tr>
				<tr><td class="normal">
						<p>TopCoder Software selects a few experienced individuals to participate in each Specification forum. You may still 
						participate in <a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Collaboration forums.</a></p>

						<p>Contact us at <a href="mailto:service@topcodersoftware.com">service@topcodersoftware.com</a> to find out how you 
						can participate in future Specification forums.</p>
					</td>
				</tr>
				<tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></tr>
			</table>
		</td>

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
