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
	String page_name = "s_standards.jsp";
	String action = request.getParameter("a");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>TopCoder Software</title>
	<jsp:include page="/includes/header-files.jsp" />
<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
	<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>

</head>

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
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
				<tr><td height="5"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>

<!-- About Us Catagories -->
<%@ include file="/includes/leftNavAbout.jsp" %>

			</table>
		</td>
		<td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0" /></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
		<td width="100%">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
				<tr><td class="normal"><img src="/images/hd_aboutus.png" alt="About Us" border="0" /></td></tr>
				<tr><td><h3>Use of standard frameworks</h3></td></tr>
				<tr><td class="normal">
						<p>An important advantage to re-usable components is the flexibility they bring to business solutions. The utilization of
						industry frameworks such as J2EE, .NET/COM+ and Web Services enforces the creation of flexible and easily
						customizable software.  The use of frameworks based software components can speed up the development cycle. These
						components can be easily extended for additional functionality as well as quickly bundled together to create full business
						solutions.</p>

						<p>TopCoder Software is the premier source for these enterprise software components. Whether EJB, COM+ or Web
						Services TopCoder Software provides high quality, flexible components for the enterprise.</p>

						<p>See what is available today. Visit our <a href="c_showroom.jsp">Component Catalog</a>.</p></td>
				</tr>
				<tr><td><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></tr>
			</table>
		</td>

<!-- Middle Column ends -->

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
