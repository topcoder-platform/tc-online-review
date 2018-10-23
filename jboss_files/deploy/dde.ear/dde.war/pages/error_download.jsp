<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="javax.servlet.http.*" %>

<%@ page import="com.topcoder.dde.catalog.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>

<%
	// STANDARD PAGE VARIABLES
	String page_name = "error_download.jsp";
	String action = request.getParameter("a");	
%>

<html>
<head>
	<title>TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css">
  
<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js">
</script>

</head>

<body class="body" marginheigh="0" marginwidth="0">

<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/nav.jsp" %>

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td valign="middle" width="1%" class="breadcrumb"><img src="/images/clear.gif" alt="" width="10" height="1" border="0"/></td>
		<td valign="middle" width="1%" class="breadcrumb" nowrap><strong></strong></td>
		<td valign="middle" width="98%" class="breadcrumb"><img src="/images/clear.gif" alt="" width="10" height="1" border="0"/></td>
	</tr>
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
		<td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0"></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
		<td width="100%">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
				<tr><td><h3>You need permission to download this software</h3></td></tr>
				<tr><td class="normal">
						<p><a href="s_subscriptions.jsp">Subscriptions</a> to the TopCoder Software Component Catalog include access to all components in the catalog, plus any updates and new 
						components added during the term of your suscription. To purchase a Subscription call us at 1-866-TOP-CODE or email us at <a href="mailto:sales@topcodersoftware.com">sales@topcodersoftware.com</a>.</p>

						<p>TopCoder Software <a href="c_prodTools.jsp?comp=600191">Productivity Tools</a> are now available for purchase. Discounts apply for 
						TopCoder Software Subscription customers. To purchase Productivity Tools call us at 1-866-TOP-CODE or email us at <a href="mailto:sales@topcodersoftware.com">sales@topcodersoftware.com</a>.</p>

						<p>If you are having technical difficulties contact us at <a href="mailto:service@topcodersoftware.com">service@topcodersoftware.com</a>.</p>
					</td>
				</tr>
				<tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0"/></td></tr>
			</table>
		</td>

<!-- Gutter 3 begins -->
		<td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"></td></form>
<!-- Gutter 3 ends -->
	</tr>
</table>
	
<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
