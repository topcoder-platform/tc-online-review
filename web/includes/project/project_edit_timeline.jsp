<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the form input elements group for editing the timeline and other
  - parameters for single project phase.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>

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
                    if (!dropdown.children[0].classList.contains("disabled")) {
                        this.querySelector('.custom-select').classList.toggle('open');
                    }
                });
            }

            for (const options of document.querySelectorAll('.custom-options')) {
                let selected = options.querySelector('.custom-option[selected]');
                if (!selected) {
                    let option = options.querySelector('.custom-option');
                    if (option !== null) {
                        option.selected = true;
                        option.classList.add('selected');
                    }
                } else {
                    selected.classList.add('selected');
                }
            }

            for (const option of document.querySelectorAll(".custom-option")) {
                const input = option.closest('.editProject__input').querySelector('input');
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
                <select class="editInputBox" name="template_name" style="width:100px;"><c:set var="OR_FIELD_TO_SELECT" value="template_name"/>
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

<table class="editPhaseTable" id="timeline_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
    <thead class="editPhaseTable__header">
        <tr>
            <c:if test="${not newProject}">
                <th width="10%"><or:text key="editProject.Phases.CurrentPhase" /></th>
            </c:if>
            <th width="27%"><or:text key="editProject.Phases.PhaseName" /></th>
            <th width="22%"><or:text key="editProject.Phases.PhaseStart" /></th>
            <th><or:text key="editProject.Phases.PhaseEnd" /></th>
            <th nowrap="nowrap"><or:text key="editProject.Phases.Duration" /></th>
            <th><or:text key="editProject.Phases.Delete" /></th>
        </tr>
    </thead>
    <tbody class="editPhaseTable__body">
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
                        <a onclick="javascript:openOrClosePhase(this.parentNode.parentNode, 'open_phase');" name="open_phase_img"
                            class="phaseButton"
                            style="${projectForm.map['phase_can_open'][phaseIdx] ? 'cursor:hand;' : 'display:none;'}">
                            <or:text key='editProject.Phases.OpenPhase.alt' />
                        </a>
                        <a onclick="javascript:openOrClosePhase(this.parentNode.parentNode, 'close_phase');" name="close_phase_img" class="phaseButton"
                            style="${projectForm.map['phase_can_close'][phaseIdx] ? 'cursor:hand;' : 'display:none;'}">
                            <or:text key='editProject.Phases.ClosePhase.alt' />
                        </a>
                        </td>
                    </c:if>
            </c:if>
                <td nowrap="nowrap">
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
                    <div class="editProject__checkbox">
                        <input id="phase_start_by_fixed_time[${phaseIdx}]" type="checkbox" name="phase_start_by_fixed_time[${phaseIdx}]"
                                    <c:if test="${isPhaseClosed or not arePhaseDependenciesEditable}">disabled</c:if>
                                    onclick="return fixedStartTimeBoxChanged(this, ${phaseIdx})" <or:checked name='phase_start_by_fixed_time[${phaseIdx}]' value='on|yes|true' /> />
                        <span class="checkbox-label"></span>
                        <label for="phase_start_by_fixed_time[${phaseIdx}]"></label>
                    </div>
                    <input type="text" onblur="JavaScript:this.value=getDateString(this.value);"
                            class="inputDate" name="phase_start_date[${phaseIdx}]" value="<or:fieldvalue field='phase_start_date[${phaseIdx}]' />"
                                <c:if test="${isPhaseClosed or not isFixedStartTimeSet}">disabled</c:if> />
                    <input type="text" onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);"
                            class="inputTime" name="phase_start_time[${phaseIdx}]" value="<or:fieldvalue field='phase_start_time[${phaseIdx}]' />"
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
                    <div class="editProject__checkbox whenCheck">
                        <div class="when"><or:text key="editProject.Phases.When" /></div>
                        <input type="checkbox" name="phase_start_by_phase[${phaseIdx}]"
                                    <c:if test="${isPhaseClosed or not arePhaseDependenciesEditable}">disabled</c:if>
                                    onclick="return phaseStartByPhaseBoxChanged(this, ${phaseIdx})" <or:checked name='phase_start_by_phase[${phaseIdx}]' value='on|yes|true' /> />
                        <span class="checkbox-label"></span>
                        <label for="phase_start_by_phase[${phaseIdx}]"></label>
                    </div>
                    <div>
                        <div class="editProject__input">
                            <input type="hidden" name="phase_start_phase[${phaseIdx}]">
                            <div class="editProject__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select grey ${(isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable) ? 'disabled' : null}">
                                        <div class="custom-select__trigger greyText"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="phase_start_phase[${phaseIdx}]"/>
                                            <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                                                <span class="custom-option" data-value="${projectForm.map['phase_js_id'][i]}" <or:selected value="${projectForm.map['phase_js_id'][i]}"/>>${projectForm.map['phase_name'][i]}</span>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="editProject__input starts">
                            <input type="hidden" name="phase_start_when[${phaseIdx}]">
                            <div class="editProject__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select grey ${(isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable) ? 'disabled' : null}">
                                        <div class="custom-select__trigger greyText"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="phase_start_when[${phaseIdx}]"/>
                                            <span class="custom-option" data-value="starts" <or:selected value="starts"/>><or:text key="editProject.Phases.Starts" def="starts"/></span>
                                            <span class="custom-option" data-value="ends" <or:selected value="ends"/>><or:text key="editProject.Phases.Ends" def="ends"/></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="editProject__input plusminus">
                            <input type="hidden" name="phase_start_plusminus[${phaseIdx}]">
                            <div class="editProject__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select grey ${(isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable) ? 'disabled' : null}">
                                        <div class="custom-select__trigger greyText"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="phase_start_plusminus[${phaseIdx}]"/>
                                            <span class="custom-option" data-value="plus" <or:selected value="plus"/>>+</span>
                                            <span class="custom-option" data-value="minus" <or:selected value="minus"/>>-</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <input type="text" class="editInputBox" name="phase_start_amount[${phaseIdx}]"
                                <c:if test="${isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable}">disabled</c:if>
                                value="<or:fieldvalue field='phase_start_amount[${phaseIdx}]' />" />
                        <div class="editProject__input dayshrs">
                            <input type="hidden" name="phase_start_dayshrs[${phaseIdx}]">
                            <div class="editProject__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select grey ${(isPhaseClosed or not isPhaseDependencySet or not arePhaseDependenciesEditable) ? 'disabled' : null}">
                                        <div class="custom-select__trigger greyText"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="phase_start_dayshrs[${phaseIdx}]"/>
                                            <span class="custom-option" data-value="days" <or:selected value="days"/>><or:text key="editProject.Phases.Days" def="days"/></span>
                                            <span class="custom-option" data-value="hrs" <or:selected value="hrs"/>><or:text key="editProject.Phases.Hrs" def="hrs"/></span>
                                            <span class="custom-option" data-value="mins" <or:selected value="mins"/>><or:text key="editProject.Phases.Mins" def="mins"/></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div name="start_date_validation_msg" class="error" style="display:none"></div>
                </td>
                <td class="value" nowrap="nowrap">
                    <div class="custom-radio customRadio__top">
                        <input type="radio" id="phase_use_duration[${phaseIdx}]" name="phase_use_duration[${phaseIdx}]" value="false" <c:if test="${isPhaseClosed}">disabled</c:if> <or:checked name='phase_use_duration[${phaseIdx}]' value='false' />/>
                        <label class="phaseUseDuration" for="phase_use_duration[${phaseIdx}]" style="margin: 0px;"></label>
                    </div>
                    <input type="text" onblur="JavaScript:this.value=getDateString(this.value);" class="inputDate" name="phase_end_date[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if> value="<or:fieldvalue field='phase_end_date[${phaseIdx}]' />" />
                    <input type="text" onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" class="inputTime" name="phase_end_time[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if> value="<or:fieldvalue field='phase_end_time[${phaseIdx}]' />" />
                    <c:out value="${currentTimezone}"/>
                    <div name="end_date_validation_msg" class="error" style="display:none"></div>
                </td>
                <td class="value">
                    <div class="custom-radio customRadio__top">
                        <input type="radio" id="phase_use_duration_last[${phaseIdx}]" name="phase_use_duration[${phaseIdx}]" value="true" <c:if test="${isPhaseClosed}">disabled</c:if> <or:checked name='phase_use_duration[${phaseIdx}]' value='true' />/>
                        <label class="phaseUseDuration" for="phase_use_duration_last[${phaseIdx}]" style="margin: 0px;"></label>
                    </div>
                    <input type="text" class="edit__InputBoxDuration" name="phase_duration[${phaseIdx}]"
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
                        <input type="text" style="width:40px;text-align:right;" class="editInputBox" <c:if test="${isPhaseClosed}">disabled</c:if>
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
                    <td class="value" colspan="4">
                            <div class=" scorecard__label">
                                <span class="label">
                                    <or:text key="editProject.Phases.Criteria.Scorecard" />
                                </span>
                                <div class="selectCustom">
                                    <select class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                                        <c:forEach items="${screeningScorecards}" var="scorecard">
                                            <c:if test="${(newProject && scorecard.category == 1)
                                                        || (not newProject && project.projectCategory.id == scorecard.category)
                                                        || projectCategoriesMap[scorecard.category].projectType.generic}">
                                                <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
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
                        <input type="text" style="width:40px;text-align:right;" class="editInputBox" <c:if test="${isPhaseClosed}">disabled</c:if>
                            size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />" />
                        &#160;<or:text key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br />
                        <div class="editProject__input scorecard__label">
                            <span class="label">
                                <or:text key="editProject.Phases.Criteria.Scorecard" />
                            </span>
                            <div class="selectCustom">
                                <select class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>

                                    <c:forEach items="${reviewScorecards}" var="scorecard">
                                        <c:if test="${(newProject && scorecard.category == 1)
                                                    or (not newProject && project.projectCategory.id == scorecard.category)
                                                    or projectCategoriesMap[scorecard.category].projectType.generic}">
                                        <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
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
                            <input type="text" style="width:40px;text-align:right;" class="editInputBox"
                                    size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />"
                                    value="${requestScope.phase_required_reviewers_approval}"/>
                        </c:if>
                        <c:if test="${phaseIdx ne 0}">
                            <input type="text" style="width:40px;text-align:right;" class="editInputBox" <c:if test="${isPhaseClosed}">disabled</c:if>
                                    size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />"/>
                        </c:if>
                        &#160;<or:text key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br/>
                        <div class="editProject__input scorecard__label">
                            <span class="label">
                                <or:text key="editProject.Phases.Criteria.Scorecard" />
                            </span>
                            <div class="selectCustom">
                                <select class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                                    <c:forEach items="${approvalScorecards}" var="scorecard">
                                        <c:if test="${(newProject && scorecard.category == 1)
                                                    || (not newProject && project.projectCategory.id == scorecard.category)
                                                    || projectCategoriesMap[scorecard.category].projectType.generic}">
                                        <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
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
                            <input type="text" style="width:40px;text-align:right;" class="editInputBox"
                                size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />"
                                value="${requestScope.phase_required_reviewers_postmortem}"/>
                        </c:if>
                        <c:if test="${phaseIdx ne 0}">
                            <input type="text" style="width:40px;text-align:right;" class="editInputBox"
                                size="30" name="phase_required_reviewers[${phaseIdx}]" value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />" <c:if test="${isPhaseClosed}">disabled</c:if>/>
                        </c:if>

                        &#160;<or:text key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br/>
                        <div class="editProject__input scorecard__label">
                            <span class="label">
                                <or:text key="editProject.Phases.Criteria.Scorecard" />
                            </span>
                            <div class="selectCustom">
                            <select class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if> ><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                                <c:forEach items="${postMortemScorecards}" var="scorecard">
                                    <c:if test="${(newProject && scorecard.category == 1)
                                                || (not newProject && project.projectCategory.id == scorecard.category)
                                                || projectCategoriesMap[scorecard.category].projectType.generic}">
                                    <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                            </div>
                        </div>
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
                    <td class="value" colspan="4">
                        <div class="editProject__input scorecard__label">
                            <span class="label">
                                <or:text key="editProject.Phases.Criteria.Scorecard" />
                            </span>
                                <div class="selectCustom">
                                    <select class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                                        <c:forEach items="${specificationReviewScorecards}" var="scorecard">
                                            <c:if test="${(newProject && scorecard.category == 1)
                                                        || (not newProject && project.projectCategory.id == scorecard.category)
                                                        || projectCategoriesMap[scorecard.category].projectType.generic}">
                                                <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
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
                    <td class="value" colspan="4">
                        <div class="editProject__input scorecard__label">
                            <span class="label">
                                <or:text key="editProject.Phases.Criteria.Scorecard" />
                            </span>
                            <div class="selectCustom">
                                <select class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                                    <c:forEach items="${checkpointScreeningScorecards}" var="scorecard">
                                        <c:if test="${(newProject && scorecard.category == 1)
                                                    || (not newProject && project.projectCategory.id == scorecard.category)
                                                    || projectCategoriesMap[scorecard.category].projectType.generic}">
                                            <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
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
                    <td class="value" colspan="4">
                        <div class="editProject__input scorecard__label">
                            <span class="label">
                                <or:text key="editProject.Phases.Criteria.Scorecard" />
                            </span>
                            <div class="selectCustom">
                                <select class="inputBox" name="phase_scorecard[${phaseIdx}]" <c:if test="${isPhaseClosed}">disabled</c:if>><c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                                    <c:forEach items="${checkpointReviewScorecards}" var="scorecard">
                                        <c:if test="${(newProject && scorecard.category == 1)
                                                    || (not newProject && project.projectCategory.id == scorecard.category)
                                                    || projectCategoriesMap[scorecard.category].projectType.generic}">
                                            <option value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
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
                        <input type="text" style="width:40px;text-align:right;" class="editInputBox" <c:if test="${isPhaseClosed}">disabled</c:if> size="30" name="phase_required_reviewers[${phaseIdx}]"  value="<or:fieldvalue field='phase_required_reviewers[${phaseIdx}]' />" />
                        &#160;<or:text key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br />
                        <div class="editProject__input scorecard__label">
                            <span class="label">
                                <or:text key="editProject.Phases.Criteria.Scorecard" />
                            </span>
                            <input type="hidden" name="phase_scorecard[${phaseIdx}]">
                            <div class="editProject__selection">
                                <div class="custom-select-wrapper">
                                    <div class="custom-select grey ${(isPhaseClosed) ? 'disabled' : null}">
                                        <div class="custom-select__trigger greyText"><span></span>
                                            <div class="arrow"></div>
                                        </div>
                                        <div class="custom-options">
                                            <c:set var="OR_FIELD_TO_SELECT" value="phase_scorecard[${phaseIdx}]"/>
                                            <c:forEach items="${iterativeReviewScorecards}" var="scorecard">
                                                <c:if test="${(newProject && scorecard.category == 1)
                                                            or (not newProject && project.projectCategory.id == scorecard.category)
                                                            or projectCategoriesMap[scorecard.category].projectType.generic}">
                                                    <span class="custom-option" data-value="${scorecard.id}" <or:selected value="${scorecard.id}"/>>${scorecard.name} ${scorecard.version}</span>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
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
    </tbody>
