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
    String page_name = "s_definition.jsp";
    String action = request.getParameter("a");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Software Components form the basis of the applications built by TopCoder Software</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
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

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="components"/>
            <jsp:param name="level2" value="find"/>
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="99%" align=center valign=top>
			<div class="appSuiteIndexBody">
			<img src="/images/appSuitePromo.jpg" border=0/><br/>
			<p>
			Imagine being able to meet all your web business needs in one place. Imagine a toolbox with every tool you could need to build that web business. TopCoder Software Application Suites were built from the ground up with this approach in mind.
			</p>
			<br/>
			<hr noshade="noshade" size="1" width="100%">
			<table cellspacing=0 cellpadding=10 border=0 width="100%">
				<tr valign=top>
					<td valign=top align=center width="50%">
						<img src="/images/catalog/java_logo.jpg" border=0/><br/>
						<hr noshade="noshade" size="1" width="100%">
						<b><A href="/components/appSuite.jsp?appSuiteID=jas_eCommerce">eCommerce</A></b><br/>
					</td>
					<td class=bodyText valign=top align=center><img src="/images/appSuiteQues.gif" border=0/></td>
					<td valign=top align=center width="50%">
						<img src="/images/catalog/dotnet_logo.jpg" border=0/><br/>
						<hr noshade="noshade" size="1" width="100%">
						<b><A href="/components/appSuite.jsp?appSuiteID=nas_eCommerce">eCommerce</A></b><br/>
<%--						<b><A href="/components/appSuite.jsp?appSuiteID=nas_DocumentManagement">Document Management</A></b><br/> --%>
					</td>
				</tr>
			</table>
			<hr noshade="noshade" size="1" width="100%">
			<br/>
			<p  class="learn_more"><a href="http://www.topcoder.com/tc?module=Static&d1=about&d2=contactus">Contact us</a> today so we can get started developing your next application.</p>
        	</div>
		</td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
        <jsp:include page="/includes/right.jsp" >
            <jsp:param name="level1" value="index"/>
        </jsp:include>
        </td>
<!--Right Column ends -->

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
