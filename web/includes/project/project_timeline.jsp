<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
	<table class="stat" cellpadding="0" cellspacing="0" width="100%" style="table-layout: fixed; border-bottom: 1px solid #999999;">
		<tr>
			<td class="title" width="392"><bean:message key="viewProjectDetails.box.Timeline" /></td>
			<c:if test="${isAllowedToSetTL}">
				<td class="title" style="text-align:right;">
					<input type="checkbox" onclick="setTimelineNotification(${project.id}, this); return false;"
						${(sendTLNotifications == 'On') ? 'value="On" checked="checked"' : 'value="Off"' } />
					<bean:message key="viewProjectDetails.ReceiveTLNotifications" />
				</td>
			</c:if>
			<c:if test="${not isAllowedToSetTL}">
				<td class="title">&nbsp;</td>
			</c:if>
		</tr>
		<tr>
			<td valign="top" style="overflow:visible;border-bottom: 1px solid #999999;">
				<table class="stat" cellpadding="0" cellspacing="0" border="0" style="width:100%;table-layout:auto;border:none;">
					<tr>
						<td class="header"><bean:message key="viewProjectDetails.Timeline.Phase" /></td>
						<td class="headerC"><bean:message key="viewProjectDetails.Timeline.Status" /></td>
						<td class="headerC"><bean:message key="viewProjectDetails.Timeline.Start" /></td>
						<td class="headerC"><bean:message key="viewProjectDetails.Timeline.End" /></td>
					</tr>
					<c:forEach items="${phases}" var="phase" varStatus="phaseStatus">
						<tr class='${(phaseStatus.index % 2 == 0) ? "light" : "dark"}'>
							<c:if test="${phaseGroupIndexes[phaseStatus.index] == -1}">
								<td class="value" nowrap="nowrap">
									<b><bean:message key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></b></td>
							</c:if>
							<c:if test="${phaseGroupIndexes[phaseStatus.index] != -1}">
								<td class="value" nowrap="nowrap">
									<a onClick='return activateTab("sc${phaseGroupIndexes[phaseStatus.index] + 1}", this)'
										href="javascript:void(0)"><b><bean:message
											key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></b></a></td>
							</c:if>
							<c:choose>
								<c:when test="${phaseStatuseCodes[phaseStatus.index] == 0}">
									<td class="valueC" nowrap="nowrap"><bean:message key="ProjectPhaseStatus.Scheduled" /></td>
								</c:when>
								<c:when test="${phaseStatuseCodes[phaseStatus.index] == 1}">
									<td class="valueC" nowrap="nowrap"><bean:message key="ProjectPhaseStatus.Closed" /></td>
								</c:when>
								<c:when test="${phaseStatuseCodes[phaseStatus.index] == 2}">
									<td class="valueC" nowrap="nowrap"><bean:message key="ProjectPhaseStatus.Open" /></td>
								</c:when>
								<c:when test="${phaseStatuseCodes[phaseStatus.index] == 3}">
									<td class="valueC" nowrap="nowrap" style="color:cccc00;"><bean:message key="ProjectPhaseStatus.Closing" /></td>
								</c:when>
								<c:when test="${phaseStatuseCodes[phaseStatus.index] == 4}">
									<td class="valueC" nowrap="nowrap" style="color:#cc0000;"><bean:message key="ProjectPhaseStatus.Late" /></td>
								</c:when>
								<c:otherwise>
									<%-- This should never happen --%>
									<td class="valueC" nowrap="nowrap"><!-- @ --></td>
								</c:otherwise>
							</c:choose>
							<td class="valueC" nowrap="nowrap" title='${!(empty originalStart[phaseStatus.index]) ? originalStart[phaseStatus.index] : ""}'>${displayedStart[phaseStatus.index]}</td>
							<td class="valueC" nowrap="nowrap" title='${!(empty originalEnd[phaseStatus.index]) ? originalEnd[phaseStatus.index] : ""}'>${displayedEnd[phaseStatus.index]}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
			<td valign="top" bgcolor="#F7F7F7" style="border-bottom: 1px solid #999999;">
				<div style="width:100%; overflow:scroll; overflow-y:visible;">
					<table class="stat" cellpadding="0" cellspacing="0" style="border:none;" width="100%">
						<tr class="header">
							<td class="header">&#160;</td>
						</tr>
						<c:forEach items="${phases}" var="phase" varStatus="phaseStatus">
							<tr class='${(phaseStatus.index % 2) == 0 ? "light" : "dark"}'>
								<td class="ganttRow">
									<table>
										<tr>
											<c:if test="${ganttOffsets[phaseStatus.index] != 0}">
												<td><html:img width="${ganttOffsets[phaseStatus.index] * pixelsPerHour}" page="/i/clear.gif" height="0" /></td>
											</c:if>
											<td class="phase"><div style="width:${ganttLengths[phaseStatus.index] * pixelsPerHour}px;"><bean:message key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></div></td>
											<c:if test="${ganttLengths[phaseStatus.index] <= 1}">
												<td class="length">${ganttLengths[phaseStatus.index]} hr</td>
											</c:if>
											<c:if test="${ganttLengths[phaseStatus.index] > 1}">
												<td class="length">${ganttLengths[phaseStatus.index]} hrs</td>
											</c:if>
										</tr>
									</table>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td class="lastRowTD" colspan="2"><!-- @ --></td>
		</tr>
	</table><br />
