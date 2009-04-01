<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>

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

<table class="scorecard" id="timeline_tbl" cellpadding="0" width="100%" style="border-collapse:collapse;">
    <tr>
        <c:if test="${not newProject}">
            <td class="headerC"><bean:message key="editProject.Phases.CurrentPhase" /></td>
        </c:if>
        <td class="header"><bean:message key="editProject.Phases.PhaseName" /></td>
        <td class="header"><bean:message key="editProject.Phases.PhaseStart" /></td>
        <td class="header"><bean:message key="editProject.Phases.PhaseEnd" /></td>
        <td class="header" nowrap="nowrap"><bean:message key="editProject.Phases.Duration" /></td>
        <td class="header"><!-- @ --></td>
    </tr>

    <%-- PHASE ROW GOES HERE --%>
    <c:forEach var="phaseIdx" begin="0" end="${fn:length(projectForm.map['phase_id']) - 1}">
        <c:set var="thisRowStyle" value='style="display:none;"' />
        <c:if test="${phaseIdx == 0}"><c:set var="thisRowId" value="phase_row_template" /></c:if>
        <c:if test="${phaseIdx != 0}">
            <c:set var="thisRowId" value="${projectForm.map['phase_js_id'][phaseIdx]}" />
            <c:if test="${projectForm.map['phase_action'][phaseIdx] != 'delete'}"><c:set
                var="thisRowStyle" value="" /></c:if>
        </c:if>
        <tr class="dark" id="${thisRowId}" ${thisRowStyle}>
        <c:if test="${not newProject}">
            <td class="valueC">
                <html:hidden property="phase_can_open[${phaseIdx}]" />
                <html:hidden property="phase_can_close[${phaseIdx}]" />
                <html:img onclick="javascript:openOrClosePhase(this.parentNode.parentNode, 'open_phase');" border="0"
                    srcKey="editProject.Phases.OpenPhase.img" altKey="editProject.Phases.OpenPhase.alt" imageName="open_phase_img"
                    style="${projectForm.map['phase_can_open'][phaseIdx] ? 'cursor:pointer;' : 'display:none;'}" /><html:img
                    onclick="javascript:openOrClosePhase(this.parentNode.parentNode, 'close_phase');" imageName="close_phase_img"
                    srcKey="editProject.Phases.ClosePhase.img" altKey="editProject.Phases.ClosePhase.alt" border="0"
                    style="${projectForm.map['phase_can_close'][phaseIdx] ? 'cursor:pointer;' : 'display:none;'}" /></td>
        </c:if>
            <td class="valueB" nowrap="nowrap">
                <c:set var="encodedPhaseName" value="${fn:replace(projectForm.map['phase_name'][phaseIdx], ' ', '')}" />
                <c:if test="${empty encodedPhaseName}"><c:set var="encodedPhaseName" value="Registration" /></c:if>
                <span name="phase_name_text"><bean:message key="ProjectPhase.${encodedPhaseName}" /></span>&#xA0;<span
                    name="phase_number_text">${(projectForm.map['phase_number'][phaseIdx] > 1) ? projectForm.map['phase_number'][phaseIdx] : ""}</span></td>
            <td class="value" nowrap="nowrap">
                <html:hidden property="phase_type[${phaseIdx}]" />
                <html:hidden property="phase_id[${phaseIdx}]" />
                <html:hidden property="phase_js_id[${phaseIdx}]" />
                <html:hidden property="phase_action[${phaseIdx}]" />
                <html:hidden property="phase_name[${phaseIdx}]" />
                <html:hidden property="phase_number[${phaseIdx}]" />
                <c:set var="canStartAtAFixedTime" value="${true}" />
                <c:forEach items="${phasesWithDep}" var="phDep"><c:if
                    test="${projectForm.map['phase_name'][phaseIdx] == phDep}"><c:set
                        var="canStartAtAFixedTime" value="${false}" /></c:if></c:forEach>
                <c:if test="${canStartAtAFixedTime}">
                    <span name="phase_fixed_start_time"><html:radio styleId="phaseStartAtRadio${phaseIdx}" property="phase_start_by_phase[${phaseIdx}]" value="false" /><label
                        for="phaseStartAtRadio${phaseIdx}"><bean:message key="editProject.Phases.At" /></label>
                    <html:text onblur="JavaScript:this.value=getDateString(this.value);" styleClass="inputBoxDate" property="phase_start_date[${phaseIdx}]" />
                    <html:text onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" styleClass="inputBoxTime" property="phase_start_time[${phaseIdx}]" />
                    <html:select styleClass="inputBox" property="phase_start_AMPM[${phaseIdx}]">
                        <html:option key="editProject.Phases.AM" value="am" />
                        <html:option key="editProject.Phases.PM" value="pm" />
                    </html:select>
                    <bean:message key="global.Timezone.EST" /><br /></span>
                </c:if>
                <html:radio styleId="phaseStartWhenRadio${phaseIdx}" property="phase_start_by_phase[${phaseIdx}]" value="true" /><label
                    for="phaseStartWhenRadio${phaseIdx}"><bean:message key="editProject.Phases.When" /></label>
                <html:select styleClass="inputBox" property="phase_start_phase[${phaseIdx}]" style="width:120px;">
                    <html:option key="editProject.Phases.SelectPhase" value="" />
                    <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                        <c:if test="${phaseIdx != i}">
                            <c:choose>
                                <c:when test="${projectForm.map['phase_number'][i] > 1}">
                                    <html:option value="${projectForm.map['phase_js_id'][i]}">
                                        <bean:message key="ProjectPhase.${fn:replace(projectForm.map['phase_name'][i], ' ', '')}" />
                                        ${projectForm.map["phase_number"][i]}</html:option></c:when>
                                <c:otherwise><html:option value="${projectForm.map['phase_js_id'][i]}"
                                    key="ProjectPhase.${fn:replace(projectForm.map['phase_name'][i], ' ', '')}" /></c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>
                </html:select>
                <div style="margin-left:21px;">
                    <html:select styleClass="inputBox" property="phase_start_when[${phaseIdx}]">
                        <html:option key="editProject.Phases.Starts" value="starts" />
                        <html:option key="editProject.Phases.Ends" value="ends" />
                    </html:select>
                    <html:select styleClass="inputBox" property="phase_start_plusminus[${phaseIdx}]">
                        <html:option value="plus">+</html:option>
                        <html:option value="minus">-</html:option>
                    </html:select>
                    <html:text styleClass="inputBox" property="phase_start_amount[${phaseIdx}]" style="width:30px;" />
                    <html:select styleClass="inputBox" property="phase_start_dayshrs[${phaseIdx}]">
                        <html:option key="editProject.Phases.Days" value="days" />
                        <html:option key="editProject.Phases.Hrs" value="hrs" />
                    </html:select>
                </div>
                <div name="start_date_validation_msg" class="error" style="display:none;" /></td>
            <td class="value" nowrap="nowrap">
                <html:radio styleId="phaseEndAtRadio${phaseIdx}" property="phase_use_duration[${phaseIdx}]" value="false" /><label
                    for="phaseEndAtRadio${phaseIdx}"><bean:message key="editProject.Phases.At" /></label>
                <html:text onblur="JavaScript:this.value=getDateString(this.value);" styleClass="inputBoxDate" property="phase_end_date[${phaseIdx}]" />
                <html:text onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" styleClass="inputBoxTime" property="phase_end_time[${phaseIdx}]" />
                <html:select styleClass="inputBox" property="phase_end_AMPM[${phaseIdx}]">
                    <html:option key="editProject.Phases.AM" value="am" />
                    <html:option key="editProject.Phases.PM" value="pm" />
                </html:select>
                <bean:message key="global.Timezone.EST" />
                <div name="end_date_validation_msg" class="error" style="display:none;" /></td>
            <td class="value">
                <html:radio property="phase_use_duration[${phaseIdx}]" value="true" />
                <html:text styleClass="inputBoxDuration" property="phase_duration[${phaseIdx}]" />
                <div name="duration_validation_msg" class="error" style="display:none;" /></td>
            <td class="value">
                <html:img srcKey="editProject.Phases.DeletePhase.img"
                    altKey="editProject.Phases.DeletePhase.alt"
                    onclick="deletePhase(this.parentNode.parentNode);" style="cursor:pointer;" /></td>
        </tr>

        <%-- PHASE CRITERIA ROWS GO HERE --%>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_required_registrations'][phaseIdx])}">
            <tr class="highlighted" ${(phaseIdx == 0) ? 'id="required_registrations_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.RequiredRegistrations.beforeInput" />
                    <%-- TODO: Set default value in Action --%>
                    <html:text style="width:30px;text-align:right;" styleClass="inputBox"
                        size="30" property="phase_required_registrations[${phaseIdx}]" />
                    &#160;<bean:message key="editProject.Phases.Criteria.RequiredRegistrations.afterInput" />
                    <br /><bean:message key="editProject.Phases.Criteria.RequiredRegistrations.note" /></td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_required_submissions'][phaseIdx])}">
            <tr class="highlighted" ${(phaseIdx == 0) ? 'id="required_submissions_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.RequiredSubmissions.beforeInput" />
                    <html:text styleClass="inputBox" property="phase_required_submissions[${phaseIdx}]" style="width:30px;text-align:right;" />
                    <bean:message key="editProject.Phases.Criteria.RequiredSubmissions.afterInput" /><br />
                    <html:checkbox styleId="manualScreeningCheckbox${phaseIdx}" property="phase_manual_screening[${phaseIdx}]" /><label
                        for="manualScreeningCheckbox${phaseIdx}"><bean:message key="editProject.Phases.Criteria.RequiredSubmissions.ManualScreening" /></label></td>
            </tr>
        </c:if>

        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_scorecard'][phaseIdx] and projectForm.map['phase_name'][phaseIdx] eq 'Screening')}">
            <tr class="highlighted" ${(phaseIdx == 0) ? 'id="screening_scorecard_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4"><bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" >
                        <c:forEach items="${screeningScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1) || (not newProject && project.projectCategory.id == scorecard.category)}">
                                <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select></td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_scorecard'][phaseIdx] and projectForm.map['phase_name'][phaseIdx] eq 'Review')}">
            <tr class="highlighted" ${(phaseIdx == 0) ? 'id="review_scorecard_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <bean:message key="editProject.Phases.Criteria.ReviewNumber.beforeInput" />
                    <html:text style="width:30px;text-align:right;" styleClass="inputBox"
                        size="30" property="phase_required_reviewers[${phaseIdx}]" />
                    &#160;<bean:message key="editProject.Phases.Criteria.ReviewNumber.afterInput" /><br />
                    <bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" >
                        <c:forEach items="${reviewScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1) or (not newProject && project.projectCategory.id == scorecard.category)}">
                            <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select></td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_scorecard'][phaseIdx] and projectForm.map['phase_name'][phaseIdx] eq 'Approval')}">
            <tr class="highlighted" ${(phaseIdx == 0) ? 'id="approval_scorecard_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4"><bean:message key="editProject.Phases.Criteria.Scorecard" />
                    <html:select style="width:350px;" styleClass="inputBox" property="phase_scorecard[${phaseIdx}]" >
                        <c:forEach items="${approvalScorecards}" var="scorecard">
                            <c:if test="${(newProject && scorecard.category == 1) || (not newProject && project.projectCategory.id == scorecard.category)}">
                            <html:option value="${scorecard.id}">${scorecard.name} ${scorecard.version}</html:option>
                            </c:if>
                        </c:forEach>
                    </html:select></td>
            </tr>
        </c:if>
        <c:if test="${(phaseIdx eq 0) or (not empty projectForm.map['phase_view_appeal_responses'][phaseIdx])}">
            <tr class="highlighted" ${(phaseIdx == 0) ? 'id="view_appeal_responses_row_template" style="display:none;"' : ''}>
                <td class="value" colspan="${(newProject) ? 1 : 2}"><!-- @ --></td>
                <td class="value" colspan="4">
                    <html:radio styleId="appealResponsesOffCheckbox${phaseIdx}" value="true" property="phase_view_appeal_responses[${phaseIdx}]" /><label
                        for="appealResponsesOffCheckbox${phaseIdx}"><bean:message key="editProject.Phases.Criteria.ViewAppealResponses.Immediately" /></label><br />
                    <html:radio styleId="appealResponsesOnCheckbox${phaseIdx}" value="false" property="phase_view_appeal_responses[${phaseIdx}]" /><label
                        for="appealResponsesOnCheckbox${phaseIdx}"><bean:message key="editProject.Phases.Criteria.ViewAppealResponses.AfterEnd" /></label></td>
            </tr>
        </c:if>
    </c:forEach>
</table><br />

<table class="scorecard" id="addphase_tbl" width="100%">
    <tr class="highlighted">
        <td class="valueB"><bean:message key="editProject.Phases.AddNewPhase" /></td>
        <td class="valueB"><bean:message key="editProject.Phases.PhaseStart" /></td>
        <td class="valueB"><bean:message key="editProject.Phases.PhaseEnd" /></td>
        <td class="valueB"><bean:message key="editProject.Phases.Duration" /></td>
        <td class="valueB"><!-- @ --></td>
    </tr>

    <%-- ADD PHASE FORM BEGINS --%>
    <tr class="light">
        <td class="value" nowrap="nowrap">
            <bean:message key="editProject.Phases.NewPhase" /><br />
            <html:select styleClass="inputBox" property="addphase_type" style="width:120px;" onchange="onPhaseTypeChange(this)">
                <html:option key="editProject.Phases.Select" value="" />
                <c:forEach items="${requestScope.phaseTypes}" var="phaseType">
                    <html:option key="ProjectPhase.${fn:replace(phaseType.name, ' ', '')}" value="${phaseType.id}" />
                </c:forEach>
            </html:select><br />
            <bean:message key="editProject.Phases.Placement" /><br />
            <html:select styleClass="inputBox" property="addphase_when">
                <html:option key="editProject.Phases.Before" value="before" />
                <html:option key="editProject.Phases.After" value="after" />
            </html:select><br />
            <html:select styleClass="inputBox" property="addphase_where" style="width:120px;">
                <html:option key="editProject.Phases.SelectPhase" value="" />
                <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                    <html:option value="${projectForm.map['phase_js_id'][i]}" key="ProjectPhase.${fn:replace(projectForm.map['phase_name'][i], ' ', '')}" />
                </c:forEach>
            </html:select></td>
        <td class="value" nowrap="nowrap">
            <html:radio styleId="phaseStartAtRadio" property="addphase_start_by_phase" value="false" /><label
                for="phaseStartAtRadio"><bean:message key="editProject.Phases.At" /></label>
            <html:text onblur="JavaScript:this.value=getDateString(this.value);" styleClass="inputBoxDate" property="addphase_start_date" />
            <html:text onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" styleClass="inputBoxTime" property="addphase_start_time" />
            <html:select styleClass="inputBox" property="addphase_start_AMPM">
                <html:option key="editProject.Phases.AM" value="am" />
                <html:option key="editProject.Phases.PM" value="pm" />
            </html:select>
            <bean:message key="global.Timezone.EST" /><br />
            <html:radio styleId="phaseStartWhenRadio" property="addphase_start_by_phase" value="true" /><label
                for="phaseStartWhenRadio"><bean:message key="editProject.Phases.When" /></label>
            <html:select styleClass="inputBox" property="addphase_start_phase" style="width:120px;">
                <html:option key="editProject.Phases.SelectPhase" value="" />
                <c:forEach var="i" begin="1" end="${fn:length(projectForm.map['phase_id']) - 1}">
                    <c:choose>
                        <c:when test="${projectForm.map['phase_number'][i] > 1}">
                            <html:option value="${projectForm.map['phase_js_id'][i]}">
                                <bean:message key="ProjectPhase.${fn:replace(projectForm.map['phase_name'][i], ' ', '')}" />
                                ${projectForm.map["phase_number"][i]}</html:option></c:when>
                        <c:otherwise><html:option value="${projectForm.map['phase_js_id'][i]}"
                            key="ProjectPhase.${fn:replace(projectForm.map['phase_name'][i], ' ', '')}" /></c:otherwise>
                    </c:choose>
                </c:forEach>
            </html:select>
            <div style="margin-left:21px;">
                <html:select styleClass="inputBox" property="addphase_start_when" value="ends">
                    <html:option key="editProject.Phases.Starts" value="starts" />
                    <html:option key="editProject.Phases.Ends" value="ends" />
                </html:select>
                <html:select styleClass="inputBox" property="addphase_start_plusminus" value="plus">
                    <html:option value="plus">+</html:option>
                    <html:option value="minus">-</html:option>
                </html:select>
                <html:text styleClass="inputBox" property="addphase_start_amount" value="0" style="width:30px;" />
                <html:select styleClass="inputBox" property="addphase_start_dayshrs" value="hrs">
                    <html:option key="editProject.Phases.Days" value="days" />
                    <html:option key="editProject.Phases.Hrs" value="hrs" />
                </html:select>
            </div>
        </td>
        <td class="value" nowrap="nowrap">
            <html:radio styleId="phaseEndAtRadio" property="addphase_use_duration" value="false" /><label
                for="phaseEndAtRadio"><bean:message key="editProject.Phases.At" /></label>
            <html:text onblur="JavaScript:this.value=getDateString(this.value);" styleClass="inputBoxDate" property="addphase_end_date" />
            <html:text onblur="JavaScript:this.value=getTimeString(this.value, this.parentNode);" styleClass="inputBoxTime" property="addphase_end_time" />
            <html:select styleClass="inputBox" property="addphase_end_AMPM">
                <html:option key="editProject.Phases.AM" value="am" />
                <html:option key="editProject.Phases.PM" value="pm" />
            </html:select>
            <bean:message key="global.Timezone.EST" /></td>
        <td class="value" nowrap="nowrap">
            <html:radio property="addphase_use_duration" value="true" />
            <html:text styleClass="inputBoxDuration" property="addphase_duration" /></td>
        <td class="value">
            <html:img srcKey="editProject.Phases.AddPhase.img" altKey="editProject.Phases.AddPhase.alt" onclick="addNewPhase();" style="cursor:pointer;" /></td>
    </tr><%-- ADD PHASE FORM ENDS --%>

    <tr>
        <td class="lastRowTD" colspan="5"><!-- @ --></td>
    </tr>
</table><br />
