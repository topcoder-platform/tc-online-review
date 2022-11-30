<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page is used to edit the project payments.
--%>
<%@ page import="com.cronos.onlinereview.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="contestPaymentTypeId" value="<%=Constants.CONTEST_PAYMENT_TYPE_ID%>" />
<c:set var="contestCheckpointPaymentTypeId" value="<%=Constants.CONTEST_CHECKPOINT_PAYMENT_TYPE_ID%>" />
<c:set var="reviewPaymentTypeId" value="<%=Constants.REVIEW_PAYMENT_TYPE_ID%>" />
<c:set var="copilotPaymentTypeId" value="<%=Constants.COPILOT_PAYMENT_TYPE_ID%>" />
<c:set var="project" value="${requestScope.project}"/>
<fmt:setLocale value="en_US"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <jsp:include page="/includes/project/project_title.jsp"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

        <!-- TopCoder CSS -->
        <link type="text/css" rel="stylesheet" href="/css/style.css" />
        <link type="text/css" rel="stylesheet" href="/css/coders.css" />
        <link type="text/css" rel="stylesheet" href="/css/stats.css" />

        <!-- Reskin -->
        <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
        <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">

        <!-- CSS and JS by Petar -->
        <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
        <link type="text/css" rel="stylesheet" href="/css/or/phasetabs.css" />
        <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript" src="/js/or/edit_project_payments.js"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"><!--
            // The contest submissions of resources
            var contestSubmissions = [];
            // The contest checkpoint submissions of resources
            var checkpointSubmissions = [];
            <c:forEach items="${resourceContestSubmissions}" var="item">
                contestSubmissions[${item.key}] = [];
                <c:forEach items="${item.value}" var="subId">
                    contestSubmissions[${item.key}].push(${subId});
                </c:forEach>
            </c:forEach>
            <c:forEach items="${resourceCheckpointSubmissions}" var="item">
                checkpointSubmissions[${item.key}] = [];
                <c:forEach items="${item.value}" var="subId">
                    checkpointSubmissions[${item.key}].push(${subId});
                </c:forEach>
            </c:forEach>
            var contestPaymentTypeId = ${contestPaymentTypeId};
            var contestCheckpointPaymentTypeId = ${contestCheckpointPaymentTypeId};
            var contestPaymentTypeText = '<or:text key="editProjectPayments.box.ContestPayment" />';
            var checkpointPaymentTypeText = '<or:text key="editProjectPayments.box.CheckpointPayment" />';
        //--></script>
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

                let selects = document.getElementsByClassName("selectCustom");
                customSelect(selects);

                let avatar = document.querySelector('.webHeader__avatar a');
                let avatarImage = document.createElement('div');
                avatarImage.className = "webHeader__avatarImage";
                let twoChar = avatar.text.substring(0, 2);
                avatarImage.innerText = twoChar;
                avatar.innerHTML = avatarImage.outerHTML;
            });
        </script>
    </head>

    <body onload="bodyLoad()">
    <jsp:include page="/includes/inc_header_reskin.jsp" />

    <jsp:include page="/includes/project/project_tabs_reskin.jsp" />

    <div class="content content">
        <div class="content__inner">
            <div class="editProjectLink__header">
                <button type="button" class="back-btn edit-back-btn" onclick="history.back()">
                    <i class="arrow-prev-icon"></i>
                </button>
                <jsp:include page="/includes/project/project_info_reskin.jsp" />
            </div>
            <div class="projectDetails">
                <div class="projectDetails__sectionHeader">
                    <div class="projectDetails__title">
                       <or:text key="editProjectPayments.box.title"/>
                    </div>
                </div>

                <div class="projectDetails__sectionBody">
                    <div>
                        <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                            <table class="editProjectLink__error" cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td colspan="2"><span style="color:red;"><or:text key="editProjectPayments.ValidationFailed" /></span></td>
                                </tr>
                                <s:actionerror escape="false" />
                            </table>
                        </c:if>

                        <c:if test="${empty tabName}">
                            <c:set var="tabName" value="submitters" scope="request" />
                        </c:if>

                        <!-- The tabs -->
                        <div class="projectDetails__tabs">
                            <div ${tabName eq 'submitters' ? "class='projectDetails__tab projectDetails__tab--active'" : "class='projectDetails__tab'"}>
                                <a href="javascript:void(0)" onclick="return showTab('submitters-table', this);">
                                    <or:text key="editProjectPayments.Submitters"/>
                                </a>
                            </div>
                            <div ${tabName eq 'reviewers' ? "class='projectDetails__tab projectDetails__tab--active'" : "class='projectDetails__tab'"}>
                                <a href="javascript:void(0)" onclick="return showTab('reviewers-table', this);">
                                    <or:text key="editProjectPayments.Reviewers"/>
                                </a>
                            </div>
                            <div ${tabName eq 'copilots' ? "class='projectDetails__tab projectDetails__tab--active'" : "class='projectDetails__tab'"}>
                                <a href="javascript:void(0)" onclick="return showTab('copilots-table', this);">
                                    <or:text key="editProjectPayments.Copilots"/>
                                </a>
                            </div>
                        </div>

                        <s:form action="SaveProjectPayments" onsubmit="return enableFormElements(this);" namespace="/actions">
                            <input type="hidden" name="pid" value="<or:fieldvalue field='pid' />" />

                            <!-- The Submitters table -->
                            <c:set var="resources" value="${submitters}" scope="request" />
                            <c:set var="resourcePayments" value="${submitterPayments}" scope="request" />
                            <jsp:include page="/includes/project/project_edit_resource_payments.jsp">
                                <jsp:param name="prefix" value="submitter" />
                            </jsp:include>

                            <!-- The Reviewers table -->
                            <c:set var="resources" value="${reviewers}" scope="request" />
                            <c:set var="resourcePayments" value="${reviewerPayments}" scope="request" />
                            <jsp:include page="/includes/project/project_edit_resource_payments.jsp">
                                <jsp:param name="prefix" value="reviewer" />
                            </jsp:include>

                            <!-- The Copilots table -->
                            <c:set var="resources" value="${copilots}" scope="request" />
                            <c:set var="resourcePayments" value="${copilotPayments}" scope="request" />
                            <jsp:include page="/includes/project/project_edit_resource_payments.jsp">
                                <jsp:param name="prefix" value="copilot" />
                            </jsp:include>
                        </s:form>
                    </div>
                </div>
            </div>
            <div class="saveChanges__button">
                <button id="saveChanges" form="SaveProjectPayments" value="Submit" class="saveChanges__save"><or:text key='btnSaveChanges.alt' /></button>
                <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />" class="saveChanges__cancel"><or:text key='btnCancel.alt' /></a>
            </div>
        </div>
    </div>
    <jsp:include page="/includes/inc_footer_reskin.jsp" />

    </body>
    <script type="text/javascript">
        const paymentsForm = document.getElementById('SaveProjectPayments');
        paymentsForm.addEventListener('submit', function() {
            document.getElementById("saveChanges").disabled = true;
        }, false);
    </script>
</html>
