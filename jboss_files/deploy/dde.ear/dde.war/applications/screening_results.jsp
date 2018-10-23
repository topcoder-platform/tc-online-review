<%@ page contentType="text/html; charset=ISO-8859-1"
import="com.topcoder.dde.util.Constants,
        java.util.Iterator,
        java.util.List,
        com.topcoder.apps.screening.ScreeningResponse" %>

<jsp:useBean id="sessionInfo" class="com.topcoder.web.common.SessionInfo" scope="request"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>
<head>
    <title>TopCoder Software</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
         <c:set var="hasErrors" value="${not empty errors}"/>
         <c:set var="hasWarnings" value="${not empty warnings}"/>
         <c:set var="hasSuccess" value="${not empty success}"/>
         <c:set var="screeningFinished" value="${hasErrors || hasWarnings || hasSuccess}"/>
          
        <c:if test="${!screeningFinished}">
            <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
            <META HTTP-EQUIV="Expires" CONTENT="-1">
            <META HTTP-EQUIV="Refresh" CONTENT="10">
        </c:if>
        <TITLE>Screening results</TITLE>
</head>
<body class="body">
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
            <jsp:include page="/includes/left.jsp" >
                <jsp:param name="level1" value=""/>
                <jsp:param name="level2" value=""/>
            </jsp:include>
        </td>
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
        <td width="100%">

<%------------ORIGINAL CONTENT--------------%>
<div align="center">
    <div style="padding: 20px 15px 20px 15px; width: 500px; text-align:left;">
                        <c:choose>
                            <c:when test="${screeningFinished}">
                                <c:if test="${hasErrors}">
<h3>Fatal Errors</h3>
                                    <c:forEach items="${errors}" var="errorItem">
                                                <c:out value="${errorItem.code}"/>: <c:out value="${errorItem.response}"/>
                                                <ul>
                                                    <c:forEach items="${errorItem.text}" var="errorText">
                                                        <li>
                                                            <c:out value="${errorText}"/>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${hasWarnings}">
<h3>Warnings</h3>
                                    <c:forEach items="${warnings}" var="warningItem">
                                                <c:out value="${warningItem.code}"/>: <c:out value="${warningItem.response}"/>
                                                <ul>
                                                    <c:forEach items="${warningItem.text}" var="warningText">
                                                        <li>
                                                            <c:out value="${warningText}"/>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${hasSuccess}">
<h3>Messages</h3>
                                    <c:forEach items="${success}" var="succesItem">
                                                <c:out value="${succesItem.code}"/>: <c:out value="${succesItem.response}"/>
                                                <ul>
                                                    <c:forEach items="${succesItem.text}" var="succesText">
                                                        <li>
                                                            <c:out value="${succesText}"/>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                    </c:forEach>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                        <div align="center">
                                        <img src="/images/interface/processing.gif" alt="Processing..." />
                                        </div>
                                        <br>
<h3>Screening still in progress</h3>
                                        The page should be automatically refreshed in 10 seconds, if it is not refreshed, please click 
                                        <A href="/tcs?module=ViewUploadResults&<%=Constants.SPECIFICATION_KEY%>=<%=request.getAttribute(Constants.SPECIFICATION_KEY)%>">here</a>.
                            </c:otherwise>
                        </c:choose>
    <div>
<div>
<%--------------------------%>

</td>
<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<td width="170">
<jsp:include page="/includes/right.jsp" >
   <jsp:param name="level1" value="components"/>
</jsp:include>
</td>

<!-- Gutter 3 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>

    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />

</body>
</html>
