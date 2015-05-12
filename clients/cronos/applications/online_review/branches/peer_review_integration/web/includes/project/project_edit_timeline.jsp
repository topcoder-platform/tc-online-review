<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the form input elements group for editing the timeline and other
  - parameters for single project phase.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>

<c:set var="currentTime" value="<%=new java.util.Date()%>"/>
<fmt:formatDate value="${currentTime}" var="currentTimezone" pattern="z"/>
<%-- If creating a new project, show "Create Timeline" table --%>
<c:if test="${newProject}">
    <table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse;">
        <tr>
            <td class="title" width="1%" nowrap="nowrap"><or:text key="editProject.CreateTimeline.title" /></td>
            <td class="title" width="99%"><!-- @ --></td>
        </tr>
        <tr class="light">
            <td class="value" width="2%" nowrap="nowrap"><b><or:text key="editProject.CreateTimeline.UseTemplate" /></b></td>
            <td class="value">
                <select class="inputBox" name="template_name" style="width:100px;"><c:set var="OR_FIELD_TO_SELECT" value="template_name"/>
                    <option value="" <or:selected value=""/>>Select</option>
                    <c:forEach var="templateName" items="${phaseTemplateNames}">
                        <option value="${templateName}" <or:selected value="${templateName}"/>>${templateName}</option>
                    </c:forEach>
                </select>
                <a href="javascript:loadTimelineTemplate();">
                    <img src="/i/or/bttn_load_template.gif" name="load_template" id="load_template" />
                </a>
            </td>
        </tr>
    </table><br />
</c:if>

