<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page is used to render a table for a specific tab in edit project payments page.
--%>
<%@ page import="com.cronos.onlinereview.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
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

<table id="${tableId}-table" class="editProjectPaymentTable" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;<c:if test="${tabName ne tableId}">display:none;</c:if>">
<thead class="editProjectPaymentTable__header">
<tr style="display: none;">
    <td class="title" colspan="4"><or:text key="editProjectPayments.box.title"/></td>
</tr>
<tr>
    <th><or:text key="editProjectPayments.box.title.Role"/></th>
    <th><or:text key="editProjectPayments.box.title.Handle"/></th>
    <th><or:text key="editProjectPayments.box.title.AutomaticManual"/></th>
    <th width="25%"></th>
</tr>
</thead>
<tbody class="editProjectPaymentTable__body">
    <tr style="display:none;" rel="false">
        <input type="hidden" value="0" rel="${prefix}Payments[0].paymentIds[0]"/>
        <td class="value" nowrap="nowrap">
            <c:if test="${tableId eq 'submitters'}">
                <div class="selectCustom-add" style="width: 65%">
                    <select class="inputBox" rel="${prefix}Payments[0].paymentTypes[0]" onchange="paymentTypeChange(this)"></select>
                </div>
            </c:if>
            <c:if test="${tableId eq 'reviewers'}">
                <div class="selectCustom-add">
                    <select class="inputBox" rel="${prefix}Payments[0].paymentTypes[0]">
                        <option value="${reviewPaymentTypeId}"><or:text key="editProjectPayments.box.ReviewPayment" /></option>
                    </select>
                </div>
            </c:if>
            <c:if test="${tableId eq 'copilots'}">
                <div class="selectCustom-add">
                    <select class="inputBox" rel="${prefix}Payments[0].paymentTypes[0]">
                        <option value="${copilotPaymentTypeId}"><or:text key="editProjectPayments.box.CopilotPayment" /></option>
                    </select>
                </div>
            </c:if>
        </td>
        <td class="value" nowrap="nowrap">
            <c:if test="${tableId eq 'submitters'}">
                <div class="editPayment__submission">
                    <label><or:text key="editProjectPayments.box.Submission" /></label>
                    <div class="selectCustom-add">
                        <select class="inputBox" rel="submitterPayments[0].submissionIds[0]"></select>
                    </div>
                </div>
            </c:if>
        </td>
        <td style="width: 26%;" nowrap="nowrap">
            <label>${"$"}</label>
            <input type="text" class="edit__inputBoxDuration" value="" rel="${prefix}Payments[${vs.index}].amounts[0]"/>
        </td>
        <td class="valueC" nowrap="nowrap">
            <a href="#" onclick="return deletePayment(this);" rel="false"><or:text key='btnDelete.alt' /></a>
        </td>
    </tr>
    <c:if test="${fn:length(resources) eq 0}">
        <tr class="dark">
            <td colspan="4" class="value"><or:text key="editProjectPayments.box.NoResources"/></td>
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
            <td nowrap="nowrap">
                ${resourceRoles[resourceId]}
                <input type="hidden" name="${prefix}Payments[${vs.index}].resourceId"  value="<or:fieldvalue field='${prefix}Payments[${vs.index}].resourceId' />" />
                <div class="error"><s:fielderror escape="false"><s:param>${prefix}Payments[${vs.index}].resourceId</s:param></s:fielderror></div>
            </td>
            <td nowrap="nowrap"><tc-webtag:handle coderId="${resourceUserIds[resourceId]}" /></td>
            <td style="width: 26%;">
                <div class="custom-radio">
                    <input type="radio" id="${prefix}Payments[${vs.index}].automatic-true" name="${prefix}Payments[${vs.index}].automatic" value="${true}" onchange="changeAutomatic('${resourceId}', true)" <or:checked name='${prefix}Payments[${vs.index}].automatic' value='${true}' />/>
                    <label for="${prefix}Payments[${vs.index}].automatic-true"><or:text key="editProjectPayments.box.Automatic"/></label>
                </div>
                <div class="custom-radio">
                    <input type="radio" id="${prefix}Payments[${vs.index}].automatic-false" name="${prefix}Payments[${vs.index}].automatic" value="${false}" onchange="changeAutomatic('${resourceId}', false)" <or:checked name='${prefix}Payments[${vs.index}].automatic' value='${false}' />/>
                    <label for="${prefix}Payments[${vs.index}].automatic-false"><or:text key="editProjectPayments.box.Manual"/></label>
                </div>
            </td>
            <td nowrap="nowrap">
                <a id="add_payment_${resourceId}" class="addPayment" <c:if test="${automatic}">style="display: none;"</c:if> href="#" onclick="return addPayment(this, '${tableId}', '${vs.index}', '${resourceId}');"><or:text key='editProjectPayments.box.AddPayment.alt' /></a>
            </td>
        </tr>
        <c:set var="paymentsCount" value="${fn:length(resourcePayments[vs.index].paymentIds)}" />
        <c:if test="${paymentsCount gt 0}">
            <c:forEach begin="0" end="${paymentsCount - 1}" var="paymentIdx">
                <c:set var="paymentId" value="${resourcePayments[vs.index].paymentIds[paymentIdx]}" />
                <c:set var="paid" value="${paymentsPaid[paymentId]}" />
                <tr class="light tr_payment_${resourceId} newTr" rel="${paid}">
                    <input type="hidden" name="${prefix}Payments[${vs.index}].paymentIds[${paymentIdx}]"  value="<or:fieldvalue field='${prefix}Payments[${vs.index}].paymentIds[${paymentIdx}]' />" />
                    <td class="value" nowrap="nowrap">
                        <c:if test="${tableId eq 'submitters'}">
                            <div class="selectCustom" style="width: 65%;">
                                <select class="inputBox" name="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]" <c:if test="${automatic or paid}">disabled="disabled" </c:if> onchange="paymentTypeChange(this)"><c:set var="OR_FIELD_TO_SELECT" value="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]"/>
                                    <c:if test="${hasContestSubmission}">
                                        <option  value="${contestPaymentTypeId}" <or:selected value="${contestPaymentTypeId}"/>><or:text key="editProjectPayments.box.ContestPayment" def="${contestPaymentTypeId}" /></option>
                                    </c:if>
                                    <c:if test="${hasCheckpointSubmission}">
                                        <option  value="${contestCheckpointPaymentTypeId}" <or:selected value="${contestCheckpointPaymentTypeId}"/>><or:text key="editProjectPayments.box.CheckpointPayment" def="${contestCheckpointPaymentTypeId}" /></option>
                                    </c:if>
                                </select>
                            </div>
                        </c:if>
                        <c:if test="${tableId eq 'reviewers'}">
                            <div class="selectCustom">
                                <select class="inputBox" name="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]" <c:if test="${automatic or paid}">disabled="disabled" </c:if>><c:set var="OR_FIELD_TO_SELECT" value="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]"/>
                                    <option  value="${reviewPaymentTypeId}" <or:selected value="${reviewPaymentTypeId}"/>><or:text key="editProjectPayments.box.ReviewPayment" def="${reviewPaymentTypeId}" /></option>
                                </select>
                            </div>
                        </c:if>
                        <c:if test="${tableId eq 'copilots'}">
                            <div class="selectCustom">
                                <select class="inputBox" name="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]" <c:if test="${automatic or paid}">disabled="disabled" </c:if>><c:set var="OR_FIELD_TO_SELECT" value="${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]"/>
                                    <option value="${copilotPaymentTypeId}" <or:selected value="${copilotPaymentTypeId}"/>><or:text key="editProjectPayments.box.CopilotPayment" def="${copilotPaymentTypeId}" /></option>
                                </select>
                            </div>
                        </c:if>
                        <div class="error"><s:fielderror escape="false"><s:param>${prefix}Payments[${vs.index}].paymentTypes[${paymentIdx}]</s:param></s:fielderror></div>
                        <div class="error"><s:fielderror escape="false"><s:param>${prefix}Payments[${vs.index}].paymentIds[${paymentIdx}]</s:param></s:fielderror></div>
                    </td>
                    <td class="value" nowrap="nowrap">
                        <c:if test="${tableId eq 'submitters'}">
                            <c:choose>
                                <c:when test="${submitterPayments[vs.index].paymentTypes[paymentIdx] eq contestPaymentTypeId}">
                                    <c:set var="candidateSubmissions" value="${resourceContestSubmissions[resourceId]}" />
                                </c:when>
                                <c:otherwise>
                                    <c:set var="candidateSubmissions" value="${resourceCheckpointSubmissions[resourceId]}" />
                                </c:otherwise>
                            </c:choose>
                            <div class="editPayment__submission">
                            <label><or:text key="editProjectPayments.box.Submission" /></label>
                                <div class="selectCustom">
                                    <select class="inputBox" name="submitterPayments[${vs.index}].submissionIds[${paymentIdx}]" <c:if test="${automatic or paid}">disabled="disabled" </c:if>><c:set var="OR_FIELD_TO_SELECT" value="submitterPayments[${vs.index}].submissionIds[${paymentIdx}]"/>
                                        <c:forEach items="${candidateSubmissions}" var="sub">
                                            <option  value="${sub}" <or:selected value="${sub}"/>>${sub}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="error"><s:fielderror escape="false"><s:param>submitterPayments[${vs.index}].submissionIds[${paymentIdx}]</s:param></s:fielderror></div>
                        </c:if>
                    </td>
                    <td style="width: 26%;" nowrap="nowrap">
                        <label>${"$"}</label>
                        <input type="text" class="edit__inputBoxDuration" name="${prefix}Payments[${vs.index}].amounts[${paymentIdx}]" <c:if test="${automatic or paid}">disabled="disabled" </c:if> value="<or:fieldvalue field='${prefix}Payments[${vs.index}].amounts[${paymentIdx}]' />" />
                        <div class="error"><s:fielderror escape="false"><s:param>${prefix}Payments[${vs.index}].amounts[${paymentIdx}]</s:param></s:fielderror></div>
                    </td>
                    <td class="valueC" nowrap="nowrap">
                        <a <c:if test="${automatic or paid}">style="display: none;"</c:if> href="#" onclick="return deletePayment(this);" class="delete_payment_${resourceId} deletePayment" rel="${paid}" resourceId="${resourceId}" resourceIdx="${vs.index}"><or:text key='btnDelete.alt' /></a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </c:forEach>
</tbody>
</table>