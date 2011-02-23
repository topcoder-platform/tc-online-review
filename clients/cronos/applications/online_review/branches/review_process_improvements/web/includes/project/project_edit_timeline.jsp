<%--
  - Author: isv
  - Version: 1.3
  - Copyright (C) 2004 - 2010 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the form input elements group for editing the timeline and other
  - parameters for single project phase.
  -
  - Version 1.1 (Online Review End of Project Analysis v1.0) changes: Added logic for supporting Post-Mortem phase.
  - Updated logic for supporting Approval phase.
  -
  - Version 1.2 (Impersonation Login Release assembly) changes: Disabled all input fields for phases which are closed.
  -
  - Version 1.3 (Specification Review Part 1 assembly) changes: Removed radio buttons for selecting the type of
  - phase start
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>

<c:set var="currentTime" value="<%=new java.util.Date()%>"/>
<fmt:formatDate value="${currentTime}" var="currentTimezone" pattern="z"/>
<%-- If creating a new project, show "Create Timeline" table --%>
<c:if test="${newProject}">
    <table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse;">
        <tr>
            <td class="title" width="1%" nowrap="nowrap"><bean:message key="editProject.CreateTimeline.title" /></td>
            <td class="title" width="99%"><!-- @ --></td>
        </tr>
        <tr class="light">
            <td class="value" width="2%" nowrap="nowrap"><b><bean:message key="editProject.CreateTimeline.UseTemplate" /></b></td>
            <td class="value">
                <html:select styleClass="inputBox" property="template_name" style="width:100px;">
                    <html:option value="">Select</html:option>
                    <c:forEach var="templateName" items="${phaseTemplateNames}">
                        <html:option value="${templateName}">${templateName}</html:option>
                    </c:forEach>
                </html:select>
                <html:link href="javascript:loadTimelineTemplate();">
                    <html:img src="/i/or/bttn_load_template.gif" imageName="load_template" styleId="load_template" />
                </html:link>
            </td>
        </tr>
    </table><br />
</c:if>

