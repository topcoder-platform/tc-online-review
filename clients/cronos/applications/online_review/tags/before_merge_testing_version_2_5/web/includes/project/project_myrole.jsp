<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
	<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
		<tr>
			<td class="title"><bean:message key="viewProjectDetails.box.MyRole" /></td>
			<c:if test="${empty myDeliverables}">
				<td class="title"><!-- @ --></td>
			</c:if>
			<c:if test="${not empty myDeliverables}">
				<td class="title"><bean:message key="viewProjectDetails.box.MyDeliverabes" /></td>
			</c:if>
			<td class="title"><bean:message key="viewProjectDetails.box.OutstandingDeliverabes" /></td>
		</tr>
		<tr class="light">
			<td class="value" width="15%" align="left">
				<b>${myRole}</b><br />
				<c:if test="${isAllowedToViewPayment}">
					<b><bean:message key="viewProjectDetails.Payment" />:</b>
					<c:if test="${empty myPayment}">
						<bean:message key="NotAvailable" />
					</c:if>
					<c:if test="${not empty myPayment}">
						${"$"}${orfn:displayPaymentAmt(pageContext.request, myPayment)}
						<c:if test="${wasPaid}">
							<bean:message key="viewProjectDetails.Paid.yes" />
						</c:if>
						<c:if test="${not wasPaid}">
							<bean:message key="viewProjectDetails.Paid.no" />
						</c:if>
					</c:if>
				</c:if>
			</td>
			<c:if test="${empty myDeliverables}">
				<td class="value" width="45%" align="left" nowrap="nowrap"><!-- @ --></td>
			</c:if>
			<c:if test="${not empty myDeliverables}">
				<td class="value" width="45%" align="left" nowrap="nowrap">
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
			<c:if test="${empty outstandingDeliverables}">
				<td class="value" width="40%" align="left" nowrap="nowrap"><bean:message key="viewProjectDetails.NoOutstandingDeliverables" /></td>
			</c:if>
			<c:if test="${not empty outstandingDeliverables}">
				<td class="value" width="40%" align="left" nowrap="nowrap">
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
				</td>
			</c:if>
		</tr>
		<tr>
			<td class="lastRowTD" colspan="3"><!-- @ --></td>
		</tr>
	</table><br />