</table>

<c:if test="${arePhaseDependenciesEditable}">
<table class="scorecard" id="addphase_tbl" width="100%">
    <tr class="highlighted">
        <td><or:text key="editProject.Phases.AddNewPhase" /></td>
        <td><or:text key="editProject.Phases.PhaseStart" /></td>
        <td><or:text key="editProject.Phases.PhaseEnd" /></td>
        <td><or:text key="editProject.Phases.Duration" /></td>
        <td><!-- @ --></td>
    </tr>

    <!-- ADD PHASE FORM BEGINS -->
    <tr class="light">
        <td class="value" nowrap="nowrap">
            <or:text key="editProject.Phases.NewPhase" />
            <br />
            <select class="editInputBox" name="addphase_type"><c:set var="OR_FIELD_TO_SELECT" value="addphase_type"/>
                <option  value=""><or:text key="editProject.Phases.Select" /></option>
                <c:forEach items="${requestScope.phaseTypes}" var="phaseType">
                    <option  value="${phaseType.id}" <or:selected value="${phaseType.id}"/>><or:text key="ProjectPhase.${fn:replace(phaseType.name, ' ', '')}" def="${phaseType.id}"/></option>
                </c:forEach>
            </select>
            <br />
            <or:text key="editProject.Phases.Placement" />
            <br />
            <select class="editInputBox" name="addphase_when"><c:set var="OR_FIELD_TO_SELECT" value="addphase_when"/>
                <option  value="before"  <or:selected value="before"/>><or:text key="editProject.Phases.Before" def="before"/></option>
                <option  value="after"  <or:selected value="after"/>><or:text key="editProject.Phases.After" def="after" /></option>
            </select>
            <br />
            <select class="editInputBox" name="addphase_where"><c:set var="OR_FIELD_TO_SELECT" value="addphase_where"/>
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
            <input type="text" onblur="JavaScript:this.value=getDateString(this.value);" class="inputDate"
                       name="addphase_start_date" value="<or:fieldvalue field='addphase_start_date' />" disabled="true"/>
            <input type="text" onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);"
                       class="inputTime" name="addphase_start_time" value="<or:fieldvalue field='addphase_start_time' />" disabled="true"/>
            <c:out value="${currentTimezone}"/><br />
