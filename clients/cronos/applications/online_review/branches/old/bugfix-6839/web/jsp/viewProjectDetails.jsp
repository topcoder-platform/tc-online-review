<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
    <jsp:include page="/includes/project/project_title.jsp" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/phasetabs.css' />" />
    <script language="JavaScript" type="text/javascript"
        src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript"
        src="<html:rewrite href='/js/or/dojo.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<html:rewrite page='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript"
        src="<html:rewrite href='/js/or/ajax1.js' />"><!-- @ --></script>

<script language="JavaScript" type="text/javascript">
    /**
     * This function is designed to send AJAX requests for timeline notification setting change
     */
    function setTimelineNotification(pid, chbox) {
        chbox.disabled = true; // Disable the checkbox temporarily
        var targetStatus = (chbox.checked) ? "On" : "Off";
        // Assemble the request XML
        var content =
            '<?xml version="1.0" ?>' +
            '<request type="SetTimelineNotification">' +
            "<parameters>" +
            '<parameter name="ProjectId">' +
            pid +
            "</parameter>" +
            '<parameter name="Status">' +
            targetStatus +
            "</parameter>" +
            "</parameters>" +
            "</request>";

        // Send the AJAX request
        sendRequest(content,
            function (result, respXML) {
                // Operation succeeded; do nothing, but enable the checkbox back
                chbox.disabled = false;
            },
            function (result, respXML) {
                // Operation failed, alert the error message to the user
                alert("An error occured while setting the Timeline change notification: " + result);
                // Checkbox's status needs to be reset
                chbox.checked = !chbox.checked;
                // And finally, enable the checkbox
                chbox.disabled = false;
            }
        );
    }
</script>
</head>

<body>
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
        <jsp:include page="/includes/project/project_tabs.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">
                    <jsp:include page="/includes/project/project_info.jsp" /><br />
                    <jsp:include page="/includes/project/project_myrole.jsp" />
                    <jsp:include page="/includes/project/project_timeline.jsp" />
                    <jsp:include page="/includes/project/project_phase.jsp" />
                    <jsp:include page="/includes/project/project_detail.jsp" />
                    <jsp:include page="/includes/project/project_resource.jsp" />
                    <div align="right">
                        <c:if test="${isAllowedToEditProjects}">
                            <a href="EditProject.do?method=editProject&pid=${project.id}"><html:img srcKey="viewProjectDetails.btnEdit.img" border="0" altKey="viewProjectDetails.btnEdit.alt" /></a>&#160;
                        </c:if>
                        <a href="javascript:history.go(-1)"><html:img srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></a>
                    </div>
                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
