<%--
  - Author: flexme
  - Version: 1.0 (Online Review - Project Payments Integration Part 1 v1.0)
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the form input elements group for editing the project prizes..
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>

<div>
    <ul id="tablist">
        <li class="current"><a id="contest-prizes-link" onclick="return showPrizeTab('contest-prizes-table', this);" href="javascript:void(0)"><bean:message key="editProject.ProjectDetails.ContestPrizes" /></a></li>
        <li><a id="checkpoint-prizes-link" onclick="return showPrizeTab('checkpoint-prizes-table', this);" href="javascript:void(0)"><bean:message key="editProject.ProjectDetails.CheckpointPrizes" /></a></li>
    </ul>
    <div style="clear:both;"></div>
</div>

<c:set var="typeId" value="${projectForm.map['project_type']}"/>
<c:set var="contestPrizesLength" value="${fn:length(projectForm.map['contest_prizes_amount'])}"/>
<table id="contest-prizes-table" class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
    <tr>
        <td class="title" colspan="4"><bean:message key="editProject.ProjectDetails.ContestPrizes" /></td>
    </tr>
    <tr>
        <td class="header"><bean:message key="editProject.ProjectDetails.Prize.Place" /></td>
        <td class="header"><bean:message key="editProject.ProjectDetails.Prize.Amount" /></td>
        <td class="header" colspan="2"><bean:message key="editProject.ProjectDetails.Prize.NoOfPrizes" /></td>
    </tr>
    <tr style="display:none;">
        <td class="value"></td>
        <td class="value">
            <label>${"$"}</label>
            <input class="inputBoxDuration" type="text" name="contest_prizes_amount_dump[]"/>
            <span name="prize_amount_validation_msg" style="display: none" class="error"></span>
        </td>
        <td class="value">
            <select class="inputBox" <c:if test="${not newProject and typeId ne 3}">disabled="disabled" </c:if> name="contest_prizes_num_dump[]">
                <c:forEach begin="1" end="10" var="index">
                    <option value="${index}">${index}</option>
                </c:forEach>
            </select>
        </td>
        <td class="value">
            <a href="#" onclick="removePrize(this);return false;"><html:img srcKey="editProject.Prizes.DeletePrize.img" altKey="editProject.Prizes.DeletePrize.alt"/></a>
        </td>
    </tr>
    <c:if test="${contestPrizesLength > 0}">
        <c:forEach begin="0" end="${contestPrizesLength - 1}" var="prizeIdx" varStatus="vs">
            <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
                <td class="value">${prizeIdx + 1}</td>
                <td class="value">
                    <label>${"$"}</label>
                    <html:text styleClass="inputBoxDuration" property="contest_prizes_amount[${prizeIdx}]" disabled="${not canEditContestPrize}"/>
                    <span name="prize_amount_validation_msg" style="display: none" class="error"></span>
                    <div class="error"><html:errors property="contest_prizes_amount[${prizeIdx}]" prefix="" suffix="" /></div>
                </td>
                <td class="value">
                    <html:select styleClass="inputBox" property="contest_prizes_num[${prizeIdx}]" disabled="${not canEditContestPrize or typeId ne 3}">
                        <c:forEach begin="1" end="10" var="index">
                            <html:option key="${index}" value="${index}" />
                        </c:forEach>
                    </html:select>
                    <div class="error"><html:errors property="contest_prizes_num[${prizeIdx}]" prefix="" suffix="" /></div>
                </td>
                <td class="value">
                    <c:if test="${prizeIdx eq contestPrizesLength - 1 and canEditContestPrize}">
                        <a href="#" onclick="removePrize(this);return false;"><html:img srcKey="editProject.Prizes.DeletePrize.img" altKey="editProject.Prizes.DeletePrize.alt" /></a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${canEditContestPrize}">
        <tr <c:if test="${contestPrizesLength % 2 eq 0}">class="light"</c:if><c:if test="${contestPrizesLength % 2 eq 1}">class="dark"</c:if> >
            <td class="value" colspan="4">
            <a href="#" onclick="addPrize(this, 'contest_prizes');return false;"><html:img srcKey="editProject.Prizes.AddPrize.img" altKey="editProject.Prizes.AddPrize.alt" /></a>
        </td>
        </tr>
    </c:if>
    <tr>
        <td class="lastRowTD" colspan="4"><!-- @ --></td>
    </tr>
