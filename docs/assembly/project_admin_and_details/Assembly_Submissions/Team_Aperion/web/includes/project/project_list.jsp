<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
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
    					<td class="title" colspan="7"><img
    						src="../i/${categoryIconNames[idxrCategory.index]}" alt="" width="25" height="17" border="0"
    						align="right"><b><a onclick="expcollHandler(this)" href="javascript:void(0)" ID="Out${idxrCategory.index}" class="Outline" style="cursor: pointer"><img
    						ID="Out${idxrCategory.index}i" class="Outline" border="0" src="../i/plus.gif" width="9" height="9"
    						style="margin-right:5px;"><bean:message key='ProjectCategory.${fn:replace(category.name, " ", "")}' /></a>
    						(${fn:length(projects[idxrCategory.index])})</b></td>
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
    									<td><img src="../i/${categoryIconNames[idxrCategory.index]}" alt="" width="25" height="17" border="0" /></td>
    									<td><img src="../i/${rootCatalogIcons[idxrCategory.index][idxrProject.index]}" alt="${rootCatalogNames[idxrCategory.index][idxrProject.index]}" border="0" /></td>
    									<td><img src="../i/clear.gif" border="0" width="5" height="17" /></td>
    									<td><a href="ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><strong>${project.allProperties["Project Name"]}</strong> version ${project.allProperties["Project Version"]}</a></td>
    								</tr>
    							</table>
								</td>
								<td class="value">Manager</td>
								<td class="value" nowrap="nowrap">Submission</td>
								<td class="valueC" nowrap="nowrap">
									00.00.0000<br>
									00:00 AM
								</td>
								<td class="valueC" nowrap="nowrap">
									00.00.0000<br>
									00:00 AM
								</td>
								<td class="value" nowrap="nowrap">Submission</td>
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
