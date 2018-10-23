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
    String page_name = "s_productivity.jsp";
    String action = request.getParameter("a");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>TopCoder Software</title>
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
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="100%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td class="normal"><img src="/images/hd_aboutus.png" alt="About Us" border="0" /></td></tr>
                <tr><td><h3>Productivity: Lowering the cost of development</h3></td></tr>
                <tr><td class="normal">
                        <p>Custom software development is expensive due to the high cost of the software development team. Developers, Senior
                        Developers, Project Managers, Technical Writers, Account Managers and the like all combine to form a costly structure
                        for the development of software.</p>

                        <p>Use of TopCoder Software components can drive down the cost of software development in two ways. The first way is
                        by introducing re-usable components to the development life cycle. TopCoder Software components can drive down the
                        cost of development as well as time to market by reducing the amount of software that must be built. The development
                        team can focus on customizing and integrating these components into a business solution.</p>

                        <p>The other way TopCoder Software can drive down the cost of development is by lowering the cost of the components
                        themselves. TopCoder Software utilizes the extensive TopCoder member base as a distributed development resource. In
                        this way, the components themselves can be developed at a lower cost, and that cost can then be leveraged over multiple
                        customers.  The result is lower cost, high quality software components.</p>

                        <p>Utilizing Component Based Development to reduce the amount of software to be written and taking advantage of low
                         cost high quality components from TopCoder Software can greatly decrease the total cost of the business solution.</p>

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