<table class="scorecard" id="timeline_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
    <tr>
        <c:if test="${not newProject}">
            <td class="headerC"><bean:message key="editProject.Phases.CurrentPhase" /></td>
        </c:if>
        <td class="header"><bean:message key="editProject.Phases.PhaseName" /></td>
        <td class="header"><bean:message key="editProject.Phases.PhaseStart" /></td>
        <td class="header"><bean:message key="editProject.Phases.PhaseEnd" /></td>
        <td class="header" nowrap="nowrap"><bean:message key="editProject.Phases.Duration" /></td>
        <td class="header"><bean:message key="editProject.Phases.Delete" /></td>
    </tr>

    <%-- PHASE ROW GOES HERE --%>
    <c:set var="arePhaseDependenciesEditable" value="${requestScope.arePhaseDependenciesEditable}"/>
    <input type="hidden" name="arePhaseDependenciesEditable" value="${arePhaseDependenciesEditable}"/>
    <c:set var="approvalPhaseMet" value="${false}"/>
    <c:set var="disableApprovalReviewerNumberInput" value="${false}"/>
    <c:forEach var="phaseIdx" begin="0" end="${fn:length(projectForm.map['phase_id']) - 1}">
        <c:set var="isPhaseClosed" value="${not (projectForm.map['phase_can_open'][phaseIdx] || projectForm.map['phase_can_close'][phaseIdx])}"/>
        <c:set var="isFixedStartTimeSet" value="${projectForm.map['phase_start_by_fixed_time'][phaseIdx]}"/>
        <c:set var="isPhaseDependencySet" value="${projectForm.map['phase_start_by_phase'][phaseIdx]}"/>
        <c:if test="${phaseIdx eq 0}">
            <tr class="dark" style="display: none;" id="phase_row_template">
        </c:if>
        <c:if test="${phaseIdx ne 0}">
            <tr class="dark" id="${projectForm.map['phase_js_id'][phaseIdx]}"
                style="${projectForm.map['phase_action'][phaseIdx] eq 'delete' ? 'display:none' : ''}" >
        </c:if>
        <c:if test="${not newProject}">
            <td class="valueC">
                <html:hidden property="phase_can_open[${phaseIdx}]" disabled="${isPhaseClosed}"/>
                <html:hidden property="phase_can_close[${phaseIdx}]" disabled="${isPhaseClosed}"/>
                <c:if test="${not isPhaseClosed}">
                    <html:img onclick="javascript:openOrClosePhase(this.parentNode.parentNode, 'open_phase');" border="0"
                        srcKey="editProject.Phases.OpenPhase.img" altKey="editProject.Phases.OpenPhase.alt" imageName="open_phase_img"
                        style="${projectForm.map['phase_can_open'][phaseIdx] ? 'cursor:hand;' : 'display:none;'}" /><html:img
                        onclick="javascript:openOrClosePhase(this.parentNode.parentNode, 'close_phase');" imageName="close_phase_img"
                        srcKey="editProject.Phases.ClosePhase.img" altKey="editProject.Phases.ClosePhase.alt" border="0"
                        style="${projectForm.map['phase_can_close'][phaseIdx] ? 'cursor:hand;' : 'display:none;'}" /></td>
                </c:if>
        </c:if>
            <td class="valueB" nowrap="nowrap">
                <span name="phase_name_text">${projectForm.map['phase_name'][phaseIdx]}</span>
                &#160; <span name="phase_number_text">${projectForm.map['phase_number'][phaseIdx]}</span>
            </td>
            <td class="value" nowrap="nowrap">
                <input type="hidden" name="isPhaseClosed[${phaseIdx}]" value="${isPhaseClosed}"/>
                <html:hidden property="phase_type[${phaseIdx}]" disabled="${isPhaseClosed}" />
                <html:hidden property="phase_id[${phaseIdx}]" disabled="${isPhaseClosed}" />
                <html:hidden property="phase_js_id[${phaseIdx}]" />
                <html:hidden property="phase_action[${phaseIdx}]" disabled="${isPhaseClosed}" />
                <html:hidden property="phase_name[${phaseIdx}]" disabled="${isPhaseClosed}" />
                <html:hidden property="phase_number[${phaseIdx}]" disabled="${isPhaseClosed}" />

                <c:if test="${not arePhaseDependenciesEditable and not isPhaseClosed}">
                    <html:hidden property="phase_start_by_fixed_time[${phaseIdx}]"/>
                </c:if>
                <input type="hidden" name="isPhaseFixedStartTimeEnabled[${phaseIdx}]"
                       value="${projectForm.map['phase_start_by_fixed_time'][phaseIdx]}"/>
                <html:checkbox property="phase_start_by_fixed_time[${phaseIdx}]"
                               disabled="${isPhaseClosed or not arePhaseDependenciesEditable}"
                               onclick="return fixedStartTimeBoxChanged(this, ${phaseIdx})"/>
                <html:text onblur="JavaScript:this.value=getDateString(this.value);"
                           styleClass="inputBoxDate" property="phase_start_date[${phaseIdx}]"
                           disabled="${isPhaseClosed or not isFixedStartTimeSet}"/>
                <html:text onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);"
                           styleClass="inputBoxTime" property="phase_start_time[${phaseIdx}]"
                           disabled="${isPhaseClosed or not isFixedStartTimeSet}"/>

                <c:out value="${currentTimezone}"/><br />

                <c:if test="${not arePhaseDependenciesEditable and not isPhaseClosed}">
                    <html:hidden property="phase_start_by_phase[${phaseIdx}]"/>
                    <html:hidden property="phase_start_phase[${phaseIdx}]"/>
                    <html:hidden property="phase_start_when[${phaseIdx}]"/>
                    <html:hidden property="phase_start_plusminus[${phaseIdx}]"/>
                    <html:hidden property="phase_start_amount[${phaseIdx}]"/>
                    <html:hidden property="phase_start_dayshrs[${phaseIdx}]"/>
                </c:if>
                <input type="hidden" name="isPhaseDependencySet[${phaseIdx}]"
                       value="${projectForm.map['phase_start_by_phase'][phaseIdx]}"/>
                <html:checkbox property="phase_start_by_phase[${phaseIdx}]"
                               disabled="${isPhaseClosed or not arePhaseDependenciesEditable}"
                               onclick="return phaseStartByPhaseBoxChanged(this, ${phaseIdx})"/>
                <bean:message key="editProject.Phases.When" />
                <div style="margin-left: 20px;">
                <html:select styleClass="inputBox" property="phase_start_phase[${phaseIdx}]" style="width:150px;"
                             disabled="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">
                    <html:option key="editProject.Phases.SelectPhase" value="" />
                    <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                        <c:if test="${phaseIdx ne i}">
                            <html:option value="${projectForm.map['phase_js_id'][i]}">${projectForm.map['phase_name'][i]}</html:option>
                        </c:if>
                    </c:forEach>
                </html:select>
                <br />
                <html:select styleClass="inputBox" property="phase_start_when[${phaseIdx}]"
                             disabled="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">
                    <html:option key="editProject.Phases.Starts" value="starts" />
                    <html:option key="editProject.Phases.Ends" value="ends" />
                </html:select>
                <br />
                <html:select styleClass="inputBox" property="phase_start_plusminus[${phaseIdx}]"
                             disabled="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">
                    <html:option value="plus">+</html:option>
                    <html:option value="minus">-</html:option>
                </html:select>
                <br />
                <html:text styleClass="inputBox" property="phase_start_amount[${phaseIdx}]" style="width:30px;"
                           disabled="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}"/>
                <br />
                <html:select styleClass="inputBox" property="phase_start_dayshrs[${phaseIdx}]"
                             disabled="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">
                    <html:option key="editProject.Phases.Days" value="days" />
                    <html:option key="editProject.Phases.Hrs" value="hrs" />
                    <html:option key="editProject.Phases.Mins" value="mins" />
                </html:select>
                </div>
                <div name="start_date_validation_msg" class="error" style="display:none"></div>
            </td>
            <td class="value" nowrap="nowrap">
                <html:radio property="phase_use_duration[${phaseIdx}]" value="false" disabled="${isPhaseClosed}"/>
                <html:text onblur="JavaScript:this.value=getDateString(this.value);" styleClass="inputBoxDate" property="phase_end_date[${phaseIdx}]" disabled="${isPhaseClosed}"/>
                <html:text onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" styleClass="inputBoxTime" property="phase_end_time[${phaseIdx}]" disabled="${isPhaseClosed}"/>
                <c:out value="${currentTimezone}"/>
                <div name="end_date_validation_msg" class="error" style="display:none"></div>
            </td>
            <td class="value">
                <html:radio property="phase_use_duration[${phaseIdx}]" value="true" disabled="${isPhaseClosed}"/>
                <html:text styleClass="inputBoxDuration" property="phase_duration[${phaseIdx}]"
                           disabled="${isPhaseClosed}"/>
                <div name="duration_validation_msg" class="error" style="display:none"></div>
            </td>

            <td class="value">
                <c:choose>
                    <c:when test="${isPhaseClosed or not arePhaseDependenciesEditable}">&nbsp;</c:when>
                    <c:otherwise>
                        <html:img srcKey="editProject.Phases.DeletePhase.img"
                            altKey="editProject.Phases.DeletePhase.alt"
                            onclick="deletePhase(this.parentNode.parentNode);" style="cursor:hand;" />
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>

        <%-- PHASE CRITERIA ROWS GO HERE --%>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_required_registrations'][phaseIdx])}">
            <tr class="highlighted" ${(phaseIdx eq 0) ? 'id="required_registrations_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.RequiredRegistrations.beforeInput" />
                    <%-- TODO: Set default value in Action --%>
                    <html:text style="width:30px;text-align:right;" styleClass="inputBox" disabled="${isPhaseClosed}"
                        size="30" property="phase_required_registrations[${phaseIdx}]" />
                    &#160;<bean:message key="editProject.Phases.Criteria.RequiredRegistrations.afterInput" />
                    <br /><bean:message key="editProject.Phases.Criteria.RequiredRegistrations.note" /></td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_required_submissions'][phaseIdx])}">
            <tr class="highlighted" ${(phaseIdx eq 0) ? 'id="required_submissions_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.RequiredSubmissions.beforeInput" />
                    <html:text styleClass="inputBox" property="phase_required_submissions[${phaseIdx}]" style="width:30px;text-align:right;" disabled="${isPhaseClosed}" />
                    <bean:message key="editProject.Phases.Criteria.RequiredSubmissions.afterInput" /><br />
                    <html:checkbox styleId="manualScreeningCheckbox${phaseIdx}" property="phase_manual_screening[${phaseIdx}]"  disabled="${isPhaseClosed}"/><label
                        for="manualScreeningCheckbox${phaseIdx}"><bean:message key="editProject.Phases.Criteria.RequiredSubmissions.ManualScreening" /></label>
                </td>
            </tr>
        </c:if>

        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_scorecard'][phaseIdx] and projectForm.map['phase_name'][phaseIdx] eq 'Screening')}">
            <c:if test="${phaseIdx eq 0}">
                <tr class="highlighted" id="screening_scorecard_row_template" style="display: none;">
            </c:if>
            <c:if test="${phaseIdx ne 0}">
                <tr class="highlighted">
            </c:if>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4"><bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" disabled="${isPhaseClosed}">
                        <c:forEach items="${screeningScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                                <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select>
                    <script type="text/javascript">
                        <!--
                         screeningScorecardNodes[screeningScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_scorecard'][phaseIdx] and projectForm.map['phase_name'][phaseIdx] eq 'Review')}">
            <tr class="highlighted" ${(phaseIdx eq 0) ? 'id="review_scorecard_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />
                    <html:text style="width:30px;text-align:right;" styleClass="inputBox" disabled="${isPhaseClosed}"
                        size="30" property="phase_required_reviewers[${phaseIdx}]" />
                    &#160;<bean:message key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br />
                    <bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" disabled="${isPhaseClosed}">

                        <c:forEach items="${reviewScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          or (not newProject && project.projectCategory.id == scorecard.category)
                                          or projectCategoriesMap[scorecard.category].projectType.generic}">
                            <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select>
                    <script type="text/javascript">
                        <!--
                         reviewScorecardNodes[reviewScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_scorecard'][phaseIdx] and projectForm.map['phase_name'][phaseIdx] eq 'Secondary Reviewer Review')}">
            <tr class="highlighted" ${(phaseIdx eq 0) ? 'id="review_scorecard_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />
                    <html:text style="width:30px;text-align:right;" styleClass="inputBox" disabled="${isPhaseClosed}"
                        size="30" property="phase_required_reviewers[${phaseIdx}]" value="${requestScope.phase_required_secondary_reviewers}"/>
                    &#160;<bean:message key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br />
                    <bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" disabled="${isPhaseClosed}">

                        <c:forEach items="${reviewScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          or (not newProject && project.projectCategory.id == scorecard.category)
                                          or projectCategoriesMap[scorecard.category].projectType.generic}">
                            <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select>
                    <script type="text/javascript">
                        <!--
                         reviewScorecardNodes[reviewScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (projectForm.map['phase_name'][phaseIdx] eq 'Approval')}">
            <c:if test="${phaseIdx eq 0}">
                <tr class="highlighted" id="approval_scorecard_row_template" style="display: none;">
            </c:if>
            <c:if test="${phaseIdx ne 0}">
                <c:if test="${approvalPhaseMet}">
                    <c:set var="disableApprovalReviewerNumberInput" value="${true}"/>
                </c:if>
                <c:set var="approvalPhaseMet" value="${true}"/>
                <tr class="highlighted">
            </c:if>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />
                    <c:choose>
                        <c:when test="${disableApprovalReviewerNumberInput}">
                            <html:text style="width:30px;text-align:right;" styleClass="inputBox" disabled="true"
                                       size="30" property="phase_required_reviewers[${phaseIdx}]"/>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${phaseIdx eq 0}">
                                <html:text style="width:30px;text-align:right;" styleClass="inputBox"
                                           size="30" property="phase_required_reviewers[${phaseIdx}]"
                                           value="${requestScope.phase_required_reviewers_approval}"/>
                            </c:if>
                            <c:if test="${phaseIdx ne 0}">
                                <html:text style="width:30px;text-align:right;" styleClass="inputBox" disabled="${isPhaseClosed}"
                                           size="30" property="phase_required_reviewers[${phaseIdx}]"/>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                    &#160;<bean:message key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br/>
                    <bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" disabled="${isPhaseClosed}">
                        <c:forEach items="${approvalScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                            <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select>
                    <script type="text/javascript">
                        <!--
                         approvalScorecardNodes[approvalScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (projectForm.map['phase_name'][phaseIdx] eq 'Post-Mortem')}">
            <c:if test="${phaseIdx eq 0}">
                <tr class="highlighted" id="post_mortem_scorecard_row_template" style="display: none;">
            </c:if>
            <c:if test="${phaseIdx ne 0}">
                <tr class="highlighted">
            </c:if>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />

                    <c:if test="${phaseIdx eq 0}">
                        <html:text style="width:30px;text-align:right;" styleClass="inputBox"
                            size="30" property="phase_required_reviewers[${phaseIdx}]"
                            value="${requestScope.phase_required_reviewers_postmortem}"/>
                    </c:if>
                    <c:if test="${phaseIdx ne 0}">
                        <html:text style="width:30px;text-align:right;" styleClass="inputBox"
                            size="30" property="phase_required_reviewers[${phaseIdx}]" disabled="${isPhaseClosed}"/>
                    </c:if>

                    &#160;<bean:message key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br/>
                    <bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" disabled="${isPhaseClosed}" >
                        <c:forEach items="${postMortemScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                            <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select>
                    <script type="text/javascript">
                        <!--
                         postMortemScorecardNodes[postMortemScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (projectForm.map['phase_name'][phaseIdx] eq 'Specification Review')}">
            <c:if test="${phaseIdx eq 0}">
                <tr class="highlighted" id="specification_review_scorecard_row_template" style="display: none;">
            </c:if>
            <c:if test="${phaseIdx ne 0}">
                <tr class="highlighted">
            </c:if>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4"><bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" disabled="${isPhaseClosed}">
                        <c:forEach items="${specificationReviewScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                                <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select>
                    <script type="text/javascript">
                        <!--
                         specReviewScorecardNodes[specReviewScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_view_appeal_responses'][phaseIdx])}">
            <tr class="highlighted" ${(phaseIdx eq 0) ? 'id="view_appeal_responses_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <html:radio styleId="appealResponsesOffCheckbox${phaseIdx}" value="true" property="phase_view_appeal_responses[${phaseIdx}]"  disabled="${isPhaseClosed}"/><label
                        for="appealResponsesOffCheckbox${phaseIdx}"><bean:message key="editProject.Phases.Criteria.ViewAppealResponses.Immediately" /></label><br />
                    <html:radio styleId="appealResponsesOnCheckbox${phaseIdx}" value="false" property="phase_view_appeal_responses[${phaseIdx}]" disabled="${isPhaseClosed}" /><label
                        for="appealResponsesOnCheckbox${phaseIdx}"><bean:message key="editProject.Phases.Criteria.ViewAppealResponses.AfterEnd" /></label></td>
            </tr>
        </c:if>
    </c:forEach>
</table><br />

<c:if test="${arePhaseDependenciesEditable}">
<table class="scorecard" id="addphase_tbl" width="100%">
    <tr class="highlighted">
        <td class="valueB"><bean:message key="editProject.Phases.AddNewPhase" /></td>
        <td class="valueB"><bean:message key="editProject.Phases.PhaseStart" /></td>
        <td class="valueB"><bean:message key="editProject.Phases.PhaseEnd" /></td>
        <td class="valueB"><bean:message key="editProject.Phases.Duration" /></td>
        <td class="valueB"><!-- @ --></td>
    </tr>

    <!-- ADD PHASE FORM BEGINS -->
    <tr class="light">
        <td class="value" nowrap="nowrap">
            <bean:message key="editProject.Phases.NewPhase" />
            <br />
            <html:select styleClass="inputBox" property="addphase_type">
                <html:option key="editProject.Phases.Select" value="" />
                <c:forEach items="${requestScope.phaseTypes}" var="phaseType">
                    <html:option key="ProjectPhase.${fn:replace(phaseType.name, ' ', '')}" value="${phaseType.id}"/>
                </c:forEach>
            </html:select>
            <br />
            <bean:message key="editProject.Phases.Placement" />
            <br />
            <html:select styleClass="inputBox" property="addphase_when">
                <html:option key="editProject.Phases.Before" value="before" />
                <html:option key="editProject.Phases.After" value="after" />
            </html:select>
            <br />
            <html:select styleClass="inputBox" property="addphase_where">
                <html:option key="editProject.Phases.SelectPhase" value="" />
                <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                    <html:option value="${projectForm.map['phase_js_id'][i]}">${projectForm.map['phase_name'][i]}</html:option>
                </c:forEach>
            </html:select>
        </td>
        <td class="value" nowrap="nowrap">
            <%--<bean:message key="editProject.Phases.PhaseStart" />--%>
<%--
            <html:radio property="addphase_start_by_phase" value="false" />
--%>
            <html:checkbox property="addphase_start_by_fixed_time"
                           onclick="return addPhaseFixedStartTimeBoxChanged(this)"/>
            <html:text onblur="JavaScript:this.value=getDateString(this.value);" styleClass="inputBoxDate"
                       property="addphase_start_date" disabled="true"/>
            <html:text onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);"
                       styleClass="inputBoxTime" property="addphase_start_time" disabled="true"/>
            <c:out value="${currentTimezone}"/><br />
<%--
            <html:radio property="addphase_start_by_phase" value="true" />
--%>
            <html:checkbox property="addphase_start_by_phase"
                           onclick="return addPhasePhaseStartByPhaseBoxChanged(this)"/>
            <bean:message key="editProject.Phases.When" />
            <div style="margin-left: 20px;">
            <html:select styleClass="inputBox" property="addphase_start_phase" style="width:150px;" disabled="true">
                <html:option key="editProject.Phases.SelectPhase" value="" />
                <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                    <html:option value="${projectForm.map['phase_js_id'][i]}">${projectForm.map['phase_name'][i]}</html:option>
                </c:forEach>
            </html:select>
            <br />
            <html:select styleClass="inputBox" property="addphase_start_when" disabled="true">
                <html:option key="editProject.Phases.Starts" value="starts" />
                <html:option key="editProject.Phases.Ends" value="ends" />
            </html:select>
            <br />
            <html:select styleClass="inputBox" property="addphase_start_plusminus" disabled="true">
                <html:option value="plus">+</html:option>
                <html:option value="minus">-</html:option>
            </html:select>
            <br />
            <html:text styleClass="inputBox" property="addphase_start_amount" style="width:30px;"  disabled="true"/>
            <br />
            <html:select styleClass="inputBox" property="addphase_start_dayshrs"  disabled="true">
                <html:option key="editProject.Phases.Days" value="days" />
                <html:option key="editProject.Phases.Hrs" value="hrs" />
                <html:option key="editProject.Phases.Mins" value="mins" />
            </html:select>
            </div>
        </td>
        <td class="value" nowrap="nowrap">
            <%--<bean:message key="editProject.Phases.PhaseEnd" />--%>
              <html:radio property="addphase_use_duration" value="false" />
            <html:text onblur="JavaScript:this.value=getDateString(this.value);" styleClass="inputBoxDate" property="addphase_end_date" />
            <html:text onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" styleClass="inputBoxTime" property="addphase_end_time" />
            <c:out value="${currentTimezone}"/>
        </td>
        <td class="value" nowrap="nowrap">
            <%--<bean:message key="editProject.Phases.Duration" />--%>
            <html:radio property="addphase_use_duration" value="true" />
            <html:text styleClass="inputBoxDuration" property="addphase_duration" />
        </td>
        <td class="value" colspan="2">
            <html:img srcKey="editProject.Phases.AddPhase.img" altKey="editProject.Phases.AddPhase.alt" onclick="addNewPhase();" style="cursor:hand;" />
        </td>
    </tr>
    <!-- ADD PHASE FORM ENDS -->

    <tr>
        <td class="lastRowTD" colspan="5"><!-- @ --></td>
    </tr>
</table><br />
</c:if>
