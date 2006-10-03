<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
	<html:img page="/i/minus.gif" style="display:none;" /><%-- This image is used to precache "minus" image in Internet Explorer --%>
	<c:if test="${totalProjectsCount != 0}">
	<c:forEach items="${projectTypes}" var="type" varStatus="idxrType">
		<c:if test="${typeCounts[idxrType.index] != 0}">
			<div style="padding: 9px 6px 6px 3px;"><span
				class="subTitle"><bean:message key='ProjectType.${fn:replace(type.name, " ", "")}.plural' />
				(${typeCounts[idxrType.index]})</span></div>
			<c:forEach items="${projectCategories}" var="category" varStatus="idxrCategory">
				<c:if test="${category.projectType.id == type.id}">
					<table class="scorecard" style="border-bottom:none;" cellpadding="0" cellspacing="0" width="100%">
						<tr>
    					<td class="title" colspan="7">
    						<html:img page="/i/${categoryIconNames[idxrCategory.index]}" alt="" width="25" height="17" border="0" align="right" />
    						<a onclick="return expcollHandler(this)" href="javascript:void(0)" id="Out${idxrCategory.index}" class="Outline">
    						<html:img styleId="Out${idxrCategory.index}i" styleClass="Outline" border="0" page="/i/plus.gif" width="9" height="9" style="margin-right:5px;" />
    						<bean:message key='ProjectCategory.${fn:replace(category.name, " ", "")}' />
    						</a>
    						(${fn:length(projects[idxrCategory.index])})
    					</td>
						</tr>
						<tbody ID="Out${idxrCategory.index}r" style="display:none">
						<tr>
							<td class="headerC" width="1%"></td>
							<td class="header"><bean:message key="listProjects.tblHeader.Project" /></td>
							<td class="header"><bean:message key="listProjects.tblHeader.MyRole" /></td>
							<td class="header" nowrap="nowrap"><a href="#" style="text-decoration:underline;"><bean:message key="listProjects.tblHeader.Phase" /></a></td>
							<td class="headerC" nowrap="nowrap"><a href="#" style="text-decoration:underline;"><bean:message key="listProjects.tblHeader.PhaseEndDate" /></a></td>
							<td class="headerC" nowrap="nowrap"><a href="#" style="text-decoration:underline;"><bean:message key="listProjects.tblHeader.ProjectEndDate" /></a></td>
							<td class="header" nowrap="nowrap"><a href="#" style="text-decoration:underline;"><bean:message key="listProjects.tblHeader.Deliverable" /></a></td>
						</tr>
						<c:forEach items="${projects[idxrCategory.index]}" var="project" varStatus="idxrProject">
							<tr class='${(idxrProject.index % 2 == 0) ? "light" : "dark"}'>
								<td class="value" nowrap="nowrap" colspan="2" width="100%">
    							<table cellspacing="0" cellpadding="0" border="0">
    								<tr valign="middle">
    									<td><html:img page="/i/${categoryIconNames[idxrCategory.index]}" alt="" width="25" height="17" border="0" /></td>
    									<td><html:img page="/i/${rootCatalogIcons[idxrCategory.index][idxrProject.index]}" alt="${rootCatalogNames[idxrCategory.index][idxrProject.index]}" border="0" /></td>
    									<td><html:img page="/i/clear.gif" border="0" width="5" height="17" /></td>
    									<td><a href="ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><strong>${project.allProperties["Project Name"]}</strong> version ${project.allProperties["Project Version"]}</a></td>
    								</tr>
    							</table>
								</td>
								<td class="valueC">${myRoles[idxrCategory.index][idxrProject.index]}</td>
								<c:set var="phase" value="${phases[idxrCategory.index][idxrProject.index]}" />
								<c:if test="${!(empty phase)}">
									<td class="value" nowrap="nowrap"><bean:message key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></td>
									<td class="valueC">${phaseEndDates[idxrCategory.index][idxrProject.index]}</td>
									<td class="valueC">${projectEndDates[idxrCategory.index][idxrProject.index]}</td>
								</c:if>
								<c:if test="${empty phase}">
									<td class="value"><!-- @ --></td>
									<td class="value"><!-- @ --></td>
									<td class="value"><!-- @ --></td>
								</c:if>
								<td class="value" nowrap="nowrap"><!-- @ --></td>
							</tr>
						</c:forEach>
						<c:if test="${fn:length(projects[idxrCategory.index]) == 0}">
							<tr class="light">
								<td class="value" colspan="7"><bean:message key="listProjects.NoProjects.Category" /></td>
							</tr>
						</c:if>
						<tr>
							<td class="lastRowTD" colspan="7"><!-- @ --></td>
						</tr>
						</tbody>
					</table>
				</c:if>
			</c:forEach>
		</c:if>
	</c:forEach>
	</c:if>

	<c:if test="${totalProjectsCount == 0}">
		<div style="text-align:center;margin-top:16px;margin-bottom:8px;">
			<bean:message key="listProjects.NoProjects" />
		</div>
	</c:if>
