<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<html:img src="/i/or/minus.gif" style="display:none;" /><%-- This image is used to precache "minus" image in Internet Explorer --%>
<c:if test="${totalProjectsCount != 0}">
	<c:forEach items="${projectTypes}" var="type" varStatus="idxrType">
		<c:if test="${typeCounts[idxrType.index] != 0}">
			<div style="padding: 9px 6px 6px 3px;"><span class="subTitle"><bean:message key="ProjectType.${fn:replace(type.name, ' ', '')}.plural"/>
				(${typeCounts[idxrType.index]})</span></div>
			<c:forEach items="${projectCategories}" var="category" varStatus="idxrCategory">
				<c:if test="${category.projectType.id == type.id}">
                    <div>
					<table class="scorecard" width="100%" cellpadding="0" cellspacing="0">
						<tr>
    					<td class="title" colspan="${(isMyProjects)?'7':'5'}">
    						<img src="/i/${categoryIconNames[idxrCategory.index]}" alt="" width="25" height="17" border="0" align="right"/>
    						<a onclick="return expcollHandler(this)" href="javascript:void(0)" id="Out${idxrCategory.index}" class="Outline"><img
    							id="Out${idxrCategory.index}i" class="Outline" border="0" src="/i/or/plus.gif" width="9" height="9" style="margin-right:5px;" />
    							<bean:message key="ProjectCategory.${fn:replace(category.name, ' ', '')}" /></a>
    						(${fn:length(projects[idxrCategory.index])})
    					</td>
						</tr>
						<tbody ID="Out${idxrCategory.index}r" style="display:none">
							<tr>
								<td class="headerC" width="1%"></td>
								<td class="header"><bean:message key="listProjects.tblHeader.Project" /></td>
								<c:if test="${isMyProjects}">
									<td class="header"><bean:message key="listProjects.tblHeader.MyRole" /></td>
								</c:if>
								<td class="header" nowrap="nowrap"><bean:message key="listProjects.tblHeader.Phase" /></td>
								<td class="headerC" nowrap="nowrap"><bean:message key="listProjects.tblHeader.PhaseEndDate" /></td>
								<td class="headerC" nowrap="nowrap"><bean:message key="listProjects.tblHeader.ProjectEndDate" /></td>
								<c:if test="${isMyProjects}">
									<td class="header" nowrap="nowrap"><bean:message key="listProjects.tblHeader.Deliverable" /></td>
								</c:if>
							</tr>
							<c:forEach items="${projects[idxrCategory.index]}" var="project" varStatus="idxrProject">
								<tr class="${(idxrProject.index % 2 == 0) ? 'light' : 'dark'}">
									<td class="value" nowrap="nowrap" colspan="2" width="100%">
	    							<table cellspacing="0" cellpadding="0" border="0">
	    								<tr valign="middle">
	    									<td><img src="/i/${categoryIconNames[idxrCategory.index]}" alt="" width="25" height="17" border="0" /></td>
	    									<td><img src="/i/${rootCatalogIcons[idxrCategory.index][idxrProject.index]}" alt="${rootCatalogNames[idxrCategory.index][idxrProject.index]}" border="0" /></td>
	    									<td><img src="/i/clear.gif" border="0" width="5" height="17" /></td>
	    									<td>
	    										<html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&amp;pid=${project.id}"><strong>${project.allProperties['Project Name']}</strong>
	    											version ${project.allProperties['Project Version']}</html:link></td>
	    								</tr>
	    							</table>
									</td>
									<c:if test="${isMyProjects}">
										<td class="valueC">${myRoles[idxrCategory.index][idxrProject.index]}</td>
									</c:if>
									<c:set var="phase" value="${phases[idxrCategory.index][idxrProject.index]}" />
									<c:if test="${!(empty phase)}">
										<td class="value" nowrap="nowrap"><bean:message key="ProjectPhase.${fn:replace(phase[0].phaseType.name, ' ', '')}" /></td>
										<td class="valueC" nowrap="nowrap">${orfn:displayDateBr(pageContext.request, phaseEndDates[idxrCategory.index][idxrProject.index])}</td>
										<td class="valueC" nowrap="nowrap">${orfn:displayDateBr(pageContext.request, projectEndDates[idxrCategory.index][idxrProject.index])}</td>
										<c:if test="${isMyProjects}">
											<td class="value" nowrap="nowrap">${myDeliverables[idxrCategory.index][idxrProject.index]}</td>
										</c:if>
									</c:if>
									<c:if test="${empty phase}">
										<td class="value" colspan="3"><!-- @ --></td>
										<c:if test="${isMyProjects}">
											<td class="value"><!-- @ --></td>
										</c:if>
									</c:if>
								</tr>
							</c:forEach>
							<c:if test="${fn:length(projects[idxrCategory.index]) == 0}">
								<tr class="light">
									<td class="value" colspan="${(isMyProjects) ? '7' : '5'}"><bean:message key="listProjects.NoProjects.Category" /></td>
								</tr>
							</c:if>
							<tr>
								<td class="lastRowTD" colspan="${(isMyProjects) ? '7' : '5'}"><!-- @ --></td>
							</tr>
						</tbody>
					</table>
                    </div>
				</c:if>
			</c:forEach>
		</c:if>
	</c:forEach>
</c:if>

<c:if test="${totalProjectsCount == 0}">
	<div style="text-align:center;margin-top:16px;margin-bottom:8px;">
		<c:choose>
			<c:when test="${projectTabIndex == 4}">
				<bean:message key="listProjects.NoProjects.inactive" />
			</c:when>
			<c:otherwise>
				<bean:message key="listProjects.NoProjects.open" />
			</c:otherwise>
		</c:choose>
	</div>
</c:if>
