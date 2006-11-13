<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
	<table class="scorecard" style="border-collapse:collapse;" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td class="title"><bean:message key="viewProjectDetails.box.MyRole" /></td>
			<c:if test="${empty myDeliverables}">
				<td class="title"><!-- @ --></td>
			</c:if>
			<c:if test="${!(empty myDeliverables)}">
				<td class="title"><bean:message key="viewProjectDetails.box.MyDeliverabes" /></td>
			</c:if>
			<td class="title"><bean:message key="viewProjectDetails.box.OutstandingDeliverabes" /></td>
		</tr>
		<tr class="light">
			<td class="value" width="15%" align="left">
				<b>${myRole}</b><br />
				<c:if test="${isAllowedToViewPayment == true}">
					<b><bean:message key="viewProjectDetails.Payment" />:</b>
					<c:if test="${empty myPayment}">
						<bean:message key="NotAvailable" />
					</c:if>
					<c:if test="${!(empty myPayment)}">
						${"$"}${myPayment}
						<c:if test="${!(empty wasPaid) && wasPaid == true}">
							<bean:message key="viewProjectDetails.Paid.yes" />
						</c:if>
						<c:if test="${!(empty wasPaid) && wasPaid != true}">
							<bean:message key="viewProjectDetails.Paid.no" />
						</c:if>
					</c:if>
				</c:if>
			</td>
			<c:if test="${empty myDeliverables}">
				<td class="value" width="45%" align="left" nowrap="nowrap"><!-- @ --></td>
			</c:if>
			<c:if test="${!(empty myDeliverables)}">
				<td class="value" width="45%" align="left" nowrap="nowrap">
					<c:forEach items="${myDeliverables}" var="deliverable" varStatus="deliverableStatus">
						<c:set var="devrStatus" value="${myDeliverableStatuses[deliverableStatus.index]}" />
						<c:choose>
							<c:when test="${deliverable.complete}">
								<html:img page="/i/icon_authorization.gif" altKey="global.Completed" styleClass="Outline" />
							</c:when>
							<c:when test="${devrStatus == 1}">
								<html:img page="/i/icon_notification.gif" altKey="global.DeadlineNear" styleClass="Outline" />
							</c:when>
							<c:when test="${devrStatus == 2}">
								<html:img page="/i/icon_warning.gif" altKey="global.Late" styleClass="Outline" />
							</c:when>
						</c:choose>
						${myDeliverableDates[deliverableStatus.index]}
						<c:if test="${empty myDeliverableLinks[deliverableStatus.index]}">
							<b><bean:message key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b><c:if
								test="${deliverableStatus.index != fn:length(myDeliverables) - 1}"><br />
							</c:if>
						</c:if>
						<c:if test="${!(empty myDeliverableLinks[deliverableStatus.index])}">
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
			<c:if test="${!(empty outstandingDeliverables)}">
				<td class="value" width="40%" align="left" nowrap="nowrap">
					<c:forEach items="${outstandingDeliverables}" var="deliverable" varStatus="deliverableStatus">
						<c:set var="devrStatus" value="${outstandingDeliverableStatuses[deliverableStatus.index]}" />
						<c:choose>
							<c:when test="${deliverable.complete}">
								<html:img page="/i/icon_authorization.gif" altKey="global.Completed" styleClass="Outline" />
							</c:when>
							<c:when test="${devrStatus == 1}">
								<html:img page="/i/icon_notification.gif" altKey="global.DeadlineNear" styleClass="Outline" />
							</c:when>
							<c:when test="${devrStatus == 2}">
								<html:img page="/i/icon_warning.gif" altKey="global.Late" styleClass="Outline" />
							</c:when>
						</c:choose>
						${outstandingDeliverableDates[deliverableStatus.index]}
						<c:if test="${!(empty outstandingDeliverableUserIds[deliverableStatus.index])}">
							<tc-webtag:handle coderId="${outstandingDeliverableUserIds[deliverableStatus.index]}" context="component" /><b>:</b>
						</c:if>
						<b><bean:message key='Deliverable.${fn:replace(deliverable.name, " ", "")}' /></b>
						<c:if test="${!(empty deliverable.submission)}">
							${deliverable.submission}
							<c:if test="${(isManager == true) && !(empty outstandingDeliverableSubmissionUserIds[deliverableStatus.index])}">
								(<tc-webtag:handle coderId="${outstandingDeliverableSubmissionUserIds[deliverableStatus.index]}" context="component" />)
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
