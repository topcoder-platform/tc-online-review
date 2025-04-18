<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005-2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment renders the list of projects of selected status.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<img src="/i/or/minus.gif" style="display:none;" /><%-- This image is used to precache "minus" image in Internet Explorer --%>
<c:if test="${projectCount != 0}">
    <s:form method="GET">
        <input type="hidden" name="scope" value="${scope}">
        <select class="inputBox" name="category" onchange="this.form.submit()">
            <c:forEach items="${userProjectTypes}" var="type" varStatus="idxrType">
                <optgroup label="${type.name} (${type.count})">
                    <c:forEach items="${type.categories}" var="category" varStatus="idxrCategory">
                        <option value="${category.id}" <c:if test="${category.id == selectedCategoryId}">selected </c:if>>${category.name} (${category.count})</option>
                    </c:forEach>
                </optgroup>
            </c:forEach>
        </select>
    </s:form>

    <div>
        <table class="scorecard" width="100%" cellpadding="0" cellspacing="0">
            <tbody ID="Out${idxrCategory.index}r">
                <tr>
                    <td class="headerC" width="1%"></td>
                    <td class="header"><or:text key="listProjects.tblHeader.Project" /></td>
                    <c:if test="${isMyProjects}">
                        <td class="header"><or:text key="listProjects.tblHeader.MyRole" /></td>
                    </c:if>
                    <td class="header" nowrap="nowrap"><or:text key="listProjects.tblHeader.Phase" /></td>
                    <td class="headerC" nowrap="nowrap"><or:text key="listProjects.tblHeader.PhaseEndDate" /></td>
                    <td class="headerC" nowrap="nowrap"><or:text key="listProjects.tblHeader.ProjectEndDate" /></td>
                    <c:if test="${isMyProjects}">
                        <td class="header" nowrap="nowrap"><or:text key="listProjects.tblHeader.Deliverable" /></td>
                    </c:if>
                </tr>
                <c:forEach items="${projects}" var="project" varStatus="idxrProject">
                    <tr class="${(idxrProject.index % 2 == 0) ? 'light' : 'dark'}">
                        <td class="value" nowrap="nowrap" colspan="2" width="100%">
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr valign="middle">
                                <td><img src="/i/${categoryIconName}" alt="" width="25" height="17" border="0" /></td>
                                <td><img src="/i/${rootCatalogIcons[idxrProject.index]}" alt="${rootCatalogNames[idxrProject.index]}" border="0" /></td>
                                <td><img src="/i/clear.gif" border="0" width="5" height="17" /></td>
                                <td>
                                    <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><strong>${project.allProperties['Project Name']}</strong>
                                        version ${project.allProperties['Project Version']}</a></td>
                            </tr>
                        </table>
                        </td>
                        <c:if test="${isMyProjects}">
                            <td class="valueC">${myRoles[idxrProject.index]}</td>
                        </c:if>
                        <c:set var="phase" value="${phases[idxrProject.index]}" />
                        <c:if test="${!(empty phase)}">
                            <td class="value" nowrap="nowrap"><or:text key="ProjectPhase.${fn:replace(phase[0].phaseType.name, ' ', '')}" /></td>
                            <td class="valueC" nowrap="nowrap">${orfn:displayDateBr(pageContext.request, phaseEndDates[idxrProject.index])}</td>
                            <td class="valueC" nowrap="nowrap">${orfn:displayDateBr(pageContext.request, projectEndDates[idxrProject.index])}</td>
                            <c:if test="${isMyProjects}">
                                <td class="value" nowrap="nowrap">${myDeliverables[idxrProject.index]}</td>
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
                <c:if test="${fn:length(projects) == 0}">
                    <tr class="light">
                        <td class="value" colspan="${(isMyProjects) ? '7' : '5'}"><or:text key="listProjects.NoProjects.Category" /></td>
                    </tr>
                </c:if>
                <tr>
                    <td class="lastRowTD" colspan="${(isMyProjects) ? '7' : '5'}"><!-- @ --></td>
                </tr>
            </tbody>
        </table>
        <div class="projectListPagination">
            <div class="info">
                Showing ${showingProjectsFrom} to ${showingProjectsTo} of ${totalProjectCount} projects
            </div>
            <c:if test="${totalProjectCount > projectCount}">
                <div class="pages">
                    <ul class="pagination">
                        <li class="pageItem ${page == 1 ? 'disabled' : ''}">
                            <a class="pageLink" href="<or:url value='?scope=${scope}&category=${selectedCategoryId}&page=${page - 1}' />">Previous</a>
                        </li>
                        <c:forEach begin="${paginationStart}" end="${paginationEnd}" var="pageIndex">
                            <li class="pageItem ${pageIndex == page ? 'active' : ''}">
                                <a class="pageLink" href="<or:url value='?scope=${scope}&category=${selectedCategoryId}&page=${pageIndex}' />">${pageIndex}</a>
                            </li>
                        </c:forEach>
                        <li class="pageItem ${page == lastPage ? 'disabled' : ''}">
                            <a class="pageLink" href="<or:url value='?scope=${scope}&category=${selectedCategoryId}&page=${page + 1}' />">Next</a>
                        </li>
                    </ul>
                </div>
            </c:if>
        </div>
    </div>

</c:if>

<c:if test="${projectCount == 0}">
    <div style="text-align:center;margin-top:16px;margin-bottom:8px;">
        <c:choose>
            <c:when test="${projectTabIndex == 4}">
                <or:text key="listProjects.NoProjects.draft" />
            </c:when>
            <c:otherwise>
                <or:text key="listProjects.NoProjects.open" />
            </c:otherwise>
        </c:choose>
    </div>
</c:if>
