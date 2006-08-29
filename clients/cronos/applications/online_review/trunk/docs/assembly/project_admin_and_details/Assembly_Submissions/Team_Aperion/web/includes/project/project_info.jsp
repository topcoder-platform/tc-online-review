<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ page import="com.cronos.onlinereview.actions.AuthorizationHelper" %>
<%@ page import="com.cronos.onlinereview.actions.Constants" %>
	<table border="0" width="100%" id="table12" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top"><img height="62" alt="" src="../i/${categoryIconName}" width="66" /></td>
			<td width="6%" height="45" valign="middle"><img src="../i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
			<td width="40%"><span class="bodyTitle">${project.allProperties["Project Name"]}</span>
				<c:if test='${!(empty project.allProperties["Project Version"])}'>
					<font size="4"><bean:message key="viewProjectDetails.version" />
						${project.allProperties["Project Version"]}</font>
				</c:if></td>
			<td width="50%" valign="top" align="right">
				<%
					if (AuthorizationHelper.hasUserPermission(request, Constants.CONTACT_PM_PERM_NAME)) {
				%>
						<a href="ContactManager.do?method=contactManager&amp;pid=${project.id}"><bean:message key="viewProjectDetails.ContactPM" /></a> |
				<%
					}
				%>
				<a class="breadcrumbLinks"
					href='https://software.topcoder.com/catalog/c_component.jsp?comp=${project.allProperties["Component ID"]}&ver=${project.allProperties["Version ID"]}'><bean:message
						key="viewProjectDetails.FullDescription" /></a> |
				<a class="breadcrumbLinks"
					href='https://software.topcoder.com/forum/c_forum.jsp?f=${project.allProperties["Developer Forum ID"]}'><bean:message
						key="viewProjectDetails.DevelopmentForum" /></a>
			</td>
		</tr>
	</table>

	<%-- NOTES TABLE HERE --%>
	<c:if test='${!(empty project.allProperties["Notes"])}'>
		<br />
		<table class="scorecard" style="border-collapse: collapse;" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td class="title"><b><bean:message key="viewProjectDetails.box.Notes" /></b></td>
			</tr>
			<tr class="light">
				<td class="value" align="left">${fn:escapeXml(project.allProperties["Notes"])}</td>
			</tr>
			<tr>
				<td class="lastRowTD"><!-- @ --></td>
			</tr>
		</table>
	</c:if>
