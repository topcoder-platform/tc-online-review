<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ page import="com.cronos.onlinereview.actions.AuthorizationHelper" %>
<%@ page import="com.cronos.onlinereview.actions.Constants" %>
	<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td class="title" colspan="2"><b><bean:message key="viewProjectDetails.ProjectDetails" /></b></td>
		</tr>
<%
	if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_SVN_LINK_PERM_NAME)) {
%>
		<tr class="light">
			<td class="value" width="15%" align="left" nowrap><p align="left"><b><bean:message key="viewProjectDetails.SVNModule" /></b></td>
			<td class="value" width="100%" align="left">
				<a href='${fn:escapeXml(project.allProperties["SVN Module"])}'>${fn:escapeXml(project.allProperties["SVN Module"])}</a>
			</td>
		</tr>
<%
	}
%>
		<tr class="dark">
			<td class="value" width="15%" align="left" nowrap><b><bean:message key="viewProjectDetails.ScreeningScorecard" /></b></td>
			<td class="value" width="100%" align="left">Component Design Screening Scorecard v1.0</td>
		</tr>
		<tr class="light">
			<td class="value" width="15%" align="left" nowrap><b><bean:message key="viewProjectDetails.ReviewScorecard" /></b></td>
			<td class="value" width="100%" align="left">Component Design Review Scorecard v1.4.2</td>
		</tr>
		<tr class="dark">
			<td class="value" width="15%" align="left" nowrap><b><bean:message key="viewProjectDetails.ApprovalScorecard" /></b></td>
			<td class="value" width="100%" align="left">Component Approval Scorecard v1.1.0 </td>
		</tr>
		<tr>
			<td class="lastRowTD" colspan="2"></td>
		</tr>
	</table><br />
