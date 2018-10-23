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
        <td width="99%" align=center>
            <img src="/images/clear.gif" width="1" height="15" alt="" border="0"><br>

<table width="60%">
  <tr>
    <td colspan="2">
        <h2>You don't have the latest version of Macromedia Flash Player.</h2>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <p />
      This web site makes use of <a href="http://www.macromedia.com/software/flash/" target="_new">Macromedia<sup><font size="-1">&reg;</font></sup>Flash<sup><font size="-2">TM</font></sup></a> software. You have an old version of Macromedia Flash Player that cannot play the content we've created.
    </td>
  </tr>
  <tr>
    <td>
      <a href="http://www.macromedia.com/go/getflashplayer"><img src="/images/flash/getFlash.gif" border="0" width="88" height="31" /></a>
    </td>
    <td width="100%">
      Why not download and install the latest version now? It will only take a moment.
    </td>
  </tr>
  <tr>
    <td colspan="2">
        <br/>Macromedia and Flash are trademarks of Macromedia, Inc.
    </td>
  </tr>
</table>

            <p><br></p>

        </td>
<!-- Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
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