<%--
            <input type="radio" name="addphase_start_by_phase" value="true"  <or:checked name='addphase_start_by_phase' value='true' />/>
--%>
            <input type="checkbox" name="addphase_start_by_phase"
                           onclick="return addPhasePhaseStartByPhaseBoxChanged(this)" <or:checked name='addphase_start_by_phase' value='on|yes|true' /> />
            <or:text key="editProject.Phases.When" />
            <div style="margin-left: 20px;">
            <select class="editInputBox" name="addphase_start_phase" style="width:150px;" disabled="true"><c:set var="OR_FIELD_TO_SELECT" value="addphase_start_phase"/>
                <option  value=""><or:text key="editProject.Phases.Select" def="" /></option>
                <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                    <option value="${projectForm.map['phase_js_id'][i]}" <or:selected value="${projectForm.map['phase_js_id'][i]}"/>>${projectForm.map['phase_name'][i]}</option>
                </c:forEach>
            </select>
            <br />
            <select class="editInputBox" name="addphase_start_when" disabled="true"><c:set var="OR_FIELD_TO_SELECT" value="addphase_start_when"/>
                <option  value="starts"  <or:selected value="starts"/>><or:text key="editProject.Phases.Starts" def="starts"/></option>
                <option  value="ends"  <or:selected value="ends"/>><or:text key="editProject.Phases.Ends" def="ends" /></option>
            </select>
            <br />
            <select class="editInputBox" name="addphase_start_plusminus" disabled="true"><c:set var="OR_FIELD_TO_SELECT" value="addphase_start_plusminus"/>
                <option value="plus" <or:selected value="plus"/>>+</option>
                <option value="minus" <or:selected value="minus"/>>-</option>
            </select>
            <br />
            <input type="text" class="editInputBox" name="addphase_start_amount" style="width:40px;"  disabled="true" value="<or:fieldvalue field='addphase_start_amount' />" />
            <br />
            <select class="editInputBox" name="addphase_start_dayshrs"  disabled="true"><c:set var="OR_FIELD_TO_SELECT" value="addphase_start_dayshrs"/>
                <option  value="days"  <or:selected value="days"/>><or:text key="editProject.Phases.Days" def="days" /></option>
                <option  value="hrs"  <or:selected value="hrs"/>><or:text key="editProject.Phases.Hrs" def="hrs" /></option>
                <option  value="mins"  <or:selected value="mins"/>><or:text key="editProject.Phases.Mins" def="mins" /></option>
            </select>
            </div>
        </td>
        <td class="value" nowrap="nowrap">
            <%--<or:text key="editProject.Phases.PhaseEnd" />--%>
              <input type="radio" name="addphase_use_duration" value="false"  <or:checked name='addphase_use_duration' value='false' />/>
            <input type="text" onblur="JavaScript:this.value=getDateString(this.value);" class="inputDate" name="addphase_end_date"  value="<or:fieldvalue field='addphase_end_date' />" />
            <input type="text" onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" class="inputTime" name="addphase_end_time"  value="<or:fieldvalue field='addphase_end_time' />" />
            <c:out value="${currentTimezone}"/>
        </td>
        <td class="value" nowrap="nowrap">
            <%--<or:text key="editProject.Phases.Duration" />--%>
            <input type="radio" name="addphase_use_duration" value="true"  <or:checked name='addphase_use_duration' value='true' />/>
            <input type="text" class="edit__InputBoxDuration" name="addphase_duration"  value="<or:fieldvalue field='addphase_duration' />" />
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