<table class="scorecard" id="timeline_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
    <tr>
        <c:if test="${not newProject}">
            <td class="headerC"><or:text key="editProject.Phases.CurrentPhase" /></td>
        </c:if>
        <td class="header"><or:text key="editProject.Phases.PhaseName" /></td>
        <td class="header"><or:text key="editProject.Phases.PhaseStart" /></td>
        <td class="header"><or:text key="editProject.Phases.PhaseEnd" /></td>
        <td class="header" nowrap="nowrap"><or:text key="editProject.Phases.Duration" /></td>
        <td class="header"><or:text key="editProject.Phases.Delete" /></td>
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
                <input type="hidden" name="phase_can_open[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if> value="<or:fieldvalue field='phase_can_open[${phaseIdx}]' />" />
                <input type="hidden" name="phase_can_close[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if> value="<or:fieldvalue field='phase_can_close[${phaseIdx}]' />" />
                <c:if test="${not isPhaseClosed}">
                    <img onclick="javascript:openOrClosePhase(this.parentNode.parentNode, 'open_phase');" border="0"
                        src="<or:text key='editProject.Phases.OpenPhase.img' />" alt="<or:text key='editProject.Phases.OpenPhase.alt' />" name="open_phase_img"
                        style="${projectForm.map['phase_can_open'][phaseIdx] ? 'cursor:hand;' : 'display:none;'}" /><img
                        onclick="javascript:openOrClosePhase(this.parentNode.parentNode, 'close_phase');" name="close_phase_img"
                        src="<or:text key='editProject.Phases.ClosePhase.img' />" alt="<or:text key='editProject.Phases.ClosePhase.alt' />" border="0"
                        style="${projectForm.map['phase_can_close'][phaseIdx] ? 'cursor:hand;' : 'display:none;'}" /></td>
                </c:if>
        </c:if>
            <td class="valueB" nowrap="nowrap">
                <span name="phase_name_text">${projectForm.map['phase_name'][phaseIdx]}</span>
                &#160; <span name="phase_number_text">${projectForm.map['phase_number'][phaseIdx]}</span>
            </td>
            <td class="value" nowrap="nowrap">
                <input type="hidden" name="isPhaseClosed[${phaseIdx}]" value="${isPhaseClosed}"/>
                <input type="hidden" name="phase_type[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>  value="<or:fieldvalue field='phase_type[${phaseIdx}]' />" />
                <input type="hidden" name="phase_id[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>  value="<or:fieldvalue field='phase_id[${phaseIdx}]' />" />
                <input type="hidden" name="phase_js_id[${phaseIdx}]"  value="<or:fieldvalue field='phase_js_id[${phaseIdx}]' />" />
                <input type="hidden" name="phase_action[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>  value="<or:fieldvalue field='phase_action[${phaseIdx}]' />" />
                <input type="hidden" name="phase_name[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>  value="<or:fieldvalue field='phase_name[${phaseIdx}]' />" />
                <input type="hidden" name="phase_number[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>  value="<or:fieldvalue field='phase_number[${phaseIdx}]' />" />

                <c:if test="${not arePhaseDependenciesEditable and not isPhaseClosed}">
                    <input type="hidden" name="phase_start_by_fixed_time[${phaseIdx}]" value="<or:fieldvalue field='phase_start_by_fixed_time[${phaseIdx}]' />" />
                </c:if>
                <input type="hidden" name="isPhaseFixedStartTimeEnabled[${phaseIdx}]"
                       value="${projectForm.map['phase_start_by_fixed_time'][phaseIdx]}"/>
                <input type="checkbox" name="phase_start_by_fixed_time[${phaseIdx}]"
                               <c:if test="${isPhaseClosed or not arePhaseDependenciesEditable}">disabled</c:if>
                               onclick="return fixedStartTimeBoxChanged(this, ${phaseIdx})" <or:checked name='phase_start_by_fixed_time[${phaseIdx}]' value='on|yes|true' /> />
                <input type="text" onblur="JavaScript:this.value=getDateString(this.value);"
                           class="inputBoxDate" name="phase_start_date[${phaseIdx}]" value="<or:fieldvalue field='phase_start_date[${phaseIdx}]' />"
                               <c:if test="${isPhaseClosed or not isFixedStartTimeSet}">disabled</c:if> />
                <input type="text" onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);"
                           class="inputBoxTime" name="phase_start_time[${phaseIdx}]" value="<or:fieldvalue field='phase_start_time[${phaseIdx}]' />"
                               <c:if test="${isPhaseClosed or not isFixedStartTimeSet}">disabled</c:if> />

                <c:out value="${currentTimezone}"/><br />

                <c:if test="${not arePhaseDependenciesEditable and not isPhaseClosed}">
                    <input type="hidden" name="phase_start_by_phase[${phaseIdx}]" value="<or:fieldvalue field='phase_start_by_phase[${phaseIdx}]' />" />
                    <input type="hidden" name="phase_start_phase[${phaseIdx}]" value="<or:fieldvalue field='phase_start_phase[${phaseIdx}]' />" />
                    <input type="hidden" name="phase_start_when[${phaseIdx}]" value="<or:fieldvalue field='phase_start_when[${phaseIdx}]' />" />
                    <input type="hidden" name="phase_start_plusminus[${phaseIdx}]" value="<or:fieldvalue field='phase_start_plusminus[${phaseIdx}]' />" />
                    <input type="hidden" name="phase_start_amount[${phaseIdx}]" value="<or:fieldvalue field='phase_start_amount[${phaseIdx}]' />" />
                    <input type="hidden" name="phase_start_dayshrs[${phaseIdx}]" value="<or:fieldvalue field='phase_start_dayshrs[${phaseIdx}]' />" />
                </c:if>
                <input type="hidden" name="isPhaseDependencySet[${phaseIdx}]"
                       value="${projectForm.map['phase_start_by_phase'][phaseIdx]}"/>
                <input type="checkbox" name="phase_start_by_phase[${phaseIdx}]"
                               <c:if test="${isPhaseClosed or not arePhaseDependenciesEditable}">disabled</c:if>
                               onclick="return phaseStartByPhaseBoxChanged(this, ${phaseIdx})" <or:checked name='phase_start_by_phase[${phaseIdx}]' value='on|yes|true' /> />
                <or:text key="editProject.Phases.When" />
                <div style="margin-left: 20px;">
                <select class="inputBox" name="phase_start_phase[${phaseIdx}]" style="width:150px;"
                               <c:if test="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">disabled</c:if> ><c:set var="OR_FIELD_TO_SELECT" value="phase_start_phase[${phaseIdx}]"/>
                    <option  value=""></option>
                    <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                        <c:if test="${phaseIdx ne i}">
                            <option value="${projectForm.map['phase_js_id'][i]}" <or:selected value="${projectForm.map['phase_js_id'][i]}"/>>${projectForm.map['phase_name'][i]}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <br />
                <select class="inputBox" name="phase_start_when[${phaseIdx}]"
                               <c:if test="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">disabled</c:if> ><c:set var="OR_FIELD_TO_SELECT" value="phase_start_when[${phaseIdx}]"/>
                    <option  value="starts"  <or:selected value="starts"/>><or:text key="editProject.Phases.Starts" def="starts"/></option>
                    <option  value="ends"  <or:selected value="ends"/>><or:text key="editProject.Phases.Ends" def="ends"/></option>
                </select>
                <br />
                <select class="inputBox" name="phase_start_plusminus[${phaseIdx}]"
                               <c:if test="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">disabled</c:if> ><c:set var="OR_FIELD_TO_SELECT" value="phase_start_plusminus[${phaseIdx}]"/>
                    <option value="plus" <or:selected value="plus"/>>+</option>
                    <option value="minus" <or:selected value="minus"/>>-</option>
                </select>
                <br />
                <input type="text" class="inputBox" name="phase_start_amount[${phaseIdx}]" style="width:30px;"
                               <c:if test="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">disabled</c:if>
                            value="<or:fieldvalue field='phase_start_amount[${phaseIdx}]' />" />
                <br />
                <select class="inputBox" name="phase_start_dayshrs[${phaseIdx}]"
                               <c:if test="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">disabled</c:if> ><c:set var="OR_FIELD_TO_SELECT" value="phase_start_dayshrs[${phaseIdx}]"/>
                    <option  value="days"  <or:selected value="days"/>><or:text key="editProject.Phases.Days" def="days"/></option>
                    <option  value="hrs"  <or:selected value="hrs"/>><or:text key="editProject.Phases.Hrs" def="hrs"/></option>
                    <option  value="mins"  <or:selected value="mins"/>><or:text key="editProject.Phases.Mins" def="mins"/></option>
                </select>
                </div>
                <div name="start_date_validation_msg" class="error" style="display:none"></div>
            </td>
            <td class="value" nowrap="nowrap">
                <input type="radio" name="phase_use_duration[${phaseIdx}]" value="false" <c:if test="${isPhaseClosed}">disabled</c:if> <or:checked name='phase_use_duration[${phaseIdx}]' value='false' />/>
                <input type="text" onblur="JavaScript:this.value=getDateString(this.value);" class="inputBoxDate" name="phase_end_date[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if> value="<or:fieldvalue field='phase_end_date[${phaseIdx}]' />" />
                <input type="text" onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" class="inputBoxTime" name="phase_end_time[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if> value="<or:fieldvalue field='phase_end_time[${phaseIdx}]' />" />
                <c:out value="${currentTimezone}"/>
                <div name="end_date_validation_msg" class="error" style="display:none"></div>
            </td>
            <td class="value">
                <input type="radio" name="phase_use_duration[${phaseIdx}]" value="true" <c:if test="${isPhaseClosed}">disabled</c:if> <or:checked name='phase_use_duration[${phaseIdx}]' value='true' />/>
                <input type="text" class="inputBoxDuration" name="phase_duration[${phaseIdx}]"
                           <c:if test="${isPhaseClosed}">disabled</c:if> value="<or:fieldvalue field='phase_duration[${phaseIdx}]' />" />
                <div name="duration_validation_msg" class="error" style="display:none"></div>
            </td>

            <td class="value">
                <c:choose>
                    <c:when test="${isPhaseClosed or not arePhaseDependenciesEditable}">&nbsp;</c:when>
                    <c:otherwise>
                        <img src="<or:text key='editProject.Phases.DeletePhase.img' />"
                            alt="<or:text key='editProject.Phases.DeletePhase.alt' />"
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
                    <or:text key="editProject.Phases.Criteria.RequiredRegistrations.beforeInput" />
                    <%-- TODO: Set default value in Action --%>
                    <input type="text" style="width:30px;text-align:right;" class="inputBox" <c:if test="${isPhaseClosed}">disabled</c:if>
                        size="30" name="phase_required_registrations[${phaseIdx}]" value="<or:fieldvalue field='phase_required_registrations[${phaseIdx}]' />" />
                    &#160;<or:text key="editProject.Phases.Criteria.RequiredRegistrations.afterInput" />
                    <br /><or:text key="editProject.Phases.Criteria.RequiredRegistrations.note" /></td>
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
                <td class="value" colspan="4"><or:text key="editProject.Phases.Criteria.Scorecard" />
                    <select style="width:350px;" class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                        <c:forEach items="${screeningScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                                <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                            </c:if>
                        </c:forEach>
                    </select>
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
                    <or:text key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />
                    <input type="text" style="width:30px;text-align:right;" class="inputBox" <c:if test="${isPhaseClosed}">disabled</c:if>
                        size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />" />
                    &#160;<or:text key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br />
                    <or:text key="editProject.Phases.Criteria.Scorecard" />
                    <select style="width:350px;" class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>

                        <c:forEach items="${reviewScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          or (not newProject && project.projectCategory.id == scorecard.category)
                                          or projectCategoriesMap[scorecard.category].projectType.generic}">
                            <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                            </c:if>
                        </c:forEach>
                    </select>
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
                <tr class="highlighted">
            </c:if>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <or:text key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />
                    <c:if test="${phaseIdx eq 0}">
                        <input type="text" style="width:30px;text-align:right;" class="inputBox"
                                   size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />"
                                   value="${requestScope.phase_required_reviewers_approval}"/>
                    </c:if>
                    <c:if test="${phaseIdx ne 0}">
                        <input type="text" style="width:30px;text-align:right;" class="inputBox" <c:if test="${isPhaseClosed}">disabled</c:if>
                                   size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />"/>
                    </c:if>
                    &#160;<or:text key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br/>
                    <or:text key="editProject.Phases.Criteria.Scorecard" />
                    <select style="width:350px;" class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                        <c:forEach items="${approvalScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                            <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                            </c:if>
                        </c:forEach>
                    </select>
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
                    <or:text key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />

                    <c:if test="${phaseIdx eq 0}">
                        <input type="text" style="width:30px;text-align:right;" class="inputBox"
                            size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />"
                            value="${requestScope.phase_required_reviewers_postmortem}"/>
                    </c:if>
                    <c:if test="${phaseIdx ne 0}">
                        <input type="text" style="width:30px;text-align:right;" class="inputBox"
                            size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />" <c:if test="${isPhaseClosed}">disabled</c:if>/>
                    </c:if>

                    &#160;<or:text key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br/>
                    <or:text key="editProject.Phases.Criteria.Scorecard" />
                    <select style="width:350px;" class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if> ><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                        <c:forEach items="${postMortemScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                            <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                            </c:if>
                        </c:forEach>
                    </select>
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
                <td class="value" colspan="4"><or:text key="editProject.Phases.Criteria.Scorecard" />
                    <select style="width:350px;" class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                        <c:forEach items="${specificationReviewScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                                <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <script type="text/javascript">
                        <!--
                         specReviewScorecardNodes[specReviewScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (projectForm.map['phase_name'][phaseIdx] eq 'Checkpoint Screening')}">
            <c:if test="${phaseIdx eq 0}">
                <tr class="highlighted" id="checkpoint_screening_scorecard_row_template" style="display: none;">
            </c:if>
            <c:if test="${phaseIdx ne 0}">
                <tr class="highlighted">
            </c:if>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4"><or:text key="editProject.Phases.Criteria.Scorecard" />
                    <select style="width:350px;" class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                        <c:forEach items="${checkpointScreeningScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                                <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <script type="text/javascript">
                        <!--
                         checkpointScreeningScorecardNodes[checkpointScreeningScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (projectForm.map['phase_name'][phaseIdx] eq 'Checkpoint Review')}">
            <c:if test="${phaseIdx eq 0}">
                <tr class="highlighted" id="checkpoint_review_scorecard_row_template" style="display: none;">
            </c:if>
            <c:if test="${phaseIdx ne 0}">
                <tr class="highlighted">
            </c:if>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4"><or:text key="editProject.Phases.Criteria.Scorecard" />
                    <select style="width:350px;" class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                        <c:forEach items="${checkpointReviewScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          || (not newProject && project.projectCategory.id == scorecard.category)
                                          || projectCategoriesMap[scorecard.category].projectType.generic}">
                                <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <script type="text/javascript">
                        <!--
                         checkpointReviewScorecardNodes[checkpointReviewScorecardNodes.length]
                             = document.getElementsByName("phase_scorecard[${phaseIdx}]")[0];
                        -->
                    </script>
                </td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_scorecard'][phaseIdx] and projectForm.map['phase_name'][phaseIdx] eq 'Iterative Review')}">
            <tr class="highlighted" ${(phaseIdx eq 0) ? 'id="iterative_review_scorecard_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <or:text key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />
                    <input type="text" style="width:30px;text-align:right;" class="inputBox" <c:if test="${isPhaseClosed}">disabled</c:if> size="30" name="phase_required_reviewers[${phaseIdx}]"  value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />" />
                    &#160;<or:text key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br />
                    <or:text key="editProject.Phases.Criteria.Scorecard" />
                    <select style="width:350px;" class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>

                        <c:forEach items="${iterativeReviewScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1)
                                          or (not newProject && project.projectCategory.id == scorecard.category)
                                          or projectCategoriesMap[scorecard.category].projectType.generic}">
                            <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <script type="text/javascript">
                        <!--
                         iterativeReviewScorecardNodes[iterativeReviewScorecardNodes.length]
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
                    <input type="radio" id="appealResponsesOffCheckbox${phaseIdx}" value="true" name="phase_view_appeal_responses[${phaseIdx}]"  <c:if test="${isPhaseClosed}">disabled</c:if> <or:checked name='phase_view_appeal_responses[${phaseIdx}]' value='true' />/><label
                        for="appealResponsesOffCheckbox${phaseIdx}"><or:text key="editProject.Phases.Criteria.ViewAppealResponses.Immediately" /></label><br />
                    <input type="radio" id="appealResponsesOnCheckbox${phaseIdx}" value="false" name="phase_view_appeal_responses[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>  <or:checked name='phase_view_appeal_responses[${phaseIdx}]' value='false' />/><label
                        for="appealResponsesOnCheckbox${phaseIdx}"><or:text key="editProject.Phases.Criteria.ViewAppealResponses.AfterEnd" /></label></td>
            </tr>
        </c:if>
    </c:forEach>
