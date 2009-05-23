<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>

<TABLE class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
	<TBODY>
		<TR>
			<TD class="title" colspan="2"><bean:message key="viewProjectDetails.ProjectLinks" /></TD>
		</TR>
		<TR>
			<TD class="header" width="70%"><B><bean:message key="viewProjectDetails.Project" /></B></TD>
			<TD class="header" width="30%"><B><bean:message key="viewProjectDetails.ProjectLinkType" /></B></TD>
		</TR>
		
		<c:forEach items="${projectLinks}" var="linkInfo" varStatus="idxLinkInfo">
			<TR class='${(idxLinkInfo.index % 2 == 0) ? "light" : "dark"}'>
				<TD class="value" nowrap="nowrap">
					<html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${linkInfo.id}">${linkInfo.name} v ${linkInfo.version}</html:link>
				</TD>
				<TD class="value" nowrap="nowrap">
				 <c:choose>
					<c:when test="${isAllowedToEditProjects}"><html:link page="/actions/EditProjectLink.do?method=viewProjectLinks&pid=${project.id}">${linkInfo.typeName}</html:link></c:when>
				 	<c:otherwise>${linkInfo.typeName}</c:otherwise>
				 </c:choose>
				</TD>
			</TR>
		</c:forEach>
		<TR>
			<TD class="lastRowTD" colspan="2"><!-- @ --></TD>
		</TR>
	</TBODY>
</TABLE>
<BR>

<TABLE class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
	<TBODY>
		<TR>
			<TD class="title" colspan="2"><bean:message key="viewProjectDetails.ProjectsLinkToThis" /></TD>
		</TR>
		<TR>
			<TD class="header" width="70%"><B><bean:message key="viewProjectDetails.Project" /></B></TD>
			<TD class="header" width="30%"><B><bean:message key="viewProjectDetails.ProjectLinkType" /></B></TD>
		</TR>
		
		<c:forEach items="${projectsLinkedTo}" var="linkInfo" varStatus="idxLinkInfo">
			<TR class='${(idxLinkInfo.index % 2 == 0) ? "light" : "dark"}'>
				<TD class="value" nowrap="nowrap">
					<html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${linkInfo.id}">${linkInfo.name} v${linkInfo.version}</html:link>
				</TD>
				<TD class="value" nowrap="nowrap">
				 ${linkInfo.typeName}
				</TD>
			</TR>
		</c:forEach>
		<TR>
			<TD class="lastRowTD" colspan="2"><!-- @ --></TD>
		</TR>
	</TBODY>
</TABLE>
<BR>
