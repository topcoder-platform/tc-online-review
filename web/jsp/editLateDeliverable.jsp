<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2011 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays form for editing a single selected late deliverable
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/late_deliverable_search.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/expand_collapse.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<or:url value='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="/js/or/ajax1.js"><!-- @ --></script>
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(){
            let avatar = document.querySelector('.webHeader__avatar a');
            let avatarImage = document.createElement('div');
            avatarImage.className = "webHeader__avatarImage";
            let twoChar = avatar.text.substring(0, 2);
            avatarImage.innerText = twoChar;
            avatar.innerHTML = avatarImage.outerHTML;
        });
    </script>
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(){
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
                const input = option.closest('.editReview__input').querySelector('input');
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
<body>
<jsp:include page="/includes/inc_header_reskin.jsp" />
<jsp:include page="/includes/project/project_tabs_reskin.jsp" />

<div class="content content--projectDetails">
    <div class="content__inner">
            <jsp:include page="/includes/review/review_project.jsp">
                <jsp:param name="hideScoreInfo" value="true" />
            </jsp:include>
            <div class="divider" style="margin-top: 0"></div>

        <div id="mainContent">
            <div class="projectDetails">
                <div class="projectDetails__sectionHeader">
                    <div class="projectDetails__title">
                        <or:text key="editLateDeliverable.title"/>
                    </div>
                </div>
                <s:form action="SaveLateDeliverable" 
                           onsubmit="return submitEditLateDeliverableForm(this);" 
                           method="post" namespace="/actions">
                    <input type="hidden" name="late_deliverable_id"  value="<or:fieldvalue field='late_deliverable_id' />" />
                    
                        <div id="globalMesssage"
                             style="display:${orfn:isErrorsPresent(pageContext.request) ? 'block' : 'none'}">
                            <div style="color:red;">
                                <or:text key="viewLateDeliverables.ValidationFailed" />
                            </div>
                            <s:actionerror escape="false" />
                        </div>
                    
                    <c:set var="lateDeliverable" value="${requestScope.lateDeliverable}"/>
                    <c:set var="project" value="${requestScope.project}"/>
                    
                    <c:set var="rowCount" value="0" />
                    <div class="projectDetails__sectionBody">
                        <table width="100%" cellspacing="0" cellpadding="0" style="border-collapse: collapse;"
                            class="editProjectDetailsTable">
                            <tbody class="editProjectDetailsTable__body">
                            <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td class="valueB"><or:text key="editLateDeliverable.LateDeliverableId.label"/>
                                    <div class="lateDeliverableValue">
                                        <c:out value="${lateDeliverable.id}"/>
                                    </div>
                                </td>
                                <td class="valueB"><or:text key="editLateDeliverable.LateDeliverableType.label"/>
                                <div class="lateDeliverableValue">
                                    <c:out value="${lateDeliverable.type.name}"/>
                                </div></td>
                            </tr>
                            <c:set var="rowCount" value="${rowCount + 1}" />

                            <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td width="9%" class="valueB">
                                    <or:text key="editLateDeliverable.ProjectName.label"/>
                                   <div class="lateDeliverableValue">
                                        <a class="lateDeliverableProjectLink" href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                                        <strong><c:out value="${project.allProperties['Project Name']}"/></strong> 
                                        <or:text key="global.version"/> 
                                        <c:out value="${project.allProperties['Project Version']}"/>
                                        </a>
                                    </div>
                                </td>
                                <td class="valueB"><or:text key="editLateDeliverable.DeliverableType.label"/>
                                <div class="lateDeliverableValue">
                                    <c:out value="${orfn:getDeliverableName(pageContext.request, lateDeliverable.deliverableId)}"/>
                                </div>
                                </td>
                            </tr>
                            <c:set var="rowCount" value="${rowCount + 1}" />

                            <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td nowrap="nowrap" class="valueB">
                                    <or:text key="editLateDeliverable.LateMember.label"/>
                                    <div class="lateDeliverableValue">
                                        <tc-webtag:handle coderId="${orfn:getUserId(lateDeliverable.resourceId)}" 
                                            context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" />
                                    </div>
                                </td>
                                 <c:if test='${lateDeliverable.deadline ne null}'>
                                    <td nowrap="nowrap" class="valueB">
                                        <or:text key="editLateDeliverable.Deadline.label"/>
                                        <div class="lateDeliverableValue">
                                            <c:choose>
                                            <c:when test="${lateDeliverable.compensatedDeadline ne null}">
                                                <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.compensatedDeadline)}"/>
                                                (compensated due to the premature end of the previous phase)
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.deadline)}"/>
                                            </c:otherwise>
                                        </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            </tr>
                            <c:if test='${lateDeliverable.delay ne null}'>
                                <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                                    <td nowrap="nowrap" class="valueB">
                                        <or:text key="editLateDeliverable.Delay.label"/>
                                        <div class="lateDeliverableValue">
                                            <c:out value="${orfn:displayDelay(lateDeliverable.delay)}"/>
                                        </div>
                                    </td>
                                    <td nowrap="nowrap" class="valueB">
                                    <or:text key="editLateDeliverable.Justified.label"/>
                                    <div class="lateDeliverableValue">
                                        <c:choose>
                                            <c:when test="${requestScope.isJustifiedEditable}">
                                            <div class="editReview__input">
                                                    <input type="hidden" name="justified" class="scoreInput">
                                                    <div class="custom-select-wrapper lateness">
                                                        <div class="custom-select grey">
                                                            <div class="custom-select__trigger"><span></span>
                                                                <div class="arrow"></div>
                                                            </div>
                                                            <div class="custom-options">
                                                                <c:set var="OR_FIELD_TO_SELECT" value="justified"/>
                                                                <span class="custom-option custom-option-grey" data-value="false" <or:selected value="false"/>><or:text key="global.answer.No"/></span>
                                                                <span class="custom-option custom-option-grey" data-value="true" <or:selected value="true"/>><or:text key="global.answer.Yes"/></span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:when test="${requestScope.isJustifiedEditable}">
                                                <select class="inputBox" name="justified"><c:set var="OR_FIELD_TO_SELECT" value="justified"/>
                                                    <option value="false" <or:selected value="false"/>><or:text key="global.answer.No"/></option>
                                                    <option value="true" <or:selected value="true"/>><or:text key="global.answer.Yes"/></option>
                                                </select>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${lateDeliverable.forgiven}">
                                                        <or:text key="global.answer.Yes"/>
                                                    </c:when>
                                                    <c:otherwise><or:text key="global.answer.No"/></c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                                </tr>
                            </c:if>
                            <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td nowrap="nowrap" class="valueB">
                                    <or:text key="editLateDeliverable.Explanation.label"/>
                                    <div class="lateDeliverableValue">
                                        <c:choose>
                                            <c:when test="${requestScope.isExplanationEditable}">
                                                <textarea rows="5" class="inputTextBox" name="explanation"><or:fieldvalue field="explanation" /></textarea>
                                                <div id="explanation_serverside_validation" class="error">
                                                    <s:fielderror escape="false"><s:param>explanation</s:param></s:fielderror>
                                                </div>
                                                <div id="explanation_validation_msg" style="display:none;" class="error"></div>
                                            </c:when>
                                            <c:when test="${lateDeliverable.explanation ne null}">
                                                <c:choose>
                                                    <c:when test="${lateDeliverable.explanationDate ne null}">
                                                        By <tc-webtag:handle coderId="${orfn:getUserId(lateDeliverable.resourceId)}" 
                                                            context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" />
                                                        at <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.explanationDate)}"/>
                                                        <br/><br/>
                                                    </c:when>
                                                </c:choose>
                                                ${orfn:htmlEncode(lateDeliverable.explanation)}
                                            </c:when>
                                            <c:otherwise><or:text key="NotAvailable"/></c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                                <td nowrap="nowrap" class="valueB">
                                    <or:text key="editLateDeliverable.Response.label"/>
                                    <div class="lateDeliverableValue">
                                        <c:choose>
                                            <c:when test="${requestScope.isResponseEditable}">
                                                <textarea rows="5" class="inputTextBox" name="response"><or:fieldvalue field="response" /></textarea>
                                                <div id="response_serverside_validation" class="error">
                                                    <s:fielderror escape="false"><s:param>response</s:param></s:fielderror>
                                                </div>
                                                <div id="response_validation_msg" style="display:none;" class="error"></div>
                                            </c:when>
                                            <c:when test="${lateDeliverable.response ne null}">
                                                <c:choose>
                                                    <c:when test="${(lateDeliverable.responseUser ne null) and (lateDeliverable.responseDate ne null)}">
                                                        By <tc-webtag:handle coderId="${lateDeliverable.responseUser}"
                                                            context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" />
                                                        at <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.responseDate)}"/>
                                                        <br/><br/>
                                                    </c:when>
                                                </c:choose>
                                                ${orfn:htmlEncode(lateDeliverable.response)}
                                            </c:when>
                                            <c:otherwise><or:text key="NotAvailable"/></c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </s:form>
            </div>
        </div>
        <c:if test="${requestScope.isFormSubmittable}">
            <div class="saveChanges__button">
                <button id="saveChanges" form="SaveLateDeliverable" value="Submit" class="saveChanges__save"><or:text key='btnSaveChanges.alt' /></button>
                <input type="hidden" property="saveLateDeliverableBtn" />
            </div>
        </c:if>
    </div>
    <jsp:include page="/includes/inc_footer_reskin.jsp" />
</div>
</body>
<script type="text/javascript">
    var editLateDeliverableForm = document.querySelector('form');
    editLateDeliverableForm.addEventListener('submit', function() {
        document.getElementById("saveChanges").disabled = true;
    }, false);
</script>
</html>