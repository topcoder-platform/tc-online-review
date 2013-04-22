<%--
  - Author: flexme
  - Version: 1.0 (Online Review - Project Payments Integration Part 2 v1.0)
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays the project payments.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="project" value="${requestScope.project}"/>
<fmt:setLocale value="en_US"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">
    <head>
        <jsp:include page="/includes/project/project_title.jsp"/>
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
                <div class="clearfix"></div>

                <div id="titlecontainer">
                    <div id="contentTitle">
                        <h3>${project.allProperties["Project Name"]}
                            version ${project.allProperties["Project Version"]} - Payments</h3>
                    </div>
                </div>

                <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                    <tr>
                        <td colspan="8" class="title"><bean:message key="projectPayments.box.title" /></td>
                    </tr>
                    <tr>
                        <td class="header"><bean:message key="projectPayments.box.title.PaymentID" /></td>
                        <td class="header"><bean:message key="projectPayments.box.title.Role" /></td>
                        <td class="header"><bean:message key="projectPayments.box.title.Handle" /></td>
                        <td class="header"><bean:message key="projectPayments.box.title.PaymentType" /></td>
                        <td class="header"><bean:message key="projectPayments.box.title.SubmissionID" /></td>
                        <td class="header"><bean:message key="projectPayments.box.title.Amount" /></td>
                        <td class="header"><bean:message key="projectPayments.box.title.Paid" /></td>
                        <td class="header" style="text-align: center;"><bean:message key="projectPayments.box.title.CreationDate" /></td>
                    </tr>
                    <c:if test="${fn:length(payments) eq 0}">
                        <tr class="light">
                            <td class="value" colspan="8"><bean:message key="projectPayments.box.NoPayments" /></td>
                        </tr>
                    </c:if>
                    <c:forEach items="${payments}" var="payment" varStatus="vs">
                        <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
                            <td class="value">${payment.projectPaymentId}</td>
                            <c:set var="resourceId" value="${payment.resourceId}" />
                            <td class="value" nowrap="nowrap">${resourceRoles[resourceId]}</td>
                            <td class="value" nowrap="nowrap"><tc-webtag:handle coderId="${resourceUserIds[resourceId]}" /></td>
                            <td class="value" nowrap="nowrap">${payment.projectPaymentType.name}</td>
                            <td class="value" nowrap="nowrap">${payment.submissionId}</td>
                            <td class="value" nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, payment.amount)}</td>
                            <td class="value" nowrap="nowrap">
                                <c:choose>
                                    <c:when test="${empty payment.pactsPaymentId}"><bean:message key="projectPayments.box.Paid.No"/></c:when>
                                    <c:otherwise>
                                        <bean:message key="projectPayments.box.Paid.Yes"/> (<a href="${pactsPaymentDetailBaseURL}${payment.pactsPaymentId}"><bean:message key="projectPayments.box.Paid.View"/></a>)
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="valueC" nowrap="nowrap"><fmt:formatDate pattern="MM.dd.yyyy HH:mm z" value="${payment.createDate}"/></td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td class="lastRowTD" colspan="8"><!-- @ --></td>
                    </tr>
                </table>
                <br/>

                <div align="right">
                    <c:if test="${isAllowedToEditPayments}">
                        <a href="EditProjectPayments.do?method=editProjectPayments&pid=${project.id}"><html:img srcKey="projectPayments.btnEditPayments.img" border="0" altKey="projectPayments.btnEditPayments.alt" /></a>&#160;
                    </c:if>
                    <a href="ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img srcKey="btnReturnToProjDet.img" border="0" altKey="btnReturnToProjDet.alt" /></a>
                </div>
            </div>

            <jsp:include page="/includes/inc_footer.jsp" />
        </div>

    </div>

    </body>
</html:html>
