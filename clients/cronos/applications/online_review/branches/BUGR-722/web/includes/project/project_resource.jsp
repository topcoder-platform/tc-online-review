<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:if test="${isAllowedToViewResources}">
	<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
		<tr>
			<td class="title" colspan='${(isAllowedToViewAllPayment) ? "4" : "2"}'><bean:message key="viewProjectDetails.box.Resources" /></td>
		</tr>
		<tr>
			<td class="header"><b><bean:message key="viewProjectDetails.Resource.Role" /></b></td>
			<td class="header"><b><bean:message key="viewProjectDetails.Resource.Name" /></b></td>
			<c:if test="${isAllowedToViewAllPayment}">
				<td class="header" nowrap="nowrap"><b><bean:message key="viewProjectDetails.Resource.Payment" /></b></td>
				<td class="header" nowrap="nowrap"><b><bean:message key="viewProjectDetails.Resource.Paid_qm" /></b></td>
			</c:if>
		</tr>
		<c:forEach items="${resources}" var="resource" varStatus="idxrResource">
			<tr class='${(idxrResource.index % 2 == 0) ? "light" : "dark"}'>
				<td class="value" nowrap="nowrap"><bean:message key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}.bold' /></td>
				<td class="value" nowrap="nowrap">
					<html:link href="mailto:${users[idxrResource.index].email}" title="Send Email"><html:img srcKey="viewProjectDetails.Resource.Email.img" border="0" /></html:link>
					<tc-webtag:handle coderId="${users[idxrResource.index].id}" context="${orfn:getHandlerContext(pageContext.request)}" />
				</td>
				<c:if test="${isAllowedToViewAllPayment}">
					<c:choose>
						<c:when test='${not empty resource.allProperties["Payment"]}'>
							<td class="value" nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, resource.allProperties["Payment"])}</td>
						</c:when>
						<c:otherwise>
							<td class="value" nowrap="nowrap"><bean:message key="NotAvailable" /></td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test='${(not empty resource.allProperties["Payment Status"])}'>
							<c:if var="isPaymentStatusNA" test='${resource.allProperties["Payment Status"] == "N/A"}'>
								<td class="value" nowrap="nowrap"><bean:message key='NotApplicable'/></td>
							</c:if>
							<c:if test="${!isPaymentStatusNA}">
								<td class="value" nowrap="nowrap"><bean:message key='viewProjectDetails.Resource.Paid.${(resource.allProperties["Payment Status"] == "Yes") ? "yes" : "no"}' /></td>
							</c:if>
						</c:when>
						<c:otherwise>
							<td class="value" nowrap="nowrap"><bean:message key="NotAvailable" /></td>
						</c:otherwise>
					</c:choose>
				</c:if>
			</tr>
		</c:forEach>
		<tr>
			<td class="lastRowTD" colspan='${(isAllowedToViewAllPayment) ? "4" : "2"}'><!-- @ --></td>
		</tr>
	</table><br />
</c:if>
