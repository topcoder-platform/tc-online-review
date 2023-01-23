<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays the project payments.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="project" value="${requestScope.project}"/>
<fmt:setLocale value="en_US"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <jsp:include page="/includes/project/project_title.jsp"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

        <!-- Reskin -->
        <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
        <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">

        <!-- TopCoder CSS -->
        <link type="text/css" rel="stylesheet" href="/css/style.css" />
        <link type="text/css" rel="stylesheet" href="/css/coders.css" />

        <!-- CSS and JS by Petar -->
        <script language="JavaScript" type="text/javascript"
                src="/js/or/rollovers2.js"><!-- @ --></script>
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

        <div class="content content--projectPayment">
            <div class="content__inner">
                <div class="viewProjectPayment__header">
                    <a type="button" class="back-btn edit-back-btn" href="ViewProjectDetails?pid=${project.id}">
                        <i class="arrow-prev-icon"></i>
                    </a>

                    <jsp:include page="/includes/project/project_info_reskin.jsp" />
                </div>
                <div class="divider"></div>
                <div class="viewProjectPayment">
                    <div class="viewProjectPayment__sectionHeader">
                        <div class="viewProjectPayment__title">
                            project payments
                        </div>
                    </div>
                    <div class="projectDetails__sectionBody">
                        <div id="mainMiddleContent">
                            <table class="projectPaymentTable" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                                <thead class="projectPaymentTable__header">
                                    <tr>
                                        <th><or:text key="projectPayments.box.title.PaymentID" /></th>
                                        <th><or:text key="projectPayments.box.title.Role" /></th>
                                        <th><or:text key="projectPayments.box.title.Handle" /></th>
                                        <th><or:text key="projectPayments.box.title.PaymentType" /></th>
                                        <th><or:text key="projectPayments.box.title.SubmissionID" /></th>
                                        <th><or:text key="projectPayments.box.title.Amount" /></th>
                                        <th><or:text key="projectPayments.box.title.Paid" /></th>
                                        <th><or:text key="projectPayments.box.title.CreationDate" /></th>
                                    </tr>
                                </thead>
                                <tbody class="projectPaymentTable__body">
                                    <c:if test="${fn:length(payments) eq 0}">
                                        <tr class="light">
                                            <td nowrap="nowrap" colspan="9"><or:text key="projectPayments.box.NoPayments" /></td>
                                        </tr>
                                    </c:if>
                                    <c:forEach items="${payments}" var="payment" varStatus="vs">
                                        <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
                                            <td class="value">${payment.projectPaymentId}</td>
                                            <c:set var="resourceId" value="${payment.resourceId}" />
                                            <td nowrap="nowrap">${resourceRoles[resourceId]}</td>
                                            <td nowrap="nowrap"><tc-webtag:handle coderId="${resourceUserIds[resourceId]}" /></td>
                                            <td nowrap="nowrap">${payment.projectPaymentType.name}</td>
                                            <td nowrap="nowrap">
                                                <c:choose>
                                                    <c:when test="${not empty payment.submissionId}">
                                                        ${payment.submissionId}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <or:text key="NotAvailable"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, payment.amount)}</td>
                                            <td nowrap="nowrap">
                                                <c:choose>
                                                    <c:when test="${empty payment.pactsPaymentId}"><or:text key="projectPayments.box.Paid.No"/></c:when>
                                                    <c:otherwise>
                                                        <or:text key="projectPayments.box.Paid.Yes"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td width="14%" nowrap="nowrap"><fmt:formatDate pattern="MM.dd.yyyy HH:mm z" value="${payment.createDate}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div align="right" style="margin-top: 56px;">
                    <c:if test="${isAllowedToEditPayments}">
                        <a class="editPaymentBtn" href="EditProjectPayments?pid=${project.id}"><or:text key='projectPayments.btnEditPayments.alt' /></a>
                    </c:if>
                    <a class="returnBtn" href="ViewProjectDetails?pid=${project.id}"><or:text key='btnReturnToProjDet.alt' /></a>
                </div>
            </div>
        </div>
        <jsp:include page="/includes/inc_footer_reskin.jsp" />
    </body>
</html>
