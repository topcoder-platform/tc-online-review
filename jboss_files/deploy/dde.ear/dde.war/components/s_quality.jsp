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
	String page_name = "s_quality.jsp";
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
<jsp:include page="/includes/leftNavAbout.html" flush="false" />

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
				<tr><td><h3>Delivering Exceptional Code</h3></td></tr>
				<tr><td class="normal">
						<p>One of the many advantages to purchasing software components from TopCoder Software is the quality of the components
						themselves.  It is difficult when building custom software solutions to implement rigorous testing procedures.  Time is
						usually a critical factor and a "we'll fix it in the next release" mentality often prevails.</p>

						<p>In contrast, Quality Assurance (QA) is the bedrock of TopCoder Software. Since TopCoder Software components are
						products that will be used by a multitude of customers, it is crucial that they perform consistently and reliably. TopCoder
						Software puts great emphasis on diligently performing several levels of Quality Assurance.  This provides crucial
						consistency of quality among all components.</p>

						<p>TopCoder Software development teams are made up of highly rated members from the TopCoder member base.  These are
						developers who have been rated and ranked through TopCoder coding competitions and have proven to be high quality
						developers.  The development team is made up of members filling roles as Software Architects, Software Developers and
						Quality Assurance Developers.  See our Resources (link) page for more information on TopCoder.</p>

						<p>The Software Developer performs the first level of Quality Assurance in the unit testing of the component. The component
						is not passed on to the next phase of QA until the Software Developer has signed off on the quality of the component and the
						Software Architect has concurred.</p>

						<p>The Quality Assurance Developer then performs the second level of QA.  The Quality Assurance Developer creates and
						executes comprehensive test cases for the component. They develop software to run the tests and work with the Software
						Developer to correct any errors.</p>

						<p>The team Software Architect next reviews the QA Developer's test results and performs additional testing. Once the
						Architect is satisfied that the quality of the component meets TopCoder Software standards, they pass it on to the Product
						Manager for final review.</p>

						<p>The TopCoder Software Product Manager has ultimate responsibility for the quality of the components in their product
						category.  Once they receive the component from the Software Architect, they perform rigorous testing utilizing the
						TopCoder Test Harness. This testing covers scalability, reliability and performance as well as adherence to component
						functional requirements. The TopCoder Software Product Manager is responsible for the delivery and management of all
						components in a specific category of the TopCoder Software Component Catalog.</p>

						<p>Utilizing this rigorous Quality Assurance methodology, TopCoder Software insures that all components in the catalog
						are of the highest quality and are exceptionally reliable.</p>

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
