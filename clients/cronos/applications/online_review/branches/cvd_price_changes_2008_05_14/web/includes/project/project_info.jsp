<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
	<table border="0" width="100%" id="table12" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top"><html:img alt="" src="/i/${categoryIconName}" /></td>
			<td width="6%" height="45" valign="middle"><html:img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
			<td width="40%"><span class="bodyTitle">${project.allProperties["Project Name"]}</span>
				<c:if test='${!(empty project.allProperties["Project Version"])}'>
					<font size="4"><bean:message key="global.version" />
						${project.allProperties["Project Version"]}</font>
				</c:if></td>
			<td width="50%" valign="top" align="right">
				<c:if test="${isAllowedToContactPM}">
					<html:link page="/actions/ContactManager.do?method=contactManager&amp;pid=${project.id}"><bean:message key="viewProjectDetails.ContactPM" /></html:link> |
				</c:if>
				<a class="breadcrumbLinks"
					href="${descriptionLink}"><bean:message key="viewProjectDetails.FullDescription" /></a> |
				<a class="breadcrumbLinks"
					href="${forumLink}"><bean:message key="viewProjectDetails.DevelopmentForum" /></a></td>
		</tr>
	</table>

	<c:set var="notesText" value='${project.allProperties["Notes"]}' />
	<c:if test='${!(empty notesText)}'>
		<br />
		<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
			<tr>
				<td class="title"><bean:message key="viewProjectDetails.box.Notes" /></td>
			</tr>
			<tr class="light">
				<td class="value" align="left">${orfn:htmlEncode(notesText)}</td>
			</tr>
			<tr>
				<td class="lastRowTD"><!-- @ --></td>
			</tr>
		</table>
	</c:if>
