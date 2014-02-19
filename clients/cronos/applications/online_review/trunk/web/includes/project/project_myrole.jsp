<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment renders the details on roles assigned to current user in context of
  - selected project.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
    <table class="stat" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;"
           id="myRolesTable">
        <tr>
            <td class="title"><or:text key="viewProjectDetails.box.MyInfo" /></td>
            <c:if test="${empty myDeliverables}">
                <td class="title"><!-- @ --></td>
            </c:if>
            <c:if test="${not empty myDeliverables}">
                <td class="title"><or:text key="viewProjectDetails.box.MyDeliverabes" /></td>
            </c:if>
            <td class="title"><or:text key="viewProjectDetails.box.OutstandingDeliverabes" /></td>
        </tr>
        <tr class="light">
            <td class="value" width="36%" align="left">
                <table style="cellpadding:1">
                    <tr>
                        <th class="value roleHeader" width="30%"><or:text key="viewProjectDetails.Role"/></th>
                        <c:if test="${isAllowedToViewPayment}">
                            <th class="value roleHeader" width="30%"><or:text key="viewProjectDetails.Payment"/></th>
                            <th class="value roleHeader" width="40%"><or:text key="viewProjectDetails.PaymentStatus"/></th>
                        </c:if>
                    </tr>
                    <c:forEach items="${requestScope.myPayment}" var="rolePayment">
                        <c:set var="roleName" value="${rolePayment.key.name}"/>
                        <c:set var="paymentAmount" value="${rolePayment.value}"/>
                        <c:set var="paid" value="${requestScope.wasPaid[rolePayment.key]}"/>
                        <tr>
                            <td class="value" nowrap="nowrap">
                                <c:out value="${roleName}"/>
                            </td>
                            <c:if test="${isAllowedToViewPayment}">
                                <td class="value">
                                    <c:choose>
                                        <c:when test="${paymentAmount eq null}">
                                            <or:text key="NotAvailable" />
                                        </c:when>
                                        <c:otherwise>
                                            ${"$"}${orfn:displayPaymentAmt(pageContext.request, paymentAmount)}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="value">
                                    <c:choose>
                                        <c:when test="${not empty paymentAmount}">
                                            <c:if test="${paid}">
                                                <or:text key="viewProjectDetails.Paid.yes"/>
                                            </c:if>
                                            <c:if test="${not paid}">
                                                <or:text key="viewProjectDetails.Paid.no"/>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise><or:text key="NotAvailable" /></c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    <c:if test="${isAllowedToViewPayment or ((myDelay ne null) and (myDelay>0))}">
                        <tr><td class="value" colspan=3><hr/></td></tr>
                    </c:if>
                    <c:if test="${isAllowedToViewPayment}">
                        <tr>
                            <th class="value roleHeader" nowrap="nowrap"><or:text key="viewProjectDetails.TotalPayment"/></th>
                            <td class="value" colspan=2>
                                ${"$"}${orfn:displayPaymentAmt(pageContext.request, requestScope.totalPayment)}
                                <c:if test="${(paymentPenaltyPercentage ne null) and (paymentPenaltyPercentage>0) and potentialPenalty}">
                                    (potentially -${paymentPenaltyPercentage}% due to the delay)
                                </c:if>
                                <c:if test="${(paymentPenaltyPercentage ne null) and (paymentPenaltyPercentage>0) and !potentialPenalty}">
                                    (-${paymentPenaltyPercentage}% due to the delay)
                                </c:if>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${(myDelay ne null) and (myDelay>0)}">
                        <tr>
                            <th class="value roleHeader" nowrap="nowrap"><or:text key="viewProjectDetails.TotalDelay"/></th>
                            <td class="value" colspan=2><c:out value="${orfn:displayDelay(myDelay)}"/></td>
                        </tr>
                    </c:if>
                </table>
            </td>
            <c:if test="${empty myDeliverables}">
                <td class="myRoleValues" width="27%" align="left" nowrap="nowrap"><!-- @ --></td>
            </c:if>
            <c:if test="${not empty myDeliverables}">
                <td class="myRoleValues" width="27%" align="left" nowrap="nowrap">
                    <c:forEach items="${myDeliverables}" var="deliverable" varStatus="deliverableStatus">
                        <c:set var="devrStatus" value="${myDeliverableStatuses[deliverableStatus.index]}" />
                        <c:choose>
                            <c:when test="${deliverable.complete}">
                                <img src="/i/or/icon_authorization.gif" alt="<or:text key='global.Completed' />" class="Outline" />
                            </c:when>
                            <c:when test="${devrStatus == 1}">
                                <img src="/i/or/icon_notification.gif" alt="<or:text key='global.DeadlineNear' />" class="Outline" />
                            </c:when>
                            <c:when test="${devrStatus == 2}">
                                <img src="/i/or/icon_warning.gif" alt="<or:text key='global.Late' />" class="Outline" />
                            </c:when>
                        </c:choose>
                        ${orfn:displayDate(pageContext.request, myDeliverableDates[deliverableStatus.index])}
                        <c:if test="${empty myDeliverableLinks[deliverableStatus.index]}">
                            <b><or:text key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b><c:if
                                test="${deliverableStatus.index != fn:length(myDeliverables) - 1}"><br />
                            </c:if>
                        </c:if>
                        <c:if test="${not empty myDeliverableLinks[deliverableStatus.index]}">

                            <c:if test="${fn:startsWith(myDeliverableLinks[deliverableStatus.index],'http') == true}">
                                  <a href="${myDeliverableLinks[deliverableStatus.index]}"><b><or:text
                                     key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b></a><c:if
                                     test="${deliverableStatus.index != fn:length(myDeliverables) - 1}"><br />
                                  </c:if>
                            </c:if>
                            <c:if test="${fn:startsWith(myDeliverableLinks[deliverableStatus.index],'http') == false}">
                                  <a href="<or:url value='/actions/${myDeliverableLinks[deliverableStatus.index]}' />"><b><or:text
                                     key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b></a><c:if
                                     test="${deliverableStatus.index != fn:length(myDeliverables) - 1}"><br />
                                  </c:if>
                            </c:if>
                        </c:if>
                    </c:forEach>
                </td>
            </c:if>

            <td class="myRoleValues" width="37%" align="left" nowrap="nowrap">
                <c:if test="${not empty outstandingDeliverables}">
                    <c:forEach items="${outstandingDeliverables}" var="deliverable" varStatus="deliverableStatus">
                        <c:if test="${deliverable.id != 1}">
                            <c:set var="devrStatus" value="${outstandingDeliverableStatuses[deliverableStatus.index]}" />
                            <c:choose>
                                <c:when test="${deliverable.complete}">
                                    <img src="/i/or/icon_authorization.gif" alt="<or:text key='global.Completed' />" class="Outline" />
                                </c:when>
                                <c:when test="${devrStatus == 1}">
                                    <img src="/i/or/icon_notification.gif" alt="<or:text key='global.DeadlineNear' />" class="Outline" />
                                </c:when>
                                <c:when test="${devrStatus == 2}">
                                    <img src="/i/or/icon_warning.gif" alt="<or:text key='global.Late' />" class="Outline" />
                                </c:when>
                            </c:choose>
                            ${orfn:displayDate(pageContext.request, outstandingDeliverableDates[deliverableStatus.index])}
                            <c:if test="${not empty outstandingDeliverableUserIds[deliverableStatus.index]}">
                                <tc-webtag:handle coderId="${outstandingDeliverableUserIds[deliverableStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" /><b>:</b>
                            </c:if>
                            <b><or:text key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b>
                            <c:if test="${not empty deliverable.submission}">
                                ${deliverable.submission}
                                <c:if test="${(isManager) && (not empty outstandingDeliverableSubmissionUserIds[deliverableStatus.index])}">
                                    (<tc-webtag:handle coderId="${outstandingDeliverableSubmissionUserIds[deliverableStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" />)
                            </c:if>
                            </c:if><br />
                        </c:if>
                    </c:forEach>
                    <br/>
                </c:if>

                <c:if test="${(empty outstandingDeliverables) and (not unrespondedLateDeliverables)}">
                    <or:text key="viewProjectDetails.NoOutstandingDeliverables" /><br/><br/>
                </c:if>

                <c:if test="${unrespondedLateDeliverables}">
                There are unresponded late deliverables! See the list <a href="<or:url value='/actions/${unrespondedLateDeliverablesLink}' />">here</a>.
                </c:if>
            </td>
        </tr>
        <tr>
            <td class="lastRowTD" colspan="3"><!-- @ --></td>
        </tr>
    </table><br />
