<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ page import="com.cronos.onlinereview.actions.AuthorizationHelper" %>
<%@ page import="com.cronos.onlinereview.actions.Constants" %>
	<table class="stat" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: 1px solid #999999;">
		<tr>
			<td class="title"><bean:message key="viewProjectDetails.box.Timeline" /></td>
			<%
				if (AuthorizationHelper.hasUserPermission(request, Constants.SET_TL_NOTIFY_PERM_NAME)) {
			%>
				<td class="title" style="text-align: right;">
					<input type="checkbox" name="C1" value="On" ${(sendTLNotifications == 'On') ? 'checked="checked"' : '' } />
					<bean:message key="viewProjectDetails.ReceiveTLNotifications" />
				</td>
			<%
				} else {
			%>
				<td class="title">&nbsp;</td>
			<%
				}
			%>
		</tr>
		<tr>
			<td width="392" valign="top" style="border-bottom: 1px solid #999999;">
				<table class="stat" cellpadding="0" cellspacing="0" border="0" style="border:none;width:392px;">
					<tr>
						<td class="header"><bean:message key="viewProjectDetails.Timeline.Phase" /></td>
						<td class="headerC"><bean:message key="viewProjectDetails.Timeline.Status" /></td>
						<td class="headerC"><bean:message key="viewProjectDetails.Timeline.Start" /></td>
						<td class="headerC"><bean:message key="viewProjectDetails.Timeline.End" /></td>
					</tr>
					<c:forEach items="${phases}" var="phase" varStatus="idxrPhase">
						<tr class='${(idxrPhase.index % 2 == 0) ? "light" : "dark"}'>
							<td class="value" nowrap="nowrap">
								<c:choose>
								<c:when test='${phase.phaseType.name == "Registration"}'>
									<%
										if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_REGISTRATIONS_PERM_NAME)) {
									%>
											<a onClick='return expandcontent("sc${idxrPhase.index + 1}", this)'
												href="javascript:void(0)"><b><bean:message
													key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></b></a>
									<%
										} else {
									%>
											<b><bean:message key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></b>
									<%
										}
									%>
								</c:when>
								<c:otherwise>
									<a onClick='return expandcontent("sc${idxrPhase.index + 1}", this)'
										href="javascript:void(0)"><b><bean:message
											key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></b></a>
								</c:otherwise>
								</c:choose>
							</td>
							<td class="valueC" nowrap="nowrap">
								<bean:message key='ProjectPhase.Status.${fn:replace(phase.phaseStatus.name, " ", "")}' />
							</td>
							<td class="valueC" nowrap="nowrap" title='${!(empty originalStart[idxrPhase.index]) ? originalStart[idxrPhase.index] : ""}'>${displayedStart[idxrPhase.index]}</td>
							<td class="valueC" nowrap="nowrap" title='${!(empty originalEnd[idxrPhase.index]) ? originalEnd[idxrPhase.index] : ""}'>${displayedEnd[idxrPhase.index]}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
			<td valign="top" width="100%" bgcolor="#F7F7F7" style="border-bottom: 1px solid #999999;">
				<iframe name="I1" src="../includes/Timelines/ganttChart.jsp" width="100%" height="189" marginwidth="1" marginheight="1" border="0" frameborder="0">
					<bean:message key="error.InlineFrameNotSupported" />
				</iframe>
			</td>
		</tr>
		<tr>
			<td class="lastRowTD" colspan="2"><!-- @ --></td>
		</tr>
	</table><br />
