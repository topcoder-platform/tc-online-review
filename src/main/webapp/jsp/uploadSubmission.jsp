<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the Specification Review scorecard.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="uploadSubmission.title" />
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

                    <s:actionerror escape="false" />
                    <s:form action="UploadContestSubmission" method="POST" enctype="multipart/form-data" namespace="/actions">
                        <input type="hidden" name="postBack" value="y" />
                        <input type="hidden" name="pid" value="${project.id}" />

                        <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                            <tr>
                                <td class="title"><or:text key="uploadSubmission.box.UploadSubmission" /></td>
                            </tr>
                            <tr class="light">
                                <td class="value">
                                    <or:text key="uploadSubmission.UploadSubmission" />
                                    <input type="file" name="file" size="40" class="inputBox" style="width:350px;vertical-align:middle;"  value="<or:fieldvalue field='file' />" />
                                    <input type="image"  src="<or:text key='btnUpload.img' />" alt="<or:text key='btnUpload.alt' />" border="0" style="vertical-align:bottom;" /><br /><br />
                                    <or:text key="uploadSubmission.HelpLine1" />
                                    <or:text key="uploadSubmission.HelpLine2" />

                                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/wiki/display/tc/Documentation+and+Submission+Guidelines"><or:text key="uploadSubmission.SampleSubmissionAndDocs" /></a>

                                    <or:text key="uploadSubmission.HelpLine3" />
                                    <c:if test="${showMultipleSubmissionsWarning == true}">
                                        <br/><br/><span class="error"><or:text key="uploadSubmission.MultipleSubmissionsWarning" /></span>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="lastRowTD"><!-- @ --></td>
                            </tr>
                        </table><br /><br />
                    </s:form>

                    <div align="right">
                        <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                            <img src="<or:text key='btnBack.img' />" alt="<or:text key='btnBack.alt' />" border="0" />
                        </a>
                    </div><br />

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
