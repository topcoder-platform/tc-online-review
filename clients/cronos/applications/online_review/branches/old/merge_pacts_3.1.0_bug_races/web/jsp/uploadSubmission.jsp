<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="uploadSubmission.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <script language="JavaScript" type="text/javascript"
        src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
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
                                <td><html:img src="/i/${categoryIconName}" alt="" border="0" /></td>
                                <td><html:img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
                                <td>
                                    <span class="bodyTitle">${project.allProperties["Project Name"]}</span>
                                    <c:if test='${!(empty project.allProperties["Project Version"])}'>
                                        <font size="4"><bean:message key="global.version" />
                                            ${project.allProperties["Project Version"]}</font>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div><br />

                    <html:errors />
                    <html:form action="/actions/UploadSubmission" method="POST" enctype="multipart/form-data">
                        <html:hidden property="method" value="uploadSubmission" />
                        <html:hidden property="postBack" value="y" />
                        <html:hidden property="pid" value="${project.id}" />

                        <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                            <tr>
                                <td class="title"><bean:message key="uploadSubmission.box.UploadSubmission" /></td>
                            </tr>
                            <tr class="light">
                                <td class="value">
                                    <bean:message key="uploadSubmission.HelpLine1" />
                                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=support&d3=${(project.projectCategory.id eq 1) ? 'des' : 'dev'}ScreeningSample"><bean:message key="linkHere" /></a>.<br /><br />
                                    <bean:message key="uploadSubmission.UploadSubmission" />
                                    <html:file property="file" size="20" styleClass="inputBox" style="width:350px;vertical-align:middle;" />
                                    <html:image srcKey="btnUpload.img" altKey="btnUpload.alt" border="0" style="vertical-align:bottom;" /><br /><br />
                                    <bean:message key="uploadSubmission.HelpLine2" />
                                    <bean:message key="uploadSubmission.HelpLine3" />

                                    <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=support&d3=${(project.projectCategory.id eq 1) ? 'des' : 'dev'}Documentation"><bean:message key="uploadSubmission.SampleSubmissionAndDocs" /></a>

                                    <bean:message key="uploadSubmission.HelpLine4" />
                                </td>
                            </tr>
                            <tr>
                                <td class="lastRowTD"><!-- @ --></td>
                            </tr>
                        </table><br /><br />
                    </html:form>

                    <div align="right">
                        <a href="javascript:history.go(-1)"><html:img srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></a>
                    </div><br />

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
