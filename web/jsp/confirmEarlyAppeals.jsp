<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays early appeals confirmation page. Based on the value of "complete" it will
  - ask for confirmation to mark appeals as completed or resume appealing. 
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="confirmEarlyAppeals.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript"
        src="/js/or/rollovers2.js"><!-- @ --></script>
</head>

<body>
<div align="center">

    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />

        <jsp:include page="/includes/project/project_tabs.jsp" />

            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">

                    <div style="padding: 11px 6px 9px 0px;">
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr valign="middle">
                                <td><img src="/i/${categoryIconName}" alt="" border="0" /></td>
                                <td><img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
                                <td>
                                    <span class="bodyTitle">${project.allProperties["Project Name"]}</span>
                                    <c:if test='${!(empty project.allProperties["Project Version"])}'>
                                        <font size="4"><or:text key="global.version" />
                                            ${project.allProperties["Project Version"]}</font>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div><br />

                    <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                        <tr>
                            <td class="title"><or:text key="confirmEarlyAppeals.box.title" /></td>
                        </tr>
                        <tr class="light">
                            <td class="valueC">
                                <br />
                                <c:choose>
                                    <c:when test="${complete}">
                                        <or:text key="confirmEarlyAppeals.completeWarning" /><br /><br />
                                        <or:text key="confirmEarlyAppeals.completeQuestion" /><br /><br />
                                    </c:when>
                                    <c:otherwise>
                                        <or:text key="confirmEarlyAppeals.resumeWarning" /><br /><br />
                                        <or:text key="confirmEarlyAppeals.resumeQuestion" /><br /><br />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td class="lastRowTD"><!-- @ --></td>
                        </tr>
                    </table><br />

                    <div align="right">
                        <a href="<or:url value='/actions/EarlyAppeals?pid=${project.id}&perform=y' />"><img src="<or:text key='confirmEarlyAppeals.btnConfirm.img' />" alt="<or:text key='confirmEarlyAppeals.btnConfirm.alt' />" border="0" /></a>&#160;
                        <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                            <img src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0" />
                        </a>
                    </div><br />

                </div>
            </div>

        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
