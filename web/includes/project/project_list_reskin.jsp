<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005-2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment renders the list of projects of selected status.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(){
            for (const dropdown of document.querySelectorAll(".custom-select-wrapper")) {
                dropdown.addEventListener('click', function () {
                    this.querySelector('.custom-select').classList.toggle('open');
                });
            }

            for (const option of document.querySelectorAll(".custom-option")) {
                if (option.classList.contains('selected')) {
                    const currentSelected = option.textContent;
                    const selectedSpan = option.closest('.custom-select').querySelector('.custom-select__trigger span');
                    if (selectedSpan.textContent == '') {
                        selectedSpan.textContent = currentSelected;
                    }
                }
                option.addEventListener('click', function (e) {
                    if (this.classList.contains('group')) {
                        e.preventDefault();
                        e.stopPropagation();
                    } else if (!this.classList.contains('selected')) {
                        this.parentNode.querySelector('.custom-option.selected').classList.remove('selected');
                        this.classList.add('selected');
                        this.closest('.custom-select').querySelector('.custom-select__trigger span').textContent = this.textContent;
                        document.getElementById('challengeCategory').value = this.getAttribute('data-value');
                        setTimeout(function(){
                            document.getElementById('challengeTypeForm').submit();
                        }, 100);
                    }
                });
            }

            window.addEventListener('click', function (e) {
                for (const select of document.querySelectorAll('.custom-select')) {
                    if (!select.contains(e.target)) {
                        select.classList.remove('open');
                    }
                }
            });
        });
</script>

<s:form id="challengeTypeForm" method="GET">
    <input type="hidden" name="scope" value="${scope}">
    <input type="hidden" id="challengeCategory" name="category" value="">
    <div class="custom-select-container">
        <div class="custom-select-wrapper">
            <div class="custom-select">
                <span class="custom-select__label">Challenge type</span>
                <div class="custom-select__trigger"><span></span>
                    <div class="arrow"></div>
                </div>
                <div class="custom-options">
                    <c:forEach items="${userProjectTypes}" var="type" varStatus="idxrType">
                        <span class="custom-option group">--- ${type.name} (${type.count}) ---</span>
                        <c:forEach items="${type.categories}" var="category" varStatus="idxrCategory">
                            <span class="custom-option ${(category.id == selectedCategoryId) ? 'selected' : ''}" data-value="${category.id}">
                                ${category.name} (${category.count})
                            </span>
                        </c:forEach>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</s:form>

<div class="projectList__inner">
    <table class="projectListTable" width="100%" cellpadding="0" cellspacing="0">
        <thead class="projectListTable__header projectListTable__header--left">
            <tr>
                <th><or:text key="listProjects.tblHeader.Project" /></th>
                <c:if test="${isMyProjects}">
                    <th><or:text key="listProjects.tblHeader.MyRole" /></th>
                </c:if>
                <th><or:text key="listProjects.tblHeader.Phase" /></th>
                <th><or:text key="listProjects.tblHeader.PhaseEndDate" /></th>
                <th><or:text key="listProjects.tblHeader.ProjectEndDate" /></th>
                <c:if test="${isMyProjects}">
                    <th><or:text key="listProjects.tblHeader.Deliverable" /></th>
                </c:if>
            </tr>
        </thead>
        <tbody ID="Out${idxrCategory.index}r" class="projectListTable__body">
            <c:if test="${projectCount != 0}">
                <c:forEach items="${projects}" var="project" varStatus="idxrProject">
                    <tr>
                        <td nowrap="nowrap" class="challengeName ${isMyProjects ? 'my' : 'all'}">
                            <div>
                                <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                                    ${project.allProperties['Project Name']}
                                </a>
                                <span>version ${project.allProperties['Project Version']}</span>
                            </div>
                        </td>
                        <c:if test="${isMyProjects}">
                            <td>${myRoles[idxrProject.index]}</td>
                        </c:if>
                        <c:set var="phase" value="${phases[idxrProject.index]}" />
                        <c:if test="${!(empty phase)}">
                            <td nowrap="nowrap"><or:text key="ProjectPhase.${fn:replace(phase[0].phaseType.name, ' ', '')}" /></td>
                            <td nowrap="nowrap">${orfn:displayDate(pageContext.request, phaseEndDates[idxrProject.index])}</td>
                            <td nowrap="nowrap">${orfn:displayDate(pageContext.request, projectEndDates[idxrProject.index])}</td>
                            <c:if test="${isMyProjects}">
                                <td nowrap="nowrap">${not empty myDeliverables[idxrProject.index] ? myDeliverables[idxrProject.index] : '-'}</td>
                            </c:if>
                        </c:if>
                        <c:if test="${empty phase}">
                            <td nowrap="nowrap">-</td>
                            <td nowrap="nowrap">-</td>
                            <td nowrap="nowrap">-</td>
                            <c:if test="${isMyProjects}">
                                <td nowrap="nowrap">-</td>
                            </c:if>
                        </c:if>
                    </tr>
                </c:forEach>
                <c:if test="${fn:length(projects) == 0}">
                    <tr>
                        <td class="noChallenges" colspan="${(isMyProjects) ? '7' : '5'}">
                            <or:text key="listProjects.NoProjects.Category" />
                        </td>
                    </tr>
                </c:if>
            </c:if>
            <c:if test="${projectCount == 0}">
                <tr>
                    <td class="noChallenges" colspan="${(isMyProjects) ? '7' : '5'}">
                        <c:choose>
                            <c:when test="${projectTabIndex == 4}">
                                <or:text key="listProjects.NoProjects.draft" />
                            </c:when>
                            <c:otherwise>
                                <or:text key="listProjects.NoProjects.open" />
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
    <c:if test="${fn:length(projects) > 0 && totalProjectCount > 0}">
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
    </c:if>
</div>
