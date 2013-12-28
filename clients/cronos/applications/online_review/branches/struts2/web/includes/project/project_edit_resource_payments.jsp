<%--
  - Author: flexme
  - Version: 1.0 (Online Review - Project Payments Integration Part 2 v1.0)
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page is used to render a table for a specific tab in edit project payments page.
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
<c:set var="project" value="${requestScope.project}"/>
<c:set var="contestPaymentTypeId" value="<%=Constants.CONTEST_PAYMENT_TYPE_ID%>" />
<c:set var="contestCheckpointPaymentTypeId" value="<%=Constants.CONTEST_CHECKPOINT_PAYMENT_TYPE_ID%>" />
<c:set var="reviewPaymentTypeId" value="<%=Constants.REVIEW_PAYMENT_TYPE_ID%>" />
<c:set var="copilotPaymentTypeId" value="<%=Constants.COPILOT_PAYMENT_TYPE_ID%>" />
<c:set var="prefix" value="${param.prefix}" />
<c:set var="tableId" value="${prefix}s" />
<fmt:setLocale value="en_US"/>

<table id="${tableId}-table" class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;<c:if test="${tabName ne tableId}">display:none;</c:if>">
<tr>
    <td class="title" colspan="4"><bean:message key="editProjectPayments.box.title"/></td>
</tr>
<tr>
    <td class="header"><bean:message key="editProjectPayments.box.title.Role"/></td>
    <td class="header"><bean:message key="editProjectPayments.box.title.Handle"/></td>
    <td class="header"><bean:message key="editProjectPayments.box.title.AutomaticManual"/></td>
    <td class="header" width="25%"></td>
</tr>
<tr style="display:none;" rel="false">
    <input type="hidden" value="0" rel="${prefix}Payments[0].paymentIds[0]"/>
    <td class="value" nowrap="nowrap">
        <c:if test="${tableId eq 'submitters'}">
            <select class="inputBox" rel="${prefix}Payments[0].paymentTypes[0]" onchange="paymentTypeChange(this)"></select>
        </c:if>
        <c:if test="${tableId eq 'reviewers'}">
            <select class="inputBox" rel="${prefix}Payments[0].paymentTypes[0]">
                <option value="${reviewPaymentTypeId}"><bean:message key="editProjectPayments.box.ReviewPayment" /></option>
            </select>
        </c:if>
        <c:if test="${tableId eq 'copilots'}">
            <select class="inputBox" rel="${prefix}Payments[0].paymentTypes[0]">
                <option value="${copilotPaymentTypeId}"><bean:message key="editProjectPayments.box.CopilotPayment" /></option>
            </select>
        </c:if>
    </td>
    <td class="value" nowrap="nowrap">
        <c:if test="${tableId eq 'submitters'}">
            <label><bean:message key="editProjectPayments.box.Submission" /></label>
            <select class="inputBox" rel="submitterPayments[0].submissionIds[0]"></select>
        </c:if>
    </td>
    <td class="value" nowrap="nowrap">
        <label>${"$"}</label>
        <input type="text" class="inputBoxDuration" value="" rel="${prefix}Payments[${vs.index}].amounts[0]"/>
    </td>
    <td class="valueC" nowrap="nowrap">
        <a href="#" onclick="return deletePayment(this);" rel="false"><html:img srcKey="btnDelete.img" altKey="btnDelete.alt" border="0" /></a>
    </td>
</tr>
<c:if test="${fn:length(resources) eq 0}">
    <tr class="dark">
        <td colspan="4" class="value"><bean:message key="editProjectPayments.box.NoResources"/></td>
    </tr>
