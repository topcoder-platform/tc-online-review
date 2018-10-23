<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.topcoder.dde.catalog.Catalog" %>

<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>Download software from the Component Catalog at TopCoder</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css">
<jsp:include page="/includes/header-files.jsp" />
<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js">
</script>

</head>

<body class="body">

<c:set value="<%=new Long(Catalog.NET_CATALOG)%>" var="NET_CATALOG"/>
<c:set value="<%=new Long(Catalog.JAVA_CATALOG)%>" var="JAVA_CATALOG"/>
<c:set value="<%=new Long(Catalog.FLASH_CATALOG)%>" var="FLASH_CATALOG"/>
<c:set value="<%=new Long(Catalog.JAVA_CUSTOM_CATALOG)%>" var="JAVA_CUSTOM_CATALOG"/>
<c:set value="<%=new Long(Catalog.NET_CUSTOM_CATALOG)%>" var="NET_CUSTOM_CATALOG"/>

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
            <jsp:include page="/includes/left.jsp" >
                <jsp:param name="level1" value="components"/>
                <jsp:param name="level2" value="find"/>
            </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0"></td>
<!-- Gutter ends -->

<!-- Center Column begins -->
        <td width="99%">
            <div style="width:580px; margin-bottom: 40px;" align="left">
                <div style="margin: 15px 0;">
                    <img src="/images/hd_components.png" alt="Components" border="0" >
                </div>
                <p  style="padding-bottom: 10px;">
                    The following is a list of TopCoder components that are available to all members for download. These components may be used for trial or non-commercial purposes.  If you are interested in gaining full trial access to download components or you wish to purchase access to the components for commercial purposes, please send an email to <a href="mailto:service@topcoder.com">service@topcoder.com</a> and explain the situation. You may download from this list up to <strong>${max_downloads}</strong> times.
                </p>
                <p style="padding-bottom: 10px;">
                    NOTE: ${max_downloads} downloads does not mean ${max_downloads} components.  For example if you download the same component twice, it counts as 2 downloads.
                </p>
                <p align="center" style="padding-bottom: 10px;">
                    You have <strong>${remaining_downloads}</strong> downloads remaining.
                </p>
                <p style="border-bottom: 1px solid #000000; padding-bottom: 5px;">
                    <strong>Sample components</strong>
                </p>
                <p>
                    <c:forEach var="component" items="${public_components}">
                        <c:choose>
                            <c:when test="${component.rootCategory == NET_CATALOG}">
                                <img src="/images/dotnetSm.gif" alt="" border="0" align="absmiddle" />
                            </c:when>
                            <c:when test="${component.rootCategory == JAVA_CATALOG}">
                                <img src="/images/javaSm.gif" alt="" border="0" align="absmiddle" />
                            </c:when>
                            <c:when test="${component.rootCategory == FLASH_CATALOG}">
                                <img src="/images/flashSm.gif" alt="" border="0" align="absmiddle" />
                            </c:when>
                            <c:when test="${component.rootCategory == JAVA_CUSTOM_CATALOG}">
                                <img src="/images/javaCustomSm.gif" alt="" border="0" align="absmiddle" />
                            </c:when>
                            <c:when test="${component.rootCategory == NET_CUSTOM_CATALOG}">
                                <img src="/images/dotnetCustomSm.gif" alt="" border="0" align="absmiddle" />
                            </c:when>
                        </c:choose>                    
                        <a href="/tcs?module=ViewComponentTerms&comp=${component.primaryKey}&ver=${component.currentVersion}">${component.componentName}</a><br/>
                    </c:forEach>
                </p>
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

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</form>
</body>
</html>
