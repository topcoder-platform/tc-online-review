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
	String page_name = "s_about.jsp";
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
				<tr><td><h3>How software will be built</h3></td></tr>
				<tr><td class="normal">
						<p>TopCoder Software, a division of TopCoder, Inc. is committed to driving down the cost of software development by
						manufacturing and distributing high quality, frameworks based re-usable software components.</p>

						<p><strong>The Vision</strong><br />
						TopCoder Software's focus is to drive software development to the middle of the software development spectrum. In this
						way, companies can get the flexibility, performance and functionality of custom software at a cost commensurate with
						packaged solutions. TopCoder Software will deliver this vision by utilizing the TopCoder distributed member base to
						create reusable business and infrastructure components. This approach, using the highest quality developers to deliver
						component based software, can drive down the cost of the solution while delivering extremely flexible, high quality software.</p>

						<p><strong>Who Benefits</strong><br />
						Companies in all industries can benefit from the increased <a href="s_productivity.jsp">productivity</a>,
						use of standard frameworks and <a href="s_reuse.jsp">reuse</a> of components while software
						integrators can benefit by adding more value for their customers.</p>

						<p>For examples and to see what's available, visit our <a href="c_showroom.jsp">Component Catalog</a>.</p>

						<div align="center"><img src="/images/valueSpectrum2.gif" alt="" width="450" height="250" border="0" /></div></td>
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
