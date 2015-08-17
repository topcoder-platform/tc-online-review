<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment renders the details on project phasee along with Gantt chart.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
    <table class="stat" width="100%" cellpadding="0" cellspacing="0"
           style="table-layout: fixed; border-bottom: 1px solid #999999;">
        <tr>
            <td class="title" width="50%"><or:text key="viewProjectDetails.box.Timeline" /></td>
            <c:if test="${isAllowedToSetTL}">
                <td class="title" style="text-align:right;" width="50%">
                    <input id="notifCheckbox" type="checkbox" onclick="setTimelineNotification(${project.id}, this)"
                        ${(sendTLNotifications == 'On') ? 'value="On" checked="checked"' : 'value="Off"' } />
                    <label for="notifCheckbox"><or:text key="viewProjectDetails.ReceiveTLNotifications" /></label>
                </td>
            </c:if>
            <c:if test="${not isAllowedToSetTL}">
                <td class="title" width="50%">&#160;</td>
            </c:if>
        </tr>
        <tr>
            <td valign="top" style="overflow:visible;border-bottom: 1px solid #999999;">
                <table class="stat" cellpadding="0" cellspacing="0" border="0" style="width:100%;table-layout:auto;border:none;">
                    <tr>
                        <td class="header"><or:text key="viewProjectDetails.Timeline.Phase" /></td>
                        <td class="headerC"><or:text key="viewProjectDetails.Timeline.Status" /></td>
                        <td class="headerC"><or:text key="viewProjectDetails.Timeline.Start" /></td>
                        <td class="headerC"><or:text key="viewProjectDetails.Timeline.End" /></td>
                    </tr>
                    <c:forEach items="${phases}" var="phase" varStatus="phaseStatus">
                        <tr class='${(phaseStatus.index % 2 == 0) ? "light" : "dark"}'>
                            <c:if test="${phaseGroupIndexes[phaseStatus.index] == -1}">
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></b></td>
                            </c:if>
                            <c:if test="${phaseGroupIndexes[phaseStatus.index] != -1}">
                                <td class="value" nowrap="nowrap">
                                    <a onClick='return activateTab("sc${phaseGroupIndexes[phaseStatus.index] + 1}", this)'
                                        href="javascript:void(0)"><b><or:text
                                            key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></b></a></td>
                            </c:if>
                            <c:choose>
                                <c:when test="${phaseStatuseCodes[phaseStatus.index] == 0}">
                                    <td class="valueC" nowrap="nowrap"><or:text key="ProjectPhaseStatus.Scheduled" /></td>
                                </c:when>
                                <c:when test="${phaseStatuseCodes[phaseStatus.index] == 1}">
                                    <td class="valueC" nowrap="nowrap"><or:text key="ProjectPhaseStatus.Closed" /></td>
                                </c:when>
                                <c:when test="${phaseStatuseCodes[phaseStatus.index] == 2}">
                                    <td class="valueC" nowrap="nowrap"><or:text key="ProjectPhaseStatus.Open" /></td>
                                </c:when>
                                <c:when test="${phaseStatuseCodes[phaseStatus.index] == 3}">
                                    <td class="valueC" nowrap="nowrap" style="color:cccc00;"><or:text key="ProjectPhaseStatus.Closing" /></td>
                                </c:when>
                                <c:when test="${phaseStatuseCodes[phaseStatus.index] == 4}">
                                    <td class="valueC" nowrap="nowrap" style="color:#cc0000;"><or:text key="ProjectPhaseStatus.Late" /></td>
                                </c:when>
                                <c:when test="${phaseStatuseCodes[phaseStatus.index] == 5}">
                                    <td class="valueC" nowrap="nowrap">
                                        <or:text key="ProjectPhaseStatus.CantOpen" />
                                        <img src="/i/or/icon_question.gif" title="${orfn:htmlEncode(cannotOpenHints[phaseStatus.index])}" />
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <%-- This should never happen --%>
                                    <td class="valueC" nowrap="nowrap"><!-- @ --></td>
                                </c:otherwise>
                            </c:choose>
                            
                            <td class="valueC" nowrap="nowrap" >${orfn:displayDate(pageContext.request, originalStart[phaseStatus.index])}</td>
                            <td class="valueC" nowrap="nowrap" >${orfn:displayDate(pageContext.request, originalEnd[phaseStatus.index])}</td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
            <td valign="top" bgcolor="#F7F7F7" style="border-bottom: 1px solid #999999;">
                <div style="width: 100%; overflow-x: scroll; overflow-y: visible; position:relative; height:100%">
                    <c:if test="${ganttCurrentTime ne null}">
                        <div class="currentTime" style="left: ${orfn:getGanttLen(ganttCurrentTime)}px;"></div>
                    </c:if>
                    <table class="stat" cellpadding="0" cellspacing="0" style="border:none;" width="100%">
                        <tr class="header">
                            <td class="header">&#160;</td>
                        </tr>
                        <c:forEach items="${phases}" var="phase" varStatus="phaseStatus">
                          <tr class='${(phaseStatus.index % 2) == 0 ? "light" : "dark"}'>
                            <td class="ganttRow">
                            <c:set var="ganttOffset" value="${orfn:getGanttLen(ganttOffsets[phaseStatus.index])}" />
                            <dl style="margin-left:${ganttOffset}px; width: ${orfn:getGanttLen(ganttLengths[phaseStatus.index]) + 80}px;">
                            <dt style="width:${orfn:getGanttLen(ganttLengths[phaseStatus.index])}px;"><or:text key='ProjectPhase.${fn:replace(phase.phaseType.name, " ", "")}' /></dt>
                            <dd>${orfn:getGanttHours(pageContext, ganttLengths[phaseStatus.index])}</dd>
                            </dl>
                            </td>
                          </tr>
                        </c:forEach>
                    </table>
                </div>
            </td>
        </tr>
        <tr>
            <td class="lastRowTD" colspan="2"><!-- @ --></td>
        </tr>
    </table><br />