</table>
<c:set var="checkpointPrizesLength" value="${fn:length(projectForm.map['checkpoint_prizes_amount'])}" />
<table id="checkpoint-prizes-table" class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;display:none;">
    <tr>
        <td class="title" colspan="4"><bean:message key="editProject.ProjectDetails.CheckpointPrizes" /></td>
    </tr>
    <tr>
        <td class="header"><bean:message key="editProject.ProjectDetails.Prize.Place" /></td>
        <td class="header"><bean:message key="editProject.ProjectDetails.Prize.Amount" /></td>
        <td class="header" colspan="2"><bean:message key="editProject.ProjectDetails.Prize.NoOfPrizes" /></td>
    </tr>
    <tr style="display:none;">
        <td class="value"></td>
        <td class="value">
            <label>${"$"}</label>
            <input class="inputBoxDuration" type="text" name="checkpoint_prizes_amount_dump[]"/>
            <span name="prize_amount_validation_msg" style="display: none" class="error"></span>
        </td>
        <td class="value">
            <select class="inputBox" name="checkpoint_prizes_num_dump[]">
                <c:forEach begin="1" end="10" var="index">
                    <option value="${index}">${index}</option>
                </c:forEach>
            </select>
        </td>
        <td class="value">
            <a href="#" onclick="removePrize(this);return false;"><html:img srcKey="editProject.Prizes.DeletePrize.img" altKey="editProject.Prizes.DeletePrize.alt"/></a>
        </td>
    </tr>
    <c:if test="${checkpointPrizesLength > 0}">
        <c:forEach begin="0" end="${checkpointPrizesLength - 1}" var="prizeIdx" varStatus="vs">
            <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
                <td class="value">${prizeIdx + 1}</td>
                <td class="value">
                    <label>${"$"}</label>
                    <html:text styleClass="inputBoxDuration" property="checkpoint_prizes_amount[${prizeIdx}]" disabled="${not canEditCheckpointPrize}"/>
                    <div class="error"><html:errors property="checkpoint_prizes_amount[${prizeIdx}]" prefix="" suffix="" /></div>
                    <span name="prize_amount_validation_msg" style="display: none" class="error"></span>
                </td>
                <td class="value">
                    <html:select styleClass="inputBox" property="checkpoint_prizes_num[${prizeIdx}]" disabled="${not canEditCheckpointPrize}">
                        <c:forEach begin="1" end="10" var="index">
                            <html:option key="${index}" value="${index}" />
                        </c:forEach>
                    </html:select>
                    <div class="error"><html:errors property="checkpoint_prizes_num[${prizeIdx}]" prefix="" suffix="" /></div>
                </td>
                <td class="value">
                    <c:if test="${prizeIdx eq checkpointPrizesLength - 1 and canEditCheckpointPrize}">
                        <a href="#" onclick="removePrize(this);return false;"><html:img srcKey="editProject.Prizes.DeletePrize.img" altKey="editProject.Prizes.DeletePrize.alt"/></a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${canEditCheckpointPrize}">
        <tr <c:if test="${checkpointPrizesLength % 2 eq 0}">class="light"</c:if><c:if test="${checkpointPrizesLength % 2 eq 1}">class="dark"</c:if> >
            <td class="value" colspan="4">
            <a href="#" onclick="addPrize(this, 'checkpoint_prizes');return false;"><html:img srcKey="editProject.Prizes.AddPrize.img" altKey="editProject.Prizes.AddPrize.alt" /></a>
        </td>
        </tr>
    </c:if>
    <tr>
        <td class="lastRowTD" colspan="4"><!-- @ --></td>
    </tr>
</table>
<br/>