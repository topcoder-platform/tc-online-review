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
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js">
</script>

</head>

<body class="body" marginheigh="0" marginwidth="0">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp"/>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->
<div class="minheight">
<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
            <jsp:include page="/includes/left.jsp" >
                <jsp:param name="level1" value="attention"/>
            </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter ends -->
    </tr>
</table>
</div>
<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
