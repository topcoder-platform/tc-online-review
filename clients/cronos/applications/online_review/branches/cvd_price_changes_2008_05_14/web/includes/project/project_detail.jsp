<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

	<table class="scorecard" style="border-collapse:collapse;" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td class="title" colspan="2"><bean:message key="viewProjectDetails.ProjectDetails" /></td>
		</tr>
		<c:set var="rowIndex" value="0" />
		<c:if test="${isAllowedToViewSVNLink}">
			<tr class="light">
				<td class="value" width="15%" nowrap="nowrap"><b><bean:message key="viewProjectDetails.SVNModule" /></b></td>
				<td class="value" width="100%">
					<a href='${fn:escapeXml(project.allProperties["SVN Module"])}'>${orfn:htmlEncode(project.allProperties["SVN Module"])}</a></td>
			</tr>
			<c:set var="rowIndex" value="1" />
		</c:if>
		<c:if test="${isAllowedToViewAutopilotStatus}">
			<c:set var="autopilotStatus" value="${project.allProperties['Autopilot Option']}" />
			<c:if test='${autopilotStatus != "On" and autopilotStatus != "Off"}'>
				<c:set var="autopilotStatus" value="Off" />
			</c:if>
			<tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
				<td class="value" width="15%" nowrap="nowrap"><b><bean:message key="viewProjectDetails.AutopilotStatus" /></b></td>
				<td class="value" width="100%"><bean:message key="global.option.${autopilotStatus}" /></td>
			</tr>
			<c:set var="rowIndex" value="${rowIndex+1}" />
		</c:if>
		<c:forEach items="${scorecardTemplates}" var="scorecard">
			<tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
				<td class="value" width="15%" nowrap="nowrap">
					<b><bean:message key='ScorecardType.${fn:replace(scorecard.scorecardType.name, " ", "")}.scorecard' />:</b></td>
				<td class="value" width="100%">
					${orfn:htmlEncode(scorecard.name)}
					<bean:message key="global.version.shortened"/>${orfn:htmlEncode(scorecard.version)}</td>
			</tr>
			<c:set var="rowIndex" value="${rowIndex+1}" />
		</c:forEach>
		<tr>
			<td class="lastRowTD" colspan="2"><!-- @ --></td>
		</tr>
	</table><br />
