<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="now" value="<%=new Date()%>"/>
<fmt:formatDate value="${now}" pattern="z" var="currentTimezoneLabel"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Online Review - Late Deliverables</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/phasetabs.css" />

    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/parseDate.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/late_deliverable_search.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/expand_collapse.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<or:url value='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="/js/or/ajax1.js"><!-- @ --></script>

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(){
            let avatar = document.querySelector('.webHeader__avatar a');
            let avatarImage = document.createElement('div');
            avatarImage.className = "webHeader__avatarImage";
            let twoChar = avatar.text.substring(0, 2);
            avatarImage.innerText = twoChar;
            avatar.innerHTML = avatarImage.outerHTML;

            let accordion = document.getElementsByClassName("lateDeliverables__accordion");
            for (let i = 0; i < accordion.length; i++) {
                accordion[i].addEventListener("click", function() {
                    this.classList.toggle("lateDeliverables__accordion--collapse");
                    let section = document.getElementsByClassName("lateDeliverables__advancedBody")[i];
                    if (section.style.display === "none") {
                        section.style.display = "block";
                    } else {
                        section.style.display = "none";
                    }
                });
            }

            for (const dropdown of document.querySelectorAll(".custom-select-wrapper")) {
                dropdown.addEventListener('click', function () {
                    this.querySelector('.custom-select').classList.toggle('open');
                });
            }

            for (const options of document.querySelectorAll('.custom-options')) {
                let selected = options.querySelector('.custom-option[selected]');
                if (!selected) {
                    let option = options.querySelector('.custom-option');
                    option.selected = true;
                    option.classList.add('selected');
                } else {
                    selected.classList.add('selected');
                }
            }

            for (const option of document.querySelectorAll(".custom-option")) {
                const input = option.closest('.lateDeliverables__input').querySelector('input');
                const selectedSpan = option.closest('.custom-select').querySelector('.custom-select__trigger span');
                if (option.classList.contains('selected')) {
                    const currentSelected = option.textContent;
                    if (selectedSpan.textContent == '') {
                        selectedSpan.textContent = currentSelected;
                    }
                    input.value = option.getAttribute('data-value');
                }
                option.addEventListener('click', function (e) {
                    if (!this.classList.contains('selected')) {
                        this.parentNode.querySelector('.custom-option.selected').classList.remove('selected');
                        this.classList.add('selected');
                        selectedSpan.textContent = this.textContent;
                        input.value = this.getAttribute('data-value');
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

</head>
<body onload="colorFormFields();">

<jsp:include page="/includes/inc_header_reskin.jsp" />

<jsp:include page="/includes/project/project_tabs_reskin.jsp" />

<div class="content">
    <div class="content__inner">
        <div class="lateDeliverables">
            <h1 class="lateDeliverables__header">Late Deliverables</h1>
        </div>

        <div class="divider"></div>

        <div style="position: relative; width: 100%;">
            <s:form action="ViewLateDeliverables" onsubmit="return submit_form(this, true);"
                       method="GET" id="ViewLateDeliverablesForm" namespace="/actions">
                    <div id="globalMesssage"
                         style="display:${orfn:isErrorsPresent(pageContext.request) ? 'block' : 'none'}">
                        <div style="color:red;">
                            <or:text key="viewLateDeliverables.ValidationFailed" />
                        </div>
                        <s:actionerror escape="false" />
                    </div>

                <div class="lateDeliverables__basic">
                    <div class="lateDeliverables__title"><or:text key="viewLateDeliverables.SearchForm.title"/></div>

                    <div class="lateDeliverables__row">
                        <div class="lateDeliverables__input">
                            <input type="hidden" name="project_categories" data-reset="0">
                            <div class="lateDeliverables__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select">
                                        <span class="custom-select__label"><or:text key="viewLateDeliverables.ProjectCategory.Label" /></span>
                                        <div class="custom-select__trigger"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="project_categories"/>
                                            <span class="custom-option" data-value="0" <or:selected value="0"/>><or:text key="global.any" def="0"/></span>
                                            <c:forEach items="${projectCategories}" var="category">
                                                 <span class="custom-option" data-value="${category.id}" <or:selected value="${category.id}"/>><or:text key="ProjectCategory.${fn:replace(category.name, ' ', '')}"  def="${category.id}"/></span>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="project_categories_validation_msg" style="display:none;" class="error"></div>
                        </div>

                        <div class="lateDeliverables__input">
                            <input type="hidden" name="project_statuses" data-reset="0">
                            <div class="lateDeliverables__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select">
                                        <span class="custom-select__label"><or:text key="viewLateDeliverables.ProjectStatus.Label" /></span>
                                        <div class="custom-select__trigger"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="project_statuses"/>
                                            <span class="custom-option" data-value="0" <or:selected value="0"/>><or:text key="global.any" def="0"/></span>
                                            <c:forEach items="${projectStatuses}" var="status">
                                                <span class="custom-option" data-value="${status.id}" <or:selected value="${status.id}"/>><or:text key="ProjectStatus.${fn:replace(status.name, ' ', '')}" def="${status.id}"/></span>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="project_statuses_validation_msg" style="display:none;" class="error"></div>
                        </div>

                        <div class="lateDeliverables__input">
                            <input type="hidden" name="deliverable_types" data-reset="0">
                            <div class="lateDeliverables__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select">
                                        <span class="custom-select__label"><or:text key="viewLateDeliverables.DeliverableType.Label" /></span>
                                        <div class="custom-select__trigger"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="deliverable_types"/>
                                            <c:forEach var="entry" items="${deliverableTypes}">
                                                <span class="custom-option" data-value="${entry.value}"  <or:selected value="${entry.value}"/>><or:text key="DeliverableType.${fn:replace(entry.key, ' ', '')}" def="${entry.key}"/></span>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="deliverable_types_validation_msg" style="display:none;" class="error"></div>
                        </div>
                    </div>

                    <div class="lateDeliverables__row">
                        <div class="lateDeliverables__input lateDeliverables__inputText">
                            <label for="project_id"><or:text key="viewLateDeliverables.ProjectID.Label" /></label>
                            <input type="text" name="project_id" id="project_id" class="inputBox"
                                       onfocus="this.value = getTextValueOnFocus(this, true);"
                                       onblur="this.value = getTextValueOnFocus(this, false);" value="<or:fieldvalue field='project_id' />" data-reset="" />
                            <div id="project_id_validation_msg" style="display:none;" class="error"></div>
                            <div id="project_id_serverside_validation" class="error"><s:fielderror escape="false"><s:param>project_id</s:param></s:fielderror></div>
                        </div>

                        <div class="lateDeliverables__input lateDeliverables__inputText">
                            <label for="handle"><or:text key="viewLateDeliverables.LateMemberHandle.Label" /></label>
                            <input type="text" name="handle" id="handle" class="inputBox"
                                       onfocus="this.value = getTextValueOnFocus(this, true);"
                                       onblur="this.value = getTextValueOnFocus(this, false);" value="<or:fieldvalue field='handle' />" data-reset="" />
                            <div id="handle_serverside_validation" class="error"><s:fielderror escape="false"><s:param>handle</s:param></s:fielderror></div>
                        </div>

                        <div class="lateDeliverables__input">
                            <input type="hidden" name="justified" data-reset="Any">
                            <div class="lateDeliverables__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select">
                                        <span class="custom-select__label"><or:text key="viewLateDeliverables.JustifiedStatus.Label" /></span>
                                        <div class="custom-select__trigger"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="justified"/>
                                            <span class="custom-option" data-value="Any" <or:selected value="Any"/>>Any</span>
                                            <span class="custom-option" data-value="Justified" <or:selected value="Justified"/>>Justified</span>
                                            <span class="custom-option" data-value="Not justified" <or:selected value="Not justified"/>>Not justified</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="lateDeliverables__advanced">
                    <div class="lateDeliverables__advancedHeader">
                        <div class="lateDeliverables__title"><or:text key="viewLateDeliverables.AdvancedSearchParameters"/></div>
                        <div class="lateDeliverables__accordion <c:if test="${not requestScope.hasAnyAdvancedSearchParameter}">lateDeliverables__accordion--collapse</c:if>"></div>
                    </div>
                    <div class="lateDeliverables__advancedBody" <c:if test="${not requestScope.hasAnyAdvancedSearchParameter}">style="display:none"</c:if>>
                        <div class="lateDeliverables__row">
                            <div class="lateDeliverables__input">
                                <input type="hidden" name="tcd_project_id" data-reset="">
                                <div class="lateDeliverables__selection">
                                    <div class="custom-select-wrapper">
                                        <div class="custom-select">
                                            <span class="custom-select__label"><or:text key="viewLateDeliverables.CockpitProject.Label"/></span>
                                            <div class="custom-select__trigger"><span></span>
                                                <div class="arrow"></div>
                                            </div>
                                            <div class="custom-options">
                                                <c:set var="OR_FIELD_TO_SELECT" value="tcd_project_id"/>
                                                <span class="custom-option" data-value="" <or:selected value=""/>><or:text key="global.any" def=""/></span>
                                                <c:forEach var="entry" items="${requestScope.cockpitProjects}">
                                                    <span class="custom-option" data-value="${entry.id}" <or:selected value="${entry.id}"/>>
                                                        <c:out value="${entry.name}"/></span>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="lateDeliverables__input">
                                <input type="hidden" name="explanation_status" data-reset="">
                                <div class="lateDeliverables__selection">
                                    <div class="custom-select-wrapper">
                                        <div class="custom-select">
                                            <span class="custom-select__label"><or:text key="viewLateDeliverables.ExplanationStatus.Label"/></span>
                                            <div class="custom-select__trigger"><span></span>
                                                <div class="arrow"></div>
                                            </div>
                                            <div class="custom-options">
                                                <c:set var="OR_FIELD_TO_SELECT" value="explanation_status"/>
                                                <span class="custom-option" data-value="" <or:selected value=""/>><or:text key="global.any" def=""/></span>
                                                <span class="custom-option" data-value="true" <or:selected value="true"/>><or:text key="global.explained" def="true"/></span>
                                                <span class="custom-option" data-value="false" <or:selected value="false"/>><or:text key="global.notExplained" def="false"/></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="lateDeliverables__input">
                                <input type="hidden" name="response_status" data-reset="">
                                <div class="lateDeliverables__selection">
                                    <div class="custom-select-wrapper">
                                        <div class="custom-select">
                                            <span class="custom-select__label"><or:text key="viewLateDeliverables.ResponseStatus.Label"/></span>
                                            <div class="custom-select__trigger"><span></span>
                                                <div class="arrow"></div>
                                            </div>
                                            <div class="custom-options">
                                                <c:set var="OR_FIELD_TO_SELECT" value="response_status"/>
                                                <span class="custom-option" data-value="" <or:selected value=""/>><or:text key="global.any" def=""/></span>
                                                <span class="custom-option" data-value="true" <or:selected value="true"/>><or:text key="global.responded" def="true"/></span>
                                                <span class="custom-option" data-value="false" <or:selected value="false"/>><or:text key="global.notResponded" def="false"/></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="lateDeliverables__row">
                            <div class="lateDeliverables__input lateDeliverables__inputText">
                                <label for="min_deadline"><or:text key="viewLateDeliverables.DeadlineMinDate.Label"/></label>
                                <input type="text" name="min_deadline"
                                           class="inputBoxDate" id="min_deadline"
                                           onfocus="this.value = getDateValueOnFocus(this, true);"
                                           onblur="this.value = getDateValueOnFocus(this, false);" value="<or:fieldvalue field='min_deadline' />" data-reset="" />
                                <div id="min_deadline_validation_msg" style="display:none;" class="error"></div>
                            </div>

                            <div class="lateDeliverables__input lateDeliverables__inputText">
                                <label for="max_deadline"><or:text key="viewLateDeliverables.DeadlineMaxDate.Label"/></label>
                                <input type="text" name="max_deadline"
                                           class="inputBoxDate" id="max_deadline"
                                           onfocus="this.value = getDateValueOnFocus(this, true);"
                                           onblur="this.value = getDateValueOnFocus(this, false);" value="<or:fieldvalue field='max_deadline' />" data-reset="" />
                                <div id="max_deadline_validation_msg" style="display:none;" class="error"></div>
                                <div id="deadline_serverside_validation" class="error">
                                    <s:fielderror escape="false"><s:param>max_deadline</s:param></s:fielderror></div>
                            </div>

                            <div class="lateDeliverables__input">
                                <input type="hidden" name="late_deliverable_type" data-reset="">
                                <div class="lateDeliverables__selection">
                                    <div class="custom-select-wrapper">
                                        <div class="custom-select">
                                            <span class="custom-select__label"><or:text key="viewLateDeliverables.LateDeliverableType.Label"/></span>
                                            <div class="custom-select__trigger"><span></span>
                                                <div class="arrow"></div>
                                            </div>
                                            <div class="custom-options">
                                                <c:set var="OR_FIELD_TO_SELECT" value="late_deliverable_type"/>
                                                <span class="custom-option" data-value="" <or:selected value=""/>><or:text key="global.any" def=""/></span>
                                                <c:forEach var="entry" items="${lateDeliverableTypes}">
                                                    <span class="custom-option" data-value="${entry.id}" <or:selected value="${entry.id}"/>><or:text key="LateDeliverableType.${fn:replace(entry.name, ' ', '')}" def="${entry.id}"/></span>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="divider"></div>

                <div class="lateDeliverables__button">
                    <button class="lateDeliverables__clear" onclick="submitForm=false;"><or:text key='btnClear.alt' /></button>
                    <button class="lateDeliverables__search" onclick="submitForm=true;"><or:text key='btnSearch.alt' /></button>
                </div>

            </s:form>

            <c:if test="${not empty showSearchResultsSection}">
            <c:choose>
                <c:when test="${ projectMap}">
                    <div class="lateDeliverables__noMatch">
                        <or:text key="viewLateDeliverables.SearchResults.NoMatch" />
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="lateDeliverables__results">
                        <div class="lateDeliverables__resultHeader">
                            <div class="lateDeliverables__title">
                                <or:text key="viewLateDeliverables.SearchResults.title" />
                            </div>

                            <div class="lateDeliverables__expand">
                                <a href="javascript:void(0)" onclick="return expandAll()"><or:text key="global.expandAll" /></a>
                                <a href="javascript:void(0)" onclick="return collapseAll()"><or:text key="global.collapseAll" /></a>
                            </div>
                        </div>

                    <table class="projectListTable" style="border-collapse: collapse;" cellpadding="0" cellspacing="0" width="100%">
                        <thead class="projectListTable__header searchResult__header">
                            <tr>
                                <th width="55%"><or:text key="viewLateDeliverables.SearchResults.column.ProjectName" /></th>
                                <th nowrap="nowrap"><or:text key="viewLateDeliverables.SearchResults.column.ProjectCategory" /></th>
                                <th nowrap="nowrap"><or:text key="viewLateDeliverables.SearchResults.column.ProjectStatus" /></th>
                            </tr>
                        </thead>
                        <tbody class="projectListTable__body">
                        <c:if test="${empty projectMap}">
                            <tr>
                                <td class="noChallenges" colspan="3"><or:text key="viewLateDeliverables.SearchResults.NoMatch" /></td>
                            </tr>
                        </c:if>
                        <c:forEach items="${projectMap}" var="entry" varStatus="idxProj">
                        <c:set var="projectId" value="${entry.key}" />
                        <c:set var="project" value="${entry.value}" />
                        <tr>
                            <td class="challengeName">
                                <div>
                                    <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                                        ${project.allProperties['Project Name']}
                                    </a>
                                    <span>version ${project.allProperties['Project Version']}</span>
                                </div>
                            </a>
                            </td>

                            <td>${project.projectCategory.name}</td>
                            <td>
                                <div class="projectStatus">
                                    <div class="projectStatus__status">
                                        ${project.projectStatus.name}
                                    </div>
                                    <a onclick="return expcollHandler(this)" href="javascript:void(0)" id="Out${idxProj.index}" class="Outline">
                                        <img id="Out${idxProj.index}i" class="Outline" border="0" src="/i/reskin/latedeliverable-chevron.svg" width="9" height="9" style="margin-right:5px;" />
                                    </a>
                                </div>
                            </td>
                        </tr>

                        <tr id="Out${idxProj.index}r" style="display:none">
                            <td colspan="3" class="lateDeliverableTable__wrapper" style="border-bottom: none;">
                                <table class="lateDeliverableTable" cellpadding="0" cellspacing="0" width="100%">
                                    <thead class="lateDeliverableTable__header">
                                        <tr>
                                            <th nowrap="nowrap" width="10%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Handle" /></th>
                                            <th nowrap="nowrap" width="30%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Deliverable" /></th>
                                            <th nowrap="nowrap" width="20%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Deadline" /></th>
                                            <th nowrap="nowrap" width="20%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Delay" /></th>
                                            <th nowrap="nowrap" width="15%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Justified" /></th>
                                            <th nowrap="nowrap" width="5%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Edit"/></th>
                                        </tr>
                                    </thead>
                                    <tbody class="lateDeliverableTable__body">
                                        <c:set var="lateDeliverables" value="${groupedLateDeliverables[projectId]}" />
                                        <c:forEach items="${lateDeliverables}" var="lateDeliverable" varStatus="status">
                                        <tr>
                                            <td width="20%"><tc-webtag:handle coderId="${orfn:getUserId(lateDeliverable.resourceId)}" context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" /></td>
                                            <c:choose>
                                                <c:when test="${lateDeliverable.type.id == 1}">
                                                    <td width="20%">${orfn:getDeliverableName(pageContext.request, lateDeliverable.deliverableId)}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td width="20%"><or:text key="LateDeliverableType.RejectedFinalFix" /></td>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${lateDeliverable.deadline != null}">
                                                    <td width="20%">${orfn:displayDate(pageContext.request, lateDeliverable.deadline)}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td width="20%">N/A</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${lateDeliverable.delay != null}">
                                                    <td width="20%">${orfn:displayDelay(lateDeliverable.delay)}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td width="20%">N/A</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td width="15%">${lateDeliverable.forgiven ? 'Yes' : 'No'}</td>
                                            <td width="5%">
                                                <a href="<or:url value='/actions/EditLateDeliverable?late_deliverable_id=${lateDeliverable.id}' />">
                                                    <or:text key="global.Edit"/></a>
                                            </td>
                                        </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${totalProjectCount > 0}">
                        <div class="lateDeliverablePagination">
                            <div class="info">
                                Showing 1 to ${totalProjectCount} of ${totalProjectCount} projects
                            </div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose> 
                </div> 
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="/includes/inc_footer_reskin.jsp" />

</body>
</html>
