<%--
  - Author: flexme
  - Version: 1.0 (Online Review - Project Payments Integration Part 2 v1.0)
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page is used to edit the project payments.
--%>
<%@ page import="com.cronos.onlinereview.actions.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="contestPaymentTypeId" value="<%=Constants.CONTEST_PAYMENT_TYPE_ID%>" />
<c:set var="contestCheckpointPaymentTypeId" value="<%=Constants.CONTEST_CHECKPOINT_PAYMENT_TYPE_ID%>" />
<c:set var="reviewPaymentTypeId" value="<%=Constants.REVIEW_PAYMENT_TYPE_ID%>" />
<c:set var="copilotPaymentTypeId" value="<%=Constants.COPILOT_PAYMENT_TYPE_ID%>" />
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
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/phasetabs.css' />" />
        <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/dojo.js' />"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/util.js' />"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/edit_project_payments.js' />"><!-- @ --></script>
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
            var contestPaymentTypeText = '<bean:message key="editProjectPayments.box.ContestPayment" />';
            var checkpointPaymentTypeText = '<bean:message key="editProjectPayments.box.CheckpointPayment" />';
        //--></script>
    </head>

    <body onload="bodyLoad()">
    <div align="center">

        <div class="maxWidthBody" align="left">

            <jsp:include page="/includes/inc_header.jsp" />

            <jsp:include page="/includes/project/project_tabs.jsp" />

            <div id="mainMiddleContent">
                <div class="clearfix"></div>

                <div id="titlecontainer">
                    <div id="contentTitle">
                        <h3>${project.allProperties["Project Name"]}
                            version ${project.allProperties["Project Version"]} - Edit Payments</h3>
                    </div>
                </div>

                <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr><td><!-- @ --></td><td width="400"><!-- @ --></td></tr>
                        <tr>
                            <td colspan="2"><span style="color:red;"><bean:message key="editProjectPayments.ValidationFailed" /></span></td>
                        </tr>
                        <html:errors property="org.apache.struts.action.GLOBAL_MESSAGE" />
                    </table>
                </c:if>

                <c:if test="${empty tabName}">
                    <c:set var="tabName" value="submitters" scope="request" />
                </c:if>

                <!-- The tabs -->
                <div>
                    <ul id="tablist">
                        <li <c:if test="${tabName eq 'submitters'}">class="current"</c:if> ><a id="submitters-link" onclick="return showTab('submitters-table', this);" href="javascript:void(0)"><bean:message key="editProjectPayments.Submitters"/></a></li>
                        <li <c:if test="${tabName eq 'reviewers'}">class="current"</c:if>><a id="reviewers-link" onclick="return showTab('reviewers-table', this);" href="javascript:void(0)"><bean:message key="editProjectPayments.Reviewers"/></a></li>
                        <li <c:if test="${tabName eq 'copilots'}">class="current"</c:if>><a id="copilots-link" onclick="return showTab('copilots-table', this);" href="javascript:void(0)"><bean:message key="editProjectPayments.Copilots"/></a></li>
                    </ul>
                    <div style="clear:both;"></div>
                </div>

                <html:form action="/actions/SaveProjectPayments" onsubmit="return enableFormElements(this);">
                    <html:hidden property="pid"/>
                    <html:hidden property="method" value="saveProjectPayments" />

                    <!-- The Submitters table -->
                    <c:set var="resources" value="${submitters}" scope="request" />
                    <c:set var="resourcePayments" value="${paymentsForm.submitterPayments}" scope="request" />
                    <jsp:include page="/includes/project/project_edit_resource_payments.jsp">
                        <jsp:param name="prefix" value="submitter" />
                    </jsp:include>

                    <!-- The Reviewers table -->
                    <c:set var="resources" value="${reviewers}" scope="request" />
                    <c:set var="resourcePayments" value="${paymentsForm.reviewerPayments}" scope="request" />
                    <jsp:include page="/includes/project/project_edit_resource_payments.jsp">
                        <jsp:param name="prefix" value="reviewer" />
                    </jsp:include>

                    <!-- The Copilots table -->
                    <c:set var="resources" value="${copilots}" scope="request" />
                    <c:set var="resourcePayments" value="${paymentsForm.copilotPayments}" scope="request" />
                    <jsp:include page="/includes/project/project_edit_resource_payments.jsp">
                        <jsp:param name="prefix" value="copilot" />
                    </jsp:include>

                    <br/>
                    <div align="right">
                        <html:image srcKey="btnSaveChanges.img" border="0" altKey="btnSaveChanges.alt" />&#160;
                        <a href="ViewProjectPayments.do?method=viewProjectPayments&pid=${project.id}"><html:img srcKey="btnCancel.img" border="0" altKey="btnCancel.alt" /></a>
                    </div>
                </html:form>
            </div>

            <jsp:include page="/includes/inc_footer.jsp" />
        </div>

    </div>

    </body>
</html:html>