</table><br />

<c:if test="${arePhaseDependenciesEditable}">
<table class="scorecard" id="addphase_tbl" width="100%">
    <tr class="highlighted">
        <td class="valueB"><or:text key="editProject.Phases.AddNewPhase" /></td>
        <td class="valueB"><or:text key="editProject.Phases.PhaseStart" /></td>
        <td class="valueB"><or:text key="editProject.Phases.PhaseEnd" /></td>
        <td class="valueB"><or:text key="editProject.Phases.Duration" /></td>
        <td class="valueB"><!-- @ --></td>
    </tr>

    <!-- ADD PHASE FORM BEGINS -->
    <tr class="light">
        <td class="value" nowrap="nowrap">
            <or:text key="editProject.Phases.NewPhase" />
            <br />
            <select class="inputBox" name="addphase_type"><c:set var="OR_FIELD_TO_SELECT" value="addphase_type"/>
                <option  value=""><or:text key="editProject.Phases.Select" /></option>
                <c:forEach items="${requestScope.phaseTypes}" var="phaseType">
                    <option  value="${phaseType.id}" <or:selected value="${phaseType.id}"/>><or:text key="ProjectPhase.${fn:replace(phaseType.name, ' ', '')}" def="${phaseType.id}"/></option>
                </c:forEach>
            </select>
            <br />
            <or:text key="editProject.Phases.Placement" />
            <br />
            <select class="inputBox" name="addphase_when"><c:set var="OR_FIELD_TO_SELECT" value="addphase_when"/>
                <option  value="before"  <or:selected value="before"/>><or:text key="editProject.Phases.Before" def="before"/></option>
                <option  value="after"  <or:selected value="after"/>><or:text key="editProject.Phases.After" def="after" /></option>
            </select>
            <br />
            <select class="inputBox" name="addphase_where"><c:set var="OR_FIELD_TO_SELECT" value="addphase_where"/>
                <option  value=""><or:text key="editProject.Phases.Select" def="" /></option>
                <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                    <option value="${projectForm.map['phase_js_id'][i]}" <or:selected value="${projectForm.map['phase_js_id'][i]}"/>>${projectForm.map['phase_name'][i]}</option>
                </c:forEach>
            </select>
        </td>
        <td class="value" nowrap="nowrap">
            <%--<or:text key="editProject.Phases.PhaseStart" />--%>
