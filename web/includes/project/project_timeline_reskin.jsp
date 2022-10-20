<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<div class="projectDetails">
    <div class="projectDetails__sectionHeader">
        <div class="projectDetails__title">
            <or:text key="viewProjectDetails.box.Timeline" />
        </div>
        <div class="projectDetails__notificationCheckbox">
            <c:if test="${isAllowedToSetTL}">
                    <input id="notifCheckbox" type="checkbox" onclick="setTimelineNotification(${project.id}, this)"
                        ${(sendTLNotifications == 'On') ? 'value="On" checked="checked"' : 'value="Off"' } />
                    <span class="checkbox-label"></span>
                    <label for="notifCheckbox"><or:text key="viewProjectDetails.ReceiveTLNotifications" /></label>
            </c:if>
        </div>
        <div class="projectDetails__accordion">
        </div>
    </div>

    <div class="projectDetails__sectionBody">
        <table class="timelineTable" width="100%" cellpadding="0" cellspacing="0"
               style="table-layout: fixed;">
            <tr>
                <td valign="top" style="overflow:hidden;">
                    <table cellpadding="0" cellspacing="0" border="0" style="width:100%;table-layout:auto;border:none;">
                        <thead class="timelineTable__header timelineTable__header--left">
                            <tr>
                                <th><or:text key="viewProjectDetails.Timeline.Phase" /></th>
                                <th><or:text key="viewProjectDetails.Timeline.Status" /></th>
                                <th><or:text key="viewProjectDetails.Timeline.Start" /></th>
                                <th><or:text key="viewProjectDetails.Timeline.End" /></th>
                            </tr>
                        </thead>
                        <tbody class="timelineTable__body timelineTable__body--left">
                        <c:forEach items="${phases}" var="phase" varStatus="phaseStatus">
                            <tr>
                                <c:if test="${phaseGroupIndexes[phaseStatus.index] == -1}">
                                    <td nowrap="nowrap">
                                        <or:text key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></td>
                                </c:if>
                                <c:if test="${phaseGroupIndexes[phaseStatus.index] != -1}">
                                    <td nowrap="nowrap">
                                        <a onClick='return activateTab("sc${phaseGroupIndexes[phaseStatus.index] + 1}", this)'
                                            href="javascript:void(0)"><or:text
                                                key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></a></td>
                                </c:if>
                                <c:choose>
                                    <c:when test="${phaseStatuseCodes[phaseStatus.index] == 0}">
                                        <td nowrap="nowrap"><or:text key="ProjectPhaseStatus.Scheduled" /></td>
                                    </c:when>
                                    <c:when test="${phaseStatuseCodes[phaseStatus.index] == 1}">
                                        <td nowrap="nowrap"><or:text key="ProjectPhaseStatus.Closed" /></td>
                                    </c:when>
                                    <c:when test="${phaseStatuseCodes[phaseStatus.index] == 2}">
                                        <td nowrap="nowrap"><or:text key="ProjectPhaseStatus.Open" /></td>
                                    </c:when>
                                    <c:when test="${phaseStatuseCodes[phaseStatus.index] == 3}">
                                        <td nowrap="nowrap" style="color:cccc00;"><or:text key="ProjectPhaseStatus.Closing" /></td>
                                    </c:when>
                                    <c:when test="${phaseStatuseCodes[phaseStatus.index] == 4}">
                                        <td nowrap="nowrap" style="color:#cc0000;"><or:text key="ProjectPhaseStatus.Late" /></td>
                                    </c:when>
                                    <c:when test="${phaseStatuseCodes[phaseStatus.index] == 5}">
                                        <td nowrap="nowrap">
                                            <or:text key="ProjectPhaseStatus.CantOpen" />
                                            <img src="/i/or/icon_question.gif" title="${orfn:htmlEncode(cannotOpenHints[phaseStatus.index])}" />
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <%-- This should never happen --%>
                                        <td nowrap="nowrap"><!-- @ --></td>
                                    </c:otherwise>
                                </c:choose>
                                
                                <td nowrap="nowrap" >${orfn:displayDate(pageContext.request, originalStart[phaseStatus.index])}</td>
                                <td nowrap="nowrap" >${orfn:displayDate(pageContext.request, originalEnd[phaseStatus.index])}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </td>
                <td valign="top">
                    <div style="width: 100%; overflow-x: scroll; overflow-y: visible; position:relative; height:100%">
                        <c:if test="${ganttCurrentTime ne null}">
                            <div class="currentTime" style="left: ${orfn:getGanttLen(ganttCurrentTime)}px;"></div>
                        </c:if>
                        <table cellpadding="0" cellspacing="0" style="border:none;" width="100%">
                            <thead class="timelineTable__header timelineTable__header--right">
                                <tr>
                                    <th>&#160;</th>
                                </tr>
                            </thead>
                            <tbody class="timelineTable__body timelineTable__body--right">
                            <c:forEach items="${phases}" var="phase" varStatus="phaseStatus">
                                <td class="ganttRow">
                                <c:set var="ganttOffset" value="${orfn:getGanttLen(ganttOffsets[phaseStatus.index])}" />
                                <dl style="margin-left:${ganttOffset}px; width: ${orfn:getGanttLen(ganttLengths[phaseStatus.index]) + 80}px;">
                                <dt style="width:${orfn:getGanttLen(ganttLengths[phaseStatus.index])}px;">&#160;</dt>
                                <dd>${orfn:getGanttHours(pageContext, ganttLengths[phaseStatus.index])}</dd>
                                </dl>
                                </td>
                              </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
