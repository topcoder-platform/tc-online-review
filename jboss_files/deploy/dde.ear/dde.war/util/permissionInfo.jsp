<%@ page import="com.topcoder.dde.util.ApplicationServer" %>
<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
    <title>TopCoder :: Component Catalog Access</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css">

<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js">
</script>
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
<div class="minheight">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
            <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="index"/>
            </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter ends -->

<!-- Center Column begins -->
        <td width="99%" align="center">
            <div style="width:510px; margin-bottom: 40px;" align="left">
                <div align="center" style="margin: 40px;">
                    <img src="/i/catalog/genCode.png" alt="TopCoder Software Components"/>
                </div>

                <p>
                    Before you can download software from the TopCoder Component Catalog, you will need to provide <strong>an access code</strong> that you should have been given.
                </p>

                <c:choose>
                    <c:when test="${sessionInfo.loggedIn}">
                        <p align="center">
                            Do you have an access code?
                        </p>

                        <p align="center">
                            <a href="/tcs?module=RequestPermissionView">Yes, I have a code.</a>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p align="center">
                            You need to log in before continuing:
                        </p>
                        <p align="center">
                            <a href="/tcs?module=RequestPermissionView">Log in</a> | <a href="https://<%=ApplicationServer.TC_SERVER%>/reg/?module=Main&rt=2">Register</a>
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </td>
<!-- Center Column begins -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td>
<!-- Gutter ends -->

<!--Right Column begins -->
        <td width="170">
            <table border="0" cellpadding="0" cellspacing="0" width="170">
                <tr><td height="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td></tr>
                <tr><td>
<%--
                <jsp:include page="/includes/topDownloads.jsp" />
                <jsp:include page="/includes/newReleases.jsp" />
              <jsp:include page="/includes/right.jsp" >
                  <jsp:param name="level1" value="index"/>
              </jsp:include>
--%>
              </td></tr>
            </table>
        </td>
<!--Right Column ends -->

<!-- Gutter begins -->
        <td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"></td>
<!-- Gutter ends -->

    </tr>
</table>
</div>
<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
