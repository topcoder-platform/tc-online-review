<%@ page import="javax.naming.*,
                 com.topcoder.dde.util.ApplicationServer" %>
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
    <title>How engineering meets manufacturing at TopCoder</title>
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
            <jsp:param name="level1" value="about"/>
            <jsp:param name="level2" value=""/>
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="99%">
        <div align="center">
            <img src="/images/clear.gif" width="1" height="15" alt="" border="0"><br>
            <img src="/images/about_title.png" alt="About TopCoder" width="530" height="30" border="0" />
<table width="530" border="0" cellspacing="0" cellpadding="0">
<tr><td align="left" colspan="2">
<H3>TopCoder Software Application Development Methodology Explained</H3>
<span class="bodyText">You will need QuickTime to view these movies. If you don't have it, download it <a href="http://www.apple.com/quicktime/download/win.html" target="blank">here</a> for free.</span>
<br /><br /></td></tr>

<tr>
<td align="center" valign="top" width="170" style="padding:10px"><A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/applDevMethod.mp4"><img src="/images/tcsmorris_1b.jpg" alt="" border="0" /></A></td>
<td class="bodyText" valign="middle" style="padding:10px"><b>Part 1:</b> <A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/applDevMethod.mp4">Application Development Methodology</A><br>(10.1 MB)</td>
</tr>
<tr>
<td align="center" valign="top" style="padding:10px"><A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/reqSpecs.mp4"><img src="/images/tcsmorris_2a.jpg" alt="" border="0" /></A></td>
<td class="bodyText" valign="middle" style="padding:10px"><b>Part 2:</b> <A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/reqSpecs.mp4">Requirements and Specifications</A><br>(5.4 MB)</td>
</tr>
<tr>
<td align="center" valign="top" width="170" style="padding:10px"><A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/specPhase.mp4"><img src="/images/tcsmorris_3c.jpg" alt="" border="0" /></A></td>
<td class="bodyText" valign="middle" style="padding:10px"><b>Part 3:</b> <A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/specPhase.mp4">Specification Phase</A><br>(18.4 MB)</td>
</tr>
<tr>
<td align="center" valign="top" width="170" style="padding:10px"><A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/specPhaseQA.mp4"><img src="/images/tcsmorris_4a.jpg" alt="" border="0" /></A></td>
<td class="bodyText" valign="middle" style="padding:10px"><b>Part 4:</b> <A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/specPhaseQA.mp4">Specification Phase - QA Plan</A><br>(8.1 MB)</td>
</tr>
<tr>
<td align="center" valign="top" width="170" style="padding:10px"><A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/applArchPhase.mp4"><img src="/images/tcsmorris_5a.jpg" alt="" border="0" /></A></td>
<td class="bodyText" valign="middle" style="padding:10px"><b>Part 5:</b> <A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/applArchPhase.mp4">Application Architecture Phase -<br>Custom Applications Using Component Building Blocks</A><br>(11.7 MB)</td>
</tr>
<tr>
<td align="center" valign="top" width="170" style="padding:10px"><A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/manuDiscipline.mp4"><img src="/images/tcsmorris_6a.jpg" alt="" border="0" /></A></td>
<td class="bodyText" valign="middle" style="padding:10px"><b>Part 6:</b> <A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/manuDiscipline.mp4">Manufacturing Discipline</A><br>(28 MB)</td>
</tr>
<tr>
<td align="center" valign="top" width="170" style="padding:10px"><A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/usingCompetitionI.mp4"><img src="/images/tcsmorris_7b.jpg" alt="" border="0" /></A></td>
<td class="bodyText" valign="middle" style="padding:10px"><b>Part 7:</b> <A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/usingCompetitionI.mp4">Using Competition to Build Software I</A><br>(15.3 MB)</td>
</tr>
<tr>
<td align="center" valign="top" width="170" style="padding:10px"><A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/usingCompetitionII.mp4"><img src="/images/tcsmorris_8.jpg" alt="" border="0" /></A></td>
<td class="bodyText" valign="middle" style="padding:10px"><b>Part 8:</b> <A href="http://<%=ApplicationServer.TC_SERVER%>/movies/promotional/usingCompetitionII.mp4">Using Competition to Build Software II</A><br>(14 MB)</td>
</tr>
<tr><td colspan="2">&#160;</td></tr>
</table>

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