</c:if>
<c:forEach items="${resources}" var="resource" varStatus="vs">
    <c:set var="resourceId" value="${resource.id}" />
    <c:set var="automatic" value="${resourcePayments[vs.index].automatic}" />
    <c:if test="${tableId eq 'submitters'}">
        <c:set var="hasContestSubmission" value="${fn:length(resourceContestSubmissions[resourceId]) gt 0}" />
        <c:set var="hasCheckpointSubmission" value="${fn:length(resourceCheckpointSubmissions[resourceId]) gt 0}" />
    </c:if>
    <tr class="dark resource-row" resourceId="${resourceId}">
        <td class="value" nowrap="nowrap">
            ${resourceRoles[resourceId]}
            <html:hidden property="${prefix}Payments[${vs.index}].resourceId" />
            <div class="error"><html:errors property="${prefix}Payments[${vs.index}].resourceId" prefix="" suffix="" /></div>
        </td>
        <td class="value" nowrap="nowrap"><tc-webtag:handle coderId="${resourceUserIds[resourceId]}" /></td>
        <td class="value" nowrap="nowrap">
            <html:radio styleId="${prefix}Payments[${vs.index}].automatic-true" property="${prefix}Payments[${vs.index}].automatic" value="${true}" onchange="changeAutomatic('${resourceId}', true)"/>
            <label for="${prefix}Payments[${vs.index}].automatic-true"><bean:message key="editProjectPayments.box.Automatic"/></label>
            <html:radio styleId="${prefix}Payments[${vs.index}].automatic-false" property="${prefix}Payments[${vs.index}].automatic" value="${false}" onchange="changeAutomatic('${resourceId}', false)"/>
            <label for="${prefix}Payments[${vs.index}].automatic-false"><bean:message key="editProjectPayments.box.Manual"/></label>
        </td>
        <td class="valueC" nowrap="nowrap">
            <a id="add_payment_${resourceId}" <c:if test="${automatic}">style="display: none;"</c:if> href="#" onclick="return addPayment(this, '${tableId}', '${vs.index}', '${resourceId}');"><html:img srcKey="editProjectPayments.box.AddPayment.img" altKey="editProjectPayments.box.AddPayment.alt" border="0" /></a>
        </td>
    </tr>
    <c:set var="paymentsCount" value="${fn:length(resourcePayments[vs.index].paymentIds)}" />
    <c:if test="${paymentsCount gt 0}">
        <c:forEach begin="0" end="${paymentsCount - 1}" var="paymentIdx">
            <c:set var="paymentId" value="${resourcePayments[vs.index].paymentIds[paymentIdx]}" />
            <c:set var="paid" value="${paymentsPaid[paymentId]}" />
            <tr class="light tr_payment_${resourceId}" rel="${paid}">
                <html:hidden property="${prefix}Payments[${vs.index}].paymentIds[${paymentIdx}]" />
                <td class="value" nowrap="nowrap">
                    <c:if test="${tableId eq 'submitters'}">
                        <html:select styleClass="inputBox" property="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]" disabled="${automatic or paid}" onchange="paymentTypeChange(this)">
                            <c:if test="${hasContestSubmission}">
                                <html:option value="${contestPaymentTypeId}" key="editProjectPayments.box.ContestPayment" />
                            </c:if>
                            <c:if test="${hasCheckpointSubmission}">
                                <html:option value="${contestCheckpointPaymentTypeId}" key="editProjectPayments.box.CheckpointPayment" />
                            </c:if>
                        </html:select>
                    </c:if>
                    <c:if test="${tableId eq 'reviewers'}">
                        <html:select styleClass="inputBox" property="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]" disabled="${automatic or paid}">
                            <html:option value="${reviewPaymentTypeId}" key="editProjectPayments.box.ReviewPayment" />
                        </html:select>
                    </c:if>
                    <c:if test="${tableId eq 'copilots'}">
                        <html:select styleClass="inputBox" property="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]" disabled="${automatic or paid}">
                            <html:option value="${copilotPaymentTypeId}" key="editProjectPayments.box.CopilotPayment" />
                        </html:select>
                    </c:if>
                    <div class="error"><html:errors property="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]" prefix="" suffix="" /></div>
                    <div class="error"><html:errors property="${prefix}Payments[${vs.index}].paymentIds[${paymentIdx}]" prefix="" suffix="" /></div>
                </td>
                <td class="value" nowrap="nowrap">
                    <c:if test="${tableId eq 'submitters'}">
                        <c:choose>
                            <c:when test="${paymentsForm.submitterPayments[vs.index].paymentTypes[paymentIdx] eq contestPaymentTypeId}">
                                <c:set var="candidateSubmissions" value="${resourceContestSubmissions[resourceId]}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="candidateSubmissions" value="${resourceCheckpointSubmissions[resourceId]}" />
                            </c:otherwise>
                        </c:choose>
                        <label><bean:message key="editProjectPayments.box.Submission" /></label>
                        <html:select styleClass="inputBox" property="submitterPayments[${vs.index}].submissionIds[${paymentIdx}]" disabled="${automatic or paid}">
                            <c:forEach items="${candidateSubmissions}" var="sub">
                                <html:option value="${sub}" key="${sub}"/>
                            </c:forEach>
                        </html:select>
                        <div class="error"><html:errors property="submitterPayments[${vs.index}].submissionIds[${paymentIdx}]" prefix="" suffix="" /></div>
                    </c:if>
                </td>
                <td class="value" nowrap="nowrap">
                    <label>${"$"}</label>
                    <html:text styleClass="inputBoxDuration" property="${prefix}Payments[${vs.index}].amounts[${paymentIdx}]" disabled="${automatic or paid}"/>
                    <div class="error"><html:errors property="${prefix}Payments[${vs.index}].amounts[${paymentIdx}]" prefix="" suffix="" /></div>
                </td>
                <td class="valueC" nowrap="nowrap">
                    <a <c:if test="${automatic or paid}">style="display: none;"</c:if> href="#" onclick="return deletePayment(this);" class="delete_payment_${resourceId}" rel="${paid}" resourceId="${resourceId}" resourceIdx="${vs.index}"><html:img srcKey="btnDelete.img" altKey="btnDelete.alt" border="0" /></a>
                </td>
            </tr>
        </c:forEach>
    </c:if>
</c:forEach>
</table>