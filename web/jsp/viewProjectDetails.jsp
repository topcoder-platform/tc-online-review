<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2006 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page provides the view for details for requested project.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/phasetabs.css" />

    <script language="JavaScript" type="text/javascript"
        src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript"
        src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<or:url value='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript"
        src="/js/or/ajax1.js"><!-- @ --></script>

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

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">
    <script type="text/javascript">
        function updateForumLink(projectId) {
            return fetch("<%=com.cronos.onlinereview.util.ConfigHelper.getChallengeByLegacyIdUrlV5()%>" + projectId)
                .then((response) => response.json())
                .then((data) => {
                    let with_forum = data.filter(item => 'discussions' in item);
                    let id = with_forum?.[0]?.id;
                    if (id !== undefined) {
                        let forumLinkEl = document.querySelector('.projectInfo__forumLink');
                        return forumLinkEl.href = "https://<%=ApplicationServer.FORUMS_SERVER_NAME%>/categories/" + id;
                    }
                });
        }

        document.addEventListener("DOMContentLoaded", function(){
            let projectId = ${project.id};
            updateForumLink(projectId);

            let accordion = document.getElementsByClassName("projectDetails__accordion");
            for (let i = 0; i < accordion.length; i++) {
                accordion[i].addEventListener("click", function() {
                    this.classList.toggle("projectDetails__accordion--collapse");
                    let section = document.getElementsByClassName("projectDetails__sectionBody")[i];
                    if (section.style.display === "none") {
                        section.style.display = "block";
                    } else {
                        section.style.display = "none";
                    }
                });
            }

            let expand = document.querySelector(".accordionAction__expandAll");
            expand.addEventListener("click", function() {
                for (let i = 0; i < accordion.length; i++) {
                    accordion[i].classList.remove("projectDetails__accordion--collapse");
                    let section = document.getElementsByClassName("projectDetails__sectionBody")[i];
                    section.style.display = "block";
                }
            });

            let collapse = document.querySelector(".accordionAction__collapseAll");
            collapse.addEventListener("click", function() {
                for (let i = 0; i < accordion.length; i++) {
                    accordion[i].classList.add("projectDetails__accordion--collapse");
                    let section = document.getElementsByClassName("projectDetails__sectionBody")[i];
                    section.style.display = "none";
                }
            });

            let avatar = document.querySelector('.webHeader__avatar a');
            let avatarImage = document.createElement('div');
            avatarImage.className = "webHeader__avatarImage";
            let twoChar = avatar.text.substring(0, 2);
            avatarImage.innerText = twoChar;
            avatar.innerHTML = avatarImage.outerHTML;
        });
    </script>

</head>

<body>

<jsp:include page="/includes/inc_header_reskin.jsp" />

<jsp:include page="/includes/project/project_tabs_reskin.jsp" />

<div class="content content--projectDetails">
    <div class="content__inner">
        <jsp:include page="/includes/project/project_info_reskin.jsp" />
        <div class="divider"></div>
        <div class="accordionAction">
            <div class="accordionAction__expandAll">Expand All</div>
            <div class="accordionAction__collapseAll">Collapse All</div>
        </div>
        <jsp:include page="/includes/project/project_myrole_reskin.jsp" />
        <jsp:include page="/includes/project/project_timeline_reskin.jsp" />
        <jsp:include page="/includes/project/project_phase_reskin.jsp" />
        <jsp:include page="/includes/project/project_detail_reskin.jsp" />
        <jsp:include page="/includes/project/project_prizes_reskin.jsp" />
        <jsp:include page="/includes/project/project_resource_reskin.jsp" />
        <!-- OR Project Linking Assembly -->
        <jsp:include page="/includes/project/project_link.jsp" />
    </div>
</div>

<div class="cta">
    <div class="cta__inner">
        <c:if test="${isAllowedToViewPayments}">
            <a href="ViewProjectPayments?pid=${project.id}"><or:text key='viewProjectDetails.btnPayments.alt' /></a>
        </c:if>
        <c:if test="${requestScope.isAllowedToManageProjects}">
            <a href="ViewManagementConsole?pid=${project.id}"><or:text key='viewProjectDetails.btnManagementConsoleLink.alt' /></a>
        </c:if>
        <c:if test="${isAllowedToEditProjects}">
            <a href="EditProjectLinks?pid=${project.id}"><or:text key='viewProjectDetails.btnEditLink.alt' /></a>
        </c:if>
        <c:if test="${isAllowedToEditProjects}">
            <a href="EditProject?pid=${project.id}"><or:text key='viewProjectDetails.btnEdit.alt' /></a>
        </c:if>
    </div>
</div>

<jsp:include page="/includes/inc_footer_reskin.jsp" />
<script type="text/javascript">
  window.onload = () => {
  var footerDetails = document.getElementById("footerNav").childNodes[0];
  footerDetails.children[0].remove();
  if (document.getElementById("footerNav")) {
    var footerHeight = document.getElementById("footerNav").clientHeight;
  }
  var cta = document.querySelector(".cta")
  if (footerHeight) {
    cta.style.bottom = (footerHeight) + 'px';
  } else {
    cta.style.bottom = '0';
  }
  footerDetails.children[0].addEventListener("click", function(){
    if (cta) {
        cta.classList.toggle("ctaOpen")
    }
  })
}
</script>
</body>
</html>