<%--
            <input type="radio" name="addphase_start_by_phase" value="false"  <or:checked name='addphase_start_by_phase' value='false' />/>
--%>
            <input type="checkbox" name="addphase_start_by_fixed_time"
                           onclick="return addPhaseFixedStartTimeBoxChanged(this)" <or:checked name='addphase_start_by_fixed_time' value='on|yes|true' /> />
            <input type="text" onblur="JavaScript:this.value=getDateString(this.value);" class="inputBoxDate"
                       name="addphase_start_date" value="<or:fieldvalue field='addphase_start_date' />" disabled="true"/>
            <input type="text" onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);"
                       class="inputBoxTime" name="addphase_start_time" value="<or:fieldvalue field='addphase_start_time' />" disabled="true"/>
            <c:out value="${currentTimezone}"/><br />
<%--
            <input type="radio" name="addphase_start_by_phase" value="true"  <or:checked name='addphase_start_by_phase' value='true' />/>
--%>
            <input type="checkbox" name="addphase_start_by_phase"
                           onclick="return addPhasePhaseStartByPhaseBoxChanged(this)" <or:checked name='addphase_start_by_phase' value='on|yes|true' /> />
            <or:text key="editProject.Phases.When" />
            <div style="margin-left: 20px;">
            <select class="inputBox" name="addphase_start_phase" style="width:150px;" disabled="true"><c:set var="OR_FIELD_TO_SELECT" value="addphase_start_phase"/>
                <option  value=""><or:text key="editProject.Phases.Select" def="" /></option>
                <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                    <option value="${projectForm.map['phase_js_id'][i]}" <or:selected value="${projectForm.map['phase_js_id'][i]}"/>>${projectForm.map['phase_name'][i]}</option>
                </c:forEach>
            </select>
            <br />
            <select class="inputBox" name="addphase_start_when" disabled="true"><c:set var="OR_FIELD_TO_SELECT" value="addphase_start_when"/>
                <option  value="starts"  <or:selected value="starts"/>><or:text key="editProject.Phases.Starts" def="starts"/></option>
                <option  value="ends"  <or:selected value="ends"/>><or:text key="editProject.Phases.Ends" def="ends" /></option>
            </select>
            <br />
            <select class="inputBox" name="addphase_start_plusminus" disabled="true"><c:set var="OR_FIELD_TO_SELECT" value="addphase_start_plusminus"/>
                <option value="plus" <or:selected value="plus"/>>+</option>
                <option value="minus" <or:selected value="minus"/>>-</option>
            </select>
            <br />
            <input type="text" class="inputBox" name="addphase_start_amount" style="width:30px;"  disabled="true" value="<or:fieldvalue field='addphase_start_amount' />" />
            <br />
            <select class="inputBox" name="addphase_start_dayshrs"  disabled="true"><c:set var="OR_FIELD_TO_SELECT" value="addphase_start_dayshrs"/>
                <option  value="days"  <or:selected value="days"/>><or:text key="editProject.Phases.Days" def="days" /></option>
                <option  value="hrs"  <or:selected value="hrs"/>><or:text key="editProject.Phases.Hrs" def="hrs" /></option>
                <option  value="mins"  <or:selected value="mins"/>><or:text key="editProject.Phases.Mins" def="mins" /></option>
            </select>
            </div>
        </td>
        <td class="value" nowrap="nowrap">
            <%--<or:text key="editProject.Phases.PhaseEnd" />--%>
              <input type="radio" name="addphase_use_duration" value="false"  <or:checked name='addphase_use_duration' value='false' />/>
            <input type="text" onblur="JavaScript:this.value=getDateString(this.value);" class="inputBoxDate" name="addphase_end_date"  value="<or:fieldvalue field='addphase_end_date' />" />
            <input type="text" onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" class="inputBoxTime" name="addphase_end_time"  value="<or:fieldvalue field='addphase_end_time' />" />
            <c:out value="${currentTimezone}"/>
        </td>
        <td class="value" nowrap="nowrap">
            <%--<or:text key="editProject.Phases.Duration" />--%>
            <input type="radio" name="addphase_use_duration" value="true"  <or:checked name='addphase_use_duration' value='true' />/>
            <input type="text" class="inputBoxDuration" name="addphase_duration"  value="<or:fieldvalue field='addphase_duration' />" />
        </td>
        <td class="value" colspan="2">
            <img src="<or:text key='editProject.Phases.AddPhase.img' />" alt="<or:text key='editProject.Phases.AddPhase.alt' />" onclick="addNewPhase();" style="cursor:hand;" />
        </td>
    </tr>
    <!-- ADD PHASE FORM ENDS -->

    <tr>
        <td class="lastRowTD" colspan="5"><!-- @ --></td>
    </tr>
</table><br />
</c:if>
