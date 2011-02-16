<%--
  - Author: George1, real_vg, isv
  - Version: 1.1
  - Copyright (C) 2005 - 2011 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment renders the details on roles assigned to current user in context of
  - selected project.
  -
  - Version 1.1 (Impersonation Login Release assembly) changes: Updated My Roles section to render details on payments
  - and payment statuses per roles assigned to user.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
	<table class="stat" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;"
           id="myRolesTable">
		<tr>
			<td class="title"><bean:message key="viewProjectDetails.box.MyRoleAndPayments" /></td>
			<c:if test="${empty myDeliverables}">
				<td class="title"><!-- @ --></td>
			</c:if>
			<c:if test="${not empty myDeliverables}">
				<td class="title"><bean:message key="viewProjectDetails.box.MyDeliverabes" /></td>
			</c:if>
			<td class="title"><bean:message key="viewProjectDetails.box.OutstandingDeliverabes" /></td>
		</tr>
		<tr class="light">
			<td class="value" width="33%" align="left">
                <table style="cellpadding:1">
                    <tr>
                        <th class="value roleHeader" width="30%"><bean:message key="viewProjectDetails.Role"/></th>
                        <c:if test="${isAllowedToViewPayment}">
                            <th class="value roleHeader" width="30%"><bean:message key="viewProjectDetails.Payment"/></th>
                            <th class="value roleHeader" width="40%"><bean:message key="viewProjectDetails.PaymentStatus"/></th>
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
                                            <bean:message key="NotAvailable" />
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
                                                <bean:message key="viewProjectDetails.Paid.yes"/>
                                            </c:if>
                                            <c:if test="${not paid}">
                                                <bean:message key="viewProjectDetails.Paid.no"/>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise><bean:message key="NotAvailable" /></c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    <c:if test="${isAllowedToViewPayment}">
                        <tr>
                            <th class="value"><bean:message key="viewProjectDetails.TotalPayment"/></th>
                            <td class="value">${"$"}${orfn:displayPaymentAmt(pageContext.request, requestScope.totalPayment)}</td>
                            <td class="value">&nbsp;</td>
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
								<html:img src="/i/or/icon_authorization.gif" altKey="global.Completed" styleClass="Outline" />
							</c:when>
							<c:when test="${devrStatus == 1}">
								<html:img src="/i/or/icon_notification.gif" altKey="global.DeadlineNear" styleClass="Outline" />
							</c:when>
							<c:when test="${devrStatus == 2}">
								<html:img src="/i/or/icon_warning.gif" altKey="global.Late" styleClass="Outline" />
							</c:when>
						</c:choose>
						${orfn:displayDate(pageContext.request, myDeliverableDates[deliverableStatus.index])}
						<c:if test="${empty myDeliverableLinks[deliverableStatus.index]}">
							<b><bean:message key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b><c:if
								test="${deliverableStatus.index != fn:length(myDeliverables) - 1}"><br />
							</c:if>
						</c:if>
						<c:if test="${not empty myDeliverableLinks[deliverableStatus.index]}">
							<html:link page="/actions/${myDeliverableLinks[deliverableStatus.index]}"><b><bean:message
								key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b></html:link><c:if
								test="${deliverableStatus.index != fn:length(myDeliverables) - 1}"><br />
							</c:if>
						</c:if>
					</c:forEach>
				</td>
			</c:if>

			<td class="myRoleValues" width="40%" align="left" nowrap="nowrap">
				<c:if test="${not empty outstandingDeliverables}">
					<c:forEach items="${outstandingDeliverables}" var="deliverable" varStatus="deliverableStatus">
						<c:set var="devrStatus" value="${outstandingDeliverableStatuses[deliverableStatus.index]}" />
						<c:choose>
							<c:when test="${deliverable.complete}">
								<html:img src="/i/or/icon_authorization.gif" altKey="global.Completed" styleClass="Outline" />
							</c:when>
							<c:when test="${devrStatus == 1}">
								<html:img src="/i/or/icon_notification.gif" altKey="global.DeadlineNear" styleClass="Outline" />
							</c:when>
							<c:when test="${devrStatus == 2}">
								<html:img src="/i/or/icon_warning.gif" altKey="global.Late" styleClass="Outline" />
							</c:when>
						</c:choose>
						${orfn:displayDate(pageContext.request, outstandingDeliverableDates[deliverableStatus.index])}
						<c:if test="${not empty outstandingDeliverableUserIds[deliverableStatus.index]}">
							<tc-webtag:handle coderId="${outstandingDeliverableUserIds[deliverableStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" /><b>:</b>
						</c:if>
						<b><bean:message key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b>
						<c:if test="${not empty deliverable.submission}">
							${deliverable.submission}
							<c:if test="${(isManager) && (not empty outstandingDeliverableSubmissionUserIds[deliverableStatus.index])}">
								(<tc-webtag:handle coderId="${outstandingDeliverableSubmissionUserIds[deliverableStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" />)
							</c:if>
						</c:if><br />
					</c:forEach>
					<br/>
				</c:if>

				<c:if test="${(empty outstandingDeliverables) and (not unrespondedLateDeliverables)}">
					<bean:message key="viewProjectDetails.NoOutstandingDeliverables" /><br/><br/>
				</c:if>

				<c:if test="${unrespondedLateDeliverables}">
				There are unresponded late deliverables! See the list <html:link page="/actions/${unrespondedLateDeliverablesLink}">here</html:link>.
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="lastRowTD" colspan="3"><!-- @ --></td>
		</tr>
	</table><br />
